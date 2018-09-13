package br.ufg.inf.fabrica.sempreufg.enums;

public enum NaturezaOrganizacao {
    PUBLICO("Público"),
    PRIVADO("Privado"),
    TRABALHO_AUTONOMO("Trabalho autônomo");

    NaturezaOrganizacao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
