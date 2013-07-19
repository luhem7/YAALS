package modelObjects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import utilObjects.Settings;
import viewObjects.Camera;
import viewObjects.WallView;

/**
 * This is the model for the bounding walls of the simulation
 * @author mehul
 *
 */
public class WallModel extends ModelObject {

	public WallModel(Camera myCam, World world) {
		//Creating the wall display
		myDisplay = new WallView(4, Settings.WALL_COLOR, this, myCam);
		
		//**Creating the wall Body
		BodyDef myBodyDef = new BodyDef();
		myBodyDef.type = BodyType.STATIC;
		
		PolygonShape polyShape = new PolygonShape();
		
		FixtureDef myFixDef = new FixtureDef();
		myFixDef.shape = polyShape;
		myFixDef.density = 1;
		myFixDef.friction = 0.5f;
		
		myBodyDef.position.set(0, 0);
		myBody = world.createBody(myBodyDef);
		
		polyShape.setAsBox(1, Settings.WALL_HEIGHT, new Vec2(-(Settings.WALL_WIDTH+1)/2, 0), 0); //Left wall
		myBody.createFixture(myFixDef);
		polyShape.setAsBox(Settings.WALL_WIDTH+2, 1, new Vec2(0, (Settings.WALL_HEIGHT+1)/2), 0); //Top wall
		myBody.createFixture(myFixDef);
		polyShape.setAsBox(1, Settings.WALL_HEIGHT, new Vec2((Settings.WALL_WIDTH+1)/2, 0), 0); // Right wall
		myBody.createFixture(myFixDef);
		polyShape.setAsBox(Settings.WALL_WIDTH+2, 1, new Vec2(0, -(Settings.WALL_HEIGHT+1)/2), 0); // Bottom wall
		myBody.createFixture(myFixDef);
	}

	@Override
	public void logicCycle() {
	}

}
