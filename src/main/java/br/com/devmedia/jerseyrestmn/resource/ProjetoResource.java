package br.com.devmedia.jerseyrestmn.resource;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.devmedia.jerseyrestmn.model.domain.Projeto;
import br.com.devmedia.jerseyrestmn.service.ProjetoService;

@Path("/projetos")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProjetoResource {
	
	private ProjetoService service = new ProjetoService();
	
	@GET
	public List<Projeto> getProjetos(@BeanParam FilterBean filterBean){
		
		if(filterBean.getOffset() >= 0 && filterBean.getLimit() > 0) {
			return service.findByPagination(filterBean.getOffset(), filterBean.getLimit());
		}
		
		if(filterBean.getName() != null) {
			return service.findByName(filterBean.getName());
		}
		
		return service.findAll();
	}
	
	@GET
	@Path("/{projetoId}")
	public Projeto getProjeto(@PathParam("projetoId") Long projetoId) {
		return service.findById(projetoId);
	}
	
	@POST
	public Response save(Projeto projeto) {
		service.save(projeto);
		return Response.status(Status.CREATED)
				.entity(projeto)
				.build();
	}
	
	@PUT
	@Path("/{projetoId}")
	public Response update(@PathParam("projetoId") Long projetoId, Projeto projeto) {
		projeto.setId(projetoId);
		service.update(projeto);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{projetoId}")
	public Response delete(@PathParam("projetoId") Long projetoId) {
		service.delete(projetoId);
		return Response.noContent().build();
	}
	
	@Path("/{projetoId}/empregados")
	public ProjetoEmpregadoResource getProjetoEmpregadoResource() {
		return new ProjetoEmpregadoResource();
	}
	
}
