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

public class PideTarjeta implements StepGroup {

	protected StepGroupType GroupType;
	private String audioTarjeta;
	private String audioTarjetaInvalido;
	private String audioSuTarjetaEs;
	private ContextVar tarjetaContexVar;
	private ContextVar confirmaTarjetaContextVar;
	private ContextVar intentosTarjetaContextVar;
	private StepPlayRead stepAudioTarjeta;
	private StepPlay stepAudioSuTarjetaEs;
	private StepPlay stepAudioTarjetaInvalido;
	private StepSayDigits stepDiceTarjeta;
	private StepCounter contadorIntentosTarjeta;
	private StepConditional evalContadorTarjeta;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private StepValidateCardNumber validarCardNumber;

	private void setSequence() {

		/* Ingreso Tarjeta */

		stepAudioTarjeta.setNextstep(validarCardNumber.GetId());

		/* Tarjeta valida */

		validarCardNumber.setNextStepIsTrue(stepIfTrueUUID);

		/* Tarjeta Invalida */

		validarCardNumber.setNextStepIsFalse(stepAudioTarjetaInvalido.GetId());

		stepAudioTarjetaInvalido.setNextstep(contadorIntentosTarjeta.GetId());

		contadorIntentosTarjeta.setNextstep(evalContadorTarjeta.GetId());
		evalContadorTarjeta.addCondition(new condition(1, "#{"
				+ intentosTarjetaContextVar.getVarName() + "} < " + intentos,
				stepAudioTarjeta.GetId(), stepIfFalseUUID));

	}

	public PideTarjeta() {
		super();

		GroupType = StepGroupType.pideTarjeta;

		/*--- Audios --- */

		// stepAudioValidate = (StepPlayRead) StepFactory.createStep(
		// StepType.PlayRead, UUID.randomUUID());
		// stepAudioValidate.setStepDescription("Audio Tarjeta valida");
		// stepAudioValidate.setPlayMaxDigits(1);
		// stepAudioValidate.setPlayTimeout(2000L);

		stepAudioTarjeta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioTarjeta.setStepDescription("PLAYREAD => INGRESO TARJETA");
		stepAudioTarjeta.setPlayMaxDigits(16);
		stepAudioTarjeta.setPlayTimeout(5000L);

		stepAudioSuTarjetaEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuTarjetaEs.setStepDescription("PLAY => SU TARJETA ES");

		stepAudioTarjetaInvalido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaInvalido.setStepDescription("PLAY => TARJETA INVALIDA");

		stepDiceTarjeta = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceTarjeta
				.setStepDescription("SAYDIGITS => LOCUCIONA NUMERO DE TARJETA");

		/*--- Contadores --- */

		contadorIntentosTarjeta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjeta
				.setStepDescription("COUNTER => INTENTOS TARJETA");

		/*--- Validaciones --- */

		validarCardNumber = (StepValidateCardNumber) StepFactory.createStep(
				StepType.ValidateCardNumber, UUID.randomUUID());
		validarCardNumber
				.setStepDescription("VALIDATECARDNUMBER => VALIDA FORMATO TARJETA");

		/*--- Condicional --- */

		evalContadorTarjeta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorTarjeta
				.setStepDescription("CONDITIONAL => INTENTOS TARJETA");

		/*--- Menu --- */

		// stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
		// StepType.Menu, UUID.randomUUID());
		// stepMenuConfirmacionMenu.setStepDescription("Menu confirma Tarjeta ");

		/*--- Actualizo --- */

		Steps.put(stepAudioTarjeta.GetId(), stepAudioTarjeta);
		Steps.put(validarCardNumber.GetId(), validarCardNumber);
		Steps.put(contadorIntentosTarjeta.GetId(), contadorIntentosTarjeta);
		Steps.put(evalContadorTarjeta.GetId(), evalContadorTarjeta);
		Steps.put(stepAudioSuTarjetaEs.GetId(), stepAudioSuTarjetaEs);
		Steps.put(stepDiceTarjeta.GetId(), stepDiceTarjeta);
		Steps.put(stepAudioTarjetaInvalido.GetId(), stepAudioTarjetaInvalido);
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioTarjeta.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioTarjeta.isEmpty() || audioTarjetaInvalido.isEmpty()
				|| audioSuTarjetaEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (tarjetaContexVar == null || confirmaTarjetaContextVar == null
				|| intentosTarjetaContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioTarjeta(String audioTarjeta) {
		this.audioTarjeta = audioTarjeta;
		stepAudioTarjeta.setPlayFile(audioTarjeta);
	}

	public void setAudioTarjetaInvalido(String audioTarjetaInvalido) {
		this.audioTarjetaInvalido = audioTarjetaInvalido;
		stepAudioTarjetaInvalido.setPlayfile(audioTarjetaInvalido);
	}

	public void setAudioSuTarjetaEs(String audioSuTarjetaEs) {
		this.audioSuTarjetaEs = audioSuTarjetaEs;
		stepAudioSuTarjetaEs.setPlayfile(audioSuTarjetaEs);
	}

	public void setTarjetaContextVar(ContextVar tarjetaContexVar) {
		this.tarjetaContexVar = tarjetaContexVar;
		stepAudioTarjeta.setContextVariableName(tarjetaContexVar);
		validarCardNumber.setContextVariableName(tarjetaContexVar);
		stepDiceTarjeta.setContextVariableName(tarjetaContexVar);
	}

	public void setConfirmaTarjetaContextVar(
			ContextVar confirmaTarjetaContextVar) {
		this.confirmaTarjetaContextVar = confirmaTarjetaContextVar;
	}

	public void setIntentosTarjetaContextVar(
			ContextVar intentosTarjetaContextVar) {
		this.intentosTarjetaContextVar = intentosTarjetaContextVar;
		contadorIntentosTarjeta
				.setContextVariableName(intentosTarjetaContextVar);
	}

	public void setContadorIntentosTarjeta(StepCounter contadorIntentosTarjeta) {
		this.contadorIntentosTarjeta = contadorIntentosTarjeta;
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
