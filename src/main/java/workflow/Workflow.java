package workflow;

import java.util.Iterator;

public interface Workflow {

	String WORKFLOW_KEY = "doivrservice.workflow.WORKFLOW";

	void addTask(String name, Task task);

	Task getTask(String name);

	Iterator getNames();

}
