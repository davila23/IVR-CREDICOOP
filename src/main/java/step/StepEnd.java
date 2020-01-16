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

public class StepEnd extends Step {

	public StepEnd(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.End;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		
		((CallContext) context).getChannel().hangup();

		return true;

	}
}
