package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.MediaDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.media.MediaClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class MediaJUnitTests {

	private static final String NEW_MEDIA_NAME = "NEWNAME-" + System.currentTimeMillis();
	private static final String NEW_MEDIA_DESC = "mediaDescTest";
	private static final String UPDATE_MEDIA_DESC = "mediaDescTestUpdate";

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testMedia() throws ValidationException, RestException {
		MediaClient mediaClient = serveur.medias();
		long createdIdMedia;
		int initialCountSize = mediaClient.all().size();

		// Ajout du média
		MediaDto media = new MediaDto();
		media.setName(NEW_MEDIA_NAME);
		media.setDescription(NEW_MEDIA_DESC);
		media = mediaClient.create(media);
		createdIdMedia = media.getId();

		assertEquals("Erreur list medias", initialCountSize + 1, mediaClient.all().size());

		// Récupération par id
		media = mediaClient.byId(createdIdMedia);
		assertEquals("Mauvais ID", createdIdMedia, media.getId());
		assertEquals("Mauvais nom", NEW_MEDIA_NAME, media.getName());
		assertEquals("Mauvaise description", NEW_MEDIA_DESC, media.getDescription());

		// Mise à jour
		media.setDescription(UPDATE_MEDIA_DESC);
		media = mediaClient.update(media);
		media = mediaClient.byName(NEW_MEDIA_NAME);
		assertEquals("Mauvais ID", createdIdMedia, media.getId());
		assertEquals("Mauvais nom", NEW_MEDIA_NAME, media.getName());
		assertEquals("Erreur update description", UPDATE_MEDIA_DESC, media.getDescription());

		// Suppression
		mediaClient.delete(NEW_MEDIA_NAME);
		try {
			mediaClient.byId(createdIdMedia);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testMedia_NotFound_ByName() throws ValidationException, RestException {
		MediaClient mediaClient = serveur.medias();
		mediaClient.byName(NEW_MEDIA_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testMedia_Empty_ByName() throws ValidationException, RestException {
		MediaClient mediaClient = serveur.medias();
		mediaClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testMedia_NotFound_ById() throws RestException {
		MediaClient mediaClient = serveur.medias();
		mediaClient.byId(System.currentTimeMillis());
	}

	@Test(expected = ValidationException.class)
	public void testMediaName_Empty_For_Create() throws RestException, ValidationException {
		MediaClient mediaClient = serveur.medias();
		MediaDto media = new MediaDto();
		media.setName("");
		mediaClient.create(media);
	}

	@Test(expected = RestException.class)
	public void testMediaName_NotFound_For_Update() throws RestException, ValidationException {
		MediaClient mediaClient = serveur.medias();
		MediaDto media = new MediaDto();
		media.setName(NEW_MEDIA_NAME);
		mediaClient.update(media);
	}

	@Test(expected = ValidationException.class)
	public void testMediaName_Empty_For_Update() throws RestException, ValidationException {
		MediaClient mediaClient = serveur.medias();
		MediaDto media = new MediaDto();
		media.setName("");
		mediaClient.update(media);
	}

	@Test(expected = ValidationException.class)
	public void testMediaName_Empty_For_Delete() throws RestException, ValidationException {
		MediaClient mediaClient = serveur.medias();
		MediaDto media = new MediaDto();
		media.setName("");
		mediaClient.delete(media);
	}

	@Test(expected = RestException.class)
	public void testMediaName_NotFound_For_Delete() throws RestException, ValidationException {
		MediaClient mediaClient = serveur.medias();
		MediaDto media = new MediaDto();
		media.setName(NEW_MEDIA_NAME);
		mediaClient.delete(media);
	}

}
