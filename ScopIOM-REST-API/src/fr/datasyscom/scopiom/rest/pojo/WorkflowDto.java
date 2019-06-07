package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;

import fr.datasyscom.pome.ejbentity.Workflow;

public class WorkflowDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	/** Nom du Workflow */
	private String nom;

	/** Description du Workflow */
	private String description;

	public WorkflowDto() {
		super();

	}

	public WorkflowDto(Workflow workflow) {
		this.id = workflow.getId();
		this.nom = workflow.getName();
		this.description = workflow.getDescription();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
