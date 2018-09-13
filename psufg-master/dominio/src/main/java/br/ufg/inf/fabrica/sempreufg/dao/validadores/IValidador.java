package br.ufg.inf.fabrica.sempreufg.dao.validadores;

import java.util.List;

/**
 *
 * @author Danillo
 */
public interface IValidador{
    
    /**
     * Validar restrições de domínio
     * @return 
     */
    public List<String> validar();
    
    
}
