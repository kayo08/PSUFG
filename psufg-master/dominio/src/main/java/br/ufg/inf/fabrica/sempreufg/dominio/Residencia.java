package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author auf
 */

@Entity
public class Residencia implements Serializable, IValidador {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private LocalDate dataDeIncio;
    
    private LocalDate dataDeFim;
    
    private String logradouro;
   
    private String numero;
    
    private String bairro;
    
    private String CEP;
    
    private String complemento;
    
    @ManyToOne 
    private LocalizacaoGeografica localizacaoGeografica;
    
    @ManyToOne     
    private Egresso egresso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataDeIncio() {
        return dataDeIncio;
    }

    public void setDataDeIncio(LocalDate dataDeIncio) {
        this.dataDeIncio = dataDeIncio;
    }

    public LocalDate getDataDeFim() {
        return dataDeFim;
    }

    public void setDataDeFim(LocalDate dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(egresso, "egresso");
        validador.validarNaoNulo(localizacaoGeografica, "localização geográfica");
        validador.validarNaoNulo(dataDeIncio, "data de início");
        validador.validarNaoNulo(dataDeFim, "data de conclusão");
        validador.validarNaoNuloEVazio(logradouro, "logradouro");
        validador.validarNaoNuloEVazio(bairro, "bairro");
        validador.validarNaoNuloEVazio(CEP, "cep");
        return validador.getInconsistencias();
    }

    public LocalizacaoGeografica getLocalizacaoGeografica() {
        return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
        this.localizacaoGeografica = localizacaoGeografica;
    }
    
}
