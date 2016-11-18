package liqueurPlant.client.commonRes;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;

import liqueurPlant.client.core.ObserversUpdater;


public class CommonResourceEnabler extends BaseInstanceEnabler implements ObserversUpdater{
	   private CommonResourceController controller;
	
	   public CommonResourceEnabler(CommonResourceController controller) {
		   this.controller=controller;
		}
	   //---------------------------------------
	   @Override
	    public ReadResponse read(int resourceid) {
	        switch (resourceid) {
	        case 0://owner
	        		return ReadResponse.success(resourceid, controller.getCurrentOwner());
	        default:
	            return super.read(resourceid);
	        }
	    }
		//--------------------------------
	    @Override
	    public ExecuteResponse execute(int resourceid, String params) {
	    	System.out.println("Execute request received.Resource= "+resourceid+"  params= "+params);
	        switch (resourceid) {
	        case 1://acquire
	        	if(params!=null){//TODO try catch parse string to int.
	        		controller.requestReceived(new Request(params, RequestType.ACQUIRE));
	        		return ExecuteResponse.success();
	        	}
	        	else {
	        		//System.out.println("execute with no args received");
	        		return ExecuteResponse.badRequest("argument required");
	        	}
	        	
	        	
	        case 2://release
	        	if(params!=null){
	        		controller.requestReceived(new Request(params,RequestType.RELEASE));
	        		return ExecuteResponse.success();
	        	}
	        	else {
	        		return ExecuteResponse.badRequest("argument required");
	        	}
	        default:
	            return super.execute(resourceid,params);
	        }
	    }
	    //-------------------------------

		
		

}//end class
