package fr.datasyscom.scopiom.rest.agent;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/agentsBis")
public class AgentRestResource {

	@EJB
	private AgentResourceLocal ar;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response restAgents(@QueryParam("ping") String pingParam) {
		boolean ping = Boolean.parseBoolean(pingParam);
		return Response.ok(ar.agents(ping)).build();
	}

}