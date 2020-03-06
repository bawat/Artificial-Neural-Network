package NetworkModel;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import NetworkModel.ActivationFunctions.ActivationFunction;
import NetworkModel.ActivationFunctions.SigmoidActivation;
/**
 * All matrices are emulated using arrays. Size notation is in mathmatical order
 * @author Bawat
 */
public class NeuralNetwork{
	
	ArrayList<NeuralLayer> layers;
	ArrayList<Matrix2D> synapseWeights;
	ArrayList<Matrix2D> synapseBiases;
	ArrayList<Matrix2D> preActivationValues = new ArrayList<Matrix2D>();
	ArrayList<Matrix2D> postActivationValues = new ArrayList<Matrix2D>();
	ActivationFunction activationFunction = new SigmoidActivation();
	NeuralNetwork(NeuralNetwork.Builder builder){
		layers = (ArrayList<NeuralLayer>) builder.layers.clone();
		synapseWeights = (ArrayList<Matrix2D>) builder.synapseWeights.clone();
		synapseBiases = (ArrayList<Matrix2D>) builder.synapseBiases.clone();
	}
	
	public static class Builder{
		private ArrayList<NeuralLayer> layers = new ArrayList<NeuralLayer>();
		private ArrayList<Matrix2D> synapseWeights = new ArrayList<Matrix2D>();
		private ArrayList<Matrix2D> synapseBiases = new ArrayList<Matrix2D>();
		public Builder(NeuralLayer inputLayer){
			layers.add(inputLayer);
		}
		
		private NeuralLayer getPreviousLayer() {
			return layers.get(layers.size()-1);
		}
		
		public Builder addHiddenLayer(NeuralLayer hiddenLayer) {
			Matrix2D synapseWeightMatrix = new Matrix2D(getPreviousLayer().size(), hiddenLayer.size());
			synapseWeightMatrix = synapseWeightMatrix.map(ele -> (double) Math.random());
			Matrix2D synapseBiasMatrix = new Matrix2D(hiddenLayer.size(), 1);
			synapseBiasMatrix = synapseBiasMatrix.map(ele -> (double) Math.random());
			
			return addHiddenLayerWithSpecificWeights(hiddenLayer, synapseWeightMatrix, synapseBiasMatrix);
		}
		public Builder addHiddenLayerWithSpecificWeights(NeuralLayer hiddenLayer, Matrix2D injectedInitialWeights, Matrix2D injectedInitialBiases) {
			synapseWeights.add(injectedInitialWeights);
			synapseBiases.add(injectedInitialBiases);
			layers.add(hiddenLayer);
			return this;
		}
		
		public NeuralNetwork addOutputLayer(NeuralLayer outputLayer) {
			Matrix2D synapseWeightMatrix = new Matrix2D(getPreviousLayer().size(), outputLayer.size());
			synapseWeightMatrix = synapseWeightMatrix.map(ele -> (double) Math.random());
			Matrix2D synapseBiasMatrix = new Matrix2D(outputLayer.size(), 1);
			synapseBiasMatrix = synapseBiasMatrix.map(ele -> (double) Math.random());
			
			return addOutputLayerWithSpecificWeights(outputLayer, synapseWeightMatrix, synapseBiasMatrix);
		}
		public NeuralNetwork addOutputLayerWithSpecificWeights(NeuralLayer outputLayer, Matrix2D injectedInitialWeights, Matrix2D injectedInitialBiases) {
			synapseWeights.add(injectedInitialWeights);
			synapseBiases.add(injectedInitialBiases);
			layers.add(outputLayer);
			return new NeuralNetwork(this); 
		}
	}
	
	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	//dim1 Number of inputs
	//dim2 Variables per input
	public Matrix2D forwardPropagation(Matrix2D inputMatrixX) throws Exception {
		preActivationValues.clear(); postActivationValues.clear();
		Matrix2D postFirstHiddenLayerA = forwardPropagationIteration(inputMatrixX, synapseWeights.get(0), synapseBiases.get(0));
		
		Matrix2D previousLayerOutputA = postFirstHiddenLayerA;
		for(int layerN = 1; layerN < layers.size()-1; layerN++) {
			previousLayerOutputA = forwardPropagationIteration(previousLayerOutputA, synapseWeights.get(layerN), synapseBiases.get(layerN));
		}
		
		Matrix2D finalLayerResultYHat = previousLayerOutputA;
		return finalLayerResultYHat;
	}
	private Matrix2D forwardPropagationIteration(Matrix2D previousLayerOutput, Matrix2D currentLayerWeights, Matrix2D currentLayerBiases) throws Exception{
		Matrix2D preActivationFunctionZ = previousLayerOutput.matrixMultiply(currentLayerWeights).add(currentLayerBiases.transpose().stretchVertically(previousLayerOutput.rows()));
		preActivationValues.add(preActivationFunctionZ);
		Matrix2D postActivationFunctionA = preActivationFunctionZ.map(activationFunction::getValue);
		postActivationValues.add(postActivationFunctionA);
		return postActivationFunctionA;
	}

