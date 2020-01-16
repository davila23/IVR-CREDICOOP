package a380.fsfata;

import java.util.HashMap;
import java.util.Map;

public class FsFaTaFactory {
	private static Map<String, Funcion> funcionFsFaTa = new HashMap<String, Funcion>();
	static {
		funcionFsFaTa.put("111", new Funcion("111", "24", "", ""));
		funcionFsFaTa.put("222", new Funcion("222", "67", "", ""));
		funcionFsFaTa.put("110", new Funcion("110", "02", "21", ""));
		funcionFsFaTa.put("120", new Funcion("120", "02", "22", ""));
		funcionFsFaTa.put("140", new Funcion("140", "02", "43", ""));
		funcionFsFaTa.put("160", new Funcion("160", "02", "20", ""));
		funcionFsFaTa.put("161", new Funcion("161", "21", "21", ""));
		funcionFsFaTa.put("162", new Funcion("162", "21", "22", ""));
		funcionFsFaTa.put("164", new Funcion("164", "21", "43", ""));
		funcionFsFaTa.put("165", new Funcion("165", "21", "20", ""));
		funcionFsFaTa.put("170", new Funcion("170", "02", "59", ""));
		funcionFsFaTa.put("217", new Funcion("217", "02", "21", "59"));
		funcionFsFaTa.put("227", new Funcion("227", "02", "22", "59"));
		funcionFsFaTa.put("247", new Funcion("247", "02", "43", "59"));
		funcionFsFaTa.put("267", new Funcion("267", "02", "20", "59"));
		funcionFsFaTa.put("-217", new Funcion("-217", "25", "21", "59"));
		funcionFsFaTa.put("-227", new Funcion("-227", "25", "22", "59"));
		funcionFsFaTa.put("-247", new Funcion("-247", "25", "43", "59"));
		funcionFsFaTa.put("-267", new Funcion("-267", "25", "20", "59"));
		funcionFsFaTa.put("211", new Funcion("211", "04", "21", "21"));
		funcionFsFaTa.put("212", new Funcion("212", "04", "21", "22"));
		funcionFsFaTa.put("221", new Funcion("221", "04", "22", "21"));
		funcionFsFaTa.put("222", new Funcion("222", "04", "22", "22"));
		funcionFsFaTa.put("244", new Funcion("244", "04", "43", "43"));
		funcionFsFaTa.put("246", new Funcion("246", "04", "43", "20"));
		funcionFsFaTa.put("264", new Funcion("264", "04", "20", "43"));
		funcionFsFaTa.put("266", new Funcion("266", "04", "20", "20"));
		funcionFsFaTa.put("310", new Funcion("310", "17", "21", ""));
		funcionFsFaTa.put("320", new Funcion("320", "17", "22", ""));
		funcionFsFaTa.put("340", new Funcion("340", "17", "43", ""));
		funcionFsFaTa.put("350", new Funcion("350", "17", "20", ""));
		funcionFsFaTa.put("360", new Funcion("360", "18", "43", ""));
		funcionFsFaTa.put("361", new Funcion("361", "18", "21", ""));
		funcionFsFaTa.put("362", new Funcion("362", "18", "21", ""));
		funcionFsFaTa.put("366", new Funcion("366", "18", "20", ""));
		funcionFsFaTa.put("367", new Funcion("367", "18", "20", ""));
		funcionFsFaTa.put("371", new Funcion("371", "20", "21", ""));
		funcionFsFaTa.put("372", new Funcion("372", "20", "22", ""));
		funcionFsFaTa.put("376", new Funcion("376", "20", "20", ""));
		funcionFsFaTa.put("400", new Funcion("400", "02", "34", ""));
		funcionFsFaTa.put("414", new Funcion("414", "12", "21", "43"));
		funcionFsFaTa.put("416", new Funcion("416", "12", "21", "20"));
		funcionFsFaTa.put("424", new Funcion("424", "12", "22", "43"));
		funcionFsFaTa.put("426", new Funcion("426", "12", "22", "20"));
		funcionFsFaTa.put("441", new Funcion("441", "13", "43", "21"));
		funcionFsFaTa.put("442", new Funcion("442", "13", "43", "22"));
		funcionFsFaTa.put("461", new Funcion("461", "13", "20", "21"));
		funcionFsFaTa.put("462", new Funcion("462", "13", "20", "22"));
		funcionFsFaTa.put("509", new Funcion("509", "02", "23", ""));
		funcionFsFaTa.put("519", new Funcion("519", "05", "21", "26"));
		funcionFsFaTa.put("529", new Funcion("529", "05", "22", "26"));
		funcionFsFaTa.put("549", new Funcion("549", "05", "43", "26"));
		funcionFsFaTa.put("569", new Funcion("569", "05", "20", "26"));
		funcionFsFaTa.put("508", new Funcion("508", "02", "54", ""));
		funcionFsFaTa.put("518", new Funcion("518", "05", "21", "54"));
		funcionFsFaTa.put("528", new Funcion("528", "05", "22", "54"));
		funcionFsFaTa.put("588", new Funcion("588", "02", "55", ""));
		funcionFsFaTa.put("548", new Funcion("548", "05", "43", "55"));
		funcionFsFaTa.put("568", new Funcion("568", "05", "20", "55"));
		funcionFsFaTa.put("601", new Funcion("601", "02", "56", ""));
		funcionFsFaTa.put("602", new Funcion("602", "02", "57", ""));
		funcionFsFaTa.put("878", new Funcion("878", "64", "71", ""));
		funcionFsFaTa.put("900", new Funcion("900", "65", "", ""));
		funcionFsFaTa.put("950", new Funcion("950", "27", "", ""));
	}

	public static Funcion obtenerFuncion(String codFuncion) {
		return funcionFsFaTa.get(codFuncion);

	}
}
