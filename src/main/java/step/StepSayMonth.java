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
public class StepSayMonth extends Step {

	private ContextVar contextVariableName = null;
	private String scapeDigit;
	private String sayMonth;
	char scapeDigitTmp;

	public StepSayMonth(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayMonth;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {

			int mes = Integer.parseInt(contextVariableName.getVarValue());
			switch (mes) {
			case 1:
				sayMonth = "mes/enero";
				break;
			case 2:
				sayMonth = "mes/febrero";
				break;
			case 3:
				sayMonth = "mes/marzo";
				break;
			case 4:
				sayMonth = "mes/abril";
				break;
			case 5:
				sayMonth = "mes/mayo";
				break;
			case 6:
				sayMonth = "mes/junio";
				break;
			case 7:
				sayMonth = "mes/julio";
				break;
			case 8:
				sayMonth = "mes/agosto";
				break;
			case 9:
				sayMonth = "mes/septiembre";
				break;
			case 10:
				sayMonth = "mes/octubre";
				break;
			case 11:
				sayMonth = "mes/noviembre";
				break;
			case 12:
				sayMonth = "mes/diciembre";
				break;

			default:
				// scapeDigitTmp = ((CallContext) context).getChannel()
				// .streamFile("coto/error", scapeDigit);
				// contextVariableName.setVarValue(String.valueOf(scapeDigitTmp));
				break;
			}

			scapeDigitTmp = ((CallContext) context).getChannel().streamFile(
					sayMonth, scapeDigit);
			contextVariableName.setVarValue(String.valueOf(scapeDigitTmp));

		}
		return false;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

}
