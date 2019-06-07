package fr.datasyscom.scopiom.rest.queue;

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

import fr.datasyscom.pome.ejbentity.Groupe;
import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.Queue.QueueStatusType;
import fr.datasyscom.pome.ejbentity.QueueProperty;
import fr.datasyscom.pome.ejbentity.filter.AgentFilter;
import fr.datasyscom.pome.ejbentity.filter.JobFilter;
import fr.datasyscom.pome.ejbentity.filter.TableExploitFilter;
import fr.datasyscom.pome.ejbentity.tableexploit.TableExploit;
import fr.datasyscom.pome.ejbsession.agent.AgentManagerLocal;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.pome.ejbsession.queue.QueueManagerLocal;
import fr.datasyscom.pome.ejbsession.queue.property.QueuePropertyManagerLocal;
import fr.datasyscom.pome.ejbsession.tableexploit.TableExploitManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.QueuePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.QueueStatusDto;
import fr.datasyscom.scopiom.rest.pojo.TableExploitDtoRest;

@Path("/queues")
public class QueueRestWS {

	@EJB
	QueueManagerLocal queueLocal;
	@EJB
	QueuePropertyManagerLocal queuePropertyLocal;
	@EJB
	JobManagerLocal jobManagerLocal;
	@EJB
	AgentManagerLocal agentLocal;
	@EJB
	DeviceManagerLocal deviceLocal;
	@EJB
	GroupeManagerLocal grpLocal;
	@EJB
	TableExploitManagerLocal tableExploitLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupérer une file de traitement par son nom
	 * 
	 * @param nameQueue
	 * @return ok
	 */
	@GET
	@Path("/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveQueue(@PathParam("nameQueue") String nameQueue) {
		QueueDto queueDto;
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			queueDto = new QueueDto(queue);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(queueDto).build();
	}

