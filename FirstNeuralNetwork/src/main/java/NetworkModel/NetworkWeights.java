package NetworkModel;

import java.util.ArrayList;
import java.util.function.Consumer;

public class NetworkWeights {
	private ArrayList<SynapseWeightMatrix> weights;
	public NetworkWeights(ArrayList<SynapseWeightMatrix> weights) {
		this.weights = weights;
	}
	
	class WeightProvider {
		private NeuralLayer originLayer;
		private WeightProvider(NeuralLayer originLayer) {
			this.originLayer = originLayer;
		}
		
		public SynapseWeightMatrix to(NeuralLayer targetLayer) {
			if(originLayer == targetLayer.previous()) {
				return weights.get(originLayer.getIndex());
			}
			return null;
		}
	}
	WeightProvider from(NeuralLayer from){
		return new WeightProvider(from);
	}
	
	public void forEach(Consumer<? super SynapseWeightMatrix> action){
		weights.stream().forEach(action);
	}

	public void calculateDerivativesForAll(Vector1D yHat, Vector1D y) {
		weights.get(0).calculateDerivativesForAll(yHat, y);
	}
}
