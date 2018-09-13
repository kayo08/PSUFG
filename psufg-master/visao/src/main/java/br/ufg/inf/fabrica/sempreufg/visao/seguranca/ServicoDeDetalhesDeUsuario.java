package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import br.ufg.inf.fabrica.sempreufg.controller.UsuarioController;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Danillo
 */
public class ServicoDeDetalhesDeUsuario implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            return null;
        }
        UsuarioController controller = new UsuarioController();
        Usuario usuario = controller.buscarPorCPF(username);
        if(usuario!=null){
            return new DetalheDoUsuario(usuario);
        }
        return null;
    }

}
