package liqueurPlant.server.testServer.general.Processes;

public class LiquerPlantThread extends Thread {
	private int processID;

	public LiquerPlantThread(int processID) {
		super();
		this.processID = processID;
	}

	public int getProcessID() {
		return processID;
	}
	

	
}