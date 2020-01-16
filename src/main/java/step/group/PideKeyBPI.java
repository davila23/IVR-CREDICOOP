package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.StepAuthInitialInfo;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayNumber;
import step.StepSetAsteriskVariable;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.StepValidateKeyBPI;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideKeyBPI implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	protected UUID stepIfTrueUUID;
	protected UUID stepIfFalseUUID;
	protected CallContext ctxVar;
	protected StepPlay stepAudioReingresoClaveInvalida;
	protected StepPlay stepAudioClaveInvalida;
	protected StepPlayRead stepAudioClave;
	protected StepCounter contadorIntentosClave;
	protected StepCounter contadorIntentosClaveVacia;
	protected StepConditional evalContadorClave;
	protected StepConditional evalContadorClaveVacia;
	protected StepConditional evalClaveOK;
	protected StepConditional evalClaveErronea;
	protected StepConditional evalCambioClaveVacio;
	protected StepPlay stepAudioFinal;
	protected StepEnd pasoFinal;
	protected StepValidateKeyBPI stepValidateKeyBPI;
	protected StepPlayRead stepAudioReingresoClave;
	protected StepConditional evalReingresoCambioClave;
	protected StepCounter contadorIntentosReingresoClaveVacia;
	protected StepConditional evalContadorReingresoClaveVacia;
	protected StepConditional evalClavesIguales;
	protected StepPlay stepAudioClavesNoCoinciden;

	private void setSequence() {

		/* Ingreso Clave */

		stepAudioClave.setNextstep(evalCambioClaveVacio.GetId());

		evalCambioClaveVacio.addCondition(new condition(1, "equals('#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "}','')", contadorIntentosClaveVacia.GetId(),
				stepValidateKeyBPI.GetId()));

		contadorIntentosClaveVacia.setNextstep(evalContadorClaveVacia.GetId());

		evalContadorClaveVacia.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveVaciaContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepIfFalseUUID));

		/* Ingreso Invalido (nros Consecutivos o iguales) */

		stepAudioClaveInvalida.setNextstep(contadorIntentosClave.GetId());

		contadorIntentosClave.setNextstep(evalContadorClave.GetId());
		evalContadorClave.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepAudioFinal.GetId()));

		stepAudioFinal.setNextstep(pasoFinal.GetId());

		stepValidateKeyBPI.setNextStepIsFalse(stepAudioClaveInvalida.GetId());

		/* Valida formato de la Clave */

		stepValidateKeyBPI.setNextStepIsTrue(stepAudioReingresoClave.GetId());

		stepAudioReingresoClave.setNextstep(evalReingresoCambioClave.GetId());

		evalReingresoCambioClave.addCondition(new condition(1, "equals('#{"
				+ ctxVar.getContextVarByName("claveReingresoContextVar")
						.getVarName() + "}','')",
				contadorIntentosReingresoClaveVacia.GetId(),
				evalClavesIguales.GetId()));

		contadorIntentosReingresoClaveVacia
				.setNextstep(evalContadorReingresoClaveVacia.GetId());

		evalContadorReingresoClaveVacia.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosReingresoClaveVaciaContextVar").getVarName()
				+ "} < " + intentos, stepAudioReingresoClave.GetId(),
				stepAudioFinal.GetId()));

		evalClavesIguales.addCondition((new condition(1, "#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "} == #{"
				+ ctxVar.getContextVarByName("claveReingresoContextVar")
						.getVarName() + "}", stepIfTrueUUID,
				stepAudioClavesNoCoinciden.GetId())));

		/* Ingreso 2 claves diferentes, va al inicio */

		stepAudioClavesNoCoinciden.setNextstep(stepAudioClave.GetId());

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {

		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioClave.GetId();
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

	public PideKeyBPI() {
		super();

		GroupType = StepGroupType.pideKeyBPI;
	}

	private void createSteps() {
		/*--- Play Read --- */

		stepAudioClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioClave.setStepDescription("PLAYREAD => INGRESO CLAVE BPI");
		stepAudioClave.setPlayMaxDigits(6);
		stepAudioClave.setPlayTimeout(5000L);
		stepAudioClave.setPlayFile("PREATENDEDORCABAL/053");
		stepAudioClave.setContextVariableName(ctxVar
				.getContextVarByName("claveContextVar"));
		Steps.put(stepAudioClave.GetId(), stepAudioClave);

		stepAudioReingresoClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioReingresoClave
				.setStepDescription("PLAYREAD => REINGRESO CLAVE BPI");
		stepAudioReingresoClave.setPlayMaxDigits(6);
		stepAudioReingresoClave.setPlayTimeout(5000L);
		stepAudioReingresoClave.setPlayFile("PREATENDEDORCABAL/054");
		stepAudioReingresoClave.setContextVariableName(ctxVar
				.getContextVarByName("claveReingresoContextVar"));
		Steps.put(stepAudioReingresoClave.GetId(), stepAudioReingresoClave);

		/*--- Audios --- */

		stepAudioClavesNoCoinciden = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClavesNoCoinciden.setPlayfile("PREATENDEDORCABAL/057");
		stepAudioClavesNoCoinciden
				.setStepDescription("PLAY => LAS CLAVES INGRESADAS NO COINCIDEN");
		Steps.put(stepAudioClavesNoCoinciden.GetId(),
				stepAudioClavesNoCoinciden);

		stepAudioClaveInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveInvalida.setStepDescription("PLAY => CLAVE INVALIDA");
		stepAudioClaveInvalida.setPlayfile("PREATENDEDORCABAL/056");
		Steps.put(stepAudioClaveInvalida.GetId(), stepAudioClaveInvalida);

		stepAudioReingresoClaveInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioReingresoClaveInvalida
				.setStepDescription("PLAY => CLAVE INCORRECTA");
		stepAudioReingresoClaveInvalida.setPlayfile("PREATENDEDORCABAL/002");
		Steps.put(stepAudioReingresoClaveInvalida.GetId(),
				stepAudioReingresoClaveInvalida);

		/*--- Contadores --- */

		contadorIntentosClave = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClave.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveContextVar"));
		contadorIntentosClave
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE");
		Steps.put(contadorIntentosClave.GetId(), contadorIntentosClave);

		contadorIntentosClaveVacia = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClaveVacia.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveVaciaContextVar"));
		contadorIntentosClaveVacia
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE VACIA");
		Steps.put(contadorIntentosClaveVacia.GetId(),
				contadorIntentosClaveVacia);

		contadorIntentosReingresoClaveVacia = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosReingresoClaveVacia.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveVaciaContextVar"));
		contadorIntentosReingresoClaveVacia
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE VACIA");
		Steps.put(contadorIntentosReingresoClaveVacia.GetId(),
				contadorIntentosReingresoClaveVacia);

		/*--- Steps Validaciones --- */

		stepValidateKeyBPI = (StepValidateKeyBPI) StepFactory.createStep(
				StepType.ValidateKeyBPI, UUID.randomUUID());
		stepValidateKeyBPI.setClaveContextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		Steps.put(stepValidateKeyBPI.GetId(), stepValidateKeyBPI);

	

		/*--- Condicional --- */

		evalContadorClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorClave
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE");
		Steps.put(evalContadorClave.GetId(), evalContadorClave);

		evalClavesIguales = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClavesIguales
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE");
		Steps.put(evalClavesIguales.GetId(), evalClavesIguales);

		evalReingresoCambioClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalReingresoCambioClave
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE");
		Steps.put(evalReingresoCambioClave.GetId(), evalReingresoCambioClave);

		evalContadorClaveVacia = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorClaveVacia
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE VACIA");
		Steps.put(evalContadorClaveVacia.GetId(), evalContadorClaveVacia);

		evalContadorReingresoClaveVacia = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorReingresoClaveVacia
				.setStepDescription("CONDITIONAL => FECHA DE CADUCIDAD CLAVE");
		Steps.put(evalContadorReingresoClaveVacia.GetId(),
				evalContadorReingresoClaveVacia);

		evalCambioClaveVacio = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCambioClaveVacio
				.setStepDescription("CONDITIONAL => CLAVE VACIA");
		Steps.put(evalCambioClaveVacio.GetId(), evalCambioClaveVacio);

		evalClaveErronea = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveErronea.setStepDescription("CONDITIONAL => CLAVE ERRONEA");
		Steps.put(evalClaveErronea.GetId(), evalClaveErronea);

		evalClaveOK = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveOK.setStepDescription("CONDITIONAL => CLAVE OK");
		Steps.put(evalClaveOK.GetId(), evalClaveOK);

		/*--- Paso final --- */

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN DE COMUNICACION");
		Steps.put(pasoFinal.GetId(), pasoFinal);
	}
}
