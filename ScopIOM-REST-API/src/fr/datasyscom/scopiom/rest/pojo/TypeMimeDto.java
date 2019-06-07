package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Type;
import fr.datasyscom.pome.ejbentity.TypeMime;

@XmlRootElement
public class TypeMimeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	private Type type;

	private boolean defaut;

	public TypeMimeDto() {
		super();
	}

	public TypeMimeDto(TypeMime typeMime) {
		super();
		this.value = typeMime.getValue();
		this.type = typeMime.getType();
		this.defaut = typeMime.isDefaut();
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
