package fr.datasyscom.scopiom.restclient.type;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.TypeDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class TypeClient {

	WebResource baseWebRessource;

	public TypeClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération du type par son nom
	 * 
	 * @param nameType : nom du type
	 * @return TypeDto
	 * @throws RestException       : nom du type n'existe pas ou erreur au niveau du
	 *                             serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du type est null ou vide
	 */
	public TypeDto byName(String nameType) throws RestException, ValidationException {
		TypeDto typeDto = null;
		if (nameType != null && !nameType.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<TypeDto> typeGeneric = new GenericType<TypeDto>() {
			};
			typeDto = response.getEntity(typeGeneric);
			return typeDto;
		} else {
			throw new ValidationException("Type name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération du type par défaut
	 * 
	 * @return TypeDto
	 * @throws RestException : erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public TypeDto getDefault() throws RestException {
		Builder builder = baseWebRessource.path("types/default").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<TypeDto> typeGeneric = new GenericType<TypeDto>() {
		};
		TypeDto typeDto = response.getEntity(typeGeneric);
		return typeDto;

	}

	/**
	 * 
	 * Récupération du type par son identifiant
	 * 
	 * @param id : identifiant du type
	 * @return TypeDto
	 * @throws RestException : Identifiant du type n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 200 OK)
	 */
	public TypeDto byId(long id) throws RestException {
		TypeDto typeDto = null;
		Builder builder = baseWebRessource.path("types").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<TypeDto>> listTypesDto = new GenericType<List<TypeDto>>() {
		};
		List<TypeDto> typeList = response.getEntity(listTypesDto);
		typeDto = typeList.get(0);

		return typeDto;
	}

	/**
	 * 
	 * Récupération du type par extension
	 * 
	 * @param nameExtension : extension du type
	 * @return TypeDto
	 * @throws RestException : extension du type n'existe pas ou erreur au niveau du
	 *                       serveur (code différent de 200 OK)
	 */
	public TypeDto ByExtension(String nameExtension) throws RestException, ValidationException {
		if (nameExtension == null || nameExtension.isEmpty()) {
			throw new ValidationException("Name extension is empty or null");
		}
		Builder builder = baseWebRessource.path("types/extension").queryParam("value", nameExtension)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<TypeDto> typeGeneric = new GenericType<TypeDto>() {
		};
		TypeDto type = response.getEntity(typeGeneric);

		return type;
	}

	/**
	 * 
	 * Récupération du type par son type mime
	 * 
	 * @param nameTypeMime : type mime du type
	 * @return TypeDto
	 * @throws RestException : type mime n'existe pas ou erreur au niveau du serveur
	 *                       (code différent de 200 OK)
	 */
	public TypeDto ByTypeMime(String nameTypeMime) throws RestException, ValidationException {
		if (nameTypeMime == null || nameTypeMime.isEmpty()) {
			throw new ValidationException("Name Mime type is empty or null");
		}
		Builder builder = baseWebRessource.path("types/typeMime").queryParam("value", nameTypeMime)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<TypeDto> typeGeneric = new GenericType<TypeDto>() {
		};
		TypeDto type = response.getEntity(typeGeneric);

		return type;
	}

	/**
	 * 
	 * Récupération de la liste des types
	 * 
	 * @return List TypeDto
	 * @throws RestException : erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<TypeDto> all() throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("types").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<TypeDto>> typeList = new GenericType<List<TypeDto>>() {
		};
		List<TypeDto> typesList = response.getEntity(typeList);
		return typesList;
	}

	/**
	 * 
	 * Création d'un type
	 * 
	 * @param typeDto
	 * @return TypeDto
	 * @throws RestException       : Erreur lors de la création du type ou niveau
	 *                             serveur (code différent de 201 Created)
	 * @throws ValidationException : Type est null
	 */
	public TypeDto create(TypeDto typeDto) throws RestException, ValidationException {
		if (typeDto.getName() != null && !typeDto.getName().isEmpty()) {
			Builder builder = baseWebRessource.path("types").accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, typeDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return byName(typeDto.getName());
		} else {
			throw new ValidationException("Type is null");
		}
	}

	/**
	 * 
	 * Suppression d'un type
	 * 
	 * @param nameType : nom du type
	 * @throws RestException       : nom du type n'existe pas ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du type est null ou vide
	 */
	public void delete(String nameType) throws RestException, ValidationException {
		if (nameType != null && !nameType.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Type name empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un type
	 * 
	 * @param typeDto
	 * @throws RestException       : nom du type n'existe pas ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du type est null ou vide
	 */
	public void delete(TypeDto typeDto) throws RestException, ValidationException {
		delete(typeDto.getName());
	}

	/**
	 * mise à jour du type
	 * 
	 * @param typeDto
	 * @return
	 * @throws RestException
	 * @throws ValidationException
	 */
	public TypeDto update(TypeDto typeDto) throws RestException, ValidationException {
		String nameType = typeDto.getName();
		if (nameType != null && !nameType.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, typeDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameType);
		} else {
			throw new ValidationException("Type name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un type Mime
	 * 
	 * @param nameType     : nom du type
	 * @param nameTypeMime : nom du type mime
	 * @throws RestException       : nom du type ou du type mime n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du type ou type mime sont null ou vides
	 */
	public void removeTypeMime(String nameType, String nameTypeMime) throws RestException, ValidationException {
		if (nameType != null && !nameType.isEmpty() && nameTypeMime != null && !nameTypeMime.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType + "/typeMime/" + nameTypeMime)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Type or Mime type name are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un type Mime
	 * 
	 * @param typeDto
	 * @param nameTypeMime : nom du type mime
	 * @throws RestException       : nom du type ou du type mime n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du type ou type mime sont null ou vides
	 */
	public void removeTypeMime(TypeDto typeDto, String nameTypeMime) throws RestException, ValidationException {
		removeTypeMime(typeDto.getName(), nameTypeMime);
	}

	/**
	 * 
	 * Suppression d'une extension
	 * 
	 * @param nameType      : nom du type
	 * @param nameExtension : nom d'extension
	 * @throws RestException       : nom du type ou d'extension n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du type ou extension sont null ou vides
	 */
	public void removeExtension(String nameType, String nameExtension) throws RestException, ValidationException {
		if (nameType != null && !nameType.isEmpty() && nameExtension != null && !nameExtension.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType + "/extension/" + nameExtension)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Type or extension name are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une extension
	 * 
	 * @param typeDto
	 * @param nameExtension : nom d'extension
	 * @throws RestException       : nom du type ou d'extension n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du type ou extension sont null ou vides
	 */
	public void removeExtension(TypeDto typeDto, String nameExtension) throws RestException, ValidationException {
		removeExtension(typeDto.getName(), nameExtension);
	}

	/**
	 * 
	 * Ajout d'une extension au type
	 * 
	 * @param nameType      : nom du type
	 * @param nameExtension : nom d'extension
	 * @throws RestException       : nom du type ou d'extension n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom du type ou d'extension sont null ou vides
	 */
	public void addExtension(String nameType, String nameExtension) throws RestException, ValidationException {
		if (nameType != null && !nameType.isEmpty() && nameExtension != null && !nameExtension.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType + "/extension/" + nameExtension)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Type or extension name are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'une extension au type
	 * 
	 * @param typeDto
	 * @param nameExtension : nom d'extension
	 * @throws RestException       : nom du type ou d'extension n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom du type ou d'extension sont null ou vides
	 */
	public void addExtension(TypeDto typeDto, String nameExtension) throws RestException, ValidationException {
		addExtension(typeDto.getName(), nameExtension);
	}

	/**
	 * 
	 * Ajout d'un typeMime au type
	 * 
	 * @param nameType     : nom du type
	 * @param nameTypeMime : nom du type mime
	 * @throws RestException       : nom du type ou type mime n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom du type ou type mime sont null ou vides
	 */
	public void addTypeMime(String nameType, String nameTypeMime) throws RestException, ValidationException {
		if (nameType != null && !nameType.isEmpty() && nameTypeMime != null && !nameTypeMime.isEmpty()) {
			Builder builder = baseWebRessource.path("types/" + nameType + "/typeMime/" + nameTypeMime)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Type or Mime type name are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'un typeMime au type
	 * 
	 * @param typeDto
	 * @param nameTypeMime : nom du type mime
	 * @throws RestException       : nom du type ou type mime n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 Ok)
	 * @throws ValidationException : nom du type ou type mime sont null ou vides
	 */
	public void addTypeMime(TypeDto typeDto, String nameTypeMime) throws RestException, ValidationException {
		addTypeMime(typeDto.getName(), nameTypeMime);
	}

}
