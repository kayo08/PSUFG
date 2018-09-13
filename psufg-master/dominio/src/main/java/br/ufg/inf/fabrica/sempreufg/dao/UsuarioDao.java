/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.List;

/**
 *
 * @author auf
 */
public class UsuarioDao extends GenericDao<Usuario> implements IEntityDao<Usuario> {

    @Override
    public Usuario buscar(Long id) {
        return super.buscar(Usuario.class, id);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return super.buscarTodos(Usuario.class);
    }

    public Usuario buscarPorCPF(String CPF) {        
        StringBuilder jpql = new StringBuilder();
        jpql.append("Select u from Usuario u where u.cpf like '").append(CPF).append("'");        
        return super.pesquisarUmJPQLCustomizada(Usuario.class, jpql.toString());
    }
}
