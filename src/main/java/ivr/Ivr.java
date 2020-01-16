package ivr;

import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import workflow.Workflow;
import workflow.Task;

public class Ivr implements Workflow {

	protected Map tasks = Collections.synchronizedMap(new HashMap());

	public Ivr() {
	}

	public Ivr(Map tasks) {
		this.tasks = Collections.synchronizedMap(tasks);
	}

	public void addTask(String name, Task task) {

		tasks.put(name, task);

	}

	public Task getTask(String name) {

		return ((Task) tasks.get(name));

	}

	public Iterator getNames() {

		return (tasks.keySet().iterator());

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
