package managedbeans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.LivroDAO;
import dominio.Arquivo;
import dominio.Livro;
import dominio.SolicitacaoLivro;
import uteis.MetodosUteis;

@ManagedBean
@SessionScoped
public class CadastrarSolicitacao {
	private SolicitacaoLivro solicitacao;

	public CadastrarSolicitacao() {
		solicitacao = new SolicitacaoLivro();
		solicitacao.setLivroSolicitado(new Livro());
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoSolicitacoes(SolicitacaoLivro solicitacao) {
		this.solicitacao = solicitacao; // o livro a ser editado sera o recebido pelo
								// parametro
		return "CadastrarSolicitacao.xhtml";
	}
	
	public String entrarCadastroSolicitacaoLivro(Livro livro) {
		solicitacao.setLivroSolicitado(livro);
		solicitacao.setUsuarioSolicitante(MetodosUteis.getUsuarioLogado());
		
		return "CadastrarSolicitacaoLivro.xhtml";
	}
	
	public List<Livro> autocompleteLivroByNome (String query){
		return new LivroDAO().buscarPelaColunaLike("nome_livro", query, Livro.class);
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(solicitacao.getDataPrazo())) {
			MetodosUteis.addMensagem("Campo prazo obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(solicitacao.getLivroSolicitado())) {
			MetodosUteis.addMensagem("Campo texto da noticia obrigatório!");
			erro = true;
		}
		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				
				if (solicitacao.getId() == 0){
					solicitacao.setDataSolicitacao(new Date());
					gerenciador.persist(solicitacao);
				} else
					gerenciador.merge(solicitacao);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		solicitacao = new SolicitacaoLivro();
		return null;
	}

	public SolicitacaoLivro getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SolicitacaoLivro solicitacao) {
		this.solicitacao = solicitacao;
	}
}