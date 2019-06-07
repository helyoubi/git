package fr.datasyscom.scopiom.restclient.property;

import java.util.List;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.PropertyDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class PropertyClient {

	WebResource baseWebRessource;

	public PropertyClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * R�cup�ration de la propri�t� par son identifiant
	 * 
	 * @param id : identifiant de la propri�t�
	 * @return PropertyDto
	 * @throws RestException : Identifiant de la propri�t� n'existe pas ou erreur au
	 *                       niveau du serveur (code diff�rent de 200 OK)
	 */
	public PropertyDto byId(long id) throws RestException {
		PropertyDto propertyDto = null;
		Builder builder = baseWebRessource.path("properties").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<PropertyDto>> listPropertiesDto = new GenericType<List<PropertyDto>>() {
		};
		List<PropertyDto> propertyList = response.getEntity(listPropertiesDto);
		propertyDto = propertyList.get(0);

		return propertyDto;
	}

	/**
	 * 
	 * R�cup�ration de la liste des propri�t�s
	 * 
	 * @return List PropertyDto
	 * @throws RestException : Erreur au niveau du serveur (code diff�rent de 200
	 *                       OK)
	 */
	public List<PropertyDto> all() throws RestException {
		Builder builder = baseWebRessource.path("properties").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<PropertyDto>> listPropertyDto = new GenericType<List<PropertyDto>>() {
		};
		List<PropertyDto> propertyList = response.getEntity(listPropertyDto);
		return propertyList;
	}

	/**
	 * 
	 * R�cup�ration de la propri�t� par son nom
	 * 
	 * @param propertyName : Nom de la propri�t�
	 * @return PropertyDto
	 * @throws RestException       : nom de la propri�t� n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom de la propri�t� null ou vide
	 */
	public PropertyDto byName(String propertyName) throws RestException, ValidationException {
		if (propertyName == null || propertyName.isEmpty()) {
			throw new ValidationException("Property name is empty or null");
		}
		Builder builder = baseWebRessource.path("properties/" + propertyName).accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<PropertyDto> propertyDtoGeneric = new GenericType<PropertyDto>() {
		};
		return response.getEntity(propertyDtoGeneric);
	}

	/**
	 * 
	 * Suppression de la propri�t� par son nom
	 * 
	 * @param propertyName : nom de la propri�t�
	 * @throws RestException       : nom de propri�t� n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du propri�t� null ou vide
	 */
	public void delete(String propertyName) throws RestException, ValidationException {
		if (propertyName != null && !propertyName.isEmpty()) {
			Builder builder = baseWebRessource.path("properties/" + propertyName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Property name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression de la propri�t�
	 * 
	 * @param mediaDto
	 * @throws RestException       : nom de la propri�t� n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du m�dia null ou vide
	 */
	public void delete(PropertyDto propertyDto) throws RestException, ValidationException {
		this.delete(propertyDto.getName());
	}

	/**
	 * 
	 * Cr�ation de la propri�t�
	 * 
	 * @param propertyDto
	 * @return PropertyDto
	 * @throws RestException       : Erreur lors de la cr�ation de la propri�t� ou
	 *                             niveau serveur (code diff�rent de 201 Created)
	 * @throws ValidationException : nom de propri�t� null ou vide
	 */
	public PropertyDto create(PropertyDto propertyDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("properties").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, propertyDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return byName( propertyDto.getName());
	}

	/**
	 * 
	 * Mise � jour de la propri�t�
	 * 
	 * @param propertyDto
	 * @return PropertyDto
	 * @throws RestException       : Erreur lors de la mise � jour de la propri�t�
	 *                             ou niveau serveur (code diff�rent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom de propri�t� null ou vide
	 */
	public PropertyDto update(PropertyDto propertyDto) throws RestException, ValidationException {
		String nameProperty = propertyDto.getName();
		if (nameProperty == null || nameProperty.isEmpty()) {
			throw new ValidationException("Property name is empty or null");
		}
		Builder builder = baseWebRessource.path("properties/" + nameProperty).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, propertyDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return byName(nameProperty);

	}

}
