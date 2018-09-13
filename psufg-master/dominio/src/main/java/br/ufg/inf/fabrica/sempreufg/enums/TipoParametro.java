package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author Danillo
 */
public enum TipoParametro {
    
    BACKUP("backup"),
    LOG("log"),
    GLOBAL("global");
        
    TipoParametro(String value) {
        this.value = value;
    }
    
    private final String value;

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public String getValue() {
        return value;
    }
    
}
