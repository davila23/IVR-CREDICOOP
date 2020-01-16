package clasesivr;

import java.util.UUID;

import org.asteriskjava.fastagi.AgiChannel;

import context.ContextVar;

import step.StepContinueOnDialPlan;
import step.StepFactory;
import step.StepGetAsteriskVariable;
import step.StepSendJPOS;
import step.StepSetAsteriskVariable;
import step.StepStringFunctions;
import step.StepSwitch;
import step.StepFactory.StepType;

public class RutinaActivacionCabal extends Rutina {
	// Pasos
	protected StepGetAsteriskVariable obtieneTarjeta;
	protected StepGetAsteriskVariable obtieneDni;
	protected StepGetAsteriskVariable obtieneFechaNacimiento;
	protected StepGetAsteriskVariable obtieneCuenta;
	protected StepSendJPOS enviaTramaJpos;
	protected StepSwitch evalRetJPOS;
	protected StepContinueOnDialPlan pasoFinal;
	protected StepStringFunctions obtieneAnio;
	protected StepStringFunctions obtieneDia;
	protected StepStringFunctions obtieneMes;
	protected StepStringFunctions obtieneAAMMDD;
	protected StepSetAsteriskVariable configuraAGIRTA;
	// ContextVars
	private ContextVar tarjetaContexVar;
	private ContextVar idLlamadaContexVar;
	private ContextVar whisperContextVar;
	private ContextVar dniContextVar;
	private ContextVar fdnContextVar;
	private ContextVar cuentaContextVar;
	private ContextVar envioServerPortJposContexVar;
	private ContextVar retornoJPOS;
	private ContextVar retornoMsgJPOS;
	private ContextVar fillerContexVar;
	private ContextVar mensajeSaldosJpos;
	private ContextVar numeroDeLineaContexVar;
	private ContextVar fdnAnioContextVar;
	private ContextVar fdnMesContextVar;
	private ContextVar fdnDiaContextVar;
	private ContextVar fdnAAMMDDContextVar;
	private ContextVar substrBeginAnioContextVar;
	private ContextVar substrEndAnioContextVar;
	private ContextVar substrBeginMesContextVar;
	private ContextVar substrEndMesContextVar;
	private ContextVar substrBeginDiaContextVar;
	private ContextVar substrEndDiaContextVar;

