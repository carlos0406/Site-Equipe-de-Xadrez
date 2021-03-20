package managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.SolicitacaoDAO;
import dominio.EventoTorneio;
import dominio.SolicitacaoLivro;

@SuppressWarnings("serial") /*Parar de exibir falsos erros*/
@ManagedBean
@SessionScoped
public class BuscaSolicitacoesMBean {
	/** Armazena os usu�rios encontrados no banco de acordo com os par�metros de busca. */
	private List<SolicitacaoLivro> solicitacoesEncontradas;
	
	private String nomeUsuario;
	
	private String nomeLivro;
	
	private Date dataInicioBusca;
	
	private Date dataFimBusca;
	
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
		return "busca_solicitacaoLivro.xhtml";
	}
	
	/** Realiza a busca de usu�rios no banco. */
	public String buscar(){
		dao = new SolicitacaoDAO();
		
		solicitacoesEncontradas = dao.buscarSolicitacao(nomeLivro, nomeUsuario, dataInicioBusca, dataFimBusca);
			
		return "busca_solicitacaoLivro.xhtml";
	}
	
	public String deferir(SolicitacaoLivro s){
		EntityManager gerenciador = Database.getInstance().getEntityManager();
		
		try {
			gerenciador.getTransaction().begin();
			
			s.setAprovado(true);
			
			gerenciador.merge(s);
			gerenciador.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

			if (gerenciador.getTransaction().isActive()) {
				gerenciador.getTransaction().rollback();
			}
		}
		
		return buscar();
	}
	
	public String indeferir(SolicitacaoLivro s){
		EntityManager gerenciador = Database.getInstance().getEntityManager();
		
		try {
			gerenciador.getTransaction().begin();
			
			s.setAprovado(false);
			
			gerenciador.merge(s);
			gerenciador.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

			if (gerenciador.getTransaction().isActive()) {
				gerenciador.getTransaction().rollback();
			}
		}
		return buscar();
	}
	
	public String devolver(SolicitacaoLivro s){
		EntityManager gerenciador = Database.getInstance().getEntityManager();
		
		try {
			gerenciador.getTransaction().begin();
			
			s.setDevolvido(true);
			
			gerenciador.merge(s);
			gerenciador.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

			if (gerenciador.getTransaction().isActive()) {
				gerenciador.getTransaction().rollback();
			}
		}
		
		return buscar();
	}

	public String deletar(SolicitacaoLivro s) {
		EntityManager em = Database.getInstance().getEntityManager();
		
		try {
			//Iniciando transa��o com o banco de dados
			em.getTransaction().begin();
			
			dao.remover(s);
			
			//Transa��o confirmada
			em.getTransaction().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			
			if (em.getTransaction().isActive())
				//Como ocorreu erro, a transa��o n�o ser� confirmada
				em.getTransaction().rollback();
		}
		
		return buscar();

		
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getNomeLivro() {
		return nomeLivro;
	}

	public void setNomeLivro(String nomeLivro) {
		this.nomeLivro = nomeLivro;
	}

	public Date getDataInicioBusca() {
		return dataInicioBusca;
	}

	public void setDataInicioBusca(Date dataInicioBusca) {
		this.dataInicioBusca = dataInicioBusca;
	}

	public Date getDataFimBusca() {
		return dataFimBusca;
	}

	public void setDataFimBusca(Date dataFimBusca) {
		this.dataFimBusca = dataFimBusca;
	}

	public List<SolicitacaoLivro> getSolicitacoesEncontradas() {
		return solicitacoesEncontradas;
	}

	public void setSolicitacoesEncontradas(List<SolicitacaoLivro> solicitacoesEncontradas) {
		this.solicitacoesEncontradas = solicitacoesEncontradas;
	}

	public SolicitacaoDAO getDao() {
		return dao;
	}

	public void setDao(SolicitacaoDAO dao) {
		this.dao = dao;
	}
	
	

}
