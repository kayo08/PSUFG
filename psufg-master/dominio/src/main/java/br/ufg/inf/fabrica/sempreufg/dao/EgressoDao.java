package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Danillo
 */
public class EgressoDao extends GenericDao<Egresso> implements IEntityDao<Egresso> {

    private final Map<String, CampoDaConsulta> camposConsultaMap = new TreeMap<>();
    public List<String> regrasDeFiltro = new ArrayList<>();

    public EgressoDao() {
        construirCamposConsulta();
    }

    //=======CONSULTAS==========================================================
    @Override
    public Egresso buscar(Long id) {
        return super.buscar(Egresso.class, id);
    }

    @Override
    public List<Egresso> buscarTodos() {
        return super.buscarTodos(Egresso.class);
    }

    public Egresso buscarTodosOsDadosDoEgresso(Long idEgresso) {
        EntityManager em = ConnectionFactory.obterManagerNova();
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("select distinct u.* ").
                append("from Usuario u ").
                append("inner join HistoricoNaUFG hu on u.id = hu.`egresso_id` ").
                append("inner join InstanciaAdministrativaUFG cur on hu.`cursoDaUFG_id` = cur.`id` and cur.`DTYPE` like 'CUR' ").
                append("inner join Residencia res ON u.id = res.`egresso_id` ").
                append("inner join LocalizacaoGeografica lg ON res.`localizacaoGeografica_id` = lg.`id` ").
                append("where u.`DTYPE` like 'EGR' and u.id = ").append(idEgresso);

        Query query = em.createNativeQuery(sbSQL.toString(), Egresso.class);
        return (Egresso) query.getSingleResult();

    }

    public int consultarNumeroRegistros(FiltroConsultaLazy filtros) {
        StringBuilder query = new StringBuilder();
        definirJpqlBaseDoSelect(filtros, query);
        query.replace(query.indexOf(" ") + 1, query.indexOf(" ") + 1, "COUNT(");
        query.replace(query.indexOf(" FROM"), query.indexOf(" FROM"), ")");

        definirJpqlTabelaDoFiltro(filtros, query);
        definirJpqlClausulaWhere(filtros, query);

        return super.pesquisarJPQLCustomizadaNumeroDeRegistros(query.toString());
    }

    public List<DadosMapa> consultarLocaisMapa(FiltroConsultaLazy filtros) {
        StringBuilder queryMapa = new StringBuilder();
        definirJpqlBaseDoMapa(filtros, queryMapa);
        definirJpqlTabelaDoFiltro(filtros, queryMapa);
        definirJpqlClausulaWhere(filtros, queryMapa);
        definirGroupByDoMapa(filtros, queryMapa);
        return super.consultarLocaisMapa(queryMapa.toString());
    }

    public List consultarJpqlLazyComFiltros(FiltroConsultaLazy filtros) {
        String queryJpql = construirConsultaJpql(filtros);
        return super.pesquisarJPQLLazyCustomizada(filtros.getClassePrincipalDaConsulta(), queryJpql, filtros.getPrimeiroRegistro(), filtros.getQuantidadeRegistros());
    }
            
    //=======METODOS PARA CONSTRUÇÃO DA CONSULTA================================
    public String construirConsultaJpql(FiltroConsultaLazy filtros) {
        StringBuilder query = new StringBuilder();
        definirJpqlBaseDoSelect(filtros, query);
        definirJpqlTabelaDoFiltro(filtros, query);
        definirJpqlClausulaWhere(filtros, query);
        definirJpqlOrdenacao(filtros, query);
        return query.toString();
    }

    private void definirJpqlBaseDoSelect(FiltroConsultaLazy filtros, StringBuilder query) {

        switch (filtros.getClassePrincipalDaConsulta().getSimpleName()) {
            case "Residencia":
                query.append("SELECT r FROM Residencia r ")
                        .append("JOIN r.egresso e");
                break;
            case "HistoricoIEM":
                query.append("SELECT em FROM HistoricoIEM em ")
                        .append("JOIN em.egresso e");
                break;
            case "HistoricoNaUFG":
                query.append("SELECT hufg FROM HistoricoNaUFG hufg ")
                        .append("JOIN hufg.egresso e");
                break;
            case "AvaliacaoDoCursoPeloEgresso":
                query.append("SELECT aval FROM AvaliacaoDoCursoPeloEgresso aval ")
                        .append("JOIN aval.historicoUFG.egresso e");
                break;
            case "RealizacaoDeProgramaAcademico":
                query.append("SELECT acad FROM RealizacaoDeProgramaAcademico acad ")
                        .append("JOIN acad.historico.egresso e");
                break;
            case "HistoricoEmOutraIES":
                query.append("SELECT hoies FROM HistoricoEmOutraIES hoies ")
                        .append("JOIN hoies.egresso e");
                break;
            case "Atuacao":
                query.append("SELECT aprof FROM Atuacao aprof ")
                        .append("JOIN aprof.egresso e ");
                break;
            default:
                query.append("SELECT e FROM Egresso e");
                break;
        }
    }

