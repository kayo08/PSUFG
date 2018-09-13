package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.EscopoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.StatusAprovacaoDivulgacao;
import br.ufg.inf.fabrica.sempreufg.enums.TipoEvento;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Danillo
 */
@Entity
public class Evento implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String assunto;

    private TipoEvento tipoEvento;

    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String descricaoEvento;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampDaSolicitacao;

    private String identificacaoSolicitante;

    private String emailSolicitante;

    private FormaDivulgacao formaDivulgacao;

    private EscopoDivulgacao escopoDivulgacao;

    private LocalDate dataExpiracao;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<AreaConhecimento> areasDeConhecimento;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<InstanciaAdministrativaUFG> instanciasAdministrativas;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "evento")
    private List<AprovacaoDivulgacao> aprovacoes;

    private StatusAprovacaoDivulgacao statusAprovacaoDivulgacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(String descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public Date getTimeStampDaSolicitacao() {
        return timeStampDaSolicitacao;
    }

    public void setTimeStampDaSolicitacao(Date timeStampDaSolicitacao) {
        this.timeStampDaSolicitacao = timeStampDaSolicitacao;
    }

    public String getIdentificacaoSolicitante() {
        return identificacaoSolicitante;
    }

    public void setIdentificacaoSolicitante(String identificacaoSolicitante) {
        this.identificacaoSolicitante = identificacaoSolicitante;
    }

    public String getEmailSolicitante() {
        return emailSolicitante;
    }

    public void setEmailSolicitante(String emailSolicitante) {
        this.emailSolicitante = emailSolicitante;
    }

    public FormaDivulgacao getFormaDivulgacao() {
        return formaDivulgacao;
    }

    public void setFormaDivulgacao(FormaDivulgacao formaDivulgacao) {
        this.formaDivulgacao = formaDivulgacao;
    }

    public EscopoDivulgacao getEscopoDivulgacao() {
        return escopoDivulgacao;
    }

    public void setEscopoDivulgacao(EscopoDivulgacao escopoDivulgacao) {
        this.escopoDivulgacao = escopoDivulgacao;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public List<AreaConhecimento> getAreasDeConhecimento() {
        return this.areasDeConhecimento;
    }

    public void setAreaConhecimento(List<AreaConhecimento> areasDeConhecimento) {
        this.areasDeConhecimento = areasDeConhecimento;
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
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Evento[ id=" + id + " ]";
    }

    public List<InstanciaAdministrativaUFG> getInstanciasAdministrativas() {
        return instanciasAdministrativas;
    }

    public void setInstanciasAdministrativas(List<InstanciaAdministrativaUFG> instanciasAdministrativas) {
        this.instanciasAdministrativas = instanciasAdministrativas;
    }

//    public List<InstanciaAdministrativaUFG> getIntAdministrativa() {
//        return instanciasAdministrativas;
//    }
//
//    public void setIntAdministrativa(List<InstanciaAdministrativaUFG> instanciasAdministrativas) {
//        this.instanciasAdministrativas = instanciasAdministrativas;
//    }
    public List<AprovacaoDivulgacao> getAprovacoes() {
        return aprovacoes;
    }

    public void setAprovacoes(List<AprovacaoDivulgacao> aprovacoes) {
        this.aprovacoes = aprovacoes;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(assunto, "assunto");
        validador.validarNaoNulo(tipoEvento, "tipo de evento");
        validador.validarNaoNulo(descricaoEvento, "descrição do evento");
        validador.validarNaoNulo(timeStampDaSolicitacao, "timestamp da solicitação");
        validador.validarNaoNulo(identificacaoSolicitante, "identificação do solicitante");
        validador.validarNaoNulo(formaDivulgacao, "forma de divulgação");
        validador.validarNaoNulo(escopoDivulgacao, "escopo de divulgação");
        return validador.getInconsistencias();
    }

    public StatusAprovacaoDivulgacao getStatusAprovacaoDivulgacao() {
        return statusAprovacaoDivulgacao;
    }

    public void setStatusAprovacaoDivulgacao(StatusAprovacaoDivulgacao statusAprovacaoDivulgacao) {
        this.statusAprovacaoDivulgacao = statusAprovacaoDivulgacao;
    }

    public String getResumoDescricao() {
        String temp = this.getDescricaoEvento();
        List<String> chaves = new ArrayList<>();
        chaves.add("</iframe");
        chaves.add("</img");
        int pontoIn = 0;

        for (String chave : chaves) {
            do {
                if (temp.contains(chave)) {
                    pontoIn = temp.indexOf(chave.replace("/", ""));
                    temp = temp.replace(temp.substring(pontoIn, temp.indexOf(chave)+chave.length()+1), "");
                }
            } while (temp.contains(chave));

        }
        return temp;
    }
}
