/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author auf
 */
public enum StatusAprovacaoDivulgacao {
    SEM_APROVACAO("Nenhuma"),
    PARCIAL("Parcial"),
    CONCLUIDA("Concluída");

    StatusAprovacaoDivulgacao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
