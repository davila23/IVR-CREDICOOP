package step;

import ivr.CallContext;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Daemon;

import auth.AuthConnector;
import auth.AuthConnector.changePasswdReturn;

import context.ContextVar;
import workflow.Context;

public class StepPasswordChange extends Step {

	private ContextVar newPasswordContextVar = null;
	private ContextVar fdnContextVar = null;
	private ContextVar idCrecerContextVar = null;
	private ContextVar passwordChangedStatusContextVar = null;
	private AuthConnector authConn;
	private changePasswdReturn chPassRet;

	public StepPasswordChange(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.CambiaClave;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (newPasswordContextVar == null || fdnContextVar == null
				|| idCrecerContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(idCrecerContextVar.getId())) {
			String idCrecer = ((ContextVar) context.get(idCrecerContextVar
					.getId())).getVarValue();

			String clave = ((ContextVar) context.get(newPasswordContextVar
					.getId())).getVarValue();

			String fdn = ((ContextVar) context.get(fdnContextVar.getId()))
					.getVarValue();
			execPasswordChange(idCrecer, clave, fdn, context);
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;
	}

	public void setNewPasswordContextVar(ContextVar newPasswordContextVar) {
		this.newPasswordContextVar = newPasswordContextVar;
	}

	public void setFdnContextVar(ContextVar fdnContextVar) {
		this.fdnContextVar = fdnContextVar;
	}

	public void setIdCrecerContextVar(ContextVar idCrecerContextVar) {
		this.idCrecerContextVar = idCrecerContextVar;
	}

	public void setPasswordChangedStatusContextVar(
			ContextVar passwordChangedStatusContextVar) {
		this.passwordChangedStatusContextVar = passwordChangedStatusContextVar;
	}

	@SuppressWarnings("unchecked")
	private void execPasswordChange(String idCrecer, String clave, String fdn,
			Context context) {
		authConn = new AuthConnector();
		chPassRet = authConn.changePassword(idCrecer, clave, fdn);
		passwordChangedStatusContextVar.setVarValue(String.valueOf(chPassRet
				.ordinal()));
		context.put(passwordChangedStatusContextVar.getId(),
				passwordChangedStatusContextVar);

		Daemon.getDbLog().addAuthLog(
				((CallContext) context).getChannel().getUniqueId(),
				"CHANGEPASSWORD", idCrecer, chPassRet.toString());
	}
}
