package fr.datasyscom.scopiom.restclient.service;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.LpdDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeTmpDto;
import fr.datasyscom.scopiom.rest.pojo.ScanWaitDto;
import fr.datasyscom.scopiom.rest.services.ServicesInfos;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class ServiceClient {

	WebResource baseWebRessource;

	public ServiceClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération des statuts de services
	 * 
	 * @return ServicesInfos
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public ServicesInfos infos() throws RestException {
		Builder builder = baseWebRessource.path("services").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<ServicesInfos> infosServicesGeneric = new GenericType<ServicesInfos>() {
		};
		return response.getEntity(infosServicesGeneric);
	}

	/**
	 * 
	 * Récupération des informations de purge
	 * 
	 * @return PurgeDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public PurgeDto purge() throws RestException {
		Builder builder = baseWebRessource.path("services/purge").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<PurgeDto> purgeGeneric = new GenericType<PurgeDto>() {
		};
		return response.getEntity(purgeGeneric);
	}

	/**
	 * 
	 * Mise à jour du service purge
	 * 
	 * @param purgeDto
	 * @return PurgeDto
	 * @throws RestException       : Erreur lors de la mise à jour du service Purge
	 *                             ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : purge null
	 */
	public PurgeDto updatePurge(PurgeDto purgeDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("services/purge").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, purgeDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return this.purge();
	}

	/**
	 * 
	 * Récupération des informations de purge temp
	 * 
	 * @return PurgeTmpDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public PurgeTmpDto purgeTmp() throws RestException {
		Builder builder = baseWebRessource.path("services/purgetmp").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<PurgeTmpDto> purgeTmpGeneric = new GenericType<PurgeTmpDto>() {
		};
		return response.getEntity(purgeTmpGeneric);
	}

	/**
	 * 
	 * Mise à jour du service purge temp
	 * 
	 * @param purgeTmpDto
	 * @return PurgeTmpDto
	 * @throws RestException       : Erreur lors de la mise à jour du service Purge
	 *                             temp ou niveau serveur (code différent de 204
	 *                             NoContent)
	 * @throws ValidationException : purge temp null
	 */
	public PurgeTmpDto updatePurgeTmp(PurgeTmpDto purgeTmpDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("services/purgetmp").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, purgeTmpDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return this.purgeTmp();
	}

	/**
	 * 
	 * Récupération des informations du LPD
	 * 
	 * @return LpdDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public LpdDto lpd() throws RestException {
		Builder builder = baseWebRessource.path("services/lpd").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<LpdDto> lpdGeneric = new GenericType<LpdDto>() {
		};
		return response.getEntity(lpdGeneric);
	}

	/**
	 * 
	 * Mise à jour du service Lpd
	 * 
	 * @param lpdDto
	 * @return PurgeTmpDto
	 * @throws RestException       : Erreur lors de la mise à jour du service Lpd ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : Lpd null
	 */
	public LpdDto updateLpd(LpdDto lpdDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("services/lpd").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, lpdDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return this.lpd();
	}

	/**
	 * 
	 * Récupération des informations de Scheduler
	 * 
	 * @return ScanWaitDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public ScanWaitDto scheduler() throws RestException {
		Builder builder = baseWebRessource.path("services/scheduler").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<ScanWaitDto> scanWaitGeneric = new GenericType<ScanWaitDto>() {
		};
		return response.getEntity(scanWaitGeneric);
	}

	/**
	 * 
	 * Mise à jour du service scheduler
	 * 
	 * @param scanWaitDto
	 * @return ScanWaitDto
	 * @throws RestException       : Erreur lors de la mise à jour du service
	 *                             Scheduler ou niveau serveur (code différent de
	 *                             204 NoContent)
	 * @throws ValidationException : scheduler null
	 */
	public ScanWaitDto updateScheduler(ScanWaitDto scanWaitDto) throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("services/scheduler").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, scanWaitDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return this.scheduler();
	}

	/**
	 * 
	 * Lance le service de purge
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void startPurge() throws RestException {
		Builder builder = baseWebRessource.path("services/purge/start").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Lance une purge manuelle
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void lunchPurgeManual() throws RestException {
		Builder builder = baseWebRessource.path("services/purge/startManual").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Arrete le service de purge
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void stopPurge() throws RestException {
		Builder builder = baseWebRessource.path("services/purge/stop").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Lance le service de purge temp
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void startPurgeTmp() throws RestException {
		Builder builder = baseWebRessource.path("services/purgetmp/start").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Arrete le service de purge temp
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void stopPurgeTmp() throws RestException {
		Builder builder = baseWebRessource.path("services/purgetmp/stop").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Lance le service de Lpd
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void startLpd() throws RestException {
		Builder builder = baseWebRessource.path("services/lpd/start").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Arrete le service de Lpd
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void stopLpd() throws RestException {
		Builder builder = baseWebRessource.path("services/lpd/stop").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Lance le service de scheduler
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void startScheduler() throws RestException {
		Builder builder = baseWebRessource.path("services/scheduler/start").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Arrete le service de scheduler
	 * 
	 * @throws RestException : erreur niveau serveur (code différent de 200 OK)
	 */
	public void stopScheduler() throws RestException {
		Builder builder = baseWebRessource.path("services/scheduler/stop").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

}
