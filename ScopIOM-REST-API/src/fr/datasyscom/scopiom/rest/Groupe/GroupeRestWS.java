package fr.datasyscom.scopiom.rest.Groupe;

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

import org.apache.commons.lang.ArrayUtils;

import fr.datasyscom.pome.ejbentity.Agency;
import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.Groupe;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.User;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbsession.agency.AgencyManagerLocal;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.ejbsession.queue.QueueManagerLocal;
import fr.datasyscom.pome.ejbsession.user.UserManagerLocal;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.AgencyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.UserDto;
import fr.datasyscom.scopiom.rest.pojo.UsersListDto;
import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;

@Path("/groups")
public class GroupeRestWS {

	@EJB
	GroupeManagerLocal grpLocal;
	@EJB
	DeviceManagerLocal deviceLocal;
	@EJB
	QueueManagerLocal queueLocal;
	@EJB
	AgencyManagerLocal agencyLocal;
	@EJB
	WorkflowManagerLocal workflowLocal;
	@EJB
	UserManagerLocal usLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupérer un groupe par son nom
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveGroup(@PathParam("nameGroup") String nameGroup) {
		GroupeDto groupeDto = null;
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			groupeDto = new GroupeDto(groupe);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(groupeDto).build();
	}

	/**
	 * 
	 * Renvoie la liste des groupes ou le groupe par id
	 * 
	 * @param idGroup
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllGroups(@QueryParam("id") long idGroup) {
		List<GroupeDto> groupeDtos = new ArrayList<GroupeDto>();
		if (idGroup != 0) {
			try {
				groupeDtos.add(new GroupeDto(grpLocal.retrieveGroupe(idGroup)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Groupe> grplist = grpLocal.retrieveAllGroupe();
			GroupeDto groupeDto = null;
			for (Groupe groupe : grplist) {
				groupeDto = new GroupeDto(groupe);
				groupeDtos.add(groupeDto);
			}
		}
		GenericEntity<List<GroupeDto>> listRestGroups = new GenericEntity<List<GroupeDto>>(groupeDtos) {
		};
		return Response.ok(listRestGroups).build();
	}

	/**
	 * Ajout d'un périphérique au groupe
	 * 
	 * @param nomGroupe
	 * @param nameDevice
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/devices/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addDeviceFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameDevice") String nameDevice) {

		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeDevicesIds = groupe.getDevices().stream().map(u -> u.getId()).collect(Collectors.toSet());
			Device device = deviceLocal.retrieveDevice(nameDevice);
			if (groupeDevicesIds.add(device.getId())) {
				long[] tabDevices = ArrayUtils.toPrimitive(groupeDevicesIds.toArray(new Long[0]));
				grpLocal.setDevices(groupe.getId(), tabDevices);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * Ajout d'une file de traitement au groupe
	 * 
	 * @param nomGroupe
	 * @param nameQueue
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/queues/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addQueueFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameQueue") String nameQueue) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeQueuesIds = groupe.getQueues().stream().map(u -> u.getId()).collect(Collectors.toSet());
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			if (groupeQueuesIds.add(queue.getId())) {
				long[] tabQueuesGrp = ArrayUtils.toPrimitive(groupeQueuesIds.toArray(new Long[0]));
				grpLocal.setQueues(groupe.getId(), tabQueuesGrp);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * Ajout d'une agence administrable au groupe
	 * 
	 * @param nomGroupe
	 * @param nameAgence
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/adminAgency/{nameAgence}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addAdminAgencyFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameAgence") String nameAgence) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeAdminAgenciesIds = groupe.getAdminAgencies().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Agency agence = agencyLocal.retrieveAgency(nameAgence);
			if (groupeAdminAgenciesIds.add(agence.getId())) {
				long[] tabAdminAgencies = ArrayUtils.toPrimitive(groupeAdminAgenciesIds.toArray(new Long[0]));
				grpLocal.setAdminAgencies(groupe.getId(), tabAdminAgencies);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}
	
	/**
	 * Ajout d'un utilisateur au groupe
	 * 
	 * @param nomGroupe
	 * @param loginUser
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/user/{loginUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addUserFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("loginUser") String loginUser) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<String> usersLoginList = groupe.getUsers().stream().map(u -> u.getLogin())
					.collect(Collectors.toSet());
			User user = usLocal.retrieveUser(loginUser);
			if (usersLoginList.add(user.getLogin())) {
				String[] listLoginUsers = usersLoginList.toArray(new String[0]);
				grpLocal.setUsers(groupe.getId(), listLoginUsers);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * Ajout d'une agence liée au groupe
	 * 
	 * @param nomGroupe
	 * @param nameAgence
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/userAgency/{nameAgence}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addUserAgencyFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameAgence") String nameAgence) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeUserAgenciesIds = groupe.getUserAgencies().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Agency agence = agencyLocal.retrieveAgency(nameAgence);
			if (groupeUserAgenciesIds.add(agence.getId())) {
				long[] tabUserAgencies = ArrayUtils.toPrimitive(groupeUserAgenciesIds.toArray(new Long[0]));
				grpLocal.setUserAgencies(groupe.getId(), tabUserAgencies);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * renvoie les utilisateurs du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/users")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveUsersFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<UserDto> userDtos = new ArrayList<UserDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<User> usersList = groupe.getUsers();
			for (User user : usersList) {
				userDtos.add(new UserDto(user));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<UserDto>> listUsers = new GenericEntity<List<UserDto>>(userDtos) {
		};
		return Response.ok(listUsers).build();
	}
	
	

	/**
	 * renvoie les files de traitements du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/queues")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveQueuesFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<QueueDto> queueDtos = new ArrayList<QueueDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<Queue> queuesList = groupe.getQueues();
			for (Queue queue : queuesList) {
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
	 * renvoie les périphériques du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/devices")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveDevicesFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<DeviceDto> deviceDtos = new ArrayList<DeviceDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<Device> devicesList = groupe.getDevices();
			for (Device device : devicesList) {
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
	 * renvoie les workflows du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/workflows")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveWorkflowsFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<WorkflowDto> workflowDtos = new ArrayList<WorkflowDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<Workflow> workflowList = groupe.getWorkflows();
			for (Workflow workflow : workflowList) {
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
	 * renvoie les agences administrables du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/adminagency")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAdminAgenciesFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<AgencyDto> agencyDtos = new ArrayList<AgencyDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<Agency> adminAgencyList = groupe.getAdminAgencies();
			for (Agency agency : adminAgencyList) {
				agencyDtos.add(new AgencyDto(agency));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<AgencyDto>> listAdminAgencies = new GenericEntity<List<AgencyDto>>(agencyDtos) {
		};
		return Response.ok(listAdminAgencies).build();
	}

	/**
	 * renvoie les agences liées du groupe
	 * 
	 * @param nameGroup
	 * @return ok
	 */
	@GET
	@Path("/{nameGroup}/useragency")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveUserAgencyFromGroup(@PathParam("nameGroup") String nameGroup) {
		List<AgencyDto> agencyDtos = new ArrayList<AgencyDto>();
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			List<Agency> userAgencyList = groupe.getUserAgencies();
			for (Agency agency : userAgencyList) {
				agencyDtos.add(new AgencyDto(agency));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<AgencyDto>> listUserAgencies = new GenericEntity<List<AgencyDto>>(agencyDtos) {
		};
		return Response.ok(listUserAgencies).build();
	}

	/**
	 * Ajout d'un workflow au groupe
	 * 
	 * @param nomGroupe
	 * @param nameWorkflow
	 * @return ok
	 */
	@POST
	@Path("/{nameGroup}/workflows/{nameWorkflow}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addWorkflowFromGroup(@PathParam("nameGroup") String nomGroupe,
			@PathParam("nameWorkflow") String nameWorkflow) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nomGroupe);
			Set<Long> groupeWorkflowsIds = groupe.getWorkflows().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			Workflow workflow = workflowLocal.retrieveWF(nameWorkflow);
			if (groupeWorkflowsIds.add(workflow.getId())) {
				long[] tabWorkFlows = ArrayUtils.toPrimitive(groupeWorkflowsIds.toArray(new Long[0]));
				grpLocal.setWorkflows(groupe.getId(), tabWorkFlows);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * 
	 * Supprime le workflow du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param nameWorkflow
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/workflows/{nameWorkflow}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeWorkflowFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("nameWorkflow") String nameWorkflow) {
		try {
			Workflow workflowRetrived = workflowLocal.retrieveWF(nameWorkflow);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<Workflow> workflows = groupe.getWorkflows();
			List<Long> idsWorkflowList = new ArrayList<Long>();
			for (Workflow workflow : workflows) {
				if (!(workflow.getName().equals(workflowRetrived.getName()))) {
					idsWorkflowList.add(workflow.getId());
				}
			}
			long[] tabWorkflows = ArrayUtils.toPrimitive(idsWorkflowList.toArray(new Long[0]));
			grpLocal.setWorkflows(groupe.getId(), tabWorkflows);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();

	}

	/**
	 * 
	 * Supprime le device du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param nameDevice
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/devices/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeDeviceFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("nameDevice") String nameDevice) {

		try {
			Device deviceD = deviceLocal.retrieveDevice(nameDevice);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<Device> deviceList = groupe.getDevices();
			List<Long> nameDeviceList = new ArrayList<Long>();
			for (Device device : deviceList) {
				if (!(device.getName().equals(deviceD.getName()))) {
					nameDeviceList.add(device.getId());
				}
			}

			long[] tabDevices = ArrayUtils.toPrimitive(nameDeviceList.toArray(new Long[0]));
			grpLocal.setDevices(groupe.getId(), tabDevices);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();

	}

	/**
	 * 
	 * retire une file de traitement du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param nameQueue
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/queues/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeQueueFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("nameQueue") String nameQueue) {

		try {
			Queue queueRetrieved = queueLocal.retrieveQueue(nameQueue);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<Queue> listQueue = groupe.getQueues();
			List<Long> listidQueues = new ArrayList<Long>();
			for (Queue queue : listQueue) {
				if (!(queue.getName().equals(queueRetrieved.getName()))) {
					listidQueues.add(queue.getId());
				}
			}
			long[] tabQueues = ArrayUtils.toPrimitive(listidQueues.toArray(new Long[0]));
			grpLocal.setQueues(groupe.getId(), tabQueues);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();

	}

	/**
	 * 
	 * Supprime une agence administrable du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param nameAgency
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/adminAgency/{nameAgency}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeAdminAgencyFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("nameAgency") String nameAgency) {
		try {
			Agency agence = agencyLocal.retrieveAgency(nameAgency);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<Agency> adminAgenciesList = groupe.getAdminAgencies();
			List<Long> idsAdminAgenciesList = new ArrayList<Long>();
			for (Agency agency : adminAgenciesList) {
				if (!(agency.getName().equals(agence.getName()))) {
					idsAdminAgenciesList.add(agency.getId());
				}
			}
			long[] tabAdminAgencies = ArrayUtils.toPrimitive(idsAdminAgenciesList.toArray(new Long[0]));
			grpLocal.setAdminAgencies(groupe.getId(), tabAdminAgencies);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}
	
	/**
	 * 
	 * Supprime une utilisateur du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param loginUser
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/user/{loginUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeUserFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("loginUser") String loginUser) {
		try {
			User userRetrieved = usLocal.retrieveUser(loginUser);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<User> usersList = groupe.getUsers();
			List<String> loginUsersList = new ArrayList<String>();
			for (User user : usersList) {
				if (!(user.getLogin().equals(userRetrieved.getLogin()))) {
					loginUsersList.add(user.getLogin());
				}
			}
			String[] listLoginUsers = loginUsersList.toArray(new String[0]);
			grpLocal.setUsers(groupe.getId(), listLoginUsers);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * 
	 * Supprime une agence liée du groupe
	 * 
	 * 
	 * @param nameGrp
	 * @param nameAgency
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGrp}/userAgency/{nameAgency}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeUserAgencyFromGroup(@PathParam("nameGrp") String nameGrp,
			@PathParam("nameAgency") String nameAgency) {
		try {
			Agency agence = agencyLocal.retrieveAgency(nameAgency);
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
			List<Agency> userAgenciesList = groupe.getUserAgencies();
			List<Long> nameUserAgenciesList = new ArrayList<Long>();
			for (Agency agency : userAgenciesList) {
				if (!(agency.getName().equals(agence.getName()))) {
					nameUserAgenciesList.add(agency.getId());
				}
			}
			long[] tabUserAgencies = ArrayUtils.toPrimitive(nameUserAgenciesList.toArray(new Long[0]));
			grpLocal.setUserAgencies(groupe.getId(), tabUserAgencies);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * création du groupe, Conflict en cas ou le nom du groupe existe déja bad
	 * request si la syntaxe de la création du groupe est incorrecte retourne 201
	 * avec un lien générer a l en-tete de la réponse http contenant le nouveau nom
	 * du groupe créé
	 * 
	 * @param groupeDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createGroup(GroupeDto groupeDto) {

		Groupe groupe;
		if (groupeDto != null) {
			try {
				groupe = new Groupe();
				if (groupeDto.getName() != null) {
					groupe.setName(groupeDto.getName());
				}
				if (groupeDto.getDescription() != null) {
					groupe.setDescription(groupeDto.getDescription());
				}
				if (groupeDto.getComment() != null) {
					groupe.setComment(groupeDto.getComment());
				}
				if (groupeDto.isMandatory() != null) {
					groupe.setMandatory(groupeDto.isMandatory());
				}
				if (groupeDto.isCanSubmitJobs() != null) {
					groupe.setCanSubmitJobs(groupeDto.isCanSubmitJobs());
				}
				if (groupeDto.isCanGenerateReports() != null) {
					groupe.setCanGenerateReports(groupeDto.isCanGenerateReports());
				}
				if (groupeDto.isCanAccessJobLinks() != null) {
					groupe.setCanAccessJobLinks(groupeDto.isCanAccessJobLinks());
				}
				if (groupeDto.isCanAccessJobsTree() != null) {
					groupe.setCanAccessJobsTree(groupeDto.isCanAccessJobsTree());
				}
				groupe = grpLocal.createGroupe(groupe);
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newNameGrp = String.valueOf(groupe.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameGrp).build();

		return Response.created(uri).build();

	}

	/**
	 * 
	 * Suppression d'un groupe
	 * 
	 * @param nameGroupe
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameGroupe}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteGroup(@PathParam("nameGroupe") String nameGroupe) {
		try {
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroupe);
			grpLocal.deleteGroupe(groupe.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Met a jour l utilisateur par le groupe not found si le nom du groupe ou login
	 * utilisateur n'est pas reconnu
	 * 
	 * @param nameGrp
	 * @param usersList
	 * @return noContent
	 */
	@PUT
	@Path("/{name}/users")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateUsersFromGroup(@PathParam("name") String nameGrp, UsersListDto usersList) {

		Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
		try {
			String[] tabUser = usersList.getUsersList().toArray(new String[0]);
			for (String userCheck : tabUser) {
				usLocal.retrieveUser(userCheck);
			}
			grpLocal.setUsers(groupe.getId(), tabUser);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * mise a jour du groupe par son nom, not found si le nom du groupe n'existe pas
	 * , bad request en cas d'erreur de syntaxe
	 * 
	 * @param nameGrp
	 * @param groupeDto
	 * @return noContent
	 */
	@PUT
	@Path("/{name}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateGroup(@PathParam("name") String nameGrp, GroupeDto groupeDto) {

		if (groupeDto != null) {
			try {
				Groupe groupe = grpLocal.retrieveGroupeByName(nameGrp);
				if (groupeDto.getDescription() != null) {
					groupe.setDescription(groupeDto.getDescription());
				}
				if (groupeDto.getComment() != null) {
					groupe.setComment(groupeDto.getComment());
				}
				if (groupeDto.isCanSubmitJobs() != null) {
					groupe.setCanSubmitJobs(groupeDto.isCanSubmitJobs());
				}
				if (groupeDto.isCanGenerateReports() != null) {
					groupe.setCanGenerateReports(groupeDto.isCanGenerateReports());
				}
				if (groupeDto.isCanAccessJobLinks() != null) {
					groupe.setCanAccessJobLinks(groupeDto.isCanAccessJobLinks());
				}
				if (groupeDto.isCanAccessJobsTree() != null) {
					groupe.setCanAccessJobsTree(groupeDto.isCanAccessJobsTree());
				}
				grpLocal.updateGroupe(groupe);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

}
