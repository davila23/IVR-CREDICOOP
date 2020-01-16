/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import step.StepFactory.StepType;
import workflow.Task;

/**
 * 
 * @author Daniel Avila
 */

public abstract class Step implements Task {

	protected StepFactory.StepType StepType;
	protected UUID nextstep;
	protected UUID id;
	protected String stepDecription = this.getClass().getName();
	protected String valor = "";

	
	public UUID GetId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getNextstep() {
		return nextstep;
	}

	public void setNextstep(UUID nextstep) {
		this.nextstep = nextstep;
	}

	public StepType getStepType() {
		return StepType;
	}

	public void setStepType(StepType StepType) {
		this.StepType = StepType;
	}

	public void setStepDescription(String _stepDecription) {
		this.stepDecription = _stepDecription;
	}

	public String getStepDescription() {
		return this.stepDecription;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
