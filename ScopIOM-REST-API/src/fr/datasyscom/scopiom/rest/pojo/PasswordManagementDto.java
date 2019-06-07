package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbsession.passwordmanagement.PasswordManagementLocal;
import fr.datasyscom.pome.ejbsession.passwordmanagement.PasswordValidationLocal;

@XmlRootElement
public class PasswordManagementDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean canResetPassword;

	private Boolean needValidatePassword;

	private Integer minLengh;

	private Integer minLowercase;

	private Integer minUppercase;

	private Integer minDigits;

	private Integer minSpecialCharacters;

	public PasswordManagementDto() {
		super();
	}

	public PasswordManagementDto(PasswordValidationLocal passValidationLocale,
			PasswordManagementLocal passwordManagementLocal) {
		super();
		this.canResetPassword = passwordManagementLocal.isPasswordResetEnabled();
		this.needValidatePassword = passValidationLocale.isEnabled();
		this.minLengh = passValidationLocale.getMinLength();
		this.minLowercase = passValidationLocale.getMinLowercase();
		this.minUppercase = passValidationLocale.getMinUppercase();
		this.minDigits = passValidationLocale.getMinDigits();
		this.minSpecialCharacters = passValidationLocale.getMinSpecialCharacters();
	}

	public Boolean getCanResetPassword() {
		return canResetPassword;
	}

	public void setCanResetPassword(Boolean canBeResetPassword) {
		this.canResetPassword = canBeResetPassword;
	}

	public Boolean getNeedValidatePassword() {
		return needValidatePassword;
	}

	public void setNeedValidatePassword(Boolean needValidatePassword) {
		this.needValidatePassword = needValidatePassword;
	}

	public Integer getMinLengh() {
		return minLengh;
	}

	public void setMinLengh(Integer minLengh) {
		this.minLengh = minLengh;
	}

	public Integer getMinLowercase() {
		return minLowercase;
	}

	public void setMinLowercase(Integer minLowercase) {
		this.minLowercase = minLowercase;
	}

	public Integer getMinUppercase() {
		return minUppercase;
	}

	public void setMinUppercase(Integer minUppercase) {
		this.minUppercase = minUppercase;
	}

	public Integer getMinDigits() {
		return minDigits;
	}

	public void setMinDigits(Integer minDigits) {
		this.minDigits = minDigits;
	}

	public Integer getMinSpecialCharacters() {
		return minSpecialCharacters;
	}

	public void setMinSpecialCharacters(Integer minSpecialCharacters) {
		this.minSpecialCharacters = minSpecialCharacters;
	}

}
