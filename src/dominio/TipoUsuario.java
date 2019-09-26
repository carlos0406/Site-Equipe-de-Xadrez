package dominio;

/**
 * Enum (enumera��o) que armazena os poss�veis tipos de usu�rios do sistema.
 * 
 * @author Renan
 */
public enum TipoUsuario {
	
	/* ATENÇÃO: ESSES NOMES NÃO PODEM SER MODIFICADOS. */
	
	/** Usuário comum. */
	COMUM,
	
	BOLSISTA,
	
	MEMBRO,
	
	/** Administrador do sistema */
	ADMINISTRADOR;
	
	/** Obtém uma descrição do tipo de usuário. */
	public String toString() {
		if (this == TipoUsuario.ADMINISTRADOR){
			return "ADMINISTRADOR";
		} else if (this == TipoUsuario.BOLSISTA){
			return "BOLSISTA";
		}else if (this == TipoUsuario.MEMBRO){
			return "MEMBRO";
		}else if (this == TipoUsuario.COMUM){
			return "COMUM";
		}else {
			return "Não identificado";
		}
	}
	
}
