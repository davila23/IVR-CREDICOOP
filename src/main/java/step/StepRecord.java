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
public class StepRecord extends Step {

	public StepRecord(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Record;
	}

	public boolean execute(Context context) throws Exception {
		System.out.println("Record");
		return false;
	}

}
