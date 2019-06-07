package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.IdsDto;
import fr.datasyscom.scopiom.rest.pojo.UserDto;
import fr.datasyscom.scopiom.rest.pojo.UserPropertyDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;
import fr.datasyscom.scopiom.restclient.user.UserClient;
import fr.datasyscom.scopiom.restclient.user.UserClient.UserProfil;

public class UserJUnitTests {

	// new User
	private static UserDto userDto;
	private static final String NEW_USER_LOGIN = "login" + System.currentTimeMillis();
	private static final String NEW_USER_NOM = "nomUser";
	private static final String NEW_USER_PRENOM = "prenomUser";
	private static final String NEW_USER_MAIL = "mail@test.fr";
	private static final String NEW_USER_COMMENT = "commentUser";
	private static final String NEW_USER_PASS = "passUser1";
	private static final String NEW_USER_PROFIL = "IOM_USER";
	private static final boolean NEW_USER_CANCHGINFOS = true;
	// test not found
	private static final String NEW_USERNOTFOUND_LOGIN = "loginTest" + System.currentTimeMillis();
	private static final String NEW_GROUPNOTFOUND_NAME = "groupe" + System.currentTimeMillis();
	// new device
	private static DeviceDto deviceDto;
	private static final String NEW_DEVICE_NAME = "NEWDEVICE-" + System.currentTimeMillis();
	// new groupe
	private static GroupeDto groupeDto;
	private static final String NEW_GROUP_NAME = "NEWGROUP-" + System.currentTimeMillis();
	// property
	private static final String NEW_USERPROPERTY_NAME = "SCOPIOM_PROP" + System.currentTimeMillis();
	private static final String NEW_USERPROPERTY_DESC = "descProperty";
	private static final String NEW_USERPROPERTY_VALUE = "valueProperty";
	private static final boolean NEW_USERPROPERTY_SCRIPTEXPORT = true;
	// update property
	private static final String UPDATE_USERPROPERTY_DESC = "descUpdate";
	private static final String UPDATE_USERPROPERTY_VALUE = "valueProperty";
	private static final boolean UPDATE_USERPROPERTY_SCRIPTEXPORT = true;
	private static final boolean UPDATE_USERPROPERTY_MANDATORY = true;

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() throws ValidationException, RestException {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
		// création d'un utilisateur
		int initialUserCountList = serveur.users().all().size();
		int initialUserProfilCountList = serveur.users().byProfil(UserProfil.IOM_USER).size();
		userDto = new UserDto();
		userDto.setLogin(NEW_USER_LOGIN);
		userDto.setPassword(NEW_USER_PASS);
		userDto = serveur.users().create(userDto);
		// check list size user created
		assertEquals("Erreur au niveau de la liste des utilisateurs", initialUserCountList + 1,
				serveur.users().all().size());
		// check list size for profil
		assertEquals("Erreur au niveau de la liste des utilisateurs par profil", initialUserProfilCountList + 1,
				serveur.users().byProfil(UserProfil.IOM_USER).size());
		assertEquals("Mauvais Login", NEW_USER_LOGIN, userDto.getLogin());
		assertEquals("Mauvais profil", NEW_USER_PROFIL, userDto.getProfil());
		// création d'un device
		deviceDto = serveur.devices().create(NEW_DEVICE_NAME, "model_new");
		// création d'un groupe
		groupeDto = new GroupeDto();
		groupeDto.setName(NEW_GROUP_NAME);
		groupeDto = serveur.groups().create(groupeDto);
	}

