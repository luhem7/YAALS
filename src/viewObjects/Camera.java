package viewObjects;

import utilObjects.Settings;

/**
 * This class represents the perspective of the user on the screen. The Camera
 * always looks down perpendicularly at the XY plane. It assumes orthographic
 * projection. 
 * @author mehul
 *
 */
public class Camera {

	//***Camera attribute state variables
	private float zoom = Settings.CAM_STARTING_ZOOM; // The current zoom amount looking down (represents the scale at which objects are rendered)
	private float[] position = new float[2]; //The position of the camera
	private float viewAngle = 0; //The starting angle of camera
	
	//***Camera action state variables
	//Moving state variables
	private static final int MOVE_LEFT = -1;
	private static final int MOVE_RIGHT = 1;
	private static final int MOVE_UP = -1;
	private static final int MOVE_DOWN = 1;
	private static final int MOVE_NONE = 0;
	private int[] moveState = {MOVE_NONE, MOVE_NONE};
	//Zoom state variables
	private static final int ZOOM_IN = 1;
	private static final int ZOOM_OUT = -1;
	private static final int ZOOM_NONE = 0;
	private int zoomState = ZOOM_NONE;
	
	
	/**
	 * @return viewAngle
	 */
	public float getViewAngle() {
		return viewAngle;
	}
	
	/**
	 * Rotate the camera clockwise
	 */
	public void rotateCW(){
		viewAngle = viewAngle - Settings.CAM_DELTA_ANGLE;
	}
	
	/**
	 * Rotate the camera counter clockwise
	 */
	public void rotateCCW(){
		viewAngle = viewAngle + Settings.CAM_DELTA_ANGLE;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void setZoom(float zoom) {
		if(zoom < Settings.CAM_MIN_ZOOM || zoom > Settings.CAM_MAX_ZOOM)
			return;
		this.zoom = zoom;
	}
	
	public void zoomIn(){
		zoomState = ZOOM_IN;
	}
	
	public void zoomOut(){
		zoomState = ZOOM_OUT;
	}
	
	public void stopZoom(){
		zoomState = ZOOM_NONE;
	}
	
	public float[] getPosition() {
		return position;
	}
	
	public void setPosition(float[] newPos){
		if(newPos[0] > -Settings.CAM_MAX_DISTANCE && newPos[0] < Settings.CAM_MAX_DISTANCE)
			position[0] = newPos[0];
		
		if(newPos[1] > -Settings.CAM_MAX_DISTANCE && newPos[1] < Settings.CAM_MAX_DISTANCE)
			position[1] = newPos[1];
	}
	
	public void moveLeft(){
		moveState[0] = MOVE_LEFT;
	}
	
	public void moveRight(){
		moveState[0] = MOVE_RIGHT;
	}
	
	public void stopHorizontalMove(){
		moveState[0] = MOVE_NONE;
	}
	
	public void moveUp(){
		moveState[1] = MOVE_UP;
	}
	
	public void moveDown(){
		moveState[1] = MOVE_DOWN;
	}

	public void stopVerticalMove(){
		moveState[1] = MOVE_NONE;
	}
	
	/**
	 * Processes the actions that the camera takes in one cycle
	 */
	public void loopCycle(){
		//process zoom action
		if(zoomState == ZOOM_IN){
			float resultZoom = zoom + Settings.CAM_DELTA_ZOOM;
			if(resultZoom < Settings.CAM_MAX_ZOOM)
				zoom = resultZoom;
		} else if (zoomState == ZOOM_OUT) {
			float resultZoom = zoom - Settings.CAM_DELTA_ZOOM;
			if(resultZoom > Settings.CAM_MIN_ZOOM)
				zoom = resultZoom;
		}
		
		//process horizontal move action
		if(moveState[0] == MOVE_LEFT) {
			
			float newXPos = position[0]-Settings.CAM_DELTA_POS;
			if(newXPos > -Settings.CAM_MAX_DISTANCE && newXPos < Settings.CAM_MAX_DISTANCE)
				position[0] = newXPos;
			
		} else if (moveState[0] == MOVE_RIGHT) {
			
			float newXPos = position[0]+Settings.CAM_DELTA_POS;
			if(newXPos > -Settings.CAM_MAX_DISTANCE && newXPos < Settings.CAM_MAX_DISTANCE)
				position[0] = newXPos;
		}
		
		//process vertical move action
		if(moveState[1] == MOVE_UP) {
			
			float newYPos = position[1]+Settings.CAM_DELTA_POS;
			if(newYPos > -Settings.CAM_MAX_DISTANCE && newYPos < Settings.CAM_MAX_DISTANCE)
				position[1] = newYPos;
			
		} else if (moveState[1] == MOVE_DOWN) {
			
			float newYPos = position[1]-Settings.CAM_DELTA_POS;
			if(newYPos > -Settings.CAM_MAX_DISTANCE && newYPos < Settings.CAM_MAX_DISTANCE)
				position[1] = newYPos;
		}
	}
}
