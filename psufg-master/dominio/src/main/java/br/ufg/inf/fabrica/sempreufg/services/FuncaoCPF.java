/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.services;

/**
 *
 * @author auf
 */
public class FuncaoCPF {

    public FuncaoCPF() {
    }

    public static String geraCPF() {
        String iniciais = "";
        Integer numero;
        for (int i = 0; i < 9; i++) {
            numero = (int) (Math.random() * 10);
            iniciais += numero.toString();
        }
        return iniciais + calcDigVerif(iniciais);
    }

    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++) {
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        }
        if (soma % 11 == 0 | soma % 11 == 1) {
            primDig = 0;
        } else {
            primDig = 11 - (soma % 11);
        }
        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++) {
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        }
        soma += primDig * 2;
        if (soma % 11 == 0 | soma % 11 == 1) {
            segDig = 0;
        } else {
            segDig = 11 - (soma % 11);
        }
        return primDig.toString() + segDig.toString();
    }

    public static boolean validaCPF(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }
        String numDig = cpf.substring(0, 9);
        return calcDigVerif(numDig).equals(cpf.substring(9, 11));
    }
}
