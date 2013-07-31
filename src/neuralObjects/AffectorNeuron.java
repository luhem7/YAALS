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
	private float recoveryPeriod = 0;

	public AffectorNeuron(String id, NeuronType myType, CreatureModel myCreature, float recoveryPeriod) {
		super(myType, id);
		this.myCreature = myCreature;
		output = 0f; //Setting the default behavior
		this.recoveryPeriod = recoveryPeriod;
	}

	@Override
	public abstract float processNeuron();
	
	/**
	 * This method returns if the neuron is sleeping or not. Note that if the current output is zero AND
	 * the sleep counter is over the recoveryPeriod, then the neuron is not put to sleep again until its
	 * value changes. 
	 * @return
	 */
	protected boolean isAsleep(){
		sleepCounter += sleepInterval;
		
		if(sleepCounter >= recoveryPeriod && this.output == 0){ // case where neuron should stay awake until output becomes non zero
			sleepCounter = recoveryPeriod;
			return false;
		} else if (sleepCounter >= recoveryPeriod) {
			sleepCounter = 0; //reset sleep counter if we have reached the sleepPeriod and output is not zero
			return false;
		} else
			return true;
	}
	
	/**
	 * Sets this neuron to wake up in the next iteration
	 */
	public void wakeUp(){
		sleepCounter = recoveryPeriod;
	}

}
