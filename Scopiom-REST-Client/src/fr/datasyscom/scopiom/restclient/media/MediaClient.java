package fr.datasyscom.scopiom.restclient.media;

import java.util.List;

import javax.ws.rs.core.MediaType;
import fr.datasyscom.pome.exception.ValidationException;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.scopiom.rest.pojo.MediaDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;

/**
 * 
 * Media Client Api
 * 
 * @author DataSyscom
 *
 */
public class MediaClient {

	WebResource baseWebRessource;

	public MediaClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * Récupération du média par son identifiant
	 * 
	 * @param id : identifiant du média
	 * @return MediaDto
	 * @throws RestException : Identifiant du média n'existe pas ou erreur au niveau
	 *                       du serveur (code différent de 200 OK)
	 */
	public MediaDto byId(long id) throws RestException {
		Builder builder = baseWebRessource.path("medias").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<MediaDto>> listMediaDto = new GenericType<List<MediaDto>>() {
		};
		List<MediaDto> mediaDtoList = response.getEntity(listMediaDto);
		MediaDto mediaDto = mediaDtoList.get(0);

		return mediaDto;
	}

	/**
	 * 
	 * Récupération de la liste des médias
	 * 
	 * @return List MediaDto
	 * @throws RestException : Erreur au niveau du serveur (code différent de 200
	 *                       OK)
	 */
	public List<MediaDto> all() throws RestException {
		Builder builder = baseWebRessource.path("medias").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<MediaDto>> listMediaDto = new GenericType<List<MediaDto>>() {
		};
		List<MediaDto> mediaDtoList = response.getEntity(listMediaDto);
		return mediaDtoList;
	}

	/**
	 * 
	 * Récupération du Media par son nom
	 * 
	 * @param mediaName : Nom du média
	 * @return MediaDto
	 * @throws RestException       : nom du média n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 200 OK)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public MediaDto byName(String mediaName) throws RestException, ValidationException {
		MediaDto mediaDto = null;
		if (mediaName != null && !mediaName.isEmpty()) {
			Builder builder = baseWebRessource.path("medias/" + mediaName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<MediaDto> mediaDtoGeneric = new GenericType<MediaDto>() {
			};
			mediaDto = response.getEntity(mediaDtoGeneric);
			return mediaDto;
		} else {
			throw new ValidationException("Media name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression du media par son nom
	 * 
	 * @param mediaName : nom du média
	 * @throws RestException       : nom du média n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public void delete(String mediaName) throws RestException, ValidationException {
		if (mediaName != null && !mediaName.isEmpty()) {
			Builder builder = baseWebRessource.path("medias/" + mediaName).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Media name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression du media
	 * 
	 * @param mediaDto
	 * @throws RestException       : nom du média n'existe pas ou erreur au niveau
	 *                             du serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public void delete(MediaDto mediaDto) throws RestException, ValidationException {
		this.delete(mediaDto.getName());
	}

	/**
	 * 
	 * Création du media
	 * 
	 * @param mediaDto
	 * @return MediaDto
	 * @throws RestException       : Erreur lors de la création du média ou niveau
	 *                             serveur (code différent de 201 Created)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public MediaDto create(MediaDto mediaDto) throws RestException, ValidationException {
		String nameMedia = mediaDto.getName();
		if (nameMedia == null || nameMedia.isEmpty()) {
			throw new ValidationException("Media name is empty or null");
		}
		Builder builder = baseWebRessource.path("medias").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, mediaDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return byName(mediaDto.getName());

	}

	/**
	 * 
	 * Mise à jour du média
	 * 
	 * @param mediaDto
	 * @return MediaDto
	 * @throws RestException       : Erreur lors de la mise à jour du média ou
	 *                             niveau serveur (code différent de 204 NoContent)
	 * @throws ValidationException : nom du média null ou vide
	 */
	public MediaDto update(MediaDto mediaDto) throws RestException, ValidationException {
		String nameMedia = mediaDto.getName();
		if (nameMedia != null && !nameMedia.isEmpty()) {
			Builder builder = baseWebRessource.path("medias/" + nameMedia).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, mediaDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameMedia);
		} else {
			throw new ValidationException("Media name is empty or null");
		}
	}

}
