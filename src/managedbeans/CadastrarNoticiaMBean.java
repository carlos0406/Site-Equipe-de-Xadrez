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
import dominio.Noticia;
import uteis.CompressaoImagemUtils;
import uteis.MetodosUteis;

@ManagedBean
@SessionScoped
public class CadastrarNoticiaMBean {
	
	private Noticia noticia;

	public CadastrarNoticiaMBean() {
		noticia = new Noticia();
	}

	/** Entra na tela de edi��o de usu�rios. */
	public String entrarEdicaoNoticias(Noticia noticia) {
		DAOGenerico dao = new DAOGenerico();
		this.noticia = dao.encontrarPeloID(noticia.getId_arquivoNoticia(), Noticia.class);
		
		return "CadastrarNoticia.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(noticia.getAutor_noticia())) {
			MetodosUteis.addMensagem("Campo Nome do autor obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(noticia.getTexto_noticia())) {
			MetodosUteis.addMensagem("Campo texto da noticia obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(noticia.getPalavraChave())) {
			MetodosUteis.addMensagem("Campo palavra-chave obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(noticia.getTitulo_noticia())) {
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
//				if (noticia.getFotoNoticia() != null && !MetodosUteis.estaVazia(noticia.getFotoNoticia().getFileName())
//						&& noticia.getFotoNoticia().getSize() != -1) {
				
//				if (!MetodosUteis.estaVazia(noticia.getFotosNoticia())) {
//					
//					if (noticia.getIdsFotos() == null)
//						noticia.setIdsFotos(new ArrayList<>());
//					
//					for (UploadedFile f : noticia.getFotosNoticia()) {
//						// Criando uma entidade Arquivo
//						Arquivo arq = new Arquivo();
//						arq.setNome(f.getFileName());
//						arq.setBytes(CompressaoImagemUtils.resize(f.getInputstream()));
//						
//						noticia.getIdsFotos().add(arq);
//					}
//					
//				}
				
				if (noticia.getId_arquivoNoticia() == 0){
					noticia.setDataCadastro(new Date());
					gerenciador.persist(noticia);
				} else
					gerenciador.merge(noticia);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		noticia= new Noticia();
		return null;
	}
	
	public void fazerUploadFotos(FileUploadEvent event) throws IOException {
		if (noticia.getIdsFotos() == null)
			noticia.setIdsFotos(new ArrayList<>());
		
		UploadedFile f = event.getFile();
		
		// Criando uma entidade Arquivo
		Arquivo arq = new Arquivo();
		arq.setNome(f.getFileName());
		arq.setBytes(CompressaoImagemUtils.resize(f.getInputstream()));
		
		noticia.getIdsFotos().add(arq);
		
		MetodosUteis.addMensagem("As fotos foram adicionadas.");
		
		//noticia.getFotosNoticia().add(event.getFile());
    }

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}
	
}
