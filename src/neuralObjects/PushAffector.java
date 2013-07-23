package neuralObjects;

import org.jbox2d.common.Vec2;

import utilObjects.Settings;

import modelObjects.CreatureActions.StateMove;
import modelObjects.CreatureModel;

public class PushAffector extends AffectorNeuron {

	public PushAffector(String id, CreatureModel myCreature, float sleepPeriod) {
		super(id, NeuronType.AFFECTOR_PUSH, myCreature, sleepPeriod);
	}

	@Override
	public float processNeuron() {
		if(!isAsleep()){
			for(Neuron n: connections)
				value = n.processNeuron();
			
			if(value >= 1) {
				myCreature.nextState.moveState = StateMove.MOVE_FORWARD;
				Vec2 pushVector = new Vec2();
				pushVector.x = Settings.CREATURE_PUSH_FORCE*(float)Math.cos(myCreature.getAngle());
				pushVector.y = Settings.CREATURE_PUSH_FORCE*(float)Math.sin(myCreature.getAngle());
				myCreature.myBody.applyLinearImpulse(pushVector, myCreature.myBody.getWorldCenter());
				
			} else if(-1 < value && value < 1) {
				myCreature.nextState.moveState = StateMove.MOVE_NONE;
			} else {
				myCreature.nextState.moveState = StateMove.MOVE_BACKWARD;
				Vec2 pushVector = new Vec2();
				pushVector.x = -1*Settings.CREATURE_PUSH_FORCE*(float)Math.cos(myCreature.getAngle());
				pushVector.y = -1*Settings.CREATURE_PUSH_FORCE*(float)Math.sin(myCreature.getAngle());
				myCreature.myBody.applyLinearImpulse(pushVector, myCreature.myBody.getWorldCenter());
			}
		}
		
		return value;
	}
}
