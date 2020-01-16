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

public class StepClearKeyBPI extends Step {

	private ContextVar numeroAdherenteContextVar;
	private ContextVar dniContextVar;
	private UUID nextStepIsFalse;
	private UUID nextStepIsTrue;
	private List<String> funciones = new ArrayList<String>();
	private ContextVar nombreOperadorContextVar;
	private ContextVar idCrecerEmpresaContextVar;
	private ContextVar claveContextVar;
	private ContextVar idLlamadaContexVar;

	public StepClearKeyBPI(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ClearKeyBPI;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		if (claveContextVar == null || dniContextVar == null
				|| idLlamadaContexVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		{
			OperadorService proxy = (OperadorService) ProxyFactory
					.createServiceProxy(OperadorService.class,
							"xstreamChannelInvocationHandler", false);

			ActualizacionClaveRequest request = new ActualizacionClaveRequest();

			request.setMedio(MEDIOS.BANCA_TELEFONICA);

			ClaveBup claveBupOperador = new ClaveBup();

			claveBupOperador.setNumeroDocumento(dniContextVar.getVarValue());
			claveBupOperador.setCodProvincia(new Numeric("00"));
			claveBupOperador.setTipoDocumento(new Numeric("01"));

			request.setClaveBup(claveBupOperador);
			request.setPass(claveContextVar.getVarValue());
			request.setSessionID(idLlamadaContexVar.getVarValue());
			request.setUsuarioIVR("USERBANCATELEFONICA");
			request.setPassAplicacion("JUE07DECXVII");
			request.setUserAplicacion("USERBANCATELEFONICA");

			ActualizacionClaveResponse response = proxy
					.actualizarClaveBcaTelefonica(request);

			if (response.getCodigoResultado() == 1) {

				this.setNextstep(nextStepIsTrue);

			} else

				this.setNextstep(nextStepIsFalse);
		}
		return false;
	}

	public void setNumeroAdherenteContextVar(
			ContextVar numeroAdherenteContextVar) {
		this.numeroAdherenteContextVar = numeroAdherenteContextVar;
	}

	public void setClaveContextVar(ContextVar claveContextVar) {
		this.claveContextVar = claveContextVar;
	}

	public void setIdLlamadaContexVar(ContextVar idLlamadaContexVar) {
		this.idLlamadaContexVar = idLlamadaContexVar;
	}

	public void setNombreOperadorContextVar(ContextVar nombreOperadorContextVar) {
		this.nombreOperadorContextVar = nombreOperadorContextVar;
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
