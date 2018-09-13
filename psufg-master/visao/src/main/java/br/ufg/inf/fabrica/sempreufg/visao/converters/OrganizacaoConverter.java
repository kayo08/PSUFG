package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.controller.OrganizacaoController;
import br.ufg.inf.fabrica.sempreufg.dominio.Organizacao;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfOrganizacaoConverterConverter")
public class OrganizacaoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        Long id = null;
        try {
            id = Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
        OrganizacaoController controller = new OrganizacaoController();
        Organizacao org = controller.buscar(id);
        return org;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof Organizacao && ((Organizacao) o).getId() != null ) {            
            return ((Organizacao) o).getId().toString();
        }        
        return null;
    }

}
