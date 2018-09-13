package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.TipoProgramaAcademico;
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
public class RealizacaoDeProgramaAcademico implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private TipoProgramaAcademico tipoProgramaAcademico;

    private LocalDate dataDeInicio;

    private LocalDate dataDeFim;

    private String descricao;

    @ManyToOne
    private HistoricoNaUFG historico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProgramaAcademico getTipoProgramaAcademico() {
        return tipoProgramaAcademico;
    }

    public void setTipoProgramaAcademico(TipoProgramaAcademico tipoProgramaAcademico) {
        this.tipoProgramaAcademico = tipoProgramaAcademico;
    }

    public LocalDate getDataDeInicio() {
        return dataDeInicio;
    }

    public void setDataDeInicio(LocalDate dataDeInicio) {
        this.dataDeInicio = dataDeInicio;
    }

    public LocalDate getDataDeFim() {
        return dataDeFim;
    }

    public void setDataDeFim(LocalDate dataDeFim) {
        this.dataDeFim = dataDeFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public HistoricoNaUFG getHistorico() {
        return historico;
    }

    public void setHistorico(HistoricoNaUFG historico) {
        this.historico = historico;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(tipoProgramaAcademico, "tipo de programa acadêmico");
        validador.validarNaoNulo(dataDeInicio, "data de início");
        validador.validarNaoNulo(dataDeFim, "data de conclusão");
        validador.validarNaoNuloEVazio(descricao, "descrição");
        return validador.getInconsistencias();
    }

}
