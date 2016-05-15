package liqueurPlant.client.siloDriverHardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

import liqueurPlant.core.HeaterState;
import liqueurPlant.core.LevelSensorOutputState;
import liqueurPlant.core.MixerState;
import liqueurPlant.core.SiloControllerInterface;
import liqueurPlant.core.SiloDriverInterface;
import liqueurPlant.core.Temperature;
import liqueurPlant.core.ValveState;
import liqueurPlant.core.ValveState.state;

public class SiloHardwareDriver implements SiloDriverInterface {
	
	public static final boolean enabled=false;  //inverted logic pins
	public static final boolean disabled=true;
	
	private static final float defaultTemperature=25.0f;
	
	private GpioController gpio;
	
	private Serial serial;
	private GpioPinDigitalOutput heaterPin,mixerPin,inValvePin,outValvePin;
	private GpioPinDigitalInput highLevelSensorPin,lowLevelSensorPin;
	private SiloControllerInterface controller;
	private GpioListener gpioListener;
	private SerialListener serialListener;
	private Temperature currentTemperature;

	@Override
	public void initialize()  {
		 currentTemperature=new Temperature(defaultTemperature);
		
		 gpioListener=new GpioListener();
		 serialListener=new SerialListener();
		 gpio = GpioFactory.getInstance();
		 
		 
		 //		INPUTS
		 heaterPin= 	gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27,PinState.HIGH);
		 mixerPin= 		gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22,PinState.HIGH);
		 inValvePin= 	gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24,PinState.HIGH);
		 outValvePin= 	gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,PinState.HIGH);
		 
		 heaterPin	.setShutdownOptions(true,PinState.LOW,PinPullResistance.OFF,PinMode.DIGITAL_INPUT);
		 mixerPin	.setShutdownOptions(true,PinState.LOW,PinPullResistance.OFF,PinMode.DIGITAL_INPUT);
		 inValvePin	.setShutdownOptions(true,PinState.LOW,PinPullResistance.OFF,PinMode.DIGITAL_INPUT);
		 outValvePin.setShutdownOptions(true,PinState.LOW,PinPullResistance.OFF,PinMode.DIGITAL_INPUT);
		 
		 //		OUTPUTS
		 highLevelSensorPin=gpio.provisionDigitalInputPin(RaspiPin.GPIO_29,"highLevelSensorPin");
		 lowLevelSensorPin=	gpio.provisionDigitalInputPin(RaspiPin.GPIO_25,"lowLevelSensorPin");
		
		 highLevelSensorPin.addListener(gpioListener);
		 lowLevelSensorPin.addListener(gpioListener);

		 //		SERIAL
		 serial=SerialFactory.createInstance();
		 serial.addListener(serialListener);
		 serial.open(Serial.DEFAULT_COM_PORT, 9600);
		 
	}
	
	@Override
	public void begin() {
		
	}

	@Override
	public void setInValveState(state newState) {
		if		(newState==ValveState.state.CLOSED)	inValvePin.setState(disabled);
		else if	(newState==ValveState.state.OPEN)	inValvePin.setState(enabled);
		else System.err.println("HardwareDriver error1");
	}

	@Override
	public void setOutValveState(state newState) {
		if		(newState==ValveState.state.CLOSED)	outValvePin.setState(disabled);
		else if	(newState==ValveState.state.OPEN)	outValvePin.setState(enabled);
		else System.err.println("HardwareDriver error2");
	}

	@Override
	public LevelSensorOutputState.state getHighLevelSensorOutput() {
		System.out.println(highLevelSensorPin.getState()); //TODO remove after debug
		if		(highLevelSensorPin.getState()==PinState.HIGH)	return LevelSensorOutputState.state.LIQUIDDETECTED;
		else if	(highLevelSensorPin.getState()==PinState.LOW)	return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
		else {System.err.println("HardwareDriver error3");return null;}
	}

	@Override
	public LevelSensorOutputState.state getLowLevelSensorOutput() {
		System.out.println(lowLevelSensorPin.getState());	//TODO remove after debug
		if		(lowLevelSensorPin.getState()==PinState.HIGH)	return LevelSensorOutputState.state.LIQUIDDETECTED;
		else if	(lowLevelSensorPin.getState()==PinState.LOW)	return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
		else {System.err.println("HardwareDriver error4");return null;}
	}

	@Override
	public void setMixerState(MixerState.state newState) {
		if		(newState==MixerState.state.NOTMIXING)	mixerPin.setState(disabled);
		else if	(newState==MixerState.state.MIXING)		mixerPin.setState(enabled);
		else System.err.println("HardwareDriver error5");
	}

	@Override
	public void setHeaterState(HeaterState.state newState) {
		if		(newState==HeaterState.state.NOTHEATING)	heaterPin.setState(disabled);
		else if	(newState==HeaterState.state.HEATING)		heaterPin.setState(enabled);
		else System.err.println("HardwareDriver error6");
	}

	@Override
	public synchronized Temperature getTemperature() {
		return new Temperature(currentTemperature);//return a copy of current temperature
	}
	
	synchronized void setTemperature(float newTemp){
		currentTemperature.setValue(newTemp);
	}

	@Override
	public void setController(SiloControllerInterface controller) {
		this.controller=controller;
		
	}
	
	public static void main(String[] args){
		SiloHardwareDriver silo=new SiloHardwareDriver();
		
		silo.initialize();
	
	}
	
	//#########################
	class GpioListener implements GpioPinListenerDigital{
		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
			if(event.getPin().getName().equals("highLevelSensorPin")){
				//System.out.println("HIGH CHANGED TO "+event.getState());
				if(event.getState()==PinState.HIGH){
					controller.highLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDDETECTED);
				}
				else if(event.getState()==PinState.LOW){
					controller.highLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
				}
			}
			else if(event.getPin().getName().equals("lowLevelSensorPin")){
				//System.out.println("LOW CHANGED TO "+event.getState());
				if(event.getState()==PinState.HIGH){
					controller.lowLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDDETECTED);
				}
				else if(event.getState()==PinState.LOW){
					controller.lowLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
				}
			}
			else {System.err.println("HardwareDriver error7");}
		}
	}
	//############################
	class SerialListener implements SerialDataListener{

		@Override
		public void dataReceived(SerialDataEvent event) {
			String receivedString=event.getData();
			//System.out.println("RECIEVED STRING :"+event.getData()+"END");
			if(receivedString.startsWith("T=") && receivedString.endsWith("\n")){
				
					//System.out.println("ends with \\n");
					float temperature=Float.parseFloat(receivedString.substring(2,receivedString.length()-1));
					//System.out.println("new string="+receivedString+"END");
					//System.out.println(temperature);
					setTemperature(temperature);
				
			}
			
		}	
	}
	//###########################	

}//end outer class
