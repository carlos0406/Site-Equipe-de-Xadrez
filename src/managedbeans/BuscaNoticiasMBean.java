package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import org.apache.commons.validator.util.ValidatorUtils;

import dao.NoticiaDAO;
import dao.DAOGenerico;
import dao.Database;
import dominio.Arquivo;
import dominio.Noticia;
import dominio.Usuario;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaNoticiasMBean {
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Noticia> noticiasEncontradas;
	
	private List<Noticia> noticiasPagInicial;
	
	private Noticia noticiaVisualizada;
	
	/** Permite o acesso ao banco. */
	private NoticiaDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		noticiasEncontradas = new ArrayList<>();
		
		dao = new NoticiaDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarNoticias(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new NoticiaDAO();
		
		noticiasEncontradas = dao.buscarNoticia();
			
		return "/sobreaequipe/busca_noticia.xhtml";
	}
	
	public List<Noticia> getBuscarPaginaInicial(){
		
		if (MetodosUteis.estaVazia(noticiasPagInicial)) {
			dao = new NoticiaDAO();
			return dao.buscarNoticiaPaginaInicial();
		} else {
			return noticiasPagInicial;
		}
		
	}
	
	
	
	public String verTexto(Noticia noticia){
		dao = new NoticiaDAO();
		
		noticiaVisualizada = dao.encontrarPeloID(noticia.getId_arquivoNoticia(), Noticia.class);
		
		return "/sobreaequipe/mostra_noticia.xhtml";
	}
	
	public String removerNoticia(Noticia noticia) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(noticia);
			
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
	
	public List<Noticia> getNoticiasEncontradas() {
		return noticiasEncontradas;
	}

	public void setNoticiasEncontradas(List<Noticia> noticiasEncontradas) {
		this.noticiasEncontradas = noticiasEncontradas;
	}

	public NoticiaDAO getDao() {
		return dao;
	}

	public void setDao(NoticiaDAO dao) {
		this.dao = dao;
	}

	public Noticia getNoticiaVisualizada() {
		return noticiaVisualizada;
	}

	public void setNoticiaVisualizada(Noticia noticiaVisualizada) {
		this.noticiaVisualizada = noticiaVisualizada;
	}

}
