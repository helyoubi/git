package fr.datasyscom.scopiom.restclient.agent;

import java.util.List;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.AgentDto;
import fr.datasyscom.scopiom.rest.pojo.AgentDto.AgentStatusType;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class AgentClient {

	WebResource baseWebRessource;

	public AgentClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération de l'agent par son identifiant
	 * 
	 * @param id : identifiant de l'agent
	 * @return AgentDto
	 * @throws RestException : Identifiant de l'agent n'existe pas ou erreur au
	 *                       niveau du serveur (code différent de 200 OK)
	 */
	public AgentDto byId(long id) throws RestException {
		AgentDto agentDto = null;
		Builder builder = baseWebRessource.path("agents").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<AgentDto>> listAgentsDto = new GenericType<List<AgentDto>>() {
		};
		List<AgentDto> angetList = response.getEntity(listAgentsDto);
		agentDto = angetList.get(0);

		return agentDto;
	}

	/**
	 * 
	 * Récupération de la liste des agents
	 * 
	 * @return List AgentDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<AgentDto> all() throws RestException {
		Builder builder = baseWebRessource.path("agents").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<AgentDto>> listAgentDto = new GenericType<List<AgentDto>>() {
		};
		List<AgentDto> agentList = response.getEntity(listAgentDto);
		return agentList;
	}

	/**
	 * 
	 * Récupération de l'agent par son adresse
	 * 
	 * @param agentAdress : Adresse ip de l'agent
	 * @return AgentDto
	 * @throws RestException       : adresse de l'agent n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 200 OK)
	 * @throws ValidationException : adresse de l'agent null ou vide
	 */
	public AgentDto byAdress(String agentAdress) throws RestException, ValidationException {
		AgentDto agentDto = null;
		if (agentAdress != null && !agentAdress.isEmpty()) {
			Builder builder = baseWebRessource.path("agents/" + agentAdress).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<AgentDto> agentGeneric = new GenericType<AgentDto>() {
			};
			agentDto = response.getEntity(agentGeneric);
			return agentDto;
		} else {
			throw new ValidationException("Agent adress is empty or null");
		}
	}

	/**
	 * 
	 * Suppression de l'agent par son adresse 
	 * 
	 * @param agentName : adresse de l'agent
	 * @throws RestException       : adresse de l'agent n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : adresse de l'agent null ou vide
	 */
	public void delete(String agentAdress) throws RestException, ValidationException {
		if (agentAdress != null && !agentAdress.isEmpty()) {
			Builder builder = baseWebRessource.path("agents/" + agentAdress).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Agent adress empty or null");
		}
	}

	/**
	 * 
	 * Suppression de l'agent
	 * 
	 * @param agentDto
	 * @throws RestException       : Adresse de l'agent n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : adresse agent null ou vide
	 */
	public void delete(AgentDto agentDto) throws RestException, ValidationException {
		this.delete(agentDto.getAdresse());
	}

	/**
	 * 
	 * Création de l'agent
	 * 
	 * @param agentDto
	 * @return AgentDto
	 * @throws RestException       : Erreur lors de la création de l'agent ou niveau
	 *                             serveur (code différent de 201 Created)
	 * @throws ValidationException : Adresse agent null ou vide
	 */
	public AgentDto create(AgentDto agentDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("agents").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, agentDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return byAdress(agentDto.getAdresse());
	}

	/**
	 * 
	 * Mise à jour de l'agent
	 * 
	 * @param agentDto
	 * @return AgentDto
	 * @throws RestException       : Erreur lors de la mise à jour de l'agent ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : Adresse agent null ou vide
	 */
	public AgentDto update(AgentDto agentDto) throws RestException, ValidationException {
		String adressAgent = agentDto.getAdresse();
		if (adressAgent != null && !adressAgent.isEmpty()) {
			Builder builder = baseWebRessource.path("agents/" + adressAgent).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, agentDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byAdress(adressAgent);
		} else {
			throw new ValidationException("Agent adress is empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour du statut de l'agent
	 * 
	 * @param adressAgent
	 * @param agentStatusType
	 * @throws RestException       : Erreur lors de la mise à jour du statut de
	 *                             l'agent ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : adresse d'agent ou statut sont null ou vides
	 */
	public void updateStatus(String adressAgent, AgentStatusType agentStatusType)
			throws RestException, ValidationException {
		if (adressAgent == null || adressAgent.isEmpty() && agentStatusType == null) {
			throw new ValidationException("Agent name or status empty or null");
		}
		Builder builder = baseWebRessource.path("agents/" + adressAgent + "/status/" + agentStatusType.name())
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
	}

}
