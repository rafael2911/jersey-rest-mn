package br.com.devmedia.jerseyrestmn.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrestmn.exceptions.DaoException;
import br.com.devmedia.jerseyrestmn.exceptions.ErrorCode;
import br.com.devmedia.jerseyrestmn.model.domain.Empregado;

public class EmpregadoDao {
	
	public List<Empregado> findAll(){
		EntityManager em = JpaUtil.getEntityManager();
		List<Empregado> empregados;
		
		try {
			empregados = em.createQuery("from Empregado e", Empregado.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar empregados: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregados;
	}
	
	public Empregado findById(Long empregadoId) {
		
		if(empregadoId <= 0) {
			throw new DaoException("O id do empregado deve ser maior que zero", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Empregado empregado;
		
		try {
			empregado = em.find(Empregado.class, empregadoId);
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao localizar empregado por id: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregado;
		
	}
	
	public List<Empregado> findByPagination(Integer firstResult, Integer maxResults){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Empregado> empregados;
		
		try {
			empregados = em.createQuery("from Empregado e", Empregado.class)
					.setFirstResult(firstResult-1)
					.setMaxResults(maxResults)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar empregados por paginação!", ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(empregados.isEmpty()) {
			throw new DaoException("A lista de empregados com paginação está vazia!", ErrorCode.NOT_FOUND);
		}
		
		return empregados;
		
	}
	
	public List<Empregado> findByName(String name){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Empregado> empregados;
		
		try {
			empregados = em.createQuery("from Empregado e where e.nome like :name", Empregado.class)
					.setParameter("name", "%" + name + "%")
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar empregados nome!", ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregados;
	}
	
	public Empregado save(Empregado empregado) {
		if(!empregadoIsValid(empregado)) {
			throw new DaoException("Dados do empregado incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(empregado);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar empregado: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregado;
	}
	
	public Empregado update(Empregado empregado) {
		if(empregado.getId() <= 0) {
			throw new DaoException("O id do empregado deve ser maior que zero", ErrorCode.BAD_REQUEST);
		}
		
		if(!empregadoIsValid(empregado)) {
			throw new DaoException("Dados do empregado incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Empregado empregadoManaged;
		
		try {
			em.getTransaction().begin();
			empregadoManaged = em.find(Empregado.class, empregado.getId());
			empregadoManaged.setNome(empregado.getNome());
			empregadoManaged.setCargo(empregado.getCargo());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Empregado não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar empregado: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregadoManaged;
		
	}
	
	public Empregado delete(Long empregadoId) {
		
		if(empregadoId <= 0) {
			throw new DaoException("O id do empregado deve ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Empregado empregado;
		
		try {
			em.getTransaction().begin();
			empregado = em.find(Empregado.class, empregadoId);
			em.remove(empregado);
			em.getTransaction().commit();
		}catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Empregado para exclusão não localizado!", ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao excluir empregado: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return empregado;
	}
	
	private boolean empregadoIsValid(Empregado empregado) {
		try {
			if(empregado.getCargo().isEmpty() || empregado.getNome().isEmpty()) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Dados do empregado incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		return true;
	}
	
}
