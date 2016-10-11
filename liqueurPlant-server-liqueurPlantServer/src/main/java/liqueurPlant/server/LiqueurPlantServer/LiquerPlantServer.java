package liqueurPlant.server.LiqueurPlantServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import liqueurPlant.core.LwM2mResourceParser;
import liqueurPlant.core.ValveState;
import liqueurPlant.server.LiqueurPlantServer.Gui.ControlPanel;
import liqueurPlant.server.LiqueurPlantServer.Processes.LiqueurGenerationProcess1;
import liqueurPlant.server.LiqueurPlantServer.Processes.LiqueurGenerationProcess2;
import liqueurPlant.server.LiqueurPlantServer.monitors.Process1Monitor;
import liqueurPlant.server.LiqueurPlantServer.monitors.Process2Monitor;
import liqueurPlant.server.core.CustomModelProvider;
import liqueurPlant.server.core.ObservationCreator;
import liqueurPlant.utilities.PropertiesHandler;
import liqueurPlant.utilities.PropertyNotFoundException;

import org.eclipse.leshan.core.request.DiscoverRequest;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistry;

//import com.sun.javafx.fxml.PropertyNotFoundException;

public class LiquerPlantServer {

	private LeshanServer server;
	private ObservationCreator observationCreator;
	private Process1Monitor siloMon1;
	private Process2Monitor siloMon2;

	public static void main(String[] args) { //// args={serverIdentifier
												//// serverIP serverPort }
		if (args.length == 0) {
			System.out.println("check arguments!");

		} else {

			new LiquerPlantServer(args[0]);
		}

	}

