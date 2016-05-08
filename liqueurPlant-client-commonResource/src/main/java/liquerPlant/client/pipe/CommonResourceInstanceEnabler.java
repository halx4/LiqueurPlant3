package liquerPlant.client.pipe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.util.LinkedList;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;


public class CommonResourceInstanceEnabler extends BaseInstanceEnabler{
		private CommonResource commonResource;
		private LinkedList<CommonResourceRequest> requests;
		private PipeGui pipeGui;
		
	
	   public CommonResourceInstanceEnabler(String endpointName) {
			super();
			commonResource = new CommonResource();
			requests = new LinkedList<CommonResourceRequest>();
			pipeGui=new PipeGui(endpointName);
			pipeGui.updateGui();
		}
	   //---------------------------------------
	@Override
	    public ReadResponse read(int resourceid) {
	       // System.out.println("Read on Pipe, resource " + resourceid);
	        switch (resourceid) {
	        case 0://owner
	        	//System.out.println("Pending acquires: "+requests.size());
	        	if(commonResource.isOwned())
	        		return ReadResponse.success(resourceid, Integer.toString(commonResource.getOwnerID()));
	        	else 
	        		return ReadResponse.success(resourceid, "NONE");
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
	        		requestReceived(new CommonResourceRequest(Integer.parseInt(params), CommonResourceRequestType.ACQUIRE));
	        		return ExecuteResponse.success();
	        	}
	        	else {
	        		//System.out.println("execute with no args received");
	        		return ExecuteResponse.badRequest("argument required");
	        	}
	        	
	        	
	        case 2://release
	        	//System.out.println("     exe and is RELEASE");
	        	if(params!=null){
	        		requestReceived(new CommonResourceRequest(Integer.parseInt(params),CommonResourceRequestType.RELEASE));
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
		private synchronized void requestReceived(CommonResourceRequest request){
			if(request.type()==CommonResourceRequestType.ACQUIRE){//type is ACQUIRE
				addNewRequest(request);
					
			}
			else{//type is RELEASE
				//System.out.println("  RELEASE received");
				if(commonResource.getOwnerID()==request.requesterID()){//is the owner so has access to release
					commonResource.removeOwner();
			
					if(!requests.isEmpty()){//if requests pending
						commonResource.setOwnerID(requests.removeFirst().requesterID());
					}
					//fire(); touch1
				}
			}
	    	fire();//touch1
	    		    	
	    }
		
		
		/*
		 * returns true if a new request was added on the requests list
		 */
		private boolean addNewRequest(CommonResourceRequest request) {
			requests.add(request);
			pipeGui.updateGui();
			if(!commonResource.isOwned()){
				System.out.println("resource was not owned ->immediate acquire");
				commonResource.setOwnerID(requests.removeFirst().requesterID());
				//fire(); //touch1
			}
			return true;
		}
		//---------------------
		private void fire(){
			pipeGui.updateGui();
			System.out.println("updating observers----------------------owner= "+commonResource.getOwnerID());
			fireResourcesChange(0);
			System.out.println("updating Finished");
		}
		
		
		//########################################
		 @SuppressWarnings("serial")
		class PipeGui extends Frame {
				
				private Label currentOwnerL,pendingRequestsL;
				private final Color noOwnerColor  =	new Color(255,51,51);
				private final Color hasOwnerColor =	new Color(128,255,0);
				private final Color pendingRequestsLColor=new Color(204,255,204);
				
				PipeGui(String name){
				
					
					this.setTitle(name);
					this.setLayout(null);
					this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
					this.setBackground(new Color(192,192,192));
					setBounds(1200,500,270,170);

					this.toFront();
					this.setResizable(true);
					

					currentOwnerL = new Label();
			        add(currentOwnerL);
			        currentOwnerL.setBounds(30, 60, 170, 20);
			        currentOwnerL.setVisible(true);
			        
			        pendingRequestsL = new Label();
			        add(pendingRequestsL);
			        pendingRequestsL.setBounds(30, 90, 170, 20);
			        pendingRequestsL.setBackground(pendingRequestsLColor);
			        pendingRequestsL.setVisible(true);

					// ---------------------------
					this.setVisible(true); // ----Frame setVisible
					// ---------------------------

				}
				
				//-------------------------------------
				void updateGui(){
					if(commonResource.isOwned()){//has owner
						this.currentOwnerL.setBackground(hasOwnerColor);
						this.currentOwnerL.setText("Owner ID: "+commonResource.getOwnerID());
					}
					else{//no owner
						this.currentOwnerL.setBackground(noOwnerColor);
						this.currentOwnerL.setText("Owner ID: -"); 
					}
					
					String pendingRequestsString=new String("Pending Requests: "+requests.size());
					//Iterator<CommonResourceRequest> iter=requests.iterator();
					//while(iter.hasNext()){
					//	pendingRequestsString=pendingRequestsString.concat(" "+iter.next().requesterID());
					//}
					//System.out.println("Gui requests string: "+pendingRequestsString);
					pendingRequestsL.setText(pendingRequestsString);
				}
				
				
				
			}//end nested class
		

}//end class
