package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import uteis.CriptografiaUtils;

/** 
 * Entidade respons�vel por armazenar um arquivo no banco de dados.
 */
@Entity
public class Arquivo {
	
	@Id
    @GeneratedValue  (strategy=GenerationType.IDENTITY)
	@Column(name="id_arquivo", nullable = false)
	private int id;
	
	@Column(nullable = false)
	private String nome;
	
	/** Bytes do arquivo. */
	@Column(nullable = false)
	private byte[] bytes;
	
	public String getUrlFoto(){
		return "/verArquivo?"
				+ "idArquivo=" + id //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(id)) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
