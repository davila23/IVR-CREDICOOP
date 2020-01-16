package utils;

import main.Daemon;

import org.apache.log4j.Level;

public class txtlog {

	public static void txtLog(String nombreClase, String accion, String dato,
			String astuid) {
		Daemon.getMiLog().log(Level.INFO,
				nombreClase + "|" + astuid + "|" + accion + "|" + dato);
	}
}
