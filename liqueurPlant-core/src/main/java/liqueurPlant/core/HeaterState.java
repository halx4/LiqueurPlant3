package liqueurPlant.core;

public class HeaterState {

	public static enum state {
		HEATING,
		NOTHEATING
	}
	
	
	public static boolean heaterState2Boolean(HeaterState.state state){
		if (state==HeaterState.state.HEATING)return true;
		else return false;
	}

	public static HeaterState.state boolean2HeaterState(boolean bool){
		if(bool)return HeaterState.state.HEATING;
		else return HeaterState.state.NOTHEATING;
	}
	
	
	
	
}
