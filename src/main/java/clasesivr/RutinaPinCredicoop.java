package clasesivr;

import ivr.CallContext;
import ivr.CallFlow;
import ivr.IvrExceptionHandler;
import java.util.UUID;
import main.Daemon;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import step.StepAnswer;
import step.StepClearPil;
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepGenerateKeyFromDni;
import step.StepGetAsteriskVariable;
import step.StepMenu;
import step.StepSayDigits;
import step.StepFactory.StepType;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSendJPOS;
import step.StepSetVariable;
import step.StepSwitch;
import step.group.PideDniCredicoop;
import step.group.PideFechaCredicoop;
import step.group.PideTarjetaCredicoop;
import step.group.StepGroupFactory;
import workflow.Handler;
import workflow.Task;
import condition.condition;
import context.ContextVar;

public class RutinaPinCredicoop extends BaseAgiScript {

	private long idContextVar = 1;
	CallContext ctx;
	CallFlow cf;
	private ContextVar resultadoAudioInicioContextVar;
	private ContextVar dniContextVar;
	private ContextVar confirmaDniContextVar;
	private ContextVar intentosDniContextVar;
	private ContextVar fechaContextVar;
	private ContextVar fdnContextVar;
	private ContextVar diaContextVar;
	private ContextVar mesContextVar;
	private ContextVar anioContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar datosCuentaContextVar;
	private ContextVar intentosFechaContextVar;
	private ContextVar tarjetaContexVar;
	private ContextVar confirmaTarjetaContextVar;
	private ContextVar intentosTarjetaContextVar;
	private ContextVar tipoMensajeJposPin;
	private ContextVar repetirPINContextVar;
	private ContextVar ejecutoJPOSContextVar;
	private ContextVar cambiaEjecutoJPOSContextVar;
	private ContextVar intentosIngresoContextVar;
	private StepEnd pasoFinal;
	private StepAnswer inicial;
	private StepPlayRead stepAudioInicio;
	private StepPlay stepAudioFinal;
	private PideDniCredicoop pideDniGrp;
	private PideFechaCredicoop pideFechaGrp;
	private StepMenu stepMenuConfirmacionIngresoRutina;
	private StepSwitch evalRetJPOS;
	private StepCounter contadorIntentosIngresoRutina;
	private StepConditional evalContadorIngresoRutina;
	private StepCounter contadorIntentosDatosCuenta;
	private StepConditional evalContadorDatosCuenta;
	private StepSendJPOS enviaTramaJpos;
	private int intentos = 3;
	private ContextVar retornoJPOS;
	private PideTarjetaCredicoop pideTarjetaGrp;
	private StepSayDigits stepAudioDecirPIN;
	private StepPlay stepAudioSuPIN;
	private StepPlay stepAudioFechaIncorrecta;
	private StepPlay stepAudioDerivoAsesor;
	private StepPlay stepAudioTarjetaNoVigente;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioNoEsPosibleGestPIN;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioDniIncorrecto;
	private StepPlay stepAudioVerificarDatos;
	private StepMenu stepMenuRepetirPIN;
	private StepPlayRead stepAudioRepetirPIN;
	private StepPlayRead stepPideDatosCuenta;
	private StepGetAsteriskVariable obtieneTarjeta;
	private StepGetAsteriskVariable obtieneDni;
	private StepGenerateKeyFromDni stepClavePin;
	private ContextVar clavePINContextVar;
	private ContextVar clavePINRandomContextVar;
	private ContextVar intentosRepetirPinContextVar;
	private StepCounter contadorIntentosRepetirPIN;
	private StepConditional evalIntentosRepetirPIN;
	private StepExecute stepVolverAlMenu;
	private StepExecute stepDerivoLlamada;
	private StepConditional evalSiEjecutoJPOS;
	private StepSetVariable cambiaEjecutoJPOS;
	private StepCounter contadorIntentosFechaJPOS;
	private StepConditional evalContadorFechaJPOS;
	private StepConditional evalContadorDNIJPOS;
	private StepCounter contadorIntentosDNIJPOS;
	private StepConditional evalContadorTarjetaJPOS;
	private StepCounter contadorIntentosTarjetaJPOS;
	private ContextVar nroCuentaContexVar;
	private ContextVar componenteContexVar;
	private ContextVar fillerContexVar;
	private ContextVar idLlamadaContexVar;
	private ContextVar whisperContextVar;
	private ContextVar envioServerJposPrecargadasContexVar;
	private ContextVar fillerClavePINContextVar;
	private StepPlay stepAudioGestionDePin;
	private StepPlayRead stepAudioConfirmarPIN;
	private ContextVar claveIngresadaContexVar;
	private StepConditional evalClaveIngresada;
	private StepPlay stepAudioPinEntregado;
	private StepPlay stepAudioCantidadMaxDeReintentos;
	private StepConditional evalClaveNull;
	private StepPlay stepAudioReingresoPIN;
	private StepExecute stepDerivoLMenuInicial;
	private StepExecute stepDerivoLMenuAnterior;
	private StepMenu stepMenuIngresoDatosCuenta;
	private StepPlay stepAudioIngreseUnDigitoValido;
	private ContextVar intentosDatosCuentaContextVar;
	private StepPlay stepAudioPinErroneo;
	private StepClearPil stepClearPil;

