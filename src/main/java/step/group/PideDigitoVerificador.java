package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import condition.condition;
import context.ContextVar;

import step.StepConditional;
import step.StepCounter;
import step.StepFactory;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayDigits;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepValidateCardNumber;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideDigitoVerificador implements StepGroup {

	protected StepGroupType GroupType;
	private String audioDigitoVerificador;
	private String audioDigitoVerificadorInvalido;
	private String audioSuDigitoVerificadorEs;
	private ContextVar digitoVerificadorContextVar;
	private ContextVar confirmaDigitoVerificadorContextVar;
	private ContextVar intentosDigitoVerificadorContextVar;
	private StepPlayRead stepAudioDigitoVerificador;
	private StepPlay stepAudioSuDigitoVerificadorEs;
	private StepPlay stepAudioDigitoVerificadorInvalido;
	private StepSayDigits stepDiceDigitoVerificador;
	private StepCounter contadorIntentosDigitoVerificador;
	private StepConditional evalContadorDigitoVerificador;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private StepValidateCardNumber validarCardNumber;

	private void setSequence() {

		/* Ingreso Digito Verificador */

		stepAudioDigitoVerificador.setNextstep(validarCardNumber.GetId());

		/* Digito Verificador valido */

		validarCardNumber.setNextStepIsTrue(stepIfTrueUUID);

		/* Digito Verificador Invalido */

		validarCardNumber.setNextStepIsFalse(stepAudioDigitoVerificadorInvalido
				.GetId());

		stepAudioDigitoVerificadorInvalido
				.setNextstep(contadorIntentosDigitoVerificador.GetId());

		contadorIntentosDigitoVerificador
				.setNextstep(evalContadorDigitoVerificador.GetId());
		evalContadorDigitoVerificador
				.addCondition(new condition(1, "#{"
						+ intentosDigitoVerificadorContextVar.getVarName()
						+ "} < " + intentos,
						stepAudioDigitoVerificador.GetId(), stepIfFalseUUID));
	}

	public PideDigitoVerificador() {
		super();

		GroupType = StepGroupType.pideTarjeta;

		/*--- Audios --- */

		// stepAudioValidate = (StepPlayRead) StepFactory.createStep(
		// StepType.PlayRead, UUID.randomUUID());
		// stepAudioValidate.setStepDescription("Audio Tarjeta valida");
		// stepAudioValidate.setPlayMaxDigits(1);
		// stepAudioValidate.setPlayTimeout(2000L);

		stepAudioDigitoVerificador = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioDigitoVerificador
				.setStepDescription("PLAYREAD => INGRESO DIGITO VERIFICADOR");
		stepAudioDigitoVerificador.setPlayMaxDigits(16);
		stepAudioDigitoVerificador.setPlayTimeout(2000L);

		stepAudioSuDigitoVerificadorEs = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSuDigitoVerificadorEs
				.setStepDescription("PLAY => SU DIGITO VERIFICADOR ES");

		stepAudioDigitoVerificadorInvalido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDigitoVerificadorInvalido
				.setStepDescription("PLAY => DIGITO VERIFICADOR INVALIDO");

		stepDiceDigitoVerificador = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceDigitoVerificador
				.setStepDescription("SAYDIGITS => DICE NUMERO TARJETA");

		/*--- Contadores --- */

		contadorIntentosDigitoVerificador = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosDigitoVerificador
				.setStepDescription("COUNTER => INTENTOS DIGITO VERIFICADOR");

		/*--- Validaciones --- */

		validarCardNumber = (StepValidateCardNumber) StepFactory.createStep(
				StepType.ValidateCardNumber, UUID.randomUUID());
		validarCardNumber
				.setStepDescription("VALIDATECARDNUMBER => VALIDA TARJETA");

		/*--- Condicional --- */

		evalContadorDigitoVerificador = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDigitoVerificador
				.setStepDescription("CONDITIONAL => INTENTOS DIGITO VERIFICADOR");

		/*--- Menu --- */

		// stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
		// StepType.Menu, UUID.randomUUID());
		// stepMenuConfirmacionMenu.setStepDescription("Menu confirma Tarjeta ");

		/*--- Actualizo --- */

		Steps.put(stepAudioDigitoVerificador.GetId(),
				stepAudioDigitoVerificador);
		Steps.put(validarCardNumber.GetId(), validarCardNumber);
		Steps.put(contadorIntentosDigitoVerificador.GetId(),
				contadorIntentosDigitoVerificador);
		Steps.put(evalContadorDigitoVerificador.GetId(),
				evalContadorDigitoVerificador);
		Steps.put(stepAudioSuDigitoVerificadorEs.GetId(),
				stepAudioSuDigitoVerificadorEs);
		Steps.put(stepDiceDigitoVerificador.GetId(), stepDiceDigitoVerificador);
		Steps.put(stepAudioDigitoVerificadorInvalido.GetId(),
				stepAudioDigitoVerificadorInvalido);
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioDigitoVerificador.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioDigitoVerificador.isEmpty()
				|| audioDigitoVerificadorInvalido.isEmpty()
				|| audioSuDigitoVerificadorEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (digitoVerificadorContextVar == null
				|| confirmaDigitoVerificadorContextVar == null
				|| intentosDigitoVerificadorContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioDigitoVerificador(String audioDigitoVerificador) {
		this.audioDigitoVerificador = audioDigitoVerificador;
		stepAudioDigitoVerificador.setPlayFile(audioDigitoVerificador);
	}

	public void setAudioDigitoVerificadorInvalido(
			String audioDigitoVerificadorInvalido) {
		this.audioDigitoVerificadorInvalido = audioDigitoVerificadorInvalido;
		stepAudioDigitoVerificadorInvalido
				.setPlayfile(audioDigitoVerificadorInvalido);
	}

	public void setAudioSuDigitoVerificadorEs(String audioSuDigitoVerificadorEs) {
		this.audioSuDigitoVerificadorEs = audioSuDigitoVerificadorEs;
		stepAudioSuDigitoVerificadorEs.setPlayfile(audioSuDigitoVerificadorEs);
	}

	public void setDigitoVerificadorContextVar(
			ContextVar digitoVerificadorContextVar) {
		this.digitoVerificadorContextVar = digitoVerificadorContextVar;
		stepAudioDigitoVerificador
				.setContextVariableName(digitoVerificadorContextVar);
		validarCardNumber.setContextVariableName(digitoVerificadorContextVar);
		stepDiceDigitoVerificador
				.setContextVariableName(digitoVerificadorContextVar);
	}

	public void setDigitoVerificadorContextVar1(
			ContextVar confirmaDigitoVerificadorContextVar) {
		this.confirmaDigitoVerificadorContextVar = confirmaDigitoVerificadorContextVar;
	}

	public void setIntentosDigitoVerificadorContextVar(
			ContextVar intentosDigitoVerificadorContextVar) {
		this.intentosDigitoVerificadorContextVar = intentosDigitoVerificadorContextVar;
		contadorIntentosDigitoVerificador
				.setContextVariableName(intentosDigitoVerificadorContextVar);
	}

	public void setContadorIntentosDigitoVerificador(
			StepCounter contadorIntentosDigitoVerificador) {
		this.contadorIntentosDigitoVerificador = contadorIntentosDigitoVerificador;
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
