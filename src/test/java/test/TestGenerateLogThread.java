package test;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import utils.LogHandler;

public class TestGenerateLogThread implements Runnable {

	LogHandler loggerDb;

	public TestGenerateLogThread(LogHandler tmplog, String nombre) {
		Thread.currentThread().setName(nombre);
		this.loggerDb = tmplog;
	}

	@Override
	public void run() {
		String aString = "";
		for (int i = 0; i < 9000; i++) {
			aString += "A" + String.valueOf(i);
		}
		long startNanos = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			String astUid = "idasterisk." + String.valueOf(i);
			UUID id = UUID.randomUUID();

			if (i % 50 == 0) {
				loggerDb.addCallFlowToLog(astUid, this.getClass().getName(),
						"5060");
				// System.out.println("Insert CallFlow");
			}

			if (i % 10 == 0) {
				loggerDb.addStepToLog(astUid, "description", "StepTest",
						"10.64.42.158", aString, id.toString(), new Date());
				// System.out.println("Insert StepLog");
			}

			loggerDb.addVarToLog(
					astUid,
					"VarDescription",
					Thread.currentThread().getName() + " :valor: "
							+ String.valueOf(i), id.toString());

		}
		System.out
				.println(Thread.currentThread().getName()
						+ ":"
						+ TimeUnit.NANOSECONDS.toMillis(System.nanoTime()
								- startNanos));

	}
}
