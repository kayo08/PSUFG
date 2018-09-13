package br.ufg.inf.fabrica.sempreufg.dategenerator;

import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import br.ufg.inf.fabrica.sempreufg.dominio.Atuacao;
import br.ufg.inf.fabrica.sempreufg.dominio.AvaliacaoDoCursoPeloEgresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Banner;
import br.ufg.inf.fabrica.sempreufg.dominio.ConfiguracaoDePastas;
import br.ufg.inf.fabrica.sempreufg.dominio.CursoDaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.CursoDeOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoEmOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoIEM;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoNaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.InstituicaoEnsinoMedio;
import br.ufg.inf.fabrica.sempreufg.dominio.LocalizacaoGeografica;
import br.ufg.inf.fabrica.sempreufg.dominio.Organizacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Papel;
import br.ufg.inf.fabrica.sempreufg.dominio.RealizacaoDeProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.dominio.Recurso;
import br.ufg.inf.fabrica.sempreufg.dominio.RegionalUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.Residencia;
import br.ufg.inf.fabrica.sempreufg.dominio.UnidadeAcademica;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import br.ufg.inf.fabrica.sempreufg.enums.EscopoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.FormaIngressoInstituicao;
import br.ufg.inf.fabrica.sempreufg.enums.FrequenciaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import br.ufg.inf.fabrica.sempreufg.enums.ModalidadeCurso;
import br.ufg.inf.fabrica.sempreufg.enums.Motivacao;
import br.ufg.inf.fabrica.sempreufg.enums.NaturezaOrganizacao;
import br.ufg.inf.fabrica.sempreufg.enums.Nivel;
import br.ufg.inf.fabrica.sempreufg.enums.Sexo;
import br.ufg.inf.fabrica.sempreufg.enums.StatusAprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoEvento;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstancia;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstituicao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoResolucao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.enums.Turno;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeDados;
import br.ufg.inf.fabrica.sempreufg.services.FuncaoCPF;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Danillo
 */
public class DataGenerator {

    private static CursoDeOutraIES cursoDeOutraIES;
    private static Organizacao organizacao;
    private static final List<Organizacao> organizacaoLista = new ArrayList<>();
    private static final List<AreaConhecimento> areaConhecimentoLista = new ArrayList<>();
    private static final List<LocalizacaoGeografica> localizacaoLista = new ArrayList<>();
    private static final Integer NUM_SUBENTIDADES = 20;
    private static Integer NumEgressosCadastrados = 0;

    private static Egresso constroiEgresso(List<CursoDaUFG> cursos, Random random, String nome, List<String> nomesMulheres,
            List<String> sobrenomes, 
            LocalizacaoGeografica localizacao, EntityManager em) {

        Egresso egresso = new Egresso();
        HistoricoNaUFG historico = constroiHistorico(random, cursos);

        Calendar c = Calendar.getInstance();
        c.set(historico.getAnoDeFim() - 20, random.nextInt(11) + 1, random.nextInt(27) + 1);
        egresso.setDataDeNascimento(c.getTime());
        egresso.setEmailAlternativo(
                nome.trim().
                        replace(' ', '_').
                        toLowerCase() + "@dominio.com.br");
        egresso.setFotoPrincipal(
                nome.trim().
                        replace(' ', '_').
                        toLowerCase() + ".jpg");

        egresso.setNomeDaMae(
                RandomItem.getNomeCompletoDaMae(nomesMulheres, sobrenomes));
        egresso.setNomeOficial(nome);
        
        String ultimaLetraPrimeiroNome = egresso.getNomeOficial().substring(egresso.getNomeOficial().indexOf(" ") - 1, egresso.getNomeOficial().indexOf(" "));
        if (ultimaLetraPrimeiroNome.equals("A") || ultimaLetraPrimeiroNome.equals("E")) {
            egresso.setSexo(Sexo.FEMININO);
        } else {
            egresso.setSexo(Sexo.MASCULINO);
        }

        egresso.setVisibiliadeDeDados(RandomItem.getVisibilidade());
        egresso.setCpf(FuncaoCPF.geraCPF());
        egresso.setEmailPrincipal("email@unidade.com.br");
        egresso.setFotoPessoal("caminho/da/foto.jpg");
        egresso.setFrequenciaDivulgacao(FrequenciaDivulgacao.DIARIA);
        if (RandomItem.getInteiroPositivo() % 13 == 0) {
            egresso.setNomeSocial("Nome social");
        }
        egresso.setSenha("$2a$10$RjVUvft9.Y8x7l3vF.WUH.oTfAtrfXlXi2iUiOo8mPYV1tfDvmV5q");
        //A senha é a palavra "senha123" criptografada
        
        egresso.setTimeStampCadastramento(new Date());
        egresso.setNaturalidade(localizacao);
        if (RandomItem.getInteiroPositivo() % 17 == 0) {
            egresso.addResidencia(construirResidencia(RandomItem.getLocalizacaoGeografica(localizacaoLista)));
        }
        egresso.addResidencia(construirResidencia(localizacao));
        egresso.addHistoricoNaUFG(historico);
        egresso.addHistoricoEnsinoMedio(construirHistoricoEnsinoMedio());
        egresso.addHistoricoOutraIES(construirHistoricoOutrasIES());
        if (RandomItem.getInteiroPositivo() % 21 == 0) {
            egresso.addAtuacao(construirAtuacao());
        }
        egresso.addAtuacao(construirAtuacao());
        em.persist(egresso);

        return egresso;
    }

