package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class TokenDeResetDeSenha implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Usuario usuario;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataExpiracao;
    
    private boolean jaUtilizado;

    public TokenDeResetDeSenha() {
        Calendar c = Calendar.getInstance();
        Date d = new Date();
        c.setTime(d);
        c.add(Calendar.DATE, +1);
        this.dataExpiracao = c.getTime();
        this.jaUtilizado = false;
    }

    public TokenDeResetDeSenha(String token, Usuario usuario) {
        Calendar c = Calendar.getInstance();
        Date d = new Date();
        c.setTime(d);
        c.add(Calendar.DATE, +1);
        
        this.dataExpiracao = c.getTime();
        this.token = token;
        this.usuario = usuario;
        this.jaUtilizado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isJaUtilizado() {
        return jaUtilizado;
    }

    public void setJaUtilizado(boolean jaUtilizado) {
        this.jaUtilizado = jaUtilizado;
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
        if (!(object instanceof TokenDeResetDeSenha)) {
            return false;
        }
        TokenDeResetDeSenha other = (TokenDeResetDeSenha) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.sempreufg.dominio.TokenDeResetDeSenha[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(usuario, "Usuário");
        validador.validarNaoNuloEVazio(token, "Token");
        return validador.getInconsistencias();
    }
    
}
