package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstituicao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Danillo
 */
@Entity
public class InstituicaoEnsinoMedio implements Serializable, IValidador {

    @OneToMany(mappedBy = "instituicaoEnsinoMedio")
    private List<HistoricoIEM> historicos;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private TipoInstituicao tipoInstituicao;

    @ManyToOne
    private LocalizacaoGeografica localizacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoInstituicao getTipoInstituicao() {
        return tipoInstituicao;
    }

    public void setTipoInstituicao(TipoInstituicao tipoInstituicao) {
        this.tipoInstituicao = tipoInstituicao;
    }

    public LocalizacaoGeografica getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(LocalizacaoGeografica localizacao) {
        this.localizacao = localizacao;
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
        if (!(object instanceof InstituicaoEnsinoMedio)) {
            return false;
        }
        InstituicaoEnsinoMedio other = (InstituicaoEnsinoMedio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public List<HistoricoIEM> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<HistoricoIEM> historicos) {
        this.historicos = historicos;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.InstituicaoEnsinoMedio[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(nome, "nome da instituição de ensino médio");
        validador.validarNaoNulo(tipoInstituicao, "tipo da instituição de ensino médio");
        return validador.getInconsistencias();
    }

}