    private static HistoricoIEM construirHistoricoEnsinoMedio() {        
        Random r = new Random();
        InstituicaoEnsinoMedio iem = new InstituicaoEnsinoMedio();
        iem.setNome("Colégio ficticio para cadastro de teste");
        iem.setTipoInstituicao(RandomItem.getTipoInstituicao());
        iem.setLocalizacao(RandomItem.getLocalizacaoGeografica(localizacaoLista));

        HistoricoIEM hiem = new HistoricoIEM();
        hiem.setMesInicio(RandomItem.getMesesAno());
        hiem.setAnoInicio(r.nextInt(36) + 1980);
        hiem.setMesConclusao(RandomItem.getMesesAno());
        hiem.setAnoConclusao(hiem.getAnoInicio()+r.nextInt(3)+3);
        hiem.setInstituicaoEnsinoMedio(iem);
        return hiem;
    }

    private static HistoricoEmOutraIES construirHistoricoOutrasIES() {
        Random r = new Random();
        NumEgressosCadastrados++;
        String strNumReg = Integer.toString(NumEgressosCadastrados / 100);
        cursoDeOutraIES = new CursoDeOutraIES();
        cursoDeOutraIES.setLocalizacaoGeografica(RandomItem.getLocalizacaoGeografica(localizacaoLista));
        cursoDeOutraIES.getId().setNomeDoCurso("Curso " + Integer.toString(r.nextInt()) + " de outra IES ficticio");
        cursoDeOutraIES.getId().setIesDoCurso("Ies" + strNumReg + " padrão");
        cursoDeOutraIES.setTipoInstituicao(RandomItem.getTipoInstituicao());
        cursoDeOutraIES.setNomeDaUnidadeAcademica("Nome da Unidade Acadêmica padrão " + strNumReg);
        cursoDeOutraIES.setArea(areaConhecimentoLista.get(r.nextInt(areaConhecimentoLista.size() - 1)));
        cursoDeOutraIES.setUrlInstitucional("www.dominioies" + strNumReg + ".edu.br");
        cursoDeOutraIES.setNivel(RandomItem.getNivel());

        HistoricoEmOutraIES historicoEmOutraIES = new HistoricoEmOutraIES();
        historicoEmOutraIES.setCursoDeOutraIES(cursoDeOutraIES);
        historicoEmOutraIES.setMesInicio(RandomItem.getMesesAno());
        historicoEmOutraIES.setAnoInicio(r.nextInt(26) + 1990);
        historicoEmOutraIES.setMesFim(RandomItem.getMesesAno());
        historicoEmOutraIES.setAnoFim(historicoEmOutraIES.getAnoInicio() + r.nextInt(7));
        return historicoEmOutraIES;
    }

    private static Atuacao construirAtuacao() {
        Random random = new Random();
        Atuacao atuacao = new Atuacao();
        atuacao.setDataInicio(LocalDate.now());
        atuacao.setDataFim(LocalDate.now());
        atuacao.setFormaIngresso(RandomItem.getFormaIngressoInstituicao());
        atuacao.setRendaMensalMedia(random.nextInt(10000) + 3000);
        atuacao.setSatisfacaoRenda(random.nextInt(6) + 4);
        atuacao.setPerspectivaFuturoArea(random.nextInt(5) + 5);
        atuacao.setComentario("Comentário fictício para a atuação de  " + atuacao.getFormaIngresso().getValue());

        atuacao.setAreaConhecimento(areaConhecimentoLista.get(random.nextInt(areaConhecimentoLista.size() - 1)));
        atuacao.setOrganizacao(organizacaoLista.get(random.nextInt(organizacaoLista.size() - 1)));

        return atuacao;
    }

    public static void main(String[] args) throws IOException {
        executar(false);
        System.exit(0);
    }

