package clasesivr;

import java.util.UUID;

import main.Daemon;

import org.asteriskjava.fastagi.AgiChannel;

import condition.condition;
import context.ContextVar;

import step.StepAnswer;
import step.StepCabalBalance;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepParseSaldosTarjeta;
import step.StepParseSaldosTarjetaPrecargada;
import step.StepPlay;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepFactory.StepType;
import step.group.ArmaSaldoTarjetaCoto;
import step.group.ArmaSaldoTarjetaPrecargada;
import step.group.StepGroupFactory;
import workflow.Task;

public class RutinaSaldoCabal extends Rutina {

	protected StepSendJPOS enviaTramaJpos;
	protected StepGetAsteriskVariable obtieneTarjeta;
	protected StepContinueOnDialPlan continuaEnDialPLan;
	protected StepParseSaldosTarjeta parserTarjeta;
	protected StepSwitch evalRetJPOS;
	protected StepCabalBalance saldoCabal;
	protected StepPlay stepAudioServNoDisponible;
	protected ContextVar envioServerJposConsultasContexVar;
	protected ContextVar retornoJPOS;
	protected ContextVar retornoMsgJPOS;
	protected ContextVar cierreProxResumDiaContextVar;
	protected ContextVar codigoDeEstudioContextVar;
	protected ContextVar cierreProxResumMesContextVar;
	protected ContextVar cierreProxResumAnioContextVar;
	protected ContextVar fechaVencimientoUltResumContextVarDia;
	protected ContextVar fechaVencimientoUltResumContextVarMes;
	protected ContextVar fechaVencimientoUltResumContextVarAnio;
	protected ContextVar pagoMinPendienteDeCanceUltResumContextVar;
	protected ContextVar pagoMinUltResumContextVar;
	protected ContextVar saldoCuentaContextVar;
	protected ContextVar saldoUltResumContextVar;
	protected ContextVar salEnCuotasContextVar;
	protected ContextVar salEnUnPagoContextVar;
	protected ContextVar salPendienteDeCanceUltResumContextVar;
	protected ContextVar vencimientoProxResumAnioContextVar;
	protected ContextVar vencimientoProxResumDiaContextVar;
	protected ContextVar vencimientoProxResumMesContextVar;
	protected ContextVar totalPagUltResumContextVar;
	protected ContextVar pagoMinPendienteDeCanceUltDecimalResumContextVar;
	protected ContextVar pagoMinUltResumDecimContextVar;
	protected ContextVar saldoCuentaDecimalContextVar;
	protected ContextVar saldoUltResumDecimalContextVar;
	protected ContextVar salEnCuotasDecimalContextVar;
	protected ContextVar salEnUnPagoDecimalContextVar;
	protected ContextVar totalPagUltResumDecimalContextVar;
	protected ContextVar salPendienteDeCanceUltResumDecimalContextVar;
	protected ContextVar tarjetaPendienteDeActivacionContextVar;
	protected ContextVar tarjetaContexVar;
	protected ContextVar mensajeSaldosJpos;
	protected ContextVar numeroDeLineaContexVar;
	protected ContextVar fillerContexVar;
	protected ContextVar idLlamadaContexVar;
	protected ContextVar whisperContextVar;
	protected StepConditional evalBinPrecargada;
	protected ContextVar mensajeSaldosJposPrecargada;
	protected StepSendJPOS enviaTramaJposPrecargada;
	protected StepConditional evalBinAgroCabal;
	protected StepExecute stepDerivoLlamada;
	protected StepPlay stepAudioDerivoAsesor;
	private ContextVar disponibleComprasContextVar;
	private ContextVar s_DispComprasContextVar;
	private ContextVar s_ImporteUltimaCargaContextVar;
	private ContextVar importeUltimaCargaContextVar;
	private ContextVar s_importeCompraContextVar_1;
	private ContextVar s_importeCompraContextVar_2;
	private ContextVar s_importeCompraContextVar_3;
	private ContextVar s_importeCompraContextVar_4;
	private ContextVar s_importeCompraContextVar_5;
	private ContextVar s_importeCompraContextVar_6;
	private ContextVar fechaCompraDiaContextVar_6;
	private ContextVar fechaCompraDiaContextVar_5;
	private ContextVar fechaCompraDiaContextVar_4;
	private ContextVar fechaCompraDiaContextVar_3;
	private ContextVar fechaCompraDiaContextVar_2;
	private ContextVar fechaCompraDiaContextVar_1;
	private ContextVar importeDecimalCompraContextVar_4;
	private ContextVar importeDecimalCompraContextVar_3;
	private ContextVar importeDecimalCompraContextVar_2;
	private ContextVar importeDecimalCompraContextVar_1;
	private ContextVar importeCompraContextVar_6;
	private ContextVar importeCompraContextVar_5;
	private ContextVar importeCompraContextVar_4;
	private ContextVar importeCompraContextVar_3;
	private ContextVar importeCompraContextVar_2;
	private ContextVar importeCompraContextVar_1;
	private ContextVar fechaCompraAnioContextVar_6;
	private ContextVar fechaCompraAnioContextVar_5;
	private ContextVar fechaCompraAnioContextVar_4;
	private ContextVar fechaCompraAnioContextVar_3;
	private ContextVar fechaCompraAnioContextVar_2;
	private ContextVar fechaCompraAnioContextVar_1;
	private ContextVar fechaCompraMesContextVar_6;
	private ContextVar fechaCompraMesContextVar_5;
	private ContextVar fechaCompraMesContextVar_4;
	private ContextVar fechaCompraMesContextVar_3;
	private ContextVar fechaCompraMesContextVar_2;
	private ContextVar fechaCompraMesContextVar_1;
	private ContextVar importeDecimalCompraContextVar_6;
	private ContextVar importeDecimalCompraContextVar_5;
	private ContextVar importeDecimalUltimaCargaContextVar;
	private ContextVar fechaUltimaCargaAnioContextVar;
	private ContextVar fechaUltimaCargaMesContextVar;
	private ContextVar fechaUltimaCargaDiaContextVar;
	private ContextVar s_disponibleRetirosContextVar;
	private ContextVar disponibleRetirosContextVar;
	private ContextVar disponibleRetirosDecimalContextVar;
	private ContextVar fillerPrecargadaContexVar;
	private StepParseSaldosTarjetaPrecargada parserPrecargadaTarjeta;
	private ArmaSaldoTarjetaPrecargada armaSaldoTarjetaPrecargadaGrp;
	private ContextVar scapeDigitContextVar;
	private ContextVar marcaContexVar;
	private StepSwitch evalRetJPOSPrecargada;

