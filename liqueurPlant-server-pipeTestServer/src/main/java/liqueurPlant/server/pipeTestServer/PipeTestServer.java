package liqueurPlant.server.pipeTestServer;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import liqueurPlant.core.LwM2mResourceParser;
import liqueurPlant.server.core.CustomModelProvider;

import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistry;
import org.eclipse.leshan.server.observation.ObservationRegistry;

public class PipeTestServer {

	private LeshanServer server;
	private String serverIdentifier;

	public static void main(String[] args) {

		new PipeTestServer(args);

	}

	// -------------------------------------------
	public PipeTestServer(String[] args) {

		ControlPanel panel = new ControlPanel(this);
		serverIdentifier = args[0];

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

	}

	// -------------------------------------------
	public void function1(int i) {
		System.out.println("function1-" + i);
		Client client;
		LwM2mResponse response;

		ObservationRegistry observationRegistry;
		Set<Observation> observations;
		Iterator<Observation> iter;
		Observation firstObservation;

		switch (i) {
		case 0:// observe request
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ObserveRequest(16666, 0, 0));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 1:
			// add observ. Listener
			observationRegistry = server.getObservationRegistry();
			observations = observationRegistry.getObservations(getClientByIdentifier("PIPE-0"));
			// System.out.println(observations.size());
			iter = observations.iterator();
			firstObservation = iter.next();
			firstObservation.addListener(new MyObservationListener());
			break;
		case 2:
			// acquire 0 request
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ExecuteRequest(16666, 0, 1, "0"));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 3:
			// acquire 1 request
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ExecuteRequest(16666, 0, 1, "1"));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 4:
			// release 0 request
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ExecuteRequest(16666, 0, 2, "0"));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 5:
			// release 1 request
			// params[0]=(byte)1;
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ExecuteRequest(16666, 0, 2, "1"));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 6:
			// read owner
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ReadRequest(16666, 0, 0));
			System.out.println("responseCode= " + response.getCode().toString() + "  responsePayload= "
					+ LwM2mResourceParser.valueOf(response));
			break;
		case 9:
			client = getClientByIdentifier("PIPE-0");
			response = server.send(client, new ReadRequest(3, 0, 13));
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
