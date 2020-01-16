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

public class PrecargadasCabalDenunciaIvr implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private PideDni pideDniGrp;
	private PideFecha pideFechaGrp;
	private StepTimeConditionDB obtieneHorario;
	private StepExecute stepDerivoLlamada;
	private StepEnd pasoFinal;
	private StepMenu stepMenuDenuncias;
	private StepMenu stepSubMenuDenuncias;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepSayNumber stepNumeroDenuncia;
	private StepSayNumber stepNumberoSaldoTransladado;
	private StepSayMonth stepFechaReposicion;
	private StepPlayFromVar stepDireccionReposicion;
	private StepLimitPlayRead stepAudioMenuDenuncias;
	private StepLimitPlayRead stepAudioSubMenuDenuncias;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioSuperoIntentos;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDni;
	private StepPlay stepAudioDisuadeDerivoAsesor;
	private StepPlay stepAudioVerifiqueFechaNacimiento;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioNroTarjetaDuplicado;
	private StepPlay stepAudioTarjetaBloqueada;
	private StepPlay stepAudioNroDocumentoInexistente;
	private StepPlay stepAudioIngresoIncorrectoMenuDenuncias;
	private StepPlay stepAudioIngresoIncorrectoSubMenuDenuncias;
	private StepPlay stepAudioDireccionReposicion;
	private StepPlay stepAudioFechaReposicion;
	private StepPlay stepAudioNumeroDenuncia;

	private void setSequence() {

		/* Pide DNI */

		pideDniGrp.setStepIfTrue(pideFechaGrp.getInitialStep());
		pideDniGrp.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* Pide Fecha */

		pideFechaGrp.setStepIfTrue(stepAudioMenuDenuncias.GetId());
		pideFechaGrp.setStepIfFalse(stepAudioVerifiqueFechaNacimiento.GetId());

		/* Menu Denuncia Por IVR */

		stepAudioMenuDenuncias.setNextstep(stepMenuDenuncias.GetId());
		stepAudioMenuDenuncias
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuDenuncias.addSteps("1", enviaTramaJpos.GetId());
		stepMenuDenuncias.addSteps("9", stepIfTrueUUID);
		stepMenuDenuncias
				.setInvalidOption(stepAudioIngresoIncorrectoMenuDenuncias
						.GetId());

		stepAudioIngresoIncorrectoMenuDenuncias
				.setNextstep(stepAudioMenuDenuncias.GetId());

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

		stepFechaReposicion.setNextstep(stepAudioSubMenuDenuncias.GetId());

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
		 * ret =>  99    ||   "Tarjeta vencida / No está permitida la transición de estados en la parametría de la MEP"
		 * ret =>  98    ||   "Error en la longitud del mensaje / Código de identificación del mensaje erróneo"    
		 * ret =>  96    ||   "Tarjeta/documento inexistente "
		 * ret =>  94    ||   "Más de una tarjeta para ser bloqueada para el mismo documento"
		 * ret =>  03    ||   "Fecha de nacimiento ingresada difiere con el Archivo "
		 * ret =>  00    ||   "Ok"
		 *    
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("99", stepAudioNroTarjVencida.GetId());
		evalRetJPOS.addSwitchValue("98", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("96", stepAudioNroTarjIncorrecto.GetId());
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

	public PrecargadasCabalDenunciaIvr() {
		super();

		GroupType = StepGroupType.precargadasCabalDenunciaIvr;
	}

	private void createSteps() {

		/* Play */

		stepDireccionReposicion = (StepPlayFromVar) StepFactory.createStep(
				StepType.PlayFromVar, UUID.randomUUID());
		stepDireccionReposicion
				.setStepDescription("PLAY => DIRECCION REPOSICION");
		Steps.put(stepDireccionReposicion.GetId(), stepDireccionReposicion);

		/* Play */

		stepAudioDireccionReposicion = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDireccionReposicion.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioDireccionReposicion
				.setStepDescription("PLAY => DIRECCION REPOSICION");
		Steps.put(stepAudioDireccionReposicion.GetId(),
				stepAudioDireccionReposicion);

		stepAudioFechaReposicion = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaReposicion.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaReposicion.setStepDescription("PLAY => FECHA REPOSICION");
		Steps.put(stepAudioFechaReposicion.GetId(), stepAudioFechaReposicion);

		stepAudioNumeroDenuncia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNumeroDenuncia.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioNumeroDenuncia
				.setStepDescription("PLAY => NUMERO DE DENUNCIA");
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

		stepAudioIngresoIncorrectoMenuDenuncias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuDenuncias
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuDenuncias
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO - MENU DENUNCIA");
		Steps.put(stepAudioIngresoIncorrectoMenuDenuncias.GetId(),
				stepAudioIngresoIncorrectoMenuDenuncias);

		stepAudioIngresoIncorrectoSubMenuDenuncias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoSubMenuDenuncias
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoSubMenuDenuncias
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO - SUB MENU DENUNCIA");
		Steps.put(stepAudioIngresoIncorrectoSubMenuDenuncias.GetId(),
				stepAudioIngresoIncorrectoSubMenuDenuncias);

		stepAudioVerifiqueNumeroDni = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueNumeroDni.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioVerifiqueNumeroDni
				.setStepDescription("PLAY => VERIFICAR NUMERO DE DNI");
		Steps.put(stepAudioVerifiqueNumeroDni.GetId(),
				stepAudioVerifiqueNumeroDni);

		stepAudioVerifiqueFechaNacimiento = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueFechaNacimiento
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioVerifiqueFechaNacimiento
				.setStepDescription("PLAY => FECHA INCORRECTA");
		Steps.put(stepAudioVerifiqueFechaNacimiento.GetId(),
				stepAudioVerifiqueFechaNacimiento);

		stepAudioNroDocumentoInexistente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroDocumentoInexistente.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroDocumentoInexistente
				.setStepDescription("PLAY => DOCUMENTO INEXISTENTE");
		Steps.put(stepAudioNroDocumentoInexistente.GetId(),
				stepAudioNroDocumentoInexistente);

		stepAudioTarjetaBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaBloqueada.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioTarjetaBloqueada
				.setStepDescription("PLAY => TARJETA BLOQUEADA");
		Steps.put(stepAudioTarjetaBloqueada.GetId(), stepAudioTarjetaBloqueada);

		stepAudioNroTarjetaDuplicado = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjetaDuplicado.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjetaDuplicado
				.setStepDescription("PLAY => TARJETA DUPLICADA");
		Steps.put(stepAudioNroTarjetaDuplicado.GetId(),
				stepAudioNroTarjetaDuplicado);

		stepAudioNroTarjVencida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjVencida.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjVencida.setStepDescription("PLAY => TARJETA VENCIDA");
		Steps.put(stepAudioNroTarjVencida.GetId(), stepAudioNroTarjVencida);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => TARJETA INCORRECTA");
		Steps.put(stepAudioNroTarjIncorrecto.GetId(),
				stepAudioNroTarjIncorrecto);

		/* Say Month */

		stepFechaReposicion = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaReposicion.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimaCargaContextVar"));
		stepFechaReposicion
				.setStepDescription("SAYMONTH => FECHA DE REPOSICION");
		Steps.put(stepFechaReposicion.GetId(), stepFechaReposicion);

		/* Say Number */

		stepNumeroDenuncia = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumeroDenuncia.setStepDescription("SAYNUMBER => NUMERO DENUNCIA");
		Steps.put(stepNumeroDenuncia.GetId(), stepNumeroDenuncia);

		stepNumberoSaldoTransladado = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberoSaldoTransladado
				.setStepDescription("SAYNUMBER => CONSUMOS TRANSLADADOS POR DENUNCA");
		Steps.put(stepNumberoSaldoTransladado.GetId(),
				stepNumberoSaldoTransladado);

		/* Limit play Read */

		stepAudioMenuDenuncias = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuDenuncias.setPlayFile("coto/A000013");
		stepAudioMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("menuDenunciaContextVar"));
		stepAudioMenuDenuncias
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA");
		Steps.put(stepAudioMenuDenuncias.GetId(), stepAudioMenuDenuncias);

		stepAudioSubMenuDenuncias = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioSubMenuDenuncias.setPlayFile("coto/A000013");
		stepAudioSubMenuDenuncias.setContextVariableName(ctxVar
				.getContextVarByName("subMenuDenunciaContextVar"));
		stepAudioSubMenuDenuncias
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA");
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
		enviaTramaJpos.addformatoVariables(0,
				ctxVar.getContextVarByName("mensajeActivacionJpos"));
		enviaTramaJpos.addformatoVariables(1,
				ctxVar.getContextVarByName("numeroDeLineaContexVar"));
		enviaTramaJpos.addformatoVariables(2,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(3,
				ctxVar.getContextVarByName("dniParaActivacionContextVar"));
		enviaTramaJpos.addformatoVariables(4,
				ctxVar.getContextVarByName("contextVarAnio"));
		enviaTramaJpos.addformatoVariables(5,
				ctxVar.getContextVarByName("contextVarMes"));
		enviaTramaJpos.addformatoVariables(6,
				ctxVar.getContextVarByName("contextVarDia"));
		enviaTramaJpos.addformatoVariables(7,
				ctxVar.getContextVarByName("nroCuentaParaActivacionContexVar"));
		enviaTramaJpos.addformatoVariables(8,
				ctxVar.getContextVarByName("primerTarjetaContextVar"));
		enviaTramaJpos.addformatoVariables(9,
				ctxVar.getContextVarByName("fillerParaActivacionContexVar"));
		enviaTramaJpos.addformatoVariables(10,
				ctxVar.getContextVarByName("idLlamadaContexVar"));
		enviaTramaJpos.addformatoVariables(11,
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
		stepDerivoLlamada.setAppOptions(Daemon
				.getConfig("DERIVOOPERADORDENUNCIA"));
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