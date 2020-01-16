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
import step.StepCheckCuenta;
import step.StepCheckCuentaEnDialPlan;
import step.StepClearPil;
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepInitDniDB;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepFactory.StepType;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSetAsteriskVariable;
import step.StepSwitch;
import step.group.PideCuenta;
import step.group.PideFechaCredicoop;
import step.group.PideTarjetaCredicoop;
import step.group.StepGroupFactory;
import workflow.Handler;
import workflow.Task;
import condition.condition;
import context.ContextVar;

public class RutinaPinDebitoCredicoop extends BaseAgiScript {

	private long idContextVar = 1;
	CallContext ctx;
	CallFlow cf;
	private ContextVar resultadoAudioInicioContextVar;
	private ContextVar dniContextVar;
	private ContextVar fechaContextVar;
	private ContextVar fdnContextVar;
	private ContextVar diaContextVar;
	private ContextVar mesContextVar;
	private ContextVar anioContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar datosCuentaContextVar;
	private ContextVar intentosFechaContextVar;
	private ContextVar tarjetaContexVar;
	private ContextVar repetirPINContextVar;
	private ContextVar intentosIngresoContextVar;
	private StepEnd pasoFinal;
	private StepAnswer inicial;
	private StepPlayRead stepAudioInicio;
	private StepPlay stepAudioFinal;
	private PideFechaCredicoop pideFechaGrp;
	private StepMenu stepMenuConfirmacionIngresoRutina;
	private StepCounter contadorIntentosIngresoRutina;
	private StepConditional evalContadorIngresoRutina;
	private StepCounter contadorIntentosCuenta;
	private StepConditional evalContadorCuenta;
	private int intentos = 3;
	private StepPlay stepAudioFechaIncorrecta;
	private StepPlay stepAudioDerivoAsesor;
	private StepPlay stepAudioTarjetaNoVigente;
	private StepPlay stepAudioNumeroDeTarjetaIncorrecto;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioDniIncorrecto;
	private StepPlay stepAudioVerificarDatos;
	private StepMenu stepMenuRepetirPIN;
	private StepPlayRead stepAudioRepetirPIN;
	private StepPlayRead stepPideDatosCuenta;
	private StepGetAsteriskVariable obtieneTarjeta;
	private StepGetAsteriskVariable obtieneDni;
	private StepExecute stepVolverAlMenu;
	private StepExecute stepDerivoLlamada;
	private ContextVar fillerContexVar;
	private StepPlayRead stepAudioConfirmarPIN;
	private ContextVar claveIngresadaContexVar;
	private StepPlay stepAudioPinEntregado;
	private StepPlay stepAudioCantidadMaxDeReintentos;
	private StepExecute stepDerivoLMenuInicial;
	private StepExecute stepDerivoAlMenuAnterior;
	private StepMenu stepMenuIngresoDatosCuenta;
	private StepPlay stepAudioIngreseUnDigitoValido;
	private ContextVar intentosCuentaPropiaContextVar;
	private StepClearPil stepClearPil;
	private StepPlay stepAudioErrorClearPil;
	private StepPlay stepAudioClearPilOk;
	private StepIsOwnCard esTarjetaPropia;
	private ContextVar idCrecerContextVar;
	private StepGetAsteriskVariable obtieneIdCrecer;
	private PideCuenta pideCuentaGrp;
	private ContextVar confirmaCuentaContextVar;
	private ContextVar cuentaContextVar;
	private ContextVar intentosCuentaContextVar;
	private StepCheckCuenta checkCuenta;
	private StepPlay stepAudioCuentaPropia;
	private ContextVar retornoClearPilContextVar;
	private StepSwitch evalRetornoClearPil;
	private StepPlay stepAudioErrorClearPilFilial;
	private StepPlay stepAudioTarjetaNoOperaConPil;
	private StepPlay stepAudioCantidadMaxDeReintentos2;
	private ContextVar confirmaTarjetaContextVar;
	private ContextVar intentosTarjetaContextVar;
	private PideTarjetaCredicoop pideTarjetaGrp;
	private ContextVar resultadoAudioCambioDeTarjetaContextVar;
	private StepMenu stepMenuCambioDeTarjeta;
	private StepPlayRead stepAudioCambioDeTarjeta;
	private StepPlay stepAudioClaerPilOkUnaHS;
	private StepInitDniDB initDB;
	private StepGetAsteriskVariable obtieneDNIS;
	private ContextVar dnisContextVar;
	private StepConditional evalCantidadDigitos;
	private StepCheckCuentaEnDialPlan stepCheckCuentaEnDialPlan;
	private StepSetAsteriskVariable stepSetReingreso;
	private ContextVar reingresoContextVar;

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

