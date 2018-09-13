package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Danillo
 */
@Entity
public class HistoricoIEM implements Serializable {

    @ManyToOne
    private Egresso egresso;
    
    @ManyToOne(cascade = {
        CascadeType.PERSIST, 
        CascadeType.MERGE, 
        CascadeType.REFRESH})
    private InstituicaoEnsinoMedio instituicaoEnsinoMedio;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private MesesAno mesInicio;
    
    private int anoInicio;
    
    private MesesAno mesConclusao;
    
    private int anoConclusao;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public InstituicaoEnsinoMedio getInstituicaoEnsinoMedio() {
        return instituicaoEnsinoMedio;
    }

    public void setInstituicaoEnsinoMedio(InstituicaoEnsinoMedio instituicaoEnsinoMedio) {
        this.instituicaoEnsinoMedio = instituicaoEnsinoMedio;
    }

    public MesesAno getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(MesesAno mesInicio) {
        this.mesInicio = mesInicio;
    }

    public int getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(int anoInicio) {
        this.anoInicio = anoInicio;
    }

    public MesesAno getMesConclusao() {
        return mesConclusao;
    }

    public void setMesConclusao(MesesAno mesConclusao) {
        this.mesConclusao = mesConclusao;
    }

    public int getAnoConclusao() {
        return anoConclusao;
    }

    public void setAnoConclusao(int anoConclusao) {
        this.anoConclusao = anoConclusao;
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
        if (!(object instanceof HistoricoIEM)) {
            return false;
        }
        HistoricoIEM other = (HistoricoIEM) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.HistoricoIEM[ id=" + id + " ]";
    }
    
}
