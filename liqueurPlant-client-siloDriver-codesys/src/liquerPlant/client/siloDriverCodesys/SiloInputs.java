package liquerPlant.client.siloDriverCodesys;

import java.util.Arrays;

import liquerPlant.core.LevelSensorOutputState;
import liquerPlant.core.Temperature;

class SiloInputs {
	
	/** 
	 * 	highLevel | lowLevel
	 */
	private char[] in=new char[]{'0','0','0','0'};
	
	public synchronized void setInputs(String inString){
		
		for(int i=0;i<4;i++){
			in[i]=inString.charAt(i);
		}
		
	}
	
	public synchronized char[] getRawLevelSensorsInputs(){
		return Arrays.copyOf(in, 2);
		
	}
	
	public synchronized LevelSensorOutputState.state getHighLevelSensor(){
		if(in[0]=='0')return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
		else return LevelSensorOutputState.state.LIQUIDDETECTED;
	}
	
	public synchronized LevelSensorOutputState.state getLowLevelSensor(){
		if(in[1]=='0')return LevelSensorOutputState.state.LIQUIDNOTDETECTED;
		else return LevelSensorOutputState.state.LIQUIDDETECTED;
	}
	
	public synchronized Temperature getTemperature(){
		System.out.println("temp="+new String(in,2,2));
		return new Temperature(Float.parseFloat(new String(in,2,2)));
	}
}
