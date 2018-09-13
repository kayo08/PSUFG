package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.NaturezaOrganizacao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Danillo
 */
@Entity
public class Organizacao implements Serializable, Cloneable, IValidador{

    @OneToMany(mappedBy = "organizacao")
    private List<Atuacao> atuacoes;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String razaoSocial;
    
    private String enderecoComercial;
    
    private NaturezaOrganizacao natureza;
    
    private String paginaWeb;

    @ManyToOne
    private LocalizacaoGeografica localizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(String enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public NaturezaOrganizacao getNatureza() {
        return natureza;
    }

    public void setNatureza(NaturezaOrganizacao natureza) {
        this.natureza = natureza;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public LocalizacaoGeografica getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoGeografica localizacao) {
        this.localizacao = localizacao;
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
        if (!(object instanceof Organizacao)) {
            return false;
        }
        Organizacao other = (Organizacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Organizacao[ id=" + id + " ]";
    }

    public List<Atuacao> getAtuacoes() {
        return atuacoes;
    }

    public void setAtuacaos(List<Atuacao> atuacoes) {
        this.atuacoes = atuacoes;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(razaoSocial, "razão social");
        validador.validarNaoNulo(natureza, "natureza");
        return validador.getInconsistencias();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}
