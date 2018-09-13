package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.AreaConhecimentoController;
import br.ufg.inf.fabrica.sempreufg.controller.EgressoController;
import br.ufg.inf.fabrica.sempreufg.controller.LocalizacaoGeograficaController;
import br.ufg.inf.fabrica.sempreufg.controller.OutrasIESController;
import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import br.ufg.inf.fabrica.sempreufg.dominio.Atuacao;
import br.ufg.inf.fabrica.sempreufg.dominio.ConfiguracaoDePastas;
import br.ufg.inf.fabrica.sempreufg.dominio.CursoDeOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoEmOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoNaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.LocalizacaoGeografica;
import br.ufg.inf.fabrica.sempreufg.dominio.Residencia;
import br.ufg.inf.fabrica.sempreufg.enums.FormaIngressoInstituicao;
import br.ufg.inf.fabrica.sempreufg.enums.FrequenciaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import br.ufg.inf.fabrica.sempreufg.enums.NaturezaOrganizacao;
import br.ufg.inf.fabrica.sempreufg.enums.Nivel;
import br.ufg.inf.fabrica.sempreufg.enums.Sexo;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstituicao;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeDados;
import br.ufg.inf.fabrica.sempreufg.controller.ConsultaCustomizadaController;
import br.ufg.inf.fabrica.sempreufg.dominio.AvaliacaoDoCursoPeloEgresso;
import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import br.ufg.inf.fabrica.sempreufg.dominio.RealizacaoDeProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.enums.Motivacao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.visao.util.MensagensTela;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Danillo
 */
@Named(value = "egressoBean")
@SessionScoped
public class EgressoBean implements Serializable {

    private String sqlFiltro;

    private int limiteRegistros;

    private Egresso egresso;

    private StreamedContent fotoExibida;

    private List<UploadedFile> fotosAdicionais;

    private Residencia residenciaEdicao;

    private Atuacao atuacaoEdicao;

    private RealizacaoDeProgramaAcademico programaEdicao;

    private AvaliacaoDoCursoPeloEgresso avaliacaoEdicao;

    private HistoricoNaUFG historico;

    private HistoricoEmOutraIES historicoOutraIES;

    private CursoDeOutraIES cursoSelecionado = null;

    private final EgressoController controllerEgresso;

    private final OutrasIESController controllerOutrasIes;

    private final AreaConhecimentoController controllerAreaConhecimento;

    private final LocalizacaoGeograficaController controllerLocalizacaoGeografia;

    private final MensagensTela mensagemDeTela = new MensagensTela();

    private boolean exibirFrmAtuacao;
    private boolean exibirFrmAvaliacao;
    private boolean exibirFrmPrograma;

    private String abaSelecionada = "abaDadosPessoais";

    private ConsultaCustomizadaController consultaCustomizadaController;
    private final List<SelectItem> camposDeConsulta = new ArrayList<>();
    private String campoConsulta;
    private String regraDeFiltro;
    private String valorParametroConsulta;
    private String operadorConsulta = "";
    private List<FiltroConsulta> filtrosConsulta;
    private String[] valoresDoEnumSelecionados;
    private String accordionAtivo = "0";

    private List<CursoDeOutraIES> listaCursoOutraIesID = new ArrayList<>();

    public boolean isCampoEnum() {
        //verificar se campo seleciona é um enum
        return consultaCustomizadaController.campoEhTipoEnum(campoConsulta);
    }

    public String getTipoDoCampoSelecionado() {
        return consultaCustomizadaController.descobrirTipoCampo(campoConsulta);
    }

    public List<CursoDeOutraIES> getListaCursoOutraIesID() {
        return listaCursoOutraIesID;
    }

    public void setListaCursoOutraIesID(List<CursoDeOutraIES> listaCursoOutraIesID) {
        this.listaCursoOutraIesID = listaCursoOutraIesID;
    }

    public boolean isExibirFrmAtuacao() {
        return exibirFrmAtuacao;
    }

