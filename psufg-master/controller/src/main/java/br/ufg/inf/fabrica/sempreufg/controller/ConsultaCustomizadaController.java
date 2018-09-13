package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.ConsultaDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Atuacao;
import br.ufg.inf.fabrica.sempreufg.dominio.AvaliacaoDoCursoPeloEgresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Consulta;
import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoEmOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoIEM;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoNaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.RealizacaoDeProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.dominio.Residencia;
import java.util.List;

/**
 *
 * @author auf
 */
public final class ConsultaCustomizadaController {

    private final EgressoDao dao;

    private final ConsultaDao consultaDao = new ConsultaDao();

    public ConsultaCustomizadaController() {
        dao = new EgressoDao();
    }

    public ConsultaCustomizadaController(EgressoDao dao) {
        this.dao = dao;
    }

    public List<String> regrasDeFiltro(String campo) throws ClassNotFoundException {
        return dao.obterRegrasDeFiltro(campo);
    }

    public List criarConsultarJpqlLazy(FiltroConsultaLazy filtros) {
        try {
            return dao.consultarJpqlLazyComFiltros(filtros);
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public int criarConsultaNumeroRegistros(FiltroConsultaLazy filtros) {
        try {
            return dao.consultarNumeroRegistros(filtros);
        } catch (NullPointerException npe) {
            return 0;
        }
    }

    public List<DadosMapa> consultarLocaisMapa(FiltroConsultaLazy filtros) {
        return dao.consultarLocaisMapa(filtros);
    }

    public List<String> camposConsulta() {
        return dao.camposConsulta();
    }

    public boolean campoEhTipoEnum(String campo) {
        return dao.campoEhTipoEnum(campo);
    }

    public String descobrirTipoCampo(String campo) {
        return dao.descobrirTipoCampo(campo);
    }

    public String recuperarChaveEnum(String campo, String valorEnum) {
        return dao.recuperarChaveEnum(campo, valorEnum);
    }

    public Class descobrirClassePrincipalDaConsulta(String abaSelecionada) {
        switch (abaSelecionada) {
            case "abaDadosResidencia":
                return Residencia.class;
            case "abaEnsinoMedio":
                return HistoricoIEM.class;
            case "abaHistoricoUFG":
                return HistoricoNaUFG.class;
            case "abaAvaliacaoCurso":
                return AvaliacaoDoCursoPeloEgresso.class;
            case "abaProgramaAcademico":
                return RealizacaoDeProgramaAcademico.class;
            case "abaHistoricoOutrasIES":
                return HistoricoEmOutraIES.class;
            case "abaAtuacaoProfissional":
                return Atuacao.class;
            default:
                return Egresso.class;
        }
    }

    public String descobrirPrefixoDaClassePrincipal(String abaSelecionada) {
        switch (abaSelecionada) {
            case "abaDadosResidencia":
                return "r.";
            case "abaEnsinoMedio":
                return "em.";
            case "abaHistoricoUFG":
                return "hufg.";
            case "abaAvaliacaoCurso":
                return "aval.";
            case "abaProgramaAcademico":
                return "acad.";
            case "abaHistoricoOutrasIES":
                return "hoies.";
            case "abaAtuacaoProfissional":
                return "aprof.";
            default:
                return "e.";
        }
    }

    public Boolean salvarConsultaFavorita(Consulta consulta) {
        return consultaDao.salvar(consulta) != null;
    }

    public void excluirConsultaFavorita(Consulta consulta) {
        consultaDao.remover(consulta);
    }

    public List consultarFavoritosPorUsuario(Long idUsuario) {
        return consultaDao.buscarConsultasDoUsuario(idUsuario);
    }

}
