package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.QueueProperty;

@XmlRootElement
public class QueuePropertyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String description;

	private String txt;

	private Boolean mandatory;

	private Boolean display;

	private Boolean persist;

	private Boolean overridable;

	private Boolean scriptExport;

	private Boolean jobExport;

	private Queue queue;

	/***************************************************************************
	 * Constructeur
	 **************************************************************************/

	public QueuePropertyDto() {
		super();
	}

	public QueuePropertyDto(QueueProperty queueProperty) {
		this.id=queueProperty.getId();
		this.name = queueProperty.getName();
		this.description = queueProperty.getDescription();
		this.txt = queueProperty.getValue();
		this.mandatory = queueProperty.isMandatory();
		this.display = queueProperty.isDisplay();
		this.persist = queueProperty.isPersist();
		this.overridable = queueProperty.isOverridable();
		this.scriptExport = queueProperty.isScriptExport();
		this.jobExport = queueProperty.isJobExport();
	}

	/***************************************************************************
	 * Setter et Getter
	 **************************************************************************/

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

	public Boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public Boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public Boolean isOverridable() {
		return overridable;
	}

	public void setOverridable(boolean overridable) {
		this.overridable = overridable;
	}

	public Boolean isScriptExport() {
		return scriptExport;
	}

	public void setScriptExport(boolean scriptExport) {
		this.scriptExport = scriptExport;
	}

	public Boolean isJobExport() {
		return jobExport;
	}

	public void setJobExport(boolean jobExport) {
		this.jobExport = jobExport;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

}
