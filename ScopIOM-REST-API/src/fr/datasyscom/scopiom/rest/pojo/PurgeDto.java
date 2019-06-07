package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurgeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer hour_purge;
	private Integer minute_purge;
	private Long nbrJobToPurge;
	private Boolean purgeOrphelin;
	private String status;

	public PurgeDto() {
		super();

	}

	public Integer getHour_purge() {
		return hour_purge;
	}

	public Integer getMinute_purge() {
		return minute_purge;
	}

	public Long getNbrJobToPurge() {
		return nbrJobToPurge;
	}

	public Boolean isPurgeOrphelin() {
		return purgeOrphelin;
	}

	public void setHour_purge(Integer hour_purge) {
		this.hour_purge = hour_purge;
	}

	public void setMinute_purge(Integer minute_purge) {
		this.minute_purge = minute_purge;
	}

	public void setNbrJobToPurge(Long nbrJobToPurge) {
		this.nbrJobToPurge = nbrJobToPurge;
	}

	public void setPurgeOrphelin(Boolean purgeOrphelin) {
		this.purgeOrphelin = purgeOrphelin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
