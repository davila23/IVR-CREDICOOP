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

public class StepSetVariable extends Step {

	private String app = null;
	private String appOptions = null;
	private ContextVar contextVariableOrigen;
	private ContextVar contextVariableDestino;

	public StepSetVariable(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SetVariable;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableOrigen == null || contextVariableDestino == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableOrigen.getId())
				&& context.containsKey(contextVariableDestino.getId())) {
			ContextVar ctv = (ContextVar) context.get(contextVariableOrigen
					.getId());
			contextVariableDestino.setVarValue(ctv.getVarValue());
			context.put(contextVariableDestino.getId(), contextVariableDestino);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setContextVariableOrigen(ContextVar contextVariableOrigen) {
		this.contextVariableOrigen = contextVariableOrigen;
	}

	public void setContextVariableDestino(ContextVar contextVariableDestino) {
		this.contextVariableDestino = contextVariableDestino;
	}

}
