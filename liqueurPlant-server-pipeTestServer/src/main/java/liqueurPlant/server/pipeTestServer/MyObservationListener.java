package liqueurPlant.server.pipeTestServer;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.ObservationListener;

import liqueurPlant.core.LwM2mResourceParser;

public class MyObservationListener implements ObservationListener {

	@Override
	public void cancelled(Observation observation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newValue(Observation observation, LwM2mNode value) {
		System.out.println("ObservationListenerTriggered-newValue="+LwM2mResourceParser.valueOf(value));
		
		
	}

}
