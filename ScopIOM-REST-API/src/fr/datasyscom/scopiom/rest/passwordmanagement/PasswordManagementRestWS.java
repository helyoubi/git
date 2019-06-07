package fr.datasyscom.scopiom.rest.passwordmanagement;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.datasyscom.pome.ejbsession.passwordmanagement.PasswordManagementLocal;
import fr.datasyscom.pome.ejbsession.passwordmanagement.PasswordValidationLocal;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.PasswordManagementDto;

@Path("/passwordManagement")
public class PasswordManagementRestWS {

	@EJB
	PasswordManagementLocal passwordManagementLocal;
	@EJB
	PasswordValidationLocal passValidationLocale;

	/**
	 * 
	 * Retourne les informations de la gestion du mot de passe
	 * 
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrievePasswordValidation() {
		PasswordManagementDto passManagementDto;
		try {
			passManagementDto = new PasswordManagementDto(passValidationLocale, passwordManagementLocal);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(passManagementDto).build();
	}

	/**
	 * 
	 * Modification des paramètres de validation du mot de passe
	 * 
	 * @param passwordManagementDto
	 * @return noContent
	 */
	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updatePasswordValidation(PasswordManagementDto passwordManagementDto) {
		if (passwordManagementDto == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try {
			if (passwordManagementDto.getCanResetPassword() != null) {
				passwordManagementLocal.setPasswordResetEnabled(passwordManagementDto.getCanResetPassword());
			}
			if (passwordManagementDto.getNeedValidatePassword() != null) {
				passValidationLocale.setEnabled(passwordManagementDto.getNeedValidatePassword());
			}
			if (passwordManagementDto.getMinDigits() != null && passwordManagementDto.getMinDigits() >= 0) {
				passValidationLocale.setMinDigits(passwordManagementDto.getMinDigits());
			}
			if (passwordManagementDto.getMinLowercase() != null && passwordManagementDto.getMinLowercase() >= 0) {
				passValidationLocale.setMinLowercase(passwordManagementDto.getMinLowercase());
			}
			if (passwordManagementDto.getMinUppercase() != null && passwordManagementDto.getMinUppercase() >= 0) {
				passValidationLocale.setMinUppercase(passwordManagementDto.getMinUppercase());
			}
			if (passwordManagementDto.getMinLengh() != null && passwordManagementDto.getMinLengh() >= 0) {
				passValidationLocale.setMinLength(passwordManagementDto.getMinLengh());
			}
			if (passwordManagementDto.getMinSpecialCharacters() != null
					&& passwordManagementDto.getMinSpecialCharacters() >= 0) {
				passValidationLocale.setMinSpecialCharacters(passwordManagementDto.getMinSpecialCharacters());
			}
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.noContent().build();
	}

}
