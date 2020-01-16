/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import workflow.Context;

/**
 * 
 * @author Daniel Avila
 */
public class StepTextToSpeech extends Step {

	public StepTextToSpeech(UUID tmpid) {
		this.id = tmpid;
		this.StepType = StepType.TextToSpeech;
	}

	public boolean execute(Context context) throws Exception {
	   
		Process p = Runtime.getRuntime().exec("/tmp/reconocimientovoz/reconoce.py grabaciones_256.wav");
		InputStream in = (InputStream) p.getInputStream() ;    
        OutputStream out = (OutputStream) p.getOutputStream();     
        InputStream err = (InputStream) p.getErrorStream() ; 
        System.out.println("in "+ in);
        System.out.println("out" + out);
        System.out.println("err" + err);
		
	    System.out.println(p);
		
		return false;

	}
}
