package test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import a380.fsfata.FsFaTaFactory;
import a380.guion.ArmaNumero;

public class TestGuiones {
	@Test
	public void TestSaldo() {
		ArmaNumero aNumero = new ArmaNumero();

		double gSaldo = Double.parseDouble("209232.08");

		int signosaldo = 0;

		int codMoneda = aNumero.devuelveMoneda("01735001643");

		String saldo = aNumero.armaGuionConMoneda(gSaldo, codMoneda,
				signosaldo, true);

	}

	@Test
	public void testDiceImporteConMoneda() {
		double myNumber = 0.00;
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionConMoneda(myNumber, 0, 0, false));
		assertNotNull(pp.armaGuionConMoneda(myNumber, 0, 0, false));
	}

	@Test
	public void testDiceCuenta() {
		String myNumber = "10010129719";
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionCuenta(myNumber));
		assertNotNull(pp.armaGuionCuenta(myNumber));
	}

	@Test
	public void testDicePorcentaje() {
		double myNumber = 0.01;
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionPorcentaje(myNumber));
		assertNotNull(pp.armaGuionPorcentaje(myNumber));
	}

	@Test
	public void testDiceFechaAAMMDD() {
		String myNumber = "740122";
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionFecha(myNumber));
		assertNotNull(pp.armaGuionFecha(myNumber));
	}

	@Test
	public void testDiceFechaAAAAMMDD() {
		String myNumber = "19740122";
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionFecha(myNumber));
		assertNotNull(pp.armaGuionFecha(myNumber));
	}

	@Test
	public void testDiceHoraHHMMSS() {
		String myNumber = "240100";
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionHora(myNumber));
		assertNotNull(pp.armaGuionHora(myNumber));
	}

	@Test
	public void testFuncion() {
		String fs = "221";
		System.out.println(FsFaTaFactory.obtenerFuncion(fs).getFs() + "-"
				+ FsFaTaFactory.obtenerFuncion(fs).getFa() + "-"
				+ FsFaTaFactory.obtenerFuncion(fs).getTa());
	}

	@Test
	public void testDiceTransferencia() {
		String myNumber = "10010129719";
		String myNumber2 = "10010129715";
		double importe = 10.0;
		ArmaNumero pp = new ArmaNumero();
		System.out.println(pp.armaGuionTransferencia(myNumber, myNumber2,
				importe));

	}
}
