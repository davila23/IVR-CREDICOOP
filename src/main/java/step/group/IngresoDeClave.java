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
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayNumber;
import step.StepSetAsteriskVariable;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;

import workflow.Task;

public class IngresoDeClave implements StepGroup {

	protected StepGroupType GroupType;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private StepPlay stepAudioClaveIncorrecta;
	private StepPlay stepAudioClaveInvalida;
	private StepPlay stepAudioClaveExpirada;
	private StepPlay stepAudioClaveBloqueada;
	private StepPlayRead stepAudioClave;
	private StepPlayRead stepAudioDeseaCambiarClave;
	private StepCounter contadorIntentosClave;
	private StepCounter contadorIntentosClaveVacia;
	private StepConditional evalContadorClave;
	private StepConditional evalContadorClaveVacia;
	private StepConditional evalFechaDeCaducidadClave;
	private StepConditional evalUsuarioConClaveBloq;
	private StepConditional evalUsuarioConClaveExpirada;
	private StepExecute stepDerivoAlMenuPrincipal;
	private StepMenu stepMenuCambiarClave;
	private StepMenu stepMenuConfirmacionMenu;
	private StepUserAuthentication autentificarClave;
	private StepConditional evalNuevoUsuario;
	private StepAuthInitialInfo infoCrecer;
	private StepIsOwnCard esTarjetaPropia;
	private StepConditional evalClaveOK;
	private StepConditional evalClaveErronea;
	private StepConditional evalClaveExpirada;
	private StepConditional evalClaveBloqueada;
	private int diasDeExpiracion = 7;
	private StepPlay stepAudioClaveVenceEn;
	private StepSayNumber stepDiceDiasExpiracion;
	private StepConditional evalCambioClave;
	private StepPlay stepAudioFinal;
	private StepEnd pasoFinal;
	private StepExecute stepDerivoLMenuInicial;
	private StepSetAsteriskVariable stepCancelDerivo;
	private StepSetAsteriskVariable stepDerivoMenu3;

