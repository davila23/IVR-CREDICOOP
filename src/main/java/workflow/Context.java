package workflow;

import java.util.Map;
import java.util.UUID;

public interface Context extends Map {

	UUID getInitialStep();
}
