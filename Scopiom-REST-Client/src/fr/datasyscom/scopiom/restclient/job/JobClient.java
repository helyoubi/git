package fr.datasyscom.scopiom.restclient.job;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.JobPropertyDto;
import fr.datasyscom.scopiom.rest.pojo.JobStatusDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class JobClient {

	WebResource baseWebRessource;

	public JobClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/** Liste des statuts possibles d'un Job */
	public enum JobStatusType {
		WAIT, HOLD, RUNNING, OK, ERROR, ALL
	}

	/**
	 * 
	 * Récupération du job par son id
	 * 
	 * @param idJob : identifiant du job
	 * @return JobDto
	 * @throws RestException : Identifiant du job n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 200 OK)
	 */
	public JobDto byId(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob).accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<JobDto> jobGeneric = new GenericType<JobDto>() {
		};
		return response.getEntity(jobGeneric);
	}

	/**
	 * 
	 * Suppression d'un job
	 * 
	 * @param idJob : identifiant du job
	 * @throws RestException : Identifiant du job n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 204 NoContent)
	 */
	public void delete(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob).accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.delete(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Suppression d'un job
	 * 
	 * @param jobDto
	 * @throws RestException : Identifiant du job n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 204 NoContent)
	 */
	public void delete(JobDto jobDto) throws RestException {
		delete(jobDto.getId());
	}

	/**
	 * 
	 * Récupération de la liste des propriétés d'un job
	 * 
	 * @param idJob : identifiant du job
	 * @return List JobPropertyDto
	 * @throws RestException : identifiant du job n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 200 OK)
	 */
	public List<JobPropertyDto> allProperties(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/property").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<JobPropertyDto>> jobPropertyGenericList = new GenericType<List<JobPropertyDto>>() {
		};
		List<JobPropertyDto> jobPropertyDtos = response.getEntity(jobPropertyGenericList);
		return jobPropertyDtos;
	}

	/**
	 * 
	 * Récupération de la liste des propriétés d'un job
	 * 
	 * @param jobDto
	 * @return List JobPropertyDto
	 * @throws RestException : identifiant du job n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 200 OK)
	 */
	public List<JobPropertyDto> allProperties(JobDto jobDto) throws RestException {
		return allProperties(jobDto.getId());
	}

	/**
	 * 
	 * Récupération d'une propriété du job
	 * 
	 * @param idJob        : identifiant du job
	 * @param nameProperty : nom de la propriété
	 * @return JobPropertyDto
	 * @throws RestException       : identifiant du job ou nom de la propriété
	 *                             n'existe pas ou erreur au niveau du serveur (code
	 *                             différent de 200 OK)
	 * @throws ValidationException : nom de la propriété null ou vide
	 */
	public JobPropertyDto retrieveProperty(long idJob, String nameProperty) throws RestException, ValidationException {
		JobPropertyDto jobPropertyDto;
		if (nameProperty == null || nameProperty.isEmpty()) {
			throw new ValidationException("Property name is empty or null");
		}
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/property/" + nameProperty)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<JobPropertyDto> jobPropertyGeneric = new GenericType<JobPropertyDto>() {
		};
		jobPropertyDto = response.getEntity(jobPropertyGeneric);
		return jobPropertyDto;
	}

	/**
	 * 
	 * Récupération d'une propriété du job
	 * 
	 * @param jobDto
	 * @param nameProperty : nom de la propriété
	 * @return JobPropertyDto
	 * @throws RestException       : identifiant du job ou nom de la propriété
	 *                             n'existe pas ou erreur au niveau du serveur (code
	 *                             différent de 200 OK)
	 * @throws ValidationException : nom de la propriété null ou vide
	 */
	public JobPropertyDto retrieveProperty(JobDto jobDto, String nameProperty)
			throws RestException, ValidationException {
		return retrieveProperty(jobDto.getId(), nameProperty);
	}

	/**
	 * 
	 * Suppression la propriété d'un job
	 * 
	 * @param idJob        : identifiant du job
	 * @param nameProperty : nom de la propriété
	 * @throws RestException       : identifiant du job ou nom de propriété n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException : nom de propriété null ou vide
	 */
	public void deleteProperty(long idJob, String nameProperty) throws RestException, ValidationException {
		if (nameProperty == null || nameProperty.isEmpty()) {
			throw new ValidationException("Property name is null or empty");
		}
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/property/" + nameProperty)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.delete(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Suppression la propriété d'un job
	 * 
	 * @param jobDto
	 * @param nameProperty : nom de la propriété
	 * @throws RestException       : identifiant du job ou nom de propriété n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException : nom de propriété null ou vide
	 */
	public void deleteProperty(JobDto jobDto, String nameProperty) throws RestException, ValidationException {
		deleteProperty(jobDto.getId(), nameProperty);
	}

	/**
	 * 
	 * Création de la propriété du job
	 * 
	 * @param idJob          : identifiant du job
	 * @param jobPropertyDto
	 * @return JobPropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom propriété est null ou vide
	 */
	public JobPropertyDto createProperty(long idJob, JobPropertyDto jobPropertyDto)
			throws RestException, ValidationException {
		if (jobPropertyDto.getName() == null || jobPropertyDto.getName().isEmpty()) {
			throw new ValidationException("Job property is null");
		}
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/property").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, jobPropertyDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return retrieveProperty(idJob, jobPropertyDto.getName());
	}

	/**
	 * 
	 * Création de la propriété du job
	 * 
	 * @param jobDto
	 * @param jobPropertyDto
	 * @return JobPropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom propriété est null ou vide
	 */
	public JobPropertyDto createProperty(JobDto jobDto, JobPropertyDto jobPropertyDto)
			throws RestException, ValidationException {
		return createProperty(jobDto.getId(), jobPropertyDto);
	}
	
	/**
	 * 
	 * Mise à jour du statut d'un job
	 * 
	 * @param idJob        : identifiant du job
	 * @param jobStatusDto
	 * @throws RestException : Erreur lors de la mise à jour du statut de job ou
	 *                       niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom de statut est vide
	 */
	public JobDto setStatus(long idJob, JobStatusDto jobStatusDto) throws RestException,ValidationException {
		
		if(jobStatusDto.getStatus()==null || jobStatusDto.getStatus().isEmpty()) {
			throw new ValidationException("Job status is empty or null");
		}		
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/status").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, jobStatusDto);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		return byId(idJob);
	}
	
	/**
	 * 
	 * Mise à jour de la propriété du job
	 * 
	 * @param idJob          : identifiant du job
	 * @param jobPropertyDto
	 * @param namePropertyToUpdate : nom de la propriété du job à
	 *                       modifier
	 * @return JobPropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             du job ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom propriété est null ou vide
	 */
	public JobPropertyDto updateProperty(long idJob, String namePropertyToUpdate, JobPropertyDto jobPropertyDto)
			throws RestException {
		String propertyName = jobPropertyDto.getName();
		if (namePropertyToUpdate == null || namePropertyToUpdate.isEmpty() && propertyName == null
				|| propertyName.isEmpty()) {
			throw new ValidationException("Property name is empty or null");
		}
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/property/" + namePropertyToUpdate)
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, jobPropertyDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return retrieveProperty(idJob, propertyName);
	}

	/**
	 * 
	 * Ajout du device au job
	 * 
	 * @param idJob    : identifiant du job
	 * @param idDevice : identifiant du périphérique
	 * @throws RestException : Erreur lors de l'ajout du device au job ou niveau
	 *                       serveur (code différent de 204 NoContent)
	 */
	public void setDevice(long idJob, long idDevice) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/device/" + idDevice)
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Arrête un Job
	 * 
	 * @param idJob : identifiant du job
	 * @throws RestException : Erreur lors de l'arrêt du job ou niveau serveur (code
	 *                       différent de 200 Ok)
	 */
	public void kill(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/kill").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Ajout d'un media au job
	 * 
	 * @param idJob   : identifiant du job
	 * @param idMedia : identifiant du média
	 * @throws RestException : Erreur lors de l'ajout du media au job ou niveau
	 *                       serveur (code différent de 204 NoContent)
	 */
	public void setMedia(long idJob, long idMedia) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/media/" + idMedia)
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Suppression d'un media du job
	 * 
	 * @param idJob : identifiant du job
	 * @throws RestException : Erreur lors de la suppression du media du job ou
	 *                       niveau serveur (code différent de 204 NoContent)
	 */
	public void removeMedia(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/media/remove").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Suppression d'un device du job
	 * 
	 * @param idJob : identifiant du job
	 * @throws RestException : Erreur lors de la suppression du device du job ou
	 *                       niveau serveur (code différent de 204 NoContent)
	 */
	public void removeDevice(long idJob) throws RestException {
		Builder builder = baseWebRessource.path("jobs/" + idJob + "/device/remove").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Mise à jour de la propriété du job
	 * 
	 * @param jobDto
	 * @param jobPropertyDto
	 * @param                : namePropertyToUpdate : nom de la propriété du job à
	 *                       modifier
	 * @return JobPropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             du job ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom propriété est null ou vide
	 */
	public JobPropertyDto updateProperty(JobDto jobDto, String namePropertyToUpdate, JobPropertyDto jobPropertyDto)
			throws RestException, ValidationException {
		return updateProperty(jobDto.getId(), namePropertyToUpdate, jobPropertyDto);
	}

}
