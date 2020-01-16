/**
 * 
 */
package step;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import context.ContextVar;
import workflow.Context;

/**
 * @author davila
 * 
 */
public class StepValidateBinCardNumber extends Step {

	private ContextVar contextVariableName;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private Map<String, String> bines = new HashMap<String, String>();

	public StepValidateBinCardNumber(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ValidateBinCardNumber;
		bines.put("60420151", "debito");
		bines.put("60420152", "debito");
		bines.put("60420153", "debito");
		bines.put("60420154", "debito");
		bines.put("60420155", "debito");
		bines.put("60420156", "debito");
		bines.put("60420157", "debito");
		bines.put("60420158", "debito");// Plan Argentino de Trabajo
		bines.put("60420159", "debito");// ANSES Asign. Univ Por Hijo
		bines.put("60420171", "debito");// Universidad Nacional del Litoral
		bines.put("60420180", "debito");// Recaudaciones
		bines.put("60420181", "deposito");// Recaudaciones
		bines.put("60421860", "precargada");
		bines.put("58965700", "credito");
		bines.put("58965703", "credito");
		bines.put("58965704", "credito");
		bines.put("627170", "credito");// Kadicard
		bines.put("604235", "fraterna");
		bines.put("454148", "vcreditovisa");
		bines.put("454149", "vcreditovisa");
		bines.put("479352", "vcreditovisa");
		bines.put("433812", "vcreditovisa");
		bines.put("493719", "vcreditovisa");
		bines.put("539909", "mcreditomaster");
		bines.put("532396", "mcreditomaster");
		bines.put("532379", "mcreditomaster");
		bines.put("553444", "mcreditomaster");
		bines.put("515688", "mcreditomaster");
		bines.put("549164", "mcreditomaster");
		// Se agregan con Kadicard
		bines.put("546624", "mcreditomaster");
		bines.put("512258", "mcreditomaster");
		bines.put("501019", "mcreditomaster");
		bines.put("553443", "mcreditomaster");
		bines.put("547294", "mcreditomaster");
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			ContextVar ctv = (ContextVar) context.get(contextVariableName
					.getId());
			boolean esValido = validarCardNumber(ctv.getVarValue());
			if (esValido)
				this.setNextstep(nextStepIsTrue);
			else
				this.setNextstep(nextStepIsFalse);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
	}

	private boolean validarCardNumber(String cardNumber) {
		boolean ret = false;
		if (bines.containsKey(cardNumber.substring(0, 8))) {
			if (bines.get(cardNumber.substring(0, 8)).equals("credito"))
				ret = true;
		} else {
			if (bines.containsKey(cardNumber.substring(0, 6))) {
				if (bines.get(cardNumber.substring(0, 6)).equals("credito"))
					ret = true;
			}
		}
		return ret;

	}

	public UUID getNextStepIsFalse() {
		return nextStepIsFalse;
	}

	public UUID getNextStepIsTrue() {
		return nextStepIsTrue;
	}

	public ContextVar getContextVariableName() {
		return contextVariableName;
	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}
}
