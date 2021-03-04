package managedbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

		Usuario usuarioBanco;
		
		try {
			UsuarioDAO dao = new UsuarioDAO();

			if (!isNumeric(usuario.getEmail())) {
				usuarioBanco = dao.findUsuarioByLoginSenha(usuario.getEmail(),
						CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			} else {
				usuarioBanco = dao.findUsuarioByMatriculaSenha(Long.valueOf(usuario.getEmail()),CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			MetodosUteis.addMensagem("Ocorreu um erro!");
			usuario = new Usuario();
			return null;
		}

		String token = buscarToken();

		suap = (token != null) ? true : false;
		
		if (suap &&usuarioBanco==null) {
			UsuarioDAO dao = new UsuarioDAO();
			// Pegando dados a partir do token gerado para salvar no banco de dados
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet("https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/");

			httpget.addHeader("Content-Type", "application/json");
			httpget.addHeader("Authorization", "JWT " + buscarToken());

			String responseGET = httpclient.execute(httpget, new ControladorPadraoRespostaAPI());

			Gson gson = new Gson();
			HashMap meusDados = gson.fromJson(responseGET, HashMap.class);
			
			String matricula = usuario.getEmail();
			String senha = usuario.getSenha();
			
			usuario = new Usuario();
			usuario.setNome(meusDados.get("nome_usual").toString());	
			usuario.setEmail((String) meusDados.get("email"));
			usuario.setCpf((String) meusDados.get("cpf"));
			usuario.setSenha(CriptografiaUtils.criptografarMD5(senha));
			EntityManager gerenciador = Database.getInstance().getEntityManager();
			usuario.setTipoUsuario(TipoUsuario.MEMBRO);
			usuario.setSexo(null);
			usuario.setMatricula(Long.parseLong(matricula));
			
			String rg = (String)meusDados.get("rg");
			rg = rg.substring(0, rg.indexOf(" "));
			usuario.setRg(rg);
			
			String data = meusDados.get("data_nascimento").toString();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
			try {
				date = formatter.parse(data);
				usuario.setDataNascimento(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			usuario.setAtivo(true);
			usuario.setCelular(null);

			if (usuarioBanco != null && usuarioBanco.getId() != 0) {
				usuario.setId(usuarioBanco.getId());
			}

			gerenciador.getTransaction().begin();

			if (usuario.getId() == 0) {
				
				gerenciador.persist(usuario);
			}else {
				gerenciador.merge(usuario);
			}
			gerenciador.getTransaction().commit();

			

		}
		
		if (usuario.getId() == 0 && usuarioBanco != null && usuarioBanco.getId() != 0)
			usuario = usuarioBanco;

		if (usuario != null && usuario.getId() != 0) {
			if (!usuario.isAtivo()) {
				MetodosUteis.addMensagem("Este usuário foi desabilitado e não possui mais acesso ao sistema.");
				usuario = new Usuario();
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
			usuario = new Usuario();
			return "index.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.BOLSISTA)) {
			usuario = new Usuario();
			return "index.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.MEMBRO)) {
			usuario = new Usuario();
			return "index.xhtml";
		} else if (usuario.getTipoUsuario().equals(TipoUsuario.COMUM)) {
			usuario = new Usuario();
			return "index.xhtml";
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
		
			return token;
		}

		return null;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}