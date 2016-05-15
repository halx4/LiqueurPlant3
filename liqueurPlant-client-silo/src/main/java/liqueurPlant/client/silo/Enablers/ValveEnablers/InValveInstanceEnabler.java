package liqueurPlant.client.silo.Enablers.ValveEnablers;

import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.ValveState;
import liqueurPlant.core.ValveState.state;

public class InValveInstanceEnabler extends ValveInstanceEnabler {

	public InValveInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected state getValveState() {
		return this.getSiloController().getInValveState();
	}

	@Override
	protected boolean setValveState(ValveState.state newState) {
		return this.getSiloController().setInValveState(newState);
	}
	

}
