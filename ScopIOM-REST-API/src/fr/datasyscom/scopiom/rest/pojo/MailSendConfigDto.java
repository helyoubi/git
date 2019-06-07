package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.datasyscom.scopiom.rest.moxy.MapStringAdapter;

@XmlRootElement
public class MailSendConfigDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlJavaTypeAdapter(MapStringAdapter.class)
	private Map<String, String> advanceParam;

	private String host;
	private Integer port;
	private Boolean useAuthentification;
	private String user;
	private String password;
	private String transmitterAdress;
	private String transmitterName;
	private Boolean useTLS;
	private Boolean useSSL;

	public MailSendConfigDto() {
		super();
		this.advanceParam = new HashMap<String, String>();
	}

	public MailSendConfigDto(Properties properties) {
		this.advanceParam = new HashMap<String, String>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey().equals("mail.smtp.host")) {
				host = String.valueOf(entry.getValue());
			} else if (entry.getKey().equals("mail.smtp.port")) {
				port = Integer.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.auth")) {
				useAuthentification = Boolean.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.user")) {
				user = String.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.password")) {
				password = String.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.from")) {
				transmitterAdress = String.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.from.display")) {
				transmitterName = String.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.starttls.enable")) {
				useTLS = Boolean.valueOf(entry.getValue().toString());
			} else if (entry.getKey().equals("mail.smtp.ssl.enable")) {
				useSSL = Boolean.valueOf(entry.getValue().toString());
			} else {
				this.advanceParam.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	}

	public Properties computeProperties() {
		Properties properties = new Properties();
		for (Entry<String, String> entry : this.advanceParam.entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		if (host != null) {
			properties.setProperty("mail.smtp.host", host);
		}
		if (port != null) {
			properties.setProperty("mail.smtp.port", port.toString());
		}
		if (useAuthentification != null) {
			properties.setProperty("mail.smtp.auth", useAuthentification.toString());
		}
		if (user != null) {
			properties.setProperty("mail.smtp.user", user);
		}
		if (password != null) {
			properties.setProperty("mail.smtp.password", password);
		}
		if (transmitterAdress != null) {
			properties.setProperty("mail.smtp.from", transmitterAdress);
		}
		if (transmitterName != null) {
			properties.setProperty("mail.smtp.from.display", transmitterName);
		}
		if (useTLS != null) {
			properties.setProperty("mail.smtp.starttls.enable", useTLS.toString());
		}
		if (useSSL != null) {
			properties.setProperty("mail.smtp.ssl.enable", useSSL.toString());
		}

		return properties;
	}

	public Map<String, String> getAdvanceParam() {
		return advanceParam;
	}

	public void setAdvanceParam(Map<String, String> advanceParam) {
		this.advanceParam = advanceParam;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getUseAuthentification() {
		return useAuthentification;
	}

	public void setUseAuthentification(Boolean useAuthentification) {
		this.useAuthentification = useAuthentification;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTransmitterAdress() {
		return transmitterAdress;
	}

	public void setTransmitterAdress(String transmitterAdress) {
		this.transmitterAdress = transmitterAdress;
	}

	public String getTransmitterName() {
		return transmitterName;
	}

	public void setTransmitterName(String transmitterName) {
		this.transmitterName = transmitterName;
	}

	public Boolean getUseTLS() {
		return useTLS;
	}

	public void setUseTLS(Boolean useTLS) {
		this.useTLS = useTLS;
	}

	public Boolean getUseSSL() {
		return useSSL;
	}

	public void setUseSSL(Boolean useSSL) {
		this.useSSL = useSSL;
	}

}
