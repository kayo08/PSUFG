package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author auf
 */
@Entity
public class HistoricoNaUFG implements Serializable, IValidador{
   
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int numeroDeMatricula;

    private MesesAno mesDeInicio;

    private int anoDeInicio;

    private MesesAno mesDeFim;

    private int anoDeFim;

    private String tituloDoTrabalhoFinal;

    @ManyToOne(optional = false)
    private Egresso egresso;
    
    @ManyToOne
    private CursoDaUFG cursoDaUFG;
    
    @OneToMany(mappedBy = "historico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RealizacaoDeProgramaAcademico> realizacaoProgramasAcademicos;

    @OneToMany(mappedBy = "historicoUFG", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvaliacaoDoCursoPeloEgresso> avaliacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroDeMatricula() {
        return numeroDeMatricula;
    }

    public void setNumeroDeMatricula(int numeroDeMatricula) {
        this.numeroDeMatricula = numeroDeMatricula;
    }

    public MesesAno getMesDeInicio() {
        return mesDeInicio;
    }

    public void setMesDeInicio(MesesAno mesDeInicio) {
        this.mesDeInicio = mesDeInicio;
    }

    public int getAnoDeInicio() {
        return anoDeInicio;
    }

    public void setAnoDeInicio(int anoDeInicio) {
        this.anoDeInicio = anoDeInicio;
    }

    public MesesAno getMesDeFim() {
        return mesDeFim;
    }

    public void setMesDeFim(MesesAno mesDeFim) {
        this.mesDeFim = mesDeFim;
    }

    public int getAnoDeFim() {
        return anoDeFim;
    }

    public void setAnoDeFim(int anoDeFim) {
        this.anoDeFim = anoDeFim;
    }

    public String getTituloDoTrabalhoFinal() {
        return tituloDoTrabalhoFinal;
    }

    public void setTituloDoTrabalhoFinal(String tituloDoTrabalhoFinal) {
        this.tituloDoTrabalhoFinal = tituloDoTrabalhoFinal;
    }

    public CursoDaUFG getCursoDaUFG() {
        return cursoDaUFG;
    }

    public void setCursoDaUFG(CursoDaUFG cursoDaUFG) {
        this.cursoDaUFG = cursoDaUFG;
    }

    public List<RealizacaoDeProgramaAcademico> getRealizacaoProgramasAcademicos() {
        if(this.realizacaoProgramasAcademicos==null){
            this.realizacaoProgramasAcademicos = new ArrayList<>();
        }
        return realizacaoProgramasAcademicos;
    }

    public void setRealizacaoProgramasAcademicos(List<RealizacaoDeProgramaAcademico> realizacaoProgramasAcademicos) {
        this.realizacaoProgramasAcademicos = realizacaoProgramasAcademicos;
    }

    public List<AvaliacaoDoCursoPeloEgresso> getAvaliacoes() {
        if(this.avaliacoes==null){
            this.avaliacoes = new ArrayList<>();
        }
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoDoCursoPeloEgresso> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public void addAvaliacao(AvaliacaoDoCursoPeloEgresso avaliacao) {
        List<AvaliacaoDoCursoPeloEgresso> avals = getAvaliacoes();
        avals.add(avaliacao);
        avaliacao.setHistoricoUFG(this);
    }
    
    public void addPrograma(RealizacaoDeProgramaAcademico programa) {
        List<RealizacaoDeProgramaAcademico> progs = getRealizacaoProgramasAcademicos();
        progs.add(programa);
        programa.setHistorico(this);
    }
    
    
    
    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(numeroDeMatricula, "número de matrícula");
        validador.validarNaoNulo(mesDeInicio, "mês de início");
        validador.validarNaoNulo(anoDeInicio, "ano de início");
        validador.validarNaoNulo(mesDeFim, "mês de conclusão");
        validador.validarNaoNulo(anoDeFim, "ano de conclusão");
        return validador.getInconsistencias();
    }

}
