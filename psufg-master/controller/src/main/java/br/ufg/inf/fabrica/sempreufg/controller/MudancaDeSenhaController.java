package br.ufg.inf.fabrica.sempreufg.controller;

import br.ufg.inf.fabrica.sempreufg.dao.TokenDeResetDeSenhaDao;
import br.ufg.inf.fabrica.sempreufg.dominio.TokenDeResetDeSenha;
import br.ufg.inf.fabrica.sempreufg.dominio.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author danillo
 */
public class MudancaDeSenhaController {

    private TokenDeResetDeSenhaDao daoToken;

    public MudancaDeSenhaController() {
        this.daoToken = new TokenDeResetDeSenhaDao();
    }

    public boolean validarToken(String tokenValue) {

        TokenDeResetDeSenha token = buscarToken(tokenValue);
        return validarToken(token);
    }

    public TokenDeResetDeSenha buscarToken(String tokenValue) {

        if (tokenValue != null && (!tokenValue.isEmpty())) {
            TokenDeResetDeSenha token
                    = daoToken.buscarPorToken(tokenValue);
            return token;
        }
        return null;
    }

    public List<String> confirmarMudancaDeSenha(TokenDeResetDeSenha token, 
            String novaSenha) {
        Usuario usuario = token.getUsuario();
        usuario.setNovaSenha(novaSenha);
        List<String> inconsistencias = usuario.validarSenha();
        if (inconsistencias.isEmpty()) {
            usuario.criptografar();
            token.setJaUtilizado(true);
            daoToken.salvar(token);
        }
        return inconsistencias;
    }

    private boolean validarToken(TokenDeResetDeSenha token) {
        if ( !(token == null || token.isJaUtilizado()) ) {
            Date dataExpiracao = token.getDataExpiracao();
            Date dataAtual = new Date();
            if (dataAtual.getTime() < dataExpiracao.getTime()) {
                return true;
            }
        }
        return false;
    }

}
