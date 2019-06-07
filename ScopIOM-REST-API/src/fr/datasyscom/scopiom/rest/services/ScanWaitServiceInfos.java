package fr.datasyscom.scopiom.rest.services;

import java.io.Serializable;
import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.quartz.SchedulerException;

import fr.datasyscom.pome.ejbsession.services.scanwait.ScanWaitLocal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanWaitServiceInfos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String status;

	public ScanWaitServiceInfos() {
	}

	public ScanWaitServiceInfos(ScanWaitLocal scanWait) {
		try {
			this.status = scanWait.isStarted() ? "started" : "stopped";
		} catch (SchedulerException | ParseException e) {
			this.status = "unknown";
		}
	}
}
