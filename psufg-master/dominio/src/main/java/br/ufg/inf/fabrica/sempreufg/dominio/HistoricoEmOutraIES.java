package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author Danillo
 */
@Entity
public class HistoricoEmOutraIES implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private MesesAno mesInicio;

    private int anoInicio;

    private MesesAno mesFim;

    private int anoFim;

    @ManyToOne()
    private Egresso egresso;

    @ManyToOne(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE,
        CascadeType.REFRESH})
    @JoinColumns({
        @JoinColumn(
            name = "nomeDoCurso",
            referencedColumnName = "nomeDoCurso"),
        @JoinColumn(
            name = "iESDoCurso",
            referencedColumnName = "iESDoCurso")
    })
    private CursoDeOutraIES cursoDeOutraIES;

    public HistoricoEmOutraIES() {
        this.cursoDeOutraIES = new CursoDeOutraIES();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MesesAno getMesFim() {
        return mesFim;
    }

    public void setMesFim(MesesAno mesFim) {
        this.mesFim = mesFim;
    }

    public int getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(int anoFim) {
        this.anoFim = anoFim;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public CursoDeOutraIES getCursoDeOutraIES() {
        return cursoDeOutraIES;
    }

    public void setCursoDeOutraIES(CursoDeOutraIES cursoDeOutraIES) {
        this.cursoDeOutraIES = cursoDeOutraIES;
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
        if (!(object instanceof HistoricoEmOutraIES)) {
            return false;
        }
        HistoricoEmOutraIES other = (HistoricoEmOutraIES) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.HistoricoEmOutraIES[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(anoInicio, "ano de início");
        validador.validarNaoNulo(mesInicio, "mês de início");
        validador.validarNaoNulo(anoFim, "ano de conclusão");
        validador.validarNaoNulo(mesFim, "mês de conclusão");
        return validador.getInconsistencias();
    }

}