	/**
	 * 
	 * Renvoie la liste des file de traitements ou par id
	 * 
	 * @param idQueue
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllQueues(@QueryParam("id") long idQueue) {
		List<QueueDto> queueDtos = new ArrayList<QueueDto>();
		if (idQueue != 0) {
			try {
				queueDtos.add(new QueueDto(queueLocal.retrieveQueue(idQueue)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {

			List<Queue> queueList = queueLocal.retrieveAllQueue();
			QueueDto queueDto = null;
			for (Queue queue : queueList) {
				queueDto = new QueueDto(queue);
				queueDtos.add(queueDto);
			}
		}
		GenericEntity<List<QueueDto>> listRestQueues = new GenericEntity<List<QueueDto>>(queueDtos) {
		};
		return Response.ok(listRestQueues).build();
	}

	/**
	 * 
	 * Supprime une file de traitement par son nom
	 * 
	 * @param nameQueue
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteQueue(@PathParam("nameQueue") String nameQueue) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			queueLocal.deleteQueue(queue.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Supprime une propriété de la file du traitement
	 * 
	 * @param nameQueue
	 * @param nameQueueProperty
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameQueue}/property/{nameQueueProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteQueueproperty(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameQueueProperty") String nameQueueProperty) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			QueueProperty queueProperty = queuePropertyLocal.retrieveQueueProperty(queue.getId(), nameQueueProperty);
			queuePropertyLocal.deleteQueueProperty(queueProperty.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * création de la propriété d'une file de traitement
	 * 
	 * @param nameQueue
	 * @return ok
	 */
	@POST
	@Path("/{nameQueue}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createQueueProperty(@PathParam("nameQueue") String nameQueue, QueuePropertyDto queuePropertyDto) {

		if (queuePropertyDto == null || nameQueue == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		QueueProperty queueProperty = new QueueProperty();
		try {
			if (queuePropertyDto.getName() != null) {
				queueProperty.setName(queuePropertyDto.getName());
			}
			if (queuePropertyDto.getDescription() != null) {
				queueProperty.setDescription(queuePropertyDto.getDescription());
			}
			if (queuePropertyDto.getTxt() != null) {
				queueProperty.setValue(queuePropertyDto.getTxt());
			}
			if (queuePropertyDto.isDisplay() != null) {
				queueProperty.setDisplay(queuePropertyDto.isDisplay());
			}
			if (queuePropertyDto.isScriptExport() != null) {
				queueProperty.setScriptExport(queuePropertyDto.isScriptExport());
			}
			if (queuePropertyDto.isPersist() != null) {
				queueProperty.setPersist(queuePropertyDto.isPersist());
			}
			if (queuePropertyDto.isOverridable() != null) {
				queueProperty.setOverridable(queuePropertyDto.isOverridable());
			}
			if (queuePropertyDto.isJobExport() != null) {
				queueProperty.setJobExport(queuePropertyDto.isJobExport());
			}

			Queue queue = queueLocal.retrieveQueue(nameQueue);
			queueProperty = queuePropertyLocal.createQueueProperty(queueProperty, queue.getId());

		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		String newNameProperyQueue = String.valueOf(queueProperty.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameProperyQueue).build();

		return Response.created(uri).build();
	}

	/**
	 * renvoie le/les groupes de la file de traitement
	 * 
	 * @param nameQueue
	 * @return ok
	 */
	@GET
	@Path("/{nameQueue}/groups")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveGroupsFromQueue(@PathParam("nameQueue") String nameQueue) {
		List<GroupeDto> groupeDtosList = new ArrayList<GroupeDto>();
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			List<Groupe> groupeList = queue.getGroupes();
			for (Groupe groupe : groupeList) {
				groupeDtosList.add(new GroupeDto(groupe));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		GenericEntity<List<GroupeDto>> listGroupebyQueue = new GenericEntity<List<GroupeDto>>(groupeDtosList) {
		};
		return Response.ok(listGroupebyQueue).build();
	}

	/**
	 * renvoie le/les table d'exploitation de la file de traitement
	 * 
	 * @param nameQueue
	 * @return ok
	 */
	@GET
	@Path("/{nameQueue}/tableExploit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveTableExploitFromQueue(@PathParam("nameQueue") String nameQueue) {
		List<TableExploitDtoRest> tableExploitDtos = new ArrayList<TableExploitDtoRest>();
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			List<TableExploit> tableExploitList = queue.getTableExploits();
			for (TableExploit tableExploit : tableExploitList) {
				tableExploitDtos.add(new TableExploitDtoRest(tableExploit));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		GenericEntity<List<TableExploitDtoRest>> listTableExploitRest = new GenericEntity<List<TableExploitDtoRest>>(
				tableExploitDtos) {
		};
		return Response.ok(listTableExploitRest).build();
	}

	/**
	 * renvoie la liste des propriétés d'une file de traitement par son nom
	 * 
	 * 
	 * @param queueName
	 * @return ok
	 */
	@GET
	@Path("/{queueName}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllQueueProperties(@PathParam("queueName") String queueName) {
		if (queueName == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		List<QueuePropertyDto> queueProprertyDto = new ArrayList<QueuePropertyDto>();
		try {
			Queue queue = queueLocal.retrieveQueue(queueName);
			List<QueueProperty> listQueueProperty = queuePropertyLocal.retrieveAllQueueProperty(queue.getId());
			for (QueueProperty queueProperty : listQueueProperty) {
				queueProprertyDto.add(new QueuePropertyDto(queueProperty));
			}
			GenericEntity<List<QueuePropertyDto>> listQueueGeneric = new GenericEntity<List<QueuePropertyDto>>(
					queueProprertyDto) {
			};
			return Response.ok(listQueueGeneric).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

	}

	/**
	 * Ajout d'un groupe à la file de traitement
	 * 
	 * @param nameQueue
	 * @param nameGroup
	 * @return ok
	 */
	@POST
	@Path("/{nameQueue}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addGroupFromQueue(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameGroup") String nameGroup) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			Set<Long> grpIds = queue.getGroupes().stream().map(u -> u.getId()).collect(Collectors.toSet());
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			if (grpIds.add(groupe.getId())) {
				long[] tabGroup = ArrayUtils.toPrimitive(grpIds.toArray(new Long[0]));
				queueLocal.setGroupes(queue.getId(), tabGroup, queue.isPublicAccess());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * Ajout d'une table d'exploitation à la file de traitement
	 * 
	 * @param nameQueue
	 * @param nameTableExploit
	 * @return ok
	 */
	@POST
	@Path("/{nameQueue}/tableExploit/{nameTableExploit}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addTableExploitFromQueue(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameTableExploit") String nameTableExploit) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			Set<Long> tableExploitIds = queue.getTableExploits().stream().map(u -> u.getId())
					.collect(Collectors.toSet());
			TableExploit tableExploit = tableExploitLocal
					.retrieveTableExploit(TableExploitFilter.all().byName(nameTableExploit));
			if (tableExploitIds.add(tableExploit.getId())) {
				long[] tabTableExploit = ArrayUtils.toPrimitive(tableExploitIds.toArray(new Long[0]));
				queueLocal.setTableExploits(queue.getId(), tabTableExploit);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * 
	 * Retire un groupe de la file de traitement
	 * 
	 * 
	 * @param nameQueue
	 * @param nameGroup
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameQueue}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeGroupFromQueue(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameGroup") String nameGroup) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			List<Groupe> listGrps = queue.getGroupes();
			List<Long> idGrpsList = new ArrayList<Long>();
			Groupe grouperetrieved = grpLocal.retrieveGroupeByName(nameGroup);
			for (Groupe groupe : listGrps) {
				if (!(groupe.getName().equals(grouperetrieved.getName()))) {
					idGrpsList.add(groupe.getId());
				}
			}
			long[] tabGroups = ArrayUtils.toPrimitive(idGrpsList.toArray(new Long[0]));
			queueLocal.setGroupes(queue.getId(), tabGroups, queue.isPublicAccess());
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Retire une table d'exploitation de la file de traitement
	 * 
	 * 
	 * @param nameQueue
	 * @param nameTableExploit
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameQueue}/tableExploit/{nameTableExploit}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeTableExploitFromQueue(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameTableExploit") String nameTableExploit) {
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			List<TableExploit> listTableExploit = queue.getTableExploits();
			List<Long> idTableExploitList = new ArrayList<Long>();
			TableExploit tableExploitRetrieved = tableExploitLocal
					.retrieveTableExploit(TableExploitFilter.all().byName(nameTableExploit));
			for (TableExploit tableExploit : listTableExploit) {
				if (!(tableExploit.getName().equals(tableExploitRetrieved.getName()))) {
					idTableExploitList.add(tableExploit.getId());
				}
			}
			long[] tabTableExploit = ArrayUtils.toPrimitive(idTableExploitList.toArray(new Long[0]));
			queueLocal.setTableExploits(queue.getId(), tabTableExploit);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Retourne le status de la file de traitement
	 * 
	 * @param nameQueue
	 * @return OK
	 */
	@GET
	@Path("/{nameQueue}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveQueueStatus(@PathParam("nameQueue") String nameQueue) {
		QueueStatusDto queueStatusDto;
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			queueStatusDto = new QueueStatusDto(queue);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(queueStatusDto).build();
	}

	/**
	 * Retourne la liste des jobs attachés à une file de traitement par statut
	 * 
	 * @param nameQueue
	 * @param status
	 * @return OK
	 */
	@GET
	@Path("/{nameQueue}/jobs/status/{status}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveJobsFromQueueByStatus(@PathParam("nameQueue") String nameQueue,
			@PathParam("status") String status) {
		if (nameQueue == null || status == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		List<JobDto> jobsDtos = new ArrayList<JobDto>();
		try {
			Queue queue = queueLocal.retrieveQueue(nameQueue);
			List<Job> queueJobList = jobManagerLocal.retrieveListJob(
					JobFilter.display().byQueueName(queue.getName()).byStatus(JobStatusType.valueOf(status)));
			for (Job job : queueJobList) {
				jobsDtos.add(new JobDto(job));
			}
			GenericEntity<List<JobDto>> listRestJobQueues = new GenericEntity<List<JobDto>>(jobsDtos) {
			};
			return Response.ok(listRestJobQueues).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
	}

	/**
	 * Retourne la liste des jobs attachés à une file de traitement
	 * 
	 * @param nameQueue
	 * @return OK
	 */
	@GET
	@Path("/{nameQueue}/jobs")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllJobsFromQueue(@PathParam("nameQueue") String nameQueue) {
		return retrieveJobsFromQueueByStatus(nameQueue, JobStatusType.ALL.name());
	}

	/**
	 * renvoie la propriété d'une file de traitement par son nom
	 * 
	 * 
	 * @param queueName
	 * @param nameProperty
	 * @return ok
	 */
	@GET
	@Path("/{queueName}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveQueueProperty(@PathParam("queueName") String queueName,
			@PathParam("nameProperty") String nameProperty) {
		QueuePropertyDto queuePropertyDto = null;
		try {
			if (queueName != null && nameProperty != null) {
				Queue queue = queueLocal.retrieveQueue(queueName);
				QueueProperty queueProperty = queuePropertyLocal.retrieveQueueProperty(queue.getId(), nameProperty);
				queuePropertyDto = new QueuePropertyDto(queueProperty);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(queuePropertyDto).build();
	}

	/**
	 * mise à jour de la propriété d'une file de traitement
	 * 
	 * @param nameQueue
	 * @param nameproperty
	 * @return noContent
	 */
	@PUT
	@Path("/{nameQueue}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateQueueProperty(@PathParam("nameQueue") String nameQueue,
			@PathParam("nameProperty") String nameproperty, QueuePropertyDto queuePropertyDto) {

		if (queuePropertyDto != null && nameQueue != null && nameproperty != null) {
			try {
				Queue queue = queueLocal.retrieveQueue(nameQueue);
				QueueProperty queueProperty = queuePropertyLocal.retrieveQueueProperty(queue.getId(), nameproperty);

				if (queuePropertyDto.getDescription() != null) {
					queueProperty.setDescription(queuePropertyDto.getDescription());
				}
				if (queuePropertyDto.getTxt() != null) {
					queueProperty.setValue(queuePropertyDto.getTxt());
				}
				if (queuePropertyDto.isDisplay() != null) {
					queueProperty.setDisplay(queuePropertyDto.isDisplay());
				}
				if (queuePropertyDto.isScriptExport() != null) {
					queueProperty.setScriptExport(queuePropertyDto.isScriptExport());
				}
				if (queuePropertyDto.isPersist() != null) {
					queueProperty.setPersist(queuePropertyDto.isPersist());
				}
				if (queuePropertyDto.isOverridable() != null) {
					queueProperty.setOverridable(queuePropertyDto.isOverridable());
				}
				if (queuePropertyDto.isJobExport() != null) {
					queueProperty.setJobExport(queuePropertyDto.isJobExport());
				}

				queuePropertyLocal.updateQueueProperty(queueProperty);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	/**
	 * 
	 * Création d'une file de traitement via une copie
	 * 
	 * @param queueDto
	 * @param nameQueue
	 * @return created
	 */
	@POST
	@Path("/{nameQueue}/model/{nameModel}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createQueue(@PathParam("nameQueue") String nameQueue, @PathParam("nameModel") String nameModel) {
		Queue queue = null;
		if ((nameModel != null && nameQueue != null)) {
			try {
				long idModel = queueLocal.retrieveQueue(nameModel).getId();
				queue = queueLocal.createQueueFromCopy(idModel, nameQueue, false);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String newNameQueue = String.valueOf(queue.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameQueue).build();

		return Response.created(uri).build();
	}

	/**
	 * 
	 * Mise à jour de la file de traitement
	 * 
	 * @param nameQueue
	 * @param queueDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameQueue}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateQueue(@PathParam("nameQueue") String nameQueue, QueueDto queueDto) {
		if (queueDto != null && nameQueue != null) {
			try {
				Queue queue = queueLocal.retrieveQueue(nameQueue);
				// configuration générale
				if (queueDto.getDescription() != null) {
					queue.setDescription(queueDto.getDescription());
				}
				// Exécution
				if (queueDto.getCommandProcess() != null) {
					queue.setCommandProcess(queueDto.getCommandProcess());
				}
				if (queueDto.getOnJobErrorCommand() != null) {
					queue.setOnJobErrorCommand(queueDto.getOnJobErrorCommand());
				}
				if (queueDto.getTimeOutExecJob() != null) {
					queue.setTimeOutExecJob(queueDto.getTimeOutExecJob());
				}
				if (queueDto.getMaxThread() != null) {
					queue.setMaxThread(queueDto.getMaxThread());
				}
				if (queueDto.getHoldDelay() != null) {
					queue.setHoldDelay(queueDto.getHoldDelay());
				}
				if (queueDto.getPriority() != null) {
					queue.setPriority(queueDto.getPriority());
				}
				if (queueDto.getAgentAdress() != null) {
					if (queueDto.getAgentAdress().isEmpty()) {
						queue.setAgent(null);
					} else {
						queue.setAgent(
								agentLocal.retrieveAgent(AgentFilter.all().byAdresse(queueDto.getAgentAdress())));
					}
				}
				// temps de conservation des jobs
				if (queueDto.getKeepLogJobOk() != null) {
					queue.setKeepLogJobOk(queueDto.getKeepLogJobOk());
				}
				if (queueDto.getDelayPurgeJobError() != null) {
					queue.setDelayPurgeJobError(queueDto.getDelayPurgeJobError());
				}
				if (queueDto.getDelayPurgeJobOk() != null) {
					queue.setDelayPurgeJobOk(queueDto.getDelayPurgeJobOk());
				}
				// Autres
				if (queueDto.getPauseIfEmpty() != null) {
					queue.setPauseIfEmpty(queueDto.getPauseIfEmpty());
				}
				if (queueDto.getAccounting() != null) {
					queue.setAccounting(queueDto.getAccounting());
				}
				// périphérique
				if (queueDto.getUseDefaultUserDevice() != null) {
					queue.setUseDefaultUserDevice(queueDto.getUseDefaultUserDevice());
				}
				if (queueDto.getDefaultDeviceName() != null) {
					if (queueDto.getDefaultDeviceName().isEmpty()) {
						queue.setDefaultDevice(null);
					} else {
						queue.setDefaultDevice(deviceLocal.retrieveDevice(queueDto.getDefaultDeviceName()));
					}
				}
				if (queueDto.isUseDeviceRouting() != null) {
					queue.setUseDeviceRouting(queueDto.isUseDeviceRouting());
				}

				queueLocal.updateQueue(queue);
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
	 * Mise à jour du statut de la file de traitement
	 * 
	 * @param nameQueue
	 * @param queueStatusDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameQueue}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateQueueStatus(@PathParam("nameQueue") String nameQueue, QueueStatusDto queueStatusDto) {
		if (queueStatusDto != null) {
			try {
				Queue queue = queueLocal.retrieveQueue(nameQueue);
				if (queueStatusDto.getStatus() != null && !queueStatusDto.getStatus().isEmpty()) {
					QueueStatusType newQueueStatus = QueueStatusType.valueOf(queueStatusDto.getStatus());
					queueLocal.setStatus(queue.getId(), newQueueStatus, queueStatusDto.getStatusDesc());
				} else {
					queueLocal.setStatus(queue.getId(), queue.getStatus(), queueStatusDto.getStatusDesc());
				}
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}
}
