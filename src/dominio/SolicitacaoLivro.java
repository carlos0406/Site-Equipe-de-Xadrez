package dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SolicitacaoLivro {

	@Id
	@GeneratedValue  (strategy=GenerationType.IDENTITY)  
	@Column(name="id_solicitacao_livro", nullable = false)
	private int id;
	
	@ManyToOne(optional = false)
	private Usuario usuarioSolicitante;
	
	@ManyToOne(optional = false)
	private Livro livroSolicitado;
	
	@Column(nullable=false)
	private Date dataPrazo;
	
	@Column(nullable=false)
	private Date dataSolicitacao;
	
	private Boolean aprovado;
	
	private boolean devolvido = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public Livro getLivroSolicitado() {
		return livroSolicitado;
	}

	public void setLivroSolicitado(Livro livroSolicitado) {
		this.livroSolicitado = livroSolicitado;
	}

	public Date getDataPrazo() {
		return dataPrazo;
	}

	public void setDataPrazo(Date dataPrazo) {
		this.dataPrazo = dataPrazo;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Boolean getAprovado() {
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}

	public boolean isDevolvido() {
		return devolvido;
	}

	public void setDevolvido(boolean devolvido) {
		this.devolvido = devolvido;
	}
	
}
