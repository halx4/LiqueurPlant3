package liqueurPlant.core;

import liqueurPlant.core.HeaterState;
import liqueurPlant.core.LevelSensorOutputState;
import liqueurPlant.core.MixerState;
import liqueurPlant.core.ValveState;


public class SiloState {

	private ValveState.state inValveState, outValveState;
	private HeaterState.state heaterState;
	private MixerState.state mixerState;

	private LevelSensorOutputState.state lowLevelSensorOutput;
	private LevelSensorOutputState.state highLevelSensorOutput;

	
	public SiloState(ValveState.state inValveState,ValveState.state outValveState,
			LevelSensorOutputState.state currentLowLevelSensorOutput,	LevelSensorOutputState.state currentHighLevelSensorOutput,
			HeaterState.state heaterState,MixerState.state mixerState){
		
		
		this.inValveState=inValveState;
		this.outValveState=outValveState;
		this.heaterState=heaterState;
		this.mixerState=mixerState;
		this.lowLevelSensorOutput=currentLowLevelSensorOutput;
		this.highLevelSensorOutput=currentHighLevelSensorOutput;
				
	}
	

	/*public SiloState(SiloState stateToClone){
		
		this.inValveState			=stateToClone.inValveState;
		this.outValveState			=stateToClone.outValveState;
		this.lowLevelSensorOutput	=stateToClone.lowLevelSensorOutput;
		this.highLevelSensorOutput	=stateToClone.highLevelSensorOutput;
		this.heaterState			=stateToClone.heaterState;
		this.mixerState				=stateToClone.mixerState;
		
		
	}*/

	
	public ValveState.state getInValveState() {
		return inValveState;
	}

	public ValveState.state getOutValveState() {
		return outValveState;
	}

	public HeaterState.state getHeaterState() {
		return heaterState;
	}

	public MixerState.state getMixerState() {
		return mixerState;
	}

	public LevelSensorOutputState.state getLowLevelSensorOutput() {
		return lowLevelSensorOutput;
	}

	public LevelSensorOutputState.state getHighLevelSensorOutput() {
		return highLevelSensorOutput;
	}

	public void setInValveState(ValveState.state inValveState) {
		this.inValveState = inValveState;
	}

	public void setOutValveState(ValveState.state outValveState) {
		this.outValveState = outValveState;
	}

	public void setHeaterState(HeaterState.state heaterState) {
		this.heaterState = heaterState;
	}

	public void setMixerState(MixerState.state mixerState) {
		this.mixerState = mixerState;
	}

	public void setLowLevelSensorOutput(LevelSensorOutputState.state lowLevelSensorOutput) {
		this.lowLevelSensorOutput = lowLevelSensorOutput;
	}

	public void setHighLevelSensorOutput(LevelSensorOutputState.state highLevelSensorOutput) {
		this.highLevelSensorOutput = highLevelSensorOutput;
	}


	@Override
	public String toString(){
		return new String("inValveState="+inValveState.toString()+" outValveState="+outValveState.toString()+" lowLevelSensorOutput"+lowLevelSensorOutput.toString()+" highLevelSensorOutput="+highLevelSensorOutput.toString()+" heatingElementState="+heaterState.toString()+" mixingElementState="+mixerState.toString());
	}
	
	
}