    private void definirJpqlTabelaDoFiltro(FiltroConsultaLazy filtros, StringBuilder query) {
        boolean primeiroFiltroAtuacao = true;
        boolean primeiroFiltroResidencia = true;
        boolean primeiroFiltroAvaliacao = true;
        boolean primeiroFiltroHistUFG = true;
        boolean primeiroFiltroHistOIES = true;
        boolean primeiroFiltroHistMedio = true;
        boolean primeiroFiltroProgramaAcademico = true;

        for (FiltroConsulta filtro : filtros.getFiltrosConsulta()) {
            if (filtro.getCampo().contains("AtP") && podeIncluirTabelaJoin(primeiroFiltroAtuacao, "aprof.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.atuacoes aprof ");
                primeiroFiltroAtuacao = false;
            } else if (filtro.getCampo().contains("AvC") && podeIncluirTabelaJoin(primeiroFiltroAvaliacao, "aval.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.historicosNaUFG hufg ")
                        .append(" JOIN hufg.avaliacoes aval ");
                primeiroFiltroAvaliacao = false;
            } else if (filtro.getCampo().contains("UFG") && podeIncluirTabelaJoin(primeiroFiltroHistUFG, "hufg.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.historicosNaUFG hufg ");
                primeiroFiltroHistUFG = false;
            } else if (filtro.getCampo().contains("EnM") && podeIncluirTabelaJoin(primeiroFiltroHistMedio, "em.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.historicosEnsinoMedio em ");
                primeiroFiltroHistMedio = false;
            } else if (filtro.getCampo().contains("IES") && podeIncluirTabelaJoin(primeiroFiltroHistOIES, "hoies.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.historicosEmOutrasIES hoies ");
                primeiroFiltroHistOIES = false;
            } else if (filtro.getCampo().contains("PrA") && podeIncluirTabelaJoin(primeiroFiltroProgramaAcademico, "acad.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.historicosNaUFG hufg ")
                        .append(" JOIN hufg.realizacaoProgramasAcademicos acad ");
                primeiroFiltroProgramaAcademico = false;
            } else if (filtro.getCampo().contains("Res") && podeIncluirTabelaJoin(primeiroFiltroResidencia, "r.", filtros.getPrefixoDaClassePrincipal())) {
                query.append(" JOIN e.residencias r ");
                primeiroFiltroResidencia = false;
            }
        }
    }

    private void definirJpqlClausulaWhere(FiltroConsultaLazy filtros, StringBuilder query) {
        if (filtros.getFiltrosConsulta() != null && !filtros.getFiltrosConsulta().isEmpty()) {
            query.append(" WHERE ");
            for (FiltroConsulta filtro : filtros.getFiltrosConsulta()) {
                query.append(filtro.getOperador())
                        .append(" ( ");
                if (camposConsultaMap.get(filtro.getCampo()).getTipoCampo().equals("data")) {
                    query.append("DATE (")
                            .append(camposConsultaMap.get(filtro.getCampo()).getJpqlCampo())
                            .append(")");
                } else {
                    query.append(camposConsultaMap.get(filtro.getCampo()).getJpqlCampo());
                }
                query.append(formataRegraValor(filtro, camposConsultaMap.get(filtro.getCampo()).tipoCampo))
                        .append(" ) ");
            };
        }
    }

    private void definirJpqlOrdenacao(FiltroConsultaLazy filtros, StringBuilder query) {
        if (filtros.getPropriedadeOrdenacao() != null && !filtros.getPropriedadeOrdenacao().isEmpty()) {
            query.append(" ORDER BY ").append(filtros.getPrefixoDaClassePrincipal()).append(filtros.getPropriedadeOrdenacao());
        } else {
            query.append(" ORDER BY e.nomeOficial ");
        }
        if (!filtros.isAscendente()) {
            query.append(" DESC ");
        }
    }

