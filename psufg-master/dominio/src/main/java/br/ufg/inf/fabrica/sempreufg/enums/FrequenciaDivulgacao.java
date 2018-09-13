package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author Danillo
 */
public enum FrequenciaDivulgacao {
    POR_EVENTO("Por evento"), 
    DIARIA("Diária"), 
    SEMANAL("Semanal"), 
    MENSAL("Mensal"),
    NAO_RECEBE("Não recebe");

    FrequenciaDivulgacao(String value) {
        this.value = value;
    }
    
    private final String value;

    public String getValue() {
        return value;
    }

}
