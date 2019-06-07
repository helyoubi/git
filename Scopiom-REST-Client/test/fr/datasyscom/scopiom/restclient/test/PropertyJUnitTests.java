package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.PropertyDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.property.PropertyClient;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class PropertyJUnitTests {

	private final String NEW_PROPERTY_NAME = "SCOPIOM_NAMEPROP-" + System.currentTimeMillis();
	private final String NEW_PROPERTY_DESC = "descProp";
	private final String NEW_PROPERTY_VALUE = "valueProp";
	private final boolean NEW_PROPERTY_ISSCRIPTEXPORT = true;
	private final boolean NEW_PROPERTY_OVERRIDABLE = true;

	private final String UPDATE_PROPERTY_DESC = "descPropUpdate";
	private final String UPDATE_PROPERTY_VALUE = "valuePropUpdate";
	private final boolean UPDATE_PROPERTY_ISSCRIPTEXPORT = false;
	private final boolean UPDATE_PROPERTY_OVERRIDABLE = false;

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testProperty() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		long createdIdProperty;
		int initialCountSize = propertyClient.all().size();
		PropertyDto propertyDto = new PropertyDto();
		propertyDto.setName(NEW_PROPERTY_NAME);
		propertyDto.setDescription(NEW_PROPERTY_DESC);
		propertyDto.setTxt(NEW_PROPERTY_VALUE);
		propertyDto.setScriptExport(NEW_PROPERTY_ISSCRIPTEXPORT);
		propertyDto.setOverridable(NEW_PROPERTY_OVERRIDABLE);
		propertyDto = propertyClient.create(propertyDto);
		createdIdProperty = propertyDto.getId();

		assertEquals("Erreur list de propriétés", initialCountSize + 1, propertyClient.all().size());
		// Récupération par id
		propertyDto = propertyClient.byId(createdIdProperty);
		assertEquals("Mauvais ID", createdIdProperty, propertyDto.getId());
		assertEquals("Mauvais nom", NEW_PROPERTY_NAME, propertyDto.getName());
		assertEquals("Mauvaise description", NEW_PROPERTY_DESC, propertyDto.getDescription());
		assertEquals("Mauvaise valeur", NEW_PROPERTY_VALUE, propertyDto.getTxt());
		assertEquals("Mauvaise valeur pour l'attribut d'export dans le process", NEW_PROPERTY_ISSCRIPTEXPORT,
				propertyDto.isScriptExport());
		assertEquals("Mauvaise valeur pour l'attribut modifiable", NEW_PROPERTY_OVERRIDABLE,
				propertyDto.isOverridable());

		propertyDto.setDescription(UPDATE_PROPERTY_DESC);
		propertyDto.setTxt(UPDATE_PROPERTY_VALUE);
		propertyDto.setScriptExport(UPDATE_PROPERTY_ISSCRIPTEXPORT);
		propertyDto.setOverridable(UPDATE_PROPERTY_OVERRIDABLE);
		propertyDto = propertyClient.update(propertyDto);
		propertyDto = propertyClient.byName(NEW_PROPERTY_NAME);
		assertEquals("Mauvais ID", createdIdProperty, propertyDto.getId());
		assertEquals("Mauvaise description", UPDATE_PROPERTY_DESC, propertyDto.getDescription());
		assertEquals("Mauvaise valeur", UPDATE_PROPERTY_VALUE, propertyDto.getTxt());
		assertEquals("Mauvaise valeur pour l'attribut d'export dans le process", UPDATE_PROPERTY_ISSCRIPTEXPORT,
				propertyDto.isScriptExport());
		assertEquals("Mauvaise valeur pour l'attribut modifiable", UPDATE_PROPERTY_OVERRIDABLE,
				propertyDto.isOverridable());

		propertyClient.delete(propertyDto);
		try {
			propertyClient.byId(createdIdProperty);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testProperty_NotFound_ById() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.byId(System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testProperty_NotFound_ByName() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.byName(NEW_PROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testProperty_Empty_ByName() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testPropertyName_NotFound_For_Delete() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.delete(NEW_PROPERTY_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testPropertyName_Empty_For_Delete() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.delete("");
	}

	@Test(expected = RestException.class)
	public void testProperty_NotFound_For_Create() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		propertyClient.create(null);
	}

	@Test(expected = RestException.class)
	public void testPropertyName_NotFound_For_Update() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		PropertyDto propertyDto = new PropertyDto();
		propertyDto.setName(NEW_PROPERTY_NAME);
		propertyClient.update(propertyDto);
	}

	@Test(expected = ValidationException.class)
	public void testPropertyName_Empty_For_Update() throws ValidationException, RestException {
		PropertyClient propertyClient = serveur.properties();
		PropertyDto propertyDto = new PropertyDto();
		propertyDto.setName("");
		propertyClient.update(propertyDto);
	}

}
