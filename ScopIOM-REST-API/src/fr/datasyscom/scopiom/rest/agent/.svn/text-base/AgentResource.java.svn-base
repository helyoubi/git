package fr.datasyscom.scopiom.rest.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.datasyscom.pome.ejbentity.Agent;
import fr.datasyscom.pome.ejbentity.Agent.AgentStatusType;
import fr.datasyscom.pome.ejbsession.agent.AgentManagerLocal;
import fr.datasyscom.pome.utile.agent.AgentPingResult;
import fr.datasyscom.scopiom.rest.agent.AgentSummary.PingResult;

@Stateless(mappedName = "scopiom/ejb/stateless/AgentResource")
public class AgentResource implements AgentResourceLocal, AgentResourceRemote {

	@EJB
	private AgentManagerLocal am;

	public AgentsSummary agents(boolean pingAgents) {
		List<Agent> agents = am.retrieveAllAgent();

		// Local agent
		Agent localAgent = new Agent();
		localAgent.setDescription("Local");
		localAgent.setAdresse("127.0.0.1");
		localAgent.setPort(am.getLocalAgentPort());
		localAgent.setStatus(AgentStatusType.OPEN);
		agents.add(localAgent);

		List<AgentSummary> summaries = new ArrayList<>(agents.size());
		for (Agent agent : agents) {
			summaries.add(new AgentSummary(agent));
		}

		if (pingAgents) {
			List<AgentPingResult> pings = am.pingAllAgents();
			Map<Long, AgentPingResult> map = mapPingResultsById(pings);

			for (AgentSummary agentSummary : summaries) {
				AgentPingResult pingResult = map.get(agentSummary.id);
				if (pingResult != null) {
					agentSummary.ping = new PingResult(pingResult);
				}
			}
		}

		return new AgentsSummary(summaries);
	}

	private Map<Long, AgentPingResult> mapPingResultsById(List<AgentPingResult> pings) {
		Map<Long, AgentPingResult> map = new HashMap<>(pings.size());
		for (AgentPingResult agentPingResult : pings) {
			map.put(agentPingResult.getAgentId(), agentPingResult);
		}

		return map;
	}
}