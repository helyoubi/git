package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import fr.datasyscom.pome.exception.ValidationException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.CalendarDto;
import fr.datasyscom.scopiom.restclient.calendar.CalendarClient;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;

public class CalendarJUnitTests {
	private final String NEW_CALENDAR_NAME = "NEWCALENDAR-" + System.currentTimeMillis();
	private final String NEW_CALENDAR_DESC = "descCalendar";
	private final String NEW_CALENDAR_CRON = "0 * * * *";

	private final String UPDATE_CALENDAR_DESC = "descCalendar";
	private final String UPDATE_CALENDAR_CRON = "0 12 * * 1";
	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test
	public void testCalendar() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		long createdIdCalendar;
		int initialCountSize = calendarClient.all().size();

		// Create
		CalendarDto calendarDto = new CalendarDto();
		calendarDto.setName(NEW_CALENDAR_NAME);
		calendarDto.setDescription(NEW_CALENDAR_DESC);
		calendarDto.setCron(NEW_CALENDAR_CRON);
		calendarDto = calendarClient.create(calendarDto);
		createdIdCalendar = calendarDto.getId();

		assertEquals("Erreur list Calendrier", initialCountSize + 1, calendarClient.all().size());
		// récupération par id
		calendarDto = calendarClient.byId(createdIdCalendar);
		assertEquals("Mauvais ID", createdIdCalendar, calendarDto.getId());
		assertEquals("Mauvaise description", NEW_CALENDAR_DESC, calendarDto.getDescription());
		assertEquals("Mauvais Cron", NEW_CALENDAR_CRON, calendarDto.getCron());

		// Update
		calendarDto.setDescription(UPDATE_CALENDAR_DESC);
		calendarDto.setCron(UPDATE_CALENDAR_CRON);
		calendarDto = calendarClient.update(calendarDto);
		calendarDto = calendarClient.byName(NEW_CALENDAR_NAME);
		assertEquals("Mauvais ID", createdIdCalendar, calendarDto.getId());
		assertEquals("Mauvaise description", UPDATE_CALENDAR_DESC, calendarDto.getDescription());
		assertEquals("Mauvais Cron", UPDATE_CALENDAR_CRON, calendarDto.getCron());

		// delete
		calendarClient.delete(calendarDto);
		try {
			calendarClient.byId(createdIdCalendar);
			fail("Suppression KO");
		} catch (RestException e) {
			assertEquals("Wrong HTTP status", 404, e.getHttpCode().intValue());
		}
	}

	@Test(expected = RestException.class)
	public void testCalendar_NotFound_ByName() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		calendarClient.byName(NEW_CALENDAR_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testCalendar_Empty_ByName() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		calendarClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testCalendar_NotFound_ById() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		calendarClient.byId(System.currentTimeMillis());
	}

	@Test(expected = ValidationException.class)
	public void testCalendarName_Empty_For_Delete() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		calendarClient.delete("");
	}
	
	@Test(expected = RestException.class)
	public void testCalendarName_NotFound_For_Delete() throws ValidationException, RestException {
		CalendarClient calendarClient = serveur.calendars();
		calendarClient.delete(NEW_CALENDAR_NAME);
	}

	@Test(expected = RestException.class)
	public void testCalendar_NotFound_For_Create() throws RestException, ValidationException {
		CalendarClient calendarClient = serveur.calendars();
		CalendarDto calendarDto = new CalendarDto();
		calendarDto.setName(NEW_CALENDAR_NAME);
		//Création du calendrier sans libellé
		calendarClient.create(calendarDto);
	}

	@Test(expected = ValidationException.class)
	public void testCalendarName_Empty_For_Create() throws RestException, ValidationException {
		CalendarClient calendarClient = serveur.calendars();
		CalendarDto calendarDto = new CalendarDto();
		calendarDto.setName("");
		calendarClient.create(calendarDto);
	}

	@Test(expected = RestException.class)
	public void testCalendarName_NotFound_For_Update() throws RestException, ValidationException {
		CalendarClient calendarClient = serveur.calendars();
		CalendarDto calendarDto = new CalendarDto();
		calendarDto.setName(NEW_CALENDAR_NAME);
		calendarClient.update(calendarDto);
	}

	@Test(expected = ValidationException.class)
	public void testCalendarName_Empty_For_Update() throws RestException, ValidationException {
		CalendarClient calendarClient = serveur.calendars();
		CalendarDto calendarDto = new CalendarDto();
		calendarDto.setName("");
		calendarClient.update(calendarDto);
	}


}
