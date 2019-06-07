package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Agent;

@XmlRootElement
public class AgentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** La liste des statuts possibles */
	public enum AgentStatusType {
		OPEN, CLOSE
	}

	private long id;

	private String adresse;

	private Integer port;

	private String description;

	private AgentStatusType status;

	public AgentDto() {
		super();

	}

	public AgentDto(Agent agent) {
		this.id = agent.getId();
		this.adresse = agent.getAdresse();
		this.port = agent.getPort();
		this.description = agent.getDescription();
		String enumAgent = agent.getStatus().name();
		setStatus(AgentStatusType.valueOf(enumAgent));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AgentStatusType getStatus() {
		return status;
	}

	public void setStatus(AgentStatusType status) {
		this.status = status;
	}

}
