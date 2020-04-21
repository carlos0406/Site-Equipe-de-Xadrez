package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.EventoTorneioDAO;
import dominio.EventoTorneio;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaEventoTorneioMBean {
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<EventoTorneio> eventosEncontrados;
	
	/** Permite o acesso ao banco. */
	private EventoTorneioDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		eventosEncontrados = new ArrayList<>();
		
		dao = new EventoTorneioDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarEventoTorneio(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new EventoTorneioDAO();
		
		eventosEncontrados = dao.buscarEventoTorneio();
			
		return "/eventos/busca_eventoTorneio.xhtml";
	}

	public List<EventoTorneio> getEventosEncontrados() {
		return eventosEncontrados;
	}

	public void setEventosEncontrados(List<EventoTorneio> eventosEncontrados) {
		this.eventosEncontrados = eventosEncontrados;
	}

	public EventoTorneioDAO getDao() {
		return dao;
	}

	public void setDao(EventoTorneioDAO dao) {
		this.dao = dao;
	}
	
	public void deletar(EventoTorneio e) {
		EntityManager gerenciador = Database.getInstance().getEntityManager();
		gerenciador.remove(e);
		
		

	}
	
	
}
