package fr.datasyscom.scopiom.rest.agent;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentsSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public int count;

	@XmlElementWrapper(name = "agents")
	@XmlElement(name = "agent")
	public List<AgentSummary> agents;

	public AgentsSummary() {
	}

	public AgentsSummary(List<AgentSummary> agentSummaries) {
		this.count = agentSummaries.size();
		this.agents = agentSummaries;
	}

}
