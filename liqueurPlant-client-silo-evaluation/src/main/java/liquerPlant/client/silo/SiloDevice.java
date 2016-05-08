
package liquerPlant.client.silo;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import liquerPlant.client.core.MyInstanceEnablerFactory;
import liquerPlant.client.silo.Enablers.HeaterInstanceEnabler;
import liquerPlant.client.silo.Enablers.TemperatureInstanceEnabler;
import liquerPlant.client.silo.Enablers.MixerInstanceEnabler;
import liquerPlant.client.silo.Enablers.SiloInstanceEnabler;
import liquerPlant.client.silo.Enablers.LevelSensorEnablers.HighLevelSensorInstanceEnabler;
import liquerPlant.client.silo.Enablers.LevelSensorEnablers.LevelSensorInstanceEnabler;
import liquerPlant.client.silo.Enablers.LevelSensorEnablers.LowLevelSensorInstanceEnabler;
import liquerPlant.client.silo.Enablers.ValveEnablers.InValveInstanceEnabler;
import liquerPlant.client.silo.Enablers.ValveEnablers.OutValveInstanceEnabler;
import liquerPlant.client.silo.Enablers.ValveEnablers.ValveInstanceEnabler;
import liquerPlant.client.siloSimulator.SiloParameters;
import liquerPlant.client.siloSimulator.SiloSimulatorDriver;
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


public class SiloDevice {
    private String registrationID;
    private int supportedObjectsIds[]={0,1,3,3303,16663,16664,16665,16667,16668}; //TODO use this list
    private String endpointName;
    
    private SiloSimulatorDriver siloSimulator;
    private SiloController smartSilo;
    private MyInstanceEnablerFactory enablersFactory;
    
    public static void main(String[] args) {//args={endpointName lowLevelSensorHeight highLevelSensorHeight time2Fill-10%(ms) hasMixer hasHeater clientIP clientPort serverIP serverPort... }
        if (args.length == 0) {
            System.out.println("check arguments!");
            
        } else {
                new SiloDevice(args);
        }
    }

