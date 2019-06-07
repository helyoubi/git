package fr.datasyscom.scopiom.rest.appinfos;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbsession.server.info.ServerInfoManagerLocal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AppInfos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String version;
	public boolean clustered;
	public String instanceName;

	public AppInfos() {
	}

	public AppInfos(ServerInfoManagerLocal si) {
		this.version = si.getVersion();
		this.clustered = si.isClustered();
		this.instanceName = si.getInstanceName();
	}
}