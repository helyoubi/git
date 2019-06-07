package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Queue;

@XmlRootElement
public class QueueStatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Id de la queue en base de données */
	private long id;

	private String name;

	/** Statut actuel de la queue */
	private String status;

	/** Description du statut */
	private String statusDesc;

	public QueueStatusDto() {
		super();
	}

	public QueueStatusDto(Queue queue) {
		super();
		this.id = queue.getId();
		this.name = queue.getName();
		this.status = queue.getStatus().name();
		this.statusDesc = queue.getStatusDesc();
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
