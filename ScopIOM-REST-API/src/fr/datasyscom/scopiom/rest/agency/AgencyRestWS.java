package fr.datasyscom.scopiom.rest.agency;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Agency;
import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.DeviceRoutage;
import fr.datasyscom.pome.ejbsession.agency.AgencyManagerLocal;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.deviceroutage.DeviceRoutageManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.AgencyDto;

@Path("/agency")
public class AgencyRestWS {

	@EJB
	AgencyManagerLocal agencyLocal;

	@EJB
	DeviceManagerLocal deviceLocal;

	@EJB
	GroupeManagerLocal groupeLocal;

	@EJB
	DeviceRoutageManagerLocal deviceRoutageLocal;

	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupérer une agence par son nom
	 * 
	 * @param nameAgency
	 * @return ok
	 */
	@GET
	@Path("/{nameAgency}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAgency(@PathParam("nameAgency") String nameAgency) {
		AgencyDto agencyDto = null;
		try {
			Agency agency = agencyLocal.retrieveAgency(nameAgency);
			DeviceRoutage deviceRoutage = deviceRoutageLocal.retrieveAgencyDefaultRoutage(agency.getName());
			agencyDto = new AgencyDto(agency);
			if (deviceRoutage != null && !deviceRoutage.getPrimaryDevice().getName().isEmpty()) {
				agencyDto.setDevice(deviceRoutage.getPrimaryDevice().getName());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(agencyDto).build();
	}

	/**
	 * 
	 * Renvoie la liste des agences ou l'agence par son id
	 * 
	 * @param idAgency
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllAgencies(@QueryParam("id") long idAgency) {

		List<AgencyDto> agencyListDto = new ArrayList<AgencyDto>();
		AgencyDto agencyDto = null;
		if (idAgency != 0) {
			try {
				Agency agence = agencyLocal.retrieveAgency(idAgency);
				agencyDto = new AgencyDto(agence);
				DeviceRoutage deviceRoutage = deviceRoutageLocal.retrieveAgencyDefaultRoutage(agence.getName());
				if (deviceRoutage != null && !deviceRoutage.getPrimaryDevice().getName().isEmpty()) {
					agencyDto.setDevice(deviceRoutage.getPrimaryDevice().getName());
				}
				agencyListDto.add(agencyDto);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Agency> allAgencies = agencyLocal.retrieveAllAgency();
			for (Agency agency : allAgencies) {
				agencyDto = new AgencyDto(agency);
				DeviceRoutage deviceRoutage = deviceRoutageLocal.retrieveAgencyDefaultRoutage(agency.getName());
				if (deviceRoutage != null && !deviceRoutage.getPrimaryDevice().getName().isEmpty()) {
					agencyDto.setDevice(deviceRoutage.getPrimaryDevice().getName());
				}
				agencyListDto.add(agencyDto);
			}
		}
		GenericEntity<List<AgencyDto>> listRestAgencies = new GenericEntity<List<AgencyDto>>(agencyListDto) {
		};
		return Response.ok(listRestAgencies).build();
	}

	/**
	 * 
	 * Supprime une agence par son nom
	 * 
	 * @param nameAgency
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameAgency}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteAgency(@PathParam("nameAgency") String nameAgency) {
		try {
			Agency agency = agencyLocal.retrieveAgency(nameAgency);
			agencyLocal.deleteAgency(agency.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * création d'une agence
	 * 
	 * @param agencyDto
	 * @param deviceName
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createAgency(AgencyDto agencyDto) {
		Agency agency;
		if (agencyDto != null) {
			try {
				agency = new Agency();
				if (agencyDto.getName() != null) {
					agency.setName(agencyDto.getName());
				}
				if (agencyDto.getDescription() != null) {
					agency.setDescription(agencyDto.getDescription());
				}
				if (agencyDto.getComment() != null) {
					agency.setComment(agencyDto.getComment());
				}
				if (agencyDto.getAdminGroup() != null) {
					if (agencyDto.getAdminGroup().length() != 0) {
						agency.setAdminGroup(groupeLocal.retrieveGroupeByName(agencyDto.getAdminGroup()));
					} else {
						agency.setAdminGroup(null);
					}
				}
				if (agencyDto.getUserGroup() != null) {
					if (agencyDto.getUserGroup().length() != 0) {
						agency.setAdminGroup(groupeLocal.retrieveGroupeByName(agencyDto.getUserGroup()));
					} else {
						agency.setAdminGroup(null);
					}
				}
				Device device = null;
				if (agencyDto.getDevice() != null && !agencyDto.getDevice().isEmpty()) {
					device = deviceLocal.retrieveDevice(agencyDto.getDevice());
				}
				agency = agencyLocal.createAgency(agency, device);
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			} catch (Exception e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newAgency = String.valueOf(agency.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newAgency).build();

		return Response.created(uri).build();
	}

	/**
	 * 
	 * Update agency by name
	 * 
	 * @param nameAgency
	 * @param agencyDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameAgency}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAgency(@PathParam("nameAgency") String nameAgency, AgencyDto agencyDto) {
		if (agencyDto != null) {
			try {
				Agency agency = agencyLocal.retrieveAgency(nameAgency);

				if (agencyDto.getDescription() != null) {
					agency.setDescription(agencyDto.getDescription());
				}
				if (agencyDto.getComment() != null) {
					agency.setComment(agencyDto.getComment());
				}
				if (agencyDto.getAdminGroup() != null) {
					if (agencyDto.getAdminGroup().length() != 0) {
						agency.setAdminGroup(groupeLocal.retrieveGroupeByName(agencyDto.getAdminGroup()));
					} else {
						agency.setAdminGroup(null);
					}
				}
				if (agencyDto.getUserGroup() != null) {
					if (agencyDto.getUserGroup().length() != 0) {
						agency.setUserGroup(groupeLocal.retrieveGroupeByName(agencyDto.getUserGroup()));
					} else {
						agency.setUserGroup(null);
					}
				}
				if (agencyDto.getDevice() != null && !agencyDto.getDevice().isEmpty()) {
					DeviceRoutage deviceRoutage = deviceRoutageLocal.retrieveAgencyDefaultRoutage(agency.getName());
					if (deviceRoutage != null && deviceRoutage.getPrimaryDevice() != null) {
						deviceRoutage.setPrimaryDevice(deviceLocal.retrieveDevice(agencyDto.getDevice()));
						deviceRoutageLocal.updateRoutage(deviceRoutage);
					}
				}
				agencyLocal.updateAgency(agency);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

}