    public static void executar(boolean dadosResumidos) throws IOException {
        Random r = new Random();

        List<String> nomesMulheres = DataGenerator.carregaArquivoDeNomesDeMulheres("");
        List<String> sobrenomes = DataGenerator.carregaArquivoDeSobrenomesMaisComuns();
        List<String> cursosUFG = DataGenerator.carregaArquivoDeNomesDeCursos();
        List<String> regionaisUFG = DataGenerator.carregaArquivoDeNomesDeRegionais();
        List<String> unidadesUFG = DataGenerator.carregaArquivoDeNomesDeUnidades();
        List<String> nomesCompletos = DataGenerator.carregaArquivosNomesCompletos(dadosResumidos);

        EntityManager em = getEM();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        cadastrarLocalidades(em);

        cadastrarAreasDeConhecimento(em);

        Recurso recurso = construirRecurso(em);
        List<Recurso> recursos = new ArrayList<>();
        recursos.add(recurso);
        List<Usuario> usuarios = new ArrayList<>();

        Papel papel = construirPapel(recursos, usuarios, em);

        Usuario usuarioCriadorCurso = construirUsuariosCriadorCursos(papel, usuarios, em);

        //Criar Instancia Administrativa tipo REGIONAL
        List<RegionalUFG> regionais = new ArrayList<>();
        for (String nomeRegional : regionaisUFG) {
            regionais.add(construirRegional(nomeRegional, usuarioCriadorCurso, em));
        }
        System.out.println("Regionais cadastradas com sucesso!");
        transaction.commit();

        transaction.begin();
        //Criar Instancia administrativa tipo UNIDADES
        List<UnidadeAcademica> unidades = new ArrayList<>();
        UnidadeAcademica unidade;
        RegionalUFG regional = new RegionalUFG();
        for (String nomeUnidade : unidadesUFG) {
            for (RegionalUFG reg : regionais) {
                if (nomeUnidade.contains(reg.getNome())) {
                    regional = reg;
                    break;
                }
            }
            List<CursoDaUFG> cursos = new ArrayList();
            unidade = construirUnidade(nomeUnidade, regional, cursos, usuarioCriadorCurso, em);
            unidades.add(unidade);
        }
        System.out.println("Unidades cadastradas com sucesso!");

        //Criar Instancia administrativa tipo CURSO        
        List<CursoDaUFG> cursos = new ArrayList<>();
        CursoDaUFG curso;

        for (String nome : cursosUFG) {
            curso = construirCurso(areaConhecimentoLista.get(r.nextInt(areaConhecimentoLista.size() - 1)), nome, usuarioCriadorCurso, em);
            cursos.add(curso);
        }
        System.out.println("Cursos cadastrados com sucesso!");

        //Cadastrar os Egressos
        Random random = new Random();
        int contador = 0;

        //Construir Organizações
        for (int i = 0; i < NUM_SUBENTIDADES; i++) {
            construirOrganizacao(em);
        }

        for (String nome : nomesCompletos) {
            Egresso egresso = constroiEgresso(cursos, random, nome,
                    nomesMulheres, sobrenomes, RandomItem.getLocalizacaoGeografica(localizacaoLista), em);
            contador++;
        }
        System.out.println("Egressos cadastrados com sucesso!!");

        //Construir Eventos
        for (int i = 0; i < NUM_SUBENTIDADES; i++) {
            construirEvento(em, cursos);
        }
        System.out.println("Eventos construidos com sucesso!!");

        //Construir banner
        construirBanner(em);
        System.out.println("Banner construido com sucesso!!");

        transaction.commit();
        System.out.println("###################################################");
        System.out.println("#.................................................#");
        System.out.println("#.........DADOS CADASTRADOS COM SUCESSO!..........#");
        System.out.println("#.................................................#");
        System.out.println("###################################################");
        em.close();
    }

    private static void construirOrganizacao(EntityManager em) {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVXWYZ";
        String nomeorg = alphabet.charAt(random.nextInt(alphabet.length())) + Integer.toString(random.nextInt(99)) + alphabet.charAt(random.nextInt(alphabet.length()));
        organizacao = new Organizacao();
        organizacao.setRazaoSocial("Organização " + nomeorg + " Ltda");
        organizacao.setEnderecoComercial("Endereço da organização n." + Integer.toString(random.nextInt(99)) + "");
        organizacao.setNatureza(RandomItem.getNaturezaOrganizacao());
        organizacao.setLocalizacao(RandomItem.getLocalizacaoGeografica(localizacaoLista));
        organizacao.setPaginaWeb("www.organizacao" + nomeorg + ".com.br");
        em.persist(organizacao);
        organizacaoLista.add(organizacao);
    }

    private static EntityManager getEM() {
        //Cadastrar os cursos na UFG
        EntityManager em
                = Persistence.createEntityManagerFactory("psufgPU").
                        createEntityManager();
        return em;
    }

