package neuralObjects;

import modelObjects.CreatureModel;
import modelObjects.CreatureActions.StateTurn;
import utilObjects.Settings;

public class TurnCCWAffector extends AffectorNeuron {

	/**
	 * An alternative to curtail this neuron's firing frequency.
	 * Maximum is 60 Hz.
	 * @param myCreature
	 * @param sleepPeriod This is the length of time that this neuron remains inactive after firing once.
	 * Units of time for this are measured in 1/60 of a second
	 */
	public TurnCCWAffector(CreatureModel myCreature, float sleepPeriod) {
		super(NeuronType.AFFECTOR_TURN_CCW, myCreature, sleepPeriod);
	}

	@Override
	public float processNeuron() {
		if(!isAsleep()){
			for(Neuron n: connections)
				value = n.processNeuron();
			
			if(value >= 1){
				myCreature.nextState.turnState = StateTurn.TURN_CCW;
				myCreature.myBody.applyAngularImpulse(Settings.CREATURE_TURN_FORCE);
			} else if (value <= -1 ) {
				myCreature.nextState.turnState = StateTurn.TURN_CW;
				myCreature.myBody.applyAngularImpulse(-1*Settings.CREATURE_TURN_FORCE);
			} else 
				myCreature.nextState.turnState = StateTurn.TURN_NONE;
		} else {
			value = 0; //If the neuron is asleep, it returns a value of zero
		}
		
		return value;
	}
}
