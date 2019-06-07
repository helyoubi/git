package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.DeviceSnmp;
import fr.datasyscom.pome.ejbentity.Media;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class DeviceDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id du device en base de données */
	private long id;

	/** Nom du device */
	private String name;

	/** Description du device */
	private String description;

	/** commentaire du device */
	private String comment;

	/**
	 * Nombre maximum de jobs pouvant être exécutés simultanément pour le device (50
	 * maximum).
	 */
	private Integer maxThread;

	/** Indique si ce device est un device de type template */
	private Boolean template;

	/** Type du device */
	private String type;

	private Integer speed;

	/** Statut actuel du device */
	private String status;

	/** Description du statut */
	private String statusDesc;

	/** ID du média monté sur ce device */
	private Long media;

	/** Indique si ce device utilise un média */
	private Boolean useMedia;

	/** Url du site web du device si il existe */
	private String urlAdmin;

	/** Id du module SNMP gérant ce Device */
	private Long snmpModule;

	/**
	 * Indique si ce device est accessible à tous ou protégé par les groupes
	 */
	private Boolean publicAccess;

	private String triggerCmdChangeStatus;

	/**
	 * Indique si ce device est obligatoire ou non.<br>
	 * Un device obligatoire ne peut pas etre supprimé.
	 */
	private Boolean mandatory;

	/** Nom du média monté sur le Device */
	private String mediaName;

	// Nombre de jobs par statut
	private Long nbJobsError;
	private Long nbJobsOk;
	private Long nbJobsWait;
	private Long nbJobsHold;
	private Long nbJobsRunning;
	private Long nbJobsTotal;

	public static List<DeviceDto> fromList(List<Device> devices) {
		List<DeviceDto> res = new ArrayList<DeviceDto>(devices.size());
		for (Device device : devices) {
			res.add(new DeviceDto(device));
		}

		return res;
	}

	public DeviceDto() {
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTriggerCmdChangeStatus() {
		return triggerCmdChangeStatus;
	}

	public void setTriggerCmdChangeStatus(String triggerCmdChangeStatus) {
		this.triggerCmdChangeStatus = triggerCmdChangeStatus;
	}

	public DeviceDto(Device device) {
		Media media = device.getMedia();
		DeviceSnmp deviceSnmp = device.getSnmpModule();

		if (media != null) {
			this.media = media.getId();
			this.mediaName = media.getName();
		}
		if (deviceSnmp != null) {
			this.snmpModule = deviceSnmp.getId();
		}

		this.comment = device.getComment();
		this.description = device.getDescription();
		this.id = device.getId();
		this.mandatory = device.isMandatory();
		this.speed = device.getSpeed();
		this.triggerCmdChangeStatus = device.getTriggerCmdChangeStatus();
		this.maxThread = device.getMaxThread();
		this.name = device.getName();
		this.publicAccess = device.isPublicAccess();
		this.status = device.getStatus().name();
		this.statusDesc = device.getStatusDesc();
		this.template = device.isTemplate();
		this.type = device.getType().name();
		this.urlAdmin = device.getUrlAdmin();
		this.useMedia = device.isUseMedia();
	}

	public void initNbJobsToZero() {
		this.nbJobsError = 0L;
		this.nbJobsOk = 0L;
		this.nbJobsWait = 0L;
		this.nbJobsHold = 0L;
		this.nbJobsRunning = 0L;
		this.nbJobsTotal = 0L;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public String getDescription() {
		return description;
	}

	public String getComment() {
		return comment;
	}

	public Integer getMaxThread() {
		return maxThread;
	}

	public Boolean getTemplate() {
		return template;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public Long getMedia() {
		return media;
	}

	public Boolean getUseMedia() {
		return useMedia;
	}

	public String getUrlAdmin() {
		return urlAdmin;
	}

	public Long getSnmpModule() {
		return snmpModule;
	}

	public Boolean getPublicAccess() {
		return publicAccess;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public String getMediaName() {
		return mediaName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getNbJobsError() {
		return nbJobsError;
	}

	public Long getNbJobsOk() {
		return nbJobsOk;
	}

	public Long getNbJobsWait() {
		return nbJobsWait;
	}

	public Long getNbJobsHold() {
		return nbJobsHold;
	}

	public Long getNbJobsRunning() {
		return nbJobsRunning;
	}

	public void setNbJobsError(Long nbJobsError) {
		this.nbJobsError = nbJobsError;
	}

	public void setNbJobsOk(Long nbJobsOk) {
		this.nbJobsOk = nbJobsOk;
	}

	public void setNbJobsWait(Long nbJobsWait) {
		this.nbJobsWait = nbJobsWait;
	}

	public void setNbJobsHold(Long nbJobsHold) {
		this.nbJobsHold = nbJobsHold;
	}

	public void setNbJobsRunning(Long nbJobsRunning) {
		this.nbJobsRunning = nbJobsRunning;
	}

	public Long getNbJobsTotal() {
		return nbJobsTotal;
	}

	public void setNbJobsTotal(Long nbJobsTotal) {
		this.nbJobsTotal = nbJobsTotal;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setMedia(Long media) {
		this.media = media;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public void setUseMedia(Boolean useMedia) {
		this.useMedia = useMedia;
	}

	public void setMaxThread(Integer maxThread) {
		this.maxThread = maxThread;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setUrlAdmin(String urlAdmin) {
		this.urlAdmin = urlAdmin;
	}

	public void setTemplate(Boolean template) {
		this.template = template;
	}
}
