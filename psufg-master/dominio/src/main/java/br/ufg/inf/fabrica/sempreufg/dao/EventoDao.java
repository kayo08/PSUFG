/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author auf
 */
public class EventoDao extends GenericDao<Evento> implements IEntityDao<Evento> {

    public List<Evento> consultar(String sqlFiltro, int limite) {
        StringBuilder sb = new StringBuilder();
        sb.append("select e.* ").
                append("from Evento e ").
                append("where ");
        if (sqlFiltro != null && !sqlFiltro.isEmpty()) {
            sb.append(sqlFiltro);
        }
        return super.pesquisarSQLCustomizada(Evento.class, sb.toString());
    }

    public List<Evento> buscarEventosUsuarioResponsavel(Usuario usuarioLogado) {
        int idEscopoEgresso = 0;
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT e FROM Evento e ");
        jpql.append("                  JOIN e.instanciasAdministrativas ins ");
        jpql.append(" WHERE ins.usuario.id  = ").append(usuarioLogado.getId());
        jpql.append(" AND e.escopoDivulgacao  = ").append(idEscopoEgresso);
        return super.pesquisarJPQLCustomizada(Evento.class, jpql.toString());
    }

    public List<Evento> buscarEventosUsuarioResponsavelEsperandoAprovacao(Usuario usuarioLogado) {
        AprovacaoDivulgacaoDao apDao = new AprovacaoDivulgacaoDao();
        List<Evento> eventos = buscarEventosUsuarioResponsavel(usuarioLogado);
        List<AprovacaoDivulgacao> aprovacoesDoEvento;
        List<Evento> eventosRetorno = new ArrayList<>();
        boolean adicionar = true;
        
        if (eventos == null || eventos.isEmpty()) {
            return null;
        }
        
        for (Evento evento : eventos) {
            aprovacoesDoEvento = apDao.buscarPorEvento(evento);
            if (aprovacoesDoEvento != null && aprovacoesDoEvento.size() > 0) {
                for (AprovacaoDivulgacao aprov : aprovacoesDoEvento) {
                    if (aprov.getInstanciaAdministrativaUFG().getUsuario().equals(usuarioLogado)) {                        
                        adicionar = false;
                        break;
                    }
                }               
            }
            if(adicionar){
                eventosRetorno.add(evento);                
            }
            adicionar = true;            
        }
        return eventosRetorno;
    }

    @Override
    public Evento buscar(Long id) {
        return super.buscar(Evento.class, id);
    }

    @Override
    public List<Evento> buscarTodos() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select e ").
                append("from Evento e ").
                append("ORDER BY e.id DESC");
        return super.pesquisarJPQLCustomizada(Evento.class, jpql.toString());
    }

}
