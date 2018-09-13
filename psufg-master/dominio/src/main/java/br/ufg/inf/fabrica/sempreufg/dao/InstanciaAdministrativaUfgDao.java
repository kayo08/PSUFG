/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.InstanciaAdministrativaUFG;
import java.util.List;

/**
 *
 * @author auf
 */
public class InstanciaAdministrativaUfgDao
        extends GenericDao<InstanciaAdministrativaUFG>
        implements IEntityDao<InstanciaAdministrativaUFG> {

    @Override
    public InstanciaAdministrativaUFG buscar(Long id) {
        return super.buscar(InstanciaAdministrativaUFG.class, id);
    }

    @Override
    public List<InstanciaAdministrativaUFG> buscarTodos() {
        return super.buscarTodos(InstanciaAdministrativaUFG.class);
    }

    public List<InstanciaAdministrativaUFG> pesquisarPorNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            return buscarTodos();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select ia ").
                append("from InstanciaAdministrativaUFG ia ").
                append("where ia.nome like '").
                append(nome).
                append("%'");
        return super.pesquisarJPQLCustomizada(InstanciaAdministrativaUFG.class, sb.toString());
    }

}
