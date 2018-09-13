package br.ufg.inf.fabrica.sempreufg.enums;

public enum MotivoReprovacaoEvento {
    NAO_RELEVANTE_A_ORGANIZACAO("naoRelevanteAOrganizacao");

    MotivoReprovacaoEvento(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
