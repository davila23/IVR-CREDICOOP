package step;

import java.util.UUID;

import sql.querys.HorariosResponse;

import main.Daemon;

import context.ContextVar;

import workflow.Context;

public class StepTimeConditionDB extends Step {

	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private ContextVar contextVarServicio = null;
	private ContextVar contextVarEmpresa = null;
	private ContextVar contextVarAudio = null;

	public StepTimeConditionDB(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.TimeConditionDB;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVarServicio == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}

		if (contextVarEmpresa == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}

		if (context.containsKey(contextVarServicio.getId())
				&& context.containsKey(contextVarEmpresa.getId())) {
			String idServicio = ((ContextVar) context.get(contextVarServicio
					.getId())).getVarValue();
			String idEmpresa = ((ContextVar) context.get(contextVarEmpresa
					.getId())).getVarValue();

			HorariosResponse hr = Daemon
					.getTimeCondition(idEmpresa, idServicio);
			if (!hr.estaDentroDeHorario()) {
				contextVarAudio.setVarValue(hr.getAudioFueraHorario());
				context.put(contextVarAudio.getId(), contextVarAudio);
				this.setNextstep(this.getNextStepIsTrue());
			} else {
				this.setNextstep(this.getNextStepIsFalse());
			}
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

	public void setContextVarServicio(ContextVar contextVarServicio) {
		this.contextVarServicio = contextVarServicio;
	}

	public void setContextVarEmpresa(ContextVar contextVarEmpresa) {
		this.contextVarEmpresa = contextVarEmpresa;
	}

	public void setContextVarAudio(ContextVar contextVarAudio) {
		this.contextVarAudio = contextVarAudio;
	}

}
