/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.InstanciaAdministrativaUfgDao;
import br.ufg.inf.fabrica.sempreufg.dominio.AvaliacaoDoCursoPeloEgresso;
import br.ufg.inf.fabrica.sempreufg.dominio.InstanciaAdministrativaUFG;
import java.util.List;

/**
 *
 * @author auf
 */
public class InstanciaAdministrativaUfgController {

    private final InstanciaAdministrativaUfgDao dao;

    public InstanciaAdministrativaUfgController() {
        this.dao = new InstanciaAdministrativaUfgDao();
    }

    public InstanciaAdministrativaUFG buscar(Long id) {
        return dao.buscar(id);
    }

    public List<InstanciaAdministrativaUFG> pesquisarPorNome(String nome) {
        return dao.pesquisarPorNome(nome);
    }

    public List<InstanciaAdministrativaUFG> buscarTodos() {
        return dao.buscarTodos();
    }
    
     public List<String> pesquisarValoresCampo(String campo){
        return dao.pesquisarValoresCampo("InstanciaAdministrativaUFG", campo);
    }

}
