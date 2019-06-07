package fr.datasyscom.scopiom.rest.services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbsession.lpd.LpdManagerLocal;
import fr.datasyscom.pome.ejbsession.services.purge.jobs.PurgeLocal;
import fr.datasyscom.pome.ejbsession.services.purge.tmp.PurgeTmpLocal;
import fr.datasyscom.pome.ejbsession.services.scanwait.ScanWaitLocal;

@XmlRootElement(name = "services")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServicesInfos implements Serializable {

	private static final long serialVersionUID = 1L;

	public ScanWaitServiceInfos scheduler;
	public PurgeJobsServiceInfos purgeJobs;
	public PurgeTmpServiceInfos purgeTmp;
	public LpdServiceInfos lpd;

	public ServicesInfos() {
	}

	public ServicesInfos(ScanWaitLocal scanWait, PurgeLocal purgeJobs,
			PurgeTmpLocal purgeTmp, LpdManagerLocal lpd) {
		this.scheduler = new ScanWaitServiceInfos(scanWait);
		this.purgeJobs = new PurgeJobsServiceInfos(purgeJobs);
		this.purgeTmp = new PurgeTmpServiceInfos(purgeTmp);
		this.lpd = new LpdServiceInfos(lpd);
	}
}
