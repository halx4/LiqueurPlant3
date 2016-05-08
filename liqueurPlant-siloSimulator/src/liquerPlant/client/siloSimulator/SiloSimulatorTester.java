package liquerPlant.client.siloSimulator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import liquerPlant.client.siloSimulator.SiloParameters;
import liquerPlant.client.siloSimulator.SiloSimulatorDriver;
import liquerPlant.core.SiloControllerInterface;
import liquerPlant.core.ValveState;
import liquerPlant.core.MixerState;
import liquerPlant.core.HeaterState;


@SuppressWarnings("serial")
public class SiloSimulatorTester extends Frame implements SiloControllerInterface {
	private final int buttonsNo=10;
	private ButtonWithID[] button=new ButtonWithID[buttonsNo];
	private ButtonHandler buttonHandler;
	private SiloSimulatorDriver siloSimulator;
	private Label observeLabel;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SiloSimulatorTester tester= new SiloSimulatorTester();
		
	}
	
	
	public SiloSimulatorTester(){
		buttonHandler=new ButtonHandler();
		
		siloSimulator=new SiloSimulatorDriver(new SiloParameters(1,9,1000,true,true) , "Silo Tester");
		siloSimulator.setController(this);
		siloSimulator.initialize();
		
		this.setTitle("Silo simulator tester");
		this.setLayout(null);
		this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		this.setBackground(new Color(112, 125, 159));
		setBounds(100,100,500,500);

		this.toFront();
		this.setResizable(true);

		observeLabel = new Label();
		observeLabel.setBackground(Color.LIGHT_GRAY);
        add(observeLabel);
        observeLabel.setBounds(100, 60, 300, 20);
        observeLabel.setVisible(true);
        

		for(int i=0;i<buttonsNo;i++){
			button[i]=new ButtonWithID("button"+i);
			button[i].addActionListener(buttonHandler);
			this.add(button[i]);
			button[i].setId(i);
			button[i].setBounds(100, 100+30*i, 150, 25);

		}
		
			button[0].setLabel("openInValve");
			button[1].setLabel("closeInValve");
			button[2].setLabel("openOutValve");
			button[3].setLabel("closeOutValve");
			button[4].setLabel("getHighLevelSensor");
			button[5].setLabel("getLowLevelSensor");
			button[6].setLabel("setMixerOn");
			button[7].setLabel("setMixerOff");
			button[8].setLabel("setHeaterOn");
			button[9].setLabel("setHeaterOff");

		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------

	}// end constructor
	
	
	// ##########################################################
	class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int buttonNoPressed=((ButtonWithID) e.getSource()).getId();
			System.out.println("pressed button No. "+buttonNoPressed);
			switch(buttonNoPressed){
			case 0:
				siloSimulator.setInValveState(ValveState.state.OPEN);
				break;
			case 1:
				siloSimulator.setInValveState(ValveState.state.CLOSED);
				break;
			case 2:
				siloSimulator.setOutValveState(ValveState.state.OPEN);
				break;
			case 3:
				siloSimulator.setOutValveState(ValveState.state.CLOSED);
				break;
			case 4:
				System.out.println("HighLevelSensor="+siloSimulator.getHighLevelSensorOutput().toString());
				break;
			case 5:
				System.out.println("LowLevelSensor="+siloSimulator.getLowLevelSensorOutput().toString());
				break;
			case 6:
				siloSimulator.setMixerState(MixerState.state.MIXING);
				break;
			case 7:
				siloSimulator.setMixerState(MixerState.state.NOTMIXING);
				break;
			case 8:
				siloSimulator.setHeaterState(HeaterState.state.HEATING);
				break;
			case 9:
				siloSimulator.setHeaterState(HeaterState.state.NOTHEATING);
				break;
				
			}

		}

	}


	@Override
	public void lowLevelSensorOutputChanged(liquerPlant.core.LevelSensorOutputState.state newState) {
		System.out.println("lowLevelCallback:newState="+newState.toString());	
		
	}


	@Override
	public void highLevelSensorOutputChanged(liquerPlant.core.LevelSensorOutputState.state newState) {
		System.out.println("highLevelCallback:newState="+newState.toString());
		
	}


}
