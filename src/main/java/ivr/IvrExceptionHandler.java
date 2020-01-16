package ivr;

import java.util.UUID;

import org.apache.log4j.Level;

import main.Daemon;

import workflow.Context;
import workflow.Handler;

public class IvrExceptionHandler implements Handler {

	private UUID id;

	public boolean postprocess(Context context, Exception exception) {
		CallContext myContext = (CallContext) context;

		if (exception != null) {
			Daemon.getMiLog().log(
					Level.ERROR,
					IvrExceptionHandler.class.getName() + "|"
							+ exception.getMessage());
		}

		return true;
	}

	public boolean execute(Context context) throws Exception {
		System.out.println("Exception handler execute");
		return false;
	}

	public UUID GetId() {
		this.setId(UUID.randomUUID());
		return this.id;
	}

	public UUID getNextstep() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setId(UUID tmpid) {
		this.id = tmpid;
	}

	@Override
	public void setStepDescription(String stepDescription) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStepDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}
