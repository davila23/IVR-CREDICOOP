/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import main.Daemon;

import org.apache.log4j.Level;
import org.asteriskjava.fastagi.AgiException;

import com.bea.xml.stream.samples.Parse;
import com.cubika.cbank.framework.exceptions.MessageException;

import a380.mensajes.ConsultaSaldoA380;
import a380.mensajes.ConsultaCotizDolarA380;
import a380.mensajes.ConsultaCuentasMultiplesA380;
import a380.mensajes.PagoTarjetaCabalA380;
import a380.mensajes.PedidoBoleteraDepositoA380;
import a380.mensajes.PedidoChequeraA380;
import a380.mensajes.PedidoDuplicadoResumenA380;
import a380.mensajes.TransferenciaCuentasPropiasA380;
import a380.errors.A380ErrorFactory;
import a380.fsfata.FsFaTaFactory;
import a380.guion.*;
import context.ContextVar;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.crecerxxi.server.XStreamSerializer;
import coop.bancocredicoop.crecerxxi.server.exposicion.ResponseTO;
import coop.bancocredicoop.service.core.request.MessageBase;
import coop.bancocredicoop.service.core.request.MessageImpl;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;

import ivr.CallContext;
import workflow.Context;

public class StepSendA380Message extends Step {

	private int funcion = 0;

	private static MessageSender service;

	private ContextVar contextVariableFuncion = null;

