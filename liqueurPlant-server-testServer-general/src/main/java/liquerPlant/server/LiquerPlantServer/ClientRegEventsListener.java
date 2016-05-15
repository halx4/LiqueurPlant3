package liquerPlant.server.LiquerPlantServer;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistryListener;

public class ClientRegEventsListener implements ClientRegistryListener{

	private LeshanServer server;
	
	
	public ClientRegEventsListener(LeshanServer server) {
		super();
		this.server = server;
	}

	@Override
	public void registered(Client client) {
		System.out.println("event: client registered. Name: "+client.getEndpoint());
		System.out.println("----starting supported objects list:");
		LwM2mModel lwM2mModel=server.getModelProvider().getObjectModel(client);
		Collection<ObjectModel> objectModels=lwM2mModel.getObjectModels();
		Iterator<ObjectModel> iter= objectModels.iterator();
		while(iter.hasNext()){
			ObjectModel tempObjModel=iter.next();
			System.out.println("    "+tempObjModel.id+"  "+tempObjModel.name);
			
			
		}
		System.out.println("----end supported objects list");
	}

	@Override
	public void updated(Client clientUpdated) {
		
		System.out.println("event client updated");

		
	}

	@Override
	public void unregistered(Client client) {
		System.out.println("event client unregistered: "+client.getEndpoint());
		
	}

}
