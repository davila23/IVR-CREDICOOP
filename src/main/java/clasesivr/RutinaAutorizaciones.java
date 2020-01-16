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
import step.StepConditional;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepMenu;
import step.StepSetAsteriskVariable;
import step.StepFactory.StepType;
import step.StepParseSaldosTarjeta;
import step.StepPlay;
import step.StepPlayFromVar;
import step.StepPlayRead;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepValidateDateTarjeta;
import step.group.ActivacionTarjetaCOTO;
import step.group.ArmaSaldoTarjetaCoto;
import step.group.BajaCompaniaAseguradoraCOTO;
import step.group.PideDigitoVerificador;
import step.group.PideDni;
import step.group.PideFecha;
import step.group.PideFechaVencimientoTarjeta;
import step.group.PideNumeroDeComercio;
import step.group.PideTarjeta;
import step.group.StepGroupFactory;
import workflow.Handler;
import workflow.Task;
import condition.condition;
import context.ContextVar;

public class RutinaAutorizaciones extends BaseAgiScript {

	private long idContextVar = 1;
	CallContext ctx;
	CallFlow cf;
	private StepAnswer inicial;
	private StepEnd pasoFinal;
	private int intentos = 3;
	private PideTarjeta pideTarjetaGrp;
	private PideFecha pideFechaGrp;
	private StepSwitch evalRetJPOS;
	private StepSendJPOS enviaTramaJpos;
	private StepPlay stepAudioFechaIncorrecta;
	private StepPlay stepAudioFinal;
	private StepPlay stepAudioDerivoAsesor;
	private StepPlay stepAudioTarjetaNoVigente;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioBienvenida;
	private StepPlay stepAudioVerificarDatos;
	private StepPlay stepAudioInicioMenuMora;
	private StepCounter contadorIntentosMenuDerivo;
	private StepCounter contadorIntentosFechaJPOS;
	private StepCounter contadorIntentosTarjetaJPOS;
	private StepConditional evalContadorMenuDerivo;
	private StepConditional evalContadorFechaJPOS;
	private StepConditional evalContadorTarjetaJPOS;
	private StepExecute stepDerivoLlamada;
	private ContextVar dniContextVar;
	private ContextVar retornoJPOS;
	private ContextVar intentosFechaContextVar;
	private ContextVar tarjetaContexVar;
	private ContextVar fillerContexVar;
	private ContextVar idLlamadaContexVar;
	private ContextVar whisperContextVar;
	private ContextVar confirmaTarjetaContextVar;
	private ContextVar intentosTarjetaContextVar;
	private ContextVar intentosMenuDerivoContextVar;
	private ContextVar retornoMsgJPOS;
	private ContextVar numeroDeLineaContexVar;
	private StepPlayRead stepAudioMenuDerivo;
	private StepMenu stepMenuDerivo;
	private ContextVar contextVarMes;
	private ContextVar contextVarAnio;
	private ContextVar contextVarDia;
	private ContextVar mensajeSaldosJpos;
	private ContextVar envioServerJposConsultasContexVar;
	private StepPlay stepAudioVerificarDatosTarjeta;
	private StepPlay stepAudioTCI;
	private StepPlay stepAudioTarjetaExtranjera;
	private StepPlay stepAudioTarjetaNOCabal;
	private StepPlayFromVar stepAudioPrevioDerivoAsesor;
	private StepTimeConditionDB obtieneHorario;
	private StepSetAsteriskVariable stepSetDni;
	private StepSetAsteriskVariable stepSetTarjeta;
	private ContextVar scapeDigitContextVar;
	private StepConditional evalBinCreditoCredicoop;
	private StepConditional evalBinDebito;
	private StepConditional evalBinDebito1;
	private StepConditional evalBinTCI;
	private StepConditional evalBinExtranjera;
	private PideFechaVencimientoTarjeta pideFechaVencimiento;
	private PideNumeroDeComercio pideComercio;
	private StepConditional evalEsTarjetaCabal;
	private PideDigitoVerificador pideDigitoVerificador;
	private ContextVar fdnContexVar;
	private ContextVar fechaContextVar;
	private ContextVar confirmaFechaContextVar;
	private ContextVar envioServerJposPrecargadasContexVar;
	private ContextVar cambiaEjecutoJPOSContextVar;
	private ContextVar ejecutoJPOSContextVar;
	private ContextVar importeContextVar;
	private StepPlayRead stepAudioImporte;
	private ContextVar cuotasContextVar;
	private StepPlayRead stepAudioCantidadDeCuotas;
	private ContextVar resultadoMenuImporteContextVar;
	private StepMenu stepMenuImporte;
	private StepPlay stepAudioDiceImporte;
	private StepPlay stepAudioDiceCantidadDeCuotas;
	private StepPlayRead stepAudioMenuConfirmaImpote;
	private ContextVar menuDerivoContextVar;
	private StepPlay stepAudioNroDeAutorizacion;
	private ContextVar autorizacionContextVar;
	private ContextVar resultadoMenuAutorizacionContextVar;
	private StepPlayRead stepAudioMenuDeAutorizacion;
	private StepMenu stepMenuAutorizacion;
	private StepConditional evalBinCreditoOtroBanco;
	private StepConditional evalBinCreditoOtroBanco1;
	private StepPlay stepAudioSuperaElLimite;
	private StepPlay stepAudioOperacionDenegada;

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