	// -------------------------------------------
	public LiquerPlantServer(String propertiesFilename) {

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
	public void actionRequestFromControlPanel(String i) {
		// System.out.println("function1-" + i);
		Client client;
		LwM2mResponse response;

		switch (i) {
		case "fill S1":// execute fill
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new ExecuteRequest(16663, 0, 1));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "empty S1":// execute empty
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new ExecuteRequest(16663, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "stop S1":// execute stop
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new ExecuteRequest(16663, 0, 3));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "read smart silo state":
			// read silo state
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new ReadRequest(16663, 0, 0));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "read in valve":
			// read in valve
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new ReadRequest(16664, 0, 5850));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= " + ValveState
					.boolean2ValveState(Boolean.parseBoolean(LwM2mResourceParser.valueOf(response))).toString());
			break;
		case "heat S1":
			// heat S3
			client = getClientByIdentifier("SILO-3");
			response = server.send(client, new ExecuteRequest(16663, 0, 5));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "read S1 heater state":
			// read S3 heater state
			client = getClientByIdentifier("SILO-3");
			response = server.send(client, new ReadRequest(16668, 0, 5850));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "bind Observe":// observations
			Client s1 = server.getClientRegistry().get("SILO-1");
			Client s2 = server.getClientRegistry().get("SILO-2");
			Client s3 = server.getClientRegistry().get("SILO-3");
			Client s4 = server.getClientRegistry().get("SILO-4");
			Client pipe = server.getClientRegistry().get("PIPE-0");
			Client power = server.getClientRegistry().get("POWER-0");

			siloMon1 = new Process1Monitor(1, server, s1, s4, pipe, power);
			siloMon2 = new Process2Monitor(2, server, s2, s3, pipe, power);

			// silo1 state
			// observationCreator.establishObservation ("SILO-1", 16663, 0, 0);
			// observationCreator.addObservationListener ("SILO-1", 16663, 0, 0,
			// siloMon1);

			// silo2 state
			// observationCreator.establishObservation ("SILO-2", 16663, 0, 0);
			// observationCreator.addObservationListener ("SILO-2", 16663, 0, 0,
			// siloMon2);

			// silo3 state
			// observationCreator.establishObservation ("SILO-3", 16663, 0, 0);
			// observationCreator.addObservationListener ("SILO-3", 16663, 0, 0,
			// siloMon2);

			// silo4 state
			// observationCreator.establishObservation ("SILO-4", 16663, 0, 0);
			// observationCreator.addObservationListener ("SILO-4", 16663, 0, 0,
			// siloMon1);

			// silo1 filling complete
			observationCreator.establishObservation("SILO-1", 16663, 0, 7);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 7, siloMon1);

			// silo1 emptying complete
			observationCreator.establishObservation("SILO-1", 16663, 0, 8);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 8, siloMon1);

			// silo4 filling complete
			observationCreator.establishObservation("SILO-4", 16663, 0, 7);
			observationCreator.addObservationListener("SILO-4", 16663, 0, 7, siloMon1);

			// silo4 emptying complete
			observationCreator.establishObservation("SILO-4", 16663, 0, 8);
			observationCreator.addObservationListener("SILO-4", 16663, 0, 8, siloMon1);

			// silo4 heating complete
			observationCreator.establishObservation("SILO-4", 16663, 0, 9);
			observationCreator.addObservationListener("SILO-4", 16663, 0, 9, siloMon1);

			// silo4 mixing complete
			observationCreator.establishObservation("SILO-4", 16663, 0, 10);
			observationCreator.addObservationListener("SILO-4", 16663, 0, 10, siloMon1);

			// silo2 filling complete
			observationCreator.establishObservation("SILO-2", 16663, 0, 7);
			observationCreator.addObservationListener("SILO-2", 16663, 0, 7, siloMon2);

			// silo2 emptying complete
			observationCreator.establishObservation("SILO-2", 16663, 0, 8);
			observationCreator.addObservationListener("SILO-2", 16663, 0, 8, siloMon2);

			// silo3 filling complete
			observationCreator.establishObservation("SILO-3", 16663, 0, 7);
			observationCreator.addObservationListener("SILO-3", 16663, 0, 7, siloMon2);

			// silo3 emptying complete
			observationCreator.establishObservation("SILO-3", 16663, 0, 8);
			observationCreator.addObservationListener("SILO-3", 16663, 0, 8, siloMon2);

			// silo2 heating complete
			observationCreator.establishObservation("SILO-2", 16663, 0, 9);
			observationCreator.addObservationListener("SILO-2", 16663, 0, 9, siloMon2);

			// silo3 mixing complete
			observationCreator.establishObservation("SILO-3", 16663, 0, 10);
			observationCreator.addObservationListener("SILO-3", 16663, 0, 10, siloMon2);

			// pipe owner
			observationCreator.establishObservation("PIPE-0", 16666, 0, 0);
			observationCreator.addObservationListener("PIPE-0", 16666, 0, 0, siloMon1);
			observationCreator.addObservationListener("PIPE-0", 16666, 0, 0, siloMon2);

			// power owner
			observationCreator.establishObservation("POWER-0", 16666, 0, 0);
			observationCreator.addObservationListener("POWER-0", 16666, 0, 0, siloMon1);
			observationCreator.addObservationListener("POWER-0", 16666, 0, 0, siloMon2);

			// silo 3 mixer
			observationCreator.establishObservation("SILO-3", 16667, 0, 5850);
			observationCreator.addObservationListener("SILO-3", 16667, 0, 5850, siloMon2);

			// silo 2 heater
			observationCreator.establishObservation("SILO-2", 16668, 0, 5850);
			observationCreator.addObservationListener("SILO-2", 16668, 0, 5850, siloMon2);

			// silo 4 mixer
			observationCreator.establishObservation("SILO-4", 16667, 0, 5850);
			observationCreator.addObservationListener("SILO-4", 16667, 0, 5850, siloMon1);

			// silo 4 heater
			observationCreator.establishObservation("SILO-4", 16668, 0, 5850);
			observationCreator.addObservationListener("SILO-4", 16668, 0, 5850, siloMon1);

			break;
		case "start processes":
			LiqueurGenerationProcess1 p1 = new LiqueurGenerationProcess1(1, siloMon1);
			LiqueurGenerationProcess2 p2 = new LiqueurGenerationProcess2(2, siloMon2);
			p1.start();
			p2.start();
			// System.out.println("processes started");
			break;

		case "fill S2":
			client = getClientByIdentifier("SILO-2");
			response = server.send(client, new ExecuteRequest(16663, 0, 1));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "empty S2":
			client = getClientByIdentifier("SILO-2");
			response = server.send(client, new ExecuteRequest(16663, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "fill S3":
			client = getClientByIdentifier("SILO-3");
			response = server.send(client, new ExecuteRequest(16663, 0, 1));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "empty S3":
			client = getClientByIdentifier("SILO-3");
			response = server.send(client, new ExecuteRequest(16663, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "fill S4":
			client = getClientByIdentifier("SILO-4");
			response = server.send(client, new ExecuteRequest(16663, 0, 1));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "empty S4":
			client = getClientByIdentifier("SILO-4");
			response = server.send(client, new ExecuteRequest(16663, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case "discover 16663":
			client = getClientByIdentifier("SILO-1");
			response = server.send(client, new DiscoverRequest(16663));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		default:
			System.err.println("LiquerPlantServer:NOT SUPPORTED COMMAND");

		}

	}

	// -------------------------------------------

	private Client getClientByIdentifier(String endpoint) {
		ClientRegistry clientRegistry = server.getClientRegistry();
		return clientRegistry.get(endpoint);
	}

}
