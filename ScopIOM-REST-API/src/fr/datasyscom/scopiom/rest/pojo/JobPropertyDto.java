package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.JobProperty;

@XmlRootElement
public class JobPropertyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String txt;

	private Boolean persist;

	public JobPropertyDto() {
		super();
	}

	public JobPropertyDto(JobProperty jobProperty) {
		super();
		this.id = jobProperty.getId();
		this.name = jobProperty.getName();
		if (jobProperty.getValue() != null) {
			this.txt = jobProperty.getValue();
		}
		this.persist = jobProperty.isPersist();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTxt() {
		return txt;
	}

	public Boolean isPersist() {
		return persist;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public void setPersist(Boolean persist) {
		this.persist = persist;
	}

}
