package a380.mensajes;

import org.joda.time.DateTime;

import sql.querys.SecuenciaA380;

public class PedidoDuplicadoResumenA380 extends A380Message {

	private String nroSucursal;
	private String nroCuenta;
	private int cantidad = 1;

	@Override
	protected void setParticularData() {
		propertiesMap.put("FILLER", "0");
		propertiesMap.put("BEQNROFRBANCO", "191");
		propertiesMap.put("BEQNROFRSUCURSAL", nroSucursal);
		propertiesMap.put("BEQNROFRCUENTA", nroCuenta);
		propertiesMap.put("FILLER", "0");
		propertiesMap.put("BEQTICHQ", "");
		propertiesMap.put("BEQCANT", String.valueOf(cantidad));
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

	public String getNroSucursal() {
		return nroSucursal;
	}

	public void setNroSucursal(String nroSucursal) {
		this.nroSucursal = String.format("%03d", Integer.valueOf(nroSucursal));
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = String.format("%07d", Integer.valueOf(nroCuenta));
	}

	public void setCantidad(int lCantidad) {
		this.cantidad = lCantidad;
	}

	@Override
	protected void setFs() {
		this.fs = "17";
	}

	@Override
	protected void setMessageType() {
		this.msgTypeIn = "ACTIVAR.SOLICITUD.PEDIDO.CHEQUERA";
		this.msgTypeOut = "ACTIVAR.OPERACION";
	}

}
