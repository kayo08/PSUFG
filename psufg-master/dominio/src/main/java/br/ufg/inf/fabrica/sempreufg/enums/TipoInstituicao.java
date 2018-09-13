package br.ufg.inf.fabrica.sempreufg.enums;

public enum TipoInstituicao {

    FEDERAL("Federal"),
    ESTADUAL("Estadual"),
    MUNICIPAL("Municipal"),
    PARTICULAR("Particular");

    TipoInstituicao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
