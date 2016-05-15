package liqueurPlant.client.siloDriverCodesys;

import java.io.File;

import liqueurPlant.core.LevelSensorOutputState;
import liqueurPlant.core.SiloControllerInterface;
import liqueurPlant.core.SiloDriverInterface;
import liqueurPlant.core.Temperature;
import liqueurPlant.core.ValveState.state;
import pipeIO.in.PipeTailer;
import pipeIO.in.PipeTailerListenerAdapter;

public class SiloCodesysDriver extends PipeTailerListenerAdapter implements SiloDriverInterface,
																			PipeFillerSource
																			 {

	protected SiloInputs inputs=new SiloInputs();
	private SiloOutputs outputs=new SiloOutputs();
	private SiloControllerInterface controller;
	private LevelSensorsChangesDetector changesDetector;
	Thread detectorThread;
	
	public SiloCodesysDriver(File inPipe,File outPipe,int sendInterval,int receiveInterval,int checkForChangesInterval) {
		PipeTailer.create(inPipe,this,receiveInterval);
		OutPipeFiller.create(outPipe,this,sendInterval);
		
		changesDetector=new LevelSensorsChangesDetector(this,checkForChangesInterval);
		detectorThread=new Thread(changesDetector);
		//System.out.println("SiloCodesysDriver constructor finished");
	}

	//######### Methods invoked by SiloController

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void begin() {
		
		//now detector can safely start
		detectorThread.start();		
	}

	@Override
	public void setInValveState(state newState) {
		outputs.setInValve(newState);
	}

	@Override
	public void setOutValveState(state newState) {
		outputs.setOutValve(newState);
	}

	@Override
	public liqueurPlant.core.LevelSensorOutputState.state getHighLevelSensorOutput() {
		return inputs.getHighLevelSensor();
	}

	@Override
	public liqueurPlant.core.LevelSensorOutputState.state getLowLevelSensorOutput() {
		return inputs.getLowLevelSensor();
	}

	@Override
	public void setMixerState(liqueurPlant.core.MixerState.state newState) {
		outputs.setMixer(newState);
	}

	@Override
	public void setHeaterState(liqueurPlant.core.HeaterState.state newState) {
		outputs.setHeater(newState);
	}

	@Override
	public Temperature getTemperature() {
		return inputs.getTemperature();
	}

	@Override
	public void setController(SiloControllerInterface controller) {
		this.controller=controller;
	}
	//###########################
	
	//######### Methods invoked by PipeTailer
	@Override
	public void handle(String line){
		inputs.setInputs(line);
	}
	//###########################
	
	//######### Methods invoked by OutPipeFiller
	@Override
	public String getStringToSend() {
		return outputs.getOutputs();
	}
	//###########################
	
	//##########  Methods invoked by changesDetector
	//@Override
	public void lowLevelSensorOutputChanged(LevelSensorOutputState.state newState) {
		if(controller!=null)controller.lowLevelSensorOutputChanged(newState);
		
	}

	//@Override
	public void highLevelSensorOutputChanged(LevelSensorOutputState.state newState) {
		if(controller!=null)controller.highLevelSensorOutputChanged(newState);
		
	}
	//###########################
}
