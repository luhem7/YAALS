package neuralObjects;

import java.text.ParseException;

/**
 * This class contains all the information that needed to generate a Neuron except for the connections between Neurons.
 * @author mehul
 *
 */
public class NeuronTemplate {
	public NeuronType myType;
	public String id;
	
	/* These are two values that may or may not be used
	 * by the implementation of this Neuron
	 */
	public static final int MAX_NEURON_VALUES = 2;
	public float[] values = new float[MAX_NEURON_VALUES];

	public NeuronTemplate(NeuronType myType, String id) {
		this.myType = myType;
		this.id = id;
	}
	
	@Override
	public String toString(){
		String result = "["+myType.getName();
		for(int i=0; i<values.length; i++){
			result += ","+values[i];
		}
		return result + "]"; 
	}
	
	/**
	 * Attempts to generate a new neuron template from the given input neuron definition
	 * @param neuronDef
	 * @return
	 */
	public static NeuronTemplate BuildNeuronTemplate(String neuronDef){
		String[] parsedString = neuronDef.split(" ");
		
		if(parsedString.length < 2){
			System.out.println("There was an error parsing the following line: "+neuronDef);
			return null;
		}
		
		NeuronType newType;
		
		if((newType = NeuronType.getNeuronType(parsedString[1])) == null ){
			System.out.println("There was an error parsing the following line: "+neuronDef);
			return null;
		}
		
		NeuronTemplate n = new NeuronTemplate(newType, parsedString[1]);
		
		for(int i=2; i<parsedString.length; i++){
			if(i >= NeuronTemplate.MAX_NEURON_VALUES+2){
				System.err.println("Attempted to load too many values into neuron: "+parsedString[0]);
				break;
			}
			
			try{
				n.values[i-2] = Float.parseFloat(parsedString[i]);
			} catch(Exception e) {
				System.err.println("Could not understand following number: "+parsedString[i]);
			}
		}
		
		return n;
	}
}