	private void initialize(AgiRequest request, AgiChannel channel) {
		cf = new CallFlow();
		ctx = new CallContext();
		Handler manejoErrores = new IvrExceptionHandler();
		manejoErrores.setId(UUID.randomUUID());
		cf.addTask(manejoErrores);
		ctx.setChannel(channel);
		ctx.setRequest(request);
	}

	/* -------------------------- Defino Secuencia -------------------------- */

	private void setSequence() {

		/* --- Atiendo y obtengo Datos --- */

		inicial.setNextstep(obtieneTarjeta.GetId());

		obtieneTarjeta.setNextstep(obtieneDni.GetId());
		obtieneDni.setNextstep(stepAudioInicio.GetId());

		/* --- Secuencia de Ingreso a la Rutina--- */

		stepAudioInicio.setNextstep(stepMenuConfirmacionIngresoRutina.GetId());

		stepMenuConfirmacionIngresoRutina.addSteps("1",
				evalSiEjecutoJPOS.GetId());
		stepMenuConfirmacionIngresoRutina.addSteps("2",
				stepVolverAlMenu.GetId());
		stepMenuConfirmacionIngresoRutina.setInvalidOption(stepVolverAlMenu
				.GetId());

		evalSiEjecutoJPOS.addCondition(new condition(1, "#{"
				+ ejecutoJPOSContextVar.getVarName() + "} == 1", stepClavePin
				.GetId(), pideFechaGrp.getInitialStep()));

		/* --- Fecha --- */

		pideFechaGrp.setStepIfTrue(stepPideDatosCuenta.GetId());
		pideFechaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Cuenta --- */

		stepPideDatosCuenta.setNextstep(stepMenuIngresoDatosCuenta.GetId());

		stepMenuIngresoDatosCuenta.addSteps("1", stepClavePin.GetId());
		stepMenuIngresoDatosCuenta.addSteps("2", stepClavePin.GetId());
		stepMenuIngresoDatosCuenta
				.setInvalidOption(stepAudioIngreseUnDigitoValido.GetId());

		stepAudioIngreseUnDigitoValido.setNextstep(contadorIntentosDatosCuenta
				.GetId());

		contadorIntentosDatosCuenta
				.setNextstep(evalContadorDatosCuenta.GetId());
		evalContadorDatosCuenta.addCondition(new condition(1, "#{"
				+ intentosDatosCuentaContextVar.getVarName() + "} < "
				+ intentos, stepPideDatosCuenta.GetId(),
				stepAudioCantidadMaxDeReintentos.GetId()));

		// contadorIntentosIngresoRutina.setNextstep(evalContadorIngresoRutina
		// .GetId());
		//
		// evalContadorIngresoRutina.addCondition(new condition(1, "#{"
		// + intentosIngresoContextVar.getVarName() + "} == " + 1,
		// stepAudioInicio.GetId(), stepAudioCantidadMaxDeReintentos.GetId()));

		/* --- Generacion y envio --- */

		stepClavePin.setNextstep(enviaTramaJpos.GetId());

		enviaTramaJpos.setNextstep(cambiaEjecutoJPOS.GetId());

		cambiaEjecutoJPOS.setNextstep(evalRetJPOS.GetId());

		evalRetJPOS.setNextstep(stepAudioServNoDisponible.GetId());

		/* --- Grupos --- */

		pideDniGrp.setStepIfTrue(evalSiEjecutoJPOS.GetId());
		pideDniGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		pideTarjetaGrp.setStepIfTrue(stepClavePin.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Secuencia de Informacion PIN --- */

		stepAudioDecirPIN.setNextstep(stepAudioConfirmarPIN.GetId());
		stepAudioConfirmarPIN.setNextstep(evalClaveNull.GetId());

		evalClaveNull
				.addCondition((new condition(1, "equals('#{"
						+ claveIngresadaContexVar.getVarName() + "}','')",
						contadorIntentosRepetirPIN.GetId(), evalClaveIngresada
								.GetId())));

		contadorIntentosRepetirPIN.setNextstep(evalIntentosRepetirPIN.GetId());

		evalIntentosRepetirPIN.addCondition((new condition(1,
				"#{" + intentosRepetirPinContextVar.getVarName() + "} < "
						+ intentos, stepAudioDecirPIN.GetId(),
				stepAudioCantidadMaxDeReintentos.GetId())));

		/* --- Valida PIN --- */

		evalClaveIngresada.addCondition((new condition(1, "#{"
				+ clavePINContextVar.getVarName() + "} == #{"
				+ claveIngresadaContexVar.getVarName() + "}",
				stepAudioPinEntregado.GetId(), stepAudioPinErroneo.GetId())));

		stepAudioPinEntregado.setNextstep(stepAudioFinal.GetId());
		stepAudioPinErroneo.setNextstep(contadorIntentosRepetirPIN.GetId());

		stepAudioRepetirPIN.setNextstep(stepMenuRepetirPIN.GetId());

		stepMenuRepetirPIN.addSteps("1", stepAudioSuPIN.GetId());
		stepMenuRepetirPIN.addSteps("9", stepAudioFinal.GetId());
		stepMenuRepetirPIN.setInvalidOption(contadorIntentosRepetirPIN.GetId());

		/* --- Cont y Eval . Cod = 02, 03 , 96 y 99 --- */

		contadorIntentosFechaJPOS.setNextstep(evalContadorFechaJPOS.GetId());
		evalContadorFechaJPOS.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				pideFechaGrp.getInitialStep(), stepAudioCantidadMaxDeReintentos
						.GetId()));

