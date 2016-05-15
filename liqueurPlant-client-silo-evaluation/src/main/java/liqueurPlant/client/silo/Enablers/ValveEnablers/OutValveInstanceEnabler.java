package liqueurPlant.client.silo.Enablers.ValveEnablers;

import liqueurPlant.client.silo.SiloController;
import liqueurPlant.core.ValveState;
import liqueurPlant.core.ValveState.state;

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