    private boolean podeIncluirTabelaJoin(boolean primeiraFiltroDessaTabela, String prefixoEsperado, String prefixoConsultado) {
        return primeiraFiltroDessaTabela && !prefixoEsperado.equalsIgnoreCase(prefixoConsultado);
    }

    //=======METODOS PARA FORMATAÇÃO DOS FILTROS (cláusula WHERE)===============
    public List<String> obterRegrasDeFiltro(String campo) throws ClassNotFoundException {
        this.construirRegrasDeFiltro(campo);
        return regrasDeFiltro;
    }

    public boolean campoEhTipoEnum(String campo) {
        return campo == null ? false : camposConsultaMap.get(campo).tipoCampo.equalsIgnoreCase("enum");
    }

    public String descobrirTipoCampo(String campo) {
        return campo == null ? "" : camposConsultaMap.get(campo).tipoCampo;
    }

    public String recuperarChaveEnum(String campo, String valor) {
        String nomeEnum = camposConsultaMap.get(campo).nomeEnum;
        try {
            Class<?> k = Class.forName("br.ufg.inf.fabrica.sempreufg.enums." + nomeEnum);
            for (Object enumConstant : k.getEnumConstants()) {
                Method m = k.getMethod("getValue", null);
                String v = (String) m.invoke(enumConstant, null);
                if (v.equalsIgnoreCase(valor)) {
                    Enum e = (Enum) enumConstant;
                    return Integer.toString(e.ordinal());
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EgressoDao.class.getName()).log(Level.SEVERE, "Erro ao criar RegraDeFiltro para o Enum selecionado", "");
        }
        return "";
    }

    private Map<String, CampoDaConsulta> getCamposConsultaMap() {
        return camposConsultaMap;
    }

    public List<String> camposConsulta() {
        List<String> listCampos = new ArrayList<>(this.getCamposConsultaMap().keySet());
        return listCampos;
    }

    private void construirCamposConsulta() {

        camposConsultaMap.put("Egr : CPF", new CampoDaConsulta("tex", "e.cpf", ""));
        camposConsultaMap.put("Egr : Sexo", new CampoDaConsulta("enum", "e.sexo", "Sexo"));
        camposConsultaMap.put("Egr : Data nascimento", new CampoDaConsulta("data", "e.dataDeNascimento", ""));
        camposConsultaMap.put("Egr : Email Principal", new CampoDaConsulta("tex", "e.emailPrincipal", ""));
        camposConsultaMap.put("Egr : Email alternativo", new CampoDaConsulta("tex", "e.emailAlternativo", ""));
        camposConsultaMap.put("Egr : Nome social", new CampoDaConsulta("tex", "e.nomeSocial", ""));
        camposConsultaMap.put("Egr : Nome da mãe", new CampoDaConsulta("tex", "e.nomeDaMae", ""));
        camposConsultaMap.put("Egr : Nome oficial", new CampoDaConsulta("tex", "e.nomeOficial", ""));
        camposConsultaMap.put("Egr : Natural da cidade", new CampoDaConsulta("tex", "e.naturalidade.cidade", ""));
        camposConsultaMap.put("Egr : País de nascimento", new CampoDaConsulta("tex", "e.naturalidade.pais", ""));
        camposConsultaMap.put("Egr : Sigla da UF de nascimento", new CampoDaConsulta("tex", "e.naturalidade.siglaUF", ""));
        camposConsultaMap.put("Egr : UF de nascimento", new CampoDaConsulta("tex", "e.naturalidade.uf", ""));
//        camposConsultaMap.put("Egr : Data cadastro", new CampoDaConsulta("data", "e.timeStampCadastramento",""));
//        camposConsultaMap.put("Egr : Data exclusão", new CampoDaConsulta("data", "e.timeStampExclusaoLogica",""));
//        camposConsultaMap.put("Egr : Data último acesso", new CampoDaConsulta("data", "e.timeStampUltimaUtilizacao",""));
//        camposConsultaMap.put("Egr : Frequência divulgação", new CampoDaConsulta("enum", "e.frequenciaDivulgacao","FrequenciaDivulgacao"));
//        camposConsultaMap.put("Egr : Visibilidade de dados", new CampoDaConsulta("enum", "e.visibiliadeDeDados","VisibilidadeDados"));
//        camposConsultaMap.put("Egr : Município de nascimento", new CampoDaConsulta("tex", "e.naturalidade.municipio", ""));
//        camposConsultaMap.put("Egr : Código referência da cidade nascimento", new CampoDaConsulta("tex", "e.naturalidade.codRef"));
//        camposConsultaMap.put("Egr : Latitude da cidade nascimento", new CampoDaConsulta("num", "e.naturalidade.latitude"));
//        camposConsultaMap.put("Egr : Longitude da cidade nascimento", new CampoDaConsulta("num", "e.naturalidade.longitude"));
//        camposConsultaMap.put("Egr : Referência do local de nascimento", new CampoDaConsulta("tex", "e.naturalidade.referencia"));

        camposConsultaMap.put("Res : CEP", new CampoDaConsulta("tex", "r.cep", ""));
        camposConsultaMap.put("Res : Bairro", new CampoDaConsulta("tex", "r.bairro", ""));
        camposConsultaMap.put("Res : Complemento", new CampoDaConsulta("tex", "r.complemento", ""));
        camposConsultaMap.put("Res : Data de fim", new CampoDaConsulta("data", "r.dataDeFim", ""));
        camposConsultaMap.put("Res : Data de início", new CampoDaConsulta("data", "r.dataDeInicio", ""));
        camposConsultaMap.put("Res : Logradouro", new CampoDaConsulta("tex", "r.logradouro", ""));
        camposConsultaMap.put("Res : Número", new CampoDaConsulta("tex", "r.numero", ""));
        camposConsultaMap.put("Res : Cidade", new CampoDaConsulta("tex", "r.localizacaoGeografica.cidade", ""));
        camposConsultaMap.put("Res : País", new CampoDaConsulta("tex", "r.localizacaoGeografica.pais", ""));
        camposConsultaMap.put("Res : Sigla da UF", new CampoDaConsulta("tex", "r.localizacaoGeografica.siglaUF", ""));
        camposConsultaMap.put("Res : UF", new CampoDaConsulta("tex", "r.localizacaoGeografica.uf", ""));

        camposConsultaMap.put("UFG : Ano egresso", new CampoDaConsulta("num", "hufg.anoDeFim", ""));
        camposConsultaMap.put("UFG : Ano ingresso", new CampoDaConsulta("num", "hufg.anoDeInicio", ""));
        camposConsultaMap.put("UFG : Mês egresso", new CampoDaConsulta("num", "hufg.mesDeFim", ""));
        camposConsultaMap.put("UFG : Mês ingresso", new CampoDaConsulta("num", "hufg.mesDeInicio", ""));
        camposConsultaMap.put("UFG : Número matrícula", new CampoDaConsulta("num", "hufg.numeroDeMatricula", ""));
        camposConsultaMap.put("UFG : Título trabalho final", new CampoDaConsulta("tex", "hufg.tituloDoTrabalhoFinal", ""));

        camposConsultaMap.put("UFG : Data encerramento do curso", new CampoDaConsulta("data", "hufg.cursoDaUFG.dataEncerramento", ""));
        camposConsultaMap.put("UFG : Data criação do curso", new CampoDaConsulta("data", "hufg.cursoDaUFG.dataCriacao", ""));
        camposConsultaMap.put("UFG : Email institucional do curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.emailInstitucional", ""));
        camposConsultaMap.put("UFG : Nome do curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.nome", ""));
        camposConsultaMap.put("UFG : Sigla do curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.sigla", ""));
        camposConsultaMap.put("UFG : Tipo de instância", new CampoDaConsulta("enum", "hufg.cursoDaUFG.tipoInstancia", "TipoInstancia"));
        camposConsultaMap.put("UFG : Url do curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.url", ""));
        camposConsultaMap.put("UFG : Modalidade do curso", new CampoDaConsulta("enum", "hufg.cursoDaUFG.modalidadeCurso", "ModalidadeCurso"));
        camposConsultaMap.put("UFG : Nível do curso", new CampoDaConsulta("enum", "hufg.cursoDaUFG.nivel", "Nivel"));
        camposConsultaMap.put("UFG : Tipo resolução", new CampoDaConsulta("enum", "hufg.cursoDaUFG.tipoDeResolucao", "TipoResolucao"));
        camposConsultaMap.put("UFG : Número da resolução do curso", new CampoDaConsulta("num", "hufg.cursoDaUFG.numeroDaResolucao", ""));
        camposConsultaMap.put("UFG : Turno do curso", new CampoDaConsulta("enum", "hufg.cursoDaUFG.turno", "Turno"));
        camposConsultaMap.put("UFG : Cidade realizou o curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.cidade", ""));
        camposConsultaMap.put("UFG : Pais realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.pais", ""));
        camposConsultaMap.put("UFG : Sigla da UF onde realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.siglaUF", ""));
        camposConsultaMap.put("UFG : UF onde realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.uf", ""));
