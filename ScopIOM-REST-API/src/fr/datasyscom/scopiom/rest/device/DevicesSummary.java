package fr.datasyscom.scopiom.rest.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.DeviceWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DevicesSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public int count;
	
	@XmlElementWrapper(name = "devices")
	@XmlElement(name = "device")
	public List<DeviceSummary> devices;

	public DevicesSummary() {
	}

	public DevicesSummary(List<DeviceWS> deviceWs) {
		this.count = deviceWs.size();
		this.devices = new ArrayList<DeviceSummary>(deviceWs.size());
		for (DeviceWS deviceWS : deviceWs) {
			devices.add(new DeviceSummary(deviceWS));
		}
	}

}
