package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.DeviceSnmp;

@XmlRootElement
public class DeviceSnmpDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Id du DeviceSnmp en base de données */
	private Long id;

	/** Commande d'interrogation */
	private String commandProcess;

	/** Delais entre 2 intérogation SNMP (en seconde) */
	private Integer delai;

	/** Indique si actif */
	private Boolean actif;

	/** Indique si on doit interroger le SNMP à chaque traitement */
	private Boolean beforeJob;

	private Boolean onlyOnError;

	public DeviceSnmpDto() {
	}

	public DeviceSnmpDto(DeviceSnmp deviceSnmp) {
		this.id = deviceSnmp.getId();
		this.beforeJob = deviceSnmp.isBeforeJob();
		this.commandProcess = deviceSnmp.getCommandProcess();
		this.delai = deviceSnmp.getDelais();
		this.actif = deviceSnmp.isActif();
		this.onlyOnError = deviceSnmp.isOnlyOnError();
	}

	public Long getId() {
		return id;
	}

	public String getCommandProcess() {
		return commandProcess;
	}

	public Integer getDelai() {
		return delai;
	}

	public Boolean isActif() {
		return actif;
	}

	public Boolean isOnlyOnError() {
		return onlyOnError;
	}

	public void setOnlyOnError(Boolean onlyOnError) {
		this.onlyOnError = onlyOnError;
	}

	public Boolean getBeforeJob() {
		return beforeJob;
	}

	public void setCommandProcess(String commandProcess) {
		this.commandProcess = commandProcess;
	}

	public void setDelai(Integer delai) {
		this.delai = delai;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public void setBeforeJob(Boolean beforeJob) {
		this.beforeJob = beforeJob;
	}

}
