package liquerPlant.client.silo.Enablers;

import liquerPlant.client.core.ObserversUpdater;
import liquerPlant.client.silo.SiloController;
import liquerPlant.core.HeaterState;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

public class HeaterInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater{

	private SiloController smartSilo;
	
	
	public HeaterInstanceEnabler(SiloController smartSilo) {
		this.smartSilo=smartSilo;

	}

	
	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on Heater, resource " + resourceid);
        switch (resourceid) {
         case 5850://  on/off
        	return ReadResponse.success(resourceid,  HeaterState.heaterState2Boolean(smartSilo.getHeaterState()));
        default:
            return super.read(resourceid);
        }
    }
	//---------------------
    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 5850:// on/off
             if(smartSilo.setHeaterState(  HeaterState.boolean2HeaterState((boolean) value.getValue()))) return WriteResponse.success();
            else return WriteResponse. 	methodNotAllowed();
        default:
            return super.write(resourceid, value);
        }
    }
	
	
	
	
}
