package br.ufg.inf.fabrica.sempreufg.visao.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Danillo
 */
@FacesConverter("jsfLocalDateConverter")
public class LocalDateConverter implements Converter {

   private static final DateTimeFormatter DATE_TIME_FORMATTER
           = DateTimeFormatter.ofPattern("dd/MM/yyyy");

   @Override
   public Object getAsObject(FacesContext context, UIComponent component, String value) {
      if (value != null && !value.isEmpty()) {
         return LocalDate.parse(value, DATE_TIME_FORMATTER);
      }
      return null;
   }

   @Override
   public String getAsString(FacesContext context, UIComponent component, Object value) {
      if (value == null) {
         return "";
      }
      LocalDate ld = (LocalDate) value;
      return ld.format(DATE_TIME_FORMATTER);
   }
}
