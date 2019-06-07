package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Agent;
import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class QueueDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id de la queue en base de données */
	private long id;

	/** Nom de la queue */
	private String name;

	/** Description de la queue */
	private String description;

	/** Priorité de la queue */
	private Integer priority;

	/** Commande d'exécution de cette queue */
	private String commandProcess;

	private String onJobErrorCommand;

	/** Statut actuel de la queue */
	private String status;

	/** Description du statut */
	private String statusDesc;

	/**
	 * Nombre maximum de jobs pouvant être exécutés simultanément pour la queue (50
	 * maximum).
	 */
	private Integer maxThread;

	/** Temps d'exécution maximum d'un job (en s) (10s minimum) */
	private Integer timeOutExecJob;

	/** Indique si cette queue est une queue de type template */
	private Boolean template;

	/**
	 * Temps de conservation des job OK en secondes.<br>
	 * -1 = pas de purge<br>
	 * 0 = pas de rétention
	 */
	private Long delayPurgeJobOk;

	/** Id de l'agent d'exécution */
	private Long agent;

	/** Description de l'agent */
	private String agentDescription;

	/** Status de l'agent */
	private String agentStatus;

	/** Id du device par défaut de la queue */
	private Long defaultDevice;

	/**
	 * Temps de conservation des job ERROR en secondes.<br>
	 * -1 = pas de purge<br>
	 * 0 = pas de rétention
	 */
	private Long delayPurgeJobError;

	/** Type de la queue */
	private String type;

	/**
	 * Indique si cette queue est obligatoire ou non.<br>
	 * Une queue obligatoire ne peut pas etre supprimée.
	 */
	private Boolean mandatory;

	/**
	 * Indique si la log est conservée pour les jobs OK
	 */
	private Boolean keepLogJobOk;

	/**
	 * Indique si cette queue est accessible à tous ou protogé par les groupes
	 */
	private Boolean publicAccess;
	
	private Boolean useDeviceRouting;

	/**
	 * Indique si la queue prend en compte les devices pour le lancement des jobs
	 */
	private Boolean useDevice;

	/** Indique si la queue utilise le device par défaut du propriétaire du job */
	private Boolean useDefaultUserDevice;

	/**
	 * Indique si la queue doit etre mise en pause si elle est vide suite à
	 * l'éxécution d'un job
	 */
	private Boolean pauseIfEmpty;

	/**
	 * Indique si cette queue autorise de rejouer un job OK.
	 */
	private Boolean canRestartOk;

	/**
	 * Indique si cette queue autorise de rejouer un job en ERREUR.
	 */
	private Boolean canRestartErreur;

	/**
	 * Indique si cette queue log les execution dans la table d'accounting
	 */
	private Boolean accounting;

	/**
	 * Indique le temps (en s) a attendre avant le lancement du job
	 */
	private Integer holdDelay;

	/** Nom du device par défaut de la queue */
	private String defaultDeviceName;
	
	private String agentAdress;

	public static List<QueueDto> fromList(List<Queue> queues) {
		List<QueueDto> res = new ArrayList<QueueDto>(queues.size());
		for (Queue queue : queues) {
			res.add(new QueueDto(queue));
		}

		return res;
	}

	public QueueDto() {
	}

	public QueueDto(Queue queue) {
		Agent agent = queue.getAgent();
		Device device = queue.getDefaultDevice();

		if (agent != null) {
			this.agent = agent.getId();
			this.agentDescription = agent.getDescription();
			this.agentAdress = agent.getAdresse();
			this.agentStatus = agent.getStatus().name();
		}
		if (device != null) {
			this.defaultDevice = device.getId();
			this.defaultDeviceName = device.getName();
		}

		this.id = queue.getId();
		this.accounting = queue.isAccounting();
		this.canRestartErreur = queue.isCanRestartErreur();
		this.canRestartOk = queue.isCanRestartOk();
		this.onJobErrorCommand = queue.getOnJobErrorCommand();
		this.commandProcess = queue.getCommandProcess();
		this.priority = queue.getPriority();
		this.delayPurgeJobError = queue.getDelayPurgeJobError();
		this.delayPurgeJobOk = queue.getDelayPurgeJobError();
		this.description = queue.getDescription();
		this.holdDelay = queue.getHoldDelay();
		this.keepLogJobOk = queue.isKeepLogJobOk();
		this.mandatory = queue.isMandatory();
		this.maxThread = queue.getMaxThread();
		this.name = queue.getName();
		this.pauseIfEmpty = queue.isPauseIfEmpty();
		this.publicAccess = queue.isPublicAccess();
		this.status = queue.getStatus().name();
		this.statusDesc = queue.getStatusDesc();
		this.template = queue.isTemplate();
		this.timeOutExecJob = queue.getTimeOutExecJob();
		this.type = queue.getType().name();
		this.useDefaultUserDevice = queue.isUseDefaultUserDevice();
		this.useDevice = queue.isUseDevice();
		this.useDeviceRouting=queue.isUseDeviceRouting();
	}

	public Long getAgent() {
		return agent;
	}

	public void setAgent(Long agent) {
		this.agent = agent;
	}

	public String getAgentAdress() {
		return agentAdress;
	}

	public void setAgentAdress(String agentAdress) {
		this.agentAdress = agentAdress;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getOnJobErrorCommand() {
		return onJobErrorCommand;
	}

	public void setOnJobErrorCommand(String onJobErrorCommand) {
		this.onJobErrorCommand = onJobErrorCommand;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean isUseDeviceRouting() {
		return useDeviceRouting;
	}

	public void setUseDeviceRouting(Boolean useDeviceRouting) {
		this.useDeviceRouting = useDeviceRouting;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setCommandProcess(String commandProcess) {
		this.commandProcess = commandProcess;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public void setMaxThread(Integer maxThread) {
		this.maxThread = maxThread;
	}

	public void setTimeOutExecJob(Integer timeOutExecJob) {
		this.timeOutExecJob = timeOutExecJob;
	}

	public void setTemplate(Boolean template) {
		this.template = template;
	}

	public void setDelayPurgeJobOk(Long delayPurgeJobOk) {
		this.delayPurgeJobOk = delayPurgeJobOk;
	}


	public void setAgentDescription(String agentDescription) {
		this.agentDescription = agentDescription;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

	public void setDefaultDevice(Long defaultDevice) {
		this.defaultDevice = defaultDevice;
	}

	public void setDelayPurgeJobError(Long delayPurgeJobError) {
		this.delayPurgeJobError = delayPurgeJobError;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setKeepLogJobOk(Boolean keepLogJobOk) {
		this.keepLogJobOk = keepLogJobOk;
	}

	public void setPublicAccess(Boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

	public void setUseDevice(Boolean useDevice) {
		this.useDevice = useDevice;
	}

	public void setUseDefaultUserDevice(Boolean useDefaultUserDevice) {
		this.useDefaultUserDevice = useDefaultUserDevice;
	}

	public void setPauseIfEmpty(Boolean pauseIfEmpty) {
		this.pauseIfEmpty = pauseIfEmpty;
	}

	public void setCanRestartOk(Boolean canRestartOk) {
		this.canRestartOk = canRestartOk;
	}

	public void setCanRestartErreur(Boolean canRestartErreur) {
		this.canRestartErreur = canRestartErreur;
	}

	public void setAccounting(Boolean accounting) {
		this.accounting = accounting;
	}

	public void setHoldDelay(Integer holdDelay) {
		this.holdDelay = holdDelay;
	}

	public void setDefaultDeviceName(String defaultDeviceName) {
		this.defaultDeviceName = defaultDeviceName;
	}

	public String getCommandProcess() {
		return commandProcess;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public Integer getMaxThread() {
		return maxThread;
	}

	public Integer getTimeOutExecJob() {
		return timeOutExecJob;
	}

	public Boolean getTemplate() {
		return template;
	}

	public Long getDelayPurgeJobOk() {
		return delayPurgeJobOk;
	}

	public String getAgentDescription() {
		return agentDescription;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public Long getDefaultDevice() {
		return defaultDevice;
	}

	public Long getDelayPurgeJobError() {
		return delayPurgeJobError;
	}

	public String getType() {
		return type;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public Boolean getKeepLogJobOk() {
		return keepLogJobOk;
	}

	public Boolean getPublicAccess() {
		return publicAccess;
	}

	public Boolean getUseDevice() {
		return useDevice;
	}

	public Boolean getUseDefaultUserDevice() {
		return useDefaultUserDevice;
	}

	public Boolean getPauseIfEmpty() {
		return pauseIfEmpty;
	}

	public Boolean getCanRestartOk() {
		return canRestartOk;
	}

	public Boolean getCanRestartErreur() {
		return canRestartErreur;
	}

	public Boolean getAccounting() {
		return accounting;
	}

	public Integer getHoldDelay() {
		return holdDelay;
	}

	public String getDefaultDeviceName() {
		return defaultDeviceName;
	}

}
