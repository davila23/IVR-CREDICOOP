/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */

public class StepAnswer extends Step {

	public StepAnswer(UUID tmpid) {
		this.id = tmpid;
		this.StepType = step.StepFactory.StepType.Answer;

	}

	@Override
	public boolean execute(Context context) throws Exception {
		((CallContext) context).getChannel().answer();
		return false;

	}
}
