package br.com.devmedia.jerseyrestmn.service;

import java.util.List;

import br.com.devmedia.jerseyrestmn.model.dao.ProjetoEmpregadoDao;
import br.com.devmedia.jerseyrestmn.model.domain.Empregado;
import br.com.devmedia.jerseyrestmn.model.domain.Projeto;

public class ProjetoEmpregadoService {
	
	private ProjetoEmpregadoDao dao = new ProjetoEmpregadoDao();
	
	public void saveRlationship(Long projetoId, Long empregadoId) {
		dao.saveRelationship(projetoId, empregadoId);
	}
	
	public List<Empregado> getEmpregados(Long projetoId){
		return dao.getEmpregados(projetoId);
	}
	
	public List<Projeto> getProjetos(Long empregadoId){
		return dao.getProjetos(empregadoId);
	}
	
	public void deleteRelationship(Long projetoId, Long empregadoId) {
		dao.deleteRelationship(projetoId, empregadoId);
	}
	
}
