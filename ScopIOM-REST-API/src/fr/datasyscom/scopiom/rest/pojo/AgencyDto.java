package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Agency;

@XmlRootElement
public class AgencyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String description;

	private String comment;

	private String adminGroup;

	private String userGroup;

	private String device;

	public AgencyDto() {

	}

	public AgencyDto(Agency agency) {
		super();
		this.id = agency.getId();
		this.name = agency.getName();
		this.description = agency.getDescription();
		this.comment = agency.getComment();
		if (agency.getAdminGroup() != null && agency.getAdminGroup().getName() != null) {
			this.adminGroup = agency.getAdminGroup().getName();
		}
		if (agency.getUserGroup() != null && agency.getUserGroup().getName() != null) {
			this.userGroup = agency.getUserGroup().getName();
		}
		this.getDevice();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(String adminGroup) {
		this.adminGroup = adminGroup;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
