package managedbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import dao.Database;
import dao.EventoTorneioDAO;
import dao.UsuarioDAO;
import dominio.EventoTorneio;
import dominio.RodadaTorneio;
import dominio.Usuario;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;

@ManagedBean
@SessionScoped
public class CadastarRodadaTorneioMBean {
	private RodadaTorneio rodadaTorneio;
	
	
	public CadastarRodadaTorneioMBean() {
		rodadaTorneio=new RodadaTorneio();
	}
	
	public List<EventoTorneio> autoCompleteTorneios(String query){

		return new EventoTorneioDAO().buscarPelaColunaLike("nome_torneio", query, EventoTorneio.class);

	}
	

	public String cadastrar() {
		boolean erro = false;
		
		

		if (MetodosUteis.estaVazia(rodadaTorneio.getNomeAdversario())) {
			MetodosUteis.addMensagem("Campo nome adversario obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(rodadaTorneio.getRodada())) {
			MetodosUteis.addMensagem("Campo rodada obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(rodadaTorneio.getMembro())) {
			MetodosUteis.addMensagem("Campo Membro obrigatório!");
			erro = true;
		}
		
		if (ValidadorUtil.isEmpty(rodadaTorneio.getTorneio())) {
			MetodosUteis.addMensagem("Campo torneio obrigatório!");
			erro = true;
		}
		
		if (ValidadorUtil.isEmpty(rodadaTorneio.getResultado())) {
			MetodosUteis.addMensagem("Campo torneio obrigatório!");
			erro = true;
		}
		
		
			
		if (erro)
			return null;
		else {
			

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			
			try {
				if (rodadaTorneio.getId() == 0)
					gerenciador.persist(rodadaTorneio);
				else
					gerenciador.merge(rodadaTorneio);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro está pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		rodadaTorneio = new RodadaTorneio();
		return null;
	}

	public RodadaTorneio getRodadaTorneio() {
		return rodadaTorneio;
	}

	public void setRodadaTorneio(RodadaTorneio rodadaTorneio) {
		this.rodadaTorneio = rodadaTorneio;
	}
	
	public String entrarEdicaoRodada(RodadaTorneio r) {
		this.rodadaTorneio = r; 
								
		return "/eventos/cadastrar_rodada.xhtml";
	}
	
	
	
	

}
