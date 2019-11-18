package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.primefaces.model.UploadedFile;

import uteis.CriptografiaUtils;
import uteis.MetodosUteis;

@Entity
public class Noticia {
	
	@Id
	@GeneratedValue  (strategy=GenerationType.IDENTITY)  
	@Column(name="id_Noticia", nullable = false)
	private int id_arquivoNoticia;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String titulo_noticia;
	
	@Column(nullable = false, columnDefinition="TEXT")
	private String texto_noticia;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String nome_autor;
	
	@Column(nullable = false)
	private String palavraChave;
	
	@Column(nullable = false)
	private boolean ativo = true;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="noticia_foto",
			joinColumns=@JoinColumn(name="id_noticia"),
			inverseJoinColumns=@JoinColumn(name="id_foto"))
	private List<Arquivo> idsFotos;
	
	/** 
	 * Atributo n�o persisitido que armazena uma foto que o usu�rio deseja
	 * para seu perfil.
	 * */
	@Transient
	private List<UploadedFile> fotosNoticia = new ArrayList<>();
	
	/** Obt�m a URL atrav�s da qual a foto do usu�rio pode ser carregada. */
	public String getUrlFotoNoticia(Integer idFoto){
		return "/verArquivo?"
				+ "idArquivo=" + idFoto //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(idFoto)) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}
	
	public String getUrlPrimeiraFoto(){
		Integer idPrimeiraFoto = MetodosUteis.estaVazia(idsFotos) ? 
									null :
									idsFotos.get(0).getId();
		
		return "/verArquivo?"
				+ "idArquivo=" + idPrimeiraFoto //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(idPrimeiraFoto)) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}

	public int getId_arquivoNoticia() {
		return id_arquivoNoticia;
	}

	public void setId_arquivoNoticia(int id_arquivoNoticia) {
		this.id_arquivoNoticia = id_arquivoNoticia;
	}

	public String getTitulo_noticia() {
		return titulo_noticia;
	}

	public void setTitulo_noticia(String titulo_noticia) {
		this.titulo_noticia = titulo_noticia;
	}

	public String getNome_autor() {
		return nome_autor;
	}

	public void setNome_autor(String nome_autor) {
		this.nome_autor = nome_autor;
	}

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public List<Arquivo> getIdsFotos() {
		return idsFotos;
	}

	public void setIdsFotos(List<Arquivo> idsFotos) {
		this.idsFotos = idsFotos;
	}

	public String getTexto_noticia() {
		return texto_noticia;
	}

	public void setTexto_noticia(String texto_noticia) {
		this.texto_noticia = texto_noticia;
	}

	public List<UploadedFile> getFotosNoticia() {
		return fotosNoticia;
	}

	public void setFotosNoticia(List<UploadedFile> fotosNoticia) {
		this.fotosNoticia = fotosNoticia;
	}

}
