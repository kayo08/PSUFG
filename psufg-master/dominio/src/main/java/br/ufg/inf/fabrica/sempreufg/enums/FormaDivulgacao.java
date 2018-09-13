package br.ufg.inf.fabrica.sempreufg.enums;

public enum FormaDivulgacao {
    MENSAGEM("Mensagem"), 
    NOTICIA("Notícia"), 
    AMBOS("Ambos");

    FormaDivulgacao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
