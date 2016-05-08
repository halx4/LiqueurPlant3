package liquerPlant.server.pipeTestServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ControlPanel extends Frame {
	private final int buttonsNo=10;
	private PipeTestServer server;
	private MyButton[] button=new MyButton[buttonsNo];
	private ButtonHandler buttonHandler;

	public ControlPanel(PipeTestServer server) {
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
		
			button[0].setLabel("observe Request");
			button[1].setLabel("add observ. Listener");
			button[2].setLabel("acquire 0");
			button[3].setLabel("acquire 1");
			button[4].setLabel("release 0");
			button[5].setLabel("release 1");
			button[6].setLabel("Read owner");
			
			
			button[9].setLabel("read Time");


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
