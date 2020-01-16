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
public class StepSayYear extends Step {

	private ContextVar contextVariableName = null;
	private String scapeDigit;
	private String sayMonth;
	char scapeDigitTmp;

	public StepSayYear(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayYear;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {

			int anio = Integer.parseInt(contextVariableName.getVarValue());

			if (anio > 16) {
				scapeDigitTmp = ((CallContext) context).getChannel()
						.streamFile("anio/1900", scapeDigit);
				((CallContext) context).getChannel().sayNumber(
						contextVariableName.getVarValue());
				contextVariableName.setVarValue(String.valueOf(scapeDigitTmp));
			} else {
				if (anio == 00) {
					scapeDigitTmp = ((CallContext) context).getChannel()
							.streamFile("anio/2000", scapeDigit);
					contextVariableName.setVarValue(String
							.valueOf(scapeDigitTmp));
				} else {
					scapeDigitTmp = ((CallContext) context).getChannel()
							.streamFile("anio/2000", scapeDigit);
					((CallContext) context).getChannel().sayNumber(
							contextVariableName.getVarValue());
					contextVariableName.setVarValue(String
							.valueOf(scapeDigitTmp));
				}
			}
		}

		return false;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
