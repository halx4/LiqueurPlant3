package liquerPlant.server.LiquerPlantServer.monitors;

import liquerPlant.core.LwM2mResourceParser;
import liquerPlant.core.SmartSiloState;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.ObservationListener;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;


public class Process1Monitor implements ObservationListener {
	protected int monitorID;
	
	protected Client siloInClient;

	protected LeshanServer server;
	
	protected SmartSiloState siloInState=SmartSiloState.EMPTY;
	
	//protected boolean transferComplete=false;

	private boolean siloInFillingCompleted=false,
					siloInEmptyingCompleted=false;

	public Process1Monitor(int monitorID,LeshanServer server,Client siloInClient) {
		super();
		this.siloInClient = siloInClient;
		this.server = server;
		this.monitorID=monitorID;
	}


	public  void sendSiloInFill() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,1));
	}
	

	public  void sendSiloInEmpty() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,2));
	}
	

	public  void sendSiloInStop() throws InterruptedException{
		server.send(siloInClient, new ExecuteRequest(16663,0,3));
	}	

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
	


	//----------------------------------
	
	synchronized void setSiloInFillingCompleted(boolean siloInFillingCompleted) {
		this.siloInFillingCompleted = siloInFillingCompleted;
	}


	synchronized void setSiloInEmptyingCompleted(boolean siloInEmptyingCompleted) {
		this.siloInEmptyingCompleted = siloInEmptyingCompleted;
	}


	//-----------------------------
	@Override
	public synchronized void newValue(Observation observation, LwM2mNode value) {
		String val=LwM2mResourceParser.valueOf(value);//val is the value of LwM2mNode only.


		if(observationMatches(observation,siloInClient,16663,0,0)){//silo In state			//DEPRECATED
			siloInState=SmartSiloState.valueOf(val);
			//if(siloInState==SmartSiloState.EMPTY)transferComplete=true;
		}	
		else if(observationMatches(observation,siloInClient,16663,0,7)){//silo in filling completed
			setSiloInFillingCompleted(Boolean.parseBoolean(val));
		}
		else if(observationMatches(observation,siloInClient,16663,0,8)){//silo in emptying completed
			setSiloInEmptyingCompleted(Boolean.parseBoolean(val));
			//transferComplete=true;
		}

		else {
			System.err.print("UNEXPECTED OBSERVATION RECEIVED :");
			System.err.println(server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId()+" = "+val);

		}
		
		//System.out.println("MON"+ monitorID   +" OBSERVE:"+server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId()+" = "+val);
		//System.out.println("        siloInState="+siloInState+" siloOutState="+siloOutState +" pipeOwner="+pipeOwner+" powerOwner="+powerOwner+" transferCompl="+transferComplete+" heatCompl="+heatComplete+" mixCompl="+mixComplete+" heaterState="+heaterState+" mixerState="+mixerState);
		
		notifyAll();
	}
	
	
	
	//-----------------------------
	@Override
	public void cancelled(Observation observation) {
		// TODO Auto-generated method stub
		System.err.println("observation cancelled : "+server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId());
		
	}

	protected boolean observationMatches(Observation observation,Client client,int objectID,int instanceID,int resourceID){
		if(	server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint().equals(client.getEndpoint())	&&
			observation.getPath().getObjectId() 		== objectID 	&&
			observation.getPath().getObjectInstanceId() == instanceID 	&&
			observation.getPath().getResourceId() 		== resourceID		){
				return true;
		}
		else return false;
	}
//------------------------
}
