package test;

import static org.junit.Assert.*;

import ivr.CallContext;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;

import org.joda.time.DateTime;
import org.junit.Test;

import a380.errors.A380ErrorFactory;
import a380.fsfata.FsFaTaFactory;
import a380.mensajes.*;

import com.cubika.cbank.framework.exceptions.MessageException;
import com.cubika.cbank.services.connector.messages.InputMessage;
import com.cubika.cbank.services.connector.messages.OutputMessage;

import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.crecerxxi.server.XStreamSerializer;
import coop.bancocredicoop.crecerxxi.server.exposicion.ResponseTO;
import coop.bancocredicoop.service.core.request.MessageBase;
import coop.bancocredicoop.service.core.request.MessageImpl;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.templating.ParseException;

public class TestMensajesA380 {

	private static MessageSender service;

	@Test
	public void TestMessages() throws ServiceException {
		testLoadService();
		// testConsultaSaldoA380();
		// testConsultaCuentasMultiplesA380();
		// testTransferenciasCuentasPropiasA380();
		// testConsultaCotizDollar380();
		// testPedidoBoleteraA380();
		// testPedidoChequeraA380();
		// testPedidoDuplicadoResumenA380();
		testPagoTarjetaCabalA380();
	}

	public void testLoadService() throws ServiceException {
		TestMensajesA380.service = (MessageSender) ServiceProvider
				.getInstance().lookupService("msgA380");
		assertNotNull(TestMensajesA380.service);
	}

	public void testConsultaSaldoA380() throws ServiceException {
		ConsultaSaldoA380 cs = new ConsultaSaldoA380();
		cs.setNroCuenta("0034347");
		cs.setNroSucursal("070");
		cs.setNroBUPAdherente("010001731914300");

		try {

			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("22", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Enviado: " + cs.getMessageToSend("22", ""));
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
			System.out.println("Msg Codigo Retornado: " + h1.get("R.WEBRPTA"));
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testConsultaCuentasMultiplesA380() throws ServiceException {
		ConsultaCuentasMultiplesA380 cs = new ConsultaCuentasMultiplesA380();
		cs.setNroCuenta("34347");
		cs.setNroSucursal("70");
		cs.setTipoCuenta("1");
		cs.setNroBUPAdherente("010001731914300");

		try {

			System.out.println("Msg Enviado: " + cs.getMessageToSend("66", ""));
			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("66", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
			System.out.println("Msg Saldo Retornado: " + h1.get("D.WEBSLD.1"));
			System.out.println("Msg Codigo Retornado: " + h1.get("D.WEBRET.1"));
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testConsultaCotizDollar380() throws ServiceException {
		ConsultaCotizDolarA380 cs = new ConsultaCotizDolarA380();
		cs.setNroBUPAdherente("010001731914300");
		try {

			System.out.println("Msg Enviado: " + cs.getMessageToSend("34", ""));
			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("34", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
			System.out.println("Msg Coti Vendedor: " + h1.get("D.BINCOVEN.1"));
			System.out.println("Msg Coti Comprador: " + h1.get("D.BINCOCOM.1"));
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testPedidoBoleteraA380() throws ServiceException {
		PedidoBoleteraDepositoA380 cs = new PedidoBoleteraDepositoA380();
		cs.setNroBUPAdherente("010001731914300");
		cs.setNroSucursal("119");
		cs.setNroCuenta("19102");
		cs.setCantidad(1);
		try {

			System.out.println("Msg Enviado: " + cs.getMessageToSend("22", ""));
			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("22", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testPedidoChequeraA380() throws ServiceException {
		PedidoChequeraA380 cs = new PedidoChequeraA380();
		cs.setNroBUPAdherente("010001731914300");
		cs.setNroSucursal("119");
		cs.setNroCuenta("19102");
		cs.setCantidad(1);
		cs.setTipoCheque(" ");
		try {

			System.out.println("Msg Enviado: " + cs.getMessageToSend("21", ""));
			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("21", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testTransferenciasCuentasPropiasA380() throws ServiceException {
		TransferenciaCuentasPropiasA380 trcp = new TransferenciaCuentasPropiasA380();
		trcp.setMonto(1);
		trcp.setNroSucursalOrigen("119");
		trcp.setNroCuentaOrigen("19102");
		trcp.setNroSucursalDestino("070");
		trcp.setNroCuentaDestino("34347");
		trcp.setNroBUPAdherente("010001731914300");

		try {
			System.out.println("Msg Enviado: "
					+ trcp.getMessageToSend("22", "22"));
			ResponseTO response = this.sendMessage(trcp.getMessageToSend("22",
					"22"));

			Hashtable h1 = trcp.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Retornado: " + h1.toString());
			if (Integer.parseInt(h1.get("D.BINRPTA.1").toString()) > 0) {
				System.out.println("Msg Retornado: "
						+ A380ErrorFactory.obtenerA380Error(
								String.format("%05d", Integer.parseInt(h1.get(
										"D.BINRPTA.1").toString())))
								.getDescripcion());
			}
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testPedidoDuplicadoResumenA380() throws ServiceException {
		PedidoDuplicadoResumenA380 cs = new PedidoDuplicadoResumenA380();
		cs.setNroBUPAdherente("010001731914300");
		cs.setNroSucursal("119");
		cs.setNroCuenta("19102");
		cs.setCantidad(1);
		try {

			System.out.println("Msg Enviado: " + cs.getMessageToSend("21", ""));
			ResponseTO response = this.sendMessage(cs
					.getMessageToSend("21", ""));

			Hashtable h1 = cs.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testPagoTarjetaCabalA380() throws ServiceException {
		PagoTarjetaCabalA380 pg = new PagoTarjetaCabalA380();
		pg.setNroSucursalDebito("020");
		pg.setNroCuentaDebito("5499384");
		pg.setMonto(100);
		pg.setNroBUPAdherente("010002430811300");
		pg.setNroTarjetaCabal("5896570052057096");

		ResponseTO response;
		try {
			response = this.sendMessage(pg.getMessageToSend(FsFaTaFactory
					.obtenerFuncion(String.valueOf(519)).getFa(), FsFaTaFactory
					.obtenerFuncion(String.valueOf(519)).getTa()));

			Hashtable h1 = pg.getOutputMsg().parseMessage(response.getData());
			System.out.println("Msg Retornado: " + response.getData());
			System.out.println("Msg Parser Retornado: " + h1.toString());
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ResponseTO sendMessage(String toSend) throws ServiceException {
		testLoadService();
		ResponseTO response = null;
		try {
			MessageBase output = service.send(MessageImpl.newInstance(toSend));
			response = (ResponseTO) XStreamSerializer.getInstance()
					.deserialize(output.getText());

		} catch (ServiceException e) {
			e.printStackTrace();
			fail("El ESB Lanzo una exception " + e.getInteractionStage());
		}
		return response;
	}
}
