/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import ivr.CallContext;
import ivr.CallFlow;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import workflow.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import main.Daemon;
import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import context.ContextVar;
import coop.bancocredicoop.service.core.sender.socket.MessageSender;
import coop.bancocredicoop.service.load.ServiceProvider;

/**
 * 
 * @author Avila Daniel
 */

public class StepClearPil extends Step {

	private ContextVar tarjetaContextVar;
	private ContextVar retornoClearPilContextVar;
	private UUID nextStepIsFalse;
	private UUID nextStepIsTrue;
	private static MessageSender service;

	public StepClearPil(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.ClearPil;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (tarjetaContextVar == null) {
			throw new Exception("INVALID CONTEXT VARIABLENAME");
		}
		try {

			String fecha = new DateTime().toString("yyyy-MM-dd%20HH:mm:ss");
			
			ApplicationContext contexto = new ClassPathXmlApplicationContext("file:/usr/local/bin/callflow/cfg/serviceManagerRegistry.xml");
			 String url_= (String) contexto.getBean("pintasChannel");
			 
			URL url = new URL(
					"http://"+ url_ +".bancocredicoop.coop/pincabal-web/clearPIL?card="
							+ tarjetaContextVar.getVarValue().toString()
							+ "&operator=IVR" + "&date=" + fecha
							+ "&operation=9999&pinlink=1111");

			URLConnection conexion = url.openConnection();
			conexion.connect();
			conexion.setConnectTimeout(5);

			InputStream is = conexion.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String respuesta;

			String digitos = "";

			while ((respuesta = br.readLine()) != null) {

				if (respuesta.contains("code")) {

					char[] letras = respuesta.toCharArray();

					for (char letra : letras) {
						if (Character.isDigit(letra)) {
							digitos += letra;
						}
					}
				}
			}
			retornoClearPilContextVar.setVarValue(digitos);
			this.setNextstep(nextStepIsTrue);
		} catch (IOException e) {
			e.printStackTrace();
			this.setNextstep(nextStepIsFalse);
		}
		return false;
	}

	public void setRetornoClearPilContextVar(
			ContextVar retornoClearPilContextVar) {
		this.retornoClearPilContextVar = retornoClearPilContextVar;
	}

	public void setTarjetaContextVar(ContextVar tarjetaContextVar) {
		this.tarjetaContextVar = tarjetaContextVar;
	}

	public UUID getNextStepIsTrue() {
		return nextStepIsTrue;
	}

	public void setNextStepIsTrue(UUID nextStepIsTrue) {
		this.nextStepIsTrue = nextStepIsTrue;
	}

	public UUID getNextStepIsFalse() {
		return nextStepIsFalse;
	}

	public void setNextStepIsFalse(UUID nextStepIsFalse) {
		this.nextStepIsFalse = nextStepIsFalse;
	}
}
