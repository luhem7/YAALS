package modelObjects;

import java.util.LinkedList;

import modelObjects.CreatureActions.StateMove;
import modelObjects.CreatureActions.StateTurn;
import neuralObjects.AffectorNeuron;
import neuralObjects.LessThanProcessor;
import neuralObjects.LinearVelocitySensor;
import neuralObjects.Neuron;
import neuralObjects.NeuronType;
import neuralObjects.PushAffector;
import neuralObjects.TurnCCWAffector;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import dnaObjects.Dna;
import dnaObjects.NeuronLink;
import dnaObjects.NeuronTemplate;

import utilObjects.Settings;
import viewObjects.Camera;
import viewObjects.CreatureView;

/**
 * This object represents a single creature.
 * @author mehul
 *
 */
public class CreatureModel extends ModelObject {
	//The two different state registers to iterate through
	private CreatureStateRegister state1 = new CreatureStateRegister();
	private CreatureStateRegister state2 = new CreatureStateRegister();
	public CreatureStateRegister currState = null;
	public CreatureStateRegister nextState = null;
	
	//The creature's DNA:
	private Dna myDna;
	
	//Maintaining a list of references to different types of 
	//Neurons to iterate through
	public LinkedList<Neuron> affectorNeuronList = new LinkedList<Neuron>();
	public LinkedList<Neuron> sensorNeuronList = new LinkedList<Neuron>();
	public LinkedList<Neuron> allNeuronList = new LinkedList<Neuron>();
	
	public CreatureModel(Vec2 position, float angle, float[] color, Camera myCam, World world, Dna myDna){
		//Setting the display
		myDisplay = new CreatureView(this, myCam);
		
		//Setting up the Body
		BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyType.DYNAMIC; // dynamic means it is subject to forces
	    bodyDef.position.set(position);
	    bodyDef.linearDamping = Settings.LINEAR_DAMPING;
	    bodyDef.angularDamping = Settings.ANGULAR_DAMPING;
	    bodyDef.angle = angle;
	    myBody = world.createBody(bodyDef);
	    CircleShape circleShape = new CircleShape();
	    circleShape.setRadius(Settings.CREATURE_BODY_SIZE);
	    FixtureDef fixtureDef = new FixtureDef(); // fixture def that we load up with the following info:
	    fixtureDef.shape = circleShape;
	    fixtureDef.density = 1.0f;
	    fixtureDef.friction = 0.2f;
	    fixtureDef.restitution = 0.5f;
	    myBody.createFixture(fixtureDef);
	    myBody.setActive(false); // All new creatures should be ignored initially by the physics engine
		
		//Setting the creature state
		currState = state1;
		nextState = state2;
		
		this.myDna = myDna;
		//Setting up the neuralNetwork
		myDna.setupNeuralNetwork(this);
	}
	
	/**
	 * Sets up this creature's internal state based on its environment.
	 */
	public void updateStateByEnv(){
		//Setting up the creature's current state using myBody variables:
		currState.setVelocity(myBody.getLinearVelocity());
	}

	/**
	 * Processes neurons and based on the results, it sets up the nextState
	 */
	@Override
	public void logicCycle() {
		//Process all affector neurons:
		for(Neuron neuron : affectorNeuronList){
			neuron.processNeuron();
		}
		
		//TODO dumpNeuronOutputs
		//dumpNeuronOutputs();
	}
	
	/**
	 * Flips the current state and next state pointers
	 */
	public void flipStates(){
		CreatureStateRegister temp = currState;
		currState = nextState;
		nextState = temp;
	}
	
	/**
	 * Used to get all the outputs of all Neurons
	 */
	private void dumpNeuronOutputs(){
		String str = "";
		for(Neuron e: allNeuronList){
			str += "["+e+" output: "+e.output+"] ";
		}
		System.out.println(str);
	}
}
