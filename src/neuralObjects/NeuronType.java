package neuralObjects;

/**
 * The different kinds of neurons that are present
 * @author mehul
 *
 */
public enum NeuronType {
	//Sensor neuron types
	SENSOR_LINEAR_VEL ("lin_vel_sen"),
	SENSOR_BLINKER ("blinker_sen"),
	
	//Processor neuron types
	PROC_LESSTHAN ("less_than_proc"),
	PROC_INVERTER ("invert_proc"),
	
	//Affector neuron types: These neurons affect the state of the Creature 
	AFFECTOR_PUSH ("push_aff"),
	AFFECTOR_TURN_CCW ("turn_ccw_aff");
	
	private String name = "";

	private NeuronType(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
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
			if( e.toString().equals(name) )
				return e;
		}
		
		System.err.println("Could not parse the following neuron type: "+name);
		return null;
	}
}