		inicial.setNextstep(obtieneDni.GetId());

		obtieneDni.setNextstep(stepCheckCuentaEnDialPlan.GetId());
		
		/* --- No tiene cuentas --- */

		stepCheckCuentaEnDialPlan.setNextStepIsFalse(stepSetReingreso.GetId());
		stepSetReingreso.setNextstep(initDB.GetId());

		
		/* --- Tiene cuentas --- */
		
		stepCheckCuentaEnDialPlan.setNextStepIsTrue(obtieneTarjeta.GetId());
				
		obtieneTarjeta.setNextstep(obtieneIdCrecer.GetId());
		obtieneIdCrecer.setNextstep(stepAudioInicio.GetId());

		/* --- Secuencia de Ingreso a la Rutina--- */

		stepAudioInicio.setNextstep(stepMenuConfirmacionIngresoRutina.GetId());

		stepMenuConfirmacionIngresoRutina
				.addSteps("1", esTarjetaPropia.GetId());
		stepMenuConfirmacionIngresoRutina.addSteps("2",
				stepVolverAlMenu.GetId());
		stepMenuConfirmacionIngresoRutina.setInvalidOption(stepVolverAlMenu
				.GetId());

		/* --- Cambio de Tarjeta --- */

		stepAudioCambioDeTarjeta.setNextstep(stepMenuCambioDeTarjeta.GetId());

		stepMenuCambioDeTarjeta.addSteps("1", esTarjetaPropia.GetId());
		stepMenuCambioDeTarjeta.addSteps("2", pideTarjetaGrp.getInitialStep());
		stepMenuCambioDeTarjeta.setInvalidOption(stepAudioCambioDeTarjeta
				.GetId());

		pideTarjetaGrp.setStepIfTrue(esTarjetaPropia.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Es tarjeta propia --- */

		esTarjetaPropia.setNextStepIsTrue(pideFechaGrp.getInitialStep());
		esTarjetaPropia.setNextStepIsFalse(stepAudioNumeroDeTarjetaIncorrecto
				.GetId());

		stepAudioNumeroDeTarjetaIncorrecto.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		/* --- Fecha --- */

		pideFechaGrp.setStepIfTrue(pideCuentaGrp.getInitialStep());
		pideFechaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos.GetId());

		/* --- Cuenta --- */

