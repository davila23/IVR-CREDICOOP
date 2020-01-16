package clasesivr;

import java.util.UUID;

import main.Daemon;

import org.asteriskjava.fastagi.AgiChannel;

import condition.condition;
import context.ContextVar;

import step.StepAnswer;
import step.StepConditional;
import step.StepEnd;
import step.StepExecute;
import step.StepFactory;
import step.StepLimitPlayRead;
import step.StepMenu;
import step.StepPlay;
import step.StepFactory.StepType;
import step.group.PreAtendedorBIE;
import step.group.PreAtendedorBIE;
import step.group.PrecargadasCabalActivacion;
import step.group.PrecargadasCabalConsulta;
import step.group.PrecargadasCabalDenunciaAsesor;
import step.group.PrecargadasCabalDenunciaIvr;
import step.group.PrecargadasCabalPedidoPin;
import step.group.PrecargadasCabalReimpresion;
import step.group.StepGroupFactory;
import workflow.Task;

public class RutinaPrecargadasCabal extends Rutina {

	protected StepEnd pasoFinal;
	private int intentos = 3;
	protected StepPlay stepAudioFinal;
	private ContextVar dniContextVar;
	protected PreAtendedorBIE preAtendedorBIE;
	protected StepPlay stepAudioSuperoIntentos;
	protected StepPlay stepAudioPrevioDerivoAsesor;
	protected StepExecute stepDerivoLlamada;
	protected StepPlay stepAudioBienvenida;
	private PrecargadasCabalActivacion activacionGrp;
	private PrecargadasCabalConsulta consultasGrp;
	private PrecargadasCabalDenunciaAsesor denunciasAsesorGrp;
	private PrecargadasCabalDenunciaIvr denunciasIvrGrp;
	private PrecargadasCabalPedidoPin pedidoPinGrp;
	private PrecargadasCabalReimpresion reimpresionGrp;
	private StepLimitPlayRead stepAudioMenuPrincipalPrecargadas;
	private StepPlay stepAudioIngresoIncorrectoMenuPrincipalPrecargadas;
	private StepMenu stepMenuPrincipalPrecargadas;
	private StepConditional evalDenuncia;
	private ContextVar tipoDenunciaContextVar;
	private ContextVar confirmaDniContextVar;
	private ContextVar intentosDniContextVar;
	private ContextVar scapeDigitContextVar;

	@Override
	protected void setInitialStep() {
		ctx.setInitialStep(stepAudioBienvenida.GetId());

	}

	@Override
	protected void setSequence() {

		stepAudioMenuPrincipalPrecargadas
				.setNextstep(stepMenuPrincipalPrecargadas.GetId());
		stepAudioMenuPrincipalPrecargadas
				.setNextStepIfAttemptLimit(stepAudioSuperoIntentos.GetId());

		stepMenuPrincipalPrecargadas.addSteps("1",
				consultasGrp.getInitialStep());
		stepMenuPrincipalPrecargadas.addSteps("2", evalDenuncia.GetId());
		stepMenuPrincipalPrecargadas.addSteps("3",
				reimpresionGrp.getInitialStep());
		stepMenuPrincipalPrecargadas.addSteps("4",
				pedidoPinGrp.getInitialStep());
		stepMenuPrincipalPrecargadas.addSteps("5",
				activacionGrp.getInitialStep());
		stepMenuPrincipalPrecargadas
				.setInvalidOption(stepAudioIngresoIncorrectoMenuPrincipalPrecargadas
						.GetId());

		stepAudioIngresoIncorrectoMenuPrincipalPrecargadas
				.setNextstep(stepAudioMenuPrincipalPrecargadas.GetId());

		evalDenuncia.addCondition(new condition(1, "#{"
				+ tipoDenunciaContextVar + "} == " + 1, denunciasAsesorGrp
				.getInitialStep(), denunciasIvrGrp.getInitialStep()));

		stepAudioBienvenida.setNextstep(preAtendedorBIE.getInitialStep());

		stepAudioPrevioDerivoAsesor.setNextstep(stepDerivoLlamada.GetId());
		stepAudioSuperoIntentos.setNextstep(pasoFinal.GetId());

	}

	@Override
	protected void addGroups() {
		/* Consultas - 1 */

		for (Task tmpTask : consultasGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : consultasGrp.getPideTarjeta().getSteps().values()) {
			cf.addTask(tmpTask);
		}

		/* Denuncia Asesor - 2 */

		for (Task tmpTask : denunciasAsesorGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : denunciasAsesorGrp.getPideDni().getSteps().values()) {
			cf.addTask(tmpTask);
		}

		/* Denuncia Ivr - 2 */

		for (Task tmpTask : denunciasIvrGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : denunciasIvrGrp.getPideDni().getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : denunciasIvrGrp.getPideFecha().getSteps().values()) {
			cf.addTask(tmpTask);
		}

		/* Reimpresion - 3 */

		for (Task tmpTask : reimpresionGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : reimpresionGrp.getPideDni().getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : reimpresionGrp.getPideTarjeta().getSteps().values()) {
			cf.addTask(tmpTask);
		}

		/* Pedido Pin - 4 */

		for (Task tmpTask : pedidoPinGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : pedidoPinGrp.getPideDni().getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : pedidoPinGrp.getPideFecha().getSteps().values()) {
			cf.addTask(tmpTask);
		}

		/* Activacion - 5 */

		for (Task tmpTask : activacionGrp.getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : activacionGrp.getPideDni().getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : activacionGrp.getPideFecha().getSteps().values()) {
			cf.addTask(tmpTask);
		}
		for (Task tmpTask : activacionGrp.getPideTarjeta().getSteps().values()) {
			cf.addTask(tmpTask);
		}
	}

