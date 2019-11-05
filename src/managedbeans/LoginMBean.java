package managedbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import dao.Database;
import dao.UsuarioDAO;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.ControladorPadraoRespostaAPI;
import uteis.CriptografiaUtils;
import uteis.MetodosUteis;

@ManagedBean
@SessionScoped
public class LoginMBean {

	private Usuario usuario = new Usuario();
	private Usuario u = new Usuario();
	private boolean suap;

	public boolean validarLogin() {

		boolean validou = true;

		if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
			MetodosUteis.addMensagem("Campo matrícula tem que ser preenchido");
			validou = false;
		}
		if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
			MetodosUteis.addMensagem("Campo senha tem que ser preenchido");
			validou = false;
		}

		return validou;
	}

	public String entrar() throws ClientProtocolException, IOException {
		if (!validarLogin()) {
			return null;
		}

		try {
			UsuarioDAO dao = new UsuarioDAO();


			if (!isNumeric(usuario.getEmail())) {
				usuario = dao.findUsuarioByLoginSenha(usuario.getEmail(),
						CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			} else {
				usuario=dao.findUsuarioByMatricula(usuario.getEmail());
				System.out.println(usuario);
				
			}
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
			MetodosUteis.addMensagem("Ocorreu um erro!");
			return null;
		}

		String token = buscarToken();

		suap = (token != null) ? true : false;

		if (suap) {
			System.out.println(token);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/");

			httpget.addHeader("Content-Type", "application/json");
			httpget.addHeader("Authorization", "JWT " + buscarToken());

			String responseGET = httpclient.execute(httpget, new ControladorPadraoRespostaAPI());
			System.out.println(responseGET);

			Gson gson = new Gson();
			HashMap meusDados = gson.fromJson(responseGET, HashMap.class);

			System.out.println(meusDados);

			/*
			 * Logar com o suap diretamente caso o usuario j� esteja Usuario
			 * usuarioBuscaSuap=new UsuarioDAO().findUsuarioByLoginSenha((String)
			 * meusDados.get("email"),CriptografiaUtils.criptografarMD5(usuario.getSenha())
			 * ); if(usuarioBuscaSuap!=null) {
			 * 
			 * 
			 * }
			 */

			u = new Usuario();
			u.setNome(meusDados.get("nome_usual").toString());
			u.setEmail((String) meusDados.get("email"));
			u.setCpf((String) meusDados.get("cpf"));
			u.setSenha(CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			EntityManager gerenciador = Database.getInstance().getEntityManager();
			u.setTipoUsuario(TipoUsuario.MEMBRO);
			u.setSexo('M');
			u.setRg("0000000000");
			u.setMatricula(Long.parseLong(usuario.getEmail()));
			u.setDataNascimento(new Date(10, 10, 10));
			u.setAtivo(true);
			u.setCelular("2345678");

			if (usuario != null && usuario.getId() != 0) {
				u.setId(usuario.getId());
			}

			gerenciador.getTransaction().begin();

			System.out.println(u);
			gerenciador.merge(u);
			gerenciador.getTransaction().commit();
			
			usuario = u;

				// MetodosUteis.getCurrentSession().setAttribute("usuarioLogado", u);
				/*
				 * if (usuario.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) { return
				 * "home.xhtml"; } else if
				 * (usuario.getTipoUsuario().equals(TipoUsuario.BOLSISTA)){ return "home.xhtml";
				 * } else if (usuario.getTipoUsuario().equals(TipoUsuario.MEMBRO)){ return
				 * "home.xhtml"; } else if (usuario.getTipoUsuario().equals(TipoUsuario.COMUM)){
				 * return "home.xhtml"; } else { usuario = new Usuario(); return null; }
				 */

		}
		
		if (!MetodosUteis.estaVazia(usuario)) {
			if (!usuario.isAtivo()) {
				MetodosUteis.addMensagem("Este usuário foi desabilitado e não possui mais acesso ao sistema.");
				return null;
			}
		} else {
			this.usuario = new Usuario();
			MetodosUteis.addMensagem("Usuário/Senha incorretos.");
			return null;
		}

		// Salvar usuário permamentemente na memória do sistema
		MetodosUteis.getCurrentSession().setAttribute("usuarioLogado", usuario);

		if (usuario.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
			return "home.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.BOLSISTA)) {
			return "home.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.MEMBRO)) {
			return "home.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.COMUM)) {
			return "home.xhtml";
		} else {
			usuario = new Usuario();
			return null;
		}

	}

	/*** Sair da conta */
	public String sair() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		HttpSession sessao = req.getSession(true);
		sessao.invalidate();
		return "/index.xhtml";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	private String buscarToken() throws ClientProtocolException, IOException {

		Form form = Form.form();
		form.add("username", usuario.getEmail());
		form.add("password", usuario.getSenha());

		HttpResponse response = Request.Post("https://suap.ifrn.edu.br/api/v2/autenticacao/token/")
				.bodyForm(form.build()).execute().returnResponse();

		HashMap tokenHM;

		if (response != null) {
			InputStream source = response.getEntity().getContent();
			Reader reader = new InputStreamReader(source);
			Gson gson = new Gson();
			tokenHM = gson.fromJson(reader, HashMap.class);
			String token = null;
			if (tokenHM.get("token") != null)
				token = tokenHM.get("token").toString();
			System.out.println("Token:" + token);
			return token;
		}

		return null;
	}
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
}