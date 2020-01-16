/**
 * 
 */
package jpos.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author davila
 */
public class MessageUtils {
	public static String devuelveTrama(Vector<String> varIds) {
		String msg = "";
		for (String variable : varIds) {
			msg += variable;
		}

		String repeated = String.format("%0130d", 0);
		msg += repeated;
		return msg.substring(0, 130);
	}

	public static HashMap<String, String> getRespuestaJpos(String respuesta) {
		HashMap<String, String> respuestas = new HashMap<String, String>();
		if (respuesta.length() > 2) {
			int tipoMensaje = Integer.valueOf(respuesta.substring(0, 1));
			switch (tipoMensaje) {
			case 1:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(17, 19));
				break;
			case 2:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(17, 19));
				break;
			case 3:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(18, 20));
				break;
			case 4:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(48, 50));
				break;
			case 5:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(19, 21));
				break;
			case 6:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(49, 51));
				break;
			case 7:
				respuestas.put("CODIGORESPUESTA", respuesta.substring(19, 21));
				break;
			}
			respuestas.put("RESPUESTAJPOS", respuesta.trim());
		}
		if (respuestas.isEmpty()) {
			respuestas.put("CODIGORESPUESTA", "EE");
		}
		return respuestas;

	}
}
