package fr.datasyscom.scopiom.rest.resourcegroup;

import java.net.URI;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.ResourceGroup;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbentity.filter.ResourceGroupFilter;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.queue.QueueManagerLocal;
import fr.datasyscom.pome.ejbsession.resourcegroup.ResourceGroupManagerLocal;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.ResourceGroupDto;
import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;

@Path("/resourceGroups")
public class ResourceGroupRestWS {

	@EJB
	ResourceGroupManagerLocal resourceGrpLocal;

	@EJB
	DeviceManagerLocal deviceLocal;

	@EJB
	QueueManagerLocal queueLocal;

	@EJB
	WorkflowManagerLocal workflowLocal;

	@Context
	UriInfo uriInfo;

	private ResourceGroupFilter resourceGroupFilter = ResourceGroupFilter.all();

	/**
	 * 
	 * Récupérer un groupe de ressource
	 * 
	 * @param resourceGroup
	 * @return ok
	 */
	@GET
	@Path("/{resourceGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveResourceGroup(@PathParam("resourceGroup") String resourceGroup) {

		ResourceGroupDto resourceGroupDto = null;
		try {
			ResourceGroup resourceGroupRetrive = resourceGrpLocal.retrieve(resourceGroupFilter.byName(resourceGroup));
			resourceGroupDto = new ResourceGroupDto(resourceGroupRetrive);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(resourceGroupDto).build();
	}

	/**
	 * 
	 * Récupérer toutes tous les groupes de ressources
	 * 
	 * @param idResourceGrp
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllResourceGroup(@QueryParam("id") long idResourceGrp) {

		List<ResourceGroupDto> resourceGroupDto = new ArrayList<ResourceGroupDto>();
		if (idResourceGrp != 0) {
			try {
				ResourceGroup resourceGroupRetrive = resourceGrpLocal.retrieve(resourceGroupFilter.byId(idResourceGrp));
				resourceGroupDto.add(new ResourceGroupDto(resourceGroupRetrive));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<ResourceGroup> resourceGroupsList = resourceGrpLocal.retrieveList(ResourceGroupFilter.all());
			for (ResourceGroup resourceGroup : resourceGroupsList) {
				resourceGroupDto.add(new ResourceGroupDto(resourceGroup));
			}
		}

		GenericEntity<List<ResourceGroupDto>> listRestGroupResources = new GenericEntity<List<ResourceGroupDto>>(
				resourceGroupDto) {
		};
		return Response.ok(listRestGroupResources).build();
	}

	/**
	 * 
	 * Création d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createResourceGroup(ResourceGroupDto resourceGroupDto) {

		ResourceGroup resourceGroup;
		if (resourceGroupDto != null) {
			try {
				resourceGroup = new ResourceGroup();
				if (resourceGroupDto.getName() != null) {
					resourceGroup.setName(resourceGroupDto.getName());
				}
				if (resourceGroupDto.getDescription() != null) {
					resourceGroup.setDescription(resourceGroupDto.getDescription());
				}
				resourceGroup = resourceGrpLocal.create(resourceGroup);
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newResourceGrp = String.valueOf(resourceGroup.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newResourceGrp).build();

		return Response.created(uri).build();

	}

	/**
	 * 
	 * Suppression d'un groupe de ressource
	 * 
	 * @param nameDevice
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameRessourceGrp}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteResourceGroup(@PathParam("nameRessourceGrp") String nameRessourceGrp) {
		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			resourceGrpLocal.delete(resourceGroup.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Mise à jour d'un groupe de ressource
	 * 
	 * @param nameRessourceGrp
	 * @param resourceGroupDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameRessourceGrp}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			ResourceGroupDto resourceGroupDto) {

		if (resourceGroupDto != null && nameRessourceGrp != null) {
			try {
				ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
				if (resourceGroupDto.getName() != null) {
					resourceGroup.setName(resourceGroupDto.getName());
				}
				if (resourceGroupDto.getDescription() != null) {
					resourceGroup.setDescription(resourceGroupDto.getDescription());
				}
				resourceGrpLocal.update(resourceGroup);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.noContent().build();
	}

	/**
	 * Ajout d'un device au ressource groupe
	 * 
	 * @param nameRessourceGrp
	 * @param nameDevice
	 * @return ok
	 */
	@POST
	@Path("/{nameRessourceGrp}/devices/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addDeviceFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameDevice") String nameDevice) {

		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			Set<Long> resourceGroupIds = resourceGroup.getDevices().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Device device = deviceLocal.retrieveDevice(nameDevice);
			if (resourceGroupIds.add(device.getId())) {
				List<Long> listResourceGroups = new ArrayList<Long>(resourceGroupIds);
				resourceGrpLocal.setDevices(resourceGroup.getId(), listResourceGroups);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 *
	 * Renvoie les devices d'un groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @return ok
	 */
	@GET
	@Path("/{nameRessourceGrp}/devices")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveDevicesFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp) {

		List<DeviceDto> deviceDtos = new ArrayList<DeviceDto>();
		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Device> deviceList = resourceGroup.getDevices();
			for (Device device : deviceList) {
				deviceDtos.add(new DeviceDto(device));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<DeviceDto>> listDevices = new GenericEntity<List<DeviceDto>>(deviceDtos) {
		};
		return Response.ok(listDevices).build();
	}

	/**
	 *
	 * Suppression d'un device du groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @param nameDevice
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameRessourceGrp}/devices/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeDeviceFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameDevice") String nameDevice) {

		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Device> deviceList = resourceGroup.getDevices();
			List<Long> deviceLongList = new ArrayList<Long>();
			for (Device device : deviceList) {
				Device deviceRetrieved = deviceLocal.retrieveDevice(nameDevice);
				if (!(device.getName().equals(deviceRetrieved.getName()))) {
					deviceLongList.add(device.getId());
				}
			}
			resourceGrpLocal.setDevices(resourceGroup.getId(), deviceLongList);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();

	}

	/**
	 * Ajout d'une file de traitement a un ressource groupe
	 * 
	 * @param nameRessourceGrp
	 * @param nameQueue
	 * @return ok
	 */
	@POST
	@Path("/{nameRessourceGrp}/queues/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addQueueFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameQueue") String nameQueue) {

		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			Set<Long> resourceQueueIds = resourceGroup.getQueues().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			if (resourceQueueIds.add(queue.getId())) {
				List<Long> listResourceQueues = new ArrayList<Long>(resourceQueueIds);
				resourceGrpLocal.setQueues(resourceGroup.getId(), listResourceQueues);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 *
	 * Renvoie les files de traitements d'un groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @return ok
	 */
	@GET
	@Path("/{nameRessourceGrp}/queues")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveQueuesFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp) {

		List<QueueDto> queueDtos = new ArrayList<QueueDto>();
		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Queue> listQueue = resourceGroup.getQueues();
			for (Queue queue : listQueue) {
				queueDtos.add(new QueueDto(queue));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<QueueDto>> listQueues = new GenericEntity<List<QueueDto>>(queueDtos) {
		};
		return Response.ok(listQueues).build();
	}

	/**
	 *
	 * Suppression d'une file de traitement du groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @param nameQueue
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameRessourceGrp}/queues/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeQueueFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameQueue") String nameQueue) {

		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Queue> listQueue = resourceGroup.getQueues();
			List<Long> queueLongList = new ArrayList<Long>();
			for (Queue queue : listQueue) {
				Queue queueRetrieved = queueLocal.retrieveQueue(nameQueue);
				if (!(queue.getName().equals(queueRetrieved.getName()))) {
					queueLongList.add(queue.getId());
				}
			}
			resourceGrpLocal.setQueues(resourceGroup.getId(), queueLongList);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();

	}

	/**
	 * Ajout d'un flux de travail à un groupe de ressource
	 * 
	 * @param nameRessourceGrp
	 * @param nameWorkflow
	 * @return ok
	 */
	@POST
	@Path("/{nameRessourceGrp}/workflows/{nameWorkflow}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addWorkflowFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameWorkflow") String nameWorkflow) {

		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			Set<Long> workFlowIds = resourceGroup.getWorkflows().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Workflow workflow = workflowLocal.retrieveWF(nameWorkflow);
			if (workFlowIds.add(workflow.getId())) {
				List<Long> listWorkflows = new ArrayList<Long>(workFlowIds);
				resourceGrpLocal.setWorkflows(resourceGroup.getId(), listWorkflows);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 *
	 * Renvoie les flux de travail d'un groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @return ok
	 */
	@GET
	@Path("/{nameRessourceGrp}/workflows")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveWorkflowFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp) {

		List<WorkflowDto> workflowDtos = new ArrayList<WorkflowDto>();
		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Workflow> listWorkflow = resourceGroup.getWorkflows();
			for (Workflow workflow : listWorkflow) {
				workflowDtos.add(new WorkflowDto(workflow));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<WorkflowDto>> listWorkflows = new GenericEntity<List<WorkflowDto>>(workflowDtos) {
		};
		return Response.ok(listWorkflows).build();
	}

	/**
	 *
	 * Suppression d'un flux de travail d'un groupe de ressource
	 *
	 * 
	 * @param nameRessourceGrp
	 * @param nameWorkflow
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameRessourceGrp}/workflows/{nameWorkflow}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeWorkflowFromResourceGrp(@PathParam("nameRessourceGrp") String nameRessourceGrp,
			@PathParam("nameWorkflow") String nameWorkflow) {
		try {
			ResourceGroup resourceGroup = resourceGrpLocal.retrieve(resourceGroupFilter.byName(nameRessourceGrp));
			List<Workflow> listWorkflow = resourceGroup.getWorkflows();
			List<Long> workflowLongList = new ArrayList<Long>();
			for (Workflow workflow : listWorkflow) {
				Workflow workflowRetrieved = workflowLocal.retrieveWF(nameWorkflow);
				if (!(workflow.getName().equals(workflowRetrieved.getName()))) {
					workflowLongList.add(workflow.getId());
				}
			}
			resourceGrpLocal.setWorkflows(resourceGroup.getId(), workflowLongList);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();

	}

}