    private static void construirEvento(EntityManager em, List<CursoDaUFG> cursos) {
        Random random = new Random();
        Evento evento = new Evento();
        evento.setAssunto("Assunto fictício [" + Integer.toString(random.nextInt(999)) + "]");
        evento.setTipoEvento(RandomItem.getTipoEvento());
        evento.setDescricaoEvento("Descrição fictício [" + Integer.toString(random.nextInt(999)) + "]");
        evento.setTimeStampDaSolicitacao(new Date());
        evento.setIdentificacaoSolicitante("Solicitante fictício");
        evento.setEmailSolicitante("ficticio@email.fic.edu.br");
        evento.setFormaDivulgacao(RandomItem.getFormaDivulgacao());
        evento.setEscopoDivulgacao(RandomItem.getEscopoDivulgacao());
        evento.setDataExpiracao(LocalDate.now());
        evento.setAreaConhecimento(new ArrayList());
        evento.getAreasDeConhecimento().add(areaConhecimentoLista.get(random.nextInt(areaConhecimentoLista.size() - 1)));
        evento.setInstanciasAdministrativas(new ArrayList());
        evento.getInstanciasAdministrativas().add(RandomItem.getCurso(cursos));
        evento.setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao.SEM_APROVACAO);
        em.persist(evento);
    }

    private static void construirBanner(EntityManager em) {
        Banner banner = new Banner();
        banner.setLinkNoticia("https://www.ufg.br/n/103923-ultimos-dias-para-manifestar-interesse-na-lista-de-espera-do-sisu");
        banner.setDestacar(true);
        banner.setPathImagem(ConfiguracaoDePastas.getPASTA_BANNERS() + "Banner-Sisu.png");
        banner.setTimeStampCriacao(new Date());
        banner.setTituloChamada("Colação de Grau 2017/2");
        em.persist(banner);
    }

    private static Residencia construirResidencia(LocalizacaoGeografica localizacao) {
        Random random = new Random();
        List<String> listaOpcoes = new ArrayList<>();
        Residencia residencia = new Residencia();

        listaOpcoes.addAll(Arrays.asList("Centro", "Setor Sul", "Ferroviario", "Vila Nova", "Serrinha", "Setor Oeste", "Jardim Goiás", "Vera Cruz", "Marista", "Universitário"));
        residencia.setBairro(listaOpcoes.get(random.nextInt(listaOpcoes.size() - 1)));

        residencia.setCEP("74" + Integer.toString((random.nextInt(500) + 300)) + "-" + Integer.toString((random.nextInt(900) + 99)));

        listaOpcoes.clear();
        listaOpcoes.addAll(Arrays.asList("Cond Planalto Goiania", "Praça da feira", "Bloco D", "Residencial Aurora", "Vila Feliz", "Alameda da flores", "Baixada Sul", "Viela dos macacos", "Território brasileiro", "Moco do Zé"));
        residencia.setComplemento(listaOpcoes.get(random.nextInt(listaOpcoes.size() - 1)));

        residencia.setDataDeIncio(LocalDate.now());
        residencia.setLocalizacaoGeografica(localizacao);

        listaOpcoes.clear();
        listaOpcoes.addAll(Arrays.asList("Rua 21", "Av. Goiás", "Al. Ricardo Paranhos", "Rua José de Alencar", "Av. 85 ", "Alameda da flores", "Rua do Machado", "Rua 10", "Av. das Flores", "Al. dos Jaburus"));
        residencia.setLogradouro(listaOpcoes.get(random.nextInt(listaOpcoes.size() - 1)));
        residencia.setNumero(Integer.toString(random.nextInt(1000) + 50));
        return residencia;
    }

    private static void constroiRealizacaoProgramasAcademicos(HistoricoNaUFG historico, Random random) {
        RealizacaoDeProgramaAcademico programaAcademico = new RealizacaoDeProgramaAcademico();
        programaAcademico.setDataDeInicio(
                LocalDate.of(
                        historico.getAnoDeInicio() + 2,
                        historico.getMesDeInicio().ordinal() + 1,
                        random.nextInt(27) + 1));
        programaAcademico.setDataDeFim(
                LocalDate.of(
                        historico.getAnoDeInicio() + 3,
                        historico.getMesDeInicio().ordinal() + 1,
                        random.nextInt(27) + 1));
        programaAcademico.setDescricao("Descrição do programa acadêmico");
        programaAcademico.setTipoProgramaAcademico(RandomItem.getTipoProgramaAcademico());
        programaAcademico.setHistorico(historico);

        historico.getRealizacaoProgramasAcademicos().add(programaAcademico);
    }

    private static void constroiAvaliacaoDoCurso(HistoricoNaUFG historico, Random random) {
        AvaliacaoDoCursoPeloEgresso avaliacao = new AvaliacaoDoCursoPeloEgresso();
        avaliacao.setCapacidadeEticaResponsabilidade(random.nextInt(2) + 8);
        avaliacao.setCapacidadeHabilidadesAreaConhecimento(random.nextInt(2) + 8);
        avaliacao.setComentario("Só elogios");
        avaliacao.setConceitoGlobal(random.nextInt(3) + 7);
        avaliacao.setHistoricoUFG(historico);
        avaliacao.setMelhoriaComunicacao(random.nextInt(5) + 5);
        avaliacao.setMotivacaoParaEscolha(RandomItem.getMotivacao());
        avaliacao.setPreparacaoMercado(random.nextInt(6) + 4);
        avaliacao.setSatisfacaoCurso(random.nextInt(3) + 7);
        avaliacao.setMomentoAvaliacao(
                LocalDate.of(
                        historico.getAnoDeInicio() + 1,
                        historico.getMesDeInicio().ordinal() + 1,
                        random.nextInt(27) + 1));
        historico.getAvaliacoes().add(avaliacao);
    }

    private static HistoricoNaUFG constroiHistorico(Random random, List<CursoDaUFG> cursos) {
        HistoricoNaUFG historico = new HistoricoNaUFG();
        historico.setAnoDeFim(2016 - random.nextInt(30));
        historico.setAnoDeInicio(historico.getAnoDeFim() - (4 + random.nextInt(2)));
        historico.setCursoDaUFG(RandomItem.getCurso(cursos));
        historico.setMesDeFim(RandomItem.getMesesAno());
        historico.setMesDeInicio(RandomItem.getMesesAno());
        historico.setNumeroDeMatricula(RandomItem.getInteiroPositivo());
        historico.setTituloDoTrabalhoFinal("Titulo de trabalho oficial");

        constroiAvaliacaoDoCurso(historico, random);
        constroiRealizacaoProgramasAcademicos(historico, random);

        return historico;
    }

    private static CursoDaUFG construirCurso(AreaConhecimento area, String nome, Usuario usuario, EntityManager em) {
        CursoDaUFG curso;
        curso = new CursoDaUFG();
        curso.setModalidadeCurso(RandomItem.getModalidade());
        curso.setNivel(RandomItem.getNivel());
        curso.setNumeroDaResolucao(RandomItem.getInteiroPositivo());
        curso.setTipoDeResolucao(RandomItem.getTipoResolucao());
        curso.setTurno(RandomItem.getTurno());
        curso.setArea(area);
        curso.setDataCriacao(LocalDate.now());
        curso.setEmailInstitucional("email@dominio.com.br");
        curso.setNome("Curso : " + nome.substring(0, nome.indexOf("<")));
        curso.setSigla(nome.substring(nome.indexOf("<") + 1, nome.indexOf(">")));
        curso.setTipoInstancia(TipoInstancia.CURSO);
        curso.setUrl("www.dominio.com.br");
        curso.setUsuario(usuario);
        String siglaUnidade = nome.substring(nome.indexOf("<") + 1, nome.indexOf(">"));
        Query q = em.createQuery("SELECT u FROM UnidadeAcademica u WHERE u.sigla LIKE :siglaU");
        q.setParameter("siglaU", siglaUnidade);
        UnidadeAcademica u = (UnidadeAcademica) q.getResultList().get(0);
        curso.setUnidadeAcademica(u);
        em.persist(curso);
        return curso;
    }

    private static RegionalUFG construirRegional(String nomeRegional, Usuario usuario, EntityManager em) {
        RegionalUFG regional;
        regional = new RegionalUFG();
        regional.setDataCriacao(LocalDate.now());
        regional.setEmailInstitucional("regional-email@ufg.br");
        regional.setLocalizacao(RandomItem.getLocalizacaoGeografica(localizacaoLista));
        regional.setTipoInstancia(TipoInstancia.REGIONAL);
        regional.setSigla(nomeRegional.substring(0, 3));
        regional.setUrl("www.ufg.br");
        regional.setNome("Regional " + nomeRegional);
        regional.setUsuario(usuario);
        em.persist(regional);
        return regional;
    }

    private static UnidadeAcademica construirUnidade(String nomeUnidade, RegionalUFG regionalUFG, List<CursoDaUFG> cursos, Usuario usuario, EntityManager em) {
        UnidadeAcademica unidade;
        unidade = new UnidadeAcademica();
        unidade.setCursos(cursos);
        unidade.setDataCriacao(LocalDate.now());
        unidade.setDataEncerramento(null);
        unidade.setEmailInstitucional("unidade-email@ufg.br");
        unidade.setImportacao(null);
        unidade.setLocalizacaoGeografica(RandomItem.getLocalizacaoGeografica(localizacaoLista));
        unidade.setNome("Unidade : " + nomeUnidade.substring(nomeUnidade.indexOf("-") + 2, nomeUnidade.indexOf("[")));
        unidade.setRegionalUFG(regionalUFG);
        unidade.setSigla(nomeUnidade.substring(nomeUnidade.indexOf("[") + 1, nomeUnidade.indexOf("]")));
        unidade.setTipoInstancia(TipoInstancia.REGIONAL);
        unidade.setUrl("unidade.ufg.br");
        unidade.setUsuario(usuario);
        em.persist(unidade);
        return unidade;
    }

    private static Usuario construirUsuariosCriadorCursos(Papel papel, List<Usuario> usuarios, EntityManager em) {
        Usuario usuario = new Usuario();
        usuario.setCpf("11111111111");
        usuario.setEmailPrincipal("email@unidade.com.br");
        usuario.setFotoPessoal("caminho/da/foto.jpg");
        usuario.setFrequenciaDivulgacao(FrequenciaDivulgacao.DIARIA);
        usuario.setNomeSocial("Usuário teste Administrador");
        List<Papel> papeis = new ArrayList<>();
        usuario.setPapeis(papeis);
        papeis.add(papel);
        usuario.setSenha("$2a$10$RjVUvft9.Y8x7l3vF.WUH.oTfAtrfXlXi2iUiOo8mPYV1tfDvmV5q");
        //A senha é a palavra "senha123" criptografada
        usuario.setTimeStampCadastramento(new Date());
        em.persist(usuario);
        usuarios.add(usuario);
        return usuario;
    }

    private static Papel construirPapel(List<Recurso> recursos, List<Usuario> usuarios, EntityManager em) {
        Papel papel = new Papel();
        papel.setNome("Administrador do Sistema");
        papel.setRecursos(recursos);
        papel.setSigla("AdmSis");
        papel.setUsuarios(usuarios);
        em.persist(papel);
        return papel;
    }

    private static Recurso construirRecurso(EntityManager em) {
        Recurso recurso = new Recurso();
        recurso.setDescricao("Descricao do recurso");
        recurso.setSigla("sigla recurso");
        em.persist(recurso);
        return recurso;
    }

    private static AreaConhecimento construirAreaConhecimento(EntityManager em) {
        AreaConhecimento area = new AreaConhecimento();
        area.setCodArea(55);
        area.setNomeArea("area");
        em.persist(area);
        return area;
    }

    public static List<String> carregaArquivoDeNomesDeMulheres(String caminho)
            throws FileNotFoundException, IOException {
        List<String> nomesMulheres = new ArrayList<>();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("files/listadeNomesDeMulheres");
        BufferedReader in = getFileReader(resource);
        while (in.ready()) {
            nomesMulheres.add(in.readLine());
        }
        return nomesMulheres;
    }

    public static List<String> carregaArquivoDeSobrenomesMaisComuns()
            throws FileNotFoundException, IOException {
        URL resource
                = Thread.currentThread().getContextClassLoader().
                        getResource("files/sobrenomesMaisComuns");
        BufferedReader in = getFileReader(resource);
        List<String> sobreNomes = new ArrayList<>();
        while (in.ready()) {
            sobreNomes.add(in.readLine());
        }
        return sobreNomes;
    }

    public static List<String> carregaArquivoDeNomesDeCursos() throws FileNotFoundException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("files/listaDeCursosUfg");
        BufferedReader in = getFileReader(resource);
        List<String> nomesCursosUfg = new ArrayList<>();
        while (in.ready()) {
            nomesCursosUfg.add(in.readLine());
        }
        return nomesCursosUfg;
    }

    public static List<String> carregaArquivoDeNomesDeRegionais() throws FileNotFoundException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("files/listaDeRegionaisUfg");
        BufferedReader in = getFileReader(resource);
        List<String> nomesRegionaisUfg = new ArrayList<>();
        while (in.ready()) {
            nomesRegionaisUfg.add(in.readLine());
        }
        return nomesRegionaisUfg;
    }

    public static List<String> carregaArquivoDeNomesDeUnidades() throws FileNotFoundException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("files/listaDeUnidadesAcademicasUfg");
        BufferedReader in = getFileReader(resource);
        List<String> nomesUnidadesUfg = new ArrayList<>();
        while (in.ready()) {
            nomesUnidadesUfg.add(in.readLine());
        }
        return nomesUnidadesUfg;
    }

    public static List<String> carregaArquivosNomesCompletos(boolean dadosResumidos) throws FileNotFoundException, IOException {
        URL resource;
        if(dadosResumidos){
            resource = Thread.currentThread().getContextClassLoader().getResource("files/listaNomesCompletos_resumo");
        } else {
            resource = Thread.currentThread().getContextClassLoader().getResource("files/listaNomesCompletos");
        }
        BufferedReader in = getFileReader(resource);
        List<String> nomesFicticiosEgressos = new ArrayList<>();
        while (in.ready()) {
            nomesFicticiosEgressos.add(in.readLine());
        }
        return nomesFicticiosEgressos;
    }

    private static void cadastrarLocalidades(EntityManager em) throws FileNotFoundException, IOException {
        URL resource
                = Thread.currentThread().
                        getContextClassLoader().
                        getResource("files/listaLocalidades");
        BufferedReader in = getFileReader(resource);

        Map<String, String> estados = new HashMap();
        estados.put("ACRE", "AC");
        estados.put("ALAGOAS", "AL");
        estados.put("AMAPÁ", "AP");
        estados.put("AMAZONAS", "AM");
        estados.put("BAHIA", "BA");
        estados.put("CEARÁ", "CE");
        estados.put("DISTRITO FEDERAL", "DF");
        estados.put("ESPÍRITO SANTO", "ES");
        estados.put("GOIÁS", "GO");
        estados.put("MARANHÃO", "MA");
        estados.put("MATO GROSSO", "MT");
        estados.put("MATO GROSSO DO SUL", "MS");
        estados.put("MINAS GERAIS", "MG");
        estados.put("PARÁ", "PA");
        estados.put("PARÃ?", "PA");
        estados.put("PARAÍBA", "PB");
        estados.put("PARAÃ?BA", "PB");
        estados.put("PARANÁ", "PR");
        estados.put("PARANÃ?", "PR");
        estados.put("PERNAMBUCO", "PE");
        estados.put("PIAUÍ", "PI");
        estados.put("PIAUÃ?", "PI");
        estados.put("RIO DE JANEIRO", "RJ");
        estados.put("RIO GRANDE DO NORTE", "RN");
        estados.put("RIO GRANDE DO SUL", "RS");
        estados.put("RONDÔNIA", "RO");
        estados.put("RONDÃ”NIA", "RO");
        estados.put("RORAIMA", "RR");
        estados.put("SANTA CATARINA", "SC");
        estados.put("SÃO PAULO", "SP");
        estados.put("SÃ?O PAULO", "SP");
        estados.put("SERGIPE", "SE");
        estados.put("TOCANTINS", "TO");

        while (in.ready()) {
            try {
                LocalizacaoGeografica l = new LocalizacaoGeografica();
                String[] linha = in.readLine().split(",");
                l.setReferencia("IBGE");
                l.setPais("BRASIL");
                l.setCodRef(linha[0]);
                l.setMunicipio(linha[1]);
                l.setUf(linha[2].toUpperCase());
                l.setCidade(linha[3].toUpperCase());
                l.setLatitude(Float.valueOf(linha[4]));
                l.setLongitude(Float.valueOf(linha[5]));
                l.setSiglaUF(estados.get(l.getUf()));
                if (l.getSiglaUF() == null || l.getSiglaUF().isEmpty()) {

                    if (l.getUf().startsWith("AMAP")) {
                        l.setSiglaUF("AP");
                    } else if (l.getUf().startsWith("CEAR")) {
                        l.setSiglaUF("CE");
                    } else if (l.getUf().startsWith("ESP")) {
                        l.setSiglaUF("ES");
                    } else if (l.getUf().startsWith("GOI")) {
                        l.setSiglaUF("GO");
                    } else if (l.getUf().startsWith("MARA")) {
                        l.setSiglaUF("MA");
                    } else if (l.getUf().startsWith("PARAN")) {
                        l.setSiglaUF("PR");
                    } else if (l.getUf().startsWith("PAR") && l.getUf().endsWith("BA")) {
                        l.setSiglaUF("PB");
                    } else if (l.getUf().startsWith("PAR")) {
                        l.setSiglaUF("PA");
                    } else if (l.getUf().startsWith("PIAU")) {
                        l.setSiglaUF("PI");
                    } else if (l.getUf().startsWith("ROND")) {
                        l.setSiglaUF("RO");
                    } else if (l.getUf().endsWith("PAULO")) {
                        l.setSiglaUF("SP");
                    } else {
                        System.out.println(l.getUf() + " === " + l.getSiglaUF());
                    }
                }
                em.persist(l);
                localizacaoLista.add(l);
            } catch (Exception ex) {
                Logger.getLogger(DataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void cadastrarAreasDeConhecimento(EntityManager em)
            throws FileNotFoundException, IOException {
        URL resource
                = Thread.currentThread().
                        getContextClassLoader().
                        getResource("files/listaAreasConhecimento");
        BufferedReader in = getFileReader(resource);

        AreaConhecimento ac1 = null;
        AreaConhecimento ac2 = null;
        while (in.ready()) {
            try {
                //Captação da informação
                String linha = in.readLine();
                String[] partes = linha.split("--");
                int nivel = partes[0].length();
                String cod = partes[1];
                String nome = partes[2];

                AreaConhecimento area = new AreaConhecimento();
                area.setCodArea(Integer.parseInt(cod));
                area.setNomeArea(nome);
                switch (nivel) {
                    case 1:
                        if (ac1 != null) {
                            em.persist(ac1);
                            System.out.println(ac1.getNomeArea() + ": "
                                    + ac1.getSubAreas().size());
                            areaConhecimentoLista.add(ac1);
                        }
                        area.setAreaPai(null);
                        ac1 = area;
                        break;
                    case 2:
                        ac2 = area;
                        ac1.addSubArea(area);
                        break;
                    case 3:
                        ac2.addSubArea(area);
                        break;
                    default:
                        break;
                }

            } catch (Exception ex) {
                Logger.getLogger(DataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (ac1 != null) {
            em.persist(ac1);
            System.out.println(ac1.getNomeArea() + ": "
                    + ac1.getSubAreas().size());
        }
        areaConhecimentoLista.add(ac1);
    }

    private static BufferedReader getFileReader(URL resource) throws
            FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(
                new InputStreamReader(new FileInputStream(resource.getFile()), "UTF8")
        );
    }

}

class RandomItem {

    private static final ModalidadeCurso[] MODALIDADES_DE_CURSO = ModalidadeCurso.values();
    private static final Nivel[] NIVEIS = Nivel.values();
    private static final TipoResolucao[] TIPOS_DE_RESOLUCAO = TipoResolucao.values();
    private static final Turno[] TURNOS = Turno.values();
    private static final Motivacao[] MOTIVACOES = Motivacao.values();
    private static final TipoProgramaAcademico[] TIPOS_DE_PROGRAMA_ACADEMICO = TipoProgramaAcademico.values();
    private static final Sexo[] SEXOS = Sexo.values();
    private static final VisibilidadeDados[] VISIBILIDADES_DE_DADOS = VisibilidadeDados.values();
    private static final TipoEvento[] TIPOS_EVENTO = TipoEvento.values();
    private static final FormaDivulgacao[] FORMAS_DIVULGACAO = FormaDivulgacao.values();
    private static final EscopoDivulgacao[] ESCOPOS_DIVULGACAO = EscopoDivulgacao.values();
    private static final StatusAprovacaoDivulgacao[] STATUS_APROVACAO_DIVULGACAO = StatusAprovacaoDivulgacao.values();
    private static final NaturezaOrganizacao[] NATUREZA_ORGANIZACAO = NaturezaOrganizacao.values();
    private static final MesesAno[] MESES_ANO = MesesAno.values();
    private static final TipoInstituicao[] TIPO_INSTITUICAO = TipoInstituicao.values();
    private static final FormaIngressoInstituicao[] FORMA_INGRESSO_INSTITUICAO = FormaIngressoInstituicao.values();

    private static final Random RANDOM = new Random();

    public static ModalidadeCurso getModalidade() {
        int indice = MODALIDADES_DE_CURSO.length - 1;
        return MODALIDADES_DE_CURSO[RANDOM.nextInt(indice)];
    }

    public static Nivel getNivel() {
        int indice = NIVEIS.length - 1;
        return NIVEIS[RANDOM.nextInt(indice)];
    }

    public static TipoResolucao getTipoResolucao() {
        int indice = TIPOS_DE_RESOLUCAO.length - 1;
        return TIPOS_DE_RESOLUCAO[RANDOM.nextInt(indice)];
    }

    public static Turno getTurno() {
        int indice = TURNOS.length - 1;
        return TURNOS[RANDOM.nextInt(indice)];
    }

    public static Motivacao getMotivacao() {
        int indice = MOTIVACOES.length - 1;
        return MOTIVACOES[RANDOM.nextInt(indice)];
    }

    public static TipoProgramaAcademico getTipoProgramaAcademico() {
        int indice = TIPOS_DE_PROGRAMA_ACADEMICO.length - 1;
        return TIPOS_DE_PROGRAMA_ACADEMICO[RANDOM.nextInt(indice)];
    }

    public static Sexo getSexo() {
        int indice = SEXOS.length - 1;
        return SEXOS[RANDOM.nextInt(indice)];
    }

    public static VisibilidadeDados getVisibilidade() {
        int indice = VISIBILIDADES_DE_DADOS.length - 1;
        return VISIBILIDADES_DE_DADOS[RANDOM.nextInt(indice)];
    }

    public static TipoEvento getTipoEvento() {
        int indice = TIPOS_EVENTO.length - 1;
        return TIPOS_EVENTO[RANDOM.nextInt(indice)];
    }

    public static FormaDivulgacao getFormaDivulgacao() {
        int indice = FORMAS_DIVULGACAO.length - 1;
        return FORMAS_DIVULGACAO[RANDOM.nextInt(indice)];
    }

    public static EscopoDivulgacao getEscopoDivulgacao() {
        int indice = ESCOPOS_DIVULGACAO.length - 1;
        return ESCOPOS_DIVULGACAO[RANDOM.nextInt(indice)];
    }

    public static StatusAprovacaoDivulgacao getStatusAprovacaoDivulgacao() {
        int indice = STATUS_APROVACAO_DIVULGACAO.length - 1;
        return STATUS_APROVACAO_DIVULGACAO[RANDOM.nextInt(indice)];
    }

    public static NaturezaOrganizacao getNaturezaOrganizacao() {
        int indice = NATUREZA_ORGANIZACAO.length - 1;
        return NATUREZA_ORGANIZACAO[RANDOM.nextInt(indice)];
    }

    public static MesesAno getMesesAno() {
        int indice = MESES_ANO.length - 1;
        return MESES_ANO[RANDOM.nextInt(indice)];
    }

    public static TipoInstituicao getTipoInstituicao() {
        int indice = TIPO_INSTITUICAO.length - 1;
        return TIPO_INSTITUICAO[RANDOM.nextInt(indice)];
    }

    public static FormaIngressoInstituicao getFormaIngressoInstituicao() {
        int indice = FORMA_INGRESSO_INSTITUICAO.length - 1;
        return FORMA_INGRESSO_INSTITUICAO[RANDOM.nextInt(indice)];
    }

    public static CursoDaUFG getCurso(List<CursoDaUFG> cursos) {
        int indice = cursos.size() - 1;
        return cursos.get(RANDOM.nextInt(indice));
    }

    public static LocalizacaoGeografica getLocalizacaoGeografica(List<LocalizacaoGeografica> localizacaoLista) {
        int indice = localizacaoLista.size() - 1;
        return localizacaoLista.get(RANDOM.nextInt(indice));
    }

    public static int getInteiroPositivo() {
        int i = RANDOM.nextInt();
        if (i < 0) {
            return i * (-1);
        }
        return i;
    }

    public static String getNomeCompletoDaMae(List<String> nomesMae, List<String> sobrenomes) {
        int indiceNome = nomesMae.size();
        int indiceSobrenome = sobrenomes.size();
        StringBuilder sb = new StringBuilder();
        sb.append(nomesMae.get(RANDOM.nextInt(indiceNome))).append(" ").
                append(sobrenomes.get(RANDOM.nextInt(indiceSobrenome))).append(" ").
                append(sobrenomes.get(RANDOM.nextInt(indiceSobrenome)));
        return sb.toString();
    }

    public static String getNomeOficial(List<String> nomesOficiais) {
        int size = nomesOficiais.size();
        return nomesOficiais.get(RANDOM.nextInt(size));
    }

    private static AreaConhecimento construirAreaConhecimento(EntityManager em) {
        AreaConhecimento area = new AreaConhecimento();
        area.setCodArea(55);
        area.setNomeArea("area");
        em.persist(area);
        return area;
    }

}
