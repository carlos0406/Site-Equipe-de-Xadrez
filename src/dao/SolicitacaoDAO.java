package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import dominio.Camisa;
import dominio.SolicitacaoLivro;
import uteis.ValidadorUtil;

public class SolicitacaoDAO extends DAOGenerico{
	public List<SolicitacaoLivro> buscarSolicitacao(String livroSolicitado, String nomeUsuario, Date dataInicioSolicitacao,
			Date dataFimSolicitacao){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT s FROM SolicitacaoLivro s WHERE 1=1 ";

		if (ValidadorUtil.isNotEmpty(livroSolicitado)){
			hql += " AND upper(s.livroSolicitado.nome_livro) like :livroSolicitado ";
		}
		if (ValidadorUtil.isNotEmpty(nomeUsuario)){
			hql += " AND upper(s.usuarioSolicitante.nome) like :usuarioSolicitante ";
		}
		if (ValidadorUtil.isNotEmpty(dataInicioSolicitacao) && ValidadorUtil.isNotEmpty(dataFimSolicitacao)){
			hql += " AND s.dataPrazo BETWEEN :dataInicio AND :dataFim ";
		}
		
		hql += " ORDER BY s.livroSolicitado.nome_livro ASC ";
		
		Query q = em.createQuery(hql);
		
		if (ValidadorUtil.isNotEmpty(livroSolicitado)){
			q.setParameter("livroSolicitado", "%" + livroSolicitado.toUpperCase() + "%");
		}
		if (ValidadorUtil.isNotEmpty(nomeUsuario)){
			q.setParameter("usuarioSolicitante", "%" + nomeUsuario.toUpperCase() + "%");
		}
		if (ValidadorUtil.isNotEmpty(dataInicioSolicitacao) && ValidadorUtil.isNotEmpty(dataFimSolicitacao)){
			q.setParameter("dataInicio", dataInicioSolicitacao);
			q.setParameter("dataFim", dataFimSolicitacao);
		}
		
		try {
			@SuppressWarnings("unchecked")
			List<SolicitacaoLivro> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
	public List<SolicitacaoLivro> buscarSolicitacaoMembro(int idUsuario){
		EntityManager em = getEntityManager();
		
		String hql = "SELECT s FROM SolicitacaoLivro s ";
		hql += "WHERE s.usuarioSolicitante.id = :idUsuario ";
		hql += " ORDER BY s.livroSolicitado.nome_livro ASC ";
		
		Query q = em.createQuery(hql);
		q.setParameter("idUsuario", idUsuario);
		
		try {
			@SuppressWarnings("unchecked")
			List<SolicitacaoLivro> result = q.getResultList();
			
			return result;
		} catch (NoResultException e){
			return null;
		}
	}
}
