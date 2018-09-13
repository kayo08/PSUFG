package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import br.ufg.inf.fabrica.sempreufg.dominio.Papel;
import org.springframework.security.core.GrantedAuthority;

public class Autoridade implements GrantedAuthority{

    private final Papel papel;

    public Autoridade(Papel papel) {
        this.papel = papel;
    }
    
    @Override
    public String getAuthority() {
        return papel.getNome();
    }
    
}
