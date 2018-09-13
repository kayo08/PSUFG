package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Danillo
 */
@Entity
public class ImportacaoDadosEgresso implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataExecucao;
    
    private LocalDate inicioPeriodo;
    
    private LocalDate fimPeriodo;
    
    private int quantidadeEgressosRecebidos;
    
    private int quantidadeEgressosImportados;
    
    private int quantidadeEgressosComFalha;
    
    private int quantidadeEgressosReplicados;
    
    @ManyToMany
    private List<Egresso> egressos;
    
    @ManyToMany
    private List<InstanciaAdministrativaUFG> instanciasAdministrativas;
    
    @ManyToOne
    private Usuario usuarioQueAutorizou;

    public List<Egresso> getEgressos() {
        return egressos;
    }

    public void setEgressos(List<Egresso> egressos) {
        this.egressos = egressos;
    }

    public List<InstanciaAdministrativaUFG> getInstanciasAdministrativas() {
        return instanciasAdministrativas;
    }

    public void setInstanciasAdministrativas(List<InstanciaAdministrativaUFG> instanciasAdministrativas) {
        this.instanciasAdministrativas = instanciasAdministrativas;
    }

    public Usuario getUsuarioQueAutorizou() {
        return usuarioQueAutorizou;
    }

    public void setUsuarioQueAutorizou(Usuario usuarioQueAutorizou) {
        this.usuarioQueAutorizou = usuarioQueAutorizou;
    }
    
    public Date getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(Date dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public LocalDate getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(LocalDate inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public LocalDate getFimPeriodo() {
        return fimPeriodo;
    }

    public void setFimPeriodo(LocalDate fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }

    public int getQuantidadeEgressosRecebidos() {
        return quantidadeEgressosRecebidos;
    }

    public void setQuantidadeEgressosRecebidos(int quantidadeEgressosRecebidos) {
        this.quantidadeEgressosRecebidos = quantidadeEgressosRecebidos;
    }

    public int getQuantidadeEgressosImportados() {
        return quantidadeEgressosImportados;
    }

    public void setQuantidadeEgressosImportados(int quantidadeEgressosImportados) {
        this.quantidadeEgressosImportados = quantidadeEgressosImportados;
    }

    public int getQuantidadeEgressosComFalha() {
        return quantidadeEgressosComFalha;
    }

    public void setQuantidadeEgressosComFalha(int quantidadeEgressosComFalha) {
        this.quantidadeEgressosComFalha = quantidadeEgressosComFalha;
    }

    public int getQuantidadeEgressosReplicados() {
        return quantidadeEgressosReplicados;
    }

    public void setQuantidadeEgressosReplicados(int quantidadeEgressosReplicados) {
        this.quantidadeEgressosReplicados = quantidadeEgressosReplicados;
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
        if (!(object instanceof ImportacaoDadosEgresso)) {
            return false;
        }
        ImportacaoDadosEgresso other = (ImportacaoDadosEgresso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.ImportacaoDadosEgresso[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(dataExecucao, "timestamp da execução da importação");
        validador.validarNaoNulo(inicioPeriodo, "inicio do período importado");
        validador.validarNaoNulo(fimPeriodo, "final do período importado");
        validador.validarNaoNulo(quantidadeEgressosRecebidos, "quantidade de egressos recebidos");
        validador.validarNaoNulo(quantidadeEgressosImportados, "quantidade de egressos importados");
        validador.validarNaoNulo(quantidadeEgressosComFalha, "quantidade de egressos com falha de dados");
        validador.validarNaoNulo(quantidadeEgressosReplicados, "quantidade de egressos replicados");
        return validador.getInconsistencias();
    }
    
}
