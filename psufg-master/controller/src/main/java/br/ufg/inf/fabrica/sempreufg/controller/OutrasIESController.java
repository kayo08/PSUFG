package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.CursoDeOutraIESDao;
import br.ufg.inf.fabrica.sempreufg.dominio.CursoDeOutraIES;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Danillo
 */
public class OutrasIESController {

    private CursoDeOutraIESDao dao;

    public OutrasIESController() {
        dao = new CursoDeOutraIESDao();
    }

    public List<CursoDeOutraIES> buscarOutraIes(String iesDoCurso) {
        return dao.buscarOutraIes(iesDoCurso);
    }

    public CursoDeOutraIES buscarCursoSuperior(String nomeIes, String nomeCurso) {
        return dao.buscarCursoSuperior(nomeIes, nomeCurso);
    }
}
