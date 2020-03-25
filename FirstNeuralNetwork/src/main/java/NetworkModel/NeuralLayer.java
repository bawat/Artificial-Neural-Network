package NetworkModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * Represents a single hidden layer
 * @author Bawat
 *
 */
public class NeuralLayer extends NeuralNetworkElement implements Iterable<Integer>{
	private int numberOfNeurons;
	private NeuralNetwork parent;
	private Vector1D preActivationValues;
	protected Optional<SynapseBiasVector> biases = Optional.empty();
	public NeuralLayer(int numberOfNeurons){
		this.numberOfNeurons = numberOfNeurons;
	}
	
	public int size() {
		return numberOfNeurons;
	}

	@Override
	public NeuralNetwork getAssociatedNetwork() {
		return parent;
	}
	public void setAssociatedNetwork(NeuralNetwork network) {
		parent = network;
	}

	public NeuralLayer previous() {
		ArrayList<NeuralLayer> layers = getAssociatedNetwork().layers;
		int myIndex = layers.indexOf(this);
		return layers.get(myIndex - 1);
	}

	public NeuralLayer next() {
		ArrayList<NeuralLayer> layers = getAssociatedNetwork().layers;
		int myIndex = layers.indexOf(this);
		return layers.get(myIndex + 1);
	}

	@Override
	public Iterator<Integer> iterator() {
		Iterator<Integer> it = new Iterator<Integer>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < numberOfNeurons;
            }

            @Override
            public Integer next() {
                return currentIndex++;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
		return it;
	}

	public void setPreActivationValues(Vector1D preActivationZ) {
		preActivationValues = new Vector1D(preActivationZ);
	}

	public Vector1D getPreActivationValues() {
		return preActivationValues;
	}

	public int getIndex() {
		return getAssociatedNetwork().layers.indexOf(this);
	}
}
