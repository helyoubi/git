package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ScanWaitDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer delayRun;
	private Integer delayScan;
	private String status;

	public ScanWaitDto() {
		super();
	}

	public Integer getDelayRun() {
		return delayRun;
	}

	public Integer getDelayScan() {
		return delayScan;
	}

	public void setDelayRun(Integer delayRun) {
		this.delayRun = delayRun;
	}

	public void setDelayScan(Integer delayScan) {
		this.delayScan = delayScan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
