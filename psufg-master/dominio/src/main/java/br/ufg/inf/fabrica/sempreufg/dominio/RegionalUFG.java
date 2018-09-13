package br.ufg.inf.fabrica.sempreufg.dominio;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Danillo
 */
@Entity
@DiscriminatorValue("REG")
public class RegionalUFG extends InstanciaAdministrativaUFG{

    @ManyToOne (fetch = FetchType.EAGER)
    private LocalizacaoGeografica localizacao;

    public LocalizacaoGeografica getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoGeografica localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegionalUFG)) {
            return false;
        }
        RegionalUFG other = (RegionalUFG) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.RegionalUFG[ id=" + getId() + " ]";
    }

}
