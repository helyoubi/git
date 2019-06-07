package fr.datasyscom.scopiom.rest.property;

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

import fr.datasyscom.pome.ejbentity.Property;
import fr.datasyscom.pome.ejbentity.filter.PropertyFilter;
import fr.datasyscom.pome.ejbsession.property.PropertyManagerLocal;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.PropertyDto;

@Path("/properties")
public class PropertyRestWS {

	@EJB
	PropertyManagerLocal propertyLocal;

	@Context
	UriInfo uriInfo;

	/**
	 * Retourne la propriété par son nom
	 * 
	 * @param nameProperty
	 * @return ok
	 */
	@GET
	@Path("/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveProperty(@PathParam("nameProperty") String nameProperty) {
		PropertyDto propertyDto = null;
		try {
			Property property = propertyLocal.retrieveProperty(nameProperty);
			propertyDto = new PropertyDto(property);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(propertyDto).build();
	}

	/**
	 * Renvoie la liste de toutes les propriétés
	 * 
	 * @param idProperty
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllProperties(@QueryParam("id") long idProperty) {

		List<PropertyDto> properties = new ArrayList<PropertyDto>();
		if (idProperty != 0) {
			try {
				properties.add(new PropertyDto(propertyLocal.retrieveProperty(idProperty)));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Property> propertieList = propertyLocal.retrieveListProperty(PropertyFilter.all());
			for (Property property : propertieList) {
				properties.add(new PropertyDto(property));
			}
		}

		GenericEntity<List<PropertyDto>> listRestproperties = new GenericEntity<List<PropertyDto>>(properties) {
		};
		return Response.ok(listRestproperties).build();
	}

	/**
	 * 
	 * Suppression d'une propriété par son nom
	 * 
	 * @param nameProperty
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteProperty(@PathParam("nameProperty") String nameProperty) {
		try {
			Property property = propertyLocal.retrieveProperty(nameProperty);
			if (!property.isMandatory()) {
				propertyLocal.deleteProperty(property.getId());
			} else {
				return Response.status(Response.Status.CONFLICT).entity("Impossible de supprimer la propriété").build();
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Création d'une propriété
	 * 
	 * @param propertyDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createProperty(PropertyDto propertyDto) {

		Property property;
		if (propertyDto != null) {
			try {
				property = new Property();
				if (propertyDto.getName() != null) {
					property.setName(propertyDto.getName());
				}
				if (propertyDto.getDescription() != null) {
					property.setDescription(propertyDto.getDescription());
				}
				if (propertyDto.getTxt() != null) {
					property.setValue(propertyDto.getTxt());
				}
				if (propertyDto.isScriptExport()) {
					property.setScriptExport(propertyDto.isScriptExport());
				}
				if (propertyDto.isOverridable() != null) {
					property.setOverridable(propertyDto.isOverridable());
				}
				property = propertyLocal.createProperty(property);
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newProperty = String.valueOf(property.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newProperty).build();

		return Response.created(uri).build();

	}

	/**
	 * mise à jour de la propriété
	 * 
	 * @param nameproperty
	 * @param propertyDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateProperty(@PathParam("nameProperty") String nameproperty, PropertyDto propertyDto) {
		if (nameproperty == null && propertyDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			Property property = propertyLocal.retrieveProperty(nameproperty);
			if (propertyDto.getTxt() != null) {
				property.setValue(propertyDto.getTxt());
			}
			if (!property.isMandatory()) {
				if (propertyDto.getDescription() != null) {
					property.setDescription(propertyDto.getDescription());
				}
				if (propertyDto.isScriptExport() != null) {
					property.setScriptExport(propertyDto.isScriptExport());
				}
				if (propertyDto.isOverridable() != null) {
					property.setOverridable(propertyDto.isOverridable());
				}
			}
			propertyLocal.updateProperty(property);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

}
