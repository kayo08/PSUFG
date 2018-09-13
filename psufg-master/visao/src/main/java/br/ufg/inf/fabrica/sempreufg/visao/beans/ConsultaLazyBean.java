/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.ConsultaCustomizadaController;
import br.ufg.inf.fabrica.sempreufg.controller.ConsultaLazyController;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.Consulta;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeConsulta;
import br.ufg.inf.fabrica.sempreufg.visao.util.MensagensTela;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author auf
 */
@Named(value = "consultaLazyBean")
@SessionScoped
public class ConsultaLazyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsultaLazyController consultaLazy = new ConsultaLazyController();
    private ConsultaCustomizadaController consultaCustomizadaController = new ConsultaCustomizadaController();

    private FiltroConsultaLazy filtro = new FiltroConsultaLazy();
    private LazyDataModel<Class> model;
    private List<Consulta> consultasSalvas = new ArrayList<>();
    private Consulta consultaEdicao;
    private final MensagensTela mensagemDeTela = new MensagensTela();
    private boolean exibirFrmSalvarConsulta = false;
    private boolean exibirConsultasFavoritas = false;
    private boolean tornarConsultaPublica = false;

    @Inject
    EgressoBean egressoBean;

    public ConsultaLazyBean() {
        model = new LazyDataModel<Class>() {

            private static final long serialVersionUID = 1L;

            @Override
            public List load(int first, int pageSize,
                    String sortField, SortOrder sortOrder,
                    Map<String, Object> filters) {
                filtro.setPrimeiroRegistro(first);
                filtro.setQuantidadeRegistros(pageSize);
                filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
                filtro.setPropriedadeOrdenacao(sortField);
                filtro.setFiltrosConsulta(egressoBean.getFiltrosConsulta());
                filtro.setClassePrincipalDaConsulta(consultaCustomizadaController.descobrirClassePrincipalDaConsulta(egressoBean.getAbaSelecionada()));
                filtro.setPrefixoDaClassePrincipal(consultaCustomizadaController.descobrirPrefixoDaClassePrincipal(egressoBean.getAbaSelecionada()));
                setRowCount(consultaLazy.quantidadeFiltrados(filtro));
                return consultaLazy.filtrados(filtro);
            }

            @Override
            public Object getRowKey(Class object) {
                return egressoBean.getEgresso();
            }

            @Override
            public Class getRowData(String rowKey) {
                 return Egresso.class;
            }
            
            
            
        };
    }
    
    public ConsultaLazyController getConsultaLazy() {
        return consultaLazy;
    }

    public void setConsultaLazy(ConsultaLazyController consultaLazy) {
        this.consultaLazy = consultaLazy;
    }

    public FiltroConsultaLazy getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroConsultaLazy filtro) {
        this.filtro = filtro;
    }

    public LazyDataModel<Class> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Class> model) {
        this.model = model;
    }

    public boolean isExibirFrmSalvarConsulta() {
        return exibirFrmSalvarConsulta;
    }

    public void setExibirFrmSalvarConsulta(boolean exibirFrmSalvarConsulta) {
        this.exibirFrmSalvarConsulta = exibirFrmSalvarConsulta;
    }

    public boolean isExibirConsultasFavoritas() {
        return exibirConsultasFavoritas;
    }

    public void setExibirConsultasFavoritas(boolean exibirConsultasFavoritas) {
        this.exibirConsultasFavoritas = exibirConsultasFavoritas;
    }

    public boolean isTornarConsultaPublica() {
        return this.getConsultaEdicao().getVisibilidadeDaConsulta() != null && this.getConsultaEdicao().getVisibilidadeDaConsulta().equals(VisibilidadeConsulta.PUBLICA);
    }

    public void setTornarConsultaPublica(boolean tornarConsultaPublica) {
        if (tornarConsultaPublica) {
            this.getConsultaEdicao().setVisibilidadeDaConsulta(VisibilidadeConsulta.PUBLICA);
        } else {
            this.getConsultaEdicao().setVisibilidadeDaConsulta(VisibilidadeConsulta.PRIVADA);
        }
    }

    public List<Consulta> getConsultasSalvas() {
        return consultasSalvas;
    }

    public void setConsultasSalvas(List<Consulta> consultasSalvas) {
        this.consultasSalvas = consultasSalvas;
    }

    public Consulta getConsultaEdicao() {
        if (consultaEdicao == null) {
            this.consultaEdicao = new Consulta();
        }
        return consultaEdicao;
    }

    public void setConsultaEdicao(Consulta consultaEdicao) {
        this.consultaEdicao = consultaEdicao;
    }

    public VisibilidadeConsulta[] getVisibilidadesConsulta() {
        return VisibilidadeConsulta.values();
    }

    public String adicionarConsultaAosFavoritos() {
        this.setExibirFrmSalvarConsulta(true);
        return "";
    }

    public String salvarConsultaFavorita(Long idUsuario) {
        consultaEdicao.setTimeStampCriacao(new Date());
        setFiltrosNaConsultaEdicao();
        consultaEdicao.setUsuario(idUsuario);
        List<String> inconsistenciasConsulta = consultaEdicao.validar();
        if (inconsistenciasConsulta.isEmpty() && consultaCustomizadaController.salvarConsultaFavorita(consultaEdicao)) {
            this.setExibirFrmSalvarConsulta(false);
            this.setExibirConsultasFavoritas(false);
            this.getConsultasSalvas().add(consultaEdicao);
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Consulta adiconada aos favoritos com sucesso!", "/egressos/listagemEgressos");
            consultaEdicao = new Consulta();
        } else {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Erro ao adicionar consulta aos favoritos!", "/egressos/listagemEgressos");
        }
        return "/egressos/listagemEgressos?faces-redirect=true";
    }

    private void setFiltrosNaConsultaEdicao() {
        List<FiltroConsulta> filtros = new ArrayList<>();
        for (FiltroConsulta filtro : egressoBean.getFiltrosConsulta()) {
            if (filtro.getConsulta() == null || filtro.getConsulta().getId() == null) {
                filtro.setConsulta(consultaEdicao);
                filtros.add(filtro);
            } else {
                filtros.add(criarCopiaDoFiltro(filtro));
            }
        }
        consultaEdicao.setFiltrosConsulta(filtros);
    }

    private FiltroConsulta criarCopiaDoFiltro(FiltroConsulta filtro) {
        FiltroConsulta filtroCopia = new FiltroConsulta(filtro.getCampo(), filtro.getRegra(), filtro.getValor(), filtro.getOperador(), filtro.getValorLabel());
        filtroCopia.setConsulta(consultaEdicao);
        return filtroCopia;
    }

    public void cancelarConsulta() {
        this.setExibirFrmSalvarConsulta(false);
        this.setExibirConsultasFavoritas(false);
    }

    public void excluirConsulta(Consulta consulta) {
        egressoBean.limparCampos();
        if (consulta != null) {
            consultaCustomizadaController.excluirConsultaFavorita(consulta);
            consultasSalvas.remove(consulta);
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Consulta excluída com sucesso!", "/egressos/listagemEgressos");
        } else {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Não foi possível excluir a consulta!", "/egressos/listagemEgressos");
        }
    }

    public void abrirFavoritos(Long idUsuario) {
        consultasSalvas = consultaCustomizadaController.consultarFavoritosPorUsuario(idUsuario);
        setExibirConsultasFavoritas(true);
        setExibirFrmSalvarConsulta(false);
    }

    public void carregarConsulta(Consulta consulta) {
        RequestContext.getCurrentInstance().execute("ativarAbaSelecionada('" + egressoBean.getAbaSelecionada() + "')");
        egressoBean.setFiltrosConsulta(consulta.getFiltrosConsulta());
        egressoBean.limparCampos();
        setExibirConsultasFavoritas(false);
        setExibirFrmSalvarConsulta(false);
    }
}
