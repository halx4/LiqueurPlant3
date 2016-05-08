package liquerPlant.client.siloDriverCodesys;

import liquerPlant.core.HeaterState;
import liquerPlant.core.MixerState;
import liquerPlant.core.ValveState;

class SiloOutputs {
	
	/** 
	 * inV | outV | heater | mixer
	 */
	private char[] out=new char[]{'0','0','0','0'};
	
	public synchronized String getOutputs(){
		return new String(out);
	}

	public synchronized void setInValve(ValveState.state newState){
		if(newState == ValveState.state.OPEN)out[0]='1';
		else out[0]='0';
	}

	public synchronized void setOutValve(ValveState.state newState){
		if(newState == ValveState.state.OPEN)out[1]='1';
		else out[1]='0';
	}
	

	public synchronized void setHeater(HeaterState.state newState){
		if(newState == HeaterState.state.HEATING)out[2]='1';
		else out[2]='0';
	}
	
	public synchronized void setMixer(MixerState.state newState){
		if(newState == MixerState.state.MIXING)out[3]='1';
		else out[3]='0';
	}
	
	
}
