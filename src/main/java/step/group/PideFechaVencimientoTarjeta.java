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
import step.StepSayNumber;
import step.StepValidateDate;
import step.StepFactory.StepType;
import step.StepValidateDateTarjeta;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideFechaVencimientoTarjeta implements StepGroup {

	protected StepGroupType GroupType;
	private String audioFecha;
	private String audioSuFechaEs;
	private String audioFechaInvalida;
	private String audioValidateFecha;
	private ContextVar fechaContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar intentosFechaContextVar;
	private ContextVar contextVarMes;
	private ContextVar contextVarAnio;
	private StepPlayRead stepAudioVencimientoTarjeta;
	private StepPlayRead stepAudioValidate;
	private StepPlay stepAudioFechaInvalida;
	private StepCounter contadorIntentosFecha;
	private StepConditional evalContadorFecha;
	private StepValidateDateTarjeta validaFechaTarjeta;
	private StepMenu stepMenuConfirmacionMenu;
	private int intentos = 3;
	private UUID stepIfFalseUUID;
	private UUID stepIfTrueUUID;

	private void setSequence() {

		/* Ingreso Fecha de Vencimiento */

		stepAudioVencimientoTarjeta.setNextstep(validaFechaTarjeta.GetId());

		/* Fecha de Vencimiento valida */

		validaFechaTarjeta.setNextStepIsTrue(stepIfTrueUUID);

		/* Fecha de Vencimiento invalida */

		validaFechaTarjeta.setNextStepIsFalse(stepAudioFechaInvalida.GetId());

		stepAudioFechaInvalida.setNextstep(contadorIntentosFecha.GetId());

		contadorIntentosFecha.setNextstep(evalContadorFecha.GetId());
		evalContadorFecha.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				stepAudioVencimientoTarjeta.GetId(), stepIfFalseUUID));

	}

	public PideFechaVencimientoTarjeta() {
		super();

		GroupType = StepGroupType.pideFechaVencimientoTarjeta;

		/*--- Audios --- */

		stepAudioVencimientoTarjeta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioVencimientoTarjeta
				.setStepDescription("PLAYREAD => INGRESO FECHA DE VENCIMIENTO");
		stepAudioVencimientoTarjeta.setPlayMaxDigits(4);
		stepAudioVencimientoTarjeta.setPlayTimeout(2000L);

		stepAudioFechaInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaInvalida
				.setStepDescription("PLAY => FECHA DE VENCIMIENTO INVALIDA");

		/*--- Contadores --- */

		contadorIntentosFecha = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFecha
				.setStepDescription("COUNTER => INTENTOS FECHA DE VENCIMIENTO");

		/*--- Validaciones --- */

		validaFechaTarjeta = (StepValidateDateTarjeta) StepFactory.createStep(
				StepType.ValidateDateTarjeta, UUID.randomUUID());
		validaFechaTarjeta
				.setStepDescription("VALIDATEDATETARJETA => FECHA INGRESADA CORRECTA");

		/*--- Condicional --- */

		evalContadorFecha = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFecha
				.setStepDescription("CONDITIONAL => INTENTOS FECHA DE VENCIMIENTO");

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu
				.setStepDescription("MENU => CONFIRMA FECHA DE VENCIMIENTO");

		/*--- Actualizo --- */

		Steps.put(stepAudioVencimientoTarjeta.GetId(),
				stepAudioVencimientoTarjeta);
		Steps.put(validaFechaTarjeta.GetId(), validaFechaTarjeta);
		Steps.put(contadorIntentosFecha.GetId(), contadorIntentosFecha);
		Steps.put(evalContadorFecha.GetId(), evalContadorFecha);
		Steps.put(stepAudioValidate.GetId(), stepAudioValidate);
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);
		Steps.put(stepAudioFechaInvalida.GetId(), stepAudioFechaInvalida);
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioVencimientoTarjeta.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioFecha.isEmpty() || audioFechaInvalida.isEmpty()
				|| audioSuFechaEs.isEmpty() || audioValidateFecha.isEmpty())
			throw new IllegalArgumentException("Variables de audio Vacias");

		if (fechaContextVar == null || confirmaFechaContextVar == null
				|| intentosFechaContextVar == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setfechaContextVar(ContextVar fechaContextVar) {
		this.fechaContextVar = fechaContextVar;
		stepAudioVencimientoTarjeta.setContextVariableName(fechaContextVar);
		validaFechaTarjeta.setContextVariableName(fechaContextVar);
	}

	public void setAudioValidateFecha(String audioValidateFecha) {
		this.audioValidateFecha = audioValidateFecha;
		stepAudioValidate.setPlayFile(audioValidateFecha);
	}

	public void setAudioFecha(String audioFecha) {
		this.audioFecha = audioFecha;
		stepAudioVencimientoTarjeta.setPlayFile(audioFecha);
	}

	public void setAudioFechaInvalida(String audioFechaInvalida) {
		this.audioFechaInvalida = audioFechaInvalida;
		stepAudioFechaInvalida.setPlayfile(audioFechaInvalida);
	}

	public void setAudioSuFechaEs(String audioSuFechaEs) {
		this.audioSuFechaEs = audioSuFechaEs;
	}

	public void setConfirmaFechaContextVar(ContextVar confirmaFechaContextVar) {
		this.confirmaFechaContextVar = confirmaFechaContextVar;
		stepAudioValidate.setContextVariableName(confirmaFechaContextVar);
		stepMenuConfirmacionMenu
				.setContextVariableName(confirmaFechaContextVar);
	}

	public void setIntentosFechaContextVar(ContextVar intentosFechaContextVar) {
		this.intentosFechaContextVar = intentosFechaContextVar;
		contadorIntentosFecha.setContextVariableName(intentosFechaContextVar);
	}

	public void setContextVarMes(ContextVar contextVarMes) {
		this.contextVarMes = contextVarMes;
		validaFechaTarjeta.setContextVariableMes(contextVarMes);
	}

	public void setContextVarAnio(ContextVar contextVarAnio) {
		this.contextVarAnio = contextVarAnio;
		validaFechaTarjeta.setContextVariableAnio(contextVarAnio);
	}

	public void setContadorIntentosFecha(StepCounter contadorIntentosFecha) {
		this.contadorIntentosFecha = contadorIntentosFecha;
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

	public ContextVar getFechaContextVar() {
		return fechaContextVar;
	}

	public void setFechaContextVar(ContextVar fechaContextVar) {
		this.fechaContextVar = fechaContextVar;
	}
}
