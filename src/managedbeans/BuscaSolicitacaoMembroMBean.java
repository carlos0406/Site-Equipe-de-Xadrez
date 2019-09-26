package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dao.CamisaDAO;
import dao.SolicitacaoDAO;
import dominio.Camisa;
import dominio.SolicitacaoLivro;
import uteis.MetodosUteis;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaSolicitacaoMembroMBean {
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<SolicitacaoLivro> solicitacoesEncontradas;
	
	/** Permite o acesso ao banco. */
	private SolicitacaoDAO dao;
	
	/** Inicializa��o do MBean */
	@PostConstruct/*Construtor para instanciar alguns objetos*/
	private void init() {
		solicitacoesEncontradas = new ArrayList<>();
		
		dao = new SolicitacaoDAO();
	}
	
	/** Entra na pagina de busca de usu�rios */
	public String entrarBuscarSolicitacoes(){
		return buscar();
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new SolicitacaoDAO();
		
		solicitacoesEncontradas = dao.buscarSolicitacaoMembro(MetodosUteis.getUsuarioLogado().getId());
			
		return "/material/busca_solicitacaoLivroMembros.xhtml";
	}

	public List<SolicitacaoLivro> getSolicitacoesEncontradas() {
		return solicitacoesEncontradas;
	}

	public void setSolicitacoesEncontradas(List<SolicitacaoLivro> solicitacoesEncontradas) {
		this.solicitacoesEncontradas = solicitacoesEncontradas;
	}
}
