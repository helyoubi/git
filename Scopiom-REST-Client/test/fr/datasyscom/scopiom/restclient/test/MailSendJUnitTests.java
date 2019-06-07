package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.MailSendConfigDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.mailSend.MailConfigClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class MailSendJUnitTests {

	// new mail config
	private static final String NEW_MAIL_HOST = "localhost";
	private static final Integer NEW_MAIL_PORT = 1255;
	private static final boolean NEW_MAIL_USEAUTHENTIFICATION = true;
	private static final String NEW_MAIL_USER = "user";
	private static final String NEW_MAIL_PASSWORD = "passUser";
	private static final String NEW_MAIL_TRANSMITTERADRESS = "adress@test.fr";
	private static final String NEW_MAIL_TRANSMITTERNAME = "nameTransmitter";
	private static final boolean NEW_MAIL_USETLS = true;
	private static final boolean NEW_MAIL_USESSL = true;
	// for update
	private static final String UPDATE_MAIL_HOST = "localhost";
	private static final Integer UPDATE_MAIL_PORT = 1022;
	private static final boolean UPDATE_MAIL_USEAUTHENTIFICATION = false;
	private static final String UPDATE_MAIL_USER = "userUpdate";
	private static final String UPDATE_MAIL_PASSWORD = "passUserUpdate";
	private static final String UPDATE_MAIL_TRANSMITTERADRESS = "adressUpdate@test.fr";
	private static final String UPDATE_MAIL_TRANSMITTERNAME = "nameTransmitterUpdate";
	private static final boolean UPDATE_MAIL_USETLS = false;
	private static final boolean UPDATE_MAIL_USESSL = false;
	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testMailSend() throws ValidationException, RestException {
		MailConfigClient mailClient = serveur.mailConfig();
		// new mailconfig
		MailSendConfigDto mailSendConfigDto = new MailSendConfigDto();
		mailSendConfigDto.setHost(NEW_MAIL_HOST);
		mailSendConfigDto.setPort(NEW_MAIL_PORT);
		mailSendConfigDto.setUseAuthentification(NEW_MAIL_USEAUTHENTIFICATION);
		mailSendConfigDto.setUser(NEW_MAIL_USER);
		mailSendConfigDto.setPassword(NEW_MAIL_PASSWORD);
		mailSendConfigDto.setTransmitterAdress(NEW_MAIL_TRANSMITTERADRESS);
		mailSendConfigDto.setTransmitterName(NEW_MAIL_TRANSMITTERNAME);
		mailSendConfigDto.setUseTLS(NEW_MAIL_USETLS);
		mailSendConfigDto.setUseSSL(NEW_MAIL_USESSL);
		mailSendConfigDto = mailClient.create(mailSendConfigDto);
		// checkon on create
		assertEquals("Mauvais hôte", NEW_MAIL_HOST, mailSendConfigDto.getHost());
		assertEquals("Mauvais port", NEW_MAIL_PORT, mailSendConfigDto.getPort());
		assertEquals("Mauvaise valeur d'authentification", NEW_MAIL_USEAUTHENTIFICATION,
				mailSendConfigDto.getUseAuthentification());
		assertEquals("Mauvais utilisateur", NEW_MAIL_USER, mailSendConfigDto.getUser());
		assertEquals("Mauvais mot de passe", NEW_MAIL_PASSWORD, mailSendConfigDto.getPassword());
		assertEquals("Mauvaise adresse émetteur", NEW_MAIL_TRANSMITTERADRESS, mailSendConfigDto.getTransmitterAdress());
		assertEquals("Mauvais nom d'émetteur", NEW_MAIL_TRANSMITTERNAME, mailSendConfigDto.getTransmitterName());
		assertEquals("Mauvaise valeur TLS", NEW_MAIL_USETLS, mailSendConfigDto.getUseTLS());
		assertEquals("Mauvaise valeur SSL", NEW_MAIL_USESSL, mailSendConfigDto.getUseSSL());
		// update
		mailSendConfigDto = mailClient.retrieve();
		mailSendConfigDto.setHost(UPDATE_MAIL_HOST);
		mailSendConfigDto.setPort(UPDATE_MAIL_PORT);
		mailSendConfigDto.setUseAuthentification(UPDATE_MAIL_USEAUTHENTIFICATION);
		mailSendConfigDto.setUser(UPDATE_MAIL_USER);
		mailSendConfigDto.setPassword(UPDATE_MAIL_PASSWORD);
		mailSendConfigDto.setTransmitterAdress(UPDATE_MAIL_TRANSMITTERADRESS);
		mailSendConfigDto.setTransmitterName(UPDATE_MAIL_TRANSMITTERNAME);
		mailSendConfigDto.setUseTLS(UPDATE_MAIL_USETLS);
		mailSendConfigDto.setUseSSL(UPDATE_MAIL_USESSL);
		mailSendConfigDto = mailClient.update(mailSendConfigDto);
		// check on update
		assertEquals("Mauvais hôte", UPDATE_MAIL_HOST, mailSendConfigDto.getHost());
		assertEquals("Mauvais port", UPDATE_MAIL_PORT, mailSendConfigDto.getPort());
		assertEquals("Mauvaise valeur d'authentification", UPDATE_MAIL_USEAUTHENTIFICATION,
				mailSendConfigDto.getUseAuthentification());
		assertEquals("Mauvais utilisateur", UPDATE_MAIL_USER, mailSendConfigDto.getUser());
		assertEquals("Mauvais mot de passe", UPDATE_MAIL_PASSWORD, mailSendConfigDto.getPassword());
		assertEquals("Mauvaise adresse émetteur", UPDATE_MAIL_TRANSMITTERADRESS,
				mailSendConfigDto.getTransmitterAdress());
		assertEquals("Mauvais nom d'émetteur", UPDATE_MAIL_TRANSMITTERNAME, mailSendConfigDto.getTransmitterName());
		assertEquals("Mauvaise valeur TLS", UPDATE_MAIL_USETLS, mailSendConfigDto.getUseTLS());
		assertEquals("Mauvaise valeur SSL", UPDATE_MAIL_USESSL, mailSendConfigDto.getUseSSL());
	}
	
	@Test(expected = ValidationException.class)
	public void testMailSend_NotFound_For_Create() throws RestException, ValidationException {
		MailConfigClient mailClient = serveur.mailConfig();
		mailClient.create(null);
	}
	
	@Test(expected = ValidationException.class)
	public void testMailSend_NotFound_For_Update() throws RestException, ValidationException {
		MailConfigClient mailClient = serveur.mailConfig();
		mailClient.update(null);
	}

}
