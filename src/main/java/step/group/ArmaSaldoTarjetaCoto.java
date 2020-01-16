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

public class ArmaSaldoTarjetaCoto implements StepGroup {

	protected StepGroupType GroupType;
	private StepSayNumber stepAudSalEnUnPago;
	private StepSayNumber stepAudSalEnCuotas;
	private StepSayNumber stepAudFechaVencimientoUltResumDia;
	private StepSayMonth stepAudFechaVencimientoUltResumMes;
	private StepSayNumber stepAudFechaVencimientoUltResumAnio;
	private StepSayNumber stepAudSaldoUltResum;
	private StepSayNumber stepAudPagoMinUltResum;
	private StepSayNumber stepAudTotalPagUltResum;
	private StepSayNumber stepAudSalPendienteDeCanceUltResum;
	private StepSayNumber stepAudPagoMinPendienteDeCanceUltResum;
	private StepSayNumber stepAudCierreProxResumDia;
	private StepSayMonth stepAudCierreProxResumMes;
	private StepSayNumber stepAudCierreProxResumAnio;
	private StepSayNumber stepAudVencimientoProxResumDia;
	private StepSayMonth stepAudVencimientoProxResumMes;
	private StepSayNumber stepAudVencimientoProxResumAnio;
	private StepSayNumber stepAudSaldoCuenta;
	private StepPlay stepLocSalEnUnPago;
	private StepPlay stepLocSalEnCuotas;
	private StepPlay stepLocFechaVencimientoUltResumDia;
	// private StepPlay stepLocFechaVencimientoUltResumMes;
	private StepPlay stepLocFechaVencimientoUltResumAnio;
	private StepPlay stepLocSaldoUltResum;
	private StepPlay stepLocPagoMinUltResum;
	private StepPlay stepLocTotalPagUltResum;
	private StepPlay stepLocSalPendienteDeCanceUltResum;
	private StepPlay stepLocPagoMinPendienteDeCanceUltResum;
	private StepPlay stepLocCierreProxResumDia;
	// private StepPlay stepLocCierreProxResumMes;
	private StepPlay stepLocCierreProxResumAnio;
	private StepPlay stepLocVencimientoProxResumDia;
	// private StepPlay stepLocVencimientoProxResumMes;
	private StepPlay stepLocVencimientoProxResumAnio;
	private StepPlay stepLocSaldoCuenta;
	private ContextVar retornoMsgJPOS;
	private ContextVar salEnUnPagoContextVar;
	private ContextVar salEnCuotasContextVar;
	private ContextVar fechaVencimientoUltResumContextVarDia;
	private ContextVar fechaVencimientoUltResumContextVarMes;
	private ContextVar fechaVencimientoUltResumContextVarAnio;
	private ContextVar saldoUltResumContextVar;
	private ContextVar pagoMinUltResumContextVar;
	private ContextVar totalPagUltResumContextVar;
	private ContextVar salPendienteDeCanceUltResumContextVar;
	private ContextVar pagoMinPendienteDeCanceUltResumContextVar;
	private ContextVar cierreProxResumDiaContextVar;
	private ContextVar cierreProxResumMesContextVar;
	private ContextVar cierreProxResumAnioContextVar;
	private ContextVar vencimientoProxResumDiaContextVar;
	private ContextVar vencimientoProxResumMesContextVar;
	private ContextVar vencimientoProxResumAnioContextVar;
	private ContextVar saldoCuentaContextVar;
	private ContextVar saldoUltResumDecimalContextVar;
	private ContextVar totalPagUltResumDecimalContextVar;
	private ContextVar pagoMinUltResumDecimContextVar;
	private ContextVar salEnUnPagoDecimalContextVar;
	private ContextVar salEnCuotasDecimalContextVar;
	private ContextVar pagoMinPendienteDeCanceUltDecimalResumContextVar;
	private ContextVar saldoCuentaDecimalContextVar;
	private StepPlay stepIncluyeCompras;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private StepSayNumber stepLocSaldoParteDecimal;
	private StepSayNumber stepAudSaldoDecimalCuenta;
	private StepSayNumber stepAudPagoMinPendienteDeCanceUltResumDec;
	private StepSayNumber stepAudSalPendienteDeCanceUltResumDec;
	private StepSayNumber stepAudTotalPagUltResumDec;
	private StepSayNumber stepAudPagoMinUltResumDec;
	private StepSayNumber stepAudSaldoUltResumDec;
	private StepSayNumber stepAudSalDecEnCuotas;
	private StepSayNumber stepAudSalDecEnUnPago;
	private StepPlay stepConectorParteDecimal;
	private StepPlay stepConectorParteDecimal8;
	private StepPlay stepConectorParteDecimal7;
	private StepPlay stepConectorParteDecimal6;
	private StepPlay stepConectorParteDecimal5;
	private StepPlay stepConectorParteDecimal4;
	private StepPlay stepConectorParteDecimal1;
	private StepPlay stepConectorParteDecimal2;
	private StepPlay stepConectorParteDecimal3;
	private StepPlay stepCentavos;
	private StepPlay stepCentavos6;
	private StepPlay stepCentavos5;
	private StepPlay stepCentavos4;
	private StepPlay stepCentavos3;
	private StepPlay stepCentavos2;
	private StepPlay stepCentavos1;
	private StepPlay stepCentavos7;
	private ContextVar salPendienteDeCanceUltResumDecimalContextVar;
	private StepConditional evalSalUltimResum;
	private StepConditional evalSalTotalPagUltResum;
	private StepConditional evalSalPenCanUltResum;
	private StepConditional evalSaldoActual;
	private StepConditional evalSalEnUnPago;
	private StepConditional evalSalEnCuotas;
	private ContextVar scapeDigitContextVar;
	private StepConditional evalLocFechaVencimientoUltResumDia;
	private StepConditional evalLocSalEnCuotas;
	private StepConditional evalLocSaldoUltResum;
	private StepConditional evalLocFechaVencimientoUltResumAnio;
	private StepConditional evalLocTotalPagUltResum;
	private StepConditional evalLocPagoMinUltResum;
	private StepConditional evalLocSalPendienteDeCanceUltResum;
	private StepConditional evalLocPagoMinPendienteDeCanceUltResum;
	private StepConditional evalLocVencimientoProxResumAnio;
	private StepConditional evalLocVencimientoProxResumDia;
	private StepConditional evalLocCierreProxResumAnio;
	private StepConditional evalLocCierreProxResumDia;
	private StepConditional evalLocSalEnUnPago;
	private StepConditional evalLocSaldoCuenta;

