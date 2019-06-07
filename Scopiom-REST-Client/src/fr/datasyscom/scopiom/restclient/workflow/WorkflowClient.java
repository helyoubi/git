package fr.datasyscom.scopiom.restclient.workflow;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;

public class WorkflowClient {

	WebResource baseWebRessource;

	public WorkflowClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération de la liste des flux de travail
	 * 
	 * @return List WorkflowDto
	 * @throws RestException : erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<WorkflowDto> all() throws RestException {
		Builder builder = baseWebRessource.path("workflows").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<WorkflowDto>> workflowList = new GenericType<List<WorkflowDto>>() {
		};
		List<WorkflowDto> workflowDtos = response.getEntity(workflowList);
		return workflowDtos;
	}

	/**
	 * 
	 * Récupération du flux de travail par son identifiant
	 * 
	 * @param id : identifiant du flux de travail
	 * @return WorkflowDto
	 * @throws RestException : Identifiant du flux de travail n'existe pas ou erreur
	 *                       au niveau du serveur (code différent de 200 OK)
	 */
	public WorkflowDto byId(long id) throws RestException {
		WorkflowDto workflowDto = null;
		Builder builder = baseWebRessource.path("workflows").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<WorkflowDto>> listWorkflowDto = new GenericType<List<WorkflowDto>>() {
		};
		List<WorkflowDto> workflowList = response.getEntity(listWorkflowDto);
		workflowDto = workflowList.get(0);

		return workflowDto;
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param nameWorkflow : nom du flux de travail
	 * @param nameGroup    : nom du groupe
	 * @throws RestException       : nom du flux de travail ou du groupe n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 200 Ok)
	 * @throws ValidationException : nom du flux de travail ou du groupe sont null
	 *                             ou vides
	 */
	public void addGroup(String nameWorkflow, String nameGroup) throws RestException, ValidationException {
		if (nameGroup != null && !nameGroup.isEmpty() && nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + nameWorkflow + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Workflow name or Group name are empty or null");
		}
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param workflowDto
	 * @param groupeDto
	 * @throws RestException       : nom du flux de travail ou du groupe n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 200 Ok)
	 * @throws ValidationException : nom du flux de travail ou du groupe sont null
	 *                             ou vides
	 */
	public void addGroup(WorkflowDto workflowDto, GroupeDto groupeDto) throws RestException, ValidationException {
		addGroup(workflowDto.getNom(), groupeDto.getName());
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param nameWorkflow : nom du flux de travail
	 * @param nameGroup    : nom du groupe
	 * @throws RestException       : nom du groupe ou du flux de travail n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du flux de travail sont null
	 *                             ou vides
	 */
	public void removeGroup(String nameWorkflow, String nameGroup) throws RestException, ValidationException {
		if (nameWorkflow != null && !nameWorkflow.isEmpty() && nameGroup != null && !nameGroup.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + nameWorkflow + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Workflow name or group name are empty or null");
		}
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param workflowDto
	 * @param groupeDto
	 * @throws RestException       : nom du groupe ou du flux de travail n'existe
	 *                             pas ,ou erreur au niveau du serveur (code
	 *                             différent de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du flux de travail sont null
	 *                             ou vides
	 */
	public void removeGroup(WorkflowDto workflowDto, GroupeDto groupeDto) throws RestException, ValidationException {
		removeGroup(workflowDto.getNom(), groupeDto.getName());
	}

	/**
	 * 
	 * Récupération du groupe
	 * 
	 * @param nameWorkflow : nom du flux de travail
	 * @return List GroupeDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(String nameWorkflow) throws RestException, ValidationException {
		List<GroupeDto> groupeDtos;
		if (nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + nameWorkflow + "/groups")
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
			throw new ValidationException("Workflow name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération du groupe
	 * 
	 * @param workflowDto
	 * @return List GroupeDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(WorkflowDto workflowDto) throws RestException, ValidationException {
		return retrieveGroup(workflowDto.getNom());
	}

	/**
	 * 
	 * Récupération d'un flux de travail par nom
	 * 
	 * @param nameWorkflow : nom du flux de travail
	 * @return WorkflowDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail est null ou vide
	 */
	public WorkflowDto byName(String nameWorkflow) throws RestException, ValidationException {
		WorkflowDto workflowDto = null;
		if (nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + nameWorkflow).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<WorkflowDto> workflowGeneric = new GenericType<WorkflowDto>() {
			};
			workflowDto = response.getEntity(workflowGeneric);
			return workflowDto;
		} else {
			throw new ValidationException("Workflow name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération de/des groupes attachés au workflow
	 * 
	 * @param workflowName : nom du workflow
	 * @return List GroupeDto
	 * @throws RestException       : nom du workflow n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du workflow null ou vide
	 */
	public List<GroupeDto> groups(String workflow) throws RestException, ValidationException {
		List<GroupeDto> groupeDtos = null;
		if (workflow != null && !workflow.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + workflow + "/groups")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<GroupeDto>> groupeGenericList = new GenericType<List<GroupeDto>>() {
			};
			groupeDtos = response.getEntity(groupeGenericList);
			return groupeDtos;
		} else {
			throw new ValidationException("Workflow name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération de/des groupes attachés au workflow
	 * 
	 * @param workflowDto
	 * @return List GroupeDto
	 * @throws RestException       : nom du workflow n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du workflow null ou vide
	 */
	public List<GroupeDto> groups(WorkflowDto workflowDto) throws RestException, ValidationException {
		return groups(workflowDto.getNom());
	}

	/**
	 * 
	 * Suppression du workflow
	 * 
	 * @param workflowName : nom du workflow
	 * @throws RestException       : nom du workflow n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du workflow null ou vide
	 */
	public void delete(String workflowName) throws RestException, ValidationException {
		if (workflowName != null && !workflowName.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + workflowName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Workflow name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression du workflow
	 * 
	 * @param workflowDto
	 * @throws RestException       : Nom du workflow n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : nom du workflow null ou vide
	 */
	public void delete(WorkflowDto workflowDto) throws RestException, ValidationException {
		this.delete(workflowDto.getNom());
	}

	/**
	 * 
	 * Suppression d'un groupe du workflow
	 * 
	 * @param workflowName : nom du workflow
	 * @param groupName    : nom du groupe
	 * @throws RestException       : nom du workflow ou du groupe n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du workflow ou du groupe sont null ou vides
	 */
	public void deleteGroup(String workflowName, String groupName) throws RestException, ValidationException {
		if (workflowName != null && !workflowName.isEmpty() && groupName != null && !groupName.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + workflowName + "/groups/" + groupName)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Workflow or group name are empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un groupe du workflow
	 * 
	 * @param workflowDto
	 * @param groupeDto
	 * @throws RestException       : nom du workflow ou du groupe n'existe pas ,ou
	 *                             erreur au niveau du serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du workflow null ou vide
	 */
	public void deleteGroup(WorkflowDto workflowDto, GroupeDto groupeDto) throws RestException, ValidationException {
		this.deleteGroup(workflowDto.getNom(), groupeDto.getName());
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés au flux de travail par status
	 * 
	 * @param nameWorkflow : nom du flux de travail
	 * @param status       : status du job
	 * @return List JobDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail null ou vide
	 */
	public List<JobDto> jobsByStatus(String nameWorkflow, JobStatusType status)
			throws RestException, ValidationException {
		List<JobDto> jobListDto;
		if (nameWorkflow == null || nameWorkflow.isEmpty()) {
			throw new ValidationException("Workflow name is null or empty");
		}
		Builder builder = baseWebRessource.path("workflows/" + nameWorkflow + "/jobs/status/" + status)
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
	 * Récupération de la liste des jobs attachés au flux de travail par status
	 * 
	 * @param workflowDto
	 * @param status      : status du job
	 * @return List JobDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail null ou vide
	 */
	public List<JobDto> jobsByStatus(WorkflowDto workflowDto, JobStatusType status)
			throws RestException, ValidationException {
		return jobsByStatus(workflowDto.getNom(), status);
	}

	/**
	 * 
	 * Récupération de la liste des jobs attachés au flux de travail
	 * 
	 * @param nameWorklow : nom du flux de travail
	 * @return List JobDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail null ou vide
	 */
	public List<JobDto> allJobs(String nameWorklow) throws RestException, ValidationException {
		List<JobDto> jobListDto;
		if (nameWorklow == null || nameWorklow.isEmpty()) {
			throw new ValidationException("Workflow name is null or empty");
		}
		Builder builder = baseWebRessource.path("workflows/" + nameWorklow + "/jobs")
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
	 * Récupération de la liste des jobs attachés au flux de travail
	 * 
	 * @param workflowDto
	 * @return List JobDto
	 * @throws RestException       : nom du flux de travail n'existe pas ou erreur
	 *                             au niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du flux de travail null ou vide
	 */
	public List<JobDto> allJobs(WorkflowDto workflowDto) throws RestException, ValidationException {
		return allJobs(workflowDto.getNom());
	}

	/**
	 * 
	 * Mise à jour du workflow
	 * 
	 * @param workflowDto
	 * @return WorkflowDto
	 * @throws RestException       : Erreur lors de la mise à jour du workflow ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom de workflow est null ou vide
	 */
	public WorkflowDto update(WorkflowDto workflowDto) throws RestException, ValidationException {
		String nameWorkflow = workflowDto.getNom();
		if (nameWorkflow != null && !nameWorkflow.isEmpty()) {
			Builder builder = baseWebRessource.path("workflows/" + nameWorkflow).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, workflowDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameWorkflow);
		} else {
			throw new ValidationException("Workflow name is empty or null");
		}
	}

}
