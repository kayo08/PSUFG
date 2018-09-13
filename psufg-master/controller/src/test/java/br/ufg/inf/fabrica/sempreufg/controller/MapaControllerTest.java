/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author murilo
 */
public class MapaControllerTest {

    private final FiltroConsultaLazy filtro = new FiltroConsultaLazy();
    String abaSelecionada = "nenhuma";

   

    public MapaControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void consultarLocaisMapaTest() {
       List<DadosMapa> list =  new ArrayList<>();
        
        DadosMapa dadosMapa = new DadosMapa();
            list.add(dadosMapa);
              
      EgressoDao daoStub = new EgressoDao() {
        @Override
        public List<DadosMapa> consultarLocaisMapa(FiltroConsultaLazy filtros) {
            return list;
        }
    };

        //Filtro para teste
        String campo = "Egr : Nome oficial";
        String regra = "contém";
        String valor = "AAron ferracini bezerra";
        String operador = "";
        String valorLabel = "";
        List filtrosConsulta = new ArrayList();
        filtrosConsulta.add(new FiltroConsulta(campo, regra, valor, operador, valorLabel));
        filtro.setFiltrosConsulta(filtrosConsulta);
        filtro.setAscendente(true);
        filtro.setClassePrincipalDaConsulta(Egresso.class);
        filtro.setPrefixoDaClassePrincipal("e.");
        filtro.setPrimeiroRegistro(1);
        filtro.setQuantidadeRegistros(10);
        filtro.setPropriedadeOrdenacao("id");
           
        MapaController controller = new MapaController(daoStub);
        List<DadosMapa> expResult = list;
        List<DadosMapa> result = controller.consultarLocaisMapa(filtro, daoStub);
        assertEquals("Objeto experado não retornado", expResult, result);
    }

}
