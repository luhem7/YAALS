package modelObjects;

import java.util.LinkedList;

import modelObjects.CreatureActions.StateMove;
import modelObjects.CreatureActions.StateTurn;
import neuralObjects.AffectorNeuron;
import neuralObjects.LessThanProcessor;
import neuralObjects.LinearVelocitySensor;
import neuralObjects.NeuronLink;
import neuralObjects.Neuron;
import neuralObjects.NeuronTemplate;
import neuralObjects.NeuronType;
import neuralObjects.PushAffector;
import neuralObjects.TurnCCWAffector;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

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
	LinkedList<NeuronTemplate> neuronTemplateList = new LinkedList<NeuronTemplate>();
	LinkedList<NeuronLink> neuronLinkList = new LinkedList<NeuronLink>();
	
	//Maintaining a list of references to different types of 
	//Neurons to iterate through
	private LinkedList<Neuron> affectorNeuronList = new LinkedList<Neuron>();
	private LinkedList<Neuron> sensorNeuronList = new LinkedList<Neuron>();
	
	public CreatureModel(Vec2 position, float angle, float[] color, Camera myCam, World world){
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
		
		//Setting up the neuralNetwork
		setupNeuralNetwork();
	}
	
	/**
	 * Sets up this creatures neural network using the NeuronTemplateList and
	 * NeuronLinkList. Requires that the two lists be setup fully first
	 */
	private void setupNeuralNetwork(){
		//This variable stores a list of all the neurons, just to facilitate looking for a particular Neuron by its ID
		LinkedList<Neuron> fullNeuronList = new LinkedList<Neuron>();  
		
		//**Creating all the neurons
		for(NeuronTemplate neuronTemplate: neuronTemplateList){
			Neuron newNeuron = null;
			
			switch (neuronTemplate.myType){
			//Doing all the Sensor Neurons
			case SENSOR_LINEAR_VEL:
				newNeuron = new LinearVelocitySensor(neuronTemplate.id, this);
				sensorNeuronList.add(newNeuron);
				break;
			
			//Doing all the Processor Neurons
			case PROC_LESSTHAN:
				newNeuron = new LessThanProcessor(neuronTemplate.id, neuronTemplate.values[0]);
				break;
			
			//Doing all the Affector neurons
			case AFFECTOR_PUSH:
				newNeuron = new PushAffector(neuronTemplate.id, this, neuronTemplate.values[0]);
				affectorNeuronList.addLast(newNeuron);
				break;
			case AFFECTOR_TURN_CCW:
				newNeuron = new TurnCCWAffector(neuronTemplate.id, this, neuronTemplate.values[0]);
				affectorNeuronList.addLast(newNeuron);
				break;
			default:
				break;
			}
			
			if(newNeuron != null)
				fullNeuronList.addLast(newNeuron);
			else
				System.err.println("Did not know how to add the following neuron to the creature's neural network: "+neuronTemplate);
		}
		
		//**Linking all the neurons
		for (NeuronLink neuronLink : neuronLinkList){
			Neuron fromNeuron = Neuron.getNeuronByID(fullNeuronList, neuronLink.fromID);
			if(fromNeuron == null)
				continue; /* Quietly ignore skip this link because this will probably become
				a common occurance when full blown evolution occurs*/
			
			Neuron toNeuron = Neuron.getNeuronByID(fullNeuronList, neuronLink.toID);
			if(toNeuron == null)
				continue; /* Quietly ignore skip this link because this will probably become
				a common occurance when full blown evolution occurs*/
			
			/*Adding the connection
			The toNeuron will call call the fromNeuron during
			processing
			*/
			toNeuron.addNeuralConnection(fromNeuron);
		}
		
		//TODO just for testing
		
		//Add always on push affector
//		AffectorNeuron n_push = new PushAffector(this, 8*1/60f);
//		n_push.value = 1f;
//		n_push.wakeUp();
//		affectorNeuronList.addLast(n_push);
//		
//		//adding linear velocity sensor -> less than processor -> Turn CCW Affector
//		Neuron n_sens = new LinearVelocitySensor(this);
//		Neuron n_proc = new LessThanProcessor(1.5f);
//		Neuron n_turn_ccw = new TurnCCWAffector(this, 120*1/60f);
//		
//		affectorNeuronList.addLast(n_push);
//		sensorNeuronList.add(n_sens);
//		n_turn_ccw.addNeuralConnection(n_proc);
//		n_proc.addNeuralConnection(n_sens);
//		affectorNeuronList.add(n_turn_ccw);
	}

	/**
	 * Processes neurons and based on the results, it sets up the nextState
	 */
	@Override
	public void logicCycle() {		
		//Setting up the creature's current state using myBody variables:
		currState.setVelocity(myBody.getLinearVelocity());

		//Process all affector neurons:
		for(Neuron neuron : affectorNeuronList)
			neuron.processNeuron();
	}
	
	/**
	 * Flips the current state and next state pointers
	 */
	public void flipStates(){
		CreatureStateRegister temp = currState;
		currState = nextState;
		nextState = temp;
	}
}