//        camposConsultaMap.put("UFG : Município realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.municipio", ""));
//        camposConsultaMap.put("UFG : Código referência cidade realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.codRef"));
//        camposConsultaMap.put("UFG : Latitude cidade realizou curso", new CampoDaConsulta("num", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.latitude"));
//        camposConsultaMap.put("UFG : Longitude cidade realizou curso", new CampoDaConsulta("num", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.longitude"));
//        camposConsultaMap.put("UFG : Referência local realizou curso", new CampoDaConsulta("tex", "hufg.cursoDaUFG.unidadeAcademica.localizacaoGeografica.referencia"));              

        camposConsultaMap.put("UFG : Área conhecimeto", new CampoDaConsulta("tex", "hufg.cursoDaUFG.area.nomeArea", ""));
        camposConsultaMap.put("UFG : Código de área conhecimento", new CampoDaConsulta("num", "hufg.cursoDaUFG.area.codArea", ""));
        camposConsultaMap.put("UFG : Área pai de conhecimento", new CampoDaConsulta("tex", "hufg.cursoDaUFG.area.areaPai", ""));
        camposConsultaMap.put("UFG : Subárea de conhecimento", new CampoDaConsulta("tex", "hufg.cursoDaUFG.area.subAreas", ""));

        camposConsultaMap.put("PrA : Tipo", new CampoDaConsulta("enum", "acad.tipoProgramaAcademico", "TipoProgramaAcademico"));
        camposConsultaMap.put("PrA : Início", new CampoDaConsulta("data", "acad.dataDeInicio", ""));
        camposConsultaMap.put("PrA : Fim", new CampoDaConsulta("data", "acad.dataDeFim", ""));
        camposConsultaMap.put("PrA : Descrição", new CampoDaConsulta("tex", "acad.descricao", ""));

        camposConsultaMap.put("AvC : Data", new CampoDaConsulta("data", "aval.momentoAvaliacao", ""));
        camposConsultaMap.put("AvC : Motivação", new CampoDaConsulta("enum", "aval.motivacaoParaEscolha", "Motivacao"));
        camposConsultaMap.put("AvC : Satisfação", new CampoDaConsulta("num", "aval.satisfacaoCurso", ""));
        camposConsultaMap.put("AvC : Conceito global", new CampoDaConsulta("num", "aval.conceitoGlobal", ""));
        camposConsultaMap.put("AvC : Preparação mercado", new CampoDaConsulta("num", "aval.preparacaoMercado", ""));
        camposConsultaMap.put("AvC : Melhoria comunicação", new CampoDaConsulta("num", "aval.melhoriaComunicacao", ""));
        camposConsultaMap.put("AvC : Capacidade ética", new CampoDaConsulta("num", "aval.capacidadeEticaResponsabilidade", ""));
        camposConsultaMap.put("AvC : Capacidade habilidades", new CampoDaConsulta("num", "aval.capacidadeHabilidadesAreaConhecimento", ""));
        //camposConsultaMap.put("AvC : Comentários", new CampoDaConsulta("data", "aval.comentario", ""));

        camposConsultaMap.put("EnM : Ano egresso", new CampoDaConsulta("num", "em.anoConclusao", ""));
        camposConsultaMap.put("EnM : Ano ingresso", new CampoDaConsulta("num", "em.anoInicio", ""));
        camposConsultaMap.put("EnM : Mês egresso", new CampoDaConsulta("num", "em.mesConclusao", ""));
        camposConsultaMap.put("EnM : Mês ingresso", new CampoDaConsulta("num", "em.mesInicio", ""));
        camposConsultaMap.put("EnM : Nome instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.nome", ""));
        camposConsultaMap.put("EnM : Tipo instituição", new CampoDaConsulta("enum", "em.instituicaoEnsinoMedio.tipoInstituicao", "TipoInstituicao"));
        camposConsultaMap.put("EnM : Cidade instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.cidade", ""));
        camposConsultaMap.put("EnM : Pais instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.pais", ""));
        camposConsultaMap.put("EnM : Sigla da UF da instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.siglaUF", ""));
        camposConsultaMap.put("EnM : UF da instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.uf", ""));
