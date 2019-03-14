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

import br.com.devmedia.jerseyrestmn.model.domain.Empregado;
import br.com.devmedia.jerseyrestmn.service.EmpregadoService;

@Path("/empregados")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class EmpregadoResource {
	
	private EmpregadoService service = new EmpregadoService();
	
	@GET
	public List<Empregado> getEmpregados(@BeanParam FilterBean filterBean){
		if(filterBean.getOffset() >= 0 && filterBean.getLimit() > 0) {
			return service.findByPagination(filterBean.getOffset(), filterBean.getLimit());
		}
		
		if(filterBean.getName() != null) {
			return service.findByName(filterBean.getName());
		}
		
		return service.findAll();
	}
	
	@GET
	@Path("/{empregadoId}")
	public Empregado getEmpregado(@PathParam("empregadoId") Long empregadoId) {
		return service.findById(empregadoId);
	}
	
	@POST
	public Response save(Empregado empregado) {
		service.save(empregado);
		return Response.status(Status.CREATED).entity(empregado).build();
	}
	
	@PUT
	@Path("/{empregadoId}")
	public Response update(@PathParam("empregadoId") Long empregadoId, Empregado empregado) {
		empregado.setId(empregadoId);
		service.update(empregado);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{empregadoId}")
	public Response delete(@PathParam("empregadoId") Long empregadoId) {
		service.delete(empregadoId);
		return Response.noContent().build();
	}
	
	@Path("/{empregadoId}/projetos")
	public EmpregadoProjetoResource getEmpregadoProjetoResource() {
		return new EmpregadoProjetoResource();
	}
	
}
