package br.ufg.inf.fabrica.sempreufg.visao.util;

import br.ufg.inf.fabrica.sempreufg.dao.ConnectionFactory;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;

/**
 *
 * @author auf
 */
public final class ConsultaCustomizada {

    private final Map<String, CampoDaConsulta> camposConsultaMap = new TreeMap<>();
    public List<String> camposConsulta = new ArrayList<>();
    public List<String> regrasDeFiltro = new ArrayList<>();
    public List<FiltroConsulta> filtrosAdicionados = new ArrayList<>();

    public ConsultaCustomizada() {
        this.construirCamposConsulta();
    }

    public List<String> getCamposConsulta() {
        List<String> listCampos = new ArrayList<>(this.getCamposConsultaMap().keySet());
        return listCampos;
    }

    private Map<String, CampoDaConsulta> getCamposConsultaMap() {
        return camposConsultaMap;
    }

    public List<String> getRegrasDeFiltro(String campo) {
        String tipoCampo = (campo == null ? "tex" : camposConsultaMap.get(campo).tipoCampo);
        this.construirRegrasDeFiltro(tipoCampo);
        return regrasDeFiltro;
    }

    public List<FiltroConsulta> getFiltrosAdicionados() {
        return filtrosAdicionados;
    }

    public void setFiltrosAdicionados(List<FiltroConsulta> filtrosAdicionados) {
        this.filtrosAdicionados = filtrosAdicionados;
    }

    public List<Egresso> criarConsultarJpql(List<FiltroConsulta> filtros) {
        EntityManager em = ConnectionFactory.obterManager();

        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT e FROM Egresso e ")
                .append("JOIN e.historicosNaUFG historicosNaUFG ")
                .append("JOIN e.historicosEnsinoMedio historicosensinomedio ")
                .append("JOIN e.historicosEmOutrasIES historicosemoutrasies ")
                .append("JOIN e.atuacoes atuacao ")
                .append("JOIN e.residencias residencias ")
                .append("WHERE ");

        for (FiltroConsulta filtro : filtros) {
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
        return em.createQuery(query.toString()).getResultList();

    }

    public String formataRegraValor(FiltroConsulta filtro, String tipoCampo) {
        if (tipoCampo.equals("tex")) {
            switch (filtro.getRegra()) {
                case "contém":
                    return " LIKE '%" + filtro.getValor() + "%'";
                case "não contém":
                    return " NOT LIKE '%" + filtro.getValor() + "%'";
                case "começa com":
                    return " LIKE '" + filtro.getValor() + "%'";
                case "termina com":
                    return " LIKE '%" + filtro.getValor() + "'";
                case "é igual a":
                    return " LIKE '" + filtro.getValor() + "'";
                case "está vazio":
                    return " IS EMPTY";
                default:
                    return "";
            }
        }
        if (tipoCampo.equals("num")) {
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
                case "está vazio":
                    return " IS EMPTY";
                default:
                    return "";
            }
        }
        if (tipoCampo.equals("data")) {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate minhaData = LocalDate.parse(filtro.getValor(), dtf).minusDays(0);

            switch (filtro.getRegra()) {
                case "antes de":
                    return " < '" + minhaData + "'";
                case "depois de":
                    return " > '" + minhaData + "'";
                case "igual a":
                    return " = '" + minhaData + "'";
                case "está vazio":
                    return " IS EMPTY";
                default:
                    return "";
            }
        }
        return " like " + filtro.getValor();
    }

