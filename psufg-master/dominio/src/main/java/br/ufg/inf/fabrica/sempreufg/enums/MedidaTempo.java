package br.ufg.inf.fabrica.sempreufg.enums;

public enum MedidaTempo {
	DIAS("dias"), 
        HORAS("horas"), 
        MINUTOS("minutos");
        
    MedidaTempo(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
