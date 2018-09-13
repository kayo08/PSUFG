package br.ufg.inf.fabrica.sempreufg.dominio;

import java.io.Serializable;

/**
 *
 * @author murilo
 */


public class DadosMapa implements Serializable{

    public DadosMapa(String cidade, Long quantidadeEgressos, float latitude, float longitude) {
        this.cidade = cidade;
        this.quantidadeEgressos = quantidadeEgressos;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DadosMapa() {
    }
    
    
    
    String cidade;
    
    Long quantidadeEgressos;
    
    float latitude;
    
    float longitude;

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Long getQuantidadeEgressos() {
        return quantidadeEgressos;
    }

    public void setQuantidadeEgressos(Long quantidadeEgressos) {
        this.quantidadeEgressos = quantidadeEgressos;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
 
    
}
