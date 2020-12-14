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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.primefaces.model.UploadedFile;

import uteis.CriptografiaUtils;
import uteis.MetodosUteis;

@Entity
public class Depoimento {
	
	@Id
	@GeneratedValue  (strategy=GenerationType.IDENTITY)  
	@Column(name="id_Depoimento", nullable = false)
	private int id_depoimento;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String titulo_depoimento;
	
	@Column(nullable = false, columnDefinition="TEXT")
	private String texto_depoimento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_usuario_autor_depoimento",nullable = false)
	private Usuario autor_depoimento;
	
	
	

	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(name="depoimento_foto",
			joinColumns=@JoinColumn(name="id_depoimento"),
			inverseJoinColumns=@JoinColumn(name="id_foto"))
	private List<Arquivo> idsFotos;
	
	/** 
	 * Atributo n�o persisitido que armazena uma foto que o usu�rio deseja
	 * para seu perfil.
	 * */
	@Transient
	private List<UploadedFile> fotosDepoimento = new ArrayList<>();
	
	/** Obt�m a URL atrav�s da qual a foto do usu�rio pode ser carregada. */
	
	public String getUrlFotoDepoimento(Integer idFoto){
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

	

	

	public int getId_depoimento() {
		return id_depoimento;
	}


	public void setId_depoimento(int id_depoimento) {
		this.id_depoimento = id_depoimento;
	}


	public String getTitulo_depoimento() {
		return titulo_depoimento;
	}


	public void setTitulo_depoimento(String titulo_depoimento) {
		this.titulo_depoimento = titulo_depoimento;
	}


	public String getTexto_depoimento() {
		return texto_depoimento;
	}


	public void setTexto_depoimento(String texto_depoimento) {
		this.texto_depoimento = texto_depoimento;
	}




	public Usuario getAutor_depoimento() {
		return autor_depoimento;
	}


	public void setAutor_depoimento(Usuario autor_depoimento) {
		this.autor_depoimento = autor_depoimento;
	}


	public List<UploadedFile> getFotosDepoimento() {
		return fotosDepoimento;
	}


	public void setFotosDepoimento(List<UploadedFile> fotosDepoimento) {
		this.fotosDepoimento = fotosDepoimento;
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

	

}
