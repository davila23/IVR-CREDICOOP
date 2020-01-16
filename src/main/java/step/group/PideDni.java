package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import condition.condition;
import context.ContextVar;

import step.StepConditional;
import step.StepCounter;
import step.StepFactory;
import step.StepGenerateKeyFromDni;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayDigits;
import step.StepValidateDni;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideDni implements StepGroup {

	protected StepGroupType GroupType;
	private String audioDni;
	private String audioDniInvalido;
	private String audioValidateDni;
	private String audioSuDniEs;
	private ContextVar dniContextVar;
	private ContextVar confirmaDniContextVar;
	private ContextVar intentosDniContextVar;
	protected StepPlayRead stepAudioDni;
	protected StepPlayRead stepAudioValidate;
	protected StepPlay stepAudioSuDniEs;
	protected StepPlay stepAudioDniInvalido;
	protected StepSayDigits stepDiceDni;
	protected StepCounter contadorIntentosDni;
	protected StepConditional evalContadorDni;

	protected StepMenu stepMenuConfirmacionMenu;
	protected StepValidateDni validaDni;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;

	private void setSequence() {

		/* Ingreso DNI */

		stepAudioDni.setNextstep(validaDni.GetId());

		/* DNI valido */

		validaDni.setNextStepIsTrue(stepAudioSuDniEs.GetId());

		stepAudioSuDniEs.setNextstep(stepDiceDni.GetId());

		stepDiceDni.setNextstep(stepAudioValidate.GetId());

		stepAudioValidate.setNextstep(stepMenuConfirmacionMenu.GetId());

		stepMenuConfirmacionMenu.addSteps("1", stepIfTrueUUID);
		stepMenuConfirmacionMenu.addSteps("2", contadorIntentosDni.GetId());
		stepMenuConfirmacionMenu.setInvalidOption(contadorIntentosDni.GetId());

		/* DNI Invalido */

		validaDni.setNextStepIsFalse(stepAudioDniInvalido.GetId());

		stepAudioDniInvalido.setNextstep(contadorIntentosDni.GetId());

		contadorIntentosDni.setNextstep(evalContadorDni.GetId());
		evalContadorDni.addCondition(new condition(1, "#{"
				+ intentosDniContextVar.getVarName() + "} < " + intentos,
				stepAudioDni.GetId(), stepIfFalseUUID));

	}

	public PideDni() {
		super();

		GroupType = StepGroupType.pideDni;

		/*--- Audios --- */

		stepAudioValidate = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioValidate.setStepDescription("PLAYREAD => DNI CORRECTO");
		stepAudioValidate.setPlayMaxDigits(1);
		stepAudioValidate.setPlayTimeout(2000L);

		stepAudioDni = (StepPlayRead) StepFactory.createStep(StepType.PlayRead,
				UUID.randomUUID());
		stepAudioDni.setStepDescription("PLAYREAD => INGRESO DNI");
		stepAudioDni.setPlayMaxDigits(8);
		stepAudioDni.setPlayTimeout(2000L);

		stepAudioSuDniEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuDniEs.setStepDescription("PLAY => SU DNI ES");

		stepAudioDniInvalido = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioDniInvalido.setStepDescription("PLAY => DNI INVALIDO");

		stepDiceDni = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceDni.setStepDescription("SAYDIGITS => LOCUCIONA DNI");

		/*--- Contadores --- */

		contadorIntentosDni = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosDni.setStepDescription("COUNTER => INTENTOS DNI");

		/*--- Validaciones --- */

		validaDni = (StepValidateDni) StepFactory.createStep(
				StepType.ValidateDni, UUID.randomUUID());
		validaDni.setStepDescription("VALIDATEDNI => FORMATO DNI VALIDO");

		/*--- Condicional --- */

		evalContadorDni = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDni
				.setStepDescription("CONDITIONAL => CANTIDAD DE INTENTOS DNI");

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu.setStepDescription("MENU => CONFIRMA DNI");

		/*--- Actualizo --- */

		Steps.put(stepAudioDni.GetId(), stepAudioDni);
		Steps.put(validaDni.GetId(), validaDni);
		Steps.put(contadorIntentosDni.GetId(), contadorIntentosDni);
		Steps.put(evalContadorDni.GetId(), evalContadorDni);
		Steps.put(stepAudioValidate.GetId(), stepAudioValidate);
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);
		Steps.put(stepAudioSuDniEs.GetId(), stepAudioSuDniEs);
		Steps.put(stepDiceDni.GetId(), stepDiceDni);
		Steps.put(stepAudioDniInvalido.GetId(), stepAudioDniInvalido);

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioDni.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioDni.isEmpty() || audioDniInvalido.isEmpty()
				|| audioValidateDni.isEmpty() || audioSuDniEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (dniContextVar == null || confirmaDniContextVar == null
				|| intentosDniContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfTrueUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioDni(String audioDni) {
		this.audioDni = audioDni;
		stepAudioDni.setPlayFile(audioDni);
	}

	public void setAudioDniInvalido(String audioDniInvalido) {
		this.audioDniInvalido = audioDniInvalido;
		stepAudioDniInvalido.setPlayfile(audioDniInvalido);
	}

	public void setAudioValidateDni(String audioValidateDni) {
		this.audioValidateDni = audioValidateDni;
		stepAudioValidate.setPlayFile(audioValidateDni);
	}

	public void setAudioSuDniEs(String audioSuDniEs) {
		this.audioSuDniEs = audioSuDniEs;
		stepAudioSuDniEs.setPlayfile(audioSuDniEs);
	}

	public void setDniContextVar(ContextVar dniContextVar) {
		this.dniContextVar = dniContextVar;
		stepAudioDni.setContextVariableName(dniContextVar);
		validaDni.setContextVariableName(dniContextVar);
		stepDiceDni.setContextVariableName(dniContextVar);
	}

	public void setConfirmaDniContextVar(ContextVar confirmaDniContextVar) {
		this.confirmaDniContextVar = confirmaDniContextVar;
		stepAudioValidate.setContextVariableName(confirmaDniContextVar);
		stepMenuConfirmacionMenu.setContextVariableName(confirmaDniContextVar);
	}

	public void setIntentosDniContextVar(ContextVar intentosDniContextVar) {
		this.intentosDniContextVar = intentosDniContextVar;
		contadorIntentosDni.setContextVariableName(intentosDniContextVar);
	}

	public void setContadorIntentosDni(StepCounter contadorIntentosDni) {
		this.contadorIntentosDni = contadorIntentosDni;
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

	public ContextVar getDniContextVar() {
		return dniContextVar;
	}

}
