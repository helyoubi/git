package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class AbstractNbJob implements Serializable {

	private static final long serialVersionUID = 1L;

	private int nbError;
	private int nbOk;
	private int nbWait;
	private int nbHold;
	private int nbRunning;

	public int getNbError() {
		return nbError;
	}

	public void setNbError(int nbError) {
		this.nbError = nbError;
	}

	public int getNbOk() {
		return nbOk;
	}

	public void setNbOk(int nbOk) {
		this.nbOk = nbOk;
	}

	public int getNbWait() {
		return nbWait;
	}

	public void setNbWait(int nbWait) {
		this.nbWait = nbWait;
	}

	public int getNbHold() {
		return nbHold;
	}

	public void setNbHold(int nbHold) {
		this.nbHold = nbHold;
	}

	public int getNbRunning() {
		return nbRunning;
	}

	public void setNbRunning(int nbRunning) {
		this.nbRunning = nbRunning;
	}

}
