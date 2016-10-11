package liqueurPlant.server.LiqueurPlantServer.Processes;

import liqueurPlant.server.LiqueurPlantServer.monitors.Process2Monitor;

public class LiqueurGenerationProcess2 extends LiqueurProcessThread {
	private Process2Monitor monitor;
	
	private SubProcess subprocess;

	public LiqueurGenerationProcess2(int processID,Process2Monitor monitor) {
		super(processID);
		this.monitor=monitor;
	}
	
	
	@Override
	public void run(){
		String id=Integer.toString( getProcessID());
		System.out.println("starting LiquerProcess1. processID= "+getProcessID());
		try{
			monitor.sendSiloInFill();
			monitor.sendSiloOutEmpty();
			//monitor.waitForSiloInState(SmartSiloState.FULL);
			monitor.waitForSiloInFillingCompleted();
			
			monitor.sendSiloInHeat();
			monitor.initializeHeat();
			//monitor.waitForHeat();
			monitor.waitForSiloInHeatingCompleted();
			
			//monitor.waitForSiloOutState(SmartSiloState.EMPTY);
			monitor.waitForSiloOutEmptyingCompleted();
			
			while(true){
					System.out.println("P"+id+" MARK 1");
					monitor.sendAcquirePipe(id);
					System.out.println("P"+id+" MARK 2");
					monitor.waitForPipe(id);
					
					monitor.initializeTransfer();
					monitor.sendSiloInEmpty();
					monitor.sendSiloOutFill();
					System.out.println("P"+id+" MARK 3");
					monitor.waitForLiquerTransfer();
					
					
					
					monitor.sendSiloInStop();
					monitor.sendSiloOutStop();
					monitor.sendReleasePipe(id);
					
					monitor.sendSiloInFill();
					monitor.sendAcquirePower(id);
					System.out.println("P"+id+" MARK 4");
					
					//make the new thread
					subprocess=new SubProcess(getProcessID());
					subprocess.start();
						System.out.println("P"+id+" MARK 5");
						monitor.waitForPower(id);
						
						monitor.sendSiloOutMix();
						monitor.initializeMix();
						System.out.println("P"+id+" MARK 6");
						
						//monitor.waitForMix();
						monitor.waitForSiloOutMixingCompleted();
						
						monitor.sendReleasePower(id);
						monitor.sendSiloOutEmpty();
						System.out.println("P"+id+" MARK 7");
						
						//monitor.waitForSiloOutState(SmartSiloState.EMPTY);
						monitor.waitForSiloOutEmptyingCompleted();
					
					try {
						subprocess.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	
	
	}//end run
		
	class SubProcess extends LiqueurProcessThread{

		
		SubProcess(int processID) {
			
			super(processID);
			System.out.println("SUBPROCESS CREATED");
		}
		
		@Override
		public void run(){
			System.out.println("SUBPROCESS STARTED");
			try{	
				//monitor.waitForSiloInState(SmartSiloState.FULL);
				monitor.waitForSiloInFillingCompleted();
				
				monitor.initializeHeat();
				monitor.sendSiloInHeat();
				//monitor.waitForHeat();
				monitor.waitForSiloInHeatingCompleted();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			//die
			
			
		}
		
	}	
		
		
		
		
	
	
}
