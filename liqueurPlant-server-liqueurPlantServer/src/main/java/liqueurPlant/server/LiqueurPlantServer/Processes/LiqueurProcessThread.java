package liqueurPlant.server.LiqueurPlantServer.Processes;

public class LiqueurProcessThread extends Thread {
	private int processID;

	public LiqueurProcessThread(int processID) {
		super();
		this.processID = processID;
	}

	public int getProcessID() {
		return processID;
	}
	

	
}
