package a380.guion;

import java.util.HashMap;
import java.util.Map.Entry;

public class ArmaNumero {
	private HashMap<Integer, UnidadNum> unidades = new HashMap<Integer, UnidadNum>();

	public ArmaNumero() {
		unidades.put(1, new UnidadNum("S/ml", "S/mls"));
		unidades.put(2, new UnidadNum("S/mll", "S/mlls"));
		unidades.put(3, new UnidadNum("S/billon", "S/billones"));
	}

	public String armaGuionCuentaCompleta(String cuenta) {
		String retval = "";
		long p_ent = Long.valueOf(cuenta);
		String parteDec = String.valueOf(cuenta);
		int numero = Integer.valueOf(parteDec.replace(".", ""));
		String ParteEntera = String.format("%016d", p_ent);

		for (Entry<Integer, String> part : getParts(ParteEntera, 3).entrySet()) {
			int num = Integer.parseInt(part.getValue());
			String tmpPart = "";
			if (num > 0) {
				tmpPart += ArmaCadena(num);
				if (part.getKey() > 0)
					tmpPart += "&" + poneUnidad(num, part.getKey());

				retval = retval.length() > 0 ? tmpPart + "&" + retval : tmpPart;
			}
		}

		return retval.replace("&&", "&");
	}

	public String armaGuionCuenta(String cuenta) {
		String retval = "";
		String parteDec = String.valueOf(cuenta).substring(
				String.valueOf(cuenta).length() - 3,
				String.valueOf(cuenta).length());
		int numero = Integer.valueOf(parteDec.replace(".", ""));
		retval = ArmaCadena(numero);
		return retval;
	}

	public String armaGuionpedidoCotizacionDolar(String hora, double cotven,
			double cotcomp) {
		StringBuilder retval = new StringBuilder();
		retval.append("S/m15&").append(armaGuionHora(hora));
		retval.append("&S/m16&").append(this.ArmaNumeroFromDouble(cotcomp));
		retval.append("&S/m17&").append(this.ArmaNumeroFromDouble(cotven));
		return retval.toString();
	}

	public String armaGuionChequesRechazados(String cuenta, int cantidad,
			double importe) {
		String retval = "";
		if (cantidad > 0) {
			retval = "S/m45&";
			retval += ArmaCadena(cantidad);
			retval += cantidad == 1 ? "&S/m08&" : "&S/m08s&";
			retval += armaGuionConMoneda(importe, 0, 3, false);
		} else {
			retval = "S/m08n";
		}
		return retval;
	}

	public String armaGuionFecha(String fecha) {
		String retval = "";
		String dia = "";
		String mes = "";
		String anio = "";
		switch (fecha.length()) {
		case 6:
			dia = fecha.substring(fecha.length() - 2, fecha.length());
			mes = fecha.substring(fecha.length() - 4, fecha.length() - 2);
			anio = ArmaCadena(Integer.parseInt(fecha.substring(0, 2)));
			break;
		case 8:
			dia = fecha.substring(fecha.length() - 2, fecha.length());
			mes = fecha.substring(fecha.length() - 4, fecha.length() - 2);
			String tmpanio = String.format("%016d",
					Integer.valueOf(fecha.substring(0, 4)));
			for (Entry<Integer, String> part : getParts(tmpanio, 3).entrySet()) {
				int num = Integer.parseInt(part.getValue());
				String tmpPart = "";
				if (num > 0) {
					tmpPart += ArmaCadena(num);
					if (part.getKey() > 0)
						tmpPart += "&" + poneUnidad(num, part.getKey());

					anio = anio.length() > 0 ? tmpPart + "&" + anio : tmpPart;
				}
			}
			anio = "&" + anio;
			break;

		default:
			break;
		}
		retval += ArmaCadena(Integer.parseInt(dia)).replace("&", "");
		retval += "&S/M" + Integer.parseInt(mes);
		retval += "&S/d" + anio;
		return retval;

	}

	public String armaGuionPorcentaje(double numero) {
		String retval = "";
		int p_ent = (int) numero;
		String parteDec = String.valueOf(numero).substring(
				String.valueOf(numero).length() - 2,
				String.valueOf(numero).length());
		int p_dec = Integer.valueOf(parteDec.replace(".", ""));

		retval += ArmaCadena(p_ent).replace("&", "");
		if (p_dec > 0) {
			retval += "&S/pun&";
			retval += ArmaCadena(p_dec);
		}
		retval += "&S/pc";

		return retval.replace("1n", "1o");
	}

	public String armaGuionConMoneda(double numero, int moneda, int signo,
			boolean diceSaldo) {
		String retval = "";
		long p_ent = (long) numero;
		double tmp_p_dec = numero - p_ent;

		int p_dec = (int) Math.round(tmp_p_dec * 100);

		String ParteEntera = String.format("%016d", p_ent);

		for (Entry<Integer, String> part : getParts(ParteEntera, 3).entrySet()) {
			int num = Integer.parseInt(part.getValue());
			String tmpPart = "";
			if (num > 0) {
				tmpPart += ArmaCadena(num);
				if (part.getKey() > 0)
					tmpPart += "&" + poneUnidad(num, part.getKey());

				retval = retval.length() > 0 ? tmpPart + "&" + retval : tmpPart;
			}
		}

		if (p_ent == 0)
			retval = "S/cero";

		switch (moneda) {
		case 1:
			if (numero == 1) {
				retval += "&S/dl";
			} else {
				retval += "&S/dls";
			}
			break;
		case 0:
			if (numero == 1) {
				retval += "&S/ps";
			} else {
				retval += "&S/pss";
			}
			break;
		default:
			break;
		}

		if (p_dec > 0) {
			retval += p_dec == 1 ? "&S/con&" + ArmaCadena(p_dec) + "&S/cn"
					: "&S/con" + ArmaCadena(p_dec) + "&S/cns";
		}
		if (numero == 0.0)
			signo = 3;

		switch (signo) {
		case 0:
			retval += "&S/m53";
			break;
		case 1:
			retval += "&S/m52";
			break;

		default:
			break;
		}
		if (diceSaldo) {
			if (retval.startsWith("&")) {
				retval = "S/m01" + retval;
			} else {
				retval = "S/m01&" + retval;
			}
		} else {
			if (retval.startsWith("&"))
				retval = retval.substring(1);
		}

		return retval.replace("&&", "&");
	}

