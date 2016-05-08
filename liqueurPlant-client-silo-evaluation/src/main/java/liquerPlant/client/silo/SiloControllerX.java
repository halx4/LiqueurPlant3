package liquerPlant.client.silo;

import java.util.Timer;
import java.util.TimerTask;

import liquerPlant.client.core.ObserversUpdater;
import liquerPlant.core.HeaterState;
import liquerPlant.core.LevelSensorOutputState;
import liquerPlant.core.LevelSensorOutputState.state;
import liquerPlant.core.MixerState;
import liquerPlant.core.SiloControllerInterface;
import liquerPlant.core.SiloDriverInterface;
import liquerPlant.core.SiloState;
import liquerPlant.core.SmartSiloState;
import liquerPlant.core.Temperature;
import liquerPlant.core.ValveState;


public class SiloControllerX implements SiloControllerInterface {

	private SmartSiloState smartSiloState;
	private SiloDriverInterface siloDriver;
	
	private SiloState siloState;
	
	private int mixerTimeToOperate=4; //default time (seconds)
	private MixerTimer mixerTimer;
	private Temperature targetTemperature=new Temperature(30); //default value
	
	private CheckHeatCompleteTimer checkHeatCompleteTimer;
	private final int heatCompleteCheckInterval=1; //seconds
	
	private boolean fillingCompleted=false, //TODO proper initialization
					emptyingCompleted=true, //this is true because silo starts in empty state
					heatingCompleted=false,
					mixingCompleted=false;
	
	private ObserversUpdater 	siloEnabler,
								inValveEnabler,outValveEnabler,
								lowLevelSensorEnabler,highLevelSensorEnabler,
								mixerEnabler=null,heaterEnabler=null,temperatureEnabler=null;
	


	//--------------------
	SiloControllerX(SiloDriverInterface siloDriver) {
		super();
		this.smartSiloState=SmartSiloState.EMPTY;//TODO proper initialization
		this.siloState=new SiloState(	ValveState.state.CLOSED,ValveState.state.CLOSED, //TODO proper initialization
										LevelSensorOutputState.state.LIQUIDNOTDETECTED,	LevelSensorOutputState.state.LIQUIDNOTDETECTED,
										HeaterState.state.NOTHEATING,MixerState.state.NOTMIXING);
		this.siloDriver = siloDriver;
		
		this.siloDriver.setController(this);
		this.siloDriver.initialize();
		
		checkHeatCompleteTimer=new CheckHeatCompleteTimer();
	}
	
	//--------------------
	public SmartSiloState getSmartSiloState(){//TODO fix in manual operation, smart state is not updated
		return smartSiloState;
	}
	//--------------------
	public ValveState.state getInValveState(){
		return siloState.getInValveState();
		}
	//--------------------
	public ValveState.state getOutValveState(){
		return siloState.getOutValveState();
		}
	//--------------------
	public LevelSensorOutputState.state getHighLevelSensorOutput(){
		return siloState.getHighLevelSensorOutput();
		}
	//--------------------
	public LevelSensorOutputState.state getLowLevelSensorOutput(){
		return siloState.getLowLevelSensorOutput();
		}
	//--------------------
	public HeaterState.state getHeaterState(){
		return siloState.getHeaterState();
		}
	//--------------------
	public Temperature getTemperature(){ //TODO or local variable which is updated in fixed interval?
		return siloDriver.getTemperature();
		}
	//--------------------
	public Temperature getTargetTemperature(){
		return targetTemperature;
	}
	//---------------------
	public MixerState.state getMixerState(){
		return siloState.getMixerState();
		}
	//--------------------
	public int getMixerTimeToOperate(){
		return mixerTimeToOperate;
		}
	
	//-------------------
	//-------------------
	
	public synchronized  boolean setInValveState(ValveState.state newState){
		
			siloState.setInValveState(newState);
			siloDriver.setInValveState(newState);
			inValveEnabler.fireResourcesChange(5850);
			return true;
				
	}
	//--------------------
	public synchronized  boolean setOutValveState(ValveState.state newState){
		
			siloState.setOutValveState(newState);
			siloDriver.setOutValveState(newState);
			outValveEnabler.fireResourcesChange(5850);
			return true;
		
	}
	//--------------------
	public synchronized boolean setHeaterState(HeaterState.state newState){
		if(newState==HeaterState.state.NOTHEATING && siloState.getHeaterState() != newState){
			setFillingCompleted(false);
			setEmptyingCompleted(false);
			setMixingCompleted(false);
			setHeatingCompleted(true);
		}
		siloState.setHeaterState(newState);
		siloDriver.setHeaterState(newState);
		heaterEnabler.fireResourcesChange(5850);
		return true; //TODO fix return and heater lowleveloperationsAllowed check
	}
	//--------------------
	public boolean setTargetTemperature(Temperature newTemperature){ //returns true on success
		this.targetTemperature=newTemperature;
		siloEnabler.fireResourcesChange(11);
		return true;  //TODO fix return
	}
	//--------------------
	public synchronized  boolean setMixerState(MixerState.state newState){
		if(newState==MixerState.state.NOTMIXING && siloState.getMixerState() != newState){
			setFillingCompleted(false);
			setHeatingCompleted(false);
			setEmptyingCompleted(false);
			setMixingCompleted(true);
		}
		siloState.setMixerState(newState);
		siloDriver.setMixerState(newState);
		mixerEnabler.fireResourcesChange(5850);
		
		return true;	//TODO fix return and mixer lowleveloperationsAllowed check
	}
	//--------------------
	public synchronized boolean setMixerTimeToOperate(int seconds){ //returns true on success
		if(seconds>0){
			this.mixerTimeToOperate=seconds;
			mixerEnabler.fireResourcesChange(0);
			return true;
		}
		else return false;
	}
	//--------------------

