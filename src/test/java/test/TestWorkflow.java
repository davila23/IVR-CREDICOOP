package test;

import condition.condition;
import context.ContextVar;
import ivr.CallContext;
import ivr.CallFlow;
import ivr.IvrExceptionHandler;
import ivr.IvrFactory;
import step.*;
import step.StepFactory.StepType;
import workflow.Handler;
import workflow.Task;
import workflow.Workflow;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TestWorkflow {

	private static Workflow workflow = null;
	private static CallFlow flow = null;

	public TestWorkflow() {

	}

	public static Workflow getWorkflow() throws Exception {

		Class clazz = Thread.currentThread().getContextClassLoader()
				.loadClass("ivr.Ivr");
		Class clazzf = Thread.currentThread().getContextClassLoader()
				.loadClass("ivr.CallFlow");
		workflow = (Workflow) clazz.newInstance();
		IvrFactory.getInstance().addWorkflow("Workflow1", workflow);
		flow = (CallFlow) clazzf.newInstance();
		workflow.addTask("SampleFlow", flow);

		Handler exc = new IvrExceptionHandler();
		flow.addTask(exc);

		// ContextVar var1 = new ContextVar();
		// var1.setVarDescrip("dni");

		/*
		 * var1.setId(1); ContextVar var2 = new ContextVar(); var2.setId(2);
		 * ContextVar var3 = new ContextVar(); var3.setId(3); ContextVar var4 =
		 * new ContextVar(); var4.setId(4);
		 * 
		 * StepValidateDni stpvdni = (StepValidateDni)
		 * StepFactory.createStep(StepType.ValidateDni, UUID.randomUUID());
		 * stpvdni.setNextStepIsFalse(UUID.randomUUID());
		 * stpvdni.setNextStepIsTrue(UUID.randomUUID());
		 * stpvdni.setContextVariableName(var1); //
		 * "#{a} >= 2 || (#{b} >= 5 && #{c} >= 8)" StepPlayRead stp = null; stp
		 * = (StepPlayRead)
		 * StepFactory.createStep(StepFactory.StepType.PlayRead,
		 * UUID.randomUUID()); stp.setNextstep(stp.GetId()); flow.addTask(stp);
		 * 
		 * StepConditional stpc; stpc = (StepConditional)
		 * StepFactory.createStep( StepFactory.StepType.Conditional,
		 * UUID.randomUUID());
		 * 
		 * stpc.addCondition(new condition(1, "#{var1} == #{var2}",
		 * stpc.GetId())); stpc.setNextstep(stpc.GetId()); flow.addTask(stpc);
		 * 
		 * StepExecute stp1 = null; stp1 = (StepExecute)
		 * StepFactory.createStep(StepFactory.StepType.Execute,
		 * UUID.randomUUID()); stp.setNextstep(stpc.GetId()); flow.addTask(stp);
		 * 
		 * StepRecord stp2 = null; stp2 = (StepRecord)
		 * StepFactory.createStep(StepFactory.StepType.Record,
		 * UUID.randomUUID()); stp.setNextstep(stpc.GetId()); flow.addTask(stp);
		 * 
		 * StepEnd stp3 = null; stp3 = (StepEnd)
		 * StepFactory.createStep(StepFactory.StepType.End, UUID.randomUUID());
		 * stp.setNextstep(stpc.GetId()); flow.addTask(stp);
		 */

		return workflow;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		try {
			Workflow wp = getWorkflow();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) throws Exception {
	// EntityManagerFactory emf =
	// Persistence.createEntityManagerFactory("doivrservicePU");
	// EntityManager entityManager = emf.createEntityManager();
	// entityManager.getTransaction().begin();
	//
	// CallFlow cf = new CallFlow();
	// cf.setExtension("2121");
	// cf.setName("Prueba");
	// cf.setInitialStep(1L);
	//
	// ContextVar cv = new ContextVar();
	//
	// cv.setVarDescrip("Prueba");
	// cv.setVarValue("0");
	// entityManager.persist(cv);
	//
	// cf.addContextVarTask(cv);
	// entityManager.persist(cf);
	//
	// Step sta4 = new StepEnd();
	// sta4.setNextstep(0);
	// entityManager.persist(sta4);
	//
	// Step sta3 = new StepPlay();
	// ((StepPlay) sta3).setContextVariableName(cv);
	// ((StepPlay) sta3).setPlayfile("digits/1");
	// sta3.setNextstep(sta4.GetId());
	// entityManager.persist(sta3);
	//
	// Step sta5 = new StepPlay();
	// ((StepPlay) sta5).setContextVariableName(cv);
	// ((StepPlay) sta5).setPlayfile("digits/2");
	// sta5.setNextstep(sta4.GetId());
	// entityManager.persist(sta5);
	//
	// Step sta2 = new StepMenu();
	//
	// sta2.setNextstep(sta4.GetId());
	// ((StepMenu) sta2).addSteps("1", sta3.GetId());
	// ((StepMenu) sta2).addSteps("2", sta5.GetId());
	// ((StepMenu) sta2).setInvalidOption(sta4.GetId());
	// ((StepMenu) sta2).setContextVariableName(cv);
	// entityManager.persist(sta2);
	//
	// Step sta6 = new StepPlayRead();
	// ((StepPlayRead) sta6).setContextVariableName(cv);
	// ((StepPlayRead) sta6).setPlayFile("demo-congrats");
	// ((StepPlayRead) sta6).setPlayMaxDigits(1);
	// ((StepPlayRead) sta6).setPlayTimeout(3L);
	// sta6.setNextstep(sta2.GetId());
	// entityManager.persist(sta6);
	//
	// Step sta = new StepAnswer();
	// sta.setNextstep(sta6.GetId());
	// entityManager.persist(sta);
	//
	// cf.addTask(sta);
	// cf.addTask(sta2);
	// cf.addTask(sta3);
	// cf.addTask(sta4);
	// cf.addTask(sta5);
	// cf.addTask(sta6);
	// cf.setInitialStep(sta.GetId());
	//
	// entityManager.persist(cf);
	// entityManager.getTransaction().commit();
	//
	// Query query = entityManager.createNamedQuery("findCallFlowByExtension");
	// query.setParameter("extension", "2121");
	// Collection cflows = query.getResultList();
	// CallFlow cf1 = (CallFlow) cflows.iterator().next();
	// CallContext ctx = new CallContext(1L);
	//
	// entityManager.close();
	// emf.close();
	//
	//
	// }
}
