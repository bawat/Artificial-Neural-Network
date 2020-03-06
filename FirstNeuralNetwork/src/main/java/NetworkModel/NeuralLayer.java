package NetworkModel;

/**
 * Represents a single hidden layer
 * @author Bawat
 *
 */
public class NeuralLayer {
	private int numberOfNeurons = 0;
	public NeuralLayer(int numberOfNeurons){
		this.numberOfNeurons = numberOfNeurons;
	}
	
	public int size() {
		return numberOfNeurons;
	}
}
