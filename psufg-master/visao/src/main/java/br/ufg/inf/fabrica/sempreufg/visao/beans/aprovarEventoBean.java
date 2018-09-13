/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.AprovacaoDivulgacaoController;
import br.ufg.inf.fabrica.sempreufg.controller.EventoController;
import br.ufg.inf.fabrica.sempreufg.controller.GerenciadorEmailController;
import br.ufg.inf.fabrica.sempreufg.dao.UsuarioDao;
import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.InstanciaAdministrativaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.StatusAprovacaoDivulgacao;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author auf
 */
@ViewScoped
@Named(value = "aprovarEventoBean")
public class aprovarEventoBean implements Serializable {

    private List<Evento> eventos;
    private Usuario usuarioLogado;
    private Evento eventoEmAprovacao;
    private List<InstanciaAdministrativaUFG> instanciasUsuarioLogado;
    private List<InstanciaAdministrativaUFG> instanciasSelecionadas;
    private boolean rejeicao;
    private final GerenciadorEmailController gerenciadorEmailController;
    private final AprovacaoDivulgacaoController aprovacaoController;
    private final EventoController eventoController;
    private String parecerSobreDivulgacao;

    public aprovarEventoBean() {
        UsuarioDao u = new UsuarioDao();
        this.usuarioLogado = u.buscar(new Long(1));
        this.setEventos(buscarEventosUsuario());
        this.gerenciadorEmailController = new GerenciadorEmailController();
        this.aprovacaoController = new AprovacaoDivulgacaoController();
        this.eventoController = new EventoController();
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public boolean isRejeicao() {
        return rejeicao;
    }

    public void setRejeicao(boolean rejeicao) {
        this.rejeicao = rejeicao;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public Evento getEventoEmAprovacao() {
        return eventoEmAprovacao;
    }

    public void setEventoEmAprovacao(Evento eventoEmAprovacao) {
        this.eventoEmAprovacao = eventoEmAprovacao;
    }

    public String getParecerSobreDivulgacao() {
        return parecerSobreDivulgacao;
    }

    public void setParecerSobreDivulgacao(String parecerSobreDivulgacao) {
        this.parecerSobreDivulgacao = parecerSobreDivulgacao;
    }

    public List<InstanciaAdministrativaUFG> getInstanciasUsuarioLogado() {
        this.instanciasUsuarioLogado = new ArrayList<>();
        for (InstanciaAdministrativaUFG ins : this.eventoEmAprovacao.getInstanciasAdministrativas()) {
            if (this.usuarioLogado.getInstanciasAdministrativas().contains(ins)) {
                this.instanciasUsuarioLogado.add(ins);
            }
        }
        return instanciasUsuarioLogado;
    }

    public void setInstanciasUsuarioLogado(List<InstanciaAdministrativaUFG> instanciasUsuarioLogado) {
        this.instanciasUsuarioLogado = instanciasUsuarioLogado;
    }

    public List<InstanciaAdministrativaUFG> getInstanciasSelecionadas() {
        return instanciasSelecionadas;
    }

    public void setInstanciasSelecionadas(List<InstanciaAdministrativaUFG> instanciasSelecionadas) {
        this.instanciasSelecionadas = instanciasSelecionadas;
    }

    private List<Evento> buscarEventosUsuario() {
        //Buscar todos os eventos do usuário logado que estão sem aprovação:
        // isso é Evento com escopo = egresso, e usuário ser responsável pelo curso
        // Eventos come escopo comunidade só o gestor do sistema aprova
        // Eventos sem escopo definido não há aprovação        
        EventoController ev = new EventoController();
        return ev.buscarEventosUsuarioResponsavelEsperandoAprovacao(this.getUsuarioLogado());

    }

    public String confirmarAprovacao() {
        //Criar uma aprovação para cada instanciaAdministrativaUFG 
        AprovacaoDivulgacao aprovacao;

        LocalDate agora = LocalDate.now();
        for (InstanciaAdministrativaUFG ins : instanciasUsuarioLogado) {
            aprovacao = new AprovacaoDivulgacao();
            aprovacao.setInstanciaAdministrativaUFG(ins);
            aprovacao.setEvento(eventoEmAprovacao);
            aprovacao.setDataDaAvaliacao(agora);
            aprovacao.setParecerSobreDivulgacao(parecerSobreDivulgacao);

            if (instanciasSelecionadas.contains(ins)) {
                aprovacao.setAprovada(true);
                // Enviar mensagem de email para interessados do evento, se FormaDivulgacao != Noticia
                if (eventoEmAprovacao.getFormaDivulgacao() != FormaDivulgacao.NOTICIA) {
                    this.gerenciadorEmailController.enviarMensagensDiariasPorAprovacao(aprovacao);
                }
                // Publica Noticia no portal PSUFG, se FormaDivulgacao != Mensagem
                if (eventoEmAprovacao.getFormaDivulgacao() != FormaDivulgacao.MENSAGEM) {
                    //publicarEventoPortal(evento)
                }
            } else {
                aprovacao.setAprovada(false);
            }
            // Salvar aprovação, remover evento aprovado da lista 'eventos'
            aprovacaoController.salvar(aprovacao);
        }

        //Se existe aprovação/rejeição de publicação para todas as instanciasAdministrativas do evento
        if (aprovacaoController.buscarNumAprovacoesEvento(eventoEmAprovacao) == eventoEmAprovacao.getInstanciasAdministrativas().size()) {
            eventoEmAprovacao.setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao.CONCLUIDA);
        } else {
            eventoEmAprovacao.setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao.PARCIAL);
        }
        eventoController.atualizar(eventoEmAprovacao);

        this.setEventos(buscarEventosUsuario());
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Publicação de evento definida com sucesso!", "");
        contexto.addMessage("aprovacaoEvento", msg);
        return "";
    }

    public String cancelarAprovacao() {
        return "";
    }

}
