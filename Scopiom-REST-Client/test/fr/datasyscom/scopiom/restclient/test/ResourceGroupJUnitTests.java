package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.QueueDto;
import fr.datasyscom.scopiom.rest.pojo.ResourceGroupDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.queue.QueueClient.QueueStatusType;
import fr.datasyscom.scopiom.restclient.resourcegroup.ResourceGroupClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class ResourceGroupJUnitTests {
	// new resouceGrp
	private static ResourceGroupDto resourceGroupDto;
	private static final String NEW_RESOURCEGRP_NAME = "RESOURCEGRP" + System.currentTimeMillis();
	private static final String NEW_RESOURCEGRP_DESC = "descResourcegrp";
	// update
	private static final String UPDATE_RESOURCEGRP_NAME = "RESOURCEUPDATE" + System.currentTimeMillis();
	private static final String UPDATE_RESOURCEGRP_DESC = "descupdate";
	// for test
	private static final String NEW_RESOURCEGRPNOTFOUND_NAME = "RESOURCEGRPTEST" + System.currentTimeMillis();
	private static final String NEW_DEVICENOTFOUND_NAME = "DEVICETEST" + System.currentTimeMillis();
	private static final String NEW_QUEUENOTFOUND_NAME = "QUEUETEST" + System.currentTimeMillis();
	private static final String NEW_WORKFLOWNOTFOUND_NAME = "WORKFLOWTEST" + System.currentTimeMillis();
	// new queue
	private static QueueDto queueDto;
	private static final String NEW_QUEUE_NAME = "NEWQUEUE-" + System.currentTimeMillis();
	// new device
	private static DeviceDto deviceDto;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE-" + System.currentTimeMillis();
	private static long idResourceGrpCreated;

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// create ressource grp
		int initialCountListResourceGrp = serveur.resourcesGroups().all().size();
		resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName(NEW_RESOURCEGRP_NAME);
		resourceGroupDto.setDescription(NEW_RESOURCEGRP_DESC);
		resourceGroupDto = serveur.resourcesGroups().create(resourceGroupDto);
		idResourceGrpCreated = resourceGroupDto.getId();
		// check
		assertEquals("Erreur list resourceGrp", initialCountListResourceGrp + 1,
				serveur.resourcesGroups().all().size());
		assertEquals("Mauvais identifiant", idResourceGrpCreated, resourceGroupDto.getId());
		assertEquals("Mauvais nom", NEW_RESOURCEGRP_NAME, resourceGroupDto.getName());
		assertEquals("Mauvaise description", NEW_RESOURCEGRP_DESC, resourceGroupDto.getDescription());
		resourceGroupDto = serveur.resourcesGroups().byName(NEW_RESOURCEGRP_NAME);
		// mise à jour
		resourceGroupDto.setName(UPDATE_RESOURCEGRP_NAME);
		resourceGroupDto.setDescription(UPDATE_RESOURCEGRP_DESC);
		resourceGroupDto = serveur.resourcesGroups().update(NEW_RESOURCEGRP_NAME, resourceGroupDto);
		assertEquals("Mauvais identifiant", idResourceGrpCreated, resourceGroupDto.getId());
		assertEquals("Mauvais nom en update ", UPDATE_RESOURCEGRP_NAME, resourceGroupDto.getName());
		assertEquals("Mauvaise description en update", UPDATE_RESOURCEGRP_DESC, resourceGroupDto.getDescription());
		// create queue
		queueDto = new QueueDto();
		queueDto = serveur.queues().create(NEW_QUEUE_NAME, "model_batch");
		// create device
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
	}

	@Test
	public void test_resourceGrpQueues() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		int listQueuesCount = ressourceGrpClient.retrieveQueues(UPDATE_RESOURCEGRP_NAME).size();
		ressourceGrpClient.addQueue(UPDATE_RESOURCEGRP_NAME, NEW_QUEUE_NAME);
		assertEquals("Erreur d'ajout d'une queue", listQueuesCount + 1,
				ressourceGrpClient.retrieveQueues(UPDATE_RESOURCEGRP_NAME).size());
		listQueuesCount = ressourceGrpClient.retrieveQueues(UPDATE_RESOURCEGRP_NAME).size();
		ressourceGrpClient.removeQueue(UPDATE_RESOURCEGRP_NAME, NEW_QUEUE_NAME);
		assertEquals("Erreur de supression d'une queue", listQueuesCount - 1,
				ressourceGrpClient.retrieveQueues(UPDATE_RESOURCEGRP_NAME).size());
	}

	@Test
	public void test_resourceGrpDevices() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		int listDevicesCount = ressourceGrpClient.retrieveDevices(UPDATE_RESOURCEGRP_NAME).size();
		ressourceGrpClient.addDevice(UPDATE_RESOURCEGRP_NAME, NEW_DEVICE_NAME);
		assertEquals("Erreur d'ajout d'un périphérique", listDevicesCount + 1,
				ressourceGrpClient.retrieveDevices(UPDATE_RESOURCEGRP_NAME).size());
		listDevicesCount = ressourceGrpClient.retrieveDevices(UPDATE_RESOURCEGRP_NAME).size();
		ressourceGrpClient.removeDevice(UPDATE_RESOURCEGRP_NAME, NEW_DEVICE_NAME);
		assertEquals("Erreur de supression d'un périphérique", listDevicesCount - 1,
				ressourceGrpClient.retrieveDevices(UPDATE_RESOURCEGRP_NAME).size());
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_ByName() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.byName(NEW_RESOURCEGRPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_ByName() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RetrieveWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveWorkflow(NEW_RESOURCEGRPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RetrieveWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveWorkflow("");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_AddWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addWorkflow(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_AddWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addWorkflow("", "");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RemoveWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeWorkflow(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RemoveWorkflow() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeWorkflow("", "");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_Create() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName(UPDATE_RESOURCEGRP_NAME);
		ressourceGrpClient.create(resourceGroupDto);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_Create() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName("");
		ressourceGrpClient.create(resourceGroupDto);
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_Delete() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName(NEW_RESOURCEGRPNOTFOUND_NAME);
		ressourceGrpClient.delete(resourceGroupDto);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_Delete() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName("");
		ressourceGrpClient.delete(resourceGroupDto);
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_Update() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName(NEW_RESOURCEGRPNOTFOUND_NAME);
		ressourceGrpClient.update(NEW_RESOURCEGRPNOTFOUND_NAME, resourceGroupDto);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_Update() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ResourceGroupDto resourceGroupDto = new ResourceGroupDto();
		resourceGroupDto.setName("");
		ressourceGrpClient.update("", resourceGroupDto);
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_AddDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addDevice(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_DEVICENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_AddDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addDevice("", "");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RetrieveDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveDevices(NEW_RESOURCEGRPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RetrieveDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveDevices("");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RetrieveQueues() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveQueues(NEW_RESOURCEGRPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RetrieveQueues() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.retrieveQueues("");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RemoveDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeDevice(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_DEVICENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RemoveDevice() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeDevice("", "");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_AddQueue() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addQueue(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_AddQueue() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.addQueue("", "");
	}

	@Test(expected = RestException.class)
	public void testResourceGrp_NotFound_For_RemoveQueue() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeQueue(NEW_RESOURCEGRPNOTFOUND_NAME, NEW_QUEUENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testResourceGrp_Empty_For_RemoveQueue() throws ValidationException, RestException {
		ResourceGroupClient ressourceGrpClient = serveur.resourcesGroups();
		ressourceGrpClient.removeQueue("", "");
	}

	@AfterClass
	public static void clear() throws ValidationException, RestException {
		// fermeture du status de la file de traitement afin de la supprimer
		queueDto = serveur.queues().updateStatus(NEW_QUEUE_NAME, QueueStatusType.CLOSE, null);
		serveur.queues().delete(queueDto);
		serveur.resourcesGroups().delete(resourceGroupDto);
		serveur.devices().delete(deviceDto);
	}

}
