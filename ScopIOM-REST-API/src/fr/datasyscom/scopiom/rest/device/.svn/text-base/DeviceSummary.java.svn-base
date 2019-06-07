package fr.datasyscom.scopiom.rest.device;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.DeviceWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	public String name;
	public String description;
	public String comment;
	public String status;
	public String statusDesc;

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

	public DeviceSummary() {
	}

	public DeviceSummary(DeviceWS device) {
		this.id = device.getId();
		this.name = device.getName();
		this.description = device.getDescription();
		this.comment = device.getComment();
		this.status = device.getStatus();
		this.statusDesc = device.getStatusDesc();

		this.jobs = new Jobs();
		this.jobs.nbError = device.getNbJobsError();
		this.jobs.nbOk = device.getNbJobsOk();
		this.jobs.nbRunning = device.getNbJobsRunning();
		this.jobs.nbWait = device.getNbJobsWait();
		this.jobs.nbHold = device.getNbJobsHold();
		this.jobs.nbTotal = device.getNbJobsTotal();
	}

}