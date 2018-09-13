/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.EventoDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.List;

/**
 *
 * @author auf
 */
public class EventoController {

    private final EventoDao dao;

    public EventoController() {
        dao = new EventoDao();
    }

    public List<Evento> consultar(String sqlFiltro, int limite) {
        return dao.consultar(sqlFiltro, limite);
    }

    public List<String> atualizar(Evento evento) {
        List<String> inconsistencias = evento.validar();
        if (inconsistencias.isEmpty()) {
            dao.salvar(evento);
        }
        return inconsistencias;
    }

    public List<Evento> buscarTodos() {
        return dao.buscarTodos();
    }

    public boolean excluirEvento(Evento evento) {
        try {
            dao.remover(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Evento buscarPorID(Long id){
        return dao.buscar(id);
    }
    
    public List<Evento> buscarEventosUsuarioResponsavel(Usuario usuarioLogado) {
        return dao.buscarEventosUsuarioResponsavel(usuarioLogado);
    }
    
    public List<Evento> buscarEventosUsuarioResponsavelEsperandoAprovacao(Usuario usuarioLogado) {
        return dao.buscarEventosUsuarioResponsavelEsperandoAprovacao(usuarioLogado);
    }
    
}
