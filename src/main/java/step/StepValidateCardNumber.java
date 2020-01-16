/**
 * 
 */
package step;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import context.ContextVar;
import workflow.Context;

/**
 * @author davila
 * 
 */
public class StepValidateCardNumber extends Step {

	private ContextVar contextVariableName;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;

	public StepValidateCardNumber(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ValidateCardNumber;
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
		if (!isNumberAndIsCorrectLength(cardNumber))
			return ret;
		else {
			char[] digits = cardNumber.toCharArray();
			int sum = 0;
			int length = digits.length;
			for (int i = 0; i < length; i++) {
				// sacar los digitos en orden inverso
				int digit = Integer.parseInt(String.valueOf(digits[length - i
						- 1]));

				// cada segundo número se multiplica por 2
				if (i % 2 == 1) {
					digit = digit * 2;
				}
				if (digit > 9) {
					digit = digit - 9;
				}
				sum = sum + digit;
			}
			ret = sum % 10 == 0;
			return ret;
		}
	}

	private boolean isNumberAndIsCorrectLength(String DNI) {

		boolean res = false;

		Pattern r = Pattern.compile("^[0-9]{16}$");
		Matcher m = r.matcher(DNI.trim());
		if (m.find()) {
			res = true;
		} else {
			res = false;
		}

		return res;
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
