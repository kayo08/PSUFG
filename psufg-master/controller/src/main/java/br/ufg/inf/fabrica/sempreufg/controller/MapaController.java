/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import java.io.Serializable;
import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import java.util.List;

/**
 *
 * @author murilo
 */
public class MapaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final ConsultaCustomizadaController consultaCustomizada = new ConsultaCustomizadaController();
    
    private final EgressoDao dao;

    public MapaController(EgressoDao dao) {
        this.dao = dao;
    }

    public MapaController() {
        this.dao = new EgressoDao();
    }

    public List<DadosMapa> consultarLocaisMapa(FiltroConsultaLazy filtro) {
        return consultaCustomizada.consultarLocaisMapa(filtro);
    }
    public List<DadosMapa> consultarLocaisMapa(FiltroConsultaLazy filtro, EgressoDao dao) {
       ConsultaCustomizadaController consulta = new ConsultaCustomizadaController(this.dao);
        return consulta.consultarLocaisMapa(filtro);
    }

}
