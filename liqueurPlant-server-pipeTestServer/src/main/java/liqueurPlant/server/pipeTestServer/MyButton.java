package liqueurPlant.server.pipeTestServer;

import java.awt.Button;

@SuppressWarnings("serial")
public class MyButton extends Button {
	private int id;

	public MyButton(String name) {
		super(name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
