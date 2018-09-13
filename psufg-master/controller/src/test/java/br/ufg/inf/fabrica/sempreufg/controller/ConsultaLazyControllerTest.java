/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author auf
 */
public class ConsultaLazyControllerTest {

    private final List listStub = new ArrayList<>();
    private final FiltroConsultaLazy filtros = new FiltroConsultaLazy();
    
    private EgressoDao daoStub = new EgressoDao() {
        @Override
        public List consultarJpqlLazyComFiltros(FiltroConsultaLazy filtros) {
            listStub.add(new Object());
            return listStub;
        }

        @Override
        public int consultarNumeroRegistros(FiltroConsultaLazy filtros) {
            return 0;
        }       
    };

    public ConsultaLazyControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void filtrados() {
        System.out.println("Consulta JPQL LAZY padrão <<STUB>>");        
        ConsultaCustomizadaController controllerStub = new ConsultaCustomizadaController(daoStub);
        List expResult = listStub;
        List result = controllerStub.criarConsultarJpqlLazy(filtros);
        assertEquals("Objeto experado não retornado", expResult, result);        
    }

    @Test
    public void quantidadeFiltrados() {   
        ConsultaCustomizadaController controllerStub = new ConsultaCustomizadaController(daoStub);
        int expResult = 0;
        int result = controllerStub.criarConsultaNumeroRegistros(filtros);
        assertEquals("Objeto experado não retornado", expResult, result);        
    }        
    
}
