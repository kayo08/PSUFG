/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.Consulta;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeConsulta;
import java.util.List;

/**
 *
 * @author auf
 */
public class ConsultaDao extends GenericDao<Consulta> implements IEntityDao<Consulta> {

    @Override
    public Consulta buscar(Long id) {
        return super.buscar(Consulta.class, id);
    }

    @Override
    public List<Consulta> buscarTodos() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select c ").
                append("from Consulta c ").
                append("ORDER c.id DESC");
        return super.pesquisarJPQLCustomizada(Consulta.class, jpql.toString());
    }

    public List buscarConsultasDoUsuario(Long idUsuario) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c ").
                append(" FROM Consulta c ").
                append(" WHERE c.idUsuario = ").append(idUsuario).
                append(" OR c.visibilidadeDaConsulta = ").append(VisibilidadeConsulta.PUBLICA.ordinal()).
                append(" ORDER BY c.visibilidadeDaConsulta DESC, c.id DESC");
        return super.pesquisarJPQLCustomizada(Consulta.class, jpql.toString());
    }
    
    
    
}
