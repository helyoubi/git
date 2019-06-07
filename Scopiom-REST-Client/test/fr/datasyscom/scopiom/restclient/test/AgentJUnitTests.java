package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.AgentDto;
import fr.datasyscom.scopiom.rest.pojo.AgentDto.AgentStatusType;
import fr.datasyscom.scopiom.restclient.agent.AgentClient;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class AgentJUnitTests {

	private Random rand = new Random();
	private final String NEW_AGENT_ADRESS = rand.nextInt(256) + "." + rand.nextInt(256) + "." + rand.nextInt(256) + "."
			+ rand.nextInt(256);
	private final String NEW_AGENT_DESC = "descAgent";
	private final int NEW_AGENT_PORT = 1599;

	private final String UPDATE_AGENT_DESC = "agentDescUpdate";
	private final int UPDATE_AGENT_PORT = 2003;

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testAgent() throws ValidationException, RestException {
		AgentClient agentClient = serveur.agents();
		long createdIdAgent;
		int initialCountSize = agentClient.all().size();

		// Ajout d'un agent
		AgentDto agentDto = new AgentDto();
		agentDto.setAdresse(NEW_AGENT_ADRESS);
		agentDto.setDescription(NEW_AGENT_DESC);
		agentDto.setPort(NEW_AGENT_PORT);
		agentDto = agentClient.create(agentDto);
		createdIdAgent = agentDto.getId();

		assertEquals("Erreur list agent", initialCountSize + 1, agentClient.all().size());
		// Récupération par id
		agentDto = agentClient.byId(createdIdAgent);
		assertEquals("Mauvais ID", createdIdAgent, agentDto.getId());
		assertEquals("Mauvaise Adresse", NEW_AGENT_ADRESS, agentDto.getAdresse());
		assertEquals("Mauvaise description", NEW_AGENT_DESC, agentDto.getDescription());
		assertEquals("Mauvais port", NEW_AGENT_PORT, agentDto.getPort().intValue());
		assertEquals("Mauvais statut", AgentStatusType.CLOSE, agentDto.getStatus());

		agentDto.setDescription(UPDATE_AGENT_DESC);
		agentDto.setPort(UPDATE_AGENT_PORT);
		agentDto = agentClient.update(agentDto);
		agentDto = agentClient.byAdress(NEW_AGENT_ADRESS);
		assertEquals("Mauvais ID", createdIdAgent, agentDto.getId());
		assertEquals("Mauvaise Adresse", NEW_AGENT_ADRESS, agentDto.getAdresse());
		assertEquals("Mauvaise description", UPDATE_AGENT_DESC, agentDto.getDescription());
		assertEquals("Mauvais port", UPDATE_AGENT_PORT, agentDto.getPort().intValue());

		// delete
		agentClient.delete(agentDto);
		try {
			agentClient.byId(createdIdAgent);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}

	}

	@Test(expected = RestException.class)
	public void testAgent_NotFound_ByAdress() throws ValidationException, RestException {
		AgentClient agentClient = serveur.agents();
		agentClient.byAdress(NEW_AGENT_ADRESS);
	}

	@Test(expected = ValidationException.class)
	public void testAgent_Empty_ByAdress() throws ValidationException, RestException {
		AgentClient agentClient = serveur.agents();
		agentClient.byAdress("");
	}

	// Retourne conflict en cas ou le statut de l'agent est ouvert
	@Test
	public void testAgent_Conflict_For_DeleteAgent() throws ValidationException, RestException {
		AgentClient agentClient = serveur.agents();
		AgentDto agentDto = new AgentDto();
		agentDto.setAdresse(NEW_AGENT_ADRESS);
		agentDto = agentClient.create(agentDto);
		agentClient.updateStatus(agentDto.getAdresse(), AgentStatusType.OPEN);
		try {
			agentClient.delete(agentDto);
			fail("Test delete agent fail with conflict");
		} catch (RestException e) {
			// fermeture du statut pour suppression de l'agent
			agentClient.updateStatus(agentDto.getAdresse(), AgentStatusType.CLOSE);
			agentClient.delete(agentDto);
		}
	}

	@Test(expected = ValidationException.class)
	public void testAgentAdress_Empty_For_Delete() throws ValidationException, RestException {
		AgentClient agentClient = serveur.agents();
		agentClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testAgentAdress_Empty_For_Create() throws RestException, ValidationException {
		AgentClient agentClient = serveur.agents();
		AgentDto agentDto = new AgentDto();
		agentDto.setAdresse("");
		agentClient.create(agentDto);
	}

	@Test(expected = ValidationException.class)
	public void testAgentAdress_Empty_For_Update() throws RestException, ValidationException {
		AgentClient agentClient = serveur.agents();
		AgentDto agentDto = new AgentDto();
		agentDto.setAdresse("");
		agentClient.update(agentDto);
	}

	@Test(expected = RestException.class)
	public void testAgentAdress_NotFound_For_Update() throws RestException, ValidationException {
		AgentClient agentClient = serveur.agents();
		AgentDto agentDto = new AgentDto();
		agentDto.setAdresse(NEW_AGENT_ADRESS);
		agentClient.update(agentDto);
	}

	@Test(expected = ValidationException.class)
	public void testAgentAdress_Empty_For_UpdateStatus() throws RestException, ValidationException {
		AgentClient agentClient = serveur.agents();
		agentClient.updateStatus("", null);
	}

	@Test(expected = RestException.class)
	public void testAgentAdress_NotFound_For_UpdateStatus() throws RestException, ValidationException {
		AgentClient agentClient = serveur.agents();
		agentClient.updateStatus(NEW_AGENT_ADRESS, AgentStatusType.CLOSE);
	}

}
