package liquerPlant.server.LiquerPlantServer.monitors;

import liquerPlant.core.HeaterState;
import liquerPlant.core.MixerState;
import liquerPlant.core.SmartSiloState;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.ObservationListener;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;


public abstract class ProcessMonitorAbstract implements ObservationListener {
	protected int monitorID;
	
	protected Client siloInClient;

	protected Client siloOutClient;

	protected Client pipeClient;

	protected Client powerClient;
	protected LeshanServer server;
	
	protected SmartSiloState siloInState=SmartSiloState.EMPTY;
	protected SmartSiloState siloOutState=SmartSiloState.EMPTY; //TODO correct initializations
	
	protected String pipeOwner="NONE";											//
	protected String powerOwner="NONE";											//
	
	protected boolean transferComplete=false;										//
	protected boolean heatComplete=false;											//
	protected boolean mixComplete=false;											//
	
	protected HeaterState.state heaterState=HeaterState.state.NOTHEATING;			//
	protected MixerState.state mixerState=MixerState.state.NOTMIXING;				//
	
	private boolean siloInFillingCompleted=false,
					siloInEmptyingCompleted=true,
					siloInHeatingCompleted=false,
					siloInMixingCompleted=false,
	
					siloOutFillingCompleted=false,
					siloOutEmptyingCompleted=true,
					siloOutHeatingCompleted=false,
					siloOutMixingCompleted=false;
	


	public ProcessMonitorAbstract(int monitorID,LeshanServer server,Client siloInClient, Client siloOutClient, Client pipeClient, Client powerClient	) {
		super();
		this.siloInClient = siloInClient;
		this.siloOutClient = siloOutClient;
		this.server = server;
		this.pipeClient=pipeClient;
		this.powerClient=powerClient;
		this.monitorID=monitorID;
		


	}


