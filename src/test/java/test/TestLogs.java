package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import utils.LogHandler;

public class TestLogs {
	@Test
	public void ConcurrencyTest() {
		LogHandler loggerDb = new LogHandler();
		Thread loggerDbServiceThread = new Thread(loggerDb);
		loggerDbServiceThread.setName("LoggerDB");
		// loggerDbServiceThread.start();

		ExecutorService threadPool = Executors.newFixedThreadPool(11);
		threadPool.submit(loggerDbServiceThread);
		for (int i = 0; i < 10; i++) {
			threadPool.submit(new TestGenerateLogThread(loggerDb, "Executado_"
					+ String.valueOf(i)));
		}

		try {
			threadPool.shutdown();
			while (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
				System.out.println("Esperando");
			}
			System.out.println("Fin");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
