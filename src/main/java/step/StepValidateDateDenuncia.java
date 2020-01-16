package step;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lanzador.Ivrlanzador;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import context.ContextVar;
import workflow.Context;

public class StepValidateDateDenuncia extends Step {

	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private String day = "0";
	private String month = "0";
	private String year = "0";
	private ContextVar contextVariableName = null;
	private ContextVar contextVariableDia = null;
	private ContextVar contextVariableMes = null;
	private ContextVar contextVariableAnio = null;

	public StepValidateDateDenuncia(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ValidateDateDenuncia;
	}

	private boolean validarFecha(String Fecha) {
		boolean res = false;
		if (Fecha == null || Fecha.isEmpty()) {
			res = false;
		} else {
			Pattern r = Pattern.compile("^[0-9]{8}$");
			Matcher m = r.matcher(Fecha.trim());
			if (m.find()) {
				day = Fecha.substring(0, 2);
				month = Fecha.substring(2, 4);
				year = Fecha.substring(4, 8);
				res = esFechaValida(day, month, year);
			} else {
				res = false;
			}
		}

		return res;
	}

	private boolean esFechaValida(String day, String month, String year) {
		boolean retval = false;
		int anio = 0;
		int mes = 0;
		int dia = 0;
		try {
			anio = Integer.parseInt(year);
			mes = Integer.parseInt(month);
			dia = Integer.parseInt(day);
		} catch (NumberFormatException e) {
			Logger.getLogger(StepValidateDateDenuncia.class.getName()).log(
					Level.WARN, null, e);
		}

		if (dia <= (getcantidadDiasDelMes(anio, mes)) && dia != 0) {
			retval = true;
		} else
			retval = false;

		return retval;
	}

	private int getcantidadDiasDelMes(int anio, int mes) {
		int diasMes = 0;

		switch (mes) {

		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			diasMes = 31;
			break;

		case 4:
		case 6:
		case 9:
		case 11:
			diasMes = 30;
			break;

		case 2:
			if (esBisiesto(anio)) {
				diasMes = 29;
			} else
				diasMes = 28;
			break;

		default:
		}
		return diasMes;
	}

	private boolean esBisiesto(int anio) {
		return ((((anio % 4) == 0) && ((anio % 100) != 0)) || ((anio % 400) == 0));
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
				contextVariableDia.setVarValue(this.day);
				contextVariableMes.setVarValue(this.month);
				contextVariableAnio.setVarValue(this.year);

				context.put(contextVariableDia.getId(), contextVariableDia);
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

	public void setContextVariableDia(ContextVar contextVariableDia) {
		this.contextVariableDia = contextVariableDia;
	}

	public void setContextVariableMes(ContextVar contextVariableMes) {
		this.contextVariableMes = contextVariableMes;
	}

	public void setContextVariableAnio(ContextVar contextVariableAnio) {
		this.contextVariableAnio = contextVariableAnio;
	}

}
