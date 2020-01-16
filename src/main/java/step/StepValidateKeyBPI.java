package step;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import context.ContextVar;
import workflow.Context;

public class StepValidateKeyBPI extends Step {

	private ContextVar claveContextVar = null;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;

	public StepValidateKeyBPI(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ValidateCuit;
	}

	private boolean validarClave(String clave) {

		boolean res = true;

		if (clave == null || clave.isEmpty()) {
			res = false;
		} else {

			Pattern r = Pattern.compile("^[0-9]{6}$");
			Matcher m = r.matcher(clave.trim());

			if (m.find()) {
				r = Pattern.compile("^([0-9])\\1*$");
				m = r.matcher(clave.trim());
				
				if (m.find())
					res = false;

				if ("0123456789".contains(clave.trim()))
					res = false;

				if ("9876543210".contains(clave.trim()))
					res = false;

			} else {
				res = false;
			}
		}

		return res;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (claveContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(claveContextVar.getId())) {
			ContextVar ctv = (ContextVar) context.get(claveContextVar.getId());
			boolean esValido = validarClave(ctv.getVarValue());
			if (esValido)
				this.setNextstep(nextStepIsTrue);
			else
				this.setNextstep(nextStepIsFalse);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
	}

	public void setClaveContextVar(ContextVar claveContextVar) {
		this.claveContextVar = claveContextVar;
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
