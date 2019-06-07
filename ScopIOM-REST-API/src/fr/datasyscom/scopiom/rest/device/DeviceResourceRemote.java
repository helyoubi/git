package fr.datasyscom.scopiom.rest.device;

import javax.ejb.Remote;

import fr.datasyscom.pome.ejbentity.filter.DeviceFilter;

@Remote
public interface DeviceResourceRemote {

	DevicesSummary devices(DeviceFilter filter);

}