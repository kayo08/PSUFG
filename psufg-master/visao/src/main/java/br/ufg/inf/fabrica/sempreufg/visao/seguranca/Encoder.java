package br.ufg.inf.fabrica.sempreufg.visao.seguranca;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Danillo
 */
public class Encoder {

    public static void main(String[] args) {
        String senhaCriptografada = new BCryptPasswordEncoder().encode("senha");
        System.out.println("valor: " + senhaCriptografada);
        System.out.println("tamanho: " + senhaCriptografada.length());
    }

}