    public void setExibirFrmAtuacao(boolean exibir) {
        this.exibirFrmAtuacao = exibir;
    }

    public boolean isExibirFrmAvaliacao() {
        return exibirFrmAvaliacao;
    }

    public void setExibirFrmAvaliacao(boolean exibirFrmAvaliacao) {
        this.exibirFrmAvaliacao = exibirFrmAvaliacao;
    }

    public boolean isExibirFrmPrograma() {
        return exibirFrmPrograma;
    }

    public void setExibirFrmPrograma(boolean exibirFrmPrograma) {
        this.exibirFrmPrograma = exibirFrmPrograma;
    }

    public EgressoBean() {
        this.fotosAdicionais = new ArrayList();
        controllerEgresso = new EgressoController();
        controllerOutrasIes = new OutrasIESController();
        controllerAreaConhecimento = new AreaConhecimentoController();
        controllerLocalizacaoGeografia = new LocalizacaoGeograficaController();
        consultaCustomizadaController = new ConsultaCustomizadaController();
        filtrosConsulta = new ArrayList<>();
        construirCamposDeConsulta();
        this.setExibirFrmAtuacao(false);

    }

    public String atualizarEgresso() {
        Long idEgresso = egresso.getId();
        List<String> inconsistencias = controllerEgresso.atualizar(egresso);
        if (inconsistencias.isEmpty()) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Atualização realizada com sucesso!", "/egressos/atualizaDadosPessoais");
        } else {
            for (String inconsistencia : inconsistencias) {
                mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, inconsistencia, "");
            }
        }
        //atualizar egresso com os dados do banco
        this.setEgresso(controllerEgresso.buscarTodosOsDadosDoEgresso(idEgresso));

        return FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo().toString() + "?faces-redirect=true";

    }

    public String cancelarAtualizacaoEgresso() {
        this.atuacaoEdicao = null;
        this.avaliacaoEdicao = null;
        this.historico = null;
        this.historicoOutraIES = null;
        this.programaEdicao = null;
        this.residenciaEdicao = null;
        RequestContext.getCurrentInstance().execute("ativarAbaSelecionada('" + this.abaSelecionada + "')");
        return "/egressos/listagemEgressos";
    }

    public String prepararAtualizacaoEgresso(Egresso egresso) {
        this.setEgresso(controllerEgresso.buscarTodosOsDadosDoEgresso(egresso.getId()));
        return "/egressos/atualizaDadosPessoais?faces-redirect=true";
    }

    public void consultar(String abaSelecionada) {
        //Esse método ao ativar a renderização da abaSelecionada 
        // dispara o 'load' da classe ConsultaLazyBean realizando assim a consulta Lazy
        this.setAbaSelecionada(abaSelecionada);
    }

    public void adicionarFiltro() {
        RequestContext.getCurrentInstance().execute("ativarAbaSelecionada('" + this.getAbaSelecionada() + "')");
        RequestContext.getCurrentInstance().execute("ocultaMapa();");
        consultar(this.getAbaSelecionada());
        String valorLabel = null;
        //  Para cada valor selecionado recupera o valor da chave dele 
        //  construir um filtroConsultar com operador OR (a partir do 2o valor) e regraDeFiltro 'contém'
        if (campoEhEnumValido()) {
            StringBuilder valoresEnum = new StringBuilder();
            StringBuilder labelValores = new StringBuilder();

            valoresEnum.append("( ");
            for (String val : this.valoresDoEnumSelecionados) {
                valoresEnum.append(consultaCustomizadaController.recuperarChaveEnum(campoConsulta, val)).append(", ");
                labelValores.append(val).append(" <b> OU </b> ");
            }
            valoresEnum.replace(valoresEnum.lastIndexOf(","), valoresEnum.length(), " )");
            labelValores.replace(labelValores.lastIndexOf("<b> OU </b>") - 1, labelValores.length(), "");
            regraDeFiltro = "contém";
            valorParametroConsulta = valoresEnum.toString();
            valorLabel = labelValores.toString();
        } else {
            valorLabel = valorParametroConsulta;
        }
        if (campoConsulta == null || regraDeFiltro == null) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Campo e Regra devem ser preenchidos!", "/egressos/listagemEgressos");
        } else {
            filtrosConsulta.add(new FiltroConsulta(campoConsulta, regraDeFiltro, valorParametroConsulta, operadorConsulta, valorLabel));
            limparCampos();
            this.setOperadorConsulta("AND");
        }

    }

    private boolean campoEhEnumValido() {
        return this.isCampoEnum() && (this.valoresDoEnumSelecionados != null && this.valoresDoEnumSelecionados.length > 0);
    }

    public String removerFiltro(FiltroConsulta filter) {
        RequestContext.getCurrentInstance().execute("ativarAbaSelecionada('" + this.getAbaSelecionada() + "')");
        RequestContext.getCurrentInstance().execute("ocultaMapa();");
        filtrosConsulta.remove(filter);
        if (filtrosConsulta != null && filtrosConsulta.size() > 0 && !filtrosConsulta.get(0).getOperador().isEmpty()) {
            filtrosConsulta.get(0).setOperador("");
        } else {
            this.setOperadorConsulta("");
        }
        limparCampos();
        return "";
    }

    public void limparCampos() {
        this.setCampoConsulta(null);
        this.setRegraDeFiltro(null);
        this.setValorParametroConsulta(null);
    }

    public void atualizarFotoPrincipal(FileUploadEvent event) throws FileNotFoundException {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
            String nomeArquivo = ConfiguracaoDePastas.getPASTA_FOTOS() + this.egresso.getId() + "_" + fmt.format(new Date()) + "_" + event.getFile().getFileName().trim();
            File diretorioAnexos = new File(ConfiguracaoDePastas.getPASTA_FOTOS());
            if (!diretorioAnexos.exists()) {
                diretorioAnexos.mkdir();
            }
            File file = new File(nomeArquivo);
            OutputStream out = new FileOutputStream(file);
            out.write(event.getFile().getContents());
            this.egresso.setFotoPrincipal(nomeArquivo);
            this.setFotoExibida(new DefaultStreamedContent(new ByteArrayInputStream(event.getFile().getContents())));

        } catch (IOException ex) {
            Logger.getLogger(EgressoBean.class.getName()).log(Level.SEVERE, "Erro upload foto egresso!", ex);
        }
    }

    public void uploadFotosAdicionais(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.fotosAdicionais.add(event.getFile());
        salvarFotosNoDisco(fotosAdicionais);
    }

    public void salvarFotosNoDisco(List<UploadedFile> fotosAdicionais) throws FileNotFoundException, IOException {
        List<String> fotosSalvar = new ArrayList<String>();
        File diretorioAnexos = new File(ConfiguracaoDePastas.getPASTA_FOTOS());
        if (!diretorioAnexos.exists()) {
            diretorioAnexos.mkdir();
        }
        for (UploadedFile uploadedFile : fotosAdicionais) {
            String nomeArquivo = ConfiguracaoDePastas.getPASTA_FOTOS() + this.egresso.getId() + "fadd_" + uploadedFile.getFileName();
            File file = new File(nomeArquivo);
            OutputStream out = new FileOutputStream(file);
            out.write(uploadedFile.getContents());
            fotosSalvar.add(nomeArquivo);
        }
        this.egresso.setFotosAdicionais(fotosSalvar);

    }

    public boolean filterByName(Object value, Object filter, Locale locale) {
        return value.toString().toUpperCase().startsWith(filter.toString().toUpperCase());
    }

    public EgressoController getController() {
        return this.controllerEgresso;
    }

    public Sexo[] getSexos() {
        return Sexo.values();
    }

    public List<String> getRegrasDeFiltro() throws ClassNotFoundException {
        return consultaCustomizadaController.regrasDeFiltro(campoConsulta == null ? null : campoConsulta);
    }

    public FrequenciaDivulgacao[] getFrequenciasDivulgacao() {
        return FrequenciaDivulgacao.values();
    }

    public VisibilidadeDados[] getVisibilidadeDados() {
        return VisibilidadeDados.values();
    }

    public VisibilidadeDados[] getVisibilidades() {
        return VisibilidadeDados.values();
    }

    public String getSqlFiltro() {
        return sqlFiltro;
    }

    public void setSqlFiltro(String sqlFiltro) {
        this.sqlFiltro = sqlFiltro;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public int getLimiteRegistros() {
        return limiteRegistros;
    }

    public void setLimiteRegistros(int limiteRegistros) {
        this.limiteRegistros = limiteRegistros;
    }

    public StreamedContent getFotoExibida() {
        return stringToStreamedContent(this.egresso.getFotoPrincipal());
    }

    public void setFotoExibida(StreamedContent fotoExibida) {
        this.fotoExibida = fotoExibida;
    }

    public Residencia getResidenciaEdicao() {
        return this.residenciaEdicao;
    }

    public void setResidenciaEdicao(Residencia residencia) {
        this.residenciaEdicao = residencia;
    }

    public Atuacao getAtuacaoEdicao() {
        return atuacaoEdicao;
    }

    public void setAtuacaoEdicao(Atuacao atuacaoEdicao) {
        this.atuacaoEdicao = atuacaoEdicao;
    }

    public AvaliacaoDoCursoPeloEgresso getAvaliacaoEdicao() {
        return avaliacaoEdicao;
    }

    public void setAvaliacaoEdicao(AvaliacaoDoCursoPeloEgresso avaliacaoEdicao) {
        this.avaliacaoEdicao = avaliacaoEdicao;
    }

    public RealizacaoDeProgramaAcademico getProgramaEdicao() {
        return programaEdicao;
    }

    public void setProgramaEdicao(RealizacaoDeProgramaAcademico programaEdicao) {
        this.programaEdicao = programaEdicao;
    }

    public HistoricoNaUFG getHistorico() {
        return historico;
    }

    public void setHistorico(HistoricoNaUFG historico) {
        this.historico = historico;
    }

    public CursoDeOutraIES getCursoSelecionado() {
        return cursoSelecionado;
    }

    public void setCursoSelecionado(CursoDeOutraIES cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    public HistoricoEmOutraIES getHistoricoOutraIES() {
        return historicoOutraIES;
    }

    public void setHistoricoOutraIES(HistoricoEmOutraIES historicoOutraIES) {
        this.historicoOutraIES = historicoOutraIES;
    }

    public OutrasIESController getControllerOutrasIes() {
        return controllerOutrasIes;
    }

    public AreaConhecimentoController getControllerAreaConhecimento() {
        return controllerAreaConhecimento;
    }

    public String getRegraDeFiltro() {
        return regraDeFiltro;
    }

    public void setRegraDeFiltro(String regraDeFiltro) {
        this.regraDeFiltro = regraDeFiltro;
    }

    public ConsultaCustomizadaController getConsultaCustomizadaController() {
        return consultaCustomizadaController;
    }

    public void setConsultaCustomizadaController(ConsultaCustomizadaController consultaCustomizadaController) {
        this.consultaCustomizadaController = consultaCustomizadaController;
    }

    public String getCampoConsulta() {
        return campoConsulta;
    }

    public void setCampoConsulta(String campoConsulta) {
        this.campoConsulta = campoConsulta;
    }

    public String getValorParametroConsulta() {
        return valorParametroConsulta;
    }

    public void setValorParametroConsulta(String valorParametroConsulta) {
        this.valorParametroConsulta = valorParametroConsulta;
    }

    public List<FiltroConsulta> getFiltrosConsulta() {
        return filtrosConsulta;
    }

    public void setFiltrosConsulta(List<FiltroConsulta> filtrosConsulta) {
        this.filtrosConsulta = filtrosConsulta;
    }

    public String getOperadorConsulta() {
        return operadorConsulta;
    }

    public void setOperadorConsulta(String operadorConsulta) {
        this.operadorConsulta = operadorConsulta;
    }

    public String getAbaSelecionada() {
        return abaSelecionada;
    }

    public void setAbaSelecionada(String abaSelecionada) {
        this.abaSelecionada = abaSelecionada;
    }

    public String carregarConteudoAbaSelecionada() {
        switch (this.abaSelecionada) {
            case "abaDadosResidencia":
                return "/egressos/consultaAbaDadosResidencia.xhtml";

            case "abaEnsinoMedio":
                return "/egressos/consultaAbaEnsinoMedio.xhtml";

            case "abaHistoricoUFG":
                return "/egressos/consultaAbaHistoricoUFG.xhtml";

            case "abaAvaliacaoCurso":
                return "/egressos/consultaAbaAvaliacaoCurso.xhtml";

            case "abaProgramaAcademico":
                return "/egressos/consultaAbaProgramaAcademico.xhtml";

            case "abaHistoricoOutrasIES":
                return "/egressos/consultaAbaHistoricoOutrasIES.xhtml";

            case "abaAtuacaoProfissional":
                return "/egressos/consultaAbaAtuacaoProfissional.xhtml";

            default:
                return "/egressos/consultaAbaDadosPessoais.xhtml";
        }
    }

    public String carregarConteudoAbaSelecionadaEdicaoEgresso(String aba) {
        switch (aba) {
            case "dadosPessoais":                
                return "/egressos/atualizaDadosPessoais?faces-redirect=true";
            case "dadosEducacionais":
                return "/egressos/atualizaDadosEducacionais.xhtml?faces-redirect=true";
            case "dadosProfissionais":
                return "/egressos/atualizaDadosProfissionais.xhtml?faces-redirect=true";
            default:
                return "/egressos/atualizaDadosPessoais?faces-redirect=true";
        }
    }

    public String[] getValoresDoEnumSelecionados() {
        return valoresDoEnumSelecionados;
    }

    public void setValoresDoEnumSelecionados(String[] valoresDoEnumSelecionados) {
        this.valoresDoEnumSelecionados = valoresDoEnumSelecionados;
    }

    public String getAccordionAtivo() {
        return accordionAtivo;
    }

    public void setAccordionAtivo(String accordionAtivo) {
        this.accordionAtivo = accordionAtivo;
    }

    public void onTabChange(TabChangeEvent event) {
        setAccordionAtivo("0");
    }

    public void onTabClose(TabCloseEvent event) {
        setAccordionAtivo("-1");
    }

    public List<SelectItem> getCamposDeConsulta() {
        return camposDeConsulta;
    }

    private void construirCamposDeConsulta() {
        List<String> campos = consultaCustomizadaController.camposConsulta();
        List<SelectItem> camposEntidade = new ArrayList<>();
        SelectItemGroup siGroup;

        siGroup = new SelectItemGroup("Atuação profissional");
        for (String campo : campos) {
            if (campo.contains("AtP")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Avaliação de Curso");
        for (String campo : campos) {
            if (campo.contains("AvC")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Egresso");
        for (String campo : campos) {
            if (campo.contains("Egr")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Ensino Médio");
        for (String campo : campos) {
            if (campo.contains("EnM")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Histórico na UFG");
        for (String campo : campos) {
            if (campo.contains("UFG")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Histórico outras IES");
        for (String campo : campos) {
            if (campo.contains("IES")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Programa acadêmico");
        for (String campo : campos) {
            if (campo.contains("PrA")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);

        camposEntidade = new ArrayList<>();
        siGroup = new SelectItemGroup("Residência");
        for (String campo : campos) {
            if (campo.contains("Res")) {
                camposEntidade.add(new SelectItem(campo, campo));
            }
        }
        siGroup.setSelectItems(camposEntidade.toArray(new SelectItem[0]));
        camposDeConsulta.add(siGroup);
    }

    /*Método é chamado quando egresso não têm foto principal*/
    public String pegarFotoPadrao() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath() + "/resources/img/person.png";
    }

    public List<StreamedContent> getFotosAdicionais() {
        List<StreamedContent> fotos = new ArrayList<>();
        for (String f : this.egresso.getFotosAdicionais()) {
            fotos.add(stringToStreamedContent(f));
        }
        return fotos;
    }

    public void setFotosAdicionais(List<UploadedFile> fotosAdicionais) {
        this.fotosAdicionais = fotosAdicionais;
    }

    public String editarResidencia(Residencia r) {
        this.setResidenciaEdicao(r);
        if (r == null) {
            this.setResidenciaEdicao(new Residencia());
        }
        return "editarResidencia?faces-redirect=true";
    }

    public String editarHistoricoOutrosCursos(HistoricoEmOutraIES h) {
        this.setHistoricoOutraIES(h);
        if (h == null) {
            this.setHistoricoOutraIES(new HistoricoEmOutraIES());
        }
        return "editarCursoExterno?faces-redirect=true";
    }

    public void editarAtuacao() {
        this.setExibirFrmAtuacao(true);
        if (atuacaoEdicao == null) {
            this.setAtuacaoEdicao(new Atuacao());
        }
    }

    public String editarHistoricoUFG(HistoricoNaUFG h) {
        this.setHistorico(h);
        if (h == null) {
            return "atualizaDadosPessoais?faces-redirect=true";
        }
        return "editarCursoUFG?faces-redirect=true";
    }

    public void editarAvaliacao() {
        this.setExibirFrmAvaliacao(true);
        if (avaliacaoEdicao == null) {
            this.setAvaliacaoEdicao(new AvaliacaoDoCursoPeloEgresso());
        }
    }

    public void editarPrograma() {
        this.setExibirFrmPrograma(true);
        if (programaEdicao == null) {
            this.setProgramaEdicao(new RealizacaoDeProgramaAcademico());
        }
    }

    public String adicionarResidencia() {
        String operacao = "inserido";
        if (residenciaEdicao.getId() != null && residenciaEdicao.getId() > 0) {
            for (Residencia res : this.egresso.getResidencias()) {
                if (Objects.equals(res.getId(), residenciaEdicao.getId())) {
                    this.egresso.remResidencia(res);
                    operacao = "editado";
                    break;
                }
            }
        }
        this.egresso.addResidencia(residenciaEdicao);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Endereço " + operacao + " com sucesso!", "/egressos/atualizaDadosPessoais");
        return "atualizaDadosPessoais?faces-redirect=true";
    }

    public String adicionarHistoricoOutrosCursos() {
        String operacao = "inserido";
        if (historicoOutraIES.getId() != null && historicoOutraIES.getId() > 0) {
            for (HistoricoEmOutraIES hoies : this.egresso.getHistoricosEmOutrasIES()) {
                if (Objects.equals(hoies.getId(), historicoOutraIES.getId())) {
                    this.getEgresso().remHistoricoOutraIES(hoies);
                    operacao = "editado";
                    break;
                }
            }
        }
        this.egresso.addHistoricoOutraIES(historicoOutraIES);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Curso " + operacao + " com sucesso!", "/egressos/atualizaDadosEducacionais");
        return "atualizaDadosEducacionais?faces-redirect=true";
    }

    public String adicionarAtuacao() {
        String operacao = "inserida";
        List<String> inconsistenciasAtuacao = atuacaoEdicao.validar();
        if (!inconsistenciasAtuacao.isEmpty()) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Existem inconsistências no registro!", "/egressos/atualizaDadosProfissionais");
            return "atualizaDadosProfissionais?faces-redirect=true";
        }
        //remover para atualizar atuacoes no historico
        if (atuacaoEdicao.getId() != null && atuacaoEdicao.getId() > 0) {
            for (Atuacao atua : this.egresso.getAtuacoes()) {
                if (Objects.equals(atua.getId(), atuacaoEdicao.getId())) {
                    this.getEgresso().remAtuacao(atua);
                    operacao = "editada";
                    break;
                }
            }
        }
        this.getEgresso().addAtuacao(atuacaoEdicao);
        this.setExibirFrmAtuacao(false);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Atuação " + operacao + " com sucesso!", "/egressos/atualizaDadosProfissionais");
        return "atualizaDadosProfissionais?faces-redirect=true";
    }

    public String adicionarAvaliacao() {
        String operacao = "inserida";
        List<String> inconsistenciasAvaliacao = avaliacaoEdicao.validar();
        if (!inconsistenciasAvaliacao.isEmpty()) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Existem inconsistências no registro!", "/egressos/atualizaDadosEducacionais");
            return "";
        }
        //remover para atualizar avaliacoes no historico
        if (avaliacaoEdicao.getId() != null && avaliacaoEdicao.getId() > 0) {
            for (AvaliacaoDoCursoPeloEgresso av : this.historico.getAvaliacoes()) {
                if (Objects.equals(av.getId(), avaliacaoEdicao.getId())) {
                    this.historico.getAvaliacoes().remove(av);
                    operacao = "editada";
                    break;
                }
            }
        }
        this.historico.addAvaliacao(avaliacaoEdicao);
        this.setExibirFrmAvaliacao(false);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Avaliação " + operacao + " com sucesso!", "/egressos/atualizaDadosEducacionais");
        return "";
    }

    public String adicionarPrograma() {
        String operacao = "inserido";
        List<String> inconsistenciasPrograma = programaEdicao.validar();
        if (!inconsistenciasPrograma.isEmpty()) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, "Existem inconsistências no registro!", "/egressos/atualizaDadosEducacionais");
            return "";
        }
        //remover para atualizar programas no historico
        if (programaEdicao.getId() != null && programaEdicao.getId() > 0) {
            for (RealizacaoDeProgramaAcademico prog : this.historico.getRealizacaoProgramasAcademicos()) {
                if (Objects.equals(prog.getId(), programaEdicao.getId())) {
                    this.historico.getRealizacaoProgramasAcademicos().remove(prog);
                    operacao = "editado";
                    break;
                }
            }
        }
        this.historico.addPrograma(programaEdicao);
        this.setExibirFrmPrograma(false);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Programa " + operacao + " com sucesso!", "/egressos/atualizaDadosEducacionais");
        return "";
    }

    public String cancelarEdicaoResidencia() {
        this.residenciaEdicao = null;
        return "atualizaDadosPessoais?faces-redirect=true";
    }

    public String cancelarEdicaoHistorico() {
        this.historicoOutraIES = null;
        return "atualizaDadosEducacionais?faces-redirect=true";
    }

    public String cancelarEdicaoAtuação() {
        this.setExibirFrmAtuacao(false);
        this.atuacaoEdicao = null;
        return "atualizaDadosProfissionais?faces-redirect=true";
    }

    public void cancelarEdicaoCursoUFG() {
        this.setExibirFrmAvaliacao(false);
        this.setExibirFrmPrograma(false);
        this.avaliacaoEdicao = null;
        this.programaEdicao = null;
    }

    public void removerResidencia(Residencia residencia) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg;
        //if (this.getEgresso().getResidencias().size() > 1) {
        this.getEgresso().remResidencia(residencia);
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Endereço removido!", "");
        //} else {
        //    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Egresso deve ter ao menos um endereço!", "");
        //}
        contexto.addMessage("atualizaDadosPessoais", msg);
    }

    public void removerHistorioOutrasIES() {
        this.getEgresso().remHistoricoOutraIES(this.historicoOutraIES);
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Curso removido", "");
        contexto.addMessage("atualizaDadosEducacionais", msg);
    }

    public void removerAtuacao() {
        this.getEgresso().remAtuacao(atuacaoEdicao);
        this.atuacaoEdicao = new Atuacao();
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atuação removida", "");
        contexto.addMessage("atualizaDadosProfissionais", msg);
    }

    public void removerAvaliacao() {
        this.historico.getAvaliacoes().remove(this.avaliacaoEdicao);
        this.avaliacaoEdicao = new AvaliacaoDoCursoPeloEgresso();
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Avaliação removida", "");
        contexto.addMessage("atualizaDadosEducacionais", msg);
    }

    public void removerPrograma() {
        this.historico.getRealizacaoProgramasAcademicos().remove(this.programaEdicao);
        this.programaEdicao = new RealizacaoDeProgramaAcademico();
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Programa acadêmico removido", "");
        contexto.addMessage("atualizaDadosEducacionais", msg);
    }

    //Função para recuperar imagem do servidor e exibi para o usuário     
    private StreamedContent stringToStreamedContent(String caminhoArquivo) {
        try {
            FileInputStream stream = new FileInputStream(caminhoArquivo);
            return new DefaultStreamedContent(stream, "application/octet-stream", caminhoArquivo);

        } catch (Exception ex) {
            Logger.getLogger(EgressoBean.class
                    .getName()).log(Level.SEVERE, null, "");
            return null;
        }
    }

    public List<String> getListaDeCursosDeIE() {
        List<String> nomesCursoDaIes = new ArrayList();
        if (this.getListaCursoOutraIesID() == null || this.getListaCursoOutraIesID().isEmpty()) {
            return nomesCursoDaIes;
        } else {
            for (CursoDeOutraIES curso : this.getListaCursoOutraIesID()) {
                nomesCursoDaIes.add(curso.getId().getNomeDoCurso());
            }
        }
        return nomesCursoDaIes;
    }

    public List<String> completeIES(String iesDoCurso) {
        List<String> nomesIes = new ArrayList<String>();
        List<CursoDeOutraIES> outrasIes = controllerOutrasIes.buscarOutraIes(iesDoCurso);
        if (outrasIes != null) {
            this.setListaCursoOutraIesID(outrasIes);
        }
        for (CursoDeOutraIES ies : outrasIes) {
            nomesIes.add(ies.getId().getIesDoCurso());
        }
        Set<String> nomesIesUnicas = new HashSet<String>(nomesIes);
        nomesIes.clear();
        nomesIes.addAll(nomesIesUnicas);
        return nomesIes;
    }

    public void setListaDeIES(List<String> lista) {
    }

    private void buscarCursoCadastrado() {
        this.cursoSelecionado = null;
        String nomeCurso = historicoOutraIES.getCursoDeOutraIES().getId().getNomeDoCurso();
        String nomeIes = historicoOutraIES.getCursoDeOutraIES().getId().getIesDoCurso();
        if (nomeCurso != null && !nomeCurso.isEmpty() && nomeIes != null && !nomeIes.isEmpty()) {
            CursoDeOutraIES cursoOutraIES = controllerOutrasIes.buscarCursoSuperior(nomeIes, nomeCurso);
            if (cursoOutraIES != null) {
                this.cursoSelecionado = cursoOutraIES;
                try {
                    historicoOutraIES.setCursoDeOutraIES(this.cursoSelecionado.clone());

                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(EgressoBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<LocalizacaoGeografica> pesquisarPorMunicipio(String municipio) {
        return this.controllerLocalizacaoGeografia.pesquisarPorMunicipio(municipio);
    }

    public void onIesChange() {
        buscarCursoCadastrado();
    }

    public Nivel[] getNiveis() {
        return Nivel.values();
    }

    public MesesAno[] getMesesAno() {
        return MesesAno.values();
    }

    public TipoInstituicao[] getTiposInstituicao() {
        return TipoInstituicao.values();
    }

    public TipoProgramaAcademico[] getTiposProgramaAcademicos() {
        return TipoProgramaAcademico.values();
    }

    public List<AreaConhecimento> pesquisarAreaConhecimentoPorNome(String nome) {
        return controllerAreaConhecimento.pesquisarPorNome(nome);
    }

    public NaturezaOrganizacao[] getNaturezasDaOrganizacao() {
        return NaturezaOrganizacao.values();
    }

    public Motivacao[] getMotivacoes() {
        return Motivacao.values();
    }

    public FormaIngressoInstituicao[] getFormasIngressoInstituicao() {
        return FormaIngressoInstituicao.values();
    }

}
