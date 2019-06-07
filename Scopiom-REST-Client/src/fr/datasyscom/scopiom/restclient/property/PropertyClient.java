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
	 * Récupération de la propriété par son identifiant
	 * 
	 * @param id : identifiant de la propriété
	 * @return PropertyDto
	 * @throws RestException : Identifiant de la propriété n'existe pas ou erreur au
	 *                       niveau du serveur (code différent de 200 OK)
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
	 * Récupération de la liste des propriétés
	 * 
	 * @return List PropertyDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
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
	 * Récupération de la propriété par son nom
	 * 
	 * @param propertyName : Nom de la propriété
	 * @return PropertyDto
	 * @throws RestException       : nom de la propriété n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom de la propriété null ou vide
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
	 * Suppression de la propriété par son nom
	 * 
	 * @param propertyName : nom de la propriété
	 * @throws RestException       : nom de propriété n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du propriété null ou vide
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
	 * Suppression de la propriété
	 * 
	 * @param mediaDto
	 * @throws RestException       : nom de la propriété n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public void delete(PropertyDto propertyDto) throws RestException, ValidationException {
		this.delete(propertyDto.getName());
	}

	/**
	 * 
	 * Création de la propriété
	 * 
	 * @param propertyDto
	 * @return PropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom de propriété null ou vide
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
	 * Mise à jour de la propriété
	 * 
	 * @param propertyDto
	 * @return PropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom de propriété null ou vide
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
