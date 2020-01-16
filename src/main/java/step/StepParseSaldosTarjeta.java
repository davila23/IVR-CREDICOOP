/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

import context.ContextVar;

import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */

public class StepParseSaldosTarjeta extends Step {

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
	private ContextVar codigoDeEstudioContextVar;
	private ContextVar saldoUltResumDecimalContextVar;
	private ContextVar totalPagUltResumDecimalContextVar;
	private ContextVar pagoMinUltResumDecimContextVar;
	private ContextVar salEnUnPagoDecimalContextVar;
	private ContextVar salEnCuotasDecimalContextVar;
	private ContextVar salPendienteDeCanceUltResumDecimalContextVar;
	private ContextVar pagoMinPendienteDeCanceUltDecimalResumContextVar;
	private ContextVar saldoCuentaDecimalContextVar;
	private ContextVar tarjetaPendienteDeActivacionContextVar;

	public StepParseSaldosTarjeta(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ParseSaldoTarjeta;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (retornoMsgJPOS == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(retornoMsgJPOS.getId())) {

			ContextVar ctv = (ContextVar) context.get(retornoMsgJPOS.getId());

			saldoUltResumContextVar.setVarValue(ctv.getVarValue().substring(21,
					31));
			context.put(saldoUltResumContextVar.getId(),
					saldoUltResumContextVar);

			saldoUltResumDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(30, 32));
			context.put(saldoUltResumDecimalContextVar.getId(),
					saldoUltResumDecimalContextVar);

			totalPagUltResumContextVar.setVarValue(ctv.getVarValue().substring(
					59, 64));
			context.put(totalPagUltResumContextVar.getId(),
					totalPagUltResumContextVar);

			totalPagUltResumDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(64, 66));
			context.put(totalPagUltResumDecimalContextVar.getId(),
					totalPagUltResumDecimalContextVar);

			pagoMinUltResumContextVar.setVarValue(ctv.getVarValue().substring(
					47, 57));
			context.put(pagoMinUltResumContextVar.getId(),
					pagoMinUltResumContextVar);

			pagoMinUltResumDecimContextVar.setVarValue(ctv.getVarValue()
					.substring(57, 59));
			context.put(pagoMinUltResumDecimContextVar.getId(),
					pagoMinUltResumDecimContextVar);

			double saldoUltResum = Double.parseDouble(saldoUltResumContextVar
					.getVarValue());
			double totalPagUltResum = Double
					.parseDouble(totalPagUltResumContextVar.getVarValue());

			double tmp_saldoPendienteDeCancelacion = saldoUltResum
					- totalPagUltResum;

			double pagoMinUltResum = Double
					.parseDouble(pagoMinUltResumContextVar.getVarValue());
			double tmp_saldoMinPendienteDeCancelacion = pagoMinUltResum
					- totalPagUltResum;

			String saldoPendienteDeCancelacion = Double
					.toString(tmp_saldoPendienteDeCancelacion);
			String saldoMinPendienteDeCancelacion = Double
					.toString(tmp_saldoMinPendienteDeCancelacion);

			codigoDeEstudioContextVar.setVarValue(ctv.getVarValue().substring(
					145, 147));
			context.put(codigoDeEstudioContextVar.getId(),
					codigoDeEstudioContextVar);

			salEnUnPagoContextVar.setVarValue(ctv.getVarValue().substring(77,
					85));
			context.put(salEnUnPagoContextVar.getId(), salEnUnPagoContextVar);

			salEnUnPagoDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(85, 87));
			context.put(salEnUnPagoDecimalContextVar.getId(),
					salEnUnPagoDecimalContextVar);

			salEnCuotasContextVar.setVarValue(ctv.getVarValue().substring(98,
					106));
			context.put(salEnCuotasContextVar.getId(), salEnCuotasContextVar);

			salEnCuotasDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(106, 108));
			context.put(salEnCuotasDecimalContextVar.getId(),
					salEnCuotasDecimalContextVar);

			fechaVencimientoUltResumContextVarDia.setVarValue(ctv.getVarValue()
					.substring(71, 73));
			context.put(fechaVencimientoUltResumContextVarDia.getId(),
					fechaVencimientoUltResumContextVarDia);

			fechaVencimientoUltResumContextVarMes.setVarValue(ctv.getVarValue()
					.substring(73, 75));
			context.put(fechaVencimientoUltResumContextVarMes.getId(),
					fechaVencimientoUltResumContextVarMes);

			fechaVencimientoUltResumContextVarAnio.setVarValue(ctv
					.getVarValue().substring(75, 77));
			context.put(fechaVencimientoUltResumContextVarAnio.getId(),
					fechaVencimientoUltResumContextVarAnio);

			salPendienteDeCanceUltResumContextVar
					.setVarValue(saldoPendienteDeCancelacion.substring(0,
							saldoPendienteDeCancelacion.indexOf(".")));
			context.put(salPendienteDeCanceUltResumContextVar.getId(),
					salPendienteDeCanceUltResumContextVar);

			salPendienteDeCanceUltResumDecimalContextVar
					.setVarValue(saldoPendienteDeCancelacion.substring(
							saldoPendienteDeCancelacion.indexOf(".") + 1,
							saldoPendienteDeCancelacion.length()));
			context.put(salPendienteDeCanceUltResumDecimalContextVar.getId(),
					salPendienteDeCanceUltResumDecimalContextVar);

			pagoMinPendienteDeCanceUltResumContextVar
					.setVarValue(saldoMinPendienteDeCancelacion.substring(0,
							saldoMinPendienteDeCancelacion.indexOf(".")));
			context.put(pagoMinPendienteDeCanceUltResumContextVar.getId(),
					pagoMinPendienteDeCanceUltResumContextVar);

			pagoMinPendienteDeCanceUltDecimalResumContextVar
					.setVarValue(saldoMinPendienteDeCancelacion.substring(
							saldoMinPendienteDeCancelacion.indexOf(".") + 1,
							saldoMinPendienteDeCancelacion.length()));
			context.put(
					pagoMinPendienteDeCanceUltDecimalResumContextVar.getId(),
					pagoMinPendienteDeCanceUltDecimalResumContextVar);

			cierreProxResumDiaContextVar.setVarValue(ctv.getVarValue()
					.substring(139, 141));
			context.put(cierreProxResumDiaContextVar.getId(),
					cierreProxResumDiaContextVar);

			cierreProxResumMesContextVar.setVarValue(ctv.getVarValue()
					.substring(141, 143));
			context.put(cierreProxResumMesContextVar.getId(),
					cierreProxResumMesContextVar);

			cierreProxResumAnioContextVar.setVarValue(ctv.getVarValue()
					.substring(143, 145));
			context.put(cierreProxResumAnioContextVar.getId(),
					cierreProxResumAnioContextVar);

			vencimientoProxResumDiaContextVar.setVarValue(ctv.getVarValue()
					.substring(185, 187));
			context.put(vencimientoProxResumDiaContextVar.getId(),
					vencimientoProxResumDiaContextVar);

			vencimientoProxResumMesContextVar.setVarValue(ctv.getVarValue()
					.substring(187, 189));
			context.put(vencimientoProxResumMesContextVar.getId(),
					vencimientoProxResumMesContextVar);

			vencimientoProxResumAnioContextVar.setVarValue(ctv.getVarValue()
					.substring(189, 191));
			context.put(vencimientoProxResumAnioContextVar.getId(),
					vencimientoProxResumAnioContextVar);

			saldoCuentaContextVar.setVarValue(ctv.getVarValue().substring(147,
					157));
			context.put(saldoCuentaContextVar.getId(), saldoCuentaContextVar);

			saldoCuentaDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(157, 159));
			context.put(saldoCuentaDecimalContextVar.getId(),
					saldoCuentaDecimalContextVar);

			tarjetaPendienteDeActivacionContextVar.setVarValue(ctv
					.getVarValue().substring(191, 192));
			context.put(tarjetaPendienteDeActivacionContextVar.getId(),
					tarjetaPendienteDeActivacionContextVar);

		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setCodigoDeEstudioContextVar(
			ContextVar codigoDeEstudioContextVar) {
		this.codigoDeEstudioContextVar = codigoDeEstudioContextVar;
	}

	public void setRetornoMsgJPOS(ContextVar retornoMsgJPOS) {
		this.retornoMsgJPOS = retornoMsgJPOS;
	}

	public void setSalEnUnPagoContextVar(ContextVar salEnUnPagoContextVar) {
		this.salEnUnPagoContextVar = salEnUnPagoContextVar;
	}

	public void setSalEnCuotasContextVar(ContextVar salEnCuotasContextVar) {
		this.salEnCuotasContextVar = salEnCuotasContextVar;
	}

	public void setFechaVencimientoUltResumContextVarDia(
			ContextVar fechaVencimientoUltResumContextVarDia) {
		this.fechaVencimientoUltResumContextVarDia = fechaVencimientoUltResumContextVarDia;
	}

	public void setFechaVencimientoUltResumContextVarMes(
			ContextVar fechaVencimientoUltResumContextVarMes) {
		this.fechaVencimientoUltResumContextVarMes = fechaVencimientoUltResumContextVarMes;
	}

	public void setFechaVencimientoUltResumContextVarAnio(
			ContextVar fechaVencimientoUltResumContextVarAnio) {
		this.fechaVencimientoUltResumContextVarAnio = fechaVencimientoUltResumContextVarAnio;
	}

	public void setSaldoUltResumContextVar(ContextVar saldoUltResumContextVar) {
		this.saldoUltResumContextVar = saldoUltResumContextVar;
	}

	public void setPagoMinUltResumContextVar(
			ContextVar pagoMinUltResumContextVar) {
		this.pagoMinUltResumContextVar = pagoMinUltResumContextVar;
	}

	public void setTotalPagUltResumContextVar(
			ContextVar totalPagUltResumContextVar) {
		this.totalPagUltResumContextVar = totalPagUltResumContextVar;
	}

	public void setSalPendienteDeCanceUltResumContextVar(
			ContextVar salPendienteDeCanceUltResumContextVar) {
		this.salPendienteDeCanceUltResumContextVar = salPendienteDeCanceUltResumContextVar;
	}

	public void setPagoMinPendienteDeCanceUltResumContextVar(
			ContextVar pagoMinPendienteDeCanceUltResumContextVar) {
		this.pagoMinPendienteDeCanceUltResumContextVar = pagoMinPendienteDeCanceUltResumContextVar;
	}

	public void setCierreProxResumDiaContextVar(
			ContextVar cierreProxResumDiaContextVar) {
		this.cierreProxResumDiaContextVar = cierreProxResumDiaContextVar;
	}

	public void setCierreProxResumMesContextVar(
			ContextVar cierreProxResumMesContextVar) {
		this.cierreProxResumMesContextVar = cierreProxResumMesContextVar;
	}

	public void setCierreProxResumAnioContextVar(
			ContextVar cierreProxResumAnioContextVar) {
		this.cierreProxResumAnioContextVar = cierreProxResumAnioContextVar;
	}

	public void setVencimientoProxResumDiaContextVar(
			ContextVar vencimientoProxResumDiaContextVar) {
		this.vencimientoProxResumDiaContextVar = vencimientoProxResumDiaContextVar;
	}

	public void setVencimientoProxResumMesContextVar(
			ContextVar vencimientoProxResumMesContextVar) {
		this.vencimientoProxResumMesContextVar = vencimientoProxResumMesContextVar;
	}

	public void setVencimientoProxResumAnioContextVar(
			ContextVar vencimientoProxResumAnioContextVar) {
		this.vencimientoProxResumAnioContextVar = vencimientoProxResumAnioContextVar;
	}

	public void setSaldoCuentaContextVar(ContextVar saldoCuentaContextVar) {
		this.saldoCuentaContextVar = saldoCuentaContextVar;
	}

	public void setSaldoUltResumDecimalContextVar(
			ContextVar saldoUltResumDecimalContextVar) {
		this.saldoUltResumDecimalContextVar = saldoUltResumDecimalContextVar;
	}

	public void setTotalPagUltResumDecimalContextVar(
			ContextVar totalPagUltResumDecimalContextVar) {
		this.totalPagUltResumDecimalContextVar = totalPagUltResumDecimalContextVar;
	}

	public void setPagoMinUltResumDecimContextVar(
			ContextVar pagoMinUltResumDecimContextVar) {
		this.pagoMinUltResumDecimContextVar = pagoMinUltResumDecimContextVar;
	}

	public void setSalEnUnPagoDecimalContextVar(
			ContextVar salEnUnPagoDecimalContextVar) {
		this.salEnUnPagoDecimalContextVar = salEnUnPagoDecimalContextVar;
	}

	public void setSalEnCuotasDecimalContextVar(
			ContextVar salEnCuotasDecimalContextVar) {
		this.salEnCuotasDecimalContextVar = salEnCuotasDecimalContextVar;
	}

	public void setSalPendienteDeCanceUltResumDecimalContextVar(
			ContextVar salPendienteDeCanceUltResumDecimalContextVar) {
		this.salPendienteDeCanceUltResumDecimalContextVar = salPendienteDeCanceUltResumDecimalContextVar;
	}

	public void setPagoMinPendienteDeCanceUltDecimalResumContextVar(
			ContextVar pagoMinPendienteDeCanceUltDecimalResumContextVar) {
		this.pagoMinPendienteDeCanceUltDecimalResumContextVar = pagoMinPendienteDeCanceUltDecimalResumContextVar;
	}

	public void setSaldoCuentaDecimalContextVar(
			ContextVar saldoCuentaDecimalContextVar) {
		this.saldoCuentaDecimalContextVar = saldoCuentaDecimalContextVar;
	}

	public void setTarjetaPendienteDeActivacionContextVar(
			ContextVar tarjetaPendienteDeActivacionContextVar) {
		this.tarjetaPendienteDeActivacionContextVar = tarjetaPendienteDeActivacionContextVar;
	}

}
