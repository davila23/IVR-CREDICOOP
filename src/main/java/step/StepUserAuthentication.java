package step;

import ivr.CallContext;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Daemon;

import auth.AuthConnector;

import context.ContextVar;
import workflow.Context;

public class StepUserAuthentication extends Step {

	private ContextVar idCrecerContextVar = null;
	private ContextVar claveContextVar = null;
	private ContextVar cantDiasExpiracioncontextVar = null;
	private ContextVar retAuthPasswordcontextVar = null;
	private AuthConnector authConnector;
	private AuthConnector.passwordStatus pwdStatus;

	public StepUserAuthentication(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.AutentificarClave;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (idCrecerContextVar == null || claveContextVar == null
				|| cantDiasExpiracioncontextVar == null
				|| retAuthPasswordcontextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		} else {
			if (context.containsKey(idCrecerContextVar.getId())
					|| context.containsKey(claveContextVar.getId())) {
				String idCrecer = ((ContextVar) context.get(idCrecerContextVar
						.getId())).getVarValue();
				String clave = ((ContextVar) context.get(claveContextVar
						.getId())).getVarValue();
				execAuthUser(idCrecer, clave, context);
			} else {
				throw new Exception("VARIABLE CONTEXT NOT EXIST");
			}

		}
		return false;
	}

	public void setIdCrecerContextVar(ContextVar idCrecerContextVar) {
		this.idCrecerContextVar = idCrecerContextVar;
	}

	public void setClaveContextVar(ContextVar claveContextVar) {
		this.claveContextVar = claveContextVar;
	}

	public void setCantDiasExpiracioncontextVar(
			ContextVar cantDiasExpiracioncontextVar) {
		this.cantDiasExpiracioncontextVar = cantDiasExpiracioncontextVar;
	}

	public void setRetAuthPasswordcontextVar(
			ContextVar retAuthPasswordcontextVar) {
		this.retAuthPasswordcontextVar = retAuthPasswordcontextVar;
	}

	@SuppressWarnings("unchecked")
	private void execAuthUser(String idCrecer, String password, Context context) {

		authConnector = new AuthConnector();
		pwdStatus = authConnector.authUser(idCrecer, password);

		retAuthPasswordcontextVar.setVarValue(String.valueOf(pwdStatus
				.ordinal()));
		context.put(retAuthPasswordcontextVar.getId(),
				retAuthPasswordcontextVar);

		cantDiasExpiracioncontextVar.setVarValue(String.valueOf(authConnector
				.getPwdExpireWarning()));
		context.put(cantDiasExpiracioncontextVar.getId(),
				cantDiasExpiracioncontextVar);

		Daemon.getDbLog().addAuthLog(
				((CallContext) context).getChannel().getUniqueId(),
				"AUTHTENTICATE", idCrecer, pwdStatus.toString());
	}
}
