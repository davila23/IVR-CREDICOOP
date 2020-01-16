package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.StepAuthInitialInfo;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepCounter;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayNumber;
import step.StepTimeConditionDB;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PreAtendedorBIE implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	protected StepMenu stepMenuInicialBIE;
	protected StepPlayRead stepAudioMenuInicial;
	protected StepPlayRead stepAudioMenuClaves;
	protected StepPlayRead stepAudioMenuTransferencias;
	protected StepPlayRead stepAudioMenuPagos;
	protected StepPlayRead stepAudioMenuOtrasConsultas;
	protected StepCounter contadorIntentosMenuInicialBIE;
	protected StepCounter contadorIntentosMenuClaves;
	protected StepCounter contadorIntentosMenuTransferencias;
	protected StepCounter contadorIntentosMenuPagos;
	protected StepCounter contadorIntentosMenuOtrasConsultas;
	protected StepConditional evalContadorMenuInicialBIE;
	protected StepConditional evalContadorMenuClaves;
	protected StepConditional evalContadorMenuTransferencias;
	protected StepConditional evalContadorMenuPagos;
	protected StepConditional evalContadorMenuOtrasConsultas;
	protected StepMenu stepMenuPagos;
	protected StepMenu stepMenuTransferencias;
	protected StepMenu stepMenuClaves;
	protected StepMenu stepMenuOtrasConsultas;
	protected StepPlay stepAudioRecuerdaClave;
	protected StepPlay stepAudioParaGenerarClaveEmpresa;
	protected StepMenu stepSubMenuClaves;
	protected StepPlayRead stepAudioSubMenuClaves;
	protected StepTimeConditionDB obtieneHorario;
	protected StepPlay stepAudioPrevioDerivoAsesor;
	protected StepPlay stepAudioPrevioDerivoAsesorTranferencias;
	protected StepPlay stepAudioPrevioDerivoAsesorCertificacion;
	protected StepPlay stepAudioDisuadeDerivoAsesor;
	protected StepCounter contadorIntentosRepeticionSubMenu;
	protected StepConditional evalContadorIntentosRepeticionGenerarClaveEmpresa;
	protected StepCounter contadorIntentosRepeticionGenerarClaveEmpresa;
	protected StepPlay stepAudioOlvidoLaClave;
	protected StepPlayRead stepAudioMenuOlvidoClave;
	protected StepMenu stepMenuOlvidoClave;
	protected StepCounter contadorIntentosOlvidoClave;
	protected StepConditional evalContadorIntentosOlvidoClave;
	protected PideDni pideDniGrp;
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
	protected StepMenu stepSubMenuTarjetaCoordenadas;
	protected StepPlayRead stepAudioSubMenuTarjetaCoordenadas;
	protected StepPlay stepAudioPrevioSubMenuTarjetaCoordenadas;
	protected StepPlay stepAudioInfoAdicionalMovil;
	protected StepPlay stepAudioInfoAdicionalTarjetaCoordenadas;
	protected StepConditional evalContadorIntentosSubMenuTarjetaCoordenadas;
	protected StepCounter contadorIntentosSubMenuTarjetaCoordenadas;
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
	protected StepCounter contadorIntentosMenuCertificarPC;
	protected StepPlay stepAudioCertificarPC;
	protected StepPlayRead stepAudioMenuCertificarPC;
	protected StepMenu stepMenuCertificarPC;
	protected StepConditional evalContadorCertificarPC;
	protected StepPlay stepAudioDesbloqueoSegundoFactor;
	protected StepPlay stepAudioFinal;
	protected StepPlay stepIngresoNuloEIncorrecto;
	protected StepPlayRead stepAudioRepetirInformacionTarjetaCoordenadas;
	protected StepMenu stepMenuRepetirInformacionTarjetaCoordenadas;
	protected StepCounter contadorIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepCounter contadorIntentosMenuRepetirInformacionTarjetaCoordenadas;
	protected StepConditional evalContadorIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepConditional evalContadorMenuIntentosRepetirInformacionTarjetaCoordenadas;
	protected StepCounter contadorIntentosRepetirOlvidoClave;
	protected StepConditional evalContadorIntentosRepetirOlvidoClave;
	protected StepPlay stepIngresoNuloEIncorrecto1;
	protected StepPlay stepIngresoNuloEIncorrecto2;
	protected StepPlay stepIngresoNuloEIncorrecto3;
	protected StepPlay stepIngresoNuloEIncorrecto4;
	protected StepPlay stepIngresoNuloEIncorrecto5;
	protected StepPlay stepIngresoNuloEIncorrecto6;
	protected StepPlay stepIngresoNuloEIncorrecto7;
	protected StepPlay stepIngresoNuloEIncorrecto8;
	protected StepPlay stepIngresoNuloEIncorrecto9;
	protected StepPlay stepIngresoNuloEIncorrecto10;
	protected StepPlay stepIngresoNuloEIncorrecto11;
	protected StepPlay stepIngresoNuloEIncorrecto12;
	protected StepPlay stepIngresoNuloEIncorrecto13;
	protected StepPlay stepIngresoNuloEIncorrecto14;
	protected StepCounter contadorIntentosMenuRepetirTutorial;
	protected StepConditional evalContadorMenuRepetirTutorial;
	protected StepExecute stepDerivoLlamada;
	protected StepExecute stepDerivoLlamadaCertificaciones;
	protected StepExecute stepDerivoLlamadaTranferencias;
	protected StepTimeConditionDB obtieneHorarioTranferencias;
	protected StepTimeConditionDB obtieneHorarioCertificaciones;

	private void setSequence() {

		/* Menu Inicial BIE */

		stepAudioMenuInicial.setNextstep(stepMenuInicialBIE.GetId());

		stepMenuInicialBIE.addSteps("1", stepAudioMenuClaves.GetId());
		stepMenuInicialBIE.addSteps("2", stepAudioCertificarPC.GetId());
		stepMenuInicialBIE.addSteps("3", stepAudioMenuTransferencias.GetId());
		stepMenuInicialBIE.addSteps("4", obtieneHorario.GetId());
		stepMenuInicialBIE.addSteps("5",
				stepAudioPrevioMenuOtrasConsultas.GetId());
		stepMenuInicialBIE.setInvalidOption(contadorIntentosMenuInicialBIE
				.GetId());

		contadorIntentosMenuInicialBIE.setNextstep(evalContadorMenuInicialBIE
				.GetId());
		evalContadorMenuInicialBIE.addCondition(new condition(1,
				"#{"
						+ ctxVar.getContextVarByName(
								"intentosMenuInicialBIEContextVar")
								.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto.setNextstep(stepAudioMenuInicial.GetId());

		/* -- 1 -- Menu Gestion de Claves */

		stepAudioMenuClaves.setNextstep(stepMenuClaves.GetId());

		stepMenuClaves.addSteps("1", stepAudioParaGenerarClaveEmpresa.GetId());
		stepMenuClaves.addSteps("2", stepAudioOlvidoLaClave.GetId());
		stepMenuClaves.addSteps("3", stepAudioMenuTarjetaCoordenadas.GetId());
		stepMenuClaves.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuClaves.setInvalidOption(contadorIntentosMenuClaves.GetId());

		contadorIntentosMenuClaves.setNextstep(evalContadorMenuClaves.GetId());
		evalContadorMenuClaves.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuClavesContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto1.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto1.setNextstep(stepAudioMenuClaves.GetId());

		// OPCION -- > 1 - 1 .

		stepAudioParaGenerarClaveEmpresa.setNextstep(stepAudioSubMenuClaves
				.GetId());

		stepAudioSubMenuClaves.setNextstep(stepSubMenuClaves.GetId());
		stepSubMenuClaves.addSteps("1",
				contadorIntentosRepeticionGenerarClaveEmpresa.GetId());
		stepSubMenuClaves.addSteps("8", stepAudioMenuClaves.GetId());
		stepSubMenuClaves.setInvalidOption(contadorIntentosSubMenuClaves
				.GetId());

		contadorIntentosSubMenuClaves
				.setNextstep(evalContadorIntentosSubMenuClaves.GetId());
		evalContadorIntentosSubMenuClaves.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosSubMenuClavesContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto2.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto2.setNextstep(stepAudioSubMenuClaves.GetId());

		contadorIntentosRepeticionGenerarClaveEmpresa
				.setNextstep(evalContadorIntentosRepeticionGenerarClaveEmpresa
						.GetId());
		evalContadorIntentosRepeticionGenerarClaveEmpresa
				.addCondition(new condition(1, "#{"
						+ ctxVar.getContextVarByName(
								"intentosRepeticionClaveEmpresaContextVar")
								.getVarName() + "} < " + intentos,
						stepAudioParaGenerarClaveEmpresa.GetId(),
						stepIfFalseUUID));

		// OPCION -- > 1 - 2 .

		stepAudioOlvidoLaClave.setNextstep(stepAudioMenuOlvidoClave.GetId());

		stepAudioMenuOlvidoClave.setNextstep(stepMenuOlvidoClave.GetId());
		stepMenuOlvidoClave.addSteps("1", obtieneHorario.GetId());
		stepMenuOlvidoClave.addSteps("8", stepAudioMenuClaves.GetId());
		stepMenuOlvidoClave.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuOlvidoClave.setInvalidOption(contadorIntentosOlvidoClave
				.GetId());

		contadorIntentosRepetirOlvidoClave
				.setNextstep(evalContadorIntentosRepetirOlvidoClave.GetId());
		evalContadorIntentosRepetirOlvidoClave.addCondition(new condition(1,
				"#{"
						+ ctxVar.getContextVarByName(
								"intentosRepetirOlvidoClaveContextVar")
								.getVarName() + "} < " + intentos,
				stepAudioOlvidoLaClave.GetId(), stepIfFalseUUID));

		contadorIntentosOlvidoClave.setNextstep(evalContadorIntentosOlvidoClave
				.GetId());
		evalContadorIntentosOlvidoClave.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosOlvidoClaveContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto3.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto3.setNextstep(stepAudioMenuOlvidoClave
				.GetId());

		// OPCION -- > 1 - 3 .
		// OPCION -- > 1 - 3 .

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

		// 1 - 3 - 1

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

		// 1 - 3 - 2

		pideDniGrp.setStepIfTrue(stepAudioDesbloqueoSegundoFactor.GetId());
		pideDniGrp.setStepIfFalse(stepIfFalseUUID);

		stepAudioDesbloqueoSegundoFactor.setNextstep(stepAudioMenuInicial
				.GetId());

		// stepAudioMenuTarjetaCoordenadas.setNextstep(stepMenuTarjetaCoordenadas
		// .GetId());
		// stepMenuTarjetaCoordenadas.addSteps("1",
		// stepAudioPrevioSubMenuTarjetaCoordenadas.GetId());
		// stepMenuTarjetaCoordenadas.addSteps("2",
		// pideDniGrp.getInitialStep());
		// stepMenuTarjetaCoordenadas.addSteps("8",
		// stepAudioMenuClaves.GetId());
		// stepMenuTarjetaCoordenadas.addSteps("9",
		// stepAudioMenuInicial.GetId());
		// stepMenuTarjetaCoordenadas.setInvalidOption(contadorIntentosMenuTarjetaCoordenadas.GetId());
		//
		// contadorIntentosMenuTarjetaCoordenadas
		// .setNextstep(evalContadorIntentosMenuTarjetaCoordenadas.GetId());
		// evalContadorIntentosMenuTarjetaCoordenadas.addCondition(new
		// condition(
		// 1, "#{"
		// + ctxVar.getContextVarByName(
		// "intentosTarjetaCoordenadasContextVar")
		// .getVarName() + "} < " + intentos,
		// stepAudioMenuTarjetaCoordenadas.GetId(), stepIfFalseUUID));
		//
		// stepAudioPrevioSubMenuTarjetaCoordenadas
		// .setNextstep(stepAudioSubMenuTarjetaCoordenadas.GetId());
		//
		// stepAudioSubMenuTarjetaCoordenadas
		// .setNextstep(stepSubMenuTarjetaCoordenadas.GetId());
		// stepSubMenuTarjetaCoordenadas.addSteps("1",
		// stepAudioInfoAdicionalMovil.GetId());
		// stepSubMenuTarjetaCoordenadas.addSteps("2",
		// stepAudioInfoAdicionalTarjetaCoordenadas.GetId());
		// stepSubMenuTarjetaCoordenadas
		// .addSteps("8", stepAudioMenuClaves.GetId());
		// stepSubMenuTarjetaCoordenadas.addSteps("9",
		// stepAudioMenuInicial.GetId());
		// stepSubMenuTarjetaCoordenadas.setInvalidOption(contadorIntentosSubMenuTarjetaCoordenadas.GetId());
		//
		// contadorIntentosSubMenuTarjetaCoordenadas
		// .setNextstep(evalContadorIntentosSubMenuTarjetaCoordenadas
		// .GetId());
		// evalContadorIntentosSubMenuTarjetaCoordenadas
		// .addCondition(new condition(1, "#{"
		// + ctxVar.getContextVarByName(
		// "intentosSubTarjetaCoordenadasContextVar")
		// .getVarName() + "} < " + intentos,
		// stepAudioSubMenuTarjetaCoordenadas.GetId(),
		// stepIfFalseUUID));

		// pideDniGrp.setStepIfTrue(stepAudioDesbloqueoSegundoFactor.GetId());
		// pideDniGrp.setStepIfFalse(stepAudioFinal.GetId());

		stepAudioInfoAdicionalMovil
				.setNextstep(stepAudioRepetirInfoAdicionalMovil.GetId());

		stepAudioRepetirInfoAdicionalMovil
				.setNextstep(stepMenuRepetirAdicionalInfoMovil.GetId());
		stepMenuRepetirAdicionalInfoMovil.addSteps("1",
				stepAudioInfoAdicionalMovil.GetId());
		stepMenuRepetirAdicionalInfoMovil.addSteps("2",
				stepAudioInfoAdicionalTarjetaCoordenadas.GetId());
		stepMenuRepetirAdicionalInfoMovil
				.setInvalidOption(stepAudioMenuTarjetaCoordenadas.GetId());

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
				.setInvalidOption(stepAudioMenuTarjetaCoordenadas.GetId());

		stepAudioDesbloqueoSegundoFactor.setNextstep(stepAudioMenuInicial
				.GetId());
		stepAudioSegundoFactor.setNextstep(stepAudioMenuInicial.GetId());

		// OPCION -- > 1 - 4 .

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
				+ "} < " + intentos,
				stepAudioMenuAsesoramientoAUsuario.GetId(), stepIfFalseUUID));

		/* -- 2 -- Certificar PC */

		stepAudioCertificarPC.setNextstep(stepAudioMenuCertificarPC.GetId());
		stepAudioMenuCertificarPC.setNextstep(stepMenuCertificarPC.GetId());
		stepMenuCertificarPC.addSteps("1",
				obtieneHorarioCertificaciones.GetId());
		stepMenuCertificarPC.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuCertificarPC.setInvalidOption(contadorIntentosMenuCertificarPC
				.GetId());

		contadorIntentosMenuCertificarPC.setNextstep(evalContadorCertificarPC
				.GetId());
		evalContadorCertificarPC.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosCertificarPCContextVar")
						.getVarName() + "} < " + intentos,
				stepIngresoNuloEIncorrecto6.GetId(), stepIfFalseUUID));

		stepIngresoNuloEIncorrecto6.setNextstep(stepAudioMenuCertificarPC
				.GetId());

		/* -- 3 -- Menu Transferencias */

		stepAudioMenuTransferencias.setNextstep(stepMenuTransferencias.GetId());

		stepMenuTransferencias.addSteps("1", stepAudioTutorialWeb.GetId());
		stepMenuTransferencias.addSteps("2",
				obtieneHorarioTranferencias.GetId());
		stepMenuTransferencias.addSteps("9", stepAudioMenuInicial.GetId());
		stepMenuTransferencias
				.setInvalidOption(contadorIntentosMenuTransferencias.GetId());

		contadorIntentosMenuTransferencias
				.setNextstep(evalContadorMenuTransferencias.GetId());
		evalContadorMenuTransferencias.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosMenuTransferenciasContextVar").getVarName()
				+ "} < " + intentos, stepIngresoNuloEIncorrecto7.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto7.setNextstep(stepAudioMenuTransferencias
				.GetId());

		// 2 - 1

		stepAudioTutorialWeb.setNextstep(stepAudioRepetirTutorial.GetId());
		stepAudioRepetirTutorial.setNextstep(stepMenuRepetirTutorial.GetId());
		stepMenuRepetirTutorial.addSteps("1",
				contadorIntentosRepetirTutorial.GetId());
		stepMenuRepetirTutorial.addSteps("8",
				stepAudioMenuTransferencias.GetId());
		stepMenuRepetirTutorial
				.setInvalidOption(contadorIntentosMenuRepetirTutorial.GetId());

		contadorIntentosRepetirTutorial
				.setNextstep(evalContadorRepetirMenuTutorial.GetId());
		evalContadorRepetirMenuTutorial.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosMenuTutorialContextVar")
						.getVarName() + "} < " + intentos, stepAudioTutorialWeb
				.GetId(), stepIfFalseUUID));

		contadorIntentosMenuRepetirTutorial
				.setNextstep(evalContadorMenuRepetirTutorial.GetId());
		evalContadorMenuRepetirTutorial.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName(
						"intentosMenuRepetirTutorialContextVar").getVarName()
				+ "} < " + intentos, stepIngresoNuloEIncorrecto8.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto8.setNextstep(stepAudioRepetirTutorial
				.GetId());

		// 2 - 2

		stepAudioTutorialWeb2
				.setNextstep(stepAudioSubMenuTransferencia.GetId());
		stepAudioSubMenuTransferencia.setNextstep(stepSubMenuTransferencia
				.GetId());
		stepSubMenuTransferencia.addSteps("1",
				contadorIntentosRepetirInformacionTransferencias.GetId());
		stepSubMenuTransferencia.addSteps("2",
				obtieneHorarioTranferencias.GetId());
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
						stepAudioTutorialWeb2.GetId(), stepIfFalseUUID));

		/* -- 4 -- Menu Pagos */

		// stepAudioTutorialPagos.setNextstep(stepAudioMenuPagos.GetId());
		// stepAudioMenuPagos.setNextstep(stepMenuPagos.GetId());
		//
		// stepMenuPagos.addSteps("1",
		// contadorIntentosRepetirInformacionPagos.GetId());
		// stepMenuPagos.addSteps("2", stepAudioPrevioSubMenuPagos.GetId());
		// stepMenuPagos.addSteps("9", stepAudioMenuInicial.GetId());
		// stepMenuPagos.setInvalidOption(contadorIntentosMenuPagos.GetId());
		//
		// contadorIntentosMenuPagos.setNextstep(evalContadorMenuPagos.GetId());
		// evalContadorMenuPagos.addCondition(new condition(1, "#{"
		// + ctxVar.getContextVarByName("intentosMenuPagosContextVar")
		// .getVarName() + "} < " + intentos, stepAudioMenuPagos
		// .GetId(), stepIfFalseUUID));
		//
		// contadorIntentosRepetirInformacionPagos
		// .setNextstep(evalContadorRepetirInformacionPagos.GetId());
		// evalContadorRepetirInformacionPagos.addCondition(new condition(1,
		// "#{"
		// + ctxVar.getContextVarByName(
		// "intentosRepetirInformacionMenuPagosContextVar")
		// .getVarName() + "} < " + intentos,
		// stepAudioTutorialPagos.GetId(), stepIfFalseUUID));
		//
		// stepAudioPrevioSubMenuPagos.setNextstep(stepAudioSubMenuPagos.GetId());
		// stepAudioSubMenuPagos.setNextstep(stepSubMenuPagos.GetId());
		//
		// stepSubMenuPagos.addSteps("1",
		// contadorIntentosRepetirInformacionSubMenuPagos.GetId());
		// stepSubMenuPagos.addSteps("2", obtieneHorario.GetId());
		// stepSubMenuPagos.addSteps("8", stepAudioMenuPagos.GetId());
		// stepSubMenuPagos.addSteps("9", stepAudioMenuInicial.GetId());
		// stepSubMenuPagos.setInvalidOption(contadorIntentosSubMenuPagos.GetId());
		//
		// contadorIntentosRepetirInformacionSubMenuPagos
		// .setNextstep(evalContadorRepetirInformacionSubMenuPagos.GetId());
		// evalContadorRepetirInformacionSubMenuPagos
		// .addCondition(new condition(
		// 1,
		// "#{"
		// + ctxVar.getContextVarByName(
		// "intentosRepetirInformacionSubMenuPagosContextVar")
		// .getVarName() + "} < " + intentos,
		// stepAudioPrevioSubMenuPagos.GetId(), stepIfFalseUUID));
		//
		// contadorIntentosSubMenuPagos
		// .setNextstep(evalContadorSubMenuPagos.GetId());
		// evalContadorSubMenuPagos
		// .addCondition(new condition(
		// 1,
		// "#{"
		// + ctxVar.getContextVarByName(
		// "intentosSubMenuPagosContextVar")
		// .getVarName() + "} < " + intentos,
		// stepAudioSubMenuPagos.GetId(), stepIfFalseUUID));

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
				+ "} < " + intentos, stepIngresoNuloEIncorrecto9.GetId(),
				stepIfFalseUUID));

		stepIngresoNuloEIncorrecto9.setNextstep(stepAudioMenuOtrasConsultas
				.GetId());

		stepAudioPrevioDerivoAsesorCertificacion
				.setNextstep(stepDerivoLlamadaCertificaciones.GetId());
		stepAudioPrevioDerivoAsesorTranferencias
				.setNextstep(stepDerivoLlamadaTranferencias.GetId());
		stepAudioPrevioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());

		/* DERIVO */

		obtieneHorarioCertificaciones
				.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorarioCertificaciones
				.setNextStepIsFalse(stepAudioPrevioDerivoAsesorCertificacion
						.GetId());

		obtieneHorarioTranferencias
				.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorarioTranferencias
				.setNextStepIsFalse(stepAudioPrevioDerivoAsesorTranferencias
						.GetId());

		obtieneHorario.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepAudioPrevioDerivoAsesor.GetId());

		stepAudioDisuadeDerivoAsesor.setNextstep(stepAudioMenuInicial.GetId());

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {
		// if (audioDni.isEmpty() || audioDniInvalido.isEmpty()
		// || audioValidateDni.isEmpty() || audioSuDniEs.isEmpty()) {
		// throw new IllegalArgumentException("Variables de audio Vacias");
		// }
		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioMenuInicial.GetId();
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

	public PreAtendedorBIE() {
		super();

		GroupType = StepGroupType.preAtendedorBIE;
	}

	private void createSteps() {
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

		obtieneHorarioTranferencias = (StepTimeConditionDB) StepFactory
				.createStep(StepType.TimeConditionDB, UUID.randomUUID());
		obtieneHorarioTranferencias
				.setStepDescription("Obtiene horario de la base de datos");
		obtieneHorarioTranferencias.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorarioTranferencias.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorarioTranferencias.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		Steps.put(obtieneHorarioTranferencias.GetId(),
				obtieneHorarioTranferencias);

		obtieneHorarioCertificaciones = (StepTimeConditionDB) StepFactory
				.createStep(StepType.TimeConditionDB, UUID.randomUUID());
		obtieneHorarioCertificaciones
				.setStepDescription("Obtiene horario de la base de datos");
		obtieneHorarioCertificaciones.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorarioCertificaciones.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorarioCertificaciones.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		Steps.put(obtieneHorarioCertificaciones.GetId(),
				obtieneHorarioCertificaciones);

		/*--- Play Read --- */

		stepAudioMenuInicial = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuInicial.setStepDescription("PLAYREAD => MENU INICIAL BIE");
		stepAudioMenuInicial.setPlayMaxDigits(1);
		stepAudioMenuInicial.setPlayTimeout(2000L);
		stepAudioMenuInicial.setPlayFile("PREATENDEDORCABAL/001");
		stepAudioMenuInicial.setContextVariableName(ctxVar
				.getContextVarByName("menuInicialBIEContextVar"));
		Steps.put(stepAudioMenuInicial.GetId(), stepAudioMenuInicial);

		// Sub - Menus

		stepAudioMenuClaves = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuClaves.setStepDescription("PLAYREAD => MENU CLAVES BIE");
		stepAudioMenuClaves.setPlayMaxDigits(1);
		stepAudioMenuClaves.setPlayTimeout(2000L);
		stepAudioMenuClaves.setPlayFile("PREATENDEDORCABAL/027");
		stepAudioMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("menuClavesContextVar"));
		Steps.put(stepAudioMenuClaves.GetId(), stepAudioMenuClaves);

		stepAudioMenuTransferencias = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuTransferencias
				.setStepDescription("PLAYREAD => MENU TRANSFERENCIAS BIE");
		stepAudioMenuTransferencias.setPlayMaxDigits(1);
		stepAudioMenuTransferencias.setPlayTimeout(2000L);
		stepAudioMenuTransferencias.setPlayFile("PREATENDEDORCABAL/003");
		stepAudioMenuTransferencias.setContextVariableName(ctxVar
				.getContextVarByName("menuTransferenciasContextVar"));
		Steps.put(stepAudioMenuTransferencias.GetId(),
				stepAudioMenuTransferencias);

		stepAudioMenuPagos = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuPagos.setStepDescription("PLAYREAD => MENU PAGOS BIE");
		stepAudioMenuPagos.setPlayMaxDigits(1);
		stepAudioMenuPagos.setPlayTimeout(2000L);
		stepAudioMenuPagos.setPlayFile("PREATENDEDORCABAL/004");
		stepAudioMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("menuPagosContextVar"));
		Steps.put(stepAudioMenuPagos.GetId(), stepAudioMenuPagos);

		stepAudioMenuOtrasConsultas = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuOtrasConsultas
				.setStepDescription("PLAYREAD => MENU OTRAS CONSULTAS BIE");
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
				.setStepDescription("PLAYREAD => SUB MENU CLAVES BIE");
		stepAudioSubMenuClaves.setPlayMaxDigits(1);
		stepAudioSubMenuClaves.setPlayTimeout(2000L);
		stepAudioSubMenuClaves.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioSubMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("subMenuClavesContextVar"));
		Steps.put(stepAudioSubMenuClaves.GetId(), stepAudioSubMenuClaves);

		stepAudioMenuOlvidoClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuOlvidoClave
				.setStepDescription("PLAYREAD => MENU OLVIDO CLAVE BIE");
		stepAudioMenuOlvidoClave.setPlayMaxDigits(1);
		stepAudioMenuOlvidoClave.setPlayTimeout(2000L);
		stepAudioMenuOlvidoClave.setPlayFile("PREATENDEDORCABAL/041bis");
		stepAudioMenuOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("olvidoClaveContextVar"));
		Steps.put(stepAudioMenuOlvidoClave.GetId(), stepAudioMenuOlvidoClave);

		stepAudioMenuCertificarPC = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuCertificarPC
				.setStepDescription("PLAYREAD => MENU CERTIFICAR PC BIE");
		stepAudioMenuCertificarPC.setPlayMaxDigits(1);
		stepAudioMenuCertificarPC.setPlayTimeout(2000L);
		stepAudioMenuCertificarPC.setPlayFile("PREATENDEDORCABAL/024");
		stepAudioMenuCertificarPC.setContextVariableName(ctxVar
				.getContextVarByName("certificarPCContextVar"));
		Steps.put(stepAudioMenuCertificarPC.GetId(), stepAudioMenuCertificarPC);

		stepAudioMenuAsesoramientoAUsuario = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuAsesoramientoAUsuario
				.setStepDescription("PLAYREAD => MENU ASESORAMIENTO A USUARIO BIE");
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
				.setStepDescription("PLAYREAD => MENU TRASNFERENCIA BIE");
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
				.setStepDescription("PLAYREAD => SUBMENU PAGOS BIE");
		stepAudioSubMenuPagos.setPlayMaxDigits(1);
		stepAudioSubMenuPagos.setPlayTimeout(2000L);
		stepAudioSubMenuPagos.setPlayFile("PREATENDEDORCABAL/010");
		stepAudioSubMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("subMenuPagosContextVar"));
		Steps.put(stepAudioSubMenuPagos.GetId(), stepAudioSubMenuPagos);

		stepAudioMenuTarjetaCoordenadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioMenuTarjetaCoordenadas
				.setStepDescription("PLAYREAD => MENU TARJETA COORDENADAS BIE");
		stepAudioMenuTarjetaCoordenadas.setPlayMaxDigits(1);
		stepAudioMenuTarjetaCoordenadas.setPlayTimeout(2000L);
		stepAudioMenuTarjetaCoordenadas.setPlayFile("PREATENDEDORCABAL/011");
		stepAudioMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("menuTarjetaCoordenadasContextVar"));
		Steps.put(stepAudioMenuTarjetaCoordenadas.GetId(),
				stepAudioMenuTarjetaCoordenadas);

		stepAudioSubMenuTarjetaCoordenadas = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioSubMenuTarjetaCoordenadas
				.setStepDescription("PLAYREAD => MENU TARJETA COORDENADAS BIE");
		stepAudioSubMenuTarjetaCoordenadas.setPlayMaxDigits(1);
		stepAudioSubMenuTarjetaCoordenadas.setPlayTimeout(2000L);
		stepAudioSubMenuTarjetaCoordenadas.setPlayFile("PREATENDEDORCABAL/012");
		stepAudioSubMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("subMenuTarjetaCoordenadasContextVar"));
		Steps.put(stepAudioSubMenuTarjetaCoordenadas.GetId(),
				stepAudioSubMenuTarjetaCoordenadas);

		stepAudioRepetirInfoAdicionalMovil = (StepPlayRead) StepFactory
				.createStep(StepType.PlayRead, UUID.randomUUID());
		stepAudioRepetirInfoAdicionalMovil
				.setStepDescription("PLAYREAD => REPETIR INFORMACION CREDICOOP MOVIL BIE");
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
				.setStepDescription("PLAYREAD =>  REPETIR INFORMACION TARJETA COORDENADAS BIE");
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
				.setStepDescription("PLAYREAD =>  REPETIR INFORMACION TUTORIAL BIE");
		stepAudioRepetirTutorial.setPlayMaxDigits(1);
		stepAudioRepetirTutorial.setPlayTimeout(2000L);
		stepAudioRepetirTutorial.setPlayFile("PREATENDEDORCABAL/013");
		stepAudioRepetirTutorial.setContextVariableName(ctxVar
				.getContextVarByName("repetirInformacionTutorialContextVar"));
		Steps.put(stepAudioRepetirTutorial.GetId(), stepAudioRepetirTutorial);

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

		/*--- Play --- */
		stepAudioRecuerdaClave = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioRecuerdaClave.setPlayfile("PREATENDEDORCABAL/028");
		stepAudioRecuerdaClave
				.setStepDescription("PLAY => PARA COMENZAR A OPERAR EN BIE GENERAR CLAVE EN UN CAJERO LINK");
		Steps.put(stepAudioRecuerdaClave.GetId(), stepAudioRecuerdaClave);

		stepAudioParaGenerarClaveEmpresa = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioParaGenerarClaveEmpresa.setPlayfile("PREATENDEDORCABAL/040");
		stepAudioParaGenerarClaveEmpresa
				.setStepDescription("PLAY => PASOS GENERAR CLAVE EN UN CAJERO LINK");
		Steps.put(stepAudioParaGenerarClaveEmpresa.GetId(),
				stepAudioParaGenerarClaveEmpresa);

		stepAudioCertificarPC = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioCertificarPC.setPlayfile("PREATENDEDORCABAL/044");
		stepAudioCertificarPC
				.setStepDescription("PLAY => AUDIO PREVIO MENU CERTIFICAR PC");
		Steps.put(stepAudioCertificarPC.GetId(), stepAudioCertificarPC);

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		stepAudioPrevioDerivoAsesorCertificacion = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesorCertificacion
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesorCertificacion
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR CERTIFICACIONES");
		Steps.put(stepAudioPrevioDerivoAsesorCertificacion.GetId(),
				stepAudioPrevioDerivoAsesorCertificacion);

		stepAudioPrevioDerivoAsesorTranferencias = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesorTranferencias
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesorTranferencias
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR TRANSFERENCIAS");
		Steps.put(stepAudioPrevioDerivoAsesorTranferencias.GetId(),
				stepAudioPrevioDerivoAsesorTranferencias);

		stepAudioDisuadeDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisuadeDerivoAsesor.setPlayfile("PREATENDEDORCABAL/098");
		stepAudioDisuadeDerivoAsesor
				.setStepDescription("PLAY => DISUADE DERIVO POR ESTAR FUERA DE HORARIO");
		Steps.put(stepAudioDisuadeDerivoAsesor.GetId(),
				stepAudioDisuadeDerivoAsesor);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL RUTINA BPI");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

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

		stepAudioOlvidoLaClave = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioOlvidoLaClave.setPlayfile("PREATENDEDORCABAL/041");
		stepAudioOlvidoLaClave.setStepDescription("PLAY => AUDIO OLVIDO CLAVE");
		Steps.put(stepAudioOlvidoLaClave.GetId(), stepAudioOlvidoLaClave);

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

		stepAudioAsesoramientoAUsuario = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioAsesoramientoAUsuario.setPlayfile("PREATENDEDORCABAL/018");
		stepAudioAsesoramientoAUsuario
				.setStepDescription("PLAY => ASESORAMIENTO A USUARIO");
		Steps.put(stepAudioAsesoramientoAUsuario.GetId(),
				stepAudioAsesoramientoAUsuario);

		stepAudioTutorialWeb = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioTutorialWeb.setPlayfile("PREATENDEDORCABAL/043");
		stepAudioTutorialWeb.setStepDescription("PLAY => TUTORIAL WEB BIE");
		Steps.put(stepAudioTutorialWeb.GetId(), stepAudioTutorialWeb);

		stepAudioTutorialWeb2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTutorialWeb2.setPlayfile("PREATENDEDORCABAL/019");
		stepAudioTutorialWeb2.setStepDescription("PLAY => TUTORIAL WEB BIE");
		Steps.put(stepAudioTutorialWeb2.GetId(), stepAudioTutorialWeb2);

		stepAudioTutorialPagos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioTutorialPagos.setPlayfile("PREATENDEDORCABAL/020");
		stepAudioTutorialPagos.setStepDescription("PLAY => TUTORIAL PAGOS BIE");
		Steps.put(stepAudioTutorialPagos.GetId(), stepAudioTutorialPagos);

		stepAudioPrevioSubMenuPagos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioSubMenuPagos.setPlayfile("PREATENDEDORCABAL/021");
		stepAudioPrevioSubMenuPagos
				.setStepDescription("PLAY => SUB MENU PAGOS BIE");
		Steps.put(stepAudioPrevioSubMenuPagos.GetId(),
				stepAudioPrevioSubMenuPagos);

		stepAudioPrevioMenuOtrasConsultas = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioMenuOtrasConsultas.setPlayfile("PREATENDEDORCABAL/043");
		stepAudioPrevioMenuOtrasConsultas
				.setStepDescription("PLAY => PREVIO OTRAS CONSULTAS BIE");
		Steps.put(stepAudioPrevioMenuOtrasConsultas.GetId(),
				stepAudioPrevioMenuOtrasConsultas);

		stepAudioPrevioSubMenuTarjetaCoordenadas = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioPrevioSubMenuTarjetaCoordenadas
				.setPlayfile("PREATENDEDORCABAL/017");
		stepAudioPrevioSubMenuTarjetaCoordenadas
				.setStepDescription("PLAY => PREVIO MENU TARJETA DE COORDENADAS BIE");
		Steps.put(stepAudioPrevioSubMenuTarjetaCoordenadas.GetId(),
				stepAudioPrevioSubMenuTarjetaCoordenadas);

		stepAudioInfoAdicionalMovil = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioInfoAdicionalMovil.setPlayfile("PREATENDEDORCABAL/023");
		stepAudioInfoAdicionalMovil
				.setStepDescription("PLAY => INFORMACION ADICIONAL CREDICOOP MOVIL BIE");
		Steps.put(stepAudioInfoAdicionalMovil.GetId(),
				stepAudioInfoAdicionalMovil);

		stepAudioInfoAdicionalTarjetaCoordenadas = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioInfoAdicionalTarjetaCoordenadas
				.setPlayfile("PREATENDEDORCABAL/023");
		stepAudioInfoAdicionalTarjetaCoordenadas
				.setStepDescription("PLAY => INFORMACION ADICIONAL TARJETA DE COORDENADAS BIE");
		Steps.put(stepAudioInfoAdicionalTarjetaCoordenadas.GetId(),
				stepAudioInfoAdicionalTarjetaCoordenadas);

		/*--- Contadores --- */

		contadorIntentosMenuInicialBIE = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuInicialBIE.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuInicialBIEContextVar"));
		contadorIntentosMenuInicialBIE
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE MENU INICIAL");
		Steps.put(contadorIntentosMenuInicialBIE.GetId(),
				contadorIntentosMenuInicialBIE);

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

		contadorIntentosMenuRepetirTutorial = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuRepetirTutorial.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuRepetirTutorialContextVar"));
		contadorIntentosMenuRepetirTutorial
				.setStepDescription("COUNTER => INCREMENTA INTENTOS MENU REPETIR INFORMACION TUTORIAL");
		Steps.put(contadorIntentosMenuRepetirTutorial.GetId(),
				contadorIntentosMenuRepetirTutorial);

		contadorIntentosRepeticionGenerarClaveEmpresa = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepeticionGenerarClaveEmpresa
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosRepeticionClaveEmpresaContextVar"));
		contadorIntentosRepeticionGenerarClaveEmpresa
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE TUTORIAL GENERACION CLAVE EMPRESA");
		Steps.put(contadorIntentosRepeticionGenerarClaveEmpresa.GetId(),
				contadorIntentosRepeticionGenerarClaveEmpresa);

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

		contadorIntentosRepetirOlvidoClave = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("intentosRepetirOlvidoClaveContextVar"));
		contadorIntentosRepetirOlvidoClave
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE OLVIDO CLAVE");
		Steps.put(contadorIntentosRepetirOlvidoClave.GetId(),
				contadorIntentosRepetirOlvidoClave);

		contadorAsesoramientoAUsuario = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorAsesoramientoAUsuario
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosAsesoramientoAUsuarioContextVar"));
		contadorAsesoramientoAUsuario
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE ASESORAMIENTO A USUARIO");
		Steps.put(contadorAsesoramientoAUsuario.GetId(),
				contadorAsesoramientoAUsuario);

		contadorIntentosMenuCertificarPC = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosMenuCertificarPC.setContextVariableName(ctxVar
				.getContextVarByName("intentosCertificarPCContextVar"));
		contadorIntentosMenuCertificarPC
				.setStepDescription("COUNTER => INCREMENTA INTENTOS MENU CERTIFICAR PC");
		Steps.put(contadorIntentosMenuCertificarPC.GetId(),
				contadorIntentosMenuCertificarPC);

		contadorIntentosRepetirInformacionTarjetaCoordenadas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosSubTarjetaCoordenadasContextVar"));
		contadorIntentosRepetirInformacionTarjetaCoordenadas
				.setStepDescription("COUNTER => INCREMENTA INTENTOS TARJETA COORDENADAS");
		Steps.put(contadorIntentosRepetirInformacionTarjetaCoordenadas.GetId(),
				contadorIntentosRepetirInformacionTarjetaCoordenadas);

		contadorIntentosRepetirInformacionPagos = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionPagos.setContextVariableName(ctxVar
				.getContextVarByName("intentosMenuPagosContextVar"));
		contadorIntentosRepetirInformacionPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION PAGOS");
		Steps.put(contadorIntentosRepetirInformacionPagos.GetId(),
				contadorIntentosRepetirInformacionPagos);

		contadorIntentosRepetirInformacionSubMenuPagos = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosRepetirInformacionSubMenuPagos
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosRepetirInformacionMenuPagosContextVar"));
		contadorIntentosRepetirInformacionSubMenuPagos
				.setStepDescription("COUNTER => INCREMENTA INTENTOS REPETIR INFORMACION SUB MENU PAGOS");
		Steps.put(contadorIntentosRepetirInformacionSubMenuPagos.GetId(),
				contadorIntentosRepetirInformacionSubMenuPagos);

		contadorIntentosSubMenuTarjetaCoordenadas = (StepCounter) StepFactory
				.createStep(StepType.Counter, UUID.randomUUID());
		contadorIntentosSubMenuTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("intentosSubTarjetaCoordenadasContextVar"));
		contadorIntentosSubMenuTarjetaCoordenadas
				.setStepDescription("COUNTER => INCREMENTA INTENTOS TARJETA COORDENADAS");
		Steps.put(contadorIntentosSubMenuTarjetaCoordenadas.GetId(),
				contadorIntentosSubMenuTarjetaCoordenadas);

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

		/*--- Condicional --- */

		evalContadorMenuInicialBIE = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuInicialBIE
				.setStepDescription("CONDITIONAL => INTENTOS MENU INICIAL BIE");
		Steps.put(evalContadorMenuInicialBIE.GetId(),
				evalContadorMenuInicialBIE);

		evalContadorMenuClaves = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuClaves
				.setStepDescription("CONDITIONAL => INTENTOS MENU CLAVES BIE");
		Steps.put(evalContadorMenuClaves.GetId(), evalContadorMenuClaves);

		evalContadorIntentosRepetirInformacionTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosRepetirInformacionTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU TARJETA COORDENADAS BPI");
		Steps.put(evalContadorIntentosRepetirInformacionTarjetaCoordenadas
				.GetId(),
				evalContadorIntentosRepetirInformacionTarjetaCoordenadas);

		evalContadorMenuTransferencias = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuTransferencias
				.setStepDescription("CONDITIONAL => INTENTOS MENU TRANSFERENCIAS BIE");
		Steps.put(evalContadorMenuTransferencias.GetId(),
				evalContadorMenuTransferencias);

		evalContadorIntentosRepetirOlvidoClave = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosRepetirOlvidoClave
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR OLVIO CLAVE");
		Steps.put(evalContadorIntentosRepetirOlvidoClave.GetId(),
				evalContadorIntentosRepetirOlvidoClave);

		evalContadorIntentosMenuTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosMenuTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS MENU TARJETA COORDENADAS");
		Steps.put(evalContadorIntentosMenuTarjetaCoordenadas.GetId(),
				evalContadorIntentosMenuTarjetaCoordenadas);

		evalContadorMenuPagos = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuPagos
				.setStepDescription("CONDITIONAL => INTENTOS MENU PAGOS BIE");
		Steps.put(evalContadorMenuPagos.GetId(), evalContadorMenuPagos);

		evalContadorMenuOtrasConsultas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuOtrasConsultas
				.setStepDescription("CONDITIONAL => INTENTOS MENU OTRAS CONSULTAS BIE");
		Steps.put(evalContadorMenuOtrasConsultas.GetId(),
				evalContadorMenuOtrasConsultas);

		evalContadorIntentosRepeticionGenerarClaveEmpresa = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosRepeticionGenerarClaveEmpresa
				.setStepDescription("CONDITIONAL => INTENTOS MENU REPETIR CLAVE EMPRESA");
		Steps.put(evalContadorIntentosRepeticionGenerarClaveEmpresa.GetId(),
				evalContadorIntentosRepeticionGenerarClaveEmpresa);

		evalContadorIntentosSubMenuClaves = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosSubMenuClaves
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU CLAVES");
		Steps.put(evalContadorIntentosSubMenuClaves.GetId(),
				evalContadorIntentosSubMenuClaves);

		evalContadorCertificarPC = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorCertificarPC
				.setStepDescription("CONDITIONAL => INTENTOS CERTIFICAR PC");
		Steps.put(evalContadorCertificarPC.GetId(), evalContadorCertificarPC);

		evalContadorIntentosOlvidoClave = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosOlvidoClave
				.setStepDescription("CONDITIONAL => INTENTOS OLVIDO CLAVE BIE");
		Steps.put(evalContadorIntentosOlvidoClave.GetId(),
				evalContadorIntentosOlvidoClave);

		evalContadorIntentosSubMenuTarjetaCoordenadas = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorIntentosSubMenuTarjetaCoordenadas
				.setStepDescription("CONDITIONAL => INTENTOS SUB MENU TARJETA COORDENADAS BIE");
		Steps.put(evalContadorIntentosSubMenuTarjetaCoordenadas.GetId(),
				evalContadorIntentosSubMenuTarjetaCoordenadas);

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
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION SUB MENU PAGOS BIE");
		Steps.put(evalContadorRepetirInformacionSubMenuPagos.GetId(),
				evalContadorRepetirInformacionSubMenuPagos);

		evalContadorRepetirInformacionPagos = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorRepetirInformacionPagos
				.setStepDescription("CONDITIONAL => INTENTOS REPETIR INFORMACION PAGOS BIE");
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

		evalContadorMenuRepetirTutorial = (StepConditional) StepFactory
				.createStep(StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorMenuRepetirTutorial
				.setStepDescription("CONDITIONAL => INTENTOS MENU REPETIR INFORMACION TUTORIAL");
		Steps.put(evalContadorMenuRepetirTutorial.GetId(),
				evalContadorMenuRepetirTutorial);

		/*--- Menu --- */

		stepMenuInicialBIE = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuInicialBIE.setStepDescription("MENU =>  MENU INICIAL BIE");
		stepMenuInicialBIE.setContextVariableName(ctxVar
				.getContextVarByName("menuInicialBIEContextVar"));
		Steps.put(stepMenuInicialBIE.GetId(), stepMenuInicialBIE);

		stepMenuPagos = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuPagos.setStepDescription("MENU =>  MENU PAGOS BIE");
		stepMenuPagos.setContextVariableName(ctxVar
				.getContextVarByName("menuPagosContextVar"));
		Steps.put(stepMenuPagos.GetId(), stepMenuPagos);

		stepMenuTransferencias = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuTransferencias
				.setStepDescription("MENU =>  MENU TRANSFERENCIAS BIE");
		stepMenuTransferencias.setContextVariableName(ctxVar
				.getContextVarByName("menuTransferenciasContextVar"));
		Steps.put(stepMenuTransferencias.GetId(), stepMenuTransferencias);

		stepMenuClaves = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuClaves.setStepDescription("MENU =>  MENU CLAVES BIE");
		stepMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("menuClavesContextVar"));
		Steps.put(stepMenuClaves.GetId(), stepMenuClaves);

		stepMenuOtrasConsultas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuOtrasConsultas
				.setStepDescription("MENU =>  MENU OTRAS CONSULTAS BIE");
		stepMenuOtrasConsultas.setContextVariableName(ctxVar
				.getContextVarByName("menuOtrasConsultasContextVar"));
		Steps.put(stepMenuOtrasConsultas.GetId(), stepMenuOtrasConsultas);

		// Sub Menu

		stepSubMenuClaves = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepSubMenuClaves.setStepDescription("MENU =>  SUBMENU CLAVES BIE");
		stepSubMenuClaves.setContextVariableName(ctxVar
				.getContextVarByName("subMenuClavesContextVar"));
		Steps.put(stepSubMenuClaves.GetId(), stepSubMenuClaves);

		stepMenuOlvidoClave = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuOlvidoClave
				.setStepDescription("MENU =>  MENU OLVIDO CLAVE BIE");
		stepMenuOlvidoClave.setContextVariableName(ctxVar
				.getContextVarByName("olvidoClaveContextVar"));
		Steps.put(stepMenuOlvidoClave.GetId(), stepMenuOlvidoClave);

		stepMenuAsesoramientoAUsuario = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuAsesoramientoAUsuario
				.setStepDescription("MENU =>  MENU ASESORAMIENTO A USUARIOS BIE");
		stepMenuAsesoramientoAUsuario.setContextVariableName(ctxVar
				.getContextVarByName("asesoramientoAUsiaroContextVar"));
		Steps.put(stepMenuAsesoramientoAUsuario.GetId(),
				stepMenuAsesoramientoAUsuario);

		stepSubMenuTransferencia = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepSubMenuTransferencia
				.setStepDescription("MENU =>  SUBMENU TRANSFERENCIA BIE");
		stepSubMenuTransferencia.setContextVariableName(ctxVar
				.getContextVarByName("subMenuTransferenciaContextVar"));
		Steps.put(stepSubMenuTransferencia.GetId(), stepSubMenuTransferencia);

		stepMenuRepetirTutorial = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuRepetirTutorial
				.setStepDescription("MENU =>  MENU REPETIR INFORMACION TUTORIAL BIE");
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

		stepSubMenuTarjetaCoordenadas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepSubMenuTarjetaCoordenadas
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepSubMenuTarjetaCoordenadas.setContextVariableName(ctxVar
				.getContextVarByName("subMenuTarjetaCoordenadasContextVar"));
		Steps.put(stepSubMenuTarjetaCoordenadas.GetId(),
				stepSubMenuTarjetaCoordenadas);

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

		stepMenuRepetirInformacionTarjetaCoordenadas = (StepMenu) StepFactory
				.createStep(StepType.Menu, UUID.randomUUID());
		stepMenuRepetirInformacionTarjetaCoordenadas
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepMenuRepetirInformacionTarjetaCoordenadas
				.setContextVariableName(ctxVar
						.getContextVarByName("subMenuTarjetaCoordenadasContextVar"));
		Steps.put(stepMenuRepetirInformacionTarjetaCoordenadas.GetId(),
				stepMenuRepetirInformacionTarjetaCoordenadas);

		stepMenuCertificarPC = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuCertificarPC
				.setStepDescription("MENU =>  SUBMENU TARJETA DE COORDENADAS");
		stepMenuCertificarPC.setContextVariableName(ctxVar
				.getContextVarByName("certificarPCContextVar"));
		Steps.put(stepMenuCertificarPC.GetId(), stepMenuCertificarPC);

		/* ---------------- Derivo -------------- */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORBIE"));
		stepDerivoLlamada
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR");
		Steps.put(stepDerivoLlamada.GetId(), stepDerivoLlamada);

		stepDerivoLlamadaTranferencias = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamadaTranferencias.setApp("goto");
		stepDerivoLlamadaTranferencias.setAppOptions(Daemon
				.getConfig("DERIVOOPERADORTRANSFERENCIASBIE"));
		stepDerivoLlamadaTranferencias
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR TRANSFERENCIAS");
		Steps.put(stepDerivoLlamadaTranferencias.GetId(),
				stepDerivoLlamadaTranferencias);

		stepDerivoLlamadaCertificaciones = (StepExecute) StepFactory
				.createStep(StepType.Execute, UUID.randomUUID());
		stepDerivoLlamadaCertificaciones.setApp("goto");
		stepDerivoLlamadaCertificaciones.setAppOptions(Daemon
				.getConfig("DERIVOOPERADORCERTIFICACIONESBIE"));
		stepDerivoLlamadaCertificaciones
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR CERTIFICACIONES");
		Steps.put(stepDerivoLlamadaCertificaciones.GetId(),
				stepDerivoLlamadaCertificaciones);

	}

	public PideDni getPideDni() {
		return pideDniGrp;
	}

}
