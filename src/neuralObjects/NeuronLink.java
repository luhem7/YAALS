package neuralObjects;

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

}
