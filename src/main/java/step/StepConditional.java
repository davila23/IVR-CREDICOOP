/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import condition.condition;
import context.ContextVar;
import workflow.Context;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.Evaluator;

/**
 * 
 * @author Daniel Avila
 */

public class StepConditional extends Step {

	private HashMap<Long, condition> conditions = new HashMap<Long, condition>();

	public StepConditional(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Conditional;
	}

	public boolean execute(Context context) throws Exception {
		Evaluator evaluator = new Evaluator();

		for (Object ctxVar : context.values()) {
			if (ctxVar instanceof ContextVar) {
				evaluator.putVariable(((ContextVar) ctxVar).getVarName(),
						((ContextVar) ctxVar).getVarValue());
			}
		}
		for (condition con : this.conditions.values()) {
			String result = evaluator.evaluate(con.getExp());
			if (result.equals(EvaluationConstants.BOOLEAN_STRING_TRUE)) {
				this.setNextstep(con.getNextStepIsTrue());
				break;
			} else {
				this.setNextstep(con.getNextStepIsFalse());
			}
		}

		return false;
	}

	public HashMap<Long, condition> getConditions() {
		return conditions;
	}

	public void setConditions(HashMap<Long, condition> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(condition condition) {
		this.conditions.put(condition.getId(), condition);
	}
}
