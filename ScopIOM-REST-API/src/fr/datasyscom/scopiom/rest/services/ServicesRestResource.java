package fr.datasyscom.scopiom.rest.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.datasyscom.pome.ejbsession.lpd.LpdManagerLocal;
import fr.datasyscom.pome.ejbsession.services.purge.jobs.PurgeLocal;
import fr.datasyscom.pome.ejbsession.services.purge.tmp.PurgeTmpLocal;
import fr.datasyscom.pome.ejbsession.services.scanwait.ScanWaitLocal;

@Stateless
@Path("/servicesBis")
public class ServicesRestResource {

	@EJB
	private ScanWaitLocal scanWait;
	@EJB
	public PurgeLocal purgeJobs;
	@EJB
	public PurgeTmpLocal purgeTmp;
	@EJB
	private LpdManagerLocal lpd;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response infos() {
		return Response.ok(new ServicesInfos(scanWait, purgeJobs, purgeTmp, lpd))
				.build();
	}

}
