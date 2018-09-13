package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.enums.FrequenciaDivulgacao;
import br.ufg.inf.fabrica.utils.EnumUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfEnumFrequenciaDivulgacaoConverter")
public class FrequenciaDivulgacaoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return EnumUtils.getEnum(FrequenciaDivulgacao.class, value);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return ((FrequenciaDivulgacao) o).getValue();
    }
    
}
