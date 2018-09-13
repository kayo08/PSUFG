package br.ufg.inf.fabrica.sempreufg.visao.tratamentoExcecoes;

import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author danillo
 */
public class TratadorDeExcecoes extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;


    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Map requestMap = facesContext.getExternalContext().getRequestMap();
    final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

    TratadorDeExcecoes(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    //Responsável por manipular as exceções do JSF
    @Override
    public void handle() throws FacesException {

        Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable exception = context.getException();
            
            System.out.println(exception.getMessage());
            
            try {                
                requestMap.put("exceptionMessage", exception.getMessage());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O sistema se recuperou de um erro inesperado.", ""));
                String target = null;
                if(exception instanceof ViewExpiredException){
                    target = "/index.xhtml";
                } else {
                    target = "/error.xhtml";
                }
                navigationHandler.handleNavigation(facesContext, null, target);
                facesContext.renderResponse();
            } finally {
                // Remove a exeção da fila
                iterator.remove();
            }
        }
        // Manipula o erro
        getWrapped().handle();
    }

}
