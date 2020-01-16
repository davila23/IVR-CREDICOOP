/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import context.ContextVar;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */
public class StepResetCounter extends Step {

	private ContextVar contextVariableName = null;

	public StepResetCounter(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ResetCounter;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			context.put(contextVariableName.getId(), 0);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
