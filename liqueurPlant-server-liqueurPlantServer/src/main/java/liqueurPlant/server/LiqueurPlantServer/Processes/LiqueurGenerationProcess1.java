package liqueurPlant.server.LiqueurPlantServer.Processes;

import liqueurPlant.server.LiqueurPlantServer.monitors.Process1Monitor;

public class LiqueurGenerationProcess1 extends LiqueurProcessThread {
	private Process1Monitor monitor;
	private boolean continueProduction=true;

	public LiqueurGenerationProcess1(int processID,Process1Monitor monitor) {
		super(processID);
		this.monitor=monitor;
	}
	
	
	@Override
	public void run(){
		String id=Integer.toString( getProcessID());

		try{
			monitor.sendSiloInFill();
			while(continueProduction){
					monitor.sendSiloOutEmpty();
					monitor.waitForSiloOutEmptyingCompleted();
					monitor.waitForSiloInFillingCompleted();	
					
					monitor.sendAcquirePipe( id );
					monitor.waitForPipe(id);
					
					monitor.initializeTransfer();
					monitor.sendSiloInEmpty();
					monitor.sendSiloOutFill();
					monitor.waitForLiqueurTransfer();
					
					monitor.sendSiloInStop();
					monitor.sendSiloOutStop();
					monitor.sendReleasePipe(id);
					
					monitor.sendSiloInFill();
					monitor.sendSiloOutHeat();
					monitor.initializeHeat();
					monitor.waitForSiloOutHeatingCompleted();
					
					monitor.sendAcquirePower(id);
					monitor.waitForPower(id);
					
					monitor.sendSiloOutMix();
					monitor.initializeMix();
					monitor.waitForSiloOutMixingCompleted();
					
					monitor.sendReleasePower(id);									
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
}