	@Test
	public void testUser() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userDto.setNom(NEW_USER_NOM);
		userDto.setPrenom(NEW_USER_PRENOM);
		userDto.setMail(NEW_USER_MAIL);
		userDto.setCommentaire(NEW_USER_COMMENT);
		userDto.setPassword(NEW_USER_PASS);
		userDto.setProfil(NEW_USER_PROFIL);
		userDto.setDefaultDeviceName(NEW_DEVICE_NAME);
		userDto.setCanChangeInfos(NEW_USER_CANCHGINFOS);
		userDto = userClient.update(userDto);
		userDto = userClient.byLogin(NEW_USER_LOGIN);
		assertEquals("Mauvais Login", NEW_USER_LOGIN, userDto.getLogin());
		assertEquals("Mauvais nom", NEW_USER_NOM, userDto.getNom());
		assertEquals("Mauvais prenom", NEW_USER_PRENOM, userDto.getPrenom());
		assertEquals("Mauvais mail", NEW_USER_MAIL, userDto.getMail());
		assertEquals("Mauvais commentaire", NEW_USER_COMMENT, userDto.getCommentaire());
		assertEquals("Mauvais profil", NEW_USER_PROFIL, userDto.getProfil());
		assertEquals("Mauvais device", NEW_DEVICE_NAME, userDto.getDefaultDeviceName());
		assertEquals("Mauvaise valeur de modification d'infos personnelles", NEW_USER_CANCHGINFOS,
				userDto.getCanChangeInfos());
		// add groupe
		userClient.addGroup(NEW_USER_LOGIN, NEW_GROUP_NAME);
		List<String> listGrp = RetrieveGroupsListString(userClient);
		assertEquals("erreur ajout du groupe", true, listGrp.contains(NEW_GROUP_NAME));
		// remove groupe
		userClient.removeGroup(NEW_USER_LOGIN, NEW_GROUP_NAME);
		listGrp = RetrieveGroupsListString(userClient);
		assertEquals("erreur suppression du groupe", false, listGrp.contains(NEW_GROUP_NAME));
	}

	@Test
	public void test_PropertyUser() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		// create property
		int initialUserPropertyCountList = userClient.allProperties(NEW_USER_LOGIN).size();
		UserPropertyDto userPropertyDto = new UserPropertyDto();
		userPropertyDto.setName(NEW_USERPROPERTY_NAME);
		userPropertyDto.setDescription(NEW_USERPROPERTY_DESC);
		userPropertyDto.setTxt(NEW_USERPROPERTY_VALUE);
		userPropertyDto.setScriptExport(NEW_USERPROPERTY_SCRIPTEXPORT);
		userPropertyDto = userClient.createProperty(NEW_USER_LOGIN, userPropertyDto);
		// check list size
		assertEquals("Erreur au niveau de la liste de propriétés des utilisateurs", initialUserPropertyCountList + 1,
				userClient.allProperties(NEW_USER_LOGIN).size());
		assertEquals("Mauvais nom de propriété", NEW_USERPROPERTY_NAME, userPropertyDto.getName());
		assertEquals("Mauvaise description de propriété", NEW_USERPROPERTY_DESC, userPropertyDto.getDescription());
		assertEquals("Mauvaise valeur de propriété", NEW_USERPROPERTY_VALUE, userPropertyDto.getTxt());
		assertEquals("Mauvaise valeur export dans les processs de propriété", NEW_USERPROPERTY_SCRIPTEXPORT,
				userPropertyDto.isScriptExport());
		// récupération
		userPropertyDto = userClient.retrieveProperty(NEW_USER_LOGIN, NEW_USERPROPERTY_NAME);
		userPropertyDto.setDescription(UPDATE_USERPROPERTY_DESC);
		userPropertyDto.setTxt(UPDATE_USERPROPERTY_VALUE);
		userPropertyDto.setScriptExport(UPDATE_USERPROPERTY_SCRIPTEXPORT);
		userPropertyDto.setMandatory(UPDATE_USERPROPERTY_MANDATORY);
		// update
		userPropertyDto = userClient.updateProperty(NEW_USER_LOGIN, userPropertyDto);
		assertEquals("Mauvais nom de propriété en update", NEW_USERPROPERTY_NAME, userPropertyDto.getName());
		assertEquals("Mauvaise description de propriété en update", UPDATE_USERPROPERTY_DESC,
				userPropertyDto.getDescription());
		assertEquals("Mauvaise valeur de propriété en update", UPDATE_USERPROPERTY_VALUE, userPropertyDto.getTxt());
		assertEquals("Mauvaise valeur export dans les processs de propriété en update",
				UPDATE_USERPROPERTY_SCRIPTEXPORT, userPropertyDto.isScriptExport());
		assertEquals("Mauvaise valeur mandatory en update", UPDATE_USERPROPERTY_MANDATORY,
				userPropertyDto.isMandatory());
		// delete property
		userClient.deleteProperty(NEW_USER_LOGIN, NEW_USERPROPERTY_NAME);
		try {
			userClient.retrieveProperty(NEW_USER_LOGIN, NEW_USERPROPERTY_NAME);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}

	}

	private List<String> RetrieveGroupsListString(UserClient userClient) throws RestException, ValidationException {
		List<GroupeDto> listGroupeUsers = userClient.retrieveGroup(NEW_USER_LOGIN);
		List<String> groupsList = new ArrayList<String>();
		for (GroupeDto groupeDto : listGroupeUsers) {
			groupsList.add(groupeDto.getName());
		}
		return groupsList;
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_ByLogin() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.byLogin(NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_ByLogin() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.byLogin("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUser_NotFound_ByProfil() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.byProfil(UserProfil.valueOf("profilNotExist"));
	}
	
	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_CreateProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.createProperty("", null);
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_CreateProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserPropertyDto userProperty = new UserPropertyDto();
		userProperty.setName("");
		userProperty.setDescription("");
		userClient.createProperty(NEW_USERNOTFOUND_LOGIN, userProperty);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_RetrieveAllProperties() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.allProperties("");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_RetrieveAllProperties() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.allProperties(NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_RetrieveGroups() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.retrieveGroup("");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_RetrieveGroups() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.retrieveGroup(NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_RetrieveProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.retrieveProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_RetrieveProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.retrieveProperty(NEW_USERNOTFOUND_LOGIN, NEW_USERPROPERTY_NAME);
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_Delete() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.delete(NEW_USERNOTFOUND_LOGIN);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_Delete() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_DeleteProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.deleteProperty(NEW_USERNOTFOUND_LOGIN, NEW_USERPROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_DeleteProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.deleteProperty("", "");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_RemoveGroup() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.removeGroup(NEW_USERNOTFOUND_LOGIN, NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_RemoveGroup() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.removeGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_AddGroup() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.addGroup(NEW_USERNOTFOUND_LOGIN, NEW_GROUPNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_AddGroup() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.addGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_Create() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserDto userDto = new UserDto();
		userDto.setLogin("");
		userDto.setPassword("");
		userClient.create(userDto);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_Create() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.create(null);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_UpdateProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserPropertyDto userPropertyDto = new UserPropertyDto();
		userPropertyDto.setName("");
		userClient.updateProperty("", userPropertyDto);
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_UpdateProperty() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserPropertyDto userPropertyDto = new UserPropertyDto();
		userPropertyDto.setName(NEW_USERPROPERTY_NAME);
		userClient.updateProperty(NEW_USERNOTFOUND_LOGIN, userPropertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_UpdateGroups() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		userClient.updateGroups("", null);
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_UpdateGroups() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		IdsDto idsDto = new IdsDto();
		idsDto.setIds(null);
		userClient.updateGroups(NEW_USERNOTFOUND_LOGIN, idsDto);
	}

	@Test(expected = ValidationException.class)
	public void testUser_Empty_For_Update() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserDto userDto = new UserDto();
		userDto.setLogin("");
		userClient.update(userDto);
	}

	@Test(expected = RestException.class)
	public void testUser_NotFound_For_Update() throws ValidationException, RestException {
		UserClient userClient = serveur.users();
		UserDto userDto = new UserDto();
		userDto.setLogin(NEW_USERNOTFOUND_LOGIN);
		userClient.update(userDto);
	}

	@AfterClass
	public static void clear() throws ValidationException, RestException {
		serveur.users().delete(userDto);
		serveur.devices().delete(deviceDto);
		serveur.groups().delete(groupeDto);
	}
}
