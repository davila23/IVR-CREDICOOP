/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package condition;

import java.util.UUID;

/**
 * 
 * @author Daniel Avila
 */
public class condition {

	public condition(long id, String exp, UUID nextStepT, UUID nextStepF) {
		this.id = id;
		this.exp = exp;
		this.setNextStepIsTrue(nextStepT);
		this.setNextStepIsFalse(nextStepF);
	}

	private long id;
	private String exp;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UUID getNextStepIsTrue() {
		return nextStepIsTrue;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public UUID getNextStepIsFalse() {
		return nextStepIsFalse;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}
}
