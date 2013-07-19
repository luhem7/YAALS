package neuralObjects;

public class LessThanProcessor extends Neuron {
	float threshold = 0;

	/**
	 * An alternative to use the neuron to compare the a single input
	 * to a threshold instead of using this neuron to compare two inputs
	 * @param threshold
	 */
	public LessThanProcessor(float threshold) {
		super(NeuronType.PROC_LESSTHAN);
		this.threshold = threshold;
	}
	
	public LessThanProcessor() {
		super(NeuronType.PROC_LESSTHAN);
		threshold = 1;
	}

	@Override
	public float processNeuron() {
		if(connections.isEmpty())
			return 0;
		else if (connections.size() == 1){
			return connections.getFirst().processNeuron() < threshold ? 1 : 0;
		}
		else
			return connections.getFirst().processNeuron() < connections.get(1).processNeuron() ? 1 : 0;
	}

}
