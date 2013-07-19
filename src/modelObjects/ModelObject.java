package modelObjects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import viewObjects.ViewObject;

/**
 * This class represents common functionality of all modelObjects
 * @author mehul
 *
 */
public abstract class ModelObject {
	public Body myBody = null;
	protected ViewObject myDisplay = null;
	
	/**
	 * Returns the position of this object in the form of a Vec2 object
	 * @return
	 */
	public Vec2 getPosition(){
		return myBody.getPosition();
	}
	
	/**
	 * Returns the angle of this object in radians
	 * @return
	 */
	public float getAngle(){
		return myBody.getAngle();
	}
	
	/**
	 * Set the angle of this object in radians
	 * @param setAngle
	 */
	public void setAngle(float newAngle){
		myBody.setTransform(myBody.getPosition(), newAngle);
	}
	
	/**
	 * Makes this Polygon take a solid filled appearance. 
	 * Also makes it so that its body is no longer ignored by the physics engine
	 */
	public void setPolyToSolid(){
		this.myDisplay.tearDownGPUBindings();
		this.myDisplay.renderAsSolid = true;
		this.myDisplay.setupGPUBindings();
		this.myBody.setActive(true);
	}
	
	/**
	 * Render's this poly's view
	 */
	public void renderView(){
		myDisplay.renderCycle();
	}
	
	/**
	 * Processes the simulation logic in one cycle
	 */
	public abstract void logicCycle();
}
