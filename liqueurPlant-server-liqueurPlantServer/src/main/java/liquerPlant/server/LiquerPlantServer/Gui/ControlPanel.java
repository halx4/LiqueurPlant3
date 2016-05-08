package liquerPlant.server.LiquerPlantServer.Gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import liquerPlant.server.LiquerPlantServer.LiquerPlantServer;

@SuppressWarnings("serial")
public class ControlPanel extends Frame {
	private final int buttonsNo=20;
	private LiquerPlantServer server;
	private Button[] button=new Button[buttonsNo];
	private ButtonHandler buttonHandler;

	public ControlPanel(LiquerPlantServer server) {
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
			button[i]=new Button("button"+i);
			button[i].addActionListener(buttonHandler);
			this.add(button[i]);
			button[i].setBounds(30, 60+30*i, 150, 25);

		}
		
			button[0].setLabel("fill S1");
			button[1].setLabel("empty S1");
			button[2].setLabel("stop S1");
			button[3].setLabel("read smart silo state");
			button[4].setLabel("read in valve");
			button[5].setLabel("heat S1");
			button[6].setLabel("read S1 heater state");
			button[7].setLabel("bind Observe");
			button[8].setLabel("start processes");
			button[9].setLabel("fill S2");
			button[10].setLabel("empty S2");
			button[11].setLabel("fill S3");
			button[12].setLabel("empty S3");
			button[13].setLabel("fill S4");
			button[14].setLabel("empty S4");
			button[15].setLabel("discover 16663");
			button[16].setLabel("");


		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------

	}// end constructor


	// ##########################################################
	class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("GUI:refreshPressed");
			server.actionRequestFromControlPanel( ((Button) e.getSource()).getLabel()   );

		}

	}




}
