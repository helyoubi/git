package fr.datasyscom.scopiom.restclient.mailSend;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.MailSendConfigDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class MailConfigClient {

	WebResource baseWebRessource;

	public MailConfigClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération de la configuration de l'envoi de mails
	 * 
	 * @return List MailSendConfigDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public MailSendConfigDto retrieve() throws RestException {
		Builder builder = baseWebRessource.path("mailConfig").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<MailSendConfigDto> mailSendDto = new GenericType<MailSendConfigDto>() {
		};
		return response.getEntity(mailSendDto);
	}

	/**
	 * 
	 * création de la configuration d'envoi de mails
	 * 
	 * @param mailSendConfigDto
	 * @return MailSendConfigDto
	 * @throws RestException       : Erreur lors de la création de la configuration
	 *                             ou niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : mail configuration is null
	 *
	 */
	public MailSendConfigDto create(MailSendConfigDto mailSendConfigDto) throws RestException, ValidationException {
		if (mailSendConfigDto != null) {
			Builder builder = baseWebRessource.path("mailConfig").accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, mailSendConfigDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return retrieve();
		} else {
			throw new ValidationException("Mail configuration is null");
		}
	}

	/**
	 * 
	 * Mise à jour de la configuration d'envoi de mails
	 * 
	 * @param mailSendConfigDto
	 * @return MailSendConfigDto
	 * @throws RestException       : Erreur lors de la mise à jour de la
	 *                             configuration ou niveau serveur (code différent
	 *                             de 204 NoContent)
	 * @throws ValidationException : mail configuration is null
	 */
	public MailSendConfigDto update(MailSendConfigDto mailSendConfigDto) throws RestException, ValidationException {
		if (mailSendConfigDto != null) {
			Builder builder = baseWebRessource.path("mailConfig").accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, mailSendConfigDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return retrieve();
		} else {
			throw new ValidationException("Mail configuration is null");
		}
	}

}
