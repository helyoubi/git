package fr.datasyscom.scopiom.rest.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.scopiom.ws.pojo.WorkflowWS;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowsSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	public int count;
	
	@XmlElementWrapper(name = "workflows")
	@XmlElement(name = "workflow")
	public List<WorkflowSummary> workflows;

	public WorkflowsSummary() {
	}

	public WorkflowsSummary(List<WorkflowWS> wfs) {
		this.count = wfs.size();
		this.workflows = new ArrayList<WorkflowSummary>(wfs.size());
		for (WorkflowWS workflowWS : wfs) {
			workflows.add(new WorkflowSummary(workflowWS));
		}
	}

}
