package fr.datasyscom.scopiom.rest.pojo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.Job;
import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.JobAccount;
import fr.datasyscom.pome.ejbentity.Media;
import fr.datasyscom.pome.ejbentity.Queue;
import fr.datasyscom.pome.ejbentity.Type;
import fr.datasyscom.pome.ejbentity.interfaces.IId;
import fr.datasyscom.pome.utile.progress.JobProcessingStatistics;
import fr.datasyscom.pome.utile.progress.JobProcessingStatistics.PredictionInfo;
import fr.datasyscom.scopiom.rest.moxy.DateTimestampAdapter;
import fr.datasyscom.scopiom.ws.job.JobWSQueryBuilder;

@XmlRootElement
public class JobDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id du job en base de données */
	private long id;

	/** Nom du job */
	private String jobName;

	/** Id de la Queue à laquelle appartient le job */
	private Long queue;

	/** Statut du Job */
	private String status;

	/** Description du statut */
	private String statusDesc;

	/** Code de sortie lors de l'exécution du job */
	private Integer exitCode;

	/** Priorité du job */
	private Integer priority;

	/** Nom de l'utilisateur qui a soumis le job */
	private String userName;

	/**
	 * Chemin du fichier attaché au job (spoolé ou non).<br>
	 * <code>null</code> si aucun fichier attaché.
	 */
	private String fichier;

	/**
	 * Indique si le fichier attaché est spoolé ou non.<br>
	 * Si <code>true</code>, indique qu'un fichier est attaché au job et qu'il est
	 * spoolé sur le serveur.
	 */
	private Boolean spooled;

	/** Date d'entrée du job dans IOM */
	@XmlJavaTypeAdapter(DateTimestampAdapter.class)
	private Date entryDate;

	/** Date d'exécution du job dans IOM */
	private Date execDate;

	/** Date de rétention du job avant execution */
	private Date holdDate;

	/** Temps d'execution du job (en milliseconde) */
	private Long timeRunning;

	/** Nombre d'execution OK du job */
	private Integer nbrRunOk;

	/**
	 * Date à  laquelle le job peut etre purgé. Si <code>null</code>, pas de purge
	 * pour ce job.
	 */
	private Date dateToPurge;

	/**
	 * Statut de purge ou non. Si <code>true</code>, le job ne doit pas etre affiché
	 * et peut être purgé.
	 */
	private Boolean toPurge;

	/** Id de device lié au job, <code>null</code> si aucun device lié. */
	private Long device;

	/** Commentaire du job */
	private String comment;

	/**
	 * Id du média à utiliser pour ce job, <code>null</code> si aucun média lié.
	 */
	private Long media;

	/** Nombre de pages du job */
	private Integer nbrPage;

	/** Id du type attaché au job */
	private Long type;

	/** Nom du device lié au job, <code>null</code> si aucun device lié. */
	private String deviceName;

	/**
	 * Nom du média à utiliser pour ce job, <code>null</code> si aucun média lié.
	 */
	private String mediaName;

	/** Nom du type attaché au job. */
	private String typeName;

	/** Nom du la queue à laquelle appartient le job. */
	private String queueName;

	/** Nom de l'étape à laquelle appartient le job. */
	private String stepName;

	/** Id de l'étape à laquelle appartient le job. */
	private Long stepId;

	/** Id du JobWorkflow lié à ce Job */
	private Long jobWorkflowId;

	/** Temps nécessaire pour le péripérique pour traiter le job */
	private Long processingTime;

	/** Description du périphérique */
	private String deviceDescription;

	/** Vitesse du périphérique */
	private Integer deviceSpeed;

	/** Description du média */
	private String mediaDescription;

	/** Description de la file de traitement */
	private String queueDescription;

	private String pageRange;
	private boolean typeSupportsPageExtraction;
	private boolean queueCanRestartOk;
	private boolean queueCanRestartError;

	/** Name of last instance that has executed the job */
	private String execInstance;

	/** Estimated processing time (in ms) */
	private Double estimatedProcessingTime;
	/** Estimated time before completion (in ms) */
	private Long eta;
	/** Job processing progress */
	private Integer progress;

	public JobDto() {
		super();
	}

	/**
	 * Builds a list of JobWS from a query.
	 * <p>
	 * The query should be made using the {@link JobWSQueryBuilder} class.
	 * 
	 * @param rows result of the query
	 * @return list of JobWS
	 */
	public static List<JobDto> jobWsListfromRows(List<Object[]> rows) {
		List<JobDto> jobs = new ArrayList<JobDto>(rows.size());
		for (Object[] row : rows) {
			jobs.add(new JobDto(row));
		}

		return jobs;
	}

	/**
	 * @param rows as returned by one the of jobws.displayedProperties NamedQuery.
	 */
	public JobDto(Object[] cols) {
		this.comment = (String) cols[0];
		this.dateToPurge = (Date) cols[1];
		this.entryDate = (Date) cols[2];
		this.execDate = (Date) cols[3];
		this.execInstance = (String) cols[4];
		this.exitCode = (Integer) cols[5];
		this.fichier = (String) cols[6];
		this.holdDate = (Date) cols[7];
		this.id = (Long) cols[8];
		this.jobName = (String) cols[9];
		this.nbrPage = (Integer) cols[10];
		this.nbrRunOk = (Integer) cols[11];
		this.pageRange = (String) cols[12];
		this.priority = (Integer) cols[13];
		this.spooled = (Boolean) cols[14];
		this.status = ((JobStatusType) cols[15]).name();
		this.statusDesc = (String) cols[16];
		this.timeRunning = (Long) cols[17];
		this.toPurge = (Boolean) cols[18];
		this.userName = (String) cols[19];
		this.queue = (Long) cols[20];
		this.queueName = (String) cols[21];
		this.queueDescription = (String) cols[22];
		this.queueCanRestartOk = (Boolean) cols[23];
		this.queueCanRestartError = (Boolean) cols[24];
		setDevice((Long) cols[25]);
		this.deviceName = (String) cols[26];
		this.deviceDescription = (String) cols[27];
		this.deviceSpeed = (Integer) cols[28];
		setMedia((Long) cols[29]);
		this.mediaName = (String) cols[30];
		this.mediaDescription = (String) cols[31];
		setType((Long) cols[32]);
		this.typeName = (String) cols[33];
		this.typeSupportsPageExtraction = !StringUtils.isEmpty((String) cols[34]);
		setJobWorkflowId(this.jobWorkflowId = (Long) cols[35]);
		setStepId((Long) cols[36]);
		this.stepName = (String) cols[37];

		fillProgressInfo();
	}

	public JobDto(Job job) {
		Device device = job.getDevice();
		Media media = job.getMedia();
		Type type = job.getType();
		Queue queue = job.getQueue();

		if (device != null) {
			this.device = device.getId();
			this.deviceName = device.getName();
			this.deviceDescription = device.getDescription();
			this.deviceSpeed = device.getSpeed();
		}
		if (media != null) {
			this.media = media.getId();
			this.mediaName = media.getName();
			this.mediaDescription = media.getDescription();
		}
		if (type != null) {
			this.type = type.getId();
			this.typeName = type.getName();
			this.typeSupportsPageExtraction = (!StringUtils.isEmpty(type.getExtractPageCmd()));
		}
		if (queue != null) {
			this.queue = queue.getId();
			this.queueName = queue.getName();
			this.queueDescription = queue.getDescription();
			this.queueCanRestartOk = queue.isCanRestartOk();
			this.queueCanRestartError = queue.isCanRestartErreur();
		}

		this.id = job.getId();
		this.jobName = job.getJobName();
		this.comment = job.getComment();
		this.dateToPurge = job.getPurgeDate();
		this.entryDate = job.getEntryDate();
		this.execDate = job.getExecDate();
		this.exitCode = job.getExitCode();
		this.fichier = job.getFile();
		this.holdDate = job.getHoldDate();
		this.nbrPage = job.getNbrPage();
		this.nbrRunOk = job.getTotalExecOk();
		this.priority = job.getPriority();
		this.spooled = job.isSpooled();
		this.status = job.getStatus().name();
		this.statusDesc = job.getStatusDesc();
		this.timeRunning = job.getTimeRunning();
		this.toPurge = job.isToPurge();
		this.userName = job.getUserName();
		this.processingTime = job.getProcessingTime();

		if (job.getStep() != null) {
			this.stepId = job.getStep().getId();
			this.stepName = job.getStep().getDescription();
		}

		if (job.getJobWorkflow() != null) {
			this.jobWorkflowId = job.getJobWorkflow().getId();
		}
		this.pageRange = job.getPageRange();
		this.execInstance = job.getExecInstance();

		fillProgressInfo();
	}

	private void fillProgressInfo() {
		JobStatusType status = JobStatusType.valueOf(this.status);
		if (status.equals(JobStatusType.RUNNING) || status.equals(JobStatusType.WAIT)
				|| status.equals(JobStatusType.HOLD)) {
			PredictionInfo predictionInfo = new PredictionInfo(queueName);
			predictionInfo.deviceName = deviceName;
			predictionInfo.deviceSpeed = deviceSpeed;
			predictionInfo.duration = timeRunning;
			if (spooled && fichier != null) {
				predictionInfo.fileSize = new File(fichier).length();
			}
			predictionInfo.pageCount = nbrPage;

			Double predict = JobProcessingStatistics.getInstance().predict(predictionInfo);
			if (predict != JobProcessingStatistics.UNKNOWN) {
				this.estimatedProcessingTime = predict;
			} else {
				this.estimatedProcessingTime = null;
			}

		}

		if (status.equals(JobStatusType.RUNNING)) {
			long currentTime = System.currentTimeMillis();
			if (estimatedProcessingTime != null) {
				this.eta = Math.max(0, Math.round(estimatedProcessingTime - (currentTime - execDate.getTime())));
				this.progress = (int) Math.round(((currentTime - execDate.getTime()) / estimatedProcessingTime) * 100);
			}
		}
	}

	// GETTERS

	public JobDto(JobAccount job) {
		this.deviceName = job.getDevice();
		this.mediaName = job.getMedia();
		this.typeName = job.getType();
		this.queueName = job.getQueue();

		this.id = job.getId();
		this.jobName = job.getName();
		this.entryDate = job.getEntryDate();
		this.execDate = job.getExecDate();
		this.exitCode = job.getExitCode();
		this.nbrPage = job.getNbrPage();
		this.priority = job.getPriority();
		this.status = job.getStatus();
		this.statusDesc = job.getStatusDesc();
		this.timeRunning = job.getTimeRunning();
		this.userName = job.getUserName();
		this.execInstance = job.getExecInstance();
	}

	public long getId() {
		return id;
	}

	public String getJobName() {
		return jobName;
	}

	public Long getQueue() {
		return queue;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public Integer getExitCode() {
		return exitCode;
	}

	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getUserName() {
		return userName;
	}

	public String getFichier() {
		return fichier;
	}

	public Boolean isSpooled() {
		return spooled;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public Date getExecDate() {
		return execDate;
	}

	public Date getHoldDate() {
		return holdDate;
	}

	public Long getTimeRunning() {
		return timeRunning;
	}

	public Integer getNbrRunOk() {
		return nbrRunOk;
	}

	public Date getDateToPurge() {
		return dateToPurge;
	}

	public Boolean isToPurge() {
		return toPurge;
	}

	public Long getDevice() {
		return device;
	}

	public String getComment() {
		return comment;
	}

	public Long getMedia() {
		return media;
	}

	public Integer getNbrPage() {
		return nbrPage;
	}

	public Long getType() {
		return type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getSpooled() {
		return spooled;
	}

	public Boolean getToPurge() {
		return toPurge;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getQueueName() {
		return queueName;
	}

	public Long getStepId() {
		return stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public Long getJobWorkflowId() {
		return jobWorkflowId;
	}

	public Long getProcessingTime() {
		return processingTime;
	}

	public String getDeviceDescription() {
		return deviceDescription;
	}

	public Integer getDeviceSpeed() {
		return deviceSpeed;
	}

	public String getMediaDescription() {
		return mediaDescription;
	}

	public String getQueueDescription() {
		return queueDescription;
	}

	public String getPageRange() {
		return pageRange;
	}

	public boolean isTypeSupportsPageExtraction() {
		return typeSupportsPageExtraction;
	}

	public boolean isQueueCanRestartOk() {
		return queueCanRestartOk;
	}

	public boolean isQueueCanRestartError() {
		return queueCanRestartError;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setToPurge(Boolean toPurge) {
		this.toPurge = toPurge;
	}

	public String getExecInstance() {
		return execInstance;
	}

	public Double getEstimatedProcessingTime() {
		return estimatedProcessingTime;
	}

	public Long getEta() {
		return eta;
	}

	public Integer getProgress() {
		return progress;
	}

	private void setDevice(Long device) {
		this.device = (device == 0L ? null : device);
	}

	private void setMedia(Long media) {
		this.media = (media == 0L ? null : media);
	}

	private void setType(Long type) {
		this.type = (type == 0L ? null : type);
	}

	private void setJobWorkflowId(Long jobWorkflowId) {
		this.jobWorkflowId = (jobWorkflowId == 0L ? null : jobWorkflowId);
	}

	private void setStepId(Long stepId) {
		this.stepId = (stepId == 0L ? null : stepId);
	}

}
