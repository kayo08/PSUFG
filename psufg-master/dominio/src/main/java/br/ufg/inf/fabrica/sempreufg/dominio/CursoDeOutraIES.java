package br.ufg.inf.fabrica.sempreufg.dominio;

import br.ufg.inf.fabrica.sempreufg.dao.validadores.IValidador;
import br.ufg.inf.fabrica.sempreufg.dao.validadores.ValidadorDeDados;
import br.ufg.inf.fabrica.sempreufg.dominio.pks.CursoDeOutraIesID;
import br.ufg.inf.fabrica.sempreufg.enums.Nivel;
import br.ufg.inf.fabrica.sempreufg.enums.TipoInstituicao;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author Danillo
 */
@Entity
public class CursoDeOutraIES implements Serializable, Cloneable, IValidador {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CursoDeOutraIesID id;
    
    private Nivel nivel;
    private String nomeDaUnidadeAcademica;
    private TipoInstituicao tipoInstituicao;
    private String urlInstitucional;

    @ManyToOne
    private LocalizacaoGeografica localizacaoGeografica;

    @ManyToOne
    private AreaConhecimento area;

    public CursoDeOutraIES() {
        this.id = new CursoDeOutraIesID();
    }
    
    public LocalizacaoGeografica getLocalizacaoGeografica() {
        return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
        this.localizacaoGeografica = localizacaoGeografica;
    }

    public CursoDeOutraIesID getId() {
        if(this.id == null){
            this.id = new CursoDeOutraIesID();
        }
        return id;
    }

    public void setId(CursoDeOutraIesID id) {
        this.id = id;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public String getNomeDaUnidadeAcademica() {
        return nomeDaUnidadeAcademica;
    }

    public void setNomeDaUnidadeAcademica(String nomeDaUnidadeAcademica) {
        this.nomeDaUnidadeAcademica = nomeDaUnidadeAcademica;
    }

    public TipoInstituicao getTipoInstituicao() {
        return tipoInstituicao;
    }

    public void setTipoInstituicao(TipoInstituicao tipoInstituicao) {
        this.tipoInstituicao = tipoInstituicao;
    }

    public String getUrlInstitucional() {
        return urlInstitucional;
    }

    public void setUrlInstitucional(String urlInstitucional) {
        this.urlInstitucional = urlInstitucional;
    }

    public AreaConhecimento getArea() {
        return area;
    }

    public void setArea(AreaConhecimento area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "br.ufg.inf.fabrica.psufg.dominio.CursoDeOutraIES[ id=" + id + " ]";
    }

    @Override
    public List<String> validar() {
        ValidadorDeDados validador = new ValidadorDeDados();
        validador.validarNaoNulo(nivel, "nível");
        validador.validarNaoNulo(tipoInstituicao, "tipo de instituição");
        return validador.getInconsistencias();
    }

    @Override
    public CursoDeOutraIES clone() throws CloneNotSupportedException {
        return (CursoDeOutraIES) super.clone(); 
    }
    
    

}
