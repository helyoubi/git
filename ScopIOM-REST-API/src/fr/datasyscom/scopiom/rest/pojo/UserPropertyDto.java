package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import fr.datasyscom.pome.ejbentity.User;
import fr.datasyscom.pome.ejbentity.UserProperty;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class UserPropertyDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String txt;

	private String description;

	// Ignore le field device lors de la sérialisation
	@XmlTransient
	private User user;

	private Boolean scriptExport;

	private Boolean mandatory;

	public UserPropertyDto() {
		super();

	}

	public UserPropertyDto(UserProperty userProperty) {
		this.id = userProperty.getId();
		this.name = userProperty.getName();
		this.txt = userProperty.getValue();
		this.description = userProperty.getDescription();
		this.user = userProperty.getUser();
		this.scriptExport = userProperty.isScriptExport();
		this.mandatory = userProperty.isMandatory();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean isScriptExport() {
		return scriptExport;
	}

	public void setScriptExport(Boolean scriptExport) {
		this.scriptExport = scriptExport;
	}

	public Boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

}
