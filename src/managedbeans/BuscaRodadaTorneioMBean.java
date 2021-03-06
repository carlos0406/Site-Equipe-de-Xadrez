package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.RodadaTorneioDAO;
import dao.UsuarioDAO;
import dominio.EventoTorneio;
import dominio.RodadaTorneio;
import dominio.Usuario;




@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaRodadaTorneioMBean {
	private RodadaTorneioDAO dao;
	private RodadaTorneio rodadaBusca;
	
	public RodadaTorneio getRodadaBusca() {
		return rodadaBusca;
	}

	public void setRodadaBusca(RodadaTorneio rodadaBusca) {
		this.rodadaBusca = rodadaBusca;
	}

	private List<RodadaTorneio>rodadasEncontradas;
	
	
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		rodadaBusca= new RodadaTorneio();
		rodadasEncontradas= new ArrayList<>();
		
		dao = new RodadaTorneioDAO();
	}
	
	
	public String buscar(){
	
		rodadasEncontradas= new ArrayList<>();
		dao = new RodadaTorneioDAO();
		rodadasEncontradas = dao.buscarRodadas(rodadaBusca);
		
		
	
	
		return "/eventos/busca_rodadaTorneio.xhtml";
	}

	public List<RodadaTorneio> getRodadasEncontradas() {
		return rodadasEncontradas;
	}

	public void setRodadasEncontradas(List<RodadaTorneio> rodadasEncontradas) {
		this.rodadasEncontradas = rodadasEncontradas;
	}
	
	
	public String deletar(RodadaTorneio r) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(r);
			
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
