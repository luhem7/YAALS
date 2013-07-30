package neuralObjects;

import utilObjects.Settings;

public class BlinkerSensor extends Neuron {
	
	private float sleepTimePeriod = 0f;
	private float sleepCounter = 0f;

	public BlinkerSensor(NeuronType myType, String id, float fireFrequency) {
		super(myType, id);
		
		this.sleepTimePeriod = 1/fireFrequency;
	}

	@Override
	public float processNeuron() {
		//Increment sleep counter
		sleepCounter += Settings.SLEEP_INTERVAL;
		
		//If SleepCounter ticks over
		if(sleepCounter >= sleepTimePeriod){
			sleepCounter = 0;
			output = 1;
		} else 
			output = 0;
		
		return output;
	}

}
