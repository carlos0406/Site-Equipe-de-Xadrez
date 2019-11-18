package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.primefaces.model.UploadedFile;

import uteis.CriptografiaUtils;

@Entity
public class Camisa {
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column(name="id_camisa", nullable = false)
	private int id_arquivo_camisa;
	
	@Column (nullable = false, columnDefinition = "TEXT")
	private String autor_camisa;
	
	@Column (nullable = false)
	private Integer ano_camisa;
	
	@Column (nullable = false)
	private Integer idFoto;
	
	@Transient
	private UploadedFile fotoCamisa;
	
	public String getUrlFotoCamisa(){
		return "/verArquivo?"
				+ "idArquivo=" + getIdFoto() //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}

	public int getId_arquivo_camisa() {
		return id_arquivo_camisa;
	}

	public void setId_arquivo_camisa(int id_arquivo_camisa) {
		this.id_arquivo_camisa = id_arquivo_camisa;
	}

	public String getAutor_camisa() {
		return autor_camisa;
	}

	public void setAutor_camisa(String autor_camisa) {
		this.autor_camisa = autor_camisa;
	}
	
	public Integer getAno_camisa() {
		return ano_camisa;
	}

	public void setAno_camisa(Integer ano_camisa) {
		this.ano_camisa = ano_camisa;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}

	public UploadedFile getFotoCamisa() {
		return fotoCamisa;
	}

	public void setFotoCamisa(UploadedFile fotoCamisa) {
		this.fotoCamisa = fotoCamisa;
	}
}