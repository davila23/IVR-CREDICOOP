/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import main.Daemon;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.soap.SOAPBodyElement;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author lsantagata
 */
public class gcaConnectorSoap {

	private SOAPConnectionFactory soapConnFactory;
	private SOAPConnection connection;

	private static gcaConnectorSoap instance = null;

	protected gcaConnectorSoap() {
		try {
			soapConnFactory = SOAPConnectionFactory.newInstance();
			connection = soapConnFactory.createConnection();
		} catch (SOAPException ex) {
			Logger.getLogger(gcaConnectorSoap.class.getName()).log(Level.FATAL,
					null, ex);
		} catch (UnsupportedOperationException ex) {
			Logger.getLogger(gcaConnectorSoap.class.getName()).log(Level.FATAL,
					null, ex);
		}
	}

	public static synchronized gcaConnectorSoap getInstance() {
		if (instance == null) {
			instance = new gcaConnectorSoap();
		}
		return instance;
	}

	public synchronized String getIdSugar(String idCrecer) {
		String value = "";
		MessageFactory messageFactory;
		try {
			messageFactory = MessageFactory.newInstance();
			SOAPMessage message = messageFactory.createMessage();
			SOAPPart soapPart = message.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement bodyElement = body.addChildElement(envelope
					.createName("obtener_id_contacto"));

			bodyElement.addChildElement("idt24").addTextNode(idCrecer);

			MimeHeaders mimeHeader = message.getMimeHeaders();

			// change header's attribute
			mimeHeader.setHeader("SOAPAction", Daemon.getConfig("URLGCA")
					+ "/obtener_id_contacto");

			message.saveChanges();
			SOAPMessage reply = connection.call(message,
					Daemon.getConfig("URLGCA"));
			value = getValueFromSoap(reply);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	public synchronized String getObtenerReclamosGestionesCalidad(
			String idCrecer) {
		String value = "";
		MessageFactory messageFactory;
		try {
			messageFactory = MessageFactory.newInstance();
			SOAPMessage message = messageFactory.createMessage();
			SOAPPart soapPart = message.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement bodyElement = body.addChildElement(envelope
					.createName("obtenerReclamosGestionesCalidad"));

			bodyElement.addChildElement("idt24").addTextNode(idCrecer);
			MimeHeaders mimeHeader = message.getMimeHeaders();

			// change header's attribute
			mimeHeader.setHeader("SOAPAction", Daemon.getConfig("URLGCA")
					+ "/obtenerReclamosGestionesCalidad");

			message.saveChanges();
			SOAPMessage reply = connection.call(message,
					Daemon.getConfig("URLGCA"));
			value = getValueFromSoap(reply);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	public synchronized String GetCountSalesOpportunityByCampaignType(
			String idSugar) {
		String value = "";
		MessageFactory messageFactory;
		try {
			messageFactory = MessageFactory.newInstance();
			SOAPMessage message = messageFactory.createMessage();
			SOAPPart soapPart = message.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			SOAPElement bodyElement = body.addChildElement(envelope
					.createName("countSalesOpportunityByCampaignType"));

			bodyElement.addChildElement("contact_id").addTextNode(idSugar);
			MimeHeaders mimeHeader = message.getMimeHeaders();

			// change header's attribute
			mimeHeader.setHeader("SOAPAction", Daemon.getConfig("URLGCA")
					+ "/countSalesOpportunityByCampaignType");
			message.saveChanges();
			SOAPMessage reply = connection.call(message,
					Daemon.getConfig("URLGCA"));
			value = getValueFromSoap(reply);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	private static String getValueFromSoap(SOAPMessage message)
			throws SOAPException, JSONException {
		String value = "";

		SOAPBody body = message.getSOAPBody();
		// System.out.println("body: " + soapMessageToString(message));
		java.util.Iterator updates = body.getChildElements();
		while (updates.hasNext()) {
			SOAPElement update = (SOAPElement) updates.next();
			java.util.Iterator i = update.getChildElements();
			while (i.hasNext()) {
				SOAPElement e = (SOAPElement) i.next();
				String name = e.getLocalName();
				value = e.getValue();
				if (value == null) {
					value = "0";
					java.util.Iterator h = e.getChildElements();
					int sum = 0;
					while (h.hasNext()) {
						sum++;
						SOAPElement e2 = (SOAPElement) h.next();
						String name2 = e2.getLocalName();
						String jsonString = e2.getValue();
						if (jsonString.contains("{")) {
							JSONObject jsonObject = new JSONObject(jsonString);
							Object objJson = jsonObject
									.get("bcc_campaign_type");
							String ctype = "";
							if (objJson != JSONObject.NULL)
								ctype = (String) objJson;

							if (ctype.equals("bancarizacion") 
									|| ctype.equals("bancarizacion-ofertacomercial")) {
								value = String.valueOf(sum);
							} else {
								value = "0";
							}
						} else {
							value = jsonString;
						}
					}
				}
				// System.out.println(name + ": " + value);
			}
		}

		return value;

	}

	private static String getValueFromSoap2(SOAPMessage message)
			throws SOAPException, JSONException {

		String value = "";

		SOAPPart soapPart = message.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		SOAPBody body2 = envelope.getBody();

		Node returnvalue = (Node) body2.getChildElements().next();

		if (returnvalue != null) {
			if (returnvalue.getChildNodes().item(0).getNodeName()
					.equals("return")) {

				for (int i = 0; i < returnvalue.getChildNodes().item(0)
						.getChildNodes().getLength(); i++) {
					if (returnvalue.getChildNodes().item(0).getChildNodes()
							.item(i).getNodeName().equals("#text")) {
						String jsonString = returnvalue.getChildNodes().item(0)
								.getChildNodes().item(i).getNodeValue();
						if (jsonString.contains("{")) {
							JSONObject jsonObject = new JSONObject(jsonString);
							value = (String) jsonObject.get("value");
						} else {
							value = jsonString;
						}
					} else {
						Logger.getLogger(gcaConnectorSoap.class.getName()).log(
								Level.FATAL, "No items ");
					}
				}
			} else {
				Logger.getLogger(gcaConnectorSoap.class.getName()).log(
						Level.FATAL,
						"no return"
								+ returnvalue.getChildNodes().item(0)
										.getNodeName());
			}
		} else {
			Logger.getLogger(gcaConnectorSoap.class.getName()).log(Level.FATAL,
					"nothing returned");
		}
		return value;
	}

	private static String soapMessageToString(SOAPMessage message) {
		String result = null;

		if (message != null) {
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				result = baos.toString();
			} catch (Exception e) {
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return result;
	}

}
