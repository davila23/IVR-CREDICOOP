package a380.errors;

import java.util.HashMap;
import java.util.Map;

import main.Daemon;

import org.apache.log4j.Level;

import step.StepSendA380Message;

public class A380ErrorFactory {
	private static Map<String, A380Error> errorsA380ToE352 = new HashMap<String, A380Error>();
	static {
		errorsA380ToE352.put("00561", new A380Error("00561", "0",
				"NO TIENE CHEQUES RECHAZADOS"));
		// errorsA380ToE352.put("",new A380Error("","01","Razón no definida"));
		errorsA380ToE352.put("00620", new A380Error("00620", "2",
				"TARJETA PERDIDA O ROBADA"));
		errorsA380ToE352.put("00614", new A380Error("00614", "3",
				"NUMERO DE TARJETA NO EXISTE"));
		errorsA380ToE352.put("00702", new A380Error("00702", "4",
				"PIN INGRESADO INVALIDO"));
		errorsA380ToE352.put("00557", new A380Error("00557", "5",
				"CUENTA PRIMARIA ERRONEA"));
		errorsA380ToE352.put("00635", new A380Error("00635", "5",
				"FALTA NRO CTTA APLICACION"));
		errorsA380ToE352.put("00562", new A380Error("00562", "6",
				"CUENTA NO VINCULADA"));
		errorsA380ToE352.put("00632", new A380Error("00632", "7",
				"SUPERO ENTRADAS PIN ERRONEAS"));
		errorsA380ToE352.put("00703", new A380Error("00703", "7",
				"SUPERO ENTRADAS PIN ERRONEAS"));
		errorsA380ToE352.put("00510", new A380Error("00510", "8",
				"CUENTA HACIA NO EXISTE"));
		errorsA380ToE352.put("00696", new A380Error("00696", "8",
				"CUENTA HACIA NO EXISTE"));
		// errorsA380ToE352.put("00696",new
		// A380Error("00696","09","CUENTA DESDE NO EXISTE"));
		errorsA380ToE352.put("00503", new A380Error("00503", "10",
				"CUENTA CERRADA"));
		// errorsA380ToE352.put("00503",new
		// A380Error("00503","11","CUENTA HACIA CERRADA"));
		// errorsA380ToE352.put("00503",new
		// A380Error("00503","12","CUENTA DESDE CERRADA"));
		errorsA380ToE352.put("00526", new A380Error("00526", "13",
				"CUENTA BLOQUEADA"));
		errorsA380ToE352.put("00525", new A380Error("00525", "16",
				"CUENTA HACIA BLOQUEADA"));
		errorsA380ToE352.put("00507", new A380Error("00507", "17",
				"CTTA CONJUNTA NO ACEPTA DEBITOS"));
		errorsA380ToE352.put("00505", new A380Error("00505", "18",
				"SUPERA LIM. MENSUAL DE DEBITOS"));
		errorsA380ToE352.put("00785", new A380Error("00785", "18",
				"ALCANZO MAX NUMERO TRANS MES   "));
		errorsA380ToE352.put("00542", new A380Error("00542", "19",
				"FONDOS INSUFICIENTES"));
		errorsA380ToE352.put("00603", new A380Error("00603", "19",
				"FONDOS INSUFICIENTES"));
		errorsA380ToE352.put("00604", new A380Error("00604", "19",
				"FONDOS INSUFICIENTES"));
		errorsA380ToE352.put("00607", new A380Error("00607", "19",
				"FONDOS INSUFICIENTES"));
		errorsA380ToE352.put("00608", new A380Error("00608", "19",
				"FONDOS INSUFICIENTES"));
		errorsA380ToE352.put("00508", new A380Error("00508", "20",
				"EXECEDE LIMITE PARA TRANSFER."));
		errorsA380ToE352.put("00630", new A380Error("00630", "20",
				"EXECEDE LIMITE PARA TRANSFER."));
		errorsA380ToE352.put("00631", new A380Error("00631", "20",
				"EXECEDE LIMITE PARA TRANSFER."));
		errorsA380ToE352.put("00786", new A380Error("00786", "20",
				"ALCANZO MAX IMPORTE TRAN MES"));
		errorsA380ToE352.put("00548", new A380Error("00548", "21",
				"EXECEDE LIMITE PARA COMPRA-VTA"));
		errorsA380ToE352.put("00547", new A380Error("00547", "22",
				"EL HORARIO COMPRA-VENTA ES ..."));
		errorsA380ToE352.put("00554", new A380Error("00554", "23",
				"NO TIENE CREDITOS RELACIONADOS"));
		errorsA380ToE352.put("00556", new A380Error("00556", "23",
				"NO TIENE CREDITOS RELACIONADOS"));
		errorsA380ToE352.put("00559", new A380Error("00559", "24",
				"NO TIENE PLAZOS FIJOS RELAC      "));
		errorsA380ToE352.put("00625", new A380Error("00625", "25",
				"CTA HACIA Y DESDE NO PUEDEN .."));
		errorsA380ToE352.put("00624", new A380Error("00624", "26",
				"EXCEDE LIMITE DE APLIC DIARIO."));
		errorsA380ToE352.put("00623", new A380Error("00623", "27",
				"EXCEDE LIMiTE DIARIO"));
		errorsA380ToE352.put("00626", new A380Error("00626", "27",
				"EXCEDE LIMTE DIARIO"));
		errorsA380ToE352.put("00627", new A380Error("00627", "27",
				"EXCEDE LIMTE DIARIO"));
		errorsA380ToE352.put("00784", new A380Error("00784", "27",
				"ALCANZO MAX NUMERO TRANS DIA   "));
		errorsA380ToE352.put("00522", new A380Error("00522", "28",
				"COTIZACION NO CARGADA"));
		errorsA380ToE352.put("00558", new A380Error("00558", "29",
				"FALTA INGRESAR CTA PPAL"));
		errorsA380ToE352.put("00726", new A380Error("00726", "30",
				"FUNCION NO HABILITADA"));
		errorsA380ToE352.put("00555", new A380Error("00555", "31",
				"NO POSEE NRO SOCIO PARA PRESTAMO"));
		errorsA380ToE352.put("00549", new A380Error("00549", "32",
				"IMPORTE MINIMO DE COMPRA"));
		// errorsA380ToE352.put("00549",new
		// A380Error("00549","33","CAJEROS OCUPADOS"));
		errorsA380ToE352.put("00570", new A380Error("00570", "39",
				"IMPORTE INFERIOR AL MINIMO"));
		// errorsA380ToE352.put("00570",new
		// A380Error("00570","40","SUBCLASE DE MENSJE INVALIDA"));
		errorsA380ToE352.put("00576", new A380Error("00576", "41",
				"ACTUALIZE SU NRO DE CUIT"));
		errorsA380ToE352.put("00578", new A380Error("00578", "42",
				"ADHERENTE (1ER TX DEBE SER CAMBIO DE PIN)"));
		errorsA380ToE352.put("00579", new A380Error("00579", "43",
				"OPERADOR/FIRMANTE (1ER TX DEBE SER CAMBIO DE PIN)"));
		errorsA380ToE352.put("00580", new A380Error("00580", "44",
				"PIN INVALIDO OPERADOR/FIRMANTE"));
		errorsA380ToE352.put("00581", new A380Error("00581", "45",
				"EXCESIVOS ERRORES DE PIN"));
		errorsA380ToE352.put("00582", new A380Error("00582", "46",
				"OPERADOR NO VALIDO CUENTA PPIA"));
		errorsA380ToE352.put("00583", new A380Error("00583", "47",
				"ESQUEMA DE FIRMAS NO ESTA EN"));
		errorsA380ToE352.put("00584", new A380Error("00584", "48",
				"OPERADOR NO HABILITADO PARA TX"));
		errorsA380ToE352.put("00585", new A380Error("00585", "49",
				"OPERADOR/FIRMANTE NO EXISTE (quien lo solicita)"));
		errorsA380ToE352.put("00586", new A380Error("00586", "50",
				"ERROR EN SUSTITUCIÓN"));
		errorsA380ToE352.put("00587", new A380Error("00587", "51",
				"ERROR EN ESQUEMA DE FIRMAS"));
		errorsA380ToE352.put("00728", new A380Error("00728", "52",
				"ADHERENTE INHABILITADO"));
		errorsA380ToE352.put("00595", new A380Error("00595", "53",
				"DOCUMENTO DUPLICADO"));
		errorsA380ToE352.put("00566", new A380Error("00566", "54",
				"CONEXION SBL EN REL"));
		errorsA380ToE352.put("00597", new A380Error("00597", "54",
				"NO HAY TRANSF. MULT. PARA PROCESAR"));
		errorsA380ToE352.put("00601", new A380Error("00601", "1",
				"CTA CORRIENTE NO EXISTE"));// 55
		errorsA380ToE352.put("00605", new A380Error("00605", "55",
				"CTA CORRIENTE NO EXISTE"));// 55
		errorsA380ToE352.put("00613", new A380Error("00613", "56",
				"ERROR DE CICS"));
		errorsA380ToE352.put("00719", new A380Error("00719", "56",
				"ANI NO INICIALIZADO"));
		errorsA380ToE352.put("00720", new A380Error("00720", "56",
				"ERROR DEL ANI"));
		errorsA380ToE352.put("00726", new A380Error("00726", "56",
				"COD DE TX INVALIDO"));
		errorsA380ToE352.put("00725", new A380Error("00725", "57",
				"NO EXISTE LA CUENTA"));
		errorsA380ToE352.put("00792", new A380Error("00792", "58",
				"DIFIERE ESPECIE DE LETRAS"));
		errorsA380ToE352.put("00793", new A380Error("00793", "59",
				"DIFIERE SUC TRANF. LETRAS"));
		errorsA380ToE352.put("00698", new A380Error("00698", "60",
				"TRANSF.CTA.CORRALITO A LD"));
		errorsA380ToE352.put("00727", new A380Error("00727", "61",
				"PROC AUT. NO DISPONIBLE"));
		errorsA380ToE352.put("00729", new A380Error("00729", "61",
				"DIA DE NEGOCIOS ERRONEO"));
		errorsA380ToE352.put("00730", new A380Error("00730", "61",
				"COMUNIC C/RED INACTIVA"));
		errorsA380ToE352.put("00721", new A380Error("00721", "62",
				"DENEGADO POR LA RED"));
		errorsA380ToE352.put("00733", new A380Error("00733", "63",
				"CAJERO MODO TEST-NO PERMITE TRABAJAR CON REDES"));
		errorsA380ToE352.put("00517", new A380Error("00517", "64",
				"PROBLEMAS C/BASES DATOS"));
		errorsA380ToE352.put("00738", new A380Error("00738", "64",
				"PROBLEMAS C/BASE DE DATOS"));
		errorsA380ToE352.put("00739", new A380Error("00739", "65",
				"PARTIDA YA PAGADA"));
		errorsA380ToE352.put("00740", new A380Error("00740", "66",
				"NUMERO DE USUARIO INVALIDO"));
		errorsA380ToE352.put("00741", new A380Error("00741", "67",
				"NO REGISTRA VTO PEND DE PAGO"));
		errorsA380ToE352.put("00742", new A380Error("00742", "68",
				"NO TIENE PAGOS ANT. REGISTR."));
		errorsA380ToE352.put("00746", new A380Error("00746", "69",
				"ADHERENTE NO VINCULADO AL P.A.S"));
		errorsA380ToE352.put("00747", new A380Error("00747", "70",
				"ERROR ARCH. DEGFCRI"));
		errorsA380ToE352.put("00502", new A380Error("00502", "71",
				"VALOR NEGOCIADO"));
		errorsA380ToE352.put("00750", new A380Error("00750", "72",
				"INEXT.PH.AHERENTE-SECUENCIA   "));
		errorsA380ToE352.put("00751", new A380Error("00751", "73",
				"PH.CTA.DB  DESEADA DIFIERE"));
		errorsA380ToE352.put("00752", new A380Error("00752", "74",
				"PH.CANT.REG.TOTAL  DIFIERE   "));
		errorsA380ToE352.put("00753", new A380Error("00753", "75",
				"PH.NRO.PAQ DESEADO DIFIERE   "));
		errorsA380ToE352.put("00754", new A380Error("00754", "76",
				"PH.CANT.REG.PAQUETE DIFIERE   "));
		errorsA380ToE352.put("00755", new A380Error("00755", "77",
				"PH.TIPO TRANSMICION DIFIERE   "));
		errorsA380ToE352.put("00756", new A380Error("00756", "78",
				"PH.TRANSMICION DUPLICADA   "));
		errorsA380ToE352.put("00757", new A380Error("00757", "79",
				"PH.MOVIMIENTO DUPLICADO   "));
		errorsA380ToE352.put("00758", new A380Error("00758", "80",
				"PH.NO SE PUEDE ANULAR   "));
		errorsA380ToE352.put("00798", new A380Error("00798", "80",
				"LETRA NO PERTENECE A PCIA."));
		errorsA380ToE352.put("00771", new A380Error("00771", "81",
				"PH.IMPORTE NO NUM. O EN CEROS   "));
		errorsA380ToE352.put("00787", new A380Error("00787", "81",
				"SUPERARIA LIMITE PERMITIDO P/TX"));
		errorsA380ToE352.put("00765", new A380Error("00765", "82",
				"NUEVA CLAVE NO PUEDE REPETIRSE"));
		errorsA380ToE352.put("00593", new A380Error("00593", "83",
				"ERROR EN TIPO DE CUENTA BONO"));
		errorsA380ToE352.put("00705", new A380Error("00705", "84",
				"NO EXISTE DOC. EN DEGFDBI"));
		errorsA380ToE352.put("00521", new A380Error("00521", "91",
				"CANCELADA POR DEMORA"));
		// errorsA380ToE352.put("00521",new
		// A380Error("00521","96","IMPOSIBLE PROCESAR"));

	}

	public static A380Error obtenerA380Error(String codA380) {
		Daemon.getMiLog()
				.log(Level.INFO,
						A380ErrorFactory.class.getName() + "|ERROR_MSG_A380|"
								+ codA380);
		A380Error errorRet = errorsA380ToE352.get(codA380);
		if (errorRet == null) {
			errorRet = new A380Error("00001", "1", "ERROR NO ESPECIFICADO");
		}
		return errorRet;

	}
}