	public void initialize(){
		siloDriver.initialize();
	}
	
	//--------------------
	//--------------------
	
 	public synchronized void  fill(){
 		
		switch(smartSiloState){
			case EMPTY:
				this.setOutValveState(ValveState.state.CLOSED);
				this.setInValveState(ValveState.state.OPEN);
				setNewSmartSiloState(SmartSiloState.FILLING);
				break;
			case EMPTYING:
				this.setOutValveState(ValveState.state.CLOSED);
				this.setInValveState(ValveState.state.OPEN);
				setNewSmartSiloState(SmartSiloState.FILLING);
				break;
			default:
				break;
			
		}
	}
	//--------------------
	public synchronized  void empty(){
		switch(smartSiloState){
			case FULL:
			this.setInValveState(ValveState.state.CLOSED);
				this.setOutValveState(ValveState.state.OPEN);
				setNewSmartSiloState(SmartSiloState.EMPTYING);
				break;
			case FILLING:
				this.setInValveState(ValveState.state.CLOSED);
				this.setOutValveState(ValveState.state.OPEN);
				setNewSmartSiloState(SmartSiloState.EMPTYING);
				break;
			default:
				break;
	}
		
	}
	//--------------------
	public synchronized  void stop(){
		switch(smartSiloState){
			case FILLING:
			this.setInValveState(ValveState.state.CLOSED);
				setNewSmartSiloState(SmartSiloState.FULL);
				break;
			case EMPTYING:
			this.setOutValveState(ValveState.state.CLOSED);
				setNewSmartSiloState(SmartSiloState.EMPTY);
				break;
		default:
			break;
	}
	}	
	//--------------------
	public synchronized  void heat(){ 
		
		this.setHeaterState(HeaterState.state.HEATING);
	}
	//--------------------
	public synchronized  void  mix(){
		mixerTimer=new MixerTimer();
		this.setMixerState(MixerState.state.MIXING);
	}//TODO
	
	//--------------------
	//--------------------
	
	void setSiloEnabler(ObserversUpdater siloEnabler) {
		this.siloEnabler = siloEnabler;
	}
	void setInValveEnabler(ObserversUpdater inValveEnabler) {
		this.inValveEnabler = inValveEnabler;
	}
	void setOutValveEnabler(ObserversUpdater outValveEnabler) {
		this.outValveEnabler = outValveEnabler;
	}
	void setLowLevelSensorEnabler(ObserversUpdater lowLevelSensorEnabler) {
		this.lowLevelSensorEnabler = lowLevelSensorEnabler;
	}
	void setHighLevelSensorEnabler(ObserversUpdater highLevelSensorEnabler) {
		this.highLevelSensorEnabler = highLevelSensorEnabler;
	}
	void setMixerEnabler(ObserversUpdater mixerEnabler) {
		this.mixerEnabler = mixerEnabler;
	}
	void setHeaterEnabler(ObserversUpdater heaterEnabler) {
		this.heaterEnabler = heaterEnabler;
	}
	void setTemperatureEnabler(ObserversUpdater temperatureEnabler){
		this.temperatureEnabler=temperatureEnabler;
	}

	//---------------------
	public boolean getFillingCompleted() {
		return fillingCompleted;
	}
	//---------------------
	public boolean getEmptyingCompleted() {
		return emptyingCompleted;
	}
	//---------------------
	public boolean getHeatingCompleted() {
		return heatingCompleted;
	}
	//---------------------
	public boolean getMixingCompleted() {
		return mixingCompleted;
	}
	//---------------------

	private synchronized void setLowLevelSensorOutput(state newState) {
		siloState.setLowLevelSensorOutput(newState);
		lowLevelSensorEnabler.fireResourcesChange(5550);
	}
	
	private synchronized void setHighLevelSensorOutput(state newState) {
		siloState.setHighLevelSensorOutput(newState);
		highLevelSensorEnabler.fireResourcesChange(5550);
	}	
	
