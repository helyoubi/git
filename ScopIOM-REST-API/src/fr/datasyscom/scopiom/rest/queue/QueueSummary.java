package fr.datasyscom.scopiom.rest.queue;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.QueueWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QueueSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	public String name;
	public String description;
	public String status;

	public Jobs jobs;

	public static class Jobs implements Serializable {

		private static final long serialVersionUID = 1L;

		public long nbError;
		public long nbOk;
		public long nbRunning;
		public long nbWait;
		public long nbHold;
		public long nbTotal;
	}

	public QueueSummary() {
	}

	public QueueSummary(QueueWS queue) {
		this.id = queue.getId();
		this.name = queue.getName();
		this.description = queue.getDescription();
		this.status = queue.getStatus();

		this.jobs = new Jobs();
		this.jobs.nbError = queue.getNbJobsError();
		this.jobs.nbOk = queue.getNbJobsOk();
		this.jobs.nbRunning = queue.getNbJobsRunning();
		this.jobs.nbWait = queue.getNbJobsWait();
		this.jobs.nbHold = queue.getNbJobsHold();
		this.jobs.nbTotal = queue.getNbJobsTotal();
	}

}