package br.ufg.inf.fabrica.sempreufg.dominio;

import java.util.Date;

/**
 * Classe não pode ser uma entidade jpa. 
 * Uma entidade JPA precisa de ter um construtor público e uma classe que ...
 * ... implementa singleton não possui um construtor público.
 * @author Danillo
 */
public class SempreUFG {
    private String nomeSistema;
    private Date instalacao;

    public String getNomeSistema() {
        return nomeSistema;
    }

    public void setNomeSistema(String nomeSistema) {
        this.nomeSistema = nomeSistema;
    }

    public Date getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Date instalacao) {
        this.instalacao = instalacao;
    }
    
    private SempreUFG() {
    }
    
    public static SempreUFG getInstance() {
        return SempreUFGHolder.INSTANCE;
    }
    
    private static class SempreUFGHolder {

        private static final SempreUFG INSTANCE = new SempreUFG();
    }
}
