package dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.primefaces.model.UploadedFile;

import uteis.CriptografiaUtils;

@Entity
public class EventoTorneio {

	public EventoTorneio() {
		participantes = new ArrayList<Usuario>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_EventoTorneio", nullable = false)
	private int id_arquivoTorneio;

	@Column(nullable = false)
	private Date data;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String nome_torneio;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String local;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "torneio_participantes")
	@Column(nullable = false)
	private List<Usuario> participantes;

	@Column
	private Integer idFoto;

	@Transient
	private UploadedFile fotoEvento;

	public String getUrlFotoEvento() {
		return "/verArquivo?" + "idArquivo=" + getIdFoto() // id do arquivo
				+ "&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) // chave criptografada para
																							// acesso � imagem
				+ "&salvar=false";
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNome_torneio() {
		return nome_torneio;
	}

	public void setNome_torneio(String nome_torneio) {
		this.nome_torneio = nome_torneio;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public List<Usuario> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Usuario> participantes) {
		this.participantes = participantes;
	}

	public int getId_arquivoTorneio() {
		return id_arquivoTorneio;
	}

	public void setId_arquivoTorneio(int id_arquivoTorneio) {
		this.id_arquivoTorneio = id_arquivoTorneio;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}

	public UploadedFile getFotoEvento() {
		return fotoEvento;
	}

	public void setFotoEvento(UploadedFile fotoEvento) {
		this.fotoEvento = fotoEvento;
	}

}
