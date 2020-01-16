package test;

import static org.junit.Assert.*;

import org.junit.Test;

import coop.bancocredicoop.common.domain.date.DateUnit;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.domain.time.TimeUnit;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.esb.integration.crecerxxi.cuenta.ClarificacionCuentaRequest;
import coop.bancocredicoop.esb.integration.crecerxxi.cuenta.ClarificacionCuentaResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.templating.ParseException;

public class TestClarificacionCuenta {
	@Test
	public void testRequestAndResponseOK() throws ParseException,
			ServiceException, java.text.ParseException {

		ClarificacionCuentaRequest requestOk = this.getRequest();
		requestOk.setFecNego(new DateUnit("20150112"));
		requestOk.setIdPersona(new Numeric("1000744232"));

		MessageSender sender = (MessageSender) ServiceProvider.getInstance()
				.lookupService("msgClarificacionCuenta");
		ClarificacionCuentaResponse response = (ClarificacionCuentaResponse) sender
				.send(requestOk, ClarificacionCuentaResponse.class);

		assertNotNull(response);
		assertEquals(new Numeric("10010129719"), response
				.getClarificacionesDeCuentas().get(0).getCuenta());
		assertEquals(new DateUnit("19940118").getDate(), response
				.getClarificacionesDeCuentas().get(0)
				.getFechaDeAltaDeLaCuenta().getDate());
		assertEquals("OK", response.getClarificacionesDeCuentas().get(0)
				.getMessages());

	}

	private ClarificacionCuentaRequest getRequest() throws ParseException,
			java.text.ParseException {

		ClarificacionCuentaRequest request = new ClarificacionCuentaRequest();
		request.setTipoMensaje("TR");
		request.setNroSecuencia(new Numeric(101011));
		request.setFechaNegocio(new DateUnit("20150112"));
		request.setFechaProceso(new DateUnit("20150112"));
		request.setHoraProceso(new TimeUnit("15:33:04"));
		request.setIdOperador(new Numeric(0));
		request.setIdCanal("WEB");
		request.setIdESB("");

		return request;

	}
}
