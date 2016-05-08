package liquerPlant.client.silo.Enablers.ValveEnablers;

import liquerPlant.client.silo.SiloController;
import liquerPlant.core.ValveState;
import liquerPlant.core.ValveState.state;

public class OutValveInstanceEnabler extends ValveInstanceEnabler {

	public OutValveInstanceEnabler(SiloController siloController) {
		super(siloController);
	}

	@Override
	protected state getValveState() {
		return this.getSiloController().getOutValveState();
	}

	@Override
	protected boolean setValveState(ValveState.state newState) {
		return this.getSiloController().setOutValveState(newState);
	}
	

}