	//---------------------
	private synchronized void setNewSmartSiloState(SmartSiloState newState){
		smartSiloState=newState;
		siloEnabler.fireResourcesChange(0); //state changed
		if(newState==SmartSiloState.EMPTY){//fillingCompleted
			setFillingCompleted(false);
			setHeatingCompleted(false);
			setMixingCompleted(false);
			setEmptyingCompleted(true);
		}
		else if(newState==SmartSiloState.FULL){//emptyingCompleted
			setEmptyingCompleted(false);
			setHeatingCompleted(false);
			setMixingCompleted(false);
			setFillingCompleted(true);
		}
		System.out.println("                                  fire smartSiloStateChange: "+newState.toString());
		
	}
	//---------------------

	private void setFillingCompleted(boolean newVal){
		if(fillingCompleted!=newVal){
			fillingCompleted=newVal;
			if(newVal)siloEnabler.fireResourcesChange(7); //notify only if newVal==true
		}
	}
	//---------------------
	private void setEmptyingCompleted(boolean newVal){
		if(emptyingCompleted!=newVal){
			emptyingCompleted=newVal;
			if(newVal)siloEnabler.fireResourcesChange(8);
		}
	}
	//---------------------
	private void setHeatingCompleted(boolean newVal){
		if(heatingCompleted!=newVal){
			heatingCompleted=newVal;
			if(newVal)siloEnabler.fireResourcesChange(9);
		}
	}
	//---------------------
	private void setMixingCompleted(boolean newVal){
		if(mixingCompleted!=newVal){
			mixingCompleted=newVal;
			if(newVal)siloEnabler.fireResourcesChange(10);
		}
	}
	//---------------------
	private void mixerTimerEvent(){
		System.out.println("mixer timer event");
		this.setMixerState(MixerState.state.NOTMIXING);
		System.out.println("---end mixer timer event");
	}//TODO
	//---------------------
	private void checkHeatComplete(){
		//System.out.println("check Heat Complete");
		if(siloState.getHeaterState()==HeaterState.state.HEATING && (siloDriver.getTemperature().getValue()>= targetTemperature.getValue()) ){
			setHeaterState(HeaterState.state.NOTHEATING);
		}
	}

	//--------------------
	//--------------------
	
	@Override
	public synchronized void lowLevelSensorOutputChanged(LevelSensorOutputState.state newState){//must be called by driver only
		this.setLowLevelSensorOutput(newState);
		switch(newState){
			case LIQUIDDETECTED:
					switch(smartSiloState){
						case FULL:
						
							break;
						case FILLING:
							
							break;
						case EMPTY:
						
							break;
						case EMPTYING:
						
							break;
				}//end case switch(smartSiloState)
					
			case LIQUIDNOTDETECTED:
					switch(smartSiloState){
						case FULL:
						
							break;
						case FILLING:
						
							break;
						case EMPTY:
						
							break;
						case EMPTYING:
						this.setOutValveState(ValveState.state.CLOSED);
							this.setNewSmartSiloState(SmartSiloState.EMPTY);
							break;
					}//end case switch(smartSiloState)
		}//end switch(newState)
	}
	//--------------------
	@Override
	public synchronized void highLevelSensorOutputChanged(LevelSensorOutputState.state newState){//must be called by driver only
		this.setHighLevelSensorOutput(newState);
		switch(newState){
				case LIQUIDDETECTED:
						switch(smartSiloState){
							case FULL:
							
								break;
							case FILLING:
							this.setInValveState(ValveState.state.CLOSED);
								this.setNewSmartSiloState(SmartSiloState.FULL);
								break;
							case EMPTY:
							
								break;
							case EMPTYING:
							
								break;
					}//end case switch(smartSiloState)
						
				case LIQUIDNOTDETECTED:
						switch(smartSiloState){
							case FULL:
							
								break;
							case FILLING:
							
								break;
							case EMPTY:
							
								break;
							case EMPTYING:
							
								break;
						}//end case switch(smartSiloState)
		}//end switch(newState)
	}
	
	//####################################
	class MixerTimerTask extends TimerTask {
	    public void run() {
	    	System.out.println("MixerTimerTask:timer event triggered");
	    	mixerTimerEvent();
	    	mixerTimer.timer.cancel();
	    }
	  }
	//####################################
	class MixerTimer{
		
		private Timer timer=new Timer();
		
		MixerTimer(){
			timer.schedule(new MixerTimerTask(), mixerTimeToOperate*1000);
			
		}
		
		
	}
	//######################################
	class CheckHeatCompleteTimerTask extends TimerTask{
		public void run(){
			//System.out.println("Heat Timer Task");
			checkHeatComplete();
		}
	}
	//######################################
	class CheckHeatCompleteTimer{
			
		private Timer timer=new Timer();
		
		CheckHeatCompleteTimer(){
			//System.out.println("heating Timer Constructor");
			timer.schedule(new CheckHeatCompleteTimerTask(),heatCompleteCheckInterval*1000, heatCompleteCheckInterval*1000);
		}
	}
}
