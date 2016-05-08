package liquerPlant.server.siloTestServer;

import liquerPlant.core.LwM2mResourceParser;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.ObservationListener;
import org.eclipse.leshan.server.californium.impl.LeshanServer;


public class MyObservationListener implements ObservationListener {

	LeshanServer server;
	
	public MyObservationListener(LeshanServer server) {
		this.server = server;
	}

	@Override
	public void cancelled(Observation observation) {
		// TODO Auto-generated method stub
		System.out.println("observation cancelled event");
	}

	@Override
	public void newValue(Observation observation, LwM2mNode value) {
		System.out.println("###########OBSERVATION RESPONSE = "+server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint() +" - "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId()+" val= " +LwM2mResourceParser.valueOf(value));
		
		
	}

}
