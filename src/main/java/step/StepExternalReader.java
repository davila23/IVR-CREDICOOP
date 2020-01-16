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

public class StepExternalReader extends Step {

	public StepExternalReader(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ExternalReader;
	}

	public boolean execute(Context context) throws Exception {
		System.out.println("End");
		return false;

	}
}
