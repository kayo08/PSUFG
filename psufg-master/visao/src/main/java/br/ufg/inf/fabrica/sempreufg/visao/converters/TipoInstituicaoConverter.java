package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.enums.TipoInstituicao;
import br.ufg.inf.fabrica.utils.EnumUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfEnumTipoInstituicaoConverter")
public class TipoInstituicaoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return EnumUtils.getEnum(TipoInstituicao.class, value);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return ((TipoInstituicao) o).getValue();
    }
    
}
