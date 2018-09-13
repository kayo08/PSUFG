/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.fabrica.sempreufg.visao.converters;

import br.ufg.inf.fabrica.sempreufg.enums.FormaDivulgacao;
import br.ufg.inf.fabrica.utils.EnumUtils;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author auf
 */
@FacesConverter("jsfStatusAprovacaoDivulgacaoConverter")
public class StatusAprovacaoDivulgacaoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return EnumUtils.getEnum(FormaDivulgacao.class, value);
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        return ((FormaDivulgacao) o).getValue();
    }

}