    public void construirCamposConsulta() {

        camposConsultaMap.put("Egresso : CPF", new CampoDaConsulta("tex", "e.cpf"));
        camposConsultaMap.put("Egresso : Email Principal", new CampoDaConsulta("tex", "e.emailPrincipal"));
        camposConsultaMap.put("Egresso : Frequência divulgação", new CampoDaConsulta("enum", "e.frequenciaDivulgacao"));
        camposConsultaMap.put("Egresso : Nome social", new CampoDaConsulta("tex", "e.nomeSocial"));
        camposConsultaMap.put("Egresso : Data cadastro", new CampoDaConsulta("data", "e.timeStampCadastramento"));
        camposConsultaMap.put("Egresso : Data exclusão", new CampoDaConsulta("data", "e.timeStampExclusaoLogica"));
        camposConsultaMap.put("Egresso : Data último acesso", new CampoDaConsulta("data", "e.timeStampUltimaUtilizacao"));
        camposConsultaMap.put("Egresso : Data nascimento", new CampoDaConsulta("data", "e.dataDeNascimento"));
        camposConsultaMap.put("Egresso : Email alternativo", new CampoDaConsulta("tex", "e.emailAlternativo"));
        camposConsultaMap.put("Egresso : Nome da mãe", new CampoDaConsulta("tex", "e.nomeDaMae"));
        camposConsultaMap.put("Egresso : Nome oficial", new CampoDaConsulta("tex", "e.nomeOficial"));
        camposConsultaMap.put("Egresso : Visibilidade de dados", new CampoDaConsulta("enum", "e.visibiliadeDeDados"));

        camposConsultaMap.put("Egresso : Natural da cidade", new CampoDaConsulta("tex", "e.naturalidade.cidade"));
        camposConsultaMap.put("Egresso : Município de nascimento", new CampoDaConsulta("tex", "e.naturalidade.municipio"));
        camposConsultaMap.put("Egresso : País de nascimento", new CampoDaConsulta("tex", "e.naturalidade.pais"));
        camposConsultaMap.put("Egresso : Sigla da UF de nascimento", new CampoDaConsulta("tex", "e.naturalidade.siglaUF"));
        camposConsultaMap.put("Egresso : UF de nascimento", new CampoDaConsulta("tex", "e.naturalidade.uf"));
//        camposConsultaMap.put("Egresso : Código referência da cidade nascimento", new CampoDaConsulta("tex", "e.naturalidade.codRef"));
//        camposConsultaMap.put("Egresso : Latitude da cidade nascimento", new CampoDaConsulta("num", "e.naturalidade.latitude"));
//        camposConsultaMap.put("Egresso : Longitude da cidade nascimento", new CampoDaConsulta("num", "e.naturalidade.longitude"));
//        camposConsultaMap.put("Egresso : Referência do local de nascimento", new CampoDaConsulta("tex", "e.naturalidade.referencia"));

        camposConsultaMap.put("Residencia : CEP", new CampoDaConsulta("tex", "residencias.cep"));
        camposConsultaMap.put("Residencia : Bairro", new CampoDaConsulta("tex", "residencias.bairro"));
        camposConsultaMap.put("Residencia : Complemento", new CampoDaConsulta("tex", "residencias.complemento"));
        camposConsultaMap.put("Residencia : Data de fim", new CampoDaConsulta("data", "residencias.dataDeFim"));
        camposConsultaMap.put("Residencia : Data de início", new CampoDaConsulta("data", "residencias.dataDeInicio"));
        camposConsultaMap.put("Residencia : Logradouro", new CampoDaConsulta("tex", "residencias.logradouro"));
        camposConsultaMap.put("Residencia : Número", new CampoDaConsulta("tex", "residencias.numero"));

        camposConsultaMap.put("Histórico UFG : Ano egresso", new CampoDaConsulta("num", "historicosNaUFG.anoDeFim"));
        camposConsultaMap.put("Histórico UFG : Ano ingresso", new CampoDaConsulta("num", "historicosNaUFG.anoDeInicio"));
        camposConsultaMap.put("Histórico UFG : Mês egresso", new CampoDaConsulta("num", "historicosNaUFG.mesDeFim"));
        camposConsultaMap.put("Histórico UFG : Mês ingresso", new CampoDaConsulta("num", "historicosNaUFG.mesDeInicio"));
        camposConsultaMap.put("Histórico UFG : Número matrícula", new CampoDaConsulta("num", "historicosNaUFG.numeroDeMatricula"));
        camposConsultaMap.put("Histórico UFG : Título trabalho final", new CampoDaConsulta("tex", "historicosNaUFG.tituloDoTrabalhoFinal"));

        camposConsultaMap.put("Histórico UFG : Data encerramento do curso", new CampoDaConsulta("data", "historicosNaUFG.cursoDaUFG.dataEncerramento"));
        camposConsultaMap.put("Histórico UFG : Data criação do curso", new CampoDaConsulta("data", "historicosNaUFG.cursoDaUFG.dataCriacao"));
        camposConsultaMap.put("Histórico UFG : Email institucional do curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.emailInstitucional"));
        camposConsultaMap.put("Histórico UFG : Nome do curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.nome"));
        camposConsultaMap.put("Histórico UFG : Sigla do curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.sigla"));
        camposConsultaMap.put("Histórico UFG : Tipo de instância", new CampoDaConsulta("enum", "historicosNaUFG.cursoDaUFG.tipoInstancia"));
        camposConsultaMap.put("Histórico UFG : Url do curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.url"));
        camposConsultaMap.put("Histórico UFG : Modalidade do curso", new CampoDaConsulta("enum", "historicosNaUFG.cursoDaUFG.modalidadeCurso"));
        camposConsultaMap.put("Histórico UFG : Nível do curso", new CampoDaConsulta("enum", "historicosNaUFG.cursoDaUFG.nivel"));
        camposConsultaMap.put("Histórico UFG : Número da resolução do curso", new CampoDaConsulta("num", "historicosNaUFG.cursoDaUFG.numeroDaResolucao"));
        camposConsultaMap.put("Histórico UFG : Turno do curso", new CampoDaConsulta("enum", "historicosNaUFG.cursoDaUFG.turno"));

        camposConsultaMap.put("Histórico UFG : Cidade realizou o curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.cidade"));
        camposConsultaMap.put("Histórico UFG : Município realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.municipio"));
        camposConsultaMap.put("Histórico UFG : Pais realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.pais"));
        camposConsultaMap.put("Histórico UFG : Sigla da UF onde realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.siglaUF"));
        camposConsultaMap.put("Histórico UFG : UF onde realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.uf"));
