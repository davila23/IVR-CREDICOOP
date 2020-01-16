package a380.utils;

import ivr.CallContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

import main.Daemon;

import org.apache.log4j.Level;
import org.asteriskjava.fastagi.AgiException;

import step.StepSendA380Message;

import workflow.Context;
import a380.errors.A380ErrorFactory;
import a380.guion.ArmaNumero;
import a380.mensajes.ConsultaCuentasMultiplesA380;
import a380.mensajes.ConsultaSaldoA380;

import com.cubika.cbank.framework.exceptions.MessageException;

import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.crecerxxi.server.XStreamSerializer;
import coop.bancocredicoop.crecerxxi.server.exposicion.ResponseTO;
import coop.bancocredicoop.service.core.request.MessageBase;
import coop.bancocredicoop.service.core.request.MessageImpl;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;

public class SaldoCuenta {

	private MessageSender service;
	private Hashtable h1;
	private Hashtable h2;
	private ArrayList<String> ctasDuplicadas = new ArrayList<String>();

	public ArrayList<String> obtieneSaldoCuenta(String accountLastDigits,
			ArrayList<String> cuentas, String tcuenta, String soapdni) {
		ArrayList<String> guion = null;
		ArrayList<String> ctas = buscaCuentaPorTipoYNumero(cuentas,
				accountLastDigits, tcuenta);

		guion = obtieneSaldo(ctas, soapdni);

		return guion;
	}

	public ArrayList<String> obtieneSaldoPredeterminadas(String cuenta,
			String soapdni) {
		ArrayList<String> guion = null;

		ArrayList<String> cuentas = new ArrayList<String>();
		cuentas.add(cuenta);
		guion = obtieneSaldo(cuentas, soapdni);
		return guion;
	}

