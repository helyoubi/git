package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Property;

/**
 * 
 * 
 * 
 * @author DataSyscom
 *
 */
@XmlRootElement
public class PropertyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String description;

	private String txt;

	private Boolean scriptExport;

	private Boolean overridable;

	public PropertyDto() {
		super();

	}

	public PropertyDto(Property property) {
		super();
		this.id = property.getId();
		this.name = property.getName();
		this.description = property.getDescription();
		this.txt = property.getValue();
		this.scriptExport = property.isScriptExport();
		this.overridable = property.isOverridable();
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

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public Boolean isScriptExport() {
		return scriptExport;
	}

	public void setScriptExport(Boolean scriptExport) {
		this.scriptExport = scriptExport;
	}

	public Boolean isOverridable() {
		return overridable;
	}

	public void setOverridable(Boolean overridable) {
		this.overridable = overridable;
	}

}
