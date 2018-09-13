package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author danillo
 */
public class SegurancaUtil {

    /**
     * Retorna o usuário logado
     *
     * @return
     */
    public static Usuario retornarUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if (principal instanceof DetalheDoUsuario) {
            DetalheDoUsuario detalhe = (DetalheDoUsuario) principal;
            return detalhe.getUsuario();
        }
        return null;
    }

    public static String criptografar(String valor) {
        return new BCryptPasswordEncoder().encode(valor);
    }
}
