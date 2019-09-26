package uteis;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dominio.Usuario;

public class MetodosUteis {

	public static boolean estaVazia(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static final boolean estaVazia(Object o) {
		if (o == null)
			return true;
		if (o instanceof String)
			return estaVazia( (String) o);
		if (o instanceof Number) {
			Number i = (Number) o;
			return (i.intValue() == 0);
		}
		if (o instanceof Object[])
			return ((Object[]) o).length == 0;
		if (o instanceof int[])
			return ((int[]) o).length == 0;
		if (o instanceof Collection<?>)
			return ((Collection<?>) o).size() == 0;
		if (o instanceof Map<?, ?>)
			return ((Map<?, ?>) o).size() == 0;
		return false;
	}
	
	public static void addMensagem(String msg) {
		FacesMessage mensagem = new FacesMessage(msg);
		FacesContext.getCurrentInstance().addMessage(
				null, mensagem);
	}
	
	public static Usuario getUsuarioLogado(){
		return (Usuario) getCurrentSession().getAttribute("usuarioLogado");
	}
	
	public static HttpSession getCurrentSession() {
		HttpServletRequest req = (HttpServletRequest) getExternalContext().getRequest();
		return req.getSession(true);
	}
	
	private static ExternalContext getExternalContext () {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
}
