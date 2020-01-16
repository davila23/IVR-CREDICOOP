/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import ivr.CallContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import a380.utils.SaldoCuenta;

import context.ContextVar;
import workflow.Context;

public class StepSayFiliales extends Step {

	private ContextVar listaDeFilialesContextVar;

	public StepSayFiliales(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayFiliales;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (listaDeFilialesContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {

			String filiales = ((ContextVar) context
					.get(listaDeFilialesContextVar.getId())).getVarValue();

			String filiales_aux[] = filiales.split("_");
			String scapeDigitTmp = "";

			for (String filial : filiales_aux) {
				if (!filial.isEmpty())

				scapeDigitTmp = String.valueOf(
							((CallContext) context).getChannel().streamFile(
								"PCT/P017", "1234567890#*")).trim();
				scapeDigitTmp = String.valueOf(
						((CallContext) context).getChannel().sayNumber(filial,
								"1234567890#*")).trim();
				scapeDigitTmp = String.valueOf(
						((CallContext) context).getChannel().streamFile(
								"PCT/S001", "1234567890#*")).trim();

				if (!scapeDigitTmp.isEmpty()) {
					break;
				}
			}
		}
		return false;
	}

	public void setListaDeFilialesContextVar(
			ContextVar listaDeFilialesContextVar) {
		this.listaDeFilialesContextVar = listaDeFilialesContextVar;
	}
}
