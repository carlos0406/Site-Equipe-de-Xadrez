package managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dominio.Arquivo;
import dominio.Camisa;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;

@ManagedBean
@SessionScoped
public class CadastrarCamisaMBean {
	private Camisa camisa;
	
	public CadastrarCamisaMBean(){
		camisa = new Camisa();
	}
	/** Entra na tela de edicao de usuarios. */
	public String entrarEdicaoCamisas(Camisa camisa) {
		this.camisa = camisa; // o livro a ser editado sera o recebido pelo
								// parametro
		return "CadastrarCamisa.xhtml";
	}
	public String cadastrar() {
		boolean erro = false;
		
		if (camisa.getId_arquivo_camisa() == 0 && (MetodosUteis.estaVazia(camisa.getFotoCamisa().getFileName())
				|| camisa.getFotoCamisa().getSize() == -1)) {
			MetodosUteis.addMensagem("Foto obrigatória!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(camisa.getAno_camisa())) {
			MetodosUteis.addMensagem("Campo Ano da camisa obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(camisa.getAutor_camisa())) {
			MetodosUteis.addMensagem("Campo Nome do desenhista obrigatório!");
			erro = true;
		}
		if (erro)
			return null;
		else {
			// verificando se o usuário anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				if (camisa.getFotoCamisa() != null && !MetodosUteis.estaVazia(camisa.getFotoCamisa().getFileName())
						&& camisa.getFotoCamisa().getSize() != -1) {

					// Criando uma entidade Arquivo
					Arquivo arq = new Arquivo();
					arq.setNome(camisa.getFotoCamisa().getFileName());
					arq.setBytes(camisa.getFotoCamisa().getContents());

					gerenciador.persist(arq);

					camisa.setIdFoto(arq.getId());
				}
				
				if (camisa.getId_arquivo_camisa() == 0)
					gerenciador.persist(camisa);
				else
					gerenciador.merge(camisa);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		camisa = new Camisa();
		return null;
	}
	public Camisa getCamisa() {
		return camisa;
	}
	public void setCamisa(Camisa camisa) {
		this.camisa = camisa;
	}
	
}
