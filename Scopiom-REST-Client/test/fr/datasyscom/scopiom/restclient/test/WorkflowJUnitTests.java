package fr.datasyscom.scopiom.restclient.test;

import static org.junit.Assert.assertNotNull;

import fr.datasyscom.pome.exception.ValidationException;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.datasyscom.scopiom.rest.pojo.WorkflowDto;
import fr.datasyscom.scopiom.restclient.exception.RestException;
import fr.datasyscom.scopiom.restclient.job.JobClient.JobStatusType;
import fr.datasyscom.scopiom.restclient.server.ScopIOMServer;
import fr.datasyscom.scopiom.restclient.workflow.WorkflowClient;

public class WorkflowJUnitTests {
	//for tests 
	private final String NEW_GROUPENOTFOUND_NAME = "GROUPETEST-" + System.currentTimeMillis();
	private final String NEW_WORKFLOWNOTFOUND_NAME = "WORKFLOW-" + System.currentTimeMillis();

	private static ScopIOMServer serveur;

	@BeforeClass
	public static void init() {
		serveur = new ScopIOMServer("http://localhost:4848/scopiom/rest/", "iomadmin", "iomadmin");
	}

	@Test(expected = RestException.class)
	public void testWorkflow_NotFound_ById() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.byId(-System.currentTimeMillis());
	}

	@Test(expected = RestException.class)
	public void testWorkflow_NotFound_ByName() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.byName(NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflow_Empty_ByName() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.byName("");
	}

	@Test(expected = RestException.class)
	public void testWorkflow_NotFound_For_Delete() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.delete(NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowName_Empty_For_Delete() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.delete("");
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowNameAndGroupe_Empty_For_DeleteGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.deleteGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testWorkflowNameAndGroupe_NotFound_For_DeleteGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.deleteGroup(NEW_WORKFLOWNOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowNameAndGroup_Empty_For_AddGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.addGroup("", "");
	}

	@Test(expected = RestException.class)
	public void testWorkflowNameAndGroup_NotFound_For_AddGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.addGroup(NEW_WORKFLOWNOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}
	
	@Test(expected = ValidationException.class)
	public void testWorkflowNameAndGroup_Empty_For_RemoveGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.removeGroup("", "");
	}
	
	@Test(expected = RestException.class)
	public void testWorkflowNameAndGroup_NotFound_For_RemoveGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.removeGroup(NEW_WORKFLOWNOTFOUND_NAME, NEW_GROUPENOTFOUND_NAME);
	}
	
	@Test(expected = RestException.class)
	public void testWorkflowNameAndGroup_NotFound_For_RetrieveGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.retrieveGroup(NEW_WORKFLOWNOTFOUND_NAME);
	}
	
	@Test(expected = ValidationException.class)
	public void testWorkflowNameAndGroup_Empty_For_RetrieveGroup() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.retrieveGroup("");
	}

	@Test(expected = RestException.class)
	public void testWorkflowName_NotFound_For_RetrieveAllJobs() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.allJobs(NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowName_Empty_For_RetrieveAllJobs() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.allJobs("");
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowName_Empty_For_RetrieveJobsByStatus() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.jobsByStatus("", JobStatusType.OK);
	}

	@Test(expected = RestException.class)
	public void testWorkflowName_NotFound_For_RetrieveJobsByStatus() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.jobsByStatus(NEW_WORKFLOWNOTFOUND_NAME, JobStatusType.OK);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowName_Empty_For_RetrieveGroups() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.groups("");
	}

	@Test(expected = RestException.class)
	public void testWorkflowName_NotFound_For_RetrieveGroups() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		workflowClient.groups(NEW_WORKFLOWNOTFOUND_NAME);
	}

	@Test(expected = ValidationException.class)
	public void testWorkflowName_Empty_For_Update() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		WorkflowDto workflowDto = new WorkflowDto();
		workflowDto.setNom("");
		workflowClient.update(workflowDto);
	}

	@Test(expected = RestException.class)
	public void testWorkflowName_NotFound_For_Update() throws ValidationException, RestException {
		WorkflowClient workflowClient = serveur.workflows();
		WorkflowDto workflowDto = new WorkflowDto();
		workflowDto.setNom(NEW_WORKFLOWNOTFOUND_NAME);
		workflowClient.update(workflowDto);
	}

	@Test
	public void testWorkflow_List() throws RestException {
		WorkflowClient workflowClient = serveur.workflows();
		assertNotNull("Erreur récupération de la liste", workflowClient.all());
	}

}
