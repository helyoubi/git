package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LpdDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer port;
	private String defaultQueue;
	private String defaultWorkflow;
	private String status;

	public LpdDto() {
		super();

	}

	public Integer getPort() {
		return port;
	}

	public String getDefaultQueue() {
		return defaultQueue;
	}

	public String getDefaultWorkflow() {
		return defaultWorkflow;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setDefaultQueue(String defaultQueue) {
		this.defaultQueue = defaultQueue;
	}

	public void setDefaultWorkflow(String defaultWorkflow) {
		this.defaultWorkflow = defaultWorkflow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
