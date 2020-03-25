package NetworkModel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import NetworkModel.ActivationFunctions.ActivationFunction;
/**
 * All matrices are emulated using arrays. Size notation is in mathmatical order
 * @author Bawat
 */
public class NeuralNetwork{
	
	SpicyArrayList<NeuralLayer> layers;
	NetworkWeights weights;
	public ActivationFunction activationFunction = ActivationFunction.SigmoidActivation;
	public NeuralNetwork(NeuralNetwork.addInputLayer builder){
		layers = new SpicyArrayList<NeuralLayer>(builder.layers);
		layers.stream().forEach(ele -> ele.setAssociatedNetwork(this));
		weights = new NetworkWeights(builder.synapseWeights);
	}
	
	public static class addInputLayer{
		private SpicyArrayList<NeuralLayer> layers = new SpicyArrayList<NeuralLayer>();
		private ArrayList<SynapseWeightMatrix> synapseWeights = new ArrayList<SynapseWeightMatrix>();
		
		public addInputLayer(NeuralLayer inputLayer){
			layers.add(inputLayer);
		}
		
		public NeuralNetwork addOutputLayer(NeuralLayer outputLayer) {
			return new NeuralNetwork(addHiddenLayer(outputLayer));
		}
		public NeuralNetwork addOutputLayerWithSpecificWeights(NeuralLayer outputLayer, SynapseWeightMatrix injectedInitialWeights, SynapseBiasVector injectedInitialBiases) {
			return new NeuralNetwork(addHiddenLayerWithSpecificWeights(outputLayer, injectedInitialWeights, injectedInitialBiases)); 
		}
		
		private NeuralLayer getPreviousLayer() {
			return layers.get(layers.size()-1);
		}
		public addInputLayer addHiddenLayer(NeuralLayer hiddenLayer) {
			SynapseWeightMatrix synapseWeightMatrix = new SynapseWeightMatrix(getPreviousLayer(), hiddenLayer).randomise();
			SynapseBiasVector synapseBiases = new SynapseBiasVector(hiddenLayer).randomise();
			
			return addHiddenLayerWithSpecificWeights(hiddenLayer, synapseWeightMatrix, synapseBiases);
		}
		public addInputLayer addHiddenLayerWithSpecificWeights(NeuralLayer hiddenLayer, SynapseWeightMatrix injectedInitialWeights, SynapseBiasVector injectedInitialBiases) {
			synapseWeights.add(injectedInitialWeights);
			hiddenLayer.biases = Optional.of(injectedInitialBiases);
			layers.add(hiddenLayer);
			return this;
		}
	}

	public ResultSet forwardPropagation(TrainingData data) throws Exception {
		ResultSet result = new ResultSet();
		for(int index = 0; index < data.mapping.size(); index++) {
			result.add(forwardPropagation(data.getNormalisedInput(index)));
		}
		return result;
	}
	public Vector1D forwardPropagation(Vector1D inputMatrixX) throws Exception {
		Vector1D previousLayerNeuronOutput = inputMatrixX;
		layers.first().setPreActivationValues(inputMatrixX);
		for(NeuralLayer layer : layers.tail()) {
			previousLayerNeuronOutput = forwardPropagationIteration(previousLayerNeuronOutput, weights.from(layer.previous()).to(layer), layer);
		}
		Vector1D finalLayerResultYHat = previousLayerNeuronOutput;
		return finalLayerResultYHat;
	}
	private Vector1D forwardPropagationIteration(Vector1D previousLayerOutputA, SynapseWeightMatrix currentLayerWeights, NeuralLayer targetLayer) throws Exception{
		Vector1D preActivationZ = previousLayerOutputA.multiplyByWeightMatrix(currentLayerWeights).add(targetLayer.biases.get().getVector());
		targetLayer.setPreActivationValues(preActivationZ);
		Vector1D postActivationA = preActivationZ.map(activationFunction::getValue);
		return postActivationA;
	}
	
	public void backPropagationTraining(TrainingData data, double learningRate) throws Exception {
		ResultSet result = forwardPropagation(data);
		
		Vector1D yHat, y;
		for(int index = 0; index < result.size(); index++) {
		
			//Aliasing
			yHat = result.get(index);
			y = data.getNormalisedOutput(index);
		
			//Collect all derivative data before modifying our model
			layers.get(1).biases.get().calculateDerivativesForAll(yHat, y);
			weights.calculateDerivativesForAll(yHat, y);
	
			//Update Model
			layers.stream().map(ele -> ele.biases).filter(biasVector -> biasVector.isPresent()).forEach(biasVector -> biasVector.get().updateBasedOnDerivative(learningRate));
			weights.forEach(ele -> ele.updateBasedOnDerivative(learningRate));
		}
	}
	
	public ArrayList<Double> calculateErrors(TrainingData data) throws Exception {
		ArrayList<Double> errors = new ArrayList<Double>();
		
		for(int index = 0; index < data.mapping.size(); index++) {
			Vector1D yHat = forwardPropagation(data.getNormalisedInput(index));
			Vector1D y = data.getNormalisedOutput(index);
			
			double error = 0;
			for(int neuron = 0; neuron < yHat.size(); neuron++) {
				error += Math.pow(y.sub(yHat).get(neuron), 2);
			}
			errors.add(error);
		}
		
		return errors;
	}
}
