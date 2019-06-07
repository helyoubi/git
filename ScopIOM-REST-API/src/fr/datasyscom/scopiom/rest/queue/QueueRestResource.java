package fr.datasyscom.scopiom.rest.queue;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.datasyscom.pome.ejbentity.filter.QueueFilter;

@Stateless
@Path("/queuesBis")
public class QueueRestResource {

	@Inject
	private Principal loggedUser;

	@EJB
	private QueueResourceLocal qr;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response restQueues(@QueryParam("name") List<String> queueNames) {
		if (queueNames == null || queueNames.isEmpty()) {
			return Response.ok(qr.queues(QueueFilter.queue().byUser(loggedUser.getName()))).build();
		}
		return Response.ok(qr.queues(QueueFilter.queue().byUser(loggedUser.getName()).byNamesLike(queueNames))).build();
	}

}