    public SiloDevice(String[] args) {
    	
    	endpointName=args[0];
    	System.out.println("endpoint name = "+endpointName);
    	
    	siloSimulator=new SiloSimulatorDriver(new SiloParameters(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Long.parseLong(args[3]),Boolean.parseBoolean(args[4]),Boolean.parseBoolean(args[5])) , endpointName);
    	smartSilo=new SiloController(siloSimulator);
    	
    	enablersFactory=new MyInstanceEnablerFactory();
    	
        // Initialize object list
    	List<ObjectModel> defaultObjectModels=ObjectLoader.loadDefault();
    	System.out.println("DEFAULT OBJECT MODELS ##= "+defaultObjectModels.size());
    	
    	
    	File f=new File("src"+File.separator+"main"+File.separator+"resources");
    	System.out.println("FILE EXISTS? "+f.exists());
    	List<ObjectModel> customObjectModels=ObjectLoader.load(f);
    	
    	List<ObjectModel> allObjectModelsList = new ArrayList<ObjectModel>();
    	allObjectModelsList.addAll(defaultObjectModels);
    	allObjectModelsList.addAll(customObjectModels);
    	
    	
/*
    	HashMap<Integer,ObjectModel> allObjectModels=new HashMap<Integer,ObjectModel>();
    	
    	Iterator<ObjectModel> allObjectModelsListIterator= allObjectModelsList.iterator();
    	while(allObjectModelsListIterator.hasNext()){
    		
    		ObjectModel tempObjModel=allObjectModelsListIterator.next();
    		if(isSupported(tempObjModel.id)){
    			//System.out.println("ADDED->"+tempObjModel.id);
    			allObjectModels.put(tempObjModel.id,tempObjModel);	
    		
    		}
    	}
*/
    	//System.out.println("allObjectModels.size= "+allObjectModels.size());
    	
    	//LwM2mModel lwM2mModel=new LwM2mModel(allObjectModels);
    	LwM2mModel lwM2mModel=new LwM2mModel(allObjectModelsList);
    	
    	
    	ObjectsInitializer initializer = new ObjectsInitializer(lwM2mModel);//lwM2mModel
    	
        initializer.setClassForObject(3, DeviceEnabler.class);

        //List<ObjectEnabler> enablers = initializer.createMandatory();
        List<ObjectEnabler> enablers = initializer.create(0,1,3);
        
        
        //---------------3303------------
        ObjectModel objectModel3303=getObjectModelFromListById(3303,allObjectModelsList);
        
        TemperatureInstanceEnabler temperatureSensor=new TemperatureInstanceEnabler(smartSilo);
        smartSilo.setTemperatureEnabler(temperatureSensor);
        HashMap<Integer,LwM2mInstanceEnabler> instances3303=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances3303.put(0, temperatureSensor);
        ObjectEnabler objectEnabler3303=new ObjectEnabler(3303, objectModel3303, instances3303, enablersFactory);
        enablers.add(objectEnabler3303);
        //----------------
        
        
        //---------------16663------------
        ObjectModel objectModel6663=getObjectModelFromListById(16663,allObjectModelsList);
        SiloInstanceEnabler silo=new SiloInstanceEnabler(smartSilo);
        
        smartSilo.setSiloEnabler(silo);
        
        HashMap<Integer,LwM2mInstanceEnabler> instances16663=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16663.put(0, silo);
        ObjectEnabler objectEnabler16663=new ObjectEnabler(16663, objectModel6663, instances16663, enablersFactory);
        enablers.add(objectEnabler16663);
        
        //----------------
        //initializer.setInstancesForObject(16663, silo);
        //enablers.add(initializer.create(16663));

        //OutValveInstanceEnabler
        //----------------16664----------
        ObjectModel objectModel16664=getObjectModelFromListById(16664,allObjectModelsList);
        
        ValveInstanceEnabler inValve=new InValveInstanceEnabler(smartSilo);
        smartSilo.setInValveEnabler(inValve);
        ValveInstanceEnabler outValve=new OutValveInstanceEnabler(smartSilo);
        smartSilo.setOutValveEnabler(outValve);
        
        HashMap<Integer,LwM2mInstanceEnabler> instances16664=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16664.put(0, inValve);
        instances16664.put(1, outValve);
        
        ObjectEnabler objectEnabler16664=new ObjectEnabler(16664, objectModel16664, instances16664, enablersFactory);
        enablers.add(objectEnabler16664);
        //----------------
        
        //---------------16665------------
        ObjectModel objectModel16665=getObjectModelFromListById(16665,allObjectModelsList);
        
        LevelSensorInstanceEnabler highLevelSensor=new HighLevelSensorInstanceEnabler(smartSilo);
        LevelSensorInstanceEnabler lowLevelSensor=new LowLevelSensorInstanceEnabler(smartSilo);
        smartSilo.setLowLevelSensorEnabler(lowLevelSensor);
        smartSilo.setHighLevelSensorEnabler(highLevelSensor);
        HashMap<Integer,LwM2mInstanceEnabler> instances16665=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16665.put(0, highLevelSensor);
        instances16665.put(1, lowLevelSensor);
        ObjectEnabler objectEnabler16665=new ObjectEnabler(16665, objectModel16665, instances16665, enablersFactory);//TODO level sensor instance enabler is abstract. check what exactly it makes this class argument
        enablers.add(objectEnabler16665);
        //----------------

        //----------------16667-----------------
        ObjectModel objectModel16667=getObjectModelFromListById(16667,allObjectModelsList);
        
        MixerInstanceEnabler mixer=new MixerInstanceEnabler(smartSilo);
        smartSilo.setMixerEnabler(mixer);
        HashMap<Integer,LwM2mInstanceEnabler> instances16667=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16667.put(0, mixer);
        ObjectEnabler objectEnabler16667=new ObjectEnabler(16667, objectModel16667, instances16667, enablersFactory);
        enablers.add(objectEnabler16667);
        //---------------
        
        //----------------16668-----------------
        ObjectModel objectModel16668=getObjectModelFromListById(16668,allObjectModelsList);
        
        HeaterInstanceEnabler heater=new HeaterInstanceEnabler(smartSilo);
        smartSilo.setHeaterEnabler(heater);
        HashMap<Integer,LwM2mInstanceEnabler> instances16668=new HashMap<Integer,LwM2mInstanceEnabler>();
        instances16668.put(0, heater);
        ObjectEnabler objectEnabler16668=new ObjectEnabler(16668, objectModel16668, instances16668, enablersFactory);
        enablers.add(objectEnabler16668);
        //---------------
        
        //-----------------
        for(int i=6;i<args.length;i=i+4){
			
			        // Create client
			        final InetSocketAddress clientAddress = new InetSocketAddress(args[i], Integer.parseInt(args[i+1]));
			        final InetSocketAddress serverAddress = new InetSocketAddress(args[i+2], Integer.parseInt(args[i+3]));
			
			        System.out.println("is server address unresolved? ->"+serverAddress.isUnresolved());
			        System.out.println("is client address unresolved? ->"+clientAddress.isUnresolved());
			        
			        final LeshanClient client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
			                enablers));
			
			        // Start the client
			        client.start();
			
			
			        RegisterResponse response = client.send(new RegisterRequest(endpointName));
			
			        // Report registration response.
			        System.out.println("Device Registration (Success? " + response.getCode() + ")");
			        if (response.getCode() == ResponseCode.CREATED) {
			            System.out.println("\tDevice: Registered Client Location '" + response.getRegistrationID() + "'");
			            registrationID = response.getRegistrationID();
			        } else {
			            // TODO Should we have a error message on response ?
			            // System.err.println("\tDevice Registration Error: " + response.getErrorMessage());
			            System.err.println("\tDevice Registration Error: " + response.getCode());
			            System.err
			                    .println("If you're having issues connecting to the LWM2M endpoint, try using the DTLS port instead");
			        }
        }

    }
    
    @Deprecated
    private boolean isSupported(int id){
    	boolean supported=false;
    	int i=0;
    	while(i<supportedObjectsIds.length && supported==false){
    		if(supportedObjectsIds[i]==id)supported=true;
    		i++;
    	}
    	return supported;
    	
    }

    private ObjectModel getObjectModelFromListById(int id,List<ObjectModel> models){
    	Iterator<ObjectModel> iter= models.iterator();
    	while(iter.hasNext()){
    		
    		ObjectModel tempObjModel=iter.next();
    		if(tempObjModel.id==id){
    			return tempObjModel;
    		}
		}
    	return null;
    }
}


