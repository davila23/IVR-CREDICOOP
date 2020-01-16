package ivr;

import context.ContextVar;
import step.Step;
import utils.LogHandler;
import workflow.Task;
import workflow.Context;
import workflow.Flow;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Level;
import org.jboss.security.plugins.TmpFilePassword;

import main.Daemon;

public class CallFlow implements Flow {

	private UUID id;
	private String name;
	private UUID initialStep;
	private IvrExceptionHandler exHandler = new IvrExceptionHandler();
	private String extension;
	private Map<Long, ContextVar> CallFlowContextVars;
	protected Map<UUID, Task> tasks;

	public CallFlow() {
		this.tasks = new HashMap<UUID, Task>();
		this.CallFlowContextVars = new HashMap<Long, ContextVar>();
	}

	public CallFlow(Step task) {
		this.tasks = new HashMap<UUID, Task>();
		this.CallFlowContextVars = new HashMap<Long, ContextVar>();
		addTask(task);

	}

	public CallFlow(Step[] tasks) {
		this.tasks = new HashMap<UUID, Task>();
		this.CallFlowContextVars = new HashMap<Long, ContextVar>();

		if (tasks == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < tasks.length; i++) {
			addTask(tasks[i]);
		}

	}

	protected boolean frozen = false;

	public void addTask(Step task) {

		if (task == null) {
			throw new IllegalArgumentException();
		}
		if (frozen) {
			throw new IllegalStateException();
		}
		tasks.put(task.GetId(), task);
	}

	public void addContextVarTask(ContextVar cVar) {

		if (cVar == null) {
			throw new IllegalArgumentException();
		}
		if (frozen) {
			throw new IllegalStateException();
		}
		CallFlowContextVars.put(cVar.getId(), cVar);
	}

	public boolean execute(Context context) throws Exception {

		if (context == null) {
			throw new IllegalArgumentException();
		}

		frozen = true;

		boolean saveResult = false;
		Exception saveException = null;
		boolean handled = false;
		boolean result = false;

		// esto viene en el contexto
		UUID nextask = context.getInitialStep(); // setea inicial
		while (!saveResult) {
			try {

				Task tmpTask = tasks.get(nextask);
				((CallContext) context)
						.setCurrentStep(((Step) tmpTask).GetId());
				saveResult = tasks.get(nextask).execute(context);
				Daemon.getDbLog().addStepToLog(
						((CallContext) context).getChannel().getUniqueId(),
						tmpTask.getStepDescription(),
						((Step) tmpTask).getStepType().toString(),
						((CallContext) context).getRequest().getRemoteAddress()
								.getHostName(), ((Step) tmpTask).getValor(),
						((Step) tmpTask).GetId().toString(), new Date());
				nextask = tmpTask.getNextstep();
			
			} catch (Exception e) {
				saveException = e;
				Daemon.getMiLog().log(
						Level.ERROR,
						CallFlow.class.getName()
								+ "|"
								+ tasks.get(
										((CallContext) context)
												.getCurrentStep())
										.getStepDescription());
				break;
			}
		}

		if (exHandler != null) {
			try {
				result = exHandler.postprocess(context, saveException);
				if (result) {
					handled = true;
				}
			} catch (Exception e) {
			}
		}

		if ((saveException != null) && !handled) {
			throw saveException;
		} else {
			return (saveResult);
		}

	}

	Map<UUID, Task> getTasks() {

		return (tasks);

	}

	public UUID GetId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public UUID getNextstep() {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public void addTask(Task task) {
		if (task == null) {
			throw new IllegalArgumentException();
		}
		if (frozen) {
			throw new IllegalStateException();
		}
		tasks.put(task.GetId(), task);
	}

	public UUID getInitialStep() {
		return initialStep;
	}

	public void setInitialStep(UUID initialStep) {
		this.initialStep = initialStep;
	}

	@Override
	public void setStepDescription(String stepDescription) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStepDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