	@Override
	protected void createSteps() {

		pasoFinal = (StepEnd) StepFactory.createStep(StepType.End,
				UUID.randomUUID());
		pasoFinal.setStepDescription("END => FIN COMUNICACION RUTINA BIE");

		/* ---------------- Grupos -------------- */

		activacionGrp = (PrecargadasCabalActivacion) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalActivacion);
		activacionGrp.setContextVar(ctx);

		consultasGrp = (PrecargadasCabalConsulta) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalConsulta);
		consultasGrp.setContextVar(ctx);

		denunciasAsesorGrp = (PrecargadasCabalDenunciaAsesor) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalDenunciaAsesor);
		denunciasAsesorGrp.setContextVar(ctx);

		denunciasIvrGrp = (PrecargadasCabalDenunciaIvr) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalDenunciaIvr);
		denunciasIvrGrp.setContextVar(ctx);

		pedidoPinGrp = (PrecargadasCabalPedidoPin) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalPedidoDePin);
		pedidoPinGrp.setContextVar(ctx);

		reimpresionGrp = (PrecargadasCabalReimpresion) StepGroupFactory
				.createStepGroup(StepGroupFactory.StepGroupType.precargadasCabalReimpresion);
		reimpresionGrp.setContextVar(ctx);

		/* ---------------- Audios -------------- */

		stepAudioFinal = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioFinal.setStepDescription("PLAY => SALUDO FINAL RUTINA BIE");
		stepAudioFinal.setPlayfile("RUTINAPINCOP/RUTINA_PIN032");

		stepAudioBienvenida = (StepPlay) StepFactory.createStep(StepType.Play,
				UUID.randomUUID());
		stepAudioBienvenida.setStepDescription("PLAY => BIENVENIDA RUTINA BIE");
		stepAudioBienvenida.setPlayfile("PREATENDEDORCABAL/031");

		stepAudioSuperoIntentos = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioSuperoIntentos.setPlayfile("PREATENDEDORCABAL/032");
		stepAudioSuperoIntentos
				.setStepDescription("PLAY => SUPERO INTENTOS RUTINA BIE");

		stepAudioPrevioDerivoAsesor = (StepPlay) StepFactory.createStep(
				StepType.Play, UUID.randomUUID());
		stepAudioPrevioDerivoAsesor.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioPrevioDerivoAsesor
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR");

		stepAudioIngresoIncorrectoMenuPrincipalPrecargadas = (StepPlay) StepFactory
				.createStep(StepType.Play, UUID.randomUUID());
		stepAudioIngresoIncorrectoMenuPrincipalPrecargadas
				.setPlayfile("PREATENDEDORCABAL/099");
		stepAudioIngresoIncorrectoMenuPrincipalPrecargadas
				.setStepDescription("PLAY => AUDIO PREVIO DERIVO ASESOR");

		/* ---------------- Limit Play Read -------------- */

		stepAudioMenuPrincipalPrecargadas = (StepLimitPlayRead) StepFactory
				.createStep(StepType.LimitPlayRead, UUID.randomUUID());
		stepAudioMenuPrincipalPrecargadas.setPlayFile("coto/A000013");
		stepAudioMenuPrincipalPrecargadas.setContextVariableName(dniContextVar);
		stepAudioMenuPrincipalPrecargadas
				.setStepDescription("LIMITPLAYREAD => MENU DENUNCIA,ASESOR");

		/* ---------------- Menu -------------- */

		stepMenuPrincipalPrecargadas = (StepMenu) StepFactory.createStep(
				StepType.Menu, UUID.randomUUID());
		stepMenuPrincipalPrecargadas
				.setStepDescription("MENU => MENU DENUNCIA");
		stepMenuPrincipalPrecargadas.setContextVariableName(dniContextVar);

		/* ---------------- Menu -------------- */
		evalDenuncia = (StepConditional) StepFactory.createStep(
				StepFactory.StepType.Conditional, UUID.randomUUID());
		evalDenuncia.setStepDescription("CONDITIONAL => ES TITULAR");

		/* ---------------- Derivo -------------- */

		stepDerivoLlamada = (StepExecute) StepFactory.createStep(
				StepType.Execute, UUID.randomUUID());
		stepDerivoLlamada.setApp("goto");
		stepDerivoLlamada.setAppOptions(Daemon.getConfig("DERIVOOPERADORBIE"));
		stepDerivoLlamada
				.setStepDescription("EXECUTE => DESVIO LLAMADA OPERADOR");

	}

	@Override
	protected void createContextVars(AgiChannel channel) {
		/* Dni */

		dniContextVar = this.getContextVar("Dni", "", "dniContextVar");
		dniContextVar.setStringFormat("%08d");

		confirmaDniContextVar = this.getContextVar("confirmaDniContextVar", "",
				"confirmaDniContextVar");

		intentosDniContextVar = this.getContextVar("intentosDniContextVar",
				"0", "intentosDniContextVar");

		tipoDenunciaContextVar = this.getContextVar("tipoDenunciaContextVar",
				"0", "tipoDenunciaContextVar");

		scapeDigitContextVar = this.getContextVar("scapeDigitContextVar", "",
				"scapeDigitContextVar");
	}

	@Override
	protected String getClassNameChild() {
		return this.getClass().getName();
	}

}
