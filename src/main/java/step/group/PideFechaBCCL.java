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
import step.StepSayNumber;
import step.StepSayYear;
import step.StepSetAsteriskVariable;
import step.StepValidateDate;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PideFechaBCCL implements StepGroup {

	protected StepGroupType GroupType;
	private String audioFecha;
	private String audioSuFechaEs;
	private String audioDia;
	private String audioMes;
	private String audioAnio;
	private String audioFechaInvalida;
	private String audioValidateFecha;
	private ContextVar fechaContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar intentosFechaContextVar;
	private ContextVar contextVarMes;
	private ContextVar contextVarAnio;
	private ContextVar contextVarDia;
	private StepPlayRead stepAudioFecha;
	private StepPlayRead stepAudioValidate;
	private StepPlay stepAudioFechaInvalida;
	private StepPlay stepAudioDia;
	private StepPlay stepAudioMes;
	private StepPlay stepAudioAnio;
	private StepPlay stepAudioSuFechaEs;
	private StepSayNumber stepDiceDia;
	private StepSayNumber stepDiceMes;
	private StepSayYear stepDiceAnio;
	private StepCounter contadorIntentosFecha;
	private StepConditional evalContadorFecha;
	private StepValidateDate validaFecha;
	private StepMenu stepMenuConfirmacionMenu;
	private int intentos = 3;
	private UUID stepIfFalseUUID;
	private UUID stepIfTrueUUID;

	private StepSetAsteriskVariable stepSetFecha;

	private void setSequence() {

		/* Ingreso Fecha de Nacimiento */

		stepAudioFecha.setNextstep(validaFecha.GetId());

		/* Fecha de Nacimiento valida */

		validaFecha.setNextStepIsTrue(stepAudioSuFechaEs.GetId());

		stepAudioSuFechaEs.setNextstep(stepAudioDia.GetId());

		stepAudioDia.setNextstep(stepDiceDia.GetId());
		stepDiceDia.setNextstep(stepAudioMes.GetId());
		stepAudioMes.setNextstep(stepDiceMes.GetId());
		stepDiceMes.setNextstep(stepAudioAnio.GetId());
		stepAudioAnio.setNextstep(stepDiceAnio.GetId());
		stepDiceAnio.setNextstep(stepAudioValidate.GetId());

		/* Confirma Fecha */

		stepAudioValidate.setNextstep(stepMenuConfirmacionMenu.GetId());

		stepMenuConfirmacionMenu.addSteps("1", stepSetFecha.GetId());
		stepMenuConfirmacionMenu.addSteps("2", contadorIntentosFecha.GetId());
		stepMenuConfirmacionMenu
				.setInvalidOption(contadorIntentosFecha.GetId());

		stepSetFecha.setNextstep(stepIfTrueUUID);
		/* Fecha de Nacimiento Invalida */

		validaFecha.setNextStepIsFalse(stepAudioFechaInvalida.GetId());

		stepAudioFechaInvalida.setNextstep(contadorIntentosFecha.GetId());

		contadorIntentosFecha.setNextstep(evalContadorFecha.GetId());
		evalContadorFecha.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				stepAudioFecha.GetId(), stepIfFalseUUID));

	}

	public PideFechaBCCL() {
		super();

		GroupType = StepGroupType.pideFecha;

		/*--- Audios --- */

		stepAudioFecha = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioFecha.setStepDescription("PLAYREAD => INGRESO FECHA");
		stepAudioFecha.setPlayMaxDigits(6);
		stepAudioFecha.setPlayTimeout(2000L);

		stepAudioValidate = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioValidate.setStepDescription("PLAYREAD => FECHA CORRECTA");
		stepAudioValidate.setPlayMaxDigits(1);
		stepAudioValidate.setPlayTimeout(2000L);

		stepAudioDia = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioDia.setStepDescription("PLAY => DIA");

		stepAudioMes = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioMes.setStepDescription("PLAY => MES");

		stepAudioAnio = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioAnio.setStepDescription("PLAY => ANIO");

		stepAudioSuFechaEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuFechaEs.setStepDescription("PLAY => SU FECHA ES");

		stepAudioFechaInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaInvalida.setStepDescription("PLAY => FECHA INVALIDA");

		stepDiceMes = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceMes.setStepDescription("SAYNUMBER => MES");

		stepDiceDia = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceDia.setStepDescription("SAYNUMBER => DIA");

		stepDiceAnio = (StepSayYear) StepFactory.createStep(StepType.SayYear,
				UUID.randomUUID());
		stepDiceAnio.setStepDescription("SAYYEAR => ANIO");

		/*--- Contadores --- */

		contadorIntentosFecha = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFecha.setStepDescription("COUNTER => INTENTOS FECHA");

		/*--- Validaciones --- */

		validaFecha = (StepValidateDate) StepFactory.createStep(
				StepType.ValidateDate, UUID.randomUUID());
		validaFecha.setStepDescription("VALIDATEDATE => FECHA VALIDA");

		/*--- Condicional --- */

		evalContadorFecha = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFecha.setStepDescription("CONDITIONAL => INTENTOS FECHA");

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu.setStepDescription("MENU => CONFIRMA FECHA");

		stepSetFecha = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetFecha.setStepDescription("SETASTERISKVARIABLE => OBTIENE DNI");
		stepSetFecha.setContextVariableName(fechaContextVar);
		stepSetFecha.setVariableName("macrovv");

		/*--- Actualizo --- */

		Steps.put(stepAudioFecha.GetId(), stepAudioFecha);
		Steps.put(validaFecha.GetId(), validaFecha);
		Steps.put(contadorIntentosFecha.GetId(), contadorIntentosFecha);
		Steps.put(evalContadorFecha.GetId(), evalContadorFecha);
		Steps.put(stepAudioValidate.GetId(), stepAudioValidate);
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);
		Steps.put(stepAudioSuFechaEs.GetId(), stepAudioSuFechaEs);
		Steps.put(stepDiceDia.GetId(), stepDiceDia);
		Steps.put(stepDiceMes.GetId(), stepDiceMes);
		Steps.put(stepDiceAnio.GetId(), stepDiceAnio);
		Steps.put(stepAudioDia.GetId(), stepAudioDia);
		Steps.put(stepAudioMes.GetId(), stepAudioMes);
		Steps.put(stepAudioAnio.GetId(), stepAudioAnio);
		Steps.put(stepAudioFechaInvalida.GetId(), stepAudioFechaInvalida);
		Steps.put(stepSetFecha.GetId(), stepSetFecha);
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioFecha.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioFecha.isEmpty() || audioDia.isEmpty() || audioMes.isEmpty()
				|| audioAnio.isEmpty() || audioFechaInvalida.isEmpty()
				|| audioSuFechaEs.isEmpty() || audioValidateFecha.isEmpty())
			throw new IllegalArgumentException("Variables de audio Vacias");

		if (fechaContextVar == null || confirmaFechaContextVar == null
				|| contextVarDia == null || contextVarMes == null
				|| contextVarAnio == null || intentosFechaContextVar == null) {
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
		stepAudioFecha.setContextVariableName(fechaContextVar);
		stepSetFecha.setContextVariableName(fechaContextVar);
		validaFecha.setContextVariableName(fechaContextVar);
	}

	public void setAudioValidateFecha(String audioValidateFecha) {
		this.audioValidateFecha = audioValidateFecha;
		stepAudioValidate.setPlayFile(audioValidateFecha);
	}

	public void setAudioFecha(String audioFecha) {
		this.audioFecha = audioFecha;
		stepAudioFecha.setPlayFile(audioFecha);
	}

	public void setAudioFechaInvalida(String audioFechaInvalida) {
		this.audioFechaInvalida = audioFechaInvalida;
		stepAudioFechaInvalida.setPlayfile(audioFechaInvalida);
	}

	public void setAudioDia(String audioDia) {
		this.audioDia = audioDia;
		stepAudioDia.setPlayfile(audioDia);
	}

	public void setAudioMes(String audioMes) {
		this.audioMes = audioMes;
		stepAudioMes.setPlayfile(audioMes);
	}

	public void setAudioAnio(String audioAnio) {
		this.audioAnio = audioAnio;
		stepAudioAnio.setPlayfile(audioAnio);
	}

	public void setAudioSuFechaEs(String audioSuFechaEs) {
		this.audioSuFechaEs = audioSuFechaEs;
		stepAudioSuFechaEs.setPlayfile(audioSuFechaEs);
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

	public void setContextVarDia(ContextVar contextVarDia) {
		this.contextVarDia = contextVarDia;
		stepDiceDia.setContextVariableName(contextVarDia);
		validaFecha.setContextVariableDia(contextVarDia);
	}

	public void setContextVarMes(ContextVar contextVarMes) {
		this.contextVarMes = contextVarMes;
		stepDiceMes.setContextVariableName(contextVarMes);
		validaFecha.setContextVariableMes(contextVarMes);
	}

	public void setContextVarAnio(ContextVar contextVarAnio) {
		this.contextVarAnio = contextVarAnio;
		stepDiceAnio.setContextVariableName(contextVarAnio);
		validaFecha.setContextVariableAnio(contextVarAnio);
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
		stepSetFecha.setContextVariableName(fechaContextVar);
	}
}
