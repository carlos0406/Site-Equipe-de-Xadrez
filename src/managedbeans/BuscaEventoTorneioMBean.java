package managedbeans;

import java.util.Date;
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
	
	
	/*Atributos usados na busca de torneios*/
		private String nomeBusca;
		private Date dataInicial;
		private Date dataFinal;
		private boolean eventosFuturosBusca;
		
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
		
		eventosEncontrados = dao.buscarEventoTorneio(nomeBusca,dataInicial,dataFinal,eventosFuturosBusca);
		
			
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
	
	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}

	

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public boolean isEventosFuturosBusca() {
		return eventosFuturosBusca;
	}

	public void setEventosFuturosBusca(boolean eventosFuturosBusca) {
		this.eventosFuturosBusca = eventosFuturosBusca;
	}

	public String deletar(EventoTorneio et) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(et);
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}
	
	
}
