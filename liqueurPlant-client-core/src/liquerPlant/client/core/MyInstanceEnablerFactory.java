package liquerPlant.client.core;

import org.eclipse.leshan.client.resource.LwM2mInstanceEnabler;
import org.eclipse.leshan.client.resource.LwM2mInstanceEnablerFactory;
import org.eclipse.leshan.core.model.ObjectModel;

public class MyInstanceEnablerFactory implements LwM2mInstanceEnablerFactory {

	@Override
	public LwM2mInstanceEnabler create(ObjectModel model) {
		System.err.println("INSTANCE ENABLER FACTORY INVOKED <---<");
		return null;
	}

}
