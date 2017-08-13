package liqueurPlant.core;

public interface SiloControllerInterface {
	
	
	
	public void lowLevelSensorOutputChanged(LevelSensorOutputState.state newState);
	public void highLevelSensorOutputChanged(LevelSensorOutputState.state newState);
}
