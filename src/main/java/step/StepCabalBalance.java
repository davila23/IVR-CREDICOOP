package step;

import ivr.CallContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import main.Daemon;

import org.apache.log4j.Level;
import org.asteriskjava.fastagi.AgiException;

import a380.guion.ArmaNumero;

import context.ContextVar;

import workflow.Context;

public class StepCabalBalance extends Step {

	private Map<String, String> varsToAsterisk = new HashMap<String, String>();

	public StepCabalBalance(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.CabalBalance;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		try {
			this.setGuion(context);
			for (Entry<String, String> varToSet : varsToAsterisk.entrySet()) {
				((CallContext) context).getChannel().setVariable(
						varToSet.getKey(), varToSet.getValue());
				Daemon.getMiLog().log(
						Level.INFO,
						"VARIABLE|" + varToSet.getKey() + "|"
								+ varToSet.getValue());
			}
			((CallContext) context).getChannel().setVariable("AGIRTA", "0");
			((CallContext) context).getChannel().setVariable("AGIERROR", "0");
		} catch (Exception e) {
			((CallContext) context).getChannel().setVariable("AGIRTA", "E");
			((CallContext) context).getChannel().setVariable("AGIERROR", "1");
		}

		return false;
	}

	private void setGuion(Context context) throws AgiException {
		ContextVar ctv = (ContextVar) ((CallContext) context)
				.getContextVarByName("retornoMsgJPOS");

		String sig_deu_pes_tc = "";
		String sig_deu_dol_tc = "";
		String ult_pag_min_imp_tc = "";
		String deu_dol_tc = "";
		String deu_pes_tc = "";

		ArmaNumero aNumero = new ArmaNumero();
		double gSaldo = Double.parseDouble(ctv.getVarValue().substring(77, 87)) / 100;
		int signosaldo = 0;
		String fecha = "";

		String saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		// limitecompra
		varsToAsterisk.put("AGISALDO0", saldo);

		// dispoplancuotas
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(98, 108)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO1", saldo);

		// limiteadelanto
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(87, 97)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO2", saldo);

		// fechavenc
		String ffechatmp = ctv.getVarValue().substring(71, 77);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA0", fecha);

		// saldoactual
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(21, 33)) / 100;
		signosaldo = ctv.getVarValue().substring(33, 34).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, signosaldo, false);
		sig_deu_pes_tc = ctv.getVarValue().substring(33, 34);
		deu_pes_tc = ctv.getVarValue().substring(21, 33);
		varsToAsterisk.put("AGISALDO3", saldo);

		// saldoactualdolares
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(34, 46)) / 100;
		signosaldo = ctv.getVarValue().substring(46, 47).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, signosaldo, false);
		sig_deu_dol_tc = ctv.getVarValue().substring(46, 47);
		deu_dol_tc = ctv.getVarValue().substring(34, 46);
		varsToAsterisk.put("AGISALDO4", saldo);

		// pagominimo
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(47, 59)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		ult_pag_min_imp_tc = ctv.getVarValue().substring(47, 59);
		varsToAsterisk.put("AGISALDO5", saldo);

		// pagomes
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(59, 66)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
		varsToAsterisk.put("AGISALDO6", saldo);

		// pagomesdolares
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(109, 116)) / 100;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, 0, false);
		varsToAsterisk.put("AGISALDO7", saldo);

		// fechaproxcierre
		ffechatmp = ctv.getVarValue().substring(139, 145);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA1", fecha);

		// fechaproxvenc
		ffechatmp = ctv.getVarValue().substring(185, 191);
		fecha = aNumero.armaGuionFecha(ffechatmp.substring(
				ffechatmp.length() - 2, ffechatmp.length())
				+ ffechatmp.substring(ffechatmp.length() - 4,
						ffechatmp.length() - 2) + ffechatmp.substring(0, 2));
		varsToAsterisk.put("AGIFECHA2", fecha);

		// saldoaldiadehoy
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(147, 159)) / 100;
		signosaldo = ctv.getVarValue().substring(159, 160).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 0, signosaldo, false);
		varsToAsterisk.put("AGISALDO8", saldo);

		// saldoaldiadehoydolares
		gSaldo = Double.parseDouble(ctv.getVarValue().substring(160, 172)) / 100;
		signosaldo = ctv.getVarValue().substring(172, 173).equals("+") ? 0 : 1;
		saldo = aNumero.armaGuionConMoneda(gSaldo, 1, signosaldo, false);
		varsToAsterisk.put("AGISALDO9", saldo);

		varsToAsterisk.put("sig_deu_pes_tc", sig_deu_pes_tc);
		varsToAsterisk.put("sig_deu_dol_tc", sig_deu_dol_tc);
		varsToAsterisk.put("ult_pag_min_imp_tc", ult_pag_min_imp_tc);
		varsToAsterisk.put("deu_dol_tc", deu_dol_tc);
		varsToAsterisk.put("deu_pes_tc", deu_pes_tc);

		if (Integer.parseInt(ctv.getVarValue().substring(116, 119)) > 0) {
			saldo = aNumero.armaGuionCuenta("000"
					+ ctv.getVarValue().substring(116, 119));
			varsToAsterisk.put("AGINUMERO0", saldo);
			gSaldo = Double.parseDouble(ctv.getVarValue().substring(119, 129)) / 100;
			saldo = aNumero.armaGuionConMoneda(gSaldo, 0, 0, false);
			varsToAsterisk.put("AGISALDO10", saldo);

			gSaldo = Double.parseDouble(ctv.getVarValue().substring(129, 139)) / 100;
			saldo = aNumero.armaGuionConMoneda(gSaldo, 1, 0, false);
			varsToAsterisk.put("AGISALDO11", saldo);
		}

	}

}