		contadorIntentosDNIJPOS.setNextstep(evalContadorDNIJPOS.GetId());
		evalContadorDNIJPOS.addCondition(new condition(1, "#{"
				+ intentosDniContextVar.getVarName() + "} < " + intentos,
				pideDniGrp.getInitialStep(), stepAudioCantidadMaxDeReintentos
						.GetId()));

		contadorIntentosTarjetaJPOS
				.setNextstep(evalContadorTarjetaJPOS.GetId());
		evalContadorTarjetaJPOS.addCondition(new condition(1, "#{"
				+ intentosTarjetaContextVar.getVarName() + "} < " + intentos,
				pideTarjetaGrp.getInitialStep(),
				stepAudioCantidadMaxDeReintentos.GetId()));

		/* --- Fin de llamada --- */

		stepAudioCantidadMaxDeReintentos.setNextstep(stepDerivoLMenuAnterior
				.GetId());
		stepAudioNoEsPosibleGestPIN
				.setNextstep(stepDerivoLMenuAnterior.GetId());
		stepAudioFinal.setNextstep(pasoFinal.GetId());

		/* --- Retornos JPOS  --- */
		
		stepAudioGestionDePin.setNextstep(stepAudioSuPIN.GetId());
		stepAudioSuPIN.setNextstep(stepAudioDecirPIN.GetId());

		stepAudioDniIncorrecto.setNextstep(stepDerivoLMenuInicial.GetId());
		stepAudioFechaIncorrecta.setNextstep(contadorIntentosFechaJPOS.GetId());
		
		stepAudioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
		stepAudioNroTarjIncorrecto.setNextstep(contadorIntentosTarjetaJPOS
				.GetId());
		
		stepAudioTarjetaNoVigente.setNextstep(contadorIntentosTarjetaJPOS
				.GetId());
		
