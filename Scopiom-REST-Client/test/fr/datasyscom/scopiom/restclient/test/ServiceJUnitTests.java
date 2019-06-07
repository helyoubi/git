package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.LpdDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeDto;
import fr.datasyscom.scopiom.rest.pojo.PurgeTmpDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.ScanWaitDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;
import fr.datasyscom.scopiom.restclient.service.ServiceClient;

public class ServiceJUnitTests {
	// new queue
	private static QueueDto queueDto;
	private static final String NEW_QUEUE_NAME = "NEWQUEUE-" + System.currentTimeMillis();
	// purge
	private static final int PURGE_HOUR = 11;
	private static final int PURGE_MIN = 05;
	private static final boolean PURGE_ORPHELIN = true;
	// lpd
	private static LpdDto lpdDto;
	private static final int LPD_PORT = 615;
	private static final int SHEDULER_DELAYRUN = 1001;
	private static final int SHEDULER_DELAYSCAN = 101;
	// purge tmp
	private static final int PURGETMP_DELAY = 61;
	private static final int PURGETMP_OLD = 601;
	private static final String PURGETMP_FOLDER = "C:\\ScopIOM\\4.0\\scopiom\\TMP";

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// création d'une queue
		queueDto = new QueueDto();
		queueDto = serveur.queues().create(NEW_QUEUE_NAME, "model_batch");
	}

	@Test
	public void testService_infos() throws RestException {
		ServiceClient serviceClient = serveur.services();
		assertNotNull("erreur récupération des statuts de services", serviceClient.infos());
	}

	@Test
	public void testService_purge() throws RestException {
		ServiceClient serviceClient = serveur.services();
		// start purge
		serviceClient.startPurge();
		assertEquals("Erreur start purge", "started", serviceClient.purge().getStatus());
		// stop purge
		serviceClient.stopPurge();
		assertEquals("Erreur stop purge", "stopped", serviceClient.purge().getStatus());
		// update
		PurgeDto purgeDto = new PurgeDto();
		purgeDto.setHour_purge(PURGE_HOUR);
		purgeDto.setMinute_purge(PURGE_MIN);
		purgeDto.setPurgeOrphelin(PURGE_ORPHELIN);
		purgeDto = serviceClient.updatePurge(purgeDto);
		assertEquals("Mauvaise heure de la purge", PURGE_HOUR, purgeDto.getHour_purge().intValue());
		assertEquals("Mauvaise minute de la purge", PURGE_MIN, purgeDto.getMinute_purge().intValue());
		assertEquals("Mauvaise valeur de répertoirs orphelins de la purge", PURGE_ORPHELIN, purgeDto.isPurgeOrphelin());
		try {
			serviceClient.lunchPurgeManual();
		} catch (RestException e) {
			fail("Erreur de lancement de la purge manuel" + e.getMessage());
		}
	}

	@Test
	public void testService_lpd() throws RestException {
		ServiceClient serviceClient = serveur.services();
		// start lpd
		serviceClient.startLpd();
		assertEquals("Erreur start lpd", "started", serviceClient.lpd().getStatus());
		// stop lpd
		serviceClient.stopLpd();
		assertEquals("Erreur stop lpd", "stopped", serviceClient.lpd().getStatus());
		// update
		lpdDto = new LpdDto();
		lpdDto.setPort(LPD_PORT);
		lpdDto.setDefaultQueue(NEW_QUEUE_NAME);
		lpdDto = serviceClient.updateLpd(lpdDto);
		assertEquals("Mauvaise port lpd", LPD_PORT, lpdDto.getPort().intValue());
		assertEquals("Mauvaise destination lpd", NEW_QUEUE_NAME, lpdDto.getDefaultQueue());
	}

	@Test
	public void testService_sheduler() throws RestException {
		ServiceClient serviceClient = serveur.services();
		// start scheduler
		serviceClient.startScheduler();
		assertEquals("Erreur start sheduler", "started", serviceClient.scheduler().getStatus());
		// stop scheduler
		serviceClient.stopScheduler();
		assertEquals("Erreur stop sheduler", "stopped", serviceClient.scheduler().getStatus());
		// update
		ScanWaitDto scanWaitDto = new ScanWaitDto();
		scanWaitDto.setDelayRun(SHEDULER_DELAYRUN);
		scanWaitDto.setDelayScan(SHEDULER_DELAYSCAN);
		scanWaitDto = serviceClient.updateScheduler(scanWaitDto);
		assertEquals("Mauvaise délai entre 2 scans au repos", SHEDULER_DELAYRUN, scanWaitDto.getDelayRun().intValue());
		assertEquals("Mauvaise délai minimum entre 2 scans en charge", SHEDULER_DELAYSCAN,
				scanWaitDto.getDelayScan().intValue());
	}

	@Test
	public void testService_purgeTmp() throws RestException {
		ServiceClient serviceClient = serveur.services();
		// start purge tmp
		serviceClient.startPurgeTmp();
		assertEquals("Erreur start purgeTmp", "started", serviceClient.purgeTmp().getStatus());
		// stop purge tmp
		serviceClient.stopPurgeTmp();
		assertEquals("Erreur stop purgeTmp", "stopped", serviceClient.purgeTmp().getStatus());
		// update
		PurgeTmpDto purgeTmpDto = new PurgeTmpDto();
		purgeTmpDto.setPurgeDelay(PURGETMP_DELAY);
		purgeTmpDto.setPurgeOld(PURGETMP_OLD);
		purgeTmpDto.setTmpFolder(PURGETMP_FOLDER);
		purgeTmpDto = serviceClient.updatePurgeTmp(purgeTmpDto);
		assertEquals("Mauvaise délai entre chaque purge", PURGETMP_DELAY, purgeTmpDto.getPurgeDelay().intValue());
		assertEquals("Mauvais age minimal d'un fichier", PURGETMP_OLD, purgeTmpDto.getPurgeOld().intValue());
		assertEquals("Mauvais répertoire temporaire", PURGETMP_FOLDER, purgeTmpDto.getTmpFolder());
	}

	@Test(expected = RestException.class)
	public void testService_NotFound_UpdatePurge() throws RestException, ValidationException {
		ServiceClient serviceClient = serveur.services();
		serviceClient.updatePurge(null);
	}

	@Test(expected = RestException.class)
	public void testService_NotFound_UpdatePurgeTmp() throws RestException, ValidationException {
		ServiceClient serviceClient = serveur.services();
		serviceClient.updatePurgeTmp(null);
	}

	@Test(expected = RestException.class)
	public void testService_NotFound_Scheduler() throws RestException, ValidationException {
		ServiceClient serviceClient = serveur.services();
		serviceClient.updateScheduler(null);
	}

	@Test(expected = RestException.class)
	public void testService_NotFound_Lpd() throws RestException, ValidationException {
		ServiceClient serviceClient = serveur.services();
		serviceClient.updateLpd(null);
	}

	@AfterClass
	public static void clear() throws ValidationException, RestException {
		ServiceClient serviceClient = serveur.services();
		// retirer la destination afin de pouvoir supprimer la queue
		lpdDto = serviceClient.lpd();
		lpdDto.setDefaultQueue("");
		serviceClient.updateLpd(lpdDto);
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, null);
		serveur.queues().delete(queueDto);

	}

}
