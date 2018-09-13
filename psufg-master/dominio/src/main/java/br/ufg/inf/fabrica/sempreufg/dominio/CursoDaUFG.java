package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.ModalidadeCurso;
import br.ufg.inf.fabrica.sempreufg.enums.Nivel;
import br.ufg.inf.fabrica.sempreufg.enums.TipoResolucao;
import br.ufg.inf.fabrica.sempreufg.enums.Turno;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author auf
 */
@Entity
@DiscriminatorValue("CUR")
public class CursoDaUFG extends InstanciaAdministrativaUFG{

    private Nivel nivel;

    private TipoResolucao tipoDeResolucao;

    private int numeroDaResolucao;
    
    private ModalidadeCurso modalidadeCurso;
    
    private Turno turno;

    @ManyToOne
    private UnidadeAcademica unidadeAcademica;
    
    @ManyToOne(optional = true)
    private AreaConhecimento area;

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public TipoResolucao getTipoDeResolucao() {
        return tipoDeResolucao;
    }

    public void setTipoDeResolucao(TipoResolucao tipoDeResolucao) {
        this.tipoDeResolucao = tipoDeResolucao;
    }

    public int getNumeroDaResolucao() {
        return numeroDaResolucao;
    }

    public void setNumeroDaResolucao(int numeroDaResolucao) {
        this.numeroDaResolucao = numeroDaResolucao;
    }

    public ModalidadeCurso getModalidadeCurso() {
        return modalidadeCurso;
    }

    public void setModalidadeCurso(ModalidadeCurso modalidadeCurso) {
        this.modalidadeCurso = modalidadeCurso;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public UnidadeAcademica getUnidadeAcademica() {
        return unidadeAcademica;
    }

    public void setUnidadeAcademica(UnidadeAcademica unidadeAcademica) {
        this.unidadeAcademica = unidadeAcademica;
    }

    public AreaConhecimento getArea() {
        return area;
    }

    public void setArea(AreaConhecimento area) {
        this.area = area;
    }

    @Override
    public List<String> validar() {
        List<String> inconsistencias = super.validar();
        ValidadorDeDados validador = new ValidadorDeDados(inconsistencias);
        validador.validarNaoNulo(nivel, "nível");
        validador.validarNaoNulo(tipoDeResolucao, "tipo de resolução");
        validador.validarNaoNulo(numeroDaResolucao, "número da resolução");
        validador.validarNaoNulo(modalidadeCurso, "modalidade do curso");
        validador.validarNaoNulo(turno, "turno");
        return validador.getInconsistencias();
    }

    
}