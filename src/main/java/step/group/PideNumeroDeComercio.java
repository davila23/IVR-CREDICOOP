package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import main.Daemon;

import condition.condition;
import context.ContextVar;

import step.StepConditional;
import step.StepCounter;
import step.StepExecute;
import step.StepFactory;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayDigits;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepValidateCardNumber;
import step.StepFactory.StepType;
import step.StepValidateCommerceNumber;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideNumeroDeComercio implements StepGroup {

	protected StepGroupType GroupType;
	private String audioComercio;
	private String audioComercioInvalido;
	private String audioSuComercioEs;
	private ContextVar comercioContextVar;
	private ContextVar confirmaComercioContextVar;
	private ContextVar intentosComercioContextVar;
	private StepPlayRead stepAudioComercio;
	private StepPlay stepAudioSuNroDeComercioEs;
	private StepPlay stepAudioComercioInvalido;
	private StepSayDigits stepDiceComercio;
	private StepCounter contadorIntentosComercio;
	private StepConditional evalContadorComercio;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private StepValidateCommerceNumber validarCommerceNumber;
	private StepMenu stepSolicitaOperador;
	private StepPlayRead stepAudioSolicitaOperador;
	private StepPlay stepAudioDerivoAsesor;
	private StepExecute stepDerivoLlamada;
	private StepConditional evalIntentosDerivo;
	private StepCounter contadorIntentosDerivo;
	private ContextVar intentosSolicitaOperadorContextVar;

	private void setSequence() {

		/* Ingreso Numero Comercio */

		stepAudioComercio.setNextstep(validarCommerceNumber.GetId());

		/* Comercio valido */

		validarCommerceNumber.setNextStepIsTrue(stepAudioSolicitaOperador
				.GetId());

		stepAudioSolicitaOperador.setNextstep(stepSolicitaOperador.GetId());

		stepSolicitaOperador.addSteps("1", stepIfTrueUUID);
		stepSolicitaOperador.addSteps("2", stepAudioDerivoAsesor.GetId());
		stepSolicitaOperador.setInvalidOption(contadorIntentosDerivo.GetId());

		contadorIntentosDerivo.setNextstep(evalIntentosDerivo.GetId());
		evalIntentosDerivo
				.addCondition(new condition(1, "#{"
						+ intentosSolicitaOperadorContextVar.getVarName()
						+ "} < " + intentos, stepAudioSolicitaOperador.GetId(),
						stepIfFalseUUID));

		stepAudioDerivoAsesor.setNextstep(stepAudioDerivoAsesor.GetId());

		/* Comercio Invalido */

		validarCommerceNumber.setNextStepIsFalse(stepAudioComercioInvalido
				.GetId());

		stepAudioComercioInvalido.setNextstep(contadorIntentosComercio.GetId());

		contadorIntentosComercio.setNextstep(evalContadorComercio.GetId());
		evalContadorComercio.addCondition(new condition(1, "#{"
				+ intentosComercioContextVar.getVarName() + "} < " + intentos,
				stepAudioComercio.GetId(), stepIfFalseUUID));

	}

	public PideNumeroDeComercio() {
		super();

		GroupType = StepGroupType.pideNumeroDeComercio;

		/*--- Audios --- */

		stepAudioComercio = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioComercio
				.setStepDescription("PLAYREAD => INGRESO NUMERO DE COMERCIO");
		stepAudioComercio.setPlayMaxDigits(11);
		stepAudioComercio.setPlayTimeout(2000L);

		stepAudioSolicitaOperador = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSolicitaOperador
				.setStepDescription("PLAYREAD => SOLICITA OPERADOR");
		stepAudioSolicitaOperador.setPlayMaxDigits(11);
		stepAudioSolicitaOperador.setPlayTimeout(2000L);

		stepAudioSuNroDeComercioEs = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSuNroDeComercioEs
				.setStepDescription("PLAY => SU NUMERO DE COMERCIO ES");

		stepAudioComercioInvalido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioComercioInvalido
				.setStepDescription("PLAY => COMERCIO INVALIDO");

		stepDiceComercio = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceComercio.setStepDescription("SAYDIGITS => NUMERO DE COMERCIO");

		/*--- Contadores --- */

		contadorIntentosComercio = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosComercio
				.setStepDescription("COUNTER => INTENTOS NUMERO DE COMERCIO");

		/*--- Contadores --- */

		contadorIntentosDerivo = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosDerivo.setStepDescription("COUNTER => INTENTOS DERIVO");

		/*--- Validaciones --- */

		validarCommerceNumber = (StepValidateCommerceNumber) StepFactory
				.createStep(StepType.ValidateCommerce, UUID.randomUUID());
		validarCommerceNumber
				.setStepDescription("VALIDATECOMMERCENUMBER => NUMERO DE COEMRCIO VALIDO");

		/*--- Condicional --- */

		evalContadorComercio = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorComercio
				.setStepDescription("CONDITIONAL => INTENTOS NUMERO COMERCIO");

		evalIntentosDerivo = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalIntentosDerivo.setStepDescription("CONDITIONAL => INTENTOS DERIVO");
		/*--- Menu --- */

		stepSolicitaOperador = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepSolicitaOperador.setStepDescription("MENU => SOLICITA OPERADOR");

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("coto/A000013");
		stepAudioDerivoAsesor.setStepDescription("PLAY => DERIVO ASESOR");

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORCOTO"));
		stepDerivoLlamada.setStepDescription("EXECUTE => DERIVO OPERADOR COTO");

		/*--- Actualizo --- */

		Steps.put(stepAudioComercio.GetId(), stepAudioComercio);
		Steps.put(validarCommerceNumber.GetId(), validarCommerceNumber);
		Steps.put(contadorIntentosComercio.GetId(), contadorIntentosComercio);
		Steps.put(evalContadorComercio.GetId(), evalContadorComercio);
		Steps.put(stepAudioSuNroDeComercioEs.GetId(),
				stepAudioSuNroDeComercioEs);
		Steps.put(stepDiceComercio.GetId(), stepDiceComercio);
		Steps.put(stepAudioComercioInvalido.GetId(), stepAudioComercioInvalido);
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioComercio.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioComercio.isEmpty() || audioComercioInvalido.isEmpty()
				|| audioSuComercioEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (comercioContextVar == null || confirmaComercioContextVar == null
				|| intentosComercioContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioTarjeta(String audioComercio) {
		this.audioComercio = audioComercio;
		stepAudioComercio.setPlayFile(audioComercio);
	}

	public void setAudioTarjetaInvalido(String audioComercioInvalido) {
		this.audioComercioInvalido = audioComercioInvalido;
		stepAudioComercioInvalido.setPlayfile(audioComercioInvalido);
	}

	public void setAudioSuComercioEs(String audioSuComercioEs) {
		this.audioSuComercioEs = audioSuComercioEs;
		stepAudioSuNroDeComercioEs.setPlayfile(audioSuComercioEs);
	}

	public void setComercioContextVar(ContextVar comercioContextVar) {
		this.comercioContextVar = comercioContextVar;
		stepAudioComercio.setContextVariableName(comercioContextVar);
		validarCommerceNumber.setContextVariableName(comercioContextVar);
		stepDiceComercio.setContextVariableName(comercioContextVar);
	}

	public void setConfirmaTarjetaContextVar(
			ContextVar confirmaComercioContextVar) {
		this.confirmaComercioContextVar = confirmaComercioContextVar;
	}

	public void setIntentosTarjetaContextVar(
			ContextVar intentosComercioContextVar) {
		this.intentosComercioContextVar = intentosComercioContextVar;
		contadorIntentosComercio
				.setContextVariableName(intentosComercioContextVar);
	}

	public void setIntentosSolicitaOperadorContextVar(
			ContextVar intentosSolicitaOperadorContextVar) {
		this.intentosSolicitaOperadorContextVar = intentosSolicitaOperadorContextVar;
		contadorIntentosDerivo
				.setContextVariableName(intentosSolicitaOperadorContextVar);
	}

	public void setContadorIntentosTarjeta(StepCounter contadorIntentosComercio) {
		this.contadorIntentosComercio = contadorIntentosComercio;
	}

	public void setIntentos(int _intentos) {
		intentos = _intentos;
	}

	public void setStepIfFalse(UUID _stepIfFalseUUID) {
		stepIfFalseUUID = _stepIfFalseUUID;
	}

	public void setStepIfTrue(UUID _stepIfTrueUUID) {
		stepIfTrueUUID = _stepIfTrueUUID;
	}

}
