package managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dao.EventoTorneioDAO;
import dominio.Usuario;
@SessionScoped
@ManagedBean
public class PerfilMBean {
	private Usuario usuario;
	
	
	public String verPerfilUsuario(Usuario usuario) {
		this.usuario=usuario;
		EventoTorneioDAO dao=new EventoTorneioDAO();
		usuario.setTorneios(dao.buscarEventoTorneiosUsuario(usuario.getId()));
		return "/sobreaequipe/perfilUsuario.xhtml";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
