package fr.datasyscom.scopiom.rest.mailSend;

import java.util.Properties;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import fr.datasyscom.pome.ejbsession.mailsend.MailSendManagerLocal;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.MailSendConfigDto;

@Path("/mailConfig")
public class MailConfigRestWS {

	@EJB
	MailSendManagerLocal mailSendLocal;

	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupération de la configuration de l'envoi de mails.
	 * 
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveMailConfigs() {
		MailSendConfigDto mailConfigPropDto;
		try {
			Properties mailConfigProp = mailSendLocal.getMailSendConfiguration();
			mailConfigPropDto = new MailSendConfigDto(mailConfigProp);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(mailConfigPropDto).build();
	}

	/**
	 * 
	 * Mise à jour de la configuration de l'envoi de mails
	 * 
	 * @return noContent
	 */
	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateMailConfigs(MailSendConfigDto mailSendConfigDto) {
		if (mailSendConfigDto != null) {
			try {
				Properties mailConfigProp = mailSendLocal.getMailSendConfiguration();
				for (Entry<Object, Object> entry : mailSendConfigDto.computeProperties().entrySet()) {
					mailConfigProp.setProperty(entry.getKey().toString(), entry.getValue().toString());
				}
				mailSendLocal.setMailSendConfiguration(mailConfigProp);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * création de la configuration d'envoi de mails
	 * 
	 * @return noContent
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createMailConfigs(MailSendConfigDto mailSendConfigDto) {
		if (mailSendConfigDto != null) {
			try {
				mailSendLocal.setMailSendConfiguration(mailSendConfigDto.computeProperties());
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.status(Status.CREATED).build();
	}

}
