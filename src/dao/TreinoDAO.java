package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Treino;

public class TreinoDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Treino> buscarTreino(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT tr FROM Treino tr ";
		hql += " ORDER BY tr.data DESC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<Treino> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
}