	@Override
	protected void setSequence() {

		obtieneTarjeta.setNextstep(evalBinPrecargada.GetId());

		// PARSEO SALDOS PRECARGADAS

		evalBinPrecargada.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,6) == '604218'",
				enviaTramaJposPrecargada.GetId(), evalBinAgroCabal.GetId()));

		evalBinAgroCabal.addCondition(new condition(1, "substring('#{"
				+ tarjetaContexVar.getVarName() + "}',0,8) == '58965703'",
				stepAudioDerivoAsesor.GetId(), enviaTramaJpos.GetId()));

		enviaTramaJposPrecargada.setNextstep(parserPrecargadaTarjeta.GetId());
		parserPrecargadaTarjeta.setNextstep(evalRetJPOSPrecargada.GetId());

		enviaTramaJpos.setNextstep(parserTarjeta.GetId());

		parserTarjeta.setNextstep(evalRetJPOS.GetId());
		evalRetJPOS.setNextstep(stepAudioServNoDisponible.GetId());

		stepAudioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
		stepAudioServNoDisponible.setNextstep(continuaEnDialPLan.GetId());

		saldoCabal.setNextstep(continuaEnDialPLan.GetId());
	}

	@Override
	protected void addGroups() {
		for (Task tmpTask : armaSaldoTarjetaPrecargadaGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
	}

	@Override
	protected void createSteps() {

		obtieneTarjeta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneTarjeta.setContextVariableName(tarjetaContexVar);
		obtieneTarjeta.setVariableName("plastico");
		obtieneTarjeta.setStepDescription("GETVARIABLE => OBTIENE PLASTICO");

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos
				.setContextVariableTipoMensaje(envioServerJposConsultasContexVar);
		enviaTramaJpos.setStepDescription("SENDJPOS => ENVIO MENSAJE JPOS");
		enviaTramaJpos.setContextVariableName(retornoJPOS);
		enviaTramaJpos.setContextVariableRspJpos(retornoMsgJPOS);
		enviaTramaJpos.addformatoVariables(0, mensajeSaldosJpos);
		enviaTramaJpos.addformatoVariables(1, numeroDeLineaContexVar);
		enviaTramaJpos.addformatoVariables(2, tarjetaContexVar);
		enviaTramaJpos.addformatoVariables(3, fillerContexVar);
		enviaTramaJpos.addformatoVariables(4, idLlamadaContexVar);
		enviaTramaJpos.addformatoVariables(5, whisperContextVar);

		parserTarjeta = (StepParseSaldosTarjeta) StepFactory.createStep(
				StepFactory.StepType.ParseSaldoTarjeta, UUID.randomUUID());
		parserTarjeta
				.setStepDescription("PARSESALDOSTARJETAS => PARSE SALDOS TARJETA CABAL");
		parserTarjeta.setRetornoMsgJPOS(retornoMsgJPOS);
		parserTarjeta
				.setCierreProxResumDiaContextVar(cierreProxResumDiaContextVar);
		parserTarjeta.setCodigoDeEstudioContextVar(codigoDeEstudioContextVar);
		parserTarjeta
				.setCierreProxResumMesContextVar(cierreProxResumMesContextVar);
		parserTarjeta
				.setCierreProxResumAnioContextVar(cierreProxResumAnioContextVar);
		parserTarjeta
				.setFechaVencimientoUltResumContextVarDia(fechaVencimientoUltResumContextVarDia);
		parserTarjeta
				.setFechaVencimientoUltResumContextVarMes(fechaVencimientoUltResumContextVarMes);
		parserTarjeta
				.setFechaVencimientoUltResumContextVarAnio(fechaVencimientoUltResumContextVarAnio);
		parserTarjeta
				.setPagoMinPendienteDeCanceUltResumContextVar(pagoMinPendienteDeCanceUltResumContextVar);
		parserTarjeta.setPagoMinUltResumContextVar(pagoMinUltResumContextVar);
		parserTarjeta.setSaldoCuentaContextVar(saldoCuentaContextVar);
		parserTarjeta.setSaldoUltResumContextVar(saldoUltResumContextVar);
		parserTarjeta.setSalEnCuotasContextVar(salEnCuotasContextVar);
		parserTarjeta.setSalEnUnPagoContextVar(salEnUnPagoContextVar);
		parserTarjeta
				.setSalPendienteDeCanceUltResumContextVar(salPendienteDeCanceUltResumContextVar);
		parserTarjeta
				.setVencimientoProxResumAnioContextVar(vencimientoProxResumAnioContextVar);
		parserTarjeta
				.setVencimientoProxResumDiaContextVar(vencimientoProxResumDiaContextVar);
		parserTarjeta
				.setVencimientoProxResumMesContextVar(vencimientoProxResumMesContextVar);
		parserTarjeta.setTotalPagUltResumContextVar(totalPagUltResumContextVar);
		parserTarjeta
				.setPagoMinPendienteDeCanceUltDecimalResumContextVar(pagoMinPendienteDeCanceUltDecimalResumContextVar);
		parserTarjeta
				.setPagoMinUltResumDecimContextVar(pagoMinUltResumDecimContextVar);
		parserTarjeta
				.setSaldoCuentaDecimalContextVar(saldoCuentaDecimalContextVar);
		parserTarjeta
				.setSaldoUltResumDecimalContextVar(saldoUltResumDecimalContextVar);
		parserTarjeta
				.setSalEnCuotasDecimalContextVar(salEnCuotasDecimalContextVar);
		parserTarjeta
				.setSalEnUnPagoDecimalContextVar(salEnUnPagoDecimalContextVar);
		parserTarjeta
				.setTotalPagUltResumDecimalContextVar(totalPagUltResumDecimalContextVar);
		parserTarjeta
				.setSalPendienteDeCanceUltResumDecimalContextVar(salPendienteDeCanceUltResumDecimalContextVar);
		parserTarjeta
				.setTarjetaPendienteDeActivacionContextVar(tarjetaPendienteDeActivacionContextVar);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(retornoJPOS);
		evalRetJPOS.setStepDescription("SWITCH => EVALUA VALOR RETORNO JPOS");
		
		evalRetJPOSPrecargada = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOSPrecargada.setContextVariableName(retornoJPOS);
		evalRetJPOSPrecargada.setStepDescription("SWITCH => EVALUA VALOR RETORNO JPOS");
		

		saldoCabal = (StepCabalBalance) StepFactory.createStep(
				StepType.CabalBalance, UUID.randomUUID());
		saldoCabal.setStepDescription("SALDOCABAL => SALDO TARJETA CABAL");

		enviaTramaJposPrecargada = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJposPrecargada
				.setContextVariableTipoMensaje(envioServerJposConsultasContexVar);
		enviaTramaJposPrecargada
				.setStepDescription("SENDJPOS => ENVIO MENSAJE JPOS PRECARGADA");
		enviaTramaJposPrecargada.setContextVariableName(retornoJPOS);
		enviaTramaJposPrecargada.setContextVariableRspJpos(retornoMsgJPOS);
		enviaTramaJposPrecargada.addformatoVariables(0,
				mensajeSaldosJposPrecargada);
		enviaTramaJposPrecargada.addformatoVariables(1, tarjetaContexVar);
		enviaTramaJposPrecargada.addformatoVariables(2,
				fillerPrecargadaContexVar);
		enviaTramaJposPrecargada.addformatoVariables(3,
				marcaContexVar);
		enviaTramaJposPrecargada.addformatoVariables(4, idLlamadaContexVar);
		enviaTramaJposPrecargada.addformatoVariables(5, whisperContextVar);

		parserPrecargadaTarjeta = (StepParseSaldosTarjetaPrecargada) StepFactory
				.createStep(StepFactory.StepType.ParseSaldoTarjetaPrecargada,
						UUID.randomUUID());
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_1(fechaCompraAnioContextVar_1);
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_2(fechaCompraAnioContextVar_2);
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_3(fechaCompraAnioContextVar_3);
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_4(fechaCompraAnioContextVar_4);
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_5(fechaCompraAnioContextVar_5);
		parserPrecargadaTarjeta
				.setFechaCompraAnioContextVar_6(fechaCompraAnioContextVar_6);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_1(fechaCompraMesContextVar_1);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_2(fechaCompraMesContextVar_2);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_3(fechaCompraMesContextVar_3);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_4(fechaCompraMesContextVar_4);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_5(fechaCompraMesContextVar_5);
		parserPrecargadaTarjeta
				.setFechaCompraMesContextVar_6(fechaCompraMesContextVar_6);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_1(fechaCompraDiaContextVar_1);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_2(fechaCompraDiaContextVar_2);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_3(fechaCompraDiaContextVar_3);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_4(fechaCompraDiaContextVar_4);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_5(fechaCompraDiaContextVar_5);
		parserPrecargadaTarjeta
				.setFechaCompraDiaContextVar_6(fechaCompraDiaContextVar_6);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_1(importeCompraContextVar_1);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_2(importeCompraContextVar_2);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_3(importeCompraContextVar_3);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_4(importeCompraContextVar_4);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_5(importeCompraContextVar_5);
		parserPrecargadaTarjeta
				.setImporteCompraContextVar_6(importeCompraContextVar_6);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_1(importeDecimalCompraContextVar_1);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_2(importeDecimalCompraContextVar_2);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_3(importeDecimalCompraContextVar_3);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_4(importeDecimalCompraContextVar_4);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_5(importeDecimalCompraContextVar_5);
		parserPrecargadaTarjeta
				.setImporteDecimalCompraContextVar_6(importeDecimalCompraContextVar_6);
		parserPrecargadaTarjeta
				.setDisponibleComprasContextVar(disponibleComprasContextVar);
		parserPrecargadaTarjeta
				.setDisponibleRetirosContextVar(disponibleRetirosContextVar);
		parserPrecargadaTarjeta
				.setDisponibleRetirosDecimalContextVar(disponibleRetirosDecimalContextVar);
		parserPrecargadaTarjeta
				.setFechaUltimaCargaAnioContextVar(fechaUltimaCargaAnioContextVar);
		parserPrecargadaTarjeta
				.setFechaUltimaCargaMesContextVar(fechaUltimaCargaMesContextVar);
		parserPrecargadaTarjeta
				.setFechaUltimaCargaDiaContextVar(fechaUltimaCargaDiaContextVar);

		armaSaldoTarjetaPrecargadaGrp = (ArmaSaldoTarjetaPrecargada) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.armaSaldoTarjetaPrecargada);
		armaSaldoTarjetaPrecargadaGrp
				.setDayPrimerCompraContextVar(fechaCompraDiaContextVar_1);
		armaSaldoTarjetaPrecargadaGrp
				.setDaySegundoCompraContextVar(fechaCompraDiaContextVar_2);
		armaSaldoTarjetaPrecargadaGrp
				.setDayTercerCompraContextVar(fechaCompraDiaContextVar_3);
		armaSaldoTarjetaPrecargadaGrp
				.setDayCuartoCompraContextVar(fechaCompraDiaContextVar_4);
		armaSaldoTarjetaPrecargadaGrp
				.setDayQuintoCompraContextVar(fechaCompraDiaContextVar_5);
		armaSaldoTarjetaPrecargadaGrp
				.setDaySextoCompraContextVar(fechaCompraDiaContextVar_6);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthPrimerCompraContextVar(fechaCompraMesContextVar_1);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthSegundoCompraContextVar(fechaCompraMesContextVar_2);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthTercerCompraContextVar(fechaCompraMesContextVar_3);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthCuartoCompraContextVar(fechaCompraMesContextVar_4);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthQuintoCompraContextVar(fechaCompraMesContextVar_5);
		armaSaldoTarjetaPrecargadaGrp
				.setMonthSextoCompraContextVar(fechaCompraMesContextVar_6);
		armaSaldoTarjetaPrecargadaGrp
				.setImportePrimerCompraContextVar(importeCompraContextVar_1);
		armaSaldoTarjetaPrecargadaGrp
				.setImporteSegundoCompraContextVar(importeCompraContextVar_2);
		armaSaldoTarjetaPrecargadaGrp
				.setImporteTercerCompraContextVar(importeCompraContextVar_3);
		armaSaldoTarjetaPrecargadaGrp
				.setImporteCuartaCompraContextVar(importeCompraContextVar_4);
		armaSaldoTarjetaPrecargadaGrp
				.setImporteQuintaCompraContextVar(importeCompraContextVar_5);
		armaSaldoTarjetaPrecargadaGrp
				.setImporteSextaCompraContextVar(importeCompraContextVar_6);
		armaSaldoTarjetaPrecargadaGrp.setDisponibleCompraContextVar(disponibleComprasContextVar);
		armaSaldoTarjetaPrecargadaGrp.setDisponibleRetirosContextVar(disponibleRetirosContextVar);
		armaSaldoTarjetaPrecargadaGrp.setDayRetirosContextVar(fechaUltimaCargaDiaContextVar);
		armaSaldoTarjetaPrecargadaGrp.setMonthRetirosContextVar(fechaUltimaCargaMesContextVar);
		armaSaldoTarjetaPrecargadaGrp
				.setScapeDigitContextVar(scapeDigitContextVar);
		armaSaldoTarjetaPrecargadaGrp
				.setRetornoMsgJPOSContextVar(retornoMsgJPOS);

		// EVAL BINES

		evalBinPrecargada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinPrecargada.setStepDescription("CONDITIONAL => BIN DON PEDRO");

		evalBinAgroCabal = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalBinAgroCabal.setStepDescription("CONDITIONAL => BIN AGROCABAL");

		// DERIVO CONF

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("RUTINAPINCOP/RUTINA_PIN034");
		stepAudioDerivoAsesor
				.setStepDescription("PLAY => DERIVO ASESOR. COD : 85");

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADOR"));
		stepDerivoLlamada.setStepDescription("EXECUTE => DERIVO ASESOR");

		// SER NO DISP

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");

		continuaEnDialPLan = (StepContinueOnDialPlan) StepFactory.createStep(
				StepType.ContinueOnDialPlan, UUID.randomUUID());
		continuaEnDialPLan
				.setStepDescription("CONTINUEONDIALPLAN => CONTINUA EN DIALPLAN");

		this.evalRetJPOS();
		this.evalRetJPOSPrecargada();
	}

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
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("00", saldoCabal.GetId());

		evalRetJPOS.addSwitchValue("99", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("96", stepAudioServNoDisponible.GetId());

		evalRetJPOS.addSwitchValue("98", stepAudioServNoDisponible.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	private void evalRetJPOSPrecargada() {

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
		-------------------------------------------------------------------------------	*/

		evalRetJPOSPrecargada.addSwitchValue("00", armaSaldoTarjetaPrecargadaGrp.getInitialStep());

		evalRetJPOSPrecargada.addSwitchValue("99", stepAudioServNoDisponible.GetId());
		evalRetJPOSPrecargada.addSwitchValue("96", stepAudioServNoDisponible.GetId());

		evalRetJPOSPrecargada.addSwitchValue("98", stepAudioServNoDisponible.GetId());
		evalRetJPOSPrecargada.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	@Override
	protected void createContextVars(AgiChannel channel) {
		envioServerJposConsultasContexVar = this.getContextVar(
				"envioServerJposConsultasContexVar", "consultas",
				"envioServerJposConsultasContexVar");

		retornoJPOS = this.getContextVar("retornoJPOS", "", "retornoJPOS");

		mensajeSaldosJpos = this.getContextVar("mensajeSaldosJpos", "5",
				"mensajeSaldosJpos");

		mensajeSaldosJposPrecargada = this.getContextVar(
				"mensajeSaldosJposPrecargada", "3",
				"mensajeSaldosJposPrecargada");

		retornoMsgJPOS = this.getContextVar("retornoMsgJPOS", "",
				"retornoMsgJPOS");

		tarjetaContexVar = this.getContextVar("tarjetaContexVar", "",
				"tarjetaContexVar");
		tarjetaContexVar.setStringFormat("%16d");

		fillerContexVar = this.getContextVar("fillerContexVar", " ",
				"fillerContexVar");

		fillerContexVar.setStringFormat("%81s");

		fillerPrecargadaContexVar = this.getContextVar(
				"fillerPrecargadaContexVar", " ", "fillerPrecargadaContexVar");
		fillerPrecargadaContexVar.setStringFormat("%81s");

		// ---VARIOS | ARMADO DE TRAMA ---//

		idLlamadaContexVar = this.getContextVar("Id llamada",
				ast_uid.substring(ast_uid.length() - 29), "idLlamadaContexVar");

		whisperContextVar = this.getContextVar("Whisper", "0",
				"whisperContextVar");

		numeroDeLineaContexVar = this.getContextVar("numeroDeLineaContexVar",
				"01", "numeroDeLineaContexVar");
		
		marcaContexVar = this.getContextVar("marcaContexVar",
				"02", "marcaContexVar");


		codigoDeEstudioContextVar = this.getContextVar(
				"codigoDeEstudioContextVar", "", "codigoDeEstudioContextVar");

		salEnCuotasContextVar = this.getContextVar("salEnCuotasContextVar", "",
				"salEnCuotasContextVar");

		salEnUnPagoContextVar = this.getContextVar("salEnUnPagoContextVar", "",
				"salEnUnPagoContextVar");

		fechaVencimientoUltResumContextVarDia = this.getContextVar(
				"fechaVencimientoUltResumContextVarDia", "",
				"fechaVencimientoUltResumContextVarDia");

		fechaVencimientoUltResumContextVarMes = this.getContextVar(
				"fechaVencimientoUltResumContextVarMes", "",
				"fechaVencimientoUltResumContextVarMes");

		fechaVencimientoUltResumContextVarAnio = this.getContextVar(
				"fechaVencimientoUltResumContextVarAnio", "",
				"fechaVencimientoUltResumContextVarAnio");

		saldoUltResumContextVar = this.getContextVar("saldoUltResumContextVar",
				"", "saldoUltResumContextVar");

		pagoMinUltResumContextVar = this.getContextVar(
				"pagoMinUltResumContextVar", "", "pagoMinUltResumContextVar");

		totalPagUltResumContextVar = this.getContextVar(
				"totalPagUltResumContextVar", "", "totalPagUltResumContextVar");

		salPendienteDeCanceUltResumContextVar = this.getContextVar(
				"salPendienteDeCanceUltResumContextVar", "",
				"salPendienteDeCanceUltResumContextVar");

		pagoMinPendienteDeCanceUltResumContextVar = this.getContextVar(
				"pagoMinPendienteDeCanceUltResumContextVar", "",
				"pagoMinPendienteDeCanceUltResumContextVar");

		cierreProxResumDiaContextVar = this.getContextVar(
				"cierreProxResumDiaContextVar", "",
				"cierreProxResumDiaContextVar");

		cierreProxResumMesContextVar = this.getContextVar(
				"cierreProxResumMesContextVar", "",
				"cierreProxResumMesContextVar");

		cierreProxResumAnioContextVar = this.getContextVar(
				"cierreProxResumAnioContextVar", "",
				"cierreProxResumAnioContextVar");

		vencimientoProxResumDiaContextVar = this.getContextVar(
				"vencimientoProxResumDiaContextVar", "",
				"vencimientoProxResumDiaContextVar");

		vencimientoProxResumMesContextVar = this.getContextVar(
				"vencimientoProxResumMesContextVar", "",
				"vencimientoProxResumMesContextVar");

		vencimientoProxResumAnioContextVar = this.getContextVar(
				"vencimientoProxResumAnioContextVar", "",
				"vencimientoProxResumAnioContextVar");

		saldoCuentaContextVar = this.getContextVar("saldoCuentaContextVar", "",
				"saldoCuentaContextVar");

		saldoUltResumDecimalContextVar = this.getContextVar(
				"saldoUltResumDecimalContextVar", "",
				"saldoUltResumDecimalContextVar");

		totalPagUltResumDecimalContextVar = this.getContextVar(
				"totalPagUltResumDecimalContextVar", "",
				"totalPagUltResumDecimalContextVar");

		pagoMinUltResumDecimContextVar = this.getContextVar(
				"pagoMinUltResumDecimContextVar", "",
				"pagoMinUltResumDecimContextVar");

		salEnUnPagoDecimalContextVar = this.getContextVar(
				"salEnUnPagoDecimalContextVar", "",
				"salEnUnPagoDecimalContextVar");

		salEnCuotasDecimalContextVar = this.getContextVar(
				"salEnCuotasDecimalContextVar", "",
				"salEnCuotasDecimalContextVar");

		pagoMinPendienteDeCanceUltDecimalResumContextVar = this.getContextVar(
				"pagoMinPendienteDeCanceUltDecimalResumContextVar", "",
				"pagoMinPendienteDeCanceUltDecimalResumContextVar");

		saldoCuentaDecimalContextVar = this.getContextVar(
				"saldoCuentaDecimalContextVar", "",
				"saldoCuentaDecimalContextVar");

		salPendienteDeCanceUltResumDecimalContextVar = this.getContextVar(
				"salPendienteDeCanceUltResumDecimalContextVar", "",
				"salPendienteDeCanceUltResumDecimalContextVar");

		tarjetaPendienteDeActivacionContextVar = this.getContextVar(
				"tarjetaPendienteDeActivacionContextVar", "",
				"tarjetaPendienteDeActivacionContextVar");

		// ---VARIOS | ARMADO DE TRAMA ---//

		disponibleComprasContextVar = this.getContextVar(
				"disponibleComprasContextVar", "",
				"disponibleComprasContextVar");

		s_DispComprasContextVar = this.getContextVar("s_DispComprasContextVar",
				"", "s_DispComprasContextVar");

		s_ImporteUltimaCargaContextVar = this.getContextVar(
				"s_ImporteUltimaCargaContextVar", "",
				"s_ImporteUltimaCargaContextVar");

		importeUltimaCargaContextVar = this.getContextVar(
				"importeUltimaCargaContextVar", "",
				"importeUltimaCargaContextVar");

		s_importeCompraContextVar_1 = this.getContextVar(
				"s_importeCompraContextVar_1", "",
				"s_importeCompraContextVar_1");

		s_importeCompraContextVar_2 = this.getContextVar(
				"s_importeCompraContextVar_2", "",
				"s_importeCompraContextVar_2");

		s_importeCompraContextVar_3 = this.getContextVar(
				"s_importeCompraContextVar_3", "",
				"s_importeCompraContextVar_3");

		s_importeCompraContextVar_4 = this.getContextVar(
				"s_importeCompraContextVar_4", "",
				"s_importeCompraContextVar_4");

		s_importeCompraContextVar_5 = this.getContextVar(
				"s_importeCompraContextVar_5", "",
				"s_importeCompraContextVar_5");

		s_importeCompraContextVar_6 = this.getContextVar(
				"s_importeCompraContextVar_6", "",
				"s_importeCompraContextVar_6");

		fechaCompraDiaContextVar_1 = this.getContextVar(
				"fechaCompraDiaContextVar_1", "", "fechaCompraDiaContextVar_1");

		fechaCompraDiaContextVar_2 = this.getContextVar(
				"fechaCompraDiaContextVar_2", "", "fechaCompraDiaContextVar_2");

		fechaCompraDiaContextVar_3 = this.getContextVar(
				"fechaCompraDiaContextVar_3", "", "fechaCompraDiaContextVar_3");

		fechaCompraDiaContextVar_4 = this.getContextVar(
				"fechaCompraDiaContextVar_4", "", "fechaCompraDiaContextVar_4");

		fechaCompraDiaContextVar_5 = this.getContextVar(
				"fechaCompraDiaContextVar_5", "", "fechaCompraDiaContextVar_5");

		fechaCompraDiaContextVar_6 = this.getContextVar(
				"fechaCompraDiaContextVar_6", "", "fechaCompraDiaContextVar_6");

		importeCompraContextVar_1 = this.getContextVar(
				"importeCompraContextVar_1", "", "importeCompraContextVar_1");

		importeCompraContextVar_2 = this.getContextVar(
				"importeCompraContextVar_2", "", "importeCompraContextVar_2");

		importeCompraContextVar_3 = this.getContextVar(
				"importeCompraContextVar_3", "", "importeCompraContextVar_3");

		importeCompraContextVar_4 = this.getContextVar(
				"importeCompraContextVar_4", "", "importeCompraContextVar_4");

		importeCompraContextVar_5 = this.getContextVar(
				"importeCompraContextVar_5", "", "importeCompraContextVar_5");

		importeCompraContextVar_6 = this.getContextVar(
				"importeCompraContextVar_6", "", "importeCompraContextVar_6");

		importeDecimalCompraContextVar_1 = this.getContextVar(
				"importeDecimalCompraContextVar_1", "",
				"importeDecimalCompraContextVar_1");

		importeDecimalCompraContextVar_2 = this.getContextVar(
				"importeDecimalCompraContextVar_2", "",
				"importeDecimalCompraContextVar_2");

		importeDecimalCompraContextVar_3 = this.getContextVar(
				"importeDecimalCompraContextVar_3", "",
				"importeDecimalCompraContextVar_3");

		importeDecimalCompraContextVar_4 = this.getContextVar(
				"importeDecimalCompraContextVar_4", "",
				"importeDecimalCompraContextVar_4");

		importeDecimalCompraContextVar_5 = this.getContextVar(
				"importeDecimalCompraContextVar_5", "",
				"importeDecimalCompraContextVar_5");

		importeDecimalCompraContextVar_6 = this.getContextVar(
				"importeDecimalCompraContextVar_6", "",
				"importeDecimalCompraContextVar_6");

		fechaCompraMesContextVar_1 = this.getContextVar(
				"fechaCompraMesContextVar_1", "", "fechaCompraMesContextVar_1");

		fechaCompraMesContextVar_2 = this.getContextVar(
				"fechaCompraMesContextVar_2", "", "fechaCompraMesContextVar_2");

		fechaCompraMesContextVar_3 = this.getContextVar(
				"fechaCompraMesContextVar_3", "", "fechaCompraMesContextVar_3");

		fechaCompraMesContextVar_4 = this.getContextVar(
				"fechaCompraMesContextVar_4", "", "fechaCompraMesContextVar_4");

		fechaCompraMesContextVar_5 = this.getContextVar(
				"fechaCompraMesContextVar_5", "", "fechaCompraMesContextVar_5");

		fechaCompraMesContextVar_6 = this.getContextVar(
				"fechaCompraMesContextVar_6", "", "fechaCompraMesContextVar_6");

		fechaCompraAnioContextVar_1 = this.getContextVar(
				"fechaCompraAnioContextVar_1", "",
				"fechaCompraAnioContextVar_1");

		fechaCompraAnioContextVar_2 = this.getContextVar(
				"fechaCompraAnioContextVar_2", "",
				"fechaCompraAnioContextVar_2");

		fechaCompraAnioContextVar_3 = this.getContextVar(
				"fechaCompraAnioContextVar_3", "",
				"fechaCompraAnioContextVar_3");

		fechaCompraAnioContextVar_4 = this.getContextVar(
				"fechaCompraAnioContextVar_4", "",
				"fechaCompraAnioContextVar_4");

		fechaCompraAnioContextVar_5 = this.getContextVar(
				"fechaCompraAnioContextVar_5", "",
				"fechaCompraAnioContextVar_5");

		fechaCompraAnioContextVar_6 = this.getContextVar(
				"fechaCompraAnioContextVar_6", "",
				"fechaCompraAnioContextVar_6");

		disponibleRetirosDecimalContextVar = this.getContextVar(
				"disponibleRetirosDecimalContextVar", "",
				"disponibleRetirosDecimalContextVar");

		disponibleRetirosContextVar = this.getContextVar(
				"disponibleRetirosContextVar", "",
				"disponibleRetirosContextVar");

		s_disponibleRetirosContextVar = this.getContextVar(
				"s_disponibleRetirosContextVar", "",
				"s_disponibleRetirosContextVar");

		fechaUltimaCargaDiaContextVar = this.getContextVar(
				"fechaUltimaCargaDiaContextVar", "",
				"fechaUltimaCargaDiaContextVar");

		fechaUltimaCargaMesContextVar = this.getContextVar(
				"fechaUltimaCargaMesContextVar", "",
				"fechaUltimaCargaMesContextVar");

		fechaUltimaCargaAnioContextVar = this.getContextVar(
				"fechaUltimaCargaAnioContextVar", "",
				"fechaUltimaCargaAnioContextVar");

		importeDecimalUltimaCargaContextVar = this.getContextVar(
				"importeDecimalUltimaCargaContextVar", "",
				"importeDecimalUltimaCargaContextVar");

		scapeDigitContextVar = this.getContextVar("scapeDigitContextVar", "",
				"scapeDigitContextVar");
	}

	@Override
	protected void setInitialStep() {
		this.ctx.setInitialStep(obtieneTarjeta.GetId());
	}

	@Override
	protected String getClassNameChild() {
		return this.getClass().getName();
	}
}
