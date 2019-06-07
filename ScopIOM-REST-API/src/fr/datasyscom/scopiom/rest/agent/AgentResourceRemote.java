package fr.datasyscom.scopiom.rest.agent;

import javax.ejb.Remote;

@Remote
public interface AgentResourceRemote {

	AgentsSummary agents(boolean pingAgents);

}