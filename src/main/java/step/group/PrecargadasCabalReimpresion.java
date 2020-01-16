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
import step.StepPlayFromVar;
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

public class PrecargadasCabalReimpresion implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private PideDni pideDniGrp;
	private PideTarjeta pideTarjetaGrp;
	private StepTimeConditionDB obtieneHorario;
	private StepEnd pasoFinal;
	private StepExecute stepDerivoLlamada;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepSayMonth stepFechaReposicion;
	private StepSayNumber stepNumeroSaldoAnterior;
	private StepSayNumber stepNumberoTramiteReimpresion;
	private StepPlayFromVar stepDireccionReposicion;
	private StepMenu stepMenuDenuncias;
	private StepMenu stepSubMenuDenuncias;
	private StepLimitPlayRead stepAudioSubMenuDenuncias;
	private StepLimitPlayRead stepAudioMenuReimpresion;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioSuperoIntentos;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDni;
	private StepPlay stepAudioDisuadeDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDeTarjeta;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioNroTarjetaDuplicado;
	private StepPlay stepAudioTarjetaReimpresa;
	private StepPlay stepAudioNroDocumentoInexistente;
	private StepPlay stepAudioIngresoIncorrectoMenuDenuncias;
	private StepPlay stepAudioIngresoIncorrectoSubMenuDenuncias;
	private StepPlay stepAudioDireccionReposicion;
	private StepPlay stepAudioFechaReposicion;
	private StepPlay stepAudioSaldoAnterior;

	private void setSequence() {

		/* Pide Tarjeta */

		pideTarjetaGrp.setStepIfTrue(pideDniGrp.getInitialStep());
		pideTarjetaGrp
				.setStepIfFalse(stepAudioVerifiqueNumeroDeTarjeta.GetId());

		/* Pide DNI */

		pideDniGrp.setStepIfTrue(stepAudioMenuReimpresion.GetId());
		pideDniGrp.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* Menu Denuncia Por IVR */

		stepAudioMenuReimpresion.setNextstep(stepMenuDenuncias.GetId());
		stepAudioMenuReimpresion
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuDenuncias.addSteps("1", enviaTramaJpos.GetId());
		stepMenuDenuncias.addSteps("9", stepIfTrueUUID);
		stepMenuDenuncias
				.setInvalidOption(stepAudioIngresoIncorrectoMenuDenuncias
						.GetId());

		stepAudioIngresoIncorrectoMenuDenuncias
				.setNextstep(stepAudioMenuReimpresion.GetId());

		/* Retorno 00 */

		stepAudioTarjetaReimpresa.setNextstep(stepNumberoTramiteReimpresion
				.GetId());
		stepNumberoTramiteReimpresion.setNextstep(stepAudioSaldoAnterior
				.GetId());
		stepAudioSaldoAnterior.setNextstep(stepNumeroSaldoAnterior.GetId());
		stepNumeroSaldoAnterior.setNextstep(stepAudioDireccionReposicion
				.GetId());
		stepAudioDireccionReposicion.setNextstep(stepDireccionReposicion
				.GetId());
		stepDireccionReposicion.setNextstep(stepAudioFechaReposicion.GetId());
		stepAudioFechaReposicion.setNextstep(stepFechaReposicion.GetId());

		stepFechaReposicion.setNextstep(stepFechaReposicion.GetId());

		stepAudioSubMenuDenuncias.setNextstep(stepSubMenuDenuncias.GetId());
		stepAudioSubMenuDenuncias
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepSubMenuDenuncias.addSteps("1", stepAudioTarjetaReimpresa.GetId());
		stepSubMenuDenuncias.addSteps("2", stepAudioPrevioDerivoAsesor.GetId());
		stepSubMenuDenuncias.addSteps("9", stepIfTrueUUID);
		stepSubMenuDenuncias
				.setInvalidOption(stepAudioIngresoIncorrectoSubMenuDenuncias
						.GetId());

		stepAudioIngresoIncorrectoSubMenuDenuncias
				.setNextstep(stepAudioSubMenuDenuncias.GetId());

		/* Secuencias comunes */

		enviaTramaJpos.setNextstep(evalRetJPOS.GetId());

		stepAudioVerifiqueNumeroDni.setNextstep(stepIfFalseUUID);
		stepAudioVerifiqueNumeroDeTarjeta.setNextstep(stepIfFalseUUID);

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

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		 * ret =>  88    ||   "Tarjeta con marca 3"    
		 * ret =>  83    ||   "Causa 08 y tarjeta adicional"
		 * ret =>  81    ||   "Tarjeta bloqueada"
		 * ret =>  99    ||   "Tarjeta inexistente"
		 *    
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("96", stepAudioNroTarjIncorrecto.GetId());
		evalRetJPOS.addSwitchValue("99", stepAudioNroTarjVencida.GetId());
		evalRetJPOS.addSwitchValue("98", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("94", stepAudioNroTarjetaDuplicado.GetId());
		evalRetJPOS.addSwitchValue("02",
				stepAudioNroDocumentoInexistente.GetId());
		evalRetJPOS.addSwitchValue("00", stepAudioTarjetaReimpresa.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	@Override
	public UUID getInitialStep() {
		return pideDniGrp.getInitialStep();
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

	public PrecargadasCabalReimpresion() {
		super();

		GroupType = StepGroupType.precargadasCabalDenunciaAsesor;
	}

	private void createSteps() {

		/* Play */

		stepDireccionReposicion = (StepPlayFromVar) StepFactory.createStep(
				StepType.PlayFromVar, UUID.randomUUID());
		stepDireccionReposicion
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepDireccionReposicion.GetId(), stepDireccionReposicion);

		/* Play */

		stepAudioDireccionReposicion = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDireccionReposicion.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioDireccionReposicion
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioDireccionReposicion.GetId(),
				stepAudioDireccionReposicion);

		stepAudioFechaReposicion = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaReposicion.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaReposicion
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioFechaReposicion.GetId(), stepAudioFechaReposicion);

		stepAudioSaldoAnterior = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSaldoAnterior.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioSaldoAnterior
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioSaldoAnterior.GetId(), stepAudioSaldoAnterior);

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

		stepAudioIngresoIncorrectoMenuDenuncias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuDenuncias
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuDenuncias
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioIngresoIncorrectoMenuDenuncias.GetId(),
				stepAudioIngresoIncorrectoMenuDenuncias);

		stepAudioIngresoIncorrectoSubMenuDenuncias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoSubMenuDenuncias
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoSubMenuDenuncias
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioIngresoIncorrectoSubMenuDenuncias.GetId(),
				stepAudioIngresoIncorrectoSubMenuDenuncias);

		stepAudioVerifiqueNumeroDni = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueNumeroDni.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioVerifiqueNumeroDni
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioVerifiqueNumeroDni.GetId(),
				stepAudioVerifiqueNumeroDni);

		stepAudioVerifiqueNumeroDeTarjeta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueNumeroDeTarjeta
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioVerifiqueNumeroDeTarjeta
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioVerifiqueNumeroDeTarjeta.GetId(),
				stepAudioVerifiqueNumeroDeTarjeta);

		stepAudioNroDocumentoInexistente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroDocumentoInexistente.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroDocumentoInexistente
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioNroDocumentoInexistente.GetId(),
				stepAudioNroDocumentoInexistente);

		stepAudioTarjetaReimpresa = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaReimpresa.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioTarjetaReimpresa
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioTarjetaReimpresa.GetId(), stepAudioTarjetaReimpresa);

		stepAudioNroTarjetaDuplicado = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjetaDuplicado.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjetaDuplicado
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioNroTarjetaDuplicado.GetId(),
				stepAudioNroTarjetaDuplicado);

		stepAudioNroTarjVencida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjVencida.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjVencida
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioNroTarjVencida.GetId(), stepAudioNroTarjVencida);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioNroTarjIncorrecto.GetId(),
				stepAudioNroTarjIncorrecto);

		/* Say Month */

		stepFechaReposicion = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaReposicion.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimaCargaContextVar"));
		stepFechaReposicion.setStepDescription("SAYMONTH => ULTIMA CARGA");
		Steps.put(stepFechaReposicion.GetId(), stepFechaReposicion);

		/* Say Number */

		stepNumeroSaldoAnterior = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumeroSaldoAnterior
				.setStepDescription("SAYNUMBER => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepNumeroSaldoAnterior.GetId(), stepNumeroSaldoAnterior);

		stepNumberoTramiteReimpresion = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberoTramiteReimpresion
				.setStepDescription("SAYNUMBER => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepNumberoTramiteReimpresion.GetId(),
				stepNumberoTramiteReimpresion);

		/* Limit play Read */

		stepAudioMenuReimpresion = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuReimpresion.setPlayFile("coto/A000013");
		stepAudioMenuReimpresion.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaContextVar"));
		stepAudioMenuReimpresion
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");
		Steps.put(stepAudioMenuReimpresion.GetId(), stepAudioMenuReimpresion);

		stepAudioSubMenuDenuncias = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioSubMenuDenuncias.setPlayFile("coto/A000013");
		stepAudioSubMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("subMenuDenunciaContextVar"));
		stepAudioSubMenuDenuncias
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");
		Steps.put(stepAudioSubMenuDenuncias.GetId(), stepAudioSubMenuDenuncias);

		/* Menu */

		stepMenuDenuncias = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuDenuncias.setStepDescription("MENU => MENU DENUNCIA");
		stepMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaContextVar"));
		Steps.put(stepMenuDenuncias.GetId(), stepMenuDenuncias);

		stepSubMenuDenuncias = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepSubMenuDenuncias.setStepDescription("MENU => SUB MENU DENUNCIA");
		stepSubMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("subMenuDenunciaContextVar"));
		Steps.put(stepSubMenuDenuncias.GetId(), stepSubMenuDenuncias);

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

		pideTarjetaGrp = (PideTarjeta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjeta);
		pideTarjetaGrp.setAudioTarjeta("RUTINAPIN/RUTINA_PIN025");
		pideTarjetaGrp.setAudioSuTarjetaEs("RUTINAPIN/RUTINA_PIN026");
		pideTarjetaGrp.setAudioTarjetaInvalido("RUTINAPIN/RUTINA_PIN016");
		pideTarjetaGrp.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideTarjetaGrp.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		pideTarjetaGrp.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContexVar"));

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

		/* JPOS */

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos.setContextVariableTipoMensaje(ctxVar
				.getContextVarByName("envioServerJposConsultasContexVar"));
		enviaTramaJpos.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		enviaTramaJpos.setContextVariableRspJpos(ctxVar
				.getContextVarByName("retornoMsgJPOS"));
		enviaTramaJpos.addformatoVariables(0, ctxVar
				.getContextVarByName("codigoOperacionReimpresionContextVar"));
		enviaTramaJpos.addformatoVariables(1,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(2,
				ctxVar.getContextVarByName("dniContexVar"));
		enviaTramaJpos.addformatoVariables(3,
				ctxVar.getContextVarByName("dniParaActivacionContextVar"));
		enviaTramaJpos.addformatoVariables(4,
				ctxVar.getContextVarByName("fillerParaReimpresionContexVar"));
		enviaTramaJpos.addformatoVariables(5,
				ctxVar.getContextVarByName("idLlamadaContexVar"));
		enviaTramaJpos.addformatoVariables(6,
				ctxVar.getContextVarByName("whisperContextVar"));
		enviaTramaJpos.setStepDescription("SENDJPOS => ENVIA TRAMA JPOS");
		Steps.put(enviaTramaJpos.GetId(), enviaTramaJpos);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		evalRetJPOS.setStepDescription("SWITCH => CODIGO RETORNO JPOS");
		Steps.put(evalRetJPOS.GetId(), evalRetJPOS);

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

	public PideTarjeta getPideTarjeta() {
		return pideTarjetaGrp;
	}

}
