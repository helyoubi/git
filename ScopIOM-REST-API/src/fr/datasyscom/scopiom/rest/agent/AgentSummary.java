package fr.datasyscom.scopiom.rest.agent;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Agent;
import fr.datasyscom.pome.utile.agent.AgentPingResult;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	public String description;
	public String address;
	public Integer port;
	public String status;

	public PingResult ping;

	public static class PingResult implements Serializable {

		private static final long serialVersionUID = 1L;

		public String status;
		public Integer nbCurrentTasks;

		public PingResult() {
		}

		public PingResult(AgentPingResult pingResult) {
			this.status = pingResult.getStatus().name();
			this.nbCurrentTasks = pingResult.getNbrCurrentTasks();
		}
	}

	public AgentSummary() {
	}

	public AgentSummary(Agent agent) {
		this.id = agent.getId();
		this.description = agent.getDescription();
		this.address = agent.getAdresse();
		this.port = agent.getPort();
		this.status = agent.getStatus().name();
	}

}