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
public class StepSqlQuery extends Step {

	public StepSqlQuery(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SqlQuery;
	}

	public boolean execute(Context context) throws Exception {
		System.out.println("SqlQuery");
		return false;

	}
}
