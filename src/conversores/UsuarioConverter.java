package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import dao.UsuarioDAO;
import dominio.Usuario;

@FacesConverter(value="usuarioConverter")
public class UsuarioConverter implements Converter {
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value!=null) {
			Usuario u=(Usuario)value;
			return String.valueOf(u.getId());
		}else
			return null;
	
	
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)throws ConverterException {
		if(value!=null&& value.trim().length()>0) {
			try {
				int id= Integer.parseInt(value);
				return new UsuarioDAO().encontrarPeloID(id, Usuario.class);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return null;
	
	
	}

}
