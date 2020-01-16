/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import ivr.CallContext;
import ivr.CallFlow;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

public class IvrServerRequestHandler extends BaseAgiScript {

	@Override
	public void service(AgiRequest request, AgiChannel channel) {

		// Collection cflows = query.getResultList();
		CallFlow cf = null;// = (CallFlow) cflows.iterator().next();
		CallContext ctx = new CallContext();
		ctx.setChannel(channel);
		ctx.setRequest(request);

		try {
			cf.execute(ctx);
		} catch (Exception ex) {
			Logger.getLogger(IvrServerRequestHandler.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
}
