package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author Danillo
 */
public enum TipoEvento {

    NOTICIAS("Notícias"), 
    PALESTRA("Palestra"), 
    CURSO("Curso"), 
    OPORTUNIDADE_DE_EMPREGO("Oportunidade De Emprego"), 
    DIVERSOS("Diversos");
        
    TipoEvento(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
