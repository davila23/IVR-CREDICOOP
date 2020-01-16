/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import main.Daemon;

import org.apache.log4j.Level;

import com.bea.xml.stream.samples.Parse;
import com.cubika.cbank.domain.diferidos.EstadoCheque;

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

public class StepGetCheckPCT extends Step {

	private ContextVar cuitContextVar;
	private ContextVar cantidadDechequesPendientesContextVar;
	private ContextVar numeroDeFilialContextVar;
	private ContextVar listaDeFilialesContextVar;
	private UUID nextStepIsFalse;
	private UUID nextStepIsTrue;
	private List<Long> sucursales = new ArrayList<Long>();
	private int i = 1;
	private String retval = "";

	public StepGetCheckPCT(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.GetCheckPct;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (cuitContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		{

			SipctService proxy = (SipctService) ProxyFactory
					.createServiceProxy(SipctService.class,
							"sipctInvocationHandler", false);

			FiltroChequesDTO filtro = new FiltroChequesDTO();
			filtro.setBeneficiarioCuit(cuitContextVar.getVarValue());
			filtro.setEstado("2");
			MessageBuscarCheques msg = proxy.findBy(filtro);

			if (msg.getLength() > 0) {

				cantidadDechequesPendientesContextVar.setVarValue(Integer
						.toString(msg.getLength()));

				ChequeDTO[] cheques = msg.getResult();

				for (ChequeDTO chequeDTO : cheques) {

					if (!sucursales.contains(chequeDTO.getFilialRetiroId()))
						sucursales.add(chequeDTO.getFilialRetiroId());
				}
				if (sucursales.size() == 1) {

					numeroDeFilialContextVar.setVarValue(Long
							.toString(sucursales.get(0)));

					this.setNextstep(nextStepIsTrue);
				}

				for (Long sucursal : sucursales) {
					retval = retval + Long.toString(sucursal) + "_";

				}

				listaDeFilialesContextVar.setVarValue(retval);

				this.setNextstep(nextStepIsTrue);

			} else

				this.setNextstep(nextStepIsFalse);
		}
		return false;
	}

	public void setCantidacantidadDechequesPendientesContextVar(
			ContextVar cantidadDechequesPendientesContextVar) {
		this.cantidadDechequesPendientesContextVar = cantidadDechequesPendientesContextVar;
	}

	public void setNumeroDeFilialContextVar(ContextVar numeroDeFilialContextVar) {
		this.numeroDeFilialContextVar = numeroDeFilialContextVar;
	}

	public void setListaDeFilialesContextVar(
			ContextVar listaDeFilialesContextVar) {
		this.listaDeFilialesContextVar = listaDeFilialesContextVar;
	}

	public void setCuitContextVar(ContextVar cuitContextVar) {
		this.cuitContextVar = cuitContextVar;
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
