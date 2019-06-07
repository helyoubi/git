package fr.datasyscom.scopiom.rest.pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import fr.datasyscom.pome.ejbentity.Groupe;
import fr.datasyscom.pome.ejbentity.interfaces.IDescription;
import fr.datasyscom.pome.ejbentity.interfaces.IId;
import fr.datasyscom.pome.ejbentity.interfaces.IName;
import fr.datasyscom.pome.export.Exportable;

@XmlRootElement
public class GroupeDto implements Serializable, Exportable, IId, IName, IDescription {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Id du groupe en base de donn�es */
	private long id;

	/** Nom du groupe */
	private String name;

	/** Description du groupe */
	private String description;

	/** commentaire du groupe */
	private String comment;

	/** Indique si ce groupe peut etre supprim� */
	private Boolean mandatory;

	/** Indique si ce groupe peut soumettre des jobs */
	private Boolean canSubmitJobs;

	/** Indique si ce groupe peut gen�rer des rapports */
	private Boolean canGenerateReports;

	/**
	 * Indique si ce groupe peut acc�der aux liens d'un job (liens de d�pendance)
	 */
	private Boolean canAccessJobLinks;

	/** Indique si ce groupe peut acc�der � l'arbre d'ex�cution d'un job */
	private Boolean canAccessJobsTree;

	public GroupeDto() {

	}

	public GroupeDto(Groupe groupe) {
		this.id = groupe.getId();
		this.name = groupe.getName();
		this.description = groupe.getDescription();
		this.comment = groupe.getComment();
		this.mandatory = groupe.isMandatory();
		this.canSubmitJobs = groupe.isCanSubmitJobs();
		this.canGenerateReports = groupe.isCanGenerateReports();
		this.canAccessJobLinks = groupe.isCanAccessJobLinks();
		this.canAccessJobsTree = groupe.isCanAccessJobsTree();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean isCanSubmitJobs() {
		return canSubmitJobs;
	}

	public void setCanSubmitJobs(boolean canSubmitJobs) {
		this.canSubmitJobs = canSubmitJobs;
	}

	public Boolean isCanGenerateReports() {
		return canGenerateReports;
	}

	public void setCanGenerateReports(boolean canGenerateReports) {
		this.canGenerateReports = canGenerateReports;
	}

	public Boolean isCanAccessJobLinks() {
		return canAccessJobLinks;
	}

	public void setCanAccessJobLinks(boolean canAccessJobLinks) {
		this.canAccessJobLinks = canAccessJobLinks;
	}

	public Boolean isCanAccessJobsTree() {
		return canAccessJobsTree;
	}

	public void setCanAccessJobsTree(boolean canAccessJobsTree) {
		this.canAccessJobsTree = canAccessJobsTree;
	}

}
