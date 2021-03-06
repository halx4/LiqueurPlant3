package liqueurPlant.server.LiqueurPlantServer.Processes;

import liqueurPlant.server.LiqueurPlantServer.monitors.Process2Monitor;

public class LiqueurGenerationProcess2 extends LiqueurProcessThread {
	private Process2Monitor monitor;
	private boolean continueProduction=true;
	private SecondaryThread secondaryThread;

	public LiqueurGenerationProcess2(int processID,Process2Monitor monitor) {
		super(processID);
		this.monitor=monitor;
	}
	
	
	@Override
	public void run(){
		String id=Integer.toString( getProcessID());

		try{
			monitor.sendSiloInFill();
			monitor.sendSiloOutEmpty();
			monitor.waitForSiloInFillingCompleted();
			
			monitor.sendSiloInHeat();
			monitor.initializeHeat();
			monitor.waitForSiloInHeatingCompleted();
			
			monitor.waitForSiloOutEmptyingCompleted();
			
			while(continueProduction){
					monitor.sendAcquirePipe(id);
					monitor.waitForPipe(id);
					
					monitor.initializeTransfer();
					monitor.sendSiloInEmpty();
					monitor.sendSiloOutFill();
					monitor.waitForLiqueurTransfer();
					
					monitor.sendSiloInStop();
					monitor.sendSiloOutStop();
					monitor.sendReleasePipe(id);
					
					monitor.sendSiloInFill();
					monitor.sendAcquirePower(id);
					
					secondaryThread=new SecondaryThread();
					secondaryThread.start();
						
					monitor.waitForPower(id);
						
					monitor.sendSiloOutMix();
					monitor.initializeMix();
					
					monitor.waitForSiloOutMixingCompleted();
						
					monitor.sendReleasePower(id);
					monitor.sendSiloOutEmpty();
						
					monitor.waitForSiloOutEmptyingCompleted();
					
					try {
						secondaryThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	
	
	}//end run
		
	class SecondaryThread extends Thread{

		
		SecondaryThread() {}
		
		@Override
		public void run(){
			try{	
				monitor.waitForSiloInFillingCompleted();
				
				monitor.initializeHeat();
				monitor.sendSiloInHeat();
				monitor.waitForSiloInHeatingCompleted();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			
			
		}
		
	}	
			
}
