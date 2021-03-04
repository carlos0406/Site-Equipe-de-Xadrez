package conversores;

import javax.faces.convert.FacesConverter;

import dao.EventoTorneioDAO;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


import dominio.EventoTorneio;



@FacesConverter(value="torneioConverter")
public class TorneioConverter implements Converter {
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value!=null) {
			EventoTorneio e=(EventoTorneio)value;
			return String.valueOf(e.getId_EventoTorneio());
		}else
			return null;
	
	
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)throws ConverterException {
		if(value!=null&& value.trim().length()>0) {
			try {
				int id= Integer.parseInt(value);
				return new EventoTorneioDAO().encontrarPeloID(id, EventoTorneio.class);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return null;
	
	
	}
	
	

}
