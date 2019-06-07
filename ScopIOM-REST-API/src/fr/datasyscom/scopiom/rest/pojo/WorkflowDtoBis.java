package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Job.JobStatusType;
import fr.datasyscom.pome.ejbentity.Step;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbentity.filter.JobFilter;
import fr.datasyscom.pome.ejbentity.interfaces.IId;
import fr.datasyscom.pome.ejbsession.job.JobManagerLocal;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.scopiom.ws.job.JobUILocal;

@XmlRootElement
public class WorkflowDtoBis implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	private long id;

	/** Nom du Workflow */
	private String nom;

	/** Description du Workflow */
	private String description;

	/** Etapes du Workflow */
	private List<StepDto> etapes;

	private long nbJobsWorkflow;

	// Nombre de jobs par statut
	private long nbJobsError;
	private long nbJobsOk;
	private long nbJobsWait;
	private long nbJobsHold;
	private long nbJobsRunning;
	private long nbJobsTotal;

	public static List<WorkflowDtoBis> fromList(JobManagerLocal jm, WorkflowManagerLocal wm, List<Workflow> workflows) {
		List<WorkflowDtoBis> res = new ArrayList<WorkflowDtoBis>(workflows.size());
		for (Workflow wf : workflows) {
			res.add(new WorkflowDtoBis(jm, wm, wf));
		}

		return res;
	}

	public WorkflowDtoBis() {
	}

	public WorkflowDtoBis(JobManagerLocal jm, WorkflowManagerLocal wm, Workflow workflow) {
		this.id = workflow.getId();
		this.nom = workflow.getName();
		this.description = workflow.getDescription();

		this.etapes = new ArrayList<StepDto>();
		for (Step step : wm.retrieveAllStep(id)) {
			etapes.add(new StepDto(jm, this, step));
		}

		long workflowId = workflow.getId();
		this.nbJobsWorkflow = wm.countJobWorkflow(workflow.getId());

		this.nbJobsHold = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.HOLD));
		this.nbJobsWait = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.WAIT));
		this.nbJobsRunning = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.RUNNING));
		this.nbJobsOk = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.OK));
		this.nbJobsError = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.ERROR));
		this.nbJobsTotal = jm.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.ALL));
	}
	
	public WorkflowDtoBis(JobUILocal jui, WorkflowManagerLocal wm, Workflow workflow, String login) {
		this.id = workflow.getId();
		this.nom = workflow.getName();
		this.description = workflow.getDescription();

		this.etapes = new ArrayList<StepDto>();
		for (Step step : wm.retrieveAllStep(id)) {
			etapes.add(new StepDto(jui, this, step, login));
		}

		long workflowId = workflow.getId();
		this.nbJobsWorkflow = wm.countJobWorkflow(workflow.getId());

		this.nbJobsHold = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.HOLD), login);
		this.nbJobsWait = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.WAIT), login);
		this.nbJobsRunning = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.RUNNING), login);
		this.nbJobsOk = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.OK), login);
		this.nbJobsError = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.ERROR), login);
		this.nbJobsTotal = jui.countJob(JobFilter.display().byWorkflowId(workflowId).byStatus(JobStatusType.ALL), login);
	}

	public long getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getDescription() {
		return description;
	}

	public List<StepDto> getEtapes() {
		return etapes;
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

	public Long getNbJobsWorkflow() {
		return nbJobsWorkflow;
	}

}
