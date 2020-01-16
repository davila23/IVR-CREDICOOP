/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import auth.AuthConnector;

import context.ContextVar;

import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */

public class StepAuthInitialInfo extends Step {
	private ContextVar idCrecerContextVar = null;
	private ContextVar userStatusContextVar = null;
	private AuthConnector aConn;
	private AuthConnector.userStatus userSt;

	public StepAuthInitialInfo(UUID tmpid) {
		this.id = tmpid;
		this.StepType = step.StepFactory.StepType.AuthInitialInfo;

	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (idCrecerContextVar == null || userStatusContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		} else {
			if (context.containsKey(idCrecerContextVar.getId())) {
				String idCrecer = ((ContextVar) context.get(idCrecerContextVar
						.getId())).getVarValue();
				this.execUserInfo(idCrecer, context);
			} else {
				throw new Exception("VARIABLE CONTEXT NOT EXIST");
			}
		}
		return false;

	}

	public void setIdCrecerContextVar(ContextVar idCrecerContextVar) {
		this.idCrecerContextVar = idCrecerContextVar;
	}

	public void setUserStatus(ContextVar userStatus) {
		this.userStatusContextVar = userStatus;
	}

	@SuppressWarnings("unchecked")
	private void execUserInfo(String idcrecer, Context context) {
		aConn = new AuthConnector();
		this.userSt = aConn.initUser(idcrecer);
		userStatusContextVar.setVarValue(String.valueOf(userSt.ordinal()));
		context.put(userStatusContextVar.getId(), userStatusContextVar);

	}

}
