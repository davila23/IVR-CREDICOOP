package step;

import java.util.Random;
import java.util.UUID;

import context.ContextVar;

import workflow.Context;

public class StepGenerateKeyFromDni extends Step {
	private ContextVar contextVariableDni;
	private ContextVar contextVariableClaveDni;
	private ContextVar contextVariableClaveRandom;
	private String claveRandom = "";
	private String claveDni = "";
	private String dni = "";

	public StepGenerateKeyFromDni(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.GenerateKeyFromDni;
	}

	private void generaClave() {

		Random rnd = new Random();
		for (int i = 1; i < 5; i++) {
			int pos = 0;
			while (pos == 0) {
				pos = rnd.nextInt(8) + 1;
			}
			claveRandom += pos;
			claveDni += dni.substring(pos - 1, pos);
		}
	}

	public String getDni() {
		return dni;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableClaveDni == null
				|| contextVariableClaveRandom == null
				|| contextVariableDni == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableClaveDni.getId())
				&& context.containsKey(contextVariableClaveRandom.getId())
				&& context.containsKey(contextVariableDni.getId())) {

			dni = String.format("%08d", Integer.parseInt(((ContextVar) context
					.get(contextVariableDni.getId())).getVarValue()));
			claveDni = "";
			claveRandom = "";
			this.generaClave();
			contextVariableClaveDni.setVarValue(claveDni);
			contextVariableClaveRandom.setVarValue(String.valueOf(claveRandom));
			context.put(contextVariableClaveDni.getId(),
					contextVariableClaveDni);
			context.put(contextVariableClaveRandom.getId(),
					contextVariableClaveRandom);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}

		return false;
	}

	public void setContextVariableClaveDni(ContextVar contextVariableClaveDni) {
		this.contextVariableClaveDni = contextVariableClaveDni;
	}

	public void setContextVariableClaveRandom(
			ContextVar contextVariableClaveRandom) {
		this.contextVariableClaveRandom = contextVariableClaveRandom;
	}

	public void setContextVariableDni(ContextVar ctxtVariableDni) {
		this.contextVariableDni = ctxtVariableDni;
	}

}
