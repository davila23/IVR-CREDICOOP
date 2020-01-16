/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import context.ContextVar;
import workflow.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author Daniel Avila
 */
public class StepMenu extends Step {

	private UUID InvalidOption;

	private ContextVar contextVariableName = null;

	private final Map<String, UUID> OptionsMenu;
	private int intentoslocal = 10;

	public StepMenu(UUID tmpid) {
		this.id = tmpid;
		this.OptionsMenu = new HashMap<String, UUID>();
		this.StepType = StepType.Menu;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}

		if (intentoslocal == 0) {
			throw new Exception("INTENTOS SUPERADOS CORTA LOOP");
		}

		String optionMenu = "";
		if (context.containsKey(contextVariableName.getId())) {
			optionMenu = ((ContextVar) context.get(contextVariableName.getId()))
					.getVarValue();
		} else {
			this.setNextstep(InvalidOption);
			intentoslocal--;
		}

		if (OptionsMenu.containsKey(optionMenu)) {
			this.setValor(optionMenu);
			this.setNextstep(this.OptionsMenu.get(optionMenu));
		} else {
			this.setValor("i");
			this.setNextstep(InvalidOption);
			intentoslocal--;
		}
		return false;

	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	public void addSteps(String option, UUID nextStep) {
		this.OptionsMenu.put(option, nextStep);
	}

	public UUID getInvalidOption() {
		return InvalidOption;
	}

	public void setInvalidOption(UUID InvalidOption) {
		this.InvalidOption = InvalidOption;
	}

}
