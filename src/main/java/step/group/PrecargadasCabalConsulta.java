package step.group;

import ivr.CallContext;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import auth.AuthConnector;
import main.Daemon;
import condition.condition;
import context.ContextVar;
import step.Step;
import step.StepAuthInitialInfo;
import step.StepConditional;
import step.StepContinueOnDialPlan;
import step.StepCounter;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepIsOwnCard;
import step.StepLimitPlayRead;
import step.StepMenu;
import step.StepPlay;
import step.StepPlayRead;
import step.StepSayMonth;
import step.StepSayNumber;
import step.StepSendJPOS;
import step.StepSwitch;
import step.StepTimeConditionDB;
import step.StepUserAuthentication;
import step.StepFactory.StepType;
import step.group.StepGroupFactory.StepGroupType;
import workflow.Task;

public class PrecargadasCabalConsulta implements StepGroup {

	protected StepGroupType GroupType;
	protected int intentos = 3;
	private UUID stepIfTrueUUID;
	private UUID stepIfFalseUUID;
	private CallContext ctxVar;
	private StepSendJPOS enviaTramaJpos;
	private StepSwitch evalRetJPOS;
	private PideTarjeta pideTarjetaGrp;
	private StepTimeConditionDB obtieneHorario;
	private StepPlay stepAudioServNoDisponible;
	private StepPlay stepAudioNroTarjVencida;
	private StepPlay stepAudioNroTarjIncorrecto;
	private StepPlay stepAudioConsumosDisponibles;
	private StepLimitPlayRead stepAudioMenuConsumosDisponibles;
	private StepSayNumber stepNumberConsumosDisponibles;
	private StepPlay stepAudioSuperoIntentos;
	private StepEnd pasoFinal;
	private StepPlay stepAudioIngresoIncorrectoConsumosDisponibles;
	private StepMenu stepMenuConsumosDisponibles;
	private StepLimitPlayRead stepAudioMenuUltimosMovimientos;
	private StepMenu stepMenuUltimosMovimientos;
	private StepPlay stepAudioIngresoIncorrectoUltimosMovimientos;
	private StepPlay stepAudioUltimosMovimientos;
	private StepConditional evalDisponibleAdelantos;
	private StepPlay stepAudioDisponibleAdelantos;
	private StepSayNumber stepNumberDisponibleAdelantos;
	private StepPlay stepAudioPrevioDerivoAsesor;
	private StepPlay stepAudioverifiqueNumeroTarjeta;
	private StepExecute stepDerivoLlamada;
	private StepPlay stepAudioDisuadeDerivoAsesor;
	private StepMenu stepMenuMovimientosAnteriores;
	private StepLimitPlayRead stepAudioMenuMovimientosAnteriores;
	private StepPlay stepAudioIngresoIncorrectoMovimientosAnteriores;
	private StepPlay stepAudioFechaUltimoCarga;
	private StepPlay stepAudioMontoUltimoCarga;
	private StepSayMonth stepFechaUltimoCarga;
	private StepSayNumber stepNumberMontoUltimoCarga;
	private StepPlay stepAudioFechaUltimoMovimiento_1;
	private StepPlay stepAudioMontoUltimoMovimiento_1;
	private StepSayMonth stepFechaUltimoMovimiento_1;
	private StepSayNumber stepNumberMontoUltimoMovimiento_1;
	private StepPlay stepAudioFechaUltimoMovimiento_2;
	private StepPlay stepAudioMontoUltimoMovimiento_2;
	private StepSayMonth stepFechaUltimoMovimiento_2;
	private StepSayNumber stepNumberMontoUltimoMovimiento_2;
	private StepPlay stepAudioFechaUltimoMovimiento_3;
	private StepPlay stepAudioMontoUltimoMovimiento_3;
	private StepSayMonth stepFechaUltimoMovimiento_3;
	private StepSayNumber stepNumberMontoUltimoMovimiento_3;
	private StepPlay stepAudioFechaMovimientosAnteriores_1;
	private StepPlay stepAudioMontoMovimientosAnteriores_1;
	private StepSayMonth stepFechaMovimientosAnteriores_1;
	private StepSayNumber stepNumberMontoMovimientosAnteriores_1;
	private StepPlay stepAudioFechaMovimientosAnteriores_2;
	private StepPlay stepAudioMontoMovimientosAnteriores_2;
	private StepSayMonth stepFechaMovimientosAnteriores_2;
	private StepSayNumber stepNumberMontoMovimientosAnteriores_2;
	private StepPlay stepAudioFechaMovimientosAnteriores_3;
	private StepPlay stepAudioMontoMovimientosAnteriores_3;
	private StepSayMonth stepFechaMovimientosAnteriores_3;
	private StepSayNumber stepNumberMontoMovimientosAnteriores_3;
	private StepPlay stepAudioMovimientosAnteriores;

