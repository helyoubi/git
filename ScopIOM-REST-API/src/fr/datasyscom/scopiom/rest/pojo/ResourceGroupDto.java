package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.ResourceGroup;

@XmlRootElement
public class ResourceGroupDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;

	public ResourceGroupDto() {
		super();

	}

	public ResourceGroupDto(ResourceGroup resourceGroup) {
		super();
		this.id = resourceGroup.getId();
		this.name = resourceGroup.getName();
		this.description = resourceGroup.getDescription();
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

}
