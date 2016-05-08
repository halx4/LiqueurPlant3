package liquerPlant.core;

public class ValveState {
	public static enum state{OPEN,CLOSED}
	
	public static boolean valveState2Boolean(ValveState.state state){
		if (state==ValveState.state.OPEN)return true;
		else return false;
	}

	public static ValveState.state boolean2ValveState(boolean bool){
		if(bool)return ValveState.state.OPEN;
		else return ValveState.state.CLOSED;
	}
	
}
