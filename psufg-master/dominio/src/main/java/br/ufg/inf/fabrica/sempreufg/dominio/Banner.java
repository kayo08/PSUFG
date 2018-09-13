package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author auf
 */
@Entity
public class Banner implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tituloChamada;

    private String linkNoticia;

    private String pathImagem;

    @Transient
    private String nomeImagem;

    private boolean destacar;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTituloChamada() {
        return tituloChamada;
    }

    public void setTituloChamada(String tituloChamada) {
        this.tituloChamada = tituloChamada;
    }

    public String getPathImagem() {
        return pathImagem;
    }

    public void setPathImagem(String pathImagem) {
        this.pathImagem = pathImagem;
    }

    public String getNomeImagem() {
        //Para evitar erros onde o path do banner aponta para uma pasta de outro SO
        String separador = pathImagem.contains("/") ? "/" : "\\";        
        return pathImagem.substring(pathImagem.lastIndexOf(separador) + 1, pathImagem.length());
    }

    public String getLinkNoticia() {
        return linkNoticia;
    }

    public void setLinkNoticia(String linkNoticia) {
        this.linkNoticia = linkNoticia;
    }

    public boolean isDestacar() {
        return destacar;
    }

    public void setDestacar(boolean destacar) {
        this.destacar = destacar;
    }

    public Date getTimeStampCriacao() {
        return timeStampCriacao;
    }

    public void setTimeStampCriacao(Date timeStampCriacao) {
        this.timeStampCriacao = timeStampCriacao;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(tituloChamada, "Título chamada do banner");
        validador.validarNaoNuloEVazio(pathImagem, "nome da imagem do banner");
        validador.validarNaoNuloEVazio(linkNoticia, "Url que leva a pagina relacionada ao banner");        
        return validador.getInconsistencias();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Banner other = (Banner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Banner[ id=" + id + " ]";
    }
}
