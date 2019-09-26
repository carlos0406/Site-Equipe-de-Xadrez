package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.EventoTorneio;

public class EventoTorneioDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<EventoTorneio> buscarEventoTorneio(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT e FROM EventoTorneio e ";
		hql += " ORDER BY e.data DESC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<EventoTorneio> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
}
