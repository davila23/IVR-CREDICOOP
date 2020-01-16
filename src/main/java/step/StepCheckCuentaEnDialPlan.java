package step;

import ivr.CallContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiException;
import org.hibernate.Query;
import org.hibernate.Session;

import context.ContextVar;
import utils.HibernateUtil2;
import utils.Usuarioclave;
import utils.Usuariocuenta;
import workflow.Context;

public class StepCheckCuentaEnDialPlan extends Step {

	private ContextVar cuentasContextVar = null;
	private ContextVar cuentaContextVar = null;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private List<String> cuentas = new ArrayList<String>();

	public StepCheckCuentaEnDialPlan(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ChekCuenta;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		String cuentas_aux = obtieneCuentas(context);

		if (cuentas_aux.equals("")) {
			this.setNextstep(nextStepIsFalse);

		} else {

			this.setNextstep(nextStepIsTrue);
		}
		return false;
	}

	private String obtieneCuentas(Context context) throws AgiException {
		String retval = "";
		int i = 1;
		String cuenta = "00";
		while (cuenta != null) {
			try {
				cuenta = ((CallContext) context).getChannel().getVariable(
						"CUENTA_" + i);
			} catch (AgiException e) {
				cuenta = "";
				((CallContext) context).getChannel().setVariable("AGIRTA", "1");
			}
			if (cuenta != null) {
				retval = retval + cuenta + "|";
				cuentas.add(cuenta);
			}
			i++;
		}
		if (retval.length() > 0)
			retval = retval.substring(0, retval.length() - 1);

		return retval;
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
