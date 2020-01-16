package test;

import main.Daemon;

import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.junit.Test;

import sql.querys.SecuenciaA380;

import coop.bancocredicoop.common.domain.date.DateUnit;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.domain.time.TimeUnit;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.ConsultaTarjetaDebitoSocioRequest;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.ConsultaTarjetaDebitoSocioResponse;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.TarjetaDebitoResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.templating.ParseException;

public class TestTarjetasDebito {
	@Test
	public void TestTarjetasDeUnSocio() {

		ConsultaTarjetaDebitoSocioRequest requestOk;
		try {
			requestOk = this.getRequest(String.valueOf("1000036665"));

			MessageSender sender = (MessageSender) ServiceProvider
					.getInstance().lookupService(
							"msgConsultaTarjetasDebitoDeSocio");
			ConsultaTarjetaDebitoSocioResponse response = (ConsultaTarjetaDebitoSocioResponse) sender
					.send(requestOk, ConsultaTarjetaDebitoSocioResponse.class);

			for (TarjetaDebitoResponse tarjeta : response.getTarjetas()) {

				System.out.println(tarjeta.getIdTarjeta());
			}
		} catch (Exception e) {
			Daemon.getMiLog().log(Level.ERROR,
					"TARJETASDEBITOSXSOCIO|Error|" + e.getMessage());
		}
	}

	private ConsultaTarjetaDebitoSocioRequest getRequest(String idcrecer)
			throws ParseException, java.text.ParseException {

		ConsultaTarjetaDebitoSocioRequest request = new ConsultaTarjetaDebitoSocioRequest();
		request.setTipoMensaje("CO");
		request.setNroSecuencia(new Numeric(SecuenciaA380
				.obtenerSecuenciaA380()));
		request.setFechaNegocio(new DateUnit(new DateTime()
				.toString("yyyyMMdd")));
		request.setFechaProceso(new DateUnit(new DateTime()
				.toString("yyyyMMdd")));
		request.setFechaTx(new DateUnit(new DateTime().toString("yyyyMMdd")));
		request.setHoraProceso(new TimeUnit(new DateTime().toString("HH:mm:ss")));
		request.setIdOperador(new Numeric(0));
		request.setIdCanal("BIE");
		request.setIdESB("");
		request.setTarjeta(new Numeric("3156"
				+ idcrecer.substring(Math.max(0, idcrecer.length() - 9))));
		request.setIdSocio(idcrecer);

		return request;

	}
}
