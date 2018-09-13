/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.ConsultaDao;
import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.GenericDao;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import br.ufg.inf.fabrica.sempreufg.dominio.Atuacao;
import br.ufg.inf.fabrica.sempreufg.dominio.AvaliacaoDoCursoPeloEgresso;
import br.ufg.inf.fabrica.sempreufg.dominio.Consulta;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoEmOutraIES;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoIEM;
import br.ufg.inf.fabrica.sempreufg.dominio.HistoricoNaUFG;
import br.ufg.inf.fabrica.sempreufg.dominio.RealizacaoDeProgramaAcademico;
import br.ufg.inf.fabrica.sempreufg.dominio.Residencia;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author auf
 */
public class ConsultaCustomizadaControllerTest {

    private final List listStub = new ArrayList<>();
    private final FiltroConsultaLazy filtro = new FiltroConsultaLazy();
    private final ConsultaCustomizadaController controller = new ConsultaCustomizadaController();
    private final Object objResu = new Object();

    String abaSelecionada = "nenhumaAba";

    private final EgressoDao daoStub = new EgressoDao() {
        @Override
        public List consultarJpqlLazyComFiltros(FiltroConsultaLazy filtros) {
            listStub.add(new Object());
            return listStub;
        }

        @Override
        public int consultarNumeroRegistros(FiltroConsultaLazy filtros) {
            return 0; //To change body of generated methods, choose Tools | Templates.
        }

    };

    private GenericDao genericDaoStub = new GenericDao() {
        @Override
        public Object salvar(Object entidade) {
            return entidade == null ? null : objResu; //To change body of generated methods, choose Tools | Templates.
        }
    };

    private final ConsultaDao consultaDaoStub = new ConsultaDao() {
        @Override
        public List buscarConsultasDoUsuario(Long idUsuario) {
            listStub.add(new Object());
            return idUsuario == null ? null : listStub;
        }
    };

    public ConsultaCustomizadaControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void RegrasDeFiltro() throws ClassNotFoundException {
        System.out.println("Obter regra de filtro com campo tipo NULL");
        List<String> resu = controller.regrasDeFiltro(null);
        assertNotNull("RegrasDeFilro não encontrada", resu);

        System.out.println("Obter regra de filtro com campo tipo DATE");
        resu = controller.regrasDeFiltro("Egr : Data nascimento");
        assertNotNull("RegrasDeFilro não encontrada", resu);

        System.out.println("Obter regra de filtro com campo tipo ENUM");
        resu = controller.regrasDeFiltro("Egr : Sexo");
        assertNotNull("RegrasDeFilro não encontrada", resu);

        System.out.println("Obter regra de filtro com campo tipo NUM");
        resu = controller.regrasDeFiltro("EnM : Ano ingresso");
        assertNotNull("RegrasDeFilro não encontrada", resu);

        System.out.println("Obter regra de filtro com campo tipo TEX");
        resu = controller.regrasDeFiltro("Egr : Nome social");
        assertNotNull("RegrasDeFilro não encontrada", resu);

    }

    @Test
    public void CriarConsultaJpqlComStub() {
        System.out.println("Consulta JPQL padrão <<STUB>>");
        ConsultaCustomizadaController controllerStub = new ConsultaCustomizadaController(daoStub);
        List expResult = listStub;
        List result = controllerStub.criarConsultarJpqlLazy(filtro);
        assertEquals("Objeto esperado não retornado", expResult, result);
    }

