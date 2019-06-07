package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.TypeDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;
import fr.datasyscom.scopiom.restclient.type.TypeClient;

public class TypeJUnitTests {

	// new type
	private final String NEW_TYPE_NAME = "TYPE" + System.currentTimeMillis();
	private final String NEW_TYPE_DEFAULTEXTENSION = "ext" + System.currentTimeMillis();
	private final String NEW_TYPE_DEFAULTMIME = "mime" + System.currentTimeMillis();
	//update type
	private final String UPDATE_TYPE_ANALYSEFLUX = "analyseFluxUpdate" + System.currentTimeMillis();
	private final String UPDATE_TYPE_EXTRACTPAGECMD = "extractPageUpdate" + System.currentTimeMillis();
	private final int UPDATE_TYPE_TIMEOUT = 20;
	private final Boolean UPDATE_TYPE_ISDEFAULT = false;
	//ext,typeMime
	private final String ADD_TYPE_EXTENSION = "newext" + System.currentTimeMillis();
	private final String ADD_TYPE_MIME = "newmime" + System.currentTimeMillis();

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testType() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		long createdIdType;
		int initialCountSize = typeClient.all().size();
		// création du type
		TypeDto typeDto = new TypeDto();
		typeDto.setName(NEW_TYPE_NAME);
		typeDto.setDefaultExtension(NEW_TYPE_DEFAULTEXTENSION);
		typeDto.setDefaultMime(NEW_TYPE_DEFAULTMIME);
		typeDto = typeClient.create(typeDto);
		createdIdType = typeDto.getId();
		// check de la liste des types
		assertEquals("Erreur list type", initialCountSize + 1, typeClient.all().size());
		// Récupération par id
		typeDto = typeClient.byId(createdIdType);
		assertEquals("Mauvais ID", createdIdType, typeDto.getId());
		assertEquals("Mauvais nom", NEW_TYPE_NAME, typeDto.getName());
		assertEquals("Mauvais analyse du flux ", null, typeDto.getAnalyseFluxCmd());
		assertEquals("Mauvaise extraction de pages ", null, typeDto.getExtractPageCmd());
		assertEquals("Mauvais timeout", 60, typeDto.getTimeoutAnalyseFlux().intValue());
		assertEquals("Mauvais default type", false, typeDto.getDefaut());
		assertEquals("Mauvaise extension par défaut", NEW_TYPE_DEFAULTEXTENSION, typeDto.getDefaultExtension());
		assertEquals("Mauvais type mime par défault", NEW_TYPE_DEFAULTMIME, typeDto.getDefaultMime());
		// Update
		typeDto.setAnalyseFluxCmd(UPDATE_TYPE_ANALYSEFLUX);
		typeDto.setExtractPageCmd(UPDATE_TYPE_EXTRACTPAGECMD);
		typeDto.setTimeoutAnalyseFlux(UPDATE_TYPE_TIMEOUT);
		typeDto.setDefaut(UPDATE_TYPE_ISDEFAULT);
		typeDto = typeClient.update(typeDto);
		typeDto = typeClient.byName(NEW_TYPE_NAME);
		assertEquals("Mauvais ID", createdIdType, typeDto.getId());
		assertEquals("Mauvais nom", NEW_TYPE_NAME, typeDto.getName());
		assertEquals("Mauvais analyse du flux ", UPDATE_TYPE_ANALYSEFLUX, typeDto.getAnalyseFluxCmd());
		assertEquals("Mauvaise extraction de pages ", UPDATE_TYPE_EXTRACTPAGECMD, typeDto.getExtractPageCmd());
		assertEquals("Mauvais timeout", UPDATE_TYPE_TIMEOUT, typeDto.getTimeoutAnalyseFlux().intValue());
		assertEquals("Mauvais default type", UPDATE_TYPE_ISDEFAULT, typeDto.getDefaut());

