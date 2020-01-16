package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import condition.condition;
import context.ContextVar;

import step.StepConditional;
import step.StepCounter;
import step.StepFactory;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayDigits;
import step.StepValidateCuit;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideCuitCredicoop implements StepGroup {

	protected StepGroupType GroupType;
	private String audioCuit;
	private String audioCuitInvalido;
	private String audioValidateCuit;
	private String audioSuCuitEs;
	private ContextVar confirmaCuitContextVar;
	private ContextVar intentosCuitContextVar;
	private StepPlayRead stepAudioCuit;
	private StepPlayRead stepAudioValidate;
	private StepPlay stepAudioSuCuitEs;
	private StepPlay stepAudioCuitInvalido;
	private StepSayDigits stepDiceCuit;
	private StepCounter contadorIntentosCuit;
	private StepConditional evalContadorCuit;
	private StepMenu stepMenuConfirmacionMenu;
	private StepValidateCuit validaCuit;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private ContextVar cuitContextVar;

	private void setSequence() {

		/* Ingreso Cuit */

		stepAudioCuit.setNextstep(validaCuit.GetId());

		/* Cuit valido */

		validaCuit.setNextStepIsTrue(stepIfTrueUUID);

		// stepAudioSuCuitEs.setNextstep(stepDiceCuit.GetId());
		//
		// stepDiceCuit.setNextstep(stepAudioValidate.GetId());
		//
		// stepAudioValidate.setNextstep(stepMenuConfirmacionMenu.GetId());
		//
		// stepMenuConfirmacionMenu.addSteps("1", stepIfTrueUUID);
		// stepMenuConfirmacionMenu.addSteps("2", contadorIntentosCuit.GetId());
		// stepMenuConfirmacionMenu.setInvalidOption(contadorIntentosCuit.GetId());

		/* Cuit Invalido */

		validaCuit.setNextStepIsFalse(stepAudioCuitInvalido.GetId());

		stepAudioCuitInvalido.setNextstep(contadorIntentosCuit.GetId());

		contadorIntentosCuit.setNextstep(evalContadorCuit.GetId());
		evalContadorCuit.addCondition(new condition(1, "#{"
				+ intentosCuitContextVar.getVarName() + "} < " + intentos,
				stepAudioCuit.GetId(), stepIfFalseUUID));

	}

	public PideCuitCredicoop() {
		super();

		GroupType = StepGroupType.pideCuit;

		/*--- Audios --- */

		stepAudioValidate = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioValidate.setStepDescription("PLAYREAD => CUIT CORRECTO");
		stepAudioValidate.setPlayMaxDigits(1);
		stepAudioValidate.setPlayTimeout(2000L);

		stepAudioCuit = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCuit.setStepDescription("PLAYREAD => INGRESO CUIT");
		stepAudioCuit.setPlayMaxDigits(11);
		stepAudioCuit.setPlayTimeout(2000L);

		stepAudioSuCuitEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuCuitEs.setStepDescription("PLAY => SU CUIT ES");

		stepAudioCuitInvalido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCuitInvalido.setStepDescription("PLAY => CUIT INVALIDO");

		stepDiceCuit = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceCuit.setStepDescription("SAYDIGITS => LOCUCIONA CUIT");

		/*--- Contadores --- */

		contadorIntentosCuit = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosCuit.setStepDescription("COUNTER => INTENTOS CUIT");

		/*--- Validaciones --- */

		validaCuit = (StepValidateCuit) StepFactory.createStep(
				StepType.ValidateCuit, UUID.randomUUID());
		validaCuit.setStepDescription("VALIDATECuit => FORMATO CUIT VALIDO");

		/*--- Condicional --- */

		evalContadorCuit = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorCuit
				.setStepDescription("CONDITIONAL => CANTIDAD DE INTENTOS CUIT");

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu.setStepDescription("MENU => CONFIRMA CUIT");

		/*--- Actualizo --- */

		Steps.put(stepAudioCuit.GetId(), stepAudioCuit);
		Steps.put(validaCuit.GetId(), validaCuit);
		Steps.put(contadorIntentosCuit.GetId(), contadorIntentosCuit);
		Steps.put(evalContadorCuit.GetId(), evalContadorCuit);
		Steps.put(stepAudioValidate.GetId(), stepAudioValidate);
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);
		Steps.put(stepAudioSuCuitEs.GetId(), stepAudioSuCuitEs);
		Steps.put(stepDiceCuit.GetId(), stepDiceCuit);
		Steps.put(stepAudioCuitInvalido.GetId(), stepAudioCuitInvalido);

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioCuit.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioCuit.isEmpty() || audioCuitInvalido.isEmpty()
				|| audioValidateCuit.isEmpty() || audioSuCuitEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (cuitContextVar == null || confirmaCuitContextVar == null
				|| intentosCuitContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfTrueUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioCuit(String audioCuit) {
		this.audioCuit = audioCuit;
		stepAudioCuit.setPlayFile(audioCuit);
	}

	public void setAudioCuitInvalido(String audioCuitInvalido) {
		this.audioCuitInvalido = audioCuitInvalido;
		stepAudioCuitInvalido.setPlayfile(audioCuitInvalido);
	}

	public void setAudioValidateCuit(String audioValidateCuit) {
		this.audioValidateCuit = audioValidateCuit;
		stepAudioValidate.setPlayFile(audioValidateCuit);
	}

	public void setAudioSuCuitEs(String audioSuCuitEs) {
		this.audioSuCuitEs = audioSuCuitEs;
		stepAudioSuCuitEs.setPlayfile(audioSuCuitEs);
	}

	public void setCuitContextVar(ContextVar CuitContextVar) {
		this.cuitContextVar = CuitContextVar;
		stepAudioCuit.setContextVariableName(CuitContextVar);
		validaCuit.setContextVariableName(CuitContextVar);
		stepDiceCuit.setContextVariableName(CuitContextVar);
	}

	public void setConfirmaCuitContextVar(ContextVar confirmaCuitContextVar) {
		this.confirmaCuitContextVar = confirmaCuitContextVar;
		stepAudioValidate.setContextVariableName(confirmaCuitContextVar);
		stepMenuConfirmacionMenu.setContextVariableName(confirmaCuitContextVar);
	}

	public void setIntentosCuitContextVar(ContextVar intentosCuitContextVar) {
		this.intentosCuitContextVar = intentosCuitContextVar;
		contadorIntentosCuit.setContextVariableName(intentosCuitContextVar);
	}

	public void setContadorIntentosCuit(StepCounter contadorIntentosCuit) {
		this.contadorIntentosCuit = contadorIntentosCuit;
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

	public ContextVar getCuitContextVar() {
		return cuitContextVar;
	}

}
