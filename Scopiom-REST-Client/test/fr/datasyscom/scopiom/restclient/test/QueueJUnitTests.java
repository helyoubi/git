package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.client.executor.groovy.Nj;
import fr.datasyscom.scopiom.client.executor.groovy.Nj.NJResult;
import fr.datasyscom.scopiom.rest.pojo.AgentDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.QueuePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.QueueStatusDto;
import fr.datasyscom.scopiom.rest.pojo.TableExploitDtoRest;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;
import fr.datasyscom.scopiom.restclient.queue.QueueClient;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class QueueJUnitTests {

	// new queue
	private static QueueDto queueDto;
	private static long idQueueCreated;
	private static final String NEW_QUEUE_NAME = "NEWQUEUE-" + System.currentTimeMillis();
	private static final String UPDATE_QUEUE_DESC = "updateDescQueue";
	private static final String UPDATE_QUEUE_CMDPROCESS = "batch.bat";
	private static final String UPDATE_QUEUE_JOBERRORCMD = "cmdErrorJob";
	private static final int UPDATE_QUEUE_TIMEOUTJOB = 10;
	private static final int UPDATE_QUEUE_MAXTHREAD = 2;
	private static final int UPDATE_QUEUE_HOLDDELAY = 10;
	private static final int UPDATE_QUEUE_PRIORITY = 11;
	private static final Boolean UPDATE_QUEUE_KEEPLOGJOBOK = true;
	private static final long UPDATE_QUEUE_DELAYPURGEJOBERROR = 60;
	private static final long UPDATE_QUEUE_DELAYPURGEJOBOK = 60;
	private static final Boolean UPDATE_QUEUE_PAUSEIFEMPTY = true;
	private static final Boolean UPDATE_QUEUE_ACCOUNTING = true;
	private static final Boolean UPDATE_QUEUE_DEFAULTUSERDEVICE = true;
	private static final Boolean UPDATE_QUEUE_USEDEVICEROUTING = true;
	// new groupe
	private static GroupeDto groupeDto;
	private static final String NEW_GROUP_NAME = "NEWGROUP-" + System.currentTimeMillis();
	private static final String NEW_GROUP_DESC = "newDescGroup";
	// new queue property
	private static final String NEW_QUEUEPROPERTY_NAME = "SCOPIOM_queue" + System.currentTimeMillis();
	private static final String NEW_QUEUEPROPERTY_DESC = "queuePropDesc";
	private static final String NEW_QUEUEPROPERTY_VALUE = "queuePropValue";
	private static final boolean NEW_QUEUEPROPERTY_DISPLAY = true;
	private static final boolean NEW_QUEUEPROPERTY_SCRIPTEXPORT = true;
	private static final boolean NEW_QUEUEPROPERTY_PERSIST = true;
	private static final boolean NEW_QUEUEPROPERTY_OVERRIDABLE = true;
	private static final boolean NEW_QUEUEPROPERTY_JOBEXPORT = true;
	// update queue property
	private static final String UPDATE_QUEUEPROPERTY_DESC = "queuePropDescUpdate";
	private static final String UPDATE_QUEUEPROPERTY_VALUE = "queuePropValueUpdate";
	private static final boolean UPDATE_QUEUEPROPERTY_DISPLAY = false;
	private static final boolean UPDATE_QUEUEPROPERTY_SCRIPTEXPORT = false;
	private static final boolean UPDATE_QUEUEPROPERTY_PERSIST = false;
	private static final boolean UPDATE_QUEUEPROPERTY_OVERRIDABLE = false;
	private static final boolean UPDATE_QUEUEPROPERTY_JOBEXPORT = false;
	// test notfound cases
	private static final String NEW_QUEUENOTFOUND_NAME = "NEWQUEUETEST-" + System.currentTimeMillis();
	private static final String NEW_GROUPENOTFOUND_NAME = "NEWGROUPTEST-" + System.currentTimeMillis();
	private static final String NEW_TABLEEXPLOITNOTFOUND_NAME = "NEWTABLEEXPLOITTEST-" + System.currentTimeMillis();
	private static final String NEW_QUEUEPROPERTYNOTFOUND_NAME = "NEWQUEUEPROPTEST-" + System.currentTimeMillis();
	// new device
	private static DeviceDto deviceDto;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE-" + System.currentTimeMillis();
	// new agent
	private static AgentDto agentDto;
	private static Random rand = new Random();
	private final static String NEW_AGENT_ADRESS = rand.nextInt(256) + "." + rand.nextInt(256) + "." + rand.nextInt(256)
			+ "." + rand.nextInt(256);
	// new table exploit
	private static TableExploitDtoRest tableExploitDto;
	private final static String NEW_TABLEEXPLOIT_NAME = "NAMETABLEEXPLOIT-" + System.currentTimeMillis();
	private final static String NEW_TABLEEXPLOIT_DESC = "descTableExploit-" + System.currentTimeMillis();
	private final static String NEW_TABLEEXPLOIT_LONGDESC = "longDescTableExploit";
	// Queue status
	private final static String NEW_QUEUE_STATUSDESC = "descQueueStatus";
	// new job
	private static NJResult jobCreated;
	private static final String NEW_JOB_NAME = "newJob-" + System.currentTimeMillis();

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// création d'une table exploit
		tableExploitDto = new TableExploitDtoRest();
		tableExploitDto.setName(NEW_TABLEEXPLOIT_NAME);
		tableExploitDto.setDescription(NEW_TABLEEXPLOIT_DESC);
		tableExploitDto.setLongDescription(NEW_TABLEEXPLOIT_LONGDESC);
		tableExploitDto = serveur.tableExploit().create(tableExploitDto);
		// création d'une queue
		int initialiseQueueList = serveur.queues().all().size();
		queueDto = new QueueDto();
		queueDto = serveur.queues().create(NEW_QUEUE_NAME, "model_batch");
		// check list queue
		assertEquals("Erreur list queue", initialiseQueueList + 1, serveur.queues().all().size());
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.HOLD, NEW_QUEUE_STATUSDESC);
		QueueStatusDto queueStatut = serveur.queues().status(NEW_QUEUE_NAME);
		assertEquals("Mauvais status de la queue", QueueStatusType.HOLD.name(), queueStatut.getStatus());
		assertEquals("Mauvaise description du status de la queue", NEW_QUEUE_STATUSDESC, queueDto.getStatusDesc());
		idQueueCreated = queueDto.getId();
		// création d'un device
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
		// Création d'un agent
		agentDto = new AgentDto();
		agentDto.setAdresse(NEW_AGENT_ADRESS);
		agentDto = serveur.agents().create(agentDto);
		// création d'un groupe
		groupeDto = new GroupeDto();
		groupeDto.setName(NEW_GROUP_NAME);
		groupeDto.setDescription(NEW_GROUP_DESC);
		groupeDto = serveur.groups().create(groupeDto);
		// création d'un job
		int initialJobsQueueCount = serveur.queues().allJobs(NEW_QUEUE_NAME).size();
		Nj nj = new Nj();
		jobCreated = nj.server("localhost:4848").queue(NEW_QUEUE_NAME).jobName(NEW_JOB_NAME).user("iomadmin").submit();
		assertEquals("Erreur list jobs de la file de traitement", initialJobsQueueCount + 1,
				serveur.queues().allJobs(NEW_QUEUE_NAME).size());
	}

	@Test
	public void testQueue() throws RestException {
		QueueClient queueClient = serveur.queues();
		queueDto = queueClient.byId(idQueueCreated);
		// configuration générale
		queueDto.setDescription(UPDATE_QUEUE_DESC);
		// Exécution
		queueDto.setCommandProcess(UPDATE_QUEUE_CMDPROCESS);
		queueDto.setOnJobErrorCommand(UPDATE_QUEUE_JOBERRORCMD);
		queueDto.setTimeOutExecJob(UPDATE_QUEUE_TIMEOUTJOB);
		queueDto.setMaxThread(UPDATE_QUEUE_MAXTHREAD);
		queueDto.setHoldDelay(UPDATE_QUEUE_HOLDDELAY);
		queueDto.setPriority(UPDATE_QUEUE_PRIORITY);
		queueDto.setAgentAdress(NEW_AGENT_ADRESS);
		// temps de conservation des jobs
		queueDto.setKeepLogJobOk(UPDATE_QUEUE_KEEPLOGJOBOK);
		queueDto.setDelayPurgeJobError(UPDATE_QUEUE_DELAYPURGEJOBERROR);
		queueDto.setDelayPurgeJobOk(UPDATE_QUEUE_DELAYPURGEJOBOK);
		// Autres
		queueDto.setPauseIfEmpty(UPDATE_QUEUE_PAUSEIFEMPTY);
		queueDto.setAccounting(UPDATE_QUEUE_ACCOUNTING);
		// périphérique
		queueDto.setUseDefaultUserDevice(UPDATE_QUEUE_DEFAULTUSERDEVICE);
		queueDto.setDefaultDeviceName(NEW_DEVICE_NAME);
		queueDto.setUseDeviceRouting(UPDATE_QUEUE_USEDEVICEROUTING);
		queueDto = queueClient.update(queueDto);
		queueDto = queueClient.byName(NEW_QUEUE_NAME);
		assertEquals("Mauvaise description", UPDATE_QUEUE_DESC, queueDto.getDescription());
		assertEquals("Mauvaise commande en cas d'erreur", UPDATE_QUEUE_JOBERRORCMD, queueDto.getOnJobErrorCommand());
		assertEquals("Mauvais temps d'exécution maximum d'un job", UPDATE_QUEUE_TIMEOUTJOB,
				queueDto.getTimeOutExecJob().intValue());
		assertEquals("Mauvais max jobs simultanés", UPDATE_QUEUE_MAXTHREAD, queueDto.getMaxThread().intValue());
		assertEquals("Mauvais délai retenu à réception", UPDATE_QUEUE_HOLDDELAY, queueDto.getHoldDelay().intValue());
		assertEquals("Mauvaise priorité", UPDATE_QUEUE_PRIORITY, queueDto.getPriority().intValue());
		assertEquals("Mauvaise adress de l'agent", NEW_AGENT_ADRESS, queueDto.getAgentAdress());
		assertEquals("Mauvaise valeur log de traitement ok", UPDATE_QUEUE_KEEPLOGJOBOK, queueDto.getKeepLogJobOk());
		assertEquals("Mauvaise temps de conservation des jobs en erreur", UPDATE_QUEUE_DELAYPURGEJOBERROR,
				queueDto.getDelayPurgeJobError().intValue());
		assertEquals("Mauvaise temps de conservation des jobs Ok", UPDATE_QUEUE_DELAYPURGEJOBOK,
				queueDto.getDelayPurgeJobOk().intValue());
		assertEquals("Mauvaise valeur mettre en pause si vide", UPDATE_QUEUE_PAUSEIFEMPTY, queueDto.getPauseIfEmpty());
		assertEquals("Mauvaise valeur accounting des exécutions", UPDATE_QUEUE_ACCOUNTING, queueDto.getAccounting());
		assertEquals("Mauvaise valeur pour périphérique par défaut du propriétaire", UPDATE_QUEUE_DEFAULTUSERDEVICE,
				queueDto.getUseDefaultUserDevice());
		assertEquals("Mauvais périphérique", NEW_DEVICE_NAME, queueDto.getDefaultDeviceName());
		assertEquals("Mauvaise valeur utiliser routage", UPDATE_QUEUE_USEDEVICEROUTING, queueDto.isUseDeviceRouting());

	}

	@Test
	public void testGroupsQueue() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		// Ajout du groupe
		int countGrp = queueClient.retrieveGroup(NEW_QUEUE_NAME).size();
		queueClient.addGroup(NEW_QUEUE_NAME, NEW_GROUP_NAME);
		assertEquals("Erreur de récupération de la liste des groupes en (Add)", countGrp + 1,
				queueClient.retrieveGroup(NEW_QUEUE_NAME).size());
		// Retirer le groupe
		countGrp = queueClient.retrieveGroup(NEW_QUEUE_NAME).size();
		queueClient.removeGroup(NEW_QUEUE_NAME, NEW_GROUP_NAME);
		assertEquals("Erreur de récupération de la liste des groupes en (Remove)", countGrp - 1,
				queueClient.retrieveGroup(NEW_QUEUE_NAME).size());
	}

	@Test
	public void testTableExploitQueue() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		// Ajout du groupe
		int countTableExploit = queueClient.retrieveTableExploit(NEW_QUEUE_NAME).size();
		queueClient.addTableExploit(NEW_QUEUE_NAME, NEW_TABLEEXPLOIT_NAME);
		assertEquals("Erreur de récupération de la liste des tables d'exploitations en (Add)", countTableExploit + 1,
				queueClient.retrieveTableExploit(NEW_QUEUE_NAME).size());
		// Retirer le groupe
		countTableExploit = queueClient.retrieveTableExploit(NEW_QUEUE_NAME).size();
		queueClient.removeTableExploit(NEW_QUEUE_NAME, NEW_TABLEEXPLOIT_NAME);
		assertEquals("Erreur de récupération de la liste des tables d'exploitations en (Remove)", countTableExploit - 1,
				queueClient.retrieveTableExploit(NEW_QUEUE_NAME).size());
	}

	@Test
	public void testQueueJobs() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.byName(NEW_QUEUE_NAME);
		// récupération du job crée
		List<JobDto> listJobsQueues = queueClient.jobsByStatus(NEW_QUEUE_NAME, JobStatusType.WAIT);
		List<String> jobsOnQueueWait = new ArrayList<>();
		for (JobDto jobDto : listJobsQueues) {
			jobsOnQueueWait.add(jobDto.getJobName());
		}
		assertTrue("Erreur de la récupération du job crée en wait", jobsOnQueueWait.contains(NEW_JOB_NAME));
	}

	@Test
	public void testQueueProperties() throws ValidationException, RestException {
		QueueClient queueClient = serveur.queues();
		int countListProperties = queueClient.allProperties(NEW_QUEUE_NAME).size();
		// Create properties
		QueuePropertyDto queuePropertyDto = new QueuePropertyDto();
		queuePropertyDto.setName(NEW_QUEUEPROPERTY_NAME);
		queuePropertyDto.setDescription(NEW_QUEUEPROPERTY_DESC);
		queuePropertyDto.setTxt(NEW_QUEUEPROPERTY_VALUE);
		queuePropertyDto.setDisplay(NEW_QUEUEPROPERTY_DISPLAY);
		queuePropertyDto.setScriptExport(NEW_QUEUEPROPERTY_SCRIPTEXPORT);
		queuePropertyDto.setPersist(NEW_QUEUEPROPERTY_PERSIST);
		queuePropertyDto.setOverridable(NEW_QUEUEPROPERTY_OVERRIDABLE);
		queuePropertyDto.setJobExport(NEW_QUEUEPROPERTY_JOBEXPORT);
		queuePropertyDto = queueClient.createProperty(NEW_QUEUE_NAME, queuePropertyDto);
		assertEquals("Erreur au niveau de la liste des propriétés", countListProperties + 1,
				queueClient.allProperties(NEW_QUEUE_NAME).size());
		// update queue properties
		queuePropertyDto.setDescription(UPDATE_QUEUEPROPERTY_DESC);
		queuePropertyDto.setTxt(UPDATE_QUEUEPROPERTY_VALUE);
		queuePropertyDto.setDisplay(UPDATE_QUEUEPROPERTY_DISPLAY);
		queuePropertyDto.setScriptExport(UPDATE_QUEUEPROPERTY_SCRIPTEXPORT);
		queuePropertyDto.setPersist(UPDATE_QUEUEPROPERTY_PERSIST);
		queuePropertyDto.setOverridable(UPDATE_QUEUEPROPERTY_OVERRIDABLE);
		queuePropertyDto.setJobExport(UPDATE_QUEUEPROPERTY_JOBEXPORT);
		queuePropertyDto = queueClient.updateProperty(NEW_QUEUE_NAME, queuePropertyDto);
		queuePropertyDto = queueClient.retrieveProperty(NEW_QUEUE_NAME, NEW_QUEUEPROPERTY_NAME);
		// delete property
		queueClient.deleteProperty(NEW_QUEUE_NAME, NEW_QUEUEPROPERTY_NAME);
		try {
			queueClient.retrieveProperty(NEW_QUEUE_NAME, NEW_QUEUEPROPERTY_NAME);
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_ByName() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.byName(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_ByName() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_AllJobs() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.allJobs(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_AllJobs() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.allJobs("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_JobsByStatus() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.jobsByStatus(NEW_QUEUENOTFOUND_NAME, JobStatusType.ALL);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_JobsByStatus() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.jobsByStatus("", JobStatusType.ALL);
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_Status() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.status(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_Status() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.status("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_ById() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_AddGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.addGroup(NEW_QUEUENOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_AddGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.addGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_AddTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.addTableExploit(NEW_QUEUENOTFOUND_NAME, NEW_TABLEEXPLOITNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_AddTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.addTableExploit("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_RetrieveTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.retrieveTableExploit(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_RetrieveTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.retrieveTableExploit("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_RetrieveGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.retrieveGroup(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_RetrieveGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.retrieveGroup("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_AllProperties() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.allProperties(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_AllProperties() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.allProperties("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_RemoveTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.removeTableExploit(NEW_QUEUENOTFOUND_NAME, NEW_TABLEEXPLOITNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_RemoveTableExploit() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.removeTableExploit("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_RemoveGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.removeGroup(NEW_QUEUENOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_RemoveGroup() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.removeGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_Delete() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.delete(NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_Delete() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_DeleteProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.deleteProperty(NEW_QUEUENOTFOUND_NAME, NEW_QUEUEPROPERTYNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_DeleteProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.deleteProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_CreateProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueuePropertyDto queuePropertyDto = new QueuePropertyDto();
		queuePropertyDto.setName(NEW_QUEUEPROPERTYNOTFOUND_NAME);
		queueClient.createProperty(NEW_QUEUENOTFOUND_NAME, queuePropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_CreateProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueuePropertyDto queuePropertyDto = new QueuePropertyDto();
		queuePropertyDto.setName("");
		queueClient.createProperty("", queuePropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_RetrieveProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.retrieveProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_UpdateProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueuePropertyDto queuePropertyDto = new QueuePropertyDto();
		queuePropertyDto.setName(NEW_QUEUEPROPERTYNOTFOUND_NAME);
		queueClient.updateProperty(NEW_QUEUENOTFOUND_NAME, queuePropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_UpdateProperty() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueuePropertyDto queuePropertyDto = new QueuePropertyDto();
		queuePropertyDto.setName("");
		queueClient.updateProperty("", queuePropertyDto);
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_Create() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.create(NEW_QUEUENOTFOUND_NAME, "model");
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_Create() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.create("", "");
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_Update() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueueDto queueDto = new QueueDto();
		queueDto.setName(NEW_QUEUENOTFOUND_NAME);
		queueClient.update(queueDto);
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_Update() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		QueueDto queueDto = new QueueDto();
		queueDto.setName("");
		queueClient.update(queueDto);
	}

	@Test(expected = RestException.class)
	public void testQueue_NotFound_For_UpdateStatus() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.updateStatus(NEW_QUEUENOTFOUND_NAME, QueueStatusType.OPEN, "desc");
	}

	@Test(expected = ValidationException.class)
	public void testQueue_Empty_For_UpdateStatus() throws RestException, ValidationException {
		QueueClient queueClient = serveur.queues();
		queueClient.updateStatus("", QueueStatusType.OPEN, "");
	}

	@AfterClass
	public static void clearQueue() throws ValidationException, RestException {
		serveur.jobs().delete(jobCreated.getJobId());
		serveur.services().lunchPurgeManual();
		// fermeture du status de la file de traitement afin de la supprimer
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, "statusDescQueue");
		serveur.queues().delete(queueDto);
		serveur.devices().delete(deviceDto);
		serveur.agents().delete(agentDto);
		serveur.groups().delete(groupeDto);
		serveur.tableExploit().delete(tableExploitDto);
	}

}
