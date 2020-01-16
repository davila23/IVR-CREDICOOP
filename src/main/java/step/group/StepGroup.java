package step.group;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import workflow.Task;

public interface StepGroup {

	ConcurrentHashMap<UUID, Task> Steps = new ConcurrentHashMap<UUID, Task>();

	StepGroupFactory.StepGroupType GroupType = null;

	UUID getNextstep();

	UUID getInitialStep();

	ConcurrentHashMap<UUID, Task> getSteps();

}
