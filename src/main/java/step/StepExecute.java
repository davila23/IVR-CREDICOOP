/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */

public class StepExecute extends Step {

	private String app = null;
	private String appOptions = null;

	public StepExecute(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.Execute;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (app == null) {
			throw new Exception("INVALID APP");
		}
		if (appOptions == null || appOptions.equals("")) {
			((CallContext) context).getChannel().exec(app);
		} else {
			((CallContext) context).getChannel().exec(app, appOptions);
		}
		return false;
	}

	public void setApp(String App) {
		this.app = App;
	}

	public void setAppOptions(String Options) {
		this.appOptions = Options;
	}

}
