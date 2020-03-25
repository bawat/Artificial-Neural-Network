package NetworkModel.Tests;

import org.junit.Test;

import NetworkModel.NeuralLayer;
import NetworkModel.NeuralNetwork;
import NetworkModel.ResultSet;
import NetworkModel.TrainingData;

public class ModelTester {

	@Test
	public void twoHiddenLayersDerived() throws Exception {
		
		NeuralNetwork gradeEstimator = new NeuralNetwork.addInputLayer(new NeuralLayer(2))
				.addHiddenLayer(new NeuralLayer(3))
				.addHiddenLayer(new NeuralLayer(2))
				.addOutputLayer(new NeuralLayer(2));
		
		TrainingData data = new TrainingData.Builder()
			.input(3d, 5d).mapsTo(24d, 28d)
			.input(4d, 6d).mapsTo(40d, 50d)
			
			.normaliseInputs(24d, 24d)
			.normaliseOutputs(100d, 100d);
		
		
		
		System.out.println("Starting training...");
		
		ResultSet estimatedGrade = gradeEstimator.forwardPropagation(data);
		
		int iterationNumber, learningRate = 1;
		for(iterationNumber = 1; !estimatedGrade.equals(data, 0.001d); iterationNumber++) {
			
			gradeEstimator.backPropagationTraining(data, learningRate);
			//learningRate *= 1.1d;// Adaptive Global Learning rate https://youtu.be/4Gu4WZvqevc
			estimatedGrade = gradeEstimator.forwardPropagation(data);
			System.out.println("Estimate with learning rate " + learningRate + ": " + System.lineSeparator() + estimatedGrade);
			System.out.println("Errors: " + gradeEstimator.calculateErrors(data).toString() + "");
		}
		System.out.println("Training finished. Took " + iterationNumber + " iteratations. Result.");
		System.out.println(estimatedGrade);
		
	}
}
