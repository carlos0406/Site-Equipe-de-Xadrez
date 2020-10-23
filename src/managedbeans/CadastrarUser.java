package managedbeans;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import com.google.gson.Gson;

import dao.Database;
import dao.UsuarioDAO;
import dominio.Arquivo;
import dominio.Usuario;
import uteis.CriptografiaUtils;
import uteis.MetodosUteis;
import uteis.ValidadorUtil;
//testando commit

@ManagedBean
@ViewScoped
public class CadastrarUser {

	private Usuario usuario;

	public CadastrarUser() {
		usuario = new Usuario();
	}

	/** Entra na tela de edi��o de usuarios. */
	public String entrarEdicaoUsuarios(Usuario usuario) {
		this.usuario = usuario; // o usu�rio a ser editado ser� o recebido pelo
								// par�metro
		return "/login/CadastrarUsuario.xhtml";
	}

	public String cadastrar() {
		boolean erro = false;

		if (MetodosUteis.estaVazia(usuario.getNome())) {
			MetodosUteis.addMensagem("Campo Nome obrigatório!");
			erro = true;
		}
		if (usuario.getDataNascimento() == null) {
			MetodosUteis.addMensagem("Campo Data de nascimento obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(usuario.getRg())) {
			MetodosUteis.addMensagem("Campo RG obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(usuario.getCpf())) {
			MetodosUteis.addMensagem("Campo CPF obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(usuario.getEmail())) {
			MetodosUteis.addMensagem("Campo Email obrigatório!");
			erro = true;
		}

		if (ValidadorUtil.isEmpty(usuario.getSexo())) {
			MetodosUteis.addMensagem("Campo Sexo obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(usuario.getCelular())) {
			MetodosUteis.addMensagem("Campo Celular obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia((usuario.getSenha()))) {
			MetodosUteis.addMensagem("Campo Senha obrigatório!");
			erro = true;
		}
		if (ValidadorUtil.isEmpty((usuario.getTipoUsuario()))) {
			MetodosUteis.addMensagem("Campo Tipo de Usuário obrigatório!");
			erro = true;
		}
		if (MetodosUteis.estaVazia(usuario.getConfirmarSenha())) {
			MetodosUteis.addMensagem("Campo Confirmar senha obrigatório!");
			erro = true;
		}
		if (!MetodosUteis.estaVazia(usuario.getSenha()) && !MetodosUteis.estaVazia(usuario.getConfirmarSenha())
				&& usuario.getSenha().equals(usuario.getConfirmarSenha())) {
			erro = false;
		} else if (!MetodosUteis.estaVazia(usuario.getSenha())
				&& !MetodosUteis.estaVazia(usuario.getConfirmarSenha())) {
			MetodosUteis.addMensagem("As senhas estão diferentes!");
			erro = true;
		}
		
		if (erro)
			return null;
		else {
			if (usuario.getId() != 0 && ValidadorUtil.isEmpty(usuario.getSenha())){
				//Se for edição, só deve modificar a senha caso o usu�rio tenha digitado alguma coisa
				//no campo de senha, ou seja, caso a senha esteja vazia, ela n�o deve ser modificada (deve
				//permanecer a mesma do banco).
				
				UsuarioDAO dao = new UsuarioDAO();
				dao.desanexarEntidade(usuario); //retira o usu�rio da mem�ria do hibernate para evitar erros
				
				//Busca novamente o usuário no banco
				Usuario usuarioBanco = dao.encontrarPeloID(usuario.getId(), Usuario.class);
				//Como ele não digitou nada na senha, ela deve permanecer a mesma do banco
				usuario.setSenha(usuarioBanco.getSenha()); //A senha do banco j� est� criptografada 
			} else {
				//Nos demais casos (cadastro ou edição com mudança de senha), a senha não está criptografada,
				//então devemos criptografá-la
				usuario.setSenha(CriptografiaUtils.criptografarMD5(usuario.getSenha()));
			}
			
			// verificando se o usu�rio anexou foto

			EntityManager gerenciador = Database.getInstance().getEntityManager();
			gerenciador.getTransaction().begin();
			
			try {
				if (usuario.getFoto() != null && !MetodosUteis.estaVazia(usuario.getFoto().getFileName())
						&& usuario.getFoto().getSize() != -1) {

					// Criando uma entidade Arquivo
					Arquivo arq = new Arquivo();
					arq.setNome(usuario.getFoto().getFileName());
					arq.setBytes(usuario.getFoto().getContents());

					gerenciador.persist(arq);

					usuario.setIdFoto(arq.getId());
				}
				
				if (usuario.getId() == 0)
					gerenciador.persist(usuario);
				else
					gerenciador.merge(usuario);

				gerenciador.getTransaction().commit();
				MetodosUteis.addMensagem("Seu cadastro est� pronto!");
				

			} catch (Exception e) {
				e.printStackTrace();

				if (gerenciador.getTransaction().isActive()) {
					gerenciador.getTransaction().rollback();
				}
			}

		}

		usuario = new Usuario();
		return null;
	}

	public List<String> getBancos() {
		List<String> bancos = new ArrayList<String>();
		bancos.add("Banco do Brasil");
		bancos.add("Caixa Econômica");
		bancos.add("Bradesco");
		bancos.add("Banco do Nordeste");

		return bancos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}