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
public class StepSayNumber extends Step {

	private ContextVar contextVariableName = null;

	public StepSayNumber(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayNumber;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {
			((CallContext) context).getChannel().sayNumber(
					contextVariableName.getVarValue());
		}
		return false;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
