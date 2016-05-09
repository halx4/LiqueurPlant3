package liquerPlant.server.core;

import java.util.Iterator;
import java.util.Set;

import liquerPlant.core.LwM2mResourceParser;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.ObservationListener;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;

public class ObservationCreator {
	private LeshanServer server;

	public ObservationCreator(LeshanServer server) {
		super();
		this.server = server;
	}
	//------------------------------------
	public boolean addObservationListener(String clientEndpoint,int objectID,int instanceID,int resourceID,ObservationListener listener){
		Client client=getClientByIdentifier(clientEndpoint);
		//LwM2mResponse response=server.send(client, new ObserveRequest(objectID,instanceID,resourceID));
		//System.out.println("observer request:responseCode= "+response.getCode().toString()+"  responsePayload= "+LwM2mResourceParser.valueOf(response));
		Set<Observation> clientObservations=server.getObservationRegistry().getObservations(client);
		Iterator<Observation> iter=clientObservations.iterator();
		
		Observation myObservation=null;
		while(iter.hasNext() && myObservation==null){
			Observation examiningObservation=iter.next();
			if(observationMatches(examiningObservation,objectID,instanceID,resourceID))myObservation=examiningObservation;
		}
		
		if(myObservation==null)return false;
		else{
			myObservation.addListener(listener);
			return true;
		}
		
		
	}
	//-------------------------------------
	public boolean establishObservation(String clientEndpoint,int objectID,int instanceID,int resourceID){
		Client client=getClientByIdentifier(clientEndpoint);
		LwM2mResponse response=null;
	

		response=server.send(client, new ObserveRequest(objectID,instanceID,resourceID));

	
		System.out.println("observer request:responseCode= "+response.getCode().toString()+"  responsePayload= "+LwM2mResourceParser.valueOf(response));
		Set<Observation> clientObservations=server.getObservationRegistry().getObservations(client);
		Iterator<Observation> iter=clientObservations.iterator();
		
		Observation myObservation=null;
		while(iter.hasNext() && myObservation==null){
			Observation examiningObservation=iter.next();
			if(observationMatches(examiningObservation,objectID,instanceID,resourceID))myObservation=examiningObservation;
		}
		
		if(myObservation==null){
			System.out.println("NO observation found");
			return false;
			}
		else{
			//myObservation.addListener(listener);
			return true;
		}
		
		
	}
	
	//-------------------------------
	private Client getClientByIdentifier(String endpoint){
				return server.getClientRegistry().get(endpoint);
	}
	
	//-------------------------------
	private boolean observationMatches(Observation observation,int objectID,int instanceID,int resourceID){
		if(	observation.getPath().getObjectId() 		== objectID &&
			observation.getPath().getObjectInstanceId() == instanceID &&
			observation.getPath().getResourceId() 		== resourceID){
				return true;
		}
		else return false;
	}


}





