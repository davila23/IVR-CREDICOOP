package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.jeval.function.string.Length;

import condition.condition;
import context.ContextVar;

import step.StepConditional;
import step.StepCounter;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayDigits;
import step.StepSayNumber;
import step.StepSayYear;
import step.StepValidateDate;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class PideFechaCredicoop implements StepGroup {

	protected StepGroupType GroupType;
	private String audioFecha;
	private String audioSuFechaEs;
	private String audioDia;
	private String audioMes;
	private String audioAnio;
	private String audioFechaInvalida;
	private String audioValidateFecha;
	private String audioDigInsuficientes;
	private String audioLaFechaNoCoincide;
	private ContextVar fechaContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar intentosFechaContextVar;
	private ContextVar contextVarMes;
	private ContextVar contextVarAnio;
	private ContextVar contextVarDia;
	private ContextVar fdnContexVar;
	private StepPlayRead stepAudioFecha;
	private StepPlayRead stepAudioValidate;
	private StepPlay stepAudioFechaInvalida;
	private StepPlay stepAudioDia;
	private StepPlay stepAudioMes;
	private StepPlay stepAudioAnio;
	private StepPlay stepAudioSuFechaEs;
	private StepPlay stepAudioDigInsuficientes;
	private StepSayNumber stepDiceDia;
	private StepSayNumber stepDiceMes;
	private StepSayNumber stepDiceAnio;
	private StepCounter contadorIntentosFecha;
	private StepConditional evalContadorFecha;
	private StepConditional evalCantidadDigitos;
	private StepValidateDate validaFecha;
	private StepMenu stepMenuConfirmacionMenu;
	private int intentos = 3;
	private UUID stepIfFalseUUID;
	private UUID stepIfTrueUUID;
	private StepConditional evalFechaValidaCredicoop;
	private StepGetAsteriskVariable obtieneFecha;
	private StepPlay stepAudioLaFechaNoCoincide;
	private StepConditional evalContadorFecha_aux;
	private StepCounter contadorIntentosFecha_aux;

	private void setSequence() {

		/* Obtiene fecha */

		obtieneFecha.setNextstep(stepAudioFecha.GetId());

		/* Ingreso Fecha */

		stepAudioFecha.setNextstep(evalCantidadDigitos.GetId());

		/* Evaluo cantidad de Digitos y Formato */

		evalCantidadDigitos.addCondition(new condition(1, "length('#{"
				+ fechaContextVar.getVarName() + "}') <  6 ",
				stepAudioDigInsuficientes.GetId(), validaFecha.GetId()));

		/* ERROR */

		validaFecha.setNextStepIsFalse(stepAudioFechaInvalida.GetId());

		stepAudioDigInsuficientes.setNextstep(contadorIntentosFecha.GetId());
		stepAudioFechaInvalida.setNextstep(contadorIntentosFecha.GetId());

		contadorIntentosFecha.setNextstep(evalContadorFecha.GetId());
		evalContadorFecha.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				stepAudioFecha.GetId(), stepIfFalseUUID));

		/* OK */

		validaFecha.setNextStepIsTrue(stepAudioSuFechaEs.GetId());

		stepAudioSuFechaEs.setNextstep(stepAudioDia.GetId());
		stepAudioDia.setNextstep(stepDiceDia.GetId());
		stepDiceDia.setNextstep(stepAudioMes.GetId());
		stepAudioMes.setNextstep(stepDiceMes.GetId());
		stepDiceMes.setNextstep(stepAudioAnio.GetId());
		stepAudioAnio.setNextstep(stepDiceAnio.GetId());
		stepDiceAnio.setNextstep(stepAudioValidate.GetId());

		/* CONFIRMA FECHA */

		stepAudioValidate.setNextstep(stepMenuConfirmacionMenu.GetId());

		stepMenuConfirmacionMenu
				.addSteps("1", evalFechaValidaCredicoop.GetId());
		// stepMenuConfirmacionMenu.addSteps("1", stepIfTrueUUID);
		stepMenuConfirmacionMenu.addSteps("2", contadorIntentosFecha.GetId());
		stepMenuConfirmacionMenu
				.setInvalidOption(contadorIntentosFecha.GetId());

		/* FECHA INGRESADA IGUAL a FDN */

		evalFechaValidaCredicoop.addCondition((new condition(1, "#{"
				+ fdnContexVar.getVarName() + "} == #{"
				+ fechaContextVar.getVarName() + "}", stepIfTrueUUID,
				contadorIntentosFecha_aux.GetId())));
	
		contadorIntentosFecha_aux.setNextstep(evalContadorFecha_aux.GetId());
		evalContadorFecha_aux.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				stepAudioLaFechaNoCoincide.GetId(), stepIfFalseUUID));

		stepAudioLaFechaNoCoincide.setNextstep(evalContadorFecha.GetId());

	}

	public PideFechaCredicoop() {
		super();

		GroupType = StepGroupType.pideFechaCredicoop;

		/*--- Variable Asterisk --- */

		obtieneFecha = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneFecha.setContextVariableName(fdnContexVar);
		obtieneFecha.setVariableName("FDN");
		obtieneFecha.setStepDescription("GETASTERISKVARIABLE => OBTIENE FDN");
		Steps.put(obtieneFecha.GetId(), obtieneFecha);
		/*--- Audios --- */

		stepAudioFecha = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioFecha.setStepDescription("PLAYREAD => INGRESO FECHA");
		stepAudioFecha.setPlayMaxDigits(6);
		stepAudioFecha.setPlayTimeout(2000L);
		Steps.put(stepAudioFecha.GetId(), stepAudioFecha);

		stepAudioValidate = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioValidate
				.setStepDescription("PLAYREAD => FECHA VALIDA / INVALIDA");
		stepAudioValidate.setPlayMaxDigits(1);
		stepAudioValidate.setPlayTimeout(2000L);
		Steps.put(stepAudioValidate.GetId(), stepAudioValidate);

		stepAudioDigInsuficientes = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDigInsuficientes
				.setStepDescription("PLAY => DIGITOS INSUFICIENTES");
		Steps.put(stepAudioDigInsuficientes.GetId(), stepAudioDigInsuficientes);

		stepAudioLaFechaNoCoincide = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioLaFechaNoCoincide
				.setStepDescription("PLAY => LA FECHA NO COINCIDE CON NUESTROS REGISTROS");
		Steps.put(stepAudioLaFechaNoCoincide.GetId(),
				stepAudioLaFechaNoCoincide);

		stepAudioDia = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioDia.setStepDescription("PLAY => DIA");
		Steps.put(stepAudioDia.GetId(), stepAudioDia);

		stepAudioMes = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioMes.setStepDescription("PLAY => MES");
		Steps.put(stepAudioMes.GetId(), stepAudioMes);

		stepAudioAnio = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioAnio.setStepDescription("PLAY => ANIO");
		Steps.put(stepAudioAnio.GetId(), stepAudioAnio);

		stepAudioSuFechaEs = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuFechaEs.setStepDescription("PLAY => SU FECHA ES");
		Steps.put(stepAudioSuFechaEs.GetId(), stepAudioSuFechaEs);

		stepAudioFechaInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaInvalida.setStepDescription("PLAY => FECHA INVALIDA");
		Steps.put(stepAudioFechaInvalida.GetId(), stepAudioFechaInvalida);

		stepDiceMes = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceMes.setStepDescription("SAYNUMBER => MES");
		Steps.put(stepDiceMes.GetId(), stepDiceMes);

		stepDiceDia = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceDia.setStepDescription("SAYNUMBER => DIA");
		Steps.put(stepDiceDia.GetId(), stepDiceDia);

		stepDiceAnio = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceAnio.setStepDescription("SAYYEAR => ANIO");
		Steps.put(stepDiceAnio.GetId(), stepDiceAnio);

		/*--- Contadores --- */

		contadorIntentosFecha = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFecha.setStepDescription("COUNTER => INTENTOS FECHA");
		Steps.put(contadorIntentosFecha.GetId(), contadorIntentosFecha);
		
		contadorIntentosFecha_aux= (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFecha_aux.setStepDescription("COUNTER => INTENTOS FECHA AUX");
		Steps.put(contadorIntentosFecha_aux.GetId(), contadorIntentosFecha_aux);

		/*--- Validaciones --- */

		validaFecha = (StepValidateDate) StepFactory.createStep(
				StepType.ValidateDate, UUID.randomUUID());
		validaFecha
				.setStepDescription("VALIDATEDATE => VERIFICA FORMATO FECHA");
		Steps.put(validaFecha.GetId(), validaFecha);

		/*--- Condicional --- */

		evalContadorFecha_aux = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFecha_aux
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE FECHA AUX");
		Steps.put(evalContadorFecha_aux.GetId(), evalContadorFecha_aux);

		evalContadorFecha = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFecha
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE FECHA");
		Steps.put(evalContadorFecha.GetId(), evalContadorFecha);

		evalCantidadDigitos = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCantidadDigitos
				.setStepDescription("CONDITIONAL => CANTIDAD DE DIGITOS MAYOR A 6");
		Steps.put(evalCantidadDigitos.GetId(), evalCantidadDigitos);

		evalFechaValidaCredicoop = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalFechaValidaCredicoop
				.setStepDescription("CONDITIONAL => FECHA VALIDA");
		Steps.put(evalFechaValidaCredicoop.GetId(), evalFechaValidaCredicoop);

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu.setStepDescription("MENU => CONFIRMA FECHA ");
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);

	}

	@Override
	public UUID getInitialStep() {
		return obtieneFecha.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (audioFecha.isEmpty() || audioDia.isEmpty() || audioMes.isEmpty()
				|| audioAnio.isEmpty() || audioFechaInvalida.isEmpty()
				|| audioSuFechaEs.isEmpty() || audioValidateFecha.isEmpty()
				|| audioDigInsuficientes.isEmpty())
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
		validaFecha.setContextVariableName(fechaContextVar);
	}

	public void setFdnContextVar(ContextVar fdnContextVar) {
		this.fdnContexVar = fdnContextVar;
		obtieneFecha.setContextVariableName(fdnContextVar);
	}

	public void setAudioValidateFecha(String audioValidateFecha) {
		this.audioValidateFecha = audioValidateFecha;
		stepAudioValidate.setPlayFile(audioValidateFecha);
	}

	public void setAudioDigInsuficientes(String audioDigInsuficientes) {
		this.audioDigInsuficientes = audioDigInsuficientes;
		stepAudioDigInsuficientes.setPlayfile(audioDigInsuficientes);
	}

	public void setAudioLaFechaNoCoincide(String audioLaFechaNoCoincide) {
		this.audioLaFechaNoCoincide = audioLaFechaNoCoincide;
		stepAudioLaFechaNoCoincide.setPlayfile(audioLaFechaNoCoincide);
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
		contadorIntentosFecha_aux.setContextVariableName(intentosFechaContextVar);
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
	}
}
