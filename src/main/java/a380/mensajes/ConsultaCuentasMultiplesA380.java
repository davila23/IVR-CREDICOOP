package a380.mensajes;

public class ConsultaCuentasMultiplesA380 extends A380Message {
	// Tipo de cuenta 0:CC
	// 1:CA
	// 2:CAD
	// 5:CCD
	private String tipoCuenta;
	private String nroCuenta;
	private String nroSucursal;

	@Override
	protected void setParticularData() {
		propertiesMap.put("BEQCTAT.1", tipoCuenta);
		propertiesMap.put("BEQCTABK.1", "191");
		propertiesMap.put("BEQCTASC.1", nroSucursal);
		propertiesMap.put("BEQCTANRO.1", nroCuenta);
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

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	@Override
	protected void setFs() {
		this.fs = "78";
	}

	@Override
	protected void setMessageType() {
		this.msgTypeIn = "MULTIPLES.CUENTAS";
		this.msgTypeOut = "MULTIPLES.CUENTAS";
	}

}
