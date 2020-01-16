package step;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lanzador.Ivrlanzador;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import context.ContextVar;
import workflow.Context;

public class StepValidateDateTarjeta extends Step {

	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private String month = "0";
	private String year = "0";
	private ContextVar contextVariableName = null;
	private ContextVar contextVariableMes = null;
	private ContextVar contextVariableAnio = null;

	public StepValidateDateTarjeta(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ValidateDate;
	}

	private boolean validarFecha(String Fecha) {
		boolean res = false;
		if (Fecha == null || Fecha.isEmpty()) {
			res = false;
		} else {
			Pattern r = Pattern.compile("^[0-9]{4}$");
			Matcher m = r.matcher(Fecha.trim());
			if (m.find()) {
				month = Fecha.substring(0, 2);
				year = Fecha.substring(2, 4);
				res = esFechaValida(month, year);
			} else {
				res = false;
			}
		}
		return res;
	}

	private boolean esFechaValida(String month, String year) {
		boolean retval = false;
		int anio = 0;
		int mes = 0;
		try {
			anio = Integer.parseInt(year);
			mes = Integer.parseInt(month);
		} catch (NumberFormatException e) {
			Logger.getLogger(StepValidateDateTarjeta.class.getName()).log(
					Level.WARN, null, e);
		}
		return true;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())) {
			ContextVar ctv = (ContextVar) context.get(contextVariableName
					.getId());
			boolean esValido = validarFecha(ctv.getVarValue());
			if (esValido) {
				contextVariableMes.setVarValue(this.month);
				contextVariableAnio.setVarValue("00" + this.year);

				context.put(contextVariableMes.getId(), contextVariableMes);
				context.put(contextVariableAnio.getId(), contextVariableAnio);

				this.setNextstep(nextStepIsTrue);
			} else
				this.setNextstep(nextStepIsFalse);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
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

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	public void setContextVariableMes(ContextVar contextVariableMes) {
		this.contextVariableMes = contextVariableMes;
	}

	public void setContextVariableAnio(ContextVar contextVariableAnio) {
		this.contextVariableAnio = contextVariableAnio;
	}

}
