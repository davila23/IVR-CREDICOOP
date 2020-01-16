/**
 * 
 */
package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jpos.JposConexion;

import lanzador.Ivrlanzador;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sql.querys.HibernateUtilHorarios;
import sql.querys.HorariosResponse;
import utils.HibernateUtil;
import utils.HibernateUtil2;
import utils.LogHandler;
import utils.ServiceConfig;

/**
 * @author davila
 * 
 */
public class Daemon {

	/**
	 * @param args
	 */
	static Logger miLog;
	static Thread loggerDbServiceThread;
	static Thread JposAutorizacionesServiceThread;
	static Thread JposPrecargadasServiceThread;
	static Thread JposConsultasServiceThread;
	static JposConexion JposAutorizaciones;
	static JposConexion JposConsultas;
	static JposConexion JposPrecargadas;
	static LogHandler loggerDb;
	static Session dbsession;
	static Session dbsessionHorarios;
	static HashMap<String, String> serviceSConfigs = new HashMap<String, String>();

	public static void main(String[] args) {

		miLog = Logger.getLogger(Daemon.class.getName());
		// PropertyConfigurator.configure("log4j.properties");
		loggerDb = new LogHandler();
		loggerDbServiceThread = new Thread(loggerDb);
		loggerDbServiceThread.setName("LoggerDB");
		loggerDbServiceThread.start();

		dbsession = HibernateUtil.getSessionFactory().openSession();
		cargaConfigs();
		dbsession.close();

		JposAutorizaciones = new JposConexion(
				serviceSConfigs.get("JPOSSERVER"),
				Integer.parseInt(serviceSConfigs.get("JPOSAUTORIZACIONESPORT")));
		JposConsultas = new JposConexion(serviceSConfigs.get("JPOSSERVER"),
				Integer.parseInt(serviceSConfigs.get("JPOSCONSULTASPORT")));
		JposPrecargadas = new JposConexion(serviceSConfigs.get("JPOSSERVER"),
				Integer.parseInt(serviceSConfigs.get("JPOSPRECARGADASPORT")));

		JposAutorizacionesServiceThread = new Thread(JposAutorizaciones);
		JposAutorizacionesServiceThread.setName("JposAutorizaciones");
		JposAutorizacionesServiceThread.start();

		JposConsultasServiceThread = new Thread(JposConsultas);
		JposConsultasServiceThread.setName("JposConsultas");
		JposConsultasServiceThread.start();

		JposPrecargadasServiceThread = new Thread(JposPrecargadas);
		JposPrecargadasServiceThread.setName("JposPrecargadas");
		JposPrecargadasServiceThread.start();

		Thread onlineServiceThread = new Thread(new Ivrlanzador());
		onlineServiceThread.setName("IvrServicioLanzador");
		onlineServiceThread.start();

		while (true) {
			try {
				Thread.sleep(20000L);
				recargaConfigs();
			} catch (Exception ex) {
				miLog.log(Level.FATAL, null, ex);
			}
		}

	}

	public static synchronized LogHandler getDbLog() {
		return loggerDb;
	}

	public static synchronized Logger getMiLog() {
		return miLog;
	}

	public static synchronized String getConfig(String sKey) {
		return serviceSConfigs.get(sKey);

	}

	public static synchronized HorariosResponse getTimeCondition(
			String idEmpresa, String idServicio) {
		dbsessionHorarios = HibernateUtilHorarios.getSessionFactoryHorarios()
				.openSession();
		dbsessionHorarios.setFlushMode(FlushMode.ALWAYS);
		HorariosResponse hr = new HorariosResponse();
		hr.loadTimeCondition(dbsessionHorarios, idEmpresa, idServicio);
		dbsessionHorarios.close();
		return hr;
	}

	public static JposConexion getJposAutorizaciones() {
		return JposAutorizaciones;
	}

	public static JposConexion getJposConsultas() {
		return JposConsultas;
	}

	public static JposConexion getJposPrecargadas() {
		return JposPrecargadas;
	}

	private static void recargaConfigs() {
		dbsession = HibernateUtil.getSessionFactory().openSession();
		List<ServiceConfig> scs = dbsession.createQuery(
				"FROM ServiceConfig SC WHERE SC.configKey = 'RELOADCONFIGS'")
				.list();
		if (scs.get(0).getConfigValue().equals("1")) {
			cargaConfigs();
			scs.get(0).setConfigValue("0");
			dbsession.beginTransaction();
			dbsession.persist(scs.get(0));
			dbsession.flush();
			dbsession.getTransaction().commit();
		}
		dbsession.close();
	}

	/**
	 * 
	 */
	private static void cargaConfigs() {
		List serviceConfigs = dbsession.createQuery("FROM ServiceConfig")
				.list();
		for (Iterator iterator1 = serviceConfigs.iterator(); iterator1
				.hasNext();) {
			ServiceConfig sc = (ServiceConfig) iterator1.next();
			serviceSConfigs.put(sc.getConfigKey(), sc.getConfigValue());
		}
	}
}
