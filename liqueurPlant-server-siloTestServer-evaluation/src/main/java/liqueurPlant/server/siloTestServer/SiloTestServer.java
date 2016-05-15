package liqueurPlant.server.siloTestServer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import liqueurPlant.core.LwM2mResourceParser;
import liqueurPlant.server.core.CustomModelProvider;
import liqueurPlant.server.core.ObservationCreator;

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

	public static void main(String[] args) { //// args={serverIdentifier
												//// serverIP serverPort }

		new SiloTestServer(args);

	}

	// -------------------------------------------
	public SiloTestServer(String[] args) {

		new ControlPanel(this);
		serverIdentifier = Integer.parseInt(args[0]);

		// port 5683
		final InetSocketAddress serverAddress = new InetSocketAddress(args[1], Integer.parseInt(args[2]));
		System.out.println("is server address unresolved? ->" + serverAddress.isUnresolved());
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
	public void function1(int i) throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println("function1-" + i);
		Client client;
		LwM2mResponse response;
		int j = 0;
		int limit = 10000;
		long start;
		long finish;
		long measurements[] = new long[limit];
		long sum = 0;
		PrintWriter writer;

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
		case 10:// 1k exec test
			limit = 1000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ExecuteRequest(16663, 0, 1));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		case 11:// 1k read test
			limit = 1000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ReadRequest(16663, 0, 0));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		case 12:// 5k exec test
			limit = 5000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ExecuteRequest(16663, 0, 1));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		case 13:// 5k read test
			limit = 5000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ReadRequest(16663, 0, 0));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		case 14:// 10k exec test
			limit = 10000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ExecuteRequest(16663, 0, 1));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		case 15:// 10k read test
			limit = 10000;
			for (j = 0; j < limit; j++) {
				if (j % 200 == 0)
					doGC();
				start = System.nanoTime();
				server.send(client, new ReadRequest(16663, 0, 0));
				finish = System.nanoTime();
				// System.out.println(j+"\t"+(finish-start));
				measurements[j] = finish - start;
			}
			sum = 0;
			writer = new PrintWriter("RTTdata.txt", "UTF-8");

			for (j = 0; j < limit; j++) {

				writer.println(j + "\t" + measurements[j] / 1000);
				sum += measurements[j];

			}

			writer.close();
			System.out.println("mean=" + sum / limit);

			// System.out.println("responseCode=
			// "+response.getCode().toString()+" responsePayload=
			// "+LwM2mResourceParser.valueOf(response) );
			break;
		}

	}

	private void doGC() {
		System.gc();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
