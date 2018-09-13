/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author auf
 */
public class MensagensTela {

    public MensagensTela() {
    }

    public void criar(FacesMessage.Severity tipoMensagem, String mensagem, String pagina) {
        
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage(tipoMensagem, mensagem, "");
            contexto.addMessage(pagina, msg);
            contexto.getExternalContext().getFlash().setKeepMessages(true);
            
        } catch (Exception ex) {
            Logger.getLogger(MensagensTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
