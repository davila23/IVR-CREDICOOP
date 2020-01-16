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
public class StepPlay extends Step {

	private String playfile = null;
	private String scapeDigit = null;
	private ContextVar contextVariableName = null;

	public StepPlay(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Play;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (playfile == null || playfile.equals("")) {
			throw new Exception("INVALID PLAYFILE");
		}

		if (scapeDigit == null || scapeDigit.equals("")) {
			((CallContext) context).getChannel().streamFile(playfile);
		} else {
			if (contextVariableName == null) {
				throw new Exception("INVALID CONTEXT VARIABLE");
			}
			char scapeDigitTmp = ((CallContext) context).getChannel()
					.streamFile(playfile, scapeDigit);
			contextVariableName.setVarValue(String.valueOf(scapeDigitTmp));
			context.put(contextVariableName.getId(), contextVariableName);
		}

		return false;

	}

	public void setPlayfile(String playfile) {
		this.playfile = playfile;
	}

	public void setScapeDigit(String scapeDigit) {
		this.scapeDigit = scapeDigit;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
