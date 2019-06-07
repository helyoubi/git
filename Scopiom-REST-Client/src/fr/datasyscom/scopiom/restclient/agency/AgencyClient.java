package fr.datasyscom.scopiom.restclient.agency;

import java.util.List;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.AgencyDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class AgencyClient {

	WebResource baseWebRessource;

	public AgencyClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération de l'agence par son identifiant
	 * 
	 * @param id : identifiant de l'agence
	 * @return AgencyDto
	 * @throws RestException : Identifiant de l'agence n'existe pas ou erreur au
	 *                       niveau du serveur (code différent de 200 OK)
	 */
	public AgencyDto byId(long id) throws RestException {
		AgencyDto agencyDto = null;
		Builder builder = baseWebRessource.path("agency").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<AgencyDto>> listAgencysDto = new GenericType<List<AgencyDto>>() {
		};
		List<AgencyDto> angetList = response.getEntity(listAgencysDto);
		agencyDto = angetList.get(0);

		return agencyDto;
	}

	/**
	 * 
	 * Récupération de la liste des agences
	 * 
	 * @return List AgencyDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<AgencyDto> all() throws RestException {
		Builder builder = baseWebRessource.path("agency").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<AgencyDto>> listAgenceDto = new GenericType<List<AgencyDto>>() {
		};
		List<AgencyDto> agencyList = response.getEntity(listAgenceDto);
		return agencyList;
	}

	/**
	 * 
	 * Récupération de l'agence par son nom
	 * 
	 * @param agencyName : nom de l'agence
	 * @return AgencyDto
	 * @throws RestException       : nom de l'agence n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom de l'agence null ou vide
	 */
	public AgencyDto byName(String agencyName) throws RestException, ValidationException {
		AgencyDto agencyDto = null;
		if (agencyName != null && !agencyName.isEmpty()) {
			Builder builder = baseWebRessource.path("agency/" + agencyName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<AgencyDto> agencyGeneric = new GenericType<AgencyDto>() {
			};
			agencyDto = response.getEntity(agencyGeneric);
			return agencyDto;
		} else {
			throw new ValidationException("Agency name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression de l'agence par son nom
	 * 
	 * @param agencyName : nom de l'agence
	 * @throws RestException       : nom d'agence n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom d'agence null ou vide
	 */
	public void delete(String agencyName) throws RestException, ValidationException {
		if (agencyName != null && !agencyName.isEmpty()) {
			Builder builder = baseWebRessource.path("agency/" + agencyName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Agency name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression de l'agence
	 * 
	 * @param agencyDto
	 * @throws RestException       : Nom d'agence n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom d'agence null ou vide
	 */
	public void delete(AgencyDto agencyDto) throws RestException, ValidationException {
		this.delete(agencyDto.getName());
	}

	/**
	 * 
	 * Création de l'agence
	 * 
	 * @param agencyDto
	 * @return AgencyDto
	 * @throws RestException       : Erreur lors de la création de l'agence ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom d'agence null ou vide
	 */
	public AgencyDto create(AgencyDto agencyDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("agency").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, agencyDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return byName(agencyDto.getName());
	}

	/**
	 * 
	 * Mise à jour de l'agence
	 * 
	 * @param agencyDto
	 * @return AgencyDto
	 * @throws RestException       : Erreur lors de la mise à jour de l'agence ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom de l'agence null ou vide
	 */
	public AgencyDto update(AgencyDto agencyDto) throws RestException, ValidationException {
		String nameAgency = agencyDto.getName();
		if (nameAgency != null && !nameAgency.isEmpty()) {
			Builder builder = baseWebRessource.path("agency/" + nameAgency).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, agencyDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameAgency);
		} else {
			throw new ValidationException("Agency name is empty or null");
		}
	}

}
