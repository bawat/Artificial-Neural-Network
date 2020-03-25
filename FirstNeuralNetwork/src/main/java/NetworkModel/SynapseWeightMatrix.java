package NetworkModel;

public class SynapseWeightMatrix extends NeuralNetworkElement implements DerivativeUpdatable<Matrix2D>{
	private Matrix2D weights;
	private Matrix2D weightDerivatives;
	private NeuralLayer originLayer;
	private NeuralLayer targetLayer;
	
	public SynapseWeightMatrix(NeuralLayer originLayer, NeuralLayer targetLayer){
		this.originLayer = originLayer;
		this.targetLayer = targetLayer;
		weights = new Matrix2D(originLayer.size(), targetLayer.size());
	}
	public SynapseWeightMatrix(NeuralLayer originLayer, NeuralLayer targetLayer, Matrix2D inject){
		this.originLayer = originLayer;
		this.targetLayer = targetLayer;
		weights = new Matrix2D(inject);
	}
	
	public SynapseWeightMatrix randomise() {
		weights = weights.map(ele -> (double) Math.random());
		return this;
	}
	
	public Matrix2D getMatrix() {
		return new Matrix2D(weights);
	}
	
	class WeightProvider {
		private int originNeuron;
		private SynapseWeightMatrix weightMatrix;
		private WeightProvider(SynapseWeightMatrix weightMatrix, int originNeuron) {
			this.originNeuron = originNeuron;
			this.weightMatrix = weightMatrix;
		}
		
		public double toNeuron(int targetNeuron) {
			return weightMatrix.getMatrix().storedMatrix[originNeuron][targetNeuron];
		}
	}
	WeightProvider fromNeuron(int from){
		return new WeightProvider(this, from);
	}
	
	@Override
	public void updateBasedOnDerivative(double learningRate) {
		weights = weights.sub(weightDerivatives.scale(learningRate));
	}
	
	@Override
	public NeuralNetwork getAssociatedNetwork() {
		return originLayer.getAssociatedNetwork();
	}
	
	@Override
	public void calculateDerivativesForAll(Vector1D esimateValues, Vector1D expectedValues) {
		for(NeuralLayer layer : getAssociatedNetwork().layers.tail()) {
			getAssociatedNetwork().weights.from(layer.previous()).to(layer).calculateDerivative();
		}
	}
	
	private Matrix2D calculateDerivative() {
		weightDerivatives = new Matrix2D(weights.rows(), weights.columns());
		Vector1D biasDerivative = targetLayer.biases.get().getDerivative();
		boolean isInputLayer = originLayer.getIndex() == 0;
		
		for(int yp = 0; yp < weights.rows(); yp++) {
			for(int xp = 0; xp < weights.columns(); xp++) {
				int ip = yp, jp = xp;
				int lastLayerIndex = getAssociatedNetwork().layers.size()-1;
				int layersBehindOutput = lastLayerIndex - originLayer.getIndex();
				double preActivation = N­(layersBehindOutput).get(ip);
				weightDerivatives.storedMatrix[yp][xp] = biasDerivative.get(jp) * (isInputLayer? preActivation : ƒ(preActivation));
			}
		}
		return getDerivative();
	}
	
	@Override
	public Matrix2D getDerivative() {
		return new Matrix2D(weightDerivatives);
	}
}
