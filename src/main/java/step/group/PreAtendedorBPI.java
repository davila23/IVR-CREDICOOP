package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.StepAnswer;
import step.StepAuthInitialInfo;
import step.StepCheckCuenta;
import step.StepCheckCuentaEnDialPlan;
import step.StepCheckServiceBanca;
import step.StepClearKeyBPI;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepInitDniDB;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayNumber;
import step.StepSetAsteriskVariable;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PreAtendedorBPI implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	protected UUID stepIfTrueUUID;
	protected UUID stepIfFalseUUID;
	protected CallContext ctxVar;
	protected StepMenu stepMenuInicialBPI;
	protected StepPlayRead stepAudioMenuInicial;
	protected StepPlayRead stepAudioMenuClaves;
	protected StepPlayRead stepAudioMenuTransferencias;
	protected StepPlayRead stepAudioMenuPagos;
	protected StepPlayRead stepAudioMenuOtrasConsultas;
	protected StepCounter contadorIntentosMenuInicialBPI;
	protected StepCounter contadorIntentosMenuClaves;
	protected StepCounter contadorIntentosMenuTransferencias;
	protected StepCounter contadorIntentosMenuPagos;
	protected StepCounter contadorIntentosMenuOtrasConsultas;
	protected StepConditional evalContadorMenuInicialBPI;
	protected StepConditional evalContadorMenuClaves;
	protected StepConditional evalContadorMenuTransferencias;
	protected StepConditional evalContadorMenuPagos;
	protected StepConditional evalContadorMenuOtrasConsultas;
	protected StepMenu stepMenuPagos;
	protected StepMenu stepMenuTransferencias;
	protected StepMenu stepMenuClaves;
	protected StepMenu stepMenuOtrasConsultas;
	protected StepPlay stepAudioParaComenzarAOperar;
	protected StepPlay stepAudioParaGenerarClaveEnUnLink;
	protected StepMenu stepSubMenuClaves;
	protected StepPlayRead stepAudioSubMenuClaves;
	protected StepTimeConditionDB obtieneHorario;
	protected StepPlay stepAudioPrevioDerivoAsesor;
	protected StepPlay stepAudioDisuadeDerivoAsesor;
	protected StepCounter contadorIntentosRepeticionSubMenu;
	protected StepConditional evalContadorIntentosRepeticionSubMenuClaves;
	protected StepCounter contadorIntentosRepeticionSubMenuClaves;
	protected StepPlay stepAudioOlvidoLaClave;
	protected StepPlayRead stepAudioMenuOlvidoClave;
	protected StepMenu stepMenuOlvidoClave;
	protected StepCounter contadorIntentosOlvidoClave;
	protected StepConditional evalContadorIntentosOlvidoClave;
	protected PideDni pideDniGrp;
	protected PideDni pideDniSegundoFactorGrp;
	protected StepPlay stepAudioSegundoFactor;
	protected StepPlay stepAudioAsesoramientoAUsuario;
	protected StepPlayRead stepAudioMenuAsesoramientoAUsuario;
	protected StepPlay stepAudioSiOlvidoLaClave;
	protected StepMenu stepMenuAsesoramientoAUsuario;
	protected StepConditional evalAsesoramientoAUsuario;
	protected StepCounter contadorAsesoramientoAUsuario;
	protected StepPlay stepAudioTutorialWeb;
	protected StepPlay stepAudioTutorialWeb2;
	protected StepPlayRead stepAudioSubMenuTransferencia;
	protected StepMenu stepSubMenuTransferencia;
	protected StepConditional evalContadorRepetirInformacionTransferencias;
	protected StepCounter contadorIntentosRepetirInformacionTransferencias;
	protected StepPlay stepAudioTutorialPagos;
	protected StepCounter contadorIntentosRepetirInformacionPagos;
	protected StepConditional evalContadorRepetirInformacionPagos;
	protected StepPlay stepAudioPrevioSubMenuPagos;
	protected StepPlayRead stepAudioSubMenuPagos;
	protected StepMenu stepSubMenuPagos;
	protected StepCounter contadorIntentosRepetirInformacionSubMenuPagos;
	protected StepConditional evalContadorRepetirInformacionSubMenuPagos;
	protected StepPlay stepAudioPrevioMenuOtrasConsultas;
	protected StepPlayRead stepAudioMenuTarjetaCoordenadas;
	protected StepMenu stepMenuTarjetaCoordenadas;
	protected StepCounter contadorIntentosMenuTarjetaCoordenadas;
	protected StepConditional evalContadorIntentosMenuTarjetaCoordenadas;
	protected StepMenu stepMenuRepetirInformacionTarjetaCoordenadas;
	protected StepPlayRead stepAudioRepetirInformacionTarjetaCoordenadas;
	protected StepPlay stepAudioPrevioSubMenuTarjetaCoordenadas;
	protected StepPlay stepAudioInfoAdicionalMovil;
	protected StepPlay stepAudioInfoAdicionalTarjetaCoordenadas;
	protected StepConditional evalContadorIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepCounter contadorIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepPlayRead stepAudioRepetirInfoAdicionalMovil;
	protected StepPlayRead stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas;
	protected StepMenu stepMenuRepetirAdicionalInfoTarjetaCoordenadas;
	protected StepMenu stepMenuRepetirAdicionalInfoMovil;
	protected StepCounter contadorIntentosSubMenuPagos;
	protected StepConditional evalContadorSubMenuPagos;
	protected StepPlayRead stepAudioRepetirTutorial;
	protected StepMenu stepMenuRepetirTutorial;
	protected StepCounter contadorIntentosRepetirTutorial;
	protected StepConditional evalContadorRepetirMenuTutorial;
	protected StepCounter contadorIntentosSubMenuClaves;
	protected StepConditional evalContadorIntentosSubMenuClaves;
	protected StepPlay stepAudioDesbloqueoSegundoFactor;
	protected StepPlay stepAudioFinal;
	protected StepPlay stepIngresoNuloEIncorrecto;
	protected StepCounter contadorIntentosMenuRepetirInformacionTarjetaCoordenadas;
	protected StepConditional evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepPlay stepIngresoNuloEIncorrecto14;
	protected StepPlay stepIngresoNuloEIncorrecto13;
	protected StepPlay stepIngresoNuloEIncorrecto12;
	protected StepPlay stepIngresoNuloEIncorrecto11;
	protected StepPlay stepIngresoNuloEIncorrecto10;
	protected StepPlay stepIngresoNuloEIncorrecto9;
	protected StepPlay stepIngresoNuloEIncorrecto8;
	protected StepPlay stepIngresoNuloEIncorrecto7;
	protected StepPlay stepIngresoNuloEIncorrecto6;
	protected StepPlay stepIngresoNuloEIncorrecto5;
	protected StepPlay stepIngresoNuloEIncorrecto4;
	protected StepPlay stepIngresoNuloEIncorrecto3;
	protected StepPlay stepIngresoNuloEIncorrecto2;
	protected StepPlay stepIngresoNuloEIncorrecto1;
	protected StepCounter contadorIntentosRepetirInfoTransferencia;
	protected StepConditional evalContadorRepetirInfoTransferencial;
	protected StepClearKeyBPI stepClearKey;
	protected GeneracionDeClaveBPI claveBpiGrp;
	protected StepMenu stepMenuIngresoDatosCuenta;
	protected StepMenu stepMenuConfirmacionIngresoRutina;
	protected StepMenu stepMenuCambioDeTarjeta;
	protected StepMenu stepMenuRepetirPIN;
	protected StepPlayRead stepAudioInicio;
	protected StepPlayRead stepAudioRepetirPIN;
	protected StepPlay stepAudioOKClearBPI;
	protected StepPlay stepAudioVerificarDatos;
	protected StepPlay stepAudioClearBpiOk;
	protected StepPlay stepAudioClaerBpiOkUnaHS;
	protected StepPlay stepAudioErrorClearBpi;
	protected StepPlay stepAudioErrorClearBpiFilial;
	protected StepPlay stepAudioDniIncorrecto;
	protected StepPlay stepAudioTarjetaNoOperaConBpi;
	protected StepPlay stepAudioFechaIncorrecta;
	protected StepPlay stepAudioIngreseUnDigitoValido;
	protected StepPlay stepAudioCantidadMaxDeReintentos;
	protected StepPlay stepAudioCantidadMaxDeReintentos2;
	protected StepPlay stepAudioCuentaPropia;
	protected StepPlay stepAudioDerivoAsesor;
	protected StepPlay stepAudioNumeroDeTarjetaIncorrecto;
	protected StepPlay stepAudioTarjetaNoVigente;
	protected StepPlay stepAudioServNoDisponible;
	protected StepCounter contadorIntentosInit;
	protected StepCounter contadorIntentosCuenta;
	protected StepCounter contadorIntentosIngresoRutina;
	protected StepConditional evalContadorIngresoRutina;
	protected StepConditional evalContadorCuenta;
	protected StepConditional evalContadorInit;
	protected StepGetAsteriskVariable obtieneTarjeta;
	protected StepCheckCuentaEnDialPlan stepCheckCuentaEnDialPlan;
	protected StepInitDniDB initDB;
	protected StepIsOwnCard esTarjetaPropia;
	protected StepCheckCuenta checkCuenta;
	protected PideCuenta pideCuentaGrp;
	protected PideFechaCredicoop pideFechaGrp;
	protected PideTarjetaCredicoop pideTarjetaGrp;
	protected StepGetAsteriskVariable obtieneDni;
	protected StepGetAsteriskVariable obtieneIdCrecer;
	protected StepSwitch evalRetornoClearBpi;
	protected StepSetAsteriskVariable stepSetReingreso;
	protected StepPlayRead stepAudioInicioRutinaClearKey;
	protected StepConditional evalInit;
	protected PideKeyBPI pideKeyBpi;
	protected StepCheckServiceBanca stepServiceBanca;
	private StepPlay stepAudioCantidadMaxDeReintentosCuenta;
	private StepPlay stepAudioCantidadMaxDeReintentosTarjeta;
	private StepPlay stepAudioCantidadMaxDeReintentosFecha;
	private StepSetAsteriskVariable stepSetDni;

	private void setSequence() {

		/* Ejecuto Init */

		evalInit.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("initDbContextVar").getVarName()
				+ "} == " + "0", stepCheckCuentaEnDialPlan.GetId(),
				stepAudioMenuInicial.GetId()));

		/* Menu Inicial BPI */

		stepAudioMenuInicial.setNextstep(stepMenuInicialBPI.GetId());

		stepMenuInicialBPI.addSteps("1", stepAudioMenuClaves.GetId());
		stepMenuInicialBPI.addSteps("2", stepAudioMenuTransferencias.GetId());
		stepMenuInicialBPI.addSteps("3", stepAudioTutorialPagos.GetId());
		stepMenuInicialBPI.addSteps("4",
				stepAudioPrevioMenuOtrasConsultas.GetId());
		stepMenuInicialBPI.setInvalidOption(contadorIntentosMenuInicialBPI
				.GetId());

		contadorIntentosMenuInicialBPI.setNextstep(evalContadorMenuInicialBPI
				.GetId());
		evalContadorMenuInicialBPI.addCondition(new condition(1,
				"#{"
						+ ctxVar.getContextVarByName(
								"intentosMenuInicialBPIContextVar")
								.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto.setNextstep(stepAudioMenuInicial.GetId());

		/* -- 1 -- Menu Gestion de Claves */

		stepAudioMenuClaves.setNextstep(stepMenuClaves.GetId());

		stepMenuClaves.addSteps("1", stepAudioInicioRutinaClearKey.GetId());
		stepMenuClaves.addSteps("2", stepAudioMenuTarjetaCoordenadas.GetId());
		stepMenuClaves.addSteps("3", stepAudioAsesoramientoAUsuario.GetId());
		stepMenuClaves.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuClaves.setInvalidOption(contadorIntentosMenuClaves.GetId());

		contadorIntentosMenuClaves.setNextstep(evalContadorMenuClaves.GetId());
		evalContadorMenuClaves.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuClavesContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto1.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto1.setNextstep(stepAudioMenuClaves.GetId());

		// OPCION -- > 1 - 1 .

		stepAudioParaGenerarClaveEnUnLink
				.setNextstep(stepAudioParaComenzarAOperar.GetId());

		stepAudioParaComenzarAOperar
				.setNextstep(stepAudioSubMenuClaves.GetId());

		stepAudioSubMenuClaves.setNextstep(stepSubMenuClaves.GetId());
		stepSubMenuClaves.addSteps("1",
				contadorIntentosRepeticionSubMenuClaves.GetId());
		stepSubMenuClaves.addSteps("2", obtieneHorario.GetId());
		stepSubMenuClaves.addSteps("8", stepAudioMenuClaves.GetId());
		stepSubMenuClaves.addSteps("9", stepAudioMenuInicial.GetId());
		stepSubMenuClaves.setInvalidOption(contadorIntentosSubMenuClaves
				.GetId());

		contadorIntentosRepeticionSubMenuClaves
				.setNextstep(evalContadorIntentosRepeticionSubMenuClaves
						.GetId());
		evalContadorIntentosRepeticionSubMenuClaves.addCondition(new condition(
				1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosRepeticionSubMenuClavesContextVar")
								.getVarName() + "} < " + intentos,
				stepAudioParaComenzarAOperar.GetId(), stepIfFalseUUID));

		contadorIntentosSubMenuClaves
				.setNextstep(evalContadorIntentosSubMenuClaves.GetId());
		evalContadorIntentosSubMenuClaves.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosSubMenuClavesContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto2.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto2.setNextstep(stepAudioSubMenuClaves.GetId());

		// OPCION -- > 1 - 2 . Quitado 23 - 10 - 17

		// stepAudioOlvidoLaClave.setNextstep(stepAudioMenuOlvidoClave.GetId());
		//
		// stepAudioMenuOlvidoClave.setNextstep(stepMenuOlvidoClave.GetId());
		// // stepMenuOlvidoClave.addSteps("1", obtieneHorario.GetId());
		// stepMenuOlvidoClave.addSteps("8", stepAudioMenuClaves.GetId());
		// stepMenuOlvidoClave.addSteps("9", stepAudioMenuInicial.GetId());
		// stepMenuOlvidoClave.setInvalidOption(contadorIntentosOlvidoClave
		// .GetId());
		//
		// contadorIntentosOlvidoClave.setNextstep(evalContadorIntentosOlvidoClave
		// .GetId());
		// evalContadorIntentosOlvidoClave.addCondition(new condition(1, "#{"
		// + ctxVar.getContextVarByName("intentosOlvidoClaveContextVar")
		// .getVarName() + "} < " + intentos,
		// stepIngresoNuloEIncorrecto3.GetId(), stepIfFalseUUID));
		//
		// stepIngresoNuloEIncorrecto3.setNextstep(stepAudioMenuOlvidoClave
		// .GetId());

		// OPCION -- > 1 - 2 .

		stepAudioMenuTarjetaCoordenadas.setNextstep(stepMenuTarjetaCoordenadas
				.GetId());
		stepMenuTarjetaCoordenadas.addSteps("1",
				stepAudioPrevioSubMenuTarjetaCoordenadas.GetId());
		stepMenuTarjetaCoordenadas.addSteps("2", pideDniGrp.getInitialStep());
		stepMenuTarjetaCoordenadas.addSteps("8", stepAudioMenuClaves.GetId());
		stepMenuTarjetaCoordenadas.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuTarjetaCoordenadas
				.setInvalidOption(contadorIntentosMenuTarjetaCoordenadas
						.GetId());

		contadorIntentosMenuTarjetaCoordenadas
				.setNextstep(evalContadorIntentosMenuTarjetaCoordenadas.GetId());
		evalContadorIntentosMenuTarjetaCoordenadas.addCondition(new condition(
				1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosTarjetaCoordenadasContextVar")
								.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto4.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto4.setNextstep(stepAudioMenuTarjetaCoordenadas
				.GetId());

		// 1 - 2 - 1

		stepAudioPrevioSubMenuTarjetaCoordenadas
				.setNextstep(stepAudioRepetirInformacionTarjetaCoordenadas
						.GetId());

		stepAudioRepetirInformacionTarjetaCoordenadas
				.setNextstep(stepMenuRepetirInformacionTarjetaCoordenadas
						.GetId());
		stepMenuRepetirInformacionTarjetaCoordenadas.addSteps("1",
				contadorIntentosRepetirInformacionTarjetaCoordenadas.GetId());
		stepMenuRepetirInformacionTarjetaCoordenadas.addSteps("8",
				stepAudioMenuClaves.GetId());
		stepMenuRepetirInformacionTarjetaCoordenadas
				.setInvalidOption(contadorIntentosMenuRepetirInformacionTarjetaCoordenadas
						.GetId());

		contadorIntentosRepetirInformacionTarjetaCoordenadas
				.setNextstep(evalContadorIntentosRepetirInformacionTarjetaCoordenadas
						.GetId());
		evalContadorIntentosRepetirInformacionTarjetaCoordenadas
				.addCondition(new condition(1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosSubTarjetaCoordenadasContextVar")
								.getVarName() + "} < " + intentos,
						stepAudioPrevioSubMenuTarjetaCoordenadas.GetId(),
						stepIfFalseUUID));

		contadorIntentosMenuRepetirInformacionTarjetaCoordenadas
				.setNextstep(evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas
						.GetId());
		evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas
				.addCondition(new condition(1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosMenuSubTarjetaCoordenadasContextVar")
								.getVarName() + "} < " + intentos,
						stepIngresoNuloEIncorrecto5.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto5
				.setNextstep(stepAudioRepetirInformacionTarjetaCoordenadas
						.GetId());

		// 1 - 2 - 2

		pideDniSegundoFactorGrp.setStepIfTrue(stepAudioDesbloqueoSegundoFactor
				.GetId());
		pideDniSegundoFactorGrp.setStepIfFalse(stepAudioFinal.GetId());

		stepAudioDesbloqueoSegundoFactor.setNextstep(stepAudioMenuInicial
				.GetId());

		// Requerimiento al pedo ->

		stepAudioInfoAdicionalMovil
				.setNextstep(stepAudioRepetirInfoAdicionalMovil.GetId());

		stepAudioRepetirInfoAdicionalMovil
				.setNextstep(stepMenuRepetirAdicionalInfoMovil.GetId());
		stepMenuRepetirAdicionalInfoMovil.addSteps("1",
				stepAudioInfoAdicionalMovil.GetId());
		stepMenuRepetirAdicionalInfoMovil.addSteps("2",
				stepAudioInfoAdicionalTarjetaCoordenadas.GetId());
		stepMenuRepetirAdicionalInfoMovil
				.setInvalidOption(stepIngresoNuloEIncorrecto6.GetId());

		stepIngresoNuloEIncorrecto6.setNextstep(stepAudioMenuTarjetaCoordenadas
				.GetId());

		stepAudioInfoAdicionalTarjetaCoordenadas
				.setNextstep(stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
						.GetId());

		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setNextstep(stepMenuRepetirAdicionalInfoTarjetaCoordenadas
						.GetId());
		stepMenuRepetirAdicionalInfoTarjetaCoordenadas.addSteps("1",
				stepAudioInfoAdicionalMovil.GetId());
		stepMenuRepetirAdicionalInfoTarjetaCoordenadas.addSteps("2",
				stepAudioInfoAdicionalTarjetaCoordenadas.GetId());
		stepMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setInvalidOption(stepIngresoNuloEIncorrecto7.GetId());

		stepIngresoNuloEIncorrecto7.setNextstep(stepAudioMenuTarjetaCoordenadas
				.GetId());

		stepAudioSegundoFactor.setNextstep(stepAudioMenuInicial.GetId());

		// OPCION -- > 1 - 3 .

		stepAudioAsesoramientoAUsuario
				.setNextstep(stepAudioMenuAsesoramientoAUsuario.GetId());

		stepAudioMenuAsesoramientoAUsuario
				.setNextstep(stepMenuAsesoramientoAUsuario.GetId());
		stepMenuAsesoramientoAUsuario.addSteps("1", obtieneHorario.GetId());
		stepMenuAsesoramientoAUsuario.addSteps("2", obtieneHorario.GetId());
		stepMenuAsesoramientoAUsuario
				.addSteps("8", stepAudioMenuClaves.GetId());
		stepMenuAsesoramientoAUsuario.addSteps("9",
				stepAudioMenuInicial.GetId());
		stepMenuAsesoramientoAUsuario
				.setInvalidOption(contadorAsesoramientoAUsuario.GetId());

		contadorAsesoramientoAUsuario.setNextstep(evalAsesoramientoAUsuario
				.GetId());
		evalAsesoramientoAUsuario.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosAsesoramientoAUsuarioContextVar").getVarName()
				+ "} < " + intentos, stepIngresoNuloEIncorrecto8.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto8
				.setNextstep(stepAudioMenuAsesoramientoAUsuario.GetId());

		/* -- 2 -- Menu Transferencias */

		stepAudioMenuTransferencias.setNextstep(stepMenuTransferencias.GetId());

		stepMenuTransferencias.addSteps("1", stepAudioTutorialWeb.GetId());
		stepMenuTransferencias.addSteps("2", obtieneHorario.GetId());
		stepMenuTransferencias.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuTransferencias
				.setInvalidOption(contadorIntentosMenuTransferencias.GetId());

		contadorIntentosMenuTransferencias
				.setNextstep(evalContadorMenuTransferencias.GetId());
		evalContadorMenuTransferencias.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosMenuTransferenciasContextVar").getVarName()
				+ "} < " + intentos, stepIngresoNuloEIncorrecto9.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto9.setNextstep(stepAudioMenuTransferencias
				.GetId());

		// 2 - 1

		stepAudioTutorialWeb.setNextstep(stepAudioRepetirTutorial.GetId());
		stepAudioRepetirTutorial.setNextstep(stepMenuRepetirTutorial.GetId());
		stepMenuRepetirTutorial.addSteps("1",
				contadorIntentosRepetirInfoTransferencia.GetId());
		stepMenuRepetirTutorial.addSteps("8",
				stepAudioMenuTransferencias.GetId());
		stepMenuRepetirTutorial
				.setInvalidOption(contadorIntentosRepetirTutorial.GetId());

		contadorIntentosRepetirInfoTransferencia
				.setNextstep(evalContadorRepetirInfoTransferencial.GetId());
		evalContadorRepetirInfoTransferencial.addCondition(new condition(1,
				"#{"
						+ ctxVar.getContextVarByName(
								"intentosRepetirTutorialContextVar")
								.getVarName() + "} < " + intentos,
				stepAudioTutorialWeb.GetId(), stepIfFalseUUID));

		contadorIntentosRepetirTutorial
				.setNextstep(evalContadorRepetirMenuTutorial.GetId());
		evalContadorRepetirMenuTutorial.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuTutorialContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto10.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto10.setNextstep(stepAudioRepetirTutorial
				.GetId());

		// 2 - 2

		stepAudioTutorialWeb2
				.setNextstep(stepAudioSubMenuTransferencia.GetId());
		stepAudioSubMenuTransferencia.setNextstep(stepSubMenuTransferencia
				.GetId());
		stepSubMenuTransferencia.addSteps("1",
				contadorIntentosRepetirInformacionTransferencias.GetId());
		stepSubMenuTransferencia.addSteps("2", obtieneHorario.GetId());
		stepSubMenuTransferencia.addSteps("8", stepAudioMenuClaves.GetId());
		stepSubMenuTransferencia.addSteps("9", stepAudioMenuInicial.GetId());
		stepSubMenuTransferencia
				.setInvalidOption(contadorIntentosRepetirInformacionTransferencias
						.GetId());

		contadorIntentosRepetirInformacionTransferencias
				.setNextstep(evalContadorRepetirInformacionTransferencias
						.GetId());
		evalContadorRepetirInformacionTransferencias
				.addCondition(new condition(
						1,
						"#{"
								+ ctxVar.getContextVarByName(
										"intentosRepetirInformacionTransferenciaContextVar")
										.getVarName() + "} < " + intentos,
						stepIngresoNuloEIncorrecto11.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto11.setNextstep(stepAudioTutorialWeb2.GetId());

		/* -- 3 -- Menu Pagos */

		stepAudioTutorialPagos.setNextstep(stepAudioMenuPagos.GetId());
		stepAudioMenuPagos.setNextstep(stepMenuPagos.GetId());

		stepMenuPagos.addSteps("1",
				contadorIntentosRepetirInformacionPagos.GetId());
		stepMenuPagos.addSteps("2", obtieneHorario.GetId());
		stepMenuPagos.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuPagos.setInvalidOption(contadorIntentosMenuPagos.GetId());

		contadorIntentosMenuPagos.setNextstep(evalContadorMenuPagos.GetId());
		evalContadorMenuPagos.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuPagosContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto12.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto12.setNextstep(stepAudioMenuPagos.GetId());

		contadorIntentosRepetirInformacionPagos
				.setNextstep(evalContadorRepetirInformacionPagos.GetId());
		evalContadorRepetirInformacionPagos.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosRepetirInformacionMenuPagosContextVar")
						.getVarName() + "} < " + intentos,
				stepAudioTutorialPagos.GetId(), stepIfFalseUUID));

		stepAudioPrevioSubMenuPagos.setNextstep(stepAudioSubMenuPagos.GetId());
		stepAudioSubMenuPagos.setNextstep(stepSubMenuPagos.GetId());

		stepSubMenuPagos.addSteps("1",
				contadorIntentosRepetirInformacionSubMenuPagos.GetId());
		stepSubMenuPagos.addSteps("2", obtieneHorario.GetId());
		stepSubMenuPagos.addSteps("8", stepAudioMenuPagos.GetId());
		stepSubMenuPagos.addSteps("9", stepAudioMenuInicial.GetId());
		stepSubMenuPagos.setInvalidOption(contadorIntentosSubMenuPagos.GetId());

		contadorIntentosSubMenuPagos.setNextstep(evalContadorSubMenuPagos
				.GetId());
		evalContadorSubMenuPagos.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosSubMenuPagosContextVar")
						.getVarName() + "} < " + intentos,
				stepAudioSubMenuPagos.GetId(), stepIfFalseUUID));

		contadorIntentosRepetirInformacionSubMenuPagos
				.setNextstep(evalContadorRepetirInformacionSubMenuPagos.GetId());
		evalContadorRepetirInformacionSubMenuPagos
				.addCondition(new condition(
						1,
						"#{"
								+ ctxVar.getContextVarByName(
										"intentosRepetirInformacionSubMenuPagosContextVar")
										.getVarName() + "} < " + intentos,
						stepIngresoNuloEIncorrecto13.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto13.setNextstep(stepAudioPrevioSubMenuPagos
				.GetId());

		/* -- 4 -- Otras consultas */

		stepAudioPrevioMenuOtrasConsultas
				.setNextstep(stepAudioMenuOtrasConsultas.GetId());

		stepAudioMenuOtrasConsultas.setNextstep(stepMenuOtrasConsultas.GetId());
		stepMenuOtrasConsultas.addSteps("1", obtieneHorario.GetId());
		stepMenuOtrasConsultas.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuOtrasConsultas
				.setInvalidOption(contadorIntentosMenuOtrasConsultas.GetId());

		contadorIntentosMenuOtrasConsultas
				.setNextstep(evalContadorMenuOtrasConsultas.GetId());
		evalContadorMenuOtrasConsultas.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosMenuOtrasConsultasContextVar").getVarName()
				+ "} < " + intentos, stepIngresoNuloEIncorrecto14.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto14.setNextstep(stepAudioMenuOtrasConsultas
				.GetId());

		/* DERIVO */

		obtieneHorario.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepIfTrueUUID);

		stepAudioDisuadeDerivoAsesor.setNextstep(stepAudioMenuInicial.GetId());

		/* Generacion de clave */

		stepAudioInicioRutinaClearKey
				.setNextstep(stepMenuConfirmacionIngresoRutina.GetId());

		stepMenuConfirmacionIngresoRutina.addSteps("1",
				pideDniGrp.getInitialStep());
		stepMenuConfirmacionIngresoRutina.addSteps("2",
				stepAudioMenuClaves.GetId());
		stepMenuConfirmacionIngresoRutina.setInvalidOption(stepAudioMenuClaves
				.GetId());

		/* Ingreso a la rutina Generacion de clave */

		pideDniGrp.setStepIfTrue(stepSetReingreso.GetId());
		pideDniGrp.setStepIfFalse(stepAudioMenuClaves.GetId());

		stepServiceBanca.setNextStepIsTrue(stepSetReingreso.GetId());
		stepServiceBanca.setNextStepIsFalse(stepAudioServNoDisponible.GetId());

		stepAudioServNoDisponible.setNextstep(stepAudioMenuClaves.GetId());

		stepSetReingreso.setNextstep(stepSetDni.GetId());
		stepSetDni.setNextstep(initDB.GetId());
		/* --- Tiene cuentas --- */

		stepCheckCuentaEnDialPlan.setNextStepIsFalse(stepAudioDniIncorrecto
				.GetId());

		stepAudioDniIncorrecto.setNextstep(pideDniGrp.getInitialStep());

		stepCheckCuentaEnDialPlan.setNextStepIsTrue(pideFechaGrp
				.getInitialStep());

		/* --- Fecha --- */

		pideFechaGrp.setStepIfTrue(pideTarjetaGrp.getInitialStep());
		pideFechaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentosFecha
				.GetId());

		stepAudioCantidadMaxDeReintentosFecha.setNextstep(stepAudioMenuClaves
				.GetId());

		/* --- Tarjeta --- */

		pideTarjetaGrp.setStepIfTrue(obtieneIdCrecer.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentosTarjeta
				.GetId());

		stepAudioCantidadMaxDeReintentosTarjeta.setNextstep(stepAudioMenuClaves
				.GetId());

		/* --- Es tarjeta propia --- */

		obtieneIdCrecer.setNextstep(esTarjetaPropia.GetId());

		esTarjetaPropia.setNextStepIsTrue(pideCuentaGrp.getInitialStep());
		// esTarjetaPropia.setNextStepIsFalse(stepAudioNumeroDeTarjetaIncorrecto
		// .GetId());
		esTarjetaPropia.setNextStepIsFalse(pideCuentaGrp.getInitialStep());

		stepAudioNumeroDeTarjetaIncorrecto.setNextstep(stepAudioMenuClaves
				.GetId());

		/* --- Cuenta --- */

		pideCuentaGrp.setStepIfTrue(checkCuenta.GetId());
		pideCuentaGrp.setStepIfFalse(stepAudioCantidadMaxDeReintentosCuenta
				.GetId());

		stepAudioCantidadMaxDeReintentosCuenta.setNextstep(stepAudioMenuClaves
				.GetId());

		checkCuenta.setNextStepIsTrue(pideKeyBpi.getInitialStep());
		checkCuenta.setNextStepIsFalse(contadorIntentosCuenta.GetId());

		contadorIntentosCuenta.setNextstep(evalContadorCuenta.GetId());
		evalContadorCuenta.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosCuentaPropiaContextVar")
						.getVarName() + "} < " + intentos,
				stepAudioCuentaPropia.GetId(), stepAudioCantidadMaxDeReintentos
						.GetId()));

		stepAudioCuentaPropia.setNextstep(pideCuentaGrp.getInitialStep());

		/* --- Pide Key BPI --- */

		pideKeyBpi.setStepIfTrueUUID(stepClearKey.GetId());
		pideKeyBpi.setStepIfFalseUUID(stepAudioMenuClaves.GetId());

		/* --- Blanqueo el Bpi --- */

		stepClearKey.setNextStepIsTrue(stepAudioClearBpiOk.GetId());
		stepClearKey.setNextStepIsFalse(stepAudioErrorClearBpi.GetId());

		/* --- Fin --- */

		evalRetornoClearBpi.setNextstep(stepAudioErrorClearBpi.GetId());

		/* --- Retornos --- */

		stepAudioErrorClearBpi.setNextstep(stepAudioMenuClaves.GetId());

		stepAudioClearBpiOk.setNextstep(stepAudioMenuClaves.GetId());

		stepAudioTarjetaNoOperaConBpi.setNextstep(stepIfFalseUUID);

		stepAudioCantidadMaxDeReintentos.setNextstep(stepIfFalseUUID);

		stepAudioFinal.setNextstep(stepIfFalseUUID);

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return evalInit.GetId();

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

	public PreAtendedorBPI() {
		super();
		GroupType = StepGroupType.preAtendedorBPI;
	}

	private void createSteps() {

		pideKeyBpi = (PideKeyBPI) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideKeyBPI);
		pideKeyBpi.setContextVar(ctxVar);

		pideDniSegundoFactorGrp = (PideDni) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDni);
		pideDniSegundoFactorGrp.setAudioDni("PREATENDEDORCABAL/033");
		pideDniSegundoFactorGrp.setAudioValidateDni("PREATENDEDORCABAL/035");
		pideDniSegundoFactorGrp.setAudioDniInvalido("PREATENDEDORCABAL/036");
		pideDniSegundoFactorGrp.setAudioSuDniEs("PREATENDEDORCABAL/034");
		pideDniSegundoFactorGrp.setDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		pideDniSegundoFactorGrp.setConfirmaDniContextVar(ctxVar
				.getContextVarByName("confirmaDniContextVar"));
		pideDniSegundoFactorGrp.setIntentosDniContextVar(ctxVar
				.getContextVarByName("intentosDniContextVar"));

		obtieneHorario = (StepTimeConditionDB) StepFactory.createStep(
				StepType.TimeConditionDB, UUID.randomUUID());
		obtieneHorario
				.setStepDescription("Obtiene horario de la base de datos");
		obtieneHorario.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorario.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorario.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		Steps.put(obtieneHorario.GetId(), obtieneHorario);

		/*--- Play Read --- */

		stepAudioMenuInicial = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuInicial.setStepDescription("PLAYREAD => MENU INICIAL BPI");
		stepAudioMenuInicial.setPlayMaxDigits(1);
		stepAudioMenuInicial.setPlayTimeout(2000L);
		stepAudioMenuInicial.setPlayFile("PREATENDEDORCABAL/026");
		stepAudioMenuInicial.setContextVariableName(ctxVar
				.getContextVarByName("menuInicialBPIContextVar"));
		Steps.put(stepAudioMenuInicial.GetId(), stepAudioMenuInicial);

		// Sub - Menus

		stepAudioMenuClaves = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuClaves.setStepDescription("PLAYREAD => MENU CLAVES BPI");
		stepAudioMenuClaves.setPlayMaxDigits(1);
		stepAudioMenuClaves.setPlayTimeout(2000L);
		stepAudioMenuClaves.setPlayFile("PREATENDEDORCABAL/002");
		stepAudioMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("menuClavesContextVar"));
		Steps.put(stepAudioMenuClaves.GetId(), stepAudioMenuClaves);

		stepAudioMenuTransferencias = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuTransferencias
				.setStepDescription("PLAYREAD => MENU TRANSFERENCIAS BPI");
		stepAudioMenuTransferencias.setPlayMaxDigits(1);
		stepAudioMenuTransferencias.setPlayTimeout(2000L);
		stepAudioMenuTransferencias.setPlayFile("PREATENDEDORCABAL/003");
		stepAudioMenuTransferencias.setContextVariableName(ctxVar
				.getContextVarByName("menuTransferenciasContextVar"));
		Steps.put(stepAudioMenuTransferencias.GetId(),
				stepAudioMenuTransferencias);

		stepAudioMenuPagos = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuPagos.setStepDescription("PLAYREAD => MENU PAGOS BPI");
		stepAudioMenuPagos.setPlayMaxDigits(1);
		stepAudioMenuPagos.setPlayTimeout(2000L);
		stepAudioMenuPagos.setPlayFile("PREATENDEDORCABAL/004");
		stepAudioMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("menuPagosContextVar"));
		Steps.put(stepAudioMenuPagos.GetId(), stepAudioMenuPagos);

		stepAudioMenuOtrasConsultas = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuOtrasConsultas
				.setStepDescription("PLAYREAD => MENU OTRAS CONSULTAS BPI");
		stepAudioMenuOtrasConsultas.setPlayMaxDigits(1);
		stepAudioMenuOtrasConsultas.setPlayTimeout(2000L);
		stepAudioMenuOtrasConsultas.setPlayFile("PREATENDEDORCABAL/005");
		stepAudioMenuOtrasConsultas.setContextVariableName(ctxVar
				.getContextVarByName("menuOtrasConsultasContextVar"));
		Steps.put(stepAudioMenuOtrasConsultas.GetId(),
				stepAudioMenuOtrasConsultas);
		// Sub Menu Claves

		stepAudioSubMenuClaves = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSubMenuClaves
				.setStepDescription("PLAYREAD => SUB MENU CLAVES BPI");
		stepAudioSubMenuClaves.setPlayMaxDigits(1);
		stepAudioSubMenuClaves.setPlayTimeout(2000L);
		stepAudioSubMenuClaves.setPlayFile("PREATENDEDORCABAL/006");
		stepAudioSubMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("subMenuClavesContextVar"));
		Steps.put(stepAudioSubMenuClaves.GetId(), stepAudioSubMenuClaves);

		stepAudioMenuOlvidoClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuOlvidoClave
				.setStepDescription("PLAYREAD => MENU OLVIDO CLAVE BPI");
		stepAudioMenuOlvidoClave.setPlayMaxDigits(1);
		stepAudioMenuOlvidoClave.setPlayTimeout(2000L);
		stepAudioMenuOlvidoClave.setPlayFile("PREATENDEDORCABAL/037");
		stepAudioMenuOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("olvidoClaveContextVar"));
		Steps.put(stepAudioMenuOlvidoClave.GetId(), stepAudioMenuOlvidoClave);

		stepAudioMenuAsesoramientoAUsuario = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuAsesoramientoAUsuario
				.setStepDescription("PLAYREAD => MENU ASESORAMIENTO A USUARIO BPI");
		stepAudioMenuAsesoramientoAUsuario.setPlayMaxDigits(1);
		stepAudioMenuAsesoramientoAUsuario.setPlayTimeout(2000L);
		stepAudioMenuAsesoramientoAUsuario.setPlayFile("PREATENDEDORCABAL/008");
		stepAudioMenuAsesoramientoAUsuario.setContextVariableName(ctxVar
				.getContextVarByName("asesoramientoAUsiaroContextVar"));
		Steps.put(stepAudioMenuAsesoramientoAUsuario.GetId(),
				stepAudioMenuAsesoramientoAUsuario);

		stepAudioSubMenuTransferencia = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSubMenuTransferencia
				.setStepDescription("PLAYREAD => MENU TRASNFERENCIA BPI");
		stepAudioSubMenuTransferencia.setPlayMaxDigits(1);
		stepAudioSubMenuTransferencia.setPlayTimeout(2000L);
		stepAudioSubMenuTransferencia.setPlayFile("PREATENDEDORCABAL/009");
		stepAudioSubMenuTransferencia.setContextVariableName(ctxVar
				.getContextVarByName("subMenuTransferenciaContextVar"));
		Steps.put(stepAudioSubMenuTransferencia.GetId(),
				stepAudioSubMenuTransferencia);

		stepAudioSubMenuPagos = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioSubMenuPagos
				.setStepDescription("PLAYREAD => SUBMENU PAGOS BPI");
		stepAudioSubMenuPagos.setPlayMaxDigits(1);
		stepAudioSubMenuPagos.setPlayTimeout(2000L);
		stepAudioSubMenuPagos.setPlayFile("PREATENDEDORCABAL/010");
		stepAudioSubMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("subMenuPagosContextVar"));
		Steps.put(stepAudioSubMenuPagos.GetId(), stepAudioSubMenuPagos);

		stepAudioMenuTarjetaCoordenadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuTarjetaCoordenadas
				.setStepDescription("PLAYREAD => MENU TARJETA COORDENADAS BPI");
		stepAudioMenuTarjetaCoordenadas.setPlayMaxDigits(1);
		stepAudioMenuTarjetaCoordenadas.setPlayTimeout(2000L);
		stepAudioMenuTarjetaCoordenadas.setPlayFile("PREATENDEDORCABAL/011");
		stepAudioMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("menuTarjetaCoordenadasContextVar"));
		Steps.put(stepAudioMenuTarjetaCoordenadas.GetId(),
				stepAudioMenuTarjetaCoordenadas);

		stepAudioRepetirInformacionTarjetaCoordenadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirInformacionTarjetaCoordenadas
				.setStepDescription("PLAYREAD => MENU TARJETA COORDENADAS BPI");
		stepAudioRepetirInformacionTarjetaCoordenadas.setPlayMaxDigits(1);
		stepAudioRepetirInformacionTarjetaCoordenadas.setPlayTimeout(2000L);
		stepAudioRepetirInformacionTarjetaCoordenadas
				.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("subMenuTarjetaCoordenadasContextVar"));
		Steps.put(stepAudioRepetirInformacionTarjetaCoordenadas.GetId(),
				stepAudioRepetirInformacionTarjetaCoordenadas);

		stepAudioRepetirInfoAdicionalMovil = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirInfoAdicionalMovil
				.setStepDescription("PLAYREAD => REPETIR INFORMACION CREDICOOP MOVIL BPI");
		stepAudioRepetirInfoAdicionalMovil.setPlayMaxDigits(1);
		stepAudioRepetirInfoAdicionalMovil.setPlayTimeout(2000L);
		stepAudioRepetirInfoAdicionalMovil.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioRepetirInfoAdicionalMovil.setContextVariableName(ctxVar
				.getContextVarByName("adicionalMovilContextVar"));
		Steps.put(stepAudioRepetirInfoAdicionalMovil.GetId(),
				stepAudioRepetirInfoAdicionalMovil);

		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setStepDescription("PLAYREAD =>  REPETIR INFORMACION TARJETA COORDENADAS BPI");
		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas.setPlayMaxDigits(1);
		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setPlayTimeout(2000L);
		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("adicionalTarjetaCoordenadasContextVar"));
		Steps.put(stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas.GetId(),
				stepAudioMenuRepetirAdicionalInfoTarjetaCoordenadas);

		stepAudioRepetirTutorial = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirTutorial
				.setStepDescription("PLAYREAD =>  REPETIR INFORMACION TUTORIAL BPI");
		stepAudioRepetirTutorial.setPlayMaxDigits(1);
		stepAudioRepetirTutorial.setPlayTimeout(2000L);
		stepAudioRepetirTutorial.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioRepetirTutorial.setContextVariableName(ctxVar
				.getContextVarByName("repetirInformacionTutorialContextVar"));
		Steps.put(stepAudioRepetirTutorial.GetId(), stepAudioRepetirTutorial);

		/*--- Play --- */
		stepAudioParaComenzarAOperar = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioParaComenzarAOperar.setPlayfile("PREATENDEDORCABAL/015bis");
		stepAudioParaComenzarAOperar
				.setStepDescription("PLAY => PARA COMENZAR A OPERAR EN BPI GENERAR CLAVE EN UN CAJERO LINK");
		Steps.put(stepAudioParaComenzarAOperar.GetId(),
				stepAudioParaComenzarAOperar);

		stepAudioParaGenerarClaveEnUnLink = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioParaGenerarClaveEnUnLink.setPlayfile("PREATENDEDORCABAL/015");
		stepAudioParaGenerarClaveEnUnLink
				.setStepDescription("PLAY => PASOS GENERAR CLAVE EN UN CAJERO LINK");
		Steps.put(stepAudioParaGenerarClaveEnUnLink.GetId(),
				stepAudioParaGenerarClaveEnUnLink);

		stepAudioSegundoFactor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSegundoFactor.setPlayfile("PREATENDEDORCABAL/017");
		stepAudioSegundoFactor
				.setStepDescription("PLAY => TUTORIAL SEGUNDO FACTOR");
		Steps.put(stepAudioSegundoFactor.GetId(), stepAudioSegundoFactor);

		stepAudioDesbloqueoSegundoFactor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDesbloqueoSegundoFactor.setPlayfile("PREATENDEDORCABAL/025");
		stepAudioDesbloqueoSegundoFactor
				.setStepDescription("PLAY => DESBLOQUEO SEGUNDO FACTOR");
		Steps.put(stepAudioDesbloqueoSegundoFactor.GetId(),
				stepAudioDesbloqueoSegundoFactor);

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		stepAudioDisuadeDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisuadeDerivoAsesor.setPlayfile("PREATENDEDORCABAL/098");
		stepAudioDisuadeDerivoAsesor
				.setStepDescription("PLAY => DISUADE DERIVO POR ESTAR FUERA DE HORARIO");
		Steps.put(stepAudioDisuadeDerivoAsesor.GetId(),
				stepAudioDisuadeDerivoAsesor);

		stepAudioOlvidoLaClave = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioOlvidoLaClave.setPlayfile("PREATENDEDORCABAL/016");
		stepAudioOlvidoLaClave.setStepDescription("PLAY => AUDIO OLVIDO CLAVE");
		Steps.put(stepAudioOlvidoLaClave.GetId(), stepAudioOlvidoLaClave);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL RUTINA BPI");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

		stepAudioAsesoramientoAUsuario = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioAsesoramientoAUsuario.setPlayfile("PREATENDEDORCABAL/018");
		stepAudioAsesoramientoAUsuario
				.setStepDescription("PLAY => ASESORAMIENTO A USUARIO");
		Steps.put(stepAudioAsesoramientoAUsuario.GetId(),
				stepAudioAsesoramientoAUsuario);

		stepAudioTutorialWeb = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioTutorialWeb.setPlayfile("PREATENDEDORCABAL/042");
		stepAudioTutorialWeb.setStepDescription("PLAY => TUTORIAL WEB BPI");
		Steps.put(stepAudioTutorialWeb.GetId(), stepAudioTutorialWeb);

		stepAudioTutorialWeb2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTutorialWeb2.setPlayfile("PREATENDEDORCABAL/042");
		stepAudioTutorialWeb2.setStepDescription("PLAY => TUTORIAL WEB BPI");
		Steps.put(stepAudioTutorialWeb2.GetId(), stepAudioTutorialWeb2);

		stepAudioTutorialPagos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTutorialPagos.setPlayfile("PREATENDEDORCABAL/020");
		stepAudioTutorialPagos.setStepDescription("PLAY => TUTORIAL PAGOS BPI");
		Steps.put(stepAudioTutorialPagos.GetId(), stepAudioTutorialPagos);

		stepAudioPrevioSubMenuPagos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioSubMenuPagos.setPlayfile("PREATENDEDORCABAL/021");
		stepAudioPrevioSubMenuPagos
				.setStepDescription("PLAY => SUB MENU PAGOS BPI");
		Steps.put(stepAudioPrevioSubMenuPagos.GetId(),
				stepAudioPrevioSubMenuPagos);

		stepAudioPrevioMenuOtrasConsultas = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioMenuOtrasConsultas.setPlayfile("PREATENDEDORCABAL/042");
		stepAudioPrevioMenuOtrasConsultas
				.setStepDescription("PLAY => PREVIO OTRAS CONSULTAS BPI");
		Steps.put(stepAudioPrevioMenuOtrasConsultas.GetId(),
				stepAudioPrevioMenuOtrasConsultas);

		stepIngresoNuloEIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto.GetId(),
				stepIngresoNuloEIncorrecto);

		stepIngresoNuloEIncorrecto1 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto1.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto1.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto1.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto1
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto1.GetId(),
				stepIngresoNuloEIncorrecto1);

		stepIngresoNuloEIncorrecto2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto2.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto2.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto2.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto2
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto2.GetId(),
				stepIngresoNuloEIncorrecto2);

		stepIngresoNuloEIncorrecto3 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto3.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto3.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto3.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto3
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto3.GetId(),
				stepIngresoNuloEIncorrecto3);

		stepIngresoNuloEIncorrecto4 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto4.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto4.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto4.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto4
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto4.GetId(),
				stepIngresoNuloEIncorrecto4);

		stepIngresoNuloEIncorrecto5 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto5.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto5.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto5.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto5
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto5.GetId(),
				stepIngresoNuloEIncorrecto5);

		stepIngresoNuloEIncorrecto6 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto6.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto6.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto6.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto6
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto6.GetId(),
				stepIngresoNuloEIncorrecto6);

		stepIngresoNuloEIncorrecto7 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto7.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto7.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto7.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto7
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto7.GetId(),
				stepIngresoNuloEIncorrecto7);

		stepIngresoNuloEIncorrecto8 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto8.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto8.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto8.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto8
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto8.GetId(),
				stepIngresoNuloEIncorrecto8);

		stepIngresoNuloEIncorrecto9 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto9.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto9.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto9.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto9
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto9.GetId(),
				stepIngresoNuloEIncorrecto9);

		stepIngresoNuloEIncorrecto10 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto10.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto10.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto10.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto10
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto10.GetId(),
				stepIngresoNuloEIncorrecto10);

		stepIngresoNuloEIncorrecto11 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto11.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto11.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto11.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto11
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto11.GetId(),
				stepIngresoNuloEIncorrecto11);

		stepIngresoNuloEIncorrecto12 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto12.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto12.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto12
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		stepIngresoNuloEIncorrecto12.setScapeDigit("0123456789*#");
		Steps.put(stepIngresoNuloEIncorrecto12.GetId(),
				stepIngresoNuloEIncorrecto12);

		stepIngresoNuloEIncorrecto13 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto13.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto13.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto13.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto13
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto13.GetId(),
				stepIngresoNuloEIncorrecto13);

		stepIngresoNuloEIncorrecto14 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepIngresoNuloEIncorrecto14.setPlayfile("PREATENDEDORCABAL/029");
		stepIngresoNuloEIncorrecto14.setContextVariableName(ctxVar
				.getContextVarByName("scapeDigitContextVar"));
		stepIngresoNuloEIncorrecto14.setScapeDigit("0123456789*#");
		stepIngresoNuloEIncorrecto14
				.setStepDescription("PLAY => INGRESO NULO E INCORRECTO");
		Steps.put(stepIngresoNuloEIncorrecto14.GetId(),
				stepIngresoNuloEIncorrecto14);

		stepAudioPrevioSubMenuTarjetaCoordenadas = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioPrevioSubMenuTarjetaCoordenadas
				.setPlayfile("PREATENDEDORCABAL/017");
		stepAudioPrevioSubMenuTarjetaCoordenadas
				.setStepDescription("PLAY => PREVIO MENU TARJETA DE COORDENADAS BPI");
		Steps.put(stepAudioPrevioSubMenuTarjetaCoordenadas.GetId(),
				stepAudioPrevioSubMenuTarjetaCoordenadas);

		stepAudioInfoAdicionalMovil = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioInfoAdicionalMovil.setPlayfile("PREATENDEDORCABAL/023");
		stepAudioInfoAdicionalMovil
				.setStepDescription("PLAY => INFORMACION ADICIONAL CREDICOOP MOVIL BPI");
		Steps.put(stepAudioInfoAdicionalMovil.GetId(),
				stepAudioInfoAdicionalMovil);

		stepAudioInfoAdicionalTarjetaCoordenadas = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioInfoAdicionalTarjetaCoordenadas
				.setPlayfile("PREATENDEDORCABAL/023");
		stepAudioInfoAdicionalTarjetaCoordenadas
				.setStepDescription("PLAY => INFORMACION ADICIONAL TARJETA DE COORDENADAS BPI");
		Steps.put(stepAudioInfoAdicionalTarjetaCoordenadas.GetId(),
				stepAudioInfoAdicionalTarjetaCoordenadas);

		/*--- Contadores --- */

		contadorIntentosMenuInicialBPI = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuInicialBPI.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuInicialBPIContextVar"));
		contadorIntentosMenuInicialBPI
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE MENU INICIAL");
		Steps.put(contadorIntentosMenuInicialBPI.GetId(),
				contadorIntentosMenuInicialBPI);

		contadorIntentosMenuClaves = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuClavesContextVar"));
		contadorIntentosMenuClaves
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE MENU CLAVES");
		Steps.put(contadorIntentosMenuClaves.GetId(),
				contadorIntentosMenuClaves);

		contadorIntentosMenuTransferencias = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuTransferencias.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuTransferenciasContextVar"));
		contadorIntentosMenuTransferencias
				.setStepDescription("COUNTER => INCREMENTA INTENTOS MENU TRANSFERENCIAS");
		Steps.put(contadorIntentosMenuTransferencias.GetId(),
				contadorIntentosMenuTransferencias);

		contadorIntentosRepetirInformacionTransferencias = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionTransferencias
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosRepetirInformacionTransferenciaContextVar"));
		contadorIntentosRepetirInformacionTransferencias
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION TRANSFERENCIAS");
		Steps.put(contadorIntentosRepetirInformacionTransferencias.GetId(),
				contadorIntentosRepetirInformacionTransferencias);

		contadorIntentosMenuPagos = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuPagosContextVar"));
		contadorIntentosMenuPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE MENU PAGOS");
		Steps.put(contadorIntentosMenuPagos.GetId(), contadorIntentosMenuPagos);

		contadorIntentosMenuOtrasConsultas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuOtrasConsultas.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuOtrasConsultasContextVar"));
		contadorIntentosMenuOtrasConsultas
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE MENU OTRAS CONSULTAS");
		Steps.put(contadorIntentosMenuOtrasConsultas.GetId(),
				contadorIntentosMenuOtrasConsultas);

		contadorIntentosSubMenuClaves = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosSubMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("intentosSubMenuClavesContextVar"));
		contadorIntentosSubMenuClaves
				.setStepDescription("COUNTER => INCREMENTA INTENTOS SUB MENU CLAVES");
		Steps.put(contadorIntentosSubMenuClaves.GetId(),
				contadorIntentosSubMenuClaves);

		contadorIntentosRepetirTutorial = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirTutorial.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuTutorialContextVar"));
		contadorIntentosRepetirTutorial
				.setStepDescription("COUNTER => INCREMENTA INTENTOS MENU REPETIR INFORMACION TUTORIAL");
		Steps.put(contadorIntentosRepetirTutorial.GetId(),
				contadorIntentosRepetirTutorial);

		contadorIntentosRepeticionSubMenuClaves = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepeticionSubMenuClaves
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosRepeticionSubMenuClavesContextVar"));
		contadorIntentosRepeticionSubMenuClaves
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE SUB MENU CLAVES");
		Steps.put(contadorIntentosRepeticionSubMenuClaves.GetId(),
				contadorIntentosRepeticionSubMenuClaves);

		contadorIntentosRepeticionSubMenu = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepeticionSubMenu.setContextVariableName(ctxVar
				.getContextVarByName("intentosRepeticionSubMenuContextVar"));
		contadorIntentosRepeticionSubMenu
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION SUB MENU");
		Steps.put(contadorIntentosRepeticionSubMenu.GetId(),
				contadorIntentosRepeticionSubMenu);

		contadorIntentosMenuTarjetaCoordenadas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("intentosTarjetaCoordenadasContextVar"));
		contadorIntentosMenuTarjetaCoordenadas
				.setStepDescription("COUNTER => INCREMENTA INTENTOS TARJETA COORDENADAS");
		Steps.put(contadorIntentosMenuTarjetaCoordenadas.GetId(),
				contadorIntentosMenuTarjetaCoordenadas);

		contadorIntentosOlvidoClave = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("intentosOlvidoClaveContextVar"));
		contadorIntentosOlvidoClave
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE OLVIDO CLAVE");
		Steps.put(contadorIntentosOlvidoClave.GetId(),
				contadorIntentosOlvidoClave);

		contadorAsesoramientoAUsuario = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorAsesoramientoAUsuario
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosAsesoramientoAUsuarioContextVar"));
		contadorAsesoramientoAUsuario
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE ASESORAMIENTO A USUARIO");
		Steps.put(contadorAsesoramientoAUsuario.GetId(),
				contadorAsesoramientoAUsuario);

		contadorIntentosRepetirInformacionPagos = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionPagos
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosRepetirInformacionMenuPagosContextVar"));
		contadorIntentosRepetirInformacionPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION PAGOS");
		Steps.put(contadorIntentosRepetirInformacionPagos.GetId(),
				contadorIntentosRepetirInformacionPagos);

		contadorIntentosRepetirInformacionSubMenuPagos = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionSubMenuPagos
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosSubMenuPagosContextVar"));
		contadorIntentosRepetirInformacionSubMenuPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION SUB MENU PAGOS");
		Steps.put(contadorIntentosRepetirInformacionSubMenuPagos.GetId(),
				contadorIntentosRepetirInformacionSubMenuPagos);

		contadorIntentosRepetirInformacionTarjetaCoordenadas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosSubTarjetaCoordenadasContextVar"));
		contadorIntentosRepetirInformacionTarjetaCoordenadas
				.setStepDescription("COUNTER => INCREMENTA INTENTOS TARJETA COORDENADAS");
		Steps.put(contadorIntentosRepetirInformacionTarjetaCoordenadas.GetId(),
				contadorIntentosRepetirInformacionTarjetaCoordenadas);

		contadorIntentosMenuRepetirInformacionTarjetaCoordenadas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosMenuSubTarjetaCoordenadasContextVar"));
		contadorIntentosMenuRepetirInformacionTarjetaCoordenadas
				.setStepDescription("COUNTER => INCREMENTA  MENU INTENTOS TARJETA COORDENADAS");
		Steps.put(contadorIntentosMenuRepetirInformacionTarjetaCoordenadas
				.GetId(),
				contadorIntentosMenuRepetirInformacionTarjetaCoordenadas);

		contadorIntentosSubMenuPagos = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosSubMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("intentosSubMenuPagosContextVar"));
		contadorIntentosSubMenuPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS SUB MENU PAGOS");
		Steps.put(contadorIntentosSubMenuPagos.GetId(),
				contadorIntentosSubMenuPagos);

		contadorIntentosRepetirInfoTransferencia = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInfoTransferencia.setContextVariableName(ctxVar
				.getContextVarByName("intentosRepetirTutorialContextVar"));
		contadorIntentosRepetirInfoTransferencia
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION TRANSFERENCIA");
		Steps.put(contadorIntentosRepetirInfoTransferencia.GetId(),
				contadorIntentosRepetirInfoTransferencia);

		/*--- Condicional --- */

		evalContadorMenuInicialBPI = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuInicialBPI
				.setStepDescription("CONDITIONAL => INTENTOS MENU INICIAL BPI");
		Steps.put(evalContadorMenuInicialBPI.GetId(),
				evalContadorMenuInicialBPI);

		evalContadorMenuClaves = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuClaves
				.setStepDescription("CONDITIONAL => INTENTOS MENU CLAVES BPI");
		Steps.put(evalContadorMenuClaves.GetId(), evalContadorMenuClaves);

		evalContadorMenuTransferencias = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuTransferencias
				.setStepDescription("CONDITIONAL => INTENTOS MENU TRANSFERENCIAS BPI");
		Steps.put(evalContadorMenuTransferencias.GetId(),
				evalContadorMenuTransferencias);

		evalContadorIntentosMenuTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosMenuTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS MENU TARJETA COORDENADAS");
		Steps.put(evalContadorIntentosMenuTarjetaCoordenadas.GetId(),
				evalContadorIntentosMenuTarjetaCoordenadas);

		evalContadorMenuPagos = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuPagos
				.setStepDescription("CONDITIONAL => INTENTOS MENU PAGOS BPI");
		Steps.put(evalContadorMenuPagos.GetId(), evalContadorMenuPagos);

		evalContadorMenuOtrasConsultas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuOtrasConsultas
				.setStepDescription("CONDITIONAL => INTENTOS MENU OTRAS CONSULTAS BPI");
		Steps.put(evalContadorMenuOtrasConsultas.GetId(),
				evalContadorMenuOtrasConsultas);

		evalContadorIntentosRepeticionSubMenuClaves = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosRepeticionSubMenuClaves
				.setStepDescription("CONDITIONAL => INTENTOS MENU OTRAS REPETIR SUB MENU CLAVES");
		Steps.put(evalContadorIntentosRepeticionSubMenuClaves.GetId(),
				evalContadorIntentosRepeticionSubMenuClaves);

		evalContadorIntentosSubMenuClaves = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosSubMenuClaves
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU CLAVES");
		Steps.put(evalContadorIntentosSubMenuClaves.GetId(),
				evalContadorIntentosSubMenuClaves);

		evalContadorIntentosOlvidoClave = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosOlvidoClave
				.setStepDescription("CONDITIONAL => INTENTOS OLVIDO CLAVE BPI");
		Steps.put(evalContadorIntentosOlvidoClave.GetId(),
				evalContadorIntentosOlvidoClave);

		evalContadorIntentosRepetirInformacionTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosRepetirInformacionTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU TARJETA COORDENADAS BPI");
		Steps.put(evalContadorIntentosRepetirInformacionTarjetaCoordenadas
				.GetId(),
				evalContadorIntentosRepetirInformacionTarjetaCoordenadas);

		evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION MENU TARJETA COORDENADAS BPI");
		Steps.put(evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas
				.GetId(),
				evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas);

		evalContadorRepetirInformacionSubMenuPagos = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirInformacionSubMenuPagos
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION SUB MENU PAGOS BPI");
		Steps.put(evalContadorRepetirInformacionSubMenuPagos.GetId(),
				evalContadorRepetirInformacionSubMenuPagos);

		evalContadorRepetirInformacionPagos = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirInformacionPagos
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION PAGOS BPI");
		Steps.put(evalContadorRepetirInformacionPagos.GetId(),
				evalContadorRepetirInformacionPagos);

		evalAsesoramientoAUsuario = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalAsesoramientoAUsuario
				.setStepDescription("CONDITIONAL => INTENTOS MENU ASESORAMIENTO A USUARIO");
		Steps.put(evalAsesoramientoAUsuario.GetId(), evalAsesoramientoAUsuario);

		evalContadorRepetirInformacionTransferencias = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirInformacionTransferencias
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION MENU TRANSFERENCIAS");
		Steps.put(evalContadorRepetirInformacionTransferencias.GetId(),
				evalContadorRepetirInformacionTransferencias);

		evalContadorSubMenuPagos = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorSubMenuPagos
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU PAGOS");
		Steps.put(evalContadorSubMenuPagos.GetId(), evalContadorSubMenuPagos);

		evalContadorRepetirMenuTutorial = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirMenuTutorial
				.setStepDescription("CONDITIONAL => INTENTOS MENU TUTORIAL");
		Steps.put(evalContadorRepetirMenuTutorial.GetId(),
				evalContadorRepetirMenuTutorial);

		evalContadorRepetirInfoTransferencial = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirInfoTransferencial
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION TRANSFERENCIA");
		Steps.put(evalContadorRepetirInfoTransferencial.GetId(),
				evalContadorRepetirInfoTransferencial);

		/*--- Menu --- */

		stepMenuInicialBPI = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuInicialBPI.setStepDescription("MENU =>  MENU INICIAL BPI");
		stepMenuInicialBPI.setContextVariableName(ctxVar
				.getContextVarByName("menuInicialBPIContextVar"));
		Steps.put(stepMenuInicialBPI.GetId(), stepMenuInicialBPI);

		stepMenuPagos = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuPagos.setStepDescription("MENU =>  MENU PAGOS BPI");
		stepMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("menuPagosContextVar"));
		Steps.put(stepMenuPagos.GetId(), stepMenuPagos);

		stepMenuTransferencias = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuTransferencias
				.setStepDescription("MENU =>  MENU TRANSFERENCIAS BPI");
		stepMenuTransferencias.setContextVariableName(ctxVar
				.getContextVarByName("menuTransferenciasContextVar"));
		Steps.put(stepMenuTransferencias.GetId(), stepMenuTransferencias);

		stepMenuClaves = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuClaves.setStepDescription("MENU =>  MENU CLAVES BPI");
		stepMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("menuClavesContextVar"));
		Steps.put(stepMenuClaves.GetId(), stepMenuClaves);

		stepMenuOtrasConsultas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuOtrasConsultas
				.setStepDescription("MENU =>  MENU OTRAS CONSULTAS BPI");
		stepMenuOtrasConsultas.setContextVariableName(ctxVar
				.getContextVarByName("menuOtrasConsultasContextVar"));
		Steps.put(stepMenuOtrasConsultas.GetId(), stepMenuOtrasConsultas);

		// Sub Menu

		stepSubMenuClaves = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepSubMenuClaves.setStepDescription("MENU =>  SUBMENU CLAVES BPI");
		stepSubMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("subMenuClavesContextVar"));
		Steps.put(stepSubMenuClaves.GetId(), stepSubMenuClaves);

		stepMenuOlvidoClave = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuOlvidoClave
				.setStepDescription("MENU =>  MENU OLVIDO CLAVE BPI");
		stepMenuOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("olvidoClaveContextVar"));
		Steps.put(stepMenuOlvidoClave.GetId(), stepMenuOlvidoClave);

		stepMenuAsesoramientoAUsuario = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuAsesoramientoAUsuario
				.setStepDescription("MENU =>  MENU ASESORAMIENTO A USUARIOS BPI");
		stepMenuAsesoramientoAUsuario.setContextVariableName(ctxVar
				.getContextVarByName("asesoramientoAUsiaroContextVar"));
		Steps.put(stepMenuAsesoramientoAUsuario.GetId(),
				stepMenuAsesoramientoAUsuario);

		stepSubMenuTransferencia = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepSubMenuTransferencia
				.setStepDescription("MENU =>  SUBMENU TRANSFERENCIA BPI");
		stepSubMenuTransferencia.setContextVariableName(ctxVar
				.getContextVarByName("subMenuTransferenciaContextVar"));
		Steps.put(stepSubMenuTransferencia.GetId(), stepSubMenuTransferencia);

		stepMenuRepetirTutorial = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuRepetirTutorial
				.setStepDescription("MENU =>  MENU REPETIR INFORMACION TUTORIAL BPI");
		stepMenuRepetirTutorial.setContextVariableName(ctxVar
				.getContextVarByName("repetirInformacionTutorialContextVar"));
		Steps.put(stepMenuRepetirTutorial.GetId(), stepMenuRepetirTutorial);

		stepSubMenuPagos = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepSubMenuPagos.setStepDescription("MENU =>  SUBMENU PAGOS");
		stepSubMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("subMenuPagosContextVar"));
		Steps.put(stepSubMenuPagos.GetId(), stepSubMenuPagos);

		stepMenuTarjetaCoordenadas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuTarjetaCoordenadas
				.setStepDescription("MENU =>  MENU TARJETA DE COORDENADAS");
		stepMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("menuTarjetaCoordenadasContextVar"));
		Steps.put(stepMenuTarjetaCoordenadas.GetId(),
				stepMenuTarjetaCoordenadas);

		stepMenuRepetirInformacionTarjetaCoordenadas = (StepMenu) StepFactory
				.createStep(StepType.Menu, UUID.randomUUID());
		stepMenuRepetirInformacionTarjetaCoordenadas
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepMenuRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("subMenuTarjetaCoordenadasContextVar"));
		Steps.put(stepMenuRepetirInformacionTarjetaCoordenadas.GetId(),
				stepMenuRepetirInformacionTarjetaCoordenadas);

		stepMenuRepetirAdicionalInfoTarjetaCoordenadas = (StepMenu) StepFactory
				.createStep(StepType.Menu, UUID.randomUUID());
		stepMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepMenuRepetirAdicionalInfoTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("adicionalTarjetaCoordenadasContextVar"));
		Steps.put(stepMenuRepetirAdicionalInfoTarjetaCoordenadas.GetId(),
				stepMenuRepetirAdicionalInfoTarjetaCoordenadas);

		stepMenuRepetirAdicionalInfoMovil = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuRepetirAdicionalInfoMovil
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepMenuRepetirAdicionalInfoMovil.setContextVariableName(ctxVar
				.getContextVarByName("adicionalMovilContextVar"));
		Steps.put(stepMenuRepetirAdicionalInfoMovil.GetId(),
				stepMenuRepetirAdicionalInfoMovil);

		/* --- Menu Ingreso --- */

		stepMenuIngresoDatosCuenta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuIngresoDatosCuenta.setStepDescription("MENU => DATOS CUENTA");
		stepMenuIngresoDatosCuenta.setContextVariableName(ctxVar
				.getContextVarByName("datosCuentaContextVar"));
		Steps.put(stepMenuIngresoDatosCuenta.GetId(),
				stepMenuIngresoDatosCuenta);

		stepMenuConfirmacionIngresoRutina = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionIngresoRutina
				.setStepDescription("MENU => INGRESO RUTINA CLEAR BPI");
		stepMenuConfirmacionIngresoRutina.setContextVariableName(ctxVar
				.getContextVarByName("resultadoAudioInicioClearKeyContextVar"));
		Steps.put(stepMenuConfirmacionIngresoRutina.GetId(),
				stepMenuConfirmacionIngresoRutina);

		stepMenuCambioDeTarjeta = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuCambioDeTarjeta.setStepDescription("MENU => CAMBIO DE TARJETA");
		stepMenuCambioDeTarjeta
				.setContextVariableName(ctxVar
						.getContextVarByName("resultadoAudioCambioDeTarjetaContextVar"));
		Steps.put(stepMenuCambioDeTarjeta.GetId(), stepMenuCambioDeTarjeta);

		stepMenuRepetirPIN = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuRepetirPIN.setContextVariableName(ctxVar
				.getContextVarByName("repetirPINContextVar"));
		stepMenuRepetirPIN.setStepDescription("MENU => REPETIR PIN");
		Steps.put(stepMenuRepetirPIN.GetId(), stepMenuRepetirPIN);

		/* --- Play Read --- */

		stepAudioInicioRutinaClearKey = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioInicioRutinaClearKey
				.setStepDescription("PLAYREAD => INICIO RUTINA");
		stepAudioInicioRutinaClearKey.setPlayFile("PREATENDEDORCABAL/050");
		stepAudioInicioRutinaClearKey.setPlayMaxDigits(1);
		stepAudioInicioRutinaClearKey.setContextVariableName(ctxVar
				.getContextVarByName("resultadoAudioInicioClearKeyContextVar"));
		stepAudioInicioRutinaClearKey.setPlayTimeout(5000L);
		Steps.put(stepAudioInicioRutinaClearKey.GetId(),
				stepAudioInicioRutinaClearKey);

		stepAudioRepetirPIN = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirPIN.setPlayMaxDigits(1);
		stepAudioRepetirPIN.setContextVariableName(ctxVar
				.getContextVarByName("repetirPINContextVar"));
		stepAudioRepetirPIN.setStepDescription("PLAYREAD => REINGRESO PIN");
		Steps.put(stepAudioRepetirPIN.GetId(), stepAudioRepetirPIN);

		/* --- Audios Varios --- */

		stepAudioOKClearBPI = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioOKClearBPI.setPlayfile("RUTINAPINCOP/RUTINA_PIN022");
		stepAudioOKClearBPI.setStepDescription("PLAY => PIN ENTREGADO");
		Steps.put(stepAudioOKClearBPI.GetId(), stepAudioOKClearBPI);

		stepAudioVerificarDatos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerificarDatos.setPlayfile("RUTINAPINCOP/RUTINA_PIN012");
		stepAudioVerificarDatos
				.setStepDescription("PLAY => VERIFIQUE DATOS Y VUELVA A LLAMAR");
		Steps.put(stepAudioVerificarDatos.GetId(), stepAudioVerificarDatos);

		/* --- Audios Retorno Jpos --- */

		stepAudioClearBpiOk = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioClearBpiOk.setPlayfile("RUTINAPINCOP/RUTINA_PIN040");
		stepAudioClearBpiOk.setStepDescription("PLAY => CLEAR BPI OK");
		Steps.put(stepAudioClearBpiOk.GetId(), stepAudioClearBpiOk);

		stepAudioErrorClearBpi = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioErrorClearBpi.setPlayfile("RUTINAPINCOP/RUTINA_PIN040");
		stepAudioErrorClearBpi.setStepDescription("PLAY => ERROR CLEAR BPI");
		Steps.put(stepAudioErrorClearBpi.GetId(), stepAudioErrorClearBpi);

		stepAudioErrorClearBpiFilial = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioErrorClearBpiFilial.setPlayfile("PREATENDEDORCABAL/012");
		stepAudioErrorClearBpiFilial
				.setStepDescription("PLAY => ERROR CLEAR Bpi , CONSULTAR EN LA FILIAL");
		Steps.put(stepAudioErrorClearBpiFilial.GetId(),
				stepAudioErrorClearBpiFilial);

		stepAudioTarjetaNoOperaConBpi = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaNoOperaConBpi.setPlayfile("PREATENDEDORCABAL/010");
		stepAudioTarjetaNoOperaConBpi
				.setStepDescription("PLAY => ERROR TARJETA NO OPERA CON Bpi");
		Steps.put(stepAudioTarjetaNoOperaConBpi.GetId(),
				stepAudioTarjetaNoOperaConBpi);

		stepAudioDniIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDniIncorrecto.setPlayfile("RUTINAPINCOP/RUTINA_PIN040");
		stepAudioDniIncorrecto
				.setStepDescription("PLAY => DNI INCORRECTO. COD : 02");
		Steps.put(stepAudioDniIncorrecto.GetId(), stepAudioDniIncorrecto);

		stepAudioFechaIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaIncorrecta.setPlayfile("RUTINAPINCOP/RUTINA_PIN039");
		stepAudioFechaIncorrecta
				.setStepDescription("PLAY => FECHA INCORRECTA. COD : 03");
		Steps.put(stepAudioFechaIncorrecta.GetId(), stepAudioFechaIncorrecta);

		stepAudioIngreseUnDigitoValido = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioIngreseUnDigitoValido
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN037");
		stepAudioIngreseUnDigitoValido
				.setStepDescription("PLAY => DIGITO INVALIDO");
		Steps.put(stepAudioIngreseUnDigitoValido.GetId(),
				stepAudioIngreseUnDigitoValido);

		stepAudioCantidadMaxDeReintentos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentos
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentos
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS");
		Steps.put(stepAudioCantidadMaxDeReintentos.GetId(),
				stepAudioCantidadMaxDeReintentos);

		stepAudioCantidadMaxDeReintentosFecha = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentosFecha
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentosFecha
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS FECHA");
		Steps.put(stepAudioCantidadMaxDeReintentosFecha.GetId(),
				stepAudioCantidadMaxDeReintentosFecha);

		stepAudioCantidadMaxDeReintentosTarjeta = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentosTarjeta
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentosTarjeta
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS TARJETA");
		Steps.put(stepAudioCantidadMaxDeReintentosTarjeta.GetId(),
				stepAudioCantidadMaxDeReintentosTarjeta);

		stepAudioCantidadMaxDeReintentosCuenta = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioCantidadMaxDeReintentosCuenta
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN024");
		stepAudioCantidadMaxDeReintentosCuenta
				.setStepDescription("PLAY => SUPERO LA CANTIDAD DE REINTENTOS CUENTA");
		Steps.put(stepAudioCantidadMaxDeReintentosCuenta.GetId(),
				stepAudioCantidadMaxDeReintentosCuenta);

		stepAudioCuentaPropia = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCuentaPropia.setPlayfile("PREATENDEDORCABAL/009");
		stepAudioCuentaPropia.setStepDescription("PLAY => CUENTA PROPIA");
		Steps.put(stepAudioCuentaPropia.GetId(), stepAudioCuentaPropia);

		stepAudioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDerivoAsesor.setPlayfile("RUTINAPINCOP/RUTINA_PIN034");
		stepAudioDerivoAsesor
				.setStepDescription("PLAY => DERIVO ASESOR. COD : 85");
		Steps.put(stepAudioDerivoAsesor.GetId(), stepAudioDerivoAsesor);

		stepAudioNumeroDeTarjetaIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNumeroDeTarjetaIncorrecto
				.setPlayfile("RUTINAPINCOP/RUTINA_PIN034");
		stepAudioNumeroDeTarjetaIncorrecto
				.setStepDescription("PLAY => TARJETA INCORRECTA. COD : 96");
		Steps.put(stepAudioNumeroDeTarjetaIncorrecto.GetId(),
				stepAudioNumeroDeTarjetaIncorrecto);

		stepAudioTarjetaNoVigente = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTarjetaNoVigente.setPlayfile("RUTINAPINCOP/RUTINA_PIN025");
		stepAudioTarjetaNoVigente
				.setStepDescription("PLAY => TARJETA VENCIDA. COD : 99");
		Steps.put(stepAudioTarjetaNoVigente.GetId(), stepAudioTarjetaNoVigente);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPINCOP/RUTINA_PIN026");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE. COD : 98");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		/* --- Contadores --- */

		contadorIntentosInit = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosInit.setStepDescription("COUNTER => INTENTOS INITDB");
		contadorIntentosInit.setContextVariableName(ctxVar
				.getContextVarByName("intentosCuentaPropiaContextVar"));
		Steps.put(contadorIntentosInit.GetId(), contadorIntentosInit);

		contadorIntentosCuenta = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosCuenta
				.setStepDescription("COUNTER => INTENTOS CUENTA PROPIA");
		contadorIntentosCuenta.setContextVariableName(ctxVar
				.getContextVarByName("intentosCuentaPropiaContextVar"));
		Steps.put(contadorIntentosCuenta.GetId(), contadorIntentosCuenta);

		contadorIntentosIngresoRutina = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosIngresoRutina
				.setStepDescription("COUNTER => INGRESO RUTINA");
		contadorIntentosIngresoRutina.setContextVariableName(ctxVar
				.getContextVarByName("intentosIngresoContextVar"));
		Steps.put(contadorIntentosIngresoRutina.GetId(),
				contadorIntentosIngresoRutina);

		/* --- Evaluadores --- */

		evalContadorIngresoRutina = (StepConditional) StepFactory.createStep(
				StepType.Conditional, UUID.randomUUID());
		evalContadorIngresoRutina
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO RUTINA");
		Steps.put(evalContadorIngresoRutina.GetId(), evalContadorIngresoRutina);

		evalContadorCuenta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorCuenta
				.setStepDescription("CONDITIONAL => INTENTOS CUENTA PROPIA");
		Steps.put(evalContadorCuenta.GetId(), evalContadorCuenta);

		evalContadorInit = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorInit.setStepDescription("CONDITIONAL => INTENTOS INITDB");
		Steps.put(evalContadorInit.GetId(), evalContadorInit);

		obtieneTarjeta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneTarjeta.setContextVariableName(ctxVar
				.getContextVarByName("tarjetaContexVar"));
		obtieneTarjeta.setVariableName("plastico");
		obtieneTarjeta
				.setStepDescription("GETASTERISKVARIABLE => OBTIENE TARJETA");
		Steps.put(obtieneTarjeta.GetId(), obtieneTarjeta);

		/* --- EJECUTO INITDB ? --- */

		stepCheckCuentaEnDialPlan = (StepCheckCuentaEnDialPlan) StepFactory
				.createStep(StepFactory.StepType.CuentaEnDialplan,
						UUID.randomUUID());
		stepCheckCuentaEnDialPlan
				.setStepDescription("CHEACKCUENTASENDIALPLAN => TIENE CUENTAS EN EL DIALPLAN");
		Steps.put(stepCheckCuentaEnDialPlan.GetId(), stepCheckCuentaEnDialPlan);

		initDB = (StepInitDniDB) StepFactory.createStep(StepType.InitDniDB,
				UUID.randomUUID());
		initDB.setContextVarDni(ctxVar.getContextVarByName("dniContextVar"));
		initDB.setStepDescription("INITDNIDB => OBTIENE DATOS A PARTIR DEL DNI, POR DERIVO ");
		Steps.put(initDB.GetId(), initDB);

		/* --- CLEAR Bpi --- */

		stepClearKey = (StepClearKeyBPI) StepFactory.createStep(
				StepType.ClearKeyBPI, UUID.randomUUID());
		stepClearKey.setClaveContextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		stepClearKey.setDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		stepClearKey.setIdLlamadaContexVar(ctxVar
				.getContextVarByName("idLlamadaContexVar"));
		Steps.put(stepClearKey.GetId(), stepClearKey);

		stepServiceBanca = (StepCheckServiceBanca) StepFactory.createStep(
				StepType.ServiceBanca, UUID.randomUUID());
		stepServiceBanca.setDniContextVar(ctxVar
				.getContextVarByName("dniContextVar"));
		Steps.put(stepServiceBanca.GetId(), stepServiceBanca);

		esTarjetaPropia = (StepIsOwnCard) StepFactory.createStep(
				StepType.IsOwnCard, UUID.randomUUID());
		esTarjetaPropia.setTarjetaContextVariableName(ctxVar
				.getContextVarByName("tarjetaContexVar"));
		esTarjetaPropia.setIdCrecerContextVariableName(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		esTarjetaPropia
				.setStepDescription("ISOWNCARD => VERIFICA QUE LA TARJETA INGRESADA SEA PROPIA");
		Steps.put(esTarjetaPropia.GetId(), esTarjetaPropia);

		checkCuenta = (StepCheckCuenta) StepFactory.createStep(
				StepType.ChekCuenta, UUID.randomUUID());
		checkCuenta.setCuentaContextVar(ctxVar
				.getContextVarByName("cuentaContextVar"));
		checkCuenta
				.setStepDescription("CHEKCUENTA => VERIFICA QUE LA CUENTA SEA PROPIA");
		Steps.put(checkCuenta.GetId(), checkCuenta);

		/* --- GRUPOS --- */

		pideCuentaGrp = (PideCuenta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideCuenta);
		pideCuentaGrp.setAudioCuenta("RUTINAPIL/002");
		pideCuentaGrp.setAudioCuentaInvalida("RUTINAPIL/005");
		pideCuentaGrp.setAudioSuCuentaEs("RUTINAPIL/003");
		pideCuentaGrp.setAudioValidateCuenta("RUTINAPIL/004");
		pideCuentaGrp.setConfirmaCuentaContextVar(ctxVar
				.getContextVarByName("confirmaCuentaContextVar"));
		pideCuentaGrp.setCuentaContextVar(ctxVar
				.getContextVarByName("cuentaContextVar"));
		pideCuentaGrp.setIntentosCuentaContextVar(ctxVar
				.getContextVarByName("intentosCuentaContextVar"));

		pideFechaGrp = (PideFechaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFechaCredicoop);
		pideFechaGrp.setAudioFecha("RUTINAPINCOP/RUTINA_PIN010");
		pideFechaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideFechaGrp.setAudioValidateFecha("RUTINAPINCOP/RUTINA_PIN009");
		pideFechaGrp.setAudioSuFechaEs("RUTINAPINCOP/RUTINA_PIN015");
		pideFechaGrp.setAudioLaFechaNoCoincide("PREATENDEDORCABAL/014");
		pideFechaGrp.setAudioAnio("RUTINAPINCOP/RUTINA_PIN008");
		pideFechaGrp.setAudioMes("RUTINAPINCOP/RUTINA_PIN007");
		pideFechaGrp.setAudioDia("RUTINAPINCOP/RUTINA_PIN006");
		pideFechaGrp.setAudioFechaInvalida("RUTINAPINCOP/RUTINA_PIN013");
		pideFechaGrp.setfechaContextVar(ctxVar
				.getContextVarByName("fechaContextVar"));
		pideFechaGrp.setContextVarDia(ctxVar
				.getContextVarByName("diaContextVar"));
		pideFechaGrp.setContextVarMes(ctxVar
				.getContextVarByName("mesContextVar"));
		pideFechaGrp.setContextVarAnio(ctxVar
				.getContextVarByName("anioContextVar"));
		pideFechaGrp.setConfirmaFechaContextVar(ctxVar
				.getContextVarByName("confirmaFechaContextVar"));
		pideFechaGrp.setIntentosFechaContextVar(ctxVar
				.getContextVarByName("intentosFechaContextVar"));
		pideFechaGrp.setFdnContextVar(ctxVar
				.getContextVarByName("fdnContextVar"));

		pideTarjetaGrp = (PideTarjetaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjetaCredicoop);
		pideTarjetaGrp.setAudioTarjeta("RUTINAPINCOP/RUTINA_PIN003");
		pideTarjetaGrp.setAudioTarjetaInvalido("RUTINAPINCOP/RUTINA_PIN023");
		pideTarjetaGrp.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideTarjetaGrp.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideTarjetaGrp.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		pideTarjetaGrp.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContexVar"));

		pideDniGrp = (PideDni) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideDni);
		pideDniGrp.setAudioDni("PREATENDEDORCABAL/033");
		pideDniGrp.setAudioValidateDni("PREATENDEDORCABAL/035");
		pideDniGrp.setAudioDniInvalido("PREATENDEDORCABAL/036");
		pideDniGrp.setAudioSuDniEs("PREATENDEDORCABAL/034");
		pideDniGrp
				.setDniContextVar(ctxVar.getContextVarByName("dniContextVar"));
		pideDniGrp.setConfirmaDniContextVar(ctxVar
				.getContextVarByName("confirmaDniContextVar"));
		pideDniGrp.setIntentosDniContextVar(ctxVar
				.getContextVarByName("intentosDniContextVar"));

		/* --- Obtiene datos --- */

		obtieneDni = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneDni.setContextVariableName(ctxVar
				.getContextVarByName("dniContextVar"));
		obtieneDni.setVariableName("dni");
		obtieneDni.setStepDescription("GETASTERISKVARIABLE => OBTIENE DNI");
		Steps.put(obtieneDni.GetId(), obtieneDni);

		obtieneIdCrecer = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneIdCrecer.setVariableName("idcrecer");
		obtieneIdCrecer.setContextVariableName(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		obtieneIdCrecer
				.setStepDescription("GETASTERISKVARIABLE => OBTIENE ID CRECER");
		Steps.put(obtieneIdCrecer.GetId(), obtieneIdCrecer);

		evalRetornoClearBpi = (StepSwitch) StepFactory.createStep(
				StepType.Switch, UUID.randomUUID());
		evalRetornoClearBpi.setContextVariableName(ctxVar
				.getContextVarByName("retornoClearBpiContextVar"));
		evalRetornoClearBpi
				.setStepDescription("SWITCH => CODIGO RETORNO CLEAR Bpi");
		Steps.put(evalRetornoClearBpi.GetId(), evalRetornoClearBpi);

		stepSetReingreso = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetReingreso.setVariableName("initDB");
		stepSetReingreso.setContextVariableName(ctxVar
				.getContextVarByName("initDb_auxContextVar"));
		stepSetReingreso
				.setStepDescription("SETASTERISKVARIABLE => SETEA VARIABLE PARA REINGRESO A LA RUTINA");
		Steps.put(stepSetReingreso.GetId(), stepSetReingreso);

		stepSetDni = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepSetDni.setVariableName("dniAux");
		stepSetDni.setContextVariableName(ctxVar
				.getContextVarByName("dniContextVar"));
		stepSetDni
				.setStepDescription("SETASTERISKVARIABLE => SETEA VARIABLE PARA REINGRESO A LA RUTINA");
		Steps.put(stepSetDni.GetId(), stepSetDni);

		/* ---------------- Conditional -------------- */

		evalInit = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalInit.setStepDescription("CONDITIONAL => EJECUTO INIT DB MENU");
		Steps.put(evalInit.GetId(), evalInit);

	}

	public PideDni getPideDni() {
		return pideDniGrp;
	}

	public PideTarjetaCredicoop getPideTarjeta() {
		return pideTarjetaGrp;
	}

	public PideFechaCredicoop getPideFecha() {
		return pideFechaGrp;
	}

	public PideCuenta getPideCuenta() {
		return pideCuentaGrp;
	}

	public PideKeyBPI getPideKeyBPI() {
		return pideKeyBpi;
	}

}