		// Récupération par extension et ajout d'une nouvelle extension
		typeDto = typeClient.ByExtension(NEW_TYPE_DEFAULTEXTENSION);
		typeClient.addExtension(NEW_TYPE_NAME, ADD_TYPE_EXTENSION);
		assertEquals("Mauvais ID", createdIdType, typeDto.getId());
		assertEquals("Mauvais nom", NEW_TYPE_NAME, typeDto.getName());
		typeDto = typeClient.byId(createdIdType);
		List<String> listExtensions = typeDto.getExtensions();
		assertTrue("Erreur d'ajout de l'extension", listExtensions.contains(ADD_TYPE_EXTENSION));

		// Récupération par type mime et ajout d'un nouveau type mime
		typeDto = typeClient.ByTypeMime(NEW_TYPE_DEFAULTMIME);
		typeClient.addTypeMime(NEW_TYPE_NAME, ADD_TYPE_MIME);
		assertEquals("Mauvais ID", createdIdType, typeDto.getId());
		assertEquals("Mauvais nom", NEW_TYPE_NAME, typeDto.getName());
		typeDto = typeClient.byId(createdIdType);
		List<String> listTypeMimes = typeDto.getTypeMimes();
		assertTrue("Erreur d'ajout du type mime", listTypeMimes.contains(ADD_TYPE_MIME));

		// Suppression de l'extension
		typeClient.removeExtension(NEW_TYPE_NAME, ADD_TYPE_EXTENSION);
		typeDto = typeClient.byId(createdIdType);
		listExtensions = typeDto.getExtensions();
		assertFalse("Erreur de suppression de l'extension", listExtensions.contains(ADD_TYPE_EXTENSION));

		// Suppression du type mime
		typeClient.removeTypeMime(NEW_TYPE_NAME, ADD_TYPE_MIME);
		typeDto = typeClient.byId(createdIdType);
		listTypeMimes = typeDto.getExtensions();
		assertFalse("Erreur de suppression du type mime", listTypeMimes.contains(ADD_TYPE_MIME));

		// delete type
		typeClient.delete(typeDto);
		try {
			typeClient.byId(createdIdType);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_ById() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_ByName() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.byName(NEW_TYPE_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_ByName() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.byName("");
	}

	@Test
	public void testType_For_GetDefault() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		assertNotNull(typeClient.getDefault());
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_ByExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.ByExtension(NEW_TYPE_DEFAULTEXTENSION);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_ByExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.ByExtension("");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_ByTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.ByTypeMime(NEW_TYPE_DEFAULTMIME);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_ByTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.ByTypeMime("");
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_Delete() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_Delete() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.delete(NEW_TYPE_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_Create() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		TypeDto typeDto = new TypeDto();
		typeDto.setName("");
		typeClient.create(typeDto);
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_Create() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		TypeDto typeDto = new TypeDto();
		// création qu'avec le nom du type
		typeDto.setName(NEW_TYPE_NAME);
		typeClient.create(typeDto);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_Update() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		TypeDto typeDto = new TypeDto();
		typeDto.setName("");
		typeClient.update(typeDto);
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_Update() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		TypeDto typeDto = new TypeDto();
		typeDto.setName(NEW_TYPE_NAME);
		typeClient.update(typeDto);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_RemoveTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.removeTypeMime("", "");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_RemoveTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.removeTypeMime(NEW_TYPE_NAME, NEW_TYPE_DEFAULTMIME);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_RemoveExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.removeExtension("", "");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_RemoveExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.removeExtension(NEW_TYPE_NAME, NEW_TYPE_DEFAULTEXTENSION);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_AddExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.addExtension("", "");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_AddExtension() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.addExtension(NEW_TYPE_NAME, NEW_TYPE_DEFAULTEXTENSION);
	}

	@Test(expected = ValidationException.class)
	public void testType_Empty_For_AddTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.addTypeMime("", "");
	}

	@Test(expected = RestException.class)
	public void testType_NotFound_For_AddTypeMime() throws ValidationException, RestException {
		TypeClient typeClient = serveur.types();
		typeClient.addTypeMime(NEW_TYPE_NAME, NEW_TYPE_DEFAULTMIME);
	}

}