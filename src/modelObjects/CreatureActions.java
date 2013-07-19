package modelObjects;

/**
 * This class acts a container for all the different kinds of states a creature can have
 * @author mehul
 *
 */
public final class CreatureActions {
	/**
	 * All the possible movement states for the creature
	 * @author mehul
	 *
	 */
	public static enum StateMove {
		MOVE_FORWARD,
		MOVE_BACKWARD,
		MOVE_NONE
	}
	
	/**
	 * All the possible turn states for the creature
	 * @author mehul
	 *
	 */
	public static enum StateTurn {
		TURN_CW,
		TURN_CCW,
		TURN_NONE
	}
}
