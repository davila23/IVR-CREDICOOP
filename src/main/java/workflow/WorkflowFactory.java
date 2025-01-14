package workflow;

import ivr.IvrFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class WorkflowFactory {

	public static final String DELIMITER = ":";

	// --------------------------------------------------------- Public Methods

	public abstract Workflow getWorkflow();

	public abstract void setWorkflow(Workflow workflow);

	public abstract Workflow getWorkflow(String name);

	public abstract void addWorkflow(String name, Workflow workflow);

	public abstract Iterator getNames();

	public Task getTask(String taskId) {

		String taskName = taskId;
		String workflowName = null;
		Workflow workflow = null;

		if (taskId != null) {
			int splitPos = taskId.indexOf(DELIMITER);
			if (splitPos != -1) {
				workflowName = taskId.substring(0, splitPos);
				taskName = taskId.substring(splitPos + DELIMITER.length());
				if (taskName.indexOf(DELIMITER) != -1) {
					throw new IllegalArgumentException(
							"taskId ["
									+ taskId
									+ "] has too many delimiters (reserved for future use)");
				}
			}
		}

		if (workflowName != null) {
			workflow = this.getWorkflow(workflowName);
			if (workflow == null) {
				Logger.getLogger(WorkflowFactory.class.getName()).log(
						Level.WARN,
						"No workflow found for name: " + workflowName + ".");
				return null;
			}
		} else {
			workflow = this.getWorkflow();
			if (workflow == null) {
				Logger.getLogger(WorkflowFactory.class.getName()).log(
						Level.WARN, "No default workflow found.");
				return null;
			}
		}

		return workflow.getTask(taskName);

	}

	// ------------------------------------------------------- Static Variables

	private static Map factories = new HashMap();

	// -------------------------------------------------------- Static Methods

	public static WorkflowFactory getInstance() {

		WorkflowFactory factory = null;
		ClassLoader cl = getClassLoader();
		synchronized (factories) {
			factory = (WorkflowFactory) factories.get(cl);
			if (factory == null) {
				factory = new IvrFactory();
				factories.put(cl, factory);
			}
		}
		return factory;

	}

	public static void clear() {

		synchronized (factories) {
			factories.remove(getClassLoader());
		}

	}

	// ------------------------------------------------------- Private Methods

	private static ClassLoader getClassLoader() {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = WorkflowFactory.class.getClassLoader();
		}
		return cl;

	}

}
