package br.ufg.inf.fabrica.sempreufg.dominio;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Danillo
 */
@Entity
@DiscriminatorValue("UNI")
public class UnidadeAcademica extends InstanciaAdministrativaUFG{

    @ManyToOne
    private LocalizacaoGeografica localizacaoGeografica;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RegionalUFG regionalUFG;

    @OneToMany(mappedBy = "unidadeAcademica")
    private List<CursoDaUFG> cursos;

    public LocalizacaoGeografica getLocalizacaoGeografica() {
        return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
        this.localizacaoGeografica = localizacaoGeografica;
    }

    public RegionalUFG getRegionalUFG() {
        return regionalUFG;
    }

    public void setRegionalUFG(RegionalUFG regionalUFG) {
        this.regionalUFG = regionalUFG;
    }

    public List<CursoDaUFG> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDaUFG> cursos) {
        this.cursos = cursos;
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
        if (!(object instanceof UnidadeAcademica)) {
            return false;
        }
        UnidadeAcademica other = (UnidadeAcademica) object;
        if ((this.getId() == null && other.getId() != null) || 
                (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.UnidadeAcademica[ id=" + getId() + " ]";
    }

}
