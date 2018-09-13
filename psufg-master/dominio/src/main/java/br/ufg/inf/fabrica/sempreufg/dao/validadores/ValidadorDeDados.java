package br.ufg.inf.fabrica.sempreufg.dao.validadores;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Danillo
 * @param <T>
 */
public class ValidadorDeDados{

    protected final List<String> inconsistencias;

    public ValidadorDeDados() {
        inconsistencias = new ArrayList<>();
    }
    
    public ValidadorDeDados(List inconsistencias){
        this.inconsistencias = inconsistencias;
    }
    
    protected void limparListaDeValidacao(){
        this.inconsistencias.clear();
    }
    
    public void adicionarInconsistencia(String inconsistencia){
        this.inconsistencias.add(inconsistencia);
    }
    
    public void adicionarInconsistencia(List inconsistencias){
        this.inconsistencias.addAll(inconsistencias);
    }
    
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO GERAL
     * *******************************************************************
     * *******************************************************************
     */
    public void validarNaoNulo(Object value, String nomeCampo){
        if(value==null){
            inconsistencias.add(nomeCampo + " não informado(a)");
        }
    }
    public void validarListaNaoNulaEVazia(List value, String nomeCampo)   {        
        if(value==null || value.isEmpty()){
            inconsistencias.add(nomeCampo + " não informado(a)");
        }
    }
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO DE ATRIBUTOS STRING
     * *******************************************************************
     * *******************************************************************
     */
    public void validarNaoNuloEVazio(String value, String nomeCampo){
        if(value==null || value.isEmpty()){
            inconsistencias.add(nomeCampo + " não informado(a)");
        }
    }
    
    public void garantirTamanhoMinimo(String value, String nomeCampo, int tamanhoMinimo){
        if(value.length()<tamanhoMinimo){
            inconsistencias.add(nomeCampo + " menor que " + tamanhoMinimo + " caracteres");
        }
    }
    
    public List<String> getInconsistencias() {
        return inconsistencias;
    }
    
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO DE ATRIBUTOS NUMÉRICOS
     * *******************************************************************
     * *******************************************************************
     */
    
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO DE ATRIBUTOS DATA
     * *******************************************************************
     * *******************************************************************
     */
    
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO DE ATRIBUTOS DE RELACIONAMETNO 1:N
     * *******************************************************************
     * *******************************************************************
     */
    
    /**
     * *******************************************************************
     * *******************************************************************
     * VALIDAÇÃO DE ATRIBUTOS DE RELACIONAMETNO N:1
     * *******************************************************************
     * *******************************************************************
     */
    
}
