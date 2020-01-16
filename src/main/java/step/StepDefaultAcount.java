package step;

import ivr.CallContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.fastagi.AgiException;
import org.hibernate.Query;
import org.hibernate.Session;

import a380.utils.SaldoCuenta;

import context.ContextVar;
import utils.HibernateUtil2;
import utils.Usuarioclave;
import utils.Usuariocuenta;
import workflow.Context;

public class StepDefaultAcount extends Step {

	private ContextVar idCrecerContextVar = null;
	private ContextVar cuentaContextVar = null;
	private ContextVar cuentasContextVar = null;
	private ContextVar tipoCuentaContextVar = null;
	private UUID nextStepIsTrue = null;
	private UUID nextStepIsFalse = null;
	private ContextVar esCuentaPredeterminadaContextVar = null;
	private ContextVar cantidadDeCuentasEncontradasContextVar = null;
	private ContextVar sucursalContextVar = null;

	public StepDefaultAcount(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.DefaultAcount;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (esCuentaPredeterminadaContextVar == null
				|| idCrecerContextVar == null || cuentaContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		SaldoCuenta sld = new SaldoCuenta();
		String idCrecer = ((ContextVar) context.get(idCrecerContextVar.getId()))
				.getVarValue();

		String esCuentaPredeterminada = ((ContextVar) context
				.get(esCuentaPredeterminadaContextVar.getId())).getVarValue();

		String cuenta = ((ContextVar) context.get(cuentaContextVar.getId()))
				.getVarValue();

		String sucursal = ((ContextVar) context.get(sucursalContextVar.getId()))
				.getVarValue();

		String strCuentas = ((ContextVar) context
				.get(cuentasContextVar.getId())).getVarValue();

		String tipoCuenta = sld.convierteTipoCuenta(((ContextVar) context
				.get(tipoCuentaContextVar.getId())).getVarValue());

		ArrayList<String> cuentas = buscaCuentaPorTipoYNumero(
				new ArrayList<String>(Arrays.asList(strCuentas.split("\\|"))),
				cuenta, tipoCuenta, sucursal);

		switch (cuentas.size()) {
		case 0:
			cantidadDeCuentasEncontradasContextVar.setVarValue("0");
			context.put(cantidadDeCuentasEncontradasContextVar.getId(),
					cantidadDeCuentasEncontradasContextVar);
			break;
		case 1:
			if (esCuentaPredeterminada.equals("1")) {
				setCuentasDefault(idCrecer, cuentas.get(0));
			}
			if (esCuentaPredeterminada.equals("0")) {
				setCuentasNoDefault(idCrecer, cuentas.get(0));
			}

			cantidadDeCuentasEncontradasContextVar.setVarValue("1");
			context.put(cantidadDeCuentasEncontradasContextVar.getId(),
					cantidadDeCuentasEncontradasContextVar);
			break;

		default:

			cantidadDeCuentasEncontradasContextVar.setVarValue("2");
			context.put(cantidadDeCuentasEncontradasContextVar.getId(),
					cantidadDeCuentasEncontradasContextVar);
			break;
		}
		sucursalContextVar.setVarValue("");
		context.put(sucursalContextVar.getId(), sucursalContextVar);
		return false;
	}

	private Usuariocuenta obtieneUsuarioCuenta(String idCrecer, String cuenta) {
		Usuariocuenta retval = null;
		Session session = HibernateUtil2.getSessionFactory().openSession();
		Query query = session
				.createQuery("from Usuariocuenta where idcrecer = :idcrecer and cuenta = :cuenta");
		query.setParameter("idcrecer", Long.parseLong(idCrecer));
		query.setParameter("cuenta", cuenta);

		List<Usuariocuenta> list = query.list();
		if (list.size() > 0)
			retval = list.get(0);
		session.close();
		return retval;
	}

	private void setCuentasDefault(String idCrecer, String cuenta) {
		Usuariocuenta ucu = obtieneUsuarioCuenta(idCrecer, cuenta);
		if (ucu == null) {
			Session session = HibernateUtil2.getSessionFactory().openSession();
			session.getTransaction().begin();
			ucu = new Usuariocuenta();
			ucu.setCuenta(cuenta);
			ucu.setIdcrecer(Long.parseLong(idCrecer));
			session.persist(ucu);
			session.getTransaction().commit();
			session.close();
		}
	}

	private void setCuentasNoDefault(String idCrecer, String cuenta) {
		Usuariocuenta ucu = obtieneUsuarioCuenta(idCrecer, cuenta);
		if (ucu != null) {
			Session session = HibernateUtil2.getSessionFactory().openSession();
			session.getTransaction().begin();
			session.delete(ucu);
			session.getTransaction().commit();
			session.close();
		}
	}

	private ArrayList<String> buscaCuentaPorTipoYNumero(
			ArrayList<String> cuentas, String cta, String tipoCuenta,
			String sucursal) {
		ArrayList<String> cuenta = new ArrayList<String>();
		if (sucursal.isEmpty()) {

			// Tipos de cuentas: 0 CC:1 CA$: 2 CAU$S
			for (String ca : cuentas) {
				if (ca.endsWith(cta) && ca.startsWith(tipoCuenta)) {
					cuenta.add(ca);
				}
			}
		} else {
			for (String ca : cuentas) {
				if (ca.endsWith(String.format("%03d", Long.valueOf(sucursal))
						+ String.format("%07d", Long.valueOf(cta)))
						&& ca.startsWith(tipoCuenta)) {
					cuenta.add(ca);
				}
			}
		}
		return cuenta;
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

	public void setEsCuentaPredeterminadaContextVar(
			ContextVar esCuentaPredeterminadaContextVar) {
		this.esCuentaPredeterminadaContextVar = esCuentaPredeterminadaContextVar;
	}

	public void setCuentacontextVar(ContextVar cuentaContextVar) {
		this.cuentaContextVar = cuentaContextVar;
	}

	public void setIdCrecercontextVar(ContextVar idCrecerContextVar) {
		this.idCrecerContextVar = idCrecerContextVar;
	}

	public void setCuentasContextVar(ContextVar cuentasContextVar) {
		this.cuentasContextVar = cuentasContextVar;
	}

	public void setTipoCuentaContextVar(ContextVar tipoCuentaContextVar) {
		this.tipoCuentaContextVar = tipoCuentaContextVar;
	}

	public void setCantidadDeCuentasEncontradasContextVar(
			ContextVar cantidadDeCuentasEncontradasContextVar) {
		this.cantidadDeCuentasEncontradasContextVar = cantidadDeCuentasEncontradasContextVar;
	}

	public void setSucursalContextVar(ContextVar sucursalContextVar) {
		this.sucursalContextVar = sucursalContextVar;
	}

}
