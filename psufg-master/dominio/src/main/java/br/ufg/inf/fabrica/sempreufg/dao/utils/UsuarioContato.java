/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao.utils;

/**
 *
 * @author auf
 */
public class UsuarioContato {

    private String nome;
    private String email;
    private String assunto;
    private String mensagem;

    public UsuarioContato() {
    }

    public UsuarioContato(String nomeContato, String emailContato, String assuntoContato, String mensagemContato) {
        this.nome = nomeContato;
        this.email = emailContato;
        this.assunto = assuntoContato;
        this.mensagem = mensagemContato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
