/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.dominio.AreaConhecimento;
import br.ufg.inf.fabrica.sempreufg.dominio.InstanciaAdministrativaUFG;
import br.ufg.inf.fabrica.sempreufg.visao.beans.EventoBean;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 *
 * @author auf
 */
@FacesConverter(value = "jsfPickListConverter")
public class PickListConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {

        Object ret = null;
        if (uic instanceof PickList) {
            Object dualList = ((PickList) uic).getValue();
            DualListModel dl = (DualListModel) dualList;
            for (Iterator iterator = dl.getSource().iterator(); iterator
                    .hasNext();) {
                Object o = iterator.next();
                String id = "";
                if (o instanceof AreaConhecimento) {
                    id = (new StringBuilder()).append(
                            ((AreaConhecimento) o).getId()).toString();
                }
                if (o instanceof InstanciaAdministrativaUFG) {
                    id = (new StringBuilder()).append(
                            ((InstanciaAdministrativaUFG) o).getId()).toString();
                }
                if (str.equals(id)) {
                    ret = o;
                    break;
                }
            }

            if (ret == null) {
                for (Iterator iterator1 = dl.getTarget().iterator(); iterator1
                        .hasNext();) {
                    Object o = iterator1.next();
                    String id = "";
                    if (o instanceof AreaConhecimento) {
                        id = (new StringBuilder()).append(
                                ((AreaConhecimento) o).getId()).toString();
                    }
                    if (o instanceof InstanciaAdministrativaUFG) {
                        id = (new StringBuilder()).append(
                                ((InstanciaAdministrativaUFG) o).getId()).toString();
                    }
                    if (str.equals(id)) {
                        ret = o;
                        break;
                    }
                }

            }
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = "";

        if (o instanceof AreaConhecimento) {
            str = (new StringBuilder()).append(
                    ((AreaConhecimento) o).getId()).toString();
        }
        if (o instanceof InstanciaAdministrativaUFG) {
            str = (new StringBuilder()).append(
                    ((InstanciaAdministrativaUFG) o).getId()).toString();
        }
        return str;
    }

}
