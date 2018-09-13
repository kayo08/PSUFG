package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.FormaIngressoInstituicao;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Atuacao implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Egresso egresso;
    
    @ManyToOne(cascade = {
        CascadeType.PERSIST, 
        CascadeType.MERGE, 
        CascadeType.REFRESH}) 
    private Organizacao organizacao;
    
    private LocalDate dataInicio;
    
    private LocalDate dataFim;
    
    private FormaIngressoInstituicao formaIngresso;
    
    private float rendaMensalMedia;
    
    private int satisfacaoRenda;
    
    private int perspectivaFuturoArea;
    
    private String comentario;
    
    @ManyToOne 
    private AreaConhecimento areaConhecimento;
    
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

    public Organizacao getOrganizacao() {
        if(this.organizacao==null){
            this.organizacao = new Organizacao();
        }
        return organizacao;
    }

    public void setOrganizacao(Organizacao organizacao) {
        this.organizacao = organizacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public FormaIngressoInstituicao getFormaIngresso() {
        return formaIngresso;
    }

    public void setFormaIngresso(FormaIngressoInstituicao formaIngresso) {
        this.formaIngresso = formaIngresso;
    }

    public float getRendaMensalMedia() {
        return rendaMensalMedia;
    }

    public void setRendaMensalMedia(float rendaMensalMedia) {
        this.rendaMensalMedia = rendaMensalMedia;
    }

    public int getSatisfacaoRenda() {
        return satisfacaoRenda;
    }

    public void setSatisfacaoRenda(int satisfacaoRenda) {
        this.satisfacaoRenda = satisfacaoRenda;
    }

    public int getPerspectivaFuturoArea() {
        return perspectivaFuturoArea;
    }

    public void setPerspectivaFuturoArea(int perspectivaFuturoArea) {
        this.perspectivaFuturoArea = perspectivaFuturoArea;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
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
        if (!(object instanceof Atuacao)) {
            return false;
        }
        Atuacao other = (Atuacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Atuacao[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(dataInicio, "Data de inicio");
        validador.validarNaoNulo(formaIngresso, "forma de ingresso");
        validador.validarNaoNulo(rendaMensalMedia, "Renda mensal média");
        validador.validarNaoNulo(satisfacaoRenda, "Satisfação com a renda");
        validador.validarNaoNulo(perspectivaFuturoArea, "Perspectiva futura com a área");
        return validador.getInconsistencias();
    }
    
}
