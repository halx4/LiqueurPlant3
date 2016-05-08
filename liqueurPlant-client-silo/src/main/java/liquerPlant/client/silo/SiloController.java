package liquerPlant.client.silo;

import java.util.Timer;
import java.util.TimerTask;

import liquerPlant.annotationsStuff.ResourceDef;
import liquerPlant.annotationsStuff.ServiceEnablementOp;
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


public class SiloController implements SiloControllerInterface {

	//private SmartSiloState smartSiloState;
	private SiloDriverInterface siloDriver;
	
	private SiloState siloState;
	
	private int mixerTimeToOperate=4; //default time (seconds)
	private MixerTimer mixerTimer;
	private Temperature targetTemperature=new Temperature(30); //default value
	
	private final int heatCompleteCheckInterval=1; //seconds
	private OperationsCompletedContainer opreationsCompleted=new OperationsCompletedContainer();
	
	private SmartSiloStateContainer smartState=new SmartSiloStateContainer();
	
	private ObserversUpdater 	siloEnabler,
								inValveEnabler,outValveEnabler,
								lowLevelSensorEnabler,highLevelSensorEnabler,
								mixerEnabler=null,heaterEnabler=null;
	


	//--------------------
	SiloController(SiloDriverInterface siloDriver) {
		super();
		smartState.setState(SmartSiloState.EMPTY);//TODO proper initialization
		this.siloState=new SiloState(	ValveState.state.CLOSED,ValveState.state.CLOSED, //TODO proper initialization
										LevelSensorOutputState.state.LIQUIDNOTDETECTED,	LevelSensorOutputState.state.LIQUIDNOTDETECTED,
										HeaterState.state.NOTHEATING,MixerState.state.NOTMIXING);
		this.siloDriver = siloDriver;
		
		this.siloDriver.setController(this);
		this.siloDriver.initialize();
		
		new CheckHeatCompleteTimer();
	}
	
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 0, //silo State
			onOperations = ServiceEnablementOp.READ
			)
	public SmartSiloState getSmartSiloState(){//TODO fix in manual operation, smart state is not updated
		return smartState.getState();
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16664,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public ValveState.state getInValveState(){
		return siloState.getInValveState();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16664,
			parentObjectInstanceId=1,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public ValveState.state getOutValveState(){
		return siloState.getOutValveState();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16665,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public LevelSensorOutputState.state getHighLevelSensorOutput(){
		return siloState.getHighLevelSensorOutput();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16665,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public LevelSensorOutputState.state getLowLevelSensorOutput(){
		return siloState.getLowLevelSensorOutput();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16668,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public HeaterState.state getHeaterState(){
		return siloState.getHeaterState();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 3303,
			parentObjectInstanceId=0,
			id = 5700,
			onOperations = ServiceEnablementOp.READ
			)
	public Temperature getTemperature(){ //TODO or local variable which is updated in fixed interval?
		return siloDriver.getTemperature();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 11,
			onOperations = ServiceEnablementOp.READ
			)
	public Temperature getTargetTemperature(){
		return targetTemperature;
	}
	//---------------------
	@ResourceDef(
			parentObjectId = 16667,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public MixerState.state getMixerState(){
		return siloState.getMixerState();
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16667,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.READ
			)
	public int getMixerTimeToOperate(){
		return mixerTimeToOperate;
		}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 7,
			onOperations = ServiceEnablementOp.READ
			)
	public boolean getFillingCompleted() {
		return opreationsCompleted.getFillingCompleted();
	}
	//---------------------
	public boolean getEmptyingCompleted() {
		return opreationsCompleted.getEmptyingCompleted();
	}
	//---------------------
	public boolean getHeatingCompleted() {
		return opreationsCompleted.getHeatingCompleted();
	}
	//---------------------
	public boolean getMixingCompleted() {
		return opreationsCompleted.getMixingCompleted();
	}
	//-------------------
	//-------------------
	//-------------------
	
	@ResourceDef(
			parentObjectId = 16664,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.WRITE
			)
	public synchronized  boolean setInValveState(ValveState.state newState){
		
			siloState.setInValveState(newState);
			siloDriver.setInValveState(newState);
			inValveEnabler.fireResourcesChange(5850);
			return true;
				
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16664,
			parentObjectInstanceId=1,
			id = 5850,
			onOperations = ServiceEnablementOp.WRITE
			)
	public synchronized  boolean setOutValveState(ValveState.state newState){
		
			siloState.setOutValveState(newState);
			siloDriver.setOutValveState(newState);
			outValveEnabler.fireResourcesChange(5850);
			return true;
		
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16668,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.WRITE
			)
	public synchronized boolean setHeaterState(HeaterState.state newState){
		if(newState==HeaterState.state.NOTHEATING && siloState.getHeaterState() != newState){
			opreationsCompleted.setHeatingCompleted(true);
		}
		siloState.setHeaterState(newState);
		siloDriver.setHeaterState(newState);
		heaterEnabler.fireResourcesChange(5850);
		return true; //TODO fix return and heater lowleveloperationsAllowed check
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 11,
			onOperations = ServiceEnablementOp.WRITE
			)
	public boolean setTargetTemperature(Temperature newTemperature){ //returns true on success
		this.targetTemperature=newTemperature;
		siloEnabler.fireResourcesChange(11);
		return true;  //TODO fix return
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16667,
			parentObjectInstanceId=0,
			id = 5850,
			onOperations = ServiceEnablementOp.WRITE
			)
	public synchronized  boolean setMixerState(MixerState.state newState){
		if(newState==MixerState.state.NOTMIXING && siloState.getMixerState() != newState){
			opreationsCompleted.setMixingCompleted(true);
		}
		siloState.setMixerState(newState);
		siloDriver.setMixerState(newState);
		mixerEnabler.fireResourcesChange(5850);
		
		return true;	//TODO fix return and mixer lowleveloperationsAllowed check
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16667,
			parentObjectInstanceId=0,
			id = 0,
			onOperations = ServiceEnablementOp.WRITE
			)
	public synchronized boolean setMixerTimeToOperate(int seconds){ //returns true on success
		if(seconds>0){
			this.mixerTimeToOperate=seconds;
			mixerEnabler.fireResourcesChange(0);
			return true;
		}
		else return false;
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 4,
			onOperations = ServiceEnablementOp.EXECUTE
			)
	public void initialize(){
		siloDriver.initialize();
	}
	
	//--------------------
	//--------------------
	
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 1,
			onOperations = ServiceEnablementOp.EXECUTE
			)
 	public synchronized void  fill(){
 		
		switch(smartState.getState()){
			case EMPTY:
				this.setOutValveState(ValveState.state.CLOSED);
				this.setInValveState(ValveState.state.OPEN);
				smartState.setState(SmartSiloState.FILLING);
				break;
			case EMPTYING:
				this.setOutValveState(ValveState.state.CLOSED);
				this.setInValveState(ValveState.state.OPEN);
				smartState.setState(SmartSiloState.FILLING);
				break;
			default:
				break;
			
		}
	}
	//--------------------
	
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 2,
			onOperations = ServiceEnablementOp.EXECUTE
			)
	public synchronized void empty(){
		switch(smartState.getState()){
			case FULL:
			this.setInValveState(ValveState.state.CLOSED);
				this.setOutValveState(ValveState.state.OPEN);
				smartState.setState(SmartSiloState.EMPTYING);
				break;
			case FILLING:
				this.setInValveState(ValveState.state.CLOSED);
				this.setOutValveState(ValveState.state.OPEN);
				smartState.setState(SmartSiloState.EMPTYING);
				break;
			default:
				break;
	}
		
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 3,
			onOperations = ServiceEnablementOp.EXECUTE
			)
	public synchronized void stop(){
		switch(smartState.getState()){
			case FILLING:
			this.setInValveState(ValveState.state.CLOSED);
			smartState.setState(SmartSiloState.FULL);
				break;
			case EMPTYING:
			this.setOutValveState(ValveState.state.CLOSED);
			smartState.setState(SmartSiloState.EMPTY);
				break;
		default:
			break;
	}
	}	
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 5,
			onOperations = ServiceEnablementOp.EXECUTE
			)
	public synchronized  void heat(){ 
		
		this.setHeaterState(HeaterState.state.HEATING);
	}
	//--------------------
	@ResourceDef(
			parentObjectId = 16663,
			parentObjectInstanceId=0,
			id = 6,
			onOperations = ServiceEnablementOp.EXECUTE
			)
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

	private synchronized void checkHeatComplete(){
		//System.out.println("check Heat Complete");
		if(siloState.getHeaterState()==HeaterState.state.HEATING && (siloDriver.getTemperature().getValue()>= targetTemperature.getValue()) ){
			setHeaterState(HeaterState.state.NOTHEATING);
		}
	}
	//---------------------

	private synchronized void mixerTimerEvent(){
		System.out.println("mixer timer event");
		this.setMixerState(MixerState.state.NOTMIXING);
		System.out.println("---end mixer timer event");
	}//TODO

	//--------------------
	//--------------------
	
	@Override
	public synchronized void lowLevelSensorOutputChanged(LevelSensorOutputState.state newState){//must be invoked by driver only
		this.setLowLevelSensorOutput(newState);
		switch(newState){
			case LIQUIDDETECTED:
					switch(smartState.getState()){
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
					switch(smartState.getState()){
						case FULL:
						
							break;
						case FILLING:
						
							break;
						case EMPTY:
						
							break;
						case EMPTYING:
							this.setOutValveState(ValveState.state.CLOSED);
							smartState.setState(SmartSiloState.EMPTY);
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
						switch(smartState.getState()){
							case FULL:
							
								break;
							case FILLING:
								this.setInValveState(ValveState.state.CLOSED);
								smartState.setState(SmartSiloState.FULL);
								break;
							case EMPTY:
							
								break;
							case EMPTYING:
							
								break;
					}//end case switch(smartSiloState)
						
				case LIQUIDNOTDETECTED:
						switch(smartState.getState()){
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
	//######################################
	class OperationsCompletedContainer {

		private boolean fillingCompleted=false, //TODO proper initialization
						emptyingCompleted=true, //silo starts in empty state
						heatingCompleted=false,
						mixingCompleted=false;
		

		private void setFillingCompleted(boolean newVal){
			if(fillingCompleted!=newVal){
				fillingCompleted=newVal;
				emptyingCompleted=false; 
				heatingCompleted=false;
				mixingCompleted=false;
				if(newVal){
					siloEnabler.fireResourcesChange(7); //notify only if newVal==true
				}
			}
		}
		//---------------------
		private void setEmptyingCompleted(boolean newVal){
			if(emptyingCompleted!=newVal){
				fillingCompleted=false;
				emptyingCompleted=newVal; 
				heatingCompleted=false;
				mixingCompleted=false;
				if(newVal)siloEnabler.fireResourcesChange(8);
			}
		}
		//---------------------
		private void setHeatingCompleted(boolean newVal){
			if(heatingCompleted!=newVal){
				fillingCompleted=false;
				emptyingCompleted=false; 
				heatingCompleted=newVal;
				mixingCompleted=false;
				if(newVal)siloEnabler.fireResourcesChange(9);
			}
		}
		//---------------------
		private void setMixingCompleted(boolean newVal){
			if(mixingCompleted!=newVal){
				fillingCompleted=false;
				emptyingCompleted=false; 
				heatingCompleted=false;
				mixingCompleted=newVal;
				if(newVal)siloEnabler.fireResourcesChange(10);
			}
		}
		//---------------------
		private boolean getFillingCompleted(){
			return fillingCompleted;
		}
		private boolean getEmptyingCompleted() {
			return emptyingCompleted;
		}
		private boolean getHeatingCompleted() {
			return heatingCompleted;
		}
		private boolean getMixingCompleted() {
			return mixingCompleted;
		}
		
	}
	//################################
	class SmartSiloStateContainer{
		private SmartSiloState smartSiloState;
		
		private SmartSiloState getState(){return smartSiloState;}
		
		private void setState(SmartSiloState newState){
			smartSiloState=newState;
			if(siloEnabler!=null)siloEnabler.fireResourcesChange(0); //state changed
			if(newState==SmartSiloState.EMPTY){//fillingCompleted
				opreationsCompleted.setEmptyingCompleted(true);
			}
			else if(newState==SmartSiloState.FULL){//emptyingCompleted
				
				opreationsCompleted.setFillingCompleted(true);
			}
			System.out.println("                                  fire smartSiloStateChange: "+newState.toString());
			
		}
		//---------------------
	}//end inner class
	
}//end class
	

