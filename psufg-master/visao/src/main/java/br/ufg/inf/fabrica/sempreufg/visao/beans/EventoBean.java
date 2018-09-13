/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.AprovacaoDivulgacaoController;
import br.ufg.inf.fabrica.sempreufg.controller.AreaConhecimentoController;
import br.ufg.inf.fabrica.sempreufg.controller.EventoController;
import br.ufg.inf.fabrica.sempreufg.controller.InstanciaAdministrativaUfgController;
import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.InstanciaAdministrativaUFG;
import br.ufg.inf.fabrica.sempreufg.enums.EscopoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.StatusAprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoEvento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author auf
 */
@Named(value = "eventoBean")
@SessionScoped
public class EventoBean implements Serializable {

    private Evento eventoEdicao;
    private List<Evento> eventos;
    private List<Evento> eventosPublicos;

    private DualListModel<AreaConhecimento> areasConhecimento;
    private DualListModel<InstanciaAdministrativaUFG> instanciasAdministrativas;

    private final EventoController controller;
    private final AreaConhecimentoController areasController = new AreaConhecimentoController();
    private final InstanciaAdministrativaUfgController instanciasController = new InstanciaAdministrativaUfgController();
    private final AprovacaoDivulgacaoController aprovacaoController = new AprovacaoDivulgacaoController();

    @PostConstruct
    public void init() {
        this.construirListasDual();
    }

    /**
     * Creates a new instance of eventoEdicao
     */
    public EventoBean() {
        controller = new EventoController();
        eventoEdicao = new Evento();
        eventoEdicao.setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao.SEM_APROVACAO);
        this.eventos = controller.buscarTodos();
    }

    public Evento getEventoEdicao() {
        RequestContext.getCurrentInstance().execute("clicarBotao('idbotaoOculto')");
        return eventoEdicao;
    }

    public void setEventoEdicao(Evento evento) {
        this.eventoEdicao = evento;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public List<Evento> getEventosPublicos() {
        if (eventosPublicos == null) {
            eventosPublicos = new ArrayList<>();
            eventos.stream().filter((evento) -> (evento.getEscopoDivulgacao().equals(EscopoDivulgacao.COMUNIDADE)
                    && evento.getStatusAprovacaoDivulgacao().equals(StatusAprovacaoDivulgacao.CONCLUIDA))
                    && !evento.getTipoEvento().equals(TipoEvento.OPORTUNIDADE_DE_EMPREGO)).forEachOrdered((evento) -> {
                eventosPublicos.add(evento);
            });
        }
        return eventosPublicos;
    }

    public void setEventosPublicos(List<Evento> eventosPublicos) {
        this.eventosPublicos = eventosPublicos;
    }

    public TipoEvento[] getTiposEventos() {
        return TipoEvento.values();
    }

    public FormaDivulgacao[] getFormasDivulgacao() {
        return FormaDivulgacao.values();
    }

    public EscopoDivulgacao[] getEscoposDivulgacao() {
        return EscopoDivulgacao.values();
    }

    public DualListModel<AreaConhecimento> getAreasConhecimento() {
        return areasConhecimento;
    }

    public void setAreasConhecimento(DualListModel<AreaConhecimento> areasConhecimento) {
        this.areasConhecimento = areasConhecimento;
    }

    public DualListModel<InstanciaAdministrativaUFG> getInstanciasAdministrativas() {
        return instanciasAdministrativas;
    }

    public void setInstanciasAdministrativas(DualListModel<InstanciaAdministrativaUFG> instanciasAdministrativas) {
        this.instanciasAdministrativas = instanciasAdministrativas;
    }

    public String atualizarEvento() {
        Date agora = new Date();
        eventoEdicao.setAreaConhecimento(areasConhecimento.getTarget());
        eventoEdicao.setInstanciasAdministrativas(instanciasAdministrativas.getTarget());
        eventoEdicao.setTimeStampDaSolicitacao(agora);
        List<String> inconsistencias = controller.atualizar(eventoEdicao);
        if (inconsistencias.isEmpty()) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualização realizada com sucesso", "");
            contexto.addMessage("/evento/editarEvento", msg);
            contexto.getExternalContext().getFlash().setKeepMessages(true);
            //atualizar lista de eventos conforme o banco
            this.setEventos(controller.buscarTodos());
            return "/evento/listagemEventos?faces-redirect=true";
        }
        for (String inconsistencia : inconsistencias) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(inconsistencia));
        }
        return "/evento/editarEvento?faces-redirect=true";
    }

    public String excluirEvento() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg;
        if (this.controller.excluirEvento(eventoEdicao)) {
            this.getEventos().remove(eventoEdicao);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento excluído com sucesso", "");
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Evento não podê ser excluído!", "");
        }
        contexto.addMessage("/evento/listagemEventos", msg);
        contexto.getExternalContext().getFlash().setKeepMessages(true);
        return "/evento/listagemEventos?faces-redirect=true";
    }

    public String editarEvento(Evento evento) {
        this.construirListasDual();
        if (evento == null) {
            this.eventoEdicao = new Evento();
            eventoEdicao.setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao.SEM_APROVACAO);
        } else {
            this.eventoEdicao = evento;
            areasConhecimento.setTarget(evento.getAreasDeConhecimento());
            if (!areasConhecimento.getTarget().isEmpty() || areasConhecimento.getTarget().size() > 0) {
                areasConhecimento.getSource().removeAll(areasConhecimento.getTarget());
            }
            instanciasAdministrativas.setTarget(evento.getInstanciasAdministrativas());
            if (!instanciasAdministrativas.getTarget().isEmpty() || instanciasAdministrativas.getTarget().size() > 0) {
                instanciasAdministrativas.getSource().removeAll(instanciasAdministrativas.getTarget());
            }
        }
        return "/evento/editarEvento?faces-redirect=true";
    }

    public String cancelarEdicaoEvento() {
        this.eventoEdicao = null;
        return "/evento/listagemEventos?faces-redirect=true";
    }

    public String statusAprovacaoEventoEdicao() {
        return this.eventoEdicao.getStatusAprovacaoDivulgacao().getValue();
    }

    private void construirListasDual() {
        List<AreaConhecimento> areasSource = areasController.buscarTodos();
        List<AreaConhecimento> areasTarget = new ArrayList<>();
        areasConhecimento = new DualListModel<>(areasSource, areasTarget);

        List<InstanciaAdministrativaUFG> instanciasSource = instanciasController.buscarTodos();
        List<InstanciaAdministrativaUFG> instanciasTarget = new ArrayList<>();
        instanciasAdministrativas = new DualListModel<>(instanciasSource, instanciasTarget);
    }

    public String visualizarNoticia() {
        this.eventoEdicao = null;
        String param = null;
        long id = 0;

        try {
            param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("numero");
        } catch (Exception ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.WARNING, "Paramêtro não pode ser recuperado", ex);
        }

        if (param != null) {
            try {
                id = Long.valueOf(param);
            } catch (NumberFormatException ex) {
                Logger.getLogger(Evento.class.getName()).log(Level.WARNING, "Paramêtro passado não é um número válido.", ex);
            }
        }
        this.eventoEdicao = controller.buscarPorID(id);
        return "/evento/eventoPublicado?numero="+String.valueOf(id)+"faces-redirect=true";
    }
    
}