	public StepSendA380Message(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.SendA380Message;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (contextVariableFuncion == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVariableFuncion.getId())) {
			this.enviaMensaje(Integer.parseInt(((ContextVar) context
					.get(contextVariableFuncion.getId())).getVarValue()),
					context);
		} else {
			throw new Exception("VARIABLE CONTEXT NOT EXIST");
		}
		return false;
	}

	public void setContextVariableFuncion(ContextVar contextVariableFuncion) {
		this.contextVariableFuncion = contextVariableFuncion;
	}

	private void enviaMensaje(int lFuncion, Context context)
			throws AgiException {
		ArrayList<String> cuentas = new ArrayList<String>();
		try {
			service = (MessageSender) ServiceProvider.getInstance()
					.lookupService("msgA380");
		} catch (ServiceException e1) {
		}
		int i = 1;
		String cuenta = "00";
		while (cuenta != null) {
			try {
				cuenta = ((CallContext) context).getChannel().getVariable(
						"CUENTA_" + i);
			} catch (AgiException e) {
				cuenta = "";
				((CallContext) context).getChannel().setVariable("AGIRTA", "1");
			}
			if (cuenta != null)
				cuentas.add(cuenta);
			i++;
		}
		String soapdni = (((CallContext) context).getChannel()
				.getVariable("SOAPDNI"));
		Hashtable h1 = null;
		this.setValor(String.valueOf(lFuncion));
		switch (lFuncion) {
		case 110:
		case 120:
		case 140:
			obtieneSaldo(context, cuentas, soapdni, lFuncion, h1);
			break;
		case 161:
		case 162:
			obtieneChequesRechazados(context, cuentas, soapdni, lFuncion, h1);
			break;
		case 211:
		case 212:
		case 221:
		case 222:
		case 244:
			realizaTransferencia(context, cuentas, soapdni, lFuncion, h1);
			break;
		case 310:
		case 320:
			realizaPedidoDuplicadoResumen(context, cuentas, soapdni, lFuncion,
					h1);
			break;
		case 361:
		case 362:
			realizaPedidoChequeras(context, cuentas, soapdni, lFuncion, h1);
			break;
		case 371:
		case 372:
			realizaPedidoBoleteraDepositos(context, cuentas, soapdni, lFuncion,
					h1);
			break;
		case 400:
			obtieneCotizacionDolar(context, cuentas, soapdni, lFuncion, h1);
			break;
		case 519:
		case 529:
			realizaPagoTarjetaCabal(context, cuentas, soapdni, lFuncion, h1);
			break;

		default:
			break;
		}
	}

	private ResponseTO sendMessage(String toSend) throws ServiceException {
		ResponseTO response = null;
		try {
			Daemon.getMiLog().log(
					Level.INFO,
					StepSendA380Message.class.getName() + "|MSG_A380_ENVIO|"
							+ toSend + "|FINMSG");
			this.setValor(toSend);
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

	private String[] buscaCuenta(ArrayList<String> cuentas, String cta) {
		String[] cuenta = { "", "" };
		int i = 0;
		for (String ca : cuentas) {
			if (ca.endsWith(cta)) {
				i++;
				cuenta[0] = ca;
				cuenta[1] = String.valueOf(i);
			}
		}
		return cuenta;
	}

	private ArrayList<String> buscaCuentaPorTipoYNumero(
			ArrayList<String> cuentas, String cta, int lFuncion) {
		ArrayList<String> cuenta = new ArrayList<String>();
		String tipoCuenta = obtieneTipoCuentaDesdeFuncion(lFuncion);

		for (String ca : cuentas) {
			if (ca.endsWith(cta) && ca.startsWith(tipoCuenta)) {
				cuenta.add(ca);
			}
		}
		return cuenta;
	}

	private void obtieneSaldo(Context context, ArrayList<String> cuentas,
			String soapdni, int lFuncion, Hashtable h1) {
		String saldo = "";
		try {
			String ncd = (((CallContext) context).getChannel()
					.getVariable("NCD"));
			ArrayList<String> ctas = buscaCuentaPorTipoYNumero(cuentas, ncd,
					lFuncion);

			String error = "";
			String cr = "0";
			String agirta = "";
			String guion = "";

			if (ctas.size() == 0) {
				((CallContext) context).getChannel().setVariable("GUION",
						"S/E8");
				((CallContext) context).getChannel().setVariable("AGIRTA", "8");
				((CallContext) context).getChannel().setVariable("ERROR", "8");
			} else {
				for (Iterator iterator = ctas.iterator(); iterator.hasNext();) {
					String cuenta = (String) iterator.next();
					if (!cuenta.isEmpty()) {
						ConsultaCuentasMultiplesA380 cs = new ConsultaCuentasMultiplesA380();
						cs.setNroSucursal(cuenta.substring(1, 4));

						String tipocuenta = cuenta.substring(0, 1);
						String audiotipocuenta = "";
						switch (Integer.parseInt(cuenta.substring(0, 1))) {
						case 0:
							audiotipocuenta = "S/i05";
							break;
						case 1:
							audiotipocuenta = "S/i06";
							break;
						case 2:
							audiotipocuenta = "S/i07";
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
						cs.setNroCuenta(cuenta.substring(cuenta.length() - 7,
								cuenta.length()));
						cs.setNroBUPAdherente(soapdni);

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

							guion += "&"
									+ audiotipocuenta
									+ "&"
									+ aNumero.armaGuionCuentaCompleta(cuenta
											.substring(4)) + "&" + saldo;

							agirta = h1.get("D.WEBRET.1").toString();

						} else {
							String err = A380ErrorFactory.obtenerA380Error(
									String.format(
											"%05d",
											Integer.parseInt(h1.get(
													"D.WEBRET.1").toString())))
									.getErrorE352();
							guion = "S/E" + err;
							agirta = err;
							cr = err;
							error = err;
						}

					}
				}
				guion = guion.replace("&&", "&");
				// TODO cambiar esto pero revisar la funcion para ver que mas
				// afecta
				guion = guion.replace("m01", "i10");
				if (guion.startsWith("&", 0))
					guion = guion.substring(1, guion.length());

				((CallContext) context).getChannel()
						.setVariable("GUION", guion);
				((CallContext) context).getChannel().setVariable("AGIRTA",
						agirta);
				((CallContext) context).getChannel().setVariable("CR", cr);
				if (!error.isEmpty())
					((CallContext) context).getChannel().setVariable("ERROR",
							error);
			}

		} catch (AgiException e) {
			saldo = "0";
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MessageException e) {
			e.printStackTrace();
		}

	}

	private void obtieneChequesRechazados(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {
			String cuenta = (((CallContext) context).getChannel()
					.getVariable("NCD"));
			String[] ctaCant = buscaCuenta(cuentas, cuenta);
			String cuentaConsulta = ctaCant[0];

			if (!cuentaConsulta.isEmpty()) {
				ConsultaSaldoA380 cs = new ConsultaSaldoA380();
				cs.setNroSucursal(cuentaConsulta.substring(1, 4));
				cs.setNroCuenta(cuentaConsulta.substring(
						cuentaConsulta.length() - 7, cuentaConsulta.length()));
				cs.setNroBUPAdherente(soapdni);

				ResponseTO response = this.sendMessage(cs.getMessageToSend(
						FsFaTaFactory.obtenerFuncion(String.valueOf(lFuncion))
								.getFa(),
						FsFaTaFactory.obtenerFuncion(String.valueOf(lFuncion))
								.getTa()));

				h1 = cs.getOutputMsg().parseMessage(response.getData());
				ArmaNumero aNumero = new ArmaNumero();

				if (Integer.parseInt(h1.get("R.WEBRPTA").toString()) == 0) {
					double gSaldo = Double.parseDouble((String) h1
							.get("D.WEBIMPCH.1"));

					int cantcheques = Integer.parseInt(h1.get("D.WEBNROCH.1")
							.toString());
					String guion = aNumero.armaGuionChequesRechazados(cuenta,
							cantcheques, gSaldo);
					((CallContext) context).getChannel().setVariable("GUION",
							guion);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							"0");
					((CallContext) context).getChannel().setVariable("CR", "0");

				} else {

					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"D.BINRPTA.1").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"S/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
				}

			}
		} catch (Exception e) {

		}

	}

	private void realizaTransferencia(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {

		try {

			String cuentaDebito = (((CallContext) context).getChannel()
					.getVariable("NCD"));
			String cuentaCredito = ((CallContext) context).getChannel()
					.getVariable("NCC");
			Double monto = Double.parseDouble(((CallContext) context)
					.getChannel().getVariable("importe")) / 100;
			String operaTR = ((CallContext) context).getChannel().getVariable(
					"operaTR");

			String[] ctaCantOri = buscaCuenta(cuentas, cuentaDebito);
			cuentaDebito = ctaCantOri[0];

			String[] ctaCantDes = buscaCuenta(cuentas, cuentaCredito);
			cuentaCredito = ctaCantDes[0];

			ArmaNumero aNumero = new ArmaNumero();

			if (!cuentaDebito.isEmpty()
					&& !cuentaCredito.isEmpty()
					&& Integer.parseInt(ctaCantOri[1]) == 1
					&& Integer.parseInt(ctaCantDes[1]) == 1
					&& (aNumero.devuelveMoneda(cuentaDebito) == aNumero
							.devuelveMoneda(cuentaCredito))) {
				if (cuentaDebito == cuentaCredito) {
					((CallContext) context).getChannel().setVariable("GUION",
							"S/E25");

					((CallContext) context).getChannel().setVariable("AGIRTA",
							"25");
					((CallContext) context).getChannel()
							.setVariable("CR", "25");
				} else {
					if (operaTR.equals("1")) {
						String transferencia = aNumero.armaGuionTransferencia(
								cuentaDebito, cuentaCredito, monto);
						((CallContext) context).getChannel().setVariable(
								"GUION", transferencia);
						((CallContext) context).getChannel().setVariable(
								"AGIRTA", "0");
						((CallContext) context).getChannel().setVariable("CR",
								"0");

					}
					if (operaTR.equals("2")) {
						TransferenciaCuentasPropiasA380 tf = new TransferenciaCuentasPropiasA380();

						tf.setNroSucursalOrigen(cuentaDebito.substring(1, 4));
						tf.setNroSucursalDestino(cuentaCredito.substring(1, 4));

						tf.setNroCuentaOrigen(cuentaDebito.substring(
								cuentaDebito.length() - 7,
								cuentaDebito.length()));
						tf.setNroCuentaDestino(cuentaCredito.substring(
								cuentaCredito.length() - 7,
								cuentaCredito.length()));

						tf.setMonto(monto);
						tf.setNroBUPAdherente(soapdni);

						ResponseTO response = this.sendMessage(tf
								.getMessageToSend(
										FsFaTaFactory.obtenerFuncion(
												String.valueOf(lFuncion))
												.getFa(),
										FsFaTaFactory.obtenerFuncion(
												String.valueOf(lFuncion))
												.getTa()));

						h1 = tf.getOutputMsg().parseMessage(response.getData());

						if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) == 0) {
							((CallContext) context).getChannel().setVariable(
									"PBM_SEC_NUM",
									h1.get("D.BINTRMSQ.1").toString());
							((CallContext) context).getChannel().setVariable(
									"AGIRTA", h1.get("D.BINRPTA.1").toString());
							((CallContext) context).getChannel().setVariable(
									"CR", "0");
							((CallContext) context).getChannel().setVariable(
									"CA", "0");
						} else {

							String err = A380ErrorFactory
									.obtenerA380Error(
											String.format("%05d", Integer
													.parseInt(h1.get(
															"D.BINRPTA.1")
															.toString())))
									.getErrorE352();
							((CallContext) context).getChannel().setVariable(
									"GUION", "S/E" + err);
							((CallContext) context).getChannel().setVariable(
									"AGIRTA", err);
							((CallContext) context).getChannel().setVariable(
									"CR", err);
							((CallContext) context).getChannel().setVariable(
									"CA", err);
						}
					}
				}
			} else {
				String guion = "";
				String agirta = "";
				String crError = "";
				if (cuentaDebito.isEmpty()) {
					guion = "S/E9";
					agirta = "9";
					crError = "9";
				}
				if (cuentaCredito.isEmpty()) {
					guion = "S/E8";
					agirta = "8";
					crError = "8";
				}

				((CallContext) context).getChannel()
						.setVariable("GUION", guion);

				((CallContext) context).getChannel().setVariable("AGIRTA",
						agirta);
				((CallContext) context).getChannel().setVariable("CR", crError);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void obtieneCotizacionDolar(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {

			ArmaNumero aNumero = new ArmaNumero();

			if (!soapdni.isEmpty()) {
				ConsultaCotizDolarA380 consCotizacionDolar = new ConsultaCotizDolarA380();

				consCotizacionDolar.setNroBUPAdherente(soapdni);

				ResponseTO response = this.sendMessage(consCotizacionDolar
						.getMessageToSend(
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getFa(),
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getTa()));

				h1 = consCotizacionDolar.getOutputMsg().parseMessage(
						response.getData());

				if (Integer.parseInt(h1.get("R.WEBRPTA").toString()) == 0) {
					double cotven = Double.parseDouble(h1.get("D.BINCOVEN.1")
							.toString());
					double cotcomp = Double.parseDouble(h1.get("D.BINCOCOM.1")
							.toString());

					String pedidoCotizacionDolar = aNumero
							.armaGuionpedidoCotizacionDolar(h1
									.get("H.WEBISBHR").toString(), cotven,
									cotcomp);
					((CallContext) context).getChannel().setVariable("GUION",
							pedidoCotizacionDolar);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							h1.get("R.WEBRPTA").toString());
					((CallContext) context).getChannel().setVariable("CR", "0");
				} else {
					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"R.WEBRPTA").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"S/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
				}
			} else {
				((CallContext) context).getChannel().setVariable("GUION", "");

				((CallContext) context).getChannel().setVariable("AGIRTA", "E");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void realizaPedidoDuplicadoResumen(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {

			String cuenta = (((CallContext) context).getChannel()
					.getVariable("NCD"));

			String[] ctaCant = buscaCuenta(cuentas, cuenta);
			String nroCuenta = ctaCant[0];

			if (!nroCuenta.isEmpty() && Integer.parseInt(ctaCant[1]) == 1) {

				PedidoDuplicadoResumenA380 pedDuplicado = new PedidoDuplicadoResumenA380();

				pedDuplicado.setNroSucursal(nroCuenta.substring(1, 4));
				pedDuplicado.setNroCuenta(nroCuenta.substring(
						nroCuenta.length() - 7, nroCuenta.length()));
				pedDuplicado.setNroBUPAdherente(soapdni);

				ResponseTO response = this.sendMessage(pedDuplicado
						.getMessageToSend(
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getFa(),
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getTa()));

				h1 = pedDuplicado.getOutputMsg().parseMessage(
						response.getData());

				if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) == 0) {
					((CallContext) context).getChannel().setVariable("GUION",
							"S/opok");
					((CallContext) context).getChannel().setVariable("AGIRTA",
							h1.get("D.BINRPTA.1").toString());
					((CallContext) context).getChannel().setVariable("CR", "0");
					((CallContext) context).getChannel().setVariable(
							"PBM_SEC_NUM", h1.get("D.BINTRMSQ.1").toString());

				} else {

					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"D.BINRPTA.1").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"IVRC/E/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
				}
			} else {
				((CallContext) context).getChannel().setVariable("GUION", "");

				((CallContext) context).getChannel().setVariable("AGIRTA", "E");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void realizaPedidoChequeras(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {
			String cuenta = (((CallContext) context).getChannel()
					.getVariable("NCD"));

			String tipo = " ";
			if (lFuncion == 362)
				tipo = "D";

			String[] ctaCant = buscaCuenta(cuentas, cuenta);
			String nroCuenta = ctaCant[0];

			if (!nroCuenta.isEmpty() && !tipo.isEmpty()
					&& Integer.parseInt(ctaCant[1]) == 1) {

				PedidoChequeraA380 pedChequera = new PedidoChequeraA380();

				pedChequera.setNroSucursal(nroCuenta.substring(1, 4));
				pedChequera.setTipoCheque(tipo);

				pedChequera.setNroBUPAdherente(soapdni);
				pedChequera.setNroCuenta(nroCuenta.substring(
						nroCuenta.length() - 7, nroCuenta.length()));

				ResponseTO response = this.sendMessage(pedChequera
						.getMessageToSend(
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getFa(),
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getTa()));

				h1 = pedChequera.getOutputMsg()
						.parseMessage(response.getData());

				System.out.println("Msg Retornado: " + response.getData());
				System.out.println("Msg Retornado: " + h1.toString());

				if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) == 0) {
					((CallContext) context).getChannel().setVariable("GUION",
							"S/opok");
					((CallContext) context).getChannel().setVariable("AGIRTA",
							h1.get("D.BINRPTA.1").toString());
					((CallContext) context).getChannel().setVariable("CR", "0");
					((CallContext) context).getChannel().setVariable(
							"PBM_SEC_NUM", h1.get("D.BINTRMSQ.1").toString());

				} else {

					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"D.BINRPTA.1").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"IVRC/E/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
				}
			} else {
				((CallContext) context).getChannel().setVariable("GUION", "");

				((CallContext) context).getChannel().setVariable("AGIRTA", "E");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void realizaPedidoBoleteraDepositos(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {

			String cuenta = (((CallContext) context).getChannel()
					.getVariable("NCD"));

			String[] ctaCant = buscaCuenta(cuentas, cuenta);
			String nroCuenta = ctaCant[0];

			if (!nroCuenta.isEmpty() && Integer.parseInt(ctaCant[1]) == 1) {
				PedidoBoleteraDepositoA380 pedBoletera = new PedidoBoleteraDepositoA380();

				pedBoletera.setNroSucursal(nroCuenta.substring(1, 4));

				pedBoletera.setNroBUPAdherente(soapdni);
				pedBoletera.setNroCuenta(nroCuenta.substring(
						nroCuenta.length() - 7, nroCuenta.length()));

				ResponseTO response = this.sendMessage(pedBoletera
						.getMessageToSend(
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getFa(),
								FsFaTaFactory.obtenerFuncion(
										String.valueOf(lFuncion)).getTa()));

				h1 = pedBoletera.getOutputMsg()
						.parseMessage(response.getData());

				System.out.println("Msg Retornado: " + response.getData());
				System.out.println("Msg Retornado: " + h1.toString());

				if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) == 0) {
					((CallContext) context).getChannel().setVariable("GUION",
							"S/opok");
					((CallContext) context).getChannel().setVariable("AGIRTA",
							h1.get("D.BINRPTA.1").toString());
					((CallContext) context).getChannel().setVariable("CR", "0");
					((CallContext) context).getChannel().setVariable(
							"PBM_SEC_NUM", h1.get("D.BINTRMSQ.1").toString());

				} else {

					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"D.BINRPTA.1").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"IVRC/E/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
				}
			} else {
				((CallContext) context).getChannel().setVariable("GUION", "");

				((CallContext) context).getChannel().setVariable("AGIRTA", "E");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void realizaPagoTarjetaCabal(Context context,
			ArrayList<String> cuentas, String soapdni, int lFuncion,
			Hashtable h1) {
		try {
			String cuentaDebito = (((CallContext) context).getChannel()
					.getVariable("NCD"));

			String ipd = ((CallContext) context).getChannel()
					.getVariable("ipd");

			if (ipd == null)
				ipd = "0";

			Double monto = Double.parseDouble(ipd) / 100;

			String confirmada = ((CallContext) context).getChannel()
					.getVariable("MQ");

			if (confirmada == null)
				confirmada = "0";

			String tarjetaCabal = (((CallContext) context).getChannel()
					.getVariable("plastico"));

			String[] ctaCantOri = buscaCuenta(cuentas, cuentaDebito);
			cuentaDebito = ctaCantOri[0];

			if (confirmada.equals("0")) {
				ArmaNumero aNumero = new ArmaNumero();
				if (!cuentaDebito.isEmpty()
						&& Integer.parseInt(ctaCantOri[1]) == 1) {
					String pagoCabal = aNumero.armaGuionPagoTarjetaCabal(
							cuentaDebito, monto);
					((CallContext) context).getChannel().setVariable("GUION",
							pagoCabal);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							"0");
					((CallContext) context).getChannel().setVariable("CR", "0");
				}
			} else {
				PagoTarjetaCabalA380 pg = new PagoTarjetaCabalA380();
				pg.setNroSucursalDebito(cuentaDebito.substring(1, 4));

				pg.setNroCuentaDebito(cuentaDebito.substring(
						cuentaDebito.length() - 7, cuentaDebito.length()));
				pg.setMonto(monto);
				pg.setNroBUPAdherente(soapdni);
				pg.setNroTarjetaCabal(tarjetaCabal);

				ResponseTO response = this.sendMessage(pg.getMessageToSend(
						FsFaTaFactory.obtenerFuncion(String.valueOf(lFuncion))
								.getFa(),
						FsFaTaFactory.obtenerFuncion(String.valueOf(lFuncion))
								.getTa()));
				h1 = pg.getOutputMsg().parseMessage(response.getData());

				if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) == 0) {
					((CallContext) context).getChannel().setVariable("CR", "0");
					((CallContext) context).getChannel().setVariable("GUION",
							"S/opok");
					((CallContext) context).getChannel().setVariable(
							"PBM_SEC_NUM", h1.get("D.BINTRMSQ.1").toString());
					((CallContext) context).getChannel().setVariable("AGIRTA",
							h1.get("D.BINRPTA.1").toString());
					((CallContext) context).getChannel()
							.setVariable("ipd", "0");
					((CallContext) context).getChannel().setVariable("MQ", "0");
				} else {

					String err = A380ErrorFactory.obtenerA380Error(
							String.format("%05d", Integer.parseInt(h1.get(
									"D.BINRPTA.1").toString()))).getErrorE352();
					((CallContext) context).getChannel().setVariable("GUION",
							"S/E" + err);
					((CallContext) context).getChannel().setVariable("AGIRTA",
							err);
					((CallContext) context).getChannel().setVariable("CR", err);
					((CallContext) context).getChannel()
							.setVariable("ipd", "0");
					((CallContext) context).getChannel().setVariable("MQ", "0");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				((CallContext) context).getChannel().setVariable("GUION",
						"S/E1");
				((CallContext) context).getChannel().setVariable("AGIRTA", "E");
				((CallContext) context).getChannel().setVariable("CR", "1");
				((CallContext) context).getChannel().setVariable("ipd", "0");
				((CallContext) context).getChannel().setVariable("MQ", "0");
			} catch (AgiException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private String obtieneTipoCuentaDesdeFuncion(int lFuncion) {
		String retval = "";

		switch (lFuncion) {
		case 110:
			retval = "0";
			break;
		case 120:
			retval = "1";
			break;
		case 140:
			retval = "2";
			break;
		}

		return retval;

	}
}
