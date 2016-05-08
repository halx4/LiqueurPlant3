package liquerPlant.core;

public class LevelSensorOutputState {

	public static enum state {
		LIQUIDDETECTED,
		LIQUIDNOTDETECTED
	}
	
	public static boolean levelSensorOutputState2Boolean(LevelSensorOutputState.state state){
		if (state==LevelSensorOutputState.state.LIQUIDDETECTED)return true;
		else return false;
	}

	public static LevelSensorOutputState.state boolean2LevelSensorOutputState(boolean bool){
		if(bool)return LevelSensorOutputState.state.LIQUIDDETECTED;
		else return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
	}
	
}
