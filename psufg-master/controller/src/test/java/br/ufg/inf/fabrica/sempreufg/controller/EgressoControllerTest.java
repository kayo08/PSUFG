/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Danillo
 */
public class EgressoControllerTest {

    public EgressoControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    @Ignore
    public void testAtualizar() {
        System.out.println("atualizar");
        Egresso egresso = null;
        EgressoController instance = new EgressoController();
        List<String> expResult = null;
        List<String> result = instance.atualizar(egresso);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    @Ignore
    public void testPesquisarValoresCampo() {
        System.out.println("pesquisarValoresCampo");
        String campo = "";
        EgressoController instance = new EgressoController();
        List<String> expResult = null;
        List<String> result = instance.pesquisarValoresCampo(campo);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}
