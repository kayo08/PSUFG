package br.ufg.inf.fabrica.sempreufg.enums;

public enum EscopoDivulgacao {
	EGRESSOS("Egressos"), 
        COMUNIDADE("Comunidade"),
        FORA_DE_ESCOPO("Fora De Escopo");
        
        EscopoDivulgacao(String value){
            this.value = value;
        }
        
        private final String value;
        
        public String getValue(){
            return this.value;
        }
}
