package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.controller.LocalizacaoGeograficaController;
import br.ufg.inf.fabrica.sempreufg.dominio.LocalizacaoGeografica;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfLocalizacaoGeograficaConverter")
public class LocalizacaoGeograficaConverter implements Converter {

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
        LocalizacaoGeograficaController controller
                = new LocalizacaoGeograficaController();
        LocalizacaoGeografica lg = controller.buscar(id);
        return lg;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof LocalizacaoGeografica && ((LocalizacaoGeografica) o).getId() != null) {
            return ((LocalizacaoGeografica) o).getId().toString();
        }
        return "";
    }

}
