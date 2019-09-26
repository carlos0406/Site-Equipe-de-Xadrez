package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import arquitetura.ControladorGeral;
import dao.Database;
import dao.UsuarioDAO;
import dominio.Usuario;
import uteis.MetodosUteis;

/** 
 * MBean que controla operações relacionadas a busca de usuarios.
 */
@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@ViewScoped
public class BuscaUsuariosMBean extends ControladorGeral {

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
		
		//usuariosEncontrados = dao.findUsuarioGeral(usuarioBusca.getNome());
			
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
		if (MetodosUteis.estaVazia(usuariosEncontrados)) {
			usuariosEncontrados = dao.findUsuarioGeral(usuarioBusca.getNome());
		}
		
		return usuariosEncontrados;
	}

	public void setUsuariosEncontrados(List<Usuario> usuariosEncontrados) {
		this.usuariosEncontrados = usuariosEncontrados;
	}
}