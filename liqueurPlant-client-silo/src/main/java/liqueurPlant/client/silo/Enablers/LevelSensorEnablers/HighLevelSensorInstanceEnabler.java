package liqueurPlant.client.silo.Enablers.LevelSensorEnablers;

import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.LevelSensorOutputState;

public class HighLevelSensorInstanceEnabler extends LevelSensorInstanceEnabler {

	public HighLevelSensorInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected LevelSensorOutputState.state getLevelSensorOutput() {
		
		 return this.getSiloController().getHighLevelSensorOutput();
	}

	
}