		pideCuentaGrp.setStepIfTrue(checkCuenta.GetId());
		pideCuentaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentos2.GetId());

		checkCuenta.setNextStepIsTrue(stepClearPil.GetId());
		checkCuenta.setNextStepIsFalse(contadorIntentosCuenta.GetId());

		contadorIntentosCuenta.setNextstep(evalContadorCuenta.GetId());
		evalContadorCuenta.addCondition(new condition(1, "#{"
				+ intentosCuentaPropiaContextVar.getVarName() + "} < "
				+ intentos, stepAudioCuentaPropia.GetId(),
				stepAudioCantidadMaxDeReintentos.GetId()));

		stepAudioCuentaPropia.setNextstep(pideCuentaGrp.getInitialStep());

		/* --- Blanqueo el Pil --- */

		stepClearPil.setNextStepIsTrue(evalRetornoClearPil.GetId());
		stepClearPil.setNextStepIsFalse(stepAudioErrorClearPil.GetId());

		/* --- Fin --- */

		evalRetornoClearPil.setNextstep(stepAudioErrorClearPil.GetId());

		/* --- Retornos --- */

		stepAudioErrorClearPil.setNextstep(stepDerivoAlMenuAnterior.GetId());
		stepAudioErrorClearPilFilial.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		stepAudioTarjetaNoOperaConPil.setNextstep(stepDerivoAlMenuAnterior
				.GetId());

		stepAudioClearPilOk.setNextstep(stepDerivoAlMenuAnterior.GetId());

		stepAudioClaerPilOkUnaHS.setNextstep(stepDerivoAlMenuAnterior.GetId());

		stepAudioCantidadMaxDeReintentos.setNextstep(stepDerivoAlMenuAnterior
				.GetId());
		stepAudioCantidadMaxDeReintentos2.setNextstep(stepDerivoLMenuInicial
				.GetId());

		stepAudioFinal.setNextstep(pasoFinal.GetId());

		/* --- Derivo --- */

		stepAudioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
	}

	@Override
	public void service(AgiRequest request, AgiChannel channel) {
		Daemon.getDbLog().addCallFlowToLog(channel.getUniqueId(),
				RutinaPinDebitoCredicoop.class.getName(),
				request.getCallerIdNumber());
		this.initialize(request, channel);
		this.createContextVars(channel);
		this.createSteps();
		this.setSequence();

		for (Task tmpTask : pideFechaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}

		for (Task tmpTask : pideCuentaGrp.getSteps().values()) {
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

	private void createSteps() {

		/* --- Inicio|Fin --- */

		inicial = (StepAnswer) StepFactory.createStep(StepType.Answer,
				UUID.randomUUID());
		inicial.setStepDescription("ANSWER => INICIO RUTINA PIL");
		cf.addTask(inicial);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN RUTINA PIL");
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

		stepMenuCambioDeTarjeta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuCambioDeTarjeta.setStepDescription("MENU => CAMBIO DE TARJETA");
		stepMenuCambioDeTarjeta
				.setContextVariableName(resultadoAudioCambioDeTarjetaContextVar);
		cf.addTask(stepMenuCambioDeTarjeta);

		stepMenuRepetirPIN = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuRepetirPIN.setContextVariableName(repetirPINContextVar);
		stepMenuRepetirPIN.setStepDescription("MENU => REPETIR PIN");
		cf.addTask(stepMenuRepetirPIN);

		/* --- Play Read --- */

		stepAudioInicio = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioInicio.setStepDescription("PLAYREAD => INICIO RUTINA");
		stepAudioInicio.setPlayFile("RUTINAPIL/001");
		stepAudioInicio.setPlayMaxDigits(1);
		stepAudioInicio.setContextVariableName(resultadoAudioInicioContextVar);
		stepAudioInicio.setPlayTimeout(5000L);
		cf.addTask(stepAudioInicio);

		stepAudioCambioDeTarjeta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCambioDeTarjeta
				.setStepDescription("PLAYREAD => CAMBIO DE TARJETA");
		stepAudioCambioDeTarjeta.setPlayFile("RUTINAPIL/015");
		stepAudioCambioDeTarjeta.setPlayMaxDigits(1);
		stepAudioCambioDeTarjeta
				.setContextVariableName(resultadoAudioCambioDeTarjetaContextVar);
		stepAudioCambioDeTarjeta.setPlayTimeout(5000L);
		cf.addTask(stepAudioCambioDeTarjeta);

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

		stepDerivoAlMenuAnterior = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoAlMenuAnterior.setApp("goto");
		stepDerivoAlMenuAnterior.setAppOptions(Daemon
				.getConfig("DERIVOMENUTARJETA"));
		stepDerivoAlMenuAnterior
				.setStepDescription("EXECUTE =>DERIVO MENU ANTERIOR");
		cf.addTask(stepDerivoAlMenuAnterior);

		stepVolverAlMenu = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepVolverAlMenu.setApp("goto");
		stepVolverAlMenu.setAppOptions(Daemon.getConfig("DERIVOMENUPRINCIPAL"));
		stepVolverAlMenu.setStepDescription("EXECUTE => DERIVO MENU PRINCIPAL");
		cf.addTask(stepVolverAlMenu);

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

		stepAudioClearPilOk = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioClearPilOk.setPlayfile("RUTINAPIL/007");
		stepAudioClearPilOk.setStepDescription("PLAY => CLEAR PIL OK");
		cf.addTask(stepAudioClearPilOk);

		stepAudioClaerPilOkUnaHS = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaerPilOkUnaHS.setPlayfile("RUTINAPIL/016");
		stepAudioClaerPilOkUnaHS
				.setStepDescription("PLAY => CLEAR PIL OK, ESPERAR 1 HS");
		cf.addTask(stepAudioClaerPilOkUnaHS);

		stepAudioErrorClearPil = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioErrorClearPil.setPlayfile("RUTINAPIL/011");
		stepAudioErrorClearPil.setStepDescription("PLAY => ERROR CLEAR PIL");
		cf.addTask(stepAudioErrorClearPil);

		stepAudioErrorClearPilFilial = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioErrorClearPilFilial.setPlayfile("RUTINAPIL/012");
		stepAudioErrorClearPilFilial
				.setStepDescription("PLAY => ERROR CLEAR PIL , CONSULTAR EN LA FILIAL");
		cf.addTask(stepAudioErrorClearPilFilial);

		stepAudioTarjetaNoOperaConPil = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaNoOperaConPil.setPlayfile("RUTINAPIL/010");
		stepAudioTarjetaNoOperaConPil
				.setStepDescription("PLAY => ERROR TARJETA NO OPERA CON PIL");
		cf.addTask(stepAudioTarjetaNoOperaConPil);

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

		stepAudioCantidadMaxDeReintentos2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentos2
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentos2
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS");
		cf.addTask(stepAudioCantidadMaxDeReintentos2);

		stepAudioCuentaPropia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCuentaPropia.setPlayfile("RUTINAPIL/009");
		stepAudioCuentaPropia.setStepDescription("PLAY => CUENTA PROPIA");
		cf.addTask(stepAudioCuentaPropia);

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("RUTINAPINCOP/RUTINA_PIN034");
		stepAudioDerivoAsesor
				.setStepDescription("PLAY => DERIVO ASESOR. COD : 85");
		cf.addTask(stepAudioDerivoAsesor);

		stepAudioNumeroDeTarjetaIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNumeroDeTarjetaIncorrecto.setPlayfile("RUTINAPIL/008");
		stepAudioNumeroDeTarjetaIncorrecto
				.setStepDescription("PLAY => TARJETA INCORRECTA. COD : 96");
		cf.addTask(stepAudioNumeroDeTarjetaIncorrecto);

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

		contadorIntentosCuenta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosCuenta
				.setStepDescription("COUNTER => INTENTOS CUENTA PROPIA");
		contadorIntentosCuenta
				.setContextVariableName(intentosCuentaPropiaContextVar);
		cf.addTask(contadorIntentosCuenta);

		contadorIntentosIngresoRutina = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosIngresoRutina
				.setStepDescription("COUNTER => INGRESO RUTINA");
		contadorIntentosIngresoRutina
				.setContextVariableName(intentosIngresoContextVar);
		cf.addTask(contadorIntentosIngresoRutina);

		/* --- Evaluadores --- */

		evalContadorIngresoRutina = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalContadorIngresoRutina
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO RUTINA");
		cf.addTask(evalContadorIngresoRutina);

		evalContadorCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorCuenta
				.setStepDescription("CONDITIONAL => INTENTOS CUENTA PROPIA");
		cf.addTask(evalContadorCuenta);
		
		
		obtieneTarjeta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneTarjeta.setContextVariableName(tarjetaContexVar);
		obtieneTarjeta.setVariableName("plastico");
		obtieneTarjeta
				.setStepDescription("GETASTERISKVARIABLE => OBTIENE TARJETA");
		cf.addTask(obtieneTarjeta);
		
		/* --- EJECUTO INITDB ? --- */
	
		stepCheckCuentaEnDialPlan = (StepCheckCuentaEnDialPlan) StepFactory.createStep(
				StepFactory.StepType.CuentaEnDialplan, UUID.randomUUID());
		stepCheckCuentaEnDialPlan
				.setStepDescription("CHEACKCUENTASENDIALPLAN => TIENE CUENTAS EN EL DIALPLAN");
		cf.addTask(stepCheckCuentaEnDialPlan); 

		initDB  = (StepInitDniDB) StepFactory.createStep(
				StepType.InitDniDB, UUID.randomUUID());
		initDB.setContextVarDni(dniContextVar);
		initDB
				.setStepDescription("INITDNIDB => OBTIENE DATOS A PARTIR DEL DNI, POR DERIVO ");
		cf.addTask(initDB);
		
		
		/* --- CLEAR PIL --- */

		stepClearPil = (StepClearPil) StepFactory.createStep(StepType.ClearPil,
				UUID.randomUUID());
		stepClearPil.setTarjetaContextVar(tarjetaContexVar);
		stepClearPil.setRetornoClearPilContextVar(retornoClearPilContextVar);
		stepClearPil.setStepDescription("CLEARPIL => CLEAR PIL");
		cf.addTask(stepClearPil);

		esTarjetaPropia = (StepIsOwnCard) StepFactory.createStep(
				StepType.IsOwnCard, UUID.randomUUID());
		esTarjetaPropia.setTarjetaContextVariableName(tarjetaContexVar);
		esTarjetaPropia.setIdCrecerContextVariableName(idCrecerContextVar);
		esTarjetaPropia
				.setStepDescription("ISOWNCARD => VERIFICA QUE LA TARJETA INGRESADA SEA PROPIA");
		cf.addTask(esTarjetaPropia);

		checkCuenta = (StepCheckCuenta) StepFactory.createStep(
				StepType.ChekCuenta, UUID.randomUUID());
		checkCuenta.setCuentaContextVar(cuentaContextVar);
		checkCuenta
				.setStepDescription("CHEKCUENTA => VERIFICA QUE LA CUENTA SEA PROPIA");
		cf.addTask(checkCuenta);

		/* --- GRUPOS --- */

		pideCuentaGrp = (PideCuenta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideCuenta);
		pideCuentaGrp.setAudioCuenta("RUTINAPIL/002");
		pideCuentaGrp.setAudioCuentaInvalida("RUTINAPIL/005");
		pideCuentaGrp.setAudioSuCuentaEs("RUTINAPIL/003");
		pideCuentaGrp.setAudioValidateCuenta("RUTINAPIL/004");
		pideCuentaGrp.setConfirmaCuentaContextVar(confirmaCuentaContextVar);
		pideCuentaGrp.setCuentaContextVar(cuentaContextVar);
		pideCuentaGrp.setIntentosCuentaContextVar(intentosCuentaContextVar);

		pideFechaGrp = (PideFechaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFechaCredicoop);
		pideFechaGrp.setAudioFecha("RUTINAPINCOP/RUTINA_PIN010");
		pideFechaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideFechaGrp.setAudioValidateFecha("RUTINAPINCOP/RUTINA_PIN009");
		pideFechaGrp.setAudioSuFechaEs("RUTINAPINCOP/RUTINA_PIN015");
		pideFechaGrp.setAudioLaFechaNoCoincide("RUTINAPIL/014");
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

		pideTarjetaGrp = (PideTarjetaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjetaCredicoop);
		pideTarjetaGrp.setAudioTarjeta("RUTINAPINCOP/RUTINA_PIN003");
		pideTarjetaGrp.setAudioTarjetaInvalido("RUTINAPINCOP/RUTINA_PIN023");
		pideTarjetaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideTarjetaGrp.setConfirmaTarjetaContextVar(confirmaTarjetaContextVar);
		pideTarjetaGrp.setIntentosTarjetaContextVar(intentosTarjetaContextVar);
		pideTarjetaGrp.setTarjetaContextVar(tarjetaContexVar);

		/* --- Obtiene datos --- */

		obtieneDni = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneDni.setContextVariableName(dniContextVar);
		obtieneDni.setVariableName("dni");
		obtieneDni.setStepDescription("GETASTERISKVARIABLE => OBTIENE DNI");
		cf.addTask(obtieneDni);

		

		obtieneIdCrecer = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneIdCrecer.setVariableName("idcrecer");
		obtieneIdCrecer.setContextVariableName(idCrecerContextVar);
		obtieneIdCrecer
				.setStepDescription("GETASTERISKVARIABLE => OBTIENE ID CRECER");
		cf.addTask(obtieneIdCrecer);

		evalRetornoClearPil = (StepSwitch) StepFactory.createStep(
				StepType.Switch, UUID.randomUUID());
		evalRetornoClearPil.setContextVariableName(retornoClearPilContextVar);
		evalRetornoClearPil
				.setStepDescription("SWITCH => CODIGO RETORNO CLEAR PIL");
		cf.addTask(evalRetornoClearPil);

		stepSetReingreso = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetReingreso.setVariableName("reingreso");
		stepSetReingreso.setContextVariableName(reingresoContextVar);
		stepSetReingreso
				.setStepDescription("SETASTERISKVARIABLE => SETEA VARIABLE PARA REINGRESO A LA RUTINA");
		cf.addTask(stepSetReingreso);
		
		this.evalRetornoClearPil();

	}

	private void evalRetornoClearPil() {

		evalRetornoClearPil.addSwitchValue("0", stepAudioClearPilOk.GetId());
		evalRetornoClearPil.addSwitchValue("481", stepAudioClearPilOk.GetId());

		evalRetornoClearPil.addSwitchValue("1995",
				stepAudioClaerPilOkUnaHS.GetId());

		evalRetornoClearPil.addSwitchValue("493",
				stepAudioErrorClearPilFilial.GetId());

		evalRetornoClearPil.addSwitchValue("4375",
				stepAudioTarjetaNoOperaConPil.GetId());
		evalRetornoClearPil.addSwitchValue("487",
				stepAudioTarjetaNoOperaConPil.GetId());

		evalRetornoClearPil.addSwitchValue("3549U",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("2707",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("27071",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("5057",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("5000",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil
				.addSwitchValue("040", stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("0409",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("4057",
				stepAudioDerivoAsesor.GetId());
		evalRetornoClearPil.addSwitchValue("4000",
				stepAudioDerivoAsesor.GetId());

	}

	/* ---------------- Inicializo las Variables De Contexto -------------- */

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

	private void createContextVars(AgiChannel channel) {
		/* --- Inicio --- */

		String AstUid = channel.getUniqueId();

		resultadoAudioInicioContextVar = this.getContextVar(
				"resultadoAudioInicioContextVar", "", AstUid);

		dniContextVar = this.getContextVar("dniContextVar", "", AstUid);
		dniContextVar.setStringFormat("%08d");

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

		intentosCuentaPropiaContextVar = this.getContextVar(
				"intentosCuentaPropiaContextVar", "0", AstUid);

		tarjetaContexVar = this.getContextVar("tarjetaContexVar", "", AstUid);

		fillerContexVar = this.getContextVar("fillerContexVar", " ", AstUid);
		fillerContexVar.setStringFormat("%51s");

		String ast_uid = "";

		if (AstUid.contains("-")) {
			ast_uid = AstUid.split("-")[1];
		} else {
			ast_uid = AstUid;
		}

		ast_uid = String.format("%030d", 0) + ast_uid.replaceAll("\\.", "");

		idCrecerContextVar = this.getContextVar("idCrecerContextVar", "",
				AstUid);

		confirmaCuentaContextVar = this.getContextVar(
				"confirmaCuentaContextVar", "", AstUid);

		cuentaContextVar = this.getContextVar("cuentaContextVar", "", AstUid);

		intentosCuentaContextVar = this.getContextVar(
				"intentosCuentaContextVar", "0", AstUid);

		retornoClearPilContextVar = this.getContextVar(
				"retornoClearPilContextVar", "", AstUid);

		confirmaTarjetaContextVar = this.getContextVar(
				"confirmaTarjetaContextVar", "", AstUid);

		intentosTarjetaContextVar = this.getContextVar(
				"intentosTarjetaContextVar", "", AstUid);

		resultadoAudioCambioDeTarjetaContextVar = this.getContextVar(
				"resultadoAudioCambioDeTarjetaContextVar", "", AstUid);
		

		reingresoContextVar= this.getContextVar(
				"reingresoContextVar", "1", AstUid);
	}
}
