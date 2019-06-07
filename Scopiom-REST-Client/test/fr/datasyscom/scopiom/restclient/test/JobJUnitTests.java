package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.client.executor.groovy.Nj;
import fr.datasyscom.scopiom.client.executor.groovy.Nj.NJResult;
import fr.datasyscom.scopiom.client.sqr.ParsingException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.rest.pojo.JobPropertyDto;
import fr.datasyscom.scopiom.rest.pojo.JobStatusDto;
import fr.datasyscom.scopiom.rest.pojo.MediaDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class JobJUnitTests {

	private static ScopIOMServer serveur;
	// Queue
	private static final String NEW_QUEUE_NAME = "NEWQUEUE-" + System.currentTimeMillis();
	// new Job Property
	private final String NEW_JOBPROPERTY_NAME = "SCOPIOM_JOBNAMEPROPERTY-" + System.currentTimeMillis();
	private final String NEW_JOBPROPERTY_VALUE = "valueJob";
	private final boolean NEW_JOBPROPERTY_PERSIST = true;
	// update Job Property
	private final String UPDATE_JOBPROPERTY_NAME = "SCOPIOM_JOBNAMEPROPERTYUPDATE-" + System.currentTimeMillis();
	private final String UPDATE_JOBPROPERTY_VALUE = "valueJobUpdate";
	private final boolean UPDATE_JOBPROPERTY_PERSIST = false;
	private static QueueDto queue;
	// status queue
	private final static String UPDATE_JOBSTATUS_DESC = "descJob";
	// new media
	private static MediaDto media;
	private static long idMediaCreated;
	private static final String NEW_MEDIA_NAME = "NEWMEDIA" + System.currentTimeMillis();
	// new device
	private static DeviceDto deviceDto;
	private static long idDeviceCreated;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE" + System.currentTimeMillis();
	// job status
	private static final String NEW_JOBSTATUS_STATUS = "OK";
	private static final String NEW_JOBSTATUS_STATUSDESC = "descJob";
	private static final Integer NEW_JOBSTATUS_EXITCODE = 10;
	// new job
	private static JobDto job;
	private static long idjobCreated;
	private static final String NEW_JOB_NAME = "newJob-" + System.currentTimeMillis();

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// Création d'une queue
		queue = serveur.queues().create(NEW_QUEUE_NAME, "MODEL_PRINT");
		// Open statut de la queue pour pouvoir la relier au job
		queue = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.OPEN, UPDATE_JOBSTATUS_DESC);
		// création d'un media
		media = new MediaDto();
		media.setName(NEW_MEDIA_NAME);
		media = serveur.medias().create(media);
		idMediaCreated = media.getId();
		// Création d'un device
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
		idDeviceCreated = deviceDto.getId();
		// Création d'un job
		Nj nj = new Nj();
		NJResult jobCreated = nj.server("localhost:4848").queue(NEW_QUEUE_NAME).jobName(NEW_JOB_NAME).user("iomadmin")
				.submit();
		// Récupération du job
		JobDto job = serveur.jobs().byId(jobCreated.getJobId());
		idjobCreated = job.getId();
		assertEquals("Mauvais id job", idjobCreated, job.getId());
		assertEquals("Mauvais nom de job", NEW_JOB_NAME, job.getJobName());
		assertEquals("Mauvaise file de traitement", NEW_QUEUE_NAME, job.getQueueName());
	}

	@Test
	public void testJob() throws RestException {
		JobClient jobClient = serveur.jobs();
		// Ajout d'un media
		jobClient.setMedia(idjobCreated, idMediaCreated);
		job = jobClient.byId(idjobCreated);
		assertEquals("Erreur ajout du media au job", idMediaCreated, job.getMedia().intValue());
		// retire le media
		jobClient.removeMedia(idjobCreated);
		job = jobClient.byId(idjobCreated);
		assertNull("Erreur de suppression du media du job", job.getMedia());
		// Ajout d'un device
		jobClient.setDevice(idjobCreated, idDeviceCreated);
		job = jobClient.byId(idjobCreated);
		assertEquals("Erreur ajout du device au job", idDeviceCreated, job.getDevice().intValue());
		// retire un device
		jobClient.removeDevice(idjobCreated);
		job = jobClient.byId(idjobCreated);
		assertNull("Erreur de suppression du device du job", job.getDevice());
		// Mise à jour du status de job
		JobStatusDto jobStatusDto = new JobStatusDto();
		jobStatusDto.setStatus(NEW_JOBSTATUS_STATUS);
		jobStatusDto.setStatusDesc(NEW_JOBSTATUS_STATUSDESC);
		jobStatusDto.setExitCode(NEW_JOBSTATUS_EXITCODE);
		job = jobClient.setStatus(idjobCreated, jobStatusDto);
		assertEquals("Mauvais id job", idjobCreated, job.getId());
		assertEquals("Mauvais statut du job", NEW_JOBSTATUS_STATUS, job.getStatus());
		assertEquals("Mauvaise description du statut job", NEW_JOBSTATUS_STATUSDESC, job.getStatusDesc());
		assertEquals("Mauvais code exit du job", NEW_JOBSTATUS_EXITCODE, job.getExitCode());
		// kill job
		jobClient.kill(idjobCreated);
		job = jobClient.byId(idjobCreated);
		assertEquals("Erreur lors de l'arrêt du job", JobStatusType.ERROR.name(), job.getStatus());
	}

	@Test
	public void testJob_property() throws ParsingException, RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		int countJobProperties = jobClient.allProperties(idjobCreated).size();
		// Création d'une propriété
		JobPropertyDto jobPropertyDto = new JobPropertyDto();
		jobPropertyDto.setName(NEW_JOBPROPERTY_NAME);
		jobPropertyDto.setTxt(NEW_JOBPROPERTY_VALUE);
		jobPropertyDto.setPersist(NEW_JOBPROPERTY_PERSIST);
		jobPropertyDto = jobClient.createProperty(idjobCreated, jobPropertyDto);
		assertEquals("Mauvais nom de propriété", NEW_JOBPROPERTY_NAME, jobPropertyDto.getName());
		assertEquals("Mauvaise valeur", NEW_JOBPROPERTY_VALUE, jobPropertyDto.getTxt());
		assertEquals("Mauvaise valeur de persistant", NEW_JOBPROPERTY_PERSIST, jobPropertyDto.isPersist());
		// Check list job properties
		assertEquals("Erreur list job properties", countJobProperties + 1,
				jobClient.allProperties(idjobCreated).size());
		// update job property
		jobPropertyDto.setName(UPDATE_JOBPROPERTY_NAME);
		jobPropertyDto.setTxt(UPDATE_JOBPROPERTY_VALUE);
		jobPropertyDto.setPersist(UPDATE_JOBPROPERTY_PERSIST);
		jobPropertyDto = jobClient.updateProperty(idjobCreated, NEW_JOBPROPERTY_NAME, jobPropertyDto);
		// Récupération de la propriété du job
		jobPropertyDto = jobClient.retrieveProperty(idjobCreated, UPDATE_JOBPROPERTY_NAME);
		assertEquals("Mauvais nom de propriété après update", UPDATE_JOBPROPERTY_NAME, jobPropertyDto.getName());
		assertEquals("Mauvaise valeur après update", UPDATE_JOBPROPERTY_VALUE, jobPropertyDto.getTxt());
		assertEquals("Mauvaise valeur de persistant après update", UPDATE_JOBPROPERTY_PERSIST,
				jobPropertyDto.isPersist());
		// delete job property
		jobClient.deleteProperty(idjobCreated, UPDATE_JOBPROPERTY_NAME);
		try {
			jobClient.retrieveProperty(idjobCreated, UPDATE_JOBPROPERTY_NAME);
			fail("Suppression job property KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
		// delete job
		jobClient.delete(idjobCreated);
		try {
			// Ce met dans la liste des jobs supprimés
			jobClient.byId(idjobCreated);
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_ById() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_Delete() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.delete(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_AllProperties() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.allProperties(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_SetStatus() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobStatusDto jobStatusDto = new JobStatusDto();
		jobStatusDto.setStatus("wrongStatus");
		jobClient.setStatus(-System.currentTimeMillis(), jobStatusDto);
	}

	@Test(expected = ValidationException.class)
	public void testJob_Empty_For_SetStatus() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobStatusDto jobStatusDto = new JobStatusDto();
		jobStatusDto.setStatus("");
		jobClient.setStatus(-System.currentTimeMillis(), jobStatusDto);
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_SetDevice() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.setDevice(-System.currentTimeMillis(), -System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_SetMedia() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.setMedia(-System.currentTimeMillis(), -System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_RemoveDevice() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.removeDevice(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_RemoveMedia() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.removeMedia(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_Kill() throws RestException {
		JobClient jobClient = serveur.jobs();
		jobClient.kill(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_RetrieveProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		jobClient.retrieveProperty(-System.currentTimeMillis(), NEW_JOBPROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testJob_Empty_For_RetrieveProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		jobClient.retrieveProperty(-System.currentTimeMillis(), "");
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_DeleteProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		jobClient.deleteProperty(-System.currentTimeMillis(), NEW_JOBPROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testJob_Empty_For_DeleteProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		jobClient.deleteProperty(-System.currentTimeMillis(), "");
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_CreateProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobPropertyDto jobPropertyDto = new JobPropertyDto();
		jobPropertyDto.setName(NEW_JOBPROPERTY_NAME);
		jobClient.createProperty(-System.currentTimeMillis(), jobPropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testJob_Empty_For_CreateProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobPropertyDto jobPropertyDto = new JobPropertyDto();
		jobPropertyDto.setName("");
		jobClient.createProperty(-System.currentTimeMillis(), jobPropertyDto);
	}

	@Test(expected = RestException.class)
	public void testJob_NotFound_For_UpdateProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobPropertyDto jobPropertyDto = new JobPropertyDto();
		jobPropertyDto.setName(UPDATE_JOBPROPERTY_NAME);
		jobClient.updateProperty(-System.currentTimeMillis(), NEW_JOBPROPERTY_NAME, jobPropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testJob_Empty_For_UpdateProperty() throws RestException, ValidationException {
		JobClient jobClient = serveur.jobs();
		JobPropertyDto jobPropertyDto = new JobPropertyDto();
		jobPropertyDto.setName("");
		jobClient.updateProperty(-System.currentTimeMillis(), "", jobPropertyDto);
	}

	@AfterClass
	public static void deleteQueue() throws ValidationException, RestException {
		// pas possible de supprimer la queue alors qu'elle a un job attaché
		serveur.services().lunchPurgeManual();
		queue = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, UPDATE_JOBSTATUS_DESC);
		serveur.queues().delete(queue);
		serveur.medias().delete(media);
		serveur.devices().delete(deviceDto);
	}

}
