/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import ivr.CallContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import main.Daemon;

import org.apache.log4j.Level;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import sql.querys.SecuenciaA380;
import utils.HibernateUtil2;
import utils.IVRCMoraTemprana;
import utils.SociosRentaSuperior;
import utils.gcaConnectorSoap;
import workflow.Context;
import context.ContextVar;
import coop.bancocredicoop.common.domain.date.DateUnit;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.domain.time.TimeUnit;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.esb.integration.aerolineas.ConsultaEstadoAerolineasPlusRequest;
import coop.bancocredicoop.esb.integration.aerolineas.ConsultaEstadoAerolineasPlusResponse;
import coop.bancocredicoop.esb.integration.crecerxxi.cuenta.ClarifDeCuentasResponse;
import coop.bancocredicoop.esb.integration.crecerxxi.cuenta.ClarificacionCuentaRequest;
import coop.bancocredicoop.esb.integration.crecerxxi.cuenta.ClarificacionCuentaResponse;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;
import coop.bancocredicoop.service.proxy.ProxyFactory;
import coop.bancocredicoop.services.interfaces.server.domain.datosbasicos.DatosBasicosResponse;
import coop.bancocredicoop.services.interfaces.server.domain.listacandidatos.ListaCandidatosResponse;
import coop.bancocredicoop.services.interfaces.server.impl.DatosBasicosService;
import coop.bancocredicoop.services.interfaces.server.impl.ListaCandidatosService;

/**
 * 
 * @author Daniel Avila
 */

public class StepInitDniDB extends Step {

	private ContextVar contextVarDni = null;
	private Map<String, String> varsToAsterisk = new HashMap<String, String>();
	private Session session;

