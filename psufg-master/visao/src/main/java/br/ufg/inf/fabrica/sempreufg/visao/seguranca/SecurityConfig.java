package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import java.util.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/faces/javax.faces.resource/**",
                        "/resources/**",
                        "/faces/index.xhtml",
                        "/faces/contato.xhtml",
                        "/faces/emconstrucao.xhtml",
                        "/faces/trocarSenha.xhtml",
                        "/faces/login.xhtml",
                        "/faces/esqueciSenha.xhtml",
                        "/faces/error.xhtml",
                        "/banners/**")
                .permitAll()
                .anyRequest().authenticated();
        http
                .formLogin().loginPage("/faces/login.xhtml")
                .successHandler(getGestorDeAutenticacoesComSucesso())
                .permitAll()
                .failureUrl("/faces/login.xhtml?error=true");
        http
                .logout().logoutSuccessUrl("/faces/login.xhtml");
        http
                .csrf().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetailsService uds = new ServicoDeDetalhesDeUsuario();
        return uds;
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDecisionManager getAccessDecisionManager() {
        AccessDecisionManager adm = new AccessDecisionManager() {
            @Override
            public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
                UserDetails ud = ((UserDetails) authentication.getPrincipal());
                System.out.println(ud.getUsername());
                System.out.println(object.getClass().getSimpleName());
                System.out.println("ok");
            }

            @Override
            public boolean supports(ConfigAttribute attribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return true;
            }
        };
        return adm;
    }

    @Bean
    public AuthenticationSuccessHandler getGestorDeAutenticacoesComSucesso(){
        return new GestorDeAutenticacoesComSucesso();
    }
    
}
