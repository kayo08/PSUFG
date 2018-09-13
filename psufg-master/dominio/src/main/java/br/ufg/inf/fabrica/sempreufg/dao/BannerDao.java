/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.Banner;
import java.util.List;

/**
 *
 * @author auf
 */
public class BannerDao extends GenericDao<Banner> implements IEntityDao<Banner> {

    public List<Banner> consultar(String sqlFiltro, int limite) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT b.* ").
                append("FROM Banner b ");
        if (sqlFiltro != null && !sqlFiltro.isEmpty()) {
            sb.append("WHERE ").append(sqlFiltro);
        }
        sb.append(" ORDER BY b.destacar DESC, b.id DESC");
        if (limite > 0) {
            sb.append("LIMIT ").append(limite);
        }
        return super.pesquisarSQLCustomizada(Banner.class, sb.toString());
    }

    @Override
    public Banner buscar(Long id) {
        return super.buscar(Banner.class, id);
    }

    @Override
    public List<Banner> buscarTodos() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select b ").
                append("from Banner b ").
                append("ORDER BY b.destacar DESC, b.id DESC");
        return super.pesquisarJPQLCustomizada(Banner.class, jpql.toString());
    }

}
