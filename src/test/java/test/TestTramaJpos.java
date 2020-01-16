package test;

import java.util.HashMap;
import java.util.Map;

import ivr.CallContext;

import org.asteriskjava.fastagi.AgiException;
import org.junit.Test;

import workflow.Context;
import a380.guion.ArmaNumero;
import context.ContextVar;

public class TestTramaJpos {
	@Test
	public void TestSaldo() {
		Map<String, String> varsToAsterisk = new HashMap<String, String>();
		String retornoJpos = "501589657006043346100000004147472+000000000000+0000013498000000300000000609160000908339000090833900002970967+00000000080000487612000000000019091600000005103661+000000000000+0002970967+0061016000000000000000000000001472852463349390";
		String sig_deu_pes_tc = "";
		String sig_deu_dol_tc = "";
		String ult_pag_min_imp_tc = "";
		String deu_dol_tc = "";
		String deu_pes_tc = "";

		ArmaNumero aNumero = new ArmaNumero();
		double gSaldo = Double.parseDouble(retornoJpos.substring(77, 87)) / 100;
		int signosaldo = 0;
		String fecha = "";

		String saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		// limitecompra
		varsToAsterisk.put("AGISALDO0", saldo);

		// dispoplancuotas
		gSaldo = Double.parseDouble(retornoJpos.substring(98, 108)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO1", saldo);

		// limiteadelanto
		gSaldo = Double.parseDouble(retornoJpos.substring(87, 97)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO2", saldo);

		// fechavenc
		String ffechatmp = retornoJpos.substring(71, 77);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA0", fecha);

		// saldoactual
		gSaldo = Double.parseDouble(retornoJpos.substring(21, 33)) / 100;
		signosaldo = retornoJpos.substring(33, 34).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, signosaldo, false);
		sig_deu_pes_tc = retornoJpos.substring(33, 34);
		deu_pes_tc = retornoJpos.substring(21, 33);
		varsToAsterisk.put("AGISALDO3", saldo);

		// saldoactualdolares
		gSaldo = Double.parseDouble(retornoJpos.substring(34, 46)) / 100;
		signosaldo = retornoJpos.substring(46, 47).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, signosaldo, false);
		sig_deu_dol_tc = retornoJpos.substring(46, 47);
		deu_dol_tc = retornoJpos.substring(34, 46);
		varsToAsterisk.put("AGISALDO4", saldo);

		// pagominimo
		gSaldo = Double.parseDouble(retornoJpos.substring(47, 59)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		ult_pag_min_imp_tc = retornoJpos.substring(47, 59);
		varsToAsterisk.put("AGISALDO5", saldo);

		// pagomes
		gSaldo = Double.parseDouble(retornoJpos.substring(59, 66)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO6", saldo);

		// pagomesdolares
		gSaldo = Double.parseDouble(retornoJpos.substring(109, 116)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, 0, false);
		varsToAsterisk.put("AGISALDO7", saldo);

		// fechaproxcierre
		ffechatmp = retornoJpos.substring(139, 145);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA1", fecha);

		// fechaproxvenc
		ffechatmp = retornoJpos.substring(185, 191);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA2", fecha);

		// saldoaldiadehoy
		gSaldo = Double.parseDouble(retornoJpos.substring(147, 159)) / 100;
		signosaldo = retornoJpos.substring(159, 160).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, signosaldo, false);
		varsToAsterisk.put("AGISALDO8", saldo);

		// saldoaldiadehoydolares
		gSaldo = Double.parseDouble(retornoJpos.substring(160, 172)) / 100;
		signosaldo = retornoJpos.substring(172, 173).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, signosaldo, false);
		varsToAsterisk.put("AGISALDO9", saldo);

		varsToAsterisk.put("sig_deu_pes_tc", sig_deu_pes_tc);
		varsToAsterisk.put("sig_deu_dol_tc", sig_deu_dol_tc);
		varsToAsterisk.put("ult_pag_min_imp_tc", ult_pag_min_imp_tc);
		varsToAsterisk.put("deu_dol_tc", deu_dol_tc);
		varsToAsterisk.put("deu_pes_tc", deu_pes_tc);

		if (Integer.parseInt(retornoJpos.substring(116, 119)) > 0) {
			saldo = aNumero.armaGuionCuenta("000"
					+ retornoJpos.substring(116, 119));
			varsToAsterisk.put("AGINUMERO0", saldo);
			gSaldo = Double.parseDouble(retornoJpos.substring(119, 129)) / 100;
			saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
			varsToAsterisk.put("AGISALDO10", saldo);

			gSaldo = Double.parseDouble(retornoJpos.substring(129, 139)) / 100;
			saldo = aNumero.armaGuionConMoneda(gSaldo, 1, 0, false);
			varsToAsterisk.put("AGISALDO11", saldo);
		}

	}
}
