package modelObjects;

import org.jbox2d.common.Vec2;

import modelObjects.CreatureActions.StateMove;
import modelObjects.CreatureActions.StateTurn;

/**
 * This class holds a set of states for a given creature that are available to its neurons
 * @author mehul
 *
 */
public class CreatureStateRegister implements Cloneable{
	public CreatureActions.StateMove moveState = StateMove.MOVE_NONE;
	public CreatureActions.StateTurn turnState = StateTurn.TURN_NONE;
	
//	public Vec2 position = new Vec2(0,0); //Position in absolute coords
//	public float angle = 0; // Angle in Radians
	private Vec2 linearVelocity = new Vec2(0,0);
	private float velocityMag = 0;
	
	public void setVelocity(Vec2 velocity){
		linearVelocity = velocity;
		velocityMag = velocity.length();
	}

	public Vec2 getLinearVelocity() {
		return linearVelocity;
	}

	public float getVelocityMag() {
		return velocityMag;
	}
}