	private void setSequence() {

		evalSalEnUnPago.addCondition(new condition(1, "#{"
				+ salEnUnPagoContextVar.getVarName() + "} == " + 0,
				evalSalEnCuotas.GetId(), stepLocSalEnUnPago.GetId()));

		stepLocSalEnUnPago.setNextstep(evalLocSalEnUnPago.GetId());

		evalLocSalEnUnPago.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudSalEnUnPago.GetId(), stepIfTrueUUID));

		stepAudSalEnUnPago.setNextstep(evalSalEnCuotas.GetId());

		evalSalEnCuotas.addCondition(new condition(1, "#{"
				+ salEnCuotasContextVar.getVarName() + "} == " + 0,
				evalSalUltimResum.GetId(), stepLocSalEnCuotas.GetId()));

		// stepConectorParteDecimal.setNextstep(stepAudSalDecEnUnPago.GetId());
		//
		// stepAudSalDecEnUnPago.setNextstep(stepLocSalEnCuotas.GetId());
		//
		// stepCentavos.setNextstep(stepLocSalEnCuotas.GetId());

		stepLocSalEnCuotas.setNextstep(evalLocSalEnCuotas.GetId());

		evalLocSalEnCuotas.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudSalEnCuotas.GetId(), stepIfTrueUUID));

		stepAudSalEnCuotas.setNextstep(evalSalUltimResum.GetId());

		// stepConectorParteDecimal1.setNextstep(stepAudSalDecEnCuotas.GetId());
		// stepAudSalDecEnCuotas.setNextstep(stepLocFechaVencimientoUltResumDia.GetId());
		//
		// stepCentavos1.setNextstep(stepLocFechaVencimientoUltResumDia.GetId());

		evalSalUltimResum.addCondition(new condition(1, "#{"
				+ saldoUltResumContextVar.getVarName() + "} == " + 0,
				evalSalTotalPagUltResum.GetId(),
				stepLocFechaVencimientoUltResumDia.GetId()));

		stepLocFechaVencimientoUltResumDia
				.setNextstep(evalLocFechaVencimientoUltResumDia.GetId());

		evalLocFechaVencimientoUltResumDia.addCondition(new condition(1,
				"trim('#{" + scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudFechaVencimientoUltResumDia.GetId(), stepIfTrueUUID));

		stepAudFechaVencimientoUltResumDia
				.setNextstep(stepAudFechaVencimientoUltResumMes.GetId());
		// stepLocFechaVencimientoUltResumMes
		// .setNextstep(stepAudFechaVencimientoUltResumMes.GetId());
		stepAudFechaVencimientoUltResumMes
				.setNextstep(stepLocFechaVencimientoUltResumAnio.GetId());
		stepLocFechaVencimientoUltResumAnio
				.setNextstep(evalLocFechaVencimientoUltResumAnio.GetId());

		evalLocFechaVencimientoUltResumAnio.addCondition(new condition(1,
				"trim('#{" + scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudFechaVencimientoUltResumAnio.GetId(), stepIfTrueUUID));

		stepAudFechaVencimientoUltResumAnio.setNextstep(stepLocSaldoUltResum
				.GetId());
		stepLocSaldoUltResum.setNextstep(evalLocSaldoUltResum.GetId());

		evalLocSaldoUltResum.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudSaldoUltResum.GetId(), stepIfTrueUUID));

		stepAudSaldoUltResum.setNextstep(stepLocPagoMinUltResum.GetId());

		// stepConectorParteDecimal2.setNextstep(stepAudSaldoUltResumDec.GetId());
		// stepAudSaldoUltResumDec.setNextstep(stepLocPagoMinUltResum.GetId());
		// stepCentavos2.setNextstep(stepLocPagoMinUltResum.GetId());
		stepLocPagoMinUltResum.setNextstep(evalLocPagoMinUltResum.GetId());

		evalLocPagoMinUltResum.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudPagoMinUltResum.GetId(), stepIfTrueUUID));

		stepAudPagoMinUltResum.setNextstep(evalSalTotalPagUltResum.GetId());

		evalSalTotalPagUltResum
				.addCondition(new condition(1,
						"#{" + totalPagUltResumContextVar.getVarName()
								+ "} == " + 0, evalSalPenCanUltResum.GetId(),
						stepLocTotalPagUltResum.GetId()));

		// stepConectorParteDecimal3
		// .setNextstep(stepAudPagoMinUltResumDec.GetId());
		// stepAudPagoMinUltResumDec.setNextstep(stepLocTotalPagUltResum.GetId());
		// stepCentavos3.setNextstep(stepLocTotalPagUltResum.GetId());
		stepLocTotalPagUltResum.setNextstep(evalLocTotalPagUltResum.GetId());

		evalLocTotalPagUltResum.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudTotalPagUltResum.GetId(), stepIfTrueUUID));

		stepAudTotalPagUltResum.setNextstep(evalSalPenCanUltResum.GetId());

		evalSalPenCanUltResum.addCondition(new condition(1, "#{"
				+ salPendienteDeCanceUltResumContextVar.getVarName() + "} < "
				+ 1, stepLocCierreProxResumDia.GetId(),
				stepLocSalPendienteDeCanceUltResum.GetId()));

		// stepConectorParteDecimal4.setNextstep(stepAudTotalPagUltResumDec
		// .GetId());
		// stepAudTotalPagUltResumDec
		// .setNextstep(stepLocSalPendienteDeCanceUltResum.GetId());
		// stepCentavos4.setNextstep(stepLocSalPendienteDeCanceUltResum.GetId());
		stepLocSalPendienteDeCanceUltResum
				.setNextstep(evalLocSalPendienteDeCanceUltResum.GetId());

		evalLocSalPendienteDeCanceUltResum.addCondition(new condition(1,
				"trim('#{" + scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudSalPendienteDeCanceUltResum.GetId(), stepIfTrueUUID));

		stepAudSalPendienteDeCanceUltResum
				.setNextstep(stepLocPagoMinPendienteDeCanceUltResum.GetId());
		// stepConectorParteDecimal5
		// .setNextstep(stepAudSalPendienteDeCanceUltResumDec.GetId());
		//
		// stepAudSalPendienteDeCanceUltResumDec.setNextstep(stepLocPagoMinPendienteDeCanceUltResum.GetId());
		// stepCentavos5.setNextstep(stepLocPagoMinPendienteDeCanceUltResum.GetId());
		stepLocPagoMinPendienteDeCanceUltResum
				.setNextstep(evalLocPagoMinPendienteDeCanceUltResum.GetId());

		evalLocPagoMinPendienteDeCanceUltResum
				.addCondition(new condition(1, "trim('#{"
						+ scapeDigitContextVar.getVarName() + "}') == ''",
						stepAudPagoMinPendienteDeCanceUltResum.GetId(),
						stepIfTrueUUID));

		stepAudPagoMinPendienteDeCanceUltResum
				.setNextstep(stepLocCierreProxResumDia.GetId());
		//
		// stepConectorParteDecimal7
		// .setNextstep(stepAudPagoMinPendienteDeCanceUltResumDec.GetId());
		//
		// stepAudPagoMinPendienteDeCanceUltResumDec
		// .setNextstep(stepLocCierreProxResumDia.GetId());
		// stepCentavos7.setNextstep(stepLocCierreProxResumDia.GetId());
		stepLocCierreProxResumDia
				.setNextstep(evalLocCierreProxResumDia.GetId());

		evalLocCierreProxResumDia.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudCierreProxResumDia.GetId(), stepIfTrueUUID));

		stepAudCierreProxResumDia
				.setNextstep(stepAudCierreProxResumMes.GetId());
		// stepLocCierreProxResumMes
		// .setNextstep(stepAudCierreProxResumMes.GetId());
		stepAudCierreProxResumMes.setNextstep(stepLocCierreProxResumAnio
				.GetId());
		stepLocCierreProxResumAnio.setNextstep(evalLocCierreProxResumAnio
				.GetId());

		evalLocCierreProxResumAnio.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudCierreProxResumAnio.GetId(), stepIfTrueUUID));

		stepAudCierreProxResumAnio.setNextstep(stepLocVencimientoProxResumDia
				.GetId());
		stepLocVencimientoProxResumDia
				.setNextstep(evalLocVencimientoProxResumDia.GetId());

		evalLocVencimientoProxResumDia.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudVencimientoProxResumDia.GetId(), stepIfTrueUUID));

		stepAudVencimientoProxResumDia
				.setNextstep(stepAudVencimientoProxResumMes.GetId());
		// stepLocVencimientoProxResumMes
		// .setNextstep(stepAudVencimientoProxResumMes.GetId());
		stepAudVencimientoProxResumMes
				.setNextstep(stepLocVencimientoProxResumAnio.GetId());
		stepLocVencimientoProxResumAnio
				.setNextstep(evalLocVencimientoProxResumAnio.GetId());

		evalLocVencimientoProxResumAnio.addCondition(new condition(1,
				"trim('#{" + scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudVencimientoProxResumAnio.GetId(), stepIfTrueUUID));

		stepAudVencimientoProxResumAnio.setNextstep(evalSaldoActual.GetId());

		evalSaldoActual.addCondition(new condition(1, "#{"
				+ saldoCuentaContextVar.getVarName() + "} == " + 0,
				stepIfTrueUUID, stepLocSaldoCuenta.GetId()));
		stepLocSaldoCuenta.setNextstep(evalLocSaldoCuenta.GetId());

		evalLocSaldoCuenta.addCondition(new condition(1, "trim('#{"
				+ scapeDigitContextVar.getVarName() + "}') == ''",
				stepAudSaldoCuenta.GetId(), stepIfTrueUUID));

		stepAudSaldoCuenta.setNextstep(stepIncluyeCompras.GetId());
		//
		// stepConectorParteDecimal6
		// .setNextstep(stepAudSaldoDecimalCuenta.GetId());
		//
		// stepAudSaldoDecimalCuenta.setNextstep(stepIncluyeCompras.GetId());
		// stepCentavos6.setNextstep(stepIncluyeCompras.GetId());
		stepIncluyeCompras.setNextstep(stepIfTrueUUID);

	}

	public ArmaSaldoTarjetaCoto() {
		super();
		GroupType = StepGroupType.armaSaldoTarjetaCOTO;

		stepCentavos = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos.setStepDescription("PLAY => CENTAVOS");
		stepCentavos.setPlayfile("coto/cen");
		Steps.put(stepCentavos.GetId(), stepCentavos);

		stepCentavos1 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos1.setStepDescription("PLAY => CENTAVOS");
		stepCentavos1.setPlayfile("coto/cen");
		Steps.put(stepCentavos1.GetId(), stepCentavos1);

		stepCentavos2 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos2.setStepDescription("PLAY => CENTAVOS");
		stepCentavos2.setPlayfile("coto/cen");
		Steps.put(stepCentavos2.GetId(), stepCentavos2);

		stepCentavos3 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos3.setStepDescription("PLAY => CENTAVOS");
		stepCentavos3.setPlayfile("coto/cen");
		Steps.put(stepCentavos3.GetId(), stepCentavos3);

		stepCentavos4 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos4.setStepDescription("PLAY => CENTAVOS");
		stepCentavos4.setPlayfile("coto/cen");
		Steps.put(stepCentavos4.GetId(), stepCentavos4);

		stepCentavos5 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos5.setStepDescription("PLAY => CENTAVOS");
		stepCentavos5.setPlayfile("coto/cen");
		Steps.put(stepCentavos5.GetId(), stepCentavos5);

		stepCentavos6 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos6.setStepDescription("PLAY => CENTAVOS");
		stepCentavos6.setPlayfile("coto/cen");
		Steps.put(stepCentavos6.GetId(), stepCentavos6);

		stepCentavos7 = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepCentavos7.setStepDescription("PLAY => CENTAVOS");
		stepCentavos7.setPlayfile("coto/cen");
		Steps.put(stepCentavos7.GetId(), stepCentavos7);

		stepConectorParteDecimal8 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal8.setStepDescription("PLAY => CON");
		stepConectorParteDecimal8.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal8.GetId(), stepConectorParteDecimal8);

		stepConectorParteDecimal7 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal7.setStepDescription("PLAY => CON");
		stepConectorParteDecimal7.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal7.GetId(), stepConectorParteDecimal7);

		stepConectorParteDecimal6 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal6.setStepDescription("PLAY => CON");
		stepConectorParteDecimal6.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal6.GetId(), stepConectorParteDecimal6);

		stepConectorParteDecimal5 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal5.setStepDescription("PLAY => CON");
		stepConectorParteDecimal5.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal5.GetId(), stepConectorParteDecimal5);

		stepConectorParteDecimal4 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal4.setStepDescription("PLAY => CON");
		stepConectorParteDecimal4.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal4.GetId(), stepConectorParteDecimal4);

		stepConectorParteDecimal3 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal3.setStepDescription("PLAY => CON");
		stepConectorParteDecimal3.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal3.GetId(), stepConectorParteDecimal3);

		stepConectorParteDecimal2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal2.setStepDescription("PLAY => CON");
		stepConectorParteDecimal2.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal2.GetId(), stepConectorParteDecimal2);

		stepConectorParteDecimal1 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal1.setStepDescription("PLAY => CON");
		stepConectorParteDecimal1.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal1.GetId(), stepConectorParteDecimal1);

		stepConectorParteDecimal = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepConectorParteDecimal.setStepDescription("PLAY => CON");
		stepConectorParteDecimal.setPlayfile("coto/CON");
		Steps.put(stepConectorParteDecimal.GetId(), stepConectorParteDecimal);

		stepLocSalEnUnPago = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepLocSalEnUnPago.setStepDescription("PLAY => SALDO EN UN PAGO");
		stepLocSalEnUnPago.setPlayfile("coto/A000014");
		stepLocSalEnUnPago.setScapeDigit("0123456789*#");
		stepLocSalEnUnPago.setContextVariableName(scapeDigitContextVar);
		Steps.put(stepLocSalEnUnPago.GetId(), stepLocSalEnUnPago);

		stepLocSalEnCuotas = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepLocSalEnCuotas.setStepDescription("PLAY => SALDO EN CUOTAS");
		stepLocSalEnCuotas.setScapeDigit("0123456789*#");
		stepLocSalEnCuotas.setPlayfile("coto/A000015");
		Steps.put(stepLocSalEnCuotas.GetId(), stepLocSalEnCuotas);

		stepLocFechaVencimientoUltResumAnio = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepLocFechaVencimientoUltResumAnio
				.setStepDescription("PLAY => FECHA DE VENCIMIENTO ULT RESUMEN. ANIO");
		stepLocFechaVencimientoUltResumAnio.setPlayfile("coto/ANIO");
		stepLocFechaVencimientoUltResumAnio.setScapeDigit("0123456789*#");
		Steps.put(stepLocFechaVencimientoUltResumAnio.GetId(),
				stepLocFechaVencimientoUltResumAnio);

		// stepLocFechaVencimientoUltResumMes = (StepPlay)
		// StepFactory.createStep(
		// StepType.Play, UUID.randomUUID());
		// stepLocFechaVencimientoUltResumMes
		// .setStepDescription("Audio fecha de vencimiento ult resum Mes");
		// stepLocFechaVencimientoUltResumMes.setPlayfile("RUTINAPIN/RUTINA_PIN008");
		// Steps.put(stepLocFechaVencimientoUltResumMes.GetId(),
		// stepLocFechaVencimientoUltResumMes);

		stepLocFechaVencimientoUltResumDia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocFechaVencimientoUltResumDia
				.setStepDescription("PLAY => FECHA DE VENCIMIENTO ULT RESUMEN. DIA");
		stepLocFechaVencimientoUltResumDia.setPlayfile("coto/A000016");
		stepLocFechaVencimientoUltResumDia.setScapeDigit("0123456789*#");
		Steps.put(stepLocFechaVencimientoUltResumDia.GetId(),
				stepLocFechaVencimientoUltResumDia);

		stepLocSaldoUltResum = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepLocSaldoUltResum.setStepDescription("PLAY => SALDO ULTIMO RESUMEN");
		stepLocSaldoUltResum.setScapeDigit("0123456789*#");
		stepLocSaldoUltResum.setPlayfile("coto/A000017");
		Steps.put(stepLocSaldoUltResum.GetId(), stepLocSaldoUltResum);

		stepLocSaldoParteDecimal = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepLocSaldoParteDecimal
				.setStepDescription("SAYNUMBER => SALDO ULTIMO RESUMEN, PARTE DECIMAL");
		Steps.put(stepLocSaldoParteDecimal.GetId(), stepLocSaldoParteDecimal);

		stepLocPagoMinUltResum = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocPagoMinUltResum
				.setStepDescription("PLAY => PAGO MINIMO ULTIMO RESUMEN");
		stepLocPagoMinUltResum.setPlayfile("coto/A000018");
		stepLocPagoMinUltResum.setScapeDigit("0123456789*#");
		Steps.put(stepLocPagoMinUltResum.GetId(), stepLocPagoMinUltResum);

		stepLocTotalPagUltResum = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocTotalPagUltResum
				.setStepDescription("PLAY => PAGO TOTAL ULTIMO RESUMEN");
		stepLocTotalPagUltResum.setScapeDigit("0123456789*#");
		stepLocTotalPagUltResum.setPlayfile("coto/A000019");
		Steps.put(stepLocTotalPagUltResum.GetId(), stepLocTotalPagUltResum);

		stepLocSalPendienteDeCanceUltResum = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocSalPendienteDeCanceUltResum
				.setStepDescription("PLAY => SALDO PENDIENDE DE CANCELACION ULTIMO RESUMEN");
		stepLocSalPendienteDeCanceUltResum.setScapeDigit("0123456789*#");
		stepLocSalPendienteDeCanceUltResum.setPlayfile("coto/A000020");
		Steps.put(stepLocSalPendienteDeCanceUltResum.GetId(),
				stepLocSalPendienteDeCanceUltResum);

		stepLocPagoMinPendienteDeCanceUltResum = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepLocPagoMinPendienteDeCanceUltResum
				.setStepDescription("PLAY => PAGO MINIMO PENDIENDE DE CANCELACION ULTIMO RESUMEN");
		stepLocPagoMinPendienteDeCanceUltResum.setScapeDigit("0123456789*#");
		stepLocPagoMinPendienteDeCanceUltResum.setPlayfile("coto/A000021");
		Steps.put(stepLocPagoMinPendienteDeCanceUltResum.GetId(),
				stepLocPagoMinPendienteDeCanceUltResum);

		stepLocCierreProxResumDia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocCierreProxResumDia
				.setStepDescription("PLAY => CIERRE PROXIMO RESUMEN. DIA");
		stepLocCierreProxResumDia.setScapeDigit("0123456789*#");
		stepLocCierreProxResumDia.setPlayfile("coto/A000022");
		Steps.put(stepLocCierreProxResumDia.GetId(), stepLocCierreProxResumDia);

		// stepLocCierreProxResumMes = (StepPlay) StepFactory.createStep(
		// StepType.Play, UUID.randomUUID());
		// stepLocCierreProxResumMes
		// .setStepDescription("Audio cierre prox Resum Mes");
		// stepLocCierreProxResumMes.setPlayfile("RUTINAPIN/RUTINA_PIN008");
		// Steps.put(stepLocCierreProxResumMes.GetId(),
		// stepLocCierreProxResumMes);

		stepLocCierreProxResumAnio = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocCierreProxResumAnio
				.setStepDescription("PLAY => CIERRE PROXIMO RESUMEN. ANIO");
		stepLocCierreProxResumAnio.setScapeDigit("0123456789*#");
		stepLocCierreProxResumAnio.setPlayfile("coto/ANIO"); // anio
		Steps.put(stepLocCierreProxResumAnio.GetId(),
				stepLocCierreProxResumAnio);

		stepLocVencimientoProxResumDia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocVencimientoProxResumDia
				.setStepDescription("PLAY => VENCIMIENTO PROXIMO RESUMEN. DIA");
		stepLocVencimientoProxResumDia.setScapeDigit("0123456789*#");
		stepLocVencimientoProxResumDia.setPlayfile("coto/A000023");
		Steps.put(stepLocVencimientoProxResumDia.GetId(),
				stepLocVencimientoProxResumDia);

		// stepLocVencimientoProxResumMes = (StepPlay) StepFactory.createStep(
		// StepType.Play, UUID.randomUUID());
		// stepLocVencimientoProxResumMes
		// .setStepDescription("Audio venc prox Resum Dia Mes");
		// stepLocVencimientoProxResumMes.setPlayfile("RUTINAPIN/RUTINA_PIN008");
		// Steps.put(stepLocVencimientoProxResumMes.GetId(),
		// stepLocVencimientoProxResumMes);

		stepLocVencimientoProxResumAnio = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepLocVencimientoProxResumAnio
				.setStepDescription("PLAY => VENCIMIENTO PROXIMO RESUMEN. ANIO");
		stepLocVencimientoProxResumAnio.setScapeDigit("0123456789*#");
		stepLocVencimientoProxResumAnio.setPlayfile("coto/ANIO");
		Steps.put(stepLocVencimientoProxResumAnio.GetId(),
				stepLocVencimientoProxResumAnio);

		stepLocSaldoCuenta = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepLocSaldoCuenta.setStepDescription("PLAY => SALDO CUENTA");
		stepLocSaldoCuenta.setScapeDigit("0123456789*#");
		stepLocSaldoCuenta.setPlayfile("coto/A000024");
		Steps.put(stepLocSaldoCuenta.GetId(), stepLocSaldoCuenta);

		stepIncluyeCompras = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepIncluyeCompras.setStepDescription("PLAY => INCLUYE COMPRAS");
		stepIncluyeCompras.setPlayfile("coto/A000025");
		stepIncluyeCompras.setScapeDigit("0123456789*#");
		Steps.put(stepIncluyeCompras.GetId(), stepIncluyeCompras);

		stepAudSaldoCuenta = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSaldoCuenta.setStepDescription("SAYNUMBER => SALDO CUENTA");
		Steps.put(stepAudSaldoCuenta.GetId(), stepAudSaldoCuenta);

		stepAudSaldoDecimalCuenta = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSaldoDecimalCuenta
				.setStepDescription("SAYNUMBER => SALDO CUENTA. PARTE DECIMAL");
		Steps.put(stepAudSaldoDecimalCuenta.GetId(), stepAudSaldoDecimalCuenta);

		stepAudVencimientoProxResumAnio = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudVencimientoProxResumAnio
				.setStepDescription("SAYNUMBER => VENCIMIENTO PROXIMO RESUMEN. ANIO");
		Steps.put(stepAudVencimientoProxResumAnio.GetId(),
				stepAudVencimientoProxResumAnio);

		stepAudVencimientoProxResumMes = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepAudVencimientoProxResumMes
				.setStepDescription("SAYNUMBER => VENCIMIENTO PROXIMO RESUMEN. MES");
		Steps.put(stepAudVencimientoProxResumMes.GetId(),
				stepAudVencimientoProxResumMes);

		stepAudVencimientoProxResumDia = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudVencimientoProxResumDia
				.setStepDescription("SAYNUMBER => VENCIMIENTO PROXIMO RESUMEN. DIA");
		Steps.put(stepAudVencimientoProxResumDia.GetId(),
				stepAudVencimientoProxResumDia);

		stepAudCierreProxResumAnio = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudCierreProxResumAnio
				.setStepDescription("SAYNUMBER => CIERRE PROXIMO RESUMEN. ANIO");
		Steps.put(stepAudCierreProxResumAnio.GetId(),
				stepAudCierreProxResumAnio);

		stepAudCierreProxResumMes = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepAudCierreProxResumMes
				.setStepDescription("SAYNUMBER => CIERRE PROXIMO RESUMEN. MES");
		Steps.put(stepAudCierreProxResumMes.GetId(), stepAudCierreProxResumMes);

		stepAudCierreProxResumDia = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudCierreProxResumDia
				.setStepDescription("SAYNUMBER => CIERRE PROXIMO RESUMEN. DIA");
		Steps.put(stepAudCierreProxResumDia.GetId(), stepAudCierreProxResumDia);

		stepAudSalEnUnPago = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSalEnUnPago.setStepDescription("SAYNUMBER => SALDO EN UN PAGO");
		Steps.put(stepAudSalEnUnPago.GetId(), stepAudSalEnUnPago);

		stepAudSalDecEnUnPago = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSalDecEnUnPago
				.setStepDescription("SAYNUMBER => SALDO EN UN PAGO. PARTE DECIMAL");
		Steps.put(stepAudSalDecEnUnPago.GetId(), stepAudSalDecEnUnPago);

		stepAudSalEnCuotas = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSalEnCuotas.setStepDescription("SAYNUMBER => SALDO EN CUOTAS");
		Steps.put(stepAudSalEnCuotas.GetId(), stepAudSalEnCuotas);

		stepAudSalDecEnCuotas = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSalDecEnCuotas
				.setStepDescription("SAYNUMBER => SALDO EN CUOTAS. PARTE DECIMAL");
		Steps.put(stepAudSalDecEnCuotas.GetId(), stepAudSalDecEnCuotas);

		stepAudFechaVencimientoUltResumAnio = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudFechaVencimientoUltResumAnio
				.setStepDescription("SAYNUMBER => VENCIMIANTO ULTIMO RESUMEN. ANIO");
		Steps.put(stepAudFechaVencimientoUltResumAnio.GetId(),
				stepAudFechaVencimientoUltResumAnio);

		stepAudFechaVencimientoUltResumMes = (StepSayMonth) StepFactory
				.createStep(StepType.SayMonth, UUID.randomUUID());
		stepAudFechaVencimientoUltResumMes
				.setStepDescription("SAYMONTH => VENCIMIANTO ULTIMO RESUMEN. MES");
		Steps.put(stepAudFechaVencimientoUltResumMes.GetId(),
				stepAudFechaVencimientoUltResumMes);

		stepAudFechaVencimientoUltResumDia = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudFechaVencimientoUltResumDia
				.setStepDescription("SAYNUMBER => VENCIMIANTO ULTIMO RESUMEN. DIA");
		Steps.put(stepAudFechaVencimientoUltResumDia.GetId(),
				stepAudFechaVencimientoUltResumDia);

		stepAudSaldoUltResum = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSaldoUltResum
				.setStepDescription("SAYNUMBER => SALDO ULTIMO RESUMEN");
		Steps.put(stepAudSaldoUltResum.GetId(), stepAudSaldoUltResum);

		stepAudSaldoUltResumDec = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudSaldoUltResumDec
				.setStepDescription("SAYNUMBER => SALDO ULTIMO RESUMEN. PARTE DECIMAL");
		Steps.put(stepAudSaldoUltResumDec.GetId(), stepAudSaldoUltResumDec);

		stepAudPagoMinUltResum = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudPagoMinUltResum
				.setStepDescription("SAYNUMBER => PAGO MINIMO ULTIMO RESUMEN");
		Steps.put(stepAudPagoMinUltResum.GetId(), stepAudPagoMinUltResum);

		stepAudPagoMinUltResumDec = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudPagoMinUltResumDec
				.setStepDescription("SAYNUMBER => PAGO MINIMO ULTIMO RESUMEN. PARTE DECIMAL");
		Steps.put(stepAudPagoMinUltResumDec.GetId(), stepAudPagoMinUltResumDec);

		stepAudTotalPagUltResum = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudTotalPagUltResum
				.setStepDescription("SAYNUMBER => PAGO TOTAL ULTIMO RESUMEN");
		Steps.put(stepAudTotalPagUltResum.GetId(), stepAudTotalPagUltResum);

		stepAudTotalPagUltResumDec = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepAudTotalPagUltResumDec
				.setStepDescription("SAYNUMBER => PAGO TOTAL ULTIMO RESUMEN. PARTE DECIMAL");
		Steps.put(stepAudTotalPagUltResumDec.GetId(),
				stepAudTotalPagUltResumDec);

		stepAudSalPendienteDeCanceUltResum = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudSalPendienteDeCanceUltResum
				.setStepDescription("SAYNUMBER => SALDO PENDIENTE DE CANCELACION ULTIMO RESUMEN");
		Steps.put(stepAudSalPendienteDeCanceUltResum.GetId(),
				stepAudSalPendienteDeCanceUltResum);

		stepAudSalPendienteDeCanceUltResumDec = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudSalPendienteDeCanceUltResumDec
				.setStepDescription("SAYNUMBER => SALDO PENDIENTE DE CANCELACION ULTIMO RESUMEN. PARTE DECIMAL");
		Steps.put(stepAudSalPendienteDeCanceUltResumDec.GetId(),
				stepAudSalPendienteDeCanceUltResumDec);

		stepAudPagoMinPendienteDeCanceUltResum = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudPagoMinPendienteDeCanceUltResum
				.setStepDescription("SAYNUMBER => PAGO MINIMO PENDIENTE DE CANCELACION ULTIMO RESUMEN");
		Steps.put(stepAudPagoMinPendienteDeCanceUltResum.GetId(),
				stepAudPagoMinPendienteDeCanceUltResum);

		stepAudPagoMinPendienteDeCanceUltResumDec = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepAudPagoMinPendienteDeCanceUltResumDec
				.setStepDescription("SAYNUMBER => SAYNUMBER => PAGO MINIMO PENDIENTE DE CANCELACION ULTIMO RESUMEN. PARTE DECIMAL");
		Steps.put(stepAudPagoMinPendienteDeCanceUltResumDec.GetId(),
				stepAudPagoMinPendienteDeCanceUltResumDec);

		evalSalUltimResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSalUltimResum
				.setStepDescription("CONDITIONAL => FECHA DE VENCIMIENTO ULT RESUMEN, DIAEvalua evalSalUltimResum");
		Steps.put(evalSalUltimResum.GetId(), evalSalUltimResum);

		evalSalTotalPagUltResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSalTotalPagUltResum
				.setStepDescription("CONDITIONAL => SALDO TOTAL A PAGAR ULTIMO RESUMEN DISTINTO A CERO");
		Steps.put(evalSalTotalPagUltResum.GetId(), evalSalTotalPagUltResum);

		evalSalPenCanUltResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSalPenCanUltResum
				.setStepDescription("CONDITIONAL =>  SALDO PENDIENTE DE CANCELACION ULTIMO RESUMEN DISTINTO A CERO");
		Steps.put(evalSalPenCanUltResum.GetId(), evalSalPenCanUltResum);

		evalSaldoActual = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSaldoActual
				.setStepDescription("CONDITIONAL =>  SALDO ACTUAL DISTINTO A CERO");
		Steps.put(evalSaldoActual.GetId(), evalSaldoActual);

		evalSalEnUnPago = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSalEnUnPago
				.setStepDescription("CONDITIONAL => SALDO EN UN PAGO DISTINTO A CERO");
		Steps.put(evalSalEnUnPago.GetId(), evalSalEnUnPago);

		evalSalEnCuotas = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalSalEnCuotas
				.setStepDescription("CONDITIONAL => SALDO EN CUOTAS DISTINTO A CERO");
		Steps.put(evalSalEnCuotas.GetId(), evalSalEnCuotas);

		evalLocVencimientoProxResumAnio = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocVencimientoProxResumAnio
				.setStepDescription("CONDITIONAL => FECHA DE VENCIMIENTO PROX RESUMEN. ANIO");
		Steps.put(evalLocVencimientoProxResumAnio.GetId(),
				evalLocVencimientoProxResumAnio);

		evalLocVencimientoProxResumDia = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocVencimientoProxResumDia
				.setStepDescription("CONDITIONAL =>  FECHA DE VENCIMIENTO PROX RESUMEN. DIA");
		Steps.put(evalLocVencimientoProxResumDia.GetId(),
				evalLocVencimientoProxResumDia);

		evalLocCierreProxResumAnio = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocCierreProxResumAnio
				.setStepDescription("CONDITIONAL => FECHA DE CIERRE PROXIMO RESUMEN. ANIO");
		Steps.put(evalLocCierreProxResumAnio.GetId(),
				evalLocCierreProxResumAnio);

		evalLocCierreProxResumDia = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocCierreProxResumDia
				.setStepDescription("CONDITIONAL => FECHA DE CIERRE PROXIMO RESUMEN. DIA");
		Steps.put(evalLocCierreProxResumDia.GetId(), evalLocCierreProxResumDia);

		evalLocPagoMinPendienteDeCanceUltResum = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocPagoMinPendienteDeCanceUltResum
				.setStepDescription("CONDITIONAL => PAGO MINIMO PENDIENTE DE CANCELACION ULTIMO RESUMEN DISTINTO A CERO");
		Steps.put(evalLocPagoMinPendienteDeCanceUltResum.GetId(),
				evalLocPagoMinPendienteDeCanceUltResum);

		evalLocSalPendienteDeCanceUltResum = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocSalPendienteDeCanceUltResum
				.setStepDescription("CONDITIONAL => SALDO PENDIENTE DE CANCELACION ULTIMO RESUMEN DISTINTO A CERO");
		Steps.put(evalLocSalPendienteDeCanceUltResum.GetId(),
				evalLocSalPendienteDeCanceUltResum);

		evalLocTotalPagUltResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocTotalPagUltResum
				.setStepDescription("CONDITIONAL => PAGO TOTAL ULTIMO RESUMEN DISTINTO A CERO");
		Steps.put(evalLocTotalPagUltResum.GetId(), evalLocTotalPagUltResum);

		evalLocPagoMinUltResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocPagoMinUltResum
				.setStepDescription("CONDITIONAL => PAGO MINIMO DISTINTO A CERO");
		Steps.put(evalLocPagoMinUltResum.GetId(), evalLocPagoMinUltResum);

		evalLocSaldoUltResum = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocSaldoUltResum
				.setStepDescription("CONDITIONAL => FECHA DE VENCIMIENTO ULT RESUMEN, DIA");
		Steps.put(evalLocSaldoUltResum.GetId(), evalLocSaldoUltResum);

		evalLocFechaVencimientoUltResumAnio = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocFechaVencimientoUltResumAnio
				.setStepDescription("CONDITIONAL => FECHA DE VENCIMIENTO ULT RESUMEN, ANIO");
		Steps.put(evalLocFechaVencimientoUltResumAnio.GetId(),
				evalLocFechaVencimientoUltResumAnio);

		evalLocFechaVencimientoUltResumDia = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocFechaVencimientoUltResumDia
				.setStepDescription("CONDITIONAL => FECHA DE VENCIMIENTO ULT RESUMEN, DIA");
		Steps.put(evalLocFechaVencimientoUltResumDia.GetId(),
				evalLocFechaVencimientoUltResumDia);

		evalLocSalEnCuotas = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocSalEnCuotas
				.setStepDescription("CONDITIONAL => SALDO EN CUOTAS DISTINTO A CERO");
		Steps.put(evalLocSalEnCuotas.GetId(), evalLocSalEnCuotas);

		evalLocSalEnUnPago = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocSalEnUnPago
				.setStepDescription("CONDITIONAL => SALDO EN UN PAGO DISTINTO A CERO");
		Steps.put(evalLocSalEnUnPago.GetId(), evalLocSalEnUnPago);

		evalLocSaldoCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalLocSaldoCuenta
				.setStepDescription("CONDITIONAL => SALDO CUENTA DISTINTO A CERO");
		Steps.put(evalLocSaldoCuenta.GetId(), evalLocSaldoCuenta);

	}

	@Override
	public UUID getInitialStep() {
		return evalSalEnUnPago.GetId();
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

	public void setTotalPagUltResumContextVar(
			ContextVar totalPagUltResumContextVar) {
		this.totalPagUltResumContextVar = totalPagUltResumContextVar;
		stepAudTotalPagUltResum
				.setContextVariableName(totalPagUltResumContextVar);
	}

	public void setVencimientoProxResumDiaContextVar(
			ContextVar vencimientoProxResumDiaContextVar) {
		this.vencimientoProxResumDiaContextVar = vencimientoProxResumDiaContextVar;
		stepAudVencimientoProxResumDia
				.setContextVariableName(vencimientoProxResumDiaContextVar);
	}

	public void setVencimientoProxResumMesContextVar(
			ContextVar vencimientoProxResumMesContextVar) {
		this.vencimientoProxResumMesContextVar = vencimientoProxResumMesContextVar;
		stepAudVencimientoProxResumMes
				.setContextVariableName(vencimientoProxResumMesContextVar);
	}

	public void setScapeDigitContextVar(ContextVar scapeDigitContextVar) {
		this.scapeDigitContextVar = scapeDigitContextVar;

		stepLocSalEnUnPago.setContextVariableName(scapeDigitContextVar);
		stepLocSalEnCuotas.setContextVariableName(scapeDigitContextVar);
		stepLocFechaVencimientoUltResumDia
				.setContextVariableName(scapeDigitContextVar);
		stepLocFechaVencimientoUltResumAnio
				.setContextVariableName(scapeDigitContextVar);
		stepLocSaldoUltResum.setContextVariableName(scapeDigitContextVar);
		stepLocPagoMinUltResum.setContextVariableName(scapeDigitContextVar);
		stepLocTotalPagUltResum.setContextVariableName(scapeDigitContextVar);
		stepLocSalPendienteDeCanceUltResum
				.setContextVariableName(scapeDigitContextVar);
		stepLocPagoMinPendienteDeCanceUltResum
				.setContextVariableName(scapeDigitContextVar);
		stepLocCierreProxResumDia.setContextVariableName(scapeDigitContextVar);
		stepLocCierreProxResumAnio.setContextVariableName(scapeDigitContextVar);
		stepLocVencimientoProxResumDia
				.setContextVariableName(scapeDigitContextVar);
		stepLocVencimientoProxResumAnio
				.setContextVariableName(scapeDigitContextVar);
		stepLocSaldoCuenta.setContextVariableName(scapeDigitContextVar);
		stepIncluyeCompras.setContextVariableName(scapeDigitContextVar);
	}

	public void setVencimientoProxResumAnioContextVar(
			ContextVar vencimientoProxResumAnioContextVar) {
		this.vencimientoProxResumAnioContextVar = vencimientoProxResumAnioContextVar;
		stepAudVencimientoProxResumAnio
				.setContextVariableName(vencimientoProxResumAnioContextVar);
	}

	public void setRetornoMsgJPOS(ContextVar retornoMsgJPOS) {
		this.retornoMsgJPOS = retornoMsgJPOS;
	}

	public void setCierreProxResumDiaContextVar(
			ContextVar cierreProxResumDiaContextVar) {
		this.cierreProxResumDiaContextVar = cierreProxResumDiaContextVar;
		stepAudCierreProxResumDia
				.setContextVariableName(cierreProxResumDiaContextVar);
	}

	public void setCierreProxResumAnioContextVar(
			ContextVar cierreProxResumAnioContextVar) {
		this.cierreProxResumAnioContextVar = cierreProxResumAnioContextVar;
		stepAudCierreProxResumAnio
				.setContextVariableName(cierreProxResumAnioContextVar);
	}

	public void setSalEnUnPagoContextVar(ContextVar salEnUnPagoContextVar) {
		this.salEnUnPagoContextVar = salEnUnPagoContextVar;
		stepAudSalEnUnPago.setContextVariableName(salEnUnPagoContextVar);
	}

	public void setSalEnCuotasContextVar(ContextVar salEnCuotasContextVar) {
		this.salEnCuotasContextVar = salEnCuotasContextVar;
		stepAudSalEnCuotas.setContextVariableName(salEnCuotasContextVar);
	}

	public void setFechaVencimientoUltResumContextVarDia(
			ContextVar fechaVencimientoUltResumContextVarDia) {
		this.fechaVencimientoUltResumContextVarDia = fechaVencimientoUltResumContextVarDia;
		stepAudFechaVencimientoUltResumDia
				.setContextVariableName(fechaVencimientoUltResumContextVarDia);
	}

	public void setFechaVencimientoUltResumContextVarMes(
			ContextVar fechaVencimientoUltResumContextVarMes) {
		this.fechaVencimientoUltResumContextVarMes = fechaVencimientoUltResumContextVarMes;
		stepAudFechaVencimientoUltResumMes
				.setContextVariableName(fechaVencimientoUltResumContextVarMes);
	}

	public void setFechaVencimientoUltResumContextVarAnio(
			ContextVar fechaVencimientoUltResumContextVarAnio) {
		this.fechaVencimientoUltResumContextVarAnio = fechaVencimientoUltResumContextVarAnio;
		stepAudFechaVencimientoUltResumAnio
				.setContextVariableName(fechaVencimientoUltResumContextVarAnio);
	}

	public void setSaldoUltResumContextVar(ContextVar saldoUltResumContextVar) {
		this.saldoUltResumContextVar = saldoUltResumContextVar;
		stepAudSaldoUltResum.setContextVariableName(saldoUltResumContextVar);
	}

	public void setPagoMinUltResumContextVar(
			ContextVar pagoMinUltResumContextVar) {
		this.pagoMinUltResumContextVar = pagoMinUltResumContextVar;
		stepAudPagoMinUltResum
				.setContextVariableName(pagoMinUltResumContextVar);
	}

	public void setSalPendienteDeCanceUltResumContextVar(
			ContextVar salPendienteDeCanceUltResumContextVar) {
		this.salPendienteDeCanceUltResumContextVar = salPendienteDeCanceUltResumContextVar;
		stepAudSalPendienteDeCanceUltResum
				.setContextVariableName(salPendienteDeCanceUltResumContextVar);
	}

	public void setSalPendienteDeCanceUltResumDecimalContextVar(
			ContextVar salPendienteDeCanceUltResumDecimalContextVar) {
		this.salPendienteDeCanceUltResumDecimalContextVar = salPendienteDeCanceUltResumDecimalContextVar;
		stepAudSalPendienteDeCanceUltResumDec
				.setContextVariableName(salPendienteDeCanceUltResumDecimalContextVar);
	}

	public void setPagoMinPendienteDeCanceUltResumContextVar(
			ContextVar pagoMinPendienteDeCanceUltResumContextVar) {
		this.pagoMinPendienteDeCanceUltResumContextVar = pagoMinPendienteDeCanceUltResumContextVar;
		stepAudPagoMinPendienteDeCanceUltResum
				.setContextVariableName(pagoMinPendienteDeCanceUltResumContextVar);
	}

	public void setCierreProxResumMesContextVar(
			ContextVar cierreProxResumMesContextVar) {
		this.cierreProxResumMesContextVar = cierreProxResumMesContextVar;
		stepAudCierreProxResumMes
				.setContextVariableName(cierreProxResumMesContextVar);
	}

	public void setSaldoCuentaContextVar(ContextVar saldoCuentaContextVar) {
		this.saldoCuentaContextVar = saldoCuentaContextVar;
		stepAudSaldoCuenta.setContextVariableName(saldoCuentaContextVar);
	}

	public void setSaldoTotalPagUltResumDecimalContextVar(
			ContextVar totalPagUltResumDecimalContextVar) {
		this.saldoUltResumDecimalContextVar = totalPagUltResumDecimalContextVar;
		stepAudTotalPagUltResumDec
				.setContextVariableName(totalPagUltResumDecimalContextVar);
	}

	public void setSaldoUltResumDecimalContextVar(
			ContextVar saldoUltResumDecimalContextVar) {
		this.saldoUltResumDecimalContextVar = saldoUltResumDecimalContextVar;
		stepAudSaldoUltResumDec
				.setContextVariableName(saldoUltResumDecimalContextVar);
	}

	public void setPagoMinUltResumDecimContextVar(
			ContextVar pagoMinUltResumDecimContextVar) {
		this.pagoMinUltResumDecimContextVar = pagoMinUltResumDecimContextVar;
		stepAudPagoMinUltResumDec
				.setContextVariableName(pagoMinUltResumDecimContextVar);
	}

	public void setSalEnUnPagoDecimalContextVar(
			ContextVar salEnUnPagoDecimalContextVar) {
		this.salEnUnPagoDecimalContextVar = salEnUnPagoDecimalContextVar;
		stepAudSalDecEnUnPago
				.setContextVariableName(salEnUnPagoDecimalContextVar);
	}

	public void setSalEnCuotasDecimalContextVar(
			ContextVar salEnCuotasDecimalContextVar) {
		this.salEnCuotasDecimalContextVar = salEnCuotasDecimalContextVar;
		stepAudSalDecEnCuotas
				.setContextVariableName(salEnCuotasDecimalContextVar);
	}

	public void setPagoMinPendienteDeCanceUltDecimalResumContextVar(
			ContextVar pagoMinPendienteDeCanceUltDecimalResumContextVar) {
		this.pagoMinPendienteDeCanceUltDecimalResumContextVar = pagoMinPendienteDeCanceUltDecimalResumContextVar;
		stepAudPagoMinPendienteDeCanceUltResumDec
				.setContextVariableName(pagoMinPendienteDeCanceUltDecimalResumContextVar);
	}

	public void setSaldoCuentaDecimalContextVar(
			ContextVar saldoCuentaDecimalContextVar) {
		this.saldoCuentaDecimalContextVar = saldoCuentaDecimalContextVar;
		stepAudSaldoDecimalCuenta
				.setContextVariableName(saldoCuentaDecimalContextVar);
	}
}
