package br.ufg.inf.fabrica.sempreufg.enums;

public enum TipoProgramaAcademico {
    INICIACAO_CIENTIFICA("Iniciação científica"),
    MONITORIA("Monitoria"),
    EXTENSAO("Extensão"),
    INTERCAMBIO("Intercâmbio");

    TipoProgramaAcademico(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
