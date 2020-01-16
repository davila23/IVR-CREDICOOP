/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package step;

import java.util.UUID;

/**
 * 
 * @author Daniel Avila
 */
public class StepFactory {

	public enum StepType {

		PlayRead, Record, Transfer, Conditional, Execute, TextToSpeech, Answer, SetAsteriskVariable, GetAsteriskVariable, ExternalReader, Play, SqlQuery, Menu, Counter, ResetCounter, ValidateDni, GenerateKeyFromDni, SayDigits, SayNumber, ValidateDate, End, ValidateCardNumber, SendJPOS, Switch, SetVariable, ValidateBinCardNumber, TimeConditionDB, ContinueOnDialPlan, InitDniDB, SendA380Message, ParseSaldoTarjeta, SayMonth, PlayFromVar, ValidateDateTarjeta, ValidateCommerce, CambiaClave, AutentificarClave, GetCuentas, IsOwnCard, AuthInitialInfo, DefaultAcount, SayDefaultAccountBalance, SayAccountBalance, CabalBalance, LimitPlayRead, StringFunctions, SayYear, ParseDenunciasTarjeta, GetCheckPct, ValidateDateDenuncia, ValidateCuit, SayFiliales, FileExist, ClearPil, ValidateCuenta, ChekCuenta, CuentaEnDialplan, ClearKeyBPI, ValidateKeyBPI, ParseSaldoTarjetaPrecargada, ServiceBanca
	}

	public static Step createStep(StepType StepType, UUID id) {
		switch (StepType) {
		case PlayRead:
			return new StepPlayRead(id);
		case Record:
			return new StepRecord(id);
		case Transfer:
			return new StepTransfer(id);
		case Conditional:
			return new StepConditional(id);
		case Execute:
			return new StepExecute(id);
		case End:
			return new StepEnd(id);
		case TextToSpeech:
			return new StepTextToSpeech(id);
		case Answer:
			return new StepAnswer(id);
		case SetAsteriskVariable:
			return new StepSetAsteriskVariable(id);
		case GetAsteriskVariable:
			return new StepGetAsteriskVariable(id);
		case ExternalReader:
			return new StepExternalReader(id);
		case Play:
			return new StepPlay(id);
		case SqlQuery:
			return new StepSqlQuery(id);
		case Menu:
			return new StepMenu(id);
		case Counter:
			return new StepCounter(id);
		case ValidateDate:
			return new StepValidateDate(id);
		case ValidateDni:
			return new StepValidateDni(id);
		case GenerateKeyFromDni:
			return new StepGenerateKeyFromDni(id);
		case SayDigits:
			return new StepSayDigits(id);
		case SayNumber:
			return new StepSayNumber(id);
		case ValidateCardNumber:
			return new StepValidateCardNumber(id);
		case SendJPOS:
			return new StepSendJPOS(id);
		case Switch:
			return new StepSwitch(id);
		case SetVariable:
			return new StepSetVariable(id);
		case ValidateBinCardNumber:
			return new StepValidateBinCardNumber(id);
		case TimeConditionDB:
			return new StepTimeConditionDB(id);
		case ContinueOnDialPlan:
			return new StepContinueOnDialPlan(id);
		case InitDniDB:
			return new StepInitDniDB(id);
		case SendA380Message:
			return new StepSendA380Message(id);
		case ParseSaldoTarjeta:
			return new StepParseSaldosTarjeta(id);
		case SayMonth:
			return new StepSayMonth(id);
		case SayYear:
			return new StepSayYear(id);
		case PlayFromVar:
			return new StepPlayFromVar(id);
		case ValidateDateTarjeta:
			return new StepValidateDateTarjeta(id);
		case ValidateCommerce:
			return new StepValidateCommerceNumber(id);
		case CambiaClave:
			return new StepPasswordChange(id);
		case AutentificarClave:
			return new StepUserAuthentication(id);
		case GetCuentas:
			return new StepGetCuentas(id);
		case IsOwnCard:
			return new StepIsOwnCard(id);
		case AuthInitialInfo:
			return new StepAuthInitialInfo(id);
		case DefaultAcount:
			return new StepDefaultAcount(id);
		case SayDefaultAccountBalance:
			return new StepSayDefaultAccountBalance(id);
		case SayAccountBalance:
			return new StepSayAccountBalance(id);
		case CabalBalance:
			return new StepCabalBalance(id);
		case LimitPlayRead:
			return new StepLimitPlayRead(id);
		case StringFunctions:
			return new StepStringFunctions(id);
		case ParseDenunciasTarjeta:
			return new StepParseDenunciasTarjeta(id);
		case GetCheckPct:
			return new StepGetCheckPCT(id);
		case ValidateDateDenuncia:
			return new StepValidateDateDenuncia(id);
		case ValidateCuit:
			return new StepValidateCuit(id);
		case SayFiliales:
			return new StepSayFiliales(id);
		case FileExist:
			return new StepFileExist(id);
		case ClearPil:
			return new StepClearPil(id);
		case ValidateCuenta:
			return new StepValidateCuenta(id);
		case ChekCuenta:
			return new StepCheckCuenta(id);
		case CuentaEnDialplan:
			return new StepCheckCuentaEnDialPlan(id);
		case ClearKeyBPI:
			return new StepClearKeyBPI(id);
		case ValidateKeyBPI:
			return new StepValidateKeyBPI(id);
		case ParseSaldoTarjetaPrecargada:
			return new StepParseSaldosTarjetaPrecargada(id);
		case ServiceBanca:
			return new StepCheckServiceBanca(id);	
			

		}

		throw new IllegalArgumentException("El tipo " + StepType.toString()
				+ " no existe.");
	}
}
