package fr.datasyscom.scopiom.rest.services;

import java.io.Serializable;
import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.quartz.SchedulerException;

import fr.datasyscom.pome.ejbsession.services.purge.jobs.PurgeLocal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PurgeJobsServiceInfos implements Serializable {

	private static final long serialVersionUID = 1L;

	public String status;

	public PurgeJobsServiceInfos() {
	}

	public PurgeJobsServiceInfos(PurgeLocal purge) {
		try {
			this.status = purge.isStarted() ? "started" : "stopped";
		} catch (SchedulerException | ParseException e) {
			this.status = "unknown";
		}
	}
}