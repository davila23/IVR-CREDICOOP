package step;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Vector;

import main.Daemon;

import jpos.utils.MessageUtils;

import context.ContextVar;

import workflow.Context;

public class StepSendJPOS extends Step {

	private ContextVar contextVariableName = null;
	private ContextVar contextVariableTipoMensaje = null;
	private ContextVar contextVariableRspJpos = null;

	private Vector<String> varIds = new Vector<String>();
	private Map<Integer, ContextVar> formatoVariables = new HashMap<Integer, ContextVar>();

	public StepSendJPOS(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SendJPOS;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (contextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableName.getId())
				&& context.containsKey(contextVariableTipoMensaje.getId())) {
			ContextVar ctv = (ContextVar) context.get(contextVariableName
					.getId());
			for (Entry<Integer, ContextVar> varFormat : formatoVariables
					.entrySet()) {
				varIds.add(varFormat.getKey(), ((ContextVar) context
						.get(varFormat.getValue().getId()))
						.getFormatedVarValue());
			}
			HashMap<String, String> JposRta = null;
			String toPort = ((ContextVar) context
					.get(contextVariableTipoMensaje.getId())).getVarValue();
			if (toPort.equals("autorizaciones")) {
				JposRta = MessageUtils.getRespuestaJpos(Daemon
						.getJposAutorizaciones().sendMsg(
								MessageUtils.devuelveTrama(varIds)));
			}
			if (toPort.equals("precargada")) {
				JposRta = MessageUtils.getRespuestaJpos(Daemon
						.getJposPrecargadas().sendMsg(
								MessageUtils.devuelveTrama(varIds)));

			}
			if (toPort.equals("consultas")) {
				JposRta = MessageUtils.getRespuestaJpos(Daemon
						.getJposConsultas().sendMsg(
								MessageUtils.devuelveTrama(varIds)));
			}
			if (JposRta != null) {
				contextVariableName.setVarValue(JposRta.get("CODIGORESPUESTA"));
				context.put(contextVariableName.getId(), contextVariableName);

				if (contextVariableRspJpos != null) {
					contextVariableRspJpos.setVarValue(JposRta
							.get("RESPUESTAJPOS"));
					context.put(contextVariableRspJpos.getId(),
							contextVariableRspJpos);
				}
			}

		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setContextVariableName(ContextVar contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	public void setContextVariableRspJpos(ContextVar contextVariableRspJpos) {
		this.contextVariableRspJpos = contextVariableRspJpos;
	}

	public void setContextVariableTipoMensaje(
			ContextVar contextVariableTipoMensaje) {
		this.contextVariableTipoMensaje = contextVariableTipoMensaje;
	}

	public void addformatoVariables(int indice, ContextVar contextVar) {
		this.formatoVariables.put(indice, contextVar);
	}

}
