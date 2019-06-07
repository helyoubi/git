package fr.datasyscom.scopiom.restclient.calendar;

import java.util.List;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.CalendarDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

public class CalendarClient {

	WebResource baseWebRessource;

	public CalendarClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération du calendrier par son nom
	 * 
	 * @param nameCalender : nom du calendrier
	 * @return CalendarDto
	 * @throws RestException       : nom du calendrier n'existe pas ou erreur au
	 *                             niveau du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du calendrier est null ou vide
	 */
	public CalendarDto byName(String nameCalender) throws RestException, ValidationException {
		CalendarDto calendarDto = null;
		if (nameCalender != null && !nameCalender.isEmpty()) {
			Builder builder = baseWebRessource.path("calendars/" + nameCalender).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<CalendarDto> calendarGeneric = new GenericType<CalendarDto>() {
			};
			calendarDto = response.getEntity(calendarGeneric);
			return calendarDto;
		} else {
			throw new ValidationException("Calendar name is empty or null");
		}
	}

	/**
	 * 
	 * Récupération de la liste des calendriers
	 * 
	 * @return List CalendarDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<CalendarDto> all() throws RestException {
		Builder builder = baseWebRessource.path("calendars").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<CalendarDto>> listCalendersDto = new GenericType<List<CalendarDto>>() {
		};
		List<CalendarDto> calendersList = response.getEntity(listCalendersDto);
		return calendersList;
	}

	/**
	 * 
	 * Création d'un calendrier
	 * 
	 * @param calendarDto
	 * @return CalendarDto
	 * @throws RestException       : Erreur lors de la création du calendrier ou
	 *                             niveau serveur (code différent de 201 Created)
	 * @throws ValidationException : nom calendrier est null
	 */
	public CalendarDto create(CalendarDto calendarDto) throws RestException, ValidationException {
		String nameCalendar = calendarDto.getName();
		if (nameCalendar != null && !nameCalendar.isEmpty()) {
			Builder builder = baseWebRessource.path("calendars").accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class, calendarDto);
			if (response.getStatus() != 201) {
				throw new RestException(response);
			}
			return byName(nameCalendar);
		} else {
			throw new ValidationException("Calendar name is empty or null");
		}
	}

	/**
	 * 
	 * Mise à jour du calendrier
	 * 
	 * @param calendarDto
	 * @return CalendarDto
	 * @throws RestException       : Erreur lors de la mise à jour du calendrier ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom calendrier null ou vide
	 */
	public CalendarDto update(CalendarDto calendarDto) throws RestException, ValidationException {
		String nameCalendar = calendarDto.getName();
		if (nameCalendar != null && !nameCalendar.isEmpty()) {
			Builder builder = baseWebRessource.path("calendars/" + nameCalendar).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, calendarDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameCalendar);
		} else {
			throw new ValidationException("Calendar name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un calendrier
	 * 
	 * @param nameCalendar : nom du calendrier
	 * @throws RestException       : nom calendrier n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom calendrier null ou vide
	 */
	public void delete(String nameCalendar) throws RestException, ValidationException {
		if (nameCalendar != null && !nameCalendar.isEmpty()) {
			Builder builder = baseWebRessource.path("calendars/" + nameCalendar).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Calendar name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un calendrier
	 * 
	 * @param calendarDto
	 * @throws RestException       : nom calendrier n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom calendrier null ou vide
	 */
	public void delete(CalendarDto calendarDto) throws RestException, ValidationException {
		delete(calendarDto.getName());
	}

	/**
	 * 
	 * Récupération d'un calendrier par son identifiant
	 * 
	 * @param id : identifiant du calendrier
	 * @return CalendarDto
	 * @throws RestException : Identifiant du calendrier n'existe pas ou erreur au
	 *                       niveau du serveur (code différent de 200 OK)
	 */
	public CalendarDto byId(long id) throws RestException {
		Builder builder = baseWebRessource.path("calendars").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<CalendarDto>> listCalendersDto = new GenericType<List<CalendarDto>>() {
		};
		List<CalendarDto> usersList = response.getEntity(listCalendersDto);

		return usersList.get(0);
	}

}
