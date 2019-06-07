package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Agency;
import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.DeviceRoutage;
import fr.datasyscom.pome.ejbentity.User;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class DeviceRoutageDto implements Serializable, IId {

	private static final long serialVersionUID = 1L;

	/** Id du device en base de données */
	private long id;
	// Primary Device
	private Long primaryDeviceId;
	private String primaryDeviceName;
	private String primaryDeviceDescription;
	// Secondary Device
	private Long secondaryDeviceId;
	private String secondaryDeviceName;
	private String secondaryDeviceDescription;
	// Agency
	private Long agencyId;
	private String agencyName;
	private String agencyDescription;
	// User
	private String user;
	// Nbr copy
	private Integer nbrCopy;
	// Nbr copy
	private String jobName;

	public static List<DeviceRoutageDto> fromList(List<DeviceRoutage> deviceRoutageList) {
		List<DeviceRoutageDto> res = new ArrayList<DeviceRoutageDto>(deviceRoutageList.size());
		for (DeviceRoutage deviceRoutage : deviceRoutageList) {
			res.add(new DeviceRoutageDto(deviceRoutage));
		}

		return res;
	}

	public DeviceRoutageDto() {
	}

	public DeviceRoutageDto(DeviceRoutage deviceRoutage) {
		this.id = deviceRoutage.getId();
		this.nbrCopy = deviceRoutage.getNbrCopy();
		this.jobName = deviceRoutage.getJobName();
		Device primaryDevice = deviceRoutage.getPrimaryDevice();
		if (primaryDevice != null) {
			this.primaryDeviceId = primaryDevice.getId();
			this.primaryDeviceName = primaryDevice.getName();
			this.primaryDeviceDescription = primaryDevice.getDescription();
		}
		Device secondaryDevice = deviceRoutage.getSecondaryDevice();
		if (secondaryDevice != null) {
			this.secondaryDeviceId = secondaryDevice.getId();
			this.secondaryDeviceName = secondaryDevice.getName();
			this.secondaryDeviceDescription = secondaryDevice.getDescription();
		}
		Agency agencyDevice = deviceRoutage.getAgency();
		if (agencyDevice != null) {
			this.agencyId = agencyDevice.getId();
			this.agencyName = agencyDevice.getName();
			this.agencyDescription = agencyDevice.getDescription();
		}
		User user = deviceRoutage.getUser();
		if (user != null) {
			this.user = user.getLogin();
		}
	}

	public boolean hasPrimaryDevice() {
		return (primaryDeviceId != null);
	}

	public boolean hasSecondaryDevice() {
		return (secondaryDeviceId != null);
	}

	public boolean hasAgency() {
		return (agencyId != null);
	}

	public boolean hasUser() {
		return (user != null);
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getPrimaryDeviceId() {
		return primaryDeviceId;
	}

	public void setPrimaryDeviceId(Long primaryDeviceId) {
		this.primaryDeviceId = primaryDeviceId;
	}

	public String getPrimaryDeviceName() {
		return primaryDeviceName;
	}

	public void setPrimaryDeviceName(String primaryDeviceName) {
		this.primaryDeviceName = primaryDeviceName;
	}

	public String getPrimaryDeviceDescription() {
		return primaryDeviceDescription;
	}

	public void setPrimaryDeviceDescription(String primaryDeviceDescription) {
		this.primaryDeviceDescription = primaryDeviceDescription;
	}

	public Long getSecondaryDeviceId() {
		return secondaryDeviceId;
	}

	public void setSecondaryDeviceId(Long secondaryDeviceId) {
		this.secondaryDeviceId = secondaryDeviceId;
	}

	public String getSecondaryDeviceName() {
		return secondaryDeviceName;
	}

	public void setSecondaryDeviceName(String secondaryDeviceName) {
		this.secondaryDeviceName = secondaryDeviceName;
	}

	public String getSecondaryDeviceDescription() {
		return secondaryDeviceDescription;
	}

	public void setSecondaryDeviceDescription(String secondaryDeviceDescription) {
		this.secondaryDeviceDescription = secondaryDeviceDescription;
	}

	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyDescription() {
		return agencyDescription;
	}

	public void setAgencyDescription(String agencyDescription) {
		this.agencyDescription = agencyDescription;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getNbrCopy() {
		return nbrCopy;
	}

	public void setNbrCopy(Integer nbrCopy) {
		this.nbrCopy = nbrCopy;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

}
