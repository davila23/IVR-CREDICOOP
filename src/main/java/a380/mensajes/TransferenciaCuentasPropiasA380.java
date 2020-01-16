package a380.mensajes;

import org.joda.time.DateTime;

import sql.querys.SecuenciaA380;

public class TransferenciaCuentasPropiasA380 extends A380Message {

	private String nroSucursalOrigen;
	private String nroCuentaOrigen;

	private String nroSucursalDestino;
	private String nroCuentaDestino;
	private String monto;

	@Override
	protected void setParticularData() {
		propertiesMap.put("BIQDBT", monto);
		propertiesMap.put("BIQNROFRBANCO", "191");
		propertiesMap.put("BIQNROFRSUCURSAL", nroSucursalOrigen);
		propertiesMap.put("BIQNROFRCUENTA", nroCuentaOrigen);
		propertiesMap.put("BIQNROTOBANCO", "191");
		propertiesMap.put("BIQNROTOSUCURSAL", nroSucursalDestino);
		propertiesMap.put("BIQNROTOCUENTA", nroCuentaDestino);

	}

	@Override
	protected void setCommonData(String fs, String fa, String ta) {
		propertiesMap.put("BIQCOD", "A380");
		propertiesMap.put("BIQDTRCV", new DateTime().toString("yyMMdd"));
		propertiesMap.put("BIQTMRCV", new DateTime().toString("HHmmss"));
		propertiesMap.put("BIQTRMSQ", SecuenciaA380.obtenerSecuenciaA380());
		propertiesMap.put("BIQTK2", String.format("%06d", 1));
		propertiesMap.put("BIQFS",
				("00" + fs.trim()).substring(("00" + fs.trim()).length() - 2));
		propertiesMap.put("BIQFA", fa);
		propertiesMap.put("BIQTA", ta);
		propertiesMap.put("BIQFILLER", String.format("%16s", " "));
		propertiesMap.put("BIQTEST", "N");
		propertiesMap.put("BIQOPTX", "00");
		propertiesMap.put("BIQOTITX", tipoAdherente);
		propertiesMap.put("BIQONITX", nroAdherente);
		propertiesMap.put("BIQOPITX", "00");
		propertiesMap.put("BIQDUMMY", "");
		propertiesMap.put("BIQOPE1", "1");
		propertiesMap.put("BIQOPTDOC1", tipoAdherente);
		propertiesMap.put("BIQOPNDOC1", nroAdherente);
		propertiesMap.put("BIQOPPDOC1", "0");
		propertiesMap.put("BIQOPFILLER1", "");
		propertiesMap.put("BIQOPE2", "0");
		propertiesMap.put("BIQOPTDOC2", "0");
		propertiesMap.put("BIQOPNDOC2", "0");
		propertiesMap.put("BIQOPPDOC2", "0");
		propertiesMap.put("BIQOPFILLER2", "");
		propertiesMap.put("BIQOPE3", "0");
		propertiesMap.put("BIQOPTDOC3", "0");
		propertiesMap.put("BIQOPNDOC3", "0");
		propertiesMap.put("BIQOPPDOC3", "0");
		propertiesMap.put("BIQOPFILLER3", "");
		propertiesMap.put("BIQOPE4", "0");
		propertiesMap.put("BIQOPTDOC4", "0");
		propertiesMap.put("BIQOPNDOC4", "0");
		propertiesMap.put("BIQOPPDOC4", "0");
		propertiesMap.put("BIQOPFILLER4", "");
		propertiesMap.put("BIQOPE5", "0");
		propertiesMap.put("BIQOPTDOC5", "0");
		propertiesMap.put("BIQOPNDOC5", "0");
		propertiesMap.put("BIQOPPDOC5", "0");
		propertiesMap.put("BIQOPFILLER5", "");
		propertiesMap.put("BIQOPE6", "0");
		propertiesMap.put("BIQOPTDOC6", "0");
		propertiesMap.put("BIQOPNDOC6", "0");
		propertiesMap.put("BIQOPPDOC6", "0");
		propertiesMap.put("BIQOPFILLER6", "");
		propertiesMap.put("BIQOPE7", "0");
		propertiesMap.put("BIQOPTDOC7", "0");
		propertiesMap.put("BIQOPNDOC7", "0");
		propertiesMap.put("BIQOPPDOC7", "0");
		propertiesMap.put("BIQOPFILLER7", "");
	}

	public void setNroCuentaOrigen(String nroCuentaOrigen) {
		this.nroCuentaOrigen = String.format("%07d",
				Integer.valueOf(nroCuentaOrigen));
	}

	public void setNroSucursalOrigen(String nroSucursalOrigen) {
		this.nroSucursalOrigen = String.format("%03d",
				Integer.valueOf(nroSucursalOrigen));
	}

	public void setNroSucursalDestino(String nroSucursalDestino) {
		this.nroSucursalDestino = String.format("%03d",
				Integer.valueOf(nroSucursalDestino));
	}

	public void setNroCuentaDestino(String nroCuentaDestino) {
		this.nroCuentaDestino = String.format("%07d",
				Integer.valueOf(nroCuentaDestino));
	}

	public void setMonto(double lMonto) {
		monto = String.format("%016.2f", lMonto).replace(".", "")
				.replace(",", "");
	}

	@Override
	protected void setFs() {
		this.fs = "04";

	}

	@Override
	protected void setMessageType() {
		this.msgTypeIn = "ACTIVAR.TRANSFER.CTAS.PROPIAS";
		this.msgTypeOut = "ACTIVAR.OPERACION";
	}

}
