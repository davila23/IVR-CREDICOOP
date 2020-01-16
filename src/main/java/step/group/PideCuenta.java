package step.group;

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
import step.StepValidateCuenta;
import step.StepValidateDni;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideCuenta implements StepGroup {

	protected StepGroupType GroupType;
	private String audioCuenta;
	private String audioCuentaInvalida;
	private String audioValidateDni;
	private String audioSuDniEs;
	private ContextVar dniContextVar;
	private ContextVar confirmaDniContextVar;
	private ContextVar intentosCuentaContextVar;
	protected StepPlayRead stepAudioCuenta;
	protected StepPlayRead stepAudioValidateCuenta;
	protected StepPlay stepAudioSuCuentaEs;
	protected StepPlay stepAudioCuentaInvalida;
	protected StepSayDigits stepDiceCuenta;
	protected StepCounter contadorIntentosCuenta;
	protected StepConditional evalContadorCuenta;
	protected StepMenu stepMenuConfirmacionMenu;
	protected StepValidateCuenta validaCuenta;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;

	private void setSequence() {

		/* Ingreso DNI */

		stepAudioCuenta.setNextstep(validaCuenta.GetId());

		/* Cuenta valida */

		validaCuenta.setNextStepIsTrue(stepAudioSuCuentaEs.GetId());

		stepAudioSuCuentaEs.setNextstep(stepDiceCuenta.GetId());

		stepDiceCuenta.setNextstep(stepAudioValidateCuenta.GetId());

		stepAudioValidateCuenta.setNextstep(stepMenuConfirmacionMenu.GetId());

		stepMenuConfirmacionMenu.addSteps("1", stepIfTrueUUID);
		stepMenuConfirmacionMenu.addSteps("2", contadorIntentosCuenta.GetId());
		stepMenuConfirmacionMenu.setInvalidOption(contadorIntentosCuenta.GetId());

		/* Cuenta InvalidA */

		validaCuenta.setNextStepIsFalse(stepAudioCuentaInvalida.GetId());

		stepAudioCuentaInvalida.setNextstep(contadorIntentosCuenta.GetId());

		contadorIntentosCuenta.setNextstep(evalContadorCuenta.GetId());
		evalContadorCuenta.addCondition(new condition(1, "#{"
				+ intentosCuentaContextVar.getVarName() + "} < " + intentos,
				stepAudioCuenta.GetId(), stepIfFalseUUID));

	}

	public PideCuenta() {
		super();

		GroupType = StepGroupType.pideCuenta;

		/*--- Audios --- */

		stepAudioValidateCuenta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioValidateCuenta.setStepDescription("PLAYREAD => DNI CORRECTO");
		stepAudioValidateCuenta.setPlayMaxDigits(1);
		stepAudioValidateCuenta.setPlayTimeout(2000L);
		Steps.put(stepAudioValidateCuenta.GetId(), stepAudioValidateCuenta);

		stepAudioCuenta = (StepPlayRead) StepFactory.createStep(StepType.PlayRead,
				UUID.randomUUID());
		stepAudioCuenta.setStepDescription("PLAYREAD => INGRESO CUENTA");
		stepAudioCuenta.setPlayMaxDigits(9);
		stepAudioCuenta.setPlayTimeout(2000L);
		Steps.put(stepAudioCuenta.GetId(), stepAudioCuenta);

		stepAudioSuCuentaEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuCuentaEs.setStepDescription("PLAY => SU CUENTA ES");
		Steps.put(stepAudioSuCuentaEs.GetId(), stepAudioSuCuentaEs);

		stepAudioCuentaInvalida = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioCuentaInvalida.setStepDescription("PLAY => CUENTA INVALIDA");
		Steps.put(stepAudioCuentaInvalida.GetId(), stepAudioCuentaInvalida);

		stepDiceCuenta = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepDiceCuenta.setStepDescription("SAYDIGITS => LOCUCIONA CUENTA");
		Steps.put(stepDiceCuenta.GetId(), stepDiceCuenta);

		/*--- Contadores --- */

		contadorIntentosCuenta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosCuenta.setStepDescription("COUNTER => INTENTOS CUENTA");
		Steps.put(contadorIntentosCuenta.GetId(), contadorIntentosCuenta);

		/*--- Validaciones --- */

		validaCuenta = (StepValidateCuenta) StepFactory.createStep(
				StepType.ValidateCuenta, UUID.randomUUID());
		validaCuenta.setStepDescription("VALIDATECUNETA => FORMATO CUENTA VALIDA");
		Steps.put(validaCuenta.GetId(), validaCuenta);

		/*--- Condicional --- */

		evalContadorCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorCuenta
				.setStepDescription("CONDITIONAL => CANTIDAD DE INTENTOS CUENTA");
		Steps.put(evalContadorCuenta.GetId(), evalContadorCuenta);

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu.setStepDescription("MENU => CONFIRMA CUENTA");
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioCuenta.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioCuenta.isEmpty() || audioCuentaInvalida.isEmpty()
				|| audioValidateDni.isEmpty() || audioSuDniEs.isEmpty()) {
			throw new IllegalArgumentException("Variables de audio Vacias");
		}

		if (dniContextVar == null || confirmaDniContextVar == null
				|| intentosCuentaContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfTrueUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setAudioCuenta(String audioCuenta) {
		this.audioCuenta = audioCuenta;
		stepAudioCuenta.setPlayFile(audioCuenta);
	}

	public void setAudioCuentaInvalida(String audioCuentaInvalida) {
		this.audioCuentaInvalida = audioCuentaInvalida;
		stepAudioCuentaInvalida.setPlayfile(audioCuentaInvalida);
	}

	public void setAudioValidateCuenta(String audioValidateDni) {
		this.audioValidateDni = audioValidateDni;
		stepAudioValidateCuenta.setPlayFile(audioValidateDni);
	}

	public void setAudioSuCuentaEs(String audioSuDniEs) {
		this.audioSuDniEs = audioSuDniEs;
		stepAudioSuCuentaEs.setPlayfile(audioSuDniEs);
	}

	public void setCuentaContextVar(ContextVar dniContextVar) {
		this.dniContextVar = dniContextVar;
		stepAudioCuenta.setContextVariableName(dniContextVar);
		validaCuenta.setContextVariableName(dniContextVar);
		stepDiceCuenta.setContextVariableName(dniContextVar);
	}

	public void setConfirmaCuentaContextVar(ContextVar confirmaDniContextVar) {
		this.confirmaDniContextVar = confirmaDniContextVar;
		stepAudioValidateCuenta.setContextVariableName(confirmaDniContextVar);
		stepMenuConfirmacionMenu.setContextVariableName(confirmaDniContextVar);
	}

	public void setIntentosCuentaContextVar(ContextVar intentosCuentaContextVar) {
		this.intentosCuentaContextVar = intentosCuentaContextVar;
		contadorIntentosCuenta.setContextVariableName(intentosCuentaContextVar);
	}

	public void setContadorIntentosCuenta(StepCounter contadorIntentosCuenta) {
		this.contadorIntentosCuenta = contadorIntentosCuenta;
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
