package dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.primefaces.model.UploadedFile;

import uteis.CriptografiaUtils;
import uteis.ValidadorUtil;

@Entity
public class Livro {
	
	@Id
	@GeneratedValue  (strategy=GenerationType.IDENTITY)  
	@Column(name="id_arquivoLivro", nullable = false)
	private int id_arquivoLivro;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String nome_livro;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String nome_autor;
	
	@Column(nullable = false)
	private String tipo;
	
	@Column(nullable = false)
	private boolean ativo = true;
	
	@Column(nullable = false)
	private boolean pedir_Livro = true;
	
	@Column(nullable = false)
	private String ano_livro;
	
	@Column(columnDefinition = "TEXT")
	private String nome_beneficiado;
	
	private Date data_emprestimo;
	
	private String data_devolucao;
	
	@Column(name="id_foto")
	private Integer idFoto;
	
	/** 
	 * Atributo n�o persisitido que armazena uma foto que o usu�rio deseja
	 * para seu perfil.
	 * */
	@Transient
	private UploadedFile fotoLivro;
	
	/** Obt�m a URL atrav�s da qual a foto do usu�rio pode ser carregada. */
	public String getUrlFotoLivro(){
		return "/verArquivo?"
				+ "idArquivo=" + getIdFoto() //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}
	
	
	
	public boolean isPedir_Livro() {
		return pedir_Livro;
	}



	public void setPedir_Livro(boolean pedir_Livro) {
		this.pedir_Livro = pedir_Livro;
	}



	public int getId_arquivoLivro() {
		return id_arquivoLivro;
	}


	public void setId_arquivoLivro(int id_arquivoLivro) {
		this.id_arquivoLivro = id_arquivoLivro;
	}


	public String getNome_livro() {
		return nome_livro;
	}


	public void setNome_livro(String nome_livro) {
		this.nome_livro = nome_livro;
	}


	public String getNome_autor() {
		return nome_autor;
	}


	public void setNome_autor(String nome_autor) {
		this.nome_autor = nome_autor;
	}


	public String getAno_livro() {
		return ano_livro;
	}


	public void setAno_livro(String ano_livro) {
		this.ano_livro = ano_livro;
	}


	public String getNome_beneficiado() {
		return nome_beneficiado;
	}


	public void setNome_beneficiado(String nome_beneficiado) {
		this.nome_beneficiado = nome_beneficiado;
	}


	public Date getData_emprestimo() {
		return data_emprestimo;
	}


	public void setData_emprestimo(Date data_emprestimo) {
		this.data_emprestimo = data_emprestimo;
	}

	public String getData_devolucao() {
		return data_devolucao;
	}

	public void setData_devolucao(String data_devolucao) {
		this.data_devolucao = data_devolucao;
	}

	public UploadedFile getFotoLivro() {
		return fotoLivro;
	}

	public void setFotoLivro(UploadedFile fotoLivro) {
		this.fotoLivro = fotoLivro;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean tipoUsuario) {
		this.ativo = tipoUsuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
