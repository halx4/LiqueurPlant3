package liquerPlant.core;

public class MixerState {
	public static enum state {
		MIXING,
		NOTMIXING
	}

	public static boolean mixerState2Boolean(MixerState.state state){
		if (state==MixerState.state.MIXING)return true;
		else return false;
	}

	public static MixerState.state boolean2MixerState(boolean bool){
		if(bool)return MixerState.state.MIXING;
		else return MixerState.state.NOTMIXING;
	}
	
	
	
}
