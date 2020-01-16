package workflow;

import java.util.UUID;

public interface Task {

	public static final boolean CONTINUE_PROCESSING = false;
	public static final boolean PROCESSING_COMPLETE = true;

	UUID GetId();

	void setId(UUID id);

	UUID getNextstep();

	void setStepDescription(String stepDescription);

	String getStepDescription();

	boolean execute(Context context) throws Exception;
}
