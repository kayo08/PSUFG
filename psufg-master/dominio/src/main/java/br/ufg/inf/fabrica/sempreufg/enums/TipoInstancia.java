package br.ufg.inf.fabrica.sempreufg.enums;

public enum TipoInstancia {

    REGIONAL("Regional"),
    UNIDADE("Unidade"),
    CURSO("Curso");

    TipoInstancia(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }

}
