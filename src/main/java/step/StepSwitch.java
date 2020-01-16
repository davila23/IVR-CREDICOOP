/**
 * 
 */
package step;

import java.util.HashMap;
import java.util.UUID;

import context.ContextVar;

import workflow.Context;

/**
 * @author davila
 * 
 */
public class StepSwitch extends Step {

	private HashMap<String, UUID> switchValues = new HashMap<String, UUID>();
	private ContextVar contextVariableName = null;

	public StepSwitch(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Switch;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			if (switchValues.containsKey(contextVariableName.getVarValue())) {
				this.setNextstep(switchValues.get(contextVariableName
						.getVarValue()));
			}
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
	}

	public void addSwitchValue(String value, UUID nxtStep) {
		this.switchValues.put(value, nxtStep);
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
