package NetworkModel;

public class SynapseBiasVector extends NeuralNetworkElement implements DerivativeUpdatable<Vector1D>{
	private Vector1D biases;
	private Vector1D biasDerivatives;
	private NeuralLayer parentLayer;
	
	public SynapseBiasVector(NeuralLayer parent){
		parentLayer = parent;
		biases = new Vector1D(parentLayer.size());
	}
	public SynapseBiasVector(NeuralLayer parent, Vector1D inject){
		parentLayer = parent;
		biases = new Vector1D(inject);
	}
	
	public SynapseBiasVector randomise() {
		biases = biases.map(ele -> (double) Math.random());
		return this;
	}
	
	public Vector1D getVector() {
		return new Vector1D(biases);
	}
	
	@Override
	public void calculateDerivativesForAll(Vector1D esimateValues, Vector1D expectedValues) {
		
		NeuralLayer firstLayer = getAssociatedNetwork().layers.get(0);
		SynapseBiasVector firstBiasTerm = firstLayer.next().biases.get();
		
		firstBiasTerm.recursiveCalculateDerivative(esimateValues, expectedValues);
	}
	
	
	private Vector1D recursiveCalculateDerivative(Vector1D yHat, Vector1D y) {
		int layersBehindOutputLayer = countLayersBehindOutputLayer(parentLayer);
		Vector1D leftmostCalculation = ÉPrime(N≠(layersBehindOutputLayer)); 
		biasDerivatives = new Vector1D(biases.size());
		
		boolean isOutputLayer = layersBehindOutputLayer == 0;
		if(isOutputLayer) {
			biasDerivatives = leftmostCalculation.elementMultiply(yHat.sub(y).scale(2));
			return biasDerivatives;
		}
		
		NeuralLayer nextLayer = getNextLayer(parentLayer);
		Vector1D nextLayerDerivative = nextLayer.biases.get().recursiveCalculateDerivative(yHat, y);
		SynapseWeightMatrix weights = getAssociatedNetwork().weights.from(parentLayer).to(nextLayer);
		
		for(int sourceNeuron : parentLayer) {
			
			//Calculate the sum part
			double sum = 0;
			for(int targetNeuron : nextLayer) {
				sum += nextLayerDerivative.get(targetNeuron) * weights.fromNeuron(sourceNeuron).toNeuron(targetNeuron);
			}
			//Finish the calculation by multiplying by the fPrime part
			biasDerivatives.set(sourceNeuron, leftmostCalculation.get(sourceNeuron) * sum);
			
		}
		
		return getDerivative();
	}
	
	@Override
	public void updateBasedOnDerivative(double learningRate) {
		biases = biases.sub(biasDerivatives.scale(learningRate));
	}

	@Override
	public NeuralNetwork getAssociatedNetwork() {
		return parentLayer.getAssociatedNetwork();
	}
	
	@Override
	public Vector1D getDerivative() {
		return new Vector1D(biasDerivatives);
	}
	
	private NeuralLayer getNextLayer(NeuralLayer layer) {
		return getAssociatedNetwork().layers.get(layer.getIndex()+1);
	}
	private int countLayersBehindOutputLayer(NeuralLayer layer) {
		return getLayerFinalIndex()-layer.getIndex();
	}
	private int getLayerFinalIndex() {
		return getAssociatedNetwork().layers.size()-1;
	}
}
