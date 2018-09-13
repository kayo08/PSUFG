package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import java.util.List;

/**
 *
 * @author Danillo
 */
public class EgressoController {

    private final EgressoDao dao;

    public EgressoController(EgressoDao dao) {
        this.dao = dao;
    }

    public EgressoController() {
        dao = new EgressoDao();
    }

    public List<String> atualizar(Egresso egresso) {
        List<String> inconsistencias = egresso.validar();
        if (inconsistencias.isEmpty()) {
            egresso.criptografar();
            dao.salvar(egresso);
        }
        return inconsistencias;
    }

    public List<String> pesquisarValoresCampo(String campo) {
        return dao.pesquisarValoresCampo("Egresso", campo);
    }

    public Egresso buscarTodosOsDadosDoEgresso(Long id) {
        return dao.buscarTodosOsDadosDoEgresso(id);
    }

}
