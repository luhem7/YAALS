package neuralObjects;

/**
 * The different kinds of neurons that are present
 * @author mehul
 *
 */
public enum NeuronType {
	//Sensor neuron types
	SENSOR_LINEAR_VEL ("lin_vel_sen"),
	
	//Processor neuron types
	PROC_LESSTHAN ("less_than_proc"),
	
	//Affector neuron types: These neurons affect the state of the Creature 
	AFFECTOR_PUSH ("push_aff"), //If value is >= 1 pushes the creature in the direction it faces. If value is less than or equal to -1, it pushes the creature backwards
	AFFECTOR_TURN_CCW ("turn_ccw_aff"); //If value >= 1 turns the creature counter clock wise. If value is less than or equal to -1, it turns the creature clockwise.
	
	private String name = "";
	
	private NeuronType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the Neuron Type that corresponds to the given name.
	 * Returns null if there was no match
	 * @param name
	 * @return
	 */
	public static NeuronType getNeuronType(String name){
		for (NeuronType e: NeuronType.values()){
			if( e.getName().equals(name) )
				return e;
		}
		
		System.err.println("Could not parse the following neuron type: "+name);
		return null;
	}
}
