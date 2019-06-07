package fr.datasyscom.scopiom.rest.appinfos;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.datasyscom.pome.ejbsession.server.info.ServerInfoManagerLocal;

@Stateless
@Path("/infos")
public class AppInfosRestResource {

	@EJB
	private ServerInfoManagerLocal si;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response infos() {
		return Response.ok(new AppInfos(si)).build();
	}

}
