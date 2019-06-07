package fr.datasyscom.scopiom.rest.device;

import java.net.URI;
import java.text.ParseException;
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
import org.quartz.SchedulerException;

import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.Device.DeviceStatusType;
import fr.datasyscom.pome.ejbentity.DeviceProperty;
import fr.datasyscom.pome.ejbentity.DeviceSnmp;
import fr.datasyscom.pome.ejbentity.Groupe;
import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.filter.JobFilter;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.device.property.DevicePropertyManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.pome.ejbsession.media.MediaManagerLocal;
import fr.datasyscom.pome.ejbsession.services.scansnmp.ScanSnmpLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.DevicePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceSnmpDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceStatusDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;

@Path("/devices")
public class DeviceRestWS {

	@EJB
	DeviceManagerLocal deviceLocal;
	@EJB
	MediaManagerLocal mediaManagerLocal;
	@EJB
	ScanSnmpLocal scanSnmp;
	@EJB
	GroupeManagerLocal grpLocal;
	@EJB
	JobManagerLocal jobManagerLocal;
	@EJB
	DevicePropertyManagerLocal devicePropertyLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupérer un périphérique par son nom
	 * 
	 * @param nameDevice
	 * @return ok
	 */
	@GET
	@Path("/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveDevice(@PathParam("nameDevice") String nameDevice) {
		DeviceDto deviceDto = null;
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			deviceDto = new DeviceDto(device);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(deviceDto).build();
	}

