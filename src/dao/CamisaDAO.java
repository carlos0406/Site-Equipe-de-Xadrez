package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Camisa;
import uteis.ValidadorUtil;

public class CamisaDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Camisa> buscarCamisa(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT c FROM Camisa c ";
		hql += " ORDER BY c.ano_camisa ASC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<Camisa> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
}
