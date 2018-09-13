
package br.ufg.inf.fabrica.sempreufg.enums;

/**
 *
 * @author auf
 */
    public enum ModalidadeCurso {
    
    Presencial("Presencial"),
    Semi_presencial("Semi-presencial"),
    A_distancia("À distância");
    
    ModalidadeCurso(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return this.value;
    }
    
}
