package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurgeTmpDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer purgeDelay;
	private Integer purgeOld;
	private String tmpFolder;
	private String status;

	public PurgeTmpDto() {
		super();
	}

	public Integer getPurgeDelay() {
		return purgeDelay;
	}

	public Integer getPurgeOld() {
		return purgeOld;
	}

	public String getTmpFolder() {
		return tmpFolder;
	}

	public void setPurgeDelay(Integer purgeDelay) {
		this.purgeDelay = purgeDelay;
	}

	public void setPurgeOld(Integer purgeOld) {
		this.purgeOld = purgeOld;
	}

	public void setTmpFolder(String tmpFolder) {
		this.tmpFolder = tmpFolder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}
