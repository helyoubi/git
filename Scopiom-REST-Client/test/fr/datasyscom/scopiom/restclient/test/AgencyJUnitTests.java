package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.AgencyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.restclient.agency.AgencyClient;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class AgencyJUnitTests {

	private final String NEW_AGENCY_NAME = "NEWNAME-" + System.currentTimeMillis();
	private final String NEW_AGENCY_DESC = "agencyDescTest";
	private final String NEW_AGENCY_COMMENT = "agencyComment";

	private final String UPDATE_AGENCY_DESC = "agencyDescUpdate";
	private final String UPDATE_AGENCY_COMMENT = "agencyCommentUpdate";

	private static ScopIOMServer serveur;
	private static GroupeDto groupeAdmin;
	private static GroupeDto groupeUsers;
	private static DeviceDto device;

	private final static String NEW_AGENCY_GROUPEADMIN = "GROUPEADMIN-" + System.currentTimeMillis();
	private final static String NEW_AGENCY_GROUPEUSERS = "GROUPEUSERS-" + System.currentTimeMillis();
	private final static String NEW_AGENCY_DEVICE = "NEWDEVICE-" + System.currentTimeMillis();

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// Création d'un groupe admin
		groupeAdmin = new GroupeDto();
		groupeAdmin.setName(NEW_AGENCY_GROUPEADMIN);
		groupeAdmin = serveur.groups().create(groupeAdmin);
		// Création d'un groupe users
		groupeUsers = new GroupeDto();
		groupeUsers.setName(NEW_AGENCY_GROUPEUSERS);
		groupeUsers = serveur.groups().create(groupeUsers);
		// Création d'un device
		device = serveur.devices().create(NEW_AGENCY_DEVICE, "model_new");
	}

	@Test
	public void testAgency() throws ValidationException, RestException {
		AgencyClient agencyClient = serveur.agency();
		long createdIdAgency;
		int initialCountSize = agencyClient.all().size();

		// Ajout d'une agence
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName(NEW_AGENCY_NAME);
		agencyDto.setDescription(NEW_AGENCY_DESC);
		agencyDto.setComment(NEW_AGENCY_COMMENT);
		agencyDto.setDevice(NEW_AGENCY_DEVICE);
		agencyDto = agencyClient.create(agencyDto);
		createdIdAgency = agencyDto.getId();

		assertEquals("Erreur list agency", initialCountSize + 1, agencyClient.all().size());

		// Récupération par id
		agencyDto = agencyClient.byId(createdIdAgency);
		assertEquals("Mauvais ID", createdIdAgency, agencyDto.getId());
		assertEquals("Mauvais nom", NEW_AGENCY_NAME, agencyDto.getName());
		assertEquals("Mauvaise description", NEW_AGENCY_DESC, agencyDto.getDescription());
		assertEquals("Mauvais commentaire", NEW_AGENCY_COMMENT, agencyDto.getComment());
		assertEquals("Mauvais groupe d'admin", null, agencyDto.getAdminGroup());
		assertEquals("Mauvais groupe d'utilisateurs", null, agencyDto.getUserGroup());
		assertEquals("Mauvais device", NEW_AGENCY_DEVICE, agencyDto.getDevice());

		// Update
		agencyDto.setDescription(UPDATE_AGENCY_DESC);
		agencyDto.setComment(UPDATE_AGENCY_COMMENT);
		agencyDto.setAdminGroup(NEW_AGENCY_GROUPEADMIN);
		agencyDto.setUserGroup(NEW_AGENCY_GROUPEUSERS);
		agencyDto.setDevice(NEW_AGENCY_DEVICE);
		agencyDto = agencyClient.update(agencyDto);
		agencyDto = agencyClient.byName(NEW_AGENCY_NAME);
		assertEquals("Mauvais ID", createdIdAgency, agencyDto.getId());
		assertEquals("Mauvais nom", NEW_AGENCY_NAME, agencyDto.getName());
		assertEquals("Mauvaise description", UPDATE_AGENCY_DESC, agencyDto.getDescription());
		assertEquals("Mauvais commentaire", UPDATE_AGENCY_COMMENT, agencyDto.getComment());
		assertEquals("Mauvais groupe d'admin", NEW_AGENCY_GROUPEADMIN, agencyDto.getAdminGroup());
		assertEquals("Mauvais groupe d'utilisateurs", NEW_AGENCY_GROUPEUSERS, agencyDto.getUserGroup());
		assertEquals("Mauvais device", NEW_AGENCY_DEVICE, agencyDto.getDevice());

		// delete
		agencyClient.delete(agencyDto);
		try {
			agencyClient.byId(createdIdAgency);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
		assertEquals("Erreur list agency", initialCountSize, agencyClient.all().size());
	}

	@Test(expected = RestException.class)
	public void testAgency_NotFound_ByName() throws ValidationException, RestException {
		AgencyClient agencyClient = serveur.agency();
		agencyClient.byName(NEW_AGENCY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testAgency_Empty_ByName() throws ValidationException, RestException {
		AgencyClient agencyClient = serveur.agency();
		agencyClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testAgency_NotFound_ById() throws RestException {
		AgencyClient agencyClient = serveur.agency();
		agencyClient.byId(System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testAgency_Empty_For_Create() throws RestException, ValidationException {
		AgencyClient agencyClient = serveur.agency();
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName("");
		agencyClient.create(agencyDto);
	}

	@Test(expected = RestException.class)
	public void testAgencyName_NotFound_For_Update() throws RestException, ValidationException {
		AgencyClient agencyClient = serveur.agency();
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName(NEW_AGENCY_NAME);
		agencyClient.update(agencyDto);
	}

	@Test(expected = ValidationException.class)
	public void testAgencyName_Empty_For_Update() throws RestException, ValidationException {
		AgencyClient agencyClient = serveur.agency();
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName("");
		agencyClient.update(agencyDto);
	}

	@Test(expected = ValidationException.class)
	public void testAgencyName_Empty_For_Delete() throws RestException, ValidationException {
		AgencyClient agencyClient = serveur.agency();
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName("");
		agencyClient.delete(agencyDto);
	}

	@Test(expected = RestException.class)
	public void testAgencyName_NotFound_For_Delete() throws RestException, ValidationException {
		AgencyClient agencyClient = serveur.agency();
		AgencyDto agencyDto = new AgencyDto();
		agencyDto.setName(NEW_AGENCY_NAME);
		agencyClient.delete(agencyDto);
	}

	// Suppréssion des groupes et devices a la fin des tests
	@AfterClass
	public static void clear() throws ValidationException, RestException {
		serveur.groups().delete(groupeAdmin);
		serveur.groups().delete(groupeUsers);
		serveur.devices().delete(device);
	}
}
