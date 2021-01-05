package dominio;

import java.util.Date;
import java.util.List;

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
public class Usuario {
	
	@Id
	@GeneratedValue  (strategy=GenerationType.IDENTITY)  
	@Column(name="id_usuario", nullable = false)
	private int id;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String nome;
	
	@Column(nullable = false)
	private boolean ativo = true;
	
	@Column(nullable=true)
	private boolean destaque = false;
	
	@Column(nullable=true)
	private String textoDestaque;
	
	@Column(nullable = false)
	private char sexo;
	
	@Column(nullable = false)
	private Date dataNascimento;
	
	/** Tipo do usu�rio */
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private TipoUsuario tipoUsuario;
	
	@Column(nullable = false)
	private String rg;
	
	@Column(nullable = false)
	private String cpf;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String celular;
	
	@Column(nullable = false)
	private String senha;
	
	@Transient
	private String confirmarSenha;
	
	private long matricula;
	
	@Column(name="id_foto")
	private Integer idFoto;
	
	@Column(nullable=false)
	private int rating;
	
	/** 
	 * Atributo n�o persisitido que armazena uma foto que o usu�rio deseja
	 * para seu perfil.
	 * */
	@Transient
	private UploadedFile foto;
	
	/** indica se o cadastro foi aprovado por quaranta ou por um bolsista */
	//private boolean aprovado = false;
	
	@Column(nullable=true)
	private Integer op;
	
	@Column(nullable=true)
	private Integer ag;
	
	@Column(nullable=true)
	private Integer conta;
	
	@Column(nullable=true)
	private String banco;
	
	@Transient
	private List<EventoTorneio> torneios;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	/** Obt�m a URL atrav�s da qual a foto do usu�rio pode ser carregada. */
	public String getUrlFotoUsuario(){
		return "/verArquivo?"
				+ "idArquivo=" + getIdFoto() //id do arquivo
				+"&key=" + CriptografiaUtils.criptografarMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso � imagem 
				+ "&salvar=false"; 
	}
	
	public boolean isAdministrador(){
		if (tipoUsuario.equals(TipoUsuario.ADMINISTRADOR)){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isMembro(){
		if (tipoUsuario.equals(TipoUsuario.MEMBRO)){
			return true;
		} else {
			return false;
		}
	}
	
	
	public String getDescricaoTipoUsuario(){
		return tipoUsuario.toString();
	}
	
	public char getSexo() {
		return sexo;
	}
	public void setSexo(char sexo) {
		this.sexo = sexo;
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
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public long getMatricula() {
		return matricula;
	}

	public void setMatricula(long matricula) {
		this.matricula = matricula;
	}

	public Integer getOp() {
		return op;
	}

	public void setOp(Integer op) {
		this.op = op;
	}

	public Integer getAg() {
		return ag;
	}

	public void setAg(Integer ag) {
		this.ag = ag;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}

	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getIdFoto() {
		return idFoto;
	}

	public void setIdFoto(Integer idFoto) {
		this.idFoto = idFoto;
	}

	public UploadedFile getFoto() {
		return foto;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public void setFoto(UploadedFile foto) {
		this.foto = foto;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean tipoUsuario) {
		this.ativo = tipoUsuario;
	}
	
	/** Identifica se o sexo da pessoa em quest�o � masculino ou n�o */
	public boolean isSexoMasculino(){
		if (ValidadorUtil.isNotEmpty(sexo) && sexo == 'M'){
			return true;
		} 
		return false;
	}
	
	/** Identifica se o sexo da pessoa em quest�o � feminino ou n�o*/
	public boolean isSexoFeminino(){
		if (ValidadorUtil.isNotEmpty(sexo) && sexo == 'F'){
			return true;
		} 
		return false;
	}
	
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public List<EventoTorneio> getTorneios() {
		return torneios;
	}

	public void setTorneios(List<EventoTorneio> torneios) {
		this.torneios = torneios;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isDestaque() {
		return destaque;
	}

	public void setDestaque(boolean destaque) {
		this.destaque = destaque;
	}

	public String getTextoDestaque() {
		return textoDestaque;
	}

	public void setTextoDestaque(String textoDestaque) {
		this.textoDestaque = textoDestaque;
	}
	
	
}
