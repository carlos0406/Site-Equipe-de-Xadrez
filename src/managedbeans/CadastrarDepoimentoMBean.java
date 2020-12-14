package managedbeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import dao.DAOGenerico;
import dao.Database;
import dominio.Arquivo;
import dominio.Depoimento;
import uteis.CompressaoImagemUtils;
import uteis.MetodosUteis;

@ManagedBean
@SessionScoped
public class CadastrarDepoimentoMBean {
	
	private Depoimento depoimento;

	public CadastrarDepoimentoMBean() {
		depoimento = new Depoimento();
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoDepoimento(Depoimento depoimento) {
		DAOGenerico dao = new DAOGenerico();
		this.depoimento = dao.encontrarPeloID(depoimento.getId_depoimento(), Depoimento.class);
		
		return "CadastrarDepoimento.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(depoimento.getAutor_depoimento())) {
			MetodosUteis.addMensagem("Campo Nome do autor obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(depoimento.getTexto_depoimento())) {
			MetodosUteis.addMensagem("Campo texto do Depoimento obrigatório!");
			erro = true;
		}
		
		if (MetodosUteis.estaVazia(depoimento.getTitulo_depoimento())) {
			MetodosUteis.addMensagem("Campo título da notícia obrigatório!");
			erro = true;
		}
		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
//				
				
				if (depoimento.getId_depoimento() == 0){
					depoimento.setDataCadastro(new Date());
					gerenciador.persist(depoimento);
				} else
					gerenciador.merge(depoimento);
				
				

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		depoimento = new Depoimento();
		return null;
	}
	
	public void fazerUploadFotos(FileUploadEvent event) throws IOException {
		if (depoimento.getIdsFotos() == null)
			depoimento.setIdsFotos(new ArrayList<>());
		
		UploadedFile f = event.getFile();
		
		// Criando uma entidade Arquivo
		Arquivo arq = new Arquivo();
		arq.setNome(f.getFileName());
		arq.setBytes(CompressaoImagemUtils.resize(f.getInputstream()));
		
		depoimento.getIdsFotos().add(arq);
		
		MetodosUteis.addMensagem("As fotos foram adicionadas.");
		
		
    }

	public Depoimento getDepoimento() {
		return depoimento;
	}

	public void setDepoimento(Depoimento depoimento) {
		this.depoimento = depoimento;
	}
	
}
