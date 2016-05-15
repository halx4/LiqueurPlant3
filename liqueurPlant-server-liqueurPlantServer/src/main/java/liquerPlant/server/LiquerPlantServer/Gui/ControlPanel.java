package liquerPlant.server.LiquerPlantServer.Gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import liquerPlant.server.LiquerPlantServer.LiquerPlantServer;
import liquerPlant.utilities.ExitHandler;

@SuppressWarnings("serial")
public class ControlPanel extends Frame {
	private final int buttonsNo=2;
	private LiquerPlantServer server;
	private Button[] button=new Button[buttonsNo];
	private ButtonHandler buttonHandler;

	public ControlPanel(LiquerPlantServer server) {
		buttonHandler=new ButtonHandler();
		this.server = server;

		this.setTitle("Server Control Panel");
		this.setLayout(null);
		this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		this.addWindowListener(new ExitHandler()); 
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
		

			button[0].setLabel("bind Observe");
			button[1].setLabel("start processes");
			


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
