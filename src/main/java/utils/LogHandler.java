package utils;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import main.Daemon;

import org.apache.log4j.Level;
import org.hibernate.FlushMode;
import org.hibernate.Session;

public class LogHandler implements Runnable {
	private boolean ShouldStop = false;
	private Session session = null;
	private static BlockingQueue<Object> databaseQueue = new LinkedBlockingQueue<Object>();

	public LogHandler() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public boolean isShouldStop() {
		return ShouldStop;
	}

	public void setShouldStop(boolean shouldStop) {
		ShouldStop = shouldStop;
	}

	public void run() {

		while (!ShouldStop) {
			try {
				this.persistLog();
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Daemon.getMiLog().log(Level.ERROR,
						LogHandler.class.getName() + "|" + e.getMessage());
			}

		}
		// session.close();
	}

	private void persistLog() {
		Object myObj;
		try {
			session.beginTransaction();
			while ((myObj = databaseQueue.poll()) != null) {
				session.persist(myObj);
			}
			session.flush();
			session.getTransaction().commit();
		} catch (Exception ex) {
			Daemon.getMiLog().log(
					Level.WARN,
					LogHandler.class.getName() + "|Reconnect: "
							+ ex.getMessage());
			session = null;
			session = HibernateUtil.getSessionFactory().openSession();
		}
	}

	public synchronized void addVarToLog(String astUid, String description,
			String value, String stepUUID) {
		ContextVarLog ctxVarLog = new ContextVarLog();
		ctxVarLog.setAstUid(astUid);
		ctxVarLog.setCalldate(new Date());
		ctxVarLog.setDescription(truncate(description, 255));
		ctxVarLog.setValue(truncate(value, 255));
		ctxVarLog.setUidstep(stepUUID);

		databaseQueue.add(ctxVarLog);
		ctxVarLog = null;
	}

	public synchronized void addStepToLog(String astUid, String description,
			String stepType, String server, String valor, String stepUUID,
			Date stepDate) {
		StepLog stLog = new StepLog();
		stLog.setAstUid(astUid);
		stLog.setCalldate(stepDate);
		stLog.setDescription(truncate(description, 255));
		stLog.setStepType(stepType);
		stLog.setServer(server);
		stLog.setValor(truncate(valor, 8192));
		stLog.setUidstep(stepUUID);

		databaseQueue.add(stLog);
		stLog = null;
	}

	public synchronized void addCallFlowToLog(String astUid,
			String callFlowClassName, String callerId) {
		CallFlowLog cfLog = new CallFlowLog();
		cfLog.setAstUid(astUid);
		cfLog.setCallerId(callerId);
		cfLog.setCallFlowClassName(callFlowClassName);

		databaseQueue.add(cfLog);
		cfLog = null;
	}

	public synchronized void addAuthLog(String astUid, String event,
			String user, String value) {
		AuthLog aLog = new AuthLog();
		aLog.setAstUid(astUid);
		aLog.setCalldate(new Date());
		aLog.setIdcrecer(user);
		aLog.setEvent(event);
		aLog.setValue(truncate(value, 255));

		databaseQueue.add(aLog);
		aLog = null;
	}

	private String truncate(String value, int length) {
		if (value != null) {
			if (value.length() > length) {
				return value.substring(0, length);
			} else {
				return value;
			}
		} else {
			return "";
		}
	}
}
