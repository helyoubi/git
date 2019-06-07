package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Calendar;

@XmlRootElement
public class CalendarDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String description;

	private String cron;

	public CalendarDto() {
		super();
	}

	public CalendarDto(Calendar calendar) {
		super();
		this.id = calendar.getId();
		this.name = calendar.getName();
		this.description = calendar.getDescription();
		this.cron = calendar.getCron();
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

	public String getCron() {
		return cron;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

}
