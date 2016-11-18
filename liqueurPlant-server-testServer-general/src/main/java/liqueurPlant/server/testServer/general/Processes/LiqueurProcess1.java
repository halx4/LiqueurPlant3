package liqueurPlant.server.testServer.general.Processes;

import liqueurPlant.server.testServer.general.monitors.Process1Monitor;

public class LiqueurProcess1 extends LiqueurPlantThread {
	private Process1Monitor monitor;

	public LiqueurProcess1(int processID, Process1Monitor monitor) {
		super(processID);
		this.monitor = monitor;
	}

	@Override
	public void run() {
		String id = Integer.toString(getProcessID());
		System.out.println("starting LiqueurProcess1. processID= " + getProcessID());
		try {

			while (true) {
				System.out.println("FILL request sent");
				monitor.sendSiloInFill();
				
				System.out.println("waiting for FILLING COMPLETED...");
				monitor.waitForSiloInFillingCompleted();
				System.out.println("FILLING COMPLETED event received!");
				
				System.out.println("EMPTY request sent");
				monitor.sendSiloInEmpty();
				
				System.out.println("waiting for EMPTYING COMPLETED...");
				monitor.waitForSiloInEmptyingCompleted();
				System.out.println("EMPTYING COMPLETED event received!");
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
