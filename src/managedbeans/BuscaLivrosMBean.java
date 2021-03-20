package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import dao.LivroDAO;
import dao.Database;
import dominio.Livro;
import dominio.RodadaTorneio;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaLivrosMBean {
	
	/** Armazena os usuï¿½rios encontrados no banco de acordo com os parï¿½metros de busca. */
	private List<Livro> livrosEncontrados;
	
	/** Permite o acesso ao banco. */
	private LivroDAO dao;
	
	/** Inicializaï¿½ï¿½o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		livrosEncontrados = new ArrayList<>();
		
		dao = new LivroDAO();
	}
	
	/** Entra na pagina de busca de usuï¿½rios */
	public String entrarBuscarLivros(){
		return buscar();
	}
	
	/** Realiza a busca de usuï¿½rios no banco. */
	public String buscar(){
		dao = new LivroDAO();
		
		livrosEncontrados = dao.buscarLivro();
			
		return "/material/busca_livro.xhtml";
	}

	public List<Livro> getLivrosEncontrados() {
		return livrosEncontrados;
	}

	public void setLivrosEncontrados(List<Livro> livrosEncontrados) {
		this.livrosEncontrados = livrosEncontrados;
	}

	public LivroDAO getDao() {
		return dao;
	}

	public void setDao(LivroDAO dao) {
		this.dao = dao;
	}
	

	public String deletar(Livro l) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transaï¿½ï¿½o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(l);
			
			//Transaï¿½ï¿½o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			if(e instanceof  javax.persistence.RollbackException) {
				MetodosUteis.addMensagem("Não é possivel deletar esse livro por que existe uma solicitação pendendete");
			}
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transaï¿½ï¿½o nï¿½o serï¿½ confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}
	
	


}
