/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.BannerDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Banner;
import java.util.List;

/**
 *
 * @author auf
 */
public class BannerController {

    private final BannerDao dao;

    public BannerController() {
        dao = new BannerDao();
    }

    public List<Banner> consultar(String sqlFiltro, int limite) {
        return dao.consultar(sqlFiltro, limite);
    }

    public List<String> atualizar(Banner banner) {
        List<String> inconsistencias = banner.validar();
        if (inconsistencias.isEmpty()) {
            dao.salvar(banner);
        }
        return inconsistencias;
    }

    public List<Banner> buscarTodos() {
        return dao.buscarTodos();
    }

    public boolean excluirBanner(Banner banner) {
        try {
            dao.remover(banner);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Banner buscarPorID(Long id){
        return dao.buscar(id);
    }
        
    
}
