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

public class StepSayDefaultAccountBalance extends Step {

	private ContextVar cuentasPredeterminadasContextVar = null;
	private ContextVar soapDniContextVar = null;

	public StepSayDefaultAccountBalance(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayDefaultAccountBalance;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (cuentasPredeterminadasContextVar == null
				|| soapDniContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {
			SaldoCuenta sld = new SaldoCuenta();

			String predeterminadas = ((ContextVar) context
					.get(cuentasPredeterminadasContextVar.getId()))
					.getVarValue();
			String soapdni = ((ContextVar) context.get(soapDniContextVar
					.getId())).getVarValue();

			sld.armaCuentasDuplicadasTipoTresDigitos(((CallContext) context)
					.getContextVarByName("cuentasContextVar").getVarValue());

			for (String cuenta : predeterminadas.split("\\|")) {
				ArrayList<String> audios = sld.obtieneSaldoPredeterminadas(
						cuenta, soapdni);
				String scapeDigitTmp = "";
				for (Iterator iterator = audios.iterator(); iterator.hasNext();) {
					String audio = (String) iterator.next();
					if (!audio.isEmpty())
						scapeDigitTmp = String.valueOf(
								((CallContext) context).getChannel()
										.streamFile(audio, "1234567890#*"))
								.trim();
					if (!scapeDigitTmp.isEmpty()) {
						break;
					}

				}
			}
		}
		return false;
	}

	public void setCuentasPredeterminadasContextVar(
			ContextVar cuentasPredeterminadasContextVar) {
		this.cuentasPredeterminadasContextVar = cuentasPredeterminadasContextVar;
	}

	public void setSoapDniContextVar(ContextVar soapDniContextVar) {
		this.soapDniContextVar = soapDniContextVar;
	}

}
