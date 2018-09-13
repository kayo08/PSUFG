package br.ufg.inf.fabrica.sempreufg.visao.beans;

import br.ufg.inf.fabrica.sempreufg.controller.ConsultaCustomizadaController;
import br.ufg.inf.fabrica.sempreufg.controller.MapaController;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author murilo
 */
@Named(value = "mapaBean")
@SessionScoped
public class MapaBean implements Serializable {

    @Inject
    EgressoBean egressoBean;

    @Inject
    ConsultaLazyBean lazyConsultaDataModel;

    private ConsultaCustomizadaController consultaCustomizadaController = new ConsultaCustomizadaController();

    MapaController controller = new MapaController();

    private MapModel simpleModel = new DefaultMapModel();

    private String tituloMapa = "";

    @PostConstruct
    public void init() {
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void mostrarMapa() {
        RequestContext.getCurrentInstance().execute("exibeMapa();");

    }

    public void mapaEgressosNascimento() {
        List<DadosMapa> dados = new ArrayList<>();
        simpleModel = new DefaultMapModel();
        String aba = egressoBean.getAbaSelecionada();
        this.tituloMapa = montaTituloMapa(aba);
        FiltroConsultaLazy filtros = new FiltroConsultaLazy();
        filtros.setPrefixoDaClassePrincipal(consultaCustomizadaController.descobrirPrefixoDaClassePrincipal(egressoBean.getAbaSelecionada()));
        filtros.setClassePrincipalDaConsulta(consultaCustomizadaController.descobrirClassePrincipalDaConsulta(egressoBean.getAbaSelecionada()));
        filtros.setFiltrosConsulta(egressoBean.getFiltrosConsulta());
        dados = controller.consultarLocaisMapa(filtros);
        if (dados != null && !dados.isEmpty()) {
            for (Iterator<DadosMapa> iterator = dados.iterator(); iterator.hasNext();) {
                DadosMapa next = iterator.next();
                LatLng coord1 = new LatLng(next.getLongitude(), next.getLatitude());
                String rotulo = "Cidade:" + next.getCidade() + "\n " + "Número de egressos:" + "(" + next.getQuantidadeEgressos() + ")";
                simpleModel.addOverlay(new Marker(coord1, rotulo));
                //  System.out.print(next.getCidade() + " " + next.getQuantidadeEgressos() + " " + next.getLatitude() + " " + next.getLongitude() );
            }
        }
         mostrarMapa();
        RequestContext.getCurrentInstance().execute("ativarAbaSelecionada('"+ aba +"')");
       
    }

    public String getTituloMapa() {
        return tituloMapa;
    }

    public void setTituloMapa(String tituloMapa) {
        this.tituloMapa = tituloMapa;
    }

    public String montaTituloMapa(String abaSelecionada) {
        String titulo = "";
        switch (abaSelecionada) {
            case "abaDadosResidencia":
                titulo = "Onde estão/estiveram nossos ex-alunos";
            break;
            case "abaEnsinoMedio":
                titulo = "Onde cursaram o ensino médio nossos ex-alunos";
            break;
            case "abaHistoricoUFG":
                titulo = "Onde nossos ex-alunos estudaram na UFG";
            break;
            case "abaAvaliacaoCurso":
                titulo = "Onde nossos ex-alunos que avaliaram seu curso estudaram na UFG";
            break;
            case "abaProgramaAcademico":
                titulo = "Onde egressaram na UFG os alunos que fizeram programa acadêmico";
            break;
            case "abaHistoricoOutrasIES":
                titulo = "Onde, alem da UFG, nossos ex-alunos estudaram em nível superior";
            break;
            case "abaAtuacaoProfissional":
                titulo = "Onde trabalham/trabalharam nossos ex-alunos";
            break;
            default:
                titulo = "Onde nasceram nossos ex-alunos";
            break;
        }

        return titulo;
    }
}
