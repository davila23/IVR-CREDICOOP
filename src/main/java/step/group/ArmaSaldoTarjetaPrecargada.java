package step.group;

import ivr.CallContext;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.fastagi.AgiChannel;

import condition.condition;
import context.ContextVar;

import step.Step;
import step.StepConditional;
import step.StepCounter;
import step.StepFactory;
import step.StepGenerateKeyFromDni;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayMonth;
import step.StepSayNumber;
import step.StepSayNumber;
import step.StepValidateDni;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class ArmaSaldoTarjetaPrecargada implements StepGroup {

	protected StepGroupType GroupType;

	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private ContextVar scapeDigitContextVar;
	private StepConditional evalImportePrimerCompra;
	private StepConditional evalImporteSegundaCompra;
	private StepConditional evalImporteTercerCompra;
	private StepConditional evalImporteCuartaCompra;
	private StepConditional evalImporteQuintaCompra;
	private StepConditional evalImporteSextaCompra;
	private ContextVar retornoMsgJPOS;
	private ContextVar importeCompraContextVar_1;
	private ContextVar fechaCompraDiaContextVar_1;
	private ContextVar fechaCompraMesContextVar_1;
	private ContextVar importeCompraContextVar_2;
	private ContextVar fechaCompraDiaContextVar_2;
	private ContextVar fechaCompraMesContextVar_2;
	private ContextVar importeCompraContextVar_3;
	private ContextVar fechaCompraDiaContextVar_3;
	private ContextVar fechaCompraMesContextVar_3;
	private ContextVar importeCompraContextVar_4;
	private ContextVar fechaCompraDiaContextVar_4;
	private ContextVar fechaCompraMesContextVar_4;
	private ContextVar importeCompraContextVar_5;
	private ContextVar fechaCompraDiaContextVar_5;
	private ContextVar fechaCompraMesContextVar_5;
	private ContextVar importeCompraContextVar_6;
	private ContextVar fechaCompraDiaContextVar_6;
	private ContextVar fechaCompraMesContextVar_6;
	private StepPlay stepAudioImportePrimerCompra;
	private StepPlay stepAudioImporteSegundaCompra;
	private StepPlay stepAudioImporteTercerCompra;
	private StepPlay stepAudioImporteCuartaCompra;
	private StepPlay stepAudioImporteQuintaCompra;
	private StepPlay stepAudioImporteSextaCompra;
	private StepPlay stepAudioFechaPrimerCompra;
	private StepPlay stepAudioFechaSegundaCompra;
	private StepPlay stepAudioFechaTercerCompra;
	private StepPlay stepAudioFechaCuartaCompra;
	private StepPlay stepAudioFechaQuintaCompra;
	private StepPlay stepAudioFechaSextaCompra;
	private StepSayNumber stepNumberImportePrimerCompra;
	private StepSayNumber stepNumberImporteSegundaCompra;
	private StepSayNumber stepNumberImporteTercerCompra;
	private StepSayNumber stepNumberImporteCuartaCompra;
	private StepSayNumber stepNumberImporteQuintaCompra;
	private StepSayNumber stepNumberImporteSextaCompra;
	private StepConditional evalSD_PrimerCompra;
	private StepConditional evalSD_SegundaCompra;
	private StepConditional evalSD_TercerCompra;
	private StepConditional evalSD_CuartaCompra;
	private StepConditional evalSD_QuintaCompra;
	private StepConditional evalSD_SextaCompra;
	private StepSayNumber stepDayFechaPrimerCompra;
	private StepSayNumber stepDayFechaSegundaCompra;
	private StepSayNumber stepDayFechaTercerCompra;
	private StepSayNumber stepDayFechaCuartaCompra;
	private StepSayNumber stepDayFechaQuintaCompra;
	private StepSayNumber stepDayFechaSextaCompra;
	private StepSayNumber stepMonthFechaPrimerCompra;
	private StepSayNumber stepMonthFechaSegundaCompra;
	private StepSayNumber stepMonthFechaTercerCompra;
	private StepSayNumber stepMonthFechaCuartaCompra;
	private StepSayNumber stepMonthFechaQuintaCompra;
	private StepSayNumber stepMonthFechaSextaCompra;
	private StepPlay stepPlayConectorFechaPrimerCompra;
	private StepPlay stepPlayConectorFechaSegundaCompra;
	private StepPlay stepPlayConectorFechaTercerCompra;
	private StepPlay stepPlayConectorFechaCuartaCompra;
	private StepPlay stepPlayConectorFechaQuintaCompra;
	private StepPlay stepPlayConectorFechaSextaCompra;
	private StepConditional evalDisponibleCompras;
	private StepConditional evalUltimaCarga;
	private StepPlay stepAudioDisponibleCompras;
	private StepConditional evalSD_DisponibleCompras;
	private StepSayNumber stepNumberDisponibleCompras;
	private StepPlay stepAudioUltimaCarga;
	private StepConditional evalSD_UltimaCarga;
	private StepSayNumber stepNumberRetiros;
	private StepPlay stepAudioFechaUltimaCarga;
	private StepSayNumber stepDayRetiros;
	private StepPlay stepPlayConectorUltimaCarga;
	private StepSayNumber stepMonthFechaRetiros;

	private ContextVar disponibleComprasContextVar;

	private ContextVar disponibleRetirosContextVar;

	private ContextVar fechaRetirosDiaContextVar;

	private ContextVar fechaCompraMesUltimaCargaContextVar;

	private ContextVar disponibleUltimaCargaContextVar;

	private ContextVar fechaMesRetirosContextVar;

	private void setSequence() {

		/* Disponible Compras */

		evalDisponibleCompras.addCondition(new condition(1, "#{"
				+ disponibleComprasContextVar.getVarName() + "} == " + 0,
				evalUltimaCarga.GetId(), stepAudioDisponibleCompras.GetId()));

		stepAudioDisponibleCompras
				.setNextstep(evalSD_DisponibleCompras.GetId());

		evalSD_DisponibleCompras.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberDisponibleCompras.GetId(), stepIfTrueUUID));

		stepNumberDisponibleCompras.setNextstep(stepAudioFechaPrimerCompra
				.GetId());

		/* Ultima Carga */

		evalUltimaCarga.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_1.getVarName() + "} == " + 0,
				evalImportePrimerCompra.GetId(), stepAudioUltimaCarga.GetId()));

		stepAudioUltimaCarga.setNextstep(evalSD_UltimaCarga.GetId());

		evalSD_UltimaCarga.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberRetiros.GetId(), stepIfTrueUUID));

		stepNumberRetiros.setNextstep(stepAudioFechaUltimaCarga.GetId());

		stepAudioFechaUltimaCarga.setNextstep(stepDayRetiros.GetId());

		stepDayRetiros.setNextstep(stepPlayConectorUltimaCarga.GetId());

		stepPlayConectorUltimaCarga.setNextstep(stepMonthFechaRetiros
				.GetId());

		stepMonthFechaRetiros.setNextstep(evalImportePrimerCompra.GetId());

		/* Primer Compra */

		evalImportePrimerCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_1.getVarName() + "} == " + 0,
				evalImporteSegundaCompra.GetId(), stepAudioImportePrimerCompra
						.GetId()));

		stepAudioImportePrimerCompra.setNextstep(evalSD_PrimerCompra.GetId());

		evalSD_PrimerCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImportePrimerCompra.GetId(), stepIfTrueUUID));

		stepNumberImportePrimerCompra.setNextstep(stepAudioFechaPrimerCompra
				.GetId());

		stepAudioFechaPrimerCompra
				.setNextstep(stepDayFechaPrimerCompra.GetId());

		stepDayFechaPrimerCompra.setNextstep(stepPlayConectorFechaPrimerCompra
				.GetId());

		stepPlayConectorFechaPrimerCompra
				.setNextstep(stepMonthFechaPrimerCompra.GetId());

		stepMonthFechaPrimerCompra.setNextstep(evalImportePrimerCompra.GetId());

		/* Segunda Compra */

		evalImportePrimerCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_2.getVarName() + "} == " + 0,
				evalImporteTercerCompra.GetId(), stepAudioImportePrimerCompra
						.GetId()));

		stepAudioImportePrimerCompra.setNextstep(evalSD_PrimerCompra.GetId());

		evalSD_PrimerCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImportePrimerCompra.GetId(), stepIfTrueUUID));

		stepNumberImportePrimerCompra.setNextstep(stepAudioFechaPrimerCompra
				.GetId());

		stepAudioFechaPrimerCompra
				.setNextstep(stepDayFechaPrimerCompra.GetId());

		stepDayFechaPrimerCompra.setNextstep(stepPlayConectorFechaSegundaCompra
				.GetId());

		stepPlayConectorFechaSegundaCompra
				.setNextstep(stepMonthFechaPrimerCompra.GetId());

		stepMonthFechaPrimerCompra.setNextstep(evalImportePrimerCompra.GetId());

		/* Tercer Compra */

		evalImportePrimerCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_3.getVarName() + "} == " + 0,
				evalImporteCuartaCompra.GetId(), stepAudioImportePrimerCompra
						.GetId()));

		stepAudioImportePrimerCompra.setNextstep(evalSD_PrimerCompra.GetId());

		evalSD_PrimerCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImportePrimerCompra.GetId(), stepIfTrueUUID));

		stepNumberImporteTercerCompra.setNextstep(stepAudioFechaTercerCompra
				.GetId());

		stepAudioFechaTercerCompra
				.setNextstep(stepDayFechaTercerCompra.GetId());

		stepDayFechaTercerCompra.setNextstep(stepPlayConectorFechaTercerCompra
				.GetId());

		stepPlayConectorFechaTercerCompra
				.setNextstep(stepMonthFechaTercerCompra.GetId());

		stepMonthFechaTercerCompra.setNextstep(evalImportePrimerCompra.GetId());

		/* Cuarta compra */

		evalImportePrimerCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_4.getVarName() + "} == " + 0,
				evalImporteQuintaCompra.GetId(), stepAudioImporteCuartaCompra
						.GetId()));

		stepAudioImporteCuartaCompra.setNextstep(evalSD_CuartaCompra.GetId());

		evalSD_CuartaCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImporteCuartaCompra.GetId(), stepIfTrueUUID));

		stepNumberImporteCuartaCompra.setNextstep(stepAudioFechaCuartaCompra
				.GetId());

		stepAudioFechaCuartaCompra
				.setNextstep(stepDayFechaCuartaCompra.GetId());

		stepDayFechaCuartaCompra.setNextstep(stepPlayConectorFechaCuartaCompra
				.GetId());

		stepPlayConectorFechaCuartaCompra
				.setNextstep(stepMonthFechaCuartaCompra.GetId());

		stepMonthFechaCuartaCompra.setNextstep(evalImporteQuintaCompra.GetId());

		/* Quinta Compra */

		evalImporteQuintaCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_5.getVarName() + "} == " + 0,
				evalImporteSextaCompra.GetId(), stepAudioImporteQuintaCompra
						.GetId()));

		stepAudioImporteQuintaCompra.setNextstep(evalSD_QuintaCompra.GetId());

		evalSD_QuintaCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImporteQuintaCompra.GetId(), stepIfTrueUUID));

		stepNumberImporteQuintaCompra.setNextstep(stepAudioFechaQuintaCompra
				.GetId());

		stepAudioFechaQuintaCompra
				.setNextstep(stepDayFechaQuintaCompra.GetId());

		stepDayFechaQuintaCompra.setNextstep(stepPlayConectorFechaQuintaCompra
				.GetId());

		stepPlayConectorFechaQuintaCompra
				.setNextstep(stepMonthFechaQuintaCompra.GetId());

		stepMonthFechaQuintaCompra.setNextstep(evalImporteSextaCompra.GetId());

		/* Sexta Compra */

		evalImporteSextaCompra.addCondition(new condition(1, "#{"
				+ importeCompraContextVar_6.getVarName() + "} == " + 0,
				evalImporteSegundaCompra.GetId(), stepAudioImporteSextaCompra
						.GetId()));

		stepAudioImporteSextaCompra.setNextstep(evalSD_SextaCompra.GetId());

		evalSD_SextaCompra.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepNumberImporteSextaCompra.GetId(), stepIfTrueUUID));

		stepNumberImporteSextaCompra.setNextstep(stepAudioFechaSextaCompra
				.GetId());

		stepAudioFechaSextaCompra.setNextstep(stepDayFechaSextaCompra.GetId());

		stepDayFechaSextaCompra.setNextstep(stepPlayConectorFechaSextaCompra
				.GetId());

		stepPlayConectorFechaSextaCompra.setNextstep(stepMonthFechaSextaCompra
				.GetId());

		stepMonthFechaSextaCompra
				.setNextstep(stepMonthFechaSextaCompra.GetId());

		/*
		 * Disponible Compras
		 * 
		 * 
		 * stepIncluyeCompras.setNextstep(stepIfTrueUUID);
		 */

	}

	public ArmaSaldoTarjetaPrecargada() {
		super();
		GroupType = StepGroupType.armaSaldoTarjetaPrecargada;

		/* Play */

		stepAudioDisponibleCompras = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisponibleCompras
				.setStepDescription("PLAY => DISPONIBLE COMPRAS");
		stepAudioDisponibleCompras.setScapeDigit("0123456789*#");
		stepAudioDisponibleCompras.setPlayfile("coto/cen");
		Steps.put(stepAudioDisponibleCompras.GetId(),
				stepAudioDisponibleCompras);

		stepAudioUltimaCarga = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioUltimaCarga.setStepDescription("PLAY => ULTIMA CARGA");
		stepAudioUltimaCarga.setScapeDigit("0123456789*#");
		stepAudioUltimaCarga.setPlayfile("coto/cen");
		Steps.put(stepAudioUltimaCarga.GetId(), stepAudioUltimaCarga);

		stepAudioImportePrimerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImportePrimerCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 1");
		stepAudioImportePrimerCompra.setScapeDigit("0123456789*#");
		stepAudioImportePrimerCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImportePrimerCompra.GetId(),
				stepAudioImportePrimerCompra);

		stepAudioImporteSegundaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImporteSegundaCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 2");
		stepAudioImporteSegundaCompra.setScapeDigit("0123456789*#");
		stepAudioImporteSegundaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImporteSegundaCompra.GetId(),
				stepAudioImporteSegundaCompra);

		stepAudioImporteTercerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImporteTercerCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 3");
		stepAudioImporteTercerCompra.setScapeDigit("0123456789*#");
		stepAudioImporteTercerCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImporteTercerCompra.GetId(),
				stepAudioImporteTercerCompra);

		stepAudioImporteCuartaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImporteCuartaCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 4");
		stepAudioImporteCuartaCompra.setScapeDigit("0123456789*#");
		stepAudioImporteCuartaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImporteCuartaCompra.GetId(),
				stepAudioImporteCuartaCompra);

		stepAudioImporteQuintaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImporteQuintaCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 5");
		stepAudioImporteQuintaCompra.setScapeDigit("0123456789*#");
		stepAudioImporteQuintaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImporteQuintaCompra.GetId(),
				stepAudioImporteQuintaCompra);

		stepAudioImporteSextaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioImporteSextaCompra
				.setStepDescription("PLAY => IMPORTE COMPRA NRO : 6");
		stepAudioImporteSextaCompra.setScapeDigit("0123456789*#");
		stepAudioImporteSextaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioImporteSextaCompra.GetId(),
				stepAudioImporteSextaCompra);

		// CONECTOR

		stepPlayConectorUltimaCarga = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorUltimaCarga
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 1");
		stepPlayConectorUltimaCarga.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorUltimaCarga.GetId(),
				stepPlayConectorUltimaCarga);

		stepPlayConectorFechaPrimerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaPrimerCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 1");
		stepPlayConectorFechaPrimerCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaPrimerCompra.GetId(),
				stepPlayConectorFechaPrimerCompra);

		stepPlayConectorFechaSegundaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaSegundaCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 2");
		stepPlayConectorFechaSegundaCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaSegundaCompra.GetId(),
				stepPlayConectorFechaSegundaCompra);

		stepPlayConectorFechaTercerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaTercerCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 3");
		stepPlayConectorFechaTercerCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaTercerCompra.GetId(),
				stepPlayConectorFechaTercerCompra);

		stepPlayConectorFechaCuartaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaCuartaCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 4");
		stepPlayConectorFechaCuartaCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaCuartaCompra.GetId(),
				stepPlayConectorFechaCuartaCompra);

		stepPlayConectorFechaQuintaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaQuintaCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 5");
		stepPlayConectorFechaQuintaCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaQuintaCompra.GetId(),
				stepPlayConectorFechaQuintaCompra);

		stepPlayConectorFechaSextaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepPlayConectorFechaSextaCompra
				.setStepDescription("PLAY => CONECTOR FECHA COMPRA NRO : 6");
		stepPlayConectorFechaSextaCompra.setPlayfile("coto/cen");
		Steps.put(stepPlayConectorFechaSextaCompra.GetId(),
				stepPlayConectorFechaSextaCompra);

		// fechas

		stepAudioFechaUltimaCarga = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaUltimaCarga
				.setStepDescription("PLAY => FECHA ULTIMA CARGA");
		stepAudioFechaUltimaCarga.setScapeDigit("0123456789*#");
		stepAudioFechaUltimaCarga.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaUltimaCarga.GetId(), stepAudioFechaUltimaCarga);

		stepAudioFechaPrimerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaPrimerCompra
				.setStepDescription("PLAY => FECHA COMPRA NRO : 1");
		stepAudioFechaPrimerCompra.setScapeDigit("0123456789*#");
		stepAudioFechaPrimerCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaPrimerCompra.GetId(),
				stepAudioFechaPrimerCompra);

		stepAudioFechaSegundaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaSegundaCompra
				.setStepDescription("PLAY => FECHA COMPRA NRO : 2");
		stepAudioFechaSegundaCompra.setScapeDigit("0123456789*#");
		stepAudioFechaSegundaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaSegundaCompra.GetId(),
				stepAudioFechaSegundaCompra);

		stepAudioFechaTercerCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaTercerCompra
				.setStepDescription("PLAY => FECHA COMPRA NRO : 3");
		stepAudioFechaTercerCompra.setScapeDigit("0123456789*#");
		stepAudioFechaTercerCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaTercerCompra.GetId(),
				stepAudioFechaTercerCompra);

		stepAudioFechaCuartaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaCuartaCompra
				.setStepDescription("PLAY => FECHA COMPRA NRO : 4");
		stepAudioFechaCuartaCompra.setScapeDigit("0123456789*#");
		stepAudioFechaCuartaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaCuartaCompra.GetId(),
				stepAudioFechaCuartaCompra);

		stepAudioFechaQuintaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaQuintaCompra
				.setStepDescription("PLAY =>  FECHA COMPRA NRO : 5");
		stepAudioFechaQuintaCompra.setScapeDigit("0123456789*#");
		stepAudioFechaQuintaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaQuintaCompra.GetId(),
				stepAudioFechaQuintaCompra);

		stepAudioFechaSextaCompra = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaSextaCompra
				.setStepDescription("PLAY => FECHA COMPRA NRO : 6");
		stepAudioFechaSextaCompra.setScapeDigit("0123456789*#");
		stepAudioFechaSextaCompra.setPlayfile("coto/cen");
		Steps.put(stepAudioFechaSextaCompra.GetId(), stepAudioFechaSextaCompra);

		/* Say Number */

		stepNumberDisponibleCompras = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberDisponibleCompras
				.setStepDescription("SAYNUMBER => DISPONIBLE COMPRAS");
		Steps.put(stepNumberDisponibleCompras.GetId(),
				stepNumberDisponibleCompras);

		stepNumberRetiros = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberRetiros.setStepDescription("SAYNUMBER => ULTIMA CARGA");
		Steps.put(stepNumberRetiros.GetId(), stepNumberRetiros);

		stepNumberImportePrimerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberImportePrimerCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 1");
		Steps.put(stepNumberImportePrimerCompra.GetId(),
				stepNumberImportePrimerCompra);

		stepNumberImporteSegundaCompra = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberImporteSegundaCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 2");
		Steps.put(stepNumberImporteSegundaCompra.GetId(),
				stepNumberImporteSegundaCompra);

		stepNumberImporteTercerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberImporteTercerCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 3");
		Steps.put(stepNumberImporteTercerCompra.GetId(),
				stepNumberImporteTercerCompra);

		stepNumberImporteCuartaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberImporteCuartaCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 4");
		Steps.put(stepNumberImporteCuartaCompra.GetId(),
				stepNumberImporteCuartaCompra);

		stepNumberImporteQuintaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberImporteQuintaCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 5");
		Steps.put(stepNumberImporteQuintaCompra.GetId(),
				stepNumberImporteQuintaCompra);

		stepNumberImporteSextaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberImporteSextaCompra
				.setStepDescription("SAYNUMBER => IMPORTE COMPRA NRO : 6");
		Steps.put(stepNumberImporteSextaCompra.GetId(),
				stepNumberImporteSextaCompra);

		// FECHA

		stepDayRetiros = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayRetiros.setStepDescription("SAYNUMBER => ULTIMA CARGA");
		Steps.put(stepDayRetiros.GetId(), stepDayRetiros);

		stepDayFechaPrimerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaPrimerCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 1");
		Steps.put(stepDayFechaPrimerCompra.GetId(), stepDayFechaPrimerCompra);

		stepDayFechaSegundaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaSegundaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 2");
		Steps.put(stepDayFechaSegundaCompra.GetId(), stepDayFechaSegundaCompra);

		stepDayFechaTercerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaTercerCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 3");
		Steps.put(stepDayFechaTercerCompra.GetId(), stepDayFechaTercerCompra);

		stepDayFechaCuartaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaCuartaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 4");
		Steps.put(stepDayFechaCuartaCompra.GetId(), stepDayFechaCuartaCompra);

		stepDayFechaQuintaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaQuintaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 5");
		Steps.put(stepDayFechaQuintaCompra.GetId(), stepDayFechaQuintaCompra);

		stepDayFechaSextaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDayFechaSextaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 6");
		Steps.put(stepDayFechaSextaCompra.GetId(), stepDayFechaSextaCompra);

		// MES

		stepMonthFechaRetiros = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaRetiros
				.setStepDescription("SAYNUMBER => FECHA ULTIMA CARGA");
		Steps.put(stepMonthFechaRetiros.GetId(), stepMonthFechaRetiros);

		stepMonthFechaPrimerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaPrimerCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 1");
		Steps.put(stepMonthFechaPrimerCompra.GetId(),
				stepMonthFechaPrimerCompra);

		stepMonthFechaSegundaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaSegundaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 2");
		Steps.put(stepMonthFechaSegundaCompra.GetId(),
				stepMonthFechaSegundaCompra);

		stepMonthFechaTercerCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaTercerCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 3");
		Steps.put(stepMonthFechaTercerCompra.GetId(),
				stepMonthFechaTercerCompra);

		stepMonthFechaCuartaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaCuartaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 4");
		Steps.put(stepMonthFechaCuartaCompra.GetId(),
				stepMonthFechaCuartaCompra);

		stepMonthFechaQuintaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaQuintaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 5");
		Steps.put(stepMonthFechaQuintaCompra.GetId(),
				stepMonthFechaQuintaCompra);

		stepMonthFechaSextaCompra = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepMonthFechaSextaCompra
				.setStepDescription("SAYNUMBER => FECHA COMPRA NRO : 6");
		Steps.put(stepMonthFechaSextaCompra.GetId(), stepMonthFechaSextaCompra);

		/* Conditional */

		evalDisponibleCompras = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalDisponibleCompras
				.setStepDescription("CONDITIONAL => DISPONIBLE COMPRAS");
		Steps.put(evalDisponibleCompras.GetId(), evalDisponibleCompras);

		evalUltimaCarga = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalUltimaCarga.setStepDescription("CONDITIONAL => ULTIMA CARGA");
		Steps.put(evalUltimaCarga.GetId(), evalUltimaCarga);

		evalImportePrimerCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImportePrimerCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 1");
		Steps.put(evalImportePrimerCompra.GetId(), evalImportePrimerCompra);

		evalImporteSegundaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImporteSegundaCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 2");
		Steps.put(evalImporteSegundaCompra.GetId(), evalImportePrimerCompra);

		evalImporteTercerCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImporteTercerCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 3");
		Steps.put(evalImporteTercerCompra.GetId(), evalImporteTercerCompra);

		evalImporteCuartaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImporteCuartaCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 4");
		Steps.put(evalImporteCuartaCompra.GetId(), evalImporteCuartaCompra);

		evalImporteQuintaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImporteQuintaCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 5");
		Steps.put(evalImporteQuintaCompra.GetId(), evalImporteQuintaCompra);

		evalImporteSextaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalImporteSextaCompra
				.setStepDescription("CONDITIONAL => IMPORTE COMPRA NRO : 6");
		Steps.put(evalImporteSextaCompra.GetId(), evalImporteSextaCompra);

		// SD

		evalSD_DisponibleCompras = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_DisponibleCompras
				.setStepDescription("CONDITIONAL => SD DISPONIBLE COMPRAS");
		Steps.put(evalSD_DisponibleCompras.GetId(), evalSD_DisponibleCompras);

		evalSD_UltimaCarga = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_UltimaCarga.setStepDescription("CONDITIONAL => SD ULTIMA CARGA");
		Steps.put(evalSD_UltimaCarga.GetId(), evalSD_UltimaCarga);

		evalSD_PrimerCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_PrimerCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 1");
		Steps.put(evalSD_PrimerCompra.GetId(), evalSD_PrimerCompra);

		evalSD_SegundaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_SegundaCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 2");
		Steps.put(evalSD_SegundaCompra.GetId(), evalSD_SegundaCompra);

		evalSD_TercerCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_TercerCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 3");
		Steps.put(evalSD_TercerCompra.GetId(), evalSD_TercerCompra);

		evalSD_CuartaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_CuartaCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 4");
		Steps.put(evalSD_CuartaCompra.GetId(), evalSD_CuartaCompra);

		evalSD_QuintaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_QuintaCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 5");
		Steps.put(evalSD_QuintaCompra.GetId(), evalSD_QuintaCompra);

		evalSD_SextaCompra = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSD_SextaCompra
				.setStepDescription("CONDITIONAL => SD COMPRA NRO : 6");
		Steps.put(evalSD_SextaCompra.GetId(), evalSD_SextaCompra);

	}

	@Override
	public UUID getInitialStep() {
		return evalImportePrimerCompra.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		if (retornoMsgJPOS == null) {
			throw new IllegalArgumentException("Variables de Contexto Vacias");
		}
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	public void setStepIfTrue(UUID _stepIfTrueUUID) {
		stepIfTrueUUID = _stepIfTrueUUID;
	}

	public void setStepIfFalse(UUID _stepIfFalseUUID) {
		stepIfFalseUUID = _stepIfFalseUUID;
	}

	public void setRetornoMsgJPOSContextVar(ContextVar retornoMsgJPOS) {
		this.retornoMsgJPOS = retornoMsgJPOS;
	}

	public void setScapeDigitContextVar(ContextVar scapeDigitContextVar) {
		this.scapeDigitContextVar = scapeDigitContextVar;
		stepAudioImportePrimerCompra
				.setContextVariableName(scapeDigitContextVar);
		stepAudioImporteSegundaCompra
				.setContextVariableName(scapeDigitContextVar);
		stepAudioImporteTercerCompra
				.setContextVariableName(scapeDigitContextVar);
		stepAudioImporteCuartaCompra
				.setContextVariableName(scapeDigitContextVar);
		stepAudioImporteQuintaCompra
				.setContextVariableName(scapeDigitContextVar);
		stepAudioImporteCuartaCompra
				.setContextVariableName(scapeDigitContextVar);

	}

	public void setDisponibleCompraContextVar(
			ContextVar disponibleComprasContextVar) {
		this.disponibleComprasContextVar = disponibleComprasContextVar;
		stepNumberDisponibleCompras
				.setContextVariableName(disponibleComprasContextVar);
	}

	public void setDisponibleRetirosContextVar(
			ContextVar disponibleUltimaCargaContextVar) {
		this.disponibleUltimaCargaContextVar = disponibleUltimaCargaContextVar;
		stepNumberRetiros
				.setContextVariableName(disponibleUltimaCargaContextVar);
	}

	public void setImportePrimerCompraContextVar(
			ContextVar importeCompraContextVar_1) {
		this.importeCompraContextVar_1 = importeCompraContextVar_1;
		stepNumberImportePrimerCompra
				.setContextVariableName(importeCompraContextVar_1);
	}

	public void setImporteSegundoCompraContextVar(
			ContextVar importeCompraContextVar_2) {
		this.importeCompraContextVar_2 = importeCompraContextVar_2;
		stepNumberImporteSegundaCompra
				.setContextVariableName(importeCompraContextVar_2);
	}

	public void setImporteTercerCompraContextVar(
			ContextVar importeCompraContextVar_3) {
		this.importeCompraContextVar_3 = importeCompraContextVar_3;
		stepNumberImporteTercerCompra
				.setContextVariableName(importeCompraContextVar_3);
	}

	public void setImporteCuartaCompraContextVar(
			ContextVar importeCompraContextVar_4) {
		this.importeCompraContextVar_4 = importeCompraContextVar_4;
		stepNumberImporteCuartaCompra
				.setContextVariableName(importeCompraContextVar_4);
	}

	public void setImporteQuintaCompraContextVar(
			ContextVar importeCompraContextVar_5) {
		this.importeCompraContextVar_5 = importeCompraContextVar_5;
		stepNumberImporteQuintaCompra
				.setContextVariableName(importeCompraContextVar_5);
	}

	public void setImporteSextaCompraContextVar(
			ContextVar importeCompraContextVar_6) {
		this.importeCompraContextVar_6 = importeCompraContextVar_6;
		stepNumberImporteSextaCompra
				.setContextVariableName(importeCompraContextVar_6);
	}

	public void setDayRetirosContextVar(ContextVar fechaRetirosDiaContextVar) {
		this.fechaRetirosDiaContextVar = fechaRetirosDiaContextVar;
		stepDayRetiros.setContextVariableName(fechaRetirosDiaContextVar);
	}

	public void setDayPrimerCompraContextVar(
			ContextVar fechaCompraDiaContextVar_1) {
		this.fechaCompraDiaContextVar_1 = fechaCompraDiaContextVar_1;
		stepDayFechaPrimerCompra
				.setContextVariableName(fechaCompraDiaContextVar_1);
	}

	public void setDaySegundoCompraContextVar(
			ContextVar fechaCompraDiaContextVar_2) {
		this.fechaCompraDiaContextVar_2 = fechaCompraDiaContextVar_2;
		stepDayFechaSegundaCompra
				.setContextVariableName(fechaCompraDiaContextVar_2);
	}

	public void setDayTercerCompraContextVar(
			ContextVar fechaCompraDiaContextVar_3) {
		this.fechaCompraDiaContextVar_3 = fechaCompraDiaContextVar_3;
		stepDayFechaTercerCompra
				.setContextVariableName(fechaCompraDiaContextVar_3);
	}

	public void setDayCuartoCompraContextVar(
			ContextVar fechaCompraDiaContextVar_4) {
		this.fechaCompraDiaContextVar_4 = fechaCompraDiaContextVar_4;
		stepDayFechaCuartaCompra
				.setContextVariableName(fechaCompraDiaContextVar_4);
	}

	public void setDayQuintoCompraContextVar(
			ContextVar fechaCompraDiaContextVar_5) {
		this.fechaCompraDiaContextVar_5 = fechaCompraDiaContextVar_5;
		stepDayFechaQuintaCompra
				.setContextVariableName(fechaCompraDiaContextVar_5);
	}

	public void setDaySextoCompraContextVar(
			ContextVar fechaCompraDiaContextVar_6) {
		this.fechaCompraDiaContextVar_6 = fechaCompraDiaContextVar_6;
		stepDayFechaSextaCompra
				.setContextVariableName(fechaCompraDiaContextVar_6);
	}

	public void setMonthRetirosContextVar(
			ContextVar fechaMesRetirosContextVar) {
		this.fechaMesRetirosContextVar = fechaMesRetirosContextVar;
		stepMonthFechaRetiros
				.setContextVariableName(fechaMesRetirosContextVar);
	}

	public void setMonthPrimerCompraContextVar(
			ContextVar fechaCompraMesContextVar_1) {
		this.fechaCompraMesContextVar_1 = fechaCompraMesContextVar_1;
		stepMonthFechaPrimerCompra
				.setContextVariableName(fechaCompraMesContextVar_1);
	}

	public void setMonthSegundoCompraContextVar(
			ContextVar fechaCompraMesContextVar_2) {
		this.fechaCompraMesContextVar_2 = fechaCompraMesContextVar_2;
		stepMonthFechaSegundaCompra
				.setContextVariableName(fechaCompraMesContextVar_2);
	}

	public void setMonthTercerCompraContextVar(
			ContextVar fechaCompraMesContextVar_3) {
		this.fechaCompraMesContextVar_3 = fechaCompraMesContextVar_3;
		stepMonthFechaTercerCompra
				.setContextVariableName(fechaCompraMesContextVar_3);
	}

	public void setMonthCuartoCompraContextVar(
			ContextVar fechaCompraMesContextVar_4) {
		this.fechaCompraMesContextVar_4 = fechaCompraMesContextVar_4;
		stepMonthFechaCuartaCompra
				.setContextVariableName(fechaCompraMesContextVar_4);
	}

	public void setMonthQuintoCompraContextVar(
			ContextVar fechaCompraMesContextVar_5) {
		this.fechaCompraMesContextVar_5 = fechaCompraMesContextVar_5;
		stepMonthFechaQuintaCompra
				.setContextVariableName(fechaCompraMesContextVar_5);
	}

	public void setMonthSextoCompraContextVar(
			ContextVar fechaCompraMesContextVar_6) {
		this.fechaCompraMesContextVar_6 = fechaCompraMesContextVar_6;
		stepMonthFechaSextaCompra
				.setContextVariableName(fechaCompraMesContextVar_6);
	}

}
