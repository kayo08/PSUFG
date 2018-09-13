package br.ufg.inf.fabrica.sempreufg.visao.filtros;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.*;

public class FiltroUrl implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.getLogger(FiltroUrl.class.getName()).log(Level.SEVERE, "Método init da classe FiltroUrlBase");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            String prefixoFacesServlet = "/faces";
            String prefixoDuplicado = prefixoFacesServlet + prefixoFacesServlet;
            String url = request.getRequestURI();
            String contextPath = request.getContextPath();
            String loginURL = contextPath + "/faces/login.xhtml";
            HttpSession session = request.getSession(false);
            
            //Se o usuario não está logado e a pagina é a de login            
            //if (request.getSession(false) == null && url.equalsIgnoreCase(loginURL) ) {
            if (session == null) {
                session = request.getSession(true);
                response.sendRedirect(loginURL);
                return;
            }
            
            if (!url.contains(prefixoFacesServlet) && url.contains("xhtml")) {
                url = url.replace(contextPath, contextPath + "/faces");
                response.sendRedirect(url);
                return;
            }

            if (!url.contains(prefixoDuplicado)) {
                chain.doFilter(req, resp);
                return;
            }

            do {
                url = url.replace(prefixoDuplicado, prefixoFacesServlet);
            } while (url.contains(prefixoDuplicado));

            response.sendRedirect(url);

        } catch (IOException ex) {
            Logger.getLogger(FiltroUrl.class.getName()).log(Level.SEVERE, "FiltroUrl.doFilter.IOException: Erro no redirecionamento da pagina!");
        } catch (ServletException sEx) {
            Logger.getLogger(FiltroUrl.class.getName()).log(Level.SEVERE, "FiltroUrl.doFilter.ServletException: Erro no redirecionamento da pagina!");

        }
    }

    @Override
    public void destroy() {
        Logger.getLogger(FiltroUrl.class.getName()).log(Level.SEVERE, "Método destroy da classe FiltroUrlBase");
    }

    private static <T> T findBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context == null ? null : (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
    }

}
