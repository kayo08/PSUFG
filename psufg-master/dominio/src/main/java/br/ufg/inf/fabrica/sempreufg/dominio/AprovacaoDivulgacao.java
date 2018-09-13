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
 * @author Danillo
 */
@Entity
public class AprovacaoDivulgacao implements Serializable, IValidador{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean aprovada;
    
    private String parecerSobreDivulgacao;
    
    private LocalDate dataDaAvaliacao;
    
    @ManyToOne
    private Evento evento;
    
    @ManyToOne
    private InstanciaAdministrativaUFG instanciaAdministrativaUFG;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAprovada() {
        return aprovada;
    }

    public void setAprovada(boolean aprovada) {
        this.aprovada = aprovada;
    }

    public String getParecerSobreDivulgacao() {
        return parecerSobreDivulgacao;
    }

    public void setParecerSobreDivulgacao(String parecerSobreDivulgacao) {
        this.parecerSobreDivulgacao = parecerSobreDivulgacao;
    }

    public LocalDate getDataDaAvaliacao() {
        return dataDaAvaliacao;
    }

    public void setDataDaAvaliacao(LocalDate dataDaAvaliacao) {
        this.dataDaAvaliacao = dataDaAvaliacao;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public InstanciaAdministrativaUFG getInstanciaAdministrativaUFG() {
        return instanciaAdministrativaUFG;
    }

    public void setInstanciaAdministrativaUFG(InstanciaAdministrativaUFG instanciaAdministrativaUFG) {
        this.instanciaAdministrativaUFG = instanciaAdministrativaUFG;
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
        if (!(object instanceof AprovacaoDivulgacao)) {
            return false;
        }
        AprovacaoDivulgacao other = (AprovacaoDivulgacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.AprovacaoDivulgacao[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(parecerSobreDivulgacao, "Parecer sobre divulgação");
        return validador.getInconsistencias();
    }
    
}
