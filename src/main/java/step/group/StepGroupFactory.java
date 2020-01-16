package step.group;

public class StepGroupFactory {

	public enum StepGroupType {
		pideDni, pideFecha, pideTarjeta, pideTarjetaCredicoop, pideFechaCredicoop, pideDniCredicoop, activacionCoto, armaSaldoTarjetaCOTO, bajaCompaniaAseguradoraCoto, pideFechaVencimientoTarjeta, pideNumeroDeComercio, pideDigitoVerificador, generacionDeClave, ingresoDeClave, cuentasPredeterminadas, menuCuentas, preAtendedorBPI, preAtendedorBIE, pideFechaBCCL, precargadasCabalConsulta, precargadasCabalDenunciaAsesor, precargadasCabalDenunciaIvr, precargadasCabalPedidoDePin, precargadasCabalReimpresion, precargadasCabalActivacion, pideFechaDenuncia, pideCuit, pideCuenta, generacionClaveBPi, pideKeyBPI, armaSaldoTarjetaPrecargada
	}

	public static StepGroup createStepGroup(StepGroupType StepGroupType) {
		switch (StepGroupType) {

		case pideDni:
			return new PideDni();
		case pideDniCredicoop:
			return new PideDniCredicoop();
		case pideFechaCredicoop:
			return new PideFechaCredicoop();
		case pideTarjetaCredicoop:
			return new PideTarjetaCredicoop();
		case pideFecha:
			return new PideFecha();
		case pideTarjeta:
			return new PideTarjeta();
		case activacionCoto:
			return new ActivacionTarjetaCOTO();
		case armaSaldoTarjetaCOTO:
			return new ArmaSaldoTarjetaCoto();
		case bajaCompaniaAseguradoraCoto:
			return new BajaCompaniaAseguradoraCOTO();
		case pideFechaVencimientoTarjeta:
			return new PideFechaVencimientoTarjeta();
		case pideNumeroDeComercio:
			return new PideNumeroDeComercio();
		case pideDigitoVerificador:
			return new PideDigitoVerificador();
		case generacionDeClave:
			return new GeneracionDeClave();
		case ingresoDeClave:
			return new IngresoDeClave();
		case menuCuentas:
			return new MenuCuentas();
		case preAtendedorBPI:
			return new PreAtendedorBPI();
		case preAtendedorBIE:
			return new PreAtendedorBIE();
		case precargadasCabalConsulta:
			return new PrecargadasCabalConsulta();
		case precargadasCabalDenunciaAsesor:
			return new PrecargadasCabalDenunciaAsesor();
		case precargadasCabalDenunciaIvr:
			return new PrecargadasCabalDenunciaIvr();
		case precargadasCabalPedidoDePin:
			return new PrecargadasCabalPedidoPin();
		case precargadasCabalReimpresion:
			return new PrecargadasCabalReimpresion();
		case precargadasCabalActivacion:
			return new PrecargadasCabalActivacion();
		case pideFechaBCCL:
			return new PideFechaBCCL();
		case pideFechaDenuncia:
			return new PideFechaDenuncia();
		case pideCuit:
			return new PideCuitCredicoop();
		case pideCuenta:
			return new PideCuenta();
		case generacionClaveBPi:
			return new GeneracionDeClave();
		case pideKeyBPI:
			return new PideKeyBPI();
		case armaSaldoTarjetaPrecargada:
			return new ArmaSaldoTarjetaPrecargada();	
			
		}
		throw new IllegalArgumentException("El tipo "
				+ StepGroupType.toString() + " no existe.");
	}
}
