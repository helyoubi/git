package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import fr.datasyscom.pome.ejbentity.Device;
import fr.datasyscom.pome.ejbentity.DeviceProperty;
import fr.datasyscom.pome.ejbentity.interfaces.IId;

@XmlRootElement
public class DevicePropertyDto implements Serializable ,IId{

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String txt;

	private String description;
	
	//Ignore le field device lors de la sérialisation
	@XmlTransient
	private Device device;

	private Boolean display;

	private Boolean scriptExport;

	private Boolean mandatory;
	
	
	public DevicePropertyDto() {
		
	}

	public DevicePropertyDto(DeviceProperty deviceProperty) {
		this.id = deviceProperty.getId();
		this.name = deviceProperty.getName();
		this.txt = deviceProperty.getValue();
		this.description = deviceProperty.getDescription();
		this.device = deviceProperty.getDevice();
		this.display = deviceProperty.isDisplay();
		this.scriptExport = deviceProperty.isScriptExport();
		this.mandatory = deviceProperty.isMandatory();

	}
	

	// ******************************************************************************************************************
	// Setter et Getter
	// *****************************************************************************************************************/


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public Boolean isScriptExport() {
		return scriptExport;
	}

	public void setScriptExport(boolean scriptExport) {
		this.scriptExport = scriptExport;
	}

	public Boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	
	
}
