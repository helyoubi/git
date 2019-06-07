package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.User;

@XmlRootElement
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Liste des profils possibles d'un User */
	public enum UserProfil {
		IOM_ADMIN, IOM_OP, IOM_USER
	}

	/** Identifiant de l'utilisateur */
	private String login;

	/** Mot de passe de l'utilisateur */
	private String password;

	/** Nom de l'utilisateur */
	private String nom;

	/** Prénom de l'utilisateur */
	private String prenom;

	/** Informations quelconques sur l'utilisateur */
	private String commentaire;

	/** Adresse e-mail de l'utilisateur */
	private String mail;

	/**
	 * Indique sur l'utilisateur a le droit de modifier son mot de passe.<br>
	 * Cela est notamment utile dans le cas d'un compte partagé par plusieurs
	 * utilisateurs.
	 */
	private Boolean canChangeInfos;

	/** Profil de l'utilisateur */
	private String profil;

	/** Indique si cet utilisateur peut etre supprimé */
	private Boolean mandatory;

	/** Id du device par défaut de l'utilisateur */
	private Long defaultDevice;

	/** Nom du device par défaut de l'utilisateur */
	private String defaultDeviceName;

	public static List<UserDto> fromList(List<User> users) {
		List<UserDto> res = new ArrayList<UserDto>(users.size());
		for (User user : users) {
			res.add(new UserDto(user));
		}
		return res;
	}

	public UserDto() {
	}

	public UserDto(User user) {
		Device defaultDevice = user.getDefaultDevice();
		if (defaultDevice != null) {
			this.defaultDevice = defaultDevice.getId();
			this.defaultDeviceName = defaultDevice.getName();
		}
		this.canChangeInfos = user.isCanChangeInfos();
		this.commentaire = user.getCommentaire();
		this.login = user.getLogin();
		this.mail = user.getMail();
		this.mandatory = user.isMandatory();
		this.nom = user.getNom();
		this.password = user.getPassword();
		this.prenom = user.getPrenom();
		this.profil = user.getProfil().name();
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public String getMail() {
		return mail;
	}

	public Boolean getCanChangeInfos() {
		return canChangeInfos;
	}

	public String getProfil() {
		return profil;
	}

	public Boolean isMandatory() {
		return mandatory;
	}

	public Long getDefaultDevice() {
		return defaultDevice;
	}

	public String getDefaultDeviceName() {
		return defaultDeviceName;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setCanChangeInfos(Boolean canChangeInfos) {
		this.canChangeInfos = canChangeInfos;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setDefaultDevice(Long defaultDevice) {
		this.defaultDevice = defaultDevice;
	}

	public void setDefaultDeviceName(String defaultDeviceName) {
		this.defaultDeviceName = defaultDeviceName;
	}
	
	

}
