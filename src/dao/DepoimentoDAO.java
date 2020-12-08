package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Depoimento;

public class DepoimentoDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Depoimento> buscarDepoimento(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT d FROM Depoimento d ";
		hql += " ORDER BY d.dataCadastro DESC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<Depoimento> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	
}

