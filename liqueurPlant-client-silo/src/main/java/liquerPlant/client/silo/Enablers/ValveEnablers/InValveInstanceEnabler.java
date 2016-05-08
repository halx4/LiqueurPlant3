package liquerPlant.client.silo.Enablers.ValveEnablers;

import liquerPlant.client.silo.SiloController;
import liquerPlant.core.ValveState;
import liquerPlant.core.ValveState.state;

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
