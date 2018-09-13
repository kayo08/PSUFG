/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.utils.UsuarioContato;
import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import br.ufg.inf.fabrica.sempreufg.scheduler.GerenciadorEmail;

/**
 *
 * @author auf
 */
public class GerenciadorEmailController {
    private final GerenciadorEmail gerenciador;

    public GerenciadorEmailController() {
        this.gerenciador = new GerenciadorEmail();
    }
    
    public void enviarMensagensDiariasPorAprovacao(AprovacaoDivulgacao aprov){
        this.gerenciador.criarEmailsEventoAprovado(aprov);
        this.gerenciador.enviarEmails();
    }
    
    public void enviarMensagensBloqueioUsuario(Egresso egresso){
        this.gerenciador.criarEmailUsuarioBloqueado(egresso);
        this.gerenciador.enviarEmails();
    }
    
    public void enviarEmailContato(UsuarioContato usuarioContato){
        this.gerenciador.criarEmailContato(usuarioContato);
        this.gerenciador.enviarEmails();
    }
    
}
