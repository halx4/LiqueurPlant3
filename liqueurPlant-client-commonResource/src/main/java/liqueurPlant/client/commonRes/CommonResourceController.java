package liqueurPlant.client.commonRes;

import java.util.LinkedList;

import liqueurPlant.client.core.ObserversUpdater;

public class CommonResourceController {
	private Lock lock;
	private LinkedList<Request> requestsList;
	private CommonResourceGui commonResourceGui;
	private ObserversUpdater commonResourceInstanceEnabler;
	
	public CommonResourceController(String endpointName) {
		super();
		lock = new Lock();
		requestsList = new LinkedList<Request>();
		commonResourceGui=new CommonResourceGui(endpointName);
		
		updateGui();
	}
	
	public void setCommonResourceEnabler(ObserversUpdater commonResourceInstanceEnabler){
		this.commonResourceInstanceEnabler=commonResourceInstanceEnabler;
	}
	
	public String getCurrentOwner(){
		return lock.getOwnerID();
	}
	
	public synchronized void requestReceived(Request request){
		if(request.type()==RequestType.ACQUIRE){//type is ACQUIRE
			addNewRequest(request);
				
		}
		else{//type is RELEASE
			//System.out.println("  RELEASE received");
			if(lock.getOwnerID().equals(request.requesterID())){//is the owner so has access to release
				lock.removeOwner();
		
				if(!requestsList.isEmpty()){//if requests pending
					lock.setOwnerID(requestsList.removeFirst().requesterID());
				}
			}
		}
    	fireOwnerChange();

	}
	
	/*
	 * returns true if a new request was added on the requests list
	 */
	private boolean addNewRequest(Request request) {
		requestsList.add(request);
		if(!lock.isOwned()){
			System.out.println("resource was not owned ->immediate acquire");
			lock.setOwnerID(requestsList.removeFirst().requesterID());
		}
		updateGui();
		return true;
	}
	//---------------------
	private void fireOwnerChange(){
		updateGui();
		//System.out.println("updating observers----------------------owner= "+lock.getOwnerID());
		commonResourceInstanceEnabler.fireResourcesChange(0);
		//System.out.println("updating Finished");
	}
	//---------------------

	void updateGui(){
		commonResourceGui.update(lock.isOwned(),lock.getOwnerID(),requestsList);

	}
}