	@Override
	protected void createSteps() {
		pasoFinal = (StepContinueOnDialPlan) StepFactory.createStep(
				StepType.ContinueOnDialPlan, UUID.randomUUID());
		pasoFinal.setStepDescription("END => CONTINUA EN DIALPLAN");

		obtieneTarjeta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneTarjeta.setContextVariableName(tarjetaContexVar);
		obtieneTarjeta.setVariableName("plastico");
		obtieneTarjeta.setStepDescription("GETVARIABLE => OBTIENE PLASTICO");

		obtieneDni = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneDni.setContextVariableName(dniContextVar);
		obtieneDni.setVariableName("dni");
		obtieneDni.setStepDescription("GETVARIABLE => OBTIENE DNI");

		obtieneFechaNacimiento = (StepGetAsteriskVariable) StepFactory
				.createStep(StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneFechaNacimiento.setContextVariableName(fdnContextVar);
		obtieneFechaNacimiento.setVariableName("FDN");
		obtieneFechaNacimiento
				.setStepDescription("GETVARIABLE => OBTIENE FECHA NACIMIENTO");

		obtieneCuenta = (StepGetAsteriskVariable) StepFactory.createStep(
				StepType.GetAsteriskVariable, UUID.randomUUID());
		obtieneCuenta.setContextVariableName(cuentaContextVar);
		obtieneCuenta.setVariableName("cuenta");
		obtieneCuenta.setStepDescription("GETVARIABLE => OBTIENE CUENTA");

		obtieneAnio = (StepStringFunctions) StepFactory.createStep(
				StepType.StringFunctions, UUID.randomUUID());
		obtieneAnio.setContextVariableDestino(fdnAnioContextVar);
		obtieneAnio.setContextVariableOrigen(fdnContextVar);
		obtieneAnio.setStepDescription("SUBSTRING => OBTIENE ANIO");
		obtieneAnio.setStringFunctionName("substring");
		obtieneAnio.addParametros(0, substrBeginAnioContextVar);
		obtieneAnio.addParametros(1, substrEndAnioContextVar);

		obtieneMes = (StepStringFunctions) StepFactory.createStep(
				StepType.StringFunctions, UUID.randomUUID());
		obtieneMes.setContextVariableDestino(fdnMesContextVar);
		obtieneMes.setContextVariableOrigen(fdnContextVar);
		obtieneMes.setStepDescription("SUBSTRING => OBTIENE MES");
		obtieneMes.setStringFunctionName("substring");
		obtieneMes.addParametros(0, substrBeginMesContextVar);
		obtieneMes.addParametros(1, substrEndMesContextVar);

		obtieneDia = (StepStringFunctions) StepFactory.createStep(
				StepType.StringFunctions, UUID.randomUUID());
		obtieneDia.setContextVariableDestino(fdnDiaContextVar);
		obtieneDia.setContextVariableOrigen(fdnContextVar);
		obtieneDia.setStepDescription("SUBSTRING => OBTIENE DIA");
		obtieneDia.setStringFunctionName("substring");
		obtieneDia.addParametros(0, substrBeginDiaContextVar);
		obtieneDia.addParametros(1, substrEndDiaContextVar);

		obtieneAAMMDD = (StepStringFunctions) StepFactory.createStep(
				StepType.StringFunctions, UUID.randomUUID());
		obtieneAAMMDD.setContextVariableDestino(fdnAAMMDDContextVar);
		obtieneAAMMDD.setStepDescription("CONCAT => OBTIENE AAMMDD");
		obtieneAAMMDD.setStringFunctionName("concat");
		obtieneAAMMDD.addParametros(0, fdnAnioContextVar);
		obtieneAAMMDD.addParametros(1, fdnMesContextVar);
		obtieneAAMMDD.addParametros(2, fdnDiaContextVar);

		enviaTramaJpos = (StepSendJPOS) StepFactory.createStep(
				StepType.SendJPOS, UUID.randomUUID());
		enviaTramaJpos
				.setContextVariableTipoMensaje(envioServerPortJposContexVar);
		enviaTramaJpos.setStepDescription("SENDJPOS => ENVIO MENSAJE JPOS");
		enviaTramaJpos.setContextVariableName(retornoJPOS);
		enviaTramaJpos.setContextVariableRspJpos(retornoMsgJPOS);
		enviaTramaJpos.addformatoVariables(0, mensajeSaldosJpos);
		enviaTramaJpos.addformatoVariables(1, numeroDeLineaContexVar);
		enviaTramaJpos.addformatoVariables(2, tarjetaContexVar);
		enviaTramaJpos.addformatoVariables(3, dniContextVar);
		enviaTramaJpos.addformatoVariables(4, fdnAAMMDDContextVar);
		enviaTramaJpos.addformatoVariables(5, cuentaContextVar);
		enviaTramaJpos.addformatoVariables(6, fillerContexVar);
		enviaTramaJpos.addformatoVariables(7, idLlamadaContexVar);
		enviaTramaJpos.addformatoVariables(8, whisperContextVar);

		evalRetJPOS = (StepSwitch) StepFactory.createStep(StepType.Switch,
				UUID.randomUUID());
		evalRetJPOS.setContextVariableName(retornoJPOS);
		evalRetJPOS.setStepDescription("SWITCH => EVALUA VALOR RETORNO JPOS");
		this.evalRetJPOS();

		configuraAGIRTA = (StepSetAsteriskVariable) StepFactory.createStep(
				StepType.SetAsteriskVariable, UUID.randomUUID());
		configuraAGIRTA.setStepDescription("SETASTERISKVARIABLE => AGIRTA");
		configuraAGIRTA.setContextVariableName(retornoJPOS);
		configuraAGIRTA.setVariableName("AGIRTA");

	}

	@Override
	protected void createContextVars(AgiChannel channel) {
		tarjetaContexVar = this
				.getContextVar("Tarjeta", "", "tarjetaContexVar");
		tarjetaContexVar.setStringFormat("%16d");

		dniContextVar = this.getContextVar("Dni", "", "dniContextVar");
		dniContextVar.setStringFormat("%011d");

		cuentaContextVar = this.getContextVar("Cuenta", "", "cuentaContextVar");
		cuentaContextVar.setStringFormat("%013d");

		fdnContextVar = this.getContextVar("Fecha leida FDN", "",
				"fdnContextVar");

		fdnAnioContextVar = this.getContextVar("Anio leida FDN", "",
				"fdnAnioContextVar");

		fdnMesContextVar = this.getContextVar("Mes leida FDN", "",
				"fdnMesContextVar");

		fdnDiaContextVar = this.getContextVar("Dia leida FDN", "",
				"fdnDiaContextVar");

		fdnAAMMDDContextVar = this.getContextVar("Fecha AAMMDD FDN", "",
				"fdnAAMMDDContextVar");

		idLlamadaContexVar = this.getContextVar("Id llamada",
				ast_uid.substring(ast_uid.length() - 29), "idLlamadaContexVar");

		whisperContextVar = this.getContextVar("Whisper", "0",
				"whisperContextVar");

		mensajeSaldosJpos = this.getContextVar("mensajeSaldosJpos", "6",
				"mensajeSaldosJpos");

		numeroDeLineaContexVar = this.getContextVar("numeroDeLineaContexVar",
				"00", "numeroDeLineaContexVar");

		envioServerPortJposContexVar = this.getContextVar("Tipo Mnj",
				"consultas", "envioServerPortJposContexVar");

		retornoJPOS = this.getContextVar("Codigo de retorno JPOS", "",
				"retornoJPOS");

		retornoMsgJPOS = this.getContextVar("Codigo de retorno JPOS", "",
				"retornoMsgJPOS");

		fillerContexVar = this.getContextVar("Filler", " ", "fillerContexVar");
		fillerContexVar.setStringFormat("%51s");

		substrBeginAnioContextVar = this.getContextVar("Begin Anio", "4",
				"substrBeginAnioContextVar");

		substrEndAnioContextVar = this.getContextVar("End Anio", "6",
				"substrEndAnioContextVar");

		substrBeginMesContextVar = this.getContextVar("Begin Mes", "2",
				"substrBeginMesContextVar");

		substrEndMesContextVar = this.getContextVar("End Mes", "4",
				"substrEndMesContextVar");

		substrBeginDiaContextVar = this.getContextVar("Begin Dia", "0",
				"substrBeginDiaContextVar");

		substrEndDiaContextVar = this.getContextVar("End Dia", "2",
				"substrEndDiaContextVar");

	}

	@Override
	protected void setSequence() {
		obtieneTarjeta.setNextstep(obtieneDni.GetId());
		obtieneDni.setNextstep(obtieneFechaNacimiento.GetId());
		obtieneFechaNacimiento.setNextstep(obtieneCuenta.GetId());
		obtieneCuenta.setNextstep(obtieneAnio.GetId());
		obtieneAnio.setNextstep(obtieneMes.GetId());
		obtieneMes.setNextstep(obtieneDia.GetId());
		obtieneDia.setNextstep(obtieneAAMMDD.GetId());

		obtieneAAMMDD.setNextstep(enviaTramaJpos.GetId());

		enviaTramaJpos.setNextstep(configuraAGIRTA.GetId());
		configuraAGIRTA.setNextstep(pasoFinal.GetId());

	}

	@Override
	protected void setInitialStep() {
		this.ctx.setInitialStep(obtieneTarjeta.GetId());

	}

	@Override
	protected void addGroups() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getClassNameChild() {
		return this.getClass().getName();
	}

	private void evalRetJPOS() {

		/*
		 * Códigos Host Descripción 0| NORMAL 99| Tarjeta inexistente, problemas
		 * Archivo de Tarjetas pasaría a ser para TQ : Tarjeta/documento
		 * inexistente 99|Vencida 80|Causa=0 81|causa <>08 o causa <> 18 83|6)
		 * Si la causa de la tarjeta es 08, el componente tiene que ser 01
		 * (Titular). Caso contrario = Error 83 82| Tiene que coincidir con la
		 * fecha de nacimiento informada. Caso contrario = Error 82 97| "Tiene
		 * que coincidir con el documento informado. Caso contrario = Error 97.
		 * Se valida para todos.Puede haber casos que no se informe? Este código
		 * antes indicaba que aplciaba sólo a casos que no son del BCCL. Se
		 * modifica para que aplique para todos." 85| Si la tarjeta tiene causa
		 * 18, la cuenta tiene que tener causa 00. Caso contrario = Error 85 86|
		 * Si la tarjeta tiene causa 08, la cuenta tiene que tener causa 08.
		 * Caso contrario = Error 86 84| Si la cuenta tiene causa 08, la cuenta
		 * tiene que permitir reactivación telefónica. Caso contrarior = Error
		 * 84 87| Si la tarjeta tiene causa 18, la Entidad emisora tiene que
		 * tener Señal de pre-emisión = 7. Caso contrario = Error 87
		 * 
		 * Validaciones TD Códigos Host Descripción 0| NORMAL 93| Tarjeta
		 * inexistente, problemas Archivo de Tarjetas pasaría a ser para TQ :
		 * Tarjeta/documento inexistente 94| Vencida 95| Causa <>08 o causa =08
		 * y marca de activación telefónica=no 92| Fecha de nacimiento informado
		 * difere de la guardada en base de datnextstepos 90| Nro de documento
		 * informado difere de la guardada en base de datos 91| Nro de cuenta
		 * informado difere de la guardada en base de datos
		 */
		evalRetJPOS.addSwitchValue("EE", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("00", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("80", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("81", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("82", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("83", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("84", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("85", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("86", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("87", pasoFinal.GetId());

		evalRetJPOS.addSwitchValue("90", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("91", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("92", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("93", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("94", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("95", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("96", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("97", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("98", pasoFinal.GetId());
		evalRetJPOS.addSwitchValue("99", pasoFinal.GetId());

	}

}
