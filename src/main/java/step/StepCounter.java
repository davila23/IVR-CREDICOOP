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

public class StepCounter extends Step {

	private ContextVar contextVariableName = null;

	public StepCounter(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Counter;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			ContextVar ctv = (ContextVar) context.get(contextVariableName
					.getId());
			int rta = Integer.parseInt((String) ctv.getVarValue()) + 1;
			contextVariableName.setVarValue(String.valueOf(rta));
			context.put(contextVariableName.getId(), contextVariableName);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
