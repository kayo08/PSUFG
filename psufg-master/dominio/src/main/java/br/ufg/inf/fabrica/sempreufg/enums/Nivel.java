package br.ufg.inf.fabrica.sempreufg.enums;

public enum Nivel {
    BACHARELADO("Bacharelado"),
    LICENCIATURA("Licenciatura"),
    APERFEICOAMENTO("Aperfeiçoamento"),
    ESPECIALIZACAO("Especialização"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");

    private final String value;

    Nivel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
