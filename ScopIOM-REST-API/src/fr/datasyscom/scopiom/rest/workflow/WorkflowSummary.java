package fr.datasyscom.scopiom.rest.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.StepWS;
import fr.datasyscom.scopiom.ws.pojo.WorkflowWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	public String name;
	public String description;

	public Jobs jobs;
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	public List<Step> steps;

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Jobs implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public long nbError;
		public long nbOk;
		public long nbRunning;
		public long nbWait;
		public long nbHold;
		public long nbTotal;
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Step implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public long id;
		public String description;
		public String queueName;
		public String status;
		public Jobs jobs;
		
		public Step() {
			// TODO Auto-generated constructor stub
		}

		public Step(StepWS step) {
			this.id = step.getId();
			this.description = step.getDescription();
			this.queueName = step.getQueueName();
			this.status = step.getQueueStatus().name();

			this.jobs = new Jobs();
			this.jobs.nbError = step.getNbJobsError();
			this.jobs.nbOk = step.getNbJobsOk();
			this.jobs.nbRunning = step.getNbJobsRunning();
			this.jobs.nbWait = step.getNbJobsWait();
			this.jobs.nbHold = step.getNbJobsHold();
			this.jobs.nbTotal = step.getNbJobsTotal();
		}
	}

	public WorkflowSummary() {
	}

	public WorkflowSummary(WorkflowWS wf) {
		this.id = wf.getId();
		this.name = wf.getNom();
		this.description = wf.getDescription();

		this.jobs = new Jobs();
		this.jobs.nbError = wf.getNbJobsError();
		this.jobs.nbOk = wf.getNbJobsOk();
		this.jobs.nbRunning = wf.getNbJobsRunning();
		this.jobs.nbWait = wf.getNbJobsWait();
		this.jobs.nbHold = wf.getNbJobsHold();
		this.jobs.nbTotal = wf.getNbJobsTotal();

		this.steps = new ArrayList<>();
		for (StepWS step : wf.getEtapes()) {
			steps.add(new Step(step));
		}
	}

}