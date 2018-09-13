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
public enum AssuntoEmailContato {
    CRITICA("Crítica"),
    DUVIDA("Dúvida"),
    ELOGIO("Elogio"),
    SUGESTAO("Sugestão"),
    OUTROS("Outros");

    AssuntoEmailContato(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
