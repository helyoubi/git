package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Extension;
import fr.datasyscom.pome.ejbentity.Type;

@XmlRootElement
public class ExtentionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	private Type type;

	private boolean defaut;

	public ExtentionDto() {
		super();
	}

	public ExtentionDto(Extension extension) {
		super();
		this.value = extension.getValue();
		this.type = extension.getType();
		this.defaut = extension.isDefaut();
	}

	public String getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public boolean isDefaut() {
		return defaut;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}

}
