package utilObjects;

import org.jbox2d.common.Vec2;

/**
 * This class contains all of the somewhat random variables used through out the
 * program
 * @author mehul
 *
 */
public final class Settings {
	private static final float PI = (float) Math.PI;
	
	public static final float SLEEP_INTERVAL = 1f/60f; // The number of time steps in one second
	
	//Physics settings:
	public static final Vec2 GRAVITY = new Vec2(0f, 0f);
	public static final float LINEAR_DAMPING = 0.5f;
	public static final float ANGULAR_DAMPING = 0.9f;
	
	//The color of the creature's head
	public static final float[] CREATURE_HEAD_COLOR = {255f/255f, 106f/255f, 0f/255f};
	//The color of the creature's head
	public static final float[] CREATURE_BODY_COLOR = {157f/255f, 117f/255f, 250f/255f};
	//The shape of the creature, that is a regular polygon has these many sides
	public static final int CREATURE_VERTICES = 6;
	//The "radius" of the creature's physics body
	public static final float CREATURE_BODY_SIZE = 1f;
	//The "radius" of the creature's display on screen
	public static final float CREATURE_VIEW_SIZE = 1.3f;
	//The magnitude of the force applied to the creature during a push
	public static final float CREATURE_PUSH_FORCE = 0.5f;
	//The magnitude of the force applied to the creature during a turn (it is CCW)
	public static final float CREATURE_TURN_FORCE = 2f;
	
	//The background color of the simulation
	public static final float[] BACKGROUND_COLOR = {1,1,1};
	//The color of the walls
	public static final float[] WALL_COLOR = {0,0,0};
	public static final float WALL_HEIGHT = 100;
	public static final float WALL_WIDTH = 100;
	
	//Window settings:
	public static final String WINDOW_TITLE = "YAALS: Yet Another Artificial Life simulator";
	public static final int WINDOW_WIDTH = 900;
	public static final int WINDOW_HEIGHT = 900;
	
	//Camera settings:
	public static final float CAM_STARTING_ZOOM = 0.02f;
	public static final float CAM_MAX_ZOOM = 1f;
	public static final float CAM_MIN_ZOOM = 0.005f;
	public static final float CAM_MAX_DISTANCE = 4f; //The maximum distance the camera can move in either axis
	public static final float CAM_DELTA_POS = 0.01f; // The rate at which the camera can move;
	public static final float CAM_DELTA_ZOOM = 0.001f; //The rate at which the camera zooms
	public static final float CAM_DELTA_ANGLE = PI/20; // The rate at which the angle changes every iteration
	
	//TODO Make this creature file path dynamic
	public static final String selectedCreatureFile = "./creatures/wallHater.cre"; //The file that contains the selected creature's DNA 	
}

