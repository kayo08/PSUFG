/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.AprovacaoDivulgacaoDao;
import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.List;

/**
 *
 * @author auf
 */
public class AprovacaoDivulgacaoController {

    private final AprovacaoDivulgacaoDao dao;

    public AprovacaoDivulgacaoController() {
        dao = new AprovacaoDivulgacaoDao();
    }

     public List<AprovacaoDivulgacao> buscarTodos() {
        return dao.buscarTodos();
    }

    public AprovacaoDivulgacao buscarPorID(Long id) {
        return dao.buscar(id);
    }

    public List<AprovacaoDivulgacao> buscarPorResponsavel(Usuario usuario){
        return dao.buscarPorResponsavel(usuario);
    }
    
    public List<AprovacaoDivulgacao> buscarPorEvento(Evento evento){
        return dao.buscarPorEvento(evento);
    }
    
     public List<String> salvar(AprovacaoDivulgacao aprovacao) {
        List<String> inconsistencias = aprovacao.validar();
        if (inconsistencias.isEmpty()) {
            dao.salvar(aprovacao);
        }
        return inconsistencias;
    }
    
    public Integer buscarNumAprovacoesEvento(Evento evento){
        return dao.numAprovacaoRejeicaoDoEvento(evento);
    }
}

