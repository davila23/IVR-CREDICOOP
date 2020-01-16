package step.group;

import ivr.CallContext;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import main.Daemon;
import condition.condition;
import step.StepConditional;
import step.StepCounter;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepMenu;
import step.StepPasswordChange;
import step.StepPlay;
import step.StepPlayRead;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class GeneracionDeClave implements StepGroup {

	protected StepGroupType GroupType;
	private int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private StepUserAuthentication autentificarClave;
	private PideFechaCredicoop pideFecha;
	private PideTarjetaCredicoop pideTarjeta;
	private StepPasswordChange cambioClave;
	private StepIsOwnCard esTarjetaPropia;
	private StepPlay stepAudioClaveIncorrecta;
	private StepPlay stepAudioClaveInvalida;
	private StepPlay stepAudioClavesNoCoinciden;
	private StepPlay stepAudioRequisitos;
	private StepPlay stepAudioClaveBloqueada;
	private StepPlay stepAudioClaveGenerada;
	private StepPlay stepAudioVerifiqueDatos;
	private StepPlay stepAudioClaveUsada;
	private StepPlay stepAudioDatosIncorrectos;
	private StepPlayRead stepAudioIngresoRutinaGenClave;
	private StepPlayRead stepAudioClave;
	private StepPlayRead stepAudioConfirmacionClave;
	private StepCounter contadorIntentosClave;
	private StepCounter contadorIntentosClaveVacia;
	private StepExecute stepDerivoAlMenuPrincipal;
	private StepMenu stepMenuRutinaGenClave;
	private StepConditional evalClaveNoUsada;
	private StepConditional evalClaveVacia;
	private StepConditional evalCambioOk;
	private StepConditional evalContadorClave;
	private StepConditional evalContadorClaveVacia;
	private StepConditional evalFechaDeCaducidadClave;
	private StepConditional evalClaveCorrecta;
	private StepConditional evalFormatoClaveOK;
	private StepConditional evalCambioClaveOk;
	private StepConditional evalCambioClaveUsada;
	private StepConditional evalCoincidenFechas;
	private StepConditional evalConfirmacionClave;

	private void setSequence() {

		/* Menu inicio Rutina */

		stepAudioIngresoRutinaGenClave.setNextstep(stepMenuRutinaGenClave
				.GetId());
		stepMenuRutinaGenClave.addSteps("1", stepAudioRequisitos.GetId());
		stepMenuRutinaGenClave.addSteps("2", stepIfFalseUUID);
		stepMenuRutinaGenClave.setInvalidOption(stepAudioIngresoRutinaGenClave
				.GetId());

		/* Ingreso Datos */

		pideTarjeta.setStepIfTrue(pideFecha.getInitialStep());
		pideTarjeta.setStepIfFalse(stepIfFalseUUID);

		pideFecha.setStepIfTrue(esTarjetaPropia.GetId());
		pideFecha.setStepIfFalse(stepIfFalseUUID);

		/* Valido Datos */

		esTarjetaPropia.setNextStepIsTrue(stepAudioClave.GetId()); /* - +- */
		esTarjetaPropia.setNextStepIsFalse(stepAudioDatosIncorrectos.GetId());

		/* Comparo ingreso fecha con SoapFdn (fecha Crecer) */

		evalCoincidenFechas.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retAutentificacionContextVar")
						.getVarName() + "} == " + 0, stepAudioClave.GetId(),
				stepAudioDatosIncorrectos.GetId()));

		/* Datos Incorrectos */

		stepAudioDatosIncorrectos.setNextstep(pideTarjeta.getInitialStep());

		/* Datos Correctos => Pido Nueva clave */

		stepAudioClave.setNextstep(evalClaveVacia.GetId());

		evalClaveVacia.addCondition(new condition(1, "equals('#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "}','')", contadorIntentosClaveVacia.GetId(),
				stepAudioConfirmacionClave.GetId()));

		contadorIntentosClaveVacia.setNextstep(evalContadorClaveVacia.GetId());

		evalContadorClaveVacia.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveVaciaGenContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepDerivoAlMenuPrincipal.GetId()));

		// autentificarClave.setNextstep(evalFormatoClaveOK.GetId());
		//
		// evalFormatoClaveOK.addCondition(new condition(1, "#{"
		// + ctxVar.getContextVarByName("retAutentificacionContextVar")
		// .getVarName() + "} == " + 0, cambioClave.GetId(),
		// stepAudioClaveInvalida.GetId()));

		cambioClave.setNextstep(evalCambioClaveUsada.GetId());
		/* Nueva Clave, Invalida (Numero Consecutivo o Iguales) */

		evalCambioClaveUsada.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retCambioClaveContextVar")
						.getVarName() + "} == " + 2, stepAudioClaveUsada
				.GetId(), evalCambioClaveOk.GetId()));

		evalCambioClaveOk.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("retCambioClaveContextVar")
						.getVarName() + "} == " + 0, stepAudioClaveGenerada
				.GetId(), stepAudioClaveInvalida.GetId()));

		stepAudioConfirmacionClave.setNextstep(evalConfirmacionClave.GetId());

		evalConfirmacionClave.addCondition((new condition(1, "#{"
				+ ctxVar.getContextVarByName("claveContextVar").getVarName()
				+ "} == #{"
				+ ctxVar.getContextVarByName("confirmacionClaveContextVar")
						.getVarName() + "}", cambioClave.GetId(),
				stepAudioClavesNoCoinciden.GetId())));

		stepAudioClaveInvalida.setNextstep(contadorIntentosClave.GetId());

		contadorIntentosClave.setNextstep(evalContadorClave.GetId());
		evalContadorClave.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("intentosClaveContextVar")
						.getVarName() + "} < " + intentos, stepAudioClave
				.GetId(), stepIfFalseUUID));

		/* Nueva Clave Valida */

		/* changePasswdReturn => Ok = 0 , Fail = 1 , InHistory = 2 */
		stepAudioRequisitos.setNextstep(pideTarjeta.getInitialStep());
		stepAudioClavesNoCoinciden.setNextstep(contadorIntentosClave.GetId());
		stepAudioClaveUsada.setNextstep(contadorIntentosClave.GetId());
		stepAudioClaveGenerada.setNextstep(stepIfTrueUUID);

	}

	@Override
	public ConcurrentHashMap<UUID, Task> getSteps() {

		// if (claveContextVar == null || intentosClaveContextVar == null) {
		// throw new IllegalArgumentException("Variables de Contexto Vacias");
		// }
		if (stepIfFalseUUID == null || stepIfFalseUUID == null) {
			throw new IllegalArgumentException(
					"Pasos verdadero o falso , vacios");
		}
		this.setSequence();
		return Steps;
	}

	@Override
	public UUID getInitialStep() {
		return stepAudioIngresoRutinaGenClave.GetId();
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

	public GeneracionDeClave() {
		super();

		GroupType = StepGroupType.generacionDeClave;
	}

	private void createSteps() {
		/*--- Play Read --- */

		stepAudioIngresoRutinaGenClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioIngresoRutinaGenClave
				.setStepDescription("PLAYREAD => INGRESO RUTINA GENERACION");
		stepAudioIngresoRutinaGenClave.setPlayMaxDigits(1);
		stepAudioIngresoRutinaGenClave.setContextVariableName(ctxVar
				.getContextVarByName("genClaveContextVar"));
		stepAudioIngresoRutinaGenClave.setPlayTimeout(2000L);
		stepAudioIngresoRutinaGenClave.setPlayFile("GENERACION/001");
		Steps.put(stepAudioIngresoRutinaGenClave.GetId(),
				stepAudioIngresoRutinaGenClave);

		stepAudioClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioClave.setStepDescription("PLAYREAD => INGRESO NUEVA CLAVE");
		stepAudioClave.setPlayMaxDigits(4);
		stepAudioClave.setContextVariableName(ctxVar
				.getContextVarByName("claveContextVar"));
		stepAudioClave.setPlayTimeout(4000L);
		stepAudioClave.setPlayFile("GENERACION/003");
		Steps.put(stepAudioClave.GetId(), stepAudioClave);

		stepAudioConfirmacionClave = (StepPlayRead) StepFactory.createStep(
				StepType.PlayRead, UUID.randomUUID());
		stepAudioConfirmacionClave
				.setStepDescription("PLAYREAD => INGRESO CLAVE PARA CONFIRMACION");
		stepAudioConfirmacionClave.setPlayMaxDigits(4);
		stepAudioConfirmacionClave.setContextVariableName(ctxVar
				.getContextVarByName("confirmacionClaveContextVar"));
		stepAudioConfirmacionClave.setPlayTimeout(4000L);
		stepAudioConfirmacionClave.setPlayFile("GENERACION/007");
		Steps.put(stepAudioConfirmacionClave.GetId(),
				stepAudioConfirmacionClave);

		/*--- Contadores --- */

		contadorIntentosClave = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClave
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE");
		contadorIntentosClave.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveContextVar"));
		Steps.put(contadorIntentosClave.GetId(), contadorIntentosClave);

		contadorIntentosClaveVacia = (StepCounter) StepFactory.createStep(
				StepType.Counter, UUID.randomUUID());
		contadorIntentosClaveVacia
				.setStepDescription("COUNTER => INCREMENTA INTENTOS DE INGRESO CLAVE VACIA");
		contadorIntentosClaveVacia.setContextVariableName(ctxVar
				.getContextVarByName("intentosClaveVaciaGenContextVar"));
		Steps.put(contadorIntentosClaveVacia.GetId(),
				contadorIntentosClaveVacia);

		/*--- Audios --- */

		stepAudioClaveUsada = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioClaveUsada.setStepDescription("PLAY => CLAVE YA USADA");
		stepAudioClaveUsada.setPlayfile("GENERACION/005");
		Steps.put(stepAudioClaveUsada.GetId(), stepAudioClaveUsada);

		stepAudioClaveBloqueada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveBloqueada.setStepDescription("PLAY => CLAVE BLOQUEADA");
		stepAudioClaveBloqueada.setPlayfile("GENERACION/010");
		Steps.put(stepAudioClaveBloqueada.GetId(), stepAudioClaveBloqueada);

		stepAudioDatosIncorrectos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDatosIncorrectos
				.setStepDescription("PLAY => DATOS INCORRECTOS");
		stepAudioDatosIncorrectos.setPlayfile("GENERACION/005");
		Steps.put(stepAudioDatosIncorrectos.GetId(), stepAudioDatosIncorrectos);

		stepAudioClaveGenerada = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveGenerada
				.setStepDescription("PLAY => CLAVE CORRECTAMENTE GENERADA");
		stepAudioClaveGenerada.setPlayfile("GENERACION/004");
		Steps.put(stepAudioClaveGenerada.GetId(), stepAudioClaveGenerada);

		stepAudioClaveIncorrecta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveIncorrecta.setStepDescription("PLAY => CLAVE INCORRECTA");
		stepAudioClaveIncorrecta.setPlayfile("GENERACION/005");
		Steps.put(stepAudioClaveIncorrecta.GetId(), stepAudioClaveIncorrecta);

		stepAudioClavesNoCoinciden = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClavesNoCoinciden
				.setStepDescription("PLAY => CLAVES NO COINCIDEN ");
		stepAudioClavesNoCoinciden.setPlayfile("GENERACION/008");
		Steps.put(stepAudioClavesNoCoinciden.GetId(),
				stepAudioClavesNoCoinciden);

		stepAudioVerifiqueDatos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioVerifiqueDatos.setStepDescription("PLAY => VERIFIQUE DATOS");
		stepAudioVerifiqueDatos.setPlayfile("GENERACION/006");
		Steps.put(stepAudioVerifiqueDatos.GetId(), stepAudioVerifiqueDatos);

		// stepAudioSuClaveEs = (StepPlay) StepFactory.createStep(StepType.Play,
		// UUID.randomUUID());
		// stepAudioSuClaveEs.setStepDescription("Audio su DNI es");
		// Steps.put(stepAudioSuClaveEs.GetId(), stepAudioSuClaveEs);

		stepAudioClaveInvalida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioClaveInvalida.setStepDescription("PLAY => CLAVE INVALIDA");
		stepAudioClaveInvalida.setPlayfile("GENERACION/005");
		Steps.put(stepAudioClaveInvalida.GetId(), stepAudioClaveInvalida);

		stepAudioRequisitos = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioRequisitos.setStepDescription("PLAY => REQUISITOS CLAVE");
		stepAudioRequisitos.setPlayfile("GENERACION/002");
		Steps.put(stepAudioRequisitos.GetId(), stepAudioRequisitos);

		/*--- Validaciones --- */

		autentificarClave = (StepUserAuthentication) StepFactory.createStep(
				StepType.AutentificarClave, UUID.randomUUID());
		autentificarClave
				.setStepDescription("USERAUTHENTICATION => AUTENTICA CLAVE INGRESADA");
		autentificarClave.setCantDiasExpiracioncontextVar(ctxVar
				.getContextVarByName("cantDiasExpiracioncontextVar"));
		autentificarClave.setClaveContextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		autentificarClave.setIdCrecerContextVar(ctxVar
				.getContextVarByName("retAutentificacionContextVar"));
		autentificarClave.setRetAuthPasswordcontextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		Steps.put(autentificarClave.GetId(), autentificarClave);

		esTarjetaPropia = (StepIsOwnCard) StepFactory.createStep(
				StepType.IsOwnCard, UUID.randomUUID());
		esTarjetaPropia.setTarjetaContextVariableName(ctxVar
				.getContextVarByName("tarjetaContexVar"));
		esTarjetaPropia.setIdCrecerContextVariableName(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		esTarjetaPropia
				.setStepDescription("ISOWNCARD => VERIFICA QUE LA TARJETA INGRESADA SEA PROPIA");
		Steps.put(esTarjetaPropia.GetId(), esTarjetaPropia);

		/*--- Cambio  De clave --- */

		cambioClave = (StepPasswordChange) StepFactory.createStep(
				StepType.CambiaClave, UUID.randomUUID());
		cambioClave.setStepDescription("PASSWORDCHANGE => CAMBIO CLAVE");
		cambioClave.setIdCrecerContextVar(ctxVar
				.getContextVarByName("idCrecerContextVar"));
		cambioClave.setNewPasswordContextVar(ctxVar
				.getContextVarByName("claveContextVar"));
		cambioClave.setPasswordChangedStatusContextVar(ctxVar
				.getContextVarByName("retCambioClaveContextVar"));
		cambioClave
				.setFdnContextVar(ctxVar.getContextVarByName("fdnContexVar"));
		Steps.put(cambioClave.GetId(), cambioClave);

		/*--- Condicional --- */

		evalClaveVacia = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveVacia.setStepDescription("CONDITIONAL => CLAVE VACIA");
		Steps.put(evalClaveVacia.GetId(), evalClaveVacia);

		evalClaveNoUsada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveNoUsada.setStepDescription("CONDITIONAL => CLAVE NO USADA");
		Steps.put(evalClaveNoUsada.GetId(), evalClaveNoUsada);

		evalCambioOk = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCambioOk.setStepDescription("CONDITIONAL => CAMBIO CLAVE OK");
		Steps.put(evalCambioOk.GetId(), evalCambioOk);

		evalConfirmacionClave = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalConfirmacionClave
				.setStepDescription("CONDITIONAL => CLAVE Y CONFIRMACION IGUALES");
		Steps.put(evalConfirmacionClave.GetId(), evalConfirmacionClave);

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
				.setStepDescription("CONDITIONAL => FECHA CADUCIDAD CLAVE");
		Steps.put(evalFechaDeCaducidadClave.GetId(), evalFechaDeCaducidadClave);

		evalClaveCorrecta = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalClaveCorrecta.setStepDescription("CONDITIONAL => CLAVE CORRECTA");
		Steps.put(evalClaveCorrecta.GetId(), evalClaveCorrecta);

		evalFormatoClaveOK = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalFormatoClaveOK
				.setStepDescription("CONDITIONAL => FORMATO CLAVE OK");
		Steps.put(evalFormatoClaveOK.GetId(), evalFormatoClaveOK);

		evalCambioClaveOk = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCambioClaveOk.setStepDescription("CONDITIONAL => CAMBIO CLAVE OK");
		Steps.put(evalCambioClaveOk.GetId(), evalCambioClaveOk);

		evalCambioClaveUsada = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCambioClaveUsada
				.setStepDescription("CONDITIONAL => CLAVE NO USADA");
		Steps.put(evalCambioClaveUsada.GetId(), evalCambioClaveUsada);

		evalCoincidenFechas = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalCoincidenFechas
				.setStepDescription("CONDITIONAL => FECHA INGRESADA == SOAPFDN");
		Steps.put(evalCoincidenFechas.GetId(), evalCoincidenFechas);

		/*--- Menu --- */

		stepMenuRutinaGenClave = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuRutinaGenClave
				.setStepDescription("MENU => INGRESO RUTINA GENERACION CLAVE");
		stepMenuRutinaGenClave.setContextVariableName(ctxVar
				.getContextVarByName("genClaveContextVar"));
		Steps.put(stepMenuRutinaGenClave.GetId(), stepMenuRutinaGenClave);

		/*--- Derivo --- */

		stepDerivoAlMenuPrincipal = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoAlMenuPrincipal.setApp("goto");
		stepDerivoAlMenuPrincipal.setAppOptions(Daemon
				.getConfig("DERIVOMENUPRINCIPAL"));
		stepDerivoAlMenuPrincipal
				.setStepDescription("EXECUTE => DERIVO MENU PRINCIPAL");
		Steps.put(stepDerivoAlMenuPrincipal.GetId(), stepDerivoAlMenuPrincipal);

		/*--- Ingreso de Datos --- */

		pideTarjeta = (PideTarjetaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjetaCredicoop);
		pideTarjeta.setAudioTarjeta("RUTINAPINCOP/RUTINA_PIN003");
		pideTarjeta.setAudioTarjetaInvalido("RUTINAPINCOP/RUTINA_PIN023");
		pideTarjeta.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideTarjeta.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideTarjeta.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));
		pideTarjeta.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContexVar"));

		pideFecha = (PideFechaCredicoop) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideFechaCredicoop);
		pideFecha.setAudioFecha("RUTINAPINCOP/RUTINA_PIN010");
		pideFecha.setAudioDigInsuficientes("RUTINAPINCOP/RUTINA_PIN012");
		pideFecha.setAudioValidateFecha("RUTINAPINCOP/RUTINA_PIN009");
		pideFecha.setAudioSuFechaEs("RUTINAPINCOP/RUTINA_PIN015");
		pideFecha.setAudioAnio("RUTINAPINCOP/RUTINA_PIN008");
		pideFecha.setAudioMes("RUTINAPINCOP/RUTINA_PIN007");
		pideFecha.setAudioDia("RUTINAPINCOP/RUTINA_PIN006");
		pideFecha.setAudioFechaInvalida("RUTINAPINCOP/RUTINA_PIN013");
		pideFecha.setAudioLaFechaNoCoincide("RUTINAPINCOP/RUTINA_PIN041");
		pideFecha.setfechaContextVar(ctxVar
				.getContextVarByName("fechaContextVar"));
		pideFecha.setFdnContextVar(ctxVar.getContextVarByName("fdnContexVar"));
		pideFecha.setContextVarDia(ctxVar.getContextVarByName("contextVarDia"));
		pideFecha.setContextVarMes(ctxVar.getContextVarByName("contextVarMes"));
		pideFecha.setContextVarAnio(ctxVar
				.getContextVarByName("contextVarAnio"));
		pideFecha.setConfirmaFechaContextVar(ctxVar
				.getContextVarByName("confirmaFechaContextVar"));
		pideFecha.setIntentosFechaContextVar(ctxVar
				.getContextVarByName("intentosFechaContextVar"));

	}

	public PideFechaCredicoop getPideFecha() {
		return pideFecha;
	}

	public PideTarjetaCredicoop getPideTarjeta() {
		return pideTarjeta;
	}

}
