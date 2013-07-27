package neuralObjects;

import java.util.LinkedList;

import dnaObjects.NeuronTemplate;

/**
 * This class represents one neuron. 
 * @author mehul
 *
 */
public abstract class Neuron {
	public String id = null;
	public float value; //The value stored in this neuron
	protected NeuronType myType = null;
	protected LinkedList<Neuron> connectionsList; //The other neurons that this neuron is connected to
	
	public Neuron(NeuronType myType, String id) {
		this.myType = myType;
		this.id = id;
		connectionsList = new LinkedList<Neuron>();
	}
	
	public Neuron(NeuronTemplate neuronTemplate){
		this.myType = neuronTemplate.myType;
		connectionsList = new LinkedList<Neuron>();
	}
	
	/**
	 * Connects this neuron to "newConnection"
	 * @param newConnection
	 */
	public void addNeuralConnection(Neuron newConnection){
		this.connectionsList.addLast(newConnection);
	}
	
	public NeuronType getNeuronType(){
		return myType;
	}
	
	/**
	 * This method underlies the function of the Neuron. It returns a value that can be either used by
	 * the simulation, or used by other neurons.
	 * @return
	 */
	public abstract float processNeuron();
	
	/**
	 * A utility method that returns a Neuron from the given list based on its give ID.
	 * Returns null if none is found
	 */
	public static Neuron getNeuronByID(LinkedList<Neuron> neuronList, String id){
		for(Neuron n: neuronList){
			if(n.id.equals(id))
				return n;
		}
		
		//If we got here, then we know that we did not find a match
		return null;
	}
}
