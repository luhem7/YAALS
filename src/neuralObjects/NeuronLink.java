package neuralObjects;

import java.util.LinkedList;

/**
 * This class contains the information necessary to link one neuron to another.
 * @author mehul
 *
 */
public class NeuronLink {

	public String fromID = ""; // The neuron where information is flowing out of
	public String toID = ""; //The neuron where information is flowing to
	
	@Override
	public String toString() {
		return "["+ fromID + " -> "+ "]";
	}

	
	/**
	 * Builds a List of neuron link objects from a string representing a set of connections.
	 * 
	 * Assumes that neuronLinkDef is of the form:
	 * neuron1 -> neuron2 -> neuron3 etc
	 * @param neuronLinkDef
	 * @return
	 */
	public static LinkedList<NeuronLink> buildNeuronLink(String neuronLinkDef){
		String[] parsedString = neuronLinkDef.split("->");
		
		if(parsedString.length < 2){
			System.out.println("There was an error parsing the following line: "+neuronLinkDef);
			return null;
		}
		
		int fromNeuron = 0, toNeuron = 1;
		LinkedList<NeuronLink> neuronLinkedList = new LinkedList<NeuronLink>();
		
		for(; toNeuron < parsedString.length; fromNeuron++, toNeuron++){
			NeuronLink nl = new NeuronLink();
			nl.fromID = parsedString[fromNeuron].trim();
			nl.toID = parsedString[toNeuron].trim();
			neuronLinkedList.add(nl);
		}
		
		return neuronLinkedList;
	}
}
