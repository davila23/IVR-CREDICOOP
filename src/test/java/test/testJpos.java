package test;

import java.util.ArrayList;
import java.util.List;

import jpos.JposConexion;

public class testJpos {

	public static void main(String[] args) {
		TestJposServer s1 = new TestJposServer(10601);
		TestJposServer s2 = new TestJposServer(10602);
		TestJposServer s3 = new TestJposServer(10603);

		Thread TjposConsutasServer1 = new Thread(s1);
		TjposConsutasServer1.setName("Server1");
		TjposConsutasServer1.start();

		Thread TjposConsutasServer2 = new Thread(s2);
		TjposConsutasServer2.setName("Server2");
		TjposConsutasServer2.start();

		Thread TjposConsutasServer3 = new Thread(s3);
		TjposConsutasServer3.setName("Server3");
		TjposConsutasServer3.start();

		// JposConexion jposConsutas1 = new JposConexion("127.0.0.1",10601);
		// Thread TjposConsutas1 = new Thread(jposConsutas1);
		//
		// TjposConsutas1.setName("Autorizaciones");
		// TjposConsutas1.start();
		//
		// JposConexion jposConsutas2 = new JposConexion("127.0.0.1",10602);
		// Thread TjposConsutas2 = new Thread(jposConsutas2);
		//
		// TjposConsutas2.setName("Precargadas");
		// TjposConsutas2.start();
		//
		// JposConexion jposConsutas3 = new JposConexion("127.0.0.1",10603);
		// Thread TjposConsutas3 = new Thread(jposConsutas3);
		//
		// TjposConsutas3.setName("Credito");
		// TjposConsutas3.start();
		//
		// List<JposConexion> TJposList = new ArrayList<JposConexion>();
		// TJposList.add(jposConsutas1);
		// TJposList.add(jposConsutas2);
		// TJposList.add(jposConsutas3);
		// int i=0;
		while (true) {
			try {
				// for (JposConexion _jposConsultas : TJposList) {
				// _jposConsultas.sendMsg("1"+String.format("%0129d", i));
				// }
				Thread.sleep(1000L);
				// i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
