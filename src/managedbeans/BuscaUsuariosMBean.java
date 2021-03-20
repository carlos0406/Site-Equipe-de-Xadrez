package managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import arquitetura.ControladorGeral;
import dao.Database;
import dao.NoticiaDAO;
import dao.UsuarioDAO;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.MetodosUteis;

/** 
 * MBean que controla operaÃ§Ãµes relacionadas a busca de usuarios.
 */
@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaUsuariosMBean extends ControladorGeral {
	private List<Usuario>usuariosDestaque;
	/*Atributos usados na busca de usuarios*/
	private String nomeBusca;
	private TipoUsuario tipoUsuarioBusca;
	private String cpfBusca;
	private String matriculaBusca ;
	private boolean ordeByRating;
	private boolean ativoBusca=true;
	
	
	
	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
		
	}

	public TipoUsuario getTipoUsuarioBusca() {
		return tipoUsuarioBusca;
	}

	public void setTipoUsuarioBusca(TipoUsuario tipoUsuarioBusca) {
		this.tipoUsuarioBusca = tipoUsuarioBusca;
	}

	public String getCpfBusca() {
		return cpfBusca;
	}

	public void setCpfBusca(String cpfBusca) {
		this.cpfBusca = cpfBusca;
	}

	

	public boolean isOrdeByRating() {
		return ordeByRating;
	}

	public void setOrdeByRating(boolean ordeByRating) {
		this.ordeByRating = ordeByRating;
	}

	public String getMatriculaBusca() {
		return matriculaBusca;
	}

	public void setMatriculaBusca(String matriculaBusca) {
		this.matriculaBusca = matriculaBusca;
	}

	public boolean isAtivoBusca() {
		return ativoBusca;
	}

	public void setAtivoBusca(boolean ativoBusca) {
		this.ativoBusca = ativoBusca;
	}

	public UsuarioDAO getDao() {
		return dao;
	}

	public void setDao(UsuarioDAO dao) {
		this.dao = dao;
	}

	/** Armazena as informacoes preenchidas na pagina de busca de usuarios. */
	private Usuario usuarioBusca;
	
	/** Armazena os usuï¿½rios encontrados no banco de acordo com os parï¿½metros de busca. */
	private List<Usuario> usuariosEncontrados;
	
	/** Permite o acesso ao banco. */
	private UsuarioDAO dao;
	
	/** Inicializaï¿½ï¿½o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		usuarioBusca = new Usuario();
		usuariosEncontrados = new ArrayList<>();
		
		dao = new UsuarioDAO();
	}
	
	/** Entra na pï¿½gina de busca de usuï¿½rios */
	public String entrarBuscarUsuarios(){
		return buscar();
	}
	
	/** Realiza a busca de usuï¿½rios no banco. */
	public String buscar(){
		dao = new UsuarioDAO();
		
		usuariosEncontrados = dao.buscarUsuarios(nomeBusca,tipoUsuarioBusca,cpfBusca,matriculaBusca,ativoBusca,ordeByRating);
	
		return "/sobreaequipe/busca_usuario.xhtml";
	}
	
	/** 
	 * Inativa um usuï¿½rio do banco de dados. Nï¿½o o remove, apenas inativa, porï¿½m
	 * tem o mesmo efeito, jï¿½ que ele nï¿½o pode mais fazer login. ï¿½ ï¿½til para quando
	 * o administrador nï¿½o quer perder as informaï¿½ï¿½es do registro, por diversos motivos.
	 *  
	 * */
	public String removerUsuario(Usuario usuario) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transaï¿½ï¿½o com o banco de dados
			em.getTransaction().begin();
			
			//se o usuï¿½rio estiver ativo
			if (usuario.isAtivo()){
				//entï¿½o vamos inativï¿½-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", false);
				
			} else {
				//nesse caso, nï¿½o estï¿½ ativo, entï¿½o vamos reativï¿½-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", true);
			}
			
			//Transaï¿½ï¿½o confirmada
			em.getTransaction().commit();
			
			dao.desanexarEntidade(usuario); //Forï¿½a o recarregamento da entidade, jï¿½ que alteramos os dados dela
			
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transaï¿½ï¿½o nï¿½o serï¿½ confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();
	}
	
	public Usuario getUsuarioBusca() {
		return usuarioBusca;
	}

	public void setUsuarioBusca(Usuario usuarioBusca) {
		this.usuarioBusca = usuarioBusca;
	}

	public List<Usuario> getUsuariosEncontrados() {
		//if (MetodosUteis.estaVazia(usuariosEncontrados)) {
		//}
		
		return usuariosEncontrados;
	}

	public void setUsuariosEncontrados(List<Usuario> usuariosEncontrados) {
		this.usuariosEncontrados = usuariosEncontrados;
	}
	
	public List<Usuario> autoCompleteUsuarios(String query) {

		return new UsuarioDAO().buscarPelaColunaLike("nome", query, Usuario.class);

	}
	
	
	
	public List<Usuario>getUsuariosDestaque(){
		
			dao = new UsuarioDAO();
			return dao.buscarDestaques();

		
	}
	
	public String alterarAtivo(Usuario u) {
	EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			
			em.getTransaction().begin();
			if(u.isAtivo()) {
				u.setAtivo(false);
				MetodosUteis.addMensagem("Usuário inativado com sucesso");
				
			}else {
				u.setAtivo(true);
				MetodosUteis.addMensagem("Usuário ativado com sucesso");
			}
			
			em.merge(u);
			
			
			
			em.getTransaction().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				
				em.getTransaction().rollback();
		}
		
		return "/sobreaequipe/busca_usuario.xhtml";
		
	}
	
	
	public String alterarRating(Usuario u) {
		EntityManager em = Database.getInstance().getEntityManager();
			
			try {
				
				em.getTransaction().begin();
				
				u.setRating(u.getRating());
				
				em.merge(u);
				
				
				
				em.getTransaction().commit();
				MetodosUteis.addMensagem("Rating do membro alterado!");
				
			} catch (Exception e){
				e.printStackTrace();
				
				if (em.getTransaction().isActive())
					
					em.getTransaction().rollback();
			}
			
			return "/sobreaequipe/busca_usuario.xhtml";
			
		}
	
	
}