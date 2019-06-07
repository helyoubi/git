package fr.datasyscom.scopiom.restclient.info;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.appinfos.AppInfos;
import fr.datasyscom.scopiom.rest.health.HealthInfos;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class InfoClient {

	WebResource baseWebRessource;

	public InfoClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération de l'app infos
	 * 
	 * @return AppInfos
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public AppInfos infosApp() throws RestException {
		Builder builder = baseWebRessource.path("infos").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<AppInfos> infosGeneric = new GenericType<AppInfos>() {
		};
		return response.getEntity(infosGeneric);
	}

	/**
	 * 
	 * Récupération du health infos
	 * 
	 * @return HealthInfos
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public HealthInfos infosHealth() throws RestException {
		Builder builder = baseWebRessource.path("health").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<HealthInfos> infoshealthGeneric = new GenericType<HealthInfos>() {
		};
		return response.getEntity(infoshealthGeneric);
	}
}
