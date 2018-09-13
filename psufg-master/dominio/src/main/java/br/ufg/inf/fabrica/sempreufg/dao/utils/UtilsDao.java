/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao.utils;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Danillo
 */
public class UtilsDao {
    
    public static <T> T getSingleResult(Query query){
        if(query==null){
            return null;
        }
        try{
            return (T) query.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
        
}
