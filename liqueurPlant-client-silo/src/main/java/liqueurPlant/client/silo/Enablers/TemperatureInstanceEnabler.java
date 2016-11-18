package liqueurPlant.client.silo.Enablers;

import liqueurPlant.client.core.ObserversUpdater;
import liqueurPlant.client.silo.SiloController;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;

public class TemperatureInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater{

	private SiloController siloController;
	
	
	public TemperatureInstanceEnabler(SiloController smartSilo) {
		this.siloController=smartSilo;

	}

	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on temperature, resource " + resourceid);
        switch (resourceid) {
         case 5700://  sensor value
        	return ReadResponse.success(resourceid,  siloController.getTemperature().getValue()	);
        default:
            return super.read(resourceid);
        }
    }
	//---------------------	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
