package lanzador;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.fastagi.ResourceBundleMappingStrategy;

public class Ivrlanzador implements Runnable {
	private DefaultAgiServer aserver;

	private void runIvr() throws IOException {
		Thread.currentThread().setName("CallFlowServer");
		aserver = new DefaultAgiServer();

		ResourceBundleMappingStrategy rb = new ResourceBundleMappingStrategy(
				"fastagi-mapping", false);
		aserver.setMappingStrategy(rb);
		aserver.setPort(9890);
		aserver.setPoolSize(60);
		aserver.setMaximumPoolSize(120);
		aserver.startup();
	}

	public void run() {
		try {
			runIvr();
		} catch (IOException ex) {
			Logger.getLogger(Ivrlanzador.class.getName()).log(Level.ERROR,
					null, ex);
		} catch (IllegalStateException ex) {
			Logger.getLogger(Ivrlanzador.class.getName()).log(Level.ERROR,
					null, ex);
		}
	}

	public void stop() {
		aserver.shutdown();
	}
}
