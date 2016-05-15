package liqueurPlant.client.silo.Enablers.LevelSensorEnablers;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;

import liqueurPlant.client.core.ObserversUpdater;
import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.LevelSensorOutputState;

public abstract class LevelSensorInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater {
	private SiloController siloController;
	
	
	public LevelSensorInstanceEnabler(SiloController siloController) {
		// TODO Auto-generated constructor stub
		this.siloController=siloController;
	}
	
	
	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on LevelSensor, resource " + resourceid);
        switch (resourceid) {
        case 5751://sensor type
        	return ReadResponse.success(resourceid, "Level Sensor");
        case 5550://digital OutputState
        	return ReadResponse.success(resourceid, LevelSensorOutputState.levelSensorOutputState2Boolean(getLevelSensorOutput())  );
        default:
            return super.read(resourceid);
        }
    }
	
	
	protected abstract LevelSensorOutputState.state getLevelSensorOutput();
	
	
	protected SiloController getSiloController(){
		return siloController;
		}
	
}
