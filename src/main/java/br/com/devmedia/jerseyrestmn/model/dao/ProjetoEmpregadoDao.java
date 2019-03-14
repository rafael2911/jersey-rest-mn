package br.com.devmedia.jerseyrestmn.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.devmedia.jerseyrestmn.exceptions.DaoException;
import br.com.devmedia.jerseyrestmn.exceptions.ErrorCode;
import br.com.devmedia.jerseyrestmn.model.domain.Empregado;
import br.com.devmedia.jerseyrestmn.model.domain.Projeto;

public class ProjetoEmpregadoDao {
	
	public void saveRelationship(Long projetoId, Long empregadoId) {
		
		if((projetoId <= 0) || (empregadoId <= 0)) {
			throw new DaoException("Os ids de projeto e empregado devem ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		if(existRelationship(projetoId, empregadoId, em)) {
			throw  new DaoException("Relacionamento solicitado para criação já existe!", ErrorCode.CONFLICT);
		}
		
		try {
			Projeto projeto = em.find(Projeto.class, projetoId);
			Empregado empregado = em.find(Empregado.class, empregadoId);
			
			if(empregado == null) {
				throw new NullPointerException();
			}
			
			em.getTransaction().begin();
			projeto.getEmpregados().add(empregado);
			em.persist(projeto);
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Id do projeto ou empregado não existe!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar relacionamento entre projeto e empregado: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
	}
	
	public List<Empregado> getEmpregados(Long projetoId){
		
		if(projetoId <= 0) {
			throw new DaoException("O id do projeto deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			return em.createQuery("select e from Empregado e join e.projetos p where p.id = :projetoId", Empregado.class)
					.setParameter("projetoId", projetoId)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao recuperar os empregados do projeto de id "
					+ projetoId + " do banco: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
	}
	
	public List<Projeto> getProjetos(Long empregadoId){
		
		if(empregadoId <= 0) {
			throw new DaoException("O id do empregado deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			
			return em.createQuery("select p from Projeto p join p.empregados e where e.id = :empregadoId", Projeto.class)
					.setParameter("empregadoId", empregadoId)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao recuperar os projetos do empregado de id "
					+ empregadoId + " do banco: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
	}
	
	public void deleteRelationship(Long projetoId, Long empregadoId) {
		
		if((projetoId <= 0) || (empregadoId <= 0)) {
			throw new DaoException("Os ids do projeto e empregado devem ser maior do que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		if(!existRelationship(projetoId, empregadoId, em)) {
			throw new DaoException("Relacionamento informado informado para exclusão não existe!", ErrorCode.NOT_FOUND);
		}
		
		try {
			Projeto projeto = em.find(Projeto.class, projetoId);
			Empregado empregado = em.find(Empregado.class, empregadoId);
			
			em.getTransaction().begin();
			projeto.getEmpregados().remove(empregado);
			em.persist(projeto);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao remover relacionamento entre projeto e empregado no banco de dados: " 
			           + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
	}
	
	public boolean existRelationship(Long projetoId, Long empregadoId, EntityManager em) {
		try {
			em.createQuery("select e from Empregado e join e.projetos p where p.id = :projetoId and e.id = :empregadoId", Empregado.class)
			.setParameter("projetoId", projetoId)
			.setParameter("empregadoId", empregadoId)
			.getSingleResult();
		}catch (NoResultException ex) {
			return false;
		}
		
		return true;
	}
	
}
