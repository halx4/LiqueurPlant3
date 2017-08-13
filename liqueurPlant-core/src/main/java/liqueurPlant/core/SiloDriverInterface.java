package liqueurPlant.core;



public interface SiloDriverInterface {

	
	void initialize();
	void begin();
	
	void setInValveState(ValveState.state newState);
	void setOutValveState(ValveState.state newState);
	
	LevelSensorOutputState.state getHighLevelSensorOutput();
	LevelSensorOutputState.state getLowLevelSensorOutput();

	
	void setMixerState(MixerState.state newState);
	void setHeaterState(HeaterState.state newState);

	Temperature getTemperature();

	public void setController(SiloControllerInterface controller);
		

}
