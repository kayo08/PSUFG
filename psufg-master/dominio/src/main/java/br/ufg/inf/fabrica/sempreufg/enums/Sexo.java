package br.ufg.inf.fabrica.sempreufg.enums;

/**
 * Created by user1 on 09/10/2016.
 */
public enum Sexo {
    MASCULINO("Masculino"),
    FEMININO("Feminino");

    Sexo(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
