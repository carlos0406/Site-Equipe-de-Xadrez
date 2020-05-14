package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.EventoTorneio;
import uteis.MetodosUteis;

public class EventoTorneioDAO extends DAOGenerico {
	/**
	 * Método que permite listar usuários através da busca por diversos
	 * atributos.
	 */
	public List<EventoTorneio> buscarEventoTorneiosUsuario(int idUsuario) {

		EntityManager em = getEntityManager();
		String hql = "select e from EventoTorneio e " + "join e.participantes p\r\n" + "where p.id = :idUsuario";

		Query q = em.createQuery(hql);
		q.setParameter("idUsuario", idUsuario);
		try {
			@SuppressWarnings("unchecked")
			List<EventoTorneio> result = q.getResultList();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<EventoTorneio> buscarEventoTorneio(String nome, Date dataInicial, Date dataFinal, boolean futuro) {
		EntityManager gerenciador = getEntityManager();
		Date dataAtual;
		String hql = "SELECT e FROM EventoTorneio e WHERE 1=1 ";

		if (!MetodosUteis.estaVazia(nome)) {
			hql += " AND upper(e.nome_torneio) like :nome";
		}
		if (!MetodosUteis.estaVazia(dataInicial) && !MetodosUteis.estaVazia(dataFinal)) {
			hql += " AND e.data BETWEEN (:dataInicial) AND (:dataFinal)";
		}

		if (futuro) {
			hql += " AND e.data>= :dataAtual";
		}

		Query q = gerenciador.createQuery(hql);

		if (dataInicial!= null && dataFinal!=null) {
			q.setParameter("dataInicial", dataInicial);
			q.setParameter("dataFinal", dataFinal);
		}
		if (!MetodosUteis.estaVazia(nome)) {
			q.setParameter("nome", nome.toUpperCase()+"%");
		}
		if (futuro) {
			dataAtual = new Date(System.currentTimeMillis());
			q.setParameter("dataAtual", dataAtual);
		}

		try {
			List<EventoTorneio> resultado = q.getResultList();
			return resultado;
		} catch (NoResultException e) {
			return null;
		}
	}

}
