package br.ufg.inf.fabrica.sempreufg.dominio.pks;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Danillo
 */
@Embeddable
public class CursoDeOutraIesID implements Serializable {

    private static final long serialVersionUID = 1L;

        private String nomeDoCurso;
        private String iesDoCurso;

    public String getNomeDoCurso() {
        return nomeDoCurso;
    }

    public void setNomeDoCurso(String nomeDoCurso) {
        this.nomeDoCurso = nomeDoCurso!=null?nomeDoCurso.trim():nomeDoCurso;
    }

    public String getIesDoCurso() {
        return iesDoCurso;
    }

    public void setIesDoCurso(String iesDoCurso) {
        this.iesDoCurso = iesDoCurso!=null?iesDoCurso.trim():iesDoCurso;
    }

        

    @Override
    public int hashCode() {
        if(getNomeDoCurso()==null || 
                getNomeDoCurso().isEmpty() || 
                getIesDoCurso()==null || 
                getIesDoCurso().isEmpty()){
            return 0;
        }
        return Objects.hash(getNomeDoCurso(), getIesDoCurso());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }
        if (!(object instanceof CursoDeOutraIesID)){
            return false;
        }
        CursoDeOutraIesID outroId = (CursoDeOutraIesID) object;
        return (Objects.equals(getNomeDoCurso(), outroId.getNomeDoCurso()) &&
                Objects.equals(getIesDoCurso(), outroId.getIesDoCurso()));
    }
}
