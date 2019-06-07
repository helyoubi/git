package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.client.executor.groovy.Nj;
import fr.datasyscom.scopiom.client.executor.groovy.Nj.NJResult;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.DevicePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceSnmpDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceStatusDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.MediaDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.restclient.device.DeviceClient;
import fr.datasyscom.scopiom.restclient.device.DeviceClient.DeviceStatusType;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class DeviceJUnitTests {
	// new device
	private static DeviceDto deviceDto;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE" + System.currentTimeMillis();
	// update device
	private final String UPDATE_DEVICE_DESC = "updateDeviceDesc";
	private final String UPDATE_DEVICE_COMMENT = "updateDeviceComment";
	private final int UPDATE_DEVICE_MAXTHREAD = 2;
	private final int UPDATE_DEVICE_SPEED = 3;
	private final String UPDATE_DEVICE_URLADMIN = "http://urlAdmin";
	private final String UPDATE_DEVICE_TRIGGERCMD = "triggerCmd";
	private final boolean UPDATE_DEVICE_USEMEDIA = true;
	// for empty test
	private static final String NEW_DEVICE_NOTFOUND_NAME = "NEWDEVICETEST-" + System.currentTimeMillis();
	private static final String NEW_GROUPENOTFOUND_NAME = "NEWGROUPTEST-" + System.currentTimeMillis();;
	// new device property
	private static final String NEW_DEVICEPROPERTY_NAME = "SCOPIOM_NEWPROP" + System.currentTimeMillis();
	private static final String NEW_DEVICEPROPERTY_DESC = "newDescProp";
	private static final String NEW_DEVICEPROPERTY_VALUE = "newValueProp";
	private static final boolean NEW_DEVICEPROPERTY_DISPLAY = true;
	private static final boolean NEW_DEVICEPROPERTY_SCRIPTEXPORT = true;
	// update device property
	private static final String UPDATE_DEVICEPROPERTY_DESC = "updateDescProp";
	private static final String UPDATE_DEVICEPROPERTY_VALUE = "updateValueProp";
	private static final boolean UPDATE_DEVICEPROPERTY_DISPLAY = false;
	private static final boolean UPDATE_DEVICEPROPERTY_SCRIPTEXPORT = false;
	// update device snmp
	private static final int UPDATE_DEVICESNMP_DELAI = 56;
	private static final String UPDATE_DEVICESNMP_CMDPROCESS = "snmp_printer.bat";
	private static final Boolean UPDATE_DEVICESNMP_BEFOREJOB = true;
	private static final Boolean UPDATE_DEVICESNMP_ONLYONERROR = true;
	// new queue
	private static QueueDto queue;
	private static final String NEW_QUEUE_NAME = "NEWQUEUE" + System.currentTimeMillis();
	// new media
	private static MediaDto media;
	private static final String NEW_MEDIA_NAME = "NEWMEDIA" + System.currentTimeMillis();
	// new groupe
	private static GroupeDto groupe;
	private static final String NEW_GROUP_NAME = "NEWGROUP" + System.currentTimeMillis();
	// new job
	private static NJResult jobCreated;
	private static final String NEW_JOB_NAME = "newJob-" + System.currentTimeMillis();

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// Création d'un device
		int initialcountDevice = serveur.devices().all().size();
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
		assertEquals("Erreur list device", initialcountDevice + 1, serveur.devices().all().size());
		// création d'un media
		media = new MediaDto();
		media.setName(NEW_MEDIA_NAME);
		media = serveur.medias().create(media);
		// création d'un groupe
		groupe = new GroupeDto();
		groupe.setName(NEW_GROUP_NAME);
		groupe = serveur.groups().create(groupe);
		// création d'une queue
		queue = new QueueDto();
		queue = serveur.queues().create(NEW_QUEUE_NAME, "model_batch");
		queue = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.HOLD, null);
		// création d'un job
		// FIXME : La récupération des jobs pour les périphériques ne marche pas , dù a
		// un bug au niveau de SCOPIOM, après la création du job
		// int initialJobsDeviceCount =
		// serveur.devices().allJobs(NEW_DEVICE_NAME).size();
		Nj nj = new Nj();
		jobCreated = nj.server("localhost:4848").queue(NEW_QUEUE_NAME).device(NEW_DEVICE_NAME).jobName(NEW_JOB_NAME)
				.user("iomadmin").submit();
		// assertEquals("Erreur list jobs du device", initialJobsDeviceCount + 1,
		// serveur.devices().allJobs(NEW_DEVICE_NAME).size());
	}

	@Test
	public void testDevice() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		long idDeviceCreated = deviceDto.getId();
		// récupération par id
		deviceDto = deviceClient.byId(idDeviceCreated);
		// changement du status open
		DeviceStatusDto deviceStatus = deviceClient.open(NEW_DEVICE_NAME);
		assertEquals("Erreur status device open", DeviceStatusType.OPEN.name(), deviceStatus.getStatus());
		// changement du status close
		deviceStatus = deviceClient.close(NEW_DEVICE_NAME);
		assertEquals("Erreur status device close", DeviceStatusType.CLOSE.name(), deviceStatus.getStatus());
		// récupération par nom
		deviceDto = deviceClient.byName(NEW_DEVICE_NAME);
		// update
		deviceDto.setDescription(UPDATE_DEVICE_DESC);
		deviceDto.setComment(UPDATE_DEVICE_COMMENT);
		deviceDto.setMaxThread(UPDATE_DEVICE_MAXTHREAD);
		deviceDto.setSpeed(UPDATE_DEVICE_SPEED);
		deviceDto.setUrlAdmin(UPDATE_DEVICE_URLADMIN);
		deviceDto.setTriggerCmdChangeStatus(UPDATE_DEVICE_TRIGGERCMD);
		deviceDto.setUseMedia(UPDATE_DEVICE_USEMEDIA);
		deviceDto.setMediaName(NEW_MEDIA_NAME);
		deviceDto = deviceClient.update(deviceDto);
		assertEquals("Mauvaise description du device", UPDATE_DEVICE_DESC, deviceDto.getDescription());
		assertEquals("Mauvais commentaire du device", UPDATE_DEVICE_COMMENT, deviceDto.getComment());
		assertEquals("Mauvaise valeur max thread du device", UPDATE_DEVICE_MAXTHREAD,
				deviceDto.getMaxThread().intValue());
		assertEquals("Mauvaise valeur du speed du device", UPDATE_DEVICE_SPEED, deviceDto.getSpeed().intValue());
		assertEquals("Mauvais url admin du device", UPDATE_DEVICE_URLADMIN, deviceDto.getUrlAdmin());
		assertEquals("Mauvaise commande sur changement de statut du device", UPDATE_DEVICE_TRIGGERCMD,
				deviceDto.getTriggerCmdChangeStatus());
		assertEquals("Mauvaise valeur pour l'utilisation du media du device", UPDATE_DEVICE_USEMEDIA,
				deviceDto.getUseMedia());
		assertEquals("Mauvais media du device", NEW_MEDIA_NAME, deviceDto.getMediaName());
		// ajout d'un groupe
		int initialGrpDeviceCount = deviceClient.retrieveGroup(NEW_DEVICE_NAME).size();
		deviceClient.addGroup(NEW_DEVICE_NAME, NEW_GROUP_NAME);
		assertEquals("Erreur list groupe device en add", initialGrpDeviceCount + 1,
				deviceClient.retrieveGroup(NEW_DEVICE_NAME).size());
		// retirer le groupe
		initialGrpDeviceCount = deviceClient.retrieveGroup(NEW_DEVICE_NAME).size();
		deviceClient.removeGroup(NEW_DEVICE_NAME, NEW_GROUP_NAME);
		assertEquals("Erreur list groupe device en remove", initialGrpDeviceCount - 1,
				deviceClient.retrieveGroup(NEW_DEVICE_NAME).size());
	}

	@Test
	public void testProprtiesDevice() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		// Création d'une propriété
		// initialisation de la liste d'éléments des propriétés du device
		int countListDeviceProperties = deviceClient.allProperties(NEW_DEVICE_NAME).size();
		DevicePropertyDto devicePropertyDto = new DevicePropertyDto();
		devicePropertyDto.setName(NEW_DEVICEPROPERTY_NAME);
		devicePropertyDto.setDescription(NEW_DEVICEPROPERTY_DESC);
		devicePropertyDto.setTxt(NEW_DEVICEPROPERTY_VALUE);
		devicePropertyDto.setDisplay(NEW_DEVICEPROPERTY_DISPLAY);
		devicePropertyDto.setScriptExport(NEW_DEVICEPROPERTY_SCRIPTEXPORT);
		devicePropertyDto = deviceClient.createProperty(NEW_DEVICE_NAME, devicePropertyDto);
		assertEquals("Mauvais nom de la propriété du device", NEW_DEVICEPROPERTY_NAME, devicePropertyDto.getName());
		assertEquals("Mauvaise description de la propriété du device", NEW_DEVICEPROPERTY_DESC,
				devicePropertyDto.getDescription());
		assertEquals("Mauvaise valeur de la propriété du device", NEW_DEVICEPROPERTY_VALUE, devicePropertyDto.getTxt());
		assertEquals("Mauvaise valeur pour display de la propriété du device", NEW_DEVICEPROPERTY_DISPLAY,
				devicePropertyDto.isDisplay());
		assertEquals("Mauvaise valeur pour script export de la propriété du device", NEW_DEVICEPROPERTY_SCRIPTEXPORT,
				devicePropertyDto.isScriptExport());
		// Check list properties du device
		assertEquals("Erreur de récupération de la liste des propriétés", countListDeviceProperties + 1,
				deviceClient.allProperties(NEW_DEVICE_NAME).size());
		// récupération de la propriété crée
		devicePropertyDto = deviceClient.retrieveProperty(NEW_DEVICE_NAME, NEW_DEVICEPROPERTY_NAME);
		// Mise à jour de la propriété
		devicePropertyDto.setDescription(UPDATE_DEVICEPROPERTY_DESC);
		devicePropertyDto.setTxt(UPDATE_DEVICEPROPERTY_VALUE);
		devicePropertyDto.setDisplay(UPDATE_DEVICEPROPERTY_DISPLAY);
		devicePropertyDto.setScriptExport(UPDATE_DEVICEPROPERTY_SCRIPTEXPORT);
		devicePropertyDto = deviceClient.updateProperty(NEW_DEVICE_NAME, devicePropertyDto);
		assertEquals("Mauvaise description de la propriété du device en update", UPDATE_DEVICEPROPERTY_DESC,
				devicePropertyDto.getDescription());
		assertEquals("Mauvaise valeur de la propriété du device en update", UPDATE_DEVICEPROPERTY_VALUE,
				devicePropertyDto.getTxt());
		assertEquals("Mauvaise valeur pour display de la propriété du device en update", UPDATE_DEVICEPROPERTY_DISPLAY,
				devicePropertyDto.isDisplay());
		assertEquals("Mauvaise valeur pour script export de la propriété du device en update",
				UPDATE_DEVICEPROPERTY_SCRIPTEXPORT, devicePropertyDto.isScriptExport());
		// Suppression de la propriété du device
		deviceClient.deleteProperty(NEW_DEVICE_NAME, NEW_DEVICEPROPERTY_NAME);
		try {
			deviceClient.retrieveProperty(NEW_DEVICE_NAME, NEW_DEVICEPROPERTY_NAME);
			fail("Suppression de la propriété du device KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test
	public void testConfigurationSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		// Mise à jour de la configuration snmp
		DeviceSnmpDto deviceSnmpDto = new DeviceSnmpDto();
		deviceSnmpDto.setDelai(UPDATE_DEVICESNMP_DELAI);
		deviceSnmpDto.setCommandProcess(UPDATE_DEVICESNMP_CMDPROCESS);
		deviceSnmpDto.setBeforeJob(UPDATE_DEVICESNMP_BEFOREJOB);
		deviceSnmpDto.setOnlyOnError(UPDATE_DEVICESNMP_ONLYONERROR);
		deviceSnmpDto = deviceClient.updateSnmpConfiguration(NEW_DEVICE_NAME, deviceSnmpDto);
		// Récupérer la config snmp
		deviceSnmpDto = deviceClient.retrieveSnmp(NEW_DEVICE_NAME);
		assertEquals("Mauvais délai (snmp)", UPDATE_DEVICESNMP_DELAI, deviceSnmpDto.getDelai().intValue());
		assertEquals("Mauvaise commande d'interrogation", UPDATE_DEVICESNMP_CMDPROCESS,
				deviceSnmpDto.getCommandProcess());
		assertEquals("Mauvais paramètre d'interrogation before job", UPDATE_DEVICESNMP_BEFOREJOB,
				deviceSnmpDto.getBeforeJob());
		assertEquals("Mauvais paramètre d'interrogation only on error", UPDATE_DEVICESNMP_ONLYONERROR,
				deviceSnmpDto.isOnlyOnError());
		// lancer le service et check
		deviceClient.startSnmp(NEW_DEVICE_NAME);
		deviceSnmpDto = deviceClient.retrieveSnmp(NEW_DEVICE_NAME);
		assertEquals("Mauvais statut du service snmp en start", true, deviceSnmpDto.isActif());
		// arrêter le service et check
		deviceClient.stopSnmp(NEW_DEVICE_NAME);
		deviceSnmpDto = deviceClient.retrieveSnmp(NEW_DEVICE_NAME);
		assertEquals("Mauvais statut du service snmp en stop", false, deviceSnmpDto.isActif());
	}

//	@Test
//	public void testDeviceJobs() throws RestException, ValidationException {
//		DeviceClient deviceClient = serveur.devices();
//		deviceClient.byName(NEW_DEVICE_NAME);
//		// récupération du job crée
//		List<JobDto> listDeviceJobs = deviceClient.jobsByStatus(NEW_DEVICE_NAME, JobStatusType.WAIT);
//		List<String> jobsOnDeviceWait = new ArrayList<>();
//		for (JobDto jobDto : listDeviceJobs) {
//			jobsOnDeviceWait.add(jobDto.getJobName());
//		}
//		assertTrue("Erreur de la récupération du job crée en wait", jobsOnDeviceWait.contains(NEW_JOB_NAME));
//	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_ByName() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.byName(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_ByName() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testDevice_Empty_ById() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_AllJobs() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.allJobs(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_AllJobs() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.allJobs("");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_JobsByStatus() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.jobsByStatus(NEW_DEVICE_NOTFOUND_NAME, JobStatusType.ALL);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_JobsByStatus() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.jobsByStatus("", JobStatusType.ALL);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_RetrieveSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.retrieveSnmp(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_RetrieveSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.retrieveSnmp("");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_Status() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.status(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_Status() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.status("");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_RetrieveGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.retrieveGroup(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_RetrieveGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.retrieveGroup("");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_RemoveGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.removeGroup(NEW_DEVICE_NOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_RemoveGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.removeGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_AddGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.addGroup(NEW_DEVICE_NOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_AddGroup() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.addGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_AllProperties() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.allProperties(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_AllProperties() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.allProperties("");
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_RetrieveProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.retrieveProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_Delete() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.delete(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_Delete() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_DeleteProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.deleteProperty(NEW_DEVICE_NOTFOUND_NAME, NEW_DEVICEPROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_DeleteProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.deleteProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_CreateProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DevicePropertyDto devicePropertyDto = new DevicePropertyDto();
		devicePropertyDto.setName(NEW_DEVICEPROPERTY_NAME);
		deviceClient.createProperty(deviceDto, devicePropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_CreateProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DevicePropertyDto devicePropertyDto = new DevicePropertyDto();
		devicePropertyDto.setName("");
		deviceClient.createProperty("", devicePropertyDto);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_UpdateProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DevicePropertyDto devicePropertyDto = new DevicePropertyDto();
		devicePropertyDto.setName(NEW_DEVICEPROPERTY_NAME);
		deviceClient.updateProperty(NEW_DEVICE_NOTFOUND_NAME, devicePropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_UpdateProperty() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DevicePropertyDto devicePropertyDto = new DevicePropertyDto();
		devicePropertyDto.setName("");
		deviceClient.updateProperty("", devicePropertyDto);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_Create() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.create(NEW_DEVICE_NOTFOUND_NAME, "ModelDevice");
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_Create() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.create("", "");
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_Update() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setName(NEW_DEVICE_NOTFOUND_NAME);
		deviceClient.update(deviceDto);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_Update() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setName("");
		deviceClient.update(deviceDto);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_UpdateSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.updateSnmpConfiguration(NEW_DEVICE_NOTFOUND_NAME, null);
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_UpdateSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.updateSnmpConfiguration("", null);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_StartSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.startSnmp(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_StopSnmp() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.stopSnmp(NEW_DEVICE_NOTFOUND_NAME);
	}

	@Test(expected = RestException.class)
	public void testDevice_NotFound_For_UpdateStatus() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.updateStatus(NEW_DEVICE_NOTFOUND_NAME, DeviceStatusType.CLOSE, "");
	}

	@Test(expected = ValidationException.class)
	public void testDevice_Empty_For_UpdateStatus() throws RestException, ValidationException {
		DeviceClient deviceClient = serveur.devices();
		deviceClient.updateStatus("", DeviceStatusType.CLOSE, "");
	}

	@AfterClass
	public static void clearDevice() throws ValidationException, RestException {
		serveur.jobs().delete(jobCreated.getJobId());
		serveur.services().lunchPurgeManual();
		queue = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, null);
		serveur.queues().delete(queue);
		serveur.devices().delete(deviceDto);
		serveur.medias().delete(media);
		serveur.groups().delete(groupe);
	}

}
