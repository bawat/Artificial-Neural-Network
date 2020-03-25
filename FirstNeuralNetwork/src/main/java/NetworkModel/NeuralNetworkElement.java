package NetworkModel;

import java.util.function.Function;

public abstract class NeuralNetworkElement {
	protected abstract NeuralNetwork getAssociatedNetwork();
	
	
	/**
	 * Gets the pre-activation values of a neuron layer
	 * @param layersBehindOutputLayer
	 * @return
	 */
	protected Vector1D N≠(int layersBehindOutputLayer) {
		SpicyArrayList<NeuralLayer> layers = getAssociatedNetwork().layers;
		NeuralLayer specifiedLayer = layers.get(layers.size()-1-layersBehindOutputLayer);
		
		return specifiedLayer.getPreActivationValues();
	}
	protected <T> T ÉPrime(Mappable<T> vector) {
		return vector.map(this::ÉPrime);
	}
	protected double ÉPrime(double val) {
		return getAssociatedNetwork().activationFunction.derivativeGetValue(val);
	}
	protected <T> T É(Mappable<T> vector) {
		return vector.map(this::É);
	}
	protected double É(double val) {
		return getAssociatedNetwork().activationFunction.getValue(val);
	}
}
