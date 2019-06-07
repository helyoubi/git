package fr.datasyscom.scopiom.restclient.resourcegroup;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.ResourceGroupDto;
import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class ResourceGroupClient {

	WebResource baseWebRessource;

	public ResourceGroupClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération du groupe de ressource par son nom
	 * 
	 * @param nameRessourceGrp : nom du groupe de ressource
	 * @return ResourceGroupDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource est null ou vide
	 */
	public ResourceGroupDto byName(String nameRessourceGrp) throws RestException, ValidationException {
		ResourceGroupDto resourceGroupDto = null;
		if (nameRessourceGrp != null && !nameRessourceGrp.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameRessourceGrp)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<ResourceGroupDto> resourceGrpGeneric = new GenericType<ResourceGroupDto>() {
			};
			resourceGroupDto = response.getEntity(resourceGrpGeneric);
			return resourceGroupDto;
		} else {
			throw new ValidationException("Resource group name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération de la liste des groupes de ressources
	 * 
	 * @return List ResourceGroupDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<ResourceGroupDto> all() throws RestException {
		Builder builder = baseWebRessource.path("resourceGroups").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<ResourceGroupDto>> resourceGrpDto = new GenericType<List<ResourceGroupDto>>() {
		};
		List<ResourceGroupDto> resourceGrpGenericDto = response.getEntity(resourceGrpDto);
		return resourceGrpGenericDto;
	}

	/**
	 * 
	 * Création d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @return ResourceGroupDto
	 * @throws RestException       : Erreur lors de la création du groupe de
	 *                             ressource ou niveau serveur (code différent de
	 *                             201 Created)
	 * @throws ValidationException : Groupe de ressource est null
	 */
	public ResourceGroupDto create(ResourceGroupDto resourceGroupDto) throws RestException, ValidationException {
		if (resourceGroupDto.getName() != null && !resourceGroupDto.getName().isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups").accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, resourceGroupDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return byName(resourceGroupDto.getName());
		} else {
			throw new ValidationException("Resource group is null");
		}
	}

	/**
	 * 
	 * Suppression d'un groupe de ressource
	 * 
	 * @param nameRessourceGrp : nom du groupe de ressource
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource est null ou vide
	 */
	public void delete(String nameRessourceGrp) throws RestException, ValidationException {
		if (nameRessourceGrp != null && !nameRessourceGrp.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameRessourceGrp)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource est null ou vide
	 */
	public void delete(ResourceGroupDto resourceGroupDto) throws RestException, ValidationException {
		delete(resourceGroupDto.getName());
	}

	/**
	 * 
	 * Mise à jour du groupe de ressource
	 * 
	 * @param nameResourceGrp : le groupe de ressource à modifier
	 * @param resourceGroupDto
	 * @return ResourceGroupDto
	 * @throws RestException       : Erreur lors de la mise à jour du groupe de
	 *                             ressource ou niveau serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource est null ou vide
	 */
	public ResourceGroupDto update(String nameResourceGrp,ResourceGroupDto resourceGroupDto) throws RestException, ValidationException {
		if (nameResourceGrp == null || nameResourceGrp.isEmpty()) {
			throw new ValidationException("Resource group name is empty or null");
		}
		Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, resourceGroupDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return byName(resourceGroupDto.getName());
	}

	/**
	 * 
	 * Ajout d'un périphérique via un groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameDevice      : nom du périphérique
	 * @throws RestException       : nom du groupe de ressource ou du périphérique
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou périphérique sont
	 *                             null ou vides
	 */
	public void addDevice(String nameResourceGrp, String nameDevice) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/devices/" + nameDevice)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name or device name are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'un périphérique via un groupe de ressource
	 * 
	 * @param resourceGroupDtoe
	 * @param deviceDto
	 * @throws RestException       : nom du groupe de ressource ou du périphérique
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou périphérique sont
	 *                             null ou vides
	 */
	public void addDevice(ResourceGroupDto resourceGroupDto, DeviceDto deviceDto)
			throws RestException, ValidationException {
		addDevice(resourceGroupDto.getName(), deviceDto.getName());
	}

	/**
	 * 
	 * Récupération des devices d'un groupe de ressource
	 * 
	 * @param nameRessourceGrp : nom du groupe de ressource
	 * @return List DeviceDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<DeviceDto> retrieveDevices(String nameRessourceGrp) throws RestException, ValidationException {
		List<DeviceDto> deviceDtos = null;
		if (nameRessourceGrp != null && !nameRessourceGrp.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameRessourceGrp + "/devices")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<DeviceDto>> deviceGenericList = new GenericType<List<DeviceDto>>() {
			};
			deviceDtos = response.getEntity(deviceGenericList);
			return deviceDtos;
		} else {
			throw new ValidationException("Group ressource name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération des devices d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @return List DeviceDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<DeviceDto> retrieveDevices(ResourceGroupDto resourceGroupDto)
			throws RestException, ValidationException {
		return retrieveDevices(resourceGroupDto.getName());
	}

	/**
	 * 
	 * Récupération des files de traitements d'un groupe de ressource
	 * 
	 * @param nameRessourceGrp : nom du groupe de ressource
	 * @return List QueueDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<QueueDto> retrieveQueues(String nameRessourceGrp) throws RestException, ValidationException {
		List<QueueDto> queueDtos = null;
		if (nameRessourceGrp != null && !nameRessourceGrp.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameRessourceGrp + "/queues")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<QueueDto>> queueGenericList = new GenericType<List<QueueDto>>() {
			};
			queueDtos = response.getEntity(queueGenericList);
			return queueDtos;
		} else {
			throw new ValidationException("Group ressource name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération des files de traitements d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @return List QueueDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<QueueDto> retrieveQueues(ResourceGroupDto resourceGroupDto) throws RestException, ValidationException {
		return retrieveQueues(resourceGroupDto.getName());
	}

	/**
	 * 
	 * Récupération des flux de traitements d'un groupe de ressource
	 * 
	 * @param nameRessourceGrp : nom du groupe de ressource
	 * @return List WorkflowDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<WorkflowDto> retrieveWorkflow(String nameRessourceGrp) throws RestException, ValidationException {
		List<WorkflowDto> workflowDtos = null;
		if (nameRessourceGrp != null && !nameRessourceGrp.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameRessourceGrp + "/workflows")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<WorkflowDto>> workflowsGenericList = new GenericType<List<WorkflowDto>>() {
			};
			workflowDtos = response.getEntity(workflowsGenericList);
			return workflowDtos;
		} else {
			throw new ValidationException("Group ressource name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération des flux de traitements d'un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @return List WorkflowDto
	 * @throws RestException       : nom du groupe de ressource n'existe pas ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             200 OK)
	 * @throws ValidationException : nom du groupe de ressource null ou vide
	 */
	public List<WorkflowDto> retrieveWorkflow(ResourceGroupDto resourceGroupDto)
			throws RestException, ValidationException {
		return retrieveWorkflow(resourceGroupDto.getName());
	}

	/**
	 * 
	 * Suppression d'un device du groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameDevice      : nom du périphérique
	 * @throws RestException       : nom du groupe de ressource ou du périphérique
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou du périphérique
	 *                             sont null ou vides
	 */
	public void removeDevice(String nameResourceGrp, String nameDevice) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/devices/" + nameDevice)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group or device name are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un device du groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @param deviceDto
	 * @throws RestException       : nom du groupe de ressource ou du périphérique
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou du périphérique
	 *                             sont null ou vides
	 */
	public void removeDevice(ResourceGroupDto resourceGroupDto, DeviceDto deviceDto)
			throws RestException, ValidationException {
		removeDevice(resourceGroupDto.getName(), deviceDto.getName());
	}

	/**
	 * 
	 * Ajout d'une file de traitement à un groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameQueue       : nom de la file de traitement
	 * @throws RestException       : nom du groupe de ressource ou file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou file de
	 *                             traitement sont null ou vides
	 */
	public void addQueue(String nameResourceGrp, String nameQueue) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/queues/" + nameQueue)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name or Queue are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'une file de traitement à un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @param queueDto
	 * @throws RestException       : nom du groupe de ressource ou file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou file de
	 *                             traitement sont null ou vides
	 */
	public void addQueue(ResourceGroupDto resourceGroupDto, QueueDto queueDto)
			throws RestException, ValidationException {
		addQueue(resourceGroupDto.getName(), queueDto.getName());
	}

	/**
	 * 
	 * Suppression d'une file de traitement du groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameQueue       : nom de la file du traitement
	 * @throws RestException       : nom du groupe de ressource ou de la file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou de la file de
	 *                             traitement sont null ou vides
	 */
	public void removeQueue(String nameResourceGrp, String nameQueue) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameQueue != null && !nameQueue.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/queues/" + nameQueue)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name or Queue are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une file de traitement du groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @param queueDto
	 * @throws RestException       : nom du groupe de ressource ou de la file de
	 *                             traitement n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou de la file de
	 *                             traitement sont null ou vides
	 */
	public void removeQueue(ResourceGroupDto resourceGroupDto, QueueDto queueDto)
			throws RestException, ValidationException {
		removeQueue(resourceGroupDto.getName(), queueDto.getName());
	}

	/**
	 * 
	 * Ajout d'un flux de travail à un groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameWorkflow    : nom du flux de travail
	 * @throws RestException       : nom du groupe de ressource ou flux de travail
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou file de
	 *                             traitement sont null ou vides
	 */
	public void addWorkflow(String nameResourceGrp, String nameWorkflow) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/workflows/" + nameWorkflow)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name or Queue are empty or null");
		}
	}

	/**
	 * 
	 * Ajout d'un flux de travail à un groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @param workflowDto
	 * @throws RestException       : nom du groupe de ressource ou flux de travail
	 *                             n'existe pas ,ou erreur au niveau du serveur
	 *                             (code différent de 200 Ok)
	 * @throws ValidationException : nom du groupe de ressource ou file de
	 *                             traitement sont null ou vides
	 */
	public void addWorkflow(ResourceGroupDto resourceGroupDto, WorkflowDto workflowDto)
			throws RestException, ValidationException {
		addWorkflow(resourceGroupDto.getName(), workflowDto.getNom());
	}

	/**
	 * 
	 * Suppression d'un flux de travail du groupe de ressource
	 * 
	 * @param nameResourceGrp : nom du groupe de ressource
	 * @param nameQueue       : nom de la file du traitement
	 * @throws RestException       : nom du groupe de ressource ou du flux de
	 *                             travail n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou du flux de
	 *                             travail sont null ou vides
	 */
	public void removeWorkflow(String nameResourceGrp, String nameWorkflow) throws RestException, ValidationException {
		if (nameResourceGrp != null && !nameResourceGrp.isEmpty() && nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("resourceGroups/" + nameResourceGrp + "/workflows/" + nameWorkflow)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Resource group name or Workflow are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un flux de travail du groupe de ressource
	 * 
	 * @param resourceGroupDto
	 * @param workflowDto
	 * @throws RestException       : nom du groupe de ressource ou du flux de
	 *                             travail n'existe pas ,ou erreur au niveau du
	 *                             serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du groupe de ressource ou du flux de
	 *                             travail sont null ou vides
	 */
	public void removeWorkflow(ResourceGroupDto resourceGroupDto, WorkflowDto workflowDto)
			throws RestException, ValidationException {
		removeWorkflow(resourceGroupDto.getName(), workflowDto.getNom());
	}

}