		stepAudioServNoDisponible.setNextstep(stepDerivoLMenuInicial.GetId());


	}

	@Override
	public void service(AgiRequest request, AgiChannel channel) {
		Daemon.getDbLog()
				.addCallFlowToLog(channel.getUniqueId(),
						RutinaPinCredicoop.class.getName(),
						request.getCallerIdNumber());
		this.initialize(request, channel);
		this.createContextVars(channel);
		this.createSteps();
		this.setSequence();

		for (Task tmpTask : pideDniGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}

		for (Task tmpTask : pideFechaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : pideTarjetaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}

		ctx.setInitialStep(inicial.GetId());

		try {
			cf.execute(ctx);
		} catch (Exception ex) {
			Logger.getLogger(TestIvr.class.getName())
					.log(Level.FATAL, null, ex);
		}
	}

	/* -------------------------- Creo Steps -------------------------- */

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		 * ret =>  00    ||   "Informa PIN"    
		 * ret =>  02    ||   "Nro de Documento erróneo"    
		 * ret =>  03    ||   "Fecha de nacimiento errónea"   
		 * ret =>  83    ||   "No es posible emitir PIN"   
		 * ret =>  84    ||   "No es posible emitir PIN"   
		 * ret =>  85    ||   "Falta PIN"   
		 * ret =>  96    ||   "Tarjeta inexistente"   
		 * ret =>  98    ||   "Error mensaje"   
		 * ret =>  99    ||   "Causa dif a 00 o Tj Vencida"   
		 * 
		 * ret =>  89    ||   "Error General"   
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("00", stepAudioGestionDePin.GetId());
		evalRetJPOS.addSwitchValue("02", stepAudioDniIncorrecto.GetId());
		evalRetJPOS.addSwitchValue("03", stepAudioFechaIncorrecta.GetId());

		evalRetJPOS.addSwitchValue("96", stepAudioNroTarjIncorrecto.GetId());
		evalRetJPOS.addSwitchValue("99", stepAudioTarjetaNoVigente.GetId());

		evalRetJPOS.addSwitchValue("98", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("89", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("88", stepAudioServNoDisponible.GetId());

		evalRetJPOS.addSwitchValue("81", stepAudioNoEsPosibleGestPIN.GetId());
		evalRetJPOS.addSwitchValue("83", stepAudioNoEsPosibleGestPIN.GetId());
		evalRetJPOS.addSwitchValue("84", stepAudioNoEsPosibleGestPIN.GetId());
		evalRetJPOS.addSwitchValue("85", stepAudioNoEsPosibleGestPIN.GetId());
		evalRetJPOS.addSwitchValue("87", stepAudioNoEsPosibleGestPIN.GetId());

	}

	private void createSteps() {

		/* --- Inicio|Fin --- */

		inicial = (StepAnswer) StepFactory.createStep(StepType.Answer,
				UUID.randomUUID());
		cf.addTask(inicial);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN DE COMUNICACION");
		cf.addTask(pasoFinal);

		/* --- Menu Ingreso --- */

		stepMenuIngresoDatosCuenta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuIngresoDatosCuenta.setStepDescription("MENU => DATOS CUENTA");
		stepMenuIngresoDatosCuenta
				.setContextVariableName(datosCuentaContextVar);
		cf.addTask(stepMenuIngresoDatosCuenta);

		stepMenuConfirmacionIngresoRutina = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionIngresoRutina
				.setStepDescription("MENU => INGRESO RUTINA");
		stepMenuConfirmacionIngresoRutina
				.setContextVariableName(resultadoAudioInicioContextVar);
		cf.addTask(stepMenuConfirmacionIngresoRutina);

		stepMenuRepetirPIN = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuRepetirPIN.setContextVariableName(repetirPINContextVar);
		stepMenuRepetirPIN.setStepDescription("MENU => REPETIR PIN");
		cf.addTask(stepMenuRepetirPIN);

		/* --- Play Read --- */

		stepAudioInicio = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioInicio.setStepDescription("PLAYREAD => INICIO RUTINA");
		stepAudioInicio.setPlayFile("RUTINAPINCOP/RUTINA_PIN001");
		stepAudioInicio.setPlayMaxDigits(1);
		stepAudioInicio.setContextVariableName(resultadoAudioInicioContextVar);
		stepAudioInicio.setPlayTimeout(5000L);
		cf.addTask(stepAudioInicio);

		stepAudioRepetirPIN = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirPIN.setPlayMaxDigits(1);
		stepAudioRepetirPIN.setContextVariableName(repetirPINContextVar);
		stepAudioRepetirPIN.setStepDescription("PLAYREAD => REINGRESO PIN");
		cf.addTask(stepAudioRepetirPIN);

		stepAudioConfirmarPIN = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioConfirmarPIN.setPlayMaxDigits(4);
		stepAudioConfirmarPIN.setPlayFile("RUTINAPINCOP/RUTINA_PIN020");
		stepAudioConfirmarPIN.setContextVariableName(claveIngresadaContexVar);
		stepAudioConfirmarPIN.setStepDescription("PLAYREAD => CONFIRMA PIN");
		cf.addTask(stepAudioConfirmarPIN);

		stepPideDatosCuenta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepPideDatosCuenta.setPlayFile("RUTINAPINCOP/RUTINA_PIN017");
		stepPideDatosCuenta.setPlayMaxDigits(1);
		stepPideDatosCuenta.setStepDescription("PLAYREAD => DATOS CUENTA");
		stepPideDatosCuenta.setContextVariableName(datosCuentaContextVar);
		cf.addTask(stepPideDatosCuenta);

		/*--- Generar Clave --- */

		stepClavePin = (StepGenerateKeyFromDni) StepFactory.createStep(
				StepType.GenerateKeyFromDni, UUID.randomUUID());
		stepClavePin.setContextVariableClaveDni(clavePINContextVar);
		stepClavePin.setContextVariableClaveRandom(clavePINRandomContextVar);
		stepClavePin.setContextVariableDni(dniContextVar);
		stepClavePin.setStepDescription("GENERATEKEY => GENERA PIN CREDITO");
		cf.addTask(stepClavePin);

		/* --- Dice PIN --- */

		stepAudioDecirPIN = (StepSayDigits) StepFactory.createStep(
				StepType.SayDigits, UUID.randomUUID());
		stepAudioDecirPIN.setContextVariableName(clavePINContextVar);
		stepAudioDecirPIN.setStepDescription("SAYDIGITS => INFORMA PIN");
		cf.addTask(stepAudioDecirPIN);

		/* --- Derivo LLamada --- */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADOR"));
		stepDerivoLlamada.setStepDescription("EXECUTE => DERIVO ASESOR");
		cf.addTask(stepDerivoLlamada);

		stepDerivoLMenuInicial = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLMenuInicial.setApp("goto");
		stepDerivoLMenuInicial.setAppOptions(Daemon
				.getConfig("DERIVOPPALCREDICOOP"));
		stepDerivoLMenuInicial
				.setStepDescription("EXECUTE => DERIVO MENU INICIAL");
		cf.addTask(stepDerivoLMenuInicial);

		stepDerivoLMenuAnterior = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLMenuAnterior.setApp("goto");
		stepDerivoLMenuAnterior.setAppOptions(Daemon
				.getConfig("DERIVOMENUTARJETA"));
		stepDerivoLMenuAnterior
				.setStepDescription("EXECUTE =>DERIVO MENU ANTERIOR");
		cf.addTask(stepDerivoLMenuAnterior);

		stepVolverAlMenu = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepVolverAlMenu.setApp("goto");
		stepVolverAlMenu.setAppOptions(Daemon.getConfig("DERIVOMENUPRINCIPAL"));
		stepVolverAlMenu.setStepDescription("EXECUTE => DERIVO MENU PRINCIPAL");
		cf.addTask(stepVolverAlMenu);

		/* --- JPOS --- */

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos
				.setContextVariableTipoMensaje(envioServerJposPrecargadasContexVar);
		enviaTramaJpos.setContextVariableName(retornoJPOS);
		enviaTramaJpos.addformatoVariables(0, tipoMensajeJposPin);
		enviaTramaJpos.addformatoVariables(1, tarjetaContexVar);
		enviaTramaJpos.addformatoVariables(2, dniContextVar);
		enviaTramaJpos.addformatoVariables(3, anioContextVar);
		enviaTramaJpos.addformatoVariables(4, mesContextVar);
		enviaTramaJpos.addformatoVariables(5, diaContextVar);
		enviaTramaJpos.addformatoVariables(6, nroCuentaContexVar);
		enviaTramaJpos.addformatoVariables(7, clavePINRandomContextVar);
		enviaTramaJpos.addformatoVariables(8, fillerClavePINContextVar);
		enviaTramaJpos.addformatoVariables(9, componenteContexVar);
		enviaTramaJpos.addformatoVariables(10, fillerContexVar);
		enviaTramaJpos.addformatoVariables(11, idLlamadaContexVar);
		enviaTramaJpos.addformatoVariables(12, whisperContextVar);
		enviaTramaJpos.setStepDescription("SENDJPOS => ENVIA TRAMA A JPOS");
		cf.addTask(enviaTramaJpos);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(retornoJPOS);
		evalRetJPOS.setStepDescription("SWITCH => EVAL RETORNO JPOS");
		cf.addTask(evalRetJPOS);

		cambiaEjecutoJPOS = (StepSetVariable) StepFactory.createStep(
				StepType.SetVariable, UUID.randomUUID());
		cambiaEjecutoJPOS.setContextVariableOrigen(cambiaEjecutoJPOSContextVar);
		cambiaEjecutoJPOS.setContextVariableDestino(ejecutoJPOSContextVar);
		cambiaEjecutoJPOS
				.setStepDescription("SETVARIABLE => SETEA EN 1 SI EJECUTO JPOS");
		cf.addTask(cambiaEjecutoJPOS);

		/* --- Audios Varios --- */

		stepAudioPinEntregado = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPinEntregado.setPlayfile("RUTINAPINCOP/RUTINA_PIN022");
		stepAudioPinEntregado.setStepDescription("PLAY => PIN ENTREGADO");
		cf.addTask(stepAudioPinEntregado);

		stepAudioVerificarDatos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatos.setPlayfile("RUTINAPINCOP/RUTINA_PIN012");
		stepAudioVerificarDatos
				.setStepDescription("PLAY => VERIFIQUE DATOS Y VUELVA A LLAMAR");
		cf.addTask(stepAudioVerificarDatos);

		/* --- Audios Retorno Jpos --- */

		stepAudioSuPIN = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioSuPIN.setPlayfile("RUTINAPINCOP/RUTINA_PIN019");
		stepAudioSuPIN.setStepDescription("PLAY => DICE PIN. Cod: 00 ");
		cf.addTask(stepAudioSuPIN);

		stepAudioPinErroneo = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioPinErroneo.setPlayfile("RUTINAPINCOP/RUTINA_PIN038");
		stepAudioPinErroneo.setStepDescription("PLAY => PIN ERRONEO");
		cf.addTask(stepAudioPinErroneo);

		stepAudioGestionDePin = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioGestionDePin.setPlayfile("RUTINAPINCOP/RUTINA_PIN018");
		stepAudioGestionDePin.setStepDescription("PLAY => GESTION DE PIN");
		cf.addTask(stepAudioGestionDePin);

		stepAudioReingresoPIN = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioReingresoPIN.setPlayfile("RUTINAPINCOP/RUTINA_PIN036");
		stepAudioReingresoPIN.setStepDescription("PLAY => REINGRESO PIN");
		cf.addTask(stepAudioReingresoPIN);

		stepAudioDniIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDniIncorrecto.setPlayfile("RUTINAPINCOP/RUTINA_PIN040");
		stepAudioDniIncorrecto
				.setStepDescription("PLAY => DNI INCORRECTO. COD : 02");
		cf.addTask(stepAudioDniIncorrecto);

		stepAudioFechaIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaIncorrecta.setPlayfile("RUTINAPINCOP/RUTINA_PIN039");
		stepAudioFechaIncorrecta
				.setStepDescription("PLAY => FECHA INCORRECTA. COD : 03");
		cf.addTask(stepAudioFechaIncorrecta);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");
		cf.addTask(stepAudioFinal);

		stepAudioIngreseUnDigitoValido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioIngreseUnDigitoValido
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN037");
		stepAudioIngreseUnDigitoValido
				.setStepDescription("PLAY => DIGITO INVALIDO");
		cf.addTask(stepAudioIngreseUnDigitoValido);

		stepAudioCantidadMaxDeReintentos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentos
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentos
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS");
		cf.addTask(stepAudioCantidadMaxDeReintentos);

		stepAudioNoEsPosibleGestPIN = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNoEsPosibleGestPIN.setPlayfile("RUTINAPINCOP/RUTINA_PIN028");
		stepAudioNoEsPosibleGestPIN
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE. COD : 83 / 84");
		cf.addTask(stepAudioNoEsPosibleGestPIN);

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("RUTINAPINCOP/RUTINA_PIN034");
		stepAudioDerivoAsesor
				.setStepDescription("PLAY => DERIVO ASESOR. COD : 85");
		cf.addTask(stepAudioDerivoAsesor);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("RUTINAPINCOP/RUTINA_PIN023");
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => TARJETA INCORRECTA. COD : 96");
		cf.addTask(stepAudioNroTarjIncorrecto);

		stepAudioTarjetaNoVigente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaNoVigente.setPlayfile("RUTINAPINCOP/RUTINA_PIN025");
		stepAudioTarjetaNoVigente
				.setStepDescription("PLAY => TARJETA VENCIDA. COD : 99");
		cf.addTask(stepAudioTarjetaNoVigente);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPINCOP/RUTINA_PIN026");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE. COD : 98");
		cf.addTask(stepAudioServNoDisponible);

		/* --- Contadores --- */

		contadorIntentosDatosCuenta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosDatosCuenta
				.setStepDescription("COUNTER => DATOS CUENTA");
		contadorIntentosDatosCuenta
				.setContextVariableName(intentosDatosCuentaContextVar);
		cf.addTask(contadorIntentosDatosCuenta);

		contadorIntentosIngresoRutina = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosIngresoRutina
				.setStepDescription("COUNTER => INGRESO RUTINA");
		contadorIntentosIngresoRutina
				.setContextVariableName(intentosIngresoContextVar);
		cf.addTask(contadorIntentosIngresoRutina);

		contadorIntentosDNIJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosDNIJPOS.setContextVariableName(intentosDniContextVar);
		contadorIntentosDNIJPOS
				.setStepDescription("COUNTER => INTENTOS DNI CONTRA JPOS. COD : 02");
		cf.addTask(contadorIntentosDNIJPOS);

		contadorIntentosFechaJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosFechaJPOS
				.setContextVariableName(intentosFechaContextVar);
		contadorIntentosFechaJPOS
				.setStepDescription("COUNTER => INTENTOS FECHA CONTRA JPOS. COD : 03");
		cf.addTask(contadorIntentosFechaJPOS);

		contadorIntentosTarjetaJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjetaJPOS
				.setContextVariableName(intentosTarjetaContextVar);
		contadorIntentosTarjetaJPOS
				.setStepDescription("COUNTER => INTENTOS TARJETA CONTRA JPOS. COD : 96");
		cf.addTask(contadorIntentosTarjetaJPOS);

		contadorIntentosRepetirPIN = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirPIN
				.setStepDescription("COUNTER => INTENTOS REPETIR PIN");
		contadorIntentosRepetirPIN
				.setContextVariableName(intentosRepetirPinContextVar);
		cf.addTask(contadorIntentosRepetirPIN);

		/* --- Evaluadores --- */

		evalContadorIngresoRutina = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalContadorIngresoRutina
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO RUTINA");
		cf.addTask(evalContadorIngresoRutina);

		evalClaveNull = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalClaveNull
				.setStepDescription("CONDITIONAL => PIN IGRESADO DISTINTO A NULO");
		cf.addTask(evalClaveNull);

		evalClaveIngresada = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalClaveIngresada
				.setStepDescription("CONDITIONAL => PIN INGRESADO IGUAL AL DADO POR LA RUTINA");
		cf.addTask(evalClaveIngresada);

		evalSiEjecutoJPOS = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalSiEjecutoJPOS
				.setStepDescription("CONDITIONAL => EVALUA SI EJECUTO JPOS");
		cf.addTask(evalSiEjecutoJPOS);

		evalIntentosRepetirPIN = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalIntentosRepetirPIN
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR PIN");
		cf.addTask(evalIntentosRepetirPIN);

		evalContadorDNIJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDNIJPOS
				.setStepDescription("CONDITIONAL => INTENTOS DNI CONTRA JPOS. COD : 02");
		cf.addTask(evalContadorDNIJPOS);

		evalContadorDatosCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorDatosCuenta
				.setStepDescription("CONDITIONAL => INTENTOS DATOS CUENTA");
		cf.addTask(evalContadorDatosCuenta);

		evalContadorFechaJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorFechaJPOS
				.setStepDescription("CONDITIONAL => INTENTOS FECHA CONTRA JPOS. COD : 03");
		cf.addTask(evalContadorFechaJPOS);

		evalContadorTarjetaJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorTarjetaJPOS
				.setStepDescription("CONDITIONAL =>  INTENTOS TARJETA CONTRA JPOS. COD : 96");
		cf.addTask(evalContadorTarjetaJPOS);

		/* --- GRUPOS --- */

		pideDniGrp = (PideDniCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDniCredicoop);
		pideDniGrp.setAudioDni("RUTINAPINCOP/RUTINA_PIN002");
		pideDniGrp.setAudioValidateDni("RUTINAPINCOP/RUTINA_PIN016");
		pideDniGrp.setAudioDniInvalido("RUTINAPINCOP/RUTINA_PIN030");
		pideDniGrp.setAudioSuDniEs("RUTINAPINCOP/RUTINA_PIN031");
		pideDniGrp.setDniContextVar(dniContextVar);
		pideDniGrp.setConfirmaDniContextVar(confirmaDniContextVar);
		pideDniGrp.setIntentosDniContextVar(intentosDniContextVar);

		pideFechaGrp = (PideFechaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFechaCredicoop);
		pideFechaGrp.setAudioFecha("RUTINAPINCOP/RUTINA_PIN010");
		pideFechaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideFechaGrp.setAudioValidateFecha("RUTINAPINCOP/RUTINA_PIN009");
		pideFechaGrp.setAudioSuFechaEs("RUTINAPINCOP/RUTINA_PIN015");
		pideFechaGrp.setAudioAnio("RUTINAPINCOP/RUTINA_PIN008");
		pideFechaGrp.setAudioMes("RUTINAPINCOP/RUTINA_PIN007");
		pideFechaGrp.setAudioDia("RUTINAPINCOP/RUTINA_PIN006");
		pideFechaGrp.setAudioFechaInvalida("RUTINAPINCOP/RUTINA_PIN013");
		pideFechaGrp.setfechaContextVar(fechaContextVar);
		pideFechaGrp.setContextVarDia(diaContextVar);
		pideFechaGrp.setContextVarMes(mesContextVar);
		pideFechaGrp.setContextVarAnio(anioContextVar);
		pideFechaGrp.setConfirmaFechaContextVar(confirmaFechaContextVar);
		pideFechaGrp.setIntentosFechaContextVar(intentosFechaContextVar);
		pideFechaGrp.setFdnContextVar(fdnContextVar);
		pideFechaGrp.setAudioLaFechaNoCoincide("RUTINAPINCOP/RUTINA_PIN041");

		pideTarjetaGrp = (PideTarjetaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjetaCredicoop);
		pideTarjetaGrp.setAudioTarjeta("RUTINAPINCOP/RUTINA_PIN003");
		pideTarjetaGrp.setAudioTarjetaInvalido("RUTINAPINCOP/RUTINA_PIN023");
		pideTarjetaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideTarjetaGrp.setConfirmaTarjetaContextVar(confirmaTarjetaContextVar);
		pideTarjetaGrp.setIntentosTarjetaContextVar(intentosTarjetaContextVar);
		pideTarjetaGrp.setTarjetaContextVar(tarjetaContexVar);
		pideTarjetaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());
		pideTarjetaGrp.setStepIfTrue(enviaTramaJpos.GetId());

		/* --- Obtiene datos --- */

		obtieneDni = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneDni.setContextVariableName(dniContextVar);
		obtieneDni.setVariableName("dni");
		obtieneDni.setStepDescription("GETASTERISKVARIABLE => OBTIENE DNI");
		cf.addTask(obtieneDni);

		obtieneTarjeta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneTarjeta.setContextVariableName(tarjetaContexVar);
		obtieneTarjeta.setVariableName("plastico");
		obtieneTarjeta
				.setStepDescription("GETASTERISKVARIABLE => OBTIENE TARJETA");
		cf.addTask(obtieneTarjeta);

		this.evalRetJPOS();
	}

	/* ---------------- Inicializo las Variables De Contexto -------------- */

	@SuppressWarnings("unchecked")
	private ContextVar getContextVar(String descrip, String initialValue,
			String astUid) {
		ContextVar tmpCtxVar = new ContextVar(ctx);
		tmpCtxVar.setId(this.idContextVar++);
		tmpCtxVar.setVarDescrip(descrip);
		tmpCtxVar.setAstUid(astUid);
		tmpCtxVar.setVarValue(initialValue);
		ctx.put(tmpCtxVar.getId(), tmpCtxVar);
		return tmpCtxVar;
	}

	@SuppressWarnings("unused")
	private void createContextVars(AgiChannel channel) {
		/* --- Inicio --- */

		String AstUid = channel.getUniqueId();

		resultadoAudioInicioContextVar = this.getContextVar(
				"resultadoAudioInicioContextVar", "", AstUid);

		dniContextVar = this.getContextVar("dniContextVar", "", AstUid);
		dniContextVar.setStringFormat("%08d");

		confirmaDniContextVar = this.getContextVar("confirmaDniContextVar", "",
				AstUid);

		intentosDniContextVar = this.getContextVar("intentosDniContextVar",
				"0", AstUid);

		fechaContextVar = this.getContextVar("fechaContextVar", "", AstUid);

		fdnContextVar = this.getContextVar("fdnContextVar", "", AstUid);

		diaContextVar = this.getContextVar("diaContextVar", "", AstUid);

		mesContextVar = this.getContextVar("mesContextVar", "", AstUid);

		anioContextVar = this.getContextVar("anioContextVar", "", AstUid);

		confirmaFechaContextVar = this.getContextVar("confirmaFechaContextVar",
				"", AstUid);

		intentosFechaContextVar = this.getContextVar("intentosFechaContextVar",
				"0", AstUid);

		intentosIngresoContextVar = this.getContextVar(
				"intentosIngresoContextVar", "0", AstUid);

		intentosDatosCuentaContextVar = this.getContextVar(
				"intentosDatosCuentaContextVar", "0", AstUid);

		tarjetaContexVar = this.getContextVar("tarjetaContexVar", "", AstUid);

		nroCuentaContexVar = this.getContextVar("nroCuentaContexVar",
				String.format("%07d", 0), AstUid);

		datosCuentaContextVar = this.getContextVar("datosCuentaContextVar",
				"T", AstUid);

		fillerContexVar = this.getContextVar("fillerContexVar", " ", AstUid);
		fillerContexVar.setStringFormat("%51s");

		String ast_uid = "";

		if (AstUid.contains("-")) {
			ast_uid = AstUid.split("-")[1];
		} else {
			ast_uid = AstUid;
		}

		ast_uid = String.format("%030d", 0) + ast_uid.replaceAll("\\.", "");

		idLlamadaContexVar = this.getContextVar("idLlamadaContexVar",
				ast_uid.substring(ast_uid.length() - 29), AstUid);

		confirmaTarjetaContextVar = this.getContextVar(
				"confirmaTarjetaContextVar", "", AstUid);

		whisperContextVar = this
				.getContextVar("whisperContextVar", "0", AstUid);

		intentosTarjetaContextVar = this.getContextVar(
				"intentosTarjetaContextVar", "0", AstUid);

		tipoMensajeJposPin = this.getContextVar("tipoMensajeJposPin", "4",
				AstUid);
		tipoMensajeJposPin.setStringFormat("%01d");

		retornoJPOS = this.getContextVar("retornoJPOS", "98", AstUid);

		repetirPINContextVar = this.getContextVar("repetirPINContextVar", "",
				AstUid);

		clavePINContextVar = this.getContextVar("clavePINContextVar", "",
				AstUid);
		clavePINContextVar.setStringFormat("%04d");

		claveIngresadaContexVar = this.getContextVar("claveIngresadaContexVar",
				"", AstUid);

		clavePINRandomContextVar = this.getContextVar(
				"clavePINRandomContextVar", String.format("%04d", 0), AstUid);

		fillerClavePINContextVar = this.getContextVar(
				"fillerClavePINContextVar", String.format("%04d", 0), AstUid);

		intentosRepetirPinContextVar = this.getContextVar(
				"intentosRepetirPinContextVar", "0", AstUid);

		cambiaEjecutoJPOSContextVar = this.getContextVar(
				"cambiaEjecutoJPOSContextVar", "1", AstUid);

		ejecutoJPOSContextVar = this.getContextVar("ejecutoJPOSContextVar",
				"0", AstUid);

		envioServerJposPrecargadasContexVar = this.getContextVar(
				"envioServerJposPrecargadasContexVarj", "precargada", AstUid);

		componenteContexVar = this.getContextVar("componenteContexVar", "B",
				AstUid);
	}
}
