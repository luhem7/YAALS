package neuralObjects;

import modelObjects.CreatureModel;
import modelObjects.CreatureStateRegister;

public class LinearVelocitySensor extends Neuron {
	CreatureModel creature;
	
	public LinearVelocitySensor(CreatureModel creature, String id) {
		super(NeuronType.SENSOR_LINEAR_VEL, id);
		
		this.creature = creature;
	}

	@Override
	public float processNeuron() {
		return creature.currState.getVelocityMag();
	}
}
