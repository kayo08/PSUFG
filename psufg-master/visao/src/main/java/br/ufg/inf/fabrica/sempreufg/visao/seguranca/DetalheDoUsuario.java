package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import br.ufg.inf.fabrica.sempreufg.dominio.Papel;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Danillo
 */
public class DetalheDoUsuario implements UserDetails{

    private final Usuario usuario;
    private final List<Autoridade> papeis;
    
    public DetalheDoUsuario(Usuario usuario){
        if(usuario==null){
            throw new InstantiationError("Usuário obrigatório na instanciação de UserDetails na biblioteca de segurança");
        }
        this.usuario = usuario;
        this.papeis = new ArrayList<>();
        for (Papel papel : usuario.getPapeis()) {
            Autoridade autoridade = new Autoridade(papel);
            papeis.add(autoridade);
        }
    }
    
    public Usuario getUsuario(){
        return this.usuario;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.papeis;
    }

    @Override
    public String getPassword() {
        return this.usuario.getSenha();
//        return "$2a$10$8m8LaQPogGE4ilcVMMc2ruwHDXpcIwxckrQne6w4naMoBwHJwi0Re"; //senha
    }

    @Override
    public String getUsername() {
        return usuario.getCpf();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return this.usuario.getTimeStampExclusaoLogica() != null;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
