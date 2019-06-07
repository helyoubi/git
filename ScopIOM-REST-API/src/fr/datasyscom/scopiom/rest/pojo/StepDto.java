package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.Queue.QueueStatusType;
import fr.datasyscom.pome.ejbentity.Step;
import fr.datasyscom.pome.ejbentity.filter.JobFilter;
import fr.datasyscom.pome.ejbentity.interfaces.IId;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.scopiom.ws.job.JobUILocal;

@XmlRootElement
public class StepDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	private long id;

	/** Description de l'étape */
	private String description;

	private String queueName;
	private Long queueId;
	private QueueStatusType queueStatus;

	private WorkflowDtoBis workflow;

	// Nombre de jobs par statut
	private long nbJobsError;
	private long nbJobsOk;
	private long nbJobsWait;
	private long nbJobsHold;
	private long nbJobsRunning;
	private long nbJobsTotal;

	public StepDto() {
	}

	public StepDto(JobManagerLocal jm, Step step) {
		this.id = step.getId();
		this.description = step.getDescription();
		this.queueId = step.getQueue().getId();
		this.queueName = step.getQueue().getName();
		this.queueStatus = step.getQueue().getStatus();

		long stepId = step.getId();

		long all = 0;
		all += this.nbJobsHold = jm.countJob(JobFilter.display()
				.byStepd(stepId).byStatus(JobStatusType.HOLD));
		all += this.nbJobsWait = jm.countJob(JobFilter.display()
				.byStepd(stepId).byStatus(JobStatusType.WAIT));
		all += this.nbJobsRunning = jm.countJob(JobFilter.display()
				.byStepd(stepId).byStatus(JobStatusType.RUNNING));
		all += this.nbJobsOk = jm.countJob(JobFilter.display().byStepd(stepId)
				.byStatus(JobStatusType.OK));
		all += this.nbJobsError = jm.countJob(JobFilter.display()
				.byStepd(stepId).byStatus(JobStatusType.ERROR));
		this.nbJobsTotal = all;
	}
	
	public StepDto(JobUILocal jui, WorkflowDtoBis workflowWS, Step step, String login) {
		this.id = step.getId();
		this.description = step.getDescription();
		this.queueId = step.getQueue().getId();
		this.queueName = step.getQueue().getName();
		this.queueStatus = step.getQueue().getStatus();

		long stepId = step.getId();

		long all = 0;
		all += this.nbJobsHold = jui.countJob(JobFilter.display().byStepd(stepId).byStatus(JobStatusType.HOLD), login);
		all += this.nbJobsWait = jui.countJob(JobFilter.display().byStepd(stepId).byStatus(JobStatusType.WAIT), login);
		all += this.nbJobsRunning = jui.countJob(JobFilter.display().byStepd(stepId).byStatus(JobStatusType.RUNNING), login);
		all += this.nbJobsOk = jui.countJob(JobFilter.display().byStepd(stepId).byStatus(JobStatusType.OK), login);
		all += this.nbJobsError = jui.countJob(JobFilter.display().byStepd(stepId).byStatus(JobStatusType.ERROR), login);
		this.nbJobsTotal = all;
	}

	public StepDto(JobManagerLocal jm, WorkflowDtoBis workflow, Step step) {
		this(jm, step);
		this.workflow = workflow;
	}

	
	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
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

	public Long getNbJobsTotal() {
		return nbJobsTotal;
	}

	public String getQueueName() {
		return queueName;
	}

	public Long getQueueId() {
		return queueId;
	}

	public WorkflowDtoBis getWorkflow() {
		return workflow;
	}

	public QueueStatusType getQueueStatus() {
		return queueStatus;
	}

}
