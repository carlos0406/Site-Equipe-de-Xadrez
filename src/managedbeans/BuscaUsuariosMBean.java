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
import dao.UsuarioDAO;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.MetodosUteis;

/** 
 * MBean que controla operações relacionadas a busca de usuarios.
 */
@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaUsuariosMBean extends ControladorGeral {
	/*Atributos usados na busca de usuarios*/
	private String nomeBusca;
	private TipoUsuario tipoUsuarioBusca;
	private String cpfBusca;
	private Long matriculaBusca ;
	private boolean ativoBusca;
	
	
	
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

	public Long getMatriculaBusca() {
		return matriculaBusca;
	}

	public void setMatriculaBusca(Long matriculaBusca) {
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
	
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<Usuario> usuariosEncontrados;
	
	/** Permite o acesso ao banco. */
	private UsuarioDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		usuarioBusca = new Usuario();
		usuariosEncontrados = new ArrayList<>();
		
		dao = new UsuarioDAO();
	}
	
	/** Entra na p�gina de busca de usu�rios */
	public String entrarBuscarUsuarios(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new UsuarioDAO();
		
		usuariosEncontrados = dao.buscarUsuarios(nomeBusca,tipoUsuarioBusca,cpfBusca,matriculaBusca,ativoBusca);
	
		return "/sobreaequipe/busca_usuario.xhtml";
	}
	
	/** 
	 * Inativa um usu�rio do banco de dados. N�o o remove, apenas inativa, por�m
	 * tem o mesmo efeito, j� que ele n�o pode mais fazer login. � �til para quando
	 * o administrador n�o quer perder as informa��es do registro, por diversos motivos.
	 *  
	 * */
	public String removerUsuario(Usuario usuario) throws Exception{
		
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			//se o usu�rio estiver ativo
			if (usuario.isAtivo()){
				//ent�o vamos inativ�-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", false);
				
			} else {
				//nesse caso, n�o est� ativo, ent�o vamos reativ�-lo
				dao.atualizarCampo(usuario.getClass(), usuario.getId(), "ativo", true);
			}
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
			dao.desanexarEntidade(usuario); //For�a o recarregamento da entidade, j� que alteramos os dados dela
			
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
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
	
	public String alterarAtivo(Usuario u) {
	EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			
			em.getTransaction().begin();
			if(u.isAtivo()) {
				u.setAtivo(false);
				
			}else {
				u.setAtivo(true);
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
				
			} catch (Exception e){
				e.printStackTrace();
				
				if (em.getTransaction().isActive())
					
					em.getTransaction().rollback();
			}
			
			return "/sobreaequipe/busca_usuario.xhtml";
			
		}
	
	
}