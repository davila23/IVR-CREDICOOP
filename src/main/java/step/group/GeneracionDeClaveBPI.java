package step.group;

import ivr.CallContext;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import main.Daemon;
import condition.condition;
import step.StepAnswer;
import step.StepCheckCuenta;
import step.StepCheckCuentaEnDialPlan;
import step.StepClearKeyBPI;
import step.StepClearPil;
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepInitDniDB;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSetAsteriskVariable;
import step.StepSwitch;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class GeneracionDeClaveBPI implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	protected UUID stepIfTrueUUID;
	protected UUID stepIfFalseUUID;
	protected CallContext ctxVar;
	protected StepAnswer inicial;
	protected StepEnd pasoFinal;
	protected StepMenu stepMenuIngresoDatosCuenta;
	protected StepMenu stepMenuConfirmacionIngresoRutina;
	protected StepMenu stepMenuCambioDeTarjeta;
	protected StepMenu stepMenuRepetirPIN;
	protected StepPlayRead stepAudioInicio;
	protected StepPlayRead stepAudioRepetirPIN;
	protected StepExecute stepDerivoLlamada;
	protected StepExecute stepDerivoLMenuInicial;
	protected StepExecute stepDerivoAlMenuAnterior;
	protected StepExecute stepVolverAlMenu;
	protected StepPlay stepAudioOKClearBPI;
	protected StepPlay stepAudioVerificarDatos;
	protected StepPlay stepAudioClearBpiOk;
	protected StepPlay stepAudioClaerBpiOkUnaHS;
	protected StepPlay stepAudioErrorClearBpi;
	protected StepPlay stepAudioErrorClearBpiFilial;
	protected StepPlay stepAudioTarjetaNoOperaConBpi;
	protected StepPlay stepAudioDniIncorrecto;
	protected StepPlay stepAudioFechaIncorrecta;
	protected StepPlay stepAudioFinal;
	protected StepPlay stepAudioIngreseUnDigitoValido;
	protected StepPlay stepAudioCantidadMaxDeReintentos;
	protected StepPlay stepAudioCantidadMaxDeReintentos2;
	protected StepPlay stepAudioCuentaPropia;
	protected StepPlay stepAudioDerivoAsesor;
	protected StepPlay stepAudioNumeroDeTarjetaIncorrecto;
	protected StepPlay stepAudioServNoDisponible;
	protected StepPlay stepAudioTarjetaNoVigente;
	protected StepCounter contadorIntentosCuenta;
	protected StepCounter contadorIntentosIngresoRutina;
	protected StepConditional evalContadorIngresoRutina;
	protected StepConditional evalContadorCuenta;
	protected StepGetAsteriskVariable obtieneTarjeta;
	protected StepCheckCuentaEnDialPlan stepCheckCuentaEnDialPlan;
	protected StepInitDniDB initDB;
	protected StepClearPil stepClearPil;
	protected StepIsOwnCard esTarjetaPropia;
	protected StepCheckCuenta checkCuenta;
	protected PideCuenta pideCuentaGrp;
	protected PideFechaCredicoop pideFechaGrp;
	protected PideTarjetaCredicoop pideTarjetaGrp;
	protected StepGetAsteriskVariable obtieneDni;
	protected StepGetAsteriskVariable obtieneIdCrecer;
	protected StepSwitch evalRetornoClearBpi;
	protected StepSetAsteriskVariable stepSetReingreso;
	protected PideDni pideDniGrp;
	protected StepCounter contadorIntentosInit;
	protected StepConditional evalContadorInit;
	protected StepClearKeyBPI stepClearKey;

	private void setSequence() {

		/* --- Secuencia de Ingreso a la Rutina--- */

		stepAudioInicio.setNextstep(stepMenuConfirmacionIngresoRutina.GetId());

		stepMenuConfirmacionIngresoRutina.addSteps("1",
				pideDniGrp.getInitialStep());
		stepMenuConfirmacionIngresoRutina.addSteps("2", stepIfFalseUUID);
		stepMenuConfirmacionIngresoRutina.setInvalidOption(stepIfFalseUUID);

		pideDniGrp.setStepIfTrue(initDB.GetId());
		pideDniGrp.setStepIfFalse(stepAudioFinal.GetId());

		initDB.setNextstep(stepCheckCuentaEnDialPlan.GetId());

		stepCheckCuentaEnDialPlan.setNextStepIsFalse(stepAudioDniIncorrecto
				.GetId());
		stepAudioDniIncorrecto.setNextstep(contadorIntentosInit.GetId());

		contadorIntentosInit.setNextstep(evalContadorInit.GetId());
		evalContadorInit
				.addCondition(new condition(1, "#{"
						+ ctxVar.getContextVarByName("intentosInitContextVar")
								.getVarName() + "} < " + intentos, initDB
						.getNextstep(), stepAudioCantidadMaxDeReintentos
						.GetId()));

		/* --- Tiene cuentas --- */

		stepCheckCuentaEnDialPlan.setNextStepIsTrue(obtieneTarjeta.GetId());

		/* --- Blanqueo el Bpi --- */

		stepClearKey.setNextStepIsTrue(pideFechaGrp.getInitialStep());
		stepClearKey.setNextStepIsFalse(stepAudioErrorClearBpi.GetId());

		/* --- Fecha --- */

		pideFechaGrp.setStepIfTrue(pideTarjetaGrp.getInitialStep());
		pideFechaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Tarjeta --- */

		pideTarjetaGrp.setStepIfTrue(esTarjetaPropia.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Es tarjeta propia --- */

		esTarjetaPropia.setNextStepIsTrue(pideCuentaGrp.getInitialStep());
		esTarjetaPropia.setNextStepIsFalse(stepAudioNumeroDeTarjetaIncorrecto
				.GetId());

		stepAudioNumeroDeTarjetaIncorrecto.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		/* --- Cuenta --- */

		pideCuentaGrp.setStepIfTrue(checkCuenta.GetId());
		pideCuentaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos2.GetId());

		checkCuenta.setNextStepIsTrue(stepClearPil.GetId());
		checkCuenta.setNextStepIsFalse(contadorIntentosCuenta.GetId());

		contadorIntentosCuenta.setNextstep(evalContadorCuenta.GetId());
		evalContadorCuenta.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosCuentaPropiaContextVar")
						.getVarName() + "} < " + intentos,
				stepAudioCuentaPropia.GetId(), stepAudioCantidadMaxDeReintentos
						.GetId()));

		stepAudioCuentaPropia.setNextstep(pideCuentaGrp.getInitialStep());

		/* --- Fin --- */

		evalRetornoClearBpi.setNextstep(stepAudioErrorClearBpi.GetId());

		/* --- Retornos --- */

		stepAudioErrorClearBpi.setNextstep(stepDerivoAlMenuAnterior.GetId());
		stepAudioErrorClearBpiFilial.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		stepAudioTarjetaNoOperaConBpi.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		stepAudioClearBpiOk.setNextstep(stepDerivoAlMenuAnterior.GetId());

		stepAudioClaerBpiOkUnaHS.setNextstep(stepDerivoAlMenuAnterior.GetId());

		stepAudioCantidadMaxDeReintentos.setNextstep(stepDerivoAlMenuAnterior
				.GetId());
		stepAudioCantidadMaxDeReintentos2.setNextstep(stepDerivoLMenuInicial
				.GetId());

		stepAudioFinal.setNextstep(pasoFinal.GetId());

		/* --- Derivo --- */

		stepAudioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {

		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return inicial.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	public void setStepIfTrueUUID(UUID stepIfTrueUUID) {
		this.stepIfTrueUUID = stepIfTrueUUID;
	}

	public void setStepIfFalseUUID(UUID stepIfFalseUUID) {
		this.stepIfFalseUUID = stepIfFalseUUID;
	}

	public void setContextVar(CallContext ctx) {
		this.ctxVar = ctx;
		createSteps();
	}

	public GeneracionDeClaveBPI() {
		super();

		GroupType = StepGroupType.preAtendedorBPI;
	}

	private void createSteps() {

	
	}

	public PideDni getPideDni() {
		return pideDniGrp;
	}
	
	public PideTarjetaCredicoop getPideTarjeta() {
		return pideTarjetaGrp;
	}
	
	public PideFechaCredicoop getPideFecha() {
		return pideFechaGrp;
	}
	
	public PideCuenta getPideCuenta() {
		return pideCuentaGrp;
	}
	
	
}
