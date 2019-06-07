package fr.datasyscom.scopiom.restclient.device;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import fr.datasyscom.pome.exception.ValidationException;
import fr.datasyscom.scopiom.rest.pojo.DeviceDto;
import fr.datasyscom.scopiom.rest.pojo.DevicePropertyDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceSnmpDto;
import fr.datasyscom.scopiom.rest.pojo.DeviceStatusDto;
import fr.datasyscom.scopiom.rest.pojo.GroupeDto;
import fr.datasyscom.scopiom.rest.pojo.JobDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;

public class DeviceClient {

	public enum DeviceStatusType {
		OPEN, CLOSE
	}

	WebResource baseWebRessource;

	public DeviceClient(WebResource baseWebRessource) {
		this.baseWebRessource = baseWebRessource;
	}

	/**
	 * 
	 * R�cup�ration du device par son nom
	 * 
	 * @param nameDevice : nom du device
	 * @return DeviceDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device est null ou vide
	 */
	public DeviceDto byName(String nameDevice) throws RestException, ValidationException {
		DeviceDto deviceDto = null;
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<DeviceDto> deviceGeneric = new GenericType<DeviceDto>() {
			};
			deviceDto = response.getEntity(deviceGeneric);
			return deviceDto;
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un p�riph�rique
	 * 
	 * @param nameDevice : nom du p�riph�rique
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 204
	 *                             NoContent)
	 * @throws ValidationException :nom du p�riph�rique null ou vide
	 */
	public void delete(String nameDevice) throws RestException, ValidationException {
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice).accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Device name empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'un p�riph�rique
	 * 
	 * @param deviceDto
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 204
	 *                             NoContent)
	 * @throws ValidationException :nom du p�riph�rique null ou vide
	 */
	public void delete(DeviceDto deviceDto) throws RestException, ValidationException {
		delete(deviceDto.getName());
	}

	/**
	 * 
	 * Suppression d'une propri�t� du p�riph�rique
	 * 
	 * @param nameDevice   : nom du p�riph�rique
	 * @param nameProperty : nom de propri�t�
	 * @throws RestException       : nom du p�riph�rique ou propri�t� n'existe pas
	 *                             ou erreur au niveau du serveur (code diff�rent de
	 *                             204 NoContent)
	 * @throws ValidationException :nom du p�riph�rique ou propri�t� null ou vide
	 */
	public void deleteProperty(String nameDevice, String nameProperty) throws RestException, ValidationException {
		if (nameDevice != null && !nameDevice.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Device name or property empty or null");
		}
	}

	/**
	 * 
	 * Suppression d'une propri�t� du p�riph�rique
	 * 
	 * @param deviceDto
	 * @param nameProperty : nom de propri�t�
	 * @throws RestException       : nom du p�riph�rique ou propri�t� n'existe pas
	 *                             ou erreur au niveau du serveur (code diff�rent de
	 *                             204 NoContent)
	 * @throws ValidationException :nom du p�riph�rique ou propri�t� null ou vide
	 */
	public void deleteProperty(DeviceDto deviceDto, String nameProperty) throws RestException, ValidationException {
		deleteProperty(deviceDto.getName(), nameProperty);
	}

	/**
	 * 
	 * R�cup�ration de la liste des jobs attach�s au p�riph�rique
	 * 
	 * @param nameDevice : nom du device
	 * @return List JobDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public List<JobDto> allJobs(String nameDevice) throws RestException, ValidationException {
		if (nameDevice == null || nameDevice.isEmpty()) {
			throw new ValidationException("Device name is null or empty");
		}
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/jobs").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<JobDto>> jobGeneric = new GenericType<List<JobDto>>() {
		};
		List<JobDto> jobListDto = response.getEntity(jobGeneric);
		return jobListDto;
	}

	/**
	 * 
	 * R�cup�ration de la liste des jobs attach�s au p�riph�rique
	 * 
	 * @param deviceDto
	 * @return List JobDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public List<JobDto> allJobs(DeviceDto deviceDto) throws RestException, ValidationException {
		return allJobs(deviceDto.getName());
	}

	/**
	 * 
	 * R�cup�ration de la liste des p�riph�rique
	 * 
	 * @return List DeviceDto
	 * @throws RestException : erreur au niveau du serveur (code diff�rent de 200
	 *                       OK)
	 */
	public List<DeviceDto> all() throws RestException {
		Builder builder = baseWebRessource.path("devices").accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<DeviceDto>> deviceList = new GenericType<List<DeviceDto>>() {
		};
		List<DeviceDto> deviceDtos = response.getEntity(deviceList);
		return deviceDtos;
	}

	/**
	 * 
	 * R�cup�ration du p�riph�rique par son identifiant
	 * 
	 * @param id : identifiant du device
	 * @return DeviceDto
	 * @throws RestException : Identifiant du p�riph�rique n'existe pas ou erreur au
	 *                       niveau du serveur (code diff�rent de 200 OK)
	 */
	public DeviceDto byId(long id) throws RestException {
		DeviceDto deviceDto = null;
		Builder builder = baseWebRessource.path("devices").queryParam("id", String.valueOf(id))
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<DeviceDto>> listdevicesDto = new GenericType<List<DeviceDto>>() {
		};
		List<DeviceDto> deviceList = response.getEntity(listdevicesDto);
		deviceDto = deviceList.get(0);

		return deviceDto;
	}

	/**
	 * 
	 * R�cup�ration de la liste de propri�t�s du p�riph�rique
	 * 
	 * @param nameDevice : nom du p�riph�rique
	 * @return List DevicePropertyDto
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du p�riph�rique null ou vide
	 */
	public List<DevicePropertyDto> allProperties(String nameDevice) throws RestException, ValidationException {
		List<DevicePropertyDto> devicePropertyDtos;
		if (nameDevice == null || nameDevice.isEmpty()) {
			throw new ValidationException("Device name is null or empty");
		}
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/property")
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<DevicePropertyDto>> devicePropertyList = new GenericType<List<DevicePropertyDto>>() {
		};
		devicePropertyDtos = response.getEntity(devicePropertyList);
		return devicePropertyDtos;
	}

	/**
	 * 
	 * R�cup�ration de la liste de propri�t�s du p�riph�rique
	 * 
	 * @param deviceDto
	 * @return List DevicePropertyDto
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du p�riph�rique null ou vide
	 */
	public List<DevicePropertyDto> allProperties(DeviceDto deviceDto) throws RestException, ValidationException {
		return allProperties(deviceDto.getName());
	}

	/**
	 * 
	 * R�cup�ration de la liste des jobs attach�s au p�riph�rique par statut
	 * 
	 * @param nameDevice : nom du device
	 * @param status     : status du job
	 * @return List JobDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public List<JobDto> jobsByStatus(String nameDevice, JobStatusType status)
			throws RestException, ValidationException {
		if (nameDevice == null || nameDevice.isEmpty()) {
			throw new ValidationException("Device name is null or empty");
		}
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/jobs/status/" + status)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
		GenericType<List<JobDto>> jobGeneric = new GenericType<List<JobDto>>() {
		};
		List<JobDto> jobListDto = response.getEntity(jobGeneric);
		return jobListDto;
	}

	/**
	 * 
	 * R�cup�ration de la liste des jobs attach�s au p�riph�rique par status
	 * 
	 * @param deviceDto
	 * @param status    : status du job
	 * @return List JobDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public List<JobDto> jobsByStatus(DeviceDto deviceDto, JobStatusType status)
			throws RestException, ValidationException {
		return jobsByStatus(deviceDto.getName(), status);
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param nameDevice : nom du p�riph�rique
	 * @param nameGroup  : nom du groupe
	 * @throws RestException       : nom du groupe ou du p�riph�rique n'existe pas
	 *                             ,ou erreur au niveau du serveur (code diff�rent
	 *                             de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du p�riph�rique sont null ou
	 *                             vides
	 */
	public void addGroup(String nameDevice, String nameGroup) throws RestException, ValidationException {
		if (nameGroup != null && !nameGroup.isEmpty() && nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.post(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Group name or device name are empty or null");
		}
	}

	/**
	 * 
	 * ajout d'un groupe
	 * 
	 * @param deviceDto
	 * @param groupeDto
	 * @throws RestException       : nom du groupe ou du p�riph�rique n'existe pas
	 *                             ,ou erreur au niveau du serveur (code diff�rent
	 *                             de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du p�riph�rique sont null ou
	 *                             vides
	 */
	public void addGroup(DeviceDto deviceDto, GroupeDto groupeDto) throws RestException, ValidationException {
		addGroup(deviceDto.getName(), groupeDto.getName());
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param nameDevice : nom du p�riph�rique
	 * @param nameGroup  : nom du groupe
	 * @throws RestException       : nom du groupe ou du p�riph�rique n'existe pas
	 *                             ,ou erreur au niveau du serveur (code diff�rent
	 *                             de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du p�riph�rique sont null ou
	 *                             vides
	 */
	public void removeGroup(String nameDevice, String nameGroup) throws RestException, ValidationException {
		if (nameDevice != null && !nameDevice.isEmpty() && nameGroup != null && !nameGroup.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/groups/" + nameGroup)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
		} else {
			throw new ValidationException("Device name or group name are empty or null");
		}
	}

	/**
	 * 
	 * retire le groupe
	 * 
	 * @param deviceDto
	 * @param groupeDto
	 * @throws RestException       : nom du groupe ou du p�riph�rique n'existe pas
	 *                             ,ou erreur au niveau du serveur (code diff�rent
	 *                             de 200 Ok)
	 * @throws ValidationException : nom du groupe ou du p�riph�rique sont null ou
	 *                             vides
	 */
	public void removeGroup(DeviceDto deviceDto, GroupeDto groupeDto) throws RestException, ValidationException {
		removeGroup(deviceDto.getName(), groupeDto.getName());
	}

	/**
	 * 
	 * R�cup�ration du groupe
	 * 
	 * @param nameGroup : nom du groupe
	 * @return List GroupeDto
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du p�riph�rique est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(String nameDevice) throws RestException, ValidationException {
		List<GroupeDto> groupeDtos;
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/groups")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<List<GroupeDto>> groupGenericList = new GenericType<List<GroupeDto>>() {
			};
			groupeDtos = response.getEntity(groupGenericList);
			return groupeDtos;
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * R�cup�ration du groupe
	 * 
	 * @param deviceDto
	 * @return List GroupeDto
	 * @throws RestException       : nom du p�riph�rique n'existe pas ou erreur au
	 *                             niveau du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du p�riph�rique est null ou vide
	 */
	public List<GroupeDto> retrieveGroup(DeviceDto deviceDto) throws RestException, ValidationException {
		return retrieveGroup(deviceDto.getName());
	}

	/**
	 * 
	 * R�cup�ration de la propri�t� du p�riph�rique
	 * 
	 * @param nameDevice   : nom du device
	 * @param nameProperty : nom de propri�t�
	 * @return DevicePropertyDto
	 * @throws RestException       : nom du device ou propri�t� n'existe pas ou
	 *                             erreur au niveau du serveur (code diff�rent de
	 *                             200 OK)
	 * @throws ValidationException : nom du device ou propri�t� null ou vide
	 */
	public DevicePropertyDto retrieveProperty(String nameDevice, String nameProperty)
			throws RestException, ValidationException {
		DevicePropertyDto devicePropertyDto = null;
		if (nameDevice != null && !nameDevice.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<DevicePropertyDto> devicePropertyGeneric = new GenericType<DevicePropertyDto>() {
			};
			devicePropertyDto = response.getEntity(devicePropertyGeneric);
			return devicePropertyDto;
		} else {
			throw new ValidationException("Device name or property are empty or null");
		}
	}

	/**
	 * 
	 * R�cup�ration de la propri�t� du p�riph�rique
	 * 
	 * @param deviceDto
	 * @param nameProperty : nom de propri�t�
	 * @return DevicePropertyDto
	 * @throws RestException       : nom du device ou propri�t� n'existe pas ou
	 *                             erreur au niveau du serveur (code diff�rent de
	 *                             200 OK)
	 * @throws ValidationException : nom du device ou propri�t� null ou vide
	 */
	public DevicePropertyDto retrieveProperty(DeviceDto deviceDto, String nameProperty)
			throws RestException, ValidationException {
		return retrieveProperty(deviceDto.getName(), nameProperty);
	}

	/**
	 * 
	 * R�cup�ration la configuration SNMP d'un device
	 * 
	 * @param nameDevice : nom du device
	 * @return DeviceSnmpDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device est null ou vide
	 */
	public DeviceSnmpDto retrieveSnmp(String nameDevice) throws RestException, ValidationException {
		DeviceSnmpDto deviceSnmpDto = null;
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/snmp")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<DeviceSnmpDto> deviceSnmpGeneric = new GenericType<DeviceSnmpDto>() {
			};
			deviceSnmpDto = response.getEntity(deviceSnmpGeneric);
			return deviceSnmpDto;
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * R�cup�ration la configuration SNMP d'un device
	 * 
	 * @param deviceDto
	 * @return DeviceSnmpDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device est null ou vide
	 */
	public DeviceSnmpDto retrieveSnmp(DeviceDto deviceDto) throws RestException, ValidationException {
		return retrieveSnmp(deviceDto.getName());
	}

	/**
	 * 
	 * R�cup�ration du statut du p�riph�rique
	 * 
	 * @param nameDevice : nom du device
	 * @return DeviceStatusDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device est null ou vide
	 */
	public DeviceStatusDto status(String nameDevice) throws RestException, ValidationException {
		DeviceStatusDto deviceStatusDto = null;
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/status")
					.accept(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RestException(response);
			}
			GenericType<DeviceStatusDto> deviceStatusGeneric = new GenericType<DeviceStatusDto>() {
			};
			deviceStatusDto = response.getEntity(deviceStatusGeneric);
			return deviceStatusDto;
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * R�cup�ration du statut du p�riph�rique
	 * 
	 * @param deviceDto
	 * @return DeviceStatusDto
	 * @throws RestException       : nom du device n'existe pas ou erreur au niveau
	 *                             du serveur (code diff�rent de 200 OK)
	 * @throws ValidationException : nom du device est null ou vide
	 */
	public DeviceStatusDto status(DeviceDto deviceDto) throws RestException, ValidationException {
		return status(deviceDto.getName());
	}

	/**
	 * 
	 * cr�ation du p�riph�rique
	 * 
	 * @param newDevice   : p�rihp�rique a cr�e
	 * @param deviceModel : model du p�riph�rique
	 *
	 * @throws RestException       : Erreur lors de la cr�ation du device ou niveau
	 *                             serveur (code diff�rent de 201 Created)
	 * @throws ValidationException : nom du device ou model sont null ou vides
	 */
	public DeviceDto create(String newDevice, String deviceModel) throws RestException, ValidationException {
		if (newDevice == null || newDevice.isEmpty() && deviceModel == null || deviceModel.isEmpty()) {
			throw new ValidationException("Device name or model are null or empty");
		}
		Builder builder = baseWebRessource.path("devices/" + newDevice + "/model/" + deviceModel)
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return byName(newDevice);
	}

	/**
	 * 
	 * cr�ation d'une propri�t� du p�riph�rique
	 * 
	 * @param nameDevice        : nom du p�rihp�rique
	 * @param devicePropertyDto
	 *
	 * @throws RestException       : Erreur lors de la cr�ation de la propri�t� du
	 *                             p�riph�rique ou niveau serveur (code diff�rent de
	 *                             201 Created)
	 * @throws ValidationException : nom du device ou propri�t� null ou vide
	 */
	public DevicePropertyDto createProperty(String nameDevice, DevicePropertyDto devicePropertyDto)
			throws RestException, ValidationException {
		if (nameDevice == null || nameDevice.isEmpty() && devicePropertyDto.getName() == null
				|| devicePropertyDto.getName().isEmpty()) {
			throw new ValidationException("Device name or property are null or empty");
		}
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/property")
				.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class, devicePropertyDto);
		if (response.getStatus() != 201) {
			throw new RestException(response);
		}
		return retrieveProperty(nameDevice, devicePropertyDto.getName());
	}

	/**
	 * 
	 * cr�ation d'une propri�t� du p�riph�rique
	 * 
	 * @param deviceDto
	 * @param devicePropertyDto
	 *
	 * @throws RestException       : Erreur lors de la cr�ation de la propri�t� du
	 *                             p�riph�rique ou niveau serveur (code diff�rent de
	 *                             201 Created)
	 * @throws ValidationException : nom du device ou propri�t� null ou vide
	 */
	public DevicePropertyDto createProperty(DeviceDto deviceDto, DevicePropertyDto devicePropertyDto)
			throws RestException, ValidationException {
		return createProperty(deviceDto.getName(), devicePropertyDto);
	}

	/**
	 * 
	 * Mise � jour du device
	 * 
	 * @param deviceDto
	 * @return DeviceDto
	 * @throws RestException       : Erreur lors de la mise � jour du device ou
	 *                             niveau serveur (code diff�rent de 204 NoContent)
	 * @throws ValidationException : name device est null ou vide
	 */
	public DeviceDto update(DeviceDto deviceDto) throws RestException, ValidationException {
		String nameDevice = deviceDto.getName();
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, deviceDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return byName(nameDevice);
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * Mise � jour de la propri�t� du device
	 * 
	 * @param nameDevice        : nom du p�riph�rique
	 * @param devicePropertyDto
	 * @return DevicePropertyDto
	 * @throws RestException       : Erreur lors de la mise � jour de la propri�t�
	 *                             du p�riph�rique ou niveau serveur (code diff�rent
	 *                             de 204 NoContent)
	 * @throws ValidationException : name device ou property est null ou vide
	 */
	public DevicePropertyDto updateProperty(String nameDevice, DevicePropertyDto devicePropertyDto)
			throws RestException, ValidationException {
		String nameProperty = devicePropertyDto.getName();
		if (nameDevice != null && !nameDevice.isEmpty() && nameProperty != null && !nameProperty.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/property/" + nameProperty)
					.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, devicePropertyDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return retrieveProperty(nameDevice, nameProperty);
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * Mise � jour de la propri�t� du device
	 * 
	 * @param deviceDto
	 * @param devicePropertyDto
	 * @return DevicePropertyDto
	 * @throws RestException       : Erreur lors de la mise � jour de la propri�t�
	 *                             du p�riph�rique ou niveau serveur (code diff�rent
	 *                             de 204 NoContent)
	 * @throws ValidationException : name device ou property est null ou vide
	 */
	public DevicePropertyDto updateProperty(DeviceDto deviceDto, DevicePropertyDto devicePropertyDto)
			throws RestException, ValidationException {
		return updateProperty(deviceDto.getName(), devicePropertyDto);
	}

	/**
	 * 
	 * Mise � jour de la configuration SNMP
	 * 
	 * @param nameDevice    : nom du device
	 * @param deviceSnmpDto
	 * @return DeviceSnmpDto
	 * @throws RestException       : Erreur lors de la mise � jour de la
	 *                             configuration SNMP ou niveau serveur (code
	 *                             diff�rent de 204 NoContent)
	 * @throws ValidationException : nom du device ou deviceSnmp sont null ou vides
	 */
	public DeviceSnmpDto updateSnmpConfiguration(String nameDevice, DeviceSnmpDto deviceSnmpDto)
			throws RestException, ValidationException {
		if (nameDevice == null || nameDevice.isEmpty() && deviceSnmpDto == null) {
			throw new ValidationException("Device name or snmp are empty or null");
		}
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/snmp").accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.put(ClientResponse.class, deviceSnmpDto);
		if (response.getStatus() != 204) {
			throw new RestException(response);
		}
		return retrieveSnmp(nameDevice);
	}

	/**
	 * 
	 * Mise � jour de la configuration SNMP
	 * 
	 * @param deviceDto
	 * @param deviceSnmpDto
	 * @return DeviceSnmpDto
	 * @throws RestException       : Erreur lors de la mise � jour de la
	 *                             configuration SNMP ou niveau serveur (code
	 *                             diff�rent de 204 NoContent)
	 * @throws ValidationException : nom du device ou deviceSnmp sont null ou vides
	 */
	public DeviceSnmpDto updateSnmpConfiguration(DeviceDto deviceDto, DeviceSnmpDto deviceSnmpDto)
			throws RestException, ValidationException {
		return updateSnmpConfiguration(deviceDto.getName(), deviceSnmpDto);
	}

	/**
	 * 
	 * Lance le service SNMP du device
	 * 
	 * @throws RestException : erreur niveau serveur (code diff�rent de 200 OK)
	 */
	public void startSnmp(String nameDevice) throws RestException {
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/snmp/start")
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Stop le service SNMP du device
	 * 
	 * @throws RestException : erreur niveau serveur (code diff�rent de 200 OK)
	 */
	public void stopSnmp(String nameDevice) throws RestException {
		Builder builder = baseWebRessource.path("devices/" + nameDevice + "/snmp/stop")
				.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		ClientResponse response = builder.post(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RestException(response);
		}
	}

	/**
	 * 
	 * Mise � jour du statut du p�riph�tique
	 * 
	 * @param nameDevice : nom du device
	 * @param status     : status du device
	 * @param statusDesc : description du device
	 * @return DeviceStatusDto
	 * @throws RestException       : Erreur lors de la mise du status du device ou
	 *                             niveau serveur (code diff�rent de 204 NoContent)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public DeviceStatusDto updateStatus(String nameDevice, DeviceStatusType status, String statusDesc)
			throws RestException, ValidationException {
		DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
		deviceStatusDto.setStatus(status.name());
		deviceStatusDto.setStatusDesc(statusDesc);
		if (nameDevice != null && !nameDevice.isEmpty()) {
			Builder builder = baseWebRessource.path("devices/" + nameDevice + "/status")
					.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
			ClientResponse response = builder.put(ClientResponse.class, deviceStatusDto);
			if (response.getStatus() != 204) {
				throw new RestException(response);
			}
			return status(nameDevice);
		} else {
			throw new ValidationException("Device name is empty or null");
		}
	}

	/**
	 * 
	 * Mise � jour du statut de p�riph�tique
	 * 
	 * @param deviceDto
	 * @param status     : status du device
	 * @param statusDesc : description du device
	 * @return DeviceStatusDto
	 * @throws RestException       : Erreur lors de la mise du status du device ou
	 *                             niveau serveur (code diff�rent de 204 NoContent)
	 * @throws ValidationException : nom du device null ou vide
	 */
	public DeviceStatusDto updateStatus(DeviceDto deviceDto, DeviceStatusType status, String statusDesc)
			throws RestException, ValidationException {
		return updateStatus(deviceDto.getName(), status, statusDesc);
	}

	/**
	 * 
	 * Ouverture du statut du p�riph�rique
	 * 
	 * @param nameDevice : nom du device
	 * @return DevicePropertyDto
	 * @throws RestException       : Erreur lors de la mise � jour de la propri�t�
	 *                             du device ou niveau serveur (code diff�rent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du device ou propri�t� sont null ou vides
	 */
	public DeviceStatusDto open(String nameDevice) throws RestException, ValidationException {
		return updateStatus(nameDevice, DeviceStatusType.OPEN, null);
	}

	/**
	 * 
	 * Fermeture du statut du p�riph�rique
	 * 
	 * @param nameDevice : nom du device
	 * @return DevicePropertyDto
	 * @throws RestException       : Erreur lors de la mise � jour de la propri�t�
	 *                             du device ou niveau serveur (code diff�rent de
	 *                             204 NoContent)
	 * @throws ValidationException : nom du device ou propri�t� sont null ou vides
	 */
	public DeviceStatusDto close(String nameDevice) throws RestException, ValidationException {
		return updateStatus(nameDevice, DeviceStatusType.CLOSE, null);
	}

}
