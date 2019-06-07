package fr.datasyscom.scopiom.rest.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fr.datasyscom.pome.ejbentity.Condition;
import fr.datasyscom.pome.ejbentity.Routage;
import fr.datasyscom.pome.ejbentity.Step;
import fr.datasyscom.pome.ejbentity.Workflow;
import fr.datasyscom.pome.utile.WorkflowTraverser;
import fr.datasyscom.pome.utile.WorkflowTraverser.IWorkflowTraverseListener;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowUi implements Serializable {

	private static final long serialVersionUID = 1L;

	public String name;
	public String description;
	public double x;
	public double y;
	public double width;
	public double height;
	public long routageId;

	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	public List<WorkflowUiStep> steps = new ArrayList<>();
	@XmlElementWrapper(name = "routages")
	@XmlElement(name = "routage")
	public List<WorkflowUiRoutage> routages = new ArrayList<>();
	@XmlElementWrapper(name = "conditions")
	@XmlElement(name = "condition")
	public List<WorkflowUiCondition> conditions = new ArrayList<>();

	public WorkflowUi() {
	}

	public WorkflowUi(Workflow wf) {
		this.name = wf.getName();
		this.description = wf.getDescription();
		this.description = wf.getDescription();
		this.x = wf.getX();
		this.y = wf.getY();
		this.width = wf.getWidth();
		this.height = wf.getHeight();
		this.routageId = wf.getRoutage().getId();

		new WorkflowTraverser(wf).traverse(new IWorkflowTraverseListener() {

			private Set<Long> processedIds = new HashSet<>();

			public void workflow(Workflow workflow) {
			}

			public void routage(Routage routage) {
				if (!processedIds.contains(routage.getId())) {
					routages.add(new WorkflowUiRoutage(routage));
					processedIds.add(routage.getId());
				}
			}

			public void condition(Condition condition) {
				if (!processedIds.contains(condition.getId())) {
					conditions.add(new WorkflowUiCondition(condition));
					processedIds.add(condition.getId());
				}
			}

			public void step(Step step) {
				if (!processedIds.contains(step.getId())) {
					steps.add(new WorkflowUiStep(step));
					processedIds.add(step.getId());
				}
			}
		});
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WorkflowUiStep implements Serializable {

		private static final long serialVersionUID = 1L;

		public long id;
		public String description;
		public double x;
		public double y;
		public double width;
		public double height;
		public long routage;
		public long queueId;
		public String queueName;

		// queue: [id: wfStep.queue?.id, name: wfStep.queue?.name]
		
		public WorkflowUiStep() {
		}

		public WorkflowUiStep(Step step) {
			this.id = step.getId();
			this.description = step.getDescription();
			this.x = step.getX();
			this.y = step.getY();
			this.width = step.getWidth();
			this.height = step.getHeight();
			if (step.getNextRoutage() != null) {
				this.routage = step.getNextRoutage().getId();
			}
			this.queueId = step.getQueue().getId();
			this.queueName = step.getQueue().getName();
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WorkflowUiRoutage implements Serializable {

		private static final long serialVersionUID = 1L;

		public long id;
		public String description;
		public double x;
		public double y;
		public double width;
		public double height;
		
		public WorkflowUiRoutage() {
		}

		public WorkflowUiRoutage(Routage routage) {
			this.id = routage.getId();
			this.description = routage.getDescription();
			this.x = routage.getX();
			this.y = routage.getY();
			this.width = routage.getWidth();
			this.height = routage.getHeight();
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WorkflowUiCondition implements Serializable {

		private static final long serialVersionUID = 1L;

		public long id;
		public String description;
		public long routage;
		public long step;
		public int priority;
		public boolean alwaysTrue;
		public boolean stopIfMatch;
		public String userQuery;
		
		public WorkflowUiCondition() {
		}

		public WorkflowUiCondition(Condition condition) {
			this.id = condition.getId();
			this.description = condition.getDescription();
			this.routage = condition.getRoutageParent().getId();
			this.step = condition.getNextStep().getId();
			this.priority = condition.getPriority();
			this.alwaysTrue = condition.isAlwaysTrue();
			this.stopIfMatch = condition.isStopIfMatch();
			this.userQuery = condition.getUserQuery();
		}
	}

}