	public StepInitDniDB(UUID tmpid) {
		this.id = tmpid;
		this.StepType = step.StepFactory.StepType.InitDniDB;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if (contextVarDni == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		if (context.containsKey(contextVarDni.getId())) {
			session = HibernateUtil2.getSessionFactory().openSession();
			session.setFlushMode(FlushMode.ALWAYS);
			this.cargaVariables(((ContextVar) context.get(contextVarDni.getId()))
					.getVarValue());
			Daemon.getMiLog().log(
					Level.INFO,
					"INICIO VARIABLES DNI|"
							+ ((ContextVar) context.get(contextVarDni.getId()))
									.getVarValue());
			for (Entry<String, String> varToSet : varsToAsterisk.entrySet()) {
				((CallContext) context).getChannel().setVariable(
						varToSet.getKey(), varToSet.getValue());
				Daemon.getMiLog().log(
						Level.INFO,
						"VARIABLE|" + varToSet.getKey() + "|"
								+ varToSet.getValue());
			}
			Daemon.getMiLog().log(
					Level.INFO,
					"FIN VARIABLES DNI|"
							+ ((ContextVar) context.get(contextVarDni.getId()))
									.getVarValue());

			session.close();
		} else {
			throw new Exception("VARIABLE CONTEXTY NOT EXIST");
		}
		return true;

	}

	public void setContextVarDni(ContextVar tmpcontextVarDni) {
		this.contextVarDni = tmpcontextVarDni;
	}

	private void cargaVariables(String dni) {
		varsToAsterisk.put("PERFIL", "ERROR");
		varsToAsterisk.put("AGIERROR", "0");
		try {
			envioListaCandidatos(dni);
		} catch (Exception e) {
			Daemon.getMiLog().log(
					Level.ERROR,
					"INTENTO|1|DNI|" + dni + "|Carga Variables Error|"
							+ e.getMessage());
			try {
				envioListaCandidatos(dni);
			} catch (Exception e1) {
				varsToAsterisk.put("AGIERROR", "1");
				Daemon.getMiLog().log(
						Level.ERROR,
						"INTENTO|FINAL|DNI|" + dni + "|Carga Variables Error|"
								+ e.getMessage());
			}
		}
	}

	private List<ListaCandidatosResponse> getListacandidatos(String dni) {
		List<ListaCandidatosResponse> lista = new ArrayList<ListaCandidatosResponse>();
		List<ListaCandidatosResponse> tmplista = null;
		ListaCandidatosService impl = (ListaCandidatosService) ProxyFactory
				.createServiceProxy(ListaCandidatosService.class);
		try {
			tmplista = impl.findListaCandidatoByNroDocumento("F", dni);
		} catch (Exception e) {
			if (!e.getCause().getCause().getMessage().contains("ERROR-00001"))
				varsToAsterisk.put("AGIERROR", "1");
			Daemon.getMiLog().log(
					Level.ERROR,
					"DNI|" + dni + "|Lista Candidatos Error|"
							+ e.getCause().getCause().getMessage());
		}
		if (tmplista != null) {
			for (Iterator iterator = tmplista.iterator(); iterator.hasNext();) {
				ListaCandidatosResponse lr = (ListaCandidatosResponse) iterator
						.next();
				if (lr.getTipoCaptura().equals("A")
						|| lr.getTipoCaptura().equals("V"))
					if (lr.getEstadoInahbilitacion().intValue() != 30
							&& lr.getEstadoInahbilitacion().intValue() != 31)
						lista.add(lr);

			}
		}
		return lista;
	}

	private void cargaDatos(List<ListaCandidatosResponse> lc) {
		boolean esMultiple = lc.size() > 1;
		int cuentaSocio = 1;
		for (ListaCandidatosResponse listaCandidatosResponse : lc) {
			varsToAsterisk
					.put("vip" + (esMultiple ? "_" + cuentaSocio : ""),
							esVip(listaCandidatosResponse.getIdPersona()
									.asString()) ? "SI" : "NO");
			varsToAsterisk.put("aerolineas"
					+ (esMultiple ? "_" + cuentaSocio : ""),
					esAeroLineasPlus(listaCandidatosResponse.getIdPersona()
							.asString()) ? "SI" : "NO");

			cargaMap(getMoraTemprana(listaCandidatosResponse.getIdPersona()
					.asString()), esMultiple, cuentaSocio);

			cargaMap(getDatosBasicos(listaCandidatosResponse.getIdPersona()
					.asString()), esMultiple, cuentaSocio);
			cargaMap(getCuentas(listaCandidatosResponse.getIdPersona()
					.asString()), esMultiple, cuentaSocio);
			cargaMap(getDatosGca(listaCandidatosResponse.getIdPersona()
					.asString()), esMultiple, cuentaSocio);

			cuentaSocio++;
		}
	}

	private boolean esVip(String idCrecer) {
		boolean retval = false;
		try {
			session.getTransaction().begin();
			Query query = session
					.createQuery("from SociosRentaSuperior where idcrecer = :crecerId");

			query.setParameter("crecerId", Integer.parseInt(idCrecer));

			List<SociosRentaSuperior> result = query.list();
			session.getTransaction().commit();
			if (result.size() > 0) {
				retval = true;
			}
		} catch (Exception e) {
			// varsToAsterisk.put("AGIERROR", "1");
			Daemon.getMiLog().log(
					Level.ERROR,
					"IdCrecer|" + idCrecer
							+ "|Es Vip SociosRentaSuperior Error|"
							+ e.getMessage());
		}
		return retval;
	}

	private Map<String, String> getMoraTemprana(String idCrecer) {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("idcrecer", idCrecer);
		try {

			session.getTransaction().begin();

			Query query = session
					.createQuery("from IVRCMoraTemprana where id_T24 = :crecerId");

			query.setParameter("crecerId",
					java.math.BigInteger.valueOf(Long.parseLong(idCrecer)));

			List<IVRCMoraTemprana> result = query.list();

			session.getTransaction().commit();

			if (result.size() > 0) {
				retMap.put("MoraMoroso", "SI");
				retMap.put("MORAhorario", result.get(0).getHorario());
				retMap.put("MORAonline",
						String.valueOf(result.get(0).getOnLine()));
				retMap.put("MORAtelestudio", result.get(0).getTel_Estudio());
				retMap.put("MORAidestudio",
						String.valueOf(result.get(0).getId_Estudio()));
			} else {
				retMap.put("MoraMoroso", "NO");
			}
		} catch (Exception e) {
			// varsToAsterisk.put("AGIERROR", "1");
			Daemon.getMiLog().log(
					Level.ERROR,
					"IdCrecer|" + idCrecer + "|Mora Temprana Error|"
							+ e.getMessage());
		}

		return retMap;
	}

	private Map<String, String> getDatosBasicos(String idCrecer) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			envioDatosBasicos(idCrecer, retMap);
		} catch (Exception e) {
			Daemon.getMiLog().log(
					Level.ERROR,
					"INTENTO|1|IdCrecer|" + idCrecer + "|Datos Basicos Error|"
							+ e.getMessage());
			try {
				envioDatosBasicos(idCrecer, retMap);
			} catch (Exception e1) {
				varsToAsterisk.put("AGIERROR", "1");
				Daemon.getMiLog().log(
						Level.ERROR,
						"INTENTO|FINAL|IdCrecer|" + idCrecer
								+ "|Datos Basicos Error|" + e.getMessage());

			}

		}
		return retMap;
	}

	private Map<String, String> getCuentas(String idCrecer) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			ClarificacionCuentaRequest request = new ClarificacionCuentaRequest();
			request.setTipoMensaje("TR");
			request.setNroSecuencia(new Numeric(SecuenciaA380
					.obtenerSecuenciaA380()));
			request.setFechaNegocio(new DateUnit(new DateTime()
					.toString("yyyyMMdd")));
			request.setFechaProceso(new DateUnit(new DateTime()
					.toString("yyyyMMdd")));
			request.setHoraProceso(new TimeUnit(new DateTime()
					.toString("HH:mm:ss")));
			request.setIdOperador(new Numeric(0));
			request.setIdCanal("WEB");
			request.setIdESB("");
			request.setFecNego(new DateUnit(new DateTime().toString("yyyyMMdd")));
			request.setIdPersona(new Numeric(idCrecer));

			MessageSender sender = (MessageSender) ServiceProvider
					.getInstance().lookupService("msgClarificacionCuenta");
			ClarificacionCuentaResponse response = (ClarificacionCuentaResponse) sender
					.send(request, ClarificacionCuentaResponse.class);
			int idCuenta = 1;
			for (ClarifDeCuentasResponse cue : response
					.getClarificacionesDeCuentas()) {
				retMap.put("CUENTA_" + idCuenta, cue.getCuenta().toString());
				idCuenta++;
			}
		} catch (Exception e) {
			// varsToAsterisk.put("AGIERROR", "1");//Arreglo sociomultiple
			Daemon.getMiLog().log(
					Level.ERROR,
					"IdCrecer|" + idCrecer + "|Clarificacion de cuentas Error|"
							+ e.getMessage());
		}
		return retMap;
	}

	private boolean esAeroLineasPlus(String idCrecer) {
		boolean retval = false;
		try {
			ConsultaEstadoAerolineasPlusRequest request = new ConsultaEstadoAerolineasPlusRequest();
			request.setTipoMensaje("TR");
			request.setNroSecuencia(new Numeric(SecuenciaA380
					.obtenerSecuenciaA380()));
			request.setFechaNegocio(new DateUnit(new DateTime()
					.toString("yyyyMMdd")));
			request.setFechaProceso(new DateUnit(new DateTime()
					.toString("yyyyMMdd")));
			request.setHoraProceso(new TimeUnit(new DateTime()
					.toString("HH:mm:ss")));
			request.setIdOperador(new Numeric(0));
			request.setIdCanal("WEB");
			request.setIdESB("");
			request.setIdCrecer(new Numeric(idCrecer));

			MessageSender sender = (MessageSender) ServiceProvider
					.getInstance().lookupService("msgAeroLineasPlus");
			ConsultaEstadoAerolineasPlusResponse response = (ConsultaEstadoAerolineasPlusResponse) sender
					.send(request, ConsultaEstadoAerolineasPlusResponse.class);
			retval = response.getInfo().isAdherido();
		} catch (Exception e) {
			// varsToAsterisk.put("AGIERROR", "1");
			Daemon.getMiLog().log(
					Level.ERROR,
					"IdCrecer|" + idCrecer + "|Aerolineas Plus Error|"
							+ e.getMessage());
		}
		return retval;
	}

	private Map<String, String> getDatosGca(String idCrecer) {
		Map<String, String> retMap = new HashMap<String, String>();
		try {
			gcaConnectorSoap gcacs = gcaConnectorSoap.getInstance();
			String idSugar = gcacs.getIdSugar(idCrecer);
			retMap.put("idsugar", idSugar);

			gcacs = gcaConnectorSoap.getInstance();
			String retornoGCA = gcacs
					.getObtenerReclamosGestionesCalidad(idCrecer);
			retMap.put("sreclamos", retornoGCA);

			gcacs = gcaConnectorSoap.getInstance();
			retornoGCA = gcacs.GetCountSalesOpportunityByCampaignType(idSugar);
			retMap.put("OPVentas", retornoGCA);
		} catch (Exception e) {
			Daemon.getMiLog().log(
					Level.ERROR,
					"IdCrecer|" + idCrecer + "|Consultas Gca Error|"
							+ e.getMessage());
		}
		return retMap;
	}

	private void cargaMap(Map<String, String> aCargar, boolean esMultiple,
			int nroSocio) {
		for (Entry<String, String> varToLoad : aCargar.entrySet()) {
			varsToAsterisk.put(varToLoad.getKey()
					+ (esMultiple ? "_" + nroSocio : ""), varToLoad.getValue());
		}
	}

	private void envioListaCandidatos(String dni) {
		List<ListaCandidatosResponse> lc = this.getListacandidatos(dni);
		if (lc != null && !lc.isEmpty()) {
			if (lc.size() == 1) {
				varsToAsterisk.put("PERFIL", "SOCIO");
				cargaDatos(lc);
			} else {
				varsToAsterisk.put("PERFIL", "SOCIOMULTIPLE");
				varsToAsterisk.put("PERFILES", String.valueOf(lc.size()));
				cargaDatos(lc);
			}

		} else {
			if (lc == null || lc.isEmpty()) {
				varsToAsterisk.put("PERFIL", "NOSOCIO");
			}
		}
	}

	private void envioDatosBasicos(String idCrecer, Map<String, String> retMap)
			throws ServiceException {
		DatosBasicosService impl = (DatosBasicosService) ProxyFactory
				.createServiceProxy(DatosBasicosService.class);
		// .createProxy(DatosBasicosService.class);
		DatosBasicosResponse response = impl.findDatosBasicosById(new Numeric(
				idCrecer), "N");
		String soapfdn = response.getFechaNacimiento().toString()
				.substring(6, 8)
				+ response.getFechaNacimiento().toString().substring(4, 6)
				+ response.getFechaNacimiento().toString().substring(0, 4);

		String fdn = response.getFechaNacimiento().toString().substring(6, 8)
				+ response.getFechaNacimiento().toString().substring(4, 6)
				+ response.getFechaNacimiento().toString().substring(2, 4);
		;
		retMap.put("SOAPFDN", soapfdn);
		retMap.put("FDN", fdn);
		retMap.put("SOAPDNI", response.getClaveBup().getText());
	}
}
