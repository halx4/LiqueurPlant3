package liqueurPlant.server.testServer.general.Processes;

public class LiqueurPlantThread extends Thread {
	private int processID;

	public LiqueurPlantThread(int processID) {
		super();
		this.processID = processID;
	}

	public int getProcessID() {
		return processID;
	}
	

	
}
