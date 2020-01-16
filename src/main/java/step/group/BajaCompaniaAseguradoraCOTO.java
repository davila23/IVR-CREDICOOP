package step.group;

import ivr.CallContext;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import clasesivr.TestIvr;

import main.Daemon;

import condition.condition;
import context.ContextVar;

import step.Step;
import step.StepAnswer;
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSendJPOS;
import step.StepSetVariable;
import step.StepSwitch;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class BajaCompaniaAseguradoraCOTO implements StepGroup {

	protected StepGroupType GroupType;
	private CallContext ctxVar;
	private StepMenu stepMenuConfirmacion;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private StepPlayRead stepAudioMenuIngresoBajaSeguro;
	private StepEnd pasoFinal;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepPlay stepAudioFinal;
	private StepPlay stepAudioSeguroBajaOK;
	private StepPlay stepAudioNoEsPosibleDarDeBaja;
	private StepPlay stepAudioServNoDisponible;
	private ContextVar idTramiteContextVar;
	private ContextVar codJposBajaSegContextVar;

	private void setSequence() {

		stepAudioMenuIngresoBajaSeguro
				.setNextstep(stepMenuConfirmacion.GetId());

		stepMenuConfirmacion.addSteps("1", enviaTramaJpos.GetId());
		stepMenuConfirmacion.setInvalidOption(stepAudioMenuIngresoBajaSeguro
				.GetId());

		stepAudioSeguroBajaOK.setNextstep(stepAudioFinal.GetId());
		stepAudioNoEsPosibleDarDeBaja.setNextstep(stepAudioFinal.GetId());

		enviaTramaJpos.setNextstep(evalRetJPOS.GetId());

		evalRetJPOS.setNextstep(stepAudioServNoDisponible.GetId());
	}

	public BajaCompaniaAseguradoraCOTO() {
		super();

		GroupType = StepGroupType.bajaCompaniaAseguradoraCoto;
	}

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		
		 * ret =>  88    ||   "Tarjeta con marca 3"    
		 * ret =>  83    ||   "Causa 08 y tarjeta adicional"
		  
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("00", stepAudioSeguroBajaOK.GetId());
		evalRetJPOS.addSwitchValue("97", stepAudioNoEsPosibleDarDeBaja.GetId());

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioMenuIngresoBajaSeguro.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		// if (audioDni.isEmpty() || audioDniInvalido.isEmpty()
		// || audioValidateDni.isEmpty() || audioSuDniEs.isEmpty()) {
		// throw new IllegalArgumentException("Variables de audio Vacias");
		// }

		// if (dniContextVar == null || confirmaDniContextVar == null
		// || intentosDniContextVar == null) {
		// throw new IllegalArgumentException("Variables de Contexto Vacias");
		// }
		// if (stepIfFalseUUID == null || stepIfTrueUUID == null) {
		// throw new IllegalArgumentException(
		// "Pasos verdadero o falso , vacios");
		// }
		this.setSequence();
		return Steps;
	}

	public void setStepIfTrueUUID(UUID stepIfTrueUUID) {
		this.stepIfTrueUUID = stepIfTrueUUID;
	}

	public void setStepIfFalseUUID(UUID stepIfFalseUUID) {
		this.stepIfFalseUUID = stepIfFalseUUID;
	}

	public void setCodJposBajaSegContextVar(ContextVar codJposBajaSegContextVar) {
		this.codJposBajaSegContextVar = codJposBajaSegContextVar;
	}

	public void setIdTramiteContextVar(ContextVar idTramiteContextVar) {
		this.idTramiteContextVar = idTramiteContextVar;
	}

	public void setContextVar(CallContext ctxVar) {
		this.ctxVar = ctxVar;
		createSteps();
	}

	private void createSteps() {

		/*--- Audios --- */

		stepAudioMenuIngresoBajaSeguro = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuIngresoBajaSeguro
				.setStepDescription("MENU => INGRESO RUTINA BAJA SEGURO");
		stepAudioMenuIngresoBajaSeguro.setPlayMaxDigits(1);
		stepAudioMenuIngresoBajaSeguro.setPlayTimeout(2000L);
		stepAudioMenuIngresoBajaSeguro.setPlayFile("coto/D000001");
		stepAudioMenuIngresoBajaSeguro.setContextVariableName(ctxVar
				.getContextVarByName("menuIngresoBajaSeguro"));
		Steps.put(stepAudioMenuIngresoBajaSeguro.GetId(),
				stepAudioMenuIngresoBajaSeguro);

		stepAudioSeguroBajaOK = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSeguroBajaOK
				.setStepDescription("PLAY => SE DIO DE BAJA EL SEGURO COTO");
		stepAudioSeguroBajaOK.setPlayfile("coto/A000078");
		Steps.put(stepAudioSeguroBajaOK.GetId(), stepAudioSeguroBajaOK);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		stepAudioNoEsPosibleDarDeBaja = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNoEsPosibleDarDeBaja
				.setStepDescription("PLAY => NO ES POSIBLE DAR DE BAJA SEGURO COTO");
		stepAudioNoEsPosibleDarDeBaja.setPlayfile("coto/A000080");
		Steps.put(stepAudioNoEsPosibleDarDeBaja.GetId(),
				stepAudioNoEsPosibleDarDeBaja);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => AUDIO FINAL COTO");
		stepAudioFinal.setPlayfile("coto/A000026");
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

		stepMenuConfirmacion = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuConfirmacion
				.setStepDescription("MENU => INGRESO RUTINA BLOQUEO COTO");
		stepMenuConfirmacion.setContextVariableName(ctxVar
				.getContextVarByName("menuIngresoBajaSeguro"));
		Steps.put(stepMenuConfirmacion.GetId(), stepMenuConfirmacion);

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos.setContextVariableTipoMensaje(ctxVar
				.getContextVarByName("envioServerJposPrecargadasContexVar"));
		enviaTramaJpos.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		enviaTramaJpos.addformatoVariables(0,
				ctxVar.getContextVarByName("codJposBajaSegContextVar"));
		enviaTramaJpos.addformatoVariables(1,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(2,
				ctxVar.getContextVarByName("idTramiteContextVar"));
		enviaTramaJpos.addformatoVariables(3,
				ctxVar.getContextVarByName("fillerContexVar"));
		enviaTramaJpos.addformatoVariables(4,
				ctxVar.getContextVarByName("idLlamadaContexVar"));
		enviaTramaJpos.addformatoVariables(5,
				ctxVar.getContextVarByName("whisperContextVar"));

		enviaTramaJpos
				.setStepDescription("SENDJPOS => ENVIA TRAMA JPOS , BLOQUEO TARJETA COTO");
		Steps.put(enviaTramaJpos.GetId(), enviaTramaJpos);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN COMUNICACION");
		Steps.put(pasoFinal.GetId(), pasoFinal);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		evalRetJPOS.setStepDescription("SWITCH => CODIGO RETORNO JPOS");
		Steps.put(evalRetJPOS.GetId(), evalRetJPOS);

		this.evalRetJPOS();
	}
}
