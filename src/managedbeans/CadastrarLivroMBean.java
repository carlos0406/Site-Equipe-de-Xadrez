package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dominio.Arquivo;
import dominio.Livro;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;

@ManagedBean
@SessionScoped
public class CadastrarLivroMBean {
	private Livro livro;

	public CadastrarLivroMBean() {
		livro = new Livro();
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoLivros(Livro livro) {
		this.livro = livro; // o livro a ser editado sera o recebido pelo
								// parametro
		return "CadastrarLivro.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(livro.getNome_livro())) {
			MetodosUteis.addMensagem("Campo Nome do livro obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(livro.getAno_livro())) {
			MetodosUteis.addMensagem("Campo Ano de fabricação obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(livro.getNome_autor())) {
			MetodosUteis.addMensagem("Campo Nome do autor obrigatório!");
			erro = true;
		}
		if (ValidadorUtil.isEmpty(livro.getTipo())) {
			MetodosUteis.addMensagem("Campo tipo obrigatório!");
			erro = true;
		}

		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				if (livro.getFotoLivro() != null && !MetodosUteis.estaVazia(livro.getFotoLivro().getFileName())
						&& livro.getFotoLivro().getSize() != -1) {

					// Criando uma entidade Arquivo
					Arquivo arq = new Arquivo();
					arq.setNome(livro.getFotoLivro().getFileName());
					arq.setBytes(livro.getFotoLivro().getContents());

					gerenciador.persist(arq);

					livro.setIdFoto(arq.getId());
				}
				
				if (livro.getId_arquivoLivro() == 0)
					gerenciador.persist(livro);
				else
					gerenciador.merge(livro);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		livro = new Livro();
		return null;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
}
