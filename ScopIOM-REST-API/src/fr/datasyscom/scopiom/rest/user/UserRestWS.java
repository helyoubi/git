package fr.datasyscom.scopiom.rest.user;

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
import fr.datasyscom.pome.ejbentity.User;
import fr.datasyscom.pome.ejbentity.User.UserProfil;
import fr.datasyscom.pome.ejbentity.UserProperty;
import fr.datasyscom.pome.ejbentity.filter.UserFilter;
import fr.datasyscom.pome.ejbsession.device.DeviceManagerLocal;
import fr.datasyscom.pome.ejbsession.groupe.GroupeManagerLocal;
import fr.datasyscom.pome.ejbsession.user.UserManagerLocal;
import fr.datasyscom.pome.ejbsession.user.property.UserPropertyManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.IdsDto;
import fr.datasyscom.scopiom.rest.pojo.UserDto;
import fr.datasyscom.scopiom.rest.pojo.UserPropertyDto;

@Path("/users")
public class UserRestWS {

	@EJB
	UserManagerLocal userLocal;
	@EJB
	UserPropertyManagerLocal userPropertyLocal;
	@EJB
	GroupeManagerLocal grpLocal;
	@EJB
	DeviceManagerLocal deviceLocal;
	@Context
	UriInfo uriInfo;

