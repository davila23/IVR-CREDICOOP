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
import step.StepPlayFromVar;
import step.StepPlayRead;
import step.StepSendJPOS;
import step.StepSetAsteriskVariable;
import step.StepSetVariable;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class ActivacionTarjetaCOTO implements StepGroup {

	protected StepGroupType GroupType;
	private CallContext ctxVar;
	private String audioDni;
	private String audioDniInvalido;
	private String audioValidateDni;
	private String audioSuDniEs;
	private StepMenu stepMenuConfirmacion;
	private StepMenu stepMenuFinal;
	private StepMenu stepMenuPrimerTarjeta;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private PideTarjeta pideTarjetaGrp;
	private PideDni pideDniGrp;
	private PideFecha pideFechaGrp;
	private StepPlayRead stepAudioMenuIngresoActivacion;
	private StepPlayRead stepAudioMenuPrimerTarjeta;
	private StepEnd pasoFinal;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepSetVariable cambiaPrimerTarjeta;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioCantidadMaxDeReintentos;
	private StepPlay stepAudioDniIncorrecto;
	private StepPlay stepAudioFechaIncorrecta;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioServicioNoDisponible;
	private StepPlay stepAudioNoPuedeSerActivada;
	private StepPlay stepAudioTarjetaAdicionalActivada;
	private StepPlay stepAudioTarjetaActivada;
	private StepPlayRead stepAudioMenuFinal;
	private StepPlay stepAudioDerivoAsesor;
	private StepPlay stepAudioNoPuedeActivarUnAdicional;
	private StepPlay stepAudioTarjetaBloqueada;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioTarjetaYaEstaActiva;
	private StepPlay stepAudioFinal;
	private StepConditional evalContadorFechaJPOS;
	private StepConditional evalContadorDNIJPOS;
	private StepConditional evalContadorTarjetaJPOS;
	private StepCounter contadorIntentosDNIJPOS;
	private StepCounter contadorIntentosTarjetaInexistenteJPOS;
	private StepCounter contadorIntentosPrimerTarjeta;
	private StepCounter contadorIntentosMenuInicial;
	private StepCounter contadorIntentosFechaJPOS;
	private StepCounter contadorIntentosAudioFinal;
	private StepCounter contadorIntentosTarjetaJPOS;
	private StepExecute stepDerivoLlamada;
	private StepConditional evalSiEsTitular;
	private StepConditional evalContadorIntentosMenuInicial;
	private StepConditional evalContadorIntentosPrimerTarjeta;
	private StepConditional evalContadorIntentosMenuFinal;
	private StepSetVariable setDniActivacion;
	private StepPlay stepAudioVerificarDatos;
	private StepPlay stepAudioVerificarDatosDni;
	private StepSetVariable cambiaEjecutoJPOSFecha;
	private StepSetVariable cambiaEjecutoJPOSDni;
	private StepSetVariable cambiaEjecutoJPOS;
	private StepConditional evalSiEjecutoJPOSFecha;
	private StepConditional evalSiEjecutoJPOSDni;
	private StepTimeConditionDB obtieneHorario;
	private StepPlayFromVar stepAudioPrevioDerivoAsesor;
	private StepSetAsteriskVariable stepSetDni;
	private StepSetAsteriskVariable stepSetTarjeta;

	private void setSequence() {

		stepAudioMenuIngresoActivacion
				.setNextstep(stepMenuConfirmacion.GetId());

		stepMenuConfirmacion.addSteps("1", pideDniGrp.getInitialStep());
		stepMenuConfirmacion.addSteps("2", stepIfFalseUUID);
		stepMenuConfirmacion.setInvalidOption(contadorIntentosMenuInicial
				.GetId());

		contadorIntentosMenuInicial.setNextstep(evalContadorIntentosMenuInicial
				.GetId());
		evalContadorIntentosMenuInicial
				.addCondition(new condition(1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosMenuInicialContextVar").getVarName()
						+ "} < " + intentos, stepAudioMenuIngresoActivacion
						.GetId(), stepAudioFinal.GetId()));

		/* --- Ingreso 1 --- */

		// pideDniGrp.setStepIfTrue(setDniActivacion.GetId());
		// pideDniGrp.setStepIfFalse(stepAudioVerificarDatosDni.GetId());

		setDniActivacion.setNextstep(evalSiEjecutoJPOSDni.GetId());

		evalSiEjecutoJPOSDni.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("ejecutoJPOSContextVar")
						.getVarName() + "} == 1", enviaTramaJpos.GetId(),
				pideFechaGrp.getInitialStep()));

		// pideFechaGrp.setStepIfTrue(evalSiEjecutoJPOSFecha.GetId());
		// pideFechaGrp.setStepIfFalse(stepAudioVerificarDatos.GetId());

		evalSiEjecutoJPOSFecha.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("ejecutoJPOSContextVar")
						.getVarName() + "} == 1", enviaTramaJpos.GetId(),
				stepAudioMenuPrimerTarjeta.GetId()));

		stepAudioMenuPrimerTarjeta.setNextstep(stepMenuPrimerTarjeta.GetId());

		stepMenuPrimerTarjeta.addSteps("1", enviaTramaJpos.GetId());
		stepMenuPrimerTarjeta.addSteps("2", cambiaPrimerTarjeta.GetId());
		stepMenuPrimerTarjeta.setInvalidOption(stepAudioMenuPrimerTarjeta
				.GetId());

		cambiaPrimerTarjeta.setNextstep(enviaTramaJpos.GetId());
		enviaTramaJpos.setNextstep(cambiaEjecutoJPOS.GetId());

		cambiaEjecutoJPOS.setNextstep(evalRetJPOS.GetId());
		evalRetJPOS.setNextstep(stepAudioServNoDisponible.GetId());

		/* --- Menu Final --- */

		stepMenuFinal.addSteps("1", stepIfFalseUUID);
		stepMenuFinal.addSteps("9", stepAudioFinal.GetId());
		stepMenuFinal.setInvalidOption(contadorIntentosAudioFinal.GetId());

		contadorIntentosAudioFinal.setNextstep(evalContadorIntentosMenuFinal
				.GetId());

		evalContadorIntentosMenuFinal.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuFinalContextVar")
						.getVarName() + "} < " + intentos, stepAudioMenuFinal
				.GetId(), stepAudioFinal.GetId()));

		contadorIntentosFechaJPOS.setNextstep(evalContadorFechaJPOS.GetId());
		evalContadorFechaJPOS.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosFechaContextVar")
						.getVarName() + "} < " + intentos, pideFechaGrp
				.getInitialStep(), obtieneHorario.GetId()));

		contadorIntentosDNIJPOS.setNextstep(evalContadorDNIJPOS.GetId());
		evalContadorDNIJPOS.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosDniContextVar")
						.getVarName() + "} < " + intentos, pideDniGrp
				.getInitialStep(), obtieneHorario.GetId()));

		contadorIntentosTarjetaJPOS
				.setNextstep(evalContadorTarjetaJPOS.GetId());
		evalContadorTarjetaJPOS.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosTarjetaContextVar")
						.getVarName() + "} < " + intentos, pideTarjetaGrp
				.getInitialStep(), stepAudioDerivoAsesor.GetId()));

		contadorIntentosTarjetaInexistenteJPOS
				.setNextstep(evalContadorTarjetaJPOS.GetId());
		evalContadorTarjetaJPOS.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosTarjetaContextVar")
						.getVarName() + "} < " + intentos, pideTarjetaGrp
				.getInitialStep(), stepAudioFinal.GetId()));

		evalSiEsTitular.addCondition(new condition(1, "substring('#{"
				+ ctxVar.getContextVarByName("retornoMsgJPOS").getVarName()
				+ "}',67,68) == 'T'", stepAudioTarjetaActivada.GetId(),
				stepAudioTarjetaAdicionalActivada.GetId()));

		evalSiEjecutoJPOSFecha.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("ejecutoJPOSContextVar")
						.getVarName() + "} == 1", enviaTramaJpos.GetId(),
				stepAudioMenuPrimerTarjeta.GetId()));

		stepAudioVerificarDatosDni.setNextstep(stepAudioFinal.GetId());
		stepAudioVerificarDatos.setNextstep(stepAudioFinal.GetId());
		
		stepAudioServicioNoDisponible.setNextstep(stepAudioFinal.GetId());
		
		stepAudioFechaIncorrecta.setNextstep(contadorIntentosFechaJPOS.GetId());
		stepAudioDniIncorrecto.setNextstep(contadorIntentosDNIJPOS.GetId());

		stepAudioTarjetaYaEstaActiva.setNextstep(stepIfFalseUUID);

		/* --- OBTIENE HORARIO DE LA BASE --- */

		obtieneHorario.setNextStepIsTrue(stepAudioPrevioDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepAudioDerivoAsesor.GetId());

		/* --- AUDIOS FINALES --- */

		stepAudioMenuFinal.setNextstep(stepMenuFinal.GetId());
		stepAudioTarjetaActivada.setNextstep(stepAudioMenuFinal.GetId());
		stepAudioNoPuedeSerActivada.setNextstep(obtieneHorario.GetId());

		stepSetDni.setNextstep(stepSetTarjeta.GetId());
		stepSetTarjeta.setNextstep(stepDerivoLlamada.GetId());
		/*
		 * ANULADO
		 * 
		 * contadorIntentosPrimerTarjeta.setNextstep(
		 * evalContadorIntentosPrimerTarjeta.GetId());
		 * evalContadorIntentosPrimerTarjeta.addCondition(new condition(1, "#{"
		 * +
		 * ctxVar.getContextVarByName("intentosPrimerTarjetaContextVar").getVarName
		 * () + "} < " + intentos, stepAudioMenuPrimerTarjeta.GetId(),
		 * stepAudioFinal.GetId()));
		 */
	}

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		 * ret =>  88    ||   "Tarjeta con marca 3"    
		 * ret =>  83    ||   "Causa 08 y tarjeta adicional"
		 * ret =>  81    ||   "Tarjeta bloqueada"
		 * ret =>  99    ||   "Tarjeta inexistente"
		 *    
		 * ret =>  95    ||   "Tarjeta Vencida"   
		 * ret =>  97    ||   "Documento Erroneo"   
		 * ret =>  82    ||   "Fecha Erronea"   
		 * ret =>  96    ||   "Archivos Cerrados"   
		 * 
		 * 
		 * ret =>84/85/86/87 || "Problemas Cuenta"

		 * ret =>  80    ||   "Tarjeta ya esta Activada"   
		 * 
		 * ret =>  00    ||   "Tarjeta Activada"   
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("88", stepAudioNoPuedeSerActivada.GetId());
		evalRetJPOS.addSwitchValue("83",
				stepAudioNoPuedeActivarUnAdicional.GetId());

		evalRetJPOS.addSwitchValue("81", stepAudioTarjetaBloqueada.GetId());
		evalRetJPOS.addSwitchValue("99", obtieneHorario.GetId());

		evalRetJPOS.addSwitchValue("95", obtieneHorario.GetId());

		evalRetJPOS.addSwitchValue("97", stepAudioDniIncorrecto.GetId());
		evalRetJPOS.addSwitchValue("82", stepAudioFechaIncorrecta.GetId());
		evalRetJPOS.addSwitchValue("96", stepAudioServicioNoDisponible.GetId());

		evalRetJPOS.addSwitchValue("84", stepAudioNoPuedeSerActivada.GetId());
		evalRetJPOS.addSwitchValue("85", stepAudioNoPuedeSerActivada.GetId());
		evalRetJPOS.addSwitchValue("86", stepAudioNoPuedeSerActivada.GetId());
		evalRetJPOS.addSwitchValue("87", stepAudioNoPuedeSerActivada.GetId());

		evalRetJPOS.addSwitchValue("80", stepAudioTarjetaYaEstaActiva.GetId());
		evalRetJPOS.addSwitchValue("00", evalSiEsTitular.GetId());

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioMenuIngresoActivacion.GetId();
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

		for (Task tmpTask : pideDniGrp.getSteps().values()) {
			Steps.put(tmpTask.GetId(), tmpTask);
		}
		for (Task tmpTask : pideTarjetaGrp.getSteps().values()) {
			Steps.put(tmpTask.GetId(), tmpTask);
		}
		for (Task tmpTask : pideFechaGrp.getSteps().values()) {
			Steps.put(tmpTask.GetId(), tmpTask);
		}

		this.setSequence();
		return Steps;
	}

	public void setStepIfTrueUUID(UUID stepIfTrueUUID) {
		this.stepIfTrueUUID = stepIfTrueUUID;
	}

	public void setStepIfFalseUUID(UUID stepIfFalseUUID) {
		this.stepIfFalseUUID = stepIfFalseUUID;
	}

	public ActivacionTarjetaCOTO() {
		super();

		GroupType = StepGroupType.activacionCoto;
	}

	private void createSteps() {
		/*--- Audios --- */

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN LLAMADA");
		Steps.put(pasoFinal.GetId(), pasoFinal);

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

		stepAudioMenuIngresoActivacion = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuIngresoActivacion
				.setStepDescription("PLAYREAD => MENU INGRESO ACTIVACION COTO");
		stepAudioMenuIngresoActivacion.setPlayMaxDigits(1);
		stepAudioMenuIngresoActivacion.setPlayTimeout(2000L);
		stepAudioMenuIngresoActivacion.setPlayFile("coto/M000001");
		stepAudioMenuIngresoActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuIngresoActivacionContextVar"));
		Steps.put(stepAudioMenuIngresoActivacion.GetId(),
				stepAudioMenuIngresoActivacion);

		contadorIntentosMenuInicial = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuInicial.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuInicialContextVar"));
		contadorIntentosMenuInicial
				.setStepDescription("COUNTER => INTENTOS MENU PRINCIPAL ACTIVACION COTO");
		Steps.put(contadorIntentosMenuInicial.GetId(),
				contadorIntentosMenuInicial);

		evalContadorIntentosMenuInicial = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosMenuInicial
				.setStepDescription("CONDITIONAL => INTENTOS MENU INICIAL");
		Steps.put(evalContadorIntentosMenuInicial.GetId(),
				evalContadorIntentosMenuInicial);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => AUDIO FINAL");
		stepAudioFinal.setPlayfile("coto/A000026");
		stepAudioFinal.setNextstep(pasoFinal.GetId());
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

		stepAudioMenuPrimerTarjeta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuPrimerTarjeta
				.setStepDescription("PLAYREAD => PRIMER TARJETA");
		stepAudioMenuPrimerTarjeta.setPlayMaxDigits(1);
		stepAudioMenuPrimerTarjeta.setPlayTimeout(2000L);
		stepAudioMenuPrimerTarjeta.setPlayFile("coto/M000012");
		stepAudioMenuPrimerTarjeta.setContextVariableName(ctxVar
				.getContextVarByName("primerTarjetaContextVar"));
		Steps.put(stepAudioMenuPrimerTarjeta.GetId(),
				stepAudioMenuPrimerTarjeta);

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORCOTO"));
		stepDerivoLlamada.setStepDescription("EXECUTE => DERIVO ASESOR");
		Steps.put(stepDerivoLlamada.GetId(), stepDerivoLlamada);

		/*--- Validaciones --- */

		/*--- Menu --- */

		stepMenuConfirmacion = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuConfirmacion
				.setStepDescription("MENU => INICIO RUTINA ACTIVACION");
		stepMenuConfirmacion.setContextVariableName(ctxVar
				.getContextVarByName("menuIngresoActivacionContextVar"));
		Steps.put(stepMenuConfirmacion.GetId(), stepMenuConfirmacion);

		stepMenuFinal = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuFinal.setStepDescription("MENU => MENU FINAL");
		stepMenuFinal.setContextVariableName(ctxVar
				.getContextVarByName("menuFinalContextVar"));
		Steps.put(stepMenuFinal.GetId(), stepMenuFinal);

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

		stepMenuPrimerTarjeta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuPrimerTarjeta.setStepDescription("MENU => PRIMER TARJETA");
		stepMenuPrimerTarjeta.setContextVariableName(ctxVar
				.getContextVarByName("primerTarjetaContextVar"));
		Steps.put(stepMenuPrimerTarjeta.GetId(), stepMenuPrimerTarjeta);

		stepAudioCantidadMaxDeReintentos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentos
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentos.setNextstep(stepAudioFinal.GetId());
		stepAudioCantidadMaxDeReintentos
				.setStepDescription("PLAY => SUPERO LOS INTENTOS");
		Steps.put(stepAudioCantidadMaxDeReintentos.GetId(),
				stepAudioCantidadMaxDeReintentos);

		stepSetDni = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetDni.setStepDescription("SETASTERISKVARIABLE => OBTIENE DNI");
		stepSetDni.setVariableName("numdoc");
		stepSetDni.setContextVariableName(ctxVar
				.getContextVarByName("dniParaActivacionContextVar"));
		Steps.put(stepSetDni.GetId(), stepSetDni);

		stepSetTarjeta = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetTarjeta
				.setStepDescription("SETASTERISKVARIABLE => OBTIENE TARJETA");
		stepSetTarjeta.setVariableName("numtarj");
		stepSetTarjeta.setContextVariableName(ctxVar
				.getContextVarByName("tarjetaContexVar"));

		Steps.put(stepSetTarjeta.GetId(), stepSetTarjeta);

		contadorIntentosTarjetaJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjetaJPOS.setContextVariableName(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		contadorIntentosTarjetaJPOS
				.setStepDescription("COUNTER => INTENTOS CONTRA JPOS CON TARJETA");
		Steps.put(contadorIntentosTarjetaJPOS.GetId(),
				contadorIntentosTarjetaJPOS);

		contadorIntentosTarjetaInexistenteJPOS = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjetaInexistenteJPOS.setContextVariableName(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		contadorIntentosTarjetaInexistenteJPOS
				.setStepDescription("COUNTER => INTENTOS CONTRA JPOS CON TARJETA INEXISTENTE");
		Steps.put(contadorIntentosTarjetaInexistenteJPOS.GetId(),
				contadorIntentosTarjetaInexistenteJPOS);

		cambiaPrimerTarjeta = (StepSetVariable) StepFactory.createStep(
				StepType.SetVariable, UUID.randomUUID());
		cambiaPrimerTarjeta.setContextVariableOrigen(ctxVar
				.getContextVarByName("cambiaPrimerTarjetaContextVar"));
		cambiaPrimerTarjeta.setContextVariableDestino(ctxVar
				.getContextVarByName("primerTarjetaContextVar"));
		cambiaPrimerTarjeta
				.setStepDescription("SETVARIABLE => CAMBIA A 1 SI ES UNA REIMPRESION");
		Steps.put(cambiaPrimerTarjeta.GetId(), cambiaPrimerTarjeta);

		stepAudioServicioNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServicioNoDisponible.setPlayfile("coto/A000057");
		stepAudioServicioNoDisponible.setNextstep(stepAudioFinal.GetId());
		stepAudioServicioNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE. COD : 96");
		Steps.put(stepAudioServicioNoDisponible.GetId(),
				stepAudioServicioNoDisponible);

		evalContadorDNIJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDNIJPOS
				.setStepDescription("CONDITIONAL => INTENTOS DNI CONTRA JPOS");
		Steps.put(evalContadorDNIJPOS.GetId(), evalContadorDNIJPOS);

		contadorIntentosPrimerTarjeta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosPrimerTarjeta.setContextVariableName(ctxVar
				.getContextVarByName("intentosPrimerTarjetaContextVar"));
		contadorIntentosPrimerTarjeta
				.setStepDescription("COUNTER => INTENTOS PRIMER TARJETA");
		Steps.put(contadorIntentosPrimerTarjeta.GetId(),
				contadorIntentosPrimerTarjeta);

		cambiaEjecutoJPOS = (StepSetVariable) StepFactory.createStep(
				StepType.SetVariable, UUID.randomUUID());
		cambiaEjecutoJPOS.setContextVariableOrigen(ctxVar
				.getContextVarByName("cambiaEjecutoJPOSContextVar"));
		cambiaEjecutoJPOS.setContextVariableDestino(ctxVar
				.getContextVarByName("ejecutoJPOSContextVar"));
		cambiaEjecutoJPOS.setNextstep(evalRetJPOS.GetId());
		cambiaEjecutoJPOS
				.setStepDescription("SETVARIABLE => CAMBIA A 1 SI YA EJECUTO JPOS");
		Steps.put(cambiaEjecutoJPOS.GetId(), cambiaEjecutoJPOS);

		evalContadorIntentosPrimerTarjeta = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosPrimerTarjeta
				.setStepDescription("CONDITIONAL => INTENTOS PRIMER TARJETA");
		Steps.put(evalContadorIntentosPrimerTarjeta.GetId(),
				evalContadorIntentosPrimerTarjeta);

		contadorIntentosDNIJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosDNIJPOS.setContextVariableName(ctxVar
				.getContextVarByName("intentosDniContextVar"));
		contadorIntentosDNIJPOS
				.setStepDescription("COUNTER => INTENTOS DNI CONTRA JPOS");
		Steps.put(contadorIntentosDNIJPOS.GetId(), contadorIntentosDNIJPOS);

		evalContadorFechaJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFechaJPOS
				.setStepDescription("CONDITIONAL => INTENTOS FECHA CONTRA JPOS");
		Steps.put(evalContadorFechaJPOS.GetId(), evalContadorFechaJPOS);

		contadorIntentosFechaJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFechaJPOS.setContextVariableName(ctxVar
				.getContextVarByName("intentosFechaContextVar"));
		contadorIntentosFechaJPOS
				.setStepDescription("COUNTER => INTENTOS FECHA CONTRA JPOS");
		Steps.put(contadorIntentosFechaJPOS.GetId(), contadorIntentosFechaJPOS);

		evalContadorTarjetaJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorTarjetaJPOS
				.setStepDescription("CONDITIONAL => INTENTOS TARJETA CONTRA JPOS");
		Steps.put(evalContadorTarjetaJPOS.GetId(), evalContadorTarjetaJPOS);

		setDniActivacion = (StepSetVariable) StepFactory.createStep(
				StepType.SetVariable, UUID.randomUUID());
		setDniActivacion.setContextVariableOrigen(ctxVar
				.getContextVarByName("dniContextVar"));
		setDniActivacion.setContextVariableDestino(ctxVar
				.getContextVarByName("dniParaActivacionContextVar"));
		setDniActivacion
				.setStepDescription("SETVARIABLE => SET DNI = DNI ACTIVACION + 000");
		Steps.put(setDniActivacion.GetId(), setDniActivacion);

		contadorIntentosAudioFinal = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosAudioFinal.setContextVariableName(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		contadorIntentosAudioFinal
				.setStepDescription("COUNTER => INTENTOS AUDIO FINAL");
		Steps.put(contadorIntentosAudioFinal.GetId(),
				contadorIntentosAudioFinal);

		evalSiEjecutoJPOSDni = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalSiEjecutoJPOSDni
				.setStepDescription("CONDITIONAL => EJECUTO JPOS, DNI");
		Steps.put(evalSiEjecutoJPOSDni.GetId(), evalSiEjecutoJPOSDni);

		evalSiEjecutoJPOSFecha = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalSiEjecutoJPOSFecha
				.setStepDescription("CONDITIONAL => EJECUTO JPOS, FECHA");
		Steps.put(evalSiEjecutoJPOSFecha.GetId(), evalSiEjecutoJPOSFecha);

		evalContadorIntentosMenuFinal = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosMenuFinal
				.setStepDescription("COUNTER => INTENTOS MENU FINAL");
		Steps.put(evalContadorIntentosMenuFinal.GetId(),
				evalContadorIntentosMenuFinal);

		evalSiEsTitular = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSiEsTitular.setStepDescription("CONDITIONAL => ES TITULAR");
		Steps.put(evalSiEsTitular.GetId(), evalSiEsTitular);

		/*--- Retornos --- */

		stepAudioDniIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDniIncorrecto.setPlayfile("coto/A000009");
		stepAudioDniIncorrecto.setNextstep(contadorIntentosDNIJPOS.GetId());
		stepAudioDniIncorrecto
				.setStepDescription("PLAY => DNI INCORRECTO. COD : 02");
		Steps.put(stepAudioDniIncorrecto.GetId(), stepAudioDniIncorrecto);

		stepAudioFechaIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaIncorrecta.setPlayfile("coto/A000056");
		stepAudioFechaIncorrecta.setNextstep(contadorIntentosFechaJPOS.GetId());
		stepAudioFechaIncorrecta
				.setStepDescription("PLAY => FECHA INCORRECTA. COD : 03");
		Steps.put(stepAudioFechaIncorrecta.GetId(), stepAudioFechaIncorrecta);

		stepAudioNroTarjVencida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjVencida.setPlayfile("coto/A000013");
		stepAudioNroTarjVencida
				.setNextstep(contadorIntentosTarjetaJPOS.GetId());
		stepAudioNroTarjVencida
				.setStepDescription("PLAY => NUMERO DE TARJETA INCORRECTO. COD : 95");
		Steps.put(stepAudioNroTarjVencida.GetId(), stepAudioNroTarjVencida);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("coto/A000012");
		stepAudioNroTarjIncorrecto
				.setNextstep(contadorIntentosTarjetaInexistenteJPOS.GetId());
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => NUMERO DE TARJETA INCORRECTO. COD : 99");
		Steps.put(stepAudioNroTarjIncorrecto.GetId(),
				stepAudioNroTarjIncorrecto);

		stepAudioTarjetaBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaBloqueada.setPlayfile("coto/A000063");
		stepAudioTarjetaBloqueada
				.setStepDescription("PLAY => TARJETA BLOQUEADA. COD : 81");
		stepAudioTarjetaBloqueada.setNextstep(stepAudioFinal.GetId());
		Steps.put(stepAudioTarjetaBloqueada.GetId(), stepAudioTarjetaBloqueada);

		stepAudioNoPuedeActivarUnAdicional = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNoPuedeActivarUnAdicional.setPlayfile("coto/A000062");
		stepAudioNoPuedeActivarUnAdicional
				.setStepDescription("PLAY => LA TARJETA NO PUEDE SER ACTIVADA POR UN ADICIONAL. COD : 83");
		stepAudioNoPuedeActivarUnAdicional.setNextstep(stepAudioFinal.GetId());
		Steps.put(stepAudioNoPuedeActivarUnAdicional.GetId(),
				stepAudioNoPuedeActivarUnAdicional);

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("coto/A000013");
		stepAudioDerivoAsesor.setStepDescription("PLAY => DERIVO ASESOR");
		stepAudioDerivoAsesor.setNextstep(stepSetDni.GetId());
		Steps.put(stepAudioDerivoAsesor.GetId(), stepAudioDerivoAsesor);

		stepAudioNoPuedeSerActivada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNoPuedeSerActivada.setPlayfile("coto/A000061");
		stepAudioNoPuedeSerActivada.setNextstep(obtieneHorario.GetId());
		stepAudioNoPuedeSerActivada
				.setStepDescription("PLAY => LA TARJETA NO PUEDE SER ACTIVADA TELEFONICAMENTE. COD : 88");
		Steps.put(stepAudioNoPuedeSerActivada.GetId(),
				stepAudioNoPuedeSerActivada);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible.setNextstep(stepAudioFinal.GetId());
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE. COD : 96");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		stepAudioTarjetaYaEstaActiva = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaYaEstaActiva.setPlayfile("coto/A000064");
		stepAudioTarjetaYaEstaActiva
				.setStepDescription("PLAY => TARJETA YA ACTIVADA");
		Steps.put(stepAudioTarjetaYaEstaActiva.GetId(),
				stepAudioTarjetaYaEstaActiva);

		stepAudioMenuFinal = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuFinal.setPlayFile("coto/M000019");
		stepAudioMenuFinal.setStepDescription("PLAYREAD => MENU FINAL COTO");
		stepAudioMenuFinal.setNextstep(stepMenuFinal.GetId());
		stepAudioMenuFinal.setContextVariableName(ctxVar
				.getContextVarByName("menuFinalContextVar"));
		Steps.put(stepAudioMenuFinal.GetId(), stepAudioMenuFinal);

		stepAudioVerificarDatos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatos.setPlayfile("coto/A000057");
		stepAudioVerificarDatos.setStepDescription("PLAY => VERIFICAR DATOS");
		Steps.put(stepAudioVerificarDatos.GetId(), stepAudioVerificarDatos);

		stepAudioVerificarDatosDni = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatosDni.setPlayfile("coto/A000008");
		stepAudioVerificarDatosDni
				.setStepDescription("PLAY => VERIFICAR DATOS DNI");
		Steps.put(stepAudioVerificarDatosDni.GetId(),
				stepAudioVerificarDatosDni);

		stepAudioTarjetaAdicionalActivada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaAdicionalActivada.setPlayfile("coto/A000066");
		stepAudioTarjetaAdicionalActivada.setNextstep(stepAudioMenuFinal
				.GetId());
		stepAudioTarjetaAdicionalActivada
				.setStepDescription("PLAY => TARJETA ADICIONAL ACTIVADA");
		Steps.put(stepAudioTarjetaAdicionalActivada.GetId(),
				stepAudioTarjetaAdicionalActivada);

		stepAudioTarjetaActivada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaActivada.setPlayfile("coto/A000065");
		stepAudioTarjetaActivada.setNextstep(stepAudioMenuFinal.GetId());
		stepAudioTarjetaActivada
				.setStepDescription("PLAY => ACTIVACION TARJETA COTO OK");
		Steps.put(stepAudioTarjetaActivada.GetId(), stepAudioTarjetaActivada);

		stepAudioPrevioDerivoAsesor = (StepPlayFromVar) StepFactory.createStep(
				StepType.PlayFromVar, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		stepAudioPrevioDerivoAsesor.setNextstep(stepAudioFinal.GetId());
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAYFORMVAR => AUDIO PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		pideFechaGrp = (PideFecha) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFecha);
		pideFechaGrp.setAudioFecha("coto/A000055");
		pideFechaGrp.setAudioValidateFecha("RUTINAPIN/RUTINA_PIN010");
		pideFechaGrp.setAudioFechaInvalida("coto/A000056");
		pideFechaGrp.setAudioSuFechaEs("coto/A000058");
		pideFechaGrp.setAudioAnio("coto/A900012");
		pideFechaGrp.setAudioMes("coto/A900011");
		pideFechaGrp.setAudioDia("coto/A900010");
		pideFechaGrp.setfechaContextVar(ctxVar
				.getContextVarByName("fechaContextVar"));
		pideFechaGrp.setContextVarDia(ctxVar
				.getContextVarByName("contextVarDia"));
		pideFechaGrp.setContextVarMes(ctxVar
				.getContextVarByName("contextVarMes"));
		pideFechaGrp.setContextVarAnio(ctxVar
				.getContextVarByName("contextVarAnio"));
		pideFechaGrp.setConfirmaFechaContextVar(ctxVar
				.getContextVarByName("confirmaFechaContextVar"));
		pideFechaGrp.setIntentosFechaContextVar(ctxVar
				.getContextVarByName("intentosFechaContextVar"));
		pideFechaGrp.setStepIfTrue(evalSiEjecutoJPOSFecha.GetId());
		pideFechaGrp.setStepIfFalse(stepAudioVerificarDatos.GetId());

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
		pideDniGrp.setStepIfTrue(setDniActivacion.GetId());
		pideDniGrp.setStepIfFalse(stepAudioVerificarDatosDni.GetId());

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
		pideTarjetaGrp.setStepIfTrue(enviaTramaJpos.GetId());
		pideTarjetaGrp.setStepIfFalse(contadorIntentosTarjetaJPOS.GetId());

		this.evalRetJPOS();
	}

	public void setContextVar(CallContext ctx) {
		this.ctxVar = ctx;
		createSteps();
	}

}
