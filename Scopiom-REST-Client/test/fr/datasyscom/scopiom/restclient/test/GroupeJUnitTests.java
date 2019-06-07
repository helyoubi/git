package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.AgencyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.UserDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.groupe.GroupeClient;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class GroupeJUnitTests {

	// test notfound case
	private static final String NEW_GROUPNOTFOUND_NAME = "NEWGROUPTEST-" + System.currentTimeMillis();
	private static final String NEW_USERNOTFOUND_LOGIN = "NEWUSERTEST-" + System.currentTimeMillis();
	private static final String NEW_DEVICENOTFOUND_NAME = "NEWDEVICETEST-" + System.currentTimeMillis();
	private static final String NEW_QUEUENOTFOUND_NAME = "NEWQUEUETEST-" + System.currentTimeMillis();
	private static final String NEW_AGENCYNOTFOUND_NAME = "NEWAGENCYTEST-" + System.currentTimeMillis();
	private static final String NEW_WORKFLOWNOTFOUND_NAME = "NEWWORKFLOWTEST-" + System.currentTimeMillis();
	// new groupe
	private static GroupeDto groupeDto;
	private static long groupCreatedId;
	private static final String NEW_GROUP_NAME = "NEWGROUP-" + System.currentTimeMillis();
	private static final String NEW_GROUP_DESC = "newDescGroup";
	// update groupe
	private static final String UPDATE_GROUP_DESC = "updateDescGroup";
	private static final String UPDATE_GROUP_COMMENT = "commentGroup";
	private static final boolean UPDATE_GROUP_CANSUBMITJOBS = true;
	private static final boolean UPDATE_GROUP_CANGENERATEREPORT = true;
	private static final boolean UPDATE_GROUP_CANACCESSJOBLINK = true;
	private static final boolean UPDATE_GROUP_CANACCESSJOBTREE = true;
	// new user
	private static UserDto userDto;
	private static final String NEW_USER_LOGIN = "user-" + System.currentTimeMillis();
	private static final String NEW_USER_PASS = "passUser1";
	// new queue
	private static QueueDto queueDto;
	private static final String NEW_QUEUE_NAME = "NEWQUEUE-" + System.currentTimeMillis();
	// new agency
	private static AgencyDto agencyDto;
	private static final String NEW_AGENCY_NAME = "NEWNAMEAGENCY-" + System.currentTimeMillis();
	private static final String NEW_AGENCY_DESC = "agencyDescTest";
	// new device
	private static DeviceDto deviceDto;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE-" + System.currentTimeMillis();

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// création d'un groupe
		int initilialGrpCount = serveur.groups().all().size();
		groupeDto = new GroupeDto();
		groupeDto.setName(NEW_GROUP_NAME);
		groupeDto.setDescription(NEW_GROUP_DESC);
		groupeDto = serveur.groups().create(groupeDto);
		groupCreatedId = groupeDto.getId();
		// check groupe crée
		assertEquals("Mauvais Id du groupe", groupCreatedId, groupeDto.getId());
		assertEquals("Mauvais nom du groupe", NEW_GROUP_NAME, groupeDto.getName());
		assertEquals("Mauvaise description du groupe", NEW_GROUP_DESC, groupeDto.getDescription());
		// check list
		assertEquals("Erreur list groupe", initilialGrpCount + 1, serveur.groups().all().size());
		// création d'un utilisateur
		userDto = new UserDto();
		userDto.setLogin(NEW_USER_LOGIN);
		userDto.setPassword(NEW_USER_PASS);
		userDto = serveur.users().create(userDto);
		// création de la queue
		queueDto = new QueueDto();
		queueDto = serveur.queues().create(NEW_QUEUE_NAME, "model_batch");
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.HOLD, null);
		// création d'un device
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
		// création d'une agence
		agencyDto = new AgencyDto();
		agencyDto.setName(NEW_AGENCY_NAME);
		agencyDto.setDescription(NEW_AGENCY_DESC);
		agencyDto.setDevice(NEW_DEVICE_NAME);
		agencyDto = serveur.agency().create(agencyDto);
	}

	@Test
	public void testGroupe() throws RestException {
		GroupeClient groupClient = serveur.groups();
		// recupération par id
		groupeDto = groupClient.byId(groupCreatedId);
		// update
		groupeDto.setDescription(UPDATE_GROUP_DESC);
		groupeDto.setComment(UPDATE_GROUP_COMMENT);
		groupeDto.setCanSubmitJobs(UPDATE_GROUP_CANSUBMITJOBS);
		groupeDto.setCanGenerateReports(UPDATE_GROUP_CANGENERATEREPORT);
		groupeDto.setCanAccessJobLinks(UPDATE_GROUP_CANACCESSJOBLINK);
		groupeDto.setCanAccessJobsTree(UPDATE_GROUP_CANACCESSJOBTREE);
		groupeDto = groupClient.update(groupeDto);
		// recupération par nom
		groupeDto = groupClient.byName(NEW_GROUP_NAME);
		assertEquals("Mauvais Id du groupe en update", groupCreatedId, groupeDto.getId());
		assertEquals("Mauvais nom du groupe en update", NEW_GROUP_NAME, groupeDto.getName());
		assertEquals("Mauvaise description du groupe en update", UPDATE_GROUP_DESC, groupeDto.getDescription());
		assertEquals("Mauvais commentare du groupe en update", UPDATE_GROUP_COMMENT, groupeDto.getComment());
		assertEquals("Mauvaise valeur de soumission de jobs du groupe en update", UPDATE_GROUP_CANSUBMITJOBS,
				groupeDto.isCanSubmitJobs());
		assertEquals("Mauvaise valeur de génération de rapports du groupe en update", UPDATE_GROUP_CANGENERATEREPORT,
				groupeDto.isCanGenerateReports());
		assertEquals("Mauvaise valeur d'accès aux liens entre jobs du groupe en update", UPDATE_GROUP_CANACCESSJOBLINK,
				groupeDto.isCanAccessJobLinks());
		assertEquals("Mauvaise valeur d'accès à l'arbre des jobs du groupe en update", UPDATE_GROUP_CANACCESSJOBTREE,
				groupeDto.isCanAccessJobsTree());
		// add user
		int initialUsersList = groupClient.retrieveUsers(NEW_GROUP_NAME).size();
		groupClient.addUser(NEW_GROUP_NAME, NEW_USER_LOGIN);
		assertEquals("Erreur list users en add", initialUsersList + 1,
				groupClient.retrieveUsers(NEW_GROUP_NAME).size());
		// remove user
		initialUsersList = groupClient.retrieveUsers(NEW_GROUP_NAME).size();
		groupClient.removeUser(NEW_GROUP_NAME, NEW_USER_LOGIN);
		assertEquals("Erreur list users en remove", initialUsersList - 1,
				groupClient.retrieveUsers(NEW_GROUP_NAME).size());
	}

	@Test
	public void testQueue_From_Groups() throws ValidationException, RestException {
		GroupeClient groupClient = serveur.groups();
		int initialQueuesList = groupClient.retrieveQueues(NEW_GROUP_NAME).size();
		// add queue
		groupClient.addQueue(NEW_GROUP_NAME, NEW_QUEUE_NAME);
		assertEquals("Erreur list queues en add", initialQueuesList + 1,
				groupClient.retrieveQueues(NEW_GROUP_NAME).size());
		// remove queue
		initialQueuesList = groupClient.retrieveQueues(NEW_GROUP_NAME).size();
		groupClient.removeQueue(NEW_GROUP_NAME, NEW_QUEUE_NAME);
		assertEquals("Erreur list queues en remove", initialQueuesList - 1,
				groupClient.retrieveQueues(NEW_GROUP_NAME).size());
	}

	@Test
	public void testDevices_From_Groups() throws ValidationException, RestException {
		GroupeClient groupClient = serveur.groups();
		int initialDeviceList = groupClient.retrieveDevices(NEW_GROUP_NAME).size();
		// add device
		groupClient.addDevice(NEW_GROUP_NAME, NEW_DEVICE_NAME);
		assertEquals("Erreur list devices en add", initialDeviceList + 1,
				groupClient.retrieveDevices(NEW_GROUP_NAME).size());
		// remove device
		initialDeviceList = groupClient.retrieveDevices(NEW_GROUP_NAME).size();
		groupClient.removeDevice(NEW_GROUP_NAME, NEW_DEVICE_NAME);
		assertEquals("Erreur list devices en remove", initialDeviceList - 1,
				groupClient.retrieveDevices(NEW_GROUP_NAME).size());
	}

	@Test
	public void testAgency_From_Groups() throws ValidationException, RestException {
		GroupeClient groupClient = serveur.groups();
		// Admin agency
		int initialAdminAgencyList = groupClient.retrieveAdminAgencies(NEW_GROUP_NAME).size();
		// add admin agency
		groupClient.addAdminAgency(NEW_GROUP_NAME, NEW_AGENCY_NAME);
		assertEquals("Erreur list admin agency en add", initialAdminAgencyList + 1,
				groupClient.retrieveAdminAgencies(NEW_GROUP_NAME).size());
		// remove admin agency
		initialAdminAgencyList = groupClient.retrieveAdminAgencies(NEW_GROUP_NAME).size();
		groupClient.removeAdminAgency(NEW_GROUP_NAME, NEW_AGENCY_NAME);
		assertEquals("Erreur list admin agency en remove", initialAdminAgencyList - 1,
				groupClient.retrieveAdminAgencies(NEW_GROUP_NAME).size());
		// User agency
		int initialUserAgencyList = groupClient.retrieveUserAgencies(NEW_GROUP_NAME).size();
		// add user agency
		groupClient.addUserAgency(NEW_GROUP_NAME, NEW_AGENCY_NAME);
		assertEquals("Erreur list user agency en add", initialUserAgencyList + 1,
				groupClient.retrieveUserAgencies(NEW_GROUP_NAME).size());
		// remove user agency
		initialUserAgencyList = groupClient.retrieveUserAgencies(NEW_GROUP_NAME).size();
		groupClient.removeUserAgency(NEW_GROUP_NAME, NEW_AGENCY_NAME);
		assertEquals("Erreur list user agency en remove", initialUserAgencyList - 1,
				groupClient.retrieveUserAgencies(NEW_GROUP_NAME).size());
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_ById() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_ByName() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.byName(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_ByName() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveDevices() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveDevices(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveDevices() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveDevices("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveQueues() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveQueues(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveQueues() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveQueues("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddWorkflow() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addWorkflow(NEW_GROUPNOTFOUND_NAME, NEW_WORKFLOWNOTFOUND_NAME);
	}
	
	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddWorkflow() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addWorkflow("", "");
	}
	
	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveWorkflow() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeWorkflow(NEW_GROUPNOTFOUND_NAME, NEW_WORKFLOWNOTFOUND_NAME);
	}
	
	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveWorkflow() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeWorkflow("", "");
	}
	
	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveWorkflows() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveWorkflows(NEW_GROUPNOTFOUND_NAME);
	}
	
	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveWorkflows() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveWorkflows("");
	}
	
	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveUserAgencies(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveUserAgencies("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveAdminAgencies(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveAdminAgencies("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RetrieveUsers() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveUsers(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RetrieveUsers() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.retrieveUsers("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_Delete() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.delete(NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_Delete() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveUser() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeUser(NEW_GROUPNOTFOUND_NAME, NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveUser() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeUser("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddUser() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addUser(NEW_GROUPNOTFOUND_NAME, NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddUser() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addUser("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddDevice() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addDevice(NEW_GROUPNOTFOUND_NAME, NEW_DEVICENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddDevice() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addDevice("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddQueue() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addQueue(NEW_GROUPNOTFOUND_NAME, NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddQueue() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addQueue("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addAdminAgency(NEW_GROUPNOTFOUND_NAME, NEW_AGENCYNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addAdminAgency("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_AddUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addUserAgency(NEW_GROUPNOTFOUND_NAME, NEW_AGENCYNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_AddUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.addUserAgency("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveDevice() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeDevice(NEW_GROUPNOTFOUND_NAME, NEW_DEVICENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveDevice() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeDevice("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveQueue() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeQueue(NEW_GROUPNOTFOUND_NAME, NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveQueue() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeQueue("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeAdminAgency(NEW_GROUPNOTFOUND_NAME, NEW_AGENCYNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveAdminAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeAdminAgency("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_RemoveUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeUserAgency(NEW_GROUPNOTFOUND_NAME, NEW_AGENCYNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_RemoveUserAgency() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		groupClient.removeUserAgency("", "");
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_Create() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		GroupeDto groupeDto = new GroupeDto();
		groupeDto.setName(NEW_GROUP_NAME);
		groupClient.create(groupeDto);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_Create() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		GroupeDto groupeDto = new GroupeDto();
		groupeDto.setName("");
		groupClient.create(groupeDto);
	}

	@Test(expected = RestException.class)
	public void testGroup_NotFound_For_Update() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		GroupeDto groupeDto = new GroupeDto();
		groupeDto.setName(NEW_GROUPNOTFOUND_NAME);
		groupClient.update(groupeDto);
	}

	@Test(expected = ValidationException.class)
	public void testGroup_Empty_For_Update() throws RestException, ValidationException {
		GroupeClient groupClient = serveur.groups();
		GroupeDto groupeDto = new GroupeDto();
		groupeDto.setName("");
		groupClient.update(groupeDto);
	}

	@AfterClass
	public static void clear() throws ValidationException, RestException {
		serveur.groups().delete(groupeDto);
		serveur.users().delete(userDto);
		// fermeture du status de la file de traitement afin de la supprimer
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, "statusDescQueue");
		serveur.queues().delete(queueDto);
		serveur.agency().delete(agencyDto);
		serveur.devices().delete(deviceDto);

	}

}
