package neuralObjects;

import utilObjects.Settings;
import modelObjects.CreatureModel;

/**
 * Represents the general class of Affector Neurons. All affector neurons have a 
 * frequency at which they can fire continuously. Realistically, the maximum possible
 * frequency is 60Hz (based on the framerate)
 * @author mehul
 *
 */
public abstract class AffectorNeuron extends Neuron {
	protected CreatureModel myCreature = null;
	private float sleepCounter = 0;
	private float sleepInterval = Settings.SLEEP_INTERVAL;
	private float sleepPeriod = 0;

	public AffectorNeuron(String id, NeuronType myType, CreatureModel myCreature, float sleepPeriod) {
		super(myType, id);
		this.myCreature = myCreature;
		output = 0f; //Setting the default behavior
		this.sleepPeriod = sleepPeriod;
	}

	@Override
	public abstract float processNeuron();
	
	/**
	 * This method returns if the neuron is sleeping or not. Note that if the current value is zero AND
	 * the sleep counter is over the sleep period, then the neuron is not put to sleep again until its
	 * value changes. 
	 * @return
	 */
	protected boolean isAsleep(){
		sleepCounter += sleepInterval;
		
		if(sleepCounter >= sleepPeriod && output == 0){ // special case where neuron should stay awake until value becomes non zero
			sleepCounter = sleepPeriod;
			return false;
		} else if (sleepCounter >= sleepPeriod) {
			sleepCounter = 0; //reset sleep counter if we have reached the sleepPeriod
			return false;
		} else
			return true;
	}
	
	/**
	 * Sets this neuron to wake up in the next iteration
	 */
	public void wakeUp(){
		sleepCounter = sleepPeriod;
	}

}
