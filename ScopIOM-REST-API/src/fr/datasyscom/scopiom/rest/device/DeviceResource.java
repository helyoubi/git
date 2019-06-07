package fr.datasyscom.scopiom.rest.device;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.datasyscom.pome.ejbentity.filter.DeviceFilter;
import fr.datasyscom.scopiom.ws.device.DeviceUILocal;
import fr.datasyscom.scopiom.ws.pojo.DeviceWS;

@Stateless(mappedName = "scopiom/ejb/stateless/DeviceResource")
public class DeviceResource implements DeviceResourceLocal, DeviceResourceRemote {

	@EJB
	private DeviceUILocal dmUi;

	public DevicesSummary devices(DeviceFilter filter) {
		List<DeviceWS> devices = dmUi.retrieveAllDevice(filter);
		return new DevicesSummary(devices);
	}
}