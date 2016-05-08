package liquerPlant.client.silo.Enablers.LevelSensorEnablers;

import liquerPlant.client.silo.SiloController;
import liquerPlant.core.LevelSensorOutputState;

public class LowLevelSensorInstanceEnabler extends LevelSensorInstanceEnabler {

	public LowLevelSensorInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected LevelSensorOutputState.state getLevelSensorOutput() {
		
		 return this.getSiloController().getLowLevelSensorOutput();
	}

	
}
