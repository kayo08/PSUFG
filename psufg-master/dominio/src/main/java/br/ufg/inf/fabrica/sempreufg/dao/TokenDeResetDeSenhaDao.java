package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.TokenDeResetDeSenha;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Danillo
 */
public class TokenDeResetDeSenhaDao extends GenericDao<TokenDeResetDeSenha> 
        implements IEntityDao<TokenDeResetDeSenha>{

    @Override
    public TokenDeResetDeSenha buscar(Long id) {
        return super.buscar(TokenDeResetDeSenha.class, id);
    }

    @Override
    public List<TokenDeResetDeSenha> buscarTodos() {
        return super.buscarTodos(TokenDeResetDeSenha.class);
    }

    public TokenDeResetDeSenha buscarPorToken(String token) {
        Class<TokenDeResetDeSenha> klass = TokenDeResetDeSenha.class;
        EntityManager em = ConnectionFactory.obterManagerNova();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select e from ").
                    append(klass.getSimpleName()).
                    append(" e where e.token like '").
                    append(token).
                    append("'");
            TypedQuery query = em.createQuery(sb.toString(), klass);
            return (TokenDeResetDeSenha) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erro ao buscar token :" + klass.toString());
            return null;
        } finally {
            em.close();
        }
    }
    
    
}
