package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.Step;
import step.StepAuthInitialInfo;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepLimitPlayRead;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayMonth;
import step.StepSayNumber;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PrecargadasCabalDenunciaAsesor implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private PideDni pideDniGrp;
	private StepTimeConditionDB obtieneHorario;
	private StepPlay stepAudioServNoDisponible;
	private StepLimitPlayRead stepAudioMenuDenuncias;
	private StepPlay stepAudioSuperoIntentos;
	private StepEnd pasoFinal;
	private StepPlay stepAudioIngresoIncorrectoDenuncias;
	private StepMenu stepMenuDenuncias;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDni;
	private StepExecute stepDerivoLlamada;
	private StepPlay stepAudioDisuadeDerivoAsesor;

	private void setSequence() {

		/* Denuncia Precargada Con Derivo */

		stepAudioMenuDenuncias.setNextstep(stepMenuDenuncias.GetId());
		stepAudioMenuDenuncias
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuDenuncias.addSteps("1", pideDniGrp.getInitialStep());
		stepMenuDenuncias.addSteps("9", stepIfTrueUUID);
		stepMenuDenuncias.setInvalidOption(stepAudioIngresoIncorrectoDenuncias
				.GetId());

		stepAudioIngresoIncorrectoDenuncias.setNextstep(stepAudioMenuDenuncias
				.GetId());

		/* Pide DNI */

		pideDniGrp.setStepIfTrue(stepAudioPrevioDerivoAsesor.GetId());
		pideDniGrp.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* Secuencias comunes */

		stepAudioVerifiqueNumeroDni.setNextstep(stepIfFalseUUID);

		obtieneHorario.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepAudioPrevioDerivoAsesor.GetId());

		stepAudioPrevioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
		stepAudioDisuadeDerivoAsesor.setNextstep(stepIfFalseUUID);
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		// if (audioDni.isEmpty() || audioDniInvalido.isEmpty()
		// || audioValidateDni.isEmpty() || audioSuDniEs.isEmpty()) {
		// throw new IllegalArgumentException("Variables de audio Vacias");
		// }
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioMenuDenuncias.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	public void setStepIfTrueUUID(UUID stepIfTrueUUID) {
		this.stepIfTrueUUID = stepIfTrueUUID;
	}

	public void setStepIfFalseUUID(UUID stepIfFalseUUID) {
		this.stepIfFalseUUID = stepIfFalseUUID;
	}

	public void setContextVar(CallContext ctx) {
		this.ctxVar = ctx;
		createSteps();
	}

	public PrecargadasCabalDenunciaAsesor() {
		super();

		GroupType = StepGroupType.precargadasCabalDenunciaAsesor;
	}

	private void createSteps() {

		/* Play */

		stepAudioDisuadeDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisuadeDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioDisuadeDerivoAsesor
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioDisuadeDerivoAsesor.GetId(),
				stepAudioDisuadeDerivoAsesor);

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		stepAudioIngresoIncorrectoDenuncias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoDenuncias
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoDenuncias
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioIngresoIncorrectoDenuncias.GetId(),
				stepAudioIngresoIncorrectoDenuncias);

		/* Limit play Read */

		stepAudioMenuDenuncias = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuDenuncias.setPlayFile("coto/A000013");
		stepAudioMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaAsesorContextVar"));
		stepAudioMenuDenuncias
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");
		Steps.put(stepAudioMenuDenuncias.GetId(), stepAudioMenuDenuncias);

		/* Menu */

		stepMenuDenuncias = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuDenuncias.setStepDescription("MENU => MENU DENUNCIA, ASESOR");
		stepMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaAsesorContextVar"));
		Steps.put(stepMenuDenuncias.GetId(), stepMenuDenuncias);

		/* FIN */

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN COMUNICACION PRECARGADA");

		/* GRUPOS */
		pideDniGrp = (PideDni) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDni);
		pideDniGrp.setAudioDni("coto/A000007");
		pideDniGrp.setAudioValidateDni("RUTINAPIN/RUTINA_PIN010");
		pideDniGrp.setAudioDniInvalido("coto/A000009");
		pideDniGrp.setAudioSuDniEs("coto/A000052");
		pideDniGrp
				.setDniContextVar(ctxVar.getContextVarByName("dniContextVar"));
		pideDniGrp.setIntentosDniContextVar(ctxVar
				.getContextVarByName("intentosDniContextVar"));
		pideDniGrp.setConfirmaDniContextVar(ctxVar
				.getContextVarByName("confirmaDniContextVar"));

		/* Horario */

		obtieneHorario = (StepTimeConditionDB) StepFactory.createStep(
				StepType.TimeConditionDB, UUID.randomUUID());
		obtieneHorario
				.setStepDescription("TIMECONDITIONDB => OBTIENE HORARIO DE LA BASE");
		obtieneHorario.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorario.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorario.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		Steps.put(obtieneHorario.GetId(), obtieneHorario);

		/* Derivo */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORBIE"));
		stepDerivoLlamada
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR");
		Steps.put(stepDerivoLlamada.GetId(), stepDerivoLlamada);

	}

	public PideDni getPideDni() {
		return pideDniGrp;
	}

}
