package liquerPlant.core;

public class Temperature {
	private float value;
	
	public float getValue() {
		return value;
	}

	public void setValue(float temperature) {
		this.value = temperature;
	}

	public Temperature (float temp){
		this.value=temp;
	}
	
	public Temperature (Temperature temp){
		this.value=temp.getValue();
	}
}
