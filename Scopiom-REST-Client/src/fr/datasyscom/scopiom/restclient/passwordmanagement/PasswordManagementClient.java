package fr.datasyscom.scopiom.restclient.passwordmanagement;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.PasswordManagementDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class PasswordManagementClient {

	WebResource baseWebRessource;

	public PasswordManagementClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * R�cup�ration des informations de la gestion de mot de passe
	 * 
	 * @return PasswordManagementDto
	 * @throws RestException : Erreur au niveau du serveur (code diff�rent de 200
	 *                       OK)
	 */
	public PasswordManagementDto retrieve() throws RestException {
		Builder builder = baseWebRessource.path("passwordManagement").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<PasswordManagementDto> passwordManagementDto = new GenericType<PasswordManagementDto>() {
		};
		return response.getEntity(passwordManagementDto);
	}

	/**
	 * 
	 * Mise � jour des param�tres de validation d'un mot de passe
	 * 
	 * @param passwordManagementDto
	 * @return PasswordManagementDto
	 * @throws RestException       : Erreur lors de la mise � jour des param�tres de
	 *                             validation du mot de passe, ou niveau serveur
	 *                             (code diff�rent de 204 NoContent)
	 * @throws ValidationException : PasswordManagement est null
	 */
	public PasswordManagementDto update(PasswordManagementDto passwordManagementDto)
			throws RestException, ValidationException {
		Builder builder = baseWebRessource.path("passwordManagement").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, passwordManagementDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return retrieve();

	}

}
