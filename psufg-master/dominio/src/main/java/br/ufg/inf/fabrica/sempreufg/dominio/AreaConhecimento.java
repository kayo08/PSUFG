package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
public class AreaConhecimento implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nomeArea;
    
    private int codArea;
    
    @OneToMany(mappedBy = "areaPai", cascade = CascadeType.ALL)
    private List<AreaConhecimento> subAreas;
    
    @ManyToOne
    private AreaConhecimento areaPai;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public List<AreaConhecimento> getSubAreas() {
        if(subAreas==null){
            subAreas = new ArrayList<>();
        }
        return subAreas;
    }

    public void setSubAreas(List<AreaConhecimento> subAreas) {
        this.subAreas = subAreas;
    }

    public AreaConhecimento getAreaPai() {
        return areaPai;
    }

    public void setAreaPai(AreaConhecimento areaPai) {
        this.areaPai = areaPai;
    }

    public void addSubArea(AreaConhecimento subArea){
        subArea.setAreaPai(this);
        getSubAreas().add(subArea);
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
        if (!(object instanceof AreaConhecimento)) {
            return false;
        }
        AreaConhecimento other = (AreaConhecimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.AreaConhecimento[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(nomeArea, "Nome da área");
        return validador.getInconsistencias();
    }
    
}
