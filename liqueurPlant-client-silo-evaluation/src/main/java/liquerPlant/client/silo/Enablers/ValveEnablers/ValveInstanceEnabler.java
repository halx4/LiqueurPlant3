package liquerPlant.client.silo.Enablers.ValveEnablers;

import liquerPlant.client.core.ObserversUpdater;
import liquerPlant.client.silo.SiloController;
import liquerPlant.core.ValveState;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
	
public abstract class ValveInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater{
	private SiloController siloController;

	
	
	public ValveInstanceEnabler(SiloController siloController){
		this.siloController=siloController;
	}
	
	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on Valve, resource " + resourceid);
        switch (resourceid) {
        case 5850://  on/off
        	return ReadResponse.success(resourceid, ValveState.valveState2Boolean( getValveState())	);
        default:
            return super.read(resourceid);
        }
    }
	//---------------------
    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 5850:
            //System.out.println("value to string= "+value.toString());
            //System.out.println("value parsed= "+LwM2mResourceParser.valueOf(value));//TODO handle parser exception
            if(setValveState(    ValveState.boolean2ValveState((boolean) value.getValue()))   )return WriteResponse.success();
            else return WriteResponse.unauthorized();
        default:
            return super.write(resourceid, value);
        }
    }
	
	
	
	protected SiloController getSiloController(){
		return siloController;
		}
	
	protected abstract ValveState.state getValveState();
	protected abstract boolean setValveState(ValveState.state newState);

	
}
