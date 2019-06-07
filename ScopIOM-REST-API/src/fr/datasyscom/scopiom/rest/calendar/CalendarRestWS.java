package fr.datasyscom.scopiom.rest.calendar;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Calendar;
import fr.datasyscom.pome.ejbsession.calendar.CalendarManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.CalendarDto;

@Path("/calendars")
public class CalendarRestWS {

	@EJB
	CalendarManagerLocal calendarLocal;

	@Context
	UriInfo uriInfo;

	/**
	 * 
	 * Récupérer du calendrier
	 * 
	 * @param nameCalendar
	 * @return ok
	 */
	@GET
	@Path("/{nameCalendar}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveCalendar(@PathParam("nameCalendar") String nameCalendar) {
		CalendarDto calendarDto = null;
		try {
			Calendar calendar = calendarLocal.retrieveCalendar(nameCalendar);
			calendarDto = new CalendarDto(calendar);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		return Response.ok(calendarDto).build();
	}

	/**
	 * 
	 * Récupération de la liste des calendriers
	 * 
	 * @param idCalendar
	 * @return ok
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response retrieveAllCalendars(@QueryParam("id") long idCalendar) {
		List<CalendarDto> calendarDtoList = new ArrayList<CalendarDto>();
		if (idCalendar != 0) {
			try {
				Calendar calendar = calendarLocal.retrieveCalendar(idCalendar);
				calendarDtoList.add(new CalendarDto(calendar));
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Calendar> calendarList = calendarLocal.retrieveAllCalendar();
			for (Calendar calendar : calendarList) {
				calendarDtoList.add(new CalendarDto(calendar));
			}
		}
		GenericEntity<List<CalendarDto>> listRestCalendars = new GenericEntity<List<CalendarDto>>(calendarDtoList) {
		};
		return Response.ok(listRestCalendars).build();
	}

	/**
	 * 
	 * Supprime un calendrier
	 * 
	 * @param nameCalendar
	 * @return noContent
	 */
	@DELETE
	@Path("/{nameCalendar}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteCalendar(@PathParam("nameCalendar") String nameCalendar) {

		try {
			Calendar calendar = calendarLocal.retrieveCalendar(nameCalendar);
			calendarLocal.deleteCalendar(calendar.getId());
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * 
	 * Création d'un calendrier
	 * 
	 * @param calendarDto
	 * @return created
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response createCalendar(CalendarDto calendarDto) {

		Calendar calendar;
		if (calendarDto != null) {
			try {
				calendar = new Calendar();
				if (calendarDto.getName() != null) {
					calendar.setName(calendarDto.getName());
				}
				if (calendarDto.getDescription() != null) {
					calendar.setDescription(calendarDto.getDescription());
				}
				if (calendarDto.getCron() != null) {
					calendar.setCron(calendarDto.getCron());
				}
				calendar = calendarLocal.createCalendar(calendar);

			} catch (ValidationException e) {
				return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String newNameCalendar = String.valueOf(calendar.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameCalendar).build();

		return Response.created(uri).build();
	}

	/**
	 * 
	 * Mise à jour du calendrier
	 * 
	 * @param nameCalendrier
	 * @param calendarDto
	 * @return noContent
	 */
	@PUT
	@Path("/{nameCalendrier}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateCalendar(@PathParam("nameCalendrier") String nameCalendrier, CalendarDto calendarDto) {

		if (calendarDto != null) {
			try {
				Calendar calendar = calendarLocal.retrieveCalendar(nameCalendrier);
				if (calendarDto.getName() != null) {
					calendar.setName(calendarDto.getName());
				}
				if (calendarDto.getDescription() != null) {
					calendar.setDescription(calendarDto.getDescription());
				}
				if (calendarDto.getCron() != null) {
					calendar.setCron(calendarDto.getCron());
				}
				calendarLocal.updateCalendar(calendar);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

}
