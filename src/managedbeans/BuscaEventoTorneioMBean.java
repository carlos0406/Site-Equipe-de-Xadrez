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
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaEventoTorneioMBean {
	/** Armazena os usuï¿½rios encontrados no banco de acordo com os parï¿½metros de busca. */
	private List<EventoTorneio> eventosEncontrados;
	
	/** Permite o acesso ao banco. */
	private EventoTorneioDAO dao;
	
	
	/*Atributos usados na busca de torneios*/
		private String nomeBusca;
		private Date dataInicial;
		private Date dataFinal;
		private boolean eventosFuturosBusca;
		
	/** Inicializaï¿½ï¿½o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		eventosEncontrados = new ArrayList<>();
		
		dao = new EventoTorneioDAO();
	}
	
	/** Entra na pagina de busca de usuï¿½rios */
	public String entrarBuscarEventoTorneio(){
		
		return buscar();
	}
	
	/** Realiza a busca de usuï¿½rios no banco. */
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
			//Iniciando transaï¿½ï¿½o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(et);
			
			//Transaï¿½ï¿½o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			MetodosUteis.addMensagem("Não é possivel deletar esse torneio por que existe(m) rodada(s) relacionadas a esse torneio");
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transaï¿½ï¿½o nï¿½o serï¿½ confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}
	
	
}
