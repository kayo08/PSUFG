package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.EgressoController;
import br.ufg.inf.fabrica.sempreufg.controller.GerenciadorEmailController;
import br.ufg.inf.fabrica.sempreufg.controller.InstanciaAdministrativaUfgController;
import br.ufg.inf.fabrica.sempreufg.controller.LocalizacaoGeograficaController;
import br.ufg.inf.fabrica.sempreufg.controller.MudancaDeSenhaController;
import br.ufg.inf.fabrica.sempreufg.controller.UsuarioController;
import br.ufg.inf.fabrica.sempreufg.dao.utils.UsuarioContato;
import br.ufg.inf.fabrica.sempreufg.dominio.ConfiguracaoDePastas;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Papel;
import br.ufg.inf.fabrica.sempreufg.dominio.TokenDeResetDeSenha;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import br.ufg.inf.fabrica.sempreufg.enums.AssuntoEmailContato;
import br.ufg.inf.fabrica.sempreufg.visao.seguranca.SegurancaUtil;
import br.ufg.inf.fabrica.sempreufg.visao.util.MensagensTela;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Session;
import javax.persistence.DiscriminatorValue;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author auf
 */
@Named(value = "usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    @Inject
    DadosDaAplicacaoBean dadosDaAplicacaoBean;

//    @Resource(name = "mail/fabrica")
    Session mailSession;
    
    private String cpf;
    private String senha;
    private String novaSenha;
    private String novaSenhaConfirma;
    private String opcaoEscolhida;

    private UsuarioContato usuarioContato;

    private static final Random RANDOM = new Random();

    private List<String> nomesEscolha = new ArrayList<>();
    private List<String> maesEscolha = new ArrayList<>();
    private List<String> naturalidadesEscolha = new ArrayList<>();
    private List<String> datasNascimentoEscolha = new ArrayList<>();
    private List<String> cursosEscolha = new ArrayList<>();
    private List<String> anosIgressoEscolha = new ArrayList<>();
    private List<String> nomesEgressos;
    private List<String> nomesMaes;
    private List<String> nomesNaturalidades;
    private List<String> nomesCursos;

    private StreamedContent fotoExibicao;
    private int tentativas;

    private final UsuarioController usuarioController = new UsuarioController();
    private final GerenciadorEmailController gerenciadorEmailController;
    private final MensagensTela mensagemDeTela = new MensagensTela();

    private TokenDeResetDeSenha token;

    /**
     * Creates a new instance of Usuario
     */
    public UsuarioBean() {
        this.tentativas = 0;
        this.gerenciadorEmailController = new GerenciadorEmailController();
        this.usuarioContato = new UsuarioContato();
    }

    public Egresso getEgresso() {
        if (isUsuarioEgresso()) {
            return (Egresso) getUsuarioLogado();
        }
        return null;
    }

    public TokenDeResetDeSenha getToken() {
        return token;
    }

    public void setToken(TokenDeResetDeSenha token) {
        this.token = token;
    }

    public Usuario getUsuarioLogado() {
        return SegurancaUtil.retornarUsuarioLogado();
    }

    public UsuarioContato getUsuarioContato() {
        return usuarioContato;
    }

    public void setUsuarioContato(UsuarioContato usuarioContato) {
        this.usuarioContato = usuarioContato;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getNovaSenhaConfirma() {
        return novaSenhaConfirma;
    }

    public void setNovaSenhaConfirma(String novaSenhaConfirma) {
        this.novaSenhaConfirma = novaSenhaConfirma;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String prepararSubstituicaoDeSenha() {

        String url = dadosDaAplicacaoBean.getUrlAplicacao();
        usuarioController.prepararSubstituicaoDeSenha(mailSession, url, cpf);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Se o cpf informado fizer parte da nossa base de dados, um email será enviado para o email cadastrado"));
        return "login";
    }

    public void tratarAlteracaoDeSenha(String token) {
        if (token == null || token.isEmpty()) {
            setToken(null);
        } else {
            MudancaDeSenhaController controller = new MudancaDeSenhaController();
            if (controller.validarToken(token)) {
                this.token = controller.buscarToken(token);
            } else {
                FacesContext.getCurrentInstance().addMessage("msgTokenInvalido", new FacesMessage("Token inválido ou expirado"));
                this.token = null;
            }
        }
    }

    public String confirmarMudancaDeSenha() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        MudancaDeSenhaController controller = new MudancaDeSenhaController();

        if (novaSenha == null || novaSenha.isEmpty()
                || novaSenhaConfirma == null || novaSenhaConfirma.isEmpty()) {
            FacesMessage m = new FacesMessage("Nova senha deve ser informada");
            facesContext.addMessage(null, m);
            return "esqueciSenha";
        }

        if (!novaSenha.equals(novaSenhaConfirma)) {
            FacesMessage m = new FacesMessage("Senhas informadas diferentes");
            facesContext.addMessage(null, m);
            return "esqueciSenha";
        }

        List<String> inconsistencias
                = controller.confirmarMudancaDeSenha(token, novaSenha);

        if (inconsistencias == null || inconsistencias.isEmpty()) {
            FacesMessage m = new FacesMessage("Senha modificada com sucesso");
            facesContext.addMessage(null, m);
            return "login";
        }

        for (String inconsistencia : inconsistencias) {
            FacesMessage m = new FacesMessage(inconsistencia);
            facesContext.addMessage(null, m);
        }
        return "esqueciSenha";
    }

    public List<String> getNomesEscolha() {
        return this.nomesEscolha;
    }

    public void setNomesEscolha(List<String> nomesEscolha) {
        this.nomesEscolha = nomesEscolha;
    }

    public List<String> getMaesEscolha() {
        return this.maesEscolha;
    }

    public void setMaesEscolha(List<String> maesEscolha) {
        this.maesEscolha = maesEscolha;
    }

    public List<String> getNaturalidadesEscolha() {
        return this.naturalidadesEscolha;
    }

    public void setNaturalidadesEscolha(List<String> naturalidadesEscolha) {
        this.naturalidadesEscolha = naturalidadesEscolha;
    }

    public int getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    public String getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(String opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public List<String> getDatasNascimentoEscolha() {
        return datasNascimentoEscolha;
    }

    public void setDatasNascimentoEscolha(List<String> datasNascimentoEscolha) {
        this.datasNascimentoEscolha = datasNascimentoEscolha;
    }

    public List<String> getCursosEscolha() {
        return cursosEscolha;
    }

    public void setCursosEscolha(List<String> cursosEscolha) {
        this.cursosEscolha = cursosEscolha;
    }

    public List<String> getAnosIgressoEscolha() {
        return anosIgressoEscolha;
    }

    public void setAnosIgressoEscolha(List<String> anosIgressoEscolha) {
        this.anosIgressoEscolha = anosIgressoEscolha;
    }

    public StreamedContent getFotoExibicao() {
        return stringToStreamedContent(getUsuarioLogado().getFotoPessoal());
    }

    public void setFotoExibicao(StreamedContent fotoExibicao) {
        this.fotoExibicao = fotoExibicao;
    }

    public String efetuarLogin() throws IOException {

        if (this != null) {

//            if (isUsuarioEgresso()) {
//                this.setEgresso((Egresso) usuarioLogado);
//            }
//            //SE PRIMEIRO ACESSO - verificar autenticidade do usuario 6 perguntas (com 5 alternativas) cadastrar senha
//            if (this.getUsuarioLogado().getTimeStampUltimaUtilizacao() == null && isUsuarioEgresso()) {
//                criarListaFontes();
//                criarListasEscolha();
//                return "/egressos/validarPrimeiroAcesso.xhtml?faces-redirect=true";
//
//            }
//            //seta timeStamp da utilização
//            this.getUsuarioLogado().setTimeStampUltimaUtilizacao(new Date());
//            if (!usuarioController.Salvar(this.usuarioLogado)) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro de conexão com o banco", "");
//                contexto.addMessage("/login.xhtml", msg);
//                return "";
//            }
//            if (!isUsuarioEgresso()) {
//                Egresso egressoFake = new Egresso();
//                egressoFake.setId(getUsuarioLogado().getId());
//                egressoFake.setNomeOficial(getUsuarioLogado().getNomeSocial());
//                egressoFake.setPapeis(getUsuarioLogado().getPapeis());
//                egressoFake.setNomeSocial(getUsuarioLogado().getNomeSocial());
//                egressoFake.setEmailPrincipal(getUsuarioLogado().getEmailPrincipal());
//                egressoFake.setInstanciasAdministrativas(getUsuarioLogado().getInstanciasAdministrativas());
//                this.setEgresso(egressoFake);
//            }
        }
        return null;
    }

    public String efetuarLogoff() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isUsuarioEgresso() {
        return "EGR".equals(getUsuarioLogado().getClass().getAnnotation(DiscriminatorValue.class).value());
    }

    public boolean isAdministrador() {
        for (Papel p : getUsuarioLogado().getPapeis()) {
            if (p.getSigla().equalsIgnoreCase("AdmSis")) {
                return true;
            }
        }
        return false;
    }

    public String conferirOpcao(FlowEvent event) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg;

        if (event.getNewStep().equalsIgnoreCase("p2")
                && this.opcaoEscolhida.equalsIgnoreCase(
                        this.getEgresso().getNomeOficial())) {
            this.opcaoEscolhida = "";
            return event.getNewStep();

        } else if (event.getNewStep().equalsIgnoreCase("p3")
                && this.opcaoEscolhida.equalsIgnoreCase(
                        dataFormatada(this.getEgresso().getDataDeNascimento()))) {
            this.opcaoEscolhida = "";
            return event.getNewStep();

        } else if (event.getNewStep().equalsIgnoreCase("p4")
                && this.opcaoEscolhida.equalsIgnoreCase(
                        this.getEgresso().getHistoricosNaUFG().
                                get(0).getCursoDaUFG().getNome())) {
            this.opcaoEscolhida = "";
            return event.getNewStep();

        } else if (event.getNewStep().equalsIgnoreCase("p5")
                && this.opcaoEscolhida.equalsIgnoreCase(Integer.toString(
                        this.getEgresso().getHistoricosNaUFG()
                                .get(0).getAnoDeInicio()))) {
            this.opcaoEscolhida = "";
            return event.getNewStep();

        } else if (event.getNewStep().equalsIgnoreCase("p6")
                && this.opcaoEscolhida.equalsIgnoreCase(
                        this.getEgresso().getNomeDaMae())) {
            this.opcaoEscolhida = "";
            return event.getNewStep();

        } else if (event.getNewStep().equalsIgnoreCase("p7")
                && this.opcaoEscolhida.equalsIgnoreCase(
                        this.getEgresso().getNaturalidade().getCidade())) {
            this.opcaoEscolhida = "";
            return event.getNewStep();
        }
        this.tentativas++;
        //BLOQUEAR USUÁRIOS SE 3 ERROS 
        if (this.tentativas > 2) {
            this.getUsuarioLogado().setTimeStampExclusaoLogica(new Date());
//            if (!usuarioController.Salvar(getUsuarioLogado())) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro de conexão com o banco", "");
//                contexto.addMessage("/login.xhtml", msg);
//                return "";
//            }
            //ENVIAR MENSAGEM PARA O ADMINISTRADOR
            gerenciadorEmailController.enviarMensagensBloqueioUsuario(this.getEgresso());
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Usuário bloqueado! Entre em contato com o administrador do sistema", "");
            contexto.addMessage("/login.xhtml", msg);
            return "";
        }
        msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Opção não conferi!", "");
        contexto.addMessage("/login.xhtml", msg);
        this.opcaoEscolhida = "";
        criarListasEscolha();
        return event.getOldStep();

    }

    private void criarListaFontes() throws IOException {
        LocalizacaoGeograficaController locGeoController = new LocalizacaoGeograficaController();
        EgressoController egressoController = new EgressoController();
        InstanciaAdministrativaUfgController instanciaController = new InstanciaAdministrativaUfgController();

        this.nomesEgressos = egressoController.pesquisarValoresCampo("nomeOficial");
        this.nomesMaes = egressoController.pesquisarValoresCampo("nomeDaMae");
        this.nomesNaturalidades = locGeoController.pesquisarValoresCampo("cidade");
        this.nomesCursos = instanciaController.pesquisarValoresCampo("nome");
        List<String> nomesCursosCopia = new ArrayList(nomesCursos);
        for (String c : this.nomesCursos) {
            if (c == null || !c.contains("Curso")) {
                nomesCursosCopia.remove(c);
            }
        }

        this.nomesCursos = nomesCursosCopia;
    }

    private void criarListasEscolha() {
        this.nomesEscolha
                = geraListaRandom(nomesEgressos, nomesEscolha,
                        this.getEgresso().getNomeOficial());
        this.datasNascimentoEscolha
                = geraDatasRandom(datasNascimentoEscolha,
                        dataFormatada(this.getEgresso().getDataDeNascimento()));
        this.cursosEscolha
                = geraListaRandom(nomesCursos, cursosEscolha,
                        this.getEgresso().getHistoricosNaUFG()
                                .get(0).getCursoDaUFG().getNome());
        this.anosIgressoEscolha
                = geraAnosRandom(anosIgressoEscolha,
                        Integer.toString(this.getEgresso()
                                .getHistoricosNaUFG().get(0).getAnoDeInicio()));
        this.maesEscolha = geraListaRandom(nomesMaes, maesEscolha,
                this.getEgresso().getNomeDaMae());
        this.naturalidadesEscolha
                = geraListaRandom(nomesNaturalidades, naturalidadesEscolha,
                        this.getEgresso().getNaturalidade().getCidade());

    }

    private List<String> geraListaRandom(List<String> listaNomes, List<String> listaAtual, String nomeCorreto) {
        List<String> listaRetorno = new ArrayList<>();
        int posNomeCorreto = RANDOM.nextInt(5);
        int posNomeManter = posNomeCorreto;
        String nomeManter = "";
        if (listaAtual.size() > 0) {
            while (posNomeCorreto == posNomeManter) {
                posNomeManter = RANDOM.nextInt(5);
            }
            nomeManter = nomeCorreto;
            while (nomeManter.equalsIgnoreCase(nomeCorreto)) {
                nomeManter = listaAtual.get(RANDOM.nextInt(listaAtual.size()));
            }
            listaNomes.remove(nomeManter);
        }
        listaNomes.remove(nomeCorreto);

        for (int i = 0; i < 5; i++) {
            String novoNome = nomeCorreto;
            while (novoNome.equalsIgnoreCase(nomeCorreto) || novoNome.equalsIgnoreCase(nomeManter)) {
                novoNome = listaNomes.get(RANDOM.nextInt(listaNomes.size()));
            }
            listaRetorno.add(novoNome);
        }

        listaRetorno.set(posNomeCorreto, nomeCorreto);

        if (!nomeManter.isEmpty()) {
            listaRetorno.set(posNomeManter, nomeManter);
        }

        return listaRetorno;
    }

    private List<String> geraDatasRandom(List<String> listaAtual, String dataCorreta) {
        List<String> listaRetorno = new ArrayList<>();
        int posDataCorreta = RANDOM.nextInt(5);
        int posDataManter = posDataCorreta;
        Calendar dataRandom = Calendar.getInstance();
        String dataManter = "";

        if (listaAtual.size() > 0) {
            while (posDataCorreta == posDataManter) {
                posDataManter = RANDOM.nextInt(5);
            }
            dataManter = dataCorreta;
            while (dataManter.equals(dataCorreta)) {
                dataManter = listaAtual.get(RANDOM.nextInt(listaAtual.size()));
            }
        }

        for (int i = 0; i < 5; i++) {
            String novaData = dataCorreta;
            while (novaData.equalsIgnoreCase(dataCorreta) || novaData.equalsIgnoreCase(dataManter)) {
                dataRandom.set(LocalDate.now().getYear() - (RANDOM.nextInt(20) + 17), RANDOM.nextInt(12) + 1, RANDOM.nextInt(28) + 1);
                novaData = dataFormatada(dataRandom.getTime());
            }
            listaRetorno.add(novaData);
        }

        listaRetorno.set(posDataCorreta, dataCorreta);
        if (!dataManter.isEmpty()) {
            listaRetorno.set(posDataManter, dataManter);
        }
        return listaRetorno;
    }

    private List<String> geraAnosRandom(List<String> listaAtual, String anoCorreto) {
        List<String> listaRetorno = new ArrayList<>();
        int posAnoCorreto = RANDOM.nextInt(5);
        int posAnoManter = posAnoCorreto;
        String anoManter = "";

        if (listaAtual.size() > 0) {
            while (posAnoCorreto == posAnoManter) {
                posAnoManter = RANDOM.nextInt(5);
            }
            anoManter = anoCorreto;
            while (anoManter.equalsIgnoreCase(anoCorreto)) {
                anoManter = listaAtual.get(RANDOM.nextInt(listaAtual.size()));
            }
        }

        for (int i = 0; i < 5; i++) {
            String novoAno = anoCorreto;
            while (novoAno.equalsIgnoreCase(anoCorreto) || novoAno.equalsIgnoreCase(anoManter)) {
                novoAno = Integer.toString(Integer.valueOf(anoCorreto) + RANDOM.nextInt(11) - 5);
            }
            listaRetorno.add(novoAno);
        }

        listaRetorno.set(posAnoCorreto, anoCorreto);

        if (!anoManter.isEmpty()) {
            listaRetorno.set(posAnoManter, anoManter);
        }

        return listaRetorno;
    }

    public String dataFormatada(Date d) {
        SimpleDateFormat formatBra;
        formatBra = new SimpleDateFormat("dd/MM/yyyy");
        return formatBra.format(d);
    }

    public String confimarPrimeiroAcesso() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage msg;
        if (this.novaSenha.equals(this.novaSenhaConfirma)) {
            getUsuarioLogado().setSenha(novaSenha);
            getUsuarioLogado().setTimeStampUltimaUtilizacao(new Date());

//            if (!usuarioController.Salvar(getUsuarioLogado())) {
//                msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro de conexão com o banco", "");
//                contexto.addMessage("/login.xhtml", msg);
//                return "";
//            }
            if (!isUsuarioEgresso()) {
                Egresso egressoFake = new Egresso();
                egressoFake.setId(getUsuarioLogado().getId());
                egressoFake.setNomeOficial(getUsuarioLogado().getNomeSocial());
                egressoFake.setPapeis(getUsuarioLogado().getPapeis());
                egressoFake.setNomeSocial(getUsuarioLogado().getNomeSocial());
                egressoFake.setEmailPrincipal(getUsuarioLogado().getEmailPrincipal());
                egressoFake.setInstanciasAdministrativas(getUsuarioLogado().getInstanciasAdministrativas());
//                this.setEgresso(egressoFake);
            }
            return "/egressos/index.xhtml?faces-redirect=true";

        } else {
            this.novaSenha = "";
            this.novaSenhaConfirma = "";
            msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Senhas divergentes, informe senhas iguais!", "");
            contexto.addMessage("/login.xhtml", msg);
            return "";
        }
    }

    public void atualizarFotoPrincipal(FileUploadEvent event) throws FileNotFoundException {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
            String nomeArquivo = ConfiguracaoDePastas.getPASTA_FOTOS() + this.getUsuarioLogado().getId() + "_" + fmt.format(new Date()) + "_" + event.getFile().getFileName().trim();
            File diretorioAnexos = new File(ConfiguracaoDePastas.getPASTA_FOTOS());
            if (!diretorioAnexos.exists()) {
                diretorioAnexos.mkdir();
            }
            File file = new File(nomeArquivo);
            OutputStream out = new FileOutputStream(file);
            out.write(event.getFile().getContents());
            this.getUsuarioLogado().setFotoPessoal(nomeArquivo);
            this.setFotoExibicao(new DefaultStreamedContent(new ByteArrayInputStream(event.getFile().getContents())));

        } catch (IOException ex) {
            Logger.getLogger(EgressoBean.class.getName()).log(Level.SEVERE, "Erro upload foto egresso!", ex);
        }
    }

    public String pegarFotoPadrao() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath() + "/resources/img/person.png";
    }

    public String atualizar() {
        
        List<String> inconsistencias = 
                usuarioController.atualizar(getUsuarioLogado());
        if (inconsistencias.isEmpty()) {
            this.senha = null;
            mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, 
                    "Atualização realizada com sucesso!", 
                    "/egressos/atualizaDadosPessoais");
            return "";
        }
        for (String inconsistencia : inconsistencias) {
            mensagemDeTela.criar(FacesMessage.SEVERITY_ERROR, inconsistencia, "");
        }
        return "";
    }

    public String cancelarAtualizacao() {
//        egresso = null;
        this.senha = null;
        return "/egressos/listagemEgressos?faces-redirect=true";
    }

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

    public AssuntoEmailContato[] getAssuntosEmailContato() {
        return AssuntoEmailContato.values();
    }

    public void enviarEmailContato() {
        this.gerenciadorEmailController.enviarEmailContato(this.usuarioContato);
        mensagemDeTela.criar(FacesMessage.SEVERITY_INFO, "Mensagem enviada com sucesso!", "/contato");
        this.usuarioContato = new UsuarioContato();
    }

}
