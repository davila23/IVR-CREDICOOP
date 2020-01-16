/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ivr;

import workflow.Task;
import workflow.Workflow;
import workflow.WorkflowFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Daniel Avila
 */
public class IvrFactory extends WorkflowFactory {

	public IvrFactory() {
	}

	public IvrFactory(Map tasks) {
		this.tasks = Collections.synchronizedMap(tasks);
	}

	private Workflow workflow = null;
	private Map workflows = new HashMap();
	protected Map tasks = Collections.synchronizedMap(new HashMap());

	@Override
	public Workflow getWorkflow() {
		return workflow;
	}

	@Override
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	@Override
	public Workflow getWorkflow(String name) {
		return ((Workflow) tasks.get(name));
	}

	@Override
	public void addWorkflow(String name, Workflow workflow) {
		tasks.put(name, workflow);
	}

	@Override
	public Iterator getNames() {
		return (tasks.keySet().iterator());
	}

	public void addTask(String name, Task task) {
		tasks.put(name, task);
	}

	@Override
	public Task getTask(String name) {
		return ((Task) tasks.get(name));
	}

	@Override
	public String toString() {

		Iterator names = getNames();
		StringBuffer str = new StringBuffer("[" + this.getClass().getName()
				+ ": ");

		while (names.hasNext()) {
			str.append(names.next());
			if (names.hasNext()) {
				str.append(", ");
			}
		}
		str.append("]");

		return str.toString();

	}
}