//        camposConsultaMap.put("Histórico UFG : Código referência cidade realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.codRef"));
//        camposConsultaMap.put("Histórico UFG : Latitude cidade realizou curso", new CampoDaConsulta("num", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.latitude"));
//        camposConsultaMap.put("Histórico UFG : Longitude cidade realizou curso", new CampoDaConsulta("num", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.longitude"));
//        camposConsultaMap.put("Histórico UFG : Referência local realizou curso", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.unidadeAcademica.localizacaoGeografica.referencia"));              
        
        camposConsultaMap.put("Histórico UFG : Área conhecimento", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.area.nomeArea"));
        camposConsultaMap.put("Histórico UFG : Código de área conhecimento", new CampoDaConsulta("num", "historicosNaUFG.cursoDaUFG.area.codArea"));
        camposConsultaMap.put("Histórico UFG : Área pai de conhecimento", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.area.areaPai"));
        camposConsultaMap.put("Histórico UFG : Subárea de conhecimento", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.area.subAreas"));

        camposConsultaMap.put("Histórico ensino médio : Ano egresso", new CampoDaConsulta("num", "historicosiem.anoConclusao"));
        camposConsultaMap.put("Histórico ensino médio : Ano ingresso", new CampoDaConsulta("num", "historicosiem.anoInicio"));
        camposConsultaMap.put("Histórico ensino médio : Mês egresso", new CampoDaConsulta("num", "historicosiem.mesConclusao"));
        camposConsultaMap.put("Histórico ensino médio : Mês ingresso", new CampoDaConsulta("num", "historicosiem.mesInicio"));
        camposConsultaMap.put("Histórico ensino médio : Nome instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.nome"));
        camposConsultaMap.put("Histórico ensino médio : Tipo instituição", new CampoDaConsulta("enum", "historicoisem.instituicaoEnsinoMedio.nome"));
        camposConsultaMap.put("Histórico ensino médio : Cidade instituição", new CampoDaConsulta("enum", "historicoisem.instituicaoEnsinoMedio.localizacao.cidade"));
        camposConsultaMap.put("Histórico ensino médio : Município instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.municipio"));
        camposConsultaMap.put("Histórico ensino médio : Pais instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.pais"));
        camposConsultaMap.put("Histórico ensino médio : Sigla da UF da instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.siglaUF"));
        camposConsultaMap.put("Histórico ensino médio : UF da instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.uf"));
