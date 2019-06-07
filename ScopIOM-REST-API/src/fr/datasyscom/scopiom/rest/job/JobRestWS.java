package fr.datasyscom.scopiom.rest.job;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.JobProperty;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.pome.ejbsession.job.property.JobPropertyManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.JobPropertyDto;
import fr.datasyscom.scopiom.rest.pojo.JobStatusDto;

@Path("/jobs")
public class JobRestWS {

	@EJB
	JobManagerLocal jobManagerLocal;
	@EJB
	JobPropertyManagerLocal jobPropertyLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * Récupére le job par id
	 * 
	 * @param idJob
	 * @return OK
	 */
	@GET
	@Path("/{idJob}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveJob(@PathParam("idJob") long idJob) {
		JobDto jobtdto = null;
		try {
			jobtdto = new JobDto(jobManagerLocal.retrieveJob(idJob));
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(jobtdto).build();
	}

	/**
	 * 
	 * Supprime le job
	 * 
	 * @param idJob
	 * @return noContent
	 */
	@DELETE
	@Path("/{idJob}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteJob(@PathParam("idJob") long idJob) {
		try {
			jobManagerLocal.deleteJob(idJob);
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * renvoie la liste des propriétées d'un job
	 * 
	 * 
	 * @param idJob
	 * @return ok
	 */
	@GET
	@Path("/{idJob}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllJobProperties(@PathParam("idJob") long idJob) {
		List<JobPropertyDto> jobPropertyDtos = new ArrayList<JobPropertyDto>();
		try {
			Job job = jobManagerLocal.retrieveJob(idJob);
			List<JobProperty> listJobsProperty = jobPropertyLocal.retrieveAllJobProperty(job.getId());
			for (JobProperty jobProperty : listJobsProperty) {
				jobPropertyDtos.add(new JobPropertyDto(jobProperty));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<JobPropertyDto>> jobListsJob = new GenericEntity<List<JobPropertyDto>>(jobPropertyDtos) {
		};
		return Response.ok(jobListsJob).build();
	}

	/**
	 * renvoie la propriété d'un job
	 * 
	 * 
	 * @param idJob
	 * @param nameProperty
	 * @return ok
	 */
	@GET
	@Path("/{idJob}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveJobProperty(@PathParam("idJob") long idJob,
			@PathParam("nameProperty") String nameProperty) {
		JobPropertyDto jobPropertyDto = null;
		try {
			Job job = jobManagerLocal.retrieveJob(idJob);
			JobProperty jobProp = jobPropertyLocal.retrieveJobProperty(job.getId(), nameProperty);
			jobPropertyDto = new JobPropertyDto(jobProp);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(jobPropertyDto).build();
	}

	/**
	 * Supprime la propriété d'un Job
	 * 
	 * 
	 * @param idJob
	 * @param nameProperty
	 * @return noContent
	 */
	@DELETE
	@Path("/{idJob}/property/{nameproperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteJobProperty(@PathParam("idJob") long idJob, @PathParam("nameproperty") String nameProperty) {
		try {
			if (nameProperty != null) {
				Job job = jobManagerLocal.retrieveJob(idJob);
				JobProperty JobProp = jobPropertyLocal.retrieveJobProperty(job.getId(), nameProperty);
				jobPropertyLocal.deleteJobProperty(JobProp.getId());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Création d'une propriété du job
	 * 
	 * 
	 * @param idJob
	 * @param jobPropertyDto
	 * @return created
	 */
	@POST
	@Path("/{idJob}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createJobProperty(@PathParam("idJob") long idJob, JobPropertyDto jobPropertyDto) {
		JobProperty jobProperty = null;
		if (jobPropertyDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			jobProperty = new JobProperty();
			if (jobPropertyDto.getName() != null && !jobPropertyDto.getName().isEmpty()) {
				jobProperty.setName(jobPropertyDto.getName());
			}
			if (jobPropertyDto.getTxt() != null) {
				jobProperty.setValue(jobPropertyDto.getTxt());
			}
			if (jobPropertyDto.isPersist() != null) {
				jobProperty.setPersist(jobPropertyDto.isPersist());
			}
			Job job = jobManagerLocal.retrieveJob(idJob);
			jobProperty = jobPropertyLocal.createJobProperty(jobProperty, job.getId());
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		String newNameProperyJob = String.valueOf(jobProperty.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameProperyJob).build();

		return Response.created(uri).build();
	}

	/**
	 * Ajout du device au job
	 * 
	 * @param idJob
	 * @param deviceId
	 * @return noContent
	 */
	@PUT
	@Path("/{idJob}/device/{deviceId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response setDevice(@PathParam("idJob") long idJob, @PathParam("deviceId") Long deviceId) {
		try {
			jobManagerLocal.setDevice(idJob, deviceId);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * retire le device du job
	 * 
	 * @param idJob
	 * @return noContent
	 */
	@PUT
	@Path("/{idJob}/device/remove")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeDevice(@PathParam("idJob") long idJob) {
		return setDevice(idJob, null);
	}

	/**
	 * Ajout d'un media au job
	 * 
	 * @param idJob
	 * @param mediaId
	 * @return noContent
	 */
	@PUT
	@Path("/{idJob}/media/{mediaId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response setMedia(@PathParam("idJob") long idJob, @PathParam("mediaId") Long mediaId) {
		try {
			jobManagerLocal.setMedia(idJob, mediaId);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * retire un media au job
	 * 
	 * @param idJob
	 * @return noContent
	 */
	@PUT
	@Path("/{idJob}/media/remove")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeMedia(@PathParam("idJob") long idJob) {
		return setMedia(idJob, null);
	}

	/**
	 * Mise à jour du statut d'un job
	 * 
	 * @param idJob
	 * @return ok
	 */
	@PUT
	@Path("/{idJob}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response setStatus(@PathParam("idJob") long idJob, JobStatusDto jobStatusDto) {
		JobDto jobDto;
		try {
			JobStatusType jobStatus = JobStatusType.valueOf(jobStatusDto.getStatus());
			Job job = jobManagerLocal.setStatus(idJob, jobStatus, jobStatusDto.getExitCode(),
					jobStatusDto.getStatusDesc());
			jobDto = new JobDto(job);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(jobDto).build();
	}

	/**
	 * Arrête un Job
	 * 
	 * @param idJob
	 * @return ok
	 */
	@POST
	@Path("/{idJob}/kill")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response killJob(@PathParam("idJob") long idJob) {
		try {
			jobManagerLocal.killJob(idJob);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * update la propriété du job
	 * 
	 * @param idJob
	 * @param nameproperty
	 * @return noContent
	 */
	@PUT
	@Path("/{idJob}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateJobProperty(@PathParam("idJob") long idJob, @PathParam("nameProperty") String nameproperty,
			JobPropertyDto jobPropertyDto) {
		if (jobPropertyDto == null && nameproperty == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Job job = jobManagerLocal.retrieveJob(idJob);
			JobProperty jobProp = jobPropertyLocal.retrieveJobProperty(job.getId(), nameproperty);
			if (jobPropertyDto.getName() != null && !jobPropertyDto.getName().isEmpty()) {
				jobProp.setName(jobPropertyDto.getName());
			}
			if (jobPropertyDto.getTxt() != null) {
				jobProp.setValue(jobPropertyDto.getTxt());
			}
			if (jobPropertyDto.isPersist() != null) {
				jobProp.setPersist(jobPropertyDto.isPersist());
			}
			jobPropertyLocal.updateJobProperty(jobProp);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

}
