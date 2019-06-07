package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Media;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class MediaDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id du média en base de données */
	private long id;

	/** Nom du média */
	private String name;

	/** Description du média */
	private String description;

	public MediaDto() {
	}

	public MediaDto(Media media) {
		this.id = media.getId();
		this.name = media.getName();
		this.description = media.getDescription();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
