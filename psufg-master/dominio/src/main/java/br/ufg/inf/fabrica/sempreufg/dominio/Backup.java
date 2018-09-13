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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Danillo
 */
@Entity
public class Backup implements Serializable, IValidador{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identificador;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inicio;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fim;
    
    private String localArmazenamento;
    
    @ManyToOne
    private Usuario usuarioSolicitouBackup;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public String getLocalArmazenamento() {
        return localArmazenamento;
    }

    public void setLocalArmazenamento(String localArmazenamento) {
        this.localArmazenamento = localArmazenamento;
    }

    public Usuario getUsuarioSolicitouBackup() {
        return usuarioSolicitouBackup;
    }

    public void setUsuarioSolicitouBackup(Usuario usuarioSolicitouBackup) {
        this.usuarioSolicitouBackup = usuarioSolicitouBackup;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Backup)) {
            return false;
        }
        Backup other = (Backup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Backup[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(identificador, "identificador");
        validador.validarNaoNulo(inicio, "timestamp de inicio");
        validador.validarNaoNulo(fim, "timestamp de finalização");
        validador.validarNaoNulo(localArmazenamento, "local de armazenamento");
        return validador.getInconsistencias();
    }
    
}
