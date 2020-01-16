/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import context.ContextVar;
import coop.bancocredicoop.banca.exposition.adherente.ActualizacionClaveRequest;
import coop.bancocredicoop.banca.exposition.adherente.AdherenteService;
import coop.bancocredicoop.banca.exposition.adherente.DataAdherente;
import coop.bancocredicoop.banca.exposition.adherente.ListaDataAdherenteRequest;
import coop.bancocredicoop.banca.exposition.adherente.ListaDataAdherenteResponse;
import coop.bancocredicoop.banca.exposition.adherente.NumeroAdherenteRequest;
import coop.bancocredicoop.banca.exposition.operador.ActualizacionClaveResponse;
import coop.bancocredicoop.banca.exposition.operador.OperadorService;
import coop.bancocredicoop.banca.exposition.operador.PermisosOperadorResponse;
import coop.bancocredicoop.common.domain.banco.ClaveBup;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.dto.constantes.MediosDeAccesoConstantes.MEDIOS;
import coop.bancocredicoop.service.proxy.ProxyFactory;
import workflow.Context;

/**
 * 
 * @author Avila Daniel
 */

public class StepCheckServiceBanca extends Step {

	private ContextVar dniContextVar;
	private UUID nextStepIsFalse;
	private UUID nextStepIsTrue;
	

	public StepCheckServiceBanca(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ServiceBanca;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		if (dniContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		{
			AdherenteService proxy = (AdherenteService) ProxyFactory
					.createServiceProxy(AdherenteService.class,
							"xstreamChannelInvocationHandler", false);

			ListaDataAdherenteRequest request = new ListaDataAdherenteRequest();

			request.setMedio(MEDIOS.BANCA_TELEFONICA);

			ClaveBup claveBupOperador = new ClaveBup();

			claveBupOperador.setNumeroDocumento(dniContextVar.getVarValue());
			claveBupOperador.setCodProvincia(new Numeric("00"));
			claveBupOperador.setTipoDocumento(new Numeric("01"));

			request.setClaveBup(claveBupOperador);
			request.setPassApp("testPassExposition");
			request.setUserApp("testUserExposition");

			ListaDataAdherenteResponse response = proxy
					.getListaDatosAdherente(request);

			if (response.getListaDataAherente().size() > 0) {

				this.setNextstep(nextStepIsTrue);

			} else

				this.setNextstep(nextStepIsFalse);
		}
		return false;
	}


	public void setDniContextVar(ContextVar dniContextVar) {
		this.dniContextVar = dniContextVar;
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
