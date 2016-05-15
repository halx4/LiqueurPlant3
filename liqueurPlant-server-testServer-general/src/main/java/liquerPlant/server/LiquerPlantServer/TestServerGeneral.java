package liquerPlant.server.LiquerPlantServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import liquerPlant.core.LwM2mResourceParser;
import liquerPlant.core.PropertiesHandler;
import liquerPlant.core.ValveState;
import liquerPlant.server.LiquerPlantServer.Gui.ControlPanel;
import liquerPlant.server.LiquerPlantServer.Processes.LiquerProcess1;
import liquerPlant.server.LiquerPlantServer.monitors.Process1Monitor;
import liquerPlant.server.core.CustomModelProvider;
import liquerPlant.server.core.ObservationCreator;

import org.eclipse.leshan.core.request.DiscoverRequest;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistry;

import com.sun.javafx.fxml.PropertyNotFoundException;

public class TestServerGeneral {

	private LeshanServer server;
	private ObservationCreator observationCreator;
	private Process1Monitor siloMon1;


	public static void main(String[] args) { //// args={serverIdentifier
												//// serverIP serverPort }
		if (args.length == 0) {
			System.out.println("check arguments!");

		} else {

			new TestServerGeneral(args[0]);
		}

	}

	// -------------------------------------------
	public TestServerGeneral(String propertiesFilename) {

		PropertiesHandler props = null;

		try {
			props = new PropertiesHandler("runConfigs" + File.separator + propertiesFilename + ".properties");
		} catch (IOException e) {
			System.err.println("ERROR: properties file: " + e.getMessage() + " NOT FOUND");
			e.printStackTrace();
		}

		new ControlPanel(this);

		String serverID = null;
		String serverIP = null;
		String serverPort = null;

		try {

			serverID = props.getProperty("serverID");
			serverIP = props.getProperty("serverIP");
			serverPort = props.getProperty("serverPort");

		} catch (PropertyNotFoundException e) {
			System.err.println("Properties Error...");
			e.printStackTrace();
		}

		// port 5683
		final InetSocketAddress serverAddress = new InetSocketAddress(serverIP, Integer.parseInt(serverPort));
		// System.out.println(serverAddress.isUnresolved());
		LeshanServerBuilder serverBuilder = new LeshanServerBuilder();
		serverBuilder.setLocalAddress(serverAddress);
		serverBuilder.setObjectModelProvider(new CustomModelProvider());

		server = serverBuilder.build();
		ClientRegEventsListener listener = new ClientRegEventsListener(server);

		server.start();
		server.getClientRegistry().addListener(listener);

		observationCreator = new ObservationCreator(server);

	}

	// -------------------------------------------
	public void actionRequestFromControlPanel(String command, String clientStr, String objID, String objInstanceID,
			String resourceID, String Value) {

		Client client;
		LwM2mResponse response;
		client = getClientByIdentifier(clientStr);
		
		if(client!= null){
		
			switch (command) {
			case "READ":

					System.out.println("Sending READ request to client: " + clientStr + " on /" + Integer.parseInt(objID)
							+ "/" + Integer.parseInt(objInstanceID) + "/" + Integer.parseInt(resourceID) + " ...");
					response = server.send(client, new ReadRequest(Integer.parseInt(objID), Integer.parseInt(objInstanceID),
							Integer.parseInt(resourceID)));
					System.out.println("    responseCode= " + response.getCode().toString() + " |  responsePayload= "
							+ LwM2mResourceParser.valueOf(response));
				break;
			case "EXECUTE":
					System.out.println("Sending EXECUTE request to client: " + clientStr + " on /" + Integer.parseInt(objID)
							+ "/" + Integer.parseInt(objInstanceID) + "/" + Integer.parseInt(resourceID) + " ...");
					response = server.send(client, new ExecuteRequest(Integer.parseInt(objID), Integer.parseInt(objInstanceID),
							Integer.parseInt(resourceID)));
					System.out.println("    responseCode= " + response.getCode().toString() + " |  responsePayload= "
							+ LwM2mResourceParser.valueOf(response));
				break;
		
			case "bind std observations":// observations
				siloMon1 = new Process1Monitor(1, server, client);
	
				// silo1 filling complete
				observationCreator.establishObservation		(clientStr, 16663, 0, 7);
				observationCreator.addObservationListener	(clientStr, 16663, 0, 7, siloMon1);
	
				// silo1 emptying complete
				observationCreator.establishObservation		(clientStr, 16663, 0, 8);
				observationCreator.addObservationListener	(clientStr, 16663, 0, 8, siloMon1);
	
				break;
			case "start fill-empty cycle": //  "bind std observations" must have been executed before this to work
				LiquerProcess1 p1 = new LiquerProcess1(1, siloMon1);
				p1.start();
				break;
			}
		}
		else {
			System.out.println("not found registered client with endpoint name: " + clientStr);
		}
	}

	// -------------------------------------------

	private Client getClientByIdentifier(String endpoint) {
		ClientRegistry clientRegistry = server.getClientRegistry();
		return clientRegistry.get(endpoint);
	}

}
