package test;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import a380.guion.ArmaNumero;

public class TestArmaGuionConMoneda {
	@Test
	public void testDiceImporteConMoneda() {
		ArmaNumero pp = new ArmaNumero();
		List<String> importes = new ArrayList<String>();
		importes.add("0.0");
		importes.add("0.15");
		importes.add("1.0");
		importes.add("1.12");
		importes.add("10.02");
		importes.add("1.3");
		importes.add("100.16");
		importes.add("100.00");
		importes.add("1000.00");
		importes.add("1000.57");
		importes.add("4.456789885E7");
		importes.add("9890567.00");
		importes.add("9890568.67");
		for (Iterator iterator = importes.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
			System.out.println(pp.armaGuionConMoneda(
					Double.parseDouble(string), 0, 3, false));
		}

	}

}
