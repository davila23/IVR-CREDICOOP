package a380.mensajes;

public class ConsultaCotizDolarA380 extends A380Message {

	@Override
	protected void setParticularData() {

		propertiesMap.put("BEQOPE", "1");
		propertiesMap.put("BEQOPTDOC", "1");
		propertiesMap.put("BEQOPNDOC", nroAdherente);
		propertiesMap.put("BEQOPPDOC", "0");
		propertiesMap.put("BEQOPFILLER", "");
		propertiesMap.put("BEQDUMMY2", "0");
		propertiesMap.put("BEQONITX", nroAdherente);

	}

	@Override
	protected void setFs() {
		this.fs = "02";
	}

	@Override
	protected void setMessageType() {
		this.msgTypeIn = "CONSULTA.COTIZ";
		this.msgTypeOut = "CONSULTA.COTIZ";
	}

}
