package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstancia;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author Danillo
 */
@Entity
@DiscriminatorValue("INS")
public class InstanciaAdministrativaUFG implements Serializable, IValidador {

    @ManyToMany(mappedBy = "instanciasAdministrativas")
    private List<ImportacaoDadosEgresso> importacao;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sigla;
    
    private String nome;
    
    private TipoInstancia tipoInstancia;
    
    private LocalDate dataCriacao;
    
    private LocalDate dataEncerramento;
    
    private String emailInstitucional;
    
    private String url;
    
    @ManyToOne
    private Usuario usuario;
    
//    @ManyToMany()
//    private List<Evento> eventos;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoInstancia getTipoInstancia() {
        return tipoInstancia;
    }

    public void setTipoInstancia(TipoInstancia tipoInstancia) {
        this.tipoInstancia = tipoInstancia;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(LocalDate dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getEmailInstitucional() {
        return emailInstitucional;
    }

    public void setEmailInstitucional(String emailInstitucional) {
        this.emailInstitucional = emailInstitucional;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

//    public List<Evento> getEvento() {
//        return eventos;
//    }
//
//    public void setEvento(List<Evento> eventos) {
//        this.eventos = eventos;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstanciaAdministrativaUFG)) {
            return false;
        }
        InstanciaAdministrativaUFG other = (InstanciaAdministrativaUFG) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.InstanciaAdministrativaUFG[ id=" + id + " ]";
    }

    public List<ImportacaoDadosEgresso> getImportacao() {
        return importacao;
    }

    public void setImportacao(List<ImportacaoDadosEgresso> importacao) {
        this.importacao = importacao;
    }

//    public List<Evento> getEventos() {
//        return eventos;
//    }
//
//    public void setEventos(List<Evento> eventos) {
//        this.eventos = eventos;
//    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(sigla, "sigla");
        validador.validarNaoNulo(nome, "nome");
        validador.validarNaoNulo(tipoInstancia, "tipo de instância");
        validador.validarNaoNulo(dataCriacao, "data de criação");
        validador.validarNaoNulo(emailInstitucional, "email institucional");
        validador.validarNaoNulo(url, "url");
        return validador.getInconsistencias();
    }
    
}
