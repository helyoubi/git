package fr.datasyscom.scopiom.rest.agent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Agent;
import fr.datasyscom.pome.ejbentity.Agent.AgentStatusType;
import fr.datasyscom.pome.ejbentity.filter.AgentFilter;
import fr.datasyscom.pome.ejbsession.agent.AgentManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.AgentDto;

@Path("/agents")
public class AgentRestWS {

	@EJB
	AgentManagerLocal agentLocal;

	@EJB
	GroupeManagerLocal grpLocal;

	@Context
	UriInfo uriInfo;

	private AgentFilter filterAgent = AgentFilter.all();

	/**
	 * 
	 * Récupérer un agent par son adresse
	 * 
	 * @param agentAdresse
	 * @return ok
	 */
	@GET
	@Path("/{agentAdress}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAgent(@PathParam("agentAdress") String agentAdress) {
		AgentDto agentDto = null;
		try {
			Agent agent = agentLocal.retrieveAgent(filterAgent.byAdresse(agentAdress));
			agentDto = new AgentDto(agent);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(agentDto).build();
	}

	/**
	 * 
	 * Renvoie la liste des agents ou l'agent par son id
	 * 
	 * @param idAgent
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllAgents(@QueryParam("id") long idAgent) {
		List<AgentDto> agentsListDto = new ArrayList<AgentDto>();
		if (idAgent != 0) {
			try {
				agentsListDto.add(new AgentDto(agentLocal.retrieveAgent(idAgent)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Agent> allAgent = agentLocal.retrieveAllAgent();
			AgentDto agentDto = null;
			for (Agent agent : allAgent) {
				agentDto = new AgentDto(agent);
				agentsListDto.add(agentDto);
			}
		}

		GenericEntity<List<AgentDto>> listRestAgents = new GenericEntity<List<AgentDto>>(agentsListDto) {
		};
		return Response.ok(listRestAgents).build();
	}

	/**
	 * 
	 * Supprime un agent par son adresse
	 * 
	 * @param agentAdresse
	 * @return noContent
	 */
	@DELETE
	@Path("/{agentAdress}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteAgent(@PathParam("agentAdress") String agentAdress) {
		try {
			Agent agent = agentLocal.retrieveAgent(filterAgent.byAdresse(agentAdress));
			agentLocal.deleteAgent(agent.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * création d'un agent
	 * 
	 * @param agentDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createAgent(AgentDto agentDto) {
		Agent agent;
		if (agentDto != null) {
			try {
				agent = new Agent();
				if (agentDto.getAdresse() != null) {
					agent.setAdresse(agentDto.getAdresse());
				}
				if (agentDto.getDescription() != null) {
					agent.setDescription(agentDto.getDescription());
				}
				if (agentDto.getPort() != null) {
					agent.setPort(agentDto.getPort());
				}
				agent = agentLocal.createAgent(agent);
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}

		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newAgentAddress = String.valueOf(agent.getAdresse());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newAgentAddress).build();

		return Response.created(uri).build();
	}

	/**
	 * 
	 * Mise à jour de l'agent
	 * 
	 * @param agentAdress
	 * @param agentDto
	 * @return noContent
	 */
	@PUT
	@Path("/{agentAdress}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAgent(@PathParam("agentAdress") String agentAdress, AgentDto agentDto) {
		if (agentDto != null) {
			try {
				Agent agent = agentLocal.retrieveAgent(filterAgent.byAdresse(agentAdress));
				if (agentDto.getAdresse() != null) {
					agent.setAdresse(agentDto.getAdresse());
				}
				if (agentDto.getDescription() != null) {
					agent.setDescription(agentDto.getDescription());
				}
				if (agentDto.getPort() != null) {
					agent.setPort(agentDto.getPort());
				}
				agentLocal.updateAgent(agent);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Mise à jour du statut de l'agent
	 * 
	 * @param agentAdress
	 * @param agentStatus
	 * @return noContent
	 */
	@PUT
	@Path("/{agentAdress}/status/{agentStatus}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAgentStatus(@PathParam("agentAdress") String agentAdress,
			@PathParam("agentStatus") String agentStatus) {
		try {
			Agent agent = agentLocal.retrieveAgent(filterAgent.byAdresse(agentAdress));
			agentLocal.setStatus(agent.getId(), AgentStatusType.valueOf(agentStatus.toUpperCase()));
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

}
