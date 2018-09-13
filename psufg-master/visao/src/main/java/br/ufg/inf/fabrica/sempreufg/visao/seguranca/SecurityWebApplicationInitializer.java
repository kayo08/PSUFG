package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import org.springframework.security.web.context.*;

public class SecurityWebApplicationInitializer
        extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