	public  void sendSiloInFill() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,1));
	}
	
	public  void sendSiloOutFill() throws InterruptedException{
		server.send(siloOutClient, new ExecuteRequest(16663,0,1));
	}
	
	public  void sendSiloInEmpty() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,2));
	}
	
	public  void sendSiloOutEmpty() throws InterruptedException{
		server.send(siloOutClient, new ExecuteRequest(16663,0,2));
	}	

	public  void sendSiloInStop() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,3));
	}	

	public  void sendSiloOutStop() throws InterruptedException{
		server.send(siloOutClient, new ExecuteRequest(16663,0,3));
	}	
	
	public  void sendSiloInHeat() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,5));
	}
	public  void sendSiloOutHeat() throws InterruptedException{
		server.send(siloOutClient, new ExecuteRequest(16663,0,5));
	}
	
	public  void sendSiloOutMix() throws InterruptedException{
		server.send(siloOutClient, new ExecuteRequest(16663,0,6));
	}
	
	public  void sendAcquirePipe(String owner) throws InterruptedException{ 

		server.send(pipeClient, new ExecuteRequest(16666,0,1,owner));
	}
	
	public  void sendReleasePipe(String owner) throws InterruptedException{ 

		server.send(pipeClient, new ExecuteRequest(16666,0,2,owner));
	}
	
	public  void sendAcquirePower(String owner) throws InterruptedException{ 
		server.send(powerClient, new ExecuteRequest(16666,0,1,owner));	}
	
	public  void sendReleasePower(String owner) throws InterruptedException{ 
		server.send(powerClient, new ExecuteRequest(16666,0,2,owner));	}
	
	//----------------------------------
	public synchronized void waitForSiloInFillingCompleted(){
		
		while(! siloInFillingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloInFillingCompleted=false;
	}
	
	public synchronized void waitForSiloInEmptyingCompleted(){
		
		while(! siloInEmptyingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloInEmptyingCompleted=false;
	}
	
	public synchronized void waitForSiloInHeatingCompleted(){
		
		while(! siloInHeatingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloInHeatingCompleted=false;
	}
	
	public synchronized void waitForSiloInMixingCompleted(){
		
		while(! siloInMixingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloInMixingCompleted=false;
	}
	//----------------------------
	
	public synchronized void waitForSiloOutFillingCompleted(){
		
		while(! siloOutFillingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloOutFillingCompleted=false;
	}
	
	public synchronized void waitForSiloOutEmptyingCompleted(){
		
		while(! siloOutEmptyingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloOutEmptyingCompleted=false;
	}
	
	public synchronized void waitForSiloOutHeatingCompleted(){
		
		while(! siloOutHeatingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloOutHeatingCompleted=false;
	}
	
	public synchronized void waitForSiloOutMixingCompleted(){
		
		while(! siloOutMixingCompleted){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		siloOutMixingCompleted=false;
	}
	
	//----------------------------
	public synchronized void waitForSiloInState(SmartSiloState targetingState){
		while(siloInState!=targetingState){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitForSiloOutState(SmartSiloState targetingState){
		while(siloOutState!=targetingState){
			//System.out.println("current silo out state= "+siloOutState.toString());
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitForPipe(String targetingOwner){
		while(!targetingOwner.equals(pipeOwner)){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitForLiquerTransfer(){
		//TODO	
		while(!transferComplete){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized void waitForHeat(){
		while(!heatComplete){
			try {
				wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitForPower(String targetingOwner){
		while(!targetingOwner.equals(powerOwner)){
			try {
				wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			}
	}
	
	public synchronized void waitForMix(){
		while(!mixComplete){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	//----------------------------------
	public synchronized void initializeTransfer(){
		transferComplete=false;
		//notifyAll(); //TODO decide to uncomment this or not.
		}
	
	public synchronized void initializeHeat(){
		heatComplete=false;
		//notifyAll(); //TODO decide to uncomment this or not.
		}
	
	public synchronized void initializeMix(){
		mixComplete=false;
		//notifyAll(); //TODO decide to uncomment this or not.
		}
	//----------------------------------
	
	synchronized void setSiloInFillingCompleted(boolean siloInFillingCompleted) {
		this.siloInFillingCompleted = siloInFillingCompleted;
	}


	synchronized void setSiloInEmptyingCompleted(boolean siloInEmptyingCompleted) {
		this.siloInEmptyingCompleted = siloInEmptyingCompleted;
	}


	synchronized void setSiloInHeatingCompleted(boolean siloInHeatingCompleted) {
		this.siloInHeatingCompleted = siloInHeatingCompleted;
	}


	synchronized void setSiloInMixingCompleted(boolean siloInMixingCompleted) {
		this.siloInMixingCompleted = siloInMixingCompleted;
	}


	synchronized void setSiloOutFillingCompleted(boolean siloOutFillingCompleted) {
		this.siloOutFillingCompleted = siloOutFillingCompleted;
	}


	synchronized void setSiloOutEmptyingCompleted(boolean siloOutEmptyingCompleted) {
		this.siloOutEmptyingCompleted = siloOutEmptyingCompleted;
	}


	synchronized void setSiloOutHeatingCompleted(boolean siloOutHeatingCompleted) {
		this.siloOutHeatingCompleted = siloOutHeatingCompleted;
	}


	synchronized void setSiloOutMixingCompleted(boolean siloOutMixingCompleted) {
		this.siloOutMixingCompleted = siloOutMixingCompleted;
	}

	//-----------------------------
	@Override
	public abstract  void newValue(Observation observation, LwM2mNode value) ;//MUST BE INSTANTIATED SYNCHRONIZED
	
	@Override
	public void cancelled(Observation observation) {
		// TODO Auto-generated method stub
		System.err.println("observation cancelled : "+server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId());
		
	}

	protected boolean observationMatches(Observation observation,Client client,int objectID,int instanceID,int resourceID){
		if(	server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint().equals(client.getEndpoint())	&&
			observation.getPath().getObjectId() == objectID 					&&
			observation.getPath().getObjectInstanceId() == instanceID 			&&
			observation.getPath().getResourceId() == resourceID					){
				return true;
		}
		else return false;
	}
//------------------------
}
