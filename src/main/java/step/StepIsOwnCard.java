/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import main.Daemon;

import org.apache.log4j.Level;
import org.joda.time.DateTime;

import sql.querys.SecuenciaA380;

import context.ContextVar;
import coop.bancocredicoop.common.domain.date.DateUnit;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.domain.time.TimeUnit;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.ConsultaTarjetaDebitoSocioRequest;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.ConsultaTarjetaDebitoSocioResponse;
import coop.bancocredicoop.esb.integration.crecerxxi.tarjeta.debito.TarjetaDebitoResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.templating.ParseException;
import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author
 */
public class StepIsOwnCard extends Step {

	private ContextVar tarjetaContextVariableName = null;
	private ContextVar idCrecerContextVariableName = null;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private String idCrecer;
	private String tarjeta;

	public StepIsOwnCard(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.IsOwnCard;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (tarjetaContextVariableName == null
				|| idCrecerContextVariableName == null) {
			throw new Exception("INVALID CONTEXT VARIABLE");
		} else {
			tarjeta = ((ContextVar) context.get(tarjetaContextVariableName
					.getId())).getVarValue();
			idCrecer = ((ContextVar) context.get(idCrecerContextVariableName
					.getId())).getVarValue();

			boolean esPropia = esTarjetaPropia(idCrecer, tarjeta);
			if (esPropia)
				this.setNextstep(nextStepIsTrue);
			else
				this.setNextstep(nextStepIsFalse);
		}
		return false;
	}

	public boolean esTarjetaPropia(String idCrecer, String sTarjeta) {
		boolean retval = false;
		ConsultaTarjetaDebitoSocioRequest requestOk;
		try {
			requestOk = this.getRequest(String.valueOf(idCrecer));

			MessageSender sender = (MessageSender) ServiceProvider
					.getInstance().lookupService(
							"msgConsultaTarjetasDebitoDeSocio");
			ConsultaTarjetaDebitoSocioResponse response = (ConsultaTarjetaDebitoSocioResponse) sender
					.send(requestOk, ConsultaTarjetaDebitoSocioResponse.class);

			for (TarjetaDebitoResponse tarjeta : response.getTarjetas()) {
				if (tarjeta.getIdTarjeta().toString().equals(sTarjeta.trim())) {
					retval = true;
				}
			}
		} catch (Exception e) {
			Daemon.getMiLog().log(Level.ERROR,
					"TARJETASDEBITOSXSOCIO|Error|" + e.getMessage());
		}

		return retval;
	}

	public void setTarjetaContextVariableName(
			ContextVar tarjetaContextVariableName) {
		this.tarjetaContextVariableName = tarjetaContextVariableName;
	}

	public void setIdCrecerContextVariableName(
			ContextVar idCrecerContextVariableName) {
		this.idCrecerContextVariableName = idCrecerContextVariableName;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
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
