package liqueurPlant.client.silo.Enablers;

import liqueurPlant.client.core.ObserversUpdater;
import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.Temperature;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

public class SiloInstanceEnabler extends BaseInstanceEnabler implements ObserversUpdater {
	private SiloController siloController;

	public SiloInstanceEnabler(SiloController siloController) {
		this.siloController=siloController;
	}
	
	@Override
    public ReadResponse read(int resourceid) {
        System.out.println("Read on Silo, resource " + resourceid);
        switch (resourceid) {
	        case 0://state
	        	return ReadResponse.success(resourceid, siloController.getSmartSiloState().toString()	);
	        case 7://filling completed
	        	return ReadResponse.success(resourceid, siloController.getFillingCompleted()	);
	        case 8://emptying completed
	        	return ReadResponse.success(resourceid, siloController.getEmptyingCompleted());
	        case 9://heating completed
	        	return ReadResponse.success(resourceid, siloController.getHeatingCompleted()	);
	        case 10://mixing completed
	        	return ReadResponse.success(resourceid, siloController.getMixingCompleted()	);
	        case 11://target temperature
	        	return ReadResponse.success(resourceid, siloController.getTargetTemperature().getValue()	);
	        	
	        	
	        default:
	            return super.read(resourceid);
        }
    }
	
	@Override
    public ExecuteResponse execute(int resourceid, String params) {
        System.out.println("Execute on resource " + resourceid + " params " + params);
        
        switch (resourceid) {
        case 1://fill
        	System.out.println("silo - FILL execution triggered");
        	siloController.fill();
        	//fireResourceChange(resourceid);
        	return ExecuteResponse.success();
        case 2://empty
        	System.out.println("silo - EMPTY execution triggered");
        	siloController.empty();
        	//fireResourceChange(resourceid);
        	return ExecuteResponse.success();
        case 3://stop
        	System.out.println("silo - STOP execution triggered");
        	siloController.stop();
        	//fireResourceChange(resourceid);
        	return ExecuteResponse.success();
        case 4://initialize
        	System.out.println("silo - INIT execution triggered");
        	siloController.initialize();
        	return ExecuteResponse.success();
        case 5://heat
        	System.out.println("silo - HEAT execution triggered");
        	siloController.heat();
        	return ExecuteResponse.success();
        case 6://mix
        	System.out.println("silo - MIX execution triggered");
        	siloController.mix();
        	return ExecuteResponse.success();
        default:
        	System.out.println("DEFAULT");
            return super.execute(resourceid,params);
        }

    }
    
	//---------------------
    @Override
    public WriteResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 11:// target temperature
             if(siloController.setTargetTemperature(  new Temperature((float) value.getValue()))) return WriteResponse.success();
            else return WriteResponse.unauthorized();
        default:
            return super.write(resourceid, value);
        }
    }
	
	
	
	
}
