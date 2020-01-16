package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepLimitPlayRead;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayFromVar;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PrecargadasCabalActivacion implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private PideDni pideDniGrp;
	private PideTarjeta pideTarjetaGrpJPOS;
	private PideFecha pideFechaGrp;
	private PideDni pideDniGrpJPOS;
	private PideTarjeta pideFechaGrpJPOS;
	private StepTimeConditionDB obtieneHorario;
	private StepEnd pasoFinal;
	private StepExecute stepDerivoLlamada;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private StepMenu stepMenuActivacion;
	private StepMenu stepMenuFinActivacion;
	private StepPlayFromVar stepDireccionReposicion;
	private StepLimitPlayRead stepAudioMenuActivacion;
	private StepLimitPlayRead stepAudioMenuFinActivacion;
	private StepLimitPlayRead stepAudioMenuConfirmacionActivacion;
	private StepConditional evalContadorTarjeta;
	private StepConditional evalEsTitular;
	private StepConditional evalContadorDNI;
	private StepConditional evalContadorFecha;
	private StepCounter contadorIntentosDNI;
	private StepCounter contadorIntentosTarjeta;
	private StepCounter contadorIntentosFecha;
	private StepPlay stepAudioTarjetaAdicionalActivada;
	private StepPlay stepAudioDisuadeDerivoAsesor;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioSuperoIntentos;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioVerifiqueNumeroDni;
	private StepPlay stepAudioVerifiqueNumeroDeTarjeta;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioNroTarjetaDuplicado;
	private StepPlay stepAudioTarjetaBloqueada;
	private StepPlay stepAudioNroDocumentoInexistente;
	private StepPlay stepAudioIngresoIncorrectoMenuActivacion;
	private StepPlay stepAudioIngresoIncorrectoMenuFinActivacion;
	private StepPlay stepAudioVerifiqueFechaNacimiento;
	private StepPlay stepAudioIngresoIncorrectoMenuConfirmacionActivacion;
	private StepPlay stepAudioTarjetaTitularActivada;
	private StepPlay stepAudioNoEsPosibleActivarTelefonicamente;
	private StepPlay stepAudioFechaDeNacimientoErronea;
	private StepPlay stepAudioTarjetaAdicional;
	private StepPlay stepAudioServNoDisponibleEmisionTarjeta;
	private StepPlay stepAudioTarjetaActiva;

	private void setSequence() {

		/* Menu Denuncia Por IVR */

		stepAudioMenuActivacion.setNextstep(stepMenuActivacion.GetId());
		stepAudioMenuActivacion
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuActivacion.addSteps("1", pideDniGrp.getInitialStep());
		stepMenuActivacion.addSteps("9", stepIfTrueUUID);
		stepMenuActivacion
				.setInvalidOption(stepAudioIngresoIncorrectoMenuActivacion
						.GetId());

		stepAudioIngresoIncorrectoMenuActivacion
				.setNextstep(stepAudioMenuActivacion.GetId());

		/* Pide DNI */

		pideDniGrp.setStepIfTrue(pideFechaGrp.getInitialStep());
		pideDniGrp.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* Pide Fecha */

		pideFechaGrp.setStepIfTrue(enviaTramaJpos.GetId());
		pideFechaGrp.setStepIfFalse(stepAudioVerifiqueFechaNacimiento.GetId());

		/* Retorno 00 */

		evalEsTitular.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("titularContextVar").getVarName()
				+ "} == " + 1, stepAudioTarjetaTitularActivada.GetId(),
				stepAudioTarjetaAdicionalActivada.GetId()));

		stepAudioTarjetaTitularActivada.setNextstep(stepAudioMenuFinActivacion
				.GetId());
		stepAudioTarjetaAdicionalActivada
				.setNextstep(stepAudioMenuFinActivacion.GetId());

		stepAudioMenuFinActivacion.setNextstep(stepMenuFinActivacion.GetId());
		stepAudioMenuFinActivacion
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuFinActivacion.addSteps("1", stepIfFalseUUID);
		stepMenuFinActivacion.addSteps("9", stepIfTrueUUID);
		stepMenuFinActivacion
				.setInvalidOption(stepAudioIngresoIncorrectoMenuFinActivacion
						.GetId());

		stepAudioIngresoIncorrectoMenuFinActivacion
				.setNextstep(stepAudioMenuFinActivacion.GetId());

		/* RET 99 */

		stepAudioNroTarjVencida.setNextstep(contadorIntentosTarjeta.GetId());

		contadorIntentosTarjeta.setNextstep(evalContadorTarjeta.GetId());
		evalContadorTarjeta.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("titularContextVar").getVarName()
				+ "} < " + intentos, pideTarjetaGrpJPOS.getInitialStep(),
				stepIfFalseUUID));

		pideTarjetaGrpJPOS.setStepIfTrue(enviaTramaJpos.GetId());
		pideTarjetaGrpJPOS.setStepIfFalse(stepAudioVerifiqueNumeroDeTarjeta
				.GetId());

		/* RET 97 */

		stepAudioNroDocumentoInexistente.setNextstep(contadorIntentosDNI
				.GetId());

		contadorIntentosDNI.setNextstep(evalContadorDNI.GetId());
		evalContadorDNI.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("titularContextVar").getVarName()
				+ "} < " + intentos, pideDniGrp.getInitialStep(),
				stepIfFalseUUID));

		pideDniGrpJPOS.setStepIfTrue(enviaTramaJpos.GetId());
		pideDniGrpJPOS.setStepIfFalse(stepAudioVerifiqueNumeroDni.GetId());

		/* RET 82 */

		stepAudioFechaDeNacimientoErronea.setNextstep(contadorIntentosFecha
				.GetId());

		contadorIntentosFecha.setNextstep(evalContadorFecha.GetId());
		evalContadorFecha.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("titularContextVar").getVarName()
				+ "} < " + intentos, pideDniGrp.getInitialStep(),
				stepIfFalseUUID));

		pideFechaGrpJPOS.setStepIfTrue(enviaTramaJpos.GetId());
		pideFechaGrpJPOS.setStepIfFalse(stepAudioVerifiqueFechaNacimiento
				.GetId());

		/* Secuencias comunes */

		enviaTramaJpos.setNextstep(evalRetJPOS.GetId());

		stepAudioVerifiqueNumeroDni.setNextstep(stepIfFalseUUID);
		stepAudioVerifiqueNumeroDeTarjeta.setNextstep(stepIfFalseUUID);
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
		 * ret =>  80    ||   "Tarjeta con  Causa 00"
		 * ret =>  81    ||   "Causa diferente a 08 y/o 18"
		 * ret =>  82    ||   "Fecha de nacimiento erróne"
		 * ret =>  83    ||   "Tarjeta Adicional con causa 08"
		 * ret =>  84    ||   "Cuenta con  causa 08 sin señal de activación  telefónica"
		 * ret =>  85    ||   "Tarjeta con  causa 18 y Cuenta con  causa diferente a 00"    
		 * ret =>  86    ||   "Tarjeta con  causa 08 y Cuenta con  causa diferente a 08"
		 * ret =>  87    ||   "Entidad  no habilitada para activación "
		 * ret =>  88    ||   "Incongruencia en señal Tarjeta con marca 3(Sólo para Entidad 900 –AUDIOSCABAL)"
		 * ret =>  95    ||   "Tarjeta vencida (Sólo para Entidad 900-AUDIOSCABAL)"    
		 * ret =>  96    ||   "Archivos no disponibles"
		 * ret =>  98    ||   "Documento erróneo"
		 * ret =>  99    ||   "Cuenta inexistente  / Tarjeta vencida  / Tarjeta inexistente"
		 * ret =>  00    ||   "Ok"
		 *    
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("80", stepAudioTarjetaActiva.GetId());
		evalRetJPOS.addSwitchValue("81", stepAudioTarjetaBloqueada.GetId());
		evalRetJPOS.addSwitchValue("82",
				stepAudioFechaDeNacimientoErronea.GetId());
		evalRetJPOS.addSwitchValue("83", stepAudioTarjetaAdicional.GetId());
		evalRetJPOS.addSwitchValue("84",
				stepAudioNoEsPosibleActivarTelefonicamente.GetId());
		evalRetJPOS.addSwitchValue("85",
				stepAudioNoEsPosibleActivarTelefonicamente.GetId());
		evalRetJPOS.addSwitchValue("86",
				stepAudioNoEsPosibleActivarTelefonicamente.GetId());
		evalRetJPOS.addSwitchValue("87",
				stepAudioServNoDisponibleEmisionTarjeta.GetId());
		evalRetJPOS.addSwitchValue("88",
				stepAudioServNoDisponibleEmisionTarjeta.GetId());
		evalRetJPOS.addSwitchValue("95", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("96", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("97",
				stepAudioNroDocumentoInexistente.GetId());
		evalRetJPOS.addSwitchValue("98",
				stepAudioNroDocumentoInexistente.GetId());
		evalRetJPOS.addSwitchValue("99", stepAudioNroTarjVencida.GetId());
		evalRetJPOS.addSwitchValue("00", evalEsTitular.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	@Override
	public UUID getInitialStep() {
		return stepAudioMenuActivacion.getNextstep();
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

	public PrecargadasCabalActivacion() {
		super();

		GroupType = StepGroupType.precargadasCabalDenunciaAsesor;
	}

	private void createSteps() {

		/* Play */

		stepDireccionReposicion = (StepPlayFromVar) StepFactory.createStep(
				StepType.PlayFromVar, UUID.randomUUID());
		stepDireccionReposicion
				.setStepDescription("PLAY => DIRECCION DE REPOSICION");
		Steps.put(stepDireccionReposicion.GetId(), stepDireccionReposicion);

		/* Play */

		stepAudioServNoDisponibleEmisionTarjeta = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponibleEmisionTarjeta.setPlayfile("ADIOSCABAL/0058");
		stepAudioServNoDisponibleEmisionTarjeta
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE PARA EMISION DE TARJETA");
		Steps.put(stepAudioServNoDisponibleEmisionTarjeta.GetId(),
				stepAudioServNoDisponibleEmisionTarjeta);

		stepAudioTarjetaAdicional = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaAdicional.setPlayfile("ADIOSCABAL/0060");
		stepAudioTarjetaAdicional
				.setStepDescription("PLAY => CAUSA 08 O TARJETA ADICIONAL");
		Steps.put(stepAudioTarjetaAdicional.GetId(), stepAudioTarjetaAdicional);

		stepAudioFechaDeNacimientoErronea = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaDeNacimientoErronea.setPlayfile("ADIOSCABAL/099");
		stepAudioFechaDeNacimientoErronea
				.setStepDescription("PLAY => FECHA NACIMIENTO ERRONEA");
		Steps.put(stepAudioFechaDeNacimientoErronea.GetId(),
				stepAudioFechaDeNacimientoErronea);

		stepAudioNoEsPosibleActivarTelefonicamente = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioNoEsPosibleActivarTelefonicamente
				.setPlayfile("ADIOSCABAL/0059");
		stepAudioNoEsPosibleActivarTelefonicamente
				.setStepDescription("PLAY => NO PUEDE ACTIVAR TELEFONICAMENTE");
		Steps.put(stepAudioNoEsPosibleActivarTelefonicamente.GetId(),
				stepAudioNoEsPosibleActivarTelefonicamente);

		stepAudioTarjetaTitularActivada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaTitularActivada.setPlayfile("ADIOSCABAL/0064");
		stepAudioTarjetaTitularActivada
				.setStepDescription("PLAY => TARJETA TITULAR ACTIVADA");
		Steps.put(stepAudioTarjetaTitularActivada.GetId(),
				stepAudioTarjetaTitularActivada);

		stepAudioTarjetaAdicionalActivada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaAdicionalActivada.setPlayfile("ADIOSCABAL/0063");
		stepAudioTarjetaAdicionalActivada
				.setStepDescription("PLAY => TARJETA ADICIONAL ACTIVADA");
		Steps.put(stepAudioTarjetaAdicionalActivada.GetId(),
				stepAudioTarjetaAdicionalActivada);

		stepAudioDisuadeDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisuadeDerivoAsesor.setPlayfile("ADIOSCABAL/099");
		stepAudioDisuadeDerivoAsesor
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioDisuadeDerivoAsesor.GetId(),
				stepAudioDisuadeDerivoAsesor);

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("ADIOSCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		stepAudioIngresoIncorrectoMenuActivacion = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuActivacion
				.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuActivacion
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoMenuActivacion.GetId(),
				stepAudioIngresoIncorrectoMenuActivacion);

		stepAudioIngresoIncorrectoMenuConfirmacionActivacion = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuConfirmacionActivacion
				.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuConfirmacionActivacion
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoMenuConfirmacionActivacion.GetId(),
				stepAudioIngresoIncorrectoMenuConfirmacionActivacion);

		stepAudioIngresoIncorrectoMenuFinActivacion = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuFinActivacion
				.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMenuFinActivacion
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoMenuFinActivacion.GetId(),
				stepAudioIngresoIncorrectoMenuFinActivacion);

		stepAudioVerifiqueFechaNacimiento = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueFechaNacimiento.setPlayfile("AUDIOSCABAL/0035");
		stepAudioVerifiqueFechaNacimiento
				.setStepDescription("PLAY => VERIFIQUE FECHA DE NACIMIENTO");
		Steps.put(stepAudioVerifiqueFechaNacimiento.GetId(),
				stepAudioVerifiqueFechaNacimiento);

		stepAudioVerifiqueNumeroDni = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueNumeroDni.setPlayfile("AUDIOSCABAL/0035");
		stepAudioVerifiqueNumeroDni.setStepDescription("PLAY => VERIFIQUE DNI");
		Steps.put(stepAudioVerifiqueNumeroDni.GetId(),
				stepAudioVerifiqueNumeroDni);

		stepAudioVerifiqueNumeroDeTarjeta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueNumeroDeTarjeta.setPlayfile("AUDIOSCABAL/0035");
		stepAudioVerifiqueNumeroDeTarjeta
				.setStepDescription("PLAY => VERIFIQUE NUMERO DE TARJETA");
		Steps.put(stepAudioVerifiqueNumeroDeTarjeta.GetId(),
				stepAudioVerifiqueNumeroDeTarjeta);

		stepAudioNroDocumentoInexistente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroDocumentoInexistente.setPlayfile("AUDIOSCABAL/0029");
		stepAudioNroDocumentoInexistente
				.setStepDescription("PLAY => NUMERO DE DOCUMENTO INEXISTENTE");
		Steps.put(stepAudioNroDocumentoInexistente.GetId(),
				stepAudioNroDocumentoInexistente);

		stepAudioTarjetaBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaBloqueada.setPlayfile("AUDIOSCABAL/0061");
		stepAudioTarjetaBloqueada
				.setStepDescription("PLAY => TARJETA BLOQUEADA");
		Steps.put(stepAudioTarjetaBloqueada.GetId(), stepAudioTarjetaBloqueada);

		stepAudioTarjetaActiva = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaActiva.setPlayfile("AUDIOSCABAL/0062");
		stepAudioTarjetaActiva.setStepDescription("PLAY => TARJETA ACTIVA");
		Steps.put(stepAudioTarjetaActiva.GetId(), stepAudioTarjetaActiva);

		stepAudioNroTarjetaDuplicado = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjetaDuplicado.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioNroTarjetaDuplicado
				.setStepDescription("PLAY => NRO DE TARJETA DUPLICADO");
		Steps.put(stepAudioNroTarjetaDuplicado.GetId(),
				stepAudioNroTarjetaDuplicado);

		stepAudioNroTarjVencida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjVencida.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioNroTarjVencida
				.setStepDescription("PLAY => NRO DE TARJETA VENCIDA");
		Steps.put(stepAudioNroTarjVencida.GetId(), stepAudioNroTarjVencida);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("AUDIOSCABAL/RUTINA_PIN021");
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => NRO DE TARJETA INCORRECTO");
		Steps.put(stepAudioNroTarjIncorrecto.GetId(),
				stepAudioNroTarjIncorrecto);

		/* Limit play Read */

		stepAudioMenuFinActivacion = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuFinActivacion.setPlayFile("AUDIOSCABAL/A000013");
		stepAudioMenuFinActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuFinActivacionContextVar"));
		stepAudioMenuFinActivacion
				.setStepDescription("LIMITPLAYREAD => MENU FIN ACTIVADOR");
		Steps.put(stepAudioMenuFinActivacion.GetId(),
				stepAudioMenuFinActivacion);

		stepAudioMenuActivacion = (StepLimitPlayRead) StepFactory.createStep(
				StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuActivacion.setPlayFile("AUDIOSCABAL/0057");
		stepAudioMenuActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuActivacionContextVar"));
		stepAudioMenuActivacion
				.setStepDescription("LIMITPLAYREAD => MENU ACTIVACION");
		Steps.put(stepAudioMenuActivacion.GetId(), stepAudioMenuActivacion);

		stepAudioMenuConfirmacionActivacion = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuConfirmacionActivacion.setPlayFile("AUDIOSCABAL/A000013");
		stepAudioMenuConfirmacionActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuConfirmacionActivacionContextVar"));
		stepAudioMenuConfirmacionActivacion
				.setStepDescription("LIMITPLAYREAD => MENU CONFIRMACION ACTIVACION");
		Steps.put(stepAudioMenuConfirmacionActivacion.GetId(),
				stepAudioMenuConfirmacionActivacion);

		/* Menu */

		stepMenuActivacion = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuActivacion.setStepDescription("MENU => MENU ACTIVACION");
		stepMenuActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuActivacionContextVar"));
		Steps.put(stepMenuActivacion.GetId(), stepMenuActivacion);

		stepMenuFinActivacion = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuFinActivacion.setContextVariableName(ctxVar
				.getContextVarByName("menuFinActivacionContextVar"));
		stepMenuFinActivacion
				.setStepDescription("MENU => MENU  FIN ACTIVACION");
		Steps.put(stepMenuFinActivacion.GetId(), stepMenuFinActivacion);

		/* FIN */

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN COMUNICACION PRECARGADA");

		/* GRUPOS */
		pideDniGrp = (PideDni) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDni);
		pideDniGrp.setAudioDni("AUDIOSCABAL/0028");
		pideDniGrp.setAudioValidateDni("AUDIOSCABAL/0032");
		pideDniGrp.setAudioDniInvalido("AUDIOSCABAL/0029");
		pideDniGrp.setAudioSuDniEs("AUDIOSCABAL/0031");
		pideDniGrp
				.setDniContextVar(ctxVar.getContextVarByName("dniContextVar"));
		pideDniGrp.setIntentosDniContextVar(ctxVar
				.getContextVarByName("intentosDniContextVar"));
		pideDniGrp.setConfirmaDniContextVar(ctxVar
				.getContextVarByName("confirmaDniContextVar"));

		pideDniGrpJPOS = (PideDni) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDni);
		pideDniGrpJPOS.setAudioDni("AUDIOSCABAL/0028");
		pideDniGrpJPOS.setAudioValidateDni("AUDIOSCABAL/0032");
		pideDniGrpJPOS.setAudioDniInvalido("AUDIOSCABAL/0029");
		pideDniGrpJPOS.setAudioSuDniEs("AUDIOSCABAL/0031");
		pideDniGrpJPOS.setDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		pideDniGrpJPOS.setIntentosDniContextVar(ctxVar
				.getContextVarByName("intentosDniJPOSContextVar"));
		pideDniGrpJPOS.setConfirmaDniContextVar(ctxVar
				.getContextVarByName("confirmaDniJPOSContextVar"));

		pideTarjetaGrpJPOS = (PideTarjeta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjeta);
		pideTarjetaGrpJPOS.setAudioTarjeta("AUDIOSCABAL/A0000010");
		pideTarjetaGrpJPOS.setAudioSuTarjetaEs("AUDIOSCABAL/RUTINA_PIN026");
		pideTarjetaGrpJPOS.setAudioTarjetaInvalido("AUDIOSCABAL/RUTINA_PIN026");
		pideTarjetaGrpJPOS.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideTarjetaGrpJPOS.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		pideTarjetaGrpJPOS.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContexVar"));

		pideFechaGrpJPOS = (PideTarjeta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjeta);
		pideFechaGrpJPOS.setAudioTarjeta("AUDIOSCABAL/A0000010");
		pideFechaGrpJPOS.setAudioSuTarjetaEs("AUDIOSCABAL/RUTINA_PIN026");
		pideFechaGrpJPOS.setAudioTarjetaInvalido("AUDIOSCABAL/RUTINA_PIN026");
		pideFechaGrpJPOS.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideFechaGrpJPOS.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		pideFechaGrpJPOS.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContexVar"));

		pideFechaGrp = (PideFecha) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFecha);
		pideFechaGrp.setAudioFecha("AUDIOSCABAL/0033");
		pideFechaGrp.setAudioValidateFecha("AUDIOSCABAL/0032");
		pideFechaGrp.setAudioFechaInvalida("AUDIOSCABAL/0034");
		pideFechaGrp.setAudioSuFechaEs("AUDIOSCABAL/0036");
		pideFechaGrp.setAudioAnio("AUDIOSCABAL/0039");
		pideFechaGrp.setAudioMes("AUDIOSCABAL/0038");
		pideFechaGrp.setAudioDia("AUDIOSCABAL/0037");
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
		obtieneHorario.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorario.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorario.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		obtieneHorario
				.setStepDescription("TIMECONDITIONDB => OBTIENE HORARIO DE LA BASE");
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
				.getContextVarByName("codigoOperacionActivacionContextVar"));
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

		/* Conditional */

		evalEsTitular = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalEsTitular.setStepDescription("CONDITIONAL => ES TITULAR");
		Steps.put(evalEsTitular.GetId(), evalEsTitular);

		evalContadorTarjeta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorTarjeta
				.setStepDescription("CONDITIONAL => CONTADOR TARJETA");
		Steps.put(evalContadorTarjeta.GetId(), evalContadorTarjeta);

		evalContadorDNI = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDNI.setStepDescription("CONDITIONAL => CONTADOR DNI");
		Steps.put(evalContadorDNI.GetId(), evalContadorDNI);

		evalContadorFecha = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFecha.setStepDescription("CONDITIONAL => CONTADOR FECHA");
		Steps.put(evalContadorFecha.GetId(), evalContadorFecha);

		/* Derivo */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon
				.getConfig("DERIVOOPERADORPRECARGADA"));
		stepDerivoLlamada
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR");
		Steps.put(stepDerivoLlamada.GetId(), stepDerivoLlamada);

		/* Contador */

		contadorIntentosTarjeta = (StepCounter) StepFactory.createStep(
				StepFactory.StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjeta
				.setStepDescription("COUNTER => INTENTOS JPOS TARJETA");
		Steps.put(contadorIntentosTarjeta.GetId(), contadorIntentosTarjeta);

		contadorIntentosDNI = (StepCounter) StepFactory.createStep(
				StepFactory.StepType.Counter, UUID.randomUUID());
		contadorIntentosDNI.setStepDescription("COUNTER => INTENTOS JPOS DNI");
		Steps.put(contadorIntentosDNI.GetId(), contadorIntentosDNI);

		contadorIntentosFecha = (StepCounter) StepFactory.createStep(
				StepFactory.StepType.Counter, UUID.randomUUID());
		contadorIntentosFecha
				.setStepDescription("COUNTER => INTENTOS JPOS DNI");
		Steps.put(contadorIntentosFecha.GetId(), contadorIntentosFecha);

	}

	public PideDni getPideDni() {
		return pideDniGrp;
	}

	public PideTarjeta getPideTarjeta() {
		return pideTarjetaGrpJPOS;
	}

	public PideFecha getPideFecha() {
		return pideFechaGrp;
	}

}
