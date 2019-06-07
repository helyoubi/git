package fr.datasyscom.scopiom.rest.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NbJobDevice extends AbstractNbJob {

	private static final long serialVersionUID = 1L;

	private Long idDevice;

	public Long getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(Long idDevice) {
		this.idDevice = idDevice;
	}
}
