/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package context;

import ivr.CallContext;

import org.apache.log4j.Level;

import main.Daemon;

/**
 * 
 * @author Daniel Avila
 */

public class ContextVar {

	private long id;
	private String varDescrip;
	private String varValue;
	private String stringFormat = "";
	private String astUid;
	private String ctxVarName;
	private CallContext ctx;

	public ContextVar(CallContext lCtx) {
		ctx = lCtx;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVarName() {
		return "var" + String.valueOf(id);
	}

	public String getVarDescrip() {
		return varDescrip;
	}

	public void setVarDescrip(String varDescrip) {
		this.varDescrip = varDescrip;
	}

	public String getVarValue() {
		return varValue;
	}

	public String getFormatedVarValue() {
		if (!stringFormat.isEmpty())
			if (stringFormat.contains("d")) {
				Daemon.getMiLog().log(Level.DEBUG,
						"Variable para JPOS|" + varValue);
				return String.format(stringFormat, Long.valueOf(varValue));
			} else
				return String.format(stringFormat, varValue);
		else
			return varValue;
	}

	public void setVarValue(String varValue) {
		Daemon.getDbLog().addVarToLog(
				astUid,
				varDescrip,
				varValue,
				ctx.getCurrentStep() == null ? "" : ctx.getCurrentStep()
						.toString());
		this.varValue = varValue;
	}

	public void setStringFormat(String stringFmt) {
		this.stringFormat = stringFmt;
	}

	public String getAstUid() {
		return astUid;
	}

	public void setAstUid(String astUid) {
		this.astUid = astUid;
	}

	public String getCtxVarName() {
		return ctxVarName;
	}

	public void setCtxVarName(String ctxVarName) {
		this.ctxVarName = ctxVarName;
	}

}
