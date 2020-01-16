package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import a380.utils.SaldoCuenta;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.Step;
import step.StepAuthInitialInfo;
import step.StepDefaultAcount;
import step.StepConditional;
import step.StepCounter;
import step.StepExecute;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepGetCuentas;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayAccountBalance;
import step.StepSayDefaultAccountBalance;
import step.StepSayDefaultAccountBalance;
import step.StepSendA380Message;
import step.StepSwitch;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class MenuCuentas implements StepGroup {

	protected StepGroupType GroupType;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private StepPlay stepAudioNoTieneEsteTipoDeCuenta;
	private StepPlayRead stepAudioQuierePredeterminar;
	private StepMenu stepMenuPredeterminarCuentas;
	private StepConditional evalTieneCuentasPredeterminadas;
	private StepGetCuentas obtieneCuentas;
	private StepDefaultAcount agregoPredeterminada;
	private StepPlayRead stepAudioCuentaADesvincular;
	private StepPlayRead stepAudioTipoDeCuenta;
	private StepMenu stepMenuSaldosCuentas;
	private StepPlayRead stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar;
	private StepSayDefaultAccountBalance saldoPredeterminadas;
	private StepSayAccountBalance saldoCuentas;
	private StepPlayRead stepAudioCuentaAVincular;
	private StepMenu stepMenuPredeterminarOtraCuenta;
	private StepPlayRead stepAudioRepetirPredeterminadas;
	private StepMenu stepMenuRepetirPredeterminadas;
	private StepMenu stepMenuQuierePredeterminar;
	private StepPlay stepAudioCuentaVinculadaConExito;
	private StepPlay stepAudioCuentaDesvinculadaConExito;
	private StepPlayRead stepAudioDeseaVincularOtraCuenta;
	private StepPlayRead stepAudioSucursal;
	private StepPlayRead stepAudioCuenta;
	private StepPlayRead stepAudioSaldosCuentas;
	private StepMenu stepMenuTipoDeCuenta;
	private StepPlayRead stepAudioPredeterminarCuentas;
	private StepDefaultAcount borroPredeterminada;
	private StepPlayRead stepAudioDeseaDesvincularOtraCuenta;
	private StepPlayRead stepAudioTipoDeCuentaAAgregar;
	private StepPlayRead stepAudioTipoDeCuentaABorrar;
	private StepMenu stepMenuTipoDeCuentaAAgregar;
	private StepMenu stepMenuTipoDeCuentaABorrar;
	private StepPlay stepAudioTipoDeCuentaInvalido;
	private StepPlay stepAudioTipoDeCuentaABorrarInvalido;
	private StepPlay stepAudioTipoDeCuentaAAgregarInvalido;
	private StepSwitch evalcantidadDeCuentasEncontradasAgregar;
	private StepPlay stepAudioNotieneCuenta;
	private StepSwitch evalcantidadDeCuentasEncontradasBorrar;
	private StepPlay stepAudioTineMasDeUnaCuenta;
	private StepSwitch evalQuiereBorrarOAgregar;
	private StepConditional evalIngresoNumeroDeCuenta;
	private StepConditional evalIngresoNumeroDeSucursal;
	private StepCounter contadorIntentosSucursal;
	private StepCounter contadorIntentosCuenta;
	private StepConditional evalContadorIntentosSucursal;
	private StepConditional evalContadorIntentosCuenta;

	private void setSequence() {

		/* OBTIENE CUENTAS DEL USUARIO */

		obtieneCuentas.setNextstep(evalTieneCuentasPredeterminadas.GetId());

		/* EVALUA SI TIENE PREDETERMINADAS */

		evalTieneCuentasPredeterminadas.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"tieneCuentasPredeterminadasContextVar").getVarName()
				+ "} == " + 1, saldoPredeterminadas.GetId(),
				stepAudioQuierePredeterminar.GetId()));

		/* USUARIO CON CUENTAS PREDETERMINADAS */

		saldoPredeterminadas.setNextstep(stepAudioRepetirPredeterminadas
				.GetId());

		stepAudioRepetirPredeterminadas
				.setNextstep(stepMenuRepetirPredeterminadas.GetId());

		stepMenuRepetirPredeterminadas.addSteps("1",
				saldoPredeterminadas.GetId());
		stepMenuRepetirPredeterminadas.addSteps("2",
				stepAudioSaldosCuentas.GetId());
		stepMenuRepetirPredeterminadas
				.setInvalidOption(stepAudioRepetirPredeterminadas.GetId());

		/* USUARIO SIN CUENTAS PREDETERMINADAS */

		/* Evalua si quiere predeterminar cuentas */

		stepAudioQuierePredeterminar.setNextstep(stepMenuQuierePredeterminar
				.GetId());

		stepMenuQuierePredeterminar.addSteps("1",
				stepAudioCuentaAVincular.GetId());
		stepMenuQuierePredeterminar.addSteps("2",
				stepAudioSaldosCuentas.GetId());
		stepMenuQuierePredeterminar
				.setInvalidOption(stepAudioQuierePredeterminar.GetId());

		/* El usuario quiere predeterminar cuentas */

		stepAudioTipoDeCuentaAAgregar.setNextstep(stepMenuTipoDeCuentaAAgregar
				.GetId());

		stepMenuTipoDeCuentaAAgregar
				.addSteps("1", agregoPredeterminada.GetId());
		stepMenuTipoDeCuentaAAgregar
				.addSteps("2", agregoPredeterminada.GetId());
		stepMenuTipoDeCuentaAAgregar
				.addSteps("4", agregoPredeterminada.GetId());
		stepMenuTipoDeCuentaAAgregar
				.setInvalidOption(stepAudioTipoDeCuentaAAgregarInvalido.GetId());

		stepAudioTipoDeCuentaAAgregarInvalido
				.setNextstep(stepAudioTipoDeCuentaAAgregar.GetId());

		/* VINCULO / DESVINCULO */

		agregoPredeterminada
				.setNextstep(evalcantidadDeCuentasEncontradasAgregar.GetId());

		borroPredeterminada.setNextstep(evalcantidadDeCuentasEncontradasBorrar
				.GetId());

		evalcantidadDeCuentasEncontradasBorrar.addSwitchValue("0",
				stepAudioNotieneCuenta.GetId());
		evalcantidadDeCuentasEncontradasBorrar.addSwitchValue("1",
				stepAudioCuentaDesvinculadaConExito.GetId());
		evalcantidadDeCuentasEncontradasBorrar.addSwitchValue("2",
				stepAudioTineMasDeUnaCuenta.GetId());

		evalcantidadDeCuentasEncontradasAgregar.addSwitchValue("0",
				stepAudioNotieneCuenta.GetId());
		evalcantidadDeCuentasEncontradasAgregar.addSwitchValue("1",
				stepAudioCuentaVinculadaConExito.GetId());
		evalcantidadDeCuentasEncontradasAgregar.addSwitchValue("2",
				stepAudioTineMasDeUnaCuenta.GetId());

		stepAudioCuentaVinculadaConExito
				.setNextstep(stepAudioDeseaVincularOtraCuenta.GetId());
		stepAudioCuentaDesvinculadaConExito
				.setNextstep(stepAudioDeseaDesvincularOtraCuenta.GetId());

		stepAudioTineMasDeUnaCuenta.setNextstep(stepAudioCuenta.GetId());
		stepAudioCuenta.setNextstep(evalIngresoNumeroDeCuenta.GetId());

		evalIngresoNumeroDeCuenta.addCondition(new condition(1, "length('#{"
				+ ctxVar.getContextVarByName("cuentaContextVar").getVarName()
				+ "}') == " + 7, stepAudioSucursal.GetId(),
				contadorIntentosCuenta.GetId()));

		contadorIntentosCuenta.setNextstep(evalContadorIntentosCuenta.GetId());
		evalContadorIntentosCuenta.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosCuentaContextVar")
						.getVarName() + "} < " + 3, stepAudioCuenta.GetId(),
				stepIfFalseUUID));

		stepAudioSucursal.setNextstep(evalIngresoNumeroDeSucursal.GetId());

		evalIngresoNumeroDeSucursal.addCondition(new condition(1, "length('#{"
				+ ctxVar.getContextVarByName("sucursalContextVar").getVarName()
				+ "}') == " + 3, evalQuiereBorrarOAgregar.GetId(),
				contadorIntentosSucursal.GetId()));

		contadorIntentosSucursal.setNextstep(evalContadorIntentosSucursal
				.GetId());
		evalContadorIntentosSucursal.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosSucursalContextVar")
						.getVarName() + "} < " + 3, stepAudioSucursal.GetId(),
				stepIfFalseUUID));

		evalQuiereBorrarOAgregar.addSwitchValue("3",
				agregoPredeterminada.GetId());
		evalQuiereBorrarOAgregar.addSwitchValue("1",
				agregoPredeterminada.GetId());
		evalQuiereBorrarOAgregar.addSwitchValue("2",
				borroPredeterminada.GetId());

		stepAudioNotieneCuenta.setNextstep(stepAudioSaldosCuentas.GetId());

		/* EVALUA SI QUIERE VINCULAR / DESVINCULAR OTRA CUENTA */

		stepAudioDeseaVincularOtraCuenta
				.setNextstep(stepMenuPredeterminarOtraCuenta.GetId());

		stepAudioDeseaDesvincularOtraCuenta
				.setNextstep(stepMenuPredeterminarOtraCuenta.GetId());

		/* MENU INDISTINTO DE PREDETERMINADAS */

		stepMenuPredeterminarOtraCuenta.addSteps("1",
				stepAudioPredeterminarCuentas.GetId());
		stepMenuPredeterminarOtraCuenta.addSteps("2",
				stepAudioSaldosCuentas.GetId());
		stepMenuPredeterminarOtraCuenta.addSteps("9", stepIfTrueUUID);
		stepMenuPredeterminarOtraCuenta
				.setInvalidOption(stepAudioDeseaVincularOtraCuenta.GetId());

		/* Menu de Saldos */

		stepAudioSaldosCuentas.setNextstep(stepMenuSaldosCuentas.GetId());

		stepMenuSaldosCuentas.addSteps("1",
				stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar.GetId());
		stepMenuSaldosCuentas.addSteps("2",
				stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar.GetId());
		stepMenuSaldosCuentas.addSteps("4",
				stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar.GetId());
		stepMenuSaldosCuentas.addSteps("7",
				stepAudioPredeterminarCuentas.GetId());
		stepMenuSaldosCuentas.addSteps("9", stepIfTrueUUID);
		stepMenuSaldosCuentas.setInvalidOption(stepAudioTipoDeCuenta.GetId());

		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setNextstep(saldoCuentas.GetId());

		saldoCuentas.setNextStepIsTrue(stepAudioSaldosCuentas.GetId());
		saldoCuentas.setNextStepIsFalse(stepAudioNotieneCuenta.GetId());

		/* MENU VINCULAR / DESVINCULAR CUENTAS */

		stepAudioPredeterminarCuentas.setNextstep(stepMenuPredeterminarCuentas
				.GetId());

		stepMenuPredeterminarCuentas.addSteps("1",
				stepAudioCuentaAVincular.GetId());
		stepMenuPredeterminarCuentas.addSteps("2",
				stepAudioCuentaADesvincular.GetId());
		stepMenuPredeterminarCuentas
				.setInvalidOption(stepAudioPredeterminarCuentas.GetId());

		/* VINCULA / DESVINCULA CUENTAS */

		stepAudioCuentaADesvincular.setNextstep(stepAudioTipoDeCuentaABorrar
				.GetId());
		stepAudioCuentaAVincular.setNextstep(stepAudioTipoDeCuentaAAgregar
				.GetId());

		/* TIPO DE CUENTA A BORRAR */

		stepAudioTipoDeCuentaABorrar.setNextstep(stepMenuTipoDeCuentaABorrar
				.GetId());

		stepMenuTipoDeCuentaABorrar.addSteps("1", borroPredeterminada.GetId());
		stepMenuTipoDeCuentaABorrar.addSteps("2", borroPredeterminada.GetId());
		stepMenuTipoDeCuentaABorrar.addSteps("4", borroPredeterminada.GetId());
		stepMenuTipoDeCuentaABorrar
				.setInvalidOption(stepAudioTipoDeCuentaABorrarInvalido.GetId());
		stepAudioTipoDeCuentaABorrarInvalido
				.setNextstep(stepAudioTipoDeCuentaABorrar.GetId());

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return obtieneCuentas.GetId();
	}

	@Override
	public UUID getNextstep() {
		return null;
	}

	public void setStepIfTrueUUID(UUID stepIfTrueUUID) {
		this.stepIfTrueUUID = stepIfTrueUUID;
	}

	public void setStepIfFalseUUID(UUID stepIfFalseUUID) {
		this.stepIfFalseUUID = stepIfFalseUUID;
	}

	public void setContextVar(CallContext ctx) {
		this.ctxVar = ctx;
		createSteps();
	}

	public MenuCuentas() {
		super();

		GroupType = StepGroupType.menuCuentas;
	}

	private void createSteps() {
		/*---Contadores --- */

		contadorIntentosCuenta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosCuenta
				.setStepDescription("COUNTER => INCREMENTO INTENTOS CUENTA");
		contadorIntentosCuenta.setContextVariableName(ctxVar
				.getContextVarByName("intentosCuentaContextVar"));
		Steps.put(contadorIntentosCuenta.GetId(), contadorIntentosCuenta);

		contadorIntentosSucursal = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosSucursal
				.setStepDescription("COUNTER => INCREMENTO INTENTOS SUCURSAL");
		contadorIntentosSucursal.setContextVariableName(ctxVar
				.getContextVarByName("intentosSucursalContextVar"));
		Steps.put(contadorIntentosSucursal.GetId(), contadorIntentosSucursal);

		/*--- Play Read --- */

		/* Sin predeterminadas, Evalua si quiere predeterminar */

		stepAudioQuierePredeterminar = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioQuierePredeterminar
				.setStepDescription("PLAYREAD => NO TIENE PREDETERMINADAS");
		stepAudioQuierePredeterminar.setPlayMaxDigits(1);
		stepAudioQuierePredeterminar.setPlayTimeout(2000L);
		stepAudioQuierePredeterminar.setPlayFile("CUENTAS/002");
		stepAudioQuierePredeterminar.setContextVariableName(ctxVar
				.getContextVarByName("quierePredeterminarContextVar"));
		Steps.put(stepAudioQuierePredeterminar.GetId(),
				stepAudioQuierePredeterminar);

		/* Cuenta a Vincular / Desvincular */

		stepAudioCuentaAVincular = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCuentaAVincular
				.setStepDescription("PLAYREAD => INGRESO 3 ULTIMOS DIGITOS CUENTA A DESVINCULAR");
		stepAudioCuentaAVincular.setPlayMaxDigits(3);
		stepAudioCuentaAVincular.setPlayTimeout(2000L);
		stepAudioCuentaAVincular.setPlayFile("CUENTAS/014");
		stepAudioCuentaAVincular.setContextVariableName(ctxVar
				.getContextVarByName("cuentaContextVar"));
		Steps.put(stepAudioCuentaAVincular.GetId(), stepAudioCuentaAVincular);

		stepAudioCuentaADesvincular = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCuentaADesvincular
				.setStepDescription("PLAYREAD => INGRESO 3 ULTIMOS DIGITOS CUENTA A VINCULAR");
		stepAudioCuentaADesvincular.setPlayMaxDigits(3);
		stepAudioCuentaADesvincular.setPlayTimeout(2000L);
		stepAudioCuentaADesvincular.setPlayFile("CUENTAS/013");
		stepAudioCuentaADesvincular.setContextVariableName(ctxVar
				.getContextVarByName("cuentaContextVar"));
		Steps.put(stepAudioCuentaADesvincular.GetId(),
				stepAudioCuentaADesvincular);

		/* Predeterminadas */

		stepAudioDeseaVincularOtraCuenta = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioDeseaVincularOtraCuenta
				.setStepDescription("PLAYREAD => DESEA VINCULAR OTRA CUENTA");
		stepAudioDeseaVincularOtraCuenta.setPlayMaxDigits(1);
		stepAudioDeseaVincularOtraCuenta.setPlayTimeout(2000L);
		stepAudioDeseaVincularOtraCuenta.setPlayFile("CUENTAS/005");
		stepAudioDeseaVincularOtraCuenta.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarOtraCuentaContextVar"));
		Steps.put(stepAudioDeseaVincularOtraCuenta.GetId(),
				stepAudioDeseaVincularOtraCuenta);

		stepAudioDeseaDesvincularOtraCuenta = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioDeseaDesvincularOtraCuenta
				.setStepDescription("PLAYREAD => DESEA VINCULAR OTRA CUENTA");
		stepAudioDeseaDesvincularOtraCuenta.setPlayMaxDigits(1);
		stepAudioDeseaDesvincularOtraCuenta.setPlayTimeout(2000L);
		stepAudioDeseaDesvincularOtraCuenta.setPlayFile("CUENTAS/007");
		stepAudioDeseaDesvincularOtraCuenta.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarOtraCuentaContextVar"));
		Steps.put(stepAudioDeseaDesvincularOtraCuenta.GetId(),
				stepAudioDeseaDesvincularOtraCuenta);

		stepAudioRepetirPredeterminadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirPredeterminadas
				.setStepDescription("PLAYREAD => REPETIR PREDETERMINADAS");
		stepAudioRepetirPredeterminadas.setPlayMaxDigits(1);
		stepAudioRepetirPredeterminadas.setPlayTimeout(2000L);
		stepAudioRepetirPredeterminadas.setPlayFile("CUENTAS/001");
		stepAudioRepetirPredeterminadas.setContextVariableName(ctxVar
				.getContextVarByName("repetirPredeterminadasContextVar"));
		Steps.put(stepAudioRepetirPredeterminadas.GetId(),
				stepAudioRepetirPredeterminadas);

		/* Tipo de cuenta a vincular / desvincular */

		stepAudioTipoDeCuenta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioTipoDeCuenta
				.setStepDescription("PLAYREAD => INGRESO TIPO DE CUENTA");
		stepAudioTipoDeCuenta.setPlayMaxDigits(1);
		stepAudioTipoDeCuenta.setPlayTimeout(2000L);
		stepAudioTipoDeCuenta.setPlayFile("CUENTAS/012");
		stepAudioTipoDeCuenta.setContextVariableName(ctxVar
				.getContextVarByName("tipoDeCuentaContextVar"));
		Steps.put(stepAudioTipoDeCuenta.GetId(), stepAudioTipoDeCuenta);

		stepAudioTipoDeCuentaAAgregar = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioTipoDeCuentaAAgregar
				.setStepDescription("PLAYREAD => INGRESO TIPO DE CUENTA A AGREGAR");
		stepAudioTipoDeCuentaAAgregar.setPlayMaxDigits(1);
		stepAudioTipoDeCuentaAAgregar.setPlayTimeout(2000L);
		stepAudioTipoDeCuentaAAgregar.setPlayFile("CUENTAS/012");
		stepAudioTipoDeCuentaAAgregar.setContextVariableName(ctxVar
				.getContextVarByName("tipoDeCuentaAAgregarContextVar"));
		Steps.put(stepAudioTipoDeCuentaAAgregar.GetId(),
				stepAudioTipoDeCuentaAAgregar);

		stepAudioTipoDeCuentaABorrar = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioTipoDeCuentaABorrar
				.setStepDescription("PLAYREAD => INGRESO TIPO DE CUENTA A BORRAR");
		stepAudioTipoDeCuentaABorrar.setPlayMaxDigits(1);
		stepAudioTipoDeCuentaABorrar.setPlayTimeout(2000L);
		stepAudioTipoDeCuentaABorrar.setPlayFile("CUENTAS/012");
		stepAudioTipoDeCuentaABorrar.setContextVariableName(ctxVar
				.getContextVarByName("tipoDeCuentaABorrarContextVar"));
		Steps.put(stepAudioTipoDeCuentaABorrar.GetId(),
				stepAudioTipoDeCuentaABorrar);

		/* Tipo de cuenta a consultar */

		stepAudioSaldosCuentas = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSaldosCuentas
				.setStepDescription("PLAYREAD => INGRESO TIPO DE CUENTA");
		stepAudioSaldosCuentas.setPlayMaxDigits(1);
		stepAudioSaldosCuentas.setPlayTimeout(2000L);
		stepAudioSaldosCuentas.setPlayFile("CUENTAS/003");
		stepAudioSaldosCuentas.setContextVariableName(ctxVar
				.getContextVarByName("saldosCuentasContextVar"));
		Steps.put(stepAudioSaldosCuentas.GetId(), stepAudioSaldosCuentas);

		/* Consulta saldo */

		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setStepDescription("PLAYREAD => INGRESO TRES ULTIMOS DIGITOS DE LA CUENTA");
		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setPlayMaxDigits(3);
		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setPlayTimeout(2000L);
		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setPlayFile("CUENTAS/008");
		stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar
				.setContextVariableName(ctxVar
						.getContextVarByName("ultimosTresDigitosCuentaContextVar"));
		Steps.put(
				stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar.GetId(),
				stepAudioIngreseUltimosTresDigitosDeLaCuentaAConsultar);

		/* Datos Si Coinciden 3 Digitos de Cuentas */

		stepAudioSucursal = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSucursal
				.setStepDescription("PLAYREAD => INGRESO 3 DIGITOS DE LA SUCURSAL");
		stepAudioSucursal.setPlayMaxDigits(3);
		stepAudioSucursal.setPlayTimeout(5000L);
		stepAudioSucursal.setPlayFile("CUENTAS/009");
		stepAudioSucursal.setContextVariableName(ctxVar
				.getContextVarByName("sucursalContextVar"));
		Steps.put(stepAudioSucursal.GetId(), stepAudioSucursal);

		stepAudioCuenta = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioCuenta
				.setStepDescription("PLAYREAD => INGRESO 7 DIGITOS CUENTA");
		stepAudioCuenta.setPlayMaxDigits(7);
		stepAudioCuenta.setPlayTimeout(5000L);
		stepAudioCuenta.setPlayFile("CUENTAS/010");
		stepAudioCuenta.setContextVariableName(ctxVar
				.getContextVarByName("cuentaContextVar"));
		Steps.put(stepAudioCuenta.GetId(), stepAudioCuenta);

		/*--- Editar Predeterminadas --- */

		stepAudioPredeterminarCuentas = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioPredeterminarCuentas
				.setStepDescription("PLAYREAD => EDITAR PREDETERMINADAS");
		stepAudioPredeterminarCuentas.setPlayMaxDigits(1);
		stepAudioPredeterminarCuentas.setPlayTimeout(2000L);
		stepAudioPredeterminarCuentas.setPlayFile("CUENTAS/011");
		stepAudioPredeterminarCuentas.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarCuentasContextVar"));
		Steps.put(stepAudioPredeterminarCuentas.GetId(),
				stepAudioPredeterminarCuentas);

		/*--- SWITCH --- */

		evalcantidadDeCuentasEncontradasAgregar = (StepSwitch) StepFactory
				.createStep(StepType.Switch, UUID.randomUUID());
		evalcantidadDeCuentasEncontradasAgregar.setContextVariableName(ctxVar
				.getContextVarByName("cantidadDeCuentasEncontradasContextVar"));
		evalcantidadDeCuentasEncontradasAgregar
				.setStepDescription("SWITCH => CANTIDAD DE CUENTAS ENCONTRADAS");
		Steps.put(evalcantidadDeCuentasEncontradasAgregar.GetId(),
				evalcantidadDeCuentasEncontradasAgregar);

		evalcantidadDeCuentasEncontradasBorrar = (StepSwitch) StepFactory
				.createStep(StepType.Switch, UUID.randomUUID());
		evalcantidadDeCuentasEncontradasBorrar.setContextVariableName(ctxVar
				.getContextVarByName("cantidadDeCuentasEncontradasContextVar"));
		evalcantidadDeCuentasEncontradasBorrar
				.setStepDescription("SWITCH => CANTIDAD DE CUENTAS ENCONTRADAS");
		Steps.put(evalcantidadDeCuentasEncontradasBorrar.GetId(),
				evalcantidadDeCuentasEncontradasBorrar);

		evalQuiereBorrarOAgregar = (StepSwitch) StepFactory.createStep(
				StepType.Switch, UUID.randomUUID());
		evalQuiereBorrarOAgregar.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarCuentasContextVar"));
		evalQuiereBorrarOAgregar
				.setStepDescription("SWITCH => QUIERE BORRAR O AGREGAR");
		Steps.put(evalQuiereBorrarOAgregar.GetId(), evalQuiereBorrarOAgregar);

		/*--- AUDIOS --- */

		stepAudioTipoDeCuentaAAgregarInvalido = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioTipoDeCuentaAAgregarInvalido
				.setStepDescription("PLAY => NO TIENE ESE TIPO DE CUENTA");
		stepAudioTipoDeCuentaAAgregarInvalido.setPlayfile("CUENTAS/015");
		Steps.put(stepAudioTipoDeCuentaAAgregarInvalido.GetId(),
				stepAudioTipoDeCuentaAAgregarInvalido);

		stepAudioTipoDeCuentaABorrarInvalido = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioTipoDeCuentaABorrarInvalido
				.setStepDescription("PLAY => NO TIENE ESE TIPO DE CUENTA");
		stepAudioTipoDeCuentaABorrarInvalido.setPlayfile("CUENTAS/015");
		Steps.put(stepAudioTipoDeCuentaABorrarInvalido.GetId(),
				stepAudioTipoDeCuentaABorrarInvalido);

		stepAudioTineMasDeUnaCuenta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTineMasDeUnaCuenta
				.setStepDescription("PLAY => TIENE MAS DE UNA CUENTA");
		stepAudioTineMasDeUnaCuenta.setPlayfile("CUENTAS/016");
		Steps.put(stepAudioTineMasDeUnaCuenta.GetId(),
				stepAudioTineMasDeUnaCuenta);

		/* vincula - desvincula */

		stepAudioCuentaVinculadaConExito = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCuentaVinculadaConExito
				.setStepDescription("PLAY => CUENTA VINCULADA CON EXITO");
		stepAudioCuentaVinculadaConExito.setPlayfile("CUENTAS/004");
		Steps.put(stepAudioCuentaVinculadaConExito.GetId(),
				stepAudioCuentaVinculadaConExito);

		stepAudioCuentaDesvinculadaConExito = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioCuentaDesvinculadaConExito
				.setStepDescription("PLAY => CUENTA DESVINCULADA CON EXITO");
		stepAudioCuentaDesvinculadaConExito.setPlayfile("CUENTAS/006");
		Steps.put(stepAudioCuentaDesvinculadaConExito.GetId(),
				stepAudioCuentaDesvinculadaConExito);

		stepAudioNotieneCuenta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNotieneCuenta
				.setStepDescription("PLAY => NO SE ENCONTRO CUENTA");
		stepAudioNotieneCuenta.setPlayfile("CUENTAS/015");
		Steps.put(stepAudioNotieneCuenta.GetId(), stepAudioNotieneCuenta);

		/*--- CONDICIONAL --- */

		evalTieneCuentasPredeterminadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalTieneCuentasPredeterminadas
				.setStepDescription("CONDITIONAL => TIENE CUENTAS PREDETERMINADAS ");
		Steps.put(evalTieneCuentasPredeterminadas.GetId(),
				evalTieneCuentasPredeterminadas);

		evalIngresoNumeroDeSucursal = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalIngresoNumeroDeSucursal
				.setStepDescription("CONDITIONAL => NUMERO DE SUCURSAL COMPLETO");
		Steps.put(evalIngresoNumeroDeSucursal.GetId(),
				evalIngresoNumeroDeSucursal);

		evalIngresoNumeroDeCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalIngresoNumeroDeCuenta
				.setStepDescription("CONDITIONAL => NUMERO DE CUENTA COMPLETO");
		Steps.put(evalIngresoNumeroDeCuenta.GetId(), evalIngresoNumeroDeCuenta);

		evalContadorIntentosCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosCuenta
				.setStepDescription("CONDITIONAL => INTENTOS CUENTA");
		Steps.put(evalContadorIntentosCuenta.GetId(),
				evalContadorIntentosCuenta);

		evalContadorIntentosSucursal = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosSucursal
				.setStepDescription("CONDITIONAL => INTENTOS SUCURSAL");
		Steps.put(evalContadorIntentosSucursal.GetId(),
				evalContadorIntentosSucursal);

		/*--- MENU --- */

		/* Sin Predeterminadas */

		stepMenuQuierePredeterminar = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuQuierePredeterminar
				.setStepDescription("MENU => NO TIENE PREDETERMINADAS");
		stepMenuQuierePredeterminar.setContextVariableName(ctxVar
				.getContextVarByName("quierePredeterminarContextVar"));
		Steps.put(stepMenuQuierePredeterminar.GetId(),
				stepMenuQuierePredeterminar);

		/* Repite Predeterminadas */

		stepMenuRepetirPredeterminadas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuRepetirPredeterminadas
				.setStepDescription("MENU => REPETIR PREDETERMINADAS");
		stepMenuRepetirPredeterminadas.setContextVariableName(ctxVar
				.getContextVarByName("repetirPredeterminadasContextVar"));
		Steps.put(stepMenuRepetirPredeterminadas.GetId(),
				stepMenuRepetirPredeterminadas);

		stepMenuPredeterminarCuentas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuPredeterminarCuentas
				.setStepDescription("MENU => PREDETERMINAR CUENTAS");
		stepMenuPredeterminarCuentas.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarCuentasContextVar"));
		Steps.put(stepMenuPredeterminarCuentas.GetId(),
				stepMenuPredeterminarCuentas);

		stepMenuPredeterminarOtraCuenta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuPredeterminarOtraCuenta
				.setStepDescription("MENU => PREDETERMINAR OTRA CUENTA");
		stepMenuPredeterminarOtraCuenta.setContextVariableName(ctxVar
				.getContextVarByName("predeterminarOtraCuentaContextVar"));
		Steps.put(stepMenuPredeterminarOtraCuenta.GetId(),
				stepMenuPredeterminarOtraCuenta);

		stepMenuSaldosCuentas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuSaldosCuentas.setStepDescription("MENU => TIPO DE CUENTA");
		stepMenuSaldosCuentas.setContextVariableName(ctxVar
				.getContextVarByName("saldosCuentasContextVar"));
		Steps.put(stepMenuSaldosCuentas.GetId(), stepMenuSaldosCuentas);

		/*--- tipos de cuenta --- */

		stepMenuTipoDeCuentaAAgregar = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuTipoDeCuentaAAgregar
				.setStepDescription("MENU => TIPO DE CUENTA A AGREGAR");
		stepMenuTipoDeCuentaAAgregar.setContextVariableName(ctxVar
				.getContextVarByName("tipoDeCuentaAAgregarContextVar"));
		Steps.put(stepMenuTipoDeCuentaAAgregar.GetId(),
				stepMenuTipoDeCuentaAAgregar);

		stepMenuTipoDeCuentaABorrar = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuTipoDeCuentaABorrar
				.setStepDescription("MENU => TIPO DE CUENTA A BORRAR");
		stepMenuTipoDeCuentaABorrar.setContextVariableName(ctxVar
				.getContextVarByName("tipoDeCuentaABorrarContextVar"));
		Steps.put(stepMenuTipoDeCuentaABorrar.GetId(),
				stepMenuTipoDeCuentaABorrar);

		/*--- Obtiene totalidad de Cuentas --- */

		obtieneCuentas = (StepGetCuentas) StepFactory.createStep(
				StepType.GetCuentas, UUID.randomUUID());
		obtieneCuentas.setTieneCuentasPredeterminadasContextVar(ctxVar
				.getContextVarByName("tieneCuentasPredeterminadasContextVar"));
		obtieneCuentas.setCuentasContextVar(ctxVar
				.getContextVarByName("cuentasContextVar"));
		obtieneCuentas.setCuentasPredeterminadasContextVar(ctxVar
				.getContextVarByName("cuentasPredeterminadasContextVar"));
		obtieneCuentas.setIdCrecerContextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		obtieneCuentas.setStepDescription("GETCUENTAS => OBTIENE CUENTAS");
		Steps.put(obtieneCuentas.GetId(), obtieneCuentas);

		/*--- Saldos Cuentas --- */

		saldoCuentas = (StepSayAccountBalance) StepFactory.createStep(
				StepType.SayAccountBalance, UUID.randomUUID());
		saldoCuentas.setCuentasContextVar(ctxVar
				.getContextVarByName("cuentasContextVar"));
		saldoCuentas.setTipoDeCuentaContextVar(ctxVar
				.getContextVarByName("saldosCuentasContextVar"));
		saldoCuentas.setUltimosTresDigitosCuentaContextVar(ctxVar
				.getContextVarByName("ultimosTresDigitosCuentaContextVar"));
		saldoCuentas.setSoapDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		Steps.put(saldoCuentas.GetId(), saldoCuentas);

		saldoPredeterminadas = (StepSayDefaultAccountBalance) StepFactory
				.createStep(StepType.SayDefaultAccountBalance,
						UUID.randomUUID());
		saldoPredeterminadas.setSoapDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		saldoPredeterminadas.setCuentasPredeterminadasContextVar(ctxVar
				.getContextVarByName("cuentasPredeterminadasContextVar"));
		Steps.put(saldoPredeterminadas.GetId(), saldoPredeterminadas);

		/*--- VINCULO / DESVINCULO CUENTAS --- */

		agregoPredeterminada = (StepDefaultAcount) StepFactory.createStep(
				StepType.DefaultAcount, UUID.randomUUID());
		agregoPredeterminada.setIdCrecercontextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		agregoPredeterminada.setCuentacontextVar(ctxVar
				.getContextVarByName("cuentaContextVar"));
		agregoPredeterminada.setEsCuentaPredeterminadaContextVar(ctxVar
				.getContextVarByName("vinculoCuentaContextVar"));
		agregoPredeterminada.setTipoCuentaContextVar(ctxVar
				.getContextVarByName("tipoDeCuentaAAgregarContextVar"));
		agregoPredeterminada.setCuentasContextVar(ctxVar
				.getContextVarByName("cuentasContextVar"));
		agregoPredeterminada.setCantidadDeCuentasEncontradasContextVar(ctxVar
				.getContextVarByName("cantidadDeCuentasEncontradasContextVar"));
		agregoPredeterminada.setSucursalContextVar(ctxVar
				.getContextVarByName("sucursalContextVar"));
		Steps.put(agregoPredeterminada.GetId(), agregoPredeterminada);

		borroPredeterminada = (StepDefaultAcount) StepFactory.createStep(
				StepType.DefaultAcount, UUID.randomUUID());
		borroPredeterminada.setIdCrecercontextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		borroPredeterminada.setCuentacontextVar(ctxVar
				.getContextVarByName("cuentaContextVar"));
		borroPredeterminada.setEsCuentaPredeterminadaContextVar(ctxVar
				.getContextVarByName("desvinculoCuentaContextVar"));
		borroPredeterminada.setTipoCuentaContextVar(ctxVar
				.getContextVarByName("tipoDeCuentaABorrarContextVar"));
		borroPredeterminada.setCuentasContextVar(ctxVar
				.getContextVarByName("cuentasContextVar"));
		borroPredeterminada.setCantidadDeCuentasEncontradasContextVar(ctxVar
				.getContextVarByName("cantidadDeCuentasEncontradasContextVar"));
		borroPredeterminada.setSucursalContextVar(ctxVar
				.getContextVarByName("sucursalContextVar"));
		Steps.put(borroPredeterminada.GetId(), borroPredeterminada);

	}
}
