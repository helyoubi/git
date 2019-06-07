package fr.datasyscom.scopiom.rest.media;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import fr.datasyscom.pome.ejbentity.Media;
import fr.datasyscom.pome.ejbsession.media.MediaManagerLocal;
import fr.datasyscom.pome.exception.CannotDeleteResourceException;
import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.MediaDto;

@Path("/medias")
public class MediaRestWS {
	@EJB
	private MediaManagerLocal mml;
	@Context
	UriInfo uriInfo;

	public MediaRestWS() {
	}

	@GET
	@Path("/{name}")
	@Produces({ "application/json", "application/xml" })
	public Response retriveMediaByName(@PathParam("name") String nameMedia) {
		MediaDto mediaws = null;
		try {
			Media media = mml.retrieveMediaByName(nameMedia);
			mediaws = new MediaDto(media);
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.ok(mediaws).build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	public Response retrieveAllMedia(@QueryParam("id") long idMedia) {
		List<MediaDto> mediasWS = new ArrayList<MediaDto>();
		if (idMedia != 0L) {
			try {
				mediasWS.add(new MediaDto(mml.retrieveMedia(idMedia)));
			} catch (Exception e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			List<Media> medias = mml.retrieveAllMedia();
			for (Media media : medias) {
				mediasWS.add(new MediaDto(media));
			}
		}

		GenericEntity<List<MediaDto>> listRestUsers = new GenericEntity<List<MediaDto>>(mediasWS) {
		};

		return Response.ok(listRestUsers).build();
	}

	@DELETE
	@Path("/{name}")
	@Produces({ "application/json", "application/xml" })
	public Response deleteMedia(@PathParam("name") String nameMedia) {
		try {
			Media media = mml.retrieveMediaByName(nameMedia);
			mml.deleteMedia(media.getId());
		} catch (CannotDeleteResourceException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		} catch (ValidationException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}

		return Response.noContent().build();
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response createMedia(MediaDto mediaRest) {
		if (mediaRest == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		Media media;
		try {
			media = new Media();
			if ((mediaRest.getName() != null) && (!mediaRest.getName().isEmpty())) {
				media.setName(mediaRest.getName());
			}
			if (mediaRest.getDescription() != null) {
				media.setDescription(mediaRest.getDescription());
			}
			media = mml.createMedia(media);
		} catch (ValidationException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
		}

		String newNameMedia = String.valueOf(media.getName());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newNameMedia).build(new Object[0]);

		return Response.created(uri).build();
	}

	@PUT
	@Path("/{name}")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response updateMedia(@PathParam("name") String name, MediaDto mediaRest) {
		if (mediaRest != null) {
			try {
				Media media = mml.retrieveMediaByName(name);
				if (mediaRest.getDescription() != null) {
					media.setDescription(mediaRest.getDescription());
				}
				mml.updateMedia(media);
			} catch (ValidationException e) {
				return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}
}