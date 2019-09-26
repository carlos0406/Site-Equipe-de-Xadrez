package managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAO;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.CriptografiaUtils;
import uteis.MetodosUteis;

@ManagedBean
@SessionScoped
public class LoginMBean {
	
	private Usuario usuario = new Usuario();
	
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
	
	public String entrar() {
		if (!validarLogin()) {
			return null;
		}

		try {
			UsuarioDAO dao = new UsuarioDAO ();
			usuario = dao.findUsuarioByLoginSenha(usuario.getEmail(),
					CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			if (!MetodosUteis.estaVazia(usuario)) {
				if (!usuario.isAtivo()) {
					MetodosUteis.addMensagem ("Este usuário foi desabilitado e não possui mais acesso ao sistema.");
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
			} else if (usuario.getTipoUsuario().equals(TipoUsuario.BOLSISTA)){
				return "home.xhtml";
			} else if (usuario.getTipoUsuario().equals(TipoUsuario.MEMBRO)){
				return "home.xhtml";
			} else if (usuario.getTipoUsuario().equals(TipoUsuario.COMUM)){
				return "home.xhtml";
			} else {
				usuario = new Usuario();																																																				
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			MetodosUteis.addMensagem("Ocorreu um erro!");
			return null;
		}

	}
	/***Sair da conta*/
	public String sair(){
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
}