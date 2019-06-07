package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Device;

@XmlRootElement
public class DeviceStatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Id du device en base de données */
	private long id;

	/** Nom du device */
	private String name;

	/** Statut actuel du device */
	private String status;

	/** Description du statut */
	private String statusDesc;

	public DeviceStatusDto() {
		super();
	}

	public DeviceStatusDto(Device device) {
		super();
		this.id = device.getId();
		this.name = device.getName();
		this.status = device.getStatus().name();
		this.statusDesc = device.getStatusDesc();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

}
