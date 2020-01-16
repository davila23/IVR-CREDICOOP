package step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jaxen.function.StringFunction;

import condition.condition;
import context.ContextVar;

import workflow.Context;

public class StepStringFunctions extends Step {
	private ContextVar contextVariableOrigen = null;
	private ContextVar contextVariableDestino = null;
	private HashMap<Integer, ContextVar> parametros = new HashMap<Integer, ContextVar>();
	private String stringFunctionName = "concat";
	private String value = "";

	public StepStringFunctions(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.StringFunctions;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (contextVariableDestino == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}

		if (stringFunctionName.equals("substring")) {
			if (contextVariableOrigen == null) {
				throw new Exception("INVALID CONTEXT VARIABLENAME");
			}
			this.makeSubString(context);
		}

		if (stringFunctionName.equals("concat")) {
			this.makeConcat(context);
		}

		contextVariableDestino.setVarValue(value);
		context.put(contextVariableDestino.getId(), contextVariableDestino);

		return false;
	}

	public void addParametros(int key, ContextVar variable) {
		this.parametros.put(key, variable);
	}

	public void setContextVariableOrigen(ContextVar contextVariableOrigen) {
		this.contextVariableOrigen = contextVariableOrigen;
	}

	public void setContextVariableDestino(ContextVar contextVariableDestino) {
		this.contextVariableDestino = contextVariableDestino;
	}

	public void setStringFunctionName(String stringFunctionName) {
		this.stringFunctionName = stringFunctionName;
	}

	private void makeSubString(Context context) {
		ContextVar ctv = (ContextVar) context
				.get(contextVariableOrigen.getId());
		if (parametros.size() == 2) {
			int beginIndex = Integer.valueOf(parametros.get(0).getVarValue());
			int endIndex = Integer.valueOf(parametros.get(1).getVarValue());
			value = ctv.getVarValue().substring(beginIndex, endIndex);
		}
		if (parametros.size() == 1) {
			int beginIndex = Integer.valueOf(parametros.get(0).getVarValue());
			value = ctv.getVarValue().substring(beginIndex);
		}

	}

	private void makeConcat(Context context) {
		for (ContextVar ctxv : parametros.values()) {
			value += ctxv.getVarValue();
		}
	}
}
