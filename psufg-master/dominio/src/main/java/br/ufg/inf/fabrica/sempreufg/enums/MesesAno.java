/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author auf
 */
public enum MesesAno {

   JANEIRO("Janeiro"),
   FEVEREIRO("Fevereiro"),
   MARCO("Março"),
   ABRIL("Abril"),
   MAIO("Maio"),
   JUNHO("Junho"),
   JULHO("Julho"),
   AGOSTO("Agosto"),
   SETEMBRO("Setembro"),
   OUTUBRO("Outubro"),
   NOVEMBRO("Novembro"),
   DEZEMBRO("Dezembro");

   MesesAno(String value) {
      this.value = value;
   }

   private final String value;

   public String getValue() {
      return this.value;
   }
}
