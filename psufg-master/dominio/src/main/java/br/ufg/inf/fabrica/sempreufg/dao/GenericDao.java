package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.DadosMapa;
import br.ufg.inf.fabrica.sempreufg.dominio.Egresso;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Danillo
 * @param <T>
 */
public class GenericDao<T> implements IDao<T> {

    @Override
    public T salvar(T entidade) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(entidade);
            tx.commit();

            if (entidade instanceof Egresso) {
                Long id = null;
                id = ((Egresso) entidade).getId();
                em.detach(entidade);
                entidade = (T) em.find(Egresso.class, id);
            }
            return entidade;
        } catch (Exception e) {
            System.out.println("Não foi possível salvar entidade :" + entidade.getClass().toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Não foi possível salvar entidade ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void remover(T entidade) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(em.contains(entidade) ? entidade : em.merge(entidade));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Não foi possível remover entidade :" + entidade.getClass().toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Não foi possível remover entidade ", e);
        } finally {
            em.close();
        }
    }

    @Override
    public T buscar(Class klass, Long id) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            return (T) em.find(klass, id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao buscar entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> buscarTodos(Class klass
    ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select e from ").
                    append(klass.getSimpleName()).
                    append(" e ");
            TypedQuery query = em.createQuery(sb.toString(), klass);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar todos da entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao buscar todos da entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> filtrarPorUnicoAtributo(Class klass, String filtroChaveValor
    ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select e from ").
                    append(klass.getSimpleName()).
                    append(" e where e.").
                    append(filtroChaveValor);
            TypedQuery query = em.createQuery(sb.toString(), klass);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao executar filtro na entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar filtro na entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> pesquisarJPQLCustomizada(Class klass, String jpql
    ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, klass);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada na entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa JPQL customizada na entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public T pesquisarUmJPQLCustomizada(Class klass, String jpql ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, klass);
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada na entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa JPQL customizada na entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> pesquisarJPQLLazyCustomizada(Class klass, String jpql, int firstResult, int maxResults
    ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, klass).setFirstResult(firstResult).setMaxResults(maxResults);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada na entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa JPQL customizada na entidade! ", e);
            return null;
        } finally {
            System.out.println(System.currentTimeMillis());
            em.close();
        }
    }

    @Override
    public List<T> pesquisarSQLCustomizada(Class klass, String sql
    ) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            Query query = em.createNativeQuery(sql, klass);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa SQL customizada na entidade :" + klass.toString());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa SQL customizada na entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> pesquisarValoresCampo(String klass, String campo) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT k.").append(campo).append(" FROM ").append(klass).append(" k ORDER BY k.").append(campo).append("  ASC");
            Query query = em.createQuery(jpql.toString());
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada na entidade :" + klass);
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa SQL customizada na entidade! ", e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public int pesquisarJPQLCustomizadaNumeroDeRegistros(String jpql) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            Query query = em.createQuery(jpql);
            Long resu = (Long) query.getSingleResult();
            return resu.intValue();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada Numero de Registros");
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa JPQL customizada na entidade! ", e);
            return 0;
        } finally {
            System.out.println(System.currentTimeMillis());
            em.close();
        }
    }

    public List<DadosMapa> consultarLocaisMapa(String jpql) {
        EntityManager em = ConnectionFactory.obterManager();
        try {
            Query query = em.createQuery(jpql);

            List<DadosMapa> dados = new ArrayList<>();
            List<Object[]> results = query.getResultList();
            for (int i = 0; i < results.size(); i++) {
                Object o[] = results.get(i);
                DadosMapa dado = new DadosMapa((String) o[1], (Long) o[0], (float) o[2], (float) o[3]);
                dados.add(dado);
            }
            return dados;
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa JPQL customizada dados mapa");
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa JPQL customizada na entidade! ", e);
            return null;
        } finally {
            System.out.println(System.currentTimeMillis());
            em.close();
        }
    }
}