	public String armaGuionHora(String fhora) {
		String retval = "";
		int ihoras = 0;
		int iminutos = 0;
		iminutos = Integer.parseInt(fhora.substring(2, 4));
		ihoras = Integer.parseInt(fhora.substring(0, 2));

		switch (ihoras) {
		case 0:
			retval += "S/cero&S/hrs";
			break;
		case 1:
			retval += "S/1a&S/hr";
			break;
		default:
			retval += ArmaCadena(ihoras).replace("&", "");
			retval += "&S/hrs";
			break;
		}

		switch (iminutos) {
		case 0:
			retval += "&S/cero&S/mnts";
			break;
		case 1:
			retval += "&S/1n&S/mnt";
			break;
		default:
			retval += ArmaCadena(iminutos);
			retval += "&S/mnts";
			break;
		}

		return retval;
	}

	public String armaGuionTransferencia(String cuentaDebito,
			String cuentaCredito, double importe) {
		String retval = "";
		retval += "S/m89&";
		retval += armaGuionCuenta(cuentaDebito);
		retval += "&S/m50";

		int codMoneda = devuelveMoneda(cuentaDebito);

		retval += "&" + armaGuionConMoneda(importe, codMoneda, 3, false);
		retval += "&S/m90&";
		retval += armaGuionCuenta(cuentaCredito);
		return retval.replace("&&", "&");
	}

	private HashMap<Integer, String> getParts(String string, int partitionSize) {
		HashMap<Integer, String> parts = new HashMap<Integer, String>();
		int len = string.length();
		int unidades = 0;

		int rest = 0;
		for (int i = partitionSize; i < len; i += partitionSize) {
			parts.put(unidades, string.substring(len - i, len - rest));
			rest += partitionSize;
			unidades++;
		}
		return parts;
	}

	private String ArmaNumeroFromDouble(double numero) {
		String retval = "";
		long p_ent = (long) numero;
		String nro = String.valueOf(numero);
		String parteDec = nro.substring(nro.indexOf(".") + 1, nro.length());
		int p_dec = Integer.valueOf(parteDec.replace(".", ""));

		String ParteEntera = String.format("%016d", p_ent);

		for (Entry<Integer, String> part : getParts(ParteEntera, 3).entrySet()) {
			int num = Integer.parseInt(part.getValue());
			String tmpPart = "";
			if (num > 0) {
				tmpPart += ArmaCadena(num);
				if (part.getKey() > 0)
					tmpPart += "&" + poneUnidad(num, part.getKey());

				retval = retval.length() > 0 ? tmpPart + "&" + retval : tmpPart;
			}
		}

		if (p_ent == 0)
			retval = "S/cero";

		if (p_dec > 0) {
			retval += p_dec == 1 ? "&S/con&" + ArmaCadena(p_dec) + "&S/cn"
					: "&S/con" + ArmaCadena(p_dec) + "&S/cns";
		}

		return retval.replace("&&", "&");
	}

	private String ArmaCadena(int Numero) {
		String Cadena = "";
		int numOri = Numero;

		int Centenas = Numero / 100;
		Numero = Numero - Centenas * 100;
		int resto = Numero;

		int Decenas = Numero / 10;
		Numero = Numero - Decenas * 10;
		int Unidades = Numero;

		if (Centenas > 0) {
			Cadena = "S/";
			if (Centenas == 1 && (Decenas != 0 || Unidades != 0)) {
				Cadena += "100o";
			} else if (Centenas > 1) {
				Cadena += String.valueOf(Centenas * 100);
			} else {
				Cadena += "100";
			}

		}
		if (Decenas > 0 || Unidades > 0) {
			if (resto == 1) {

				Cadena += numOri == 1 ? "S/" : "&S/";
				Cadena += "1n";
			} else {
				Cadena += "&S/";
				Cadena += resto;
			}
		} else {
			if (numOri == 0)
				Cadena = "S/cero";
		}

		return Cadena;
	}

	private String poneUnidad(int num, int unidad) {
		String retval = "";

		if (num == 1 || unidad == 1) {
			retval = unidades.get(unidad).getSingular();
		} else {
			retval = unidades.get(unidad).getPlural();
		}
		return retval;
	}

	public int devuelveMoneda(String cuenta) {

		int codMoneda = 0;
		switch (Integer.parseInt(cuenta.substring(0, 1))) {
		case 0:
		case 1:
			codMoneda = 0;
			break;
		case 2:
		case 5:
			codMoneda = 1;
			break;
		default:
			break;
		}
		return codMoneda;
	}

	public String armaGuionPagoTarjetaCabal(String cuentaDebito, double importe) {
		String retval = "";

		retval += "S/m89&";
		retval += armaGuionCuenta(cuentaDebito);
		retval += "&S/m50";

		int codMoneda = devuelveMoneda(cuentaDebito);

		retval += "&" + armaGuionConMoneda(importe, codMoneda, 3, false);

		return retval.replace("&&", "&");
	}

}
