/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import main.Daemon;

import org.apache.log4j.Level;

import com.bea.xml.stream.samples.Parse;

import context.ContextVar;
import coop.bancocredicoop.esb.integration.aerolineas.ConsultaEstadoAerolineasPlusResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.integration.sipct.SipctService;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.service.proxy.ProxyFactory;
import coop.bancocredicoop.sipct.dto.ChequeDTO;
import coop.bancocredicoop.sipct.dto.FiltroChequesDTO;
import coop.bancocredicoop.sipct.dto.MessageBuscarCheques;
import coop.bancocredicoop.sipct.dto.MessageObtenerCheque;
import coop.bancocredicoop.sipct.dto.MessageObtenerSucursales;
import ivr.CallContext;
import utils.Usuariocuenta;
import workflow.Context;

/**
 * 
 * @author Avila Daniel
 */

public class StepFileExist extends Step {

	private ContextVar numeroDeFilialContextVar;
	private UUID nextStepIsFalse;
	private UUID nextStepIsTrue;
	private String directorio;
	private String scapeDigitTmp;

	public StepFileExist(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.FileExist;
	}

	// /opt/asterisk/var/lib/asterisk/sounds/PCT/filial
	@Override
	public boolean execute(Context context) throws Exception {
		if (numeroDeFilialContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		String scapeDigitTmp = "";

		String sucursal = numeroDeFilialContextVar.getVarValue().toString();

		File audio = new File(directorio + sucursal + ".wav").getAbsoluteFile();

		if (audio.exists()) {

			scapeDigitTmp = String.valueOf(
					((CallContext) context).getChannel().streamFile(
							"PCT/" + sucursal, "1234567890#*")).trim();

			this.setNextstep(nextStepIsTrue);

		} else {

			scapeDigitTmp = String.valueOf(
					((CallContext) context).getChannel().streamFile(
							"PCT/P010" , "1234567890#*")).trim();
			
			this.setNextstep(nextStepIsFalse);
		}
		return false;
	}

	public void setNumeroDeFilialContextVar(ContextVar numeroDeFilialContextVar) {
		this.numeroDeFilialContextVar = numeroDeFilialContextVar;
	}

	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}

	public UUID getNextStepIsTrue() {
		return nextStepIsTrue;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public UUID getNextStepIsFalse() {
		return nextStepIsFalse;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}
}
