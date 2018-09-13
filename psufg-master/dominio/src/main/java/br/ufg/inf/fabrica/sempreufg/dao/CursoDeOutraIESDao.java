package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dao.utils.UtilsDao;
import br.ufg.inf.fabrica.sempreufg.dominio.CursoDeOutraIES;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Danillo
 */
public class CursoDeOutraIESDao
        extends GenericDao<CursoDeOutraIES>
        implements IEntityDao<CursoDeOutraIES> {

    @Override
    public CursoDeOutraIES buscar(Long id) {
        return super.buscar(CursoDeOutraIES.class, id);
    }

    @Override
    public List<CursoDeOutraIES> buscarTodos() {
        return super.buscarTodos(CursoDeOutraIES.class);
    }

    public CursoDeOutraIES buscarCursoSuperior(String nomeIES, String nomeCurso) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CursoDeOutraIES c where c.id.nomeDoCurso like '").
                append(nomeCurso).
                append("'").
                append(" and c.id.iesDoCurso like '").append(nomeIES).append("'");
        EntityManager em = ConnectionFactory.obterManager();
        Query query = em.createQuery(sb.toString());
        return UtilsDao.getSingleResult(query);
    }
    
    public List<CursoDeOutraIES> buscarOutraIes(String iesDoCurso){        
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CursoDeOutraIES c where c.id.iesDoCurso like '%").
                append(iesDoCurso).append("%'");
        EntityManager em = ConnectionFactory.obterManager();
        Query query = em.createQuery(sb.toString());
        return query.getResultList();
    }
}
