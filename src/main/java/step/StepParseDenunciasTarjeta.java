package step;

import java.util.UUID;

import context.ContextVar;

import ivr.CallContext;
import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */

public class StepParseDenunciasTarjeta extends Step {

	private ContextVar retornoMsgJPOS;
	private ContextVar sucursalContextVar;
	private ContextVar disponibleDeComprasContextVar;
	private ContextVar disponibleDeComprasDecimalContextVar;
	private ContextVar numeroDeDenunciaContextVar;
	private ContextVar fechaDeEntregaContextVar;

	public StepParseDenunciasTarjeta(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ParseDenunciasTarjeta;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (retornoMsgJPOS == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(retornoMsgJPOS.getId())) {

			ContextVar ctv = (ContextVar) context.get(retornoMsgJPOS.getId());

			sucursalContextVar.setVarValue(ctv.getVarValue().substring(35, 38));
			context.put(sucursalContextVar.getId(), sucursalContextVar);

			disponibleDeComprasContextVar.setVarValue(ctv.getVarValue()
					.substring(39, 48));
			context.put(disponibleDeComprasContextVar.getId(),
					disponibleDeComprasContextVar);

			disponibleDeComprasDecimalContextVar.setVarValue(ctv.getVarValue()
					.substring(48, 49));
			context.put(disponibleDeComprasDecimalContextVar.getId(),
					disponibleDeComprasDecimalContextVar);

			numeroDeDenunciaContextVar.setVarValue(ctv.getVarValue().substring(
					50, 55));
			context.put(numeroDeDenunciaContextVar.getId(),
					numeroDeDenunciaContextVar);

			fechaDeEntregaContextVar.setVarValue(ctv.getVarValue().substring(
					55, 62));
			context.put(fechaDeEntregaContextVar.getId(),
					fechaDeEntregaContextVar);

		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setRetornoMsgJPOS(ContextVar retornoMsgJPOS) {
		this.retornoMsgJPOS = retornoMsgJPOS;
	}

	public void setSucursalContextVar(ContextVar sucursalContextVar) {
		this.sucursalContextVar = sucursalContextVar;
	}

	public void setDisponibleDeComprasContextVar(
			ContextVar disponibleDeComprasContextVar) {
		this.disponibleDeComprasContextVar = disponibleDeComprasContextVar;
	}

	public void setDisponibleDeComprasDecimalContextVar(
			ContextVar disponibleDeComprasDecimalContextVar) {
		this.disponibleDeComprasDecimalContextVar = disponibleDeComprasDecimalContextVar;
	}

	public void setNumeroDeDenunciaContextVar(
			ContextVar numeroDeDenunciaContextVar) {
		this.numeroDeDenunciaContextVar = numeroDeDenunciaContextVar;
	}

	public void setFechaDeEntregaContextVar(ContextVar fechaDeEntregaContextVar) {
		this.fechaDeEntregaContextVar = fechaDeEntregaContextVar;
	}

}
