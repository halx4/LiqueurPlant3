package liqueurPlant.server.LiqueurPlantServer.Gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import liqueurPlant.server.LiqueurPlantServer.TestServerGeneral;
import liqueurPlant.utilities.ExitHandler;

@SuppressWarnings("serial")
public class ControlPanel extends Frame {
	private final int buttonsNo = 6;
	private final String CLIENT_DEFAULT_VALUE="SILO-1";
	private final String OBJECT_ID_DEFAULT_VALUE="16663";
	private final String OBJECT_INSTANCE_ID_DEFAULT_VALUE="0";
	private final String RESOURCE_ID_DEFAULT_VALUE="1";
	
	
	private TestServerGeneral server;
	private Button[] button = new Button[buttonsNo];
	private ButtonHandler buttonHandler;

	private TextField clientTF 			= new TextField();
	private TextField objIDTF 			= new TextField();
	private TextField objInstanceIDTF 	= new TextField();
	private TextField resourceTF 		= new TextField();
	private TextField valueTF 			= new TextField();

	private Label clientL 			= new Label("Client");
	private Label objIDL 			= new Label("ObjID");
	private Label objInstanceIDL 	= new Label("ObjInstID");
	private Label resourceL 		= new Label("ResID");
	private Label valueL 			= new Label("value");

	public ControlPanel(TestServerGeneral server) {
		buttonHandler = new ButtonHandler();
		this.server = server;

		this.setTitle("Test Server (GEN)");
		this.setLayout(null);
		this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
		this.setBackground(new Color(112, 135, 159));
		this.toFront();
		this.setResizable(true);
		this.addWindowListener(new ExitHandler()); 
		setBounds(50, 50, 300, 300 + buttonsNo * 30);



		int ySize1 = 25;
		int ySize2 = 25;
		int xSize1 = 70;
		int xSize2 = 150;
		int xPos1  = 30;
		int xPos2  = 120;

		this.add(clientL		);
		this.add(objIDL			);
		this.add(objInstanceIDL	);
		this.add(resourceL		);
		this.add(valueL			);

		clientL			.setBounds(xPos1, 60 + 0 * 30, xSize1, ySize1);
		objIDL			.setBounds(xPos1, 60 + 1 * 30, xSize1, ySize1);
		objInstanceIDL	.setBounds(xPos1, 60 + 2 * 30, xSize1, ySize1);
		resourceL		.setBounds(xPos1, 60 + 3 * 30, xSize1, ySize1);
		valueL			.setBounds(xPos1, 60 + 4 * 30, xSize1, ySize1);

		
		this.add(clientTF		);
		this.add(objIDTF		);
		this.add(objInstanceIDTF);
		this.add(resourceTF		);
		this.add(valueTF		);

		clientTF		.setBounds(xPos2, 60 + 0 * 30, xSize2, ySize2);
		objIDTF			.setBounds(xPos2, 60 + 1 * 30, xSize2, ySize2);
		objInstanceIDTF	.setBounds(xPos2, 60 + 2 * 30, xSize2, ySize2);
		resourceTF		.setBounds(xPos2, 60 + 3 * 30, xSize2, ySize2);
		valueTF			.setBounds(xPos2, 60 + 4 * 30, xSize2, ySize2);

		clientTF		.setText(CLIENT_DEFAULT_VALUE				);	
		objIDTF			.setText(OBJECT_ID_DEFAULT_VALUE			);	
		objInstanceIDTF	.setText(OBJECT_INSTANCE_ID_DEFAULT_VALUE	);
		resourceTF		.setText(RESOURCE_ID_DEFAULT_VALUE			);
		

		for (int i = 0; i < buttonsNo; i++) {
			button[i] = new Button("button" + i);
			button[i].addActionListener(buttonHandler);
			this.add(button[i]);
			button[i].setBounds(xPos2, 230 + 30 * i, xSize2, ySize1);

		}

		button[0].setLabel("READ"						);
		button[1].setLabel("WRITE"						);
		button[2].setLabel("EXECUTE"					);
		button[3].setLabel("bind observation"			);
		button[4].setLabel("bind std observations"		);
		button[5].setLabel("start fill-empty cycle"		);
		
		//TODO implement write and erase this line
		button[1].setEnabled(false);
		button[3].setEnabled(false);

		// ---------------------------
		this.setVisible(true); // ----Frame setVisible
		// ---------------------------

	}// end constructor

	// ##########################################################
	class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// System.out.println("GUI:refreshPressed");
			server.actionRequestFromControlPanel(((Button) e.getSource()).getLabel(),clientTF.getText(),objIDTF.getText(),
					objInstanceIDTF.getText(),resourceTF.getText(),valueTF.getText());

		}

	}


}
