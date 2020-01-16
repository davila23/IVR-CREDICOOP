package step;

import ivr.CallContext;

import java.util.UUID;

import workflow.Context;

public class StepContinueOnDialPlan extends Step {

	public StepContinueOnDialPlan(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ContinueOnDialPlan;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		((CallContext) context).getChannel().verbose("Continuo en el dialplan",
				3);
		return true;
	}

}
