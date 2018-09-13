package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.LocalizacaoGeografica;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Danillo
 */
public class LocalizacaoGeograficaDao 
        extends GenericDao<LocalizacaoGeografica> 
        implements IEntityDao<LocalizacaoGeografica>{

    @Override
    public LocalizacaoGeografica buscar(Long id) {
        return super.buscar(LocalizacaoGeografica.class, id);
    }

    @Override
    public List<LocalizacaoGeografica> buscarTodos() {
        return super.buscarTodos(LocalizacaoGeografica.class);
    }

    public List<LocalizacaoGeografica> pesquisarPorMunicipio(String municipio) {
        if(municipio==null || municipio.isEmpty()){
            return buscarTodos();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select lg ").
                append("from LocalizacaoGeografica lg ").
                append("where lg.municipio like '").
                append(municipio).
                append("%'");
        EntityManager em = ConnectionFactory.obterManager();
        Query query = em.createQuery(sb.toString());
        return query.getResultList();
    }

}
