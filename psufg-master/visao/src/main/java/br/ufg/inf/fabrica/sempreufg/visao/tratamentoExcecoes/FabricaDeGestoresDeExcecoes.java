package br.ufg.inf.fabrica.sempreufg.visao.tratamentoExcecoes;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author danillo
 */
public class FabricaDeGestoresDeExcecoes extends ExceptionHandlerFactory {

    private final ExceptionHandlerFactory parent;

    public FabricaDeGestoresDeExcecoes(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new TratadorDeExcecoes(parent.getExceptionHandler());
        return handler;
    }

}
