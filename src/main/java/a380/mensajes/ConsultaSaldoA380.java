package a380.mensajes;

public class ConsultaSaldoA380 extends A380Message {

	private String nroSucursal;
	private String nroCuenta;

	@Override
	protected void setParticularData() {
		propertiesMap.put("FILLER", "000000000000000");
		propertiesMap.put("BEQBANCO", "191");
		propertiesMap.put("BEQSUCURSAL", nroSucursal);
		propertiesMap.put("BEQNUMERO", nroCuenta);
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

	@Override
	protected void setFs() {
		this.fs = "02";
	}

	@Override
	protected void setMessageType() {
		this.msgTypeIn = "CONSULTA.CUENTA";
		this.msgTypeOut = "CONSULTA.CUENTA";
	}

}
