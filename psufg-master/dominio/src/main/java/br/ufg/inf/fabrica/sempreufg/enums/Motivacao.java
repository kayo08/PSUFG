package br.ufg.inf.fabrica.sempreufg.enums;

/**
 * Created by ${Rafael_Canedo} on 09/10/2016.
 */
public enum Motivacao {
    QUALIDADE_OU_REPUTACAO__IES("Qualidade ou reputação IES"),
    QUALIDADE_OU_REPUTACAO_DO_CURSO("Qualidade ou reputação do curso"), 
    GRATUIDADE("Gratuidade"), 
    OUTRA("Outra");
    
    Motivacao(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
}
