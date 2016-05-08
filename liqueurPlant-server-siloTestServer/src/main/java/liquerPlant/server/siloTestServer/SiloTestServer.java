package liquerPlant.server.siloTestServer;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import liquerPlant.core.LwM2mResourceParser;
import liquerPlant.server.core.CustomModelProvider;
import liquerPlant.server.core.ObservationCreator;

import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistry;
import org.eclipse.leshan.server.observation.ObservationRegistry;

public class SiloTestServer {

	private LeshanServer server;
	private int serverIdentifier;
	private ObservationCreator observationCreator;

	/*
	 * args={serverIdentifier serverIP serverPort } eg: 1 0.0.0.0 5683
	 */

	public static void main(String[] args) {
		if (args.length == 0)
			System.out.println("check args!");
		else
			new SiloTestServer(args);

	}

	// -------------------------------------------
	public SiloTestServer(String[] args) {

		ControlPanel panel = new ControlPanel(this);
		serverIdentifier = Integer.parseInt(args[0]);

		// port 5683
		final InetSocketAddress serverAddress = new InetSocketAddress(args[1], Integer.parseInt(args[2]));
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
	public void function1(int i) {
		System.out.println("function1-" + i);
		Client client;
		LwM2mResponse response;
		LwM2mResource resourceNode;

		ObservationRegistry observationRegistry;
		Set<Observation> observations;
		Iterator<Observation> iter;
		Observation firstObservation;
		byte[] params = { (byte) serverIdentifier };

		MyObservationListener observationListener = new MyObservationListener(server);

		client = getClientByIdentifier("SILO-1");

		switch (i) { // observe all
		case 0:

			observationCreator.establishObservation("SILO-1", 16663, 0, 0);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 0, observationListener);

			observationCreator.establishObservation("SILO-1", 16663, 0, 7);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 7, observationListener);

			observationCreator.establishObservation("SILO-1", 16663, 0, 8);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 8, observationListener);

			observationCreator.establishObservation("SILO-1", 16663, 0, 9);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 9, observationListener);

			observationCreator.establishObservation("SILO-1", 16663, 0, 10);
			observationCreator.addObservationListener("SILO-1", 16663, 0, 10, observationListener);

			// mixer
			observationCreator.establishObservation("SILO-1", 16667, 0, 5850);
			observationCreator.addObservationListener("SILO-1", 16667, 0, 5850, observationListener);

			// heater
			observationCreator.establishObservation("SILO-1", 16668, 0, 5850);
			observationCreator.addObservationListener("SILO-1", 16668, 0, 5850, observationListener);

			break;
		case 1:// execute fill
			response = server.send(client, new ExecuteRequest(16663, 0, 1));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ response.getErrorMessage());
			break;
		case 2:// execute empty
			response = server.send(client, new ExecuteRequest(16663, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 3:// execute stop
			response = server.send(client, new ExecuteRequest(16663, 0, 3));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 4:// heat
			response = server.send(client, new ExecuteRequest(16663, 0, 5));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 5:// mix
			response = server.send(client, new ExecuteRequest(16663, 0, 6));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 6:
			// write in valve true
			// resourceNode= LwM2mSingleResource.newBooleanResource(5850,true);
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16664, 0, 5850, true));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 7:
			// write in valve false
			// resourceNode=new
			// LwM2mResource(5850,Value.newBooleanValue(false));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16664, 0, 5850, false));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 8:
			// write out valve true
			// resourceNode= LwM2mSingleResource.newBooleanResource(5850,true);
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16664, 1, 5850, true));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 9:
			// write out valve false
			// resourceNode=new
			// LwM2mResource(5850,Value.newBooleanValue(false));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16664, 1, 5850, false));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;

		case 10:// set Mixer time 2 sec (write request)
			// resourceNode=new LwM2mResource(0,Value.newIntegerValue(2));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16667, 0, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 11:// set Mixer time 10 sec (write request)
			// resourceNode=new LwM2mResource(0,Value.newIntegerValue(10));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16667, 0, 0, 10));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;

		case 12:// read mix time
			response = server.send(client, new ReadRequest(16667, 0, 0));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 13:// setMixerOn
			// resourceNode=new LwM2mResource(5850,Value.newBooleanValue(true));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16667, 0, 5850, true));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 14:// setMixerOff
			client = getClientByIdentifier("SILO-1");
			// resourceNode=new
			// LwM2mResource(5850,Value.newBooleanValue(false));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16667, 0, 5850, false));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 15:// set Heater time 2 sec (write request)
			// resourceNode=new LwM2mResource(0,Value.newIntegerValue(2));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16668, 0, 0, 2));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 16:// set Heater time 10 sec (write request)
			// resourceNode=new LwM2mResource(0,Value.newIntegerValue(10));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16668, 0, 0, 10));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 17:// read heat time
			response = server.send(client, new ReadRequest(16668, 0, 5850));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 18:// setheaterOn
			// resourceNode=new LwM2mResource(5850,Value.newBooleanValue(true));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16668, 0, 5850, true));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 19:// setheaterOff
			client = getClientByIdentifier("SILO-1");
			// resourceNode=new
			// LwM2mResource(5850,Value.newBooleanValue(false));
			response = server.send(client, new WriteRequest(WriteRequest.Mode.REPLACE, 16668, 0, 5850, false));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 20:// get Silo State
			response = server.send(client, new ReadRequest(16663, 0, 0));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 21:// get low level state
			response = server.send(client, new ReadRequest(16665, 1, 5550));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 22:// get high level state
			response = server.send(client, new ReadRequest(16665, 0, 5550));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		}

	}

	// -------------------------------------------
	private Client getFirstClient() {
		ClientRegistry clientRegistry = server.getClientRegistry();
		Collection<Client> clientsCollection = clientRegistry.allClients();
		Iterator<Client> iter = clientsCollection.iterator();
		Client client = iter.next();
		return client;

	}
	// -------------------------------------------

	private Client getClientByIdentifier(String endpoint) {
		ClientRegistry clientRegistry = server.getClientRegistry();
		return clientRegistry.get(endpoint);
	}

}
