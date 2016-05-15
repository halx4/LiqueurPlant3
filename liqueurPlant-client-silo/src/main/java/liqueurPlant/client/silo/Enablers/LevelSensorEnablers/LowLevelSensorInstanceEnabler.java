package liqueurPlant.client.silo.Enablers.LevelSensorEnablers;

import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.LevelSensorOutputState;

public class LowLevelSensorInstanceEnabler extends LevelSensorInstanceEnabler {

	public LowLevelSensorInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected LevelSensorOutputState.state getLevelSensorOutput() {
		
		 return this.getSiloController().getLowLevelSensorOutput();
	}

	
}
