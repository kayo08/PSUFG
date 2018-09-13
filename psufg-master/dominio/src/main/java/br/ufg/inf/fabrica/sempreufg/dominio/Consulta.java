package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.EgressoDao;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.VisibilidadeConsulta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author auf
 */
@Entity
public class Consulta implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    private String nomeUsuario;

    private String descricaoDaConsulta;
    private Long idUsuario;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampCriacao;

    private VisibilidadeConsulta visibilidadeDaConsulta;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "consulta", cascade = CascadeType.ALL)
    private List<FiltroConsulta> filtrosConsulta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoDaConsulta() {
        return descricaoDaConsulta;
    }

    public void setDescricaoDaConsulta(String descricaoDaConsulta) {
        this.descricaoDaConsulta = descricaoDaConsulta;
    }

    public String getNomeUsuario() {
        EgressoDao dao = new EgressoDao();
        Usuario u = dao.buscar(Usuario.class, this.getIdUsuario());
        if (u instanceof Egresso) {
            return u.getNomeSocial() == null || u.getNomeSocial().length() < 2 ? ((Egresso) u).getNomeOficial() : u.getNomeSocial();
        } else {
            return u.getNomeSocial() == null || u.getNomeSocial().length() < 2 ? u.getEmailPrincipal() : u.getNomeSocial();
        }
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getTimeStampCriacao() {
        return timeStampCriacao;
    }

    public void setTimeStampCriacao(Date timeStampCriacao) {
        this.timeStampCriacao = timeStampCriacao;
    }

    public VisibilidadeConsulta getVisibilidadeDaConsulta() {
        return visibilidadeDaConsulta;
    }

    public void setVisibilidadeDaConsulta(VisibilidadeConsulta visibilidadeDaConsulta) {
        this.visibilidadeDaConsulta = visibilidadeDaConsulta;
    }

    public List<FiltroConsulta> getFiltrosConsulta() {
        return this.filtrosConsulta == null ? new ArrayList<>() : this.filtrosConsulta;
    }

    public void setFiltrosConsulta(List<FiltroConsulta> filtrosConsulta) {
        this.filtrosConsulta = filtrosConsulta;
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(descricaoDaConsulta, "Descrição da consulta");
        validador.validarNaoNulo(idUsuario, "ID do usuário que criou a consulta");
        validador.validarNaoNulo(timeStampCriacao, "Data de criação da consulta");
        return validador.getInconsistencias();
    }

}
