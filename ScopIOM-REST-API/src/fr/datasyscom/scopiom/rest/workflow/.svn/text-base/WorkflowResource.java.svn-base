package fr.datasyscom.scopiom.rest.workflow;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.ejbentity.filter.WorkflowFilter;
import fr.datasyscom.pome.ejbsession.workflow.WorkflowManagerLocal;
import fr.datasyscom.scopiom.ws.pojo.WorkflowWS;
import fr.datasyscom.scopiom.ws.workflow.WorkflowUILocal;

@Stateless(mappedName = "scopiom/ejb/stateless/WorkflowResource")
public class WorkflowResource implements WorkflowResourceLocal, WorkflowResourceRemote {

	@EJB
	private WorkflowManagerLocal wfm;
	@EJB
	private WorkflowUILocal wfUi;

	public WorkflowsSummary workflows(WorkflowFilter filter) {
		List<WorkflowWS> wfs = wfUi.retrieveAllWorkflow(filter);
		return new WorkflowsSummary(wfs);
	}

	public WorkflowSummary workflow(String name) {
		WorkflowWS wf = wfUi.retrieveWorkflow(name);
		return new WorkflowSummary(wf);
	}

	public WorkflowUi workflowUi(String name) {
		Workflow wf = wfm.retrieveWF(name);
		return new WorkflowUi(wf);
	}
}