	/**
	 * 
	 * Renvoie la liste des périphériques ou le device par son id
	 * 
	 * @param idDevice
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllDevices(@QueryParam("id") long idDevice) {
		List<DeviceDto> deviceDtos = new ArrayList<DeviceDto>();
		if (idDevice != 0) {
			try {
				deviceDtos.add(new DeviceDto(deviceLocal.retrieveDevice(idDevice)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Device> devicesList = deviceLocal.retrieveAllDevice();
			DeviceDto deviceDto = null;
			for (Device device : devicesList) {
				deviceDto = new DeviceDto(device);
				deviceDtos.add(deviceDto);
			}
		}

		GenericEntity<List<DeviceDto>> listRestDevices = new GenericEntity<List<DeviceDto>>(deviceDtos) {
		};
		return Response.ok(listRestDevices).build();
	}

	/**
	 * 
	 * Supprime un périphérique par son nom
	 * 
	 * @param nameDevice
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameDevice}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteDevice(@PathParam("nameDevice") String nameDevice) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			deviceLocal.deleteDevice(device.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Ajout d'un groupe au périphérique
	 * 
	 * @param nameDevice
	 * @param nameGroup
	 * @return ok
	 */
	@POST
	@Path("/{nameDevice}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addGroupFromDevice(@PathParam("nameDevice") String nameDevice,
			@PathParam("nameGroup") String nameGroup) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			Set<Long> grpsIds = device.getGroupes().stream().map(u -> u.getId()).collect(Collectors.toSet());
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			if (grpsIds.add(groupe.getId())) {
				long[] tabGroup = ArrayUtils.toPrimitive(grpsIds.toArray(new Long[0]));
				deviceLocal.setGroupes(device.getId(), tabGroup, device.isPublicAccess());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * 
	 * Retire un groupe du périphérique
	 * 
	 * 
	 * @param nameDevice
	 * @param nameGroup
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameDevice}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeGroupFromDevice(@PathParam("nameDevice") String nameDevice,
			@PathParam("nameGroup") String nameGroup) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			List<Groupe> listGrps = device.getGroupes();
			List<Long> idGrpsList = new ArrayList<Long>();
			Groupe grouperetrieved = grpLocal.retrieveGroupeByName(nameGroup);
			for (Groupe groupe : listGrps) {
				if (!(groupe.getName().equals(grouperetrieved.getName()))) {
					idGrpsList.add(groupe.getId());
				}
			}
			long[] tabGroups = ArrayUtils.toPrimitive(idGrpsList.toArray(new Long[0]));
			deviceLocal.setGroupes(device.getId(), tabGroups, device.isPublicAccess());
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Création du device retourne ,Conflict en cas ou la syntaxe est incorrecte bad
	 * request en cas où le device ou son nom ne sont pas créés retourne 201 avec un
	 * lien générer a l en-tete de la réponse http contenant le nom du device
	 * 
	 * @param deviceDto
	 * @return created
	 */
	@POST
	@Path("/{nameDevice}/model/{deviceModel}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createDevice(@PathParam("nameDevice") String newDevice,
			@PathParam("deviceModel") String deviceModel) {
		Device device = null;
		if ((newDevice != null && deviceModel != null)) {
			try {
				long deviceId = deviceLocal.retrieveDevice(deviceModel).getId();
				device = deviceLocal.createDeviceFromCopy(deviceId, newDevice);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String newNameDevice = String.valueOf(device.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameDevice).build();

		return Response.created(uri).build();

	}

	/**
	 * renvoie les groups attachés au périphérique
	 * 
	 * @param nameDevice
	 * @return ok
	 */
	@GET
	@Path("/{nameDevice}/groups")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveGroupsFromDevice(@PathParam("nameDevice") String nameDevice) {
		List<GroupeDto> groupeDtos = new ArrayList<GroupeDto>();
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			List<Groupe> groupeList = device.getGroupes();
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
	 * Retourne la liste des jobs attachés à un périphérique par statut
	 * 
	 * @param nameDevice
	 * @param status
	 * @return OK
	 */
	@GET
	@Path("/{nameDevice}/jobs/status/{status}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveJobsFromDeviceByStatus(@PathParam("nameDevice") String nameDevice,
			@PathParam("status") String status) {
		if (nameDevice == null || status == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		List<JobDto> jobsDtos = new ArrayList<JobDto>();
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			List<Job> deviceJobList = jobManagerLocal.retrieveListJob(
					JobFilter.display().byDeviceId(device.getId()).byStatus(JobStatusType.valueOf(status)));
			for (Job job : deviceJobList) {
				jobsDtos.add(new JobDto(job));
			}
			GenericEntity<List<JobDto>> listRestJobDevices = new GenericEntity<List<JobDto>>(jobsDtos) {
			};
			return Response.ok(listRestJobDevices).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
	}

	/**
	 * Retourne la liste des jobs attachés à un device
	 * 
	 * @param nameDevice
	 * @return OK
	 */
	@GET
	@Path("/{nameDevice}/jobs")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllJobsFromDevice(@PathParam("nameDevice") String nameDevice) {
		return retrieveJobsFromDeviceByStatus(nameDevice, JobStatusType.ALL.name());
	}

	/**
	 * creation de la propriété du device
	 * 
	 * @param nameDevice
	 * @param devicePropertyDto
	 * @return created
	 */
	@POST
	@Path("/{nameDevice}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createDeviceProperty(@PathParam("nameDevice") String nameDevice,
			DevicePropertyDto devicePropertyDto) {
		DeviceProperty deviceProperty = null;
		try {
			deviceProperty = new DeviceProperty();
			if (devicePropertyDto.getName() != null) {
				deviceProperty.setName(devicePropertyDto.getName());
			}
			if (devicePropertyDto.getDescription() != null) {
				deviceProperty.setDescription(devicePropertyDto.getDescription());
			}
			if (devicePropertyDto.getTxt() != null) {
				deviceProperty.setValue(devicePropertyDto.getTxt());
			}
			if (devicePropertyDto.isDisplay() != null) {
				deviceProperty.setDisplay(devicePropertyDto.isDisplay());
			}
			if (devicePropertyDto.isScriptExport() != null) {
				deviceProperty.setScriptExport(devicePropertyDto.isScriptExport());
			}
			Device device = deviceLocal.retrieveDevice(nameDevice);
			deviceProperty = devicePropertyLocal.createDeviceProperty(deviceProperty, device.getId());
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		String newNameProperyDevice = String.valueOf(deviceProperty.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameProperyDevice).build();

		return Response.created(uri).build();
	}

	/**
	 * mise à jour de la propriété du périphérique
	 * 
	 * @param nameDevice
	 * @param nameproperty
	 * @param devicePropertyDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameDevice}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDeviceProperty(@PathParam("nameDevice") String nameDevice,
			@PathParam("nameProperty") String nameproperty, DevicePropertyDto devicePropertyDto) {
		if (devicePropertyDto != null && nameDevice != null && nameproperty != null) {
			try {
				Device device = deviceLocal.retrieveDevice(nameDevice);
				DeviceProperty deviceProperty = devicePropertyLocal.retrieveDeviceProperty(nameproperty,
						device.getId());
				if (devicePropertyDto.getDescription() != null) {
					deviceProperty.setDescription(devicePropertyDto.getDescription());
				}
				if (devicePropertyDto.getTxt() != null) {
					deviceProperty.setValue(devicePropertyDto.getTxt());
				}
				if (devicePropertyDto.isDisplay() != null) {
					deviceProperty.setDisplay(devicePropertyDto.isDisplay());
				}
				if (devicePropertyDto.isScriptExport() != null) {
					deviceProperty.setScriptExport(devicePropertyDto.isScriptExport());
				}
				devicePropertyLocal.updateDeviceProperty(deviceProperty);
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
	 * Supprime une propriété du périphérique
	 * 
	 * @param nameDevice
	 * @param nameProperty
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameDevice}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteDeviceProperty(@PathParam("nameDevice") String nameDevice,
			@PathParam("nameProperty") String nameProperty) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			DeviceProperty deviceProperty = devicePropertyLocal.retrieveDeviceProperty(nameProperty, device.getId());
			devicePropertyLocal.deleteDeviceProperty(deviceProperty.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * update le device par son nom,not found en cas ou le nom du device n'existe
	 * pas bad request syntaxe device incorrecte
	 * 
	 * @param nameDevice
	 * @param deviceDto
	 * @return noContent
	 */
	@PUT
	@Path("/{name}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDevice(@PathParam("name") String nameDevice, DeviceDto deviceDto) {
		if (deviceDto != null && nameDevice != null) {
			try {
				Device device = deviceLocal.retrieveDevice(nameDevice);
				if (deviceDto.getDescription() != null) {
					device.setDescription(deviceDto.getDescription());
				}
				if (deviceDto.getComment() != null) {
					device.setComment(deviceDto.getComment());
				}
				if (deviceDto.getMaxThread() != null && deviceDto.getMaxThread() >= 1) {
					device.setMaxThread(deviceDto.getMaxThread());
				}
				if (deviceDto.getSpeed() != null && deviceDto.getSpeed() >= 0) {
					device.setSpeed(deviceDto.getSpeed());
				}
				if (deviceDto.getUrlAdmin() != null) {
					device.setUrlAdmin(deviceDto.getUrlAdmin());
				}
				if (deviceDto.getTriggerCmdChangeStatus() != null) {
					device.setTriggerCmdChangeStatus(deviceDto.getTriggerCmdChangeStatus());
				}
				if (deviceDto.getUseMedia() != null) {
					device.setUseMedia(deviceDto.getUseMedia());
				}
				if (deviceDto.getMediaName() != null) {
					if (deviceDto.getMediaName().length() == 0) {
						device.setMedia(null);
					} else {
						device.setMedia(mediaManagerLocal.retrieveMediaByName(deviceDto.getMediaName()));
					}
				}

				deviceLocal.updateDevice(device);

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
	 * Retourne le status du périphérique
	 * 
	 * @param nameDevice
	 * @return OK
	 */
	@GET
	@Path("/{nameDevice}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveDeviceStatus(@PathParam("nameDevice") String nameDevice) {
		DeviceStatusDto deviceStatusDto;
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			deviceStatusDto = new DeviceStatusDto(device);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(deviceStatusDto).build();
	}

	/**
	 * 
	 * Retourne la configuration SNMP du périphérique
	 * 
	 * @param nameDevice
	 * @return OK
	 */
	@GET
	@Path("/{nameDevice}/snmp")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retriveDeviceConfigSnmp(@PathParam("nameDevice") String nameDevice) {
		DeviceSnmpDto deviceSnmpDto;
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			DeviceSnmp deviceSnmp = device.getSnmpModule();
			deviceSnmp = scanSnmp.retrieveSnmp(deviceSnmp.getId());
			deviceSnmpDto = new DeviceSnmpDto(deviceSnmp);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(deviceSnmpDto).build();
	}

	/**
	 * renvoie la propriété d'un device
	 * 
	 * 
	 * @param nameDevice
	 * @param nameProperty
	 * @return ok
	 */
	@GET
	@Path("/{nameDevice}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveDeviceProperty(@PathParam("nameDevice") String nameDevice,
			@PathParam("nameProperty") String nameProperty) {
		DevicePropertyDto devicePropertyDto = null;
		try {
			if (nameDevice != null && nameProperty != null) {
				Device device = deviceLocal.retrieveDevice(nameDevice);
				DeviceProperty deviceProperty = devicePropertyLocal.retrieveDeviceProperty(nameProperty,
						device.getId());
				devicePropertyDto = new DevicePropertyDto(deviceProperty);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(devicePropertyDto).build();
	}

	/**
	 * renvoie la liste des propriétés du device
	 * 
	 * 
	 * @param nameDevice
	 * @return ok
	 */
	@GET
	@Path("/{nameDevice}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllDeviceProperty(@PathParam("nameDevice") String nameDevice) {
		List<DevicePropertyDto> devicePropertyDtos = new ArrayList<DevicePropertyDto>();
		if (nameDevice == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			List<DeviceProperty> listDeviceProperties = devicePropertyLocal.retrieveAllDeviceProperty(device.getId());
			for (DeviceProperty deviceProperty : listDeviceProperties) {
				devicePropertyDtos.add(new DevicePropertyDto(deviceProperty));
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		GenericEntity<List<DevicePropertyDto>> listRestDeviceproperties = new GenericEntity<List<DevicePropertyDto>>(
				devicePropertyDtos) {
		};
		return Response.ok(listRestDeviceproperties).build();
	}

	/**
	 * 
	 * démarre le service de configuration snmp du device
	 * 
	 * @param nameDevice
	 * @return OK
	 */
	@POST
	@Path("/{nameDevice}/snmp/start")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response startSnmp(@PathParam("nameDevice") String nameDevice) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			DeviceSnmp deviceSnmp = device.getSnmpModule();
			scanSnmp.snmpActivate(deviceSnmp.getId(), true);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (SchedulerException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ParseException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * 
	 * stop le service de configuration snmp du device
	 * 
	 * @param nameDevice
	 * @return OK
	 */
	@POST
	@Path("/{nameDevice}/snmp/stop")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response stopSnmp(@PathParam("nameDevice") String nameDevice) {
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			DeviceSnmp deviceSnmp = device.getSnmpModule();
			scanSnmp.snmpActivate(deviceSnmp.getId(), false);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (SchedulerException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ParseException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * 
	 * Mise à jour du statut du périphérique
	 * 
	 * @param nameDevice
	 * @param deviceStatusDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameDevice}/status")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDeviceStatus(@PathParam("nameDevice") String nameDevice, DeviceStatusDto deviceStatusDto) {
		if (deviceStatusDto != null) {
			try {
				Device device = deviceLocal.retrieveDevice(nameDevice);
				if (deviceStatusDto.getStatus() != null && !deviceStatusDto.getStatus().isEmpty()) {
					DeviceStatusType deviceStatusType = DeviceStatusType.valueOf(deviceStatusDto.getStatus());
					deviceLocal.setStatus(device.getId(), deviceStatusType, deviceStatusDto.getStatusDesc());
				} else {
					deviceLocal.setStatus(device.getId(), device.getStatus(), deviceStatusDto.getStatusDesc());
				}
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Mise à jour de la configuration SNMP
	 * 
	 * @param nameDevice
	 * @param deviceSnmpDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameDevice}/snmp")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateSnmpConfiguration(@PathParam("nameDevice") String nameDevice, DeviceSnmpDto deviceSnmpDto) {
		if (deviceSnmpDto == null && nameDevice == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Device device = deviceLocal.retrieveDevice(nameDevice);
			DeviceSnmp deviceSnmp = device.getSnmpModule();
			if (deviceSnmpDto.getDelai() != null) {
				deviceSnmp.setDelais(deviceSnmpDto.getDelai());
			}
			if (deviceSnmpDto.getCommandProcess() != null) {
				deviceSnmp.setCommandProcess(deviceSnmpDto.getCommandProcess());
			}
			if (deviceSnmpDto.getBeforeJob() != null) {
				deviceSnmp.setBeforeJob(deviceSnmpDto.getBeforeJob());
			}
			if (deviceSnmpDto.isOnlyOnError() != null) {
				deviceSnmp.setOnlyOnError(deviceSnmpDto.isOnlyOnError());
			}
			scanSnmp.updateSnmp(deviceSnmp);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

}
