package liquerPlant.client.siloSimulator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import liquerPlant.core.HeaterState;
import liquerPlant.core.LevelSensorOutputState;
import liquerPlant.core.MixerState;
import liquerPlant.core.ValveState;
import liquerPlant.utilities.ExitHandler;


@SuppressWarnings("serial")
public class SiloSimulatorGui extends Frame {
	private final Color valveOpenColor=			new Color(128,255,0);
	private final Color valveClosedColor=		new Color(153,0,0);
	
	private final Color liquidDetectedColor=	new Color(255,128,0);
	private final Color liquidNotDetectedColor=	new Color(255,255,255);
	
	private final Color heatElementOnColor=		new Color(255,0,0);
	private final Color heatElementOffColor=	new Color(82,158,204);
	
	private final Color mixElementOnColor  =	new Color(255,0,0);
	private final Color mixElementOffColor =	new Color(82,158,204);
	
	private final Color temperatureElementColor=new Color(82,158,204);
	
	private JProgressBar liquidLevel;
	
	private Label inValveL,outValveL,highLevelSensorL,lowLevelSensorL,heatL,mixL,temperatureL;
	
	//private boolean hasHeater,hasMixer;
	
	SiloSimulatorGui(String name,boolean hasHeater, boolean hasMixer){
		//this.hasHeater=hasHeater;
		//this.hasMixer=hasMixer;
		
		this.setTitle(name);
		this.setLayout(null);
		this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		this.addWindowListener(new ExitHandler()); 
		this.setBackground(new Color(255, 204, 153));
		setBounds(800,100,200,200);

		this.toFront();
		this.setResizable(true);
		
		//this.setVisible(true);
		
        inValveL = new Label();
        inValveL.setBounds(50, 30, 20, 20);
        add(inValveL);
        inValveL.setVisible(true);
        
        
        outValveL = new Label();
        add(outValveL);
        outValveL.setBounds(50, 170, 20, 20);
        outValveL.setVisible(true);
     
        highLevelSensorL = new Label("F");
        add(highLevelSensorL);
        highLevelSensorL.setBounds(10, 60, 20, 20);
        highLevelSensorL.setVisible(true);

        lowLevelSensorL = new Label("E");
        add(lowLevelSensorL);
        lowLevelSensorL.setBounds(10, 140, 20, 20);
        lowLevelSensorL.setVisible(true);

        temperatureL = new Label("T=    C"); //TODO fix it
        add(temperatureL);
        temperatureL.setBounds(90, 70, 80, 20);
        temperatureL.setBackground(temperatureElementColor);
        temperatureL.setVisible(hasHeater);
        
        heatL = new Label("H");
        add(heatL);
        heatL.setBounds(90, 100, 20, 20);
        heatL.setVisible(hasHeater);
        
        mixL = new Label("M");
        add(mixL);
        mixL.setBounds(90, 130, 20, 20);
        mixL.setVisible(hasMixer);
        
        
        liquidLevel=new JProgressBar(SwingConstants.VERTICAL,0,10);
        liquidLevel.setBounds(30, 50, 60, 120);
        liquidLevel.setStringPainted(true);
        add(liquidLevel);
        liquidLevel.setVisible(true);
        
        setInValve(ValveState.state.CLOSED);
        setOutValve(ValveState.state.CLOSED);
        
        setLowLevelSensor(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
        setHighLevelSensor(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
        
        setHeatElement(HeaterState.state.NOTHEATING);
        setMixingElement(MixerState.state.NOTMIXING);
        
        
		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------
		
	}
	
	//-------------------------------------
	void setLiquidLevel(int levelFilled) {
		liquidLevel.setValue(levelFilled);
	}
	//-------------------------------------

	void setInValve(ValveState.state newState){
		switch(newState){
			case CLOSED:
				inValveL.setBackground(valveClosedColor);
				
				break;
			case OPEN:
				inValveL.setBackground(valveOpenColor);
				break;
		}
		//inValveL.setText("inValve: "+newState.toString());
		
		
	}
	//-------------------------------------

	void setOutValve(ValveState.state newState){
		switch(newState){
			case CLOSED:
				outValveL.setBackground(valveClosedColor);
				break;
			case OPEN:
				outValveL.setBackground(valveOpenColor);
				break;
		}
		//outValveL.setText("outValve: "+newState.toString());
       
	}
	//-------------------------------------

	void setLowLevelSensor(LevelSensorOutputState.state newState){
		switch(newState){
			case LIQUIDDETECTED:
				lowLevelSensorL.setBackground(liquidDetectedColor);
				break;
			case LIQUIDNOTDETECTED:
				lowLevelSensorL.setBackground(liquidNotDetectedColor);
				break;
		}
		//lowLevelSensorL.setText("lowLevelSensor: "+newState.toString());
		

	}
	//-------------------------------------

	void setHighLevelSensor(LevelSensorOutputState.state newState){
		switch(newState){
			case LIQUIDDETECTED:
				highLevelSensorL.setBackground(liquidDetectedColor);
				break;
			case LIQUIDNOTDETECTED:
				highLevelSensorL.setBackground(liquidNotDetectedColor);
				break;
		}
		//highLevelSensorL.setText("highLevelSensor: "+newState.toString());
		

	}
	//-------------------------------------

	void setHeatElement(HeaterState.state newState){
		if(newState==null)return;
		switch(newState){
			case HEATING:
				heatL.setBackground(heatElementOnColor);
				break;
			case NOTHEATING:
				heatL.setBackground(heatElementOffColor);
				break;
		}
		//heatL.setText("heater: "+newState.toString());
        
	}
	//-------------------------------------

	void setMixingElement(MixerState.state newState){
		if(newState==null)return;
		switch(newState){
			case MIXING:
				mixL.setBackground(mixElementOnColor);
				break;
			case NOTMIXING:
				mixL.setBackground(mixElementOffColor);
				break;
		}
		//mixL.setText("mixer: "+newState.toString());
		
	}
	//----------------------------------------
	void setTemperature(float temperature){
		temperatureL.setText("T= "+String.format("%.2f", temperature)+" C");
	}
	
	
}//end class
