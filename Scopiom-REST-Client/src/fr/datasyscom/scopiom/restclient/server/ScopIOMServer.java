package fr.datasyscom.scopiom.restclient.server;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import fr.datasyscom.scopiom.restclient.agency.AgencyClient;
import fr.datasyscom.scopiom.restclient.agent.AgentClient;
import fr.datasyscom.scopiom.restclient.calendar.CalendarClient;
import fr.datasyscom.scopiom.restclient.device.DeviceClient;
import fr.datasyscom.scopiom.restclient.groupe.GroupeClient;
import fr.datasyscom.scopiom.restclient.info.InfoClient;
import fr.datasyscom.scopiom.restclient.job.JobClient;
import fr.datasyscom.scopiom.restclient.mailSend.MailConfigClient;
import fr.datasyscom.scopiom.restclient.media.MediaClient;
import fr.datasyscom.scopiom.restclient.passwordmanagement.PasswordManagementClient;
import fr.datasyscom.scopiom.restclient.property.PropertyClient;
import fr.datasyscom.scopiom.restclient.queue.QueueClient;
import fr.datasyscom.scopiom.restclient.resourcegroup.ResourceGroupClient;
import fr.datasyscom.scopiom.restclient.service.ServiceClient;
import fr.datasyscom.scopiom.restclient.tableexploit.TableExploitClient;
import fr.datasyscom.scopiom.restclient.type.TypeClient;
import fr.datasyscom.scopiom.restclient.user.UserClient;
import fr.datasyscom.scopiom.restclient.workflow.WorkflowClient;

public class ScopIOMServer {

	WebResource baseWebRessource;

	public ScopIOMServer(String url, String login, String pass) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(clientConfig);
		baseWebRessource = client.resource(url);
		client.addFilter(new HTTPBasicAuthFilter(login, pass));
	}

	public MediaClient medias() {
		return new MediaClient(baseWebRessource);
	}

	public PropertyClient properties() {
		return new PropertyClient(baseWebRessource);
	}

	public AgentClient agents() {
		return new AgentClient(baseWebRessource);
	}

	public AgencyClient agency() {
		return new AgencyClient(baseWebRessource);
	}

	public TableExploitClient tableExploit() {
		return new TableExploitClient(baseWebRessource);
	}

	public MailConfigClient mailConfig() {
		return new MailConfigClient(baseWebRessource);
	}

	public InfoClient app() {
		return new InfoClient(baseWebRessource);
	}

	public InfoClient heatlh() {
		return new InfoClient(baseWebRessource);
	}

	public PasswordManagementClient passwordManagement() {
		return new PasswordManagementClient(baseWebRessource);
	}

	public WorkflowClient workflows() {
		return new WorkflowClient(baseWebRessource);
	}

	public UserClient users() {
		return new UserClient(baseWebRessource);
	}

	public DeviceClient devices() {
		return new DeviceClient(baseWebRessource);
	}

	public ResourceGroupClient resourcesGroups() {
		return new ResourceGroupClient(baseWebRessource);
	}

	public GroupeClient groups() {
		return new GroupeClient(baseWebRessource);
	}

	public CalendarClient calendars() {
		return new CalendarClient(baseWebRessource);
	}

	public TypeClient types() {
		return new TypeClient(baseWebRessource);
	}
 
	public QueueClient queues() {
		return new QueueClient(baseWebRessource);
	}
	
	public ServiceClient services() {
		return new ServiceClient(baseWebRessource);
	}
	
	public JobClient jobs() {
		return new JobClient(baseWebRessource);
	}
	
}
