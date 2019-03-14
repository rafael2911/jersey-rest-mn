package br.com.devmedia.jerseyrestmn.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrestmn.exceptions.DaoException;
import br.com.devmedia.jerseyrestmn.exceptions.ErrorCode;
import br.com.devmedia.jerseyrestmn.model.domain.Projeto;

public class ProjetoDao {
	
	public List<Projeto> findAll(){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Projeto> projetos;
		
		try {
			
			projetos = em.createQuery("from Projeto p", Projeto.class).getResultList();
			
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar projetos: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return projetos;
	}
	
	public Projeto findById(Long projetoId) {
		
		if(projetoId <= 0) {
			throw new DaoException("O id deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Projeto projeto;
		
		try {
			
			projeto = em.find(Projeto.class, projetoId);
			
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar projeto por id: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}
		
		return projeto;
	}
	
	public List<Projeto> findByPagination(Integer firstResult, Integer maxResults){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Projeto> projetos;
		
		try {
			
			projetos = em.createQuery("from Projeto p", Projeto.class)
					.setFirstResult(firstResult-1)
					.setMaxResults(maxResults)
					.getResultList();
			
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar projetos com paginação: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(projetos.isEmpty()) {
			throw new DaoException("A lista de projetos com paginação está vazia!", ErrorCode.NOT_FOUND);
		}
		
		return projetos;
	}
	
	public List<Projeto> findByName(String name){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Projeto> projetos;
		
		try {
			projetos = em.createQuery("FROM Projeto p WHERE p.nome LIKE :name", Projeto.class)
					.setParameter("name", "%" + name + "%")
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar projetos por nome: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(projetos.isEmpty()) {
			throw new DaoException("A lista de projetos com paginação está vazia!", ErrorCode.NOT_FOUND);
		}
		
		return projetos;
	}
	
	public Projeto save(Projeto projeto) {
		
		if(!projetoIsValid(projeto)) {
			throw new DaoException("Dados do projeto incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(projeto);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar projeto no BD: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return projeto;
	}
	
	public Projeto update(Projeto projeto) {
		
		if(projeto.getId() <= 0) {
			throw new DaoException("O id do projeto deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(!projetoIsValid(projeto)) {
			throw new DaoException("Dados do projeto incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Projeto projetoManaged;
		
		try {
			em.getTransaction().begin();
			projetoManaged = em.find(Projeto.class, projeto.getId());
			projetoManaged.setNome(projeto.getNome());
			projetoManaged.setDescricao(projeto.getDescricao());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Projeto não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar projeto: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return projetoManaged;
	}
	
	public Projeto delete(Long projetoId) {
		
		if(projetoId <= 0) {
			throw new DaoException("O id do projeto deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Projeto projeto;
		
		try {
			em.getTransaction().begin();
			projeto = em.find(Projeto.class, projetoId);
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Projeto não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao excluir projeto: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return projeto;
	}
	
	private boolean projetoIsValid(Projeto projeto) {
		
		try {
			if(projeto.getNome().isEmpty() || projeto.getDescricao().isEmpty()) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Dados do projeto incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		return true;
		
	}
	
}
