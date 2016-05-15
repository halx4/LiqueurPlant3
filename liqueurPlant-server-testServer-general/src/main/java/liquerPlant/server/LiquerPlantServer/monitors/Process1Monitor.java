package liquerPlant.server.LiquerPlantServer.monitors;

import liquerPlant.core.HeaterState;
import liquerPlant.core.LwM2mResourceParser;
import liquerPlant.core.MixerState;
import liquerPlant.core.SmartSiloState;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;


public class Process1Monitor extends  ProcessMonitorAbstract{
	
	
	public Process1Monitor(int monitorID, LeshanServer server,
			Client siloInClient, Client siloOutClient, Client pipeClient,
			Client powerClient) {
		super(monitorID, server, siloInClient, siloOutClient, pipeClient, powerClient);
	}

	@Override
	public synchronized void newValue(Observation observation, LwM2mNode value) {
		String val=LwM2mResourceParser.valueOf(value);//val is the value of LwM2mNode only.


		if(observationMatches(observation,siloInClient,16663,0,0)){//silo In state			//DEPRECATED
			siloInState=SmartSiloState.valueOf(val);
			if(siloInState==SmartSiloState.EMPTY)transferComplete=true;
		}
		else if(observationMatches(observation,siloOutClient,16663,0,0)){//silo Out state	//DEPRECATED
			siloOutState=SmartSiloState.valueOf(val);
			if(siloOutState==SmartSiloState.FULL)transferComplete=true;
		}
		
		
		
		else if(observationMatches(observation,siloInClient,16663,0,7)){//silo in filling completed
			setSiloInFillingCompleted(Boolean.parseBoolean(val));
		}
		else if(observationMatches(observation,siloInClient,16663,0,8)){//silo in emptying completed
			setSiloInEmptyingCompleted(Boolean.parseBoolean(val));
			transferComplete=true;
		}
		else if(observationMatches(observation,siloOutClient,16663,0,7)){//silo Out filling completed
			setSiloOutFillingCompleted(Boolean.parseBoolean(val));
			transferComplete=true;
		}
		else if(observationMatches(observation,siloOutClient,16663,0,8)){//silo Out emptying completed
			setSiloOutEmptyingCompleted(Boolean.parseBoolean(val));
			//transferComplete=true;
			//@ above line : RIP nasty bug!
		}
		else if(observationMatches(observation,siloOutClient,16663,0,9)){//silo Out heating completed
			setSiloOutHeatingCompleted(Boolean.parseBoolean(val));
		}
		else if(observationMatches(observation,siloOutClient,16663,0,10)){//silo Out mixing completed
			setSiloOutMixingCompleted(Boolean.parseBoolean(val));
		}
		
		
		else if(observationMatches(observation,pipeClient,16666,0,0)){//pipe owner
			pipeOwner=val;
		}
		else if(observationMatches(observation,powerClient,16666,0,0)){//power owner
			powerOwner=val;
		}
		else if(observationMatches(observation,siloOutClient,16667,0,5850)){//silo out mixer state
			mixerState=MixerState.boolean2MixerState(Boolean.parseBoolean(val));
			if(mixerState==MixerState.state.NOTMIXING)mixComplete=true;
		}
		else if(observationMatches(observation,siloOutClient,16668,0,5850)){//silo Out Heater state
			System.out.println("                                  HEATER STATE - >"+val);
			heaterState=HeaterState.boolean2HeaterState(Boolean.parseBoolean(val)); 
			if(heaterState==HeaterState.state.NOTHEATING)heatComplete=true;
		}
		else {
			System.err.print("UNEXPECTED OBSERVATION RECEIVED :");
			System.err.println(server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId()+" = "+val);

		}
		
		System.out.println("MON"+ monitorID   +" OBSERVE:"+server.getClientRegistry().findByRegistrationId(observation.getRegistrationId()).getEndpoint()+" "+observation.getPath().getObjectId()+" "+observation.getPath().getObjectInstanceId()+" "+observation.getPath().getResourceId()+" = "+val);
		System.out.println("        siloInState="+siloInState+" siloOutState="+siloOutState +" pipeOwner="+pipeOwner+" powerOwner="+powerOwner+" transferCompl="+transferComplete+" heatCompl="+heatComplete+" mixCompl="+mixComplete+" heaterState="+heaterState+" mixerState="+mixerState);
		
		notifyAll();
	}
	
	
//------------------------
}
