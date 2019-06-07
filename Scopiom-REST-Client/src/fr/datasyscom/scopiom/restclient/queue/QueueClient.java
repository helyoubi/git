package fr.datasyscom.scopiom.restclient.queue;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.ejbsession.tableexploit.TableExploitDto;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.QueuePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.QueueStatusDto;
import fr.datasyscom.scopiom.rest.pojo.TableExploitDtoRest;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;

public class QueueClient {

	WebResource baseWebRessource;

	public QueueClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/** La liste des statuts possibles d'une queue */
	public enum QueueStatusType {
		OPEN, HOLD, CLOSE
	}
	
	/**
	 * 
	 * Récupération d'une file de traitement par nom
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return QueueDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public QueueDto byName(String nameQueue) throws RestException, ValidationException {
		QueueDto queueDto = null;
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<QueueDto> queueGeneric = new GenericType<QueueDto>() {
			};
			queueDto = response.getEntity(queueGeneric);
			return queueDto;
		} else {
			throw new ValidationException("Queue name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération de la liste de file de traitement
	 * 
	 * @return List QueueDto
	 * @throws RestException : erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<QueueDto> all() throws RestException {
		Builder builder = baseWebRessource.path("queues").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<QueueDto>> queueList = new GenericType<List<QueueDto>>() {
		};
		List<QueueDto> queueDtos = response.getEntity(queueList);
		return queueDtos;
	}

	/**
	 * 
	 * Récupération de la file de traitement par son identifiant
	 * 
	 * @param id : identifiant de la file de traitement
	 * @return QueueDto
	 * @throws RestException : Identifiant de la file de traitement n'existe pas ou
	 *                       erreur au niveau du serveur (code différent de 200 OK)
	 */
	public QueueDto byId(long id) throws RestException {
		QueueDto queueDto = null;
		Builder builder = baseWebRessource.path("queues").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<QueueDto>> listQueuesDto = new GenericType<List<QueueDto>>() {
		};
		List<QueueDto> queueList = response.getEntity(listQueuesDto);
		queueDto = queueList.get(0);

		return queueDto;
	}

