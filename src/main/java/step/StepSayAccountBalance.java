/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import a380.utils.SaldoCuenta;

import context.ContextVar;
import ivr.CallContext;
import workflow.Context;

public class StepSayAccountBalance extends Step {
	private ContextVar tipoDeCuentaContextVar = null;
	private ContextVar ultimosTresDigitosCuentaContextVar = null;
	private ContextVar soapDniContextVar = null;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;

	private ContextVar cuentasContextVar = null;

	public StepSayAccountBalance(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SayAccountBalance;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (cuentasContextVar == null | tipoDeCuentaContextVar == null
				|| ultimosTresDigitosCuentaContextVar == null
				|| soapDniContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {
			SaldoCuenta sld = new SaldoCuenta();
			String tipoCuenta = sld.convierteTipoCuenta(((ContextVar) context
					.get(tipoDeCuentaContextVar.getId())).getVarValue());

			String ultimosDigitos = ((ContextVar) context
					.get(ultimosTresDigitosCuentaContextVar.getId()))
					.getVarValue();

			String strCuentas = ((ContextVar) context.get(cuentasContextVar
					.getId())).getVarValue();

			String soapdni = ((ContextVar) context.get(soapDniContextVar
					.getId())).getVarValue();

			sld.armaCuentasDuplicadasTipoTresDigitos(strCuentas);

			ArrayList<String> cuentas = new ArrayList<String>(
					Arrays.asList(strCuentas.split("\\|")));
			ArrayList<String> audios = sld.obtieneSaldoCuenta(ultimosDigitos,
					cuentas, tipoCuenta, soapdni);
			String scapeDigitTmp = "";
			if (audios.size() > 0) {
				this.setNextstep(this.nextStepIsTrue);
			} else {
				this.setNextstep(this.nextStepIsFalse);
			}
			for (Iterator iterator = audios.iterator(); iterator.hasNext();) {
				String audio = (String) iterator.next();
				if (!audio.isEmpty()) {
					scapeDigitTmp = String.valueOf(
							((CallContext) context).getChannel().streamFile(
									audio, "1234567890#*")).trim();
				}
				if (!scapeDigitTmp.isEmpty()) {
					break;
				}

			}

		}
		return false;
	}

	public void setUltimosTresDigitosCuentaContextVar(
			ContextVar ultimosTresDigitosCuentaContextVar) {
		this.ultimosTresDigitosCuentaContextVar = ultimosTresDigitosCuentaContextVar;
	}

	public void setCuentasContextVar(ContextVar cuentasContextVar) {
		this.cuentasContextVar = cuentasContextVar;
	}

	public void setTipoDeCuentaContextVar(ContextVar tipoDeCuentaContextVar) {
		this.tipoDeCuentaContextVar = tipoDeCuentaContextVar;
	}

	public void setSoapDniContextVar(ContextVar soapDniContextVar) {
		this.soapDniContextVar = soapDniContextVar;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}

}
