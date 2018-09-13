package br.ufg.inf.fabrica.sempreufg.enums;

public enum FormaIngressoInstituicao {
    CONCURSO_PUBLICO("Concurso público"),
    SELECAO_INTERNA("Seleção interna"),
    INDICACAO("Indicação"),
    VOLUNTARIO("Voluntário"),
    OUTRA("Outra");

    FormaIngressoInstituicao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
