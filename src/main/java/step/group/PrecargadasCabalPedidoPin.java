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

public class PrecargadasCabalPedidoPin implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private PideDni pideDniGrp;
	private PideFecha pideFechaGrp;
	private StepTimeConditionDB obtieneHorario;
	private StepPlay stepAudioServNoDisponible;
	private StepLimitPlayRead stepAudioMenuGeneracionPIN;
	private StepPlay stepAudioSuperoIntentos;
	private StepEnd pasoFinal;
	private StepPlay stepAudioIngresoIncorrectoDenuncias;
	private StepMenu stepMenuGeneracionPIN;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDni;
	private StepExecute stepDerivoLlamada;
	private StepPlay stepAudioDisuadeDerivoAsesor;
	private StepPlay stepAudioVerifiqueFechaNacimiento;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioNroTarjetaDuplicado;
	private StepPlay stepAudioTarjetaBloqueada;
	private StepPlay stepAudioNroDocumentoInexistente;
	private StepPlay stepAudioIngresoIncorrectoMenuGeneracionPIN;
	private StepPlay stepAudioIngresoIncorrectoSubMenuDenuncias;
	private StepSayMonth stepFechaReposicion;
	private StepPlay stepAudioDireccionReposicion;
	private StepPlay stepAudioFechaReposicion;
	private StepPlay stepAudioNumeroDenuncia;
	private StepSayNumber stepNumeroDenuncia;
	private StepSayNumber stepNumberoSaldoTransladado;
	private StepLimitPlayRead stepAudioSubMenuDenuncias;
	private StepMenu stepSubMenuDenuncias;
	private StepPlayFromVar stepDireccionReposicion;

	private void setSequence() {

		/* Menu Generacion PIN */

		stepAudioMenuGeneracionPIN.setNextstep(stepMenuGeneracionPIN.GetId());
		stepAudioMenuGeneracionPIN
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuGeneracionPIN.addSteps("1", pideDniGrp.getInitialStep());
		stepMenuGeneracionPIN.addSteps("9", stepIfTrueUUID);
		stepMenuGeneracionPIN
				.setInvalidOption(stepAudioIngresoIncorrectoMenuGeneracionPIN
						.GetId());

		stepAudioIngresoIncorrectoMenuGeneracionPIN
				.setNextstep(stepAudioMenuGeneracionPIN.GetId());

		/* Pide DNI */

		pideDniGrp.setStepIfTrue(pideFechaGrp.getInitialStep());
		pideDniGrp.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* Pide Fecha */

		pideFechaGrp.setStepIfTrue(enviaTramaJpos.GetId());
		pideFechaGrp.setStepIfFalse(stepAudioVerifiqueFechaNacimiento.GetId());

		/* Retorno 00 */

		stepAudioTarjetaBloqueada.setNextstep(stepNumberoSaldoTransladado
				.GetId());
		stepNumberoSaldoTransladado
				.setNextstep(stepAudioNumeroDenuncia.GetId());
		stepAudioNumeroDenuncia.setNextstep(stepNumeroDenuncia.GetId());
		stepNumeroDenuncia.setNextstep(stepAudioDireccionReposicion.GetId());
		stepAudioDireccionReposicion.setNextstep(stepDireccionReposicion
				.GetId());
		stepDireccionReposicion.setNextstep(stepAudioFechaReposicion.GetId());
		stepAudioFechaReposicion.setNextstep(stepFechaReposicion.GetId());

		stepFechaReposicion.setNextstep(stepFechaReposicion.GetId());

		stepAudioSubMenuDenuncias.setNextstep(stepSubMenuDenuncias.GetId());
		stepAudioSubMenuDenuncias
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepSubMenuDenuncias.addSteps("1", stepAudioTarjetaBloqueada.GetId());
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
		stepAudioVerifiqueFechaNacimiento.setNextstep(stepIfFalseUUID);

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
		evalRetJPOS.addSwitchValue("03",
				stepAudioNroDocumentoInexistente.GetId());
		evalRetJPOS.addSwitchValue("00", stepAudioTarjetaBloqueada.GetId());
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

	public PrecargadasCabalPedidoPin() {
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

		stepAudioNumeroDenuncia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNumeroDenuncia.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioNumeroDenuncia
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioNumeroDenuncia.GetId(), stepAudioNumeroDenuncia);

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

		stepAudioIngresoIncorrectoMenuGeneracionPIN = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuGeneracionPIN
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuGeneracionPIN
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioIngresoIncorrectoMenuGeneracionPIN.GetId(),
				stepAudioIngresoIncorrectoMenuGeneracionPIN);

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

		stepAudioVerifiqueFechaNacimiento = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueFechaNacimiento
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioVerifiqueFechaNacimiento
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioVerifiqueFechaNacimiento.GetId(),
				stepAudioVerifiqueFechaNacimiento);

		stepAudioNroDocumentoInexistente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroDocumentoInexistente.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroDocumentoInexistente
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioNroDocumentoInexistente.GetId(),
				stepAudioNroDocumentoInexistente);

		stepAudioTarjetaBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaBloqueada.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioTarjetaBloqueada
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioTarjetaBloqueada.GetId(), stepAudioTarjetaBloqueada);

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

		stepNumeroDenuncia = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumeroDenuncia
				.setStepDescription("SAYNUMBER => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepNumeroDenuncia.GetId(), stepNumeroDenuncia);

		stepNumberoSaldoTransladado = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberoSaldoTransladado
				.setStepDescription("SAYNUMBER => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepNumberoSaldoTransladado.GetId(),
				stepNumberoSaldoTransladado);

		/* Limit play Read */

		stepAudioMenuGeneracionPIN = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuGeneracionPIN.setPlayFile("coto/A000013");
		stepAudioMenuGeneracionPIN.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaContextVar"));
		stepAudioMenuGeneracionPIN
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");
		Steps.put(stepAudioMenuGeneracionPIN.GetId(),
				stepAudioMenuGeneracionPIN);

		stepAudioSubMenuDenuncias = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioSubMenuDenuncias.setPlayFile("coto/A000013");
		stepAudioSubMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("subMenuDenunciaContextVar"));
		stepAudioSubMenuDenuncias
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");
		Steps.put(stepAudioSubMenuDenuncias.GetId(), stepAudioSubMenuDenuncias);

		/* Menu */

		stepMenuGeneracionPIN = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuGeneracionPIN.setStepDescription("MENU => MENU DENUNCIA");
		stepMenuGeneracionPIN.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaContextVar"));
		Steps.put(stepMenuGeneracionPIN.GetId(), stepMenuGeneracionPIN);

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

		pideFechaGrp = (PideFecha) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFecha);
		pideFechaGrp.setAudioFecha("coto/A000055");
		pideFechaGrp.setAudioValidateFecha("RUTINAPIN/RUTINA_PIN010");
		pideFechaGrp.setAudioFechaInvalida("coto/A000056");
		pideFechaGrp.setAudioSuFechaEs("coto/A000058");
		pideFechaGrp.setAudioAnio("coto/A900012");
		pideFechaGrp.setAudioMes("coto/A900011");
		pideFechaGrp.setAudioDia("coto/A900012");
		pideFechaGrp.setfechaContextVar(ctxVar
				.getContextVarByName("fechaContextVar"));
		pideFechaGrp.setContextVarDia(ctxVar
				.getContextVarByName("diaContextVar"));
		pideFechaGrp.setContextVarMes(ctxVar
				.getContextVarByName("mesContextVar"));
		pideFechaGrp.setContextVarAnio(ctxVar
				.getContextVarByName("anioContextVar"));
		pideFechaGrp.setConfirmaFechaContextVar(ctxVar
				.getContextVarByName("confirmaFechaContextVar"));
		pideFechaGrp.setIntentosFechaContextVar(ctxVar
				.getContextVarByName("intentosFechaContextVar"));

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
				.getContextVarByName("codigoOperacionPedidoPinContextVar"));
		enviaTramaJpos.addformatoVariables(1,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(2,
				ctxVar.getContextVarByName("dniContexVar"));
		enviaTramaJpos.addformatoVariables(3,
				ctxVar.getContextVarByName("contextVarAnio"));
		enviaTramaJpos.addformatoVariables(4,
				ctxVar.getContextVarByName("contextVarMes"));
		enviaTramaJpos.addformatoVariables(5,
				ctxVar.getContextVarByName("contextVarDia"));
		enviaTramaJpos.addformatoVariables(6,
				ctxVar.getContextVarByName("nroCuentaParaPedidoPinContexVar"));
		enviaTramaJpos.addformatoVariables(7,
				ctxVar.getContextVarByName("pinCajeroContextVar"));
		enviaTramaJpos.addformatoVariables(8,
				ctxVar.getContextVarByName("pinComprasContextVar"));
		enviaTramaJpos.addformatoVariables(9,
				ctxVar.getContextVarByName("señalValidacionContexVar"));
		enviaTramaJpos.addformatoVariables(10,
				ctxVar.getContextVarByName("fillerParaPedidoPinContexVar"));
		enviaTramaJpos.addformatoVariables(11,
				ctxVar.getContextVarByName("idLlamadaContexVar"));
		enviaTramaJpos.addformatoVariables(12,
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

	public PideFecha getPideFecha() {
		return pideFechaGrp;
	}

}
