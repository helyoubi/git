package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fr.datasyscom.pome.exception.ValidationException;
import org.junit.BeforeClass;
import org.junit.Test;
import fr.datasyscom.scopiom.rest.pojo.PasswordManagementDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.passwordmanagement.PasswordManagementClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class PasswordManagementJUnitTests {

	private final Boolean NEW_PASSWORDCONF_CANRESETPASS = true;
	private final Boolean NEW_PASSWORDCONF_NEEDVALIDATEPASS = false;
	private final int NEW_PASSWORDCONF_MINLENGH = 5;
	private final int NEW_PASSWORDCONF_MINLLOWERCASE = 2;
	private final int NEW_PASSWORDCONF_MINUPPERCASE = 1;
	private final int NEW_PASSWORDCONF_MINDIGITS = 2;
	private final int NEW_PASSWORDCONF_MINSPECIALCHARS = 0;

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testPasswordManagement_For_Retrive() throws RestException {
		PasswordManagementClient passwordManagementClient = serveur.passwordManagement();
		assertNotNull("Erreur de récupération de la configuration du mot de passe",
				passwordManagementClient.retrieve());
	}

	@Test
	public void testPasswordManagement_For_Update() throws RestException, ValidationException {
		PasswordManagementClient passwordManagementClient = serveur.passwordManagement();
		PasswordManagementDto passwordManagementDto = new PasswordManagementDto();
		passwordManagementDto.setCanResetPassword(NEW_PASSWORDCONF_CANRESETPASS);
		passwordManagementDto.setNeedValidatePassword(NEW_PASSWORDCONF_NEEDVALIDATEPASS);
		passwordManagementDto.setMinLengh(NEW_PASSWORDCONF_MINLENGH);
		passwordManagementDto.setMinLowercase(NEW_PASSWORDCONF_MINLLOWERCASE);
		passwordManagementDto.setMinUppercase(NEW_PASSWORDCONF_MINUPPERCASE);
		passwordManagementDto.setMinDigits(NEW_PASSWORDCONF_MINDIGITS);
		passwordManagementDto.setMinSpecialCharacters(NEW_PASSWORDCONF_MINSPECIALCHARS);
		passwordManagementDto = passwordManagementClient.update(passwordManagementDto);

		assertEquals("Can reset password error", NEW_PASSWORDCONF_CANRESETPASS,
				passwordManagementDto.getCanResetPassword());
		assertEquals("Need Validate password error", NEW_PASSWORDCONF_NEEDVALIDATEPASS,
				passwordManagementDto.getNeedValidatePassword());
		assertEquals("Min lengh password error", NEW_PASSWORDCONF_MINLENGH,
				passwordManagementDto.getMinLengh().intValue());
		assertEquals("Min lower case password error", NEW_PASSWORDCONF_MINLLOWERCASE,
				passwordManagementDto.getMinLowercase().intValue());
		assertEquals("Min upper case password error", NEW_PASSWORDCONF_MINUPPERCASE,
				passwordManagementDto.getMinUppercase().intValue());
		assertEquals("Min digits case password error", NEW_PASSWORDCONF_MINDIGITS,
				passwordManagementDto.getMinDigits().intValue());
		assertEquals("Min special characters password error", NEW_PASSWORDCONF_MINSPECIALCHARS,
				passwordManagementDto.getMinSpecialCharacters().intValue());
	}

	@Test(expected = RestException.class)
	public void testPasswordManagement_NotFound_For_Update() throws RestException, ValidationException {
		PasswordManagementClient passwordManagementClient = serveur.passwordManagement();
		passwordManagementClient.update(null);
	}

}
