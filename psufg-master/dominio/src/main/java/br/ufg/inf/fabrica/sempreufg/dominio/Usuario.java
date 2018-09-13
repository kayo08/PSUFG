package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.enums.FrequenciaDivulgacao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Danillo
 */
@Entity
@DiscriminatorValue("USU")
public class Usuario implements Serializable, IValidador {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String emailPrincipal;

    private String senha;
    
    @Transient
    private String novaSenha;

    private String nomeOficial;
    
    private String nomeSocial;

    private String cpf;

    private String fotoPessoal;

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    private FrequenciaDivulgacao frequenciaDivulgacao;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampCadastramento;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampUltimaUtilizacao;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date timeStampExclusaoLogica;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InstanciaAdministrativaUFG> instanciasAdministrativas;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Papel> papeis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailPrincipal() {
        return emailPrincipal;
    }

    public void setEmailPrincipal(String emailPrincipal) {
        this.emailPrincipal = emailPrincipal;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getNomeOficial() {
        return nomeOficial;
    }

    public void setNomeOficial(String nomeOficial) {
        this.nomeOficial = nomeOficial;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFotoPessoal() {
        return fotoPessoal;
    }

    public void setFotoPessoal(String fotoPessoal) {
        this.fotoPessoal = fotoPessoal;
    }

    public FrequenciaDivulgacao getFrequenciaDivulgacao() {
        return frequenciaDivulgacao;
    }

    public void setFrequenciaDivulgacao(FrequenciaDivulgacao frequenciaDivulgacao) {
        this.frequenciaDivulgacao = frequenciaDivulgacao;
    }

    public Date getTimeStampCadastramento() {
        return timeStampCadastramento;
    }

    public void setTimeStampCadastramento(Date timeStampCadastramento) {
        this.timeStampCadastramento = timeStampCadastramento;
    }

    public Date getTimeStampUltimaUtilizacao() {
        return timeStampUltimaUtilizacao;
    }

    public void setTimeStampUltimaUtilizacao(Date timeStampUltimaUtilizacao) {
        this.timeStampUltimaUtilizacao = timeStampUltimaUtilizacao;
    }

    public Date getTimeStampExclusaoLogica() {
        return timeStampExclusaoLogica;
    }

    public void setTimeStampExclusaoLogica(Date timeStampExclusaoLogica) {
        this.timeStampExclusaoLogica = timeStampExclusaoLogica;
    }

    public List<InstanciaAdministrativaUFG> getInstanciasAdministrativas() {
        return instanciasAdministrativas;
    }

    public void setInstanciasAdministrativas(List<InstanciaAdministrativaUFG> instanciasAdministrativas) {
        this.instanciasAdministrativas = instanciasAdministrativas;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.Usuario[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {     
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNuloEVazio(nomeOficial, "nome oficial");
        validador.validarNaoNuloEVazio(emailPrincipal, "email principal");
        validador.validarNaoNuloEVazio(senha, "senha");
        
        validador.adicionarInconsistencia(validarSenha());
        
        validador.validarNaoNuloEVazio(cpf, "CPF");
        validador.validarNaoNulo(frequenciaDivulgacao, "frequência de divulgação");
        validador.validarNaoNulo(timeStampCadastramento, "timestamp de cadastramento");
        return validador.getInconsistencias();
    }
    
    public List<String> validarSenha(){
        List<String> inconsistencias = new ArrayList<>();
        ValidadorDeSenha validadorSenha = new ValidadorDeSenha();
        boolean senhaValida = (novaSenha==null 
                || novaSenha.isEmpty() 
                || validadorSenha.validar(novaSenha));
        if(!senhaValida){
            String inconsistencia = "Senha deve ter no mínimo 6 caracteres, "
                    + "possuir no mínimo um digito númerico "
                    + "e um dígito não númerico";
            inconsistencias.add(inconsistencia);
        }
        return inconsistencias;
    }

    public void criptografar(){
        if(novaSenha== null || novaSenha.isEmpty()){
            return;
        }
        String senhaCriptografada =
                BCrypt.hashpw(novaSenha, BCrypt.gensalt()); // Padrão = 10 log_rounds
        this.senha = senhaCriptografada;
        this.novaSenha = null;
    }
    
}

class ValidadorDeSenha {

    private final static String NUMEROS_E_CARACTERES = "[!-~]+";
    private final static String SO_NUMEROS = "[0-9]+";
    private final static String SO_CARACTERES = "([!-/]|[:-~])+";

    public boolean validar(String senha) {
        if(senha!=null 
                && senha.length()>5 
                && senha.matches(NUMEROS_E_CARACTERES)
                && !(senha.matches(SO_NUMEROS)) 
                && !(senha.matches(SO_CARACTERES))){
            return true;
        }
        return false;
    }

}