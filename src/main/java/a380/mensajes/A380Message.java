package a380.mensajes;

import java.util.Hashtable;

import org.joda.time.DateTime;

import sql.querys.SecuenciaA380;

import com.cubika.cbank.framework.exceptions.MessageException;
import com.cubika.cbank.services.connector.messages.InputMessage;
import com.cubika.cbank.services.connector.messages.OutputMessage;

public abstract class A380Message {
	protected Hashtable<String, String> propertiesMap = new Hashtable<String, String>();
	protected String msgTypeOut = "";
	protected String msgTypeIn = "";
	protected String fs = "";
	protected OutputMessage outputMsg;
	protected String nroBUPAdherente;
	protected String nroAdherente = String.format("%011d", 0);
	protected String tipoAdherente = "01";

	abstract protected void setParticularData();

	abstract protected void setFs();

	abstract protected void setMessageType();

	public String getMessageToSend(String fa, String ta)
			throws MessageException {
		setFs();
		setMessageType();
		setCommonData(fs, fa, ta);
		setParticularData();
		InputMessage inputMsg = new InputMessage(msgTypeIn);
		outputMsg = new OutputMessage(msgTypeOut);

		return inputMsg.generateMessage(propertiesMap);
	}

	protected void setCommonData(String fs, String fa, String ta) {
		propertiesMap.put("BEQCOD", "A380");
		propertiesMap.put("BEQDTRCV", new DateTime().toString("yyMMdd"));
		propertiesMap.put("BEQTMRCV", new DateTime().toString("HHmmss"));
		propertiesMap.put("BEQTRMSQ", SecuenciaA380.obtenerSecuenciaA380());
		try {
			propertiesMap.put("BEQTK2",
					String.format("%09d", Integer.parseInt(nroAdherente)));
		} catch (Exception e) {
			propertiesMap.put("BEQTK2", String.format("%09d", 1));
		}
		propertiesMap.put("BEQFS",
				("00" + fs.trim()).substring(("00" + fs.trim()).length() - 2));
		propertiesMap.put("BEQFA", fa);
		propertiesMap.put("BEQTA", ta);
		propertiesMap.put("BEQFILLER", String.format("%16s", " "));
		propertiesMap.put("BEQTEST", "N");
		propertiesMap.put("BEQOPTX", "00");
		propertiesMap.put("BEQOTITX", tipoAdherente);
		propertiesMap.put("BEQONITX", nroAdherente);
		propertiesMap.put("BEQOPITX", "00");
		propertiesMap.put("BEQDUMMY", "");
		propertiesMap.put("BEQESQFRM", "");
		propertiesMap.put("BEQOPE", "");
		propertiesMap.put("BEQOPTDOC", "");
		propertiesMap.put("BEQOPNDOC", "");
		propertiesMap.put("BEQOPPDOC", "");
		propertiesMap.put("BEQOPFILLER", "");
		propertiesMap.put("BEQESQFRM", "");

	}

	public OutputMessage getOutputMsg() {
		return outputMsg;
	}

	public void setNroBUPAdherente(String lNroAdherente) {
		this.nroAdherente = lNroAdherente.substring(2, 13);
		this.tipoAdherente = lNroAdherente.substring(0, 2);
		this.nroBUPAdherente = lNroAdherente;
	}

}
