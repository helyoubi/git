package fr.datasyscom.scopiom.rest.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.ArrayUtils;

import fr.datasyscom.pome.ejbentity.Groupe;
import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.filter.JobFilter;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;

@Path("/workflows")
public class WorkflowRestWS {
 
	@EJB
	WorkflowManagerLocal workflowLocal;
	@EJB 
	GroupeManagerLocal grpLocal;
	@EJB
	JobManagerLocal jobManagerLocal;

	private JobFilter jobFilter = JobFilter.all();
	
	/**
	 * 
	 * Retourne le workflow par son nom
	 * 
	 * @param workflowName
	 * @return ok
	 */
	@GET
	@Path("/{workflowName}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveWorkflowByName(@PathParam("workflowName") String workflowName) {

		WorkflowDto workflowDto = null;
		try {
			Workflow workflow = workflowLocal.retrieveWF(workflowName);
			workflowDto = new WorkflowDto(workflow);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(workflowDto).build();
	}
	
	@GET
	@Path("/test/testW")
	@Produces({MediaType.APPLICATION_XML})
	public Response test() {
		return Response.ok("toto!!").build();
		
	}

	/**
	 * 
	 * Renvoie la liste des workflows
	 * 
	 * @param idWorkflow
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllWorkflow(@QueryParam("id") long idWorkflow) {

		List<WorkflowDto> workflowDtos = new ArrayList<WorkflowDto>();
		if (idWorkflow != 0) {
			try {
				workflowDtos.add(new WorkflowDto(workflowLocal.retrieveWF(idWorkflow)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Workflow> workflowList = workflowLocal.retrieveAllWF();
			for (Workflow workflow : workflowList) {
				workflowDtos.add(new WorkflowDto(workflow));
			}
		}

		GenericEntity<List<WorkflowDto>> listRestWorkflows = new GenericEntity<List<WorkflowDto>>(workflowDtos) {
		};
		return Response.ok(listRestWorkflows).build();
	}

	/**
	 * 
	 * ajout d'un groupe au workflow
	 * 
	 * @param nomGroupe
	 * @param nameWorkflow
	 * @return ok
	 */
	@POST
	@Path("/{nameWorkflow}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addGroupFromWorkflow(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameWorkflow") String nameWorkflow) {
		try {
			Workflow workflow = workflowLocal.retrieveWF(nameWorkflow);
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeIds = workflow.getGroupes().stream().map(g -> g.getId()).collect(Collectors.toSet());
			if (groupeIds.add(groupe.getId())) {
				long[] tabId = ArrayUtils.toPrimitive(groupeIds.toArray(new Long[0]));
				workflowLocal.setGroupes(workflow.getId(), tabId);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();

	}

	/**
	 * 
	 * retire le groupe d'un workflow
	 * 
	 * @param nameGroupe
	 * @param nameGroup
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameWorkflow}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeGroupFromWorkflow(@PathParam("nameGroup") String nameGroupe,
			@PathParam("nameWorkflow") String nameWorkflow) {
		try {
			Workflow workflow = workflowLocal.retrieveWF(nameWorkflow);
			List<Groupe> groupeList = workflow.getGroupes();
			List<Long> listgrp = new ArrayList<Long>();
			for (Groupe groupe : groupeList) {
				if (!(groupe.getName().equals(nameGroupe))) {
					listgrp.add(groupe.getId());
				}
			}
			long[] tabId = ArrayUtils.toPrimitive(listgrp.toArray(new Long[0]));
			workflowLocal.setGroupes(workflow.getId(), tabId);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Retourne la liste des jobs attachés à un workflow par status
	 * 
	 * @param nameWF
	 * @param status
	 * @return OK
	 */
	@GET
	@Path("/{nameWF}/jobs/status/{status}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveJobsFromWorkflowByStatus(@PathParam("nameWF") String nameWF, @PathParam("status") String status) {
		List<JobDto> jobsDtos = new ArrayList<JobDto>();
		if (nameWF == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Workflow workflow = workflowLocal.retrieveWF(nameWF);
			List<Job> workflowJobList = jobManagerLocal
					.retrieveListJob(jobFilter.byWorkflowId(workflow.getId()).byStatus(JobStatusType.valueOf(status)));
			for (Job job : workflowJobList) {
				jobsDtos.add(new JobDto(job));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<JobDto>> listRestJobWF = new GenericEntity<List<JobDto>>(jobsDtos) {
		};
		return Response.ok(listRestJobWF).build();
	}
	
	/**
	 * Retourne la liste des jobs attachés à un workflow
	 * 
	 * @param nameWF
	 * @return OK
	 */
	@GET
	@Path("/{nameWF}/jobs")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllJobsFromWorkflow(@PathParam("nameWF") String nameWF) {
		return retrieveJobsFromWorkflowByStatus(nameWF,JobStatusType.ALL.name());
	}
	
	/**
	 * renvoie les groupes attachés au workflow
	 * 
	 * @param workflowName
	 * @return ok
	 */
	@GET
	@Path("/{workflowName}/groups")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveGroupByWorkflow(@PathParam("workflowName") String workflowName) {
		List<GroupeDto> groupeDtos = new ArrayList<GroupeDto>();
		try {
			Workflow workflow = workflowLocal.retrieveWF(workflowName);
			List<Groupe> groupeList = workflow.getGroupes();
			for (Groupe groupe : groupeList) {
				groupeDtos.add(new GroupeDto(groupe));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<GroupeDto>> listGroupe = new GenericEntity<List<GroupeDto>>(groupeDtos) {
		};
		return Response.ok(listGroupe).build();
	}

	/**
	 * mise à jour du workflow
	 * 
	 * 
	 * @param workflowName
	 * @param workflowDto
	 * @return noContent
	 */
	@PUT
	@Path("/{workflowName}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateWorkflow(@PathParam("workflowName") String workflowName, WorkflowDto workflowDto) {

		if (workflowDto != null && workflowName != null) {
			try {
				Workflow workflow = workflowLocal.retrieveWF(workflowName);
				if (workflowDto.getNom() != null) {
					workflow.setName(workflowDto.getNom());
				}
				if (workflowDto.getDescription() != null) {
					workflow.setDescription(workflowDto.getDescription());
				}
				workflowLocal.updateWF(workflow);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 *
	 * Supprime le workflow par son nom
	 * 
	 * @param workflowName
	 * @return noContent
	 */
	@DELETE
	@Path("/{workflowName}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteWorkflow(@PathParam("workflowName") String workflowName) {

		try {
			Workflow workflow = workflowLocal.retrieveWF(workflowName);

			workflowLocal.deleteWF(workflow.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

}
