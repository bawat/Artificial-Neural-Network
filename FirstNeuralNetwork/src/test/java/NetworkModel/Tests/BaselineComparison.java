package NetworkModel.Tests;

import org.junit.Test;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class BaselineComparison {

	@Test
	public void test() {
		
		//Create new Perceptron Network
		MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 2, 2);
		
		System.out.println("Input Neurons: " + neuralNetwork.getInputsCount());
		System.out.println("Output Neurons: " +neuralNetwork.getOutputsCount());
		neuralNetwork.randomizeWeights();
		
		//Create training set
		DataSet trainingSet = new DataSet(neuralNetwork.getInputsCount(), neuralNetwork.getOutputsCount());
		trainingSet.addRow(
			new DataSetRow(new double[] {3d/24d, 5d/24d}, new double[] {0.90d, 0.10d})
		);
		trainingSet.addRow(
			new DataSetRow(new double[] {5d/24d, 1d/24d}, new double[] {0.14d, 0.80d})
		);/*
		trainingSet.addRow(
				new DataSetRow(new double[] {1, 0}, new double[] {10})
		);
		trainingSet.addRow(
				new DataSetRow(new double[] {1, 1}, new double[] {11})
		);*/
		
		BackPropagation rule = new BackPropagation();
		rule.setMaxError(0.0000001d);
		neuralNetwork.setLearningRule(rule);
		
		//Learn the training set
		neuralNetwork.learn(trainingSet);
		
		double[] networkOutput;
		
		//Set network input
		neuralNetwork.setInput(3d/24d, 5d/24d);
		
		//Calculate network
		neuralNetwork.calculate();
		
		//Get network output
		networkOutput = neuralNetwork.getOutput();
		System.out.println("Output: " + networkOutput[0] + ", " + networkOutput[1]);
		
		//Set network input
		neuralNetwork.setInput(5d/24d, 1d/24d);
		
		//Calculate network
		neuralNetwork.calculate();
		
		//Get network output
		networkOutput = neuralNetwork.getOutput();
		System.out.println("Output: " + networkOutput[0] + ", " + networkOutput[1]);
	}

}
