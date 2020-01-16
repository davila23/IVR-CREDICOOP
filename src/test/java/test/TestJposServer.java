package test;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import jpos.JposConexion;
import main.Daemon;

import org.apache.log4j.Level;

public class TestJposServer implements Runnable {

	private int port;

	public TestJposServer(int port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(1);
		}

		Socket clientSocket = null;
		System.out.println("Waiting for connection.....");

		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}

		System.out.println("Connection successful");
		System.out.println("Waiting for input......");

		PrintWriter out;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String rta = "";
			while (true) {

				try {
					char[] buffer = new char[130];
					in.read(buffer);
					rta = new String(buffer, 0, 130);
					if (rta == null || rta.isEmpty() || rta.trim().isEmpty()) {
						rta = "ERROR";
					} else {
						if (rta.substring(0, 1).equals("9")) {
							out.print("9"
									+ String.format("%0199d", 0)
									+ String.format("%029d",
											System.currentTimeMillis() / 1000L)
									+ "0");
							out.flush();
							System.out.println("Respondo Ping:"
									+ "9"
									+ String.format("%0199d", 0)
									+ String.format("%029d",
											System.currentTimeMillis() / 1000L)
									+ "0");
						} else {
							String envio = "";
							switch (Integer.valueOf(rta.substring(0, 1))) {
							case 5:
								envio = "501603167200011667900000000010368+000000000000+0000000103680000000000001002160001009027000000000000000998985+00000000000000000000000000000016021600000000020573+000000000000+0000998985+0100316000000000000000000000000014575378902130";
								out.print(envio);
								out.flush();
								System.out.println("Envio:" + envio);
								break;
							case 6:
								envio = "6006042015432819055000258786377705180000011017400000000000000000000 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000147861683340";
								out.print(envio);
								out.flush();
								System.out.println("Envio:" + envio);
								break;
							default:
								System.out.println("Recibido: " + rta);
								envio = rta.substring(0, 48) + "00";
								out.print(String.format("%-230s", envio)
										.replace(' ', '0'));
								out.flush();
								System.out.println("Envio:" + envio);
								break;
							}
						}
					}
				} catch (IOException e) {
					System.out.println("Error testjpos");
				}

			}

			// out.close();
			// in.close();
			// clientSocket.close();
			// serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
