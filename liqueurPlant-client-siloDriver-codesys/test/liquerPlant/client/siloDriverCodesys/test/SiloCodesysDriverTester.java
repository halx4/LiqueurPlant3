package liquerPlant.client.siloDriverCodesys.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import liquerPlant.client.siloDriverCodesys.SiloCodesysDriver;
import liquerPlant.core.LevelSensorOutputState.state;
import liquerPlant.core.HeaterState;
import liquerPlant.core.MixerState;
import liquerPlant.core.SiloControllerInterface;
import liquerPlant.core.ValveState;

@SuppressWarnings("serial")
public class SiloCodesysDriverTester extends Frame implements SiloControllerInterface{
	public static final String inPipe="cod2j";
	public static final String outPipe="j2cod";
	public static final int sendInterval=1000;
	public static final int receiveInterval=200;
	public static final int checkForChangesInterval=200;

	private final int buttonsNo=11;
	private ButtonWithID[] button=new ButtonWithID[buttonsNo];
	private ButtonHandler buttonHandler;
	private SiloCodesysDriver siloSimulator;
	private Label observeLabel;
	
	
	public SiloCodesysDriverTester() {
		buttonHandler=new ButtonHandler();
		
		siloSimulator = new SiloCodesysDriver(new File(inPipe),new File(outPipe),sendInterval,receiveInterval,checkForChangesInterval);
		siloSimulator.setController(this);
		
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
			button[10].setLabel("get Temperature");

		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------

		
		siloSimulator.begin();
		
	}

	public static void main(String[] args) {
		new SiloCodesysDriverTester();
	}

	@Override
	public void lowLevelSensorOutputChanged(state newState) {
		System.out.println("Low Level Changed to "+newState);
	}

	@Override
	public void highLevelSensorOutputChanged(state newState) {
		// TODO Auto-generated method stub
		System.out.println("High Level Changed to "+newState);
	}
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
			case 10:
				System.out.println("Temperature="+siloSimulator.getTemperature().getValue());
				
			}

		}

	}


	
}
