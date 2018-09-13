package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.enums.MesesAno;
import br.ufg.inf.fabrica.utils.EnumUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfMesesAnoConverter")
public class MesesAnoConverter implements Converter{

     @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return EnumUtils.getEnum(MesesAno.class, value);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return ((MesesAno) o).getValue();
    }
    
}
