package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Noticia;

public class NoticiaDAO extends DAOGenerico{
	/**
	 * Método que permite listar usuários através da busca por
	 * diversos atributos.
	 */
	public List<Noticia> buscarNoticia(){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT n FROM Noticia n ";
		hql += " ORDER BY n.dataCadastro DESC ";
		
		Query q = em.createQuery(hql);
		
		try {
			@SuppressWarnings("unchecked")
			List<Noticia> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	
}

