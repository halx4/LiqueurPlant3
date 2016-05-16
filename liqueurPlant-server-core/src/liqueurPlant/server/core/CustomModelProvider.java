package liqueurPlant.server.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.model.LwM2mModelProvider;

public class CustomModelProvider implements LwM2mModelProvider {

	private LwM2mModel lwM2mModel;
	
	public CustomModelProvider(){
		
		List<ObjectModel> defaultObjectModels=ObjectLoader.loadDefault();
		
    	File f=new File("src"+File.separator+"main"+File.separator+"resources");
		
    	System.out.println("custom objects folder found? "+f.exists());
    	List<ObjectModel> customObjectModels=ObjectLoader.load(f);
    	
    	List<ObjectModel> allObjectModelsList = new ArrayList<ObjectModel>();
    	allObjectModelsList.addAll(defaultObjectModels);
    	allObjectModelsList.addAll(customObjectModels);


    	/*
    	HashMap<Integer,ObjectModel> allObjectModels=new HashMap<Integer,ObjectModel>();
    	
    	Iterator<ObjectModel> allObjectModelsListIterator= allObjectModelsList.iterator();
    	while(allObjectModelsListIterator.hasNext()){
    		ObjectModel tempObjModel=allObjectModelsListIterator.next();
    		allObjectModels.put(tempObjModel.id,tempObjModel);	
    		System.out.println("supported ObjModel: "+tempObjModel.id);
    	}
 	*/

    	
    	this.lwM2mModel=new LwM2mModel(allObjectModelsList);
		
	}
	
	
	@Override
	public LwM2mModel getObjectModel(Client client) {
		return lwM2mModel;
	}

}