//        camposConsultaMap.put("EnM : Município instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.municipio", ""));
//        camposConsultaMap.put("EnM : Código referência cidade da instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.codRef"));
//        camposConsultaMap.put("EnM : Latitude cidade da instituição", new CampoDaConsulta("num", "em.instituicaoEnsinoMedio.localizacao.latitude"));
//        camposConsultaMap.put("EnM : Longitude cidade da instituição", new CampoDaConsulta("num", "em.instituicaoEnsinoMedio.localizacao.longitude"));
//        camposConsultaMap.put("EnM : Referência local da instituição", new CampoDaConsulta("tex", "em.instituicaoEnsinoMedio.localizacao.referencia"));
        camposConsultaMap.put("IES : Ano egresso", new CampoDaConsulta("num", "hoies.anoFim", ""));
        camposConsultaMap.put("IES : Ano ingresso", new CampoDaConsulta("num", "hoies.anoInicio", ""));
        camposConsultaMap.put("IES : Mês egresso", new CampoDaConsulta("num", "hoies.mesFim", ""));
        camposConsultaMap.put("IES : Mês ingresso", new CampoDaConsulta("num", "hoies.mesInicio", ""));
        camposConsultaMap.put("IES : Nome da IES", new CampoDaConsulta("tex", "hoies.iESDoCurso", ""));
        camposConsultaMap.put("IES : Nome curso", new CampoDaConsulta("tex", "hoies.nomeDoCurso", ""));

        camposConsultaMap.put("AtP : Razão social", new CampoDaConsulta("tex", "aprof.organizacao.razaoSocial", ""));
        camposConsultaMap.put("AtP : Endereço comercial", new CampoDaConsulta("tex", "aprof.organizacao.enderecoComercial", ""));
        camposConsultaMap.put("AtP : Natureza", new CampoDaConsulta("enum", "aprof.organizacao.natureza", "NaturezaOrganizacao"));
        camposConsultaMap.put("AtP : Página Web", new CampoDaConsulta("tex", "aprof.organizacao.paginaWeb", ""));

        camposConsultaMap.put("AtP : Comentário", new CampoDaConsulta("tex", "aprof.comentario", ""));
        camposConsultaMap.put("AtP : Data registro", new CampoDaConsulta("data", "aprof.dataInicio", ""));
        camposConsultaMap.put("AtP : Data saída", new CampoDaConsulta("data", "aprof.dataFim", ""));
        camposConsultaMap.put("AtP : Forma ingresso", new CampoDaConsulta("enum", "aprof.formaIngresso", "FormaIngressoInstituicao"));
        camposConsultaMap.put("AtP : Perspectiva futuro da área", new CampoDaConsulta("num", "aprof.perspectivaFuturoArea", ""));
        camposConsultaMap.put("AtP : Renda mensal", new CampoDaConsulta("num", "aprof.rendaMensalMedia", ""));
        camposConsultaMap.put("AtP : Satisfação com a renda", new CampoDaConsulta("num", "aprof.satisfacaoRenda", ""));

        camposConsultaMap.put("AtP : Àrea conhecimento", new CampoDaConsulta("tex", "aprof.areaConhecimento.nomeArea", ""));
        camposConsultaMap.put("AtP : Código de área conhecimento", new CampoDaConsulta("num", "aprof.areaConhecimento.codArea", ""));
        camposConsultaMap.put("AtP : Área pai de conhecimento", new CampoDaConsulta("tex", "hufg.cursoDaUFG.areaPai", ""));
        camposConsultaMap.put("AtP : Subárea de conhecimento", new CampoDaConsulta("tex", "hufg.cursoDaUFG.subAreas", ""));

        camposConsultaMap.put("AtP : Cidade", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.cidade", ""));
        camposConsultaMap.put("AtP : País", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.pais", ""));
        camposConsultaMap.put("AtP : Sigla da UF", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.siglaUF", ""));
        camposConsultaMap.put("AtP : UF", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.uf", ""));
//        camposConsultaMap.put("AtP : Município", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.municipio", ""));
//        camposConsultaMap.put("AtP : Código referência da cidade", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.codRef"));
//        camposConsultaMap.put("AtP : Latitude da cidade", new CampoDaConsulta("num", "aprof.organizacao.localizacao.latitude"));
//        camposConsultaMap.put("AtP : Longitude da cidade", new CampoDaConsulta("num", "aprof.organizacao.localizacao.longitude"));
//        camposConsultaMap.put("AtP : Referência do local", new CampoDaConsulta("tex", "aprof.organizacao.localizacao.referencia"));
    }

    public void construirRegrasDeFiltro(String campo) {
        String tipoCampo = (campo == null ? "tex" : camposConsultaMap.get(campo).tipoCampo);
        regrasDeFiltro = new ArrayList<>();
        if (tipoCampo.equals("tex")) {
            regrasDeFiltro.add("contém");
            regrasDeFiltro.add("não contém");
            regrasDeFiltro.add("começa com");
            regrasDeFiltro.add("termina com");
        }
        if (tipoCampo.equals("num")) {
            regrasDeFiltro.add("maior que");
            regrasDeFiltro.add("menor que");
            regrasDeFiltro.add("menor ou igual a");
            regrasDeFiltro.add("maior ou igual a");
        }
        if (tipoCampo.equals("data")) {
            regrasDeFiltro.add("antes de");
            regrasDeFiltro.add("depois de");

        }
        if (tipoCampo.equals("enum")) {
            String nomeEnum = camposConsultaMap.get(campo).nomeEnum;
            try {
                Class<?> k = Class.forName("br.ufg.inf.fabrica.sempreufg.enums." + nomeEnum);
                for (Object enumConstant : k.getEnumConstants()) {
                    Method m = k.getMethod("getValue", null);
                    regrasDeFiltro.add((String) m.invoke(enumConstant, null));
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(EgressoDao.class.getName()).log(Level.SEVERE, "Erro ao criar RegraDeFiltro para o Enum selecionado", "");
            }
        } else {
            regrasDeFiltro.add("está vazio");
            regrasDeFiltro.add("igual a");
        }

    }

    class CampoDaConsulta {

        private String tipoCampo;
        private String jpqlCampo;
        private String nomeEnum;

        public CampoDaConsulta(String tipoCampo, String jpqlCampo, String nomeEnum) {
            this.tipoCampo = tipoCampo;
            this.jpqlCampo = jpqlCampo;
            this.nomeEnum = nomeEnum;
        }

        public String getTipoCampo() {
            return tipoCampo;
        }

        public void setTipoCampo(String tipoCampo) {
            this.tipoCampo = tipoCampo;
        }

        public String getJpqlCampo() {
            return jpqlCampo;
        }

        public void setJpqlCampo(String jpqlCampo) {
            this.jpqlCampo = jpqlCampo;
        }

        public String getNomeEnum() {
            return nomeEnum;
        }

        public void setNomeEnum(String nomeEnum) {
            this.nomeEnum = nomeEnum;
        }

    }

    public String formataRegraValor(FiltroConsulta filtro, String tipoCampo) {
        if (filtro.getValor() == null || filtro.getRegra().equalsIgnoreCase("está vazio")) {
            return " IS NULL";
        }
        switch (tipoCampo) {
            case "tex":
                switch (filtro.getRegra()) {
                    case "contém":
                        return " LIKE '%" + filtro.getValor() + "%'";
                    case "não contém":
                        return " NOT LIKE '%" + filtro.getValor() + "%'";
                    case "começa com":
                        return " LIKE '" + filtro.getValor() + "%'";
                    case "termina com":
                        return " LIKE '%" + filtro.getValor() + "'";
                    case "igual a":
                        return " LIKE '" + filtro.getValor() + "'";
                    default:
                        return "";
                }
            case "num":
                switch (filtro.getRegra()) {
                    case "maior que":
                        return " > " + filtro.getValor();
                    case "menor que":
                        return " < " + filtro.getValor();
                    case "igual a":
                        return " = " + filtro.getValor();
                    case "menor ou igual a":
                        return " <= " + filtro.getValor();
                    case "maior ou igual a":
                        return " >= " + filtro.getValor();
                    default:
                        return "";
                }
            case "data":
                final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate minhaData = LocalDate.parse(filtro.getValor(), dtf).minusDays(0);

                switch (filtro.getRegra()) {
                    case "antes de":
                        return " < '" + minhaData + "'";
                    case "depois de":
                        return " > '" + minhaData + "'";
                    case "igual a":
                        return " = '" + minhaData + "'";
                    default:
                        return "";
                }
            case "enum":
                return " IN " + filtro.getValor();
            default:
                break;
        }
        return " like " + filtro.getValor();
    }

    private void definirJpqlBaseDoMapa(FiltroConsultaLazy filtros, StringBuilder query) {
        String entidadePrincipal = filtros.getClassePrincipalDaConsulta().getSimpleName();

        switch (entidadePrincipal) {
            case "Residencia":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, r.localizacaoGeografica.cidade as cidade, r.localizacaoGeografica.latitude as latitude, ")
                        .append("r.localizacaoGeografica.longitude as longitude FROM Residencia r ")
                        .append("JOIN r.egresso e ")
                        .append("JOIN r.localizacaoGeografica l ");
                break;
            case "HistoricoIEM":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM HistoricoIEM em ")
                        .append("JOIN em.egresso e ")
                        .append("JOIN em.instituicaoEnsinoMedio iem ")
                        .append("JOIN em.instituicaoEnsinoMedio.localizacao l ");

                break;
            case "HistoricoNaUFG":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM HistoricoNaUFG hufg ")
                        .append("JOIN hufg.egresso e ")
                        .append("JOIN hufg.cursoDaUFG curso ")
                        .append("JOIN hufg.cursoDaUFG.unidadeAcademica u ")
                        .append("JOIN hufg.cursoDaUFG.unidadeAcademica.regionalUFG r ")
                        .append("JOIN hufg.cursoDaUFG.unidadeAcademica.regionalUFG.localizacao l ");
                break;

            case "RealizacaoDeProgramaAcademico":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM RealizacaoDeProgramaAcademico acad ")
                        .append("JOIN acad.historico h ")
                        .append("JOIN h.egresso e ")
                        .append("JOIN h.cursoDaUFG curso ")
                        .append("JOIN curso.unidadeAcademica uni ")
                        .append("JOIN uni.regionalUFG as reg ")
                        .append("JOIN reg.localizacao as l ");
                break;
            case "AvaliacaoDoCursoPeloEgresso":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM AvaliacaoDoCursoPeloEgresso aval ")
                        .append("JOIN aval.historicoUFG h ")
                        .append("JOIN h.egresso e ")
                        .append("JOIN h.cursoDaUFG curso ")
                        .append("JOIN curso.unidadeAcademica uni ")
                        .append("JOIN uni.regionalUFG as reg ")
                        .append("JOIN reg.localizacao as l ");
                break;

            case "HistoricoEmOutraIES":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM HistoricoEmOutraIES hoies ")
                        .append("JOIN hoies.egresso e ")
                        .append("JOIN hoies.cursoDeOutraIES as cursoOies ")
                        .append("JOIN hoies.cursoDeOutraIES.localizacaoGeografica as l ");
                break;
            case "Atuacao":
                query.append("SELECT COUNT(distinct e) as quantidadeEgressos, l.cidade as cidade, l.latitude as latitude, ")
                        .append("l.longitude as longitude FROM Atuacao aprof ")
                        .append("JOIN aprof.egresso e ")
                        .append("JOIN aprof.organizacao as o ")
                        .append("JOIN o.localizacao as l ");
                break;
            default:
                query.append("SELECT COUNT(e) as quantidadeEgressos, e.naturalidade.cidade as cidade, e.naturalidade.latitude as latitude, "
                        + "e.naturalidade.longitude as longitude ")
                        .append("FROM Egresso e");
                break;
        }
    }

    private void definirGroupByDoMapa(FiltroConsultaLazy filtros, StringBuilder query) {

        switch (filtros.getClassePrincipalDaConsulta().getSimpleName()) {
            case "Residencia":
                query.append(" Group by l.cidade");
                break;
            case "HistoricoIEM":
                query.append(" Group by l.cidade");
                break;
            case "HistoricoNaUFG":
                query.append(" Group by l.cidade");
                break;
            case "RealizacaoDeProgramaAcademico":
                query.append(" Group by l.cidade");
                break;
            case "AvaliacaoDoCursoPeloEgresso":
                query.append(" Group by l.cidade");
                break;
            case "HistoricoEmOutraIES":
                query.append(" Group by l.cidade");
                break;
            case "Atuacao":
                query.append(" Group by l.cidade");
                break;
            default:
                query.append(" Group by e.naturalidade.cidade");
                break;
        }
    }
    
}
