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

public class StepParseSaldosTarjetaPrecargada extends Step {

	private ContextVar Sig_importeCompraContextVar_6 ;
	private ContextVar retornoMsgJPOS;
	private ContextVar s_DispComprasContextVar;
	private ContextVar disponibleComprasContextVar;
	private ContextVar s_ImporteUltimaCargaContextVar;
	private ContextVar importeUltimaCargaContextVar;
	private ContextVar s_importeCompraContextVar_1;
	private ContextVar importeCompraContextVar_1;
	private ContextVar importeDecimalCompraContextVar_1;
	private ContextVar fechaCompraDiaContextVar_1;
	private ContextVar fechaCompraMesContextVar_1;
	private ContextVar fechaCompraAnioContextVar_1;
	private ContextVar fechaCompraDiaContextVar_2;
	private ContextVar importeDecimalCompraContextVar_2;
	private ContextVar importeCompraContextVar_2;
	private ContextVar s_importeCompraContextVar_2;
	private ContextVar fechaCompraAnioContextVar_2;
	private ContextVar fechaCompraMesContextVar_2;
	private ContextVar s_importeCompraContextVar_3;
	private ContextVar importeCompraContextVar_3;
	private ContextVar importeDecimalCompraContextVar_3;
	private ContextVar fechaCompraMesContextVar_3;
	private ContextVar fechaCompraDiaContextVar_3;
	private ContextVar importeCompraContextVar_4;
	private ContextVar s_importeCompraContextVar_4;
	private ContextVar fechaCompraAnioContextVar_3;
	private ContextVar fechaCompraAnioContextVar_4;
	private ContextVar fechaCompraMesContextVar_4;
	private ContextVar fechaCompraDiaContextVar_4;
	private ContextVar importeDecimalCompraContextVar_4;
	private ContextVar s_importeCompraContextVar_5;
	private ContextVar importeCompraContextVar_5;
	private ContextVar importeDecimalCompraContextVar_5;
	private ContextVar fechaCompraDiaContextVar_5;
	private ContextVar fechaCompraAnioContextVar_5;
	private ContextVar fechaCompraMesContextVar_5;
	private ContextVar fechaCompraDiaContextVar_6;
	private ContextVar importeDecimalCompraContextVar_6;
	private ContextVar importeCompraContextVar_6;
	private ContextVar fechaCompraAnioContextVar_6;
	private ContextVar fechaCompraMesContextVar_6;
	private ContextVar disponibleRetirosDecimalContextVar;
	private ContextVar disponibleRetirosContextVar;
	private ContextVar s_disponibleRetirosContextVar;
	private ContextVar fechaUltimaCargaDiaContextVar;
	private ContextVar fechaUltimaCargaMesContextVar;
	private ContextVar fechaUltimaCargaAnioContextVar;
	private ContextVar importeDecimalUltimaCargaContextVar;

	public StepParseSaldosTarjetaPrecargada(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ParseSaldoTarjetaPrecargada;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (retornoMsgJPOS == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(retornoMsgJPOS.getId())) {

			ContextVar ctv = (ContextVar) context.get(retornoMsgJPOS.getId());

			/* Disponible Compras */

			s_DispComprasContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_DispComprasContextVar.getId(), s_DispComprasContextVar);

			disponibleComprasContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(disponibleComprasContextVar.getId(), disponibleComprasContextVar);

			/* Ultima Carga */

			fechaUltimaCargaDiaContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(fechaUltimaCargaDiaContextVar.getId(), fechaUltimaCargaDiaContextVar);

			fechaUltimaCargaMesContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(fechaUltimaCargaMesContextVar.getId(), fechaUltimaCargaMesContextVar);

			fechaUltimaCargaAnioContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(fechaUltimaCargaAnioContextVar.getId(), fechaUltimaCargaAnioContextVar);

			s_ImporteUltimaCargaContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_ImporteUltimaCargaContextVar.getId(), s_ImporteUltimaCargaContextVar);

			importeUltimaCargaContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeUltimaCargaContextVar.getId(), importeUltimaCargaContextVar);

			importeDecimalUltimaCargaContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalUltimaCargaContextVar.getId(), importeDecimalUltimaCargaContextVar);

			/* Primer Consumo */
			/* Importe */

			s_importeCompraContextVar_1.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_importeCompraContextVar_1.getId(), s_importeCompraContextVar_1);

			importeCompraContextVar_1.setVarValue(ctv.getVarValue().substring(55, 62));
			context.put(importeCompraContextVar_1.getId(), importeCompraContextVar_1);

			importeDecimalCompraContextVar_1.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_1.getId(), importeDecimalCompraContextVar_1);

			/* Fecha */

			fechaCompraDiaContextVar_1.setVarValue(ctv.getVarValue().substring(70, 72));
			context.put(fechaCompraDiaContextVar_1.getId(), fechaCompraDiaContextVar_1);

			fechaCompraMesContextVar_1.setVarValue(ctv.getVarValue().substring(68, 70));
			context.put(fechaCompraMesContextVar_1.getId(), fechaCompraMesContextVar_1);

			fechaCompraAnioContextVar_1.setVarValue(ctv.getVarValue().substring(64, 68));
			context.put(fechaCompraAnioContextVar_1.getId(), fechaCompraAnioContextVar_1);

			/* Segundo Consumo */

			/* Importe */

			s_importeCompraContextVar_2.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_importeCompraContextVar_2.getId(), s_importeCompraContextVar_2);

			importeCompraContextVar_2.setVarValue(ctv.getVarValue().substring(73, 80));
			context.put(importeCompraContextVar_2.getId(), importeCompraContextVar_2);

			importeDecimalCompraContextVar_2.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_2.getId(), importeDecimalCompraContextVar_2);

			/* Fecha */

			fechaCompraDiaContextVar_2.setVarValue(ctv.getVarValue().substring(88, 90));
			context.put(fechaCompraDiaContextVar_2.getId(), fechaCompraDiaContextVar_2);

			fechaCompraMesContextVar_2.setVarValue(ctv.getVarValue().substring(86, 88));
			context.put(fechaCompraMesContextVar_2.getId(), fechaCompraMesContextVar_2);

			fechaCompraAnioContextVar_2.setVarValue(ctv.getVarValue().substring(82, 86));
			context.put(fechaCompraAnioContextVar_2.getId(), fechaCompraAnioContextVar_2);

			/* Tercer Consumo */

			/* Importe */

			s_importeCompraContextVar_3.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_importeCompraContextVar_3.getId(), s_importeCompraContextVar_3);

			importeCompraContextVar_3.setVarValue(ctv.getVarValue().substring(91, 98));
			context.put(importeCompraContextVar_3.getId(), importeCompraContextVar_3);

			importeDecimalCompraContextVar_3.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_3.getId(), importeDecimalCompraContextVar_3);

			/* Fecha */

			fechaCompraDiaContextVar_3.setVarValue(ctv.getVarValue().substring(106, 108));
			context.put(fechaCompraDiaContextVar_3.getId(), fechaCompraDiaContextVar_3);

			fechaCompraMesContextVar_3.setVarValue(ctv.getVarValue().substring(104, 106));
			context.put(fechaCompraMesContextVar_3.getId(), fechaCompraMesContextVar_3);

			fechaCompraAnioContextVar_3.setVarValue(ctv.getVarValue().substring(100, 104));
			context.put(fechaCompraAnioContextVar_3.getId(), fechaCompraAnioContextVar_3);

			/* Cuarto Consumo */

			/* Importe */

			s_importeCompraContextVar_4.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_importeCompraContextVar_4.getId(), s_importeCompraContextVar_4);

			importeCompraContextVar_4.setVarValue(ctv.getVarValue().substring(109, 116));
			context.put(importeCompraContextVar_4.getId(), importeCompraContextVar_4);

			importeDecimalCompraContextVar_4.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_4.getId(), importeDecimalCompraContextVar_4);

			/* Fecha */

			fechaCompraDiaContextVar_4.setVarValue(ctv.getVarValue().substring(124, 126));
			context.put(fechaCompraDiaContextVar_4.getId(), fechaCompraDiaContextVar_4);

			fechaCompraMesContextVar_4.setVarValue(ctv.getVarValue().substring(122, 124));
			context.put(fechaCompraMesContextVar_4.getId(), fechaCompraMesContextVar_4);

			fechaCompraAnioContextVar_4.setVarValue(ctv.getVarValue().substring(118, 122));
			context.put(fechaCompraAnioContextVar_4.getId(), fechaCompraAnioContextVar_4);

			/* Quinto Consumo */

			/* Importe */

			s_importeCompraContextVar_5.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_importeCompraContextVar_5.getId(), s_importeCompraContextVar_5);

			importeCompraContextVar_5.setVarValue(ctv.getVarValue().substring(127, 134));
			context.put(importeCompraContextVar_5.getId(), importeCompraContextVar_5);

			importeDecimalCompraContextVar_5.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_5.getId(), importeDecimalCompraContextVar_5);

			/* Fecha */

			fechaCompraDiaContextVar_5.setVarValue(ctv.getVarValue().substring(142, 144));
			context.put(fechaCompraDiaContextVar_5.getId(), fechaCompraDiaContextVar_5);

			fechaCompraMesContextVar_5.setVarValue(ctv.getVarValue().substring(140, 142));
			context.put(fechaCompraMesContextVar_5.getId(), fechaCompraMesContextVar_5);

			fechaCompraAnioContextVar_5.setVarValue(ctv.getVarValue().substring(136, 140));
			context.put(fechaCompraAnioContextVar_5.getId(), fechaCompraAnioContextVar_5);

			/* Sexto Consumo */
			/* Importe */
			Sig_importeCompraContextVar_6.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(Sig_importeCompraContextVar_6.getId(), Sig_importeCompraContextVar_6);

			importeCompraContextVar_6.setVarValue(ctv.getVarValue().substring(145, 152));
			context.put(importeCompraContextVar_6.getId(), importeCompraContextVar_6);

			importeDecimalCompraContextVar_6.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(importeDecimalCompraContextVar_6.getId(), importeDecimalCompraContextVar_6);

			/* Fecha */

			fechaCompraDiaContextVar_6.setVarValue(ctv.getVarValue().substring(160, 162));
			context.put(fechaCompraDiaContextVar_6.getId(), fechaCompraDiaContextVar_6);

			fechaCompraMesContextVar_6.setVarValue(ctv.getVarValue().substring(158, 160));
			context.put(fechaCompraMesContextVar_6.getId(), fechaCompraMesContextVar_6);

			fechaCompraAnioContextVar_6.setVarValue(ctv.getVarValue().substring(154, 158));
			context.put(fechaCompraAnioContextVar_6.getId(), fechaCompraAnioContextVar_6);

			/* Disponible Retiros */

			s_disponibleRetirosContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(s_disponibleRetirosContextVar.getId(), s_disponibleRetirosContextVar);

			disponibleRetirosContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(disponibleRetirosContextVar.getId(), disponibleRetirosContextVar);

			disponibleRetirosDecimalContextVar.setVarValue(ctv.getVarValue().substring(30, 32));
			context.put(disponibleRetirosDecimalContextVar.getId(), disponibleRetirosDecimalContextVar);

		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return false;

	}

	public void setS_DispComprasContextVar(ContextVar s_DispComprasContextVar) {
		this.s_DispComprasContextVar = s_DispComprasContextVar;
	}

	public void setDisponibleComprasContextVar(ContextVar disponibleComprasContextVar) {
		this.disponibleComprasContextVar = disponibleComprasContextVar;
	}

	public void setFechaUltimaCargaDiaContextVar(ContextVar fechaUltimaCargaDiaContextVar) {
		this.fechaUltimaCargaDiaContextVar = fechaUltimaCargaDiaContextVar;
	}

	public void setFechaUltimaCargaMesContextVar(ContextVar fechaUltimaCargaDiaContextVar) {
		this.fechaUltimaCargaDiaContextVar = fechaUltimaCargaDiaContextVar;
	}

	public void setFechaUltimaCargaAnioContextVar(ContextVar fechaUltimaCargaAnioContextVar) {
		this.fechaUltimaCargaAnioContextVar = fechaUltimaCargaAnioContextVar;
	}

	public void setS_ImporteUltimaCargaContextVar(ContextVar s_ImporteUltimaCargaContextVar) {
		this.s_ImporteUltimaCargaContextVar = s_ImporteUltimaCargaContextVar;
	}

	public void setImporteUltimaCargaContextVar(ContextVar importeUltimaCargaContextVar) {
		this.importeUltimaCargaContextVar = importeUltimaCargaContextVar;
	}

	public void setImporteDecimalUltimaCargaContextVar(ContextVar importeDecimalUltimaCargaContextVar) {
		this.importeDecimalUltimaCargaContextVar = importeDecimalUltimaCargaContextVar;
	}

	public void setS_importeCompraContextVar_1(ContextVar s_importeCompraContextVar_1) {
		this.s_importeCompraContextVar_1 = s_importeCompraContextVar_1;
	}

	public void setImporteCompraContextVar_1(ContextVar importeCompraContextVar_1) {
		this.importeCompraContextVar_1 = importeCompraContextVar_1;
	}

	public void setImporteDecimalCompraContextVar_1(ContextVar importeDecimalCompraContextVar_1) {
		this.importeDecimalCompraContextVar_1 = importeDecimalCompraContextVar_1;
	}

	public void setFechaCompraDiaContextVar_1(ContextVar fechaCompraDiaContextVar_1) {
		this.fechaCompraDiaContextVar_1 = fechaCompraDiaContextVar_1;
	}

	public void setFechaCompraMesContextVar_1(ContextVar fechaCompraMesContextVar_1) {
		this.fechaCompraMesContextVar_1 = fechaCompraMesContextVar_1;
	}

	public void setFechaCompraAnioContextVar_1(ContextVar fechaCompraAnioContextVar_1) {
		this.fechaCompraAnioContextVar_1 = fechaCompraAnioContextVar_1;
	}

	public void setFechaCompraDiaContextVar_2(ContextVar fechaCompraDiaContextVar_2) {
		this.fechaCompraDiaContextVar_2 = fechaCompraDiaContextVar_2;
	}

	public void setImporteDecimalCompraContextVar_2(ContextVar importeDecimalCompraContextVar_2) {
		this.importeDecimalCompraContextVar_2 = importeDecimalCompraContextVar_2;
	}

	public void setImporteCompraContextVar_2(ContextVar importeCompraContextVar_2) {
		this.importeCompraContextVar_2 = importeCompraContextVar_2;
	}

	public void setS_importeCompraContextVar_2(ContextVar s_importeCompraContextVar_2) {
		this.s_importeCompraContextVar_2 = s_importeCompraContextVar_2;
	}

	public void setFechaCompraAnioContextVar_2(ContextVar fechaCompraAnioContextVar_2) {
		this.fechaCompraAnioContextVar_2 = fechaCompraAnioContextVar_2;
	}

	public void setFechaCompraMesContextVar_2(ContextVar fechaCompraMesContextVar_2) {
		this.fechaCompraMesContextVar_2 = fechaCompraMesContextVar_2;
	}

	public void setS_importeCompraContextVar_3(ContextVar s_importeCompraContextVar_3) {
		this.s_importeCompraContextVar_3 = s_importeCompraContextVar_3;
	}

	public void setImporteCompraContextVar_3(ContextVar importeCompraContextVar_3) {
		this.importeCompraContextVar_3 = importeCompraContextVar_3;
	}

	public void setImporteDecimalCompraContextVar_3(ContextVar importeDecimalCompraContextVar_3) {
		this.importeDecimalCompraContextVar_3 = importeDecimalCompraContextVar_3;
	}

	public void setFechaCompraAnioContextVar_3(ContextVar fechaCompraAnioContextVar_3) {
		this.fechaCompraAnioContextVar_3 = fechaCompraAnioContextVar_3;
	}
	
	public void setFechaCompraMesContextVar_3(ContextVar fechaCompraMesContextVar_3) {
		this.fechaCompraMesContextVar_3 = fechaCompraMesContextVar_3;
	}

	public void setFechaCompraDiaContextVar_3(ContextVar fechaCompraDiaContextVar_3) {
		this.fechaCompraDiaContextVar_3 = fechaCompraDiaContextVar_3;
	}

	public void setImporteCompraContextVar_4(ContextVar importeCompraContextVar_4) {
		this.importeCompraContextVar_4 = importeCompraContextVar_4;
	}

	public void setCodigoDeEstudioContextVar(ContextVar s_importeCompraContextVar_4) {
		this.s_importeCompraContextVar_4 = s_importeCompraContextVar_4;
	}

	public void setFechaCompraAnioContextVar_4(ContextVar fechaCompraAnioContextVar_4) {
		this.fechaCompraAnioContextVar_4 = fechaCompraAnioContextVar_4;
	}

	public void setFechaCompraMesContextVar_4(ContextVar fechaCompraMesContextVar_4) {
		this.fechaCompraMesContextVar_4 = fechaCompraMesContextVar_4;
	}
	
	public void setFechaCompraDiaContextVar_4(ContextVar fechaCompraDiaContextVar_4) {
		this.fechaCompraDiaContextVar_4 = fechaCompraDiaContextVar_4;
	}

	public void setImporteDecimalCompraContextVar_4(ContextVar importeDecimalCompraContextVar_4) {
		this.importeDecimalCompraContextVar_4 = importeDecimalCompraContextVar_4;
	}

	public void setS_importeCompraContextVar_5(ContextVar s_importeCompraContextVar_5) {
		this.s_importeCompraContextVar_5 = s_importeCompraContextVar_5;
	}

	public void setImporteCompraContextVar_5(ContextVar importeCompraContextVar_5) {
		this.importeCompraContextVar_5 = importeCompraContextVar_5;
	}

	public void setImporteDecimalCompraContextVar_5(ContextVar importeDecimalCompraContextVar_5) {
		this.importeDecimalCompraContextVar_5 = importeDecimalCompraContextVar_5;
	}

	public void setFechaCompraDiaContextVar_5(ContextVar fechaCompraDiaContextVar_5) {
		this.fechaCompraDiaContextVar_5 = fechaCompraDiaContextVar_5;
	}

	public void setFechaCompraAnioContextVar_5(ContextVar fechaCompraAnioContextVar_5) {
		this.fechaCompraAnioContextVar_5 = fechaCompraAnioContextVar_5;
	}

	public void setFechaCompraMesContextVar_5(ContextVar fechaCompraMesContextVar_5) {
		this.fechaCompraMesContextVar_5 = fechaCompraMesContextVar_5;
	}

	public void setFechaCompraDiaContextVar_6(ContextVar fechaCompraDiaContextVar_6) {
		this.fechaCompraDiaContextVar_6 = fechaCompraDiaContextVar_6;
	}

	public void setImporteDecimalCompraContextVar_6(ContextVar importeDecimalCompraContextVar_6) {
		this.importeDecimalCompraContextVar_6 = importeDecimalCompraContextVar_6;
	}

	public void setImporteCompraContextVar_6(ContextVar importeCompraContextVar_6) {
		this.importeCompraContextVar_6 = importeCompraContextVar_6;
	}

	public void setFechaCompraAnioContextVar_6(ContextVar fechaCompraAnioContextVar_6) {
		this.fechaCompraAnioContextVar_6 = fechaCompraAnioContextVar_6;
	}

	public void setFechaCompraMesContextVar_6(ContextVar fechaCompraMesContextVar_6) {
		this.fechaCompraMesContextVar_6 = fechaCompraMesContextVar_6;
	}

	public void setS_disponibleRetirosContextVar(ContextVar s_disponibleRetirosContextVar) {
		this.s_disponibleRetirosContextVar = s_disponibleRetirosContextVar;
	}

	public void setDisponibleRetirosContextVar(ContextVar disponibleRetirosContextVar) {
		this.disponibleRetirosContextVar = disponibleRetirosContextVar;
	}

	public void setDisponibleRetirosDecimalContextVar(ContextVar disponibleRetirosDecimalContextVar) {
		this.disponibleRetirosDecimalContextVar = disponibleRetirosDecimalContextVar;
	}
}
