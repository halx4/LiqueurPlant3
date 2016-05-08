package liquerPlant.client.silo.Enablers.LevelSensorEnablers;

import liquerPlant.client.silo.SiloController;
import liquerPlant.core.LevelSensorOutputState;

public class HighLevelSensorInstanceEnabler extends LevelSensorInstanceEnabler {

	public HighLevelSensorInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected LevelSensorOutputState.state getLevelSensorOutput() {
		
		 return this.getSiloController().getHighLevelSensorOutput();
	}

	
}
