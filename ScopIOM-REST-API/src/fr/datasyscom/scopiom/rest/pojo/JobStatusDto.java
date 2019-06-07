package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Job;

@XmlRootElement
public class JobStatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String status;

	private String statusDesc;

	private Integer exitCode;

	public JobStatusDto() {
		super();
	}

	public JobStatusDto(Job job) {
		super();
		this.id = job.getId();
		this.name = job.getJobName();
		this.status = job.getStatus().name();
		this.statusDesc = job.getStatusDesc();
		this.exitCode = job.getExitCode();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Integer getExitCode() {
		return exitCode;
	}

	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}

}
