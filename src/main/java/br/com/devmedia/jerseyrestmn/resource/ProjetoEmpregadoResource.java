package br.com.devmedia.jerseyrestmn.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.devmedia.jerseyrestmn.model.domain.Empregado;
import br.com.devmedia.jerseyrestmn.service.ProjetoEmpregadoService;

@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProjetoEmpregadoResource {
	
	private ProjetoEmpregadoService service = new ProjetoEmpregadoService();
	
	@POST
	@Path("/{empregadoId}")
	public Response saveRelationship(@PathParam("projetoId") Long projetoId, @PathParam("empregadoId") Long empregadoId) {
		service.saveRlationship(projetoId, empregadoId);
		return Response.status(Status.CREATED).build();
	}
	
	@GET
	public List<Empregado> getEmpregados(@PathParam("projetoId") Long projetoId){
		return service.getEmpregados(projetoId);
	}
	
}
