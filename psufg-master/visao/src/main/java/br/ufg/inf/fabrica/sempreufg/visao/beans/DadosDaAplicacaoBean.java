package br.ufg.inf.fabrica.sempreufg.visao.beans;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "dadosDaAplicacaoBean")
@ApplicationScoped
public class DadosDaAplicacaoBean {

    public DadosDaAplicacaoBean() {
    }

    public String getUrlAplicacao() {
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String protocolo = context.getRequestScheme();
        String dominio = context.getRequestServerName();
        int porta = context.getRequestServerPort();
        String aplicacao = context.getApplicationContextPath();
        
        StringBuilder sb = new StringBuilder();
        sb.append(protocolo).append("://").
                append(dominio).append(":").
                append(porta).append(aplicacao);
        return sb.toString();
    }

}
