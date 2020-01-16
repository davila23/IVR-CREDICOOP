/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package step;

import java.util.UUID;

import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */
public class StepTransfer extends Step {

	public StepTransfer(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Transfer;
	}

	public boolean execute(Context context) throws Exception {
		System.out.println("Transfer");
		return false;
	}

}
