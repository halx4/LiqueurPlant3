package liquerPlant.client.siloSimulator;

public class SiloParameters {
	
	private int highLevelSensorHeight,lowLevelSensorHeight;
	boolean hasMixer,hasHeater;
	
	boolean hasMixer() {
		return hasMixer;
	}

	boolean hasHeater() {
		return hasHeater;
	}

	int getHighLevelSensorHeight() {
		return highLevelSensorHeight;
	}

	int getLowLevelSensorHeight() {
		return lowLevelSensorHeight;
	}

	long time2Fill;//ms for 1/10 filling step
	
	public SiloParameters(int lowLevelSensorHeight,int highLevelSensorHeight, long time2Fill,boolean hasMixer,boolean hasHeater) {
		super();
		this.highLevelSensorHeight = highLevelSensorHeight;
		this.lowLevelSensorHeight = lowLevelSensorHeight;
		this.time2Fill = time2Fill;
		this.hasMixer=hasMixer;
		this.hasHeater=hasHeater;
	}

}
