package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.info.InfoClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class InfoJUnitTests {

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void test_infoApp() throws RestException {
		InfoClient infoAppClient = serveur.app();
		assertNotNull("erreur récupération des infos app", infoAppClient.infosApp());
	}

	@Test
	public void test_infoHealth() throws RestException {
		InfoClient infoHealthClient = serveur.heatlh();
		assertNotNull("erreur récupération des infos health", infoHealthClient.infosHealth());
	}

}
