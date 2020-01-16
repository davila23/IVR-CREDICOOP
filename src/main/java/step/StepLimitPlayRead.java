/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import context.ContextVar;
import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */
public class StepLimitPlayRead extends Step {

	private String playFile = null;
	private Long playTimeout = 5000L;
	private Integer playMaxDigits = 1;
	private ContextVar contextVariableName = null;
	private UUID nextStepIfAttemptLimit;
	private Integer intentos = 0;
	private int _int;

	public StepLimitPlayRead(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.LimitPlayRead;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (playFile == null || playFile.equals("")) {
			throw new Exception("INVALID PLAY FILE");
		}
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			if (intentos < _int) {
				contextVariableName.setVarValue(((CallContext) context)
						.getChannel().getData(playFile, playTimeout,
								playMaxDigits));
				context.put(contextVariableName.getId(), contextVariableName);
				this.setValor(contextVariableName.getVarValue());
				intentos++;
			} else {
				this.setNextstep(nextStepIfAttemptLimit);
			}
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}

		return false;
	}

	public void setPlayFile(String playFile) {
		this.playFile = playFile;
	}

	public void setPlayTimeout(Long playTimeout) {
		this.playTimeout = playTimeout;
	}

	public void setPlayMaxDigits(Integer playMaxDigits) {
		this.playMaxDigits = playMaxDigits;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	public UUID getNextStepIfAttemptLimit() {
		return nextStepIfAttemptLimit;
	}

	public void setNextStepIfAttemptLimit(UUID nextStepIfAttemptLimit) {
		this.nextStepIfAttemptLimit = nextStepIfAttemptLimit;
	}

	public int getIntentos() {
		return _int;
	}

	public void setIntentos(int _int) {
		this._int = _int;
	}

}