	private void setSequence() {

		/*
		 * usserStatus => New = 0, Migrated = 1, NoMigrated = 2, BloqKey = 3,
		 * ExpireKey =4
		 */

		infoCrecer.setNextstep(evalNuevoUsuario.GetId());

		evalNuevoUsuario.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("estadoDelUsuarioContextVar")
						.getVarName() + "} == " + 0, stepIfFalseUUID,
				evalUsuarioConClaveBloq.GetId()));

		evalUsuarioConClaveBloq.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("estadoDelUsuarioContextVar")
						.getVarName() + "} == " + 3, stepAudioClaveExpirada
				.GetId(), evalUsuarioConClaveExpirada.GetId()));

		evalUsuarioConClaveExpirada.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("estadoDelUsuarioContextVar")
						.getVarName() + "} == " + 4, stepAudioClaveExpirada
				.GetId(), stepAudioClave.GetId()));

		/* Ingreso Clave */

		stepAudioClave.setNextstep(evalCambioClave.GetId());

		evalCambioClave.addCondition(new condition(1, "equals('#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "}','')", contadorIntentosClaveVacia.GetId(),
				autentificarClave.GetId()));

		evalCambioClave.addCondition(new condition(2, "#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "} == " + 1, stepIfFalseUUID, autentificarClave.GetId()));

		contadorIntentosClaveVacia.setNextstep(evalContadorClaveVacia.GetId());

		evalContadorClaveVacia.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveVaciaContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepCancelDerivo.GetId()));

		stepCancelDerivo.setNextstep(stepDerivoMenu3.GetId());
		
		stepDerivoMenu3.setNextstep(stepDerivoAlMenuPrincipal.GetId());
		
		// stepAudioFinal.setNextstep(pasoFinal.GetId());

		autentificarClave.setNextstep(evalClaveOK.GetId());

		/* Ingreso Valido */

		/* passwordStatus => Ok = 0, Wrong = 1 ,Expired = 2,Locked = 3 */

		evalClaveOK.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 0, stepIfTrueUUID,
				evalClaveErronea.GetId()));

		evalClaveErronea.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 1, stepAudioClaveInvalida
				.GetId(), evalClaveExpirada.GetId()));

		evalClaveExpirada.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 2, stepIfFalseUUID,
				evalClaveBloqueada.GetId()));

		evalClaveBloqueada.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 3, stepAudioClaveBloqueada
				.GetId(), evalFechaDeCaducidadClave.GetId()));

		// pwdExpireWarning = (int) (pwdMaxAge - expiringTime) / 86400;

		evalFechaDeCaducidadClave.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 4, stepAudioClaveVenceEn
				.GetId(), stepIfTrueUUID));

		/* Menu Cambio por Expiracion */

		stepAudioClaveVenceEn.setNextstep(stepDiceDiasExpiracion.GetId());
		stepDiceDiasExpiracion.setNextstep(stepAudioDeseaCambiarClave.GetId());

		stepAudioDeseaCambiarClave.setNextstep(stepMenuCambiarClave.GetId());
		stepMenuCambiarClave.addSteps("1", stepIfFalseUUID);
		stepMenuCambiarClave.addSteps("2", stepIfTrueUUID);
		stepMenuCambiarClave.setInvalidOption(stepAudioDeseaCambiarClave
				.GetId());

		/* Ingreso Invalido (nros Consecutivos o iguales) */

		stepAudioClaveInvalida.setNextstep(contadorIntentosClave.GetId());

		contadorIntentosClave.setNextstep(evalContadorClave.GetId());
		evalContadorClave.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepAudioClaveBloqueada.GetId()));

		stepAudioClaveExpirada.setNextstep(stepIfFalseUUID);
		stepAudioClaveBloqueada.setNextstep(stepIfFalseUUID);
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
		return infoCrecer.GetId();
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

	public IngresoDeClave() {
		super();

		GroupType = StepGroupType.generacionDeClave;
	}

	private void createSteps() {
		/*--- Play Read --- */

		stepAudioClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioClave.setStepDescription("PLAYREAD => INGRESO CLAVE");
		stepAudioClave.setPlayMaxDigits(4);
		stepAudioClave.setPlayTimeout(5000L);
		stepAudioClave.setPlayFile("INGRESO/001");
		stepAudioClave.setContextVariableName(ctxVar
				.getContextVarByName("claveContextVar"));
		Steps.put(stepAudioClave.GetId(), stepAudioClave);

		stepAudioDeseaCambiarClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioDeseaCambiarClave
				.setStepDescription("PLAYREAD => CLAVE POR EXPIRAR, DESEA CAMBIARLA ?");
		stepAudioDeseaCambiarClave.setPlayMaxDigits(1);
		stepAudioDeseaCambiarClave.setPlayTimeout(2000L);
		stepAudioDeseaCambiarClave.setPlayFile("INGRESO/005bis");
		stepAudioDeseaCambiarClave.setContextVariableName(ctxVar
				.getContextVarByName("cambiarClaveContextVar"));
		Steps.put(stepAudioDeseaCambiarClave.GetId(),
				stepAudioDeseaCambiarClave);

		/*--- Dias De Expiracion --- */

		stepDiceDiasExpiracion = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepDiceDiasExpiracion
				.setStepDescription("SAYNUMBER => DIAS EXPIRACION");
		stepDiceDiasExpiracion.setContextVariableName(ctxVar
				.getContextVarByName("cantDiasExpiracioncontextVar"));
		Steps.put(stepDiceDiasExpiracion.GetId(), stepDiceDiasExpiracion);

		/*--- Audios --- */

		stepAudioClaveVenceEn = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveVenceEn.setPlayfile("INGRESO/005");
		stepAudioClaveVenceEn.setStepDescription("PLAY => CLAVE VENCE EN XX ");
		Steps.put(stepAudioClaveVenceEn.GetId(), stepAudioClaveVenceEn);

		stepAudioClaveBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveBloqueada.setPlayfile("INGRESO/003");
		stepAudioClaveBloqueada.setStepDescription("PLAY => CLAVE BLOQUEADA");
		Steps.put(stepAudioClaveBloqueada.GetId(), stepAudioClaveBloqueada);

		stepAudioClaveExpirada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveExpirada.setStepDescription("PLAY => CLAVE EXPIRADA");
		stepAudioClaveExpirada.setPlayfile("INGRESO/006");
		Steps.put(stepAudioClaveExpirada.GetId(), stepAudioClaveExpirada);

		stepAudioClaveInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveInvalida.setStepDescription("PLAY => CLAVE INVALIDA");
		stepAudioClaveInvalida.setPlayfile("INGRESO/002");
		Steps.put(stepAudioClaveInvalida.GetId(), stepAudioClaveInvalida);

		stepAudioClaveIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveIncorrecta.setStepDescription("PLAY => CLAVE INCORRECTA");
		stepAudioClaveIncorrecta.setPlayfile("INGRESO/002");
		Steps.put(stepAudioClaveIncorrecta.GetId(), stepAudioClaveIncorrecta);

		/*--- Contadores --- */

		contadorIntentosClave = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClave.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveContextVar"));
		contadorIntentosClave
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE");
		Steps.put(contadorIntentosClave.GetId(), contadorIntentosClave);

		contadorIntentosClaveVacia = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClaveVacia.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveVaciaContextVar"));
		contadorIntentosClaveVacia
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE VACIA");
		Steps.put(contadorIntentosClaveVacia.GetId(),
				contadorIntentosClaveVacia);

		/*--- Steps Validaciones --- */

		infoCrecer = (StepAuthInitialInfo) StepFactory.createStep(
				StepType.AuthInitialInfo, UUID.randomUUID());
		infoCrecer
				.setStepDescription("INITIALINFO => OBTIENE STATUS DEL USUARIO");
		infoCrecer.setUserStatus(ctxVar
				.getContextVarByName("estadoDelUsuarioContextVar"));
		infoCrecer.setIdCrecerContextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		Steps.put(infoCrecer.GetId(), infoCrecer);

		autentificarClave = (StepUserAuthentication) StepFactory.createStep(
				StepType.AutentificarClave, UUID.randomUUID());
		autentificarClave
				.setStepDescription("USERAUTHENTICATION => AUTENTICA CLAVE INGRESADA");
		autentificarClave.setCantDiasExpiracioncontextVar(ctxVar
				.getContextVarByName("cantDiasExpiracioncontextVar"));
		autentificarClave.setClaveContextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		autentificarClave.setIdCrecerContextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		autentificarClave.setRetAuthPasswordcontextVar(ctxVar
				.getContextVarByName("retAutentificacionContextVar"));
		Steps.put(autentificarClave.GetId(), autentificarClave);

		esTarjetaPropia = (StepIsOwnCard) StepFactory.createStep(
				StepType.IsOwnCard, UUID.randomUUID());
		esTarjetaPropia.setTarjetaContextVariableName(ctxVar
				.getContextVarByName("tarjetaContextVar"));
		esTarjetaPropia.setIdCrecerContextVariableName(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		esTarjetaPropia
				.setStepDescription("ISOWNCARD => VERIFICA QUE LA TARJETA INGRESADA SEA PROPIA");
		Steps.put(esTarjetaPropia.GetId(), esTarjetaPropia);

		/*--- Condicional --- */

		evalContadorClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorClave
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE");
		Steps.put(evalContadorClave.GetId(), evalContadorClave);

		evalContadorClaveVacia = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalContadorClaveVacia
				.setStepDescription("CONDITIONAL => INTENTOS INGRESO DE CLAVE VACIA");
		Steps.put(evalContadorClaveVacia.GetId(), evalContadorClaveVacia);

		evalFechaDeCaducidadClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalFechaDeCaducidadClave
				.setStepDescription("CONDITIONAL => FECHA DE CADUCIDAD CLAVE");
		Steps.put(evalFechaDeCaducidadClave.GetId(), evalFechaDeCaducidadClave);

		evalNuevoUsuario = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalNuevoUsuario.setStepDescription("CONDITIONAL => USUARIO NUEVO");
		Steps.put(evalNuevoUsuario.GetId(), evalNuevoUsuario);

		evalUsuarioConClaveBloq = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalUsuarioConClaveBloq
				.setStepDescription("CONDITIONAL => USUARIO CON CLAVE BLOQUEADA");
		Steps.put(evalUsuarioConClaveBloq.GetId(), evalUsuarioConClaveBloq);

		evalCambioClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCambioClave
				.setStepDescription("CONDITIONAL => USUARIO QUIERE CAMBIAR LA CLAVE");
		Steps.put(evalCambioClave.GetId(), evalCambioClave);

		evalUsuarioConClaveExpirada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalUsuarioConClaveExpirada
				.setStepDescription("CONDITIONAL => USUARIO CON CLAVE ERRONEA");
		Steps.put(evalUsuarioConClaveExpirada.GetId(),
				evalUsuarioConClaveExpirada);

		evalClaveBloqueada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveBloqueada.setStepDescription("CONDITIONAL => CLAVE BLOQUEADA");
		Steps.put(evalClaveBloqueada.GetId(), evalClaveBloqueada);

		evalClaveExpirada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveExpirada.setStepDescription("CONDITIONAL => CLAVE EXPIRADA");
		Steps.put(evalClaveExpirada.GetId(), evalClaveExpirada);

		evalClaveErronea = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveErronea.setStepDescription("CONDITIONAL => CLAVE ERRONEA");
		Steps.put(evalClaveErronea.GetId(), evalClaveErronea);

		evalClaveOK = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveOK.setStepDescription("CONDITIONAL => CLAVE OK");
		Steps.put(evalClaveOK.GetId(), evalClaveOK);

		/*--- Menu --- */

		stepMenuConfirmacionMenu = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConfirmacionMenu
				.setStepDescription("MENU => INGRESO RUTINA GENERACION CLAVE");
		stepMenuConfirmacionMenu.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveContextVar"));
		Steps.put(stepMenuConfirmacionMenu.GetId(), stepMenuConfirmacionMenu);

		stepMenuCambiarClave = (StepMenu) StepFactory.createStep(StepType.Menu,
				UUID.randomUUID());
		stepMenuCambiarClave
				.setStepDescription("MENU =>  CAMBIO CLAVE POR EXPIRACION");
		stepMenuCambiarClave.setContextVariableName(ctxVar
				.getContextVarByName("cambiarClaveContextVar"));
		Steps.put(stepMenuCambiarClave.GetId(), stepMenuCambiarClave);

		/*--- Derivo --- */

		stepCancelDerivo = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepCancelDerivo
				.setStepDescription("SETASTERISKVARIABLE => SETEA VARIABLE CANCEL DERIVO");
		stepCancelDerivo.setContextVariableName(ctxVar
				.getContextVarByName("derivoContextVar"));
		stepCancelDerivo.setVariableName("DESVIO");
		Steps.put(stepCancelDerivo.GetId(), stepCancelDerivo);

		stepDerivoMenu3 = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		stepDerivoMenu3
				.setStepDescription("SETASTERISKVARIABLE => SETEA VARIABLE SALTO MENU 3");
		stepDerivoMenu3.setContextVariableName(ctxVar
				.getContextVarByName("menuPrincipalTresContextVar"));
		stepDerivoMenu3.setVariableName("MENUREP");
		Steps.put(stepDerivoMenu3.GetId(), stepDerivoMenu3);

		stepDerivoAlMenuPrincipal = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoAlMenuPrincipal.setApp("goto");
		stepDerivoAlMenuPrincipal.setAppOptions(Daemon
				.getConfig("DERIVOMENUPRINCIPAL"));
		stepDerivoAlMenuPrincipal
				.setStepDescription("EXECUTE =>  DERIVO MENU INICIAL");
		Steps.put(stepDerivoAlMenuPrincipal.GetId(), stepDerivoAlMenuPrincipal);

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");
		Steps.put(stepAudioFinal.GetId(), stepAudioFinal);

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN DE COMUNICACION");
		Steps.put(pasoFinal.GetId(), pasoFinal);
	}
}
