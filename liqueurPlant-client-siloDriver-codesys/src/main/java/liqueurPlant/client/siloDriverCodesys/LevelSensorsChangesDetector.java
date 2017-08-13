package liqueurPlant.client.siloDriverCodesys;

import liqueurPlant.core.LevelSensorOutputState;

class LevelSensorsChangesDetector implements Runnable {

	
	private SiloCodesysDriver driver;
	private int checkForChangesInterval;
	private volatile boolean run=true;
	
	

	public LevelSensorsChangesDetector(SiloCodesysDriver siloCodesysDriver, int checkForChangesInterval) {
		this.driver = siloCodesysDriver;
		this.checkForChangesInterval = checkForChangesInterval;
	}


	public static LevelSensorsChangesDetector create(SiloCodesysDriver siloCodesysDriver, int checkForChangesInterval) {
		LevelSensorsChangesDetector detector=new LevelSensorsChangesDetector(siloCodesysDriver,checkForChangesInterval);
		Thread thread=new Thread(detector);
		thread.setDaemon(false);
		thread.start();
		return detector;
	}


	@Override
	public void run() {
		char[] prevState=new char[2];
		char[] curState=new char[2];
		boolean changeDetected;
		
		//initialize state
		prevState[0]='0';
		prevState[1]='0';

		while(run){
			changeDetected=false;
			//System.out.println("change detection cycle");
			curState=driver.inputs.getRawLevelSensorsInputs();
			if(prevState[0] != curState[0] ){ //if high level sensor changed
				changeDetected=true;
				//System.out.println("curstate[0]="+curState[0]);
				if(curState[0]=='1')driver.highLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDDETECTED);
				else driver.highLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
			}
			
			if(prevState[1] != curState[1] ){	//if low level sensor changed
				changeDetected=true;
				//System.out.println("curstate[1]="+curState[1]);
				if(curState[1]=='1')driver.lowLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDDETECTED);
				else driver.lowLevelSensorOutputChanged(LevelSensorOutputState.state.LIQUIDNOTDETECTED);
			}
			if(changeDetected){
					System.arraycopy(curState, 0, prevState, 0, prevState.length);
					//System.out.println("LevelSensorChangesDetector: change detected!");
			}
			try {
				Thread.sleep(checkForChangesInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			}
		}
	}
	
	
	public void stop(){
		run=false;
	}
}