	private ArrayList<String> obtieneSaldo(ArrayList<String> ctas,
			String soapdni) {
		String saldo = "";
		ArrayList<String> guionFinal = new ArrayList<String>();
		String guion = "";
		try {
			if (ctas.size() > 0) {
				for (Iterator iterator = ctas.iterator(); iterator.hasNext();) {
					String cuenta = (String) iterator.next();
					if (!cuenta.isEmpty()) {
						try {
							service = (MessageSender) ServiceProvider
									.getInstance().lookupService("msgA380");
						} catch (ServiceException e1) {
						}
						ConsultaCuentasMultiplesA380 cs = new ConsultaCuentasMultiplesA380();
						ConsultaSaldoA380 cs2 = new ConsultaSaldoA380();
						cs.setNroSucursal(cuenta.substring(1, 4));
						cs2.setNroSucursal(cuenta.substring(1, 4));

						String tipocuenta = cuenta.substring(0, 1);
						String nroCuenta = cuenta.substring(4);
						String audiotipocuenta = "";
						String tipocuentatrx = "";
						switch (Integer.parseInt(tipocuenta)) {
						case 0:
							audiotipocuenta = "S/i05";
							tipocuentatrx = "21";
							break;
						case 1:
							audiotipocuenta = "S/i06";
							tipocuentatrx = "22";
							break;
						case 2:
							audiotipocuenta = "S/i07";
							tipocuentatrx = "43";
							tipocuenta = "5";
							break;
						case 5:
							audiotipocuenta = "S/i09";
							tipocuenta = "9";
							break;
						default:
							break;
						}

						cs.setTipoCuenta(tipocuenta);
						cs.setNroCuenta(nroCuenta);
						cs.setNroBUPAdherente(soapdni);
						cs2.setNroCuenta(nroCuenta);
						cs2.setNroBUPAdherente(soapdni);

						ResponseTO response2 = this.sendMessage(cs2
								.getMessageToSend(tipocuentatrx, ""));

						h2 = cs2.getOutputMsg().parseMessage(
								response2.getData());

						ResponseTO response = this.sendMessage(cs
								.getMessageToSend("66", ""));

						h1 = cs.getOutputMsg().parseMessage(response.getData());

						ArmaNumero aNumero = new ArmaNumero();

						double gSaldo = Double.parseDouble((String) h1
								.get("D.WEBSLD.1"));

						int signosaldo = h1.get("D.WEBSGN.1").toString()
								.equals("C") ? 0 : 1;

						int codMoneda = aNumero.devuelveMoneda(cuenta);

						saldo = aNumero.armaGuionConMoneda(gSaldo, codMoneda,
								signosaldo, true);

						if (Integer.parseInt(h1.get("D.WEBRET.1").toString()) == 0) {

							String guionCuenta = "";
							if (ctasDuplicadas
									.contains(tipocuenta
											+ nroCuenta.substring(nroCuenta
													.length() - 3))) {
								guionCuenta = aNumero
										.armaGuionCuentaCompleta(nroCuenta);
							} else {
								guionCuenta = aNumero
										.armaGuionCuenta(nroCuenta);
							}
							guion += "&" + audiotipocuenta + "&" + guionCuenta
									+ "&" + saldo;

							if (Integer
									.parseInt(h2.get("R.WEBRPTA").toString()) == 0) {
								double cam24 = Double.parseDouble((String) h2
										.get("D.WEBDEP24.1"));
								double cam48 = Double.parseDouble((String) h2
										.get("D.WEBDEP48.1"));
								double efe = Double.parseDouble((String) h2
										.get("D.WEBDEPEF.1"));

								if (cam24 > 0) {
									String scam24 = aNumero.armaGuionConMoneda(
											cam24, codMoneda, 3, false);
									guion += "&" + "S/m03" + "&" + scam24;
								}

								if (cam48 > 0) {
									String scam48 = aNumero.armaGuionConMoneda(
											cam48, codMoneda, 3, false);

									guion += "&" + "S/m04" + "&" + scam48;
								}

								if (efe > 0) {
									String sefe = aNumero.armaGuionConMoneda(
											efe, codMoneda, 3, false);
									guion += "&" + "S/m02" + "&" + sefe;
								}
							}
						} else {
							String err = A380ErrorFactory.obtenerA380Error(
									String.format(
											"%05d",
											Integer.parseInt(h1.get(
													"D.WEBRET.1").toString())))
									.getErrorE352();
							guion = "S/E" + err;
						}

					}
				}
				guion = guion.replace("&&", "&");
				// TODO cambiar esto pero revisar la funcion para ver que mas
				// afecta
				guion = guion.replace("m01", "i10");
				if (guion.startsWith("&", 0))
					guion = guion.substring(1, guion.length());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MessageException e) {
			e.printStackTrace();
		}
		if (!guion.trim().isEmpty())
			guionFinal = new ArrayList<String>(Arrays.asList(guion.split("&")));
		return guionFinal;

	}

	private ArrayList<String> buscaCuentaPorTipoYNumero(
			ArrayList<String> cuentas, String cta, String tipoCuenta) {
		ArrayList<String> cuenta = new ArrayList<String>();

		// Tipos de cuentas: 0 CC:1 CA$: 2 CAU$S
		for (String ca : cuentas) {
			if (!cta.trim().isEmpty()) {
				if (ca.endsWith(cta) && ca.startsWith(tipoCuenta)) {
					cuenta.add(ca);
				}
			}
		}
		return cuenta;
	}

	private ResponseTO sendMessage(String toSend) throws ServiceException {
		ResponseTO response = null;
		try {
			Daemon.getMiLog().log(
					Level.INFO,
					StepSendA380Message.class.getName() + "|MSG_A380_ENVIO|"
							+ toSend + "|FINMSG");
			MessageBase output = service.send(MessageImpl.newInstance(toSend));
			response = (ResponseTO) XStreamSerializer.getInstance()
					.deserialize(output.getText());

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		Daemon.getMiLog().log(
				Level.INFO,
				StepSendA380Message.class.getName() + "|MSG_A380_RESPUESTA|"
						+ response.getData() + "|FINMSG");
		return response;
	}

	public String convierteTipoCuenta(String tipoCuenta) {
		String retval = "";
		switch (Integer.parseInt(tipoCuenta)) {
		case 1:
			retval = "0";
			break;
		case 2:
			retval = "1";
			break;
		case 4:
			retval = "2";
			break;
		default:
			retval = "1";
			break;
		}
		return retval;
	}

	public void armaCuentasDuplicadasTipoTresDigitos(String separadasPipe) {
		ArrayList<String> cuentas = new ArrayList<String>(
				Arrays.asList(separadasPipe.split("\\|")));
		ArrayList<String> todas = new ArrayList<String>();

		for (String cuenta : cuentas) {
			String claveCuenta = cuenta.substring(0, 1)
					+ cuenta.substring(cuenta.length() - 3);
			if (todas.contains(claveCuenta)) {
				ctasDuplicadas.add(claveCuenta);
			}
			todas.add(claveCuenta);
		}
	}
}
