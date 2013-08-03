package neuralObjects;

public class InverterProcessor extends Neuron {

	public InverterProcessor(String id) {
		super(NeuronType.PROC_INVERTER, id);
	}

	@Override
	public float processNeuron() {
		if(inputList.isEmpty())
			output = 0;
		else
			output = -1 * inputList.getFirst().processNeuron();
		
		return output;
	}

}
