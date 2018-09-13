package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.AreaConhecimentoDao;
import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import java.util.List;

/**
 *
 * @author Danillo
 */
public class AreaConhecimentoController {
    private final AreaConhecimentoDao dao;

    public AreaConhecimentoController() {
        this.dao = new AreaConhecimentoDao();
    }
    
    public AreaConhecimento buscar(Long id){
        return dao.buscar(id);
    }
    
    public List<AreaConhecimento> pesquisarPorNome(String nome) {
        return dao.pesquisarPorNome(nome);
    }
    
    public List<AreaConhecimento> buscarTodos() {
        return dao.buscarTodos();
    }
}
