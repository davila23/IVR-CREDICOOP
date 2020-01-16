package test;

import static org.junit.Assert.*;

import org.junit.Test;

import coop.bancocredicoop.common.domain.date.DateUnit;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.domain.time.TimeUnit;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.esb.integration.aerolineas.ConsultaEstadoAerolineasPlusRequest;
import coop.bancocredicoop.esb.integration.aerolineas.ConsultaEstadoAerolineasPlusResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.templating.ParseException;

public class TestAeroLineasPlus {

	@Test
	public void testAeroLineasRequestAndResponseOK() throws ParseException,
			ServiceException, java.text.ParseException {

		ConsultaEstadoAerolineasPlusRequest requestOk = this.getRequest();
		requestOk.setFechaNegocio(new DateUnit("20150112"));
		requestOk.setIdCrecer(new Numeric("1001112999"));

		MessageSender sender = (MessageSender) ServiceProvider.getInstance()
				.lookupService("msgAeroLineasPlus");
		ConsultaEstadoAerolineasPlusResponse response = (ConsultaEstadoAerolineasPlusResponse) sender
				.send(requestOk, ConsultaEstadoAerolineasPlusResponse.class);

		assertNotNull(response);
		// assertEquals(new Boolean(true), response.getInfo().isAdherido());

	}

	private ConsultaEstadoAerolineasPlusRequest getRequest()
			throws ParseException, java.text.ParseException {

		ConsultaEstadoAerolineasPlusRequest request = new ConsultaEstadoAerolineasPlusRequest();
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
