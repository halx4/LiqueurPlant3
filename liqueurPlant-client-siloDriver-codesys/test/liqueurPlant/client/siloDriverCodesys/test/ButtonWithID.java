package liqueurPlant.client.siloDriverCodesys.test;

import java.awt.Button;

@SuppressWarnings("serial")
public class ButtonWithID extends Button {
	private int id;

	public ButtonWithID(String name) {
		super(name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
