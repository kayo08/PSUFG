/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.utils.FiltroConsultaLazy;
import java.io.Serializable;
import java.util.List;


public class ConsultaLazyController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final ConsultaCustomizadaController consultaCustomizada = new ConsultaCustomizadaController();

    @SuppressWarnings("unchecked")
    public List filtrados(FiltroConsultaLazy filtro) {
        return consultaCustomizada.criarConsultarJpqlLazy(filtro);
    }

    public int quantidadeFiltrados(FiltroConsultaLazy filtro) {        
        return consultaCustomizada.criarConsultaNumeroRegistros(filtro);
    }        
}