	/**
	 * 
	 * Récupération de la liste de propriétés de la file de traitement
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return List QueuePropertyDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<QueuePropertyDto> allProperties(String nameQueue) throws RestException, ValidationException {
		List<QueuePropertyDto> queuePropertyDtos;
		if (nameQueue == null || nameQueue.isEmpty()) {
			throw new ValidationException("Queue name is null or empty");
		}
		Builder builder = baseWebRessource.path("queues/" + nameQueue + "/property").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<QueuePropertyDto>> queuepropertyList = new GenericType<List<QueuePropertyDto>>() {
		};
		queuePropertyDtos = response.getEntity(queuepropertyList);
		return queuePropertyDtos;
	}

	/**
	 * 
	 * Récupération de la liste de propriétés de la file de traitement
	 * 
	 * @param queueDto
	 * @return List QueuePropertyDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<QueuePropertyDto> allProperties(QueueDto queueDto) throws RestException, ValidationException {
		return allProperties(queueDto.getName());
	}

	/**
	 * 
	 * Suppression d'une propriété de la file de traitement
	 * 
	 * @param nameQueue    : nom de la file de traitement
	 * @param nameProperty : nom de propriété
	 * @throws RestException       : nom de la file de traitement ou propriété
	 *                             n'existe pas ou erreur au niveau du serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException :nom de la file de traitement ou propriété sont
	 *                             null ou vides
	 */
	public void deleteProperty(String nameQueue, String nameProperty) throws RestException, ValidationException {
		if (nameQueue != null && !nameQueue.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Queue name or property empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une propriété de la file de traitement
	 * 
	 * @param queueDto
	 * @param nameProperty : nom de propriété
	 * @throws RestException       : nom de la file de traitement ou propriété
	 *                             n'existe pas ou erreur au niveau du serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException :nom de la file de traitement ou propriété sont
	 *                             null ou vides
	 */
	public void deleteProperty(QueueDto queueDto, String nameProperty) throws RestException, ValidationException {
		deleteProperty(queueDto.getName(), nameProperty);
	}

	/**
	 * 
	 * Suppression d'une file de traitement
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public void delete(String nameQueue) throws RestException, ValidationException {
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Queue name empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une file de traitement
	 * 
	 * @param queueDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public void delete(QueueDto queueDto) throws RestException, ValidationException {
		delete(queueDto.getName());
	}

	/**
	 * 
	 * Création de la propriété d'une file de traitement
	 * 
	 * @param nameQueue        : nom de la file de traitement
	 * @param queuePropertyDto
	 * @return QueuePropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom de la fle de traitement ou propriété sont
	 *                             null ou vide
	 */
	public QueuePropertyDto createProperty(String nameQueue, QueuePropertyDto queuePropertyDto)
			throws RestException, ValidationException {
		if (nameQueue != null && !nameQueue.isEmpty() && queuePropertyDto != null) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/property")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, queuePropertyDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return retrieveProperty(nameQueue, queuePropertyDto.getName());
		} else {
			throw new ValidationException("Queue name or property are null or empty");
		}
	}

	/**
	 * 
	 * Création de la propriété d'une file de traitement
	 * 
	 * @param queueDto
	 * @param queuePropertyDto
	 * @return QueuePropertyDto
	 * @throws RestException       : Erreur lors de la création de la propriété ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom de la fle de traitement ou propriété sont
	 *                             null ou vide
	 */
	public QueuePropertyDto createProperty(QueueDto queueDto, QueuePropertyDto queuePropertyDto)
			throws RestException, ValidationException {
		return createProperty(queueDto, queuePropertyDto);
	}

	/**
	 * 
	 * Récupération de la propriété d'une file de traitement
	 * 
	 * @param queueName    : nom de la file de traitement
	 * @param nameProperty : nom de la propriété
	 * @return QueuePropertyDto
	 * @throws RestException       : nom de la file de traitement ou nom de la
	 *                             propriété n'existe pas ou erreur au niveau du
	 *                             serveur (code différent de 200 OK)
	 * @throws ValidationException : nom de la file de traitement ou nom de la
	 *                             propriété sont null ou vides
	 */
	public QueuePropertyDto retrieveProperty(String queueName, String nameProperty)
			throws RestException, ValidationException {
		QueuePropertyDto queuePropertyDto;
		if (queueName != null && !queueName.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + queueName + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<QueuePropertyDto> queueGenericType = new GenericType<QueuePropertyDto>() {
			};
			queuePropertyDto = response.getEntity(queueGenericType);
			return queuePropertyDto;
		} else {
			throw new ValidationException("Queue name or property are empty or null");
		}
	}

	/**
	 * 
	 * Récupération de la propriété d'une file de traitement
	 * 
	 * @param queueDto
	 * @param nameProperty : nom de la propriété
	 * @return QueuePropertyDto
	 * @throws RestException       : nom de la file de traitement ou nom de la
	 *                             propriété n'existe pas ou erreur au niveau du
	 *                             serveur (code différent de 200 OK)
	 * @throws ValidationException : nom de la file de traitement ou nom de la
	 *                             propriété sont null ou vides
	 */
	public QueuePropertyDto retrieveProperty(QueueDto queueDto, String nameProperty)
			throws RestException, ValidationException {
		return retrieveProperty(queueDto.getName(), nameProperty);
	}

	/**
	 * 
	 * Récupération du groupe
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return List GroupeDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(String nameQueue) throws RestException, ValidationException {
		List<GroupeDto> groupeDtos;
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/groups")
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
			throw new ValidationException("Queue name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération du groupe
	 * 
	 * @param queueDto
	 * @return List GroupeDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(QueueDto queueDto) throws RestException, ValidationException {
		return retrieveGroup(queueDto.getName());
	}

	/**
	 * 
	 * Récupération des tables d'exploitations
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return List TableExploitDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public List<TableExploitDtoRest> retrieveTableExploit(String nameQueue) throws RestException, ValidationException {
		List<TableExploitDtoRest> tableExploitDtos;
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/tableExploit")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<TableExploitDtoRest>> tableExploitGenericList = new GenericType<List<TableExploitDtoRest>>() {
			};
			tableExploitDtos = response.getEntity(tableExploitGenericList);
			return tableExploitDtos;
		} else {
			throw new ValidationException("Queue name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération des tables d'exploitations
	 * 
	 * @param queueDto
	 * @return List TableExploitDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public List<TableExploitDtoRest> retrieveTableExploit(QueueDto queueDto) throws RestException, ValidationException {
		return retrieveTableExploit(queueDto.getName());
	}

	/**
	 * 
	 * Mise à jour de la propriété d'une file de traitement
	 * 
	 * @param nameQueue        : nom de la file de traitement
	 * @param queuePropertyDto
	 * @return QueuePropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             de la file de traitement ou niveau serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException : nom de la file de traitement ou de la propriété
	 *                             sont null ou vides
	 */
	public QueuePropertyDto updateProperty(String nameQueue, QueuePropertyDto queuePropertyDto)
			throws RestException, ValidationException {
		String nameproperty = queuePropertyDto.getName();
		if (nameQueue != null && !nameQueue.isEmpty() && nameproperty != null && !nameproperty.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/property/" + nameproperty)
					.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, queuePropertyDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return retrieveProperty(nameQueue, nameproperty);
		} else {
			throw new ValidationException("Queue name or property are empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour de la propriété d'une file de traitement
	 * 
	 * @param queueDto
	 * @param queuePropertyDto
	 * @return QueuePropertyDto
	 * @throws RestException       : Erreur lors de la mise à jour de la propriété
	 *                             de la file de traitement ou niveau serveur (code
	 *                             différent de 204 NoContent)
	 * @throws ValidationException : nom de la file de traitement ou de la propriété
	 *                             sont null ou vides
	 */
	public QueuePropertyDto updateProperty(QueueDto queueDto, QueuePropertyDto queuePropertyDto)
			throws RestException, ValidationException {
		return updateProperty(queueDto.getName(), queuePropertyDto);
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @param nameGroup : nom du groupe
	 * @throws RestException       : nom du groupe ou de la file de traitement
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou de la file de traitement sont
	 *                             null ou vides
	 */
	public void addGroup(String nameQueue, String nameGroup) throws RestException, ValidationException {
		if (nameGroup != null && !nameGroup.isEmpty() && nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Group name or Queue name are empty or null");
		}
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param queueDto
	 * @param groupeDto
	 * @throws RestException       : nom du groupe ou de la file de traitement
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou de la file de traitement sont
	 *                             null ou vides
	 */
	public void addGroup(QueueDto queueDto, GroupeDto groupeDto) throws RestException, ValidationException {
		addGroup(queueDto.getName(), groupeDto.getName());
	}

	/**
	 * 
	 * ajout d'une table d'exploitation
	 * 
	 * @param nameQueue        : nom de la file de traitement
	 * @param nameTableExploit : nom de la table d'exploitation
	 * @throws RestException       : nom de table d'exploitation ou de la file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom de table d'exploitation ou de la file de
	 *                             traitement sont null ou vides
	 */
	public void addTableExploit(String nameQueue, String nameTableExploit) throws RestException, ValidationException {
		if (nameTableExploit != null && !nameTableExploit.isEmpty() && nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/tableExploit/" + nameTableExploit)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Table d'exploit name or Queue name are empty or null");
		}
	}

	/**
	 * 
	 * ajout d'une table d'exploitation
	 * 
	 * @param queueDto
	 * @param tableExploitDto
	 * @throws RestException       : nom de table d'exploitation ou de la file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom de table d'exploitation ou de la file de
	 *                             traitement sont null ou vides
	 */
	public void addTableExploit(QueueDto queueDto, TableExploitDto tableExploitDto)
			throws RestException, ValidationException {
		addTableExploit(queueDto.getName(), tableExploitDto.getName());
	}

	/**
	 * 
	 * Récupération du statut de la file de traitement
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return QueueStatusDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public QueueStatusDto status(String nameQueue) throws RestException, ValidationException {
		QueueStatusDto queueStatusDto = null;
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/status")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<QueueStatusDto> queueStatusGeneric = new GenericType<QueueStatusDto>() {
			};
			queueStatusDto = response.getEntity(queueStatusGeneric);
			return queueStatusDto;
		} else {
			throw new ValidationException("Queue name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération du statut de la file de traitement
	 * 
	 * @param queueDto
	 * @return QueueStatusDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement est null ou vide
	 */
	public QueueStatusDto status(QueueDto queueDto) throws RestException, ValidationException {
		return status(queueDto.getName());
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @param nameGroup : nom du groupe
	 * @throws RestException       : nom du groupe ou de la file de traitement
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou de la file de traitement sont
	 *                             null ou vides
	 */
	public void removeGroup(String nameQueue, String nameGroup) throws RestException, ValidationException {
		if (nameQueue != null && !nameQueue.isEmpty() && nameGroup != null && !nameGroup.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Queue name or group name are empty or null");
		}
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param queueDto
	 * @param groupeDto
	 * @throws RestException       : nom du groupe ou de la file de traitement
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou de la file de traitement sont
	 *                             null ou vides
	 */
	public void removeGroup(QueueDto queueDto, GroupeDto groupeDto) throws RestException, ValidationException {
		removeGroup(queueDto.getName(), groupeDto.getName());
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés à la file de traitement
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @return List JobDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<JobDto> allJobs(String nameQueue) throws RestException, ValidationException {
		List<JobDto> jobListDto;
		if (nameQueue == null || nameQueue.isEmpty()) {
			throw new ValidationException("Queue name is null or empty");
		}
		Builder builder = baseWebRessource.path("queues/" + nameQueue + "/jobs").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<JobDto>> jobGeneric = new GenericType<List<JobDto>>() {
		};
		jobListDto = response.getEntity(jobGeneric);
		return jobListDto;
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés à la file de traitement
	 * 
	 * @param queueDto
	 * @return List JobDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<JobDto> allJobs(QueueDto queueDto) throws RestException, ValidationException {
		return allJobs(queueDto.getName());
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés à la file de traitement par status
	 * 
	 * @param nameQueue : nom de la file de traitement
	 * @param status    : status du job
	 * @return List JobDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<JobDto> jobsByStatus(String nameQueue, JobStatusType status) throws RestException, ValidationException {
		List<JobDto> jobListDto;
		if (nameQueue == null || nameQueue.isEmpty()) {
			throw new ValidationException("Queue name is null or empty");
		}
		Builder builder = baseWebRessource.path("queues/" + nameQueue + "/jobs/status/" + status)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<JobDto>> jobGeneric = new GenericType<List<JobDto>>() {
		};
		jobListDto = response.getEntity(jobGeneric);
		return jobListDto;
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés à la file de traitement par status
	 * 
	 * @param queueDto
	 * @param status   : status du job
	 * @return List JobDto
	 * @throws RestException       : nom de la file de traitement n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public List<JobDto> jobsByStatus(QueueDto queueDto, JobStatusType status)
			throws RestException, ValidationException {
		return jobsByStatus(queueDto.getName(), status);
	}

	/**
	 * 
	 * retire la table d'exploitation
	 * 
	 * @param nameQueue        : nom de la file de traitement
	 * @param nameTableExploit : nom de la table d'exploitation
	 * @throws RestException       : nom de la file de traitement ou table
	 *                             d'exploitation n'existe pas ,ou erreur au niveau
	 *                             du serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom de la file de traitement ou table
	 *                             d'exploitation sont null ou vides
	 */
	public void removeTableExploit(String nameQueue, String nameTableExploit)
			throws RestException, ValidationException {
		if (nameQueue != null && !nameQueue.isEmpty() && nameTableExploit != null && !nameTableExploit.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/tableExploit/" + nameTableExploit)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Queue name or table exploit name are empty or null");
		}
	}

	/**
	 * 
	 * retire la table d'exploitation
	 * 
	 * @param queueDto
	 * @param tableExploitDto
	 * @throws RestException       : nom de la file de traitement ou table
	 *                             d'exploitation n'existe pas ,ou erreur au niveau
	 *                             du serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom de la file de traitement ou table
	 *                             d'exploitation sont null ou vides
	 */
	public void removeTableExploit(QueueDto queueDto, TableExploitDto tableExploitDto)
			throws RestException, ValidationException {
		removeTableExploit(queueDto.getName(), tableExploitDto.getName());
	}

	/**
	 * 
	 * Création d'une file de traitement
	 * 
	 * @param nameQueue : nom de la queue a créer
	 * @param nameModel : nom du modèle
	 * @return QueueDto
	 * @throws RestException       : Erreur lors de la création de la file de
	 *                             traitement ou niveau serveur (code différent de
	 *                             201 Created)
	 * @throws ValidationException : nom de la queue ou model sont nul ous vides
	 */
	public QueueDto create(String nameQueue, String nameModel) throws RestException, ValidationException {
		if (nameModel != null && !nameModel.isEmpty() && nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/model/" + nameModel)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return byName(nameQueue);
		} else {
			throw new ValidationException("Queue name or model are null or empty");
		}
	}

	/**
	 * 
	 * Mise à jour d'une file de traitement
	 * 
	 * @param queueDto
	 * @return QueueDto
	 * @throws RestException       : Erreur lors de la mise à jour d'une file de
	 *                             traitement ou niveau serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom de la file de traitement null ou vide
	 */
	public QueueDto update(QueueDto queueDto) throws RestException, ValidationException {
		String nameQueue = queueDto.getName();
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, queueDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameQueue);
		} else {
			throw new ValidationException("Queue name is empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour du statut de la file de traitement
	 * 
	 * @param nameQueue  : nom de la file de traitement
	 * @param status
	 * @param statusDesc
	 * @return QueueStatusDto
	 * @throws RestException       : Erreur lors de la mise à jour du status ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom de la file de traitement sont null ou vides
	 */
	public QueueDto updateStatus(String nameQueue, QueueStatusType status, String statusDesc)
			throws RestException, ValidationException {
		QueueStatusDto queueStatusDto = new QueueStatusDto();
		queueStatusDto.setStatus(status.name());
		queueStatusDto.setStatusDesc(statusDesc);
		if (nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("queues/" + nameQueue + "/status")
					.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, queueStatusDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameQueue);
		} else {
			throw new ValidationException("Queue name empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour du statut de la file de traitement
	 * 
	 * @param queueDto
	 * @param status
	 * @param statusDesc
	 * @return QueueStatusDto
	 * @throws RestException       : Erreur lors de la mise à jour du status ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom de la file de traitement sont null ou vides
	 */
	public QueueDto updateStatus(QueueDto queueDto, QueueStatusType status, String statusDesc)
			throws RestException, ValidationException {
		return updateStatus(queueDto.getName(), status, statusDesc);
	}

}
