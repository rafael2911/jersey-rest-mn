package br.com.devmedia.jerseyrestmn.service;

import java.util.List;

import br.com.devmedia.jerseyrestmn.model.dao.EmpregadoDao;
import br.com.devmedia.jerseyrestmn.model.domain.Empregado;

public class EmpregadoService {
	
	private EmpregadoDao dao = new EmpregadoDao();
	
	public List<Empregado> findAll(){
		return dao.findAll();
	}
	
	public Empregado findById(Long empregadoId) {
		return dao.findById(empregadoId);
	}
	
	public List<Empregado> findByPaginatio(Integer firstResult, Integer maxResults){
		return dao.findByPagination(firstResult, maxResults);
	}
	
	public List<Empregado> findByName(String name){
		return dao.findByName(name);
	}
	
	public List<Empregado> findByPagination(Integer firstResult, Integer maxResults){
		return dao.findByPagination(firstResult, maxResults);
	}
	
	public Empregado save(Empregado empregado) {
		return dao.save(empregado);
	}
	
	public Empregado update(Empregado empregado) {
		return dao.update(empregado);
	}
	
	public Empregado delete(Long empregadoId) {
		return dao.delete(empregadoId);
	}
	
}
