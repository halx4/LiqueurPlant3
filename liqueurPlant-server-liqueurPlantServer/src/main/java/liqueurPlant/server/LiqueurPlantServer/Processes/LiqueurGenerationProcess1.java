package liqueurPlant.server.LiqueurPlantServer.Processes;

import liqueurPlant.server.LiqueurPlantServer.monitors.Process1Monitor;

public class LiqueurGenerationProcess1 extends LiqueurProcessThread {
	private Process1Monitor monitor;
	

	public LiqueurGenerationProcess1(int processID,Process1Monitor monitor) {
		super(processID);
		this.monitor=monitor;
	}
	
	
	@Override
	public void run(){
		String id=Integer.toString( getProcessID());
		System.out.println("starting LiquerProcess1. processID= "+getProcessID());
		try{
			monitor.sendSiloInFill();
													System.out.println("P"+id+" MARK 1");
			while(true){
					monitor.sendSiloOutEmpty();
													System.out.println("P"+id+" MARK 2");
					//monitor.waitForSiloOutState(SmartSiloState.EMPTY);
					monitor.waitForSiloOutEmptyingCompleted();
													System.out.println("P"+id+" MARK 3");
					//monitor.waitForSiloInState(SmartSiloState.FULL);
					monitor.waitForSiloInFillingCompleted();								
													System.out.println("P"+id+" MARK 4");
					monitor.sendAcquirePipe( id );
					monitor.waitForPipe(id);
													System.out.println("P"+id+" MARK 5");
					monitor.initializeTransfer();
					monitor.sendSiloInEmpty();
													System.out.println("P"+id+" MARK 6");
					monitor.sendSiloOutFill();
													System.out.println("P"+id+" MARK 7");
					monitor.waitForLiquerTransfer();
													System.out.println("P"+id+" MARK 8");
					monitor.sendSiloInStop();
					monitor.sendSiloOutStop();
													System.out.println("P"+id+" MARK 9");
					monitor.sendReleasePipe(id);
					monitor.sendSiloInFill();
					monitor.sendSiloOutHeat();
					monitor.initializeHeat();
													System.out.println("P"+id+" MARK 9");
					//monitor.waitForHeat();
					monitor.waitForSiloOutHeatingCompleted();
													System.out.println("P"+id+" MARK 10");
					monitor.sendAcquirePower(id);
													System.out.println("P"+id+" MARK 10A");
					monitor.waitForPower(id);
													System.out.println("P"+id+" MARK 11");
					monitor.sendSiloOutMix();
					monitor.initializeMix();
					//monitor.waitForMix();
					monitor.waitForSiloOutMixingCompleted();
													System.out.println("P"+id+" MARK 12");
					monitor.sendReleasePower(id);
													System.out.println("P"+id+" MARK 13");
													
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
		
		
		
	
	}
}