    @Test
    @Ignore
    public void CriarConsultaJpqlLazy() {
        System.out.println("Consulta JPQL padrão");
        List expResult = null;
        List result = controller.criarConsultarJpqlLazy(filtro);
        assertEquals("Objeto esperado não retornado", expResult, result);

        //Filtro para teste
        String campo = "Egr : CPF";
        String regra = "contém";
        String valor = "111111";
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

        System.out.println("Consulta JPQL tipo Egresso");
        abaSelecionada = "abaDadosPessoais";
        result = controller.criarConsultarJpqlLazy(filtro);
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof Egresso);
        }

        System.out.println("Consulta JPQL tipo Residência");
        filtro.setClassePrincipalDaConsulta(Residencia.class);
        filtro.setPrefixoDaClassePrincipal("r.");
        result = controller.criarConsultarJpqlLazy(filtro);
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof Residencia);
        }

        System.out.println("Consulta JPQL tipo Ensino Médio");
        filtro.setClassePrincipalDaConsulta(HistoricoIEM.class);
        filtro.setPrefixoDaClassePrincipal("em.");
        result = controller.criarConsultarJpqlLazy(filtro);
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof HistoricoIEM);
        }

        System.out.println("Consulta JPQL tipo Histórico UFG");
        filtro.setClassePrincipalDaConsulta(HistoricoNaUFG.class);
        filtro.setPrefixoDaClassePrincipal("hufg.");
        result = controller.criarConsultarJpqlLazy(filtro);
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof HistoricoNaUFG);
        }

        System.out.println("Consulta JPQL tipo Avalição de Curso");
        filtro.setClassePrincipalDaConsulta(AvaliacaoDoCursoPeloEgresso.class);
        filtro.setPrefixoDaClassePrincipal("aval.");
        result = controller.criarConsultarJpqlLazy(filtro);
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof AvaliacaoDoCursoPeloEgresso);
        }

        System.out.println("Consulta JPQL tipo Programa Acadêmico");
        filtro.setClassePrincipalDaConsulta(RealizacaoDeProgramaAcademico.class);
        filtro.setPrefixoDaClassePrincipal("acad.");
        result = controller.criarConsultarJpqlLazy(filtro);

        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof RealizacaoDeProgramaAcademico);
        }

        System.out.println("Consulta JPQL tipo Histórico em Outras IES");
        filtro.setClassePrincipalDaConsulta(HistoricoEmOutraIES.class);
        filtro.setPrefixoDaClassePrincipal("hoies.");
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof HistoricoEmOutraIES);
        }

        System.out.println("Consulta JPQL tipo Programa Acadêmico");
        filtro.setClassePrincipalDaConsulta(Atuacao.class);
        filtro.setPrefixoDaClassePrincipal("aprof.");
        for (Object object : result) {
            assertTrue("Objeto esperado não retornado", object instanceof Atuacao);
        }
    }

    @Test
    public void criarConsultaNumeroRegistros() {
        Integer expResult = 0;
        Integer result = controller.criarConsultaNumeroRegistros(filtro);
        assertEquals("Valor esperado não retornado", expResult, result);
    }

    @Test
    public void CamposConsulta() {
        System.out.println("Obter campos Consulta");
        List<String> resu = controller.camposConsulta();
        assertNotNull("CamposConsulta não encontrados", resu);
        assertTrue("CamposConsulta não encontrados", !resu.isEmpty());
    }

    @Test
    public void CampoEhTipoEnum() {
        String campoEnum = "Egr : Sexo";
        String campoNaoEnum = "Egr : CPF";
        boolean resu = controller.campoEhTipoEnum(campoEnum);
        assertTrue("Objeto esperado não retornado", resu);
        resu = controller.campoEhTipoEnum(campoNaoEnum);
        assertFalse("Objeto esperado não retornado", resu);
    }

    @Test
    public void RecuperarChaveEnum() {
        String campoEnum = "Egr : Sexo";
        String valorEnum = "Feminino";
        String expResult = "1";
        String result = controller.recuperarChaveEnum(campoEnum, valorEnum);
        assertEquals("Objeto esperado não retornado", expResult, result);
    }

    @Test
    public void descobrirClassePrincipalDaConsulta() {
        Class expResult = Egresso.class;
        Class result = controller.descobrirClassePrincipalDaConsulta(abaSelecionada);
        assertEquals("Objeto esperado não retornado", expResult, result);

        abaSelecionada = "abaAtuacaoProfissional";
        expResult = Atuacao.class;
        result = controller.descobrirClassePrincipalDaConsulta(abaSelecionada);
        assertEquals("Objeto esperado não retornado", expResult, result);
    }

    @Test
    public void descobrirPrefixoDaClassePrincipal() {
        String aba = "";
        String expResult = "e.";
        String result = controller.descobrirPrefixoDaClassePrincipal(aba);
        assertEquals("Objeto esperado não retornado", expResult, result);
        aba = "abaEnsinoMedio";
        expResult = "em.";
        result = controller.descobrirPrefixoDaClassePrincipal(aba);
        assertEquals("Objeto esperado não retornado", expResult, result);
    }

    @Test
    public void salvarConsultaFavorita() {
        Consulta c = new Consulta();
        Object expResult = objResu;
        Object result = genericDaoStub.salvar(c);
        assertEquals("Objeto esperado não retornado", expResult, result);
        assertNotNull("Consulta favorita não retornada", result);

        result = genericDaoStub.salvar(null);
        assertNull("Objeto esperado não retornado", result);
    }

    @Test
    public void excluirConsultaFavorita() {
        System.out.println("Método responsabilidade da camada Dominio");
    }

    @Test
    public void consultarFavoritosPorUsuario() {
        Long idUsuario = new Long(0);
        List expResult = listStub;
        List result = consultaDaoStub.buscarConsultasDoUsuario(idUsuario);
        assertEquals("Objeto esperado não retornado", expResult, result);
        assertNotNull("Consulta favorita não retornada", result);

        result = consultaDaoStub.buscarConsultasDoUsuario(null);
        assertNull("Objeto esperado não retornado", result);
    }

}
