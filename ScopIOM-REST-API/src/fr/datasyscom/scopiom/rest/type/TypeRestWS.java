package fr.datasyscom.scopiom.rest.type;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import fr.datasyscom.pome.ejbentity.Extension;
import fr.datasyscom.pome.ejbentity.Type;
import fr.datasyscom.pome.ejbentity.TypeMime;
import fr.datasyscom.pome.ejbsession.type.TypeManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.TypeDto;

@Path("/types")
public class TypeRestWS {
	@EJB
	TypeManagerLocal typeLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * Récupération du type par son nom
	 * 
	 * @param nameType
	 * @return ok
	 */
	@GET
	@Path("/{nameType}")
	@Produces({ "application/json", "application/xml" })
	public Response retrieveTypeByName(@PathParam("nameType") String nameType) {
		TypeDto typeDto = null;
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			typeDto = new TypeDto(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(typeDto).build();
	}

	/**
	 * Récupération du type par défaut
	 * 
	 * @return ok
	 */
	@GET
	@Path("/default")
	@Produces({ "application/json", "application/xml" })
	public Response retrieveDefaultType() {
		TypeDto typeDto = null;
		try {
			Type type = typeLocal.retrieveDefaultType();
			typeDto = new TypeDto(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(typeDto).build();
	}

	/**
	 * Récupération du type par extension
	 * 
	 * 
	 * @param extention
	 * @return ok
	 */
	@GET
	@Path("/extension")
	@Produces({ "application/json", "application/xml" })
	public Response retrieveTypeByExtension(@QueryParam("value") String extension) {
		TypeDto typeDto = null;
		try {
			Type type = typeLocal.retrieveTypeByExtension(extension);
			typeDto = new TypeDto(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(typeDto).build();
	}

	/**
	 * Récupération du type par type mime
	 * 
	 * @param typeMime
	 * @return ok
	 */
	@GET
	@Path("/typeMime")
	@Produces({ "application/json", "application/xml" })
	public Response retrieveTypeByTypeMime(@QueryParam("value") String typeMime) {
		TypeDto typeDto = null;
		try {
			Type type = typeLocal.retrieveTypeByTypeMime(typeMime);
			typeDto = new TypeDto(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(typeDto).build();
	}

	/**
	 * Récupération de la liste des types ou type par id
	 * 
	 * @param idType
	 * @return ok
	 */
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response retrieveAllTypes(@QueryParam("id") long idType) {
		List<TypeDto> typeDtos = new ArrayList<TypeDto>();
		if (idType != 0L) {
			try {
				Type type = typeLocal.retrieveType(idType);
				typeDtos.add(new TypeDto(type));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Type> listTypes = typeLocal.retrieveAllType();
			for (Type type : listTypes) {
				typeDtos.add(new TypeDto(type));
			}
		}
		GenericEntity<List<TypeDto>> listRestTypes = new GenericEntity<List<TypeDto>>(typeDtos) {
		};

		return Response.ok(listRestTypes).build();
	}

	/**
	 * Suppression du type
	 * 
	 * @param nameType
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameType}")
	@Produces({ "application/json", "application/xml" })
	public Response deleteType(@PathParam("nameType") String nameType) {
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			if (!type.isDefaut()) {
				typeLocal.deleteType(type.getId());
			} else {
				return Response.status(Response.Status.CONFLICT).entity("Default Type : Cannot be deleted").build();
			}
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * 
	 * Création du type
	 * 
	 * @param typeDto
	 * @return created
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response createType(TypeDto typeDto) {
		Type type;
		if ((typeDto != null) && (typeDto.getName() != null) && (!typeDto.getName().isEmpty())
				&& (typeDto.getDefaultExtension() != null) && (!typeDto.getDefaultExtension().isEmpty())
				&& (typeDto.getDefaultMime() != null) && (!typeDto.getDefaultMime().isEmpty())) {
			try {
				type = new Type();
				type.setName(typeDto.getName());
				type = typeLocal.createType(type, typeDto.getDefaultExtension(), typeDto.getDefaultMime());
			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String newType = String.valueOf(type.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newType).build(new Object[0]);

		return Response.created(uri).build();
	}

	/**
	 * 
	 * mise à jour du type
	 * 
	 * @param nameType
	 * @param typeDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameType}")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updateType(@PathParam("nameType") String nameType, TypeDto typeDto) {
		if (typeDto != null) {
			try {
				Type type = typeLocal.retrieveTypeByName(nameType);

				if ((typeDto.getAnalyseFluxCmd() != null) && (!typeDto.getAnalyseFluxCmd().isEmpty())) {
					type.setAnalayseFluxCmd(typeDto.getAnalyseFluxCmd());
				}
				if ((typeDto.getExtractPageCmd() != null) && (!typeDto.getExtractPageCmd().isEmpty())) {
					type.setExtractPageCmd(typeDto.getExtractPageCmd());
				}
				if ((typeDto.getTimeoutAnalyseFlux() != null) && (typeDto.getTimeoutAnalyseFlux().intValue() > 0)) {
					type.setTimeoutAnalyseFlux(typeDto.getTimeoutAnalyseFlux());
				}
				if (typeDto.getDefaut() != null) {
					type.setDefaut(typeDto.getDefaut().booleanValue());
				}

				if ((typeDto.getDefaultExtension() != null) && (!typeDto.getDefaultExtension().isEmpty())) {
					String defaultExt = typeDto.getDefaultExtension();
					for (Extension ext : type.getExtensions()) {
						if (ext.getValue().equalsIgnoreCase(defaultExt)) {
							Extension oldDefault = type.getDefaultExtension();
							if (oldDefault != null) {
								oldDefault.setDefaut(false);
							}
							ext.setDefaut(true);
							break;
						}
					}
				}
				if ((typeDto.getDefaultMime() != null) && (!typeDto.getDefaultMime().isEmpty())) {
					String defaultMime = typeDto.getDefaultMime();
					for (TypeMime typeMime : type.getMimeTypes()) {
						if (typeMime.getValue().equalsIgnoreCase(defaultMime)) {
							TypeMime oldDefaultMime = type.getDefaultTypeMime();
							if (oldDefaultMime != null) {
								oldDefaultMime.setDefaut(false);
							}
							typeMime.setDefaut(true);
							break;
						}
					}
				}

				typeLocal.updateType(type);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Retire une extension du type
	 * 
	 * @param nameType
	 * @param nameExtension
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameType}/extension/{nameExtension}")
	@Produces({ "application/json", "application/xml" })
	public Response removeExtensionFromType(@PathParam("nameType") String nameType,
			@PathParam("nameExtension") String nameExtension) {
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			List<Extension> listTypeExtensions = new ArrayList<Extension>();
			for (Extension ext : type.getExtensions()) {
				if (ext.getValue().equalsIgnoreCase(nameExtension)) {
					if (ext.isDefaut()) {
						return Response.status(Response.Status.CONFLICT).entity("Default Extension").build();
					}
				} else {
					listTypeExtensions.add(ext);
				}
			}
			type.setExtensions(listTypeExtensions);
			typeLocal.updateType(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * 
	 * Ajout d'une extension au type
	 * 
	 * @param nameType
	 * @param nameExtension
	 * @return ok
	 */
	@POST
	@Path("/{nameType}/extension/{nameExtension}")
	@Produces({ "application/json", "application/xml" })
	public Response addExtensionFromType(@PathParam("nameType") String nameType,
			@PathParam("nameExtension") String nameExtension) {
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			for (Extension extension : type.getExtensions()) {
				if (extension.getValue().equalsIgnoreCase(nameExtension)) {
					throw new ValidationException("Extension already exist");
				}
			}
			Extension newExtension = new Extension();
			newExtension.setValue(nameExtension);
			type.getExtensions().add(newExtension);
			typeLocal.updateType(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	/**
	 * 
	 * Retire un type mime du type
	 * 
	 * @param nameType
	 * @param nameTypeMime
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameType}/typeMime/{nameTypeMime: .+}")
	@Produces({ "application/json", "application/xml" })
	public Response removeTypeMimeFromType(@PathParam("nameType") String nameType,
			@PathParam("nameTypeMime") String nameTypeMime) {
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			List<TypeMime> listTypeMimes = new ArrayList<>();
			for (TypeMime typeMime : type.getMimeTypes()) {
				if (typeMime.getValue().equalsIgnoreCase(nameTypeMime)) {
					if (typeMime.isDefaut()) {
						return Response.status(Response.Status.CONFLICT).entity("Default Mime Type").build();
					}
				} else {
					listTypeMimes.add(typeMime);
				}
			}
			type.setMimeTypes(listTypeMimes);
			typeLocal.updateType(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * ajout du type mime au type
	 * 
	 * @param nameType
	 * @param nameTypeMime
	 * @return ok
	 */
	@POST
	@Path("/{nameType}/typeMime/{nameTypeMime: .+}")
	@Produces({ "application/json", "application/xml" })
	public Response addTypeMimeFromType(@PathParam("nameType") String nameType,
			@PathParam("nameTypeMime") String nameTypeMime) {
		try {
			Type type = typeLocal.retrieveTypeByName(nameType);
			for (TypeMime typeMime : type.getMimeTypes()) {
				if (typeMime.getValue().equalsIgnoreCase(nameTypeMime)) {
					throw new ValidationException("Extension already exist");
				}
			}
			TypeMime newTypeMime = new TypeMime();
			newTypeMime.setValue(nameTypeMime);
			type.getMimeTypes().add(newTypeMime);
			typeLocal.updateType(type);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
}