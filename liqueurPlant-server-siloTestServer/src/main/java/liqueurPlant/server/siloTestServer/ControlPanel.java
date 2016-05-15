package liqueurPlant.server.siloTestServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ControlPanel extends Frame {
	private final int buttonsNo=25;
	private SiloTestServer server;
	private MyButton[] button=new MyButton[buttonsNo];
	private ButtonHandler buttonHandler;

	public ControlPanel(SiloTestServer server) {
		buttonHandler=new ButtonHandler();
		this.server = server;

		this.setTitle("Server Control Panel");
		this.setLayout(null);
		this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		this.setBackground(new Color(112, 135, 159));
		setBounds(50,50,200,100+buttonsNo*30);
		
		this.toFront();
		this.setResizable(true);


		for(int i=0;i<buttonsNo;i++){
			button[i]=new MyButton("button"+i);
			button[i].addActionListener(buttonHandler);
			this.add(button[i]);
			button[i].setId(i);
			button[i].setBounds(30, 60+30*i, 150, 25);

		}
		
		button[0].setLabel("Observe all");
		button[1].setLabel("fill");
		button[2].setLabel("empty");
		button[3].setLabel("stop");
		button[4].setLabel("heat");
		button[5].setLabel("mix");
		button[6].setLabel("open in valve");
		button[7].setLabel("close in valve");
		button[8].setLabel("open out valve");
		button[9].setLabel("close out valve");
		
		button[10].setLabel("setMixerTimer 2 sec");
		button[11].setLabel("setMixerTimer 10 sec");
		button[12].setLabel("read mix state");
		button[13].setLabel("set Mixer on");
		button[14].setLabel("set Mixer off");
		
		button[15].setLabel("setHeaterTimer 2 sec");
		button[16].setLabel("setHeaterTimer 10 sec");
		button[17].setLabel("read heat state");
		button[18].setLabel("set Heater on");
		button[19].setLabel("set Heater off");
		button[20].setLabel("get silo state");
		
		button[21].setLabel("get LowLevelState");
		button[22].setLabel("get highLevelState");
		
		/*	button[0].setLabel("fill");
			button[1].setLabel("empty");
			button[2].setLabel("stop");
			button[3].setLabel("read smart silo state");
			button[4].setLabel("read in valve");
			button[5].setLabel("OPEN in valve");
			button[6].setLabel("CLOSE in valve");
			button[7].setLabel("observer time request");
			button[8].setLabel("add listener for observe");
			button[9].setLabel("setMixerTime 10 sec");
			button[10].setLabel("setMixerTimer 2 sec");
			button[11].setLabel("read mix time");
			button[12].setLabel("set Mixer on");
			button[13].setLabel("set Mixer off");
			button[14].setLabel("read mixer state");
			button[15].setLabel("observe mixer state");
			button[16].setLabel("add listener for mixer state");
			button[17].setLabel("smart mix");
			//button[18].setLabel("");
			//button[19].setLabel("");
			//button[20].setLabel("");
			  
			 */
			 
			
			
			

		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------

	}// end constructor


	// ##########################################################
	class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("GUI:refreshPressed");
			server.function1( ((MyButton) e.getSource()).getId()   );

		}

	}




}
