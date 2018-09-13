package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
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
public class FiltroConsulta implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String campo;
    private String regra;
    private String valor;
    private String valorLabel;
    private String operador;

    @ManyToOne
    private Consulta consulta;

    public FiltroConsulta() {
    }

    public FiltroConsulta(String campo, String regra, String valor, String operador, String valorLabel) {
        this.campo = campo;
        this.regra = regra;
        this.valor = valor;
        this.operador = operador;
        this.valorLabel = valorLabel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getRegra() {
        return regra;
    }

    public void setRegra(String regra) {
        this.regra = regra;
    }

    public String getValor() {
        return valor;
    }

    public String getValorLabel() {
        return valorLabel;
    }

    public void setValorLabel(String valorLabel) {
        this.valorLabel = valorLabel;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getOperador() {
        return operador;
    }

    public String getOperadorLabel() {
        try {
            if (getOperador().equals("AND")) {
                return "E";
            } else if (getOperador().equals("OR")) {
                return "OU";
            }
        } catch (NullPointerException npe) {
            return "";
        }
        return "";
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(campo, "Campo consulta");
        validador.validarNaoNulo(regra, "Regra consulta");
        validador.validarNaoNulo(valor, "Valor consulta");
        return validador.getInconsistencias();
    }

}
