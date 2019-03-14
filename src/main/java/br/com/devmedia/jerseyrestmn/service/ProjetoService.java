package br.com.devmedia.jerseyrestmn.service;

import java.util.List;

import br.com.devmedia.jerseyrestmn.model.dao.ProjetoDao;
import br.com.devmedia.jerseyrestmn.model.domain.Projeto;

public class ProjetoService {
	
	private ProjetoDao dao = new ProjetoDao();
	
	public List<Projeto> findAll(){
		return dao.findAll();
	}
	
	public Projeto findById(Long projetoId) {
		return dao.findById(projetoId);
	}
	
	public List<Projeto> findByPagination(Integer firstResult, Integer maxResults){
		return dao.findByPagination(firstResult, maxResults);
	}
	
	public List<Projeto> findByName(String name){
		return dao.findByName(name);
	}
	
	public Projeto save(Projeto projeto) {
		return dao.save(projeto);
	}
	
	public Projeto update(Projeto projeto) {
		return dao.update(projeto);
	}
	
	public Projeto delete(Long projetoId) {
		return dao.delete(projetoId);
	}
	
}
