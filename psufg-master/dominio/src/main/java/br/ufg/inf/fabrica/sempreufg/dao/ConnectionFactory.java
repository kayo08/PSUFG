package br.ufg.inf.fabrica.sempreufg.dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author
 */
public class ConnectionFactory {

    private static EntityManager em;
    private static EntityManagerFactory emf;

    public static EntityManager obterManager() {

        if (recriarConexoes()) {
            emf = Persistence.createEntityManagerFactory("psufgPU");
            em = emf.createEntityManager();
        }
        return em;
    }

    private static boolean recriarConexoes() {
        try {
            if (em == null || !em.isOpen()) {
                em = emf.createEntityManager();
            }
            em.createQuery("select u.id from Usuario u").getResultList();
            return false;
        } catch (Exception e) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Conexões perdidas e sendo recriadas! ", e.getClass());
            return true;
        }
    }

    public static EntityManager obterManagerNova() {
        emf = Persistence.createEntityManagerFactory("psufgPU");
        em = emf.createEntityManager();
        return em;
    }
}
