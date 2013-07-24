package dnaObjects;

import java.util.LinkedList;

import neuralObjects.LessThanProcessor;
import neuralObjects.LinearVelocitySensor;
import neuralObjects.Neuron;
import neuralObjects.PushAffector;
import neuralObjects.TurnCCWAffector;

import modelObjects.CreatureModel;

/**
 * This class serves as a container for NeuronLinks and NeuronTemplates.
 * @author mehul
 *
 */
public class Dna {
	private LinkedList<NeuronTemplate> neuronTemplateList = new LinkedList<NeuronTemplate>();
	private LinkedList<NeuronLink> neuronLinkList = new LinkedList<NeuronLink>();

	public void addNeuronTemplate(NeuronTemplate nt){
		neuronTemplateList.add(nt);
	}
	
	public void addNeuronLink(NeuronLink nl){
		neuronLinkList.add(nl);
	}
	
	/**
	 * Sets up a creature's neural network
	 * @param creature
	 */
	public void setupNeuralNetwork(CreatureModel creature){
		//This variable stores a list of all the neurons, just to facilitate looking for a particular Neuron by its ID
		LinkedList<Neuron> fullNeuronList = new LinkedList<Neuron>();  
		
		//**Creating all the neurons
		for(NeuronTemplate neuronTemplate: neuronTemplateList){
			Neuron newNeuron = null;
			
			switch (neuronTemplate.myType){
			//Doing all the Sensor Neurons
			case SENSOR_LINEAR_VEL:
				newNeuron = new LinearVelocitySensor(neuronTemplate.id, creature);
				creature.sensorNeuronList.add(newNeuron);
				break;
			
			//Doing all the Processor Neurons
			case PROC_LESSTHAN:
				newNeuron = new LessThanProcessor(neuronTemplate.id, neuronTemplate.values[0]);
				break;
			
			//Doing all the Affector neurons
			case AFFECTOR_PUSH:
				newNeuron = new PushAffector(neuronTemplate.id, creature, neuronTemplate.values[0]);
				creature.affectorNeuronList.addLast(newNeuron);
				break;
			case AFFECTOR_TURN_CCW:
				newNeuron = new TurnCCWAffector(neuronTemplate.id, creature, neuronTemplate.values[0]);
				creature.affectorNeuronList.addLast(newNeuron);
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
		
//		//Add always on push affector
//		AffectorNeuron n_push = new PushAffector("", this, 8*1/60f);
//		n_push.value = 1f;
//		n_push.wakeUp();
//		affectorNeuronList.addLast(n_push);
//		
//		//adding linear velocity sensor -> less than processor -> Turn CCW Affector
//		Neuron n_sens = new LinearVelocitySensor("", this);
//		Neuron n_proc = new LessThanProcessor("", 1.5f);
//		Neuron n_turn_ccw = new TurnCCWAffector("", this, 120*1/60f);
//		
//		affectorNeuronList.addLast(n_push);
//		sensorNeuronList.add(n_sens);
//		n_turn_ccw.addNeuralConnection(n_proc);
//		n_proc.addNeuralConnection(n_sens);
//		affectorNeuronList.add(n_turn_ccw);
	}
}
