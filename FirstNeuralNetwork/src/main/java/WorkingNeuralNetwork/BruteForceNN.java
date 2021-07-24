/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package WorkingNeuralNetwork;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BruteForceNN {
	public static void main(String[] args) {
		NN.InputLayerNode 
			n00 = new NN.InputLayerNode(0),
		    n01 = new NN.InputLayerNode(1);
		
		NN.HiddenLayerNode
			n10 = new NN.HiddenLayerNode(1,0),
		    n11 = new NN.HiddenLayerNode(1,1),
		    
			n20 = new NN.HiddenLayerNode(2,0),
		    n21 = new NN.HiddenLayerNode(2,1);
		
		NN.OutputLayerNode 
			n30 = new NN.OutputLayerNode(3,0),
		    n31 = new NN.OutputLayerNode(3,1);
		    
		NN.Weight
			w00to10 = new NN.Weight(n00,n10),
			w01to10 = new NN.Weight(n01,n10),
	
			w00to11 = new NN.Weight(n00,n11),
			w01to11 = new NN.Weight(n01,n11),
			
	
			w10to20 = new NN.Weight(n10,n20),
			w11to20 = new NN.Weight(n11,n20),
	
			w10to21 = new NN.Weight(n10,n21),
			w11to21 = new NN.Weight(n11,n21),
		    		
	
			w20to30 = new NN.Weight(n20,n30),
			w21to30 = new NN.Weight(n21,n30),
	
			w20to31 = new NN.Weight(n20,n31),
			w21to31 = new NN.Weight(n21,n31);
		
		NN.Bias
	    	b10 = new NN.Bias(n10),
		    b11 = new NN.Bias(n11),
		    
			b20 = new NN.Bias(n20),
		    b21 = new NN.Bias(n21),
		    
			b30 = new NN.Bias(n30),
		    b31 = new NN.Bias(n31);
		
		//LeCun Initialization
		NN.initWeights((fanIn, fanOut) -> new Random().nextGaussian()/((double) fanIn));
		NN.initBiases((fanIn, fanOut) -> new Random().nextGaussian()/((double) fanIn));
		
		//trainOneInput();
		trainTwoInputs();
	}
	
    public static void trainOneInput() {
    	
    	ArrayList<Double> input1 = new ArrayList<>();
    	input1.add(1d);
    	input1.add(0.5d);
    	
    	ArrayList<Double> expectedOutput1 = new ArrayList<>();
    	expectedOutput1.add(7d);
    	expectedOutput1.add(20d);
    	
    	//bruteForceTraining(input1, expectedOutput1, 0.1);
    	System.out.println("Testing [1.0,0.5] -> " + forwardProp(input1));
    }
    
    public static void trainTwoInputs() {
    	
    	ArrayList<Double> input1 = new ArrayList<>();
    	input1.add(1d);
    	input1.add(0.5d);
    	
    	ArrayList<Double> expectedOutput1 = new ArrayList<>();
    	expectedOutput1.add(7d);
    	expectedOutput1.add(20d);
    	

    	
    	ArrayList<Double> input2 = new ArrayList<>();
    	input2.add(0.3d);
    	input2.add(0.7d);
    	
    	ArrayList<Double> expectedOutput2 = new ArrayList<>();
    	expectedOutput2.add(101d);
    	expectedOutput2.add(99d);
    	

    	
    	ArrayList<Double> input3 = new ArrayList<>();
    	input3.add(0.1d);
    	input3.add(0.1d);
    	
    	ArrayList<Double> expectedOutput3 = new ArrayList<>();
    	expectedOutput3.add(69d);
    	expectedOutput3.add(69d);
    	
    	
    	InputsAndExpectedOutputs trainingData = new InputsAndExpectedOutputs(
    			new InputAndExpectedOutput(input1, expectedOutput1),
    			new InputAndExpectedOutput(input2, expectedOutput2),
    			new InputAndExpectedOutput(input3, expectedOutput3)
    	);
    	
    	LocalDateTime startTime = LocalDateTime.now();
    	bruteForceTraining(trainingData);
    	System.out.println("Testing [1.0,0.5] -> [7, 20] actual (" + forwardProp(input1) + ")");
    	System.out.println("Testing [0.3,0.7] -> [101, 99] actual (" + forwardProp(input2) + ")");
    	System.out.println("Testing [0.1,0.1] -> [69, 69] actual (" + forwardProp(input3) + ")");
    	System.out.println("Time Taken: " + startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) + " seconds ");
    }
    
    static class InputsAndExpectedOutputs{
    	ArrayList<InputAndExpectedOutput> trainingData = new ArrayList<>();
    	InputsAndExpectedOutputs(InputAndExpectedOutput... trainingData){
    		this.trainingData = new ArrayList<>(Arrays.asList(trainingData));
    	}
    }
    static class InputAndExpectedOutput{
    	ArrayList<Double> inputs;
    	ArrayList<Double> outputs;
    	InputAndExpectedOutput(ArrayList<Double> inputs, ArrayList<Double> outputs){
    		this.inputs = inputs;
    		this.outputs = outputs;
    	}
    }
    
    private static Stream<NN.Node> getNodes(){
    	return Stream.concat(NN.outputLayer.stream(), Stream.concat(NN.inputLayer.stream(), NN.hiddenLayers.stream()));
    }
    
    private static ArrayList<NN.Node> nodesInLayer(int layer){
    	return new ArrayList<NN.Node>(getNodes().filter(n -> n.layer == layer).collect(Collectors.toList()));
    }
    private static NN.Weight weightBetween(NN.Node from, NN.Node to){
    	return NN.weights.stream().filter(w -> w.startNode == from && w.endNode == to).findAny().get();
    }
    private static NN.Bias biasAt(NN.Node node){
    	return NN.biases.stream().filter(b -> b.parent == node).findAny().get();
    }
    
    public static double getNNError(InputAndExpectedOutput trainingSample) {
    	return getNNError(trainingSample.inputs, trainingSample.outputs, forwardProp(trainingSample.inputs));
    }
    public static double getNNError(ArrayList<Double> inputs, ArrayList<Double> expectedValues) {
    	return getNNError(inputs, expectedValues, forwardProp(inputs));
    }
    private static double getNNError(ArrayList<Double> inputs, ArrayList<Double> expectedValues, ArrayList<Double> actualValues) {
    	double totalError = 0;
    	for(int i = 0; i < expectedValues.size(); i++)
    		totalError += Math.pow(expectedValues.get(i) - actualValues.get(i), 2);
    	return totalError;
    }
    public static double getNNError(InputsAndExpectedOutputs trainingData) {
    	ArrayList<InputAndExpectedOutput> data = trainingData.trainingData;
    	double totalError = 0;
    	for(int i = 0; i < data.size(); i++)
    		totalError += Math.pow(getNNError(data.get(i)), 2);
    	return totalError;
    }
    
    public static ArrayList<Double> forwardProp(ArrayList<Double> inputs){
    	ArrayList<Double> prevBins = inputs;
    	
    	int layer = 0;
    	while(nodesInLayer(++layer).size() != 0) {
    		ArrayList<NN.Node> prevLayer = nodesInLayer(layer-1);
    		ArrayList<NN.Node> thisLayer = nodesInLayer(layer);
    		
    		ArrayList<Double> bins = new ArrayList<Double>();
    		for(NN.Node node : thisLayer) {
    			double postActivationValue = 0d;
    			for(NN.Node prevNode : prevLayer) {
    				postActivationValue += prevBins.get(prevNode.nodeIndex) * weightBetween(prevNode, node).value;
    			}
    			postActivationValue += biasAt(node).value;
    			
    			if(nodesInLayer(layer+1).size() != 0)
    				postActivationValue = NN.activationFunction.apply(postActivationValue);
    			
    			bins.add(postActivationValue);
    		}
    		
    		prevBins = bins;
    	}
    	return prevBins;
    }
    
    private static Stream<NN.NNParameter> getNNParameters(){
    	return Stream.concat(NN.weights.stream(), NN.biases.stream());
    }
    static class TrainingResult{
    	NN.NNParameter parameterToChange;
    	double newValue;
    	double newResultingError;
    }
    public static void bruteForceTraining(InputsAndExpectedOutputs trainingData) {
    	double currentError;
    	double ammountToChangeBy = 0.1;
    	
		while(true) {
			currentError = getNNError(trainingData);
			System.out.println("Error: " + currentError);
			
	    	if(currentError < Math.pow(10, -30))
	    		return;
	    	
	    	final double _ammountToChangeBy = ammountToChangeBy;
	    	TrainingResult bestChoice = getNNParameters().flatMap(nnParameter -> {
	    		double originalValue = nnParameter.value;
	    		
	    		
	    		nnParameter.value = originalValue + _ammountToChangeBy;
	    		
	    		TrainingResult increase = new TrainingResult();
	    		increase.parameterToChange = nnParameter;
	    		increase.newValue = nnParameter.value;
	    		increase.newResultingError = getNNError(trainingData);
	    		
	    		nnParameter.value = originalValue - _ammountToChangeBy;
	    		
	    		TrainingResult decrease = new TrainingResult();
	    		decrease.parameterToChange = nnParameter;
	    		decrease.newValue = nnParameter.value;
	    		decrease.newResultingError = getNNError(trainingData);
	    		
	    		nnParameter.value = originalValue;
	    		
	    		return Arrays.stream(new TrainingResult[] {increase, decrease});
	    	}).collect(Collectors.minBy((result1, result2) -> {
	    		return Double.compare(result1.newResultingError, result2.newResultingError);
	    	})).get();
	    	
	    	if(bestChoice.newResultingError < currentError) {
	    		bestChoice.parameterToChange.value = bestChoice.newValue;
	    	} else {
	    		ammountToChangeBy /= 2d;
	    	}
    	}
    }
    
    static class NN{
    	static ArrayList<InputLayerNode> inputLayer = new ArrayList<InputLayerNode>();
    	static ArrayList<HiddenLayerNode> hiddenLayers = new ArrayList<HiddenLayerNode>();
    	static ArrayList<OutputLayerNode> outputLayer = new ArrayList<OutputLayerNode>();

    	static ArrayList<Weight> weights = new ArrayList<Weight>();
    	static ArrayList<Bias> biases = new ArrayList<Bias>();
    	
    	static Function<Double, Double> activationFunction = (val) -> 1d/(1d + Math.pow(Math.E,-val));
    	
    	public static void initWeights(BiFunction<Integer, Integer, Double> initialisationFunction) {
    		weights.forEach(weight -> weight.value = initialisationFunction.apply(
    				nodesInLayer(weight.startNode.layer).size(),
    				nodesInLayer(Math.min(weight.endNode.layer+1, outputLayer.get(0).layer)).size()
    			));
    	}
    	public static void initBiases(BiFunction<Integer, Integer, Double> initialisationFunction) {
    		biases.forEach(bias -> bias.value = initialisationFunction.apply(
    				nodesInLayer(bias.parent.layer-1).size(),
    				nodesInLayer(Math.min(bias.parent.layer+1, outputLayer.get(0).layer)).size()
    			));
    	}
    	
    	private static class Node{
    		int layer;
    		int nodeIndex;
    		Node(int layer, int nodeIndex){
    			this.layer = layer; this.nodeIndex = nodeIndex;
    		}
    	}
    	static class InputLayerNode extends Node{
			InputLayerNode(int nodeIndex) {
				super(0, nodeIndex);
				inputLayer.add(this);
			}
    	}
    	static class HiddenLayerNode extends Node{
    		HiddenLayerNode(int layer, int nodeIndex) {
				super(layer, nodeIndex);
				hiddenLayers.add(this);
			}
    	}
    	static class OutputLayerNode extends Node{
    		OutputLayerNode(int layer, int nodeIndex) {
				super(layer, nodeIndex);
				outputLayer.add(this);
			}
    	}
    	
    	static abstract class NNParameter{
    		double value = 0;
    	}
    	static class Weight extends NN.NNParameter{
			Node startNode;
    		Node endNode;
    		{
    			weights.add(this);
    		}
    		public Weight(Node from, Node to) {
    			startNode = from;
    			endNode = to;
			}
    	}
    	static class Bias extends NN.NNParameter{
    		Node parent;
    		{
    			biases.add(this);
    		}
    		public Bias(OutputLayerNode parent) {
    			this.parent = parent;
			}
    		public Bias(HiddenLayerNode parent) {
    			this.parent = parent;
			}
    	}
    }
}
