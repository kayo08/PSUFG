package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.Motivacao;
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
 * @author auf
 */
@Entity
public class AvaliacaoDoCursoPeloEgresso implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate momentoAvaliacao;

    private Motivacao motivacaoParaEscolha;

    private int satisfacaoCurso;

    private int conceitoGlobal;

    private int preparacaoMercado;

    private int melhoriaComunicacao;

    private int capacidadeEticaResponsabilidade;

    private int capacidadeHabilidadesAreaConhecimento;

    private String comentario;

    @ManyToOne
    private HistoricoNaUFG historicoUFG;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMomentoAvaliacao() {
        if(momentoAvaliacao == null){
            this.setMomentoAvaliacao(LocalDate.now());
        }
        return momentoAvaliacao;
    }

    public void setMomentoAvaliacao(LocalDate momentoAvaliacao) {
        this.momentoAvaliacao = momentoAvaliacao;
    }

    public Motivacao getMotivacaoParaEscolha() {
        return motivacaoParaEscolha;
    }

    public void setMotivacaoParaEscolha(Motivacao motivacaoParaEscolha) {
        this.motivacaoParaEscolha = motivacaoParaEscolha;
    }

    public int getSatisfacaoCurso() {
        return satisfacaoCurso;
    }

    public void setSatisfacaoCurso(int satisfacaoCurso) {
        this.satisfacaoCurso = satisfacaoCurso;
    }

    public int getConceitoGlobal() {
        return conceitoGlobal;
    }

    public void setConceitoGlobal(int conceitoGlobal) {
        this.conceitoGlobal = conceitoGlobal;
    }

    public int getPreparacaoMercado() {
        return preparacaoMercado;
    }

    public void setPreparacaoMercado(int preparacaoMercado) {
        this.preparacaoMercado = preparacaoMercado;
    }

    public int getMelhoriaComunicacao() {
        return melhoriaComunicacao;
    }

    public void setMelhoriaComunicacao(int melhoriaComunicacao) {
        this.melhoriaComunicacao = melhoriaComunicacao;
    }

    public int getCapacidadeEticaResponsabilidade() {
        return capacidadeEticaResponsabilidade;
    }

    public void setCapacidadeEticaResponsabilidade(int capacidadeEticaResponsabilidade) {
        this.capacidadeEticaResponsabilidade = capacidadeEticaResponsabilidade;
    }

    public int getCapacidadeHabilidadesAreaConhecimento() {
        return capacidadeHabilidadesAreaConhecimento;
    }

    public void setCapacidadeHabilidadesAreaConhecimento(int capacidadeHabilidadesAreaConhecimento) {
        this.capacidadeHabilidadesAreaConhecimento = capacidadeHabilidadesAreaConhecimento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public HistoricoNaUFG getHistoricoUFG() {
        return historicoUFG;
    }

    public void setHistoricoUFG(HistoricoNaUFG historicoUFG) {
        this.historicoUFG = historicoUFG;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(momentoAvaliacao, "momento da avaliação");
        validador.validarNaoNulo(motivacaoParaEscolha, "motivação para escolha");
        validador.validarNaoNulo(satisfacaoCurso, "satisfação com o curso");
        validador.validarNaoNulo(conceitoGlobal, "conceito global");
        validador.validarNaoNulo(preparacaoMercado, "preparação do mercado");
        validador.validarNaoNulo(melhoriaComunicacao, "melhoria da comunicação");
        validador.validarNaoNulo(capacidadeEticaResponsabilidade,
                "capacidade em fomentar ética e responsabilidade");
        validador.validarNaoNulo(capacidadeHabilidadesAreaConhecimento,
                "capacidade em melhorar habilidades");
        return validador.getInconsistencias();
    }

}
