package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import br.ufg.inf.fabrica.sempreufg.controller.UsuarioController;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author Danillo
 */
public class GestorDeAutenticacoesComSucesso implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        
        String targetUrl = null;
        if (isUser) {
            targetUrl = "/homepage.html";
        } else if (isAdmin) {
            targetUrl = "/homepage.html";
        }
        
        Usuario usuario = ((DetalheDoUsuario)authentication.getPrincipal()).getUsuario();
        UsuarioController controller = new UsuarioController();
        controller.atualizarDataUltimoLogin(usuario);
        
        targetUrl = "/faces/egressos/index.xhtml";

        if (response.isCommitted()) {
            System.out.println( "Response has already been committed. Unable to redirect to "
                    + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);

        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        
    }

}
