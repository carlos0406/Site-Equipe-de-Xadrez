package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Livro;
import uteis.ValidadorUtil;

public class LivroDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Livro> buscarLivro(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT l FROM Livro l ";
		hql += " ORDER BY l.nome_livro ASC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<Livro> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
}