		/* --- INICIO IVR COTO --- */

		inicial.setNextstep(stepAudioBienvenida.GetId());
		stepAudioFinal.setNextstep(pasoFinal.GetId());

		stepAudioBienvenida.setNextstep(pideComercio.getInitialStep());

		pideComercio.setStepIfTrue(pideTarjetaGrp.getInitialStep());
		pideComercio.setStepIfFalse(stepAudioVerificarDatos.GetId());

		pideTarjetaGrp.setStepIfTrue(evalEsTarjetaCabal.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioVerificarDatosTarjeta.GetId());
		// ¡?
		evalEsTarjetaCabal.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '603167'",
				evalBinCreditoCredicoop.GetId(), stepAudioTarjetaNOCabal
						.GetId()));

		evalBinCreditoCredicoop.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '589657'",
				pideFechaVencimiento.getInitialStep(), evalBinCreditoOtroBanco
						.GetId()));

		evalBinCreditoOtroBanco.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '60420'",
				pideFechaVencimiento.getInitialStep(), evalBinCreditoOtroBanco1
						.GetId()));

		evalBinCreditoOtroBanco1.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '60421'",
				pideFechaVencimiento.getInitialStep(), evalBinDebito.GetId()));

		evalBinDebito.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '6042015'",
				stepAudioDerivoAsesor.GetId(), evalBinDebito1.GetId()));

		evalBinDebito1.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '6042017'",
				stepAudioDerivoAsesor.GetId(), evalBinTCI.GetId()));

		evalBinTCI.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '603167'",
				stepAudioTCI.GetId(), evalBinExtranjera.GetId()));

		evalBinExtranjera.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '603167'",
				stepAudioTarjetaExtranjera.GetId(), stepAudioNroTarjIncorrecto
						.GetId()));

		/* --- GRUPOS --- */

		pideFechaVencimiento.setStepIfTrue(pideDigitoVerificador
				.getInitialStep());
		pideFechaVencimiento.setStepIfFalse(stepAudioVerificarDatos.GetId());

		pideDigitoVerificador.setStepIfTrue(stepAudioImporte.GetId());
		pideDigitoVerificador.setStepIfFalse(stepAudioVerificarDatos.GetId());

		stepAudioImporte.setNextstep(stepAudioCantidadDeCuotas.GetId());

		stepAudioCantidadDeCuotas.setNextstep(stepAudioDiceImporte.GetId());

		stepAudioDiceImporte.setNextstep(stepAudioDiceCantidadDeCuotas.GetId());

		stepAudioDiceCantidadDeCuotas.setNextstep(stepAudioMenuConfirmaImpote
				.GetId());

		stepAudioMenuConfirmaImpote.setNextstep(stepMenuImporte.GetId());

		stepMenuImporte.addSteps("1", enviaTramaJpos.GetId());
		stepMenuImporte.addSteps("2", stepAudioDiceImporte.GetId());
		stepMenuImporte.setInvalidOption(stepAudioMenuConfirmaImpote.GetId());

		/* --- DERIVOS --- */

		stepAudioMenuDerivo.setNextstep(stepMenuDerivo.GetId());

		stepMenuDerivo.addSteps("9", obtieneHorario.GetId());
		stepMenuDerivo.addSteps("0", stepAudioFinal.GetId());
		stepMenuDerivo.setInvalidOption(contadorIntentosMenuDerivo.GetId());

		contadorIntentosMenuDerivo.setNextstep(evalContadorMenuDerivo.GetId());

		evalContadorMenuDerivo.addCondition(new condition(1,
				"#{" + intentosMenuDerivoContextVar.getVarName() + "} < "
						+ intentos, obtieneHorario.GetId(), stepAudioFinal
						.GetId()));

		stepAudioPrevioDerivoAsesor.setNextstep(stepAudioFinal.GetId());

		stepAudioDerivoAsesor.setNextstep(stepSetDni.GetId());
		stepSetDni.setNextstep(stepSetTarjeta.GetId());
		stepSetTarjeta.setNextstep(stepDerivoLlamada.GetId());

		/* --- JPOS --- */

		enviaTramaJpos.setNextstep(evalRetJPOS.GetId());
		evalRetJPOS.setNextstep(stepAudioServNoDisponible.GetId());

		/* --- ITERACIONES JPOS --- */

		stepAudioFechaIncorrecta.setNextstep(contadorIntentosFechaJPOS.GetId());

		contadorIntentosFechaJPOS.setNextstep(evalContadorFechaJPOS.GetId());
		evalContadorFechaJPOS.addCondition(new condition(1, "#{"
				+ intentosFechaContextVar.getVarName() + "} < " + intentos,
				pideFechaGrp.getInitialStep(), obtieneHorario.GetId()));

		stepAudioNroTarjIncorrecto.setNextstep(contadorIntentosTarjetaJPOS
				.GetId());
		stepAudioTarjetaNoVigente.setNextstep(contadorIntentosTarjetaJPOS
				.GetId());

		contadorIntentosTarjetaJPOS
				.setNextstep(evalContadorTarjetaJPOS.GetId());
		evalContadorTarjetaJPOS.addCondition(new condition(1, "#{"
				+ intentosTarjetaContextVar.getVarName() + "} < " + intentos,
				pideTarjetaGrp.getInitialStep(), obtieneHorario.GetId()));

		/* --- AUDIOS FINALES --- */
		stepAudioTCI.setNextstep(stepAudioFinal.GetId());
		stepAudioTarjetaExtranjera.setNextstep(stepAudioFinal.GetId());
		stepAudioTarjetaNOCabal.setNextstep(stepAudioFinal.GetId());
		stepAudioServNoDisponible.setNextstep(stepAudioFinal.GetId());
		stepAudioVerificarDatos.setNextstep(stepAudioFinal.GetId());
		stepAudioVerificarDatosTarjeta.setNextstep(stepAudioFinal.GetId());

		// stepAudioMenuBloqueo.setNextstep(stepMenuBloqueo.GetId());

		/* --- OBTIENE HORARIO DE LA BASE --- */

		obtieneHorario.setNextStepIsTrue(stepAudioPrevioDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepAudioDerivoAsesor.GetId());

		stepAudioNroDeAutorizacion.setNextstep(stepAudioMenuDeAutorizacion
				.GetId());

		stepAudioMenuDeAutorizacion.setNextstep(stepMenuAutorizacion.GetId());

		stepMenuImporte.addSteps("1", stepAudioNroDeAutorizacion.GetId());
		stepMenuImporte.addSteps("2", pideTarjetaGrp.getInitialStep());
		stepMenuImporte.addSteps("3", stepAudioFinal.GetId());
		stepMenuImporte.setInvalidOption(stepAudioMenuDeAutorizacion.GetId());
	}

	@Override
	public void service(AgiRequest request, AgiChannel channel) {
		Daemon.getDbLog().addCallFlowToLog(channel.getUniqueId(),
				RutinaAutorizaciones.class.getName(),
				request.getCallerIdNumber());
		this.initialize(request, channel);
		this.createContextVars(channel);
		this.createSteps();
		this.setSequence();

		for (Task tmpTask : pideFechaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : pideTarjetaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}

		ctx.setInitialStep(inicial.GetId());
		// ctx.setInitialStep(enviaTramaJpos.GetId());
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
		inicial.setStepDescription("Inicio comunicacion IVR COTO ");
		cf.addTask(inicial);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("Fin comunicacion IVR COTO");
		cf.addTask(pasoFinal);

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORAUTO"));
		stepDerivoLlamada.setStepDescription("Derivo llamada a un Operador");
		cf.addTask(stepDerivoLlamada);

		/* --- AUDIOS Y MENUś --- */

		stepAudioBienvenida = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioBienvenida
				.setStepDescription("Audio Bienvenida Autorizaciones CABAL");
		stepAudioBienvenida.setPlayfile("coto/AUTO001");
		cf.addTask(stepAudioBienvenida);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("Audio Final IVR Autorizaciones");
		stepAudioFinal.setPlayfile("coto/A000026");
		cf.addTask(stepAudioFinal);

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("coto/A000013");
		stepAudioDerivoAsesor.setStepDescription("Audio Derivo Asesor");
		cf.addTask(stepAudioDerivoAsesor);

		stepSetDni = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetDni.setStepDescription("Setea varible Dni");
		stepSetDni.setVariableName("numdoc");
		stepSetDni.setContextVariableName(dniContextVar);
		cf.addTask(stepSetDni);

		stepSetTarjeta = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetTarjeta.setStepDescription("Setea varible Dni");
		stepSetTarjeta.setVariableName("numtarj");
		stepSetTarjeta.setContextVariableName(tarjetaContexVar);
		cf.addTask(stepSetTarjeta);

		stepAudioFechaIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaIncorrecta.setPlayfile("RUTINAPIN/RUTINA_PIN013");
		stepAudioFechaIncorrecta.setStepDescription("Audio Fecha Incorrecta");
		cf.addTask(stepAudioFechaIncorrecta);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("RUTINAPIN/RUTINA_PIN016");
		stepAudioNroTarjIncorrecto
				.setStepDescription("Audio Nro Tarjeta Incorrecta");
		cf.addTask(stepAudioNroTarjIncorrecto);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("Audio Servicio No Disponible");
		cf.addTask(stepAudioServNoDisponible);

		stepAudioOperacionDenegada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioOperacionDenegada.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioOperacionDenegada
				.setStepDescription("Audio Operacion Denegada");
		cf.addTask(stepAudioOperacionDenegada);

		stepAudioSuperaElLimite = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSuperaElLimite.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioSuperaElLimite
				.setStepDescription("Audio supera el limite gestion telefonica");
		cf.addTask(stepAudioSuperaElLimite);

		stepAudioVerificarDatos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatos.setPlayfile("coto/A000057");
		stepAudioVerificarDatos.setStepDescription("Audio Verificar Datos");
		cf.addTask(stepAudioVerificarDatos);

		stepAudioVerificarDatosTarjeta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatosTarjeta.setPlayfile("coto/A0000012");
		stepAudioVerificarDatosTarjeta
				.setStepDescription("Audio Verificar Datos");
		cf.addTask(stepAudioVerificarDatosTarjeta);

		stepAudioTarjetaNOCabal = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaNOCabal.setPlayfile("coto/A0000012");
		stepAudioTarjetaNOCabal.setStepDescription("Audio Tarjeta NO cabal");
		cf.addTask(stepAudioTarjetaNOCabal);

		stepAudioTarjetaExtranjera = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaExtranjera.setPlayfile("coto/A0000012");
		stepAudioTarjetaExtranjera
				.setStepDescription("Audio Tarjeta extranjera");
		cf.addTask(stepAudioTarjetaExtranjera);

		stepAudioTCI = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioTCI.setPlayfile("coto/A0000012");
		stepAudioTCI.setStepDescription("Audio Tarjeta TCI");
		cf.addTask(stepAudioTCI);

		stepMenuAutorizacion = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuAutorizacion.setStepDescription("Menu Autorizaciones");
		stepMenuAutorizacion
				.setContextVariableName(resultadoMenuAutorizacionContextVar);
		cf.addTask(stepMenuAutorizacion);

		stepMenuImporte = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuImporte.setStepDescription("Menu Importe");
		stepMenuImporte.setContextVariableName(resultadoMenuImporteContextVar);
		cf.addTask(stepMenuImporte);

		/* --- TRAMA JPOS --- */

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos
				.setContextVariableTipoMensaje(envioServerJposConsultasContexVar);
		enviaTramaJpos.setContextVariableName(retornoJPOS);
		enviaTramaJpos.setContextVariableRspJpos(retornoMsgJPOS);
		enviaTramaJpos.addformatoVariables(0, mensajeSaldosJpos);
		enviaTramaJpos.addformatoVariables(1, numeroDeLineaContexVar);
		enviaTramaJpos.addformatoVariables(2, tarjetaContexVar);
		enviaTramaJpos.addformatoVariables(3, fillerContexVar);
		enviaTramaJpos.addformatoVariables(4, idLlamadaContexVar);
		enviaTramaJpos.addformatoVariables(5, whisperContextVar);

		enviaTramaJpos.setStepDescription("Envia Trama a JPOS");
		cf.addTask(enviaTramaJpos);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(retornoJPOS);
		evalRetJPOS.setStepDescription("Evalua el codigo de retorno JPOS");
		cf.addTask(evalRetJPOS);

		/* --- STEPS VARIOS --- */

		/* --- CONT / EVAL --- */

		evalEsTarjetaCabal = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalEsTarjetaCabal.setStepDescription("Evalua si es tarjeta CABAL");
		cf.addTask(evalEsTarjetaCabal);

		evalBinCreditoCredicoop = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinCreditoCredicoop
				.setStepDescription("Evalua si es tarjeta Credito Credicoop");
		cf.addTask(evalBinCreditoCredicoop);

		evalBinCreditoOtroBanco = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinCreditoOtroBanco
				.setStepDescription("Evalua si es tarjeta credito Otro Bancos (60420)");
		cf.addTask(evalBinCreditoOtroBanco);

		evalBinCreditoOtroBanco1 = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinCreditoOtroBanco1
				.setStepDescription("Evalua si es tarjeta credito Otro Bancos (60421)");
		cf.addTask(evalBinCreditoOtroBanco1);

		evalBinDebito = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinDebito
				.setStepDescription("Evalua cantidad de Intentos de FECHA contra JPOS por codigo 03");
		cf.addTask(evalBinDebito);

		evalBinDebito1 = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinDebito1.setStepDescription("Evalua si es tarjeta Debito");
		cf.addTask(evalBinDebito1);

		evalBinTCI = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinTCI.setStepDescription("Evalua si es tarjeta TCI");
		cf.addTask(evalBinTCI);

		evalBinExtranjera = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinExtranjera.setStepDescription("Evaluasi es tarjeta Extranjera");
		cf.addTask(evalBinExtranjera);

		evalContadorTarjetaJPOS = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorTarjetaJPOS
				.setStepDescription("Evalua cantidad de Intentos Tarjeta contra JPOS");
		cf.addTask(evalContadorTarjetaJPOS);

		contadorIntentosTarjetaJPOS = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosTarjetaJPOS
				.setContextVariableName(intentosTarjetaContextVar);
		contadorIntentosTarjetaJPOS
				.setStepDescription("Cantidad de intentos  TARJETA contra JPOS ");
		cf.addTask(contadorIntentosTarjetaJPOS);

		stepAudioImporte = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioImporte.setPlayFile("coto/M000090");
		stepAudioImporte.setContextVariableName(importeContextVar);
		stepAudioImporte.setStepDescription("Audio Menu Importe");
		cf.addTask(stepAudioImporte);

		stepAudioCantidadDeCuotas = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCantidadDeCuotas.setPlayFile("coto/M000090");
		stepAudioCantidadDeCuotas.setContextVariableName(cuotasContextVar);
		stepAudioCantidadDeCuotas
				.setStepDescription("Audio Menu Cantiedad de cuotas");
		cf.addTask(stepAudioCantidadDeCuotas);

		stepMenuDerivo = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuDerivo.setContextVariableName(menuDerivoContextVar);
		stepMenuDerivo.setStepDescription("Menu Derivo");
		cf.addTask(stepMenuDerivo);
		/* --- GRUPOS --- */

		// RETORNOS DE JPOS, PARA ITERACIONES//

		pideTarjetaGrp = (PideTarjeta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjeta);
		pideTarjetaGrp.setAudioTarjeta("coto/A0000010");
		pideTarjetaGrp.setAudioSuTarjetaEs("RUTINAPIN/RUTINA_PIN026");
		pideTarjetaGrp.setAudioTarjetaInvalido("coto/A0000011");
		pideTarjetaGrp.setConfirmaTarjetaContextVar(confirmaTarjetaContextVar);
		pideTarjetaGrp.setIntentosTarjetaContextVar(intentosTarjetaContextVar);
		pideTarjetaGrp.setTarjetaContextVar(tarjetaContexVar);

		// PIDE DNI PROPIO DEL IVR COTO//
		stepAudioMenuDeAutorizacion = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuDeAutorizacion
				.setStepDescription("Audio menu Autorizaciones");
		stepAudioMenuDeAutorizacion.setPlayFile("coto/A000014");
		stepAudioMenuDeAutorizacion
				.setContextVariableName(resultadoMenuAutorizacionContextVar);
		cf.addTask(stepAudioMenuDeAutorizacion);

		stepAudioMenuConfirmaImpote = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuConfirmaImpote.setStepDescription("Audio menu Importe");
		stepAudioMenuConfirmaImpote.setPlayFile("coto/A000014");
		stepAudioMenuConfirmaImpote
				.setContextVariableName(resultadoMenuImporteContextVar);
		cf.addTask(stepAudioMenuConfirmaImpote);

		stepAudioDiceCantidadDeCuotas = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDiceCantidadDeCuotas
				.setStepDescription("Audio cantidad De cuotas");
		stepAudioDiceCantidadDeCuotas.setPlayfile("coto/A000014");
		stepAudioDiceCantidadDeCuotas.setScapeDigit("0123456789*#");
		stepAudioDiceCantidadDeCuotas.setContextVariableName(cuotasContextVar);
		cf.addTask(stepAudioDiceCantidadDeCuotas);

		stepAudioDiceImporte = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioDiceImporte.setStepDescription("Audio dice Importe");
		stepAudioDiceImporte.setPlayfile("coto/A000014");
		stepAudioDiceImporte.setScapeDigit("0123456789*#");
		stepAudioDiceImporte.setContextVariableName(importeContextVar);
		cf.addTask(stepAudioDiceImporte);

		stepAudioNroDeAutorizacion = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroDeAutorizacion
				.setStepDescription("Audio numero de Autorizacion");
		stepAudioNroDeAutorizacion.setPlayfile("coto/A000014");
		stepAudioNroDeAutorizacion.setScapeDigit("0123456789*#");
		stepAudioNroDeAutorizacion
				.setContextVariableName(autorizacionContextVar);
		cf.addTask(stepAudioNroDeAutorizacion);

		/* --- Set Audios Informa PIN --- */

		this.evalRetJPOS();

	}

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		 *
		 * ret =>  00    ||   "Informa PIN"    
		 * ret =>  01    ||   "Nro de Documento erróneo"    
		 * ret =>  02    ||   "Fecha de nacimiento errónea"   
		 * ret =>  03    ||   "No es posible emitir PIN"   
		 * ret =>  04    ||   "No es posible emitir PIN"   
		 * ret =>  05    ||   "Operacion Denegada" 		
		 * ret =>  06    ||   "Falta PIN"   
		 * ret =>  08    ||   "Tarjeta inexistente"   
		 *  
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("00", stepAudioNroDeAutorizacion.GetId());
		evalRetJPOS.addSwitchValue("01", pideComercio.getInitialStep());
		evalRetJPOS.addSwitchValue("02", pideTarjetaGrp.getInitialStep());
		evalRetJPOS.addSwitchValue("03", pideFechaVencimiento.getInitialStep());
		evalRetJPOS
				.addSwitchValue("04", pideDigitoVerificador.getInitialStep());
		evalRetJPOS.addSwitchValue("05", stepAudioOperacionDenegada.GetId());
		evalRetJPOS.addSwitchValue("06", stepAudioDerivoAsesor.GetId());
		evalRetJPOS.addSwitchValue("08", stepAudioSuperaElLimite.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	/* ---------------- Inicializo las Variables De Contexto -------------- */

	@SuppressWarnings("unchecked")
	private ContextVar getContextVar(String descrip, String initialValue,
			String astUid, String ctxVarName) {
		ContextVar tmpCtxVar = new ContextVar(ctx);
		tmpCtxVar.setId(this.idContextVar++);
		tmpCtxVar.setVarDescrip(descrip);
		tmpCtxVar.setAstUid(astUid);
		tmpCtxVar.setVarValue(initialValue);
		tmpCtxVar.setCtxVarName(ctxVarName);
		ctx.put(tmpCtxVar.getId(), tmpCtxVar);
		return tmpCtxVar;
	}

	@SuppressWarnings("unused")
	private void createContextVars(AgiChannel channel) {
		/* --- Inicio --- */

		String AstUid = channel.getUniqueId();

		String ast_uid = "";

		if (AstUid.contains("-")) {
			ast_uid = AstUid.split("-")[1];
		} else {
			ast_uid = AstUid;
		}

		ast_uid = String.format("%030d", 0) + ast_uid.replaceAll("\\.", "");

		// --CONTEXT GENERALES--//

		tarjetaContexVar = this.getContextVar("Tarjeta", "", AstUid,
				"tarjetaContexVar");
		// tarjetaContexVar = this.getContextVar("Tarjeta", "6031670010534840",
		// AstUid, "tarjetaContexVar");
		tarjetaContexVar.setStringFormat("%16d");

		confirmaTarjetaContextVar = this.getContextVar("Confirma Tarjeta", "",
				AstUid, "confirmaTarjetaContextVar");

		fechaContextVar = this.getContextVar("Fecha Formato 8 Digitos", "",
				AstUid, "fechaContextVar");

		confirmaFechaContextVar = this.getContextVar(
				"Confirmacion de Fecha de Nacimiento", "", AstUid,
				"confirmaFechaContextVar");

		// ---INTENTOS---//

		intentosFechaContextVar = this.getContextVar("Intentos Fecha", "0",
				AstUid, "intentosFechaContextVar");

		intentosTarjetaContextVar = this.getContextVar("Intentos Tarjeta", "0",
				AstUid, "intentosTarjetaContextVar");

		scapeDigitContextVar = this.getContextVar("Scape Digit", "", AstUid,
				"scapeDigitContextVar");

		// ---JPOS---//

		retornoJPOS = this.getContextVar("Codigo de retorno JPOS", "", AstUid,
				"retornoJPOS");

		retornoMsgJPOS = this.getContextVar("Codigo de retorno JPOS", "",
				AstUid, "retornoMsgJPOS");

		envioServerJposConsultasContexVar = this.getContextVar("Tipo Mnj",
				"consultas", AstUid, "envioServerJposConsultasContexVar");

		envioServerJposPrecargadasContexVar = this.getContextVar("Tipo Mnj",
				"precargada", AstUid, "envioServerJposPrecargadasContexVar");

		// ---VARIOS | ARMADO DE TRAMA ---//

		fillerContexVar = this.getContextVar("Filler", " ", AstUid,
				"fillerContexVar");

		fillerContexVar.setStringFormat("%81s");

		idLlamadaContexVar = this.getContextVar("Id llamada",
				ast_uid.substring(ast_uid.length() - 29), AstUid,
				"idLlamadaContexVar");

		whisperContextVar = this.getContextVar("Whisper", "0", AstUid,
				"whisperContextVar");

		fdnContexVar = this.getContextVar("fdnContexVar", "", AstUid,
				"fdnContexVar");

		contextVarMes = this.getContextVar("contextVarMes", "", AstUid,
				"contextVarMes");

		contextVarAnio = this.getContextVar("contextVarAnio", "", AstUid,
				"contextVarAnio");
		contextVarAnio.setStringFormat("%02d");

		contextVarDia = this.getContextVar("contextVarDia", "", AstUid,
				"contextVarDia");

		cambiaEjecutoJPOSContextVar = this.getContextVar(
				"cambiaEjecutoJPOSContextVar", "1", AstUid,
				"cambiaEjecutoJPOSContextVar");

		ejecutoJPOSContextVar = this.getContextVar("ejecutoJPOSContextVar",
				"0", AstUid, "ejecutoJPOSContextVar");

		importeContextVar = this.getContextVar("importeContextVar", "", AstUid,
				"importeContextVar");

		cuotasContextVar = this.getContextVar("cuotasContextVar", "", AstUid,
				"cuotasContextVar");

		resultadoMenuImporteContextVar = this.getContextVar(
				"resultadoMenuImporteContextVar", "", AstUid,
				"resultadoMenuImporteContextVar");

		menuDerivoContextVar = this.getContextVar("menuDerivoContextVar", "",
				AstUid, "menuDerivoContextVar");

		autorizacionContextVar = this.getContextVar("autorizacionContextVar",
				"", AstUid, "autorizacionContextVar");

		resultadoMenuAutorizacionContextVar = this.getContextVar(
				"autoriresultadoMenuAutorizacionContextVarzacionContextVar",
				"", AstUid, "resultadoMenuAutorizacionContextVar");
	}
}
