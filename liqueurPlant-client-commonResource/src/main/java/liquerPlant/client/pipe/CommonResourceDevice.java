
package liquerPlant.client.pipe;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import liquerPlant.client.core.MyInstanceEnablerFactory;

import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.resource.LwM2mInstanceEnabler;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.response.RegisterResponse;


public class CommonResourceDevice {
    private int supportedObjectsIds[]={0,1,3,16666};
    private String endpointName;
    private MyInstanceEnablerFactory enablersFactory;

    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("check arguments!");
            
        } else {
                new CommonResourceDevice(args);
        }
    }

    public CommonResourceDevice(String[] args) {

    	endpointName=args[0];
    	System.out.println("endpoint name = "+endpointName);
    	
    	enablersFactory=new MyInstanceEnablerFactory();

    	
        // Initialize object list
    	List<ObjectModel> defaultObjectModels=ObjectLoader.loadDefault();
    	
    	File f=new File("src"+File.separator+"main"+File.separator+"resources");
    	//System.out.println(f.exists());
    	List<ObjectModel> customObjectModels=ObjectLoader.load(f);
    	
    	List<ObjectModel> allObjectModelsList = new ArrayList<ObjectModel>();
    	allObjectModelsList.addAll(defaultObjectModels);
    	allObjectModelsList.addAll(customObjectModels);
    	
    	

    	HashMap<Integer,ObjectModel> allObjectModels=new HashMap<Integer,ObjectModel>();
    	
    	Iterator<ObjectModel> allObjectModelsListIterator= allObjectModelsList.iterator();
    	while(allObjectModelsListIterator.hasNext()){
    		ObjectModel tempObjModel=allObjectModelsListIterator.next();
    		if(isSupported(tempObjModel.id)){
    			//System.out.println("ADDED->"+tempObjModel.id);
    			allObjectModels.put(tempObjModel.id,tempObjModel);	
    		
    		}
    	}

    	//System.out.println("allObjectModels.size= "+allObjectModels.size());
    	LwM2mModel lwM2mModel=new LwM2mModel(allObjectModelsList);
    	
    	
    	
    	ObjectsInitializer initializer = new ObjectsInitializer(lwM2mModel);//lwM2mModel
    	
        initializer.setClassForObject(3, DeviceEnabler.class);

        //List<ObjectEnabler> enablers = initializer.createMandatory();
        List<ObjectEnabler> enablers = initializer.create(0,1,3);
        
        
        //---------------16666------------
        ObjectModel objectModel6666=allObjectModels.get(16666);
        CommonResourceInstanceEnabler commonResourceInstanceEnabler=new CommonResourceInstanceEnabler(endpointName);
        
        //commonResourceInstanceEnabler.setObjectModel(objectModel6666);
                
        HashMap<Integer,LwM2mInstanceEnabler> instances16666=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16666.put(0, commonResourceInstanceEnabler);
        ObjectEnabler objectEnabler16666=new ObjectEnabler(16666, objectModel6666, instances16666, enablersFactory);
        enablers.add(objectEnabler16666);

        for(int i=1;i<args.length;i=i+4){
			
			        // Create client
			        final InetSocketAddress clientAddress = new InetSocketAddress(args[i], Integer.parseInt(args[i+1]));
			        final InetSocketAddress serverAddress = new InetSocketAddress(args[i+2], Integer.parseInt(args[i+3]));
			
			        final LeshanClient client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
			                enablers));
			
			        // Start the client
			        client.start();
			
			
			        RegisterResponse response = client.send(new RegisterRequest(endpointName));
			
			        // Report registration response.
			        System.out.println("Device Registration (Success? " + response.getCode() + ")");
			        if (response.getCode() == ResponseCode.CREATED) {
			            System.out.println("\tDevice: Registered Client Location '" + response.getRegistrationID() + "'");
			            response.getRegistrationID();
			        } else {
			            // TODO Should we have a error message on response ?
			            // System.err.println("\tDevice Registration Error: " + response.getErrorMessage());
			            System.err.println("\tDevice Registration Error: " + response.getCode());
			            System.err
			                    .println("If you're having issues connecting to the LWM2M endpoint, try using the DTLS port instead");
			        }
        }

    }
    
    private boolean isSupported(int id){
    	boolean supported=false;
    	int i=0;
    	while(i<supportedObjectsIds.length && supported==false){
    		if(supportedObjectsIds[i]==id)supported=true;
    		i++;
    	}
    	return supported;
    	
    }


}
