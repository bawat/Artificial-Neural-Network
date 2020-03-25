package NetworkModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class TrainingData {
	HashMap<Vector1D, Vector1D> mapping;
	Vector1D inputNormaliser;
	Vector1D outputNormaliser;
	private TrainingData(HashMap<Vector1D, Vector1D> mapping, Vector1D inputNormaliser, Vector1D outputNormaliser) {
		this.mapping = new HashMap<Vector1D, Vector1D>(mapping);
		this.inputNormaliser = inputNormaliser;
		this.outputNormaliser = outputNormaliser;
	}
	
	public Vector1D getNormalisedInput(int index) {
		return mapping.keySet().toArray(new Vector1D[0])[index];
	}
	public Vector1D getNormalisedOutput(int index) {
		return mapping.get(mapping.keySet().toArray(new Vector1D[0])[mapping.size()-1-index]);
	}
	
	public static class Builder {
		HashMap<Vector1D, Vector1D> trainingSet = new HashMap<Vector1D, Vector1D>();
		HashMap<Vector1D, Vector1D> normalisedSet = new HashMap<Vector1D, Vector1D>();
		
		Integer inputSize = null, outputSize = null;
		
		public Builder(){}
		
		public ExpectingOutput input(Double... input) {
			return input(new Vector1D(Arrays.asList(input)));
		}
		public ExpectingOutput input(Vector1D input) {
			if(inputSize == null) {
				inputSize = input.size();
			} else {
				if(input.size() != inputSize)
					throw new RuntimeException("Input " + input.toString() + " has a size of " + input.size() + " when the TrainingData expects a size of " + inputSize);
			}
			return new ExpectingOutput(this, input);
		}
		
		public class ExpectingOutput {
			private TrainingData.Builder parent;
			private Vector1D input;

			private ExpectingOutput(Builder builder, Vector1D input) {
				this.parent = builder;
				this.input = input;
			}
			
			public TrainingData.Builder mapsTo(Double... output) {
				return mapsTo(new Vector1D(Arrays.asList(output)));
			}
			public TrainingData.Builder mapsTo(Vector1D output) {
				if(outputSize == null) {
					outputSize = output.size();
				} else {
					if(output.size() != outputSize)
						throw new RuntimeException("Output " + output.toString() + " has a size of " + output.size() + " when the TrainingData expects a size of " + outputSize);
				}
				
				trainingSet.put(input, output);
				return parent;
			}
		}
		
		public ExpectingOutputNormalisation normaliseInputs(Double... input) {
			return normaliseInputs(new Vector1D(Arrays.asList(input)));
		}
		public ExpectingOutputNormalisation normaliseInputs(Vector1D input) {
			if(inputSize == null) {
				inputSize = input.size();
			} else {
				if(input.size() != inputSize)
					throw new RuntimeException("Input normiliser " + input.toString() + " has a size of " + input.size() + " when the TrainingData expects a size of " + inputSize);
			}
			return new ExpectingOutputNormalisation(this, input);
		}
		
		public class ExpectingOutputNormalisation {
			private TrainingData.Builder parent;
			private Vector1D inputNormaliser;

			private ExpectingOutputNormalisation(Builder builder, Vector1D input) {
				this.parent = builder;
				this.inputNormaliser = input;
			}
			
			public TrainingData normaliseOutputs(Double... outputNormaliser) {
				return normaliseOutputs(new Vector1D(Arrays.asList(outputNormaliser)));
			}
			public TrainingData normaliseOutputs(Vector1D outputNormaliser) {
				if(outputSize == null) {
					outputSize = outputNormaliser.size();
				} else {
					if(outputNormaliser.size() != outputSize)
						throw new RuntimeException("Output normiliser " + outputNormaliser.toString() + " has a size of " + outputNormaliser.size() + " when the TrainingData expects a size of " + outputSize);
				}
				
				for(Vector1D key : trainingSet.keySet()){
					Vector1D value = trainingSet.get(key);
					
					normalisedSet.put(key.elementDivide(inputNormaliser), value.elementDivide(outputNormaliser));
				}
				
				return new TrainingData(normalisedSet, inputNormaliser, outputNormaliser);
			}
		}
	}
}
