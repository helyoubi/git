package fr.datasyscom.scopiom.restclient.user;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.IdsDto;
import fr.datasyscom.scopiom.rest.pojo.UserDto;
import fr.datasyscom.scopiom.rest.pojo.UserPropertyDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class UserClient {

	WebResource baseWebRessource;

	public UserClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/** Liste des profils possibles d'un User */
	public enum UserProfil {
		IOM_ADMIN, IOM_OP, IOM_USER
	}

	/**
	 * 
	 * Récupération d'une utilisateur par son login
	 * 
	 * @param loginUser : login utilisateur
	 * @return UserDto
	 * @throws RestException       : login utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : login utilisateur est null ou vide
	 */
	public UserDto byLogin(String loginUser) throws RestException, ValidationException {
		UserDto userDto = null;
		if (loginUser != null && !loginUser.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<UserDto> userGeneric = new GenericType<UserDto>() {
			};
			userDto = response.getEntity(userGeneric);
			return userDto;
		} else {
			throw new ValidationException("Login user is empty or null");
		}
	}

	/**
	 * 
	 * Récupération d'une utilisateur par son profil
	 * 
	 * @param profilUser : profil utilisateur
	 * @return UserDto
	 * @throws RestException       : profil utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : profil utilisateur est null ou vide
	 */
	public List<UserDto> byProfil(UserProfil profilUser) throws RestException, ValidationException {

		String profil = profilUser.name();
		Builder builder = baseWebRessource.path("users").queryParam("profil", profil)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<UserDto>> listUserGeneric = new GenericType<List<UserDto>>() {
		};
		return response.getEntity(listUserGeneric);

	}

	/**
	 * 
	 * Récupération de la liste des utilisateurs
	 * 
	 * @return List UserDto
	 * @throws RestException : erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<UserDto> all() throws RestException {
		Builder builder = baseWebRessource.path("users").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<UserDto>> usersList = new GenericType<List<UserDto>>() {
		};
		List<UserDto> userDtos = response.getEntity(usersList);
		return userDtos;
	}

	/**
	 * 
	 * retire un groupe de l'utilisateur
	 * 
	 * @param loginUser : login utilisateur
	 * @param nameGroup : nom du groupe
	 * @throws RestException       : login utilisateur ou nom du groupe n'existe pas
	 *                             ,ou erreur au niveau du serveur (code différent
	 *                             de 200 Ok)
	 * @throws ValidationException : login utilisateur ou nom du groupe sont null ou
	 *                             vides
	 */
	public void removeGroup(String loginUser, String nameGroup) throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty() && nameGroup != null && !nameGroup.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Login user or group name are empty or null");
		}
	}

	/**
	 * 
	 * retire un groupe de l'utilisateur
	 * 
	 * @param loginUser : login utilisateur
	 * @param nameGroup : nom du groupe
	 * @throws RestException       : login utilisateur ou nom du groupe n'existe pas
	 *                             ,ou erreur au niveau du serveur (code différent
	 *                             de 200 Ok)
	 * @throws ValidationException : login utilisateur ou nom du groupe sont null ou
	 *                             vides
	 */
	public void removeGroup(UserDto userDto, GroupeDto groupeDto) throws RestException, ValidationException {
		this.removeGroup(userDto.getLogin(), groupeDto.getName());
	}

	/**
	 * 
	 * Récupération des groupes
	 * 
	 * @param loginUser : login utilisateur
	 * @return List GroupeDto
	 * @throws RestException       : login utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : login utilisateur est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(String loginUser) throws RestException, ValidationException {
		List<GroupeDto> groupeDtos;
		if (loginUser != null && !loginUser.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/groups")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<GroupeDto>> groupGenericList = new GenericType<List<GroupeDto>>() {
			};
			groupeDtos = response.getEntity(groupGenericList);
			return groupeDtos;
		} else {
			throw new ValidationException("Login user is empty or null");
		}
	}

	/**
	 * 
	 * Récupération des groupes
	 * 
	 * @param userDto
	 * @return List GroupeDto
	 * @throws RestException       : login utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : login utilisateur est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(UserDto userDto) throws RestException, ValidationException {
		return retrieveGroup(userDto.getLogin());
	}

	/**
	 * 
	 * Ajout d'un groupe à l'utilisateur
	 * 
	 * @param loginUser : nom d'utilisateur
	 * @param groupName : nom du groupe
	 * @throws RestException       : nom d'utilisateur ou du groupe n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom d'utilisateur ou nom du groupe sont null ou
	 *                             vides
	 */
	public void addGroup(String loginUser, String groupName) throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty() && groupName != null && !groupName.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/groups/" + groupName)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("User login or group name are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'un groupe à l'utilisateur
	 * 
	 * @param loginUser : nom d'utilisateur
	 * @param groupName : nom du groupe
	 * @throws RestException       : nom d'utilisateur ou du groupe n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom d'utilisateur ou nom du groupe sont null ou
	 *                             vides
	 */
	public void addGroup(UserDto userDto, GroupeDto groupeDto) throws RestException, ValidationException {
		this.addGroup(userDto.getLogin(), groupeDto.getName());
	}

	/**
	 * 
	 * Création de l'utilisateur
	 * 
	 * @param userDto
	 * @return UserDto
	 * @throws RestException       : Erreur lors de la création de l'utilisateu ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : utilisateur null
	 */
	public UserDto create(UserDto userDto) throws RestException, ValidationException {
		if (userDto != null) {
			Builder builder = baseWebRessource.path("users").accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, userDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return byLogin(userDto.getLogin());
		} else {
			throw new ValidationException("User is null");
		}
	}

	/**
	 * 
	 * Création de la propriété de l'utilisateur
	 * 
	 * @param userDto
	 * @param userPropertyDto
	 * @return UserPropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : login utilisateur ou propriété d'utilisateur
	 *                             sont null ou vide
	 */
	public UserPropertyDto createProperty(UserDto userDto, UserPropertyDto userPropertyDto)
			throws RestException, ValidationException {
		return createProperty(userDto.getLogin(), userPropertyDto);
	}

	/**
	 * 
	 * Récupération de la propriété d'un utilisateur
	 * 
	 * @param loginUser    : login utilisateur
	 * @param nameProperty : nom de la propriété
	 * @return UserPropertyDto
	 * @throws RestException       : login utilisateur ou nom de la propriété
	 *                             n'existe pas ou erreur au niveau du serveur (code
	 *                             différent de 200 OK)
	 * @throws ValidationException : login utilisateur ou nom de la propriété sont
	 *                             null ou vides
	 */
	public UserPropertyDto retrieveProperty(String loginUser, String nameProperty)
			throws RestException, ValidationException {
		UserPropertyDto userPropertyDto;
		if (loginUser != null && !loginUser.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<UserPropertyDto> userPropGeneric = new GenericType<UserPropertyDto>() {
			};
			userPropertyDto = response.getEntity(userPropGeneric);
			return userPropertyDto;
		} else {
			throw new ValidationException("Login user or property are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un utilisateur
	 * 
	 * @param loginUser : login d'utilisateur
	 * @throws RestException       : login d'utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : login d'utilisateur null ou vide
	 */
	public void delete(String loginUser) throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Login user empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un utilisateur
	 * 
	 * @param userDto
	 * @throws RestException       : login d'utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : login d'utilisateur null ou vide
	 */
	public void delete(UserDto userDto) throws RestException, ValidationException {
		delete(userDto.getLogin());
	}

	/**
	 * 
	 * Suppression d'une propriété d'utilisateur
	 * 
	 * @param loginUser    : login utilisateur
	 * @param nameProperty : nom de propriété
	 * @throws RestException       : login utilisateur ou propriété n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : login utilisateur ou propriété sont null ou
	 *                             vides
	 */
	public void deleteProperty(String loginUser, String nameProperty) throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Login user or property empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une propriété d'utilisateur
	 * 
	 * @param userDto
	 * @param nameProperty : nom de propriété
	 * @throws RestException       : login utilisateur ou propriété n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : login utilisateur ou propriété sont null ou
	 *                             vides
	 */
	public void deleteProperty(UserDto userDto, String nameProperty) throws RestException, ValidationException {
		deleteProperty(userDto.getLogin(), nameProperty);
	}

	/**
	 * 
	 * Récupération de la liste de propriétés d'un utilisateur
	 * 
	 * @param loginUser : login utilisateur
	 * @return List UserPropertyDto
	 * @throws RestException       : login utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : login utilisateur null ou vide
	 */
	public List<UserPropertyDto> allProperties(String loginUser) throws RestException, ValidationException {
		List<UserPropertyDto> userPropertyDtos;
		if (loginUser == null || loginUser.isEmpty()) {
			throw new ValidationException("Login user is null or empty");
		}
		Builder builder = baseWebRessource.path("users/" + loginUser + "/property").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<UserPropertyDto>> userpropertyList = new GenericType<List<UserPropertyDto>>() {
		};
		userPropertyDtos = response.getEntity(userpropertyList);
		return userPropertyDtos;
	}

	/**
	 * 
	 * Récupération de la liste de propriétés d'un utilisateur
	 * 
	 * @param userDto
	 * @return List UserPropertyDto
	 * @throws RestException       : login utilisateur n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : login utilisateur null ou vide
	 */
	public List<UserPropertyDto> allProperties(UserDto userDto) throws RestException, ValidationException {
		return allProperties(userDto.getLogin());
	}

	/**
	 * 
	 * Création de la propriété de l'utilisateur
	 * 
	 * @param loginUser
	 * @param userPropertyDto
	 * @return UserPropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : login utilisateur ou propriété d'utilisateur
	 *                             sont null ou vide
	 */
	public UserPropertyDto createProperty(String loginUser, UserPropertyDto userPropertyDto)
			throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty() && userPropertyDto != null) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/property")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, userPropertyDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return retrieveProperty(loginUser, userPropertyDto.getName());
		} else {
			throw new ValidationException("User property or User login is null");
		}
	}

	/**
	 * 
	 * Mise à jour de la propriété de l'utilisateur
	 * 
	 * @param userDto
	 * @return UserPropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             d'utilisateur ou niveau serveur (code différent
	 *                             de 204 NoContent)
	 * @throws ValidationException : login utilisateur ou nom de propriété sont null
	 *                             ou vides
	 */
	public UserPropertyDto updateProperty(UserDto userDto, UserPropertyDto userPropertyDto)
			throws RestException, ValidationException {
		return updateProperty(userDto.getLogin(), userPropertyDto);
	}

	/**
	 * 
	 * Mise à jour de la propriété de l'utilisateur
	 * 
	 * @param userPropertyDto
	 * @return UserPropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             d'utilisateur ou niveau serveur (code différent
	 *                             de 204 NoContent)
	 * @throws ValidationException : login utilisateur ou nom de propriété sont null
	 *                             ou vides
	 */
	public UserPropertyDto updateProperty(String userLogin, UserPropertyDto userPropertyDto)
			throws RestException, ValidationException {
		String propertyName = userPropertyDto.getName();
		if (userLogin == null || userLogin.isEmpty() && propertyName == null || propertyName.isEmpty()) {
			throw new ValidationException("User login or property name are empty or null");
		}
		Builder builder = baseWebRessource.path("users/" + userLogin + "/property/" + propertyName)
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, userPropertyDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return retrieveProperty(userLogin, propertyName);
	}

	/**
	 * 
	 * Mise à jour des groupes par utilisateur
	 * 
	 * @param userPropertyDto
	 * @return GroupeDto
	 * @throws RestException       : Erreur lors de la mise à jour des groupes
	 *                             d'utilisateurs ou niveau serveur (code différent
	 *                             de 204 NoContent)
	 * @throws ValidationException : login utilisateur ou nom de propriété sont null
	 *                             ou vides
	 */
	public void updateGroups(String loginUser, IdsDto idsDto) throws RestException, ValidationException {
		if (loginUser != null && !loginUser.isEmpty() && idsDto != null) {
			Builder builder = baseWebRessource.path("users/" + loginUser + "/groups").accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, idsDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("User login or Ids group are empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour de l'utilisateur
	 * 
	 * @param userDto
	 * @return UserDto
	 * @throws RestException       : Erreur lors de la mise à jour de l'utilisateur
	 *                             ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : login utilisateur null ou vide
	 */
	public UserDto update(UserDto userDto) throws RestException, ValidationException {
		String loginUser = userDto.getLogin();
		if (loginUser != null && !loginUser.isEmpty()) {
			Builder builder = baseWebRessource.path("users/" + loginUser).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, userDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byLogin(loginUser);
		} else {
			throw new ValidationException("User login is empty or null");
		}
	}

}
