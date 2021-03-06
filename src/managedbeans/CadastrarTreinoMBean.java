package managedbeans;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dominio.Treino;
import dominio.Usuario;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;

@ManagedBean
@SessionScoped
public class CadastrarTreinoMBean {
	private Treino treino;
	private Usuario usuarioTreino;

	public CadastrarTreinoMBean() {
		treino = new Treino();
		usuarioTreino=new Usuario();
		
		
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoTreinos(Treino treino) {
		this.treino = treino; // o livro a ser editado sera o recebido pelo
								// parametro
		return "/eventos/CadastrarTreino.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(treino.getMinistrantes())) {
			MetodosUteis.addMensagem("Campo Ministrantes obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(treino.getParticipantes())) {
			MetodosUteis.addMensagem("Campo participantes obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(treino.getTema())) {
			MetodosUteis.addMensagem("Campo tema obrigatório!");
			erro = true;
		}
		if (treino.getData() == null) {
			MetodosUteis.addMensagem("Campo data obrigatório!");
			erro = true;
		}
		if (ValidadorUtil.isEmpty(treino.getTurno())) {
			MetodosUteis.addMensagem("Campo turno obrigatório!");
			erro = true;
		}

		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				if (treino.getId_arquivoTreino() == 0)
					gerenciador.persist(treino);
				else
					gerenciador.merge(treino);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		treino = new Treino();
		return null;
	}

	public Treino getTreino() {
		return treino;
	}

	public void setTreino(Treino treino) {
		this.treino = treino;
	}
	
	public void addParticipante() {
		if(!treino.getParticipantes().contains(usuarioTreino)&&usuarioTreino!=null) {
		treino.getParticipantes().add(usuarioTreino);
		}else {
			MetodosUteis.addMensagem("Usuario j� incluso na lista de participantes ou usuario vazio");
		}
		}

	public Usuario getUsuarioTreino() {
		return usuarioTreino;
	}

	public void setUsuarioTreino(Usuario usuarioTreino) {
		
		this.usuarioTreino = usuarioTreino;
	}
	
	public String removerParticipante(Usuario p) {
		treino.getParticipantes().remove(p);
		return "Erro ao remover participante";
	}

}
