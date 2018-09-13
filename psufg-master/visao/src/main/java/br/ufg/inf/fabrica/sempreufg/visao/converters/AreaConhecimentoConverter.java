package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.controller.AreaConhecimentoController;
import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfAreaConhecimentoConverter")
public class AreaConhecimentoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Long id = null;
        try {
            id = Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return null;
        }
        AreaConhecimentoController controller
                = new AreaConhecimentoController();
        AreaConhecimento ac = controller.buscar(id);
        return ac;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof AreaConhecimento && ((AreaConhecimento) o).getId() != null) {
            return ((AreaConhecimento) o).getId().toString();
        }
        return "";        
    }
}