	public void backPropagationTraining(Matrix2D trainingInputMatrixX, Matrix2D trainingOutputY, double learningRate) throws Exception {
		Matrix2D neuralEstimateYHat = forwardPropagation(trainingInputMatrixX);
		//Aliasing
		Matrix2D y = trainingOutputY, yHat = neuralEstimateYHat, x = trainingInputMatrixX;
		
		Matrix2D djdw3 = generateGradientMatrix(GradientMatrixType.weights, 0, y, yHat, x);
		Matrix2D djdb3 = generateGradientMatrix(GradientMatrixType.biases , 0, y, yHat, x);
		Matrix2D djdw2 = generateGradientMatrix(GradientMatrixType.weights, 1, y, yHat, x);
		Matrix2D djdb2 = generateGradientMatrix(GradientMatrixType.biases , 1, y, yHat, x);
		Matrix2D djdw1 = generateGradientMatrix(GradientMatrixType.weights, 2, y, yHat, x);
		Matrix2D djdb1 = generateGradientMatrix(GradientMatrixType.biases , 2, y, yHat, x);

		w(3).destructiveUpdate(djdw3.scale(-Math.abs(learningRate)));
		w(2).destructiveUpdate(djdw2.scale(-Math.abs(learningRate)));
		w(1).destructiveUpdate(djdw1.scale(-Math.abs(learningRate)));

		b(3).destructiveUpdate(djdb3.scale(-Math.abs(learningRate)));
		b(2).destructiveUpdate(djdb2.scale(-Math.abs(learningRate)));
		b(1).destructiveUpdate(djdb1.scale(-Math.abs(learningRate)));
	}
	private enum GradientMatrixType{ weights, biases }
	private Matrix2D generateGradientMatrix(GradientMatrixType type, int layersBehindFrontLayer, Matrix2D y, Matrix2D yHat, Matrix2D trainingInputMatrixX) {

		if(type == GradientMatrixType.weights && layersBehindFrontLayer == 0) {
			//System.out.println("Error: " + yHat.sub(y));
		}
		
		Matrix2D weights = synapseWeights.get(synapseWeights.size()-1-layersBehindFrontLayer);
		Matrix2D weightGradient = new Matrix2D(weights).empty();
		
		for(int inputN = 0; inputN < trainingInputMatrixX.rows(); inputN++) {

			double roe = y.sub(yHat).transpose().map(ele -> (double)Math.pow(ele, 2)).verticalBatch_SumVertically().getColumn(inputN).val();
			
			for(int yp = 0; yp < weights.getRows(); yp++) {
				for(int xp = 0; xp < weights.getColumns(); xp++) {
					double gradient = roe;
					if(layersBehindFrontLayer == 0) {
						gradient *= (yHat.sub(y).getRow(inputN).getColumn(xp).val()) * fPrime(Nminus(0, xp)) * ((type == GradientMatrixType.weights)?f(Nminus(1, yp)) : 1);
					}
					if(layersBehindFrontLayer == 1) {
						double temp = 0;
						for(int s = 0; s < y.columns(); s++) {
							temp += (yHat.sub(y).getRow(inputN).getColumn(s).val()) * fPrime(Nminus(0, s)) * fPrime(Nminus(1, xp)) * weightNminus(1, xp, s) * ((type == GradientMatrixType.weights)?f(Nminus(2, yp)): 1);
						}
						
						gradient *= temp;
					}
					if(layersBehindFrontLayer == 2) {
						double temp = 0;
						for(int s = 0; s < y.columns(); s++) {
							double temp2 = 0;
							for(int r = 0; r < LengthNminus(1); r++) {
								temp2 += fPrime(Nminus(1, r)) * weightNminus(1, r, s) * fPrime(Nminus(2, xp)) * weightNminus(2, xp, r) * trainingInputMatrixX.getRow(0).getColumn(yp).val();
							}
							temp += (yHat.sub(y).getRow(inputN).getColumn(s).val()) * fPrime(Nminus(0, s)) * temp2;
						}
						gradient *= temp;
					}
					
					weightGradient.storedMatrix[yp][xp] += gradient;
				}
			}
		}
		
		//Average the calculated gradient between each output's opinion of how it should change.
		return weightGradient;
	}
	private double Nminus(int layer, int depth){
		Matrix2D z = preActivationValues.get(preActivationValues.size()-1-layer);
		return z.storedMatrix[0][depth];
	}
	private double LengthNminus(int layer){
		Matrix2D z = preActivationValues.get(preActivationValues.size()-1-layer);
		return z.storedMatrix[0].length;
	}
	private double weightNminus(int exitLayer, int exitLayerDepth, int entranceLayerDepth) {
		Matrix2D w = synapseWeights.get(synapseWeights.size()-exitLayer);
		return w.storedMatrix[exitLayerDepth][entranceLayerDepth];
	}
	
	//Helper functions
	private Matrix2D w(int index) {
		return synapseWeights.get(index-1);
	}
	private Matrix2D b(int index) {
		return synapseBiases.get(index-1);
	}
	private Matrix2D fPrime(Matrix2D matrix) {
		return matrix.map(activationFunction::derivativeGetValue);
	}
	private double fPrime(double val) {
		return activationFunction.derivativeGetValue(val);
	}
	private double f(double val) {
		return activationFunction.getValue(val);
	}
}
