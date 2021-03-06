package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import dao.CamisaDAO;
import dao.Database;
import dominio.Camisa;
import dominio.Livro;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaCamisasMBean {
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Camisa> camisasEncontradas;
	
	/** Permite o acesso ao banco. */
	private CamisaDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		camisasEncontradas = new ArrayList<>();
		
		dao = new CamisaDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarCamisas(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new CamisaDAO();
		
		camisasEncontradas = dao.buscarCamisa();
			
		return "/sobreaequipe/busca_camisa.xhtml";
	}
	
	public List<Camisa> getCamisasEncontradas() {
		return camisasEncontradas;
	}

	public void setCamisasEncontradas(List<Camisa> camisasEncontradas) {
		this.camisasEncontradas = camisasEncontradas;
	}

	public CamisaDAO getDao() {
		return dao;
	}

	public void setDao(CamisaDAO dao) {
		this.dao = dao;
	}
	public String deletar(Camisa c) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(c);
			
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
