package fr.datasyscom.scopiom.rest.workflow;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.datasyscom.pome.ejbentity.filter.WorkflowFilter;

@Stateless
@Path("/workflowsBis")
public class WorkflowRestResource {

	@Inject
	private Principal loggedUser;

	@EJB
	private WorkflowResourceLocal wfr;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response restWorkflows(@QueryParam("name") List<String> wfNames) {
		if (wfNames == null || wfNames.isEmpty()) {
			return Response.ok(wfr.workflows(WorkflowFilter.all().byUser(loggedUser.getName()))).build();
		}
		return Response.ok(wfr.workflows(WorkflowFilter.all().byUser(loggedUser.getName()).byNamesLike(wfNames))).build();
	}

	@GET
	@Path("/{name}/ui")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response restWorkflowUi(@PathParam("name") String name) {
		return Response.ok(wfr.workflowUi(name)).build();
	}

}