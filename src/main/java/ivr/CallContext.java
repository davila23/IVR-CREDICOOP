package ivr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import workflow.Context;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;

import context.ContextVar;

@SuppressWarnings({ "rawtypes", "serial" })
public class CallContext extends HashMap implements Context {
	private UUID initialStep;
	private UUID currentStep;
	private AgiChannel channel;
	private AgiRequest request;
	private HashMap contextVarByName = new HashMap<String, ContextVar>();

	public CallContext() {
	}

	public UUID getInitialStep() {
		return initialStep;
	}

	public void setInitialStep(UUID initialStep) {
		this.initialStep = initialStep;
	}

	public CallContext(UUID init) {
		initialStep = init;

	}

	@SuppressWarnings("unchecked")
	public CallContext(Map map) {
		putAll(map);
	}

	public AgiChannel getChannel() {
		return channel;
	}

	public void setChannel(AgiChannel channel) {
		this.channel = channel;
	}

	public AgiRequest getRequest() {
		return request;
	}

	public void setRequest(AgiRequest request) {
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		contextVarByName.put(((ContextVar) value).getCtxVarName(), value);
		return super.put(key, value);
	}

	public ContextVar getContextVarByName(String VarName) {
		return (ContextVar) contextVarByName.get(VarName);
	}

	public UUID getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(UUID currentStep) {
		this.currentStep = currentStep;
	}

}
