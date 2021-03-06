package managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
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
	
	public String alterarDestaque() {
		EntityManager em = Database.getInstance().getEntityManager();
			
			try {
				
				em.getTransaction().begin();
				
				if(!usuario.isDestaque()) {
					usuario.setTextoDestaque(null);
				}
				em.merge(usuario);
				
				
				
				em.getTransaction().commit();
				
			} catch (Exception e){
				e.printStackTrace();
				
				if (em.getTransaction().isActive())
					
					em.getTransaction().rollback();
			}
			
			return "/sobreaequipe/busca_usuario.xhtml";
			
		}
	
	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
