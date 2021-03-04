package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.RodadaTorneio;
import dominio.TipoUsuario;
import dominio.Usuario;
import uteis.MetodosUteis;

public class RodadaTorneioDAO extends DAOGenerico {
	public List<RodadaTorneio> buscarRodadas(RodadaTorneio rodada) {
		
		EntityManager gerenciador = getEntityManager();
		String hql="";
		if (!MetodosUteis.estaVazia(rodada.getMembro())&&!MetodosUteis.estaVazia(rodada.getTorneio())){
			 hql = "select r FROM RodadaTorneio r"+ " join r.membro m " + 
						" join r.torneio t "+ "where t.id_EventoTorneio = :idTorneio "+ "and m.id = :idUsuario ";
		}
		else if (!MetodosUteis.estaVazia(rodada.getMembro())) {
			 hql = "select r FROM RodadaTorneio r"+ " join r.membro m " + "where m.id = :idUsuario ";
		}else if (!MetodosUteis.estaVazia(rodada.getTorneio())) {
			 hql = "select r FROM RodadaTorneio r"+ " join r.torneio t " + "where t.id_EventoTorneio = :idTorneio ";
		}else {
			 hql = "select r FROM RodadaTorneio r"+" where 1=1";
		}
		if (!MetodosUteis.estaVazia(rodada.getNomeAdversario())) {
			 hql+="and r.nomeAdversario like:nomeAdversario";
		}
		
		if (!MetodosUteis.estaVazia(rodada.getRodada())) {
			 hql+="and r.rodada like:rodada";
		}
		
		
		
		

		

		// set parametes query

		Query q = gerenciador.createQuery(hql);

		

		if (!MetodosUteis.estaVazia(rodada.getMembro())) {
		q.setParameter("idUsuario", rodada.getMembro().getId());
		}
		if (!MetodosUteis.estaVazia(rodada.getTorneio())) {
		q.setParameter("idTorneio", rodada.getTorneio().getId_EventoTorneio());
		}
		if (!MetodosUteis.estaVazia(rodada.getRodada())) {
			q.setParameter("rodada", rodada.getRodada());
		}
		if (!MetodosUteis.estaVazia(rodada.getNomeAdversario())) {
			q.setParameter("nomeAdversario", rodada.getNomeAdversario());
		}
		
		
		//if (!MetodosUteis.estaVazia(rodada.getTorneio())) {
		//	q.setParameter("torneioId",rodada.getTorneio().getId_EventoTorneio());
		//}

		
		
		
		
		
		

		try {
			List<RodadaTorneio> resultado = q.getResultList();
			return resultado;
		} catch (NoResultException e) {
			return null;
		}
	}

}
