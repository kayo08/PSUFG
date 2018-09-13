package br.ufg.inf.fabrica.sempreufg.dao;

import java.util.List;

/**
 *
 * @author Danillo
 */
public interface IEntityDao<T> {

    /**
     *
     * @param id
     * @return
     */
    public T buscar(Long id);

    /**
     *
     * @return
     */
    public List<T> buscarTodos();
    
}
