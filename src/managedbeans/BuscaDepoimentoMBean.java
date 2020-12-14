package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import org.apache.commons.validator.util.ValidatorUtils;


import dao.DAOGenerico;
import dao.Database;
import dao.DepoimentoDAO;
import dominio.Arquivo;
import dominio.Depoimento;
import dominio.Usuario;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaDepoimentoMBean {
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Depoimento> depoimentosEncontradas;
	
	
	
	private Depoimento depoimentoVisualizado;
	
	/** Permite o acesso ao banco. */
	private DepoimentoDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		depoimentosEncontradas = new ArrayList<>();
		
		dao = new DepoimentoDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscardepoimentos(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new DepoimentoDAO();
		
		depoimentosEncontradas = dao.buscarDepoimento();
			
		return "/sobreaequipe/busca_depoimento.xhtml";
	}
	
	
	
	
	
	public String verTexto(Depoimento depoimento){
		dao = new DepoimentoDAO();
		
		depoimentoVisualizado = dao.encontrarPeloID(depoimento.getId_depoimento(), Depoimento.class);
		
		return "/sobreaequipe/mostra_depoimento.xhtml";
	}
	
	public String removerdepoimento(Depoimento depoimento) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(depoimento);
			
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
	
	public List<Depoimento> getdepoimentosEncontradas() {
		return depoimentosEncontradas;
	}

	public void setdepoimentosEncontradas(List<Depoimento> depoimentosEncontradas) {
		this.depoimentosEncontradas = depoimentosEncontradas;
	}

	public DepoimentoDAO getDao() {
		return dao;
	}

	public void setDao(DepoimentoDAO dao) {
		this.dao = dao;
	}

	public List<Depoimento> getDepoimentosEncontradas() {
		return depoimentosEncontradas;
	}

	public void setDepoimentosEncontradas(List<Depoimento> depoimentosEncontradas) {
		this.depoimentosEncontradas = depoimentosEncontradas;
	}

	public Depoimento getDepoimentoVisualizado() {
		return depoimentoVisualizado;
	}

	public void setDepoimentoVisualizado(Depoimento depoimentoVisualizado) {
		this.depoimentoVisualizado = depoimentoVisualizado;
	}

	
}
