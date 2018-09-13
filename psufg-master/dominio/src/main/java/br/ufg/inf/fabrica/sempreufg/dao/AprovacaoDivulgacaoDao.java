/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao;

import br.ufg.inf.fabrica.sempreufg.dominio.AprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.dominio.Evento;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author auf
 */
public class AprovacaoDivulgacaoDao extends GenericDao<AprovacaoDivulgacao>
        implements IEntityDao<AprovacaoDivulgacao> {

    @Override
    public AprovacaoDivulgacao buscar(Long id) {
        return super.buscar(AprovacaoDivulgacao.class, id);
    }

    @Override
    public List<AprovacaoDivulgacao> buscarTodos() {
        return super.buscarTodos(AprovacaoDivulgacao.class);
    }

    public List<AprovacaoDivulgacao> buscarPorResponsavel(Usuario usuario) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT ap FROM AprovacaoDivulgacao ap ");
        jpql.append("          WHERE ap.instanciaAdministrativaUFG.usuario.id = ").append(usuario.getId());
        return super.pesquisarJPQLCustomizada(AprovacaoDivulgacao.class, jpql.toString());
    }

    public List<AprovacaoDivulgacao> buscarPorEvento(Evento evento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT ap FROM AprovacaoDivulgacao ap ");
        jpql.append("          WHERE ap.evento.id = ").append(evento.getId());
        return super.pesquisarJPQLCustomizada(AprovacaoDivulgacao.class, jpql.toString());
    }
             
    public Integer numAprovacaoRejeicaoDoEvento(Evento evento) {
        StringBuilder jpql = new StringBuilder();
        
        jpql.append(" SELECT COUNT(ap) ");
        jpql.append("        FROM AprovacaoDivulgacao ap WHERE ap.evento.id = ").append(evento.getId());
        EntityManager em = ConnectionFactory.obterManager();
        try {
            Query query = em.createQuery(jpql.toString());
            return (Integer) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erro ao executar pesquisa SQL customizada na entidade :" + evento.getClass());
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "Erro ao executar pesquisa SQL customizada na entidade! ", e);
            return 0;
        } finally {
            em.close();
        }
    }

//    public List<AprovacaoDivulgacao> buscarPorEventoInstancia(Evento evento, InstanciaAdministrativaUFG ins){
//        StringBuilder jpql = new StringBuilder();
//        jpql.append("SELECT ap FROM AprovacaoDivulgacao ap ");
//        jpql.append("          WHERE ap.evento.id = ").append(evento.getId());
//        jpql.append("       AND ap.instanciaAdministrativaUFG.id = ").append(ins.getId());
//        
//        return super.pesquisarJPQLCustomizada(AprovacaoDivulgacao.class, jpql.toString());                                
//    }
//    public static void main(String[] args) {
//        Evento test = new Evento();
//        test.setId((long)1);
//        boolean resu = numAprovacaoRejeicaoDoEvento(test);
//        System.exit(0);
//    }
    }
