package liqueurPlant.client.silo.Enablers;

import liqueurPlant.client.core.ObserversUpdater;
import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.MixerState;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

public class MixerInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater{

	private SiloController siloController;
	
	
	public MixerInstanceEnabler(SiloController siloController) {
		this.siloController=siloController;

	}

	
	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on Mixer, resource " + resourceid);
        switch (resourceid) {
        case 0:   //  time to operate
        	return ReadResponse.success(resourceid,  siloController.getMixerTimeToOperate()	);
        case 5850://  on/off
        	return ReadResponse.success(resourceid,  MixerState.mixerState2Boolean(siloController.getMixerState())	);
        default:
            return super.read(resourceid);
        }
    }
	//---------------------
    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 0: //time to operate
        	if(siloController.setMixerTimeToOperate((int) value.getValue()))return WriteResponse.success();
        	else return WriteResponse.unauthorized();
        case 5850:// on/off
             if(siloController.setMixerState(  MixerState.boolean2MixerState((boolean) value.getValue()))) return WriteResponse.success();
            else return WriteResponse.unauthorized();
        default:
            return super.write(resourceid, value);
        }
    }
	
	
	
	
}
