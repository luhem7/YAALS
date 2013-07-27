package controlObjects;

import java.util.LinkedList;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import dnaObjects.Dna;

import utilObjects.Settings;
import viewObjects.Camera;

import modelObjects.ModelObject;
import modelObjects.CreatureModel;
import modelObjects.WallModel;

/**
 * Takes care of initializing the world and the display
 * @author mehul
 *
 */
public class Simulator {
	private Random rand = new Random();
	
	//All the Objects in this simulation
	private LinkedList<ModelObject> modelObjectList = new LinkedList<ModelObject>();
	//The creatures in the simulation
	private LinkedList<CreatureModel> creatureList = new LinkedList<CreatureModel>();
	
	private ModelObject ghostPoly = null;
	private Camera myCam = new Camera();
	private World world;
	private Dna selectedDna = null; //The Dna to use when inserting a creature into the sim
	//Inital mouse click on placing creature
	private float initMouseX = 0;
	private float initMouseY = 0;
	
	public Simulator(){
		//Initialize OpenGL (Display)
		this.setupOpenGL();
		
		setupPhysics();
		
		//Setting up world items
		setupSim();
		
		while(!Display.isCloseRequested()){
			this.loopCycle();
			
			Display.sync(60);
			Display.update();
		}
		
		this.tearDownOpenGL();
	}
	
	/**
	 * Sets up world physics settings
	 */
	private void setupPhysics(){
		world = new World(Settings.GRAVITY);
	}
	
	private void setupOpenGL(){
		//Setup OpenGL context with API version 3.2
		try{
			PixelFormat pixelformat = new PixelFormat();
			ContextAttribs contextAttribs = new ContextAttribs(3, 2)
			.withForwardCompatible(true)
			.withProfileCore(true);
			
			Display.setDisplayMode(new DisplayMode(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT));
			Display.setTitle(Settings.WINDOW_TITLE);
			Display.create(pixelformat, contextAttribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Setup background color
		GL11.glClearColor(1f, 1f, 1f, 0);
		
		//Map the interal openGL coordinate system
		GL11.glViewport(0, 0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
	}
	
	public void loopCycle(){
		//**Rendering the objects in the scene
		//Clear the screen
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		for(ModelObject p : modelObjectList){
			p.renderView();
		}
		
		//**Processing inputs in queue
		while(Keyboard.next()){
			
			switch(Keyboard.getEventKey()){
				//Camera Events
				case Keyboard.KEY_UP:
					if (Keyboard.getEventKeyState())
						myCam.moveUp();
					else
						myCam.stopVerticalMove();
					break;
				case Keyboard.KEY_DOWN:
					if (Keyboard.getEventKeyState())
						myCam.moveDown();
					else
						myCam.stopVerticalMove();
					break;
				case Keyboard.KEY_LEFT:
					if (Keyboard.getEventKeyState())
						myCam.moveLeft();
					else
						myCam.stopHorizontalMove();
					break;
				case Keyboard.KEY_RIGHT:
					if (Keyboard.getEventKeyState())
						myCam.moveRight();
					else
						myCam.stopHorizontalMove();
					break;
				case Keyboard.KEY_A:
					if (Keyboard.getEventKeyState())
						myCam.zoomIn();
					else
						myCam.stopZoom();
					break;
				case Keyboard.KEY_Z:
					if (Keyboard.getEventKeyState())
						myCam.zoomOut();
					else
						myCam.stopZoom();
					break;
			}
		}
		
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){ //If this is the left mouse button pressed down
				if(Mouse.getEventButtonState()){ //Button press
					//Keep track of mouse press for fling direction
					initMouseX = Mouse.getX();
					initMouseY = Mouse.getY();
					
					ghostPoly = addCreature(initMouseX, initMouseY);
				} else { //Button release
					if(ghostPoly != null){
						ghostPoly.setPolyToSolid();
						
						//Give the new polygon a fling in the direction opposite from where the mouse button was pressed.
//						float currMouseX = Mouse.getX();
//						float currMouseY = Mouse.getY();
//						float startAngle = (float) Math.atan((double)(currMouseY - initMouseY) / (double)(currMouseX - initMouseX));
//						ghostPoly.setAngle(startAngle);
						ghostPoly.setPolyToSolid();
					}
					
				}
			}
		}
		
		//**Processing simulation logic
		myCam.loopCycle(); //Process Camera logic
		
		
		//TODO For every creature, change its current state based on simulation 
		
		//Process logic for every modelObject
		for(ModelObject m: modelObjectList)
			m.logicCycle();
		
		//For every creature flip the creature states
		for(CreatureModel creature: creatureList){
			creature.flipStates();
		}
		
		//Process physics
		float timeStep = Settings.SLEEP_INTERVAL;
		int velocityIterations = 6;
	    int positionIterations = 2;
	    world.step(timeStep, velocityIterations, positionIterations);
	}
	
	/**
	 * Sets up simulation specific items
	 */
	private void setupSim(){
		//**Sets up the boundary walls for this world
		ModelObject newModel = new WallModel(myCam, world);
		//Adding the wall to the models list so that they are also rendered
		modelObjectList.add(newModel);
		
		//**Read in default selected creature's DNA
		selectedDna = new Dna(Settings.selectedCreatureFile);
	}
	
	/**
	 * Adds a Creature at the specific screen position. If it is not possible to add the creature at this position, it will return null
	 */
	private CreatureModel addCreature(float screenx, float screeny){
		float xCamPos = myCam.getPosition()[0];
		float yCamPos = myCam.getPosition()[1];
		float xpos = (screenx*2/Settings.WINDOW_WIDTH - 1 + xCamPos)/myCam.getZoom();
		float ypos = (screeny*2/Settings.WINDOW_HEIGHT - 1 + yCamPos)/myCam.getZoom();
		
		//Checking to see if xpos and ypos fall within bounds
		if(!isWithinWallBounds(xpos, ypos))
			return null;
		
		float[] randColor = {rand.nextFloat(), rand.nextFloat(), rand.nextFloat()};
		Vec2 position = new Vec2(xpos, ypos);
		float randAngle = 360*rand.nextFloat();
		CreatureModel newCreature = new CreatureModel(position, randAngle, randColor, myCam, world, selectedDna.getPerfectCopy());
	    
		modelObjectList.add(newCreature);
		creatureList.add(newCreature);
		
		return newCreature;
	}
	
	/**
	 * Checks to see if the point represented by xpos and ypos is within the bounds
	 * defined by the walls
	 * @param xpos
	 * @param ypos
	 */
	private boolean isWithinWallBounds(float xpos, float ypos){
		return (-Settings.WALL_WIDTH/2 < xpos) && (xpos < Settings.WALL_WIDTH/2) && (-Settings.WALL_HEIGHT/2 < ypos) && (ypos < Settings.WALL_HEIGHT/2);
	}
	
	public void tearDownOpenGL(){
		Display.destroy();
	}
}
