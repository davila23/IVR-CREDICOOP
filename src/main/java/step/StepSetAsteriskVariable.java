/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import context.ContextVar;
import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */
public class StepSetAsteriskVariable extends Step {

	private String variableName = null;
	private ContextVar contextVariableName = null;

	public StepSetAsteriskVariable(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SetAsteriskVariable;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (variableName == null || variableName.equals("")) {
			throw new Exception("INVALID VARIABLENAME");
		}
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			((CallContext) context).getChannel().setVariable(
					variableName,
					((ContextVar) context.get(contextVariableName.getId()))
							.getVarValue());
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}
}
