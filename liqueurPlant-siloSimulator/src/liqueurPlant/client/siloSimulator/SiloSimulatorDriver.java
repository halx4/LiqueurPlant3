package liqueurPlant.client.siloSimulator;

import java.util.Timer;
import java.util.TimerTask;

import liqueurPlant.core.HeaterState;
import liqueurPlant.core.LevelSensorOutputState;
import liqueurPlant.core.MixerState;
import liqueurPlant.core.SiloControllerInterface;
import liqueurPlant.core.SiloDriverInterface;
import liqueurPlant.core.Temperature;
import liqueurPlant.core.ValveState;


public class SiloSimulatorDriver implements SiloDriverInterface {
	private SiloParameters siloParams; 

	private  LevelSensorOutputState.state prevLowLevelSensorOutput,prevHighLevelSensorOutput;
	private ValveState.state inValveState,outValveState;
	private HeaterState.state heaterState;
	
	private SiloControllerInterface controller;
	
	private SiloSimulatorTimer liquidLevelTimer,heaterTimer;
	private SiloLiquidLevel level;
	private HeatingSimulator heatingSimulator;
	
	private SiloSimulatorGui gui; 
	
	private final float ROOMTEMPERATURE=25.0f;
	private int time2Heat1Degree=500;//in ms. TODO should be made parameter (is in ms)
								
	//----------------------------------------
	public SiloSimulatorDriver(SiloParameters params,String name){
		this.siloParams=params;
		
		level=new SiloLiquidLevel();
		heatingSimulator=new HeatingSimulator();
		prevLowLevelSensorOutput=getLowLevelSensorOutput();
		prevHighLevelSensorOutput=getHighLevelSensorOutput();
		
		inValveState=ValveState.state.CLOSED;//TODO make correct initilization?
		outValveState=ValveState.state.CLOSED; //TODO make correct initilization?
		
		heaterState=HeaterState.state.NOTHEATING;
		
		gui=new SiloSimulatorGui(name,params.hasHeater,params.hasMixer);
		//TODO INIT GUI		
	
	}

	//----------------------------------------
	@Override
	public synchronized void setInValveState(ValveState.state newState){
		inValveState=newState;
		gui.setInValve(newState);
	}
	//----------------------------------------
	@Override
	public synchronized void setOutValveState(ValveState.state newState){
		outValveState=newState;
		gui.setOutValve(newState);
		}
	//----------------------------------------
	@Override
	public void begin() {
		
	}
	//----------------------------------------
	@Override
	public void initialize() {
		liquidLevelTimer=new SiloSimulatorTimer(new LiquidLevelTimerTask(), siloParams.time2Fill);
		heaterTimer=new SiloSimulatorTimer(new HeaterTimerTask(),time2Heat1Degree );
	}
	//----------------------------------------
	@Override
	public synchronized  void setMixerState(MixerState.state newState){
		if(siloParams.hasMixer){
			gui.setMixingElement(newState);
		}
	}
	//----------------------------------------
	@Override
	public synchronized  void setHeaterState(HeaterState.state newState){
		if(siloParams.hasHeater){
			heaterState=newState;
			gui.setHeatElement(newState);
		}
	}
	//----------------------------------------
	@Override
	public synchronized LevelSensorOutputState.state getHighLevelSensorOutput(){  
		if(level.getLevel()>=siloParams.getHighLevelSensorHeight())return LevelSensorOutputState.state.LIQUIDDETECTED;
		else return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
	}
	//----------------------------------------
	@Override
	public synchronized LevelSensorOutputState.state getLowLevelSensorOutput(){  
		if(level.getLevel()>=siloParams.getLowLevelSensorHeight())return LevelSensorOutputState.state.LIQUIDDETECTED;
		else return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
		
	}
	//----------------------------------------
	@Override
	public void setController(SiloControllerInterface controller) {
		this.controller=controller;
		
	}
	//----------------------------------------
	@Override
	public Temperature getTemperature() {
		
		return new Temperature(heatingSimulator.getCurrentTemperature());  // TODO fix it
	}
	//---------------------------------------

	private synchronized void checkLevelSensorsOutputChange(){

		LevelSensorOutputState.state currentHighLevelSensorOutput=this.getHighLevelSensorOutput();
		LevelSensorOutputState.state currentLowLevelSensorOutput=this.getLowLevelSensorOutput();
		
		if(currentLowLevelSensorOutput	!=	prevLowLevelSensorOutput ){//low level sensor output changed
			gui.setLowLevelSensor(currentLowLevelSensorOutput);
			controller.lowLevelSensorOutputChanged(currentLowLevelSensorOutput);
			
		}
		else if(currentHighLevelSensorOutput != prevHighLevelSensorOutput	){//high level sensor output changed
			gui.setHighLevelSensor(currentHighLevelSensorOutput);
			controller.highLevelSensorOutputChanged(currentHighLevelSensorOutput);
		}
		
		prevLowLevelSensorOutput=currentLowLevelSensorOutput;
		prevHighLevelSensorOutput=currentHighLevelSensorOutput;
				
	
	}
	//----------------------------------------	
	private synchronized  void liquidLevelTimerEvent(){
		try{
			if(inValveState==ValveState.state.CLOSED && outValveState==ValveState.state.OPEN){// EMPTYING
				level.empty1Part();
				
			}
			else if(inValveState==ValveState.state.OPEN && outValveState==ValveState.state.CLOSED){
					level.fill1Part();
					heatingSimulator.setDefaultTemperature();

			}
		}
		catch(SiloOverflowException e){
			System.err.println("EXCEPTION: SILO OVERFLOW");
		}
		
		gui.setLiquidLevel(level.getLevel());
		gui.setTemperature(heatingSimulator.getCurrentTemperature());
		checkLevelSensorsOutputChange();
		
		
	}
	//----------------------------------------
	private synchronized void heaterTimerEvent(){
		
		//System.out.println("heaterTimerEvent-------------------~~~~!!!!");
		if(inValveState==ValveState.state.CLOSED && outValveState==ValveState.state.CLOSED && heaterState==HeaterState.state.HEATING){
			heatingSimulator.increase();
			gui.setTemperature(heatingSimulator.getCurrentTemperature());

		}
	}
	//----------------------------------------


	//####################################
	class LiquidLevelTimerTask extends TimerTask {
	    public void run() {
	    	liquidLevelTimerEvent();
	      
	    }
	  }
	//####################################
	class HeaterTimerTask extends TimerTask {
	    public void run() {
	    	heaterTimerEvent();
	      
	    }
	  }	
	//####################################
	class SiloSimulatorTimer{
		
		private Timer timer=new Timer();
		
		SiloSimulatorTimer(TimerTask tt,long timeInterval){
			timer.schedule(tt, timeInterval,timeInterval);
			
		}
		
		
	}
	//####################################
	class SiloLiquidLevel{
		private int levelFilled=0;
		
		
		synchronized void  fill1Part() throws SiloOverflowException{
			
			
			if(levelFilled<10){
				levelFilled++;
			}
			else{
				throw new SiloOverflowException();
				
			}
			
		}
		//----------------
		synchronized void empty1Part(){

			if(levelFilled>0)
				levelFilled--;
		}
		//----------------
		synchronized int getLevel() {
			return levelFilled;
		}
	}
	//####################################
	class HeatingSimulator{
		private float currentTemperature=ROOMTEMPERATURE;
		
		void increase(){
			currentTemperature+=1.0;
		}
		void setDefaultTemperature(){
			currentTemperature=ROOMTEMPERATURE;
		}
		float getCurrentTemperature(){
			return currentTemperature;
		}
	}

	

}
