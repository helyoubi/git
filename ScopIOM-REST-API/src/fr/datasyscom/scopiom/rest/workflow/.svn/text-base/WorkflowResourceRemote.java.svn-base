package fr.datasyscom.scopiom.rest.workflow;

import javax.ejb.Remote;

import fr.datasyscom.pome.ejbentity.filter.WorkflowFilter;

@Remote
public interface WorkflowResourceRemote {

	public WorkflowsSummary workflows(WorkflowFilter filter);

	WorkflowSummary workflow(String name);

	WorkflowUi workflowUi(String name);

}