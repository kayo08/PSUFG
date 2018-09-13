package br.ufg.inf.fabrica.sempreufg.dominio;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Danillo
 */
@Entity
@DiscriminatorValue("PLG")
public class ParametroLogging extends Parametro{

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroLogging)) {
            return false;
        }
        ParametroLogging other = (ParametroLogging) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.ParametroLogging[ id=" + getId() + " ]";
    }
    
}
