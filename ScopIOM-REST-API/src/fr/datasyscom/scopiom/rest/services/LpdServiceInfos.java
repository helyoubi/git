package fr.datasyscom.scopiom.rest.services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbsession.lpd.LpdManagerLocal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LpdServiceInfos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String status;

	public LpdServiceInfos() {
	}

	public LpdServiceInfos(LpdManagerLocal lpd) {
		this.status = lpd.isStarted() ? "started" : "stopped";
	}
}