	private void setSequence() {

		/* Consultas Precargada */

		pideTarjetaGrp.setStepIfTrue(enviaTramaJpos.GetId());
		pideTarjetaGrp.setStepIfFalse(stepAudioverifiqueNumeroTarjeta.GetId());

		stepAudioverifiqueNumeroTarjeta.setNextstep(stepIfFalseUUID);

		/* Retorno Jpos 99 y 96 */

		stepAudioNroTarjIncorrecto.setNextstep(pideTarjetaGrp.getInitialStep());
		stepAudioNroTarjVencida.setNextstep(pideTarjetaGrp.getInitialStep());

		/* Retorno Jpos 00 */

		stepAudioConsumosDisponibles.setNextstep(stepNumberConsumosDisponibles
				.GetId());
		stepNumberConsumosDisponibles.setNextstep(evalDisponibleAdelantos
				.GetId());

		/* Adelantos > 10 */

		evalDisponibleAdelantos.addCondition(new condition(1, "#{"
				+ ctxVar.getContextVarByName("adelantosContextVar")
						.getVarName() + "} > " + 10,
				stepAudioDisponibleAdelantos.GetId(), stepAudioFechaUltimoCarga
						.GetId()));

		stepAudioDisponibleAdelantos.setNextstep(stepNumberDisponibleAdelantos
				.GetId());
		stepNumberDisponibleAdelantos.setNextstep(stepAudioFechaUltimoCarga
				.GetId());

		/* Continua Callflow */

		stepAudioFechaUltimoCarga.setNextstep(stepFechaUltimoCarga.GetId());
		stepFechaUltimoCarga.setNextstep(stepAudioMontoUltimoCarga.GetId());
		stepAudioMontoUltimoCarga.setNextstep(stepNumberMontoUltimoCarga
				.GetId());
		stepNumberMontoUltimoCarga.setNextstep(stepAudioMenuConsumosDisponibles
				.GetId());

		stepAudioMenuConsumosDisponibles
				.setNextstep(stepMenuConsumosDisponibles.GetId());
		stepAudioMenuConsumosDisponibles
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuConsumosDisponibles.addSteps("1",
				stepAudioConsumosDisponibles.GetId());
		stepMenuConsumosDisponibles.addSteps("2",
				stepAudioUltimosMovimientos.GetId());
		stepMenuConsumosDisponibles.addSteps("9", stepIfTrueUUID);
		stepMenuConsumosDisponibles
				.setInvalidOption(stepAudioIngresoIncorrectoConsumosDisponibles
						.GetId());

		stepAudioIngresoIncorrectoConsumosDisponibles
				.setNextstep(stepAudioMenuConsumosDisponibles.GetId());

		/* Ultimos 3 Movimientos */

		stepAudioUltimosMovimientos
				.setNextstep(stepAudioFechaUltimoMovimiento_1.GetId());

		stepAudioFechaUltimoMovimiento_1
				.setNextstep(stepFechaUltimoMovimiento_1.GetId());
		stepFechaUltimoMovimiento_1
				.setNextstep(stepAudioMontoUltimoMovimiento_1.GetId());
		stepAudioMontoUltimoMovimiento_1
				.setNextstep(stepNumberMontoUltimoMovimiento_1.GetId());
		stepNumberMontoUltimoMovimiento_1
				.setNextstep(stepAudioFechaUltimoMovimiento_2.GetId());

		stepAudioFechaUltimoMovimiento_2
				.setNextstep(stepFechaUltimoMovimiento_2.GetId());
		stepFechaUltimoMovimiento_2
				.setNextstep(stepAudioMontoUltimoMovimiento_2.GetId());
		stepAudioMontoUltimoMovimiento_2
				.setNextstep(stepNumberMontoUltimoMovimiento_2.GetId());
		stepNumberMontoUltimoMovimiento_2
				.setNextstep(stepAudioFechaUltimoMovimiento_3.GetId());

		stepAudioFechaUltimoMovimiento_3
				.setNextstep(stepFechaUltimoMovimiento_3.GetId());
		stepFechaUltimoMovimiento_3
				.setNextstep(stepAudioMontoUltimoMovimiento_3.GetId());
		stepAudioMontoUltimoMovimiento_3
				.setNextstep(stepNumberMontoUltimoMovimiento_3.GetId());
		stepNumberMontoUltimoMovimiento_3
				.setNextstep(stepAudioMenuUltimosMovimientos.GetId());

		/* Opcion 2 => Menu Ultimos Movimientos */

		stepAudioMenuUltimosMovimientos.setNextstep(stepMenuUltimosMovimientos
				.GetId());
		stepAudioMenuUltimosMovimientos
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuUltimosMovimientos.addSteps("1",
				stepAudioUltimosMovimientos.GetId());
		stepMenuUltimosMovimientos.addSteps("2",
				stepAudioMenuUltimosMovimientos.GetId());
		stepMenuUltimosMovimientos.addSteps("3", obtieneHorario.GetId());
		stepMenuUltimosMovimientos.addSteps("9", stepIfTrueUUID);
		stepMenuUltimosMovimientos
				.setInvalidOption(stepAudioIngresoIncorrectoUltimosMovimientos
						.GetId());

		stepAudioIngresoIncorrectoUltimosMovimientos
				.setNextstep(stepAudioMenuUltimosMovimientos.GetId());

		/* Ultimos 3 Movimientos Anteriores */

		stepAudioMovimientosAnteriores
				.setNextstep(stepAudioFechaMovimientosAnteriores_1.GetId());

		stepAudioFechaMovimientosAnteriores_1
				.setNextstep(stepFechaMovimientosAnteriores_1.GetId());
		stepFechaMovimientosAnteriores_1
				.setNextstep(stepAudioMontoMovimientosAnteriores_1.GetId());
		stepAudioMontoMovimientosAnteriores_1
				.setNextstep(stepNumberMontoMovimientosAnteriores_1.GetId());
		stepNumberMontoMovimientosAnteriores_1
				.setNextstep(stepAudioFechaMovimientosAnteriores_2.GetId());

		stepAudioFechaMovimientosAnteriores_2
				.setNextstep(stepFechaMovimientosAnteriores_2.GetId());
		stepFechaMovimientosAnteriores_2
				.setNextstep(stepAudioMontoMovimientosAnteriores_2.GetId());
		stepAudioMontoMovimientosAnteriores_2
				.setNextstep(stepNumberMontoMovimientosAnteriores_2.GetId());
		stepNumberMontoMovimientosAnteriores_2
				.setNextstep(stepAudioFechaMovimientosAnteriores_3.GetId());

		stepAudioFechaMovimientosAnteriores_3
				.setNextstep(stepFechaMovimientosAnteriores_3.GetId());
		stepFechaMovimientosAnteriores_3
				.setNextstep(stepAudioMontoMovimientosAnteriores_3.GetId());
		stepAudioMontoMovimientosAnteriores_3
				.setNextstep(stepNumberMontoMovimientosAnteriores_3.GetId());
		stepNumberMontoMovimientosAnteriores_3
				.setNextstep(stepAudioMenuUltimosMovimientos.GetId());

		/* Opcion 2 => Menu Movimientos Anteriores */

		stepAudioMenuMovimientosAnteriores
				.setNextstep(stepMenuMovimientosAnteriores.GetId());
		stepAudioMenuMovimientosAnteriores
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuMovimientosAnteriores.addSteps("1",
				stepAudioMovimientosAnteriores.GetId());
		stepMenuMovimientosAnteriores.addSteps("9", stepIfTrueUUID);
		stepMenuMovimientosAnteriores
				.setInvalidOption(stepAudioIngresoIncorrectoUltimosMovimientos
						.GetId());

		stepAudioIngresoIncorrectoMovimientosAnteriores
				.setNextstep(stepAudioMenuMovimientosAnteriores.GetId());

		/* Secuencias comunes */

		enviaTramaJpos.setNextstep(evalRetJPOS.GetId());

		stepAudioSuperoIntentos.setNextstep(stepIfFalseUUID);

		obtieneHorario.setNextStepIsTrue(stepAudioDisuadeDerivoAsesor.GetId());
		obtieneHorario.setNextStepIsFalse(stepAudioPrevioDerivoAsesor.GetId());

		stepAudioPrevioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
		stepAudioDisuadeDerivoAsesor.setNextstep(stepIfFalseUUID);
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

	private void evalRetJPOS() {

		/*-------------------------------------------------------------------------------
		 * ret =>  88    ||   "Tarjeta con marca 3"    
		 * ret =>  83    ||   "Causa 08 y tarjeta adicional"
		 * ret =>  81    ||   "Tarjeta bloqueada"
		 * ret =>  99    ||   "Tarjeta inexistente"
		 *    
		-------------------------------------------------------------------------------	*/

		evalRetJPOS.addSwitchValue("99", stepAudioNroTarjIncorrecto.GetId());
		evalRetJPOS.addSwitchValue("98", obtieneHorario.GetId());
		evalRetJPOS.addSwitchValue("96", stepAudioNroTarjVencida.GetId());
		evalRetJPOS.addSwitchValue("00", stepAudioConsumosDisponibles.GetId());
		evalRetJPOS.addSwitchValue("EE", stepAudioServNoDisponible.GetId());

	}

	@Override
	public UUID getInitialStep() {
		return enviaTramaJpos.GetId();
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

	public PrecargadasCabalConsulta() {
		super();

		GroupType = StepGroupType.precargadasCabalConsulta;
	}

	private void createSteps() {
		/* Play */

		stepAudioDisuadeDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisuadeDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioDisuadeDerivoAsesor
				.setStepDescription("PLAY => DISUADE DERIVO ASESOR");
		Steps.put(stepAudioDisuadeDerivoAsesor.GetId(),
				stepAudioDisuadeDerivoAsesor);

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => PREVIO DERIVO ASESOR");
		Steps.put(stepAudioPrevioDerivoAsesor.GetId(),
				stepAudioPrevioDerivoAsesor);

		stepAudioMovimientosAnteriores = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioMovimientosAnteriores.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMovimientosAnteriores
				.setStepDescription("PLAY => MOVIMIENTOS ANTERIORES");
		Steps.put(stepAudioMovimientosAnteriores.GetId(),
				stepAudioMovimientosAnteriores);

		stepAudioFechaMovimientosAnteriores_1 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioFechaMovimientosAnteriores_1
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaMovimientosAnteriores_1
				.setStepDescription("PLAY => FECHA MOVIMIENTOS ANTERIORES - 1 / 3");
		Steps.put(stepAudioFechaMovimientosAnteriores_1.GetId(),
				stepAudioFechaMovimientosAnteriores_1);

		stepAudioFechaMovimientosAnteriores_2 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioFechaMovimientosAnteriores_2
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaMovimientosAnteriores_2
				.setStepDescription("PLAY => FECHA MOVIMIENTOS ANTERIORES - 2 / 3");
		Steps.put(stepAudioFechaMovimientosAnteriores_2.GetId(),
				stepAudioFechaMovimientosAnteriores_2);

		stepAudioFechaMovimientosAnteriores_3 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioFechaMovimientosAnteriores_3
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaMovimientosAnteriores_3
				.setStepDescription("PLAY => FECHA MOVIMIENTOS ANTERIORES - 3 /3");
		Steps.put(stepAudioFechaMovimientosAnteriores_3.GetId(),
				stepAudioFechaMovimientosAnteriores_3);

		stepAudioMontoMovimientosAnteriores_1 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioMontoMovimientosAnteriores_1
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoMovimientosAnteriores_1
				.setStepDescription("PLAY => MONTO MOVIMIENTOS ANTERIORES - 1 / 3");
		Steps.put(stepAudioMontoMovimientosAnteriores_1.GetId(),
				stepAudioMontoMovimientosAnteriores_1);

		stepAudioMontoMovimientosAnteriores_2 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioMontoMovimientosAnteriores_2
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoMovimientosAnteriores_2
				.setStepDescription("PLAY => MONTO MOVIMIENTOS ANTERIORES - 2 / 3");
		Steps.put(stepAudioMontoMovimientosAnteriores_2.GetId(),
				stepAudioMontoMovimientosAnteriores_2);

		stepAudioMontoMovimientosAnteriores_3 = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioMontoMovimientosAnteriores_3
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoMovimientosAnteriores_3
				.setStepDescription("PLAY => MONTO MOVIMIENTOS ANTERIORES - 3 / 3");
		Steps.put(stepAudioMontoMovimientosAnteriores_3.GetId(),
				stepAudioMontoMovimientosAnteriores_3);

		stepAudioUltimosMovimientos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioUltimosMovimientos.setPlayfile("coto/A000013");
		stepAudioUltimosMovimientos
				.setStepDescription("PLAY => ULTIMOS MOVIMIENTOS");
		Steps.put(stepAudioUltimosMovimientos.GetId(),
				stepAudioUltimosMovimientos);

		stepAudioSuperoIntentos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSuperoIntentos.setPlayfile("PREATENDEDORCABAL/032");
		stepAudioSuperoIntentos
				.setStepDescription("PLAY => SUPERO INTENTOS PRECARGADAS");
		Steps.put(stepAudioSuperoIntentos.GetId(), stepAudioSuperoIntentos);

		stepAudioNroTarjVencida = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjVencida.setPlayfile("coto/A000013");
		stepAudioNroTarjVencida
				.setStepDescription("PLAY => NUMERO DE TARJETA VENCIDA. COD : 99");
		Steps.put(stepAudioNroTarjVencida.GetId(), stepAudioNroTarjVencida);

		stepAudioNroTarjIncorrecto = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioNroTarjIncorrecto.setPlayfile("coto/A000012");
		stepAudioNroTarjIncorrecto
				.setStepDescription("PLAY => NUMERO DE TARJETA INCORRECTO. COD : 96");
		Steps.put(stepAudioNroTarjIncorrecto.GetId(),
				stepAudioNroTarjIncorrecto);

		stepAudioverifiqueNumeroTarjeta = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioverifiqueNumeroTarjeta.setPlayfile("coto/A000013");
		stepAudioverifiqueNumeroTarjeta
				.setStepDescription("PLAY => VERIFIQUE NUMERO DE TARJETA");
		Steps.put(stepAudioverifiqueNumeroTarjeta.GetId(),
				stepAudioverifiqueNumeroTarjeta);

		stepAudioConsumosDisponibles = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioConsumosDisponibles.setPlayfile("coto/A000012");
		stepAudioConsumosDisponibles
				.setStepDescription("PLAY => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepAudioConsumosDisponibles.GetId(),
				stepAudioConsumosDisponibles);

		stepAudioServNoDisponible = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioServNoDisponible.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioServNoDisponible
				.setStepDescription("PLAY => SERVICIO NO DISPONIBLE");
		Steps.put(stepAudioServNoDisponible.GetId(), stepAudioServNoDisponible);

		stepAudioDisponibleAdelantos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioDisponibleAdelantos.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioDisponibleAdelantos
				.setStepDescription("PLAY => DISPONIBLE ADELANTOS");
		Steps.put(stepAudioDisponibleAdelantos.GetId(),
				stepAudioDisponibleAdelantos);

		stepAudioIngresoIncorrectoUltimosMovimientos = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoUltimosMovimientos
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoUltimosMovimientos
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoUltimosMovimientos.GetId(),
				stepAudioIngresoIncorrectoUltimosMovimientos);

		stepAudioIngresoIncorrectoMovimientosAnteriores = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMovimientosAnteriores
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoMovimientosAnteriores
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoMovimientosAnteriores.GetId(),
				stepAudioIngresoIncorrectoMovimientosAnteriores);

		stepAudioIngresoIncorrectoConsumosDisponibles = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoConsumosDisponibles
				.setPlayfile("RUTINAPIN/RUTINA_PIN021");
		stepAudioIngresoIncorrectoConsumosDisponibles
				.setStepDescription("PLAY => INGRESO NULO O INCORRECTO");
		Steps.put(stepAudioIngresoIncorrectoConsumosDisponibles.GetId(),
				stepAudioIngresoIncorrectoConsumosDisponibles);

		stepAudioFechaUltimoCarga = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaUltimoCarga.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaUltimoCarga
				.setStepDescription("PLAY => FECHA ULTIMA CARGA");
		Steps.put(stepAudioFechaUltimoCarga.GetId(), stepAudioFechaUltimoCarga);

		stepAudioMontoUltimoCarga = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioMontoUltimoCarga.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoUltimoCarga
				.setStepDescription("PLAY => MONTO ULTIMA CARGA");
		Steps.put(stepAudioMontoUltimoCarga.GetId(), stepAudioMontoUltimoCarga);

		stepAudioFechaUltimoMovimiento_1 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaUltimoMovimiento_1.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaUltimoMovimiento_1
				.setStepDescription("PLAY => FECHA ULTIMOS MOVIMIENTOS - 1 / 3");
		Steps.put(stepAudioFechaUltimoMovimiento_1.GetId(),
				stepAudioFechaUltimoMovimiento_1);

		stepAudioFechaUltimoMovimiento_2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaUltimoMovimiento_2.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaUltimoMovimiento_2
				.setStepDescription("PLAY => FECHA ULTIMOS MOVIMIENTOS - 2 / 3");
		Steps.put(stepAudioFechaUltimoMovimiento_2.GetId(),
				stepAudioFechaUltimoMovimiento_2);

		stepAudioFechaUltimoMovimiento_3 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioFechaUltimoMovimiento_3.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioFechaUltimoMovimiento_3
				.setStepDescription("PLAY => FECHA ULTIMOS MOVIMIENTOS - 3 / 3");
		Steps.put(stepAudioFechaUltimoMovimiento_3.GetId(),
				stepAudioFechaUltimoMovimiento_3);

		stepAudioMontoUltimoMovimiento_1 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioMontoUltimoMovimiento_1.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoUltimoMovimiento_1
				.setStepDescription("PLAY => MONTO ULTIMOS MOVIMIENTOS - 1 / 3");
		Steps.put(stepAudioMontoUltimoMovimiento_1.GetId(),
				stepAudioMontoUltimoMovimiento_1);

		stepAudioMontoUltimoMovimiento_2 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioMontoUltimoMovimiento_2.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoUltimoMovimiento_2
				.setStepDescription("PLAY => MONTO ULTIMOS MOVIMIENTOS - 2 / 3");
		Steps.put(stepAudioMontoUltimoMovimiento_2.GetId(),
				stepAudioMontoUltimoMovimiento_2);

		stepAudioMontoUltimoMovimiento_3 = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioMontoUltimoMovimiento_3.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioMontoUltimoMovimiento_3
				.setStepDescription("PLAY => MONTO ULTIMOS MOVIMIENTOS - 3 / 3");
		Steps.put(stepAudioMontoUltimoMovimiento_3.GetId(),
				stepAudioMontoUltimoMovimiento_3);

		/* Say Month */

		stepFechaUltimoCarga = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaUltimoCarga.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimaCargaContextVar"));
		stepFechaUltimoCarga.setStepDescription("SAYMONTH => ULTIMA CARGA");
		Steps.put(stepFechaUltimoCarga.GetId(), stepFechaUltimoCarga);

		stepFechaUltimoMovimiento_1 = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaUltimoMovimiento_1.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimosMovimientos_1ContextVar"));
		stepFechaUltimoMovimiento_1
				.setStepDescription("SAYMONTH => ULTIMOS MOVIMIENTOS - 1 / 3");
		Steps.put(stepFechaUltimoMovimiento_1.GetId(),
				stepFechaUltimoMovimiento_1);

		stepFechaUltimoMovimiento_2 = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaUltimoMovimiento_2.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimosMovimientos_2ContextVar"));
		stepFechaUltimoMovimiento_2
				.setStepDescription("SAYMONTH => ULTIMOS MOVIMIENTOS - 2 / 3");
		Steps.put(stepFechaUltimoMovimiento_2.GetId(),
				stepFechaUltimoMovimiento_2);

		stepFechaUltimoMovimiento_3 = (StepSayMonth) StepFactory.createStep(
				StepType.SayMonth, UUID.randomUUID());
		stepFechaUltimoMovimiento_3.setContextVariableName(ctxVar
				.getContextVarByName("fechaUltimosMovimientos_3ContextVar"));
		stepFechaUltimoMovimiento_3
				.setStepDescription("SAYMONTH => ULTIMOS MOVIMIENTOS - 3 / 3");
		Steps.put(stepFechaUltimoMovimiento_3.GetId(),
				stepFechaUltimoMovimiento_3);

		stepFechaMovimientosAnteriores_1 = (StepSayMonth) StepFactory
				.createStep(StepType.SayMonth, UUID.randomUUID());
		stepFechaMovimientosAnteriores_1
				.setContextVariableName(ctxVar
						.getContextVarByName("fechaMovimientosAnteriores_1CargaContextVar"));
		stepFechaMovimientosAnteriores_1
				.setStepDescription("SAYMONTH => MOVIMIENTOS ANTERIORES - 1 / 3");
		Steps.put(stepFechaMovimientosAnteriores_1.GetId(),
				stepFechaMovimientosAnteriores_1);

		stepFechaMovimientosAnteriores_2 = (StepSayMonth) StepFactory
				.createStep(StepType.SayMonth, UUID.randomUUID());
		stepFechaMovimientosAnteriores_2
				.setContextVariableName(ctxVar
						.getContextVarByName("fechaMovimientosAnteriores_2CargaContextVar"));
		stepFechaMovimientosAnteriores_2
				.setStepDescription("SAYMONTH => MOVIMIENTOS ANTERIORES - 2 / 3");
		Steps.put(stepFechaMovimientosAnteriores_2.GetId(),
				stepFechaMovimientosAnteriores_2);

		stepFechaMovimientosAnteriores_3 = (StepSayMonth) StepFactory
				.createStep(StepType.SayMonth, UUID.randomUUID());
		stepFechaMovimientosAnteriores_3
				.setContextVariableName(ctxVar
						.getContextVarByName("fechaMovimientosAnteriores_3CargaContextVar"));
		stepFechaMovimientosAnteriores_3
				.setStepDescription("SAYMONTH => MOVIMIENTOS ANTERIORES - 3 / 3");
		Steps.put(stepFechaMovimientosAnteriores_3.GetId(),
				stepFechaMovimientosAnteriores_3);

		/* Limit Play Read */

		stepAudioMenuConsumosDisponibles = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuConsumosDisponibles.setPlayFile("coto/A000013");
		stepAudioMenuConsumosDisponibles.setContextVariableName(ctxVar
				.getContextVarByName("menuConsumosDisponiblesContextVar"));
		stepAudioMenuConsumosDisponibles
				.setStepDescription("LIMITPLAYREAD => MENU CONSUMOS DISPONIBLES");
		Steps.put(stepAudioMenuConsumosDisponibles.GetId(),
				stepAudioMenuConsumosDisponibles);

		stepAudioMenuUltimosMovimientos = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuUltimosMovimientos.setPlayFile("coto/A000013");
		stepAudioMenuUltimosMovimientos.setContextVariableName(ctxVar
				.getContextVarByName("menuUltimosMovimientosContextVar"));
		stepAudioMenuUltimosMovimientos
				.setStepDescription("LIMITPLAYREAD => MENU ULTIMOS MOVIMIENTOS");
		Steps.put(stepAudioMenuUltimosMovimientos.GetId(),
				stepAudioMenuUltimosMovimientos);

		stepAudioMenuMovimientosAnteriores = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuMovimientosAnteriores.setPlayFile("coto/A000013");
		stepAudioMenuUltimosMovimientos.setContextVariableName(ctxVar
				.getContextVarByName("menuMovimientosAnterioresContextVar"));
		stepAudioMenuMovimientosAnteriores
				.setStepDescription("LIMITPLAYREAD => MENU MOVIMIENTOS ANTERIORES");
		Steps.put(stepAudioMenuMovimientosAnteriores.GetId(),
				stepAudioMenuMovimientosAnteriores);

		/* Menu */

		stepMenuConsumosDisponibles = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuConsumosDisponibles
				.setStepDescription("MENU => MENU CONSUMOS DISPONIBLES");
		stepMenuConsumosDisponibles.setContextVariableName(ctxVar
				.getContextVarByName("menuConsumosDisponiblesContextVar"));
		Steps.put(stepMenuConsumosDisponibles.GetId(),
				stepMenuConsumosDisponibles);

		stepMenuUltimosMovimientos = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuUltimosMovimientos
				.setStepDescription("MENU => MENU ULTIMOS MOVIMIENTOS");
		stepMenuUltimosMovimientos.setContextVariableName(ctxVar
				.getContextVarByName("menuUltimosMovimientosContextVar"));
		Steps.put(stepMenuUltimosMovimientos.GetId(),
				stepMenuUltimosMovimientos);

		stepMenuMovimientosAnteriores = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuMovimientosAnteriores
				.setStepDescription("MENU => MENU MOVIMIENTOS ANTERIORES");
		stepMenuMovimientosAnteriores.setContextVariableName(ctxVar
				.getContextVarByName("menuMovimientosAnterioresContextVar"));
		Steps.put(stepMenuMovimientosAnteriores.GetId(),
				stepMenuMovimientosAnteriores);

		/* Say Number */

		stepNumberConsumosDisponibles = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberConsumosDisponibles
				.setStepDescription("SAYNUMBER => CONSUMOS DISPONIBLES PRECARGADA CABAL");
		Steps.put(stepNumberConsumosDisponibles.GetId(),
				stepNumberConsumosDisponibles);

		stepNumberMontoUltimoCarga = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoUltimoCarga
				.setStepDescription("SAYNUMBER => MONTO ULTIMA CARGA");
		Steps.put(stepNumberMontoUltimoCarga.GetId(),
				stepNumberMontoUltimoCarga);

		stepNumberDisponibleAdelantos = (StepSayNumber) StepFactory.createStep(
				StepType.SayNumber, UUID.randomUUID());
		stepNumberDisponibleAdelantos
				.setStepDescription("SAYNUMBER => DISPONIBLE ADELANTOS");
		Steps.put(stepNumberDisponibleAdelantos.GetId(),
				stepNumberDisponibleAdelantos);

		stepNumberMontoMovimientosAnteriores_1 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoMovimientosAnteriores_1
				.setStepDescription("SAYNUMBER => MOVIMIENTOS ANTERIORES - 1 / 3");
		Steps.put(stepNumberMontoMovimientosAnteriores_1.GetId(),
				stepNumberMontoMovimientosAnteriores_1);

		stepNumberMontoMovimientosAnteriores_2 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoMovimientosAnteriores_2
				.setStepDescription("SAYNUMBER => MOVIMIENTOS ANTERIORES - 2 / 3");
		Steps.put(stepNumberMontoMovimientosAnteriores_2.GetId(),
				stepNumberMontoMovimientosAnteriores_2);

		stepNumberMontoMovimientosAnteriores_3 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoMovimientosAnteriores_3
				.setStepDescription("SAYNUMBER => MOVIMIENTOS ANTERIORES - 3 / 3");
		Steps.put(stepNumberMontoMovimientosAnteriores_3.GetId(),
				stepNumberMontoMovimientosAnteriores_3);

		stepNumberMontoUltimoMovimiento_1 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoUltimoMovimiento_1
				.setStepDescription("SAYNUMBER => ULTIMO MOVIMIENTO - 1 / 3");
		Steps.put(stepNumberMontoUltimoMovimiento_1.GetId(),
				stepNumberMontoUltimoMovimiento_1);

		stepNumberMontoUltimoMovimiento_2 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoUltimoMovimiento_2
				.setStepDescription("SAYNUMBER => SAYNUMBER => ULTIMO MOVIMIENTO - 2 / 3");
		Steps.put(stepNumberMontoUltimoMovimiento_2.GetId(),
				stepNumberMontoUltimoMovimiento_2);

		stepNumberMontoUltimoMovimiento_3 = (StepSayNumber) StepFactory
				.createStep(StepType.SayNumber, UUID.randomUUID());
		stepNumberMontoUltimoMovimiento_3
				.setStepDescription("SAYNUMBER => SAYNUMBER => ULTIMO MOVIMIENTO - 3 / 3");
		Steps.put(stepNumberMontoUltimoMovimiento_3.GetId(),
				stepNumberMontoUltimoMovimiento_3);

		/* FIN */

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN COMUNICACION PRECARGADA");

		/* GRUPOS */
		pideTarjetaGrp = (PideTarjeta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.pideTarjeta);
		pideTarjetaGrp.setAudioTarjeta("PRECARGADAS/033");
		pideTarjetaGrp.setAudioSuTarjetaEs("PRECARGADAS/035");
		pideTarjetaGrp.setAudioTarjetaInvalido("PRECARGADAS/036");
		pideTarjetaGrp.setTarjetaContextVar(ctxVar
				.getContextVarByName("tarjetaContextVar"));
		pideTarjetaGrp.setConfirmaTarjetaContextVar(ctxVar
				.getContextVarByName("confirmaTarjetaContextVar"));
		pideTarjetaGrp.setIntentosTarjetaContextVar(ctxVar
				.getContextVarByName("intentosTarjetaContextVar"));

		/* JPOS */

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos.setContextVariableTipoMensaje(ctxVar
				.getContextVarByName("envioServerJposConsultasContexVar"));
		enviaTramaJpos.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		enviaTramaJpos.setContextVariableRspJpos(ctxVar
				.getContextVarByName("retornoMsgJPOS"));
		enviaTramaJpos
				.addformatoVariables(
						0,
						ctxVar.getContextVarByName("codigoOperacionConsultaContextVar"));
		enviaTramaJpos.addformatoVariables(1,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(2,
				ctxVar.getContextVarByName("tarjetaContexVar"));
		enviaTramaJpos.addformatoVariables(3,
				ctxVar.getContextVarByName("fillerParaConsultaContexVar"));
		enviaTramaJpos.addformatoVariables(4,
				ctxVar.getContextVarByName("idLlamadaContexVar"));
		enviaTramaJpos.addformatoVariables(5,
				ctxVar.getContextVarByName("whisperContextVar"));
		enviaTramaJpos.setStepDescription("SENDJPOS => ENVIA TRAMA JPOS");
		Steps.put(enviaTramaJpos.GetId(), enviaTramaJpos);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(ctxVar
				.getContextVarByName("retornoJPOS"));
		evalRetJPOS.setStepDescription("SWITCH => CODIGO RETORNO JPOS");
		Steps.put(evalRetJPOS.GetId(), evalRetJPOS);

		/* Horario */

		obtieneHorario = (StepTimeConditionDB) StepFactory.createStep(
				StepType.TimeConditionDB, UUID.randomUUID());
		obtieneHorario
				.setStepDescription("TIMECONDITIONDB => OBTIENE HORARIO DE LA BASE");
		obtieneHorario.setContextVarEmpresa(ctxVar
				.getContextVarByName("empresaIdContextVar"));
		obtieneHorario.setContextVarServicio(ctxVar
				.getContextVarByName("servicioIdContextVar"));
		obtieneHorario.setContextVarAudio(ctxVar
				.getContextVarByName("audioFueraHorarioContextVar"));
		Steps.put(obtieneHorario.GetId(), obtieneHorario);

		/* Conditional */

		evalDisponibleAdelantos = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalDisponibleAdelantos
				.setStepDescription("CONDITIONAL => TIENE DISPONIBLE ADELANTO");
		Steps.put(evalDisponibleAdelantos.GetId(), evalDisponibleAdelantos);

		/* ---------------- Derivo -------------- */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORBIE"));
		stepDerivoLlamada
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR");
		Steps.put(stepDerivoLlamada.GetId(), stepDerivoLlamada);

		this.evalRetJPOS();

	}

	public PideTarjeta getPideTarjeta() {
		return pideTarjetaGrp;
	}

}
