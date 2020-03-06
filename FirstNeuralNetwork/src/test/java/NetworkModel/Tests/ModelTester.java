package NetworkModel.Tests;

import org.testng.annotations.Test;

import NetworkModel.Matrix2D;
import NetworkModel.NeuralLayer;
import NetworkModel.NeuralNetwork;

public class ModelTester {

	@Test
	public void twoHiddenLayersDerived() throws Exception {
		
		NeuralNetwork gradeEstimator = new NeuralNetwork.Builder(new NeuralLayer(2))
				.addHiddenLayer(new NeuralLayer(3))
				.addHiddenLayer(new NeuralLayer(2))
				.addOutputLayer(new NeuralLayer(2));

		Matrix2D inputMatrix = new Matrix2D.Builder()
			.addRow(3, 5)
			.addRow(5, 1)
			//.addRow(10, 2)
			.build();
		Matrix2D maxValues = new Matrix2D.Builder()
			.addRow(24, 24)
			.build();
		inputMatrix = inputMatrix.normaliseVerticallyWith(maxValues);
		
		Matrix2D outputMatrix = new Matrix2D.Builder()
			.addRow(90, 10)
			.addRow(14, 80)
			//.addRow(70, 30)
			.build();
		Matrix2D maxValue = new Matrix2D.Builder()
			.addRow(100, 100)
			.build();
		outputMatrix = outputMatrix.normaliseVerticallyWith(maxValue);
		
		Matrix2D estimatedGrade = gradeEstimator.forwardPropagation(inputMatrix);
		
		System.out.println(estimatedGrade);
		int iterationNumber = 10;
		//while(!estimatedGrade.equals(outputMatrix, 0.001d)) {
		for(int i = 0; i < 200; i++) {
			gradeEstimator.backPropagationTraining(inputMatrix, outputMatrix, iterationNumber);
			iterationNumber *= 1.1;// Adaptive Global Learning rate https://youtu.be/4Gu4WZvqevc
			estimatedGrade = gradeEstimator.forwardPropagation(inputMatrix);
			System.out.println("Estimate with learning rate " + iterationNumber + ": " + System.lineSeparator() + estimatedGrade);
		}
		
		System.out.println(estimatedGrade);
		
	}
}
