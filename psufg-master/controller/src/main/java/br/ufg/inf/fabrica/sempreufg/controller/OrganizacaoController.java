package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.GenericDao;
import br.ufg.inf.fabrica.sempreufg.dominio.Organizacao;
import java.util.List;

/**
 *
 * @author Danillo
 */
public class OrganizacaoController {
    
    public List<Organizacao> buscarTodas(){
        GenericDao<Organizacao> dao = new GenericDao<>();
        return dao.buscarTodos(Organizacao.class);
    }
    
    public List<Organizacao> buscarPorNome(String nome){
        GenericDao<Organizacao> dao = new GenericDao<>();
        StringBuilder sb = new StringBuilder();
        sb.append("razaoSocial like '").
                append(nome).
                append("%'");
        return dao.filtrarPorUnicoAtributo(Organizacao.class, sb.toString());
    }

    public Organizacao buscar(Long id) {
        GenericDao<Organizacao> dao = new GenericDao<>();
        return dao.buscar(Organizacao.class, id);
    }
    
}
