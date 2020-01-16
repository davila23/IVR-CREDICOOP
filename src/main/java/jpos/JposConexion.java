package jpos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Level;

import main.Daemon;

public class JposConexion implements Runnable {

	private boolean ShouldStop = false;
	private BufferedReader bfi = null;
	private PrintWriter writer = null;
	private InetAddress address;
	private int port;
	private Socket socket;

	/*
	 * "autorizaciones"=>10601, "precargada"=>10602, "consultas"=>10603,
	 */
	public JposConexion(String strAddress, int _port) {
		try {
			address = InetAddress.getByName(strAddress);
			port = _port;
			Daemon.getMiLog().log(
					Level.INFO,
					JposConexion.class.getName() + "CREANDOTHREADJPOS|PORT|"
							+ port);
		} catch (UnknownHostException e) {
			Daemon.getMiLog().log(Level.INFO,
					JposConexion.class.getName() + "|" + e.getMessage());
		}
	}

	public synchronized String sendMsg(String msg) {
		String rta = "";
		char[] buffer = new char[230];
		try {
			Daemon.getMiLog().log(Level.INFO,
					"PORT|" + port + "|JPOSMSGSENDED|" + msg);
			if (writer == null || bfi == null) {
				rta = "EE";
				return rta;
			}
			writer.print(msg);
			writer.flush();
			bfi.read(buffer);
			rta = new String(buffer, 0, 230);
			if (rta == null || rta.isEmpty() || rta.trim().isEmpty()) {
				rta = "ERROR";
				this.reconnectSocket();
			}
		} catch (IOException e) {
			this.reconnectSocket();
			Daemon.getMiLog().log(Level.ERROR,
					JposConexion.class.getName() + "|" + e.getMessage());
		}
		Daemon.getMiLog().log(Level.INFO,
				"PORT|" + port + "|JPOSMSGRECEIVED|" + rta);
		return rta;
	}

	private void initialize() {
		boolean isConnected = false;
		while (!isConnected) {
			try {
				Daemon.getMiLog().log(
						Level.INFO,
						JposConexion.class.getName() + "|Conectando a:"
								+ address.toString() + ":" + port);
				socket = new Socket(address, port);
				socket.setSoTimeout(12000);
				writer = new PrintWriter(socket.getOutputStream(), true);
				bfi = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				isConnected = true;
			} catch (IOException e) {
				Daemon.getMiLog().log(Level.ERROR,
						JposConexion.class.getName() + "|" + e.getMessage());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					Daemon.getMiLog().log(
							Level.ERROR,
							JposConexion.class.getName() + "|"
									+ e1.getMessage());
				}
			}

		}
	}

	public void run() {
		this.initialize();
		String rta;
		while (!ShouldStop) {
			try {
				rta = this.sendMsg("9"
						+ String.format("%099d", 0)
						+ String.format("%029d",
								System.currentTimeMillis() / 1000L) + "0");
				Thread.sleep(30000L);
			} catch (InterruptedException e) {
				Daemon.getMiLog().log(Level.ERROR,
						JposConexion.class.getName() + "|" + e.getMessage());
			}
		}
	}

	public boolean isShouldStop() {
		return ShouldStop;
	}

	public void setShouldStop(boolean shouldStop) {
		ShouldStop = shouldStop;
	}

	private void reconnectSocket() {
		try {
			bfi.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			Daemon.getMiLog().log(Level.ERROR,
					JposConexion.class.getName() + "|" + e.getMessage());
		}
		this.initialize();
	}

}