	/**
	 * Ajout d'un groupe à l'utilisateur
	 * 
	 * @param loginUser
	 * @param nameGroup
	 * @return ok
	 */
	@POST
	@Path("/{loginUser}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addGroupFromUser(@PathParam("loginUser") String loginUser,
			@PathParam("nameGroup") String nameGroup) {
		try {
			User user = userLocal.retrieveUser(loginUser);
			Set<Long> grpsIds = user.getGroupes().stream().map(u -> u.getId()).collect(Collectors.toSet());
			Groupe groupe = grpLocal.retrieveGroupeByName(nameGroup);
			if (grpsIds.add(groupe.getId())) {
				long[] tabGroup = ArrayUtils.toPrimitive(grpsIds.toArray(new Long[0]));
				userLocal.setGroupes(user.getLogin(), tabGroup);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok().build();
	}

	/**
	 * 
	 * Retire un groupe de l'utilisateur
	 * 
	 * 
	 * @param loginUser
	 * @param nameGroup
	 * @return noContent
	 */
	@DELETE
	@Path("/{loginUser}/groups/{nameGroup}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response removeGroupFromUser(@PathParam("loginUser") String loginUser,
			@PathParam("nameGroup") String nameGroup) {
		try {
			User user = userLocal.retrieveUser(loginUser);
			List<Groupe> listGrps = user.getGroupes();
			List<Long> idGrpsList = new ArrayList<Long>();
			Groupe grouperetrieved = grpLocal.retrieveGroupeByName(nameGroup);
			for (Groupe groupe : listGrps) {
				if (!(groupe.getName().equals(grouperetrieved.getName()))) {
					idGrpsList.add(groupe.getId());
				}
			}
			long[] tabGroups = ArrayUtils.toPrimitive(idGrpsList.toArray(new Long[0]));
			userLocal.setGroupes(user.getLogin(), tabGroups);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Récupération d'un utilisateur par son login
	 * 
	 * @param loginUser
	 * @return ok
	 */
	@GET
	@Path("/{loginUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveUser(@PathParam("loginUser") String loginUser) {
		UserDto userDto = null;
		try {
			User user = userLocal.retrieveUser(loginUser);
			userDto = new UserDto(user);
			userDto.setPassword(null);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(userDto).build();
	}

	/**
	 * renvoie la propriété d'un utilisateur
	 * 
	 * 
	 * @param loginUser
	 * @param nameProperty
	 * @return ok
	 */
	@GET
	@Path("/{loginUser}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveUserProperty(@PathParam("loginUser") String loginUser,
			@PathParam("nameProperty") String nameProperty) {
		UserPropertyDto userPropertyDto = null;
		try {
			if (loginUser != null && nameProperty != null) {
				User user = userLocal.retrieveUser(loginUser);
				UserProperty userProperty = userPropertyLocal.retrieveUserProperty(nameProperty, user.getLogin());
				userPropertyDto = new UserPropertyDto(userProperty);
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(userPropertyDto).build();
	}

	/**
	 * renvoie la liste des propriétés d'un utilisateur
	 * 
	 * 
	 * @param loginUser
	 * @return ok
	 */
	@GET
	@Path("/{loginUser}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllUserProperty(@PathParam("loginUser") String loginUser) {
		if (loginUser == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		List<UserPropertyDto> userPropertyDtos = new ArrayList<UserPropertyDto>();
		try {
			User user = userLocal.retrieveUser(loginUser);
			List<UserProperty> listUser = userPropertyLocal.retrieveAllUserProperty(user.getLogin());
			for (UserProperty userProperty : listUser) {
				userPropertyDtos.add(new UserPropertyDto(userProperty));
			}
			GenericEntity<List<UserPropertyDto>> listRestUserproperties = new GenericEntity<List<UserPropertyDto>>(
					userPropertyDtos) {
			};
			return Response.ok(listRestUserproperties).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

	}

	/**
	 * renvoie les groups attachés a l'utilisateur
	 * 
	 * @param loginUser
	 * @return ok
	 */
	@GET
	@Path("/{loginUser}/groups")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveGroupsFromUser(@PathParam("loginUser") String loginUser) {
		List<GroupeDto> groupeDtos = new ArrayList<GroupeDto>();
		try {
			User user = userLocal.retrieveUser(loginUser);
			List<Groupe> groupeList = user.getGroupes();
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
	 * 
	 * Supprime un utilisateur par son login
	 * 
	 * @param loginUser
	 * @return noContent
	 */
	@DELETE
	@Path("/{loginUser}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteUser(@PathParam("loginUser") String loginUser) {
		try {
			User user = userLocal.retrieveUser(loginUser);
			userLocal.deleteUser(user.getLogin());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Supprime une propriété de l'utilisateur
	 * 
	 * @param loginUser
	 * @param nameProperty
	 * @return noContent
	 */
	@DELETE
	@Path("/{loginUser}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteUserProperty(@PathParam("loginUser") String loginUser,
			@PathParam("nameProperty") String nameProperty) {
		try {
			User user = userLocal.retrieveUser(loginUser);
			UserProperty userProperty = userPropertyLocal.retrieveUserProperty(nameProperty, user.getLogin());
			userPropertyLocal.deleteUserProperty(userProperty.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

	/**
	 * 
	 * Renvoie la liste des utilisateurs ou par profil
	 * 
	 * @param profilUser
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllUsers(@QueryParam("profil") String profilUser) {
		List<UserDto> userDtos = new ArrayList<UserDto>();
		UserFilter userFilter;
		if (profilUser != null) {
			try {
				userFilter = UserFilter.all().byProfil(UserProfil.valueOf(profilUser));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			userFilter = UserFilter.all();
		}
		List<User> userList = userLocal.retrieveListUser(userFilter);
		UserDto userDto = null;
		for (User user : userList) {
			userDto = new UserDto(user);
			userDto.setPassword(null);
			userDtos.add(userDto);
		}
		GenericEntity<List<UserDto>> listRestUsers = new GenericEntity<List<UserDto>>(userDtos) {
		};
		return Response.ok(listRestUsers).build();
	}

	/**
	 * creation de la propriété de l'utilisateur
	 * 
	 * @param loginUser
	 * @param userPropertyDto
	 * @return noContent
	 */
	@POST
	@Path("/{loginUser}/property")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createUserProperty(@PathParam("loginUser") String loginUser, UserPropertyDto userPropertyDto) {
		UserProperty userProperty = null;
		if ((userPropertyDto != null && loginUser != null)) {
			try {
				userProperty = new UserProperty();
				if (userPropertyDto.getName() != null) {
					userProperty.setName(userPropertyDto.getName());
				}
				if (userPropertyDto.getDescription() != null) {
					userProperty.setDescription(userPropertyDto.getDescription());
				}
				if (userPropertyDto.getTxt() != null) {
					userProperty.setValue(userPropertyDto.getTxt());
				}
				if (userPropertyDto.isScriptExport() != null) {
					userProperty.setScriptExport(userPropertyDto.isScriptExport());
				}
				User user = userLocal.retrieveUser(loginUser);
				userProperty = userPropertyLocal.createUserProperty(userProperty, user.getLogin());
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String newNameProperyUser = String.valueOf(userProperty.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameProperyUser).build();

		return Response.created(uri).build();

	}

	/**
	 * update la propriété de l'utilisateur
	 * 
	 * @param loginUser
	 * @param nameproperty
	 * @param userPropertyDto
	 * @return noContent
	 */
	@PUT
	@Path("/{loginUser}/property/{nameProperty}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateUserProperty(@PathParam("loginUser") String loginUser,
			@PathParam("nameProperty") String nameproperty, UserPropertyDto userPropertyDto) {

		if (userPropertyDto != null && loginUser != null && nameproperty != null) {
			try {
				UserProperty userProperty = userPropertyLocal.retrieveUserProperty(nameproperty, loginUser);
				if (userPropertyDto.getDescription() != null) {
					userProperty.setDescription(userPropertyDto.getDescription());
				}
				if (userPropertyDto.getTxt() != null) {
					userProperty.setValue(userPropertyDto.getTxt());
				}
				if (userPropertyDto.isScriptExport() != null) {
					userProperty.setScriptExport(userPropertyDto.isScriptExport());
				}
				if (userPropertyDto.isMandatory() != null) {
					userProperty.setMandatory(userPropertyDto.isMandatory());
				}
				userPropertyLocal.updateUserProperty(userProperty);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 * Met à jour le groupe par utilisateur not found si l'id groupe ou login
	 * utilisateur n'est pas reconnu
	 * 
	 * @param login
	 * @param idsGroupe
	 * @return noContent
	 */
	@PUT
	@Path("/{login}/groups")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateGroupsFromUser(@PathParam("login") String login, IdsDto idsGroupe) {
		try {
			long[] tabId = ArrayUtils.toPrimitive(idsGroupe.getIds().toArray(new Long[0]));
			userLocal.setGroupes(login, tabId);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * création d'un utilisateur
	 * 
	 * @param userDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createUser(UserDto userDto) {
		User user;
		if (userDto != null) {
			try {
				user = new User();
				if (userDto.getLogin() != null) {
					user.setLogin(userDto.getLogin());
				}
				if (userDto.getProfil() != null) {
					UserProfil userProfil = UserProfil.valueOf(userDto.getProfil());
					user.setProfil(userProfil);
				}
				String pass = userDto.getPassword();
				user = userLocal.createUser(user, pass);

			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newUser = String.valueOf(user.getLogin());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newUser).build();

		return Response.created(uri).build();

	}

	/**
	 * mise à jour de l'utilisateur par son login not found si le login n'existe pas
	 * , bad request en cas d'erreur de syntaxe
	 * 
	 * @param login
	 * @param userRest
	 * @return noContent
	 */
	@PUT
	@Path("/{login}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateUser(@PathParam("login") String login, UserDto userRest) {

		if (userRest != null) {
			try {
				User user = userLocal.retrieveUser(login);
				if (userRest.getNom() != null) {
					user.setNom(userRest.getNom());
				}
				if (userRest.getPrenom() != null) {
					user.setPrenom(userRest.getPrenom());
				}
				if (userRest.getCommentaire() != null) {
					user.setCommentaire(userRest.getCommentaire());
				}
				if (userRest.getMail() != null) {
					user.setMail(userRest.getMail());
				}
				if (userRest.getCanChangeInfos() != null) {
					user.setCanChangeInfos(userRest.getCanChangeInfos());
				}

				if (userRest.getProfil() != null) {
					UserProfil userProfil = UserProfil.valueOf(userRest.getProfil());
					user.setProfil(userProfil);
				}
				if (userRest.getDefaultDeviceName() != null) {
					if (userRest.getDefaultDeviceName().length() == 0) {
						user.setDefaultDevice(null);
					} else {
						user.setDefaultDevice(deviceLocal.retrieveDevice(userRest.getDefaultDeviceName()));
					}
				}
				if (userRest.getCanChangeInfos() != null) {
					user.setCanChangeInfos(userRest.getCanChangeInfos());
				}
				String pass = userRest.getPassword();
				userLocal.updateUser(user, pass);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

}