//        camposConsultaMap.put("Histórico ensino médio : Código referência cidade da instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.codRef"));
//        camposConsultaMap.put("Histórico ensino médio : Latitude cidade da instituição", new CampoDaConsulta("num", "historicoisem.instituicaoEnsinoMedio.localizacao.latitude"));
//        camposConsultaMap.put("Histórico ensino médio : Longitude cidade da instituição", new CampoDaConsulta("num", "historicoisem.instituicaoEnsinoMedio.localizacao.longitude"));
//        camposConsultaMap.put("Histórico ensino médio : Referência local da instituição", new CampoDaConsulta("tex", "historicoisem.instituicaoEnsinoMedio.localizacao.referencia"));
        
        
        
        camposConsultaMap.put("Histórico outra IES : Ano egresso", new CampoDaConsulta("num", "historicosemoutraies.anoFim"));
        camposConsultaMap.put("Histórico outra IES : Ano ingresso", new CampoDaConsulta("num", "historicosemoutraies.anoInicio"));
        camposConsultaMap.put("Histórico outra IES : Mês egresso", new CampoDaConsulta("num", "historicosemoutraies.mesFim"));
        camposConsultaMap.put("Histórico outra IES : Mês ingresso", new CampoDaConsulta("num", "historicosemoutraies.mesInicio"));
        camposConsultaMap.put("Histórico outra IES : Nome da IES", new CampoDaConsulta("tex", "historicosemoutraies.iESDoCurso"));
        camposConsultaMap.put("Histórico outra IES : Nome curso", new CampoDaConsulta("tex", "historicosemoutraies.nomeDoCurso"));

        camposConsultaMap.put("Atuação profissional : Razão social", new CampoDaConsulta("tex", "atuacao.organizacao.razaoSocial"));
        camposConsultaMap.put("Atuação profissional : Endereço comercial", new CampoDaConsulta("tex", "atuacao.organizacao.enderecoComercial"));
        camposConsultaMap.put("Atuação profissional : Natureza", new CampoDaConsulta("tex", "atuacao.organizacao.natureza"));
        camposConsultaMap.put("Atuação profissional : Página Web", new CampoDaConsulta("tex", "atuacao.organizacao.paginaWeb"));

        camposConsultaMap.put("Atuação profissional : Comentário", new CampoDaConsulta("tex", "atuacao.comentario"));
        camposConsultaMap.put("Atuação profissional : Data registro", new CampoDaConsulta("data", "atuacao.dataInicio"));
        camposConsultaMap.put("Atuação profissional : Data saída", new CampoDaConsulta("data", "atuacao.dataFim"));
        camposConsultaMap.put("Atuação profissional : Forma ingresso", new CampoDaConsulta("enum", "atuacao.formaIngresso"));
        camposConsultaMap.put("Atuação profissional : Perspectiva futuro da área", new CampoDaConsulta("enum", "atuacao.perspectivaFuturoArea"));
        camposConsultaMap.put("Atuação profissional : Renda mensal", new CampoDaConsulta("num", "atuacao.rendaMensalMedia"));
        camposConsultaMap.put("Atuação profissional : Satisfação com a renda", new CampoDaConsulta("num", "atuacao.satisfacaoRenda"));
        
        camposConsultaMap.put("Atuação profissional : Àrea conhecimento", new CampoDaConsulta("tex", "atuacao.areaConhecimento.nomeArea"));
        camposConsultaMap.put("Atuação profissional : Código de área conhecimento", new CampoDaConsulta("num", "atuacao.areaConhecimento.codArea"));
        camposConsultaMap.put("Atuação profissional : Área pai de conhecimento", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.areaPai"));
        camposConsultaMap.put("Atuação profissional : Subárea de conhecimento", new CampoDaConsulta("tex", "historicosNaUFG.cursoDaUFG.subAreas"));

        camposConsultaMap.put("Atuação profissional : Cidade", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.cidade"));
        camposConsultaMap.put("Atuação profissional : Município", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.municipio"));
        camposConsultaMap.put("Atuação profissional : País", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.pais"));
        camposConsultaMap.put("Atuação profissional : Sigla da UF", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.siglaUF"));
        camposConsultaMap.put("Atuação profissional : UF", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.uf"));
//        camposConsultaMap.put("Atuação profissional : Código referência da cidade", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.codRef"));
//        camposConsultaMap.put("Atuação profissional : Latitude da cidade", new CampoDaConsulta("num", "atuacao.organizacao.localizacao.latitude"));
//        camposConsultaMap.put("Atuação profissional : Longitude da cidade", new CampoDaConsulta("num", "atuacao.organizacao.localizacao.longitude"));
//        camposConsultaMap.put("Atuação profissional : Referência do local", new CampoDaConsulta("tex", "atuacao.organizacao.localizacao.referencia"));
    }

    public void construirRegrasDeFiltro(String tipoCampo) {
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
            //descobrir tipoEnum
        }

        regrasDeFiltro.add("está vazio");
        regrasDeFiltro.add("igual a");

    }

    class CampoDaConsulta {

        private String tipoCampo;
        private String jpqlCampo;

        public CampoDaConsulta(String tipoCampo, String jpqlCampo) {
            this.tipoCampo = tipoCampo;
            this.jpqlCampo = jpqlCampo;
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
    }

//    camposConsultaMap.put("Egresso : CPF", new CampoDaConsulta(Egresso.class, "cpf"));
//        
//        Class cl = Egresso.class;
//        String nomeCampo = "cpf";
//        
//        Field f = cl.getField("cpf"); 
//        f.getType();
//        
//        Method m = cl.getMethod("get"+nomeCampo, null);
//        Class<?> tipoRetorno = m.getReturnType();
//        
    public static void main(String[] args) {
        EntityManager em = ConnectionFactory.obterManager();
        em.createQuery("SELECT DISTINCT e FROM Egresso e JOIN FETCH e.atuacoes atuacao JOIN FETCH e.historicosNaUFG historicosNaUFG WHERE historicosNaUFG.cursoDaUFG.area.areaPai and atuacao.areaConhecimento.");
    }

}
