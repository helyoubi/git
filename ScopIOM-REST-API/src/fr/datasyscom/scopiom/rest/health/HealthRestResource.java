package fr.datasyscom.scopiom.rest.health;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/health")
public class HealthRestResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response health(@QueryParam("physicalMemory") String physicalMemory,
			@QueryParam("vmMemory") String vmMemory,
			@QueryParam("cpu") String cpu, @QueryParam("fs") String fs) {

		boolean includePhysicalMemory = shouldInclude(physicalMemory);
		boolean includeVmMemory = shouldInclude(vmMemory);
		boolean includeCpu = shouldInclude(cpu);
		boolean includeFs = shouldInclude(fs);

		return Response.ok(
				new HealthInfos(includePhysicalMemory, includeVmMemory,
						includeCpu, includeFs)).build();
	}

	private boolean shouldInclude(String param) {
		boolean include = true;
		if (param != null) {
			include = Boolean.parseBoolean(param);
		}
		return include;
	}

}