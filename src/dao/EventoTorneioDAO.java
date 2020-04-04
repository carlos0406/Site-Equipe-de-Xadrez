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
	
	public List<EventoTorneio> buscarEventoTorneiosUsuario(int idUsuario){
		
		EntityManager em = getEntityManager();
		String hql="select e from EventoTorneio e " + 
				"join e.participantes p\r\n" + 
				"where p.id = :idUsuario";
		
		Query q=em.createQuery(hql);
		q.setParameter("idUsuario", idUsuario);
		try {
			@SuppressWarnings("unchecked")
			List<EventoTorneio> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
		
		
	}
	
	
	

