/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.dao.utils;

import br.ufg.inf.fabrica.sempreufg.dominio.FiltroConsulta;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author auf
 */
public class FiltroConsultaLazy implements Serializable {

    private static final long serialVersionUID = 1L;

    private int primeiroRegistro;
    private int quantidadeRegistros;
    private String propriedadeOrdenacao;
    private boolean ascendente;
    private Class classePrincipalDaConsulta;
    private String prefixoDaClassePrincipal;
    
    private List<FiltroConsulta> filtrosConsulta;

    public FiltroConsultaLazy() {
    }

    public FiltroConsultaLazy(int primeiroRegistro, int quantidadeRegistros, String propriedadeOrdenacao, boolean ascendente, Class classePrincipalDaConsulta, String prefixoDaClassePrincipal, List<FiltroConsulta> filtrosConsulta) {
        this.primeiroRegistro = primeiroRegistro;
        this.quantidadeRegistros = quantidadeRegistros;
        this.propriedadeOrdenacao = propriedadeOrdenacao;
        this.ascendente = ascendente;
        this.classePrincipalDaConsulta = classePrincipalDaConsulta;
        this.prefixoDaClassePrincipal = prefixoDaClassePrincipal;
        this.filtrosConsulta = filtrosConsulta;
    }

    
    public int getPrimeiroRegistro() {
        return primeiroRegistro;
    }

    public void setPrimeiroRegistro(int primeiroRegistro) {
        this.primeiroRegistro = primeiroRegistro;
    }

    public int getQuantidadeRegistros() {
        return quantidadeRegistros;
    }

    public void setQuantidadeRegistros(int quantidadeRegistros) {
        this.quantidadeRegistros = quantidadeRegistros;
    }

    public String getPropriedadeOrdenacao() {
        return propriedadeOrdenacao;
    }

    public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
        this.propriedadeOrdenacao = propriedadeOrdenacao;
    }

    public boolean isAscendente() {
        return ascendente;
    }

    public void setAscendente(boolean ascendente) {
        this.ascendente = ascendente;
    }

    public List<FiltroConsulta> getFiltrosConsulta() {
        return filtrosConsulta;
    }

    public void setFiltrosConsulta(List<FiltroConsulta> filtrosConsulta) {
        this.filtrosConsulta = filtrosConsulta;
    }

    public Class getClassePrincipalDaConsulta() {
        return classePrincipalDaConsulta;
    }

    public void setClassePrincipalDaConsulta(Class classePrincipalDaConsulta) {
        this.classePrincipalDaConsulta = classePrincipalDaConsulta;
    }

    public String getPrefixoDaClassePrincipal() {
        return prefixoDaClassePrincipal;
    }

    public void setPrefixoDaClassePrincipal(String prefixoDaClassePrincipal) {
        this.prefixoDaClassePrincipal = prefixoDaClassePrincipal;
    }        

}
