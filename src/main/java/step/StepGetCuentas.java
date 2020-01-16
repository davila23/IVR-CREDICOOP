package step;

import ivr.CallContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiException;
import org.hibernate.Query;
import org.hibernate.Session;

import context.ContextVar;
import utils.HibernateUtil2;
import utils.Usuarioclave;
import utils.Usuariocuenta;
import workflow.Context;

public class StepGetCuentas extends Step {

	private ContextVar idCrecerContextVar = null;
	private ContextVar cuentasContextVar = null;
	private ContextVar cuentasPredeterminadasContextVar = null;

	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private ContextVar tieneCuentasPredeterminadasContextVar = null;
	private List<String> cuentas = new ArrayList<String>();

	public StepGetCuentas(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.GetCuentas;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		String idCrecer = ((ContextVar) context.get(idCrecerContextVar.getId()))
				.getVarValue();

		cuentasContextVar.setVarValue(obtieneCuentas(context));
		context.put(cuentasContextVar.getId(), cuentasContextVar);

		String ctasPredeterminadas = obtieneCuentasPredeterminadas(idCrecer);
		cuentasPredeterminadasContextVar.setVarValue(ctasPredeterminadas);
		context.put(cuentasPredeterminadasContextVar.getId(),
				cuentasPredeterminadasContextVar);

		if (ctasPredeterminadas.length() > 0) {
			tieneCuentasPredeterminadasContextVar.setVarValue("1");
			context.put(tieneCuentasPredeterminadasContextVar.getId(),
					tieneCuentasPredeterminadasContextVar);
		}

		return false;
	}

	private String obtieneCuentas(Context context) throws AgiException {
		String retval = "";
		int i = 1;
		String cuenta = "00";
		while (cuenta != null) {
			try {
				cuenta = ((CallContext) context).getChannel().getVariable(
						"CUENTA_" + i);
			} catch (AgiException e) {
				cuenta = "";
				((CallContext) context).getChannel().setVariable("AGIRTA", "1");
			}
			if (cuenta != null) {
				retval = retval + cuenta + "|";
				cuentas.add(cuenta);
			}
			i++;
		}
		if (retval.length() > 0)
			retval = retval.substring(0, retval.length() - 1);

		return retval;
	}

	private String obtieneCuentasPredeterminadas(String idCrecer) {
		String retval = "";
		Session session = HibernateUtil2.getSessionFactory().openSession();
		Query query = session
				.createQuery("from Usuariocuenta where idcrecer = :idcrecer");
		query.setParameter("idcrecer", Long.parseLong(idCrecer));

		List<Usuariocuenta> list = query.list();
		session.close();
		for (Usuariocuenta usuariocuenta : list) {
			if (cuentas.contains(usuariocuenta.getCuenta()))
				retval = retval + usuariocuenta.getCuenta() + "|";
		}

		if (retval.length() > 0)
			retval = retval.substring(0, retval.length() - 1);

		return retval;
	}

	public UUID getNextStepIsTrue() {
		return nextStepIsTrue;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public UUID getNextStepIsFalse() {
		return nextStepIsFalse;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}

	public void setIdCrecerContextVar(ContextVar idCrecerContextVar) {
		this.idCrecerContextVar = idCrecerContextVar;
	}

	public void setCuentasContextVar(ContextVar cuentasContextVar) {
		this.cuentasContextVar = cuentasContextVar;
	}

	public void setCuentasPredeterminadasContextVar(
			ContextVar cuentasPredeterminadasContextVar) {
		this.cuentasPredeterminadasContextVar = cuentasPredeterminadasContextVar;
	}

	public void setTieneCuentasPredeterminadasContextVar(
			ContextVar tieneCuentasPredeterminadasContextVar) {
		this.tieneCuentasPredeterminadasContextVar = tieneCuentasPredeterminadasContextVar;
	}

}
