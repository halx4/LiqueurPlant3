
package liquerPlant.client.pipe;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import liquerPlant.client.core.MyInstanceEnablerFactory;
import liquerPlant.core.PropertiesHandler;

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

import com.sun.javafx.fxml.PropertyNotFoundException;

public class CommonResourceDevice {
	private int supportedObjectsIds[] = { 0, 1, 3, 16666 };
	private String endpointName=null;
	private MyInstanceEnablerFactory enablersFactory;
	private List<ObjectEnabler> enablers;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("check arguments!");

		} else {
			new CommonResourceDevice(args[0]);
		}
	}

	public CommonResourceDevice(String propertiesFilename) {

		PropertiesHandler props = null;

		try {
			props = new PropertiesHandler("runConfigs" + File.separator + propertiesFilename + ".properties");
		} catch (IOException e) {
			System.err.println("ERROR: properties file: " + e.getMessage() + " NOT FOUND");
			e.printStackTrace();
		}

		
		String clientIP = null;
		String clientPort = null;
		String serverIP = null;
		String serverPort = null;

		try {

			endpointName = props.getProperty("endpointName");
			clientIP = props.getProperty("clientIP");
			clientPort = props.getProperty("clientPort");
			serverIP = props.getProperty("serverIP");
			serverPort = props.getProperty("serverPort");
		} catch (PropertyNotFoundException e) {
			System.err.println("Properties Error...");
			e.printStackTrace();
		}

		System.out.println("endpoint name = " + endpointName);

		initializeCustomObjects();

		// Create client
		final InetSocketAddress clientAddress = new InetSocketAddress(clientIP, Integer.parseInt(clientPort));
		final InetSocketAddress serverAddress = new InetSocketAddress(serverIP, Integer.parseInt(serverPort));

		final LeshanClient client = new LeshanClient(clientAddress, serverAddress,
				new ArrayList<LwM2mObjectEnabler>(enablers));

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
			// System.err.println("\tDevice Registration Error: " +
			// response.getErrorMessage());
			System.err.println("\tDevice Registration Error: " + response.getCode());
			System.err.println(
					"If you're having issues connecting to the LWM2M endpoint, try using the DTLS port instead");
		}

	}

	private void initializeCustomObjects() {
		enablersFactory = new MyInstanceEnablerFactory();

		// Initialize object list
		List<ObjectModel> defaultObjectModels = ObjectLoader.loadDefault();

		File f = new File("src" + File.separator + "main" + File.separator + "resources");
		// System.out.println(f.exists());
		List<ObjectModel> customObjectModels = ObjectLoader.load(f);

		List<ObjectModel> allObjectModelsList = new ArrayList<ObjectModel>();
		allObjectModelsList.addAll(defaultObjectModels);
		allObjectModelsList.addAll(customObjectModels);

		HashMap<Integer, ObjectModel> allObjectModels = new HashMap<Integer, ObjectModel>();

		Iterator<ObjectModel> allObjectModelsListIterator = allObjectModelsList.iterator();
		while (allObjectModelsListIterator.hasNext()) {
			ObjectModel tempObjModel = allObjectModelsListIterator.next();
			if (isSupported(tempObjModel.id)) {
				// System.out.println("ADDED->"+tempObjModel.id);
				allObjectModels.put(tempObjModel.id, tempObjModel);

			}
		}

		// System.out.println("allObjectModels.size= "+allObjectModels.size());
		LwM2mModel lwM2mModel = new LwM2mModel(allObjectModelsList);

		ObjectsInitializer initializer = new ObjectsInitializer(lwM2mModel);// lwM2mModel

		initializer.setClassForObject(3, DeviceEnabler.class);

		// List<ObjectEnabler> enablers = initializer.createMandatory();
		enablers = initializer.create(0, 1, 3);

		// ---------------16666------------
		ObjectModel objectModel6666 = allObjectModels.get(16666);
		CommonResourceInstanceEnabler commonResourceInstanceEnabler = new CommonResourceInstanceEnabler(endpointName);

		// commonResourceInstanceEnabler.setObjectModel(objectModel6666);

		HashMap<Integer, LwM2mInstanceEnabler> instances16666 = new HashMap<Integer, LwM2mInstanceEnabler>();
		instances16666.put(0, commonResourceInstanceEnabler);
		ObjectEnabler objectEnabler16666 = new ObjectEnabler(16666, objectModel6666, instances16666, enablersFactory);
		enablers.add(objectEnabler16666);
	}

	private boolean isSupported(int id) {
		boolean supported = false;
		int i = 0;
		while (i < supportedObjectsIds.length && supported == false) {
			if (supportedObjectsIds[i] == id)
				supported = true;
			i++;
		}
		return supported;

